package com.yuseogi.pos.domain.store.unit.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yuseogi.pos.common.ControllerUnitTest;
import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.domain.store.controller.TradeDeviceController;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.service.StoreService;
import com.yuseogi.pos.domain.store.service.TradeDeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeDeviceController.class)
public class TradeDeviceControllerUnitTest extends ControllerUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private StoreService storeService;

    @MockitoBean
    private TradeDeviceService tradeDeviceService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 주문용 태블릿 기기 목록 조회 성공
     */
    @Test
    @WithMockUser(username = "username")
    void 주문용_태블릿_기기_목록_조회_성공() throws Exception {
        // given
        String accessToken = "accessToken";
        String username = "username";
        StoreEntity store = mock(StoreEntity.class);
        List<Long> expectedResponse = LongStream.range(1L, 11L).boxed().toList();

        // stub
        when(storeService.getStore(username)).thenReturn(store);
        when(tradeDeviceService.getTradeDeviceList(store)).thenReturn(expectedResponse);

        // when
        ResultActions resultActions = requestGetTradeDeviceList(accessToken);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, new TypeReference<List<Long>>() {});
    }

    /**
     * 주문용 태블릿 기기 목록 조회 실패
     * - 실패 사유 : 인증
     */
    @Test
    @WithAnonymousUser
    void 주문용_태블릿_기기_목록_조회_실패_인증() throws Exception {
        // given
        String invalidAccessToken = "invalidAccessToken";

        // when
        ResultActions resultActions = requestGetTradeDeviceList(invalidAccessToken);

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
    }

    private ResultActions requestGetTradeDeviceList(String accessToken) throws Exception {
        return mvc.perform(get("/store/trade-device")
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }
}
