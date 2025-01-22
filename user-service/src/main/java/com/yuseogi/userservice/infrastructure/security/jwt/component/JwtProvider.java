package com.yuseogi.userservice.infrastructure.security.jwt.component;

import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.userservice.infrastructure.cache.redis.repository.InvalidAccessTokenRedisRepository;
import com.yuseogi.userservice.infrastructure.security.ExpireTime;
import com.yuseogi.userservice.infrastructure.security.dto.TokenInfoResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    private static final String BEARER_TYPE = "Bearer";
    private static final String AUTHORITY_KEY = "authority";
    private static final String TYPE_KEY = "type";

    public static final String TYPE_ACCESS = "access";
    public static final String TYPE_REFRESH = "refresh";

    private final SecretKey secretKey;

    public JwtProvider(
        @Value("${jwt.secret.key}") String secretKey
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    /**
     * userId, authorityList 을 가지고 AccessToken, RefreshToken 을 생성
     */
    public TokenInfoResponseDto generateToken(Long userId, List<String> authorityList) {

        Date now = new Date();

        // Access JWT Token 생성
        String accessToken = generateJWT(String.valueOf(userId), authorityList, TYPE_ACCESS, now, ExpireTime.ACCESS_TOKEN.getMillSecond());

        // Refresh JWT Token 생성
        String refreshToken = generateJWT(String.valueOf(userId), authorityList, TYPE_REFRESH, now, ExpireTime.REFRESH_TOKEN.getMillSecond());

        return TokenInfoResponseDto.builder()
                .authorityList(authorityList)
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpireIn(ExpireTime.ACCESS_TOKEN.getSecond())
                .refreshToken(refreshToken)
                .refreshTokenExpireIn(ExpireTime.REFRESH_TOKEN.getSecond())
                .build();
    }

    /**
     * JWT 생성
     */
    public String generateJWT(String subject, List<String> authorityList, String type, Date issuedAt, long expireTime) {
        return Jwts.builder()
                .subject(subject)
                .claim(AUTHORITY_KEY, authorityList)
                .claim(TYPE_KEY, type)
                .issuedAt(issuedAt)
                .expiration(new Date(issuedAt.getTime() + expireTime)) //토큰 만료 시간 설정
                .signWith(secretKey)
                .compact();
    }

    /**
     * Jwt 토큰을 복호화
     */
    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    /**
     * JWT 검증 수행
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            List<String> authorityList = claims.get(AUTHORITY_KEY, List.class);
            if (authorityList.isEmpty()) {
                throw new CustomException(CommonErrorCode.UNAUTHORIZED_JWT);
            }

            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomException(CommonErrorCode.EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(CommonErrorCode.UNSUPPORTED_JWT);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(CommonErrorCode.INVALID_JWT);
        }
    }

    /**
     * JWT 타입 검증
     */
    public void validateTokenType(String token, String tokenType) {
        if (!getType(token).equals(tokenType)) {
            throw new CustomException(CommonErrorCode.INVALID_TOKEN_TYPE);
        }
    }

    /**
     * JWT 타입 추출
     */
    public String getType(String token) {
        return (String) parseClaims(token).get(TYPE_KEY);
    }

    /**
     * JWT 잔여 유효시간 (milli 초)
     */
    public Long getExpireIn(String token) {
        Date expiration = parseClaims(token).getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
