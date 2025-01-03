package com.yuseogi.tradeservice.unit.service;

import com.yuseogi.tradeservice.dto.ProductInfoDto;
import com.yuseogi.tradeservice.dto.request.CreateOrderRequestDto;
import com.yuseogi.tradeservice.dto.request.DecreaseProductStockRequestDto;
import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.entity.OrderEntity;
import com.yuseogi.tradeservice.entity.TradeEntity;
import com.yuseogi.tradeservice.infrastructure.client.StoreServiceClient;
import com.yuseogi.tradeservice.repository.OrderDetailRepository;
import com.yuseogi.tradeservice.repository.OrderRepository;
import com.yuseogi.tradeservice.service.TradeService;
import com.yuseogi.tradeservice.service.implementation.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class OrderServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private StoreServiceClient storeServiceClient;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TradeService tradeService;

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 주문 생성 성공 - 완료되지 않은 거래 존재 O
     */
    @Test
    void createOrder_성공_완료되지_않은_거래_존재_O() {
        // given
        Long tradeDeviceId = 1L;
        CreateOrderRequestDto request = mock(CreateOrderRequestDto.class);
        TradeEntity trade = mock(TradeEntity.class);
        Long storeId = 1L;
        OrderEntity order = mock(OrderEntity.class);
        OrderEntity savedOrder = mock(OrderEntity.class);

        CreateOrderRequestDto.Product productRequest1 = mock(CreateOrderRequestDto.Product.class);
        Long productId1 = 1L;
        Integer productRequestCount1 = 10;
        CreateOrderRequestDto.Product productRequest2 = mock(CreateOrderRequestDto.Product.class);
        Long productId2 = 2L;
        Integer productRequestCount2 = 20;
        List<CreateOrderRequestDto.Product> productRequestList = List.of(productRequest1, productRequest2);

        ProductInfoDto productInfoDto1 = mock(ProductInfoDto.class);
        Integer productPrice1 = 1000;
        ProductInfoDto productInfoDto2 = mock(ProductInfoDto.class);
        Integer productPrice2 = 500;

        OrderDetailEntity orderDetail1 = mock(OrderDetailEntity.class);
        Integer orderDetailTotalAmount1 = productPrice1 * productRequestCount1;
        OrderDetailEntity orderDetail2 = mock(OrderDetailEntity.class);
        Integer orderDetailTotalAmount2 = productPrice2 * productRequestCount2;
        List<OrderDetailEntity> orderDetailList = List.of(orderDetail1, orderDetail2);

        Integer orderAmount = orderDetailTotalAmount1 + orderDetailTotalAmount2;

        // stub
        when(tradeService.getTradeIsNotCompleted(tradeDeviceId)).thenReturn(Optional.of(trade));
        when(trade.getStoreId()).thenReturn(storeId);
        when(request.toOrderEntity(trade)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(request.productList()).thenReturn(productRequestList);

        when(productRequest1.id()).thenReturn(productId1);
        when(productRequest1.count()).thenReturn(productRequestCount1);
        when(storeServiceClient.decreaseProductStock(eq(productId1), any(DecreaseProductStockRequestDto.class))).thenReturn(productInfoDto1);
        when(productRequest1.toOrderDetailEntity(savedOrder, productInfoDto1)).thenReturn(orderDetail1);
        when(orderDetail1.getTotalAmount()).thenReturn(orderDetailTotalAmount1);

        when(productRequest2.id()).thenReturn(productId2);
        when(productRequest2.count()).thenReturn(productRequestCount2);
        when(storeServiceClient.decreaseProductStock(eq(productId2), any(DecreaseProductStockRequestDto.class))).thenReturn(productInfoDto2);
        when(productRequest2.toOrderDetailEntity(savedOrder, productInfoDto2)).thenReturn(orderDetail2);
        when(orderDetail2.getTotalAmount()).thenReturn(orderDetailTotalAmount2);

        // when
        orderService.createOrder(tradeDeviceId, request);

        // then
        verify(tradeService, never()).createTrade(tradeDeviceId);

        verify(orderDetailRepository, times(1)).saveAll(orderDetailList);
        verify(savedOrder, times(1)).updateOrderAmount(orderAmount);
        verify(trade, times(1)).increaseTradeAmount(orderAmount);
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 주문 생성 성공 - 완료되지 않은 거래 존재 X
     */
    @Test
    void createOrder_성공_완료되지_않은_거래_존재_X() {
        // given
        Long tradeDeviceId = 1L;
        CreateOrderRequestDto request = mock(CreateOrderRequestDto.class);
        TradeEntity trade = mock(TradeEntity.class);
        Long storeId = 1L;
        OrderEntity order = mock(OrderEntity.class);
        OrderEntity savedOrder = mock(OrderEntity.class);

        CreateOrderRequestDto.Product productRequest1 = mock(CreateOrderRequestDto.Product.class);
        Long productId1 = 1L;
        Integer productRequestCount1 = 10;
        CreateOrderRequestDto.Product productRequest2 = mock(CreateOrderRequestDto.Product.class);
        Long productId2 = 2L;
        Integer productRequestCount2 = 20;
        List<CreateOrderRequestDto.Product> productRequestList = List.of(productRequest1, productRequest2);

        ProductInfoDto productInfoDto1 = mock(ProductInfoDto.class);
        Integer productPrice1 = 1000;
        ProductInfoDto productInfoDto2 = mock(ProductInfoDto.class);
        Integer productPrice2 = 500;

        OrderDetailEntity orderDetail1 = mock(OrderDetailEntity.class);
        Integer orderDetailTotalAmount1 = productPrice1 * productRequestCount1;
        OrderDetailEntity orderDetail2 = mock(OrderDetailEntity.class);
        Integer orderDetailTotalAmount2 = productPrice2 * productRequestCount2;
        List<OrderDetailEntity> orderDetailList = List.of(orderDetail1, orderDetail2);

        Integer orderAmount = orderDetailTotalAmount1 + orderDetailTotalAmount2;

        // stub
        when(tradeService.getTradeIsNotCompleted(tradeDeviceId)).thenReturn(Optional.empty());
        when(tradeService.createTrade(tradeDeviceId)).thenReturn(trade);
        when(trade.getStoreId()).thenReturn(storeId);
        when(request.toOrderEntity(trade)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(request.productList()).thenReturn(productRequestList);

        when(productRequest1.id()).thenReturn(productId1);
        when(productRequest1.count()).thenReturn(productRequestCount1);
        when(storeServiceClient.decreaseProductStock(eq(productId1), any(DecreaseProductStockRequestDto.class))).thenReturn(productInfoDto1);
        when(productRequest1.toOrderDetailEntity(savedOrder, productInfoDto1)).thenReturn(orderDetail1);
        when(orderDetail1.getTotalAmount()).thenReturn(orderDetailTotalAmount1);

        when(productRequest2.id()).thenReturn(productId2);
        when(productRequest2.count()).thenReturn(productRequestCount2);
        when(storeServiceClient.decreaseProductStock(eq(productId2), any(DecreaseProductStockRequestDto.class))).thenReturn(productInfoDto2);
        when(productRequest2.toOrderDetailEntity(savedOrder, productInfoDto2)).thenReturn(orderDetail2);
        when(orderDetail2.getTotalAmount()).thenReturn(orderDetailTotalAmount2);

        // when
        orderService.createOrder(tradeDeviceId, request);

        verify(orderDetailRepository, times(1)).saveAll(orderDetailList);
        verify(savedOrder, times(1)).updateOrderAmount(orderAmount);
        verify(trade, times(1)).increaseTradeAmount(orderAmount);
    }
}