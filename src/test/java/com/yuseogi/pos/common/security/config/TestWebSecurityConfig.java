package com.yuseogi.pos.common.security.config;

import com.yuseogi.pos.common.security.component.CustomAuthenticationProvider;
import com.yuseogi.pos.common.security.jwt.filter.JwtAuthFilter;
import com.yuseogi.pos.common.security.jwt.filter.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@TestConfiguration
@RequiredArgsConstructor
public class TestWebSecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors(withDefaults());
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests((authorizeRequests) ->
            authorizeRequests
                .requestMatchers("/image/**").permitAll()
                .requestMatchers("/page/**").permitAll()
                .requestMatchers("/user/logout").authenticated()
                .requestMatchers("/user/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/store/product").permitAll()
                .requestMatchers("/store/**").authenticated()
                .anyRequest().authenticated()
        );

        httpSecurity.exceptionHandling(handler -> handler.authenticationEntryPoint(authenticationEntryPoint)); // 커스텀 인증 에러 처리 설정
        httpSecurity.exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler)); // 커스텀 인가 에러 처리 설정

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 API Endpoint 에 동일한 configuration 적용
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
