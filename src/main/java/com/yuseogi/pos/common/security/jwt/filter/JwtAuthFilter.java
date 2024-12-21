package com.yuseogi.pos.common.security.jwt.filter;

import com.yuseogi.pos.common.security.jwt.component.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtProvider.resolveToken((HttpServletRequest) request);

        if (token != null && jwtProvider.validateToken(token)) {
            jwtProvider.validateTokenType(token, JwtProvider.TYPE_ACCESS);

            String httpMethod = ((HttpServletRequest) request).getMethod();
            String uri = ((HttpServletRequest)request).getRequestURI();

            if ((httpMethod.equals(HttpMethod.POST.name()) && uri.equals("/user/logout"))) {
                request.setAttribute("resolvedAccessToken", token);
            }

            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

}
