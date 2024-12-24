package com.yuseogi.pos.domain.user.unit.controller;

import com.yuseogi.pos.common.ControllerUnitTest;
import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.common.security.dto.TokenInfoResponseDto;
import com.yuseogi.pos.domain.user.controller.UserController;
import com.yuseogi.pos.domain.user.dto.request.SignUpKakaoRequestDto;
import com.yuseogi.pos.domain.user.dto.request.SignUpKakaoRequestDtoBuilder;
import com.yuseogi.pos.domain.user.exception.UserErrorCode;
import com.yuseogi.pos.domain.user.service.UserAccountService;
import com.yuseogi.pos.domain.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerUnitTest extends ControllerUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private UserAccountService userAccountService;

    @MockitoBean
    private UserAuthService userAuthService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 소셜(카카오) 로그인 성공
     */
    @Test
    void 소셜_카카오_로그인_성공() throws Exception {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        Authentication authentication = mock(Authentication.class);
        TokenInfoResponseDto expectedTokenInfoResponse = mock(TokenInfoResponseDto.class);

        // stub
        when(userAuthService.authenticateKakao(kakaoAccessToken)).thenReturn(authentication);
        when(userAuthService.login(any(), eq(authentication))).thenReturn(expectedTokenInfoResponse);

        // when
        ResultActions resultActions = requestLoginKakao(kakaoAccessToken);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, TokenInfoResponseDto.class);
    }

    /**
     * 소셜(카카오) 로그인 실패
     * - 실패 사유 : token 파라미터 null
     */
    @Test
    void 소셜_카카오_로그인_실패_token_파라미터_null() throws Exception {
        // given
        String nullKakaoAccessToken = null;

        // when
        ResultActions resultActions = requestLoginKakao(nullKakaoAccessToken);

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_PARAMETER, resultActions, "token");
    }

    /**
     * 소셜(카카오) 로그인 실패
     * - 실패 사유 : token 파라미터 empty
     */
    @Test
    void 소셜_카카오_로그인_실패_token_파라미터_empty() throws Exception {
        // given
        String emptyKakaoAccessToken = "";

        // when
        ResultActions resultActions = requestLoginKakao(emptyKakaoAccessToken);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "token은 필수 입력값입니다.");
    }

    /**
     * 소셜(카카오) 회원가입 성공
     */
    @Test
    void 소셜_카카오_회원가입_성공() throws Exception {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        SignUpKakaoRequestDto request = SignUpKakaoRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestSignUpKakao(kakaoAccessToken, request);

        // then
        resultActions.andExpect(status().isOk());
        verify(userAccountService, times(1)).signUpKakao(kakaoAccessToken, request);
    }

    /**
     * 소셜(카카오) 회원가입 실패
     * - 실패 사유 : token 파라미터 null
     */
    @Test
    void 소셜_카카오_회원가입_실패_token_파라미터_null() throws Exception {
        // given
        String nullKakaoAccessToken = null;
        SignUpKakaoRequestDto request = SignUpKakaoRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestSignUpKakao(nullKakaoAccessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_PARAMETER, resultActions, "token");
    }

    /**
     * 소셜(카카오) 회원가입 실패
     * - 실패 사유 : token 파라미터 empty
     */
    @Test
    void 소셜_카카오_회원가입_실패_token_파라미터_empty() throws Exception {
        // given
        String emptyKakaoAccessToken = "";
        SignUpKakaoRequestDto request = SignUpKakaoRequestDtoBuilder.build();

        // when
        ResultActions resultActions = requestSignUpKakao(emptyKakaoAccessToken, request);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "token은 필수 입력값입니다.");
    }

    /**
     * 소셜(카카오) 회원가입 실패
     * - 실패 사유 : RequestBody - storeName 필드 null
     */
    @Test
    void 소셜_카카오_회원가입_실패_RequestBody_storeName_필드_null() throws Exception {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        SignUpKakaoRequestDto nullStoreNameRequest = SignUpKakaoRequestDtoBuilder.nullStoreNameBuild();

        // when
        ResultActions resultActions = requestSignUpKakao(kakaoAccessToken, nullStoreNameRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상점 이름은 필수 입력값입니다.");
    }

    /**
     * 소셜(카카오) 회원가입 실패
     * - 실패 사유 : RequestBody - storeName 필드 empty
     */
    @Test
    void 소셜_카카오_회원가입_실패_RequestBody_storeName_필드_empty() throws Exception {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        SignUpKakaoRequestDto emptyStoreNameRequest = SignUpKakaoRequestDtoBuilder.emptyStoreNameBuild();

        // when
        ResultActions resultActions = requestSignUpKakao(kakaoAccessToken, emptyStoreNameRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상점 이름은 필수 입력값입니다.");
    }

    /**
     * 소셜(카카오) 회원가입 실패
     * - 실패 사유 : RequestBody - storeName 필드 유효성
     */
    @Test
    void 소셜_카카오_회원가입_실패_RequestBody_storeName_필드_유효성() throws Exception {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        SignUpKakaoRequestDto invalidStoreNameRequest = SignUpKakaoRequestDtoBuilder.invalidStoreNameBuild();

        // when
        ResultActions resultActions = requestSignUpKakao(kakaoAccessToken, invalidStoreNameRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "상점 이름의 최대 글자수는 20자 입니다.");
    }

    /**
     * 소셜(카카오) 회원가입 실패
     * - 실패 사유 : RequestBody - posGrade 필드 null
     */
    @Test
    void 소셜_카카오_회원가입_실패_RequestBody_posGrade_필드_null() throws Exception {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        SignUpKakaoRequestDto nullPosGradeRequest = SignUpKakaoRequestDtoBuilder.nullPosGradeBuild();

        // when
        ResultActions resultActions = requestSignUpKakao(kakaoAccessToken, nullPosGradeRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "POS 시스템 사용 등급은 BRONZE, SILVER, GOLD 중 하나 이어야 합니다.");
    }

    /**
     * 소셜(카카오) 회원가입 실패
     * - 실패 사유 : RequestBody - posGrade 필드 empty
     */
    @Test
    void 소셜_카카오_회원가입_실패_RequestBody_posGrade_필드_empty() throws Exception {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        SignUpKakaoRequestDto emptyPosGradeRequest = SignUpKakaoRequestDtoBuilder.emptyPosGradeBuild();

        // when
        ResultActions resultActions = requestSignUpKakao(kakaoAccessToken, emptyPosGradeRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "POS 시스템 사용 등급은 BRONZE, SILVER, GOLD 중 하나 이어야 합니다.");
    }

    /**
     * 소셜(카카오) 회원가입 실패
     * - 실패 사유 : RequestBody - posGrade 필드 유효성
     */
    @Test
    void 소셜_카카오_회원가입_실패_RequestBody_posGrade_필드_유효성() throws Exception {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        SignUpKakaoRequestDto invalidPosGradeRequest = SignUpKakaoRequestDtoBuilder.invalidPosGradeBuild();

        // when
        ResultActions resultActions = requestSignUpKakao(kakaoAccessToken, invalidPosGradeRequest);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_REQUEST_BODY_FIELDS, resultActions, "POS 시스템 사용 등급은 BRONZE, SILVER, GOLD 중 하나 이어야 합니다.");
    }

    /**
     * Refresh Token 을 이용한 Access Token 재 발급 (Re-Issue) 성공
     */
    @Test
    void Access_Token_재발급_성공() throws Exception {
        // given
        String refreshToken = "refreshToken";
        TokenInfoResponseDto expectedTokenInfoResponse = mock(TokenInfoResponseDto.class);

        // stub
        when(userAuthService.reIssue(any(), eq(refreshToken))).thenReturn(expectedTokenInfoResponse);

        // when
        ResultActions resultActions = requestReIssue(refreshToken);

        // then
        String responseString = resultActions
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        objectMapper.readValue(responseString, TokenInfoResponseDto.class);
    }

    /**
     * Refresh Token 을 이용한 Access Token 재 발급 (Re-Issue) 성공
     * - 실패 사유 : 요청 시, Cookie 에 Authorization 정보(Refresh Token)을 추가하지 않음
     */
    @Test
    void Access_Token_재발급_실패_Cookie_존재() throws Exception {
        // given

        // when
        ResultActions resultActions = requestReIssueWithOutCookie();

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_COOKIE, resultActions, "refreshToken");
    }

    /**
     * Refresh Token 을 이용한 Access Token 재 발급 (Re-Issue) 실패
     * - 실패 사유 : 유효하지 않은 Refresh Token 을 사용한
     */
    @Test
    void Access_Token_재발급_실패_유효하지_않은_Refresh_Token() throws Exception {
        // given
        String invalidRefreshToken = "invalidRefreshToken";

        // stub
        when(userAuthService.reIssue(any(), eq(invalidRefreshToken))).thenThrow(new CustomException(UserErrorCode.INVALID_REFRESH_TOKEN));

        // when
        ResultActions resultActions = requestReIssue(invalidRefreshToken);

        // then
        assertError(UserErrorCode.INVALID_REFRESH_TOKEN, resultActions);
    }

    /**
     * 로그아웃 성공
     */
    @Test
    @WithMockUser
    void 로그아웃_성공() throws Exception {
        // given
        String accessToken = "accessToken";

        // when
        ResultActions resultActions = requestLogout(accessToken);

        // then
        resultActions.andExpect(status().isOk());
        verify(userAuthService, times(1)).logout(any());
    }

    /**
     * 로그아웃 실패
     * - 실패 사유 : 인증 실패
     */
    @Test
    @WithAnonymousUser
    void 로그아웃_실패_인증() throws Exception {
        // given
        String accessToken = "accessToken";

        // when
        ResultActions resultActions = requestLogout(accessToken);

        // then
        assertError(CommonErrorCode.INSUFFICIENT_AUTHENTICATION, resultActions);
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

    private ResultActions requestReIssueWithOutCookie() throws Exception {
        return mvc.perform(post("/user/re-issue"))
            .andDo(print());
    }

    private ResultActions requestLogout(String accessToken) throws Exception {
        return mvc.perform(post("/user/logout")
                .header("Authorization", "Bearer " + accessToken))
            .andDo(print());
    }
}
