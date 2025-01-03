package com.yuseogi.tradeservice.service.implementation;

import com.yuseogi.tradeservice.dto.ProductInfoDto;
import com.yuseogi.tradeservice.dto.request.CreateOrderRequestDto;
import com.yuseogi.tradeservice.dto.request.RollbackCreateOrderRequest;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.request.DecreaseProductStockRequestMessage;
import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.entity.OrderEntity;
import com.yuseogi.tradeservice.entity.TradeEntity;
import com.yuseogi.tradeservice.infrastructure.client.StoreServiceClient;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.TradeKafkaProducer;
import com.yuseogi.tradeservice.repository.OrderDetailRepository;
import com.yuseogi.tradeservice.repository.OrderRepository;
import com.yuseogi.tradeservice.service.OrderService;
import com.yuseogi.tradeservice.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
        AtomicBoolean isNewTrade = new AtomicBoolean(false);

        TradeEntity trade = tradeService.getTradeIsNotCompleted(tradeDeviceId)
            .orElseGet(() -> {
                isNewTrade.set(true);
                return tradeService.createTrade(tradeDeviceId);
            });
        Long storeId = trade.getStoreId();

        OrderEntity order = request.toOrderEntity(trade);
        OrderEntity savedOrder = orderRepository.save(order);

        List<OrderDetailEntity> orderDetailList = new ArrayList<>();
        int orderAmount = 0;

        for (CreateOrderRequestDto.Product productRequest : request.productList()) {
            ProductInfoDto product = storeServiceClient.getProductInfo(productRequest.id());

            DecreaseProductStockRequestMessage decreaseProductStockMessage = DecreaseProductStockRequestMessage.builder()
                .isNewTrade(isNewTrade.get())
                .orderId(savedOrder.getId())
                .productId(productRequest.id())
                .storeId(storeId)
                .decreasingStock(productRequest.count())
                .build();

            tradeKafkaProducer.produceDecreaseProductStockMessage(decreaseProductStockMessage);

            OrderDetailEntity orderDetail = productRequest.toOrderDetailEntity(savedOrder, product);

            orderDetailList.add(orderDetail);
            orderAmount += orderDetail.getTotalAmount();
        }

        orderDetailRepository.saveAll(orderDetailList);
        savedOrder.updateOrderAmount(orderAmount);
        trade.increaseTradeAmount(orderAmount);
    }

    @Transactional
    @Override
    public void rollBackCreateOrder(RollbackCreateOrderRequest request) {
        // TODO: 2025-01-4 상품 주문 재고 roll back

        orderDetailRepository.deleteAllByOrderId(request.orderId());
        orderRepository.deleteById(request.orderId());

        if (request.isNewTrade()) {
            tradeService.deleteTrade(request.tradeId());
        }
    }
}
