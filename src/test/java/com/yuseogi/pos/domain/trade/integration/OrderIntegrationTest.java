package com.yuseogi.pos.domain.trade.integration;

import com.yuseogi.pos.gateway.IntegrationTest;
import com.yuseogi.pos.domain.store.exception.StoreErrorCode;
import com.yuseogi.pos.domain.trade.dto.request.CreateOrderRequestDto;
import com.yuseogi.pos.domain.trade.dto.request.CreateOrderRequestDtoBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderIntegrationTest extends IntegrationTest {

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
    }

    /**
     * 주문용 태블릿 기기에서 상품 주문 실패
     * - 실패 사유 : 상품 재고 부족
     */
    @Test
    void 주문용_태블릿_기기에서_상품_주문_실패_상품_재고_부족() throws Exception {
        // given
        Long tradeDeviceId = 1L;
        CreateOrderRequestDto overProductCountRequest = CreateOrderRequestDtoBuilder.overProductCountbuild();

        // when
        ResultActions resultActions = requestCreateOrder(String.valueOf(tradeDeviceId), overProductCountRequest);

        // then
        assertError(StoreErrorCode.OUT_OF_STOCK, resultActions);
    }

    private ResultActions requestCreateOrder(String tradeDeviceId, CreateOrderRequestDto request) throws Exception {
        return mvc.perform(post("/trade/order")
                .cookie(new MockCookie("tradeDeviceId", tradeDeviceId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }
}
