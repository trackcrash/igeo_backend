package igeo.site.Provider;

import igeo.site.Auth.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private int expiration;
    private static final int refreshExpiration = 1000 * 60 * 60 * 24 * 7;
    private final CustomUserDetailsService userDetailsService;
    public String generateToken(Authentication authentication)
    {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("authorities", authentication.getAuthorities());
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + expiration); // 토큰 만료 시간 설정
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("authorities", authentication.getAuthorities());
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + refreshExpiration); // 토큰 만료 시간 설정
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }
    // 토큰에서 정보 추출 (일반적인 클레임)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 토큰 유효성 검사
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    // 토큰에서 사용자 이름 추출
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);

        // "authorities" 배열을 가져오기 위해 클레임에서 꺼내기
        List<Map<String, Object>> authorities = (List<Map<String, Object>>) claims.get("authorities");

        if (authorities != null && !authorities.isEmpty()) {
            Map<String, Object> attributes = (Map<String, Object>) authorities.get(0).get("attributes");
            if (attributes != null) {
                String email = (String) attributes.get("email");
                if (email != null) {
                    return email;
                }
            }
        }

        throw new RuntimeException("Email not found in token");
    }

    // 토큰 만료 여부 확인
    private Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // 토큰에서 모든 클레임 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(extractUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
