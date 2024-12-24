package com.yuseogi.pos.domain.store.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yuseogi.pos.common.IntegrationTest;
import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.common.security.component.JwtBuilder;
import com.yuseogi.pos.domain.store.dto.request.CreateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.request.CreateProductRequestDtoBuilder;
import com.yuseogi.pos.domain.store.dto.request.UpdateProductRequestDto;
import com.yuseogi.pos.domain.store.dto.request.UpdateProductRequestDtoBuilder;
import com.yuseogi.pos.domain.store.dto.response.GetProductResponseDto;
import com.yuseogi.pos.domain.store.exception.StoreErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 상품 정보 등록 성공
     */
    @Test
    void 상품_정보_등록_성공() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        CreateProductRequestDto request = CreateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestCreateProduct(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보(Access Token)를 추가하지 않음
     */
    @Test
    void 상품_정보_등록_실패_Header_Authorization_존재() throws Exception {
        // given
        CreateProductRequestDto request = CreateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestCreateProductWithOutAccessToken(request);

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
    }


    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보(Access Token)에 권한 정보가 없음
     */
    @Test
    void 상품_정보_등록_실패_Unauthorized_Access_Token() throws Exception {
        // given
        String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessTokenBuild();
        CreateProductRequestDto request = CreateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestCreateProduct(unauthorizedAccessToken, request);

        // then
        assertError(CommonErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : 요청 시, Header 에 있는 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    void 상품_정보_등록_실패_Token_Type() throws Exception {
        // given
        String refreshToken = jwtBuilder.refreshTokenBuild();
        CreateProductRequestDto request = CreateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestCreateProduct(refreshToken, request);

        // then
        assertError(CommonErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    void 상품_정보_등록_실패_Expired_Access_Token() throws Exception {
        // given
        String expiredAccessToken = jwtBuilder.expiredAccessTokenBuild();
        CreateProductRequestDto request = CreateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestCreateProduct(expiredAccessToken, request);

        // then
        assertError(CommonErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 상품 정보 등록 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    void 상품_정보_등록_실패_Invalid_jwt() throws Exception {
        // given
        String invalidJwt = jwtBuilder.invalidJwtBuild();
        CreateProductRequestDto request = CreateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestCreateProduct(invalidJwt, request);

        // then
        assertError(CommonErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 상품 목록 조회 성공 - With AccessToken
     */
    @Test
    void 상품_목록_조회_성공_With_AccessToken() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();

        // when
        ResultActions resultActions = requestGetProductListWithAccessToken(accessToken);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, new TypeReference<List<GetProductResponseDto>>() {});
    }

    /**
     * 상품 목록 조회 성공 - With tradeDeviceId
     */
    @Test
    void 상품_목록_조회_성공_With_tradeDeviceId() throws Exception {
        // given
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestGetProductListWithTradeDeviceId(String.valueOf(tradeDeviceId));

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, new TypeReference<List<GetProductResponseDto>>() {});
    }

    /**
     * 상품 정보 수정 성공
     */
    @Test
    void 상품_정보_수정_성공() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        String productId = "1";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestUpdateProduct(accessToken, productId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보(Access Token)를 추가하지 않음
     */
    @Test
    void 상품_정보_수정_실패_Header_Authorization_존재() throws Exception {
        // given
        String productId = "1";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestUpdateProductWithOutAccessToken(productId, request);

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보(Access Token)에 권한 정보가 없음
     */
    @Test
    void 상품_정보_수정_실패_Unauthorized_Access_Token() throws Exception {
        // given
        String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessTokenBuild();
        String productId = "1";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestUpdateProduct(unauthorizedAccessToken, productId, request);

        // then
        assertError(CommonErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    void 상품_정보_수정_실패_Token_Type() throws Exception {
        // given
        String refreshToken = jwtBuilder.refreshTokenBuild();
        String productId = "1";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestUpdateProduct(refreshToken, productId, request);

        // then
        assertError(CommonErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    void 상품_정보_수정_실패_Expired_Access_Token() throws Exception {
        // given
        String expiredAccessToken = jwtBuilder.expiredAccessTokenBuild();
        String productId = "1";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestUpdateProduct(expiredAccessToken, productId, request);

        // then
        assertError(CommonErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    void 상품_정보_수정_실패_Invalid_jwt() throws Exception {
        // given
        String invalidJwt = jwtBuilder.invalidJwtBuild();
        String productId = "1";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestUpdateProduct(invalidJwt, productId, request);

        // then
        assertError(CommonErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : 존재하지 않는 상품
     */
    @Test
    void 상품_정보_수정_실패_존재하지_않는_상품() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        String unknownProductId = "0";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestUpdateProduct(accessToken, unknownProductId, request);

        // then
        assertError(StoreErrorCode.NOT_FOUND_PRODUCT, resultActions);
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : 상품에 대한 접근 권한이 없음
     */
    @Test
    void 상품_정보_수정_실패_상품에_대한_접근_권한이_없음() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        String notOwnedProductId = "5";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestUpdateProduct(accessToken, notOwnedProductId, request);

        // then
        assertError(StoreErrorCode.DENIED_ACCESS_TO_PRODUCT, resultActions);
    }

    /**
     * 상품 정보 수정 실패
     * - 실패 사유 : 삭제된 상품은 수정할 수 없음
     */
    @Test
    void 상품_정보_수정_실패_삭제된_상품은_수정할_수_없음() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        String deletedProductId = "3";
        UpdateProductRequestDto request = UpdateProductRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestUpdateProduct(accessToken, deletedProductId, request);

        // then
        assertError(StoreErrorCode.UNABLE_UPDATE_DELETED_PRODUCT, resultActions);
    }

    /**
     * 상품 정보 삭제 처리 성공
     */
    @Test
    void 상품_정보_삭제_처리_성공() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        String productId = "1";

        // when
        ResultActions resultActions = requestDeleteProduct(accessToken, productId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 상품 정보 삭제 처리 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보(Access Token)를 추가하지 않음
     */
    @Test
    void 상품_정보_삭제_처리_실패_Header_Authorization_존재() throws Exception {
        // given
        String productId = "1";

        // when
        ResultActions resultActions = requestDeleteProductWithOutAccessToken(productId);

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
    }

    /**
     * 상품 정보 삭제 처리 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보(Access Token)에 권한 정보가 없음
     */
    @Test
    void 상품_정보_삭제_처리_실패_Unauthorized_Access_Token() throws Exception {
        // given
        String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessTokenBuild();
        String productId = "1";

        // when
        ResultActions resultActions = requestDeleteProduct(unauthorizedAccessToken, productId);

        // then
        assertError(CommonErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 상품 정보 삭제 처리 실패
     * - 실패 사유 : 요청 시, Header 에 있는 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    void 상품_정보_삭제_처리_실패_Token_Type() throws Exception {
        // given
        String refreshToken = jwtBuilder.refreshTokenBuild();
        String productId = "1";

        // when
        ResultActions resultActions = requestDeleteProduct(refreshToken, productId);

        // then
        assertError(CommonErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 상품 정보 삭제 처리 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    void 상품_정보_삭제_처리_실패_Expired_Access_Token() throws Exception {
        // given
        String expiredAccessToken = jwtBuilder.expiredAccessTokenBuild();
        String productId = "1";

        // when
        ResultActions resultActions = requestDeleteProduct(expiredAccessToken, productId);

        // then
        assertError(CommonErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 상품 정보 삭제 처리 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    void 상품_정보_삭제_처리_실패_Invalid_jwt() throws Exception {
        // given
        String invalidJwt = jwtBuilder.invalidJwtBuild();
        String productId = "1";

        // when
        ResultActions resultActions = requestDeleteProduct(invalidJwt, productId);

        // then
        assertError(CommonErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 상품 정보 삭제 처리 실패
     * - 실패 사유 : 존재하지 않는 상품
     */
    @Test
    void 상품_정보_삭제_처리_실패_존재하지_않는_상품() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        String unknownProductId = "0";

        // when
        ResultActions resultActions = requestDeleteProduct(accessToken, unknownProductId);

        // then
        assertError(StoreErrorCode.NOT_FOUND_PRODUCT, resultActions);
    }

    /**
     * 상품 정보 삭제 처리 실패
     * - 실패 사유 : 상품에 대한 접근 권한이 없음
     */
    @Test
    void 상품_정보_삭제_처리_실패_상품에_대한_접근_권한이_없음() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        String notOwnedProductId = "5";

        // when
        ResultActions resultActions = requestDeleteProduct(accessToken, notOwnedProductId);

        // then
        assertError(StoreErrorCode.DENIED_ACCESS_TO_PRODUCT, resultActions);
    }

    /**
     * 상품 정보 삭제 처리 실패
     * - 실패 사유 : 이미 삭제된 상품은 삭제할 수 없음
     */
    @Test
    void 상품_정보_삭제_처리_실패_이미_삭제된_상품은_삭제할_수_없음() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        String deletedProductId = "3";

        // when
        ResultActions resultActions = requestDeleteProduct(accessToken, deletedProductId);

        // then
        assertError(StoreErrorCode.UNABLE_DELETE_DELETED_PRODUCT, resultActions);
    }

    /**
     * 상품 현재 재고 초기화 성공
     */
    @Test
    void 상품_현재_재고_초기화_성공() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();

        // when
        ResultActions resultActions = requestReStock(accessToken);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 상품 현재 재고 초기화 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보(Access Token)를 추가하지 않음
     */
    @Test
    void 상품_현재_재고_초기화_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        ResultActions resultActions = requestReStockWithOutAccessToken();

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
    }

    /**
     * 상품 현재 재고 초기화 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보(Access Token)에 권한 정보가 없음
     */
    @Test
    void 상품_현재_재고_초기화_실패_Unauthorized_Access_Token() throws Exception {
        // given
        String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessTokenBuild();

        // when
        ResultActions resultActions = requestReStock(unauthorizedAccessToken);

        // then
        assertError(CommonErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 상품 현재 재고 초기화 실패
     * - 실패 사유 : 요청 시, Header 에 있는 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    void 상품_현재_재고_초기화_실패_Token_Type() throws Exception {
        // given
        String refreshToken = jwtBuilder.refreshTokenBuild();

        // when
        ResultActions resultActions = requestReStock(refreshToken);

        // then
        assertError(CommonErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 상품 현재 재고 초기화 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    void 상품_현재_재고_초기화_실패_Expired_Access_Token() throws Exception {
        // given
        String expiredAccessToken = jwtBuilder.expiredAccessTokenBuild();

        // when
        ResultActions resultActions = requestReStock(expiredAccessToken);

        // then
        assertError(CommonErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 상품 현재 재고 초기화 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    void 상품_현재_재고_초기화_실패_Invalid_jwt() throws Exception {
        // given
        String invalidJwt = jwtBuilder.invalidJwtBuild();

        // when
        ResultActions resultActions = requestReStock(invalidJwt);

        // then
        assertError(CommonErrorCode.INVALID_JWT, resultActions);
    }

    private ResultActions requestCreateProduct(String accessToken, CreateProductRequestDto request) throws Exception {
        return mvc.perform(post("/store/product")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }

    private ResultActions requestCreateProductWithOutAccessToken(CreateProductRequestDto request) throws Exception {
        return mvc.perform(post("/store/product")
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

    private ResultActions requestUpdateProduct(String accessToken, String productId, UpdateProductRequestDto request) throws Exception {
        return mvc.perform(patch("/store/product/{product-id}", productId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }

    private ResultActions requestUpdateProductWithOutAccessToken(String productId, UpdateProductRequestDto request) throws Exception {
        return mvc.perform(patch("/store/product/{product-id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }

    private ResultActions requestDeleteProduct(String accessToken, String productId) throws Exception {
        return mvc.perform(delete("/store/product/{product-id}", productId)
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }

    private ResultActions requestDeleteProductWithOutAccessToken(String productId) throws Exception {
        return mvc.perform(delete("/store/product/{product-id}", productId))
            .andDo(print());
    }

    private ResultActions requestReStock(String accessToken) throws Exception {
        return mvc.perform(patch("/store/product/re-stock")
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }

    private ResultActions requestReStockWithOutAccessToken() throws Exception {
        return mvc.perform(patch("/store/product/re-stock"))
            .andDo(print());
    }
}
