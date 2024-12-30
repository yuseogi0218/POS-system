package com.yuseogi.tradeservice.service.implementation;

import com.yuseogi.tradeservice.dto.ProductInfoDto;
import com.yuseogi.tradeservice.dto.request.CreateOrderRequestDto;
import com.yuseogi.tradeservice.dto.request.DecreaseProductStockRequestDto;
import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.entity.OrderEntity;
import com.yuseogi.tradeservice.entity.TradeEntity;
import com.yuseogi.tradeservice.infrastructure.client.StoreServiceClient;
import com.yuseogi.tradeservice.repository.OrderDetailRepository;
import com.yuseogi.tradeservice.repository.OrderRepository;
import com.yuseogi.tradeservice.service.OrderService;
import com.yuseogi.tradeservice.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final StoreServiceClient storeServiceClient;

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    private final TradeService tradeService;

    @Transactional
    @Override
    public void createOrder(Long tradeDeviceId, CreateOrderRequestDto request) {
        TradeEntity trade = tradeService.getTradeIsNotCompleted(tradeDeviceId)
            .orElseGet(() -> tradeService.createTrade(tradeDeviceId));
        Long storeId = trade.getStoreId();

        OrderEntity order = request.toOrderEntity(trade);
        OrderEntity savedOrder = orderRepository.save(order);

        List<OrderDetailEntity> orderDetailList = new ArrayList<>();
        int orderAmount = 0;

        for (CreateOrderRequestDto.Product productRequest : request.productList()) {
            DecreaseProductStockRequestDto decreaseProductStockRequest = DecreaseProductStockRequestDto.builder()
                .storeId(storeId)
                .decreasingStock(productRequest.count())
                .build();

            ProductInfoDto product = storeServiceClient.decreaseProductStock(productRequest.id(), decreaseProductStockRequest);

            OrderDetailEntity orderDetail = productRequest.toOrderDetailEntity(savedOrder, product);

            orderDetailList.add(orderDetail);
            orderAmount += orderDetail.getTotalAmount();
        }

        orderDetailRepository.saveAll(orderDetailList);
        savedOrder.updateOrderAmount(orderAmount);
        trade.increaseTradeAmount(orderAmount);
    }
}
