package com.yuseogi.pos.gateway.security.component;

import com.yuseogi.pos.gateway.security.ExpireTime;
import com.yuseogi.pos.gateway.security.jwt.component.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtBuilder {

    private final JwtProvider jwtProvider;

    public String accessTokenBuild() {
        return jwtProvider.generateJWT("user@domain.com", List.of("ROLE_STORE_OWNER"), JwtProvider.TYPE_ACCESS, new Date(), ExpireTime.ACCESS_TOKEN.getMillSecond());
    }

    public String refreshTokenBuild() {
        return jwtProvider.generateJWT("user@domain.com", List.of("ROLE_STORE_OWNER"), JwtProvider.TYPE_REFRESH, new Date(), ExpireTime.REFRESH_TOKEN.getMillSecond());
    }

    public String invalidJwtBuild() {
        return "invalid.jwt.token";
    }

    public String unauthorizedAccessTokenBuild() {
        return jwtProvider.generateJWT("user@domain.com", new ArrayList<>(), JwtProvider.TYPE_ACCESS, new Date(), ExpireTime.ACCESS_TOKEN.getMillSecond());
    }

    public String unauthorizedRefreshTokenBuild() {
        return jwtProvider.generateJWT("user@domain.com", new ArrayList<>(), JwtProvider.TYPE_REFRESH, new Date(), ExpireTime.REFRESH_TOKEN.getMillSecond());
    }

    public String expiredAccessTokenBuild() {
        Date expiredDate = new Date(new Date().getTime() - ExpireTime.ACCESS_TOKEN.getMillSecond());
        return jwtProvider.generateJWT("user@domain.com", List.of("ROLE_STORE_OWNER"), JwtProvider.TYPE_ACCESS, expiredDate, ExpireTime.ACCESS_TOKEN.getMillSecond());
    }

    public String expiredRefreshTokenBuild() {
        Date expiredDate = new Date(new Date().getTime() - ExpireTime.REFRESH_TOKEN.getMillSecond());
        return jwtProvider.generateJWT("user@domain.com", List.of("ROLE_STORE_OWNER"), JwtProvider.TYPE_REFRESH, expiredDate, ExpireTime.REFRESH_TOKEN.getMillSecond());
    }

}
