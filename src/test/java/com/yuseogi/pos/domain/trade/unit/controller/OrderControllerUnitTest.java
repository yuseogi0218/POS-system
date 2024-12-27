package com.yuseogi.pos.domain.trade.unit.controller;

import com.yuseogi.pos.common.ControllerUnitTest;
import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.domain.store.service.TradeDeviceService;
import com.yuseogi.pos.domain.trade.controller.OrderController;
import com.yuseogi.pos.domain.trade.dto.request.CreateOrderRequestDto;
import com.yuseogi.pos.domain.trade.dto.request.CreateOrderRequestDtoBuilder;
import com.yuseogi.pos.domain.trade.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerUnitTest extends ControllerUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private TradeDeviceService tradeDeviceService;

    @MockitoBean
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 주문용 태블릿 기기에서 상품 주문 성공
     */
    @Test
    void 주문용_태블릿_기기에서_상품_주문_성공() throws Exception {
        // given
        Long tradeDeviceId = 1L;
        CreateOrderRequestDto request = CreateOrderRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestCreateOrder(String.valueOf(tradeDeviceId), request);

        // then
        resultActions.andExpect(status().isOk());
        verify(tradeDeviceService, times(1)).checkExistTradeDevice(tradeDeviceId);
        verify(orderService, times(1)).createOrder(tradeDeviceId, request);
    }

    /**
     * 주문용 태블릿 기기에서 상품 주문 실패
     * - 실패 사유 : 요청 시, Cookie 에 tradeDeviceId 를 추가하지 않음
     */
    @Test
    void 주문용_태블릿_기기에서_상품_주문_실패_Cookie_존재() throws Exception {
        // given
        CreateOrderRequestDto request = CreateOrderRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestCreateOrderWithOutCookie(request);

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_COOKIE, resultActions, "tradeDeviceId");
    }

    /**
     * 주문용 태블릿 기기에서 상품 주문 실패
     * - 실패 사유 : RequestBody - productList 필드 - id 필드 null
     */
    @Test
    void 주문용_태블릿_기기에서_상품_주문_실패_RequestBody_productList_필드_id_필드_null() throws Exception {
        // given
        Long tradeDeviceId = 1L;
        CreateOrderRequestDto nullProductIdRequest = CreateOrderRequestDtoBuilder.nullProductIdBuild();

        // when
        ResultActions resultActions = requestCreateOrder(String.valueOf(tradeDeviceId), nullProductIdRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 DB Id 는 필수 입력값입니다.");
    }

    /**
     * 주문용 태블릿 기기에서 상품 주문 실패
     * - 실패 사유 : RequestBody - productList 필드 - count 필드 null
     */
    @Test
    void 주문용_태블릿_기기에서_상품_주문_실패_RequestBody_productList_필드_count_필드_null() throws Exception {
        // given
        Long tradeDeviceId = 1L;
        CreateOrderRequestDto nullProductCountRequest = CreateOrderRequestDtoBuilder.nullProductCountBuild();

        // when
        ResultActions resultActions = requestCreateOrder(String.valueOf(tradeDeviceId), nullProductCountRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 주문 수량은 필수 입력값입니다.");
    }

    /**
     * 주문용 태블릿 기기에서 상품 주문 실패
     * - 실패 사유 : RequestBody - productList 필드 - count 필드 유효성
     */
    @Test
    void 주문용_태블릿_기기에서_상품_주문_실패_RequestBody_productList_필드_count_필드_유효성() throws Exception {
        // given
        Long tradeDeviceId = 1L;
        CreateOrderRequestDto invalidProductCountRequest = CreateOrderRequestDtoBuilder.invalidProductCountBuild();

        // when
        ResultActions resultActions = requestCreateOrder(String.valueOf(tradeDeviceId), invalidProductCountRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 주문 수량은 1 이상, 10,000,000 이하의 정수 입니다.");
    }

    private ResultActions requestCreateOrder(String tradeDeviceId, CreateOrderRequestDto request) throws Exception {
        return mvc.perform(post("/trade/order")
                .cookie(new MockCookie("tradeDeviceId", tradeDeviceId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }

    private ResultActions requestCreateOrderWithOutCookie(CreateOrderRequestDto request) throws Exception {
        return mvc.perform(post("/trade/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }
}
