package example.rollingpager.global.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CookieUtil {
    private final Logger logger = LoggerFactory.getLogger(CookieUtil.class);

    public void addJwtCookie(HttpServletResponse response, String accessToken, String refreshToken) {
        logger.info("CookieUtil : addJwtCookie() 실행 - jwt를 response에 쿠키로 저장합니다.");
        Cookie access = new Cookie("access", accessToken);
        access.setPath("/");
        access.setHttpOnly(true);
        access.setSecure(true);
        access.setMaxAge(24 * 60 * 60);
        response.addCookie(access);
        logger.info("CookieUtil : addJwtCookie() 실행 - accessToken: {}", accessToken);

        Cookie refresh = new Cookie("refresh", refreshToken);
        refresh.setPath("/");
        refresh.setHttpOnly(true);
        refresh.setSecure(true);
        refresh.setMaxAge(7 * 24 * 60 * 60); // 7 days
        response.addCookie(refresh);
        logger.info("CookieUtil : addJwtCookie() 실행 - refreshToken: {}", refreshToken);
    }

    public String getJwtCookie(HttpServletRequest request){
        if(request.getCookies() != null){
            logger.info("CookieUtil : getJwtCookie() 실행 - ");
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("access")){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
