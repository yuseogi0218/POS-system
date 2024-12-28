package com.yuseogi.pos.gateway.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExpireTime {
    ACCESS_TOKEN(60 * 60L, 60 * 60 * 1000L),
    REFRESH_TOKEN(7 * 24 * 60 * 60L, 7 * 24 * 60 * 60 * 1000L);

    private final Long second;

    private final Long millSecond;
}
