package com.yuseogi.tradeservice.service.implementation;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.tradeservice.dto.ProductInfoDto;
import com.yuseogi.tradeservice.dto.request.CreateOrderRequestDto;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.request.RollbackCreateOrderRequest;
import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.entity.OrderEntity;
import com.yuseogi.tradeservice.entity.TradeEntity;
import com.yuseogi.tradeservice.exception.TradeErrorCode;
import com.yuseogi.tradeservice.infrastructure.client.StoreServiceClient;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.TradeKafkaProducer;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.request.DecreaseProductStockRequestMessage;
import com.yuseogi.tradeservice.repository.OrderDetailRepository;
import com.yuseogi.tradeservice.repository.OrderRepository;
import com.yuseogi.tradeservice.service.OrderService;
import com.yuseogi.tradeservice.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final StoreServiceClient storeServiceClient;

    private final TradeKafkaProducer tradeKafkaProducer;

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    private final TradeService tradeService;

    //TODO: 2025-12-31 Service 코드 나누기
    @Transactional
    @Override
    public void createOrder(Long tradeDeviceId, CreateOrderRequestDto request) {
        // tradeDeviceId 에서 진행중인 거래 조회 또는 신규 거래 생성
        AtomicBoolean isNewTrade = new AtomicBoolean(false);

        TradeEntity trade = tradeService.getTradeIsNotCompleted(tradeDeviceId)
            .orElseGet(() -> {
                isNewTrade.set(true);
                return tradeService.createTrade(tradeDeviceId);
            });
        Long storeId = trade.getStoreId();

        // 주문 생성
        OrderEntity order = request.toOrderEntity(trade);
        OrderEntity savedOrder = orderRepository.save(order);

        List<OrderDetailEntity> orderDetailList = new ArrayList<>();
        int orderAmount = 0;

        List<DecreaseProductStockRequestMessage.Item> decreaseProductStockRequestItemList = new ArrayList<>();

        // 주문 상세의 주문한 상품들에 대해서,
        for (CreateOrderRequestDto.Product productRequest : request.productList()) {
            // 상품 DB Id 를 기반으로 상품 정보 조회
            ProductInfoDto product = storeServiceClient.getProductInfo(productRequest.id());

            // 상품 재고 부족 시, 예외 반환
            if (product.stock() < productRequest.count()) {
                throw new CustomException(TradeErrorCode.OUT_OF_STOCK);
            }

            // 상품 재고 감소 요청 메시지 아이템 생성
            decreaseProductStockRequestItemList.add(
                DecreaseProductStockRequestMessage.Item.builder()
                    .productId(productRequest.id())
                    .decreasingStock(productRequest.count())
                    .build()
            );

            // 주문 상세 정보 생성
            OrderDetailEntity orderDetail = productRequest.toOrderDetailEntity(savedOrder, product);

            orderDetailList.add(orderDetail);

            // 주문 총 금액 계산
            orderAmount += orderDetail.getTotalAmount();
        }

        // 상품 재고 감소 요청 메시지 전송
        DecreaseProductStockRequestMessage decreaseProductStockMessage = DecreaseProductStockRequestMessage.builder()
            .storeId(storeId)
            .isNewTrade(isNewTrade.get())
            .tradeId(trade.getId())
            .orderId(savedOrder.getId())
            .itemList(decreaseProductStockRequestItemList)
            .build();
        tradeKafkaProducer.produceDecreaseProductStockMessage(decreaseProductStockMessage);

        // 생성된 주문 상세 정보 저장
        orderDetailRepository.saveAll(orderDetailList);
        // 주문 총 금액 초기화
        savedOrder.initializeOrderAmount(orderAmount);
        // 거래 총 금액에 주문 총 금액을 더함
        trade.increaseTradeAmount(orderAmount);
    }

    @Transactional
    @Override
    public void rollBackCreateOrder(RollbackCreateOrderRequest request) {
        List<OrderDetailEntity> orderDetailList = orderDetailRepository.findAllByOrderId(request.orderId());

        if (!request.isNewTrade()) {
            TradeEntity trade = tradeService.getTrade(request.tradeId());
            Optional<OrderEntity> optionalOrder = orderRepository.findById(request.orderId());

            optionalOrder.ifPresent(
                order -> trade.decreaseTradeAmount(order.getOrderAmount())
            );
        }

        orderDetailRepository.deleteAll(orderDetailList);
        orderRepository.deleteById(request.orderId());

        if (request.isNewTrade()) {
            tradeService.deleteTrade(request.tradeId());
        }
    }
}
