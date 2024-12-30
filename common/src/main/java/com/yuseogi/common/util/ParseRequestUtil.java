package com.yuseogi.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class ParseRequestUtil {

    private ParseRequestUtil() {

    }

    public static String extractUserEmailFromRequest(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("X-Authorization-userEmail");
    }

    public static String extractAccessTokenFromRequest(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("X-Authorization-accessToken");
    }
}
