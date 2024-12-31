package com.yuseogi.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class ParseRequestUtil {

    private ParseRequestUtil() {

    }

    public static Long extractUserIdFromRequest(HttpServletRequest httpServletRequest) {
        String userId = httpServletRequest.getHeader("X-Authorization-userId");

        return (userId == null || userId.isEmpty()) ? null : Long.parseLong(userId);
    }

    public static String extractAccessTokenFromRequest(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("X-Authorization-accessToken");
    }
}
