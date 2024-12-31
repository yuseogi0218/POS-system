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
     * 주문용 태블릿 기기 DB Id 기준으로 주문용 태블릿 기기 존재 유무 확인 성공
     */
    @Test
    void 주문용_태블릿_기기_DB_Id_기준으로_주문용_태블릿_기기_존재_유무_확인_성공() throws Exception {
        // given
        String tradeDeviceId = "1";

        // when
        ResultActions resultActions = requestCheckExistTradeDevice(tradeDeviceId);

        // then
        resultActions.andExpect(status().isOk());
        verify(tradeDeviceService, times(1)).checkExistTradeDevice(Long.valueOf(tradeDeviceId));
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 주문용 태블릿 기기 존재 유무 확인 실패
     * - 실패 사유 : PathVariable - trade-device-id 타입
     */
    @Test
    void 주문용_태블릿_기기_DB_Id_기준으로_주문용_태블릿_기기_존재_유무_확인_실패() throws Exception {
        // given
        String invalidTradeDeviceId = "invalid-trade-device-id";

        // when
        ResultActions resultActions = requestCheckExistTradeDevice(invalidTradeDeviceId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "trade-device-id");
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 주문용 태블릿 기기 정보 조회 성공
     */
    @Test
    void 주문용_태블릿_기기_DB_Id_기준으로_주문용_태블릿_기기_정보_조회_성공() throws Exception {
        // given
        String tradeDeviceId = "1";
        TradeDeviceEntity tradeDevice = mock(TradeDeviceEntity.class);
        Long storeId = 1L;
        StoreEntity store = mock(StoreEntity.class);

        // stub
        when(tradeDeviceService.getTradeDevice(Long.valueOf(tradeDeviceId))).thenReturn(tradeDevice);
        when(tradeDevice.getId()).thenReturn(Long.valueOf(tradeDeviceId));
        when(tradeDevice.getStore()).thenReturn(store);
        when(store.getId()).thenReturn(storeId);

        // when
        ResultActions resultActions = requestGetTradeDevice(tradeDeviceId);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, TradeDeviceInfoDto.class);
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 주문용 태블릿 기기 정보 조회 실패
     * - 실패 사유 : PathVariable - trade-device-id 타입
     */
    @Test
    void 주문용_태블릿_기기_DB_Id_기준으로_주문용_태블릿_기기_정보_조회_실패() throws Exception {
        // given
        String invalidTradeDeviceId = "invalid-trade-device-id";

        // when
        ResultActions resultActions = requestGetTradeDevice(invalidTradeDeviceId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "trade-device-id");
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

    public ResultActions requestCheckExistTradeDevice(String tradeDeviceId) throws Exception {
        return mvc.perform(get("/store/trade-device/check-exist/{trade-device-id}", tradeDeviceId))
            .andDo(print());
    }

    public ResultActions requestGetTradeDevice(String tradeDeviceId) throws Exception {
        return mvc.perform(get("/store/trade-device/{trade-device-id}", tradeDeviceId))
            .andDo(print());
    }

    private ResultActions requestGetTradeDeviceList(String userId) throws Exception {
        return mvc.perform(get("/store/trade-device")
                .header("X-Authorization-userId", userId))
            .andDo(print());
    }
}
