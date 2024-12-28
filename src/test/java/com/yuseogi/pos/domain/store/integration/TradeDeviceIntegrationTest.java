package com.yuseogi.pos.domain.store.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yuseogi.pos.gateway.IntegrationTest;
import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.pos.gateway.security.component.JwtBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TradeDeviceIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 주문용 태블릿 기기 목록 조회 성공
     */
    @Test
    void 주문용_태블릿_기기_목록_조회_성공() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();

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
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보(Access Token)를 추가하지 않음
     */
    @Test
    void 주문용_태블릿_기기_목록_조회_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        ResultActions resultActions = requestGetTradeDeviceListWithOutAccessToken();

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
    }

    /**
     * 주문용 태블릿 기기 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보(Access Token)에 권한 정보가 없음
     */
    @Test
    void 주문용_태블릿_기기_목록_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessTokenBuild();

        // when
        ResultActions resultActions = requestGetTradeDeviceList(unauthorizedAccessToken);

        // then
        assertError(CommonErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 주문용 태블릿 기기 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    void 주문용_태블릿_기기_목록_조회_실패_Token_Type() throws Exception {
        // given
        String refreshToken = jwtBuilder.refreshTokenBuild();

        // when
        ResultActions resultActions = requestGetTradeDeviceList(refreshToken);

        // then
        assertError(CommonErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 주문용 태블릿 기기 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    void 주문용_태블릿_기기_목록_조회_실패_Expired_Access_Token() throws Exception {
        // given
        String expiredAccessToken = jwtBuilder.expiredAccessTokenBuild();

        // when
        ResultActions resultActions = requestGetTradeDeviceList(expiredAccessToken);

        // then
        assertError(CommonErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 주문용 태블릿 기기 목록 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    void 주문용_태블릿_기기_목록_조회_실패_Invalid_jwt() throws Exception {
        // given
        String invalidJwt = jwtBuilder.invalidJwtBuild();

        // when
        ResultActions resultActions = requestGetTradeDeviceList(invalidJwt);

        // then
        assertError(CommonErrorCode.INVALID_JWT, resultActions);
    }

    private ResultActions requestGetTradeDeviceList(String accessToken) throws Exception {
        return mvc.perform(get("/store/trade-device")
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }

    private ResultActions requestGetTradeDeviceListWithOutAccessToken() throws Exception {
        return mvc.perform(get("/store/trade-device"))
            .andDo(print());
    }
}
