package com.yuseogi.pos.domain.user.integration;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yuseogi.pos.gateway.IntegrationTest;
import com.yuseogi.pos.gateway.client.dto.response.KakaoAccountResponseDto;
import com.yuseogi.pos.gateway.client.dto.response.KakaoAccountResponseDtoBuilder;
import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.pos.gateway.security.component.JwtBuilder;
import com.yuseogi.pos.gateway.security.dto.TokenInfoResponseDto;
import com.yuseogi.pos.domain.user.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.pos.domain.user.dto.request.SignUpKakaoRequestDtoBuilder;
import com.yuseogi.pos.domain.user.exception.UserErrorCode;
import jakarta.servlet.http.Cookie;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    public static MockWebServer mockWebServer;

    @DynamicPropertySource
    public static void addUrlProperties(DynamicPropertyRegistry registry) {
        registry.add("kakao.account-uri", () -> "http://localhost:" + mockWebServer.getPort() + "/v2/user/me");
    }

    @BeforeAll
    public static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    /**
     * 소셜(카카오) 로그인 성공
     */
    @Test
    void 소셜_카카오_로그인_성공() throws Exception {
        // before
        objectMapper.registerModule(new JavaTimeModule());

        // given
        String kakaoAccessToken = "kakaoAccessToken";
        KakaoAccountResponseDto kakaoAccountResponse = KakaoAccountResponseDtoBuilder.build();

        // stub
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(objectMapper.writeValueAsString(kakaoAccountResponse))
            .addHeader("Content-Type", "application/json"));

        // when
        ResultActions resultActions = requestLoginKakao(kakaoAccessToken);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, TokenInfoResponseDto.class);
    }

    /**
     * 소셜(카카오) 로그인 성공
     * - 존재하지 않는 사용자
     */
    @Test
    void 소셜_카카오_로그인_실패_존재하지_않는_사용자() throws Exception {
        // before
        objectMapper.registerModule(new JavaTimeModule());

        // given
        String kakaoAccessToken = "kakaoAccessToken";
        KakaoAccountResponseDto unknownKakaoAccountResponse = KakaoAccountResponseDtoBuilder.unknownBuild();

        // stub
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(objectMapper.writeValueAsString(unknownKakaoAccountResponse))
            .addHeader("Content-Type", "application/json"));

        // when
        ResultActions resultActions = requestLoginKakao(kakaoAccessToken);

        // then
        assertError(UserErrorCode.NOT_FOUND_USER, resultActions);
    }

    /**
     * 소셜(카카오) 회원가입 성공
     */
    @Test
    void 소셜_카카오_회원가입_성공() throws Exception {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        SignUpKakaoRequestDto request = SignUpKakaoRequestDtoBuilder.build();
        KakaoAccountResponseDto unknownKakaoAccountResponse = KakaoAccountResponseDtoBuilder.unknownBuild();

        // stub
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(objectMapper.writeValueAsString(unknownKakaoAccountResponse))
            .addHeader("Content-Type", "application/json"));

        // when
        ResultActions resultActions = requestSignUpKakao(kakaoAccessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 소셜(카카오) 회원가입 실패
     * - 실패 사유 : 이미 사용 중인 이메일 입니다.
     */
    @Test
    void 소셜_카카오_회원가입_실패_이메일_중복() throws Exception {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        SignUpKakaoRequestDto request = SignUpKakaoRequestDtoBuilder.build();
        KakaoAccountResponseDto kakaoAccountResponse = KakaoAccountResponseDtoBuilder.build();

        // stub
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(objectMapper.writeValueAsString(kakaoAccountResponse))
            .addHeader("Content-Type", "application/json"));

        // when
        ResultActions resultActions = requestSignUpKakao(kakaoAccessToken, request);

        // then
        assertError(UserErrorCode.EMAIL_ALREADY_USED, resultActions);
    }

    /**
     * Refresh Token 을 이용한 Access Token 재 발급 (Re-Issue) 성공
     */
    @Test
    void Access_Token_재발급_성공() throws Exception {
        // before
        objectMapper.registerModule(new JavaTimeModule());

        // given
        String kakaoAccessToken = "kakaoAccessToken";
        KakaoAccountResponseDto kakaoAccountResponse = KakaoAccountResponseDtoBuilder.build();

        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(objectMapper.writeValueAsString(kakaoAccountResponse))
            .addHeader("Content-Type", "application/json"));

        String responseStringOfLogin = requestLoginKakao(kakaoAccessToken).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        String refreshToken = objectMapper.readValue(responseStringOfLogin, TokenInfoResponseDto.class).refreshToken();

        // when
        ResultActions resultActions = requestReIssue(refreshToken);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, TokenInfoResponseDto.class);
    }

    /**
     * Refresh Token 을 이용한 Access Token 재 발급 (Re-Issue) 실패
     * - 실패 사유 : 요청 시, Cookie 에 있는 Authorization 정보 (Refresh Token) 에 권한 정보가 없음
     */
    @Test
    void Access_Token_재발급_실패_Unauthorized_Refresh_Token() throws Exception {
        // given
        String unauthorizedRefreshToken = jwtBuilder.unauthorizedRefreshTokenBuild();

        // when
        ResultActions resultActions = requestReIssue(unauthorizedRefreshToken);

        // then
        assertError(CommonErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * Refresh Token 을 이용한 Access Token 재 발급 (Re-Issue) 실패
     * - 실패 사유 : 요청 시, Cookie 에 다른 타입의 Authorization 정보 (Access Token) 를 추가함
     */
    @Test
    void Access_Token_재발급_실패_Token_Type() throws Exception {
        // given
        String accessToken = jwtBuilder.accessTokenBuild();

        // when
        ResultActions resultActions = requestReIssue(accessToken);

        // then
        assertError(CommonErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * Refresh Token 을 이용한 Access Token 재 발급 (Re-Issue) 실패
     * - 실패 사유 : 요청 시, Cookie 에 있는 Authorization(Refresh Token) 의 유효기간 만료
     */
    @Test
    void Access_Token_재발급_실패_Expired_Refresh_Token() throws Exception {
        // given
        String expiredRefreshToken = jwtBuilder.expiredRefreshTokenBuild();

        // when
        ResultActions resultActions = requestReIssue(expiredRefreshToken);

        // then
        assertError(CommonErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * Refresh Token 을 이용한 Access Token 재 발급 (Re-Issue) 실패
     * - 실패 사유 : 요청 시, Cookie 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    void Access_Token_재발급_실패_Invalid_Token() throws Exception {
        // given
        String invalidJwt = jwtBuilder.invalidJwtBuild();

        // when
        ResultActions resultActions = requestReIssue(invalidJwt);

        // then
        assertError(CommonErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * Refresh Token 을 이용한 Access Token 재 발급 (Re-Issue) 실패
     * - 실패 사유 : 최초 로그인 IP 주소와 요청 IP 주소가 일치하지 않음
     */
    @Test
    void Access_Token_재발급_실패_IP_Address() throws Exception {
        // before
        objectMapper.registerModule(new JavaTimeModule());

        // given
        String kakaoAccessToken = "kakaoAccessToken";
        KakaoAccountResponseDto kakaoAccountResponse = KakaoAccountResponseDtoBuilder.build();

        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(objectMapper.writeValueAsString(kakaoAccountResponse))
            .addHeader("Content-Type", "application/json"));

        String responseStringOfLogin = requestLoginKakao(kakaoAccessToken).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        String refreshToken = objectMapper.readValue(responseStringOfLogin, TokenInfoResponseDto.class).refreshToken();

        // when
        ResultActions resultActions = requestReIssueWithDifferentIpAddress(refreshToken);

        // then
        assertError(UserErrorCode.INVALID_REFRESH_TOKEN, resultActions);
    }

    /**
     * 로그아웃 성공
     */
    @Test
    void 로그아웃_성공() throws Exception {
        // before
        objectMapper.registerModule(new JavaTimeModule());

        // given
        String kakaoAccessToken = "kakaoAccessToken";
        KakaoAccountResponseDto kakaoAccountResponse = KakaoAccountResponseDtoBuilder.build();

        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(objectMapper.writeValueAsString(kakaoAccountResponse))
            .addHeader("Content-Type", "application/json"));

        String responseStringOfLogin = requestLoginKakao(kakaoAccessToken).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        String accessToken = objectMapper.readValue(responseStringOfLogin, TokenInfoResponseDto.class).accessToken();

        // when
        ResultActions resultActions = requestLogout(accessToken);

        // then
        resultActions.andExpect(status().isOk());

        // 로그아웃 한 accessToken 을 활용한 접근 시도
        // when
        ResultActions resultActionsAfterLogout = requestLogout(accessToken);

        // then
        assertError(CommonErrorCode.INVALID_ACCESS_TOKEN, resultActionsAfterLogout);
    }

    /**
     * 로그아웃 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보(Access Token)를 추가하지 않음
     */
    @Test
    void 로그아웃_실패_Header_Authorization_존재() throws Exception {
        // given

        // when
        ResultActions resultActions = requestLogoutWithOutAccessToken();

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
    }

    /**
     * 로그아웃 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보(Access Token)에 권한 정보가 없음
     */
    @Test
    void 로그아웃_실패_Unauthorized_Access_Token() throws Exception {
        // given
        String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessTokenBuild();

        // when
        ResultActions resultActions = requestLogout(unauthorizedAccessToken);

        // then
        assertError(CommonErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 로그아웃 실패
     * - 실패 사유 : 요청 시, Header 에 있는 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    void 로그아웃_실패_Token_Type() throws Exception {
        // given
        String refreshToken = jwtBuilder.refreshTokenBuild();

        // when
        ResultActions resultActions = requestLogout(refreshToken);

        // then
        assertError(CommonErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 로그아웃 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    void 로그아웃_실패_Expired_Access_Token() throws Exception {
        // given
        String expiredAccessToken = jwtBuilder.expiredAccessTokenBuild();

        // when
        ResultActions resultActions = requestLogout(expiredAccessToken);

        // then
        assertError(CommonErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 로그아웃 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    void 로그아웃_실패_Invalid_jwt() throws Exception {
        // given
        String invalidJwt = jwtBuilder.invalidJwtBuild();

        // when
        ResultActions resultActions = requestLogout(invalidJwt);

        // then
        assertError(CommonErrorCode.INVALID_JWT, resultActions);
    }

    private ResultActions requestLoginKakao(String token) throws Exception {
        return mvc.perform(post("/user/login/kakao")
                .param("token", token))
            .andDo(print());
    }

    private ResultActions requestSignUpKakao(String token, SignUpKakaoRequestDto request) throws Exception {
        return mvc.perform(post("/user/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .param("token", token)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());
    }

    private ResultActions requestReIssue(String refreshToken) throws Exception {
        return mvc.perform(post("/user/re-issue")
                .cookie(new MockCookie("refreshToken", refreshToken)))
            .andDo(print());
    }

    private ResultActions requestReIssueWithDifferentIpAddress(String refreshToken) throws Exception {
        Cookie cookie = new Cookie("refreshToken", refreshToken);

        return mvc.perform(post("/user/re-issue")
                .with(request -> {
                    request.setRemoteAddr("256.100.100.100");
                    return request;
                })
                .cookie(cookie))
            .andDo(print());
    }

    private ResultActions requestLogout(String accessToken) throws Exception {
        return mvc.perform(post("/user/logout")
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }

    private ResultActions requestLogoutWithOutAccessToken() throws Exception {
        return mvc.perform(post("/user/logout"))
            .andDo(print());
    }
}
