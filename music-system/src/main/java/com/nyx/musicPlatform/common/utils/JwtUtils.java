package com.nyx.musicPlatform.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author nyx
 * @since 2025-06
 */
@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${music.jwt.secret:music-platform-secret-key-for-jwt-token-generation}")
    private String secret;

    @Value("${music.jwt.expiration:86400}")
    private Long expiration;

    @Value("${music.jwt.refresh-expiration:604800}")
    private Long refreshExpiration;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private static final String USER_ID_KEY = "userId";
    private static final String USERNAME_KEY = "username";
    private static final String AUTHORITIES_KEY = "authorities";
    private static final String TOKEN_TYPE_KEY = "tokenType";
    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    /**
     * 获取密钥
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成访问令牌
     */
    public String generateAccessToken(Long userId, String username, String authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID_KEY, userId);
        claims.put(USERNAME_KEY, username);
        claims.put(AUTHORITIES_KEY, authorities);
        claims.put(TOKEN_TYPE_KEY, ACCESS_TOKEN);
        return generateToken(claims, expiration);
    }

    /**
     * 生成刷新令牌
     */
    public String generateRefreshToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID_KEY, userId);
        claims.put(USERNAME_KEY, username);
        claims.put(TOKEN_TYPE_KEY, REFRESH_TOKEN);
        return generateToken(claims, refreshExpiration);
    }

    /**
     * 生成令牌
     */
    private String generateToken(Map<String, Object> claims, Long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 从令牌中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            Object userId = claims.get(USER_ID_KEY);
            if (userId instanceof Integer) {
                return ((Integer) userId).longValue();
            } else if (userId instanceof Long) {
                return (Long) userId;
            }
        }
        return null;
    }

    /**
     * 从令牌中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? (String) claims.get(USERNAME_KEY) : null;
    }

    /**
     * 从令牌中获取权限
     */
    public String getAuthoritiesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? (String) claims.get(AUTHORITIES_KEY) : null;
    }

    /**
     * 从令牌中获取令牌类型
     */
    public String getTokenTypeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? (String) claims.get(TOKEN_TYPE_KEY) : null;
    }

    /**
     * 从令牌中获取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getExpiration() : null;
    }

    /**
     * 从令牌中获取声明
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("解析JWT令牌失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证令牌是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT令牌已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的JWT令牌: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT令牌格式错误: {}", e.getMessage());
        } catch (SignatureException e) {
            log.warn("JWT令牌签名验证失败: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT令牌参数错误: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 检查令牌是否过期
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration != null && expiration.before(new Date());
    }

    /**
     * 检查是否为访问令牌
     */
    public boolean isAccessToken(String token) {
        String tokenType = getTokenTypeFromToken(token);
        return ACCESS_TOKEN.equals(tokenType);
    }

    /**
     * 检查是否为刷新令牌
     */
    public boolean isRefreshToken(String token) {
        String tokenType = getTokenTypeFromToken(token);
        return REFRESH_TOKEN.equals(tokenType);
    }

    /**
     * 从请求头中获取令牌
     */
    public String getTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            return authHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * 从请求中获取令牌
     */
    public String getTokenFromRequest(javax.servlet.http.HttpServletRequest request) {
        String authHeader = request.getHeader(HEADER_STRING);
        return getTokenFromHeader(authHeader);
    }

    /**
     * 获取令牌剩余有效时间（秒）
     */
    public long getTokenRemainingTime(String token) {
        Date expiration = getExpirationDateFromToken(token);
        if (expiration != null) {
            long remainingTime = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            return Math.max(remainingTime, 0);
        }
        return 0;
    }

    /**
     * 刷新令牌（基于刷新令牌生成新的访问令牌）
     */
    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken) || !isRefreshToken(refreshToken)) {
            return null;
        }
        
        Long userId = getUserIdFromToken(refreshToken);
        String username = getUsernameFromToken(refreshToken);
        
        if (userId != null && username != null) {
            // 这里可以从数据库重新获取用户权限，确保权限是最新的
            return generateAccessToken(userId, username, "ROLE_USER");
        }
        
        return null;
    }

    /**
     * 获取令牌头名称
     */
    public String getHeaderString() {
        return HEADER_STRING;
    }

    /**
     * 获取令牌前缀
     */
    public String getTokenPrefix() {
        return TOKEN_PREFIX;
    }

    /**
     * 获取访问令牌过期时间（秒）
     */
    public Long getExpiration() {
        return expiration;
    }

    /**
     * 获取刷新令牌过期时间（秒）
     */
    public Long getRefreshExpiration() {
        return refreshExpiration;
    }
}