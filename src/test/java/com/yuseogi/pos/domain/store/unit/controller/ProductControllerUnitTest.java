package com.yuseogi.pos.domain.store.unit.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yuseogi.pos.common.ControllerUnitTest;
import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.domain.store.controller.ProductController;
import com.yuseogi.pos.domain.store.dto.request.CreateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.request.CreateProductRequestDtoBuilder;
import com.yuseogi.pos.domain.store.dto.request.UpdateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.request.UpdateProductRequestDtoBuilder;
import com.yuseogi.pos.domain.store.dto.response.GetProductResponseDto;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.store.service.ProductService;
import com.yuseogi.pos.domain.store.service.StoreService;
import com.yuseogi.pos.domain.store.service.TradeDeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerUnitTest extends ControllerUnitTest {

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
     * 상품 정보 등록 성공
     */
    @Test
    @WithMockUser(username = "username")
    void 상품_정보_등록_성공() throws Exception {
        // given
        String accessToken = "accessToken";
        CreateProductRequestDto request = CreateProductRequestDtoBuilder.build();
        String username = "username";
        StoreEntity store = mock(StoreEntity.class);

        // stub
        when(storeService.getStore(username)).thenReturn(store);

        // when
        ResultActions resultActions = requestCreateProduct(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
        verify(productService, times(1)).createProduct(store, request);
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - name 필드 null
     */
    @Test
    @WithMockUser
    void 상품_정보_등록_실패_RequestBody_name_필드_null() throws Exception {
        // given
        String accessToken = "accessToken";
        CreateProductRequestDto nullNameRequest = CreateProductRequestDtoBuilder.nullNameBuild();

        // when
        ResultActions resultActions = requestCreateProduct(accessToken, nullNameRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 이름은 필수 입력값입니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - name 필드 empty
     */
    @Test
    @WithMockUser
    void 상품_정보_등록_실패_RequestBody_name_필드_empty() throws Exception {
        // given
        String accessToken = "accessToken";
        CreateProductRequestDto emptyNameRequest = CreateProductRequestDtoBuilder.emptyNameBuild();

        // when
        ResultActions resultActions = requestCreateProduct(accessToken, emptyNameRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 이름은 필수 입력값입니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - category 필드 null
     */
    @Test
    @WithMockUser
    void 상품_정보_등록_실패_RequestBody_category_필드_null() throws Exception {
        // given
        String accessToken = "accessToken";
        CreateProductRequestDto nullCategoryRequest = CreateProductRequestDtoBuilder.nullCategoryBuild();

        // when
        ResultActions resultActions = requestCreateProduct(accessToken, nullCategoryRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - category 필드 empty
     */
    @Test
    @WithMockUser
    void 상품_정보_등록_실패_RequestBody_category_필드_empty() throws Exception {
        // given
        String accessToken = "accessToken";
        CreateProductRequestDto emptyCategoryRequest = CreateProductRequestDtoBuilder.emptyCategoryBuild();

        // when
        ResultActions resultActions = requestCreateProduct(accessToken, emptyCategoryRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - category 필드 유효성
     */
    @Test
    @WithMockUser
    void 상품_정보_등록_실패_RequestBody_category_필드_유효성() throws Exception {
        // given
        String accessToken = "accessToken";
        CreateProductRequestDto invalidCategoryRequest = CreateProductRequestDtoBuilder.invalidCategoryBuild();

        // when
        ResultActions resultActions = requestCreateProduct(accessToken, invalidCategoryRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - price 필드 null
     */
    @Test
    @WithMockUser
    void 상품_정보_등록_실패_RequestBody_price_필드_null() throws Exception {
        // given
        String accessToken = "accessToken";
        CreateProductRequestDto nullPriceRequest = CreateProductRequestDtoBuilder.nullPriceBuild();

        // when
        ResultActions resultActions = requestCreateProduct(accessToken, nullPriceRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 판매 단가는 필수 입력값입니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - price 필드 유효성
     */
    @Test
    @WithMockUser
    void 상품_정보_등록_실패_RequestBody_price_필드_유효성() throws Exception {
        // given
        String accessToken = "accessToken";
        CreateProductRequestDto invalidPriceRequest = CreateProductRequestDtoBuilder.invalidPriceBuild();

        // when
        ResultActions resultActions = requestCreateProduct(accessToken, invalidPriceRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 판매 단가는 0 이상 10,000,000 이하의 정수 입니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - baseStock 필드 null
     */
    @Test
    @WithMockUser
    void 상품_정보_등록_실패_RequestBody_baseStock_필드_null() throws Exception {
        // given
        String accessToken = "accessToken";
        CreateProductRequestDto nullBaseStockRequest = CreateProductRequestDtoBuilder.nullBaseStockBuild();

        // when
        ResultActions resultActions = requestCreateProduct(accessToken, nullBaseStockRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 기초 재고는 필수 입력값입니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - baseStock 필드 유효성
     */
    @Test
    @WithMockUser
    void 상품_정보_등록_실패_RequestBody_baseStock_필드_유효성() throws Exception {
        // given
        String accessToken = "accessToken";
        CreateProductRequestDto invalidBaseStockRequest = CreateProductRequestDtoBuilder.invalidBaseStockBuild();

        // when
        ResultActions resultActions = requestCreateProduct(accessToken, invalidBaseStockRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 기초 재고는 0 이상 10,000,000 이하의 정수 입니다.");
    }

    /**
     * 상품 목록 조회 성공 - With AccessToken
     */
    @Test
    @WithMockUser(username = "username")
    void 상품_목록_조회_성공_With_AccessToken() throws Exception {
        // given
        String accessToken = "accessToken";
        String username = "username";
        StoreEntity store = mock(StoreEntity.class);

        // stub
        when(storeService.getStore(username)).thenReturn(store);

        // when
        ResultActions resultActions = requestGetProductListWithAccessToken(accessToken);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, new TypeReference<List<GetProductResponseDto>>() {});
        verify(tradeDeviceService, never()).getTradeDevice(any());
    }

    /**
     * 상품 목록 조회 성공 - With tradeDeviceId
     */
    @Test
    void 상품_목록_조회_성공_With_tradeDeviceId() throws Exception {
        // given
        Long tradeDeviceId = 1L;
        TradeDeviceEntity tradeDevice = mock(TradeDeviceEntity.class);
        StoreEntity store = mock(StoreEntity.class);

        // stub
        when(tradeDeviceService.getTradeDevice(tradeDeviceId)).thenReturn(tradeDevice);
        when(tradeDevice.getStore()).thenReturn(store);

        // when
        ResultActions resultActions = requestGetProductListWithTradeDeviceId(String.valueOf(tradeDeviceId));

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, new TypeReference<List<GetProductResponseDto>>() {});
        verify(storeService, never()).getStore(anyString());
    }

    /**
     * 상품 목록 조회 실패
     * - 실패 사유 : 충분하지 않은 인증정보
     */
    @Test
    void 상품_목록_조회_실패_충분하지_않은_인증정보() throws Exception {
        // given

        // when
        ResultActions resultActions = requestGetProductListWithOutAuthentication();

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
    }

    /**
     * 상품 정보 수정 성공
     */
    @Test
    @WithMockUser(username = "username")
    void 상품_정보_수정_성공() throws Exception {
        // given
        String accessToken = "accessToken";
        String productId = "1";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();
        String username = "username";
        StoreEntity store = mock(StoreEntity.class);

        // stub
        when(storeService.getStore(username)).thenReturn(store);

        // when
        ResultActions resultActions = requestUpdateProduct(accessToken, productId, request);

        // then
        resultActions.andExpect(status().isOk());
        verify(productService, times(1)).updateProduct(store, Long.valueOf(productId), request);
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : PathVariable - product-id 타입
     */
    @Test
    @WithMockUser
    void 상품_정보_수정_실패_PathVariable_product_id_타입() throws Exception {
        // given
        String accessToken = "accessToken";
        String invalidProductId = "invalid-product-id";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestUpdateProduct(accessToken, invalidProductId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "product-id");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - price 필드 null
     */
    @Test
    @WithMockUser
    void 상품_정보_수정_실패_RequestBody_price_필드_null() throws Exception {
        // given
        String accessToken = "accessToken";
        String productId = "1";
        UpdateProductRequestDto nullPriceRequest = UpdateProductRequestDtoBuilder.nullPriceBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(accessToken, productId, nullPriceRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 판매 단가는 필수 입력값입니다.");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - price 필드 유효성
     */
    @Test
    @WithMockUser
    void 상품_정보_수정_실패_RequestBody_price_필드_유효성() throws Exception {
        // given
        String accessToken = "accessToken";
        String productId = "1";
        UpdateProductRequestDto invalidPriceRequest = UpdateProductRequestDtoBuilder.invalidPriceBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(accessToken, productId, invalidPriceRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 판매 단가는 0 이상 10,000,000 이하의 정수 입니다.");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - baseStock 필드 null
     */
    @Test
    @WithMockUser
    void 상품_정보_수정_실패_RequestBody_baseStock_필드_null() throws Exception {
        // given
        String accessToken = "accessToken";
        String productId = "1";
        UpdateProductRequestDto nullBaseStockRequest = UpdateProductRequestDtoBuilder.nullBaseStockBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(accessToken, productId, nullBaseStockRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 기초 재고는 필수 입력값입니다.");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - baseStock 필드 유효성
     */
    @Test
    @WithMockUser
    void 상품_정보_수정_실패_RequestBody_baseStock_필드_유효성() throws Exception {
        // given
        String accessToken = "accessToken";
        String productId = "1";
        UpdateProductRequestDto invalidBaseStockRequest = UpdateProductRequestDtoBuilder.invalidBaseStockBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(accessToken, productId, invalidBaseStockRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 기초 재고는 0 이상 10,000,000 이하의 정수 입니다.");
    }

    /**
     * 상품 정보 삭제 처리 성공
     */
    @Test
    @WithMockUser(username = "username")
    void 상품_정보_삭제_처리_성공() throws Exception {
        // given
        String accessToken = "accessToken";
        String productId = "1";
        String username = "username";
        StoreEntity store = mock(StoreEntity.class);

        // stub
        when(storeService.getStore(username)).thenReturn(store);

        // when
        ResultActions resultActions = requestDeleteProduct(accessToken, productId);

        // then
        resultActions.andExpect(status().isOk());
        verify(productService, times(1)).softDeleteProduct(store, Long.valueOf(productId));
    }

    /**
     * 상품 정보 삭제 처리 실패
     * - 실패 사유 : PathVariable - product-id 타입
     */
    @Test
    @WithMockUser
    void 상품_정보_삭제_처리_실패_PathVariable_product_id_타입() throws Exception {
        // given
        String accessToken = "accessToken";
        String invalidProductId = "invalid-product-id";

        // when
        ResultActions resultActions = requestDeleteProduct(accessToken, invalidProductId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "product-id");
    }

    /**
     * 상품 현재 재고 초기화 성공
     */
    @Test
    @WithMockUser(username = "username")
    void 상품_현재_재고_초기화_성공() throws Exception {
        // given
        String accessToken = "accessToken";
        String username = "username";
        StoreEntity store = mock(StoreEntity.class);

        // stub
        when(storeService.getStore(username)).thenReturn(store);

        // when
        ResultActions resultActions = requestReStock(accessToken);

        // then
        resultActions.andExpect(status().isOk());
        verify(productService, times(1)).reStock(store);
    }

    private ResultActions requestCreateProduct(String accessToken, CreateProductRequestDto request) throws Exception {
        return mvc.perform(post("/store/product")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }

    private ResultActions requestGetProductListWithAccessToken(String accessToken) throws Exception {
        return mvc.perform(get("/store/product")
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }

    private ResultActions requestGetProductListWithTradeDeviceId(String tradeDeviceId) throws Exception {
        return mvc.perform(get("/store/product")
            .cookie(new MockCookie("tradeDeviceId", tradeDeviceId)))
            .andDo(print());
    }

    private ResultActions requestGetProductListWithOutAuthentication() throws Exception {
        return mvc.perform(get("/store/product"))
            .andDo(print());
    }

    private ResultActions requestUpdateProduct(String accessToken, String productId, UpdateProductRequestDto request) throws Exception {
        return mvc.perform(patch("/store/product/{product-id}", productId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }

    private ResultActions requestDeleteProduct(String accessToken, String productId) throws Exception {
        return mvc.perform(delete("/store/product/{product-id}", productId)
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }

    private ResultActions requestReStock(String accessToken) throws Exception {
        return mvc.perform(patch("/store/product/re-stock")
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }
}
