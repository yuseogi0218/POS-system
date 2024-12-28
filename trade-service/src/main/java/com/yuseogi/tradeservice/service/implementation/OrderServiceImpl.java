package com.yuseogi.tradeservice.service.implementation;

import com.yuseogi.storeservice.entity.ProductEntity;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.service.ProductService;
import com.yuseogi.tradeservice.dto.request.CreateOrderRequestDto;
import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.entity.OrderEntity;
import com.yuseogi.tradeservice.entity.TradeEntity;
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

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    private final ProductService productService;
    private final TradeService tradeService;

    @Transactional
    @Override
    public void createOrder(Long tradeDeviceId, CreateOrderRequestDto request) {
        TradeEntity trade = tradeService.getTradeIsNotCompleted(tradeDeviceId)
            .orElseGet(() -> tradeService.createTrade(tradeDeviceId));
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
