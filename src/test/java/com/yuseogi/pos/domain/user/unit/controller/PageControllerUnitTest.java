package com.yuseogi.pos.domain.user.unit.controller;

import com.yuseogi.pos.gateway.ControllerUnitTest;
import com.yuseogi.pos.domain.user.controller.PageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PageController.class)
public class PageControllerUnitTest extends ControllerUnitTest  {

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 로그인 페이지 테스트
     */
    @Test
    void 로그인_페이지() throws Exception {
        // given

        // when
        ResultActions resultActions = requestLoginPage();

        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(view().name("login"));
    }

    /**
     * 회원가입 페이지 테스트
     */
    @Test
    void 회원가입_페이지() throws Exception {
        // given
        String oauth = "oauth";
        String code = "code";

        // when
        ResultActions resultActions = requestSignUpPage(oauth, code);

        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(view().name("signup"))
            .andExpect(model().attribute("oauth", oauth))
            .andExpect(model().attribute("code", code));
    }

    ResultActions requestLoginPage() throws Exception {
        return mvc.perform(get("/page/login"))
            .andDo(print());
    }

    ResultActions requestSignUpPage(String oauth, String code) throws Exception {
        return mvc.perform(get("/page/signup")
                .param("oauth", oauth)
                .param("code", code))
            .andDo(print());
    }
}
