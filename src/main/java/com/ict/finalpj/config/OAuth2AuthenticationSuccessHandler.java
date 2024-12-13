package com.ict.finalpj.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.ict.finalpj.common.util.JwtUtil;
import com.ict.finalpj.domain.auth.service.MyUserDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
    private final JwtUtil jwtUtil;
    private final MyUserDetailService userDetailService;

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil, MyUserDetailService userDetailService){
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }
    
    // 실제로 성공한 다음에 Client로 redirect 해주는 method
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        try {
            log.info("OAuth2AuthenticationSuccessHandler operating " );
            // OAuth2User 타입인지 확인
            if(authentication.getPrincipal() instanceof OAuth2User){
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                String uri = request.getRequestURI();

                String provider = "";
                if(uri.contains("kakao")){
                    provider = "kakao";
                } else if(uri.contains("naver")){
                    provider = "naver";
                } else{
                    provider = "unknown";
                }
                log.info("OAuth2 user : " + oAuth2User);
                log.info("provider : " + provider);

                // 성공 후 토큰을 만들어서 client에게 redirect 한다
                // UserDetails userDetails = userDetailService.loadUserByOAuth2User(oAuth2User, provider);

                String token = jwtUtil.generateToken(oAuth2User.getAttribute("id").toString());
                
                String redirectUrl = String.format(
                    "http://localhost:3000/login?token=%s&username=%s&name=%s&email=%s",
                    URLEncoder.encode(token, StandardCharsets.UTF_8),
                    URLEncoder.encode(oAuth2User.getAttribute("id").toString(), StandardCharsets.UTF_8),
                    URLEncoder.encode(oAuth2User.getAttribute("name"), StandardCharsets.UTF_8),
                    URLEncoder.encode(oAuth2User.getAttribute("email"), StandardCharsets.UTF_8)
                    );

                // client에 토큰, 이름, email 등 정보를 가지고 간다
                response.sendRedirect(redirectUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/login?error");
        }
    }

}
