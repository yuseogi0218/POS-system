package com.yuseogi.storeservice.unit.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.storeservice.controller.ProductController;
import com.yuseogi.storeservice.dto.ProductInfoDto;
import com.yuseogi.storeservice.dto.request.*;
import com.yuseogi.storeservice.dto.response.GetProductResponseDto;
import com.yuseogi.storeservice.entity.ProductEntity;
import com.yuseogi.storeservice.entity.ProductEntityBuilder;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import com.yuseogi.storeservice.service.ProductService;
import com.yuseogi.storeservice.service.StoreService;
import com.yuseogi.storeservice.service.TradeDeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
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
    void 상품_정보_등록_성공() throws Exception {
        // given
        String userId = "1";
        CreateProductRequestDto request = CreateProductRequestDtoBuilder.build();
        StoreEntity store = mock(StoreEntity.class);

        // stub
        when(storeService.getStoreByOwnerUser(Long.valueOf(userId))).thenReturn(store);

        // when
        ResultActions resultActions = requestCreateProduct(userId, request);

        // then
        resultActions.andExpect(status().isOk());
        verify(productService, times(1)).createProduct(store, request);
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - name 필드 null
     */
    @Test
    void 상품_정보_등록_실패_RequestBody_name_필드_null() throws Exception {
        // given
        String userId = "1";
        CreateProductRequestDto nullNameRequest = CreateProductRequestDtoBuilder.nullNameBuild();

        // when
        ResultActions resultActions = requestCreateProduct(userId, nullNameRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 이름은 필수 입력값입니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - name 필드 empty
     */
    @Test
    void 상품_정보_등록_실패_RequestBody_name_필드_empty() throws Exception {
        // given
        String userId = "1";
        CreateProductRequestDto emptyNameRequest = CreateProductRequestDtoBuilder.emptyNameBuild();

        // when
        ResultActions resultActions = requestCreateProduct(userId, emptyNameRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 이름은 필수 입력값입니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - category 필드 null
     */
    @Test
    void 상품_정보_등록_실패_RequestBody_category_필드_null() throws Exception {
        // given
        String userId = "1";
        CreateProductRequestDto nullCategoryRequest = CreateProductRequestDtoBuilder.nullCategoryBuild();

        // when
        ResultActions resultActions = requestCreateProduct(userId, nullCategoryRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - category 필드 empty
     */
    @Test
    void 상품_정보_등록_실패_RequestBody_category_필드_empty() throws Exception {
        // given
        String userId = "1";
        CreateProductRequestDto emptyCategoryRequest = CreateProductRequestDtoBuilder.emptyCategoryBuild();

        // when
        ResultActions resultActions = requestCreateProduct(userId, emptyCategoryRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - category 필드 유효성
     */
    @Test
    void 상품_정보_등록_실패_RequestBody_category_필드_유효성() throws Exception {
        // given
        String userId = "1";
        CreateProductRequestDto invalidCategoryRequest = CreateProductRequestDtoBuilder.invalidCategoryBuild();

        // when
        ResultActions resultActions = requestCreateProduct(userId, invalidCategoryRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - price 필드 null
     */
    @Test
    void 상품_정보_등록_실패_RequestBody_price_필드_null() throws Exception {
        // given
        String userId = "1";
        CreateProductRequestDto nullPriceRequest = CreateProductRequestDtoBuilder.nullPriceBuild();

        // when
        ResultActions resultActions = requestCreateProduct(userId, nullPriceRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 판매 단가는 필수 입력값입니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - price 필드 유효성
     */
    @Test
    void 상품_정보_등록_실패_RequestBody_price_필드_유효성() throws Exception {
        // given
        String userId = "1";
        CreateProductRequestDto invalidPriceRequest = CreateProductRequestDtoBuilder.invalidPriceBuild();

        // when
        ResultActions resultActions = requestCreateProduct(userId, invalidPriceRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 판매 단가는 0 이상 10,000,000 이하의 정수 입니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - baseStock 필드 null
     */
    @Test
    void 상품_정보_등록_실패_RequestBody_baseStock_필드_null() throws Exception {
        // given
        String userId = "1";
        CreateProductRequestDto nullBaseStockRequest = CreateProductRequestDtoBuilder.nullBaseStockBuild();

        // when
        ResultActions resultActions = requestCreateProduct(userId, nullBaseStockRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 기초 재고는 필수 입력값입니다.");
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : RequestBody - baseStock 필드 유효성
     */
    @Test
    void 상품_정보_등록_실패_RequestBody_baseStock_필드_유효성() throws Exception {
        // given
        String userId = "1";
        CreateProductRequestDto invalidBaseStockRequest = CreateProductRequestDtoBuilder.invalidBaseStockBuild();

        // when
        ResultActions resultActions = requestCreateProduct(userId, invalidBaseStockRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 기초 재고는 0 이상 10,000,000 이하의 정수 입니다.");
    }

    /**
     * 상품 목록 조회 성공 - With tradeDeviceId
     */
    @Test
    void 상품_목록_조회_성공_With_tradeDeviceId() throws Exception {
        // given
        String tradeDeviceId = "1";
        TradeDeviceEntity tradeDevice = mock(TradeDeviceEntity.class);
        StoreEntity store = mock(StoreEntity.class);

        GetProductResponseDto expectedGetProductResponse = mock(GetProductResponseDto.class);
        List<GetProductResponseDto> expectedResponse = List.of(expectedGetProductResponse);

        // stub
        when(tradeDeviceService.getTradeDevice(Long.valueOf(tradeDeviceId))).thenReturn(tradeDevice);
        when(tradeDevice.getStore()).thenReturn(store);
        when(productService.getProductList(store)).thenReturn(expectedResponse);

        // when
        ResultActions resultActions = requestGetProductListWithTradeDeviceId(tradeDeviceId);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, new TypeReference<List<GetProductResponseDto>>() {});
        verify(storeService, never()).getStoreByOwnerUser(anyLong());
    }

    /**
     * 상품 목록 조회 성공 - With AccessToken
     */
    @Test
    void 상품_목록_조회_성공_With_AccessToken() throws Exception {
        // given
        String userId = "1";
        StoreEntity store = mock(StoreEntity.class);

        GetProductResponseDto expectedGetProductResponse = mock(GetProductResponseDto.class);
        List<GetProductResponseDto> expectedResponse = List.of(expectedGetProductResponse);

        // stub
        when(storeService.getStoreByOwnerUser(Long.valueOf(userId))).thenReturn(store);
        when(productService.getProductList(store)).thenReturn(expectedResponse);

        // when
        ResultActions resultActions = requestGetProductListWithAuthorization(userId);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, new TypeReference<List<GetProductResponseDto>>() {});
        verify(tradeDeviceService, never()).getTradeDevice(any());
    }

    /**
     * 상품 목록 조회 실패
     * - 실패 사유 : 인증
     */
    @Test
    void 상품_목록_조회_실패_인증() throws Exception {
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
    void 상품_정보_수정_성공() throws Exception {
        // given
        String userId = "1";
        String productId = "1";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();
        StoreEntity store = mock(StoreEntity.class);

        // stub
        when(storeService.getStoreByOwnerUser(Long.valueOf(userId))).thenReturn(store);

        // when
        ResultActions resultActions = requestUpdateProduct(userId, productId, request);

        // then
        resultActions.andExpect(status().isOk());
        verify(productService, times(1)).updateProduct(store, Long.valueOf(productId), request);
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : PathVariable - product-id 타입
     */
    @Test
    void 상품_정보_수정_실패_PathVariable_product_id_타입() throws Exception {
        // given
        String userId = "1";
        String invalidProductId = "invalid-product-id";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestUpdateProduct(userId, invalidProductId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "product-id");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - name 필드 null
     */
    @Test
    void 상품_정보_수정_실패_RequestBody_name_필드_null() throws Exception {
        // given
        String userId = "1";
        String productId = "1";
        UpdateProductRequestDto nullNameRequest = UpdateProductRequestDtoBuilder.nullNameBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(userId, productId, nullNameRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 이름은 필수 입력값입니다.");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - name 필드 empty
     */
    @Test
    void 상품_정보_수정_실패_RequestBody_name_필드_empty() throws Exception {
        // given
        String userId = "1";
        String productId = "1";
        UpdateProductRequestDto emptyNameRequest = UpdateProductRequestDtoBuilder.emptyNameBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(userId, productId, emptyNameRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 이름은 필수 입력값입니다.");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - category 필드 null
     */
    @Test
    void 상품_정보_수정_실패_RequestBody_category_필드_null() throws Exception {
        // given
        String userId = "1";
        String productId = "1";
        UpdateProductRequestDto nullCategoryRequest = UpdateProductRequestDtoBuilder.nullCategoryBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(userId, productId, nullCategoryRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - category 필드 empty
     */
    @Test
    void 상품_정보_수정_실패_RequestBody_category_필드_empty() throws Exception {
        // given
        String userId = "1";
        String productId = "1";
        UpdateProductRequestDto emptyCategoryRequest = UpdateProductRequestDtoBuilder.emptyCategoryBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(userId, productId, emptyCategoryRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - category 필드 유효성
     */
    @Test
    void 상품_정보_수정_실패_RequestBody_category_필드_유효성() throws Exception {
        // given
        String userId = "1";
        String productId = "1";
        UpdateProductRequestDto invalidCategoryRequest = UpdateProductRequestDtoBuilder.invalidCategoryBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(userId, productId, invalidCategoryRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - price 필드 null
     */
    @Test
    void 상품_정보_수정_실패_RequestBody_price_필드_null() throws Exception {
        // given
        String userId = "1";
        String productId = "1";
        UpdateProductRequestDto nullPriceRequest = UpdateProductRequestDtoBuilder.nullPriceBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(userId, productId, nullPriceRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 판매 단가는 필수 입력값입니다.");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - price 필드 유효성
     */
    @Test
    void 상품_정보_수정_실패_RequestBody_price_필드_유효성() throws Exception {
        // given
        String userId = "1";
        String productId = "1";
        UpdateProductRequestDto invalidPriceRequest = UpdateProductRequestDtoBuilder.invalidPriceBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(userId, productId, invalidPriceRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 판매 단가는 0 이상 10,000,000 이하의 정수 입니다.");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - baseStock 필드 null
     */
    @Test
    void 상품_정보_수정_실패_RequestBody_baseStock_필드_null() throws Exception {
        // given
        String userId = "1";
        String productId = "1";
        UpdateProductRequestDto nullBaseStockRequest = UpdateProductRequestDtoBuilder.nullBaseStockBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(userId, productId, nullBaseStockRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 기초 재고는 필수 입력값입니다.");
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : RequestBody - baseStock 필드 유효성
     */
    @Test
    void 상품_정보_수정_실패_RequestBody_baseStock_필드_유효성() throws Exception {
        // given
        String userId = "1";
        String productId = "1";
        UpdateProductRequestDto invalidBaseStockRequest = UpdateProductRequestDtoBuilder.invalidBaseStockBuild();

        // when
        ResultActions resultActions = requestUpdateProduct(userId, productId, invalidBaseStockRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상품 기초 재고는 0 이상 10,000,000 이하의 정수 입니다.");
    }

    /**
     * 상품 정보 삭제 처리 성공
     */
    @Test
    void 상품_정보_삭제_처리_성공() throws Exception {
        // given
        String userId = "1";
        String productId = "1";
        StoreEntity store = mock(StoreEntity.class);

        // stub
        when(storeService.getStoreByOwnerUser(Long.valueOf(userId))).thenReturn(store);

        // when
        ResultActions resultActions = requestDeleteProduct(userId, productId);

        // then
        resultActions.andExpect(status().isOk());
        verify(productService, times(1)).softDeleteProduct(store, Long.valueOf(productId));
    }

    /**
     * 상품 정보 삭제 처리 실패
     * - 실패 사유 : PathVariable - product-id 타입
     */
    @Test
    void 상품_정보_삭제_처리_실패_PathVariable_product_id_타입() throws Exception {
        // given
        String userId = "1";
        String invalidProductId = "invalid-product-id";

        // when
        ResultActions resultActions = requestDeleteProduct(userId, invalidProductId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "product-id");
    }

    /**
     * 상품 현재 재고 초기화 성공
     */
    @Test
    void 상품_현재_재고_초기화_성공() throws Exception {
        // given
        String userId = "1";
        StoreEntity store = mock(StoreEntity.class);

        // stub
        when(storeService.getStoreByOwnerUser(Long.valueOf(userId))).thenReturn(store);

        // when
        ResultActions resultActions = requestReStock(userId);

        // then
        resultActions.andExpect(status().isOk());
        verify(productService, times(1)).reStock(store);
    }

    private ResultActions requestCreateProduct(String userId, CreateProductRequestDto request) throws Exception {
        return mvc.perform(post("/store/product")
                .header("X-Authorization-userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }

    private ResultActions requestGetProductListWithTradeDeviceId(String tradeDeviceId) throws Exception {
        return mvc.perform(get("/store/product")
                .cookie(new MockCookie("tradeDeviceId", tradeDeviceId)))
            .andDo(print());
    }

    private ResultActions requestGetProductListWithAuthorization(String userId) throws Exception {
        return mvc.perform(get("/store/product")
                .header("X-Authorization-userId", userId))
            .andDo(print());
    }

    private ResultActions requestGetProductListWithOutAuthentication() throws Exception {
        return mvc.perform(get("/store/product"))
            .andDo(print());
    }

    private ResultActions requestUpdateProduct(String userId, String productId, UpdateProductRequestDto request) throws Exception {
        return mvc.perform(patch("/store/product/{product-id}", productId)
                .header("X-Authorization-userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }

    private ResultActions requestDeleteProduct(String userId, String productId) throws Exception {
        return mvc.perform(delete("/store/product/{product-id}", productId)
                .header("X-Authorization-userId", userId))
            .andDo(print());
    }

    private ResultActions requestReStock(String userId) throws Exception {
        return mvc.perform(patch("/store/product/re-stock")
                .header("X-Authorization-userId", userId))
            .andDo(print());
    }

}
