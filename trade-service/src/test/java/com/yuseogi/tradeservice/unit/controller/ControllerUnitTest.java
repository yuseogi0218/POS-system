package com.yuseogi.tradeservice.unit.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yuseogi.common.exception.ErrorCode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller 컴포넌트의 역할
 * - API 호출 방식을 정의 -> ex. @GetMapping
 * - 어떤 비즈니스 로직을 실행할 것인지 결정 -> ex. 서비스 컴포넌트의 메서드 호출
 * - API 호출 결과를 어떤 포맷으로 응답할지 정의 -> ex. ResponseEntity<>, response DTO 변환
 */

/**
 * Controller Unit Test
 * 1. 테스트 성공
 * 2. Role, Authority 검증
 * 3. 요청 데이터(필드 및 파라미터) 검증
 */
@ActiveProfiles("test")
public class ControllerUnitTest {

    protected MockMvc mvc;

    protected ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true).registerModule(new JavaTimeModule());

    public MockMvc buildMockMvc(WebApplicationContext context) {
        return MockMvcBuilders
            .webAppContextSetup(context)
            .build();
    }

    public void assertError(ErrorCode expected, ResultActions actual) throws Exception {
        actual
            .andExpect(status().is(expected.getHttpStatus().value()))
            .andExpect(jsonPath("errorCode").value(expected.getCode()))
            .andExpect(jsonPath("message").value(expected.getMessage()));
    }

    public void assertErrorWithMessage(ErrorCode expected, ResultActions actual, String message) throws Exception {
        actual
            .andExpect(status().is(expected.getHttpStatus().value()))
            .andExpect(jsonPath("errorCode").value(expected.getCode()))
            .andExpect(jsonPath("message").value(String.format(expected.getMessage(), message)));
    }
}
