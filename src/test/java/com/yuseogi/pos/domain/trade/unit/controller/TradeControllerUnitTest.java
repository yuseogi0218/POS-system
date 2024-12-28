package com.yuseogi.pos.domain.trade.unit.controller;

import com.yuseogi.pos.gateway.ControllerUnitTest;
import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.pos.domain.store.service.TradeDeviceService;
import com.yuseogi.pos.domain.trade.controller.TradeController;
import com.yuseogi.pos.domain.trade.dto.request.PayWithCardRequestDto;
import com.yuseogi.pos.domain.trade.dto.request.PayWithCardRequestDtoBuilder;
import com.yuseogi.pos.domain.trade.dto.response.GetTradeIsNotCompletedResponseDto;
import com.yuseogi.pos.domain.trade.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeController.class)
public class TradeControllerUnitTest extends ControllerUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private TradeDeviceService tradeDeviceService;

    @MockitoBean
    private TradeService tradeService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 상점 주인에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 성공
     */
    @Test
    @WithMockUser
    void 상점_주인에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_성공() throws Exception  {
        // given
        String accessToken = "accessToken";
        Long tradeDeviceId = 1L;
        GetTradeIsNotCompletedResponseDto expectedResponse = mock(GetTradeIsNotCompletedResponseDto.class);

        // stub
        when(tradeService.getTradeInfoIsNotCompleted(tradeDeviceId)).thenReturn(expectedResponse);

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByStoreOwner(accessToken, String.valueOf(tradeDeviceId));

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, GetTradeIsNotCompletedResponseDto.class);

        verify(tradeDeviceService, times(1)).checkExistTradeDevice(tradeDeviceId);
    }

    /**
     * 상점 주인에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 실패
     * - 실패 사유 : 인증
     */
    @Test
    @WithAnonymousUser
    void 상점_주인에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_실패_인증() throws Exception  {
        // given
        String invalidAccessToken = "invalidAccessToken";
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByStoreOwner(invalidAccessToken, String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
        verify(tradeDeviceService, never()).checkExistTradeDevice(tradeDeviceId);
    }

    /**
     * 상점 주인에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 실패
     * - 실패 사유 : PathVariable - trade-device-id 타입
     */
    @Test
    @WithMockUser
    void 상점_주인에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_실패_PathVariable_trade_device_id_타입() throws Exception  {
        // given
        String accessToken = "accessToken";
        String invalidTradeDeviceId = "invalid-trade-device-id";

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByStoreOwner(accessToken, invalidTradeDeviceId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "trade-device-id");
    }

    /**
     * 주문용 태블릿 기기에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 성공
     */
    @Test
    void 주문용_태블릿_기기에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_성공() throws Exception {
        // given
        Long tradeDeviceId = 1L;
        GetTradeIsNotCompletedResponseDto expectedResponse = mock(GetTradeIsNotCompletedResponseDto.class);

        // stub
        when(tradeService.getTradeInfoIsNotCompleted(tradeDeviceId)).thenReturn(expectedResponse);

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByTradeDevice(String.valueOf(tradeDeviceId));

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, GetTradeIsNotCompletedResponseDto.class);

        verify(tradeDeviceService, times(1)).checkExistTradeDevice(tradeDeviceId);
    }

    /**
     * 주문용 태블릿 기기에 의한 주문용 태블릿 기기에서 진행 중인 거래의 주문 내역 조회 실패
     * - 실패 사유 : 요청 시, Cookie 에 tradeDeviceId 를 추가하지 않음
     */
    @Test
    void 주문용_태블릿_기기에_의한_주문용_태블릿_기기에서_진행_중인_거래의_주문_내역_조회_실패_Cookie_존재() throws Exception {
        // given

        // when
        ResultActions resultActions = requestGetTradeIsNotCompletedByTradeDeviceWithOutCookie();

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_COOKIE, resultActions, "tradeDeviceId");
        verify(tradeDeviceService, never()).checkExistTradeDevice(anyLong());
    }

    /**
     * 거래 주문 내역 일괄 현금 결제 성공
     */
    @Test
    @WithMockUser
    void 거래_주문_내역_일괄_현금_결제_성공() throws Exception {
        // given
        String accessToken = "accessToken";
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestPayWithCash(accessToken, String.valueOf(tradeDeviceId));

        // then
        resultActions.andExpect(status().isOk());
        verify(tradeDeviceService, times(1)).checkExistTradeDevice(tradeDeviceId);
        verify(tradeService, times(1)).payWithCash(tradeDeviceId);
    }

    /**
     * 거래 주문 내역 일괄 현금 결제 실패
     * - 실패 사유 : 인증
     */
    @Test
    @WithAnonymousUser
    void 거래_주문_내역_일괄_현금_결제_실패_인증() throws Exception {
        // given
        String invalidAccessToken = "invalidAccessToken";
        Long tradeDeviceId = 1L;

        // when
        ResultActions resultActions = requestPayWithCash(invalidAccessToken, String.valueOf(tradeDeviceId));

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
        verify(tradeDeviceService, never()).checkExistTradeDevice(tradeDeviceId);
        verify(tradeService, never()).payWithCash(tradeDeviceId);
    }

    /**
     * 거래 주문 내역 일괄 현금 결제 실패
     * - 실패 사유 : PathVariable - trade-device-id 타입
     */
    @Test
    @WithMockUser
    void 거래_주문_내역_일괄_현금_결제_실패_PathVariable_trade_device_id_타입() throws Exception {
        // given
        String accessToken = "accessToken";
        String invalidTradeDeviceId = "invalid-trade-device-id";

        // when
        ResultActions resultActions = requestPayWithCash(accessToken, invalidTradeDeviceId);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "trade-device-id");
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 성공
     */
    @Test
    @WithMockUser
    void 거래_주문_내역_일괄_카드_결제_성공() throws Exception {
        // given
        String accessToken = "accessToken";
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto request = PayWithCardRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestPayWithCard(accessToken, String.valueOf(tradeDeviceId), request);

        // then
        resultActions.andExpect(status().isOk());
        verify(tradeDeviceService, times(1)).checkExistTradeDevice(tradeDeviceId);
        verify(tradeService, times(1)).payWithCard(tradeDeviceId, request);
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 실패
     * - 실패 사유 : 인증
     */
    @Test
    @WithAnonymousUser
    void 거래_주문_내역_일괄_카드_결제_실패_인증() throws Exception {
        // given
        String invalidAccessToken = "invalidAccessToken";
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto request = PayWithCardRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestPayWithCard(invalidAccessToken, String.valueOf(tradeDeviceId), request);

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
        verify(tradeDeviceService, never()).checkExistTradeDevice(tradeDeviceId);
        verify(tradeService, never()).payWithCard(tradeDeviceId, request);
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 실패
     * - 실패 사유 : PathVariable - trade-device-id 타입
     */
    @Test
    @WithMockUser
    void 거래_주문_내역_일괄_카드_결제_실패_PathVariable_trade_device_id_타입() throws Exception {
        // given
        String accessToken = "accessToken";
        String invalidTradeDeviceId = "invalid-trade-device-id";
        PayWithCardRequestDto request = PayWithCardRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestPayWithCard(accessToken, invalidTradeDeviceId, request);

        // then
        assertErrorWithMessage(CommonErrorCode.MISMATCH_PARAMETER_TYPE, resultActions, "trade-device-id");
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 실패
     * - 실패 사유 : RequestBody - cardCompany 필드 null
     */
    @Test
    @WithMockUser
    void 거래_주문_내역_일괄_카드_결제_실패_RequestBody_cardCompany_필드_null() throws Exception {
        // given
        String accessToken = "accessToken";
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto nullCardCompanyRequest = PayWithCardRequestDtoBuilder.nullCardCompanyBuild();

        // when
        ResultActions resultActions = requestPayWithCard(accessToken, String.valueOf(tradeDeviceId), nullCardCompanyRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "카드 회사는 K, H, S 중 하나 이어야 합니다.");
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 실패
     * - 실패 사유 : RequestBody - cardCompany 필드 empty
     */
    @Test
    @WithMockUser
    void 거래_주문_내역_일괄_카드_결제_실패_RequestBody_cardCompany_필드_empty() throws Exception {
        // given
        String accessToken = "accessToken";
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto emptyCardCompanyRequest = PayWithCardRequestDtoBuilder.emptyCardCompanyBuild();

        // when
        ResultActions resultActions = requestPayWithCard(accessToken, String.valueOf(tradeDeviceId), emptyCardCompanyRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "카드 회사는 K, H, S 중 하나 이어야 합니다.");
    }

    /**
     * 거래 주문 내역 일괄 카드 결제 실패
     * - 실패 사유 : RequestBody - cardCompany 필드 유효성
     */
    @Test
    @WithMockUser
    void 거래_주문_내역_일괄_카드_결제_실패_RequestBody_cardCompany_필드_유효성() throws Exception {
        // given
        String accessToken = "accessToken";
        Long tradeDeviceId = 1L;
        PayWithCardRequestDto invalidCardCompanyRequest = PayWithCardRequestDtoBuilder.invalidCardCompanyBuild();

        // when
        ResultActions resultActions = requestPayWithCard(accessToken, String.valueOf(tradeDeviceId), invalidCardCompanyRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "카드 회사는 K, H, S 중 하나 이어야 합니다.");
    }

    private ResultActions requestGetTradeIsNotCompletedByStoreOwner(String accessToken, String tradeDeviceId) throws Exception{
        return mvc.perform(get("/trade/{trade-device-id}", tradeDeviceId)
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }

    private ResultActions requestGetTradeIsNotCompletedByTradeDevice(String tradeDeviceId) throws Exception {
        return mvc.perform(get("/trade")
                .cookie(new MockCookie("tradeDeviceId", tradeDeviceId)))
            .andDo(print());
    }

    private ResultActions requestGetTradeIsNotCompletedByTradeDeviceWithOutCookie() throws Exception {
        return mvc.perform(get("/trade"))
            .andDo(print());
    }

    private ResultActions requestPayWithCash(String accessToken, String tradeDeviceId) throws Exception {
        return mvc.perform(post("/trade/pay/cash/{trade-device-id}", tradeDeviceId)
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }

    private ResultActions requestPayWithCard(String accessToken, String tradeDeviceId, PayWithCardRequestDto request) throws Exception {
        return mvc.perform(post("/trade/pay/card/{trade-device-id}", tradeDeviceId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }

}
