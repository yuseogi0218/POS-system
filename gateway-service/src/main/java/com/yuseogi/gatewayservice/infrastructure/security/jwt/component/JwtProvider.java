package com.yuseogi.gatewayservice.infrastructure.security.jwt.component;

import com.yuseogi.common.exception.CommonErrorCode;
import com.yuseogi.common.exception.CustomException;
import com.yuseogi.gatewayservice.infrastructure.cache.redis.repository.InvalidAccessTokenRedisRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

    private static final String BEARER_TYPE = "Bearer";
    private static final String AUTHORITY_KEY = "authority";
    private static final String TYPE_KEY = "type";

    public static final String TYPE_ACCESS = "access";

    private final SecretKey secretKey;

    private final InvalidAccessTokenRedisRepository invalidAccessTokenRedisRepository;

    public JwtProvider(
        @Value("${jwt.secret.key}") String secretKey,
        InvalidAccessTokenRedisRepository invalidAccessTokenRedisRepository
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.invalidAccessTokenRedisRepository = invalidAccessTokenRedisRepository;
    }

    /**
     * Jwt 토큰을 복호화
     */
    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    /**
     * JWT 토큰을 복호화하여 토큰에 들어있는 정보를 추출하여 Authentication 생성
     */
    public String getUserEmail(String jwtToken) {
        // 토큰 복호화
        Claims claims = parseClaims(jwtToken);

        return claims.getSubject();
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

            // access token 이 유효하지 않은지 확인
            if (getType(token).equals(TYPE_ACCESS) && invalidAccessTokenRedisRepository.findByAccessToken(token).isPresent()) {
                throw new CustomException(CommonErrorCode.INVALID_ACCESS_TOKEN);
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

    /**
     * Request Header 에서 토큰 정보 추출
     */
    public String resolveToken(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return null;
        }

        String bearerToken = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            try {
                return bearerToken.substring(7);
            } catch (StringIndexOutOfBoundsException e) {
                throw new CustomException(CommonErrorCode.INVALID_JWT);
            }
        }

        return null;
    }
}
