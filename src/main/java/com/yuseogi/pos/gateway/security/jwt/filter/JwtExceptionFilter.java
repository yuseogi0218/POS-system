package com.yuseogi.pos.gateway.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.common.exception.dto.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            createJwtErrorResponse(request, response, e);
        }
    }

    private void createJwtErrorResponse(HttpServletRequest request, HttpServletResponse response, CustomException e) throws IOException {

        ResponseEntity<ErrorResponse> errorResponse = e.toErrorResponse();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(errorResponse.getStatusCode().value());

        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(errorResponse.getBody());

        try (PrintWriter writer = response.getWriter()) {
            writer.write(responseBody);
        }

    }
}
