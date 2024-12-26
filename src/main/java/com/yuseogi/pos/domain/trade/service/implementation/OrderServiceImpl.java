package com.yuseogi.pos.domain.trade.service.implementation;

import com.yuseogi.pos.domain.store.entity.ProductEntity;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.service.ProductService;
import com.yuseogi.pos.domain.trade.dto.request.CreateOrderRequestDto;
import com.yuseogi.pos.domain.trade.entity.OrderDetailEntity;
import com.yuseogi.pos.domain.trade.entity.OrderEntity;
import com.yuseogi.pos.domain.trade.entity.TradeEntity;
import com.yuseogi.pos.domain.trade.repository.OrderDetailRepository;
import com.yuseogi.pos.domain.trade.repository.OrderRepository;
import com.yuseogi.pos.domain.trade.service.OrderService;
import com.yuseogi.pos.domain.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    private final ProductService productService;
    private final TradeService tradeService;

    @Transactional
    @Override
    public void createOrder(Long tradeDeviceId, CreateOrderRequestDto request) {
        TradeEntity trade = tradeService.getTradeIsNotCompleted(tradeDeviceId)
            .orElse(tradeService.createTrade(tradeDeviceId));
        StoreEntity store = trade.getStore();

        OrderEntity order = request.toOrderEntity(trade);
        OrderEntity savedOrder = orderRepository.save(order);

        List<OrderDetailEntity> orderDetailList = new ArrayList<>();
        int orderAmount = 0;

        for (CreateOrderRequestDto.Product productRequest : request.productList()) {
            ProductEntity product = productService.getProduct(productRequest.id());
            product.checkAuthority(store);
            product.decreaseStock(productRequest.count());

            OrderDetailEntity orderDetail = productRequest.toOrderDetailEntity(savedOrder, product);

            orderDetailList.add(orderDetail);
            orderAmount += orderDetail.getTotalAmount();
        }

        orderDetailRepository.saveAll(orderDetailList);
        savedOrder.updateOrderAmount(orderAmount);
        trade.increaseTradeAmount(orderAmount);
    }
}
