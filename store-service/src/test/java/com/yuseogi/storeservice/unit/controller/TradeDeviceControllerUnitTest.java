package com.yuseogi.storeservice.unit.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.storeservice.controller.TradeDeviceController;
import com.yuseogi.storeservice.dto.TradeDeviceInfoDto;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import com.yuseogi.storeservice.service.StoreService;
import com.yuseogi.storeservice.service.TradeDeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.Mockito.*;
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
    void 주문용_태블릿_기기_목록_조회_성공() throws Exception {
        // given
        String userId = "1";
        StoreEntity store = mock(StoreEntity.class);
        List<Long> expectedResponse = LongStream.range(1L, 11L).boxed().toList();

        // stub
        when(storeService.getStoreByOwnerUser(Long.valueOf(userId))).thenReturn(store);
        when(tradeDeviceService.getTradeDeviceList(store)).thenReturn(expectedResponse);

        // when
        ResultActions resultActions = requestGetTradeDeviceList(userId);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, new TypeReference<List<Long>>() {});
    }

    private ResultActions requestGetTradeDeviceList(String userId) throws Exception {
        return mvc.perform(get("/trade-device")
                .header("X-Authorization-userId", userId))
            .andDo(print());
    }
}
