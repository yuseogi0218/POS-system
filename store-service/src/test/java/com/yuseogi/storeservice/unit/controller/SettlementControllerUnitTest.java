package com.yuseogi.storeservice.unit.controller;

import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.ErrorCode;
import com.yuseogi.storeservice.controller.SettlementController;
import com.yuseogi.storeservice.dto.response.GetSettlementDetailResponseDto;
import com.yuseogi.storeservice.dto.response.GetSettlementResponseDto;
import com.yuseogi.storeservice.service.SettlementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SettlementController.class)
public class SettlementControllerUnitTest extends ControllerUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private SettlementService settlementService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 상점 정산 데이터 조회 성공
     */
    @ParameterizedTest
    @MethodSource("provideSuccessTestData")
    void 상점_정산_데이터_조회_성공(String dateTerm) throws Exception {
        // given
        String userId = "1";
        String startDate = "2024-12-01";

        GetSettlementResponseDto expectedResponse = mock(GetSettlementResponseDto.class);

        // stub
        when(settlementService.getSettlement(Long.valueOf(userId), dateTerm, LocalDate.parse(startDate))).thenReturn(expectedResponse);

        // when
        ResultActions resultActions = requestGetSettlement(userId, dateTerm, startDate);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, GetSettlementResponseDto.class);
    }

    /**
     * 상점 정산 데이터 조회 실패
     */
    @ParameterizedTest
    @MethodSource("provideFailureTestData")
    void 상점_정산_데이터_조회_실패(ErrorCode errorCode, String expectedErrorMessage, String dateTerm, String startDate) throws Exception {
        // given
        String userId = "1";

        // when
        ResultActions resultActions = requestGetSettlement(userId, dateTerm, startDate);

        // then
        assertErrorWithMessage(errorCode, resultActions, expectedErrorMessage);
    }

    /**
     * 상점 정산 세부 데이터 조회 성공
     */
    @ParameterizedTest
    @MethodSource("provideSuccessTestData")
    void 상점_정산_세부_데이터_조회_성공(String dateTerm) throws Exception {
        // given
        String userId = "1";
        String startDate = "2024-12-01";

        GetSettlementDetailResponseDto expectedResponse = mock(GetSettlementDetailResponseDto.class);

        // stub
        when(settlementService.getSettlementDetail(Long.valueOf(userId), dateTerm, LocalDate.parse(startDate))).thenReturn(expectedResponse);

        // when
        ResultActions resultActions = requestGetSettlementDetail(userId, dateTerm, startDate);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, GetSettlementDetailResponseDto.class);
    }

    /**
     * 상점 정산 세부 데이터 조회 실패
     */
    @ParameterizedTest
    @MethodSource("provideFailureTestData")
    void 상점_정산_세부_데이터_조회_실패(ErrorCode errorCode, String expectedErrorMessage, String dateTerm, String startDate) throws Exception {
        // given
        String userId = "1";

        // when
        ResultActions resultActions = requestGetSettlementDetail(userId, dateTerm, startDate);

        // then
        assertErrorWithMessage(errorCode, resultActions, expectedErrorMessage);
    }

    private static Stream<Arguments> provideSuccessTestData() {
        return Stream.of(
            Arguments.of("DAY"),
            Arguments.of("MONTH")
        );
    }

    private static Stream<Arguments> provideFailureTestData() {
        return Stream.of(
            // 1. date-term empty
            Arguments.of(CommonErrorCode.INVALID_PARAMETER, "조회 기간은 필수 선택값입니다.", "", "2024-12-01"),

            // 2. date-term 유효성
            Arguments.of(CommonErrorCode.INVALID_PARAMETER, "조회 기간은 DAY, MONTH 중 하나 이어야 합니다.", "INVALID_DATE_TERM", "2024-12-01"),

            // 3. start-date null
            Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "start-date", "DAY", null),

            // 4. start-date 유효성
            Arguments.of(CommonErrorCode.MISMATCH_PARAMETER_TYPE, "start-date", "DAY", "invalid-date")
        );
    }

    private ResultActions requestGetSettlement(String userId, String dateTerm, String startDate) throws Exception {
        return mvc.perform(get("/store/settlement")
                .header("X-Authorization-userId", userId)
                .queryParam("date-term", dateTerm)
                .queryParam("start-date", startDate))
            .andDo(print());
    }

    private ResultActions requestGetSettlementDetail(String userId, String dateTerm, String startDate) throws Exception {
        return mvc.perform(get("/store/settlement/detail")
                .header("X-Authorization-userId", userId)
                .queryParam("date-term", dateTerm)
                .queryParam("start-date", startDate))
            .andDo(print());
    }
}
