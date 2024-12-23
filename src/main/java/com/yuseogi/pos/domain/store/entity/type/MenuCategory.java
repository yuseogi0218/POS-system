package com.yuseogi.pos.domain.store.entity.type;

import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.common.exception.CustomException;

public enum MenuCategory {
    MAIN_MENU,
    SUB_MENU,
    DRINK;

    public static MenuCategory ofRequest(String request) {
        try {
            return MenuCategory.valueOf(request);
        } catch (IllegalArgumentException e) {
            throw new CustomException(CommonErrorCode.INVALID_PARAMETER, "category 파라미터가 올바르지 않습니다.");
        }
    }
}
