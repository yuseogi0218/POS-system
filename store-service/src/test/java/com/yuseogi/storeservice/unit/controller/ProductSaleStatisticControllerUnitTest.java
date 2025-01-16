package com.yuseogi.storeservice.unit.controller;

import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.ErrorCode;
import com.yuseogi.storeservice.controller.ProductSaleStatisticController;
import com.yuseogi.storeservice.dto.response.GetProductSaleStatisticResponseDto;
import com.yuseogi.storeservice.service.ProductSaleStatisticService;
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

@WebMvcTest(ProductSaleStatisticController.class)
public class ProductSaleStatisticControllerUnitTest extends ControllerUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ProductSaleStatisticService productSaleStatisticService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 상품 판매 통계 데이터 조회 성공
     */
    @ParameterizedTest
    @MethodSource("provideSuccessTestData")
    void 상품_판매_통계_데이터_조회_성공(String category, String dateTerm, String criteria) throws Exception {
        // given
        String userId = "1";
        String startDate = "2024-12-01";

        GetProductSaleStatisticResponseDto expectedResponse = mock(GetProductSaleStatisticResponseDto.class);

        // stub
        when(productSaleStatisticService.getProductSaleStatistic(Long.valueOf(userId), category, dateTerm, LocalDate.parse(startDate), criteria)).thenReturn(expectedResponse);

        // when
        ResultActions resultActions = requestGetProductSaleStatistic(userId, category, dateTerm, startDate, criteria);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, GetProductSaleStatisticResponseDto.class);
    }

    private static Stream<Arguments> provideSuccessTestData() {
        return Stream.of(
            Arguments.of("MAIN_MENU", "DAY", "COUNT"),
            Arguments.of("MAIN_MENU", "DAY", "AMOUNT"),
            Arguments.of("MAIN_MENU", "WEEK", "COUNT"),
            Arguments.of("MAIN_MENU", "MONTH", "COUNT"),
            Arguments.of("SUB_MENU", "DAY", "COUNT"),
            Arguments.of("DRINK", "DAY", "COUNT")
        );
    }

    /**
     * 상품 판매 통계 데이터 조회 실패
     */
    @ParameterizedTest
    @MethodSource("provideFailureTestData")
    void 상품_판매_통계_데이터_조회_실패(ErrorCode errorCode, String expectedErrorMessage, String category, String dateTerm, String startDate, String criteria) throws Exception {
        // given
        String userId = "1";

        // when
        ResultActions resultActions = requestGetProductSaleStatistic(userId, category, dateTerm, startDate, criteria);

        // then
        assertErrorWithMessage(errorCode, resultActions, expectedErrorMessage);
    }

    private static Stream<Arguments> provideFailureTestData() {
        return Stream.of(
            // 1. category null
            Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "category", null, "DAY", "2024-12-01", "COUNT"),

            // 2. category empty
            Arguments.of(CommonErrorCode.INVALID_PARAMETER, "상품 카테고리는 필수 선택값입니다.", "", "DAY", "2024-12-01", "COUNT"),

            // 3. category 유효성
            Arguments.of(CommonErrorCode.INVALID_PARAMETER, "상품 카테고리는 MAIN_MENU, SUB_MENU, DRINK 중 하나 이어야 합니다.", "INVALID_CATEGORY", "DAY", "2024-12-01", "COUNT"),

            // 4. date-term null
            Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "date-term", "MAIN_MENU", null, "2024-12-01", "COUNT"),

            // 5. date-term empty
            Arguments.of(CommonErrorCode.INVALID_PARAMETER, "조회 기간은 필수 선택값입니다.", "MAIN_MENU", "", "2024-12-01", "COUNT"),

            // 6. date-term 유효성
            Arguments.of(CommonErrorCode.INVALID_PARAMETER, "조회 기간은 DAY, WEEK, MONTH 중 하나 이어야 합니다.", "MAIN_MENU", "INVALID_DATE_TERM", "2024-12-01", "COUNT"),

            // 7. start-date null
            Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "start-date", "MAIN_MENU", "DAY", null, "COUNT"),

            // 8. start-date 유효성
            Arguments.of(CommonErrorCode.MISMATCH_PARAMETER_TYPE, "start-date", "MAIN_MENU", "DAY", "invalid-date", "COUNT"),

            // 9. criteria null
            Arguments.of(CommonErrorCode.REQUIRED_PARAMETER, "criteria", "MAIN_MENU", "DAY", "2024-12-01", null),

            // 10. criteria empty
            Arguments.of(CommonErrorCode.INVALID_PARAMETER, "조회 기준은 필수 선택값입니다.", "MAIN_MENU", "DAY", "2024-12-01", ""),

            // 11. criteria 유효성
            Arguments.of(CommonErrorCode.INVALID_PARAMETER, "조회 기준은 COUNT, AMOUNT 중 하나 이어야 합니다.", "MAIN_MENU", "DAY", "2024-12-01", "INVALID_CRITERIA")
        );
    }

    private ResultActions requestGetProductSaleStatistic(String userId, String category, String dateTerm, String startDate, String criteria) throws Exception {
        return mvc.perform(get("/product/sale/statistic")
                .header("X-Authorization-userId", userId)
                .queryParam("category", category)
                .queryParam("date-term", dateTerm)
                .queryParam("start-date", startDate)
                .queryParam("criteria", criteria))
            .andDo(print());
    }
}
