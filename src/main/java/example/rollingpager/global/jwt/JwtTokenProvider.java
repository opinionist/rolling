package example.rollingpager.global.jwt;

import example.rollingpager.global.entity.UserDetails;
import example.rollingpager.global.service.UserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;
    Key key;
    private final long tokenValidMillisecond = 1000L * 60 * 60; // 1시간
    private final long refreshTokenValidMillisecond = 1000L * 60 * 60 * 24 * 7; // 7일

    @PostConstruct
    protected void init() {
        logger.info("JwtTokenProvider : init() 실행 - secretKey 초기화 시작");
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        logger.info("JwtTokenProvider : init() 실행 - secretKey 초기화 완료");
    }

    public String createRefresh(String nickName, List<String> roles) {
        logger.info("JwtTokenProvider : createRefresh() 실행 - 리프레쉬 토큰 생성 시작");

        Claims claims = Jwts.claims().setSubject(nickName);
        claims.put("roles", roles);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        logger.info("JwtTokenProvider : createRefresh() 실행 - 리프레쉬 토큰 생성 완료");

        return token;
    }

    public String createAccess(String nickName, List<String> roles) {
        logger.info("JwtTokenProvider : createAccess() 실행 - 액세스 토큰 생성 시작");

        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(nickName);
        claims.put("roles", roles);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        logger.info("JwtTokenProvider : createAccess() 실행 - 액세스 토큰 생성 완료");
        return token;
    }
    public String getNickname(String token) {
        logger.info("JwtTokenProvider : getUsername() 실행 - 토큰으로부터 유저 정보 가져오기 시작");
        String info = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        logger.info("JwtTokenProvider : getUsername() 실행 - 토큰으로부터 유저 정보 가져오기 완료");
        return info;
    }

    public Authentication getAuthentication(String token) {
        logger.info("JwtTokenProvider : getAuthentication() 실행 - 토큰으로부터 인증 정보 가져오기 시작");
        UserDetails userDetails = userDetailsService.loadUserByNickname(this.getNickname(token));

        logger.info("JwtTokenProvider : getAuthentication() 실행 - 토큰으로부터 인증 정보 가져오기 완료");
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public List<String> getRoles(String token) {
        logger.info("JwtTokenProvider : getRoles() 실행 - 토큰으로부터 권한 정보 가져오기 시작");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        List<String> roles = claims.get("roles", List.class);
        logger.info("JwtTokenProvider : getRoles() 실행 - 토큰으로부터 권한 정보 가져오기 완료");
        return roles;
    }

    public String resolveToken(HttpServletRequest request){
        logger.info("JwtTokenProvider : resolveToken() 실행 - HTTP 헤더에서 토큰 값 추출");
        return request.getHeader("Authorization") != null ? request.getHeader("Authorization").substring(7) : null;
    }

    public boolean validateToken(String token){
        logger.info("JwtTokenProvider : validateToken() 실행 - 토큰 유효 체크 시작");
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            logger.error("JwtTokenProvider : validateToken() 실행 - 토큰 유효 체크 실패: {}", e.getMessage());
            return false;
        }
    }
}
