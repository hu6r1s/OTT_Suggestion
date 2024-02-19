package com.three.ott_suggestion.auth;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.ott_suggestion.auth.filter.MockSpringSecurityFilter;
import com.three.ott_suggestion.global.config.SecurityConfig;
import com.three.ott_suggestion.user.controller.AuthController;
import com.three.ott_suggestion.user.service.AuthService;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(
    controllers = {AuthController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = SecurityConfig.class
        )
    }
)
public class AuthTest {

    private MockMvc mvc;
    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_성공() throws Exception {
        // given
        Map<String, Object> signupRequest = new HashMap<>();
        signupRequest.put("email", "darmaa333@gmail.com");
        signupRequest.put("password", "ASDasd123!!!");
        signupRequest.put("nickname", "test");
        signupRequest.put("introduction", "test");

        // when - then
        mvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signupRequest))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("회원가입 성공"))
            .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패")
    void 회원가입_실패() throws Exception {
        // given
        Map<String, Object> signupRequest = new HashMap<>();
        signupRequest.put("email", "bademail");
        signupRequest.put("password", "ASDasd123");
        signupRequest.put("nickname", "test");
        signupRequest.put("introduction", "test");

        // when - then
        mvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signupRequest))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("회원가입 실패"))
            .andExpect(jsonPath("$.data[*].message", containsInAnyOrder(
                "이메일 주소 형식을 지켜주세요",
                "비밀번호는 8글자~20자, 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 1개 이상 포함하세요."
            )))
            .andDo(print());
    }
}
