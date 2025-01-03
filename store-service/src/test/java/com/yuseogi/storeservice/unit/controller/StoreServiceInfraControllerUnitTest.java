package com.yuseogi.storeservice.unit.controller;

import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.storeservice.dto.ProductInfoDto;
import com.yuseogi.storeservice.dto.TradeDeviceInfoDto;
import com.yuseogi.storeservice.dto.request.CreateStoreRequestDto;
import com.yuseogi.storeservice.entity.ProductEntity;
import com.yuseogi.storeservice.entity.ProductEntityBuilder;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import com.yuseogi.storeservice.infrastructure.server.StoreServiceInfraController;
import com.yuseogi.storeservice.service.ProductService;
import com.yuseogi.storeservice.service.StoreService;
import com.yuseogi.storeservice.service.TradeDeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreServiceInfraController.class)
public class StoreServiceInfraControllerUnitTest extends ControllerUnitTest{

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private StoreService storeService;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private TradeDeviceService tradeDeviceService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * Store(상점) 및 TradeDevice(주문용 태블릿 기기) 엔티티 데이터 생성 성공
     */
    @Test
    void 상점_및_주문용_태블릿_기기_엔티티_데이터_생성_성공() throws Exception {
        // given
        CreateStoreRequestDto request = mock(CreateStoreRequestDto.class);

        // when
        ResultActions resultActions = requestCreateStore(request);

        // then
        resultActions.andExpect(status().isOk());
        verify(storeService, times(1)).createStore(any(CreateStoreRequestDto.class));
    }

    /**
     * 상품 정보 조회 성공
     */
    @Test
    void 상품_정보_조회_성공() throws Exception {
        // given
        String productId = "1";
        ProductEntity product = ProductEntityBuilder.build();

        // stub
        when(productService.getProduct(Long.valueOf(productId))).thenReturn(product);

        // when
        ResultActions resultActions = requestGetProductInfo(productId);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, ProductInfoDto.class);
    }

    /**
     * 상품 정보 조회 실패
     * - 실패 사유 : PathVariable - product-id 타입
     */
    @Test
    void 상품_정보_조회_실패_PathVariable_product_id_타입() throws Exception {
        // given
        String invalidProductId = "invalid-product-id";

        // when
        ResultActions resultActions = requestGetProductInfo(invalidProductId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "product-id");
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
    void 주문용_태블릿_기기_DB_Id_기준으로_주문용_태블릿_기기_존재_유무_확인_실패_PathVariable_trade_device_id_타입() throws Exception {
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
    void 주문용_태블릿_기기_DB_Id_기준으로_주문용_태블릿_기기_정보_조회_실패_PathVariable_trade_device_id_타입() throws Exception {
        // given
        String invalidTradeDeviceId = "invalid-trade-device-id";

        // when
        ResultActions resultActions = requestGetTradeDevice(invalidTradeDeviceId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "trade-device-id");
    }

    private ResultActions requestCreateStore(CreateStoreRequestDto request) throws Exception {
        return mvc.perform(post("/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());

    }

    private ResultActions requestGetProductInfo(String productId) throws Exception {
        return mvc.perform(get("/store/product/{product-id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());
    }

    public ResultActions requestCheckExistTradeDevice(String tradeDeviceId) throws Exception {
        return mvc.perform(get("/store/trade-device/check-exist/{trade-device-id}", tradeDeviceId))
            .andDo(print());
    }

    public ResultActions requestGetTradeDevice(String tradeDeviceId) throws Exception {
        return mvc.perform(get("/store/trade-device/{trade-device-id}", tradeDeviceId))
            .andDo(print());
    }
}
