package com.yuseogi.pos.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuseogi.pos.PosApplication;
import com.yuseogi.pos.common.cache.redis.config.RedisTestContainers;
import com.yuseogi.pos.common.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 통합 테스트
 * 1. 테스트 성공
 * 2. JWT 기반의 인증 및 권한 부여 테스트
 * 3. 예외 및 에러 핸들링 테스트 (가상 시나리오 설정)
 */

@SpringBootTest(classes = PosApplication.class, webEnvironment = MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(RedisTestContainers.class)
@Transactional
public class IntegrationTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected RedisConnectionFactory redisConnectionFactory;

    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * JWT 기반의 인증 및 권한 부여 테스트
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */

    @AfterEach
    public void flushAllRedis() {
        redisConnectionFactory.getConnection().serverCommands().flushAll();
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
