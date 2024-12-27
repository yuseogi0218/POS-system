package com.yuseogi.pos.domain.trade.integration;

import com.yuseogi.pos.common.IntegrationTest;
import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.common.security.component.JwtBuilder;
import com.yuseogi.pos.domain.trade.dto.request.PayWithCardRequestDto;
import com.yuseogi.pos.domain.trade.dto.request.PayWithCardRequestDtoBuilder;
import com.yuseogi.pos.domain.trade.dto.response.GetTradeIsNotCompletedResponseDto;
import com.yuseogi.pos.domain.trade.exception.TradeErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TradeIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 상점 주인에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 성공
     */
    @Test
    void 상점_주인에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_성공() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByStoreOwner(accessToken, String.valueOf(tradeDeviceId));

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, GetTradeIsNotCompletedResponseDto.class);
    }

    /**
     * 상점 주인에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보(Access Token)를 추가하지 않음
     */
    @Test
    void 상점_주인에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_실패_Header_Authorization_존재() throws Exception {
        // given
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByStoreOwnerWithOutAccessToken(String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
    }

    /**
     * 상점 주인에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보(Access Token)에 권한 정보가 없음
     */
    @Test
    void 상점_주인에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessTokenBuild();
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByStoreOwner(unauthorizedAccessToken, String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 상점 주인에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    void 상점_주인에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_실패_Token_Type() throws Exception {
        // given
        String refreshToken = jwtBuilder.refreshTokenBuild();
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByStoreOwner(refreshToken, String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 상점 주인에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    void 상점_주인에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_실패_Expired_Access_Token() throws Exception {
        // given
        String expiredAccessToken = jwtBuilder.expiredAccessTokenBuild();
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByStoreOwner(expiredAccessToken, String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 상점 주인에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    void 상점_주인에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_실패_Invalid_jwt() throws Exception {
        // given
        String invalidJwt = jwtBuilder.invalidJwtBuild();
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByStoreOwner(invalidJwt, String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 주문용 태블릿 기기에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 성공
     */
    @Test
    void 주문용_태블릿_기기에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_성공() throws Exception {
        // given
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByTradeDevice(String.valueOf(tradeDeviceId));

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, GetTradeIsNotCompletedResponseDto.class);
    }

    /**
     * 거래 주문 내역 일괄 현금 결제 성공
     */
    @Test
    void 거래_주문_내역_일괄_현금_결제_성공() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestPayWithCash(accessToken, String.valueOf(tradeDeviceId));

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 주문 내역 일괄 현금 결제 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보(Access Token)를 추가하지 않음
     */
    @Test
    void 거래_주문_내역_일괄_현금_결제_실패_Header_Authorization_존재() throws Exception {
        // given
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestPayWithCashWithOutAccessToken(String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
    }

    /**
     * 거래 주문 내역 일괄 현금 결제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보(Access Token)에 권한 정보가 없음
     */
    @Test
    void 거래_주문_내역_일괄_현금_결제_실패_Unauthorized_Access_Token() throws Exception {
        // given
        String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessTokenBuild();
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestPayWithCash(unauthorizedAccessToken, String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 주문 내역 일괄 현금 결제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    void 거래_주문_내역_일괄_현금_결제_실패_Token_Type() throws Exception {
        // given
        String refreshToken = jwtBuilder.refreshTokenBuild();
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestPayWithCash(refreshToken, String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 주문 내역 일괄 현금 결제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    void 거래_주문_내역_일괄_현금_결제_실패_Expired_Access_Token() throws Exception {
        // given
        String expiredAccessToken = jwtBuilder.expiredAccessTokenBuild();
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestPayWithCash(expiredAccessToken, String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 주문 내역 일괄 현금 결제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    void 거래_주문_내역_일괄_현금_결제_실패_Invalid_jwt() throws Exception {
        // given
        String invalidJwt = jwtBuilder.invalidJwtBuild();
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestPayWithCash(invalidJwt, String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 주문 내역 일괄 현금 결제 실패
     * - 실패 사유 : 완료되지 않은 거래 존재 X
     */
    @Test
    void 거래_주문_내역_일괄_현금_결제_실패_완료되지_않은_거래_존재_X() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        Long tradeDeviceId = 3L;

        // when
        ResultActions resultActions = requestPayWithCash(accessToken, String.valueOf(tradeDeviceId));

        // then
        assertError(TradeErrorCode.NOT_FOUND_TRADE_IS_NOT_COMPLETED, resultActions);
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 성공
     */
    @Test
    void 거래_주문_내역_일괄_카드_결제_성공() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto request = PayWithCardRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestPayWithCard(accessToken, String.valueOf(tradeDeviceId), request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보(Access Token)를 추가하지 않음
     */
    @Test
    void 거래_주문_내역_일괄_카드_결제_실패_Header_Authorization_존재() throws Exception {
        // given
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto request = PayWithCardRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestPayWithCardWithOutAccessToken(String.valueOf(tradeDeviceId), request);

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보(Access Token)에 권한 정보가 없음
     */
    @Test
    void 거래_주문_내역_일괄_카드_결제_실패_Unauthorized_Access_Token() throws Exception {
        // given
        String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessTokenBuild();
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto request = PayWithCardRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestPayWithCard(unauthorizedAccessToken, String.valueOf(tradeDeviceId), request);

        // then
        assertError(CommonErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    void 거래_주문_내역_일괄_카드_결제_실패_Token_Type() throws Exception {
        // given
        String refreshToken = jwtBuilder.refreshTokenBuild();
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto request = PayWithCardRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestPayWithCard(refreshToken, String.valueOf(tradeDeviceId), request);

        // then
        assertError(CommonErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    void 거래_주문_내역_일괄_카드_결제_실패_Expired_Access_Token() throws Exception {
        // given
        String expiredAccessToken = jwtBuilder.expiredAccessTokenBuild();
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto request = PayWithCardRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestPayWithCard(expiredAccessToken, String.valueOf(tradeDeviceId), request);

        // then
        assertError(CommonErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    void 거래_주문_내역_일괄_카드_결제_실패_Invalid_jwt() throws Exception {
        // given
        String invalidJwt = jwtBuilder.invalidJwtBuild();
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto request = PayWithCardRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestPayWithCard(invalidJwt, String.valueOf(tradeDeviceId), request);

        // then
        assertError(CommonErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 실패
     * - 실패 사유 : 완료되지 않은 거래 존재 X
     */
    @Test
    void 거래_주문_내역_일괄_카드_결제_실패_완료되지_않은_거래_존재_X() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();
        Long tradeDeviceId = 3L;
        PayWithCardRequestDto request = PayWithCardRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestPayWithCard(accessToken, String.valueOf(tradeDeviceId), request);

        // then
        assertError(TradeErrorCode.NOT_FOUND_TRADE_IS_NOT_COMPLETED, resultActions);
    }

    private ResultActions requestGetTradeIsNotCompletedByStoreOwner(String accessToken, String tradeDeviceId) throws Exception{
        return mvc.perform(get("/trade/{trade-device-id}", tradeDeviceId)
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }

    private ResultActions requestGetTradeIsNotCompletedByStoreOwnerWithOutAccessToken(String tradeDeviceId) throws Exception{
        return mvc.perform(get("/trade/{trade-device-id}", tradeDeviceId))
            .andDo(print());
    }

    private ResultActions requestGetTradeIsNotCompletedByTradeDevice(String tradeDeviceId) throws Exception {
        return mvc.perform(get("/trade")
                .cookie(new MockCookie("tradeDeviceId", tradeDeviceId)))
            .andDo(print());
    }

    private ResultActions requestPayWithCash(String accessToken, String tradeDeviceId) throws Exception {
        return mvc.perform(post("/trade/pay/cash/{trade-device-id}", tradeDeviceId)
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }

    private ResultActions requestPayWithCashWithOutAccessToken(String tradeDeviceId) throws Exception {
        return mvc.perform(post("/trade/pay/cash/{trade-device-id}", tradeDeviceId))
            .andDo(print());
    }

    private ResultActions requestPayWithCard(String accessToken, String tradeDeviceId, PayWithCardRequestDto request) throws Exception {
        return mvc.perform(post("/trade/pay/card/{trade-device-id}", tradeDeviceId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }

    private ResultActions requestPayWithCardWithOutAccessToken(String tradeDeviceId, PayWithCardRequestDto request) throws Exception {
        return mvc.perform(post("/trade/pay/card/{trade-device-id}", tradeDeviceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }
}
