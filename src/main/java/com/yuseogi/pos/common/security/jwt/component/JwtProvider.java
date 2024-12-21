package com.yuseogi.pos.common.security.jwt.component;

import com.yuseogi.pos.common.cache.redis.repository.InvalidAccessTokenRedisRepository;
import com.yuseogi.pos.common.exception.CommonErrorCode;
import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.common.security.ExpireTime;
import com.yuseogi.pos.common.security.dto.TokenInfoResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";
    private static final String AUTHORITY_KEY = "authority";
    private static final String TYPE_KEY = "type";

    public static final String TYPE_ACCESS = "access";
    public static final String TYPE_REFRESH = "refresh";

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
     * email, authorityList 을 가지고 AccessToken, RefreshToken 을 생성
     */
    public TokenInfoResponseDto generateToken(String email, List<String> authorityList) {

        Date now = new Date();

        // Access JWT Token 생성
        String accessToken = generateJWT(email, authorityList, TYPE_ACCESS, now, ExpireTime.ACCESS_TOKEN.getMillSecond());

        // Refresh JWT Token 생성
        String refreshToken = generateJWT(email, authorityList, TYPE_REFRESH, now, ExpireTime.REFRESH_TOKEN.getMillSecond());

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
    private String generateJWT(String subject, List<String> authorityList, String type, Date issuedAt, long expireTime) {
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
     * JWT 토큰을 복호화하여 토큰에 들어있는 정보를 추출하여 Authentication 생성
     */
    public Authentication getAuthentication(String jwtToken) {
        // 토큰 복호화
        Claims claims = parseClaims(jwtToken);

        // 클레임에서 권한 정보 가져오기
        List<String> authorityList = claims.get(AUTHORITY_KEY, List.class);
        List<GrantedAuthority> authorities = authorityList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
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
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            try {
                return bearerToken.substring(7);
            } catch (StringIndexOutOfBoundsException e) {
                throw e;
            }
        }

        return null;
    }
}
