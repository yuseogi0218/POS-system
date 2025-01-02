package com.yuseogi.common.util;

public class PhoneNumberUtil {

    private PhoneNumberUtil() {

    }

    public static String formatPhoneNumber(String rawPhoneNumber) {
        if (rawPhoneNumber == null || !rawPhoneNumber.startsWith("+82")) {
            return rawPhoneNumber; // 변환이 필요 없는 경우 원본 반환
        }
        rawPhoneNumber = rawPhoneNumber.replaceAll("\\s+", "");
        rawPhoneNumber = rawPhoneNumber.replaceAll("-", "");
        // +82 -> 0
        String localPhoneNumber = rawPhoneNumber.replace("+82", "0");

        return localPhoneNumber;
    }
}
