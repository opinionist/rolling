package example.rollingpager.global.config;

import example.rollingpager.global.jwt.CustomAccessDeniedHandler;
import example.rollingpager.global.jwt.CustomAuthenticationEntryPoint;
import example.rollingpager.global.jwt.JwtAuthenticationFilter;
import example.rollingpager.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않음

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()) // 모든 요청을 허용 -> 나중에 변경할 거
                .logout(logout -> logout
                        .logoutUrl("/sign-out") // 로그아웃 URL 설정
                        .logoutSuccessUrl(null) // 로그아웃 성공 시 리다이렉트 URL 설정
                        .invalidateHttpSession(true) // 세션 무효화
                        .logoutSuccessHandler((request, response, authentication) -> {
                            logger.info("SecurityConfig : logoutSuccessHandler() - 로그아웃 요청이 들어왔습니다.");
                            // access 쿠키 삭제
                            logger.info("SecurityConfig : logoutSuccessHandler() - access 쿠키를 삭제합니다.");
                            Cookie accessCookie = new Cookie("access", null);
                            accessCookie.setMaxAge(0);
                            accessCookie.setPath("/");
                            accessCookie.setHttpOnly(true);
                            response.addCookie(accessCookie);

                            // refresh 쿠키 삭제
                            logger.info("SecurityConfig : logoutSuccessHandler() - refresh 쿠키를 삭제합니다.");
                            Cookie refreshCookie = new Cookie("refresh", null);
                            refreshCookie.setMaxAge(0);
                            refreshCookie.setPath("/");
                            refreshCookie.setHttpOnly(true);
                            response.addCookie(refreshCookie);

                            // 응답 코드 및 메시지 설정
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\": \"로그아웃 성공\"}");
                        })
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                )
                .formLogin(form -> form.disable()) // 폼 로그인 비활성화 -> 나중에 변경할 거

                .httpBasic(http -> http.disable()) // HTTP Basic 인증 비활성화 -> 나중에 변경할 거

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())// 인증되지 않은 경우
                        .accessDeniedHandler(new CustomAccessDeniedHandler())// 권한이 없는 경우
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.addAllowedOrigin("http://localhost:3000"); // 허용할 프론트 도메인
                    corsConfig.addAllowedMethod("*");
                    corsConfig.addAllowedHeader("*");
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
        ;

        return httpSecurity.build();
    }
}
