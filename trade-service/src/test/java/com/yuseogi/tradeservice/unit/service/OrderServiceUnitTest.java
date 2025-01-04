package com.yuseogi.tradeservice.unit.service;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.tradeservice.dto.ProductInfoDto;
import com.yuseogi.tradeservice.dto.request.CreateOrderRequestDto;
import com.yuseogi.tradeservice.exception.TradeErrorCode;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.TradeKafkaProducer;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.request.DecreaseProductStockRequestMessage;
import com.yuseogi.tradeservice.entity.OrderDetailEntity;
import com.yuseogi.tradeservice.entity.OrderEntity;
import com.yuseogi.tradeservice.entity.TradeEntity;
import com.yuseogi.tradeservice.infrastructure.client.StoreServiceClient;
import com.yuseogi.tradeservice.infrastructure.messagequeue.kafka.dto.request.RollbackCreateOrderRequest;
import com.yuseogi.tradeservice.repository.OrderDetailRepository;
import com.yuseogi.tradeservice.repository.OrderRepository;
import com.yuseogi.tradeservice.service.TradeService;
import com.yuseogi.tradeservice.service.implementation.OrderServiceImpl;
import org.assertj.core.api.Assertions;
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
    private TradeKafkaProducer tradeKafkaProducer;

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
        Long tradeId = 1L;
        TradeEntity trade = mock(TradeEntity.class);
        Long storeId = 1L;
        Long orderId = 1L;
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
        Integer productStock1 = 20;
        Integer productPrice1 = 1000;
        ProductInfoDto productInfoDto2 = mock(ProductInfoDto.class);
        Integer productStock2 = 40;
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
        when(storeServiceClient.getProductInfo(productId1)).thenReturn(productInfoDto1);
        when(productInfoDto1.stock()).thenReturn(productStock1);
        when(productRequest1.count()).thenReturn(productRequestCount1);
        when(productRequest1.toOrderDetailEntity(savedOrder, productInfoDto1)).thenReturn(orderDetail1);
        when(orderDetail1.getTotalAmount()).thenReturn(orderDetailTotalAmount1);

        when(productRequest2.id()).thenReturn(productId2);
        when(storeServiceClient.getProductInfo(productId2)).thenReturn(productInfoDto2);
        when(productInfoDto2.stock()).thenReturn(productStock2);
        when(productRequest2.count()).thenReturn(productRequestCount2);
        when(productRequest2.toOrderDetailEntity(savedOrder, productInfoDto2)).thenReturn(orderDetail2);
        when(orderDetail2.getTotalAmount()).thenReturn(orderDetailTotalAmount2);

        when(trade.getId()).thenReturn(tradeId);
        when(savedOrder.getId()).thenReturn(orderId);

        // when
        orderService.createOrder(tradeDeviceId, request);

        // then
        verify(tradeService, never()).createTrade(tradeDeviceId);

        verify(tradeKafkaProducer, times(1)).produceDecreaseProductStockMessage(any(DecreaseProductStockRequestMessage.class));

        verify(orderDetailRepository, times(1)).saveAll(orderDetailList);
        verify(savedOrder, times(1)).initializeOrderAmount(orderAmount);
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
        Long savedTradeId = 1L;
        TradeEntity savedTrade = mock(TradeEntity.class);
        Long storeId = 1L;
        Long orderId = 1L;
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
        Integer productStock1 = 20;
        Integer productPrice1 = 1000;
        ProductInfoDto productInfoDto2 = mock(ProductInfoDto.class);
        Integer productStock2 = 40;
        Integer productPrice2 = 500;

        OrderDetailEntity orderDetail1 = mock(OrderDetailEntity.class);
        Integer orderDetailTotalAmount1 = productPrice1 * productRequestCount1;
        OrderDetailEntity orderDetail2 = mock(OrderDetailEntity.class);
        Integer orderDetailTotalAmount2 = productPrice2 * productRequestCount2;
        List<OrderDetailEntity> orderDetailList = List.of(orderDetail1, orderDetail2);

        Integer orderAmount = orderDetailTotalAmount1 + orderDetailTotalAmount2;

        // stub
        when(tradeService.getTradeIsNotCompleted(tradeDeviceId)).thenReturn(Optional.empty());
        when(tradeService.createTrade(tradeDeviceId)).thenReturn(savedTrade);
        when(savedTrade.getStoreId()).thenReturn(storeId);
        when(request.toOrderEntity(savedTrade)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(request.productList()).thenReturn(productRequestList);

        when(productRequest1.id()).thenReturn(productId1);
        when(storeServiceClient.getProductInfo(productId1)).thenReturn(productInfoDto1);
        when(productInfoDto1.stock()).thenReturn(productStock1);
        when(productRequest1.count()).thenReturn(productRequestCount1);
        when(productRequest1.toOrderDetailEntity(savedOrder, productInfoDto1)).thenReturn(orderDetail1);
        when(orderDetail1.getTotalAmount()).thenReturn(orderDetailTotalAmount1);

        when(productRequest2.id()).thenReturn(productId2);
        when(storeServiceClient.getProductInfo(productId2)).thenReturn(productInfoDto2);
        when(productInfoDto2.stock()).thenReturn(productStock2);
        when(productRequest2.count()).thenReturn(productRequestCount2);
        when(productRequest2.toOrderDetailEntity(savedOrder, productInfoDto2)).thenReturn(orderDetail2);
        when(orderDetail2.getTotalAmount()).thenReturn(orderDetailTotalAmount2);

        when(savedTrade.getId()).thenReturn(savedTradeId);
        when(savedOrder.getId()).thenReturn(orderId);

        // when
        orderService.createOrder(tradeDeviceId, request);

        // then
        verify(tradeKafkaProducer, times(1)).produceDecreaseProductStockMessage(any(DecreaseProductStockRequestMessage.class));

        verify(orderDetailRepository, times(1)).saveAll(orderDetailList);
        verify(savedOrder, times(1)).initializeOrderAmount(orderAmount);
        verify(savedTrade, times(1)).increaseTradeAmount(orderAmount);
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 주문 생성 실패
     * - 실패 사유 : 상품 재고 부족
     */
    @Test
    void createOrder_실패_OUT_OF_STOCK() {
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
        List<CreateOrderRequestDto.Product> productRequestList = List.of(productRequest1, productRequest2);

        ProductInfoDto productInfoDto1 = mock(ProductInfoDto.class);
        Integer productStock1 = 5;

        // stub
        when(tradeService.getTradeIsNotCompleted(tradeDeviceId)).thenReturn(Optional.of(trade));
        when(trade.getStoreId()).thenReturn(storeId);
        when(request.toOrderEntity(trade)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(request.productList()).thenReturn(productRequestList);

        when(productRequest1.id()).thenReturn(productId1);
        when(storeServiceClient.getProductInfo(productId1)).thenReturn(productInfoDto1);
        when(productInfoDto1.stock()).thenReturn(productStock1);
        when(productRequest1.count()).thenReturn(productRequestCount1);

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.createOrder(tradeDeviceId, request))
            .isInstanceOf(CustomException.class)
            .hasMessage(TradeErrorCode.OUT_OF_STOCK.getMessage());

        verify(tradeService, never()).createTrade(tradeDeviceId);

        verify(tradeKafkaProducer, never()).produceDecreaseProductStockMessage(any(DecreaseProductStockRequestMessage.class));

        verify(orderDetailRepository, never()).saveAll(anyList());
        verify(savedOrder, never()).initializeOrderAmount(anyInt());
        verify(trade, never()).increaseTradeAmount(anyInt());
    }

    /**
     * 주문 생성 실패 시, 트랜잭션 롤백 - 기존 거래
     */
    @Test
    void rollBackCreateOrder_기존_거래() {
        // given
        RollbackCreateOrderRequest request = mock(RollbackCreateOrderRequest.class);
        OrderDetailEntity orderDetail = mock(OrderDetailEntity.class);
        List<OrderDetailEntity> orderDetailList = List.of(orderDetail);
        Long tradeId = 1L;
        TradeEntity trade = mock(TradeEntity.class);
        Long orderId = 1L;
        Integer orderAmount = 10000;
        OrderEntity order = mock(OrderEntity.class);

        // stub
        when(request.orderId()).thenReturn(orderId);
        when(orderDetailRepository.findAllByOrderId(orderId)).thenReturn(orderDetailList);
        when(request.isNewTrade()).thenReturn(Boolean.FALSE);
        when(request.tradeId()).thenReturn(tradeId);
        when(tradeService.getTrade(tradeId)).thenReturn(trade);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(order.getOrderAmount()).thenReturn(orderAmount);

        // when
        orderService.rollBackCreateOrder(request);

        // then
        verify(orderDetailRepository, times(1)).deleteAll(orderDetailList);
        verify(trade, times(1)).decreaseTradeAmount(orderAmount);
        verify(orderRepository, times(1)).deleteById(orderId);
        verify(tradeService, never()).deleteTrade(tradeId);
    }

    /**
     * 주문 생성 실패 시, 트랜잭션 롤백 - 신규 거래
     */
    @Test
    void rollBackCreateOrder_신규_거래() {
        // given
        RollbackCreateOrderRequest request = mock(RollbackCreateOrderRequest.class);
        OrderDetailEntity orderDetail = mock(OrderDetailEntity.class);
        List<OrderDetailEntity> orderDetailList = List.of(orderDetail);
        Long tradeId = 1L;
        TradeEntity trade = mock(TradeEntity.class);
        Long orderId = 1L;
        Integer orderAmount = 10000;

        // stub
        when(request.orderId()).thenReturn(orderId);
        when(orderDetailRepository.findAllByOrderId(orderId)).thenReturn(orderDetailList);
        when(request.isNewTrade()).thenReturn(Boolean.TRUE);

        // when
        orderService.rollBackCreateOrder(request);

        // then
        verify(trade, never()).decreaseTradeAmount(orderAmount);
        verify(orderDetailRepository, times(1)).deleteAll(orderDetailList);
        verify(orderRepository, times(1)).deleteById(orderId);
        verify(tradeService, never()).deleteTrade(tradeId);
    }
}
