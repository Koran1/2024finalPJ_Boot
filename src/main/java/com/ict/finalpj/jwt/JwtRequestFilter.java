package com.ict.finalpj.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ict.finalpj.common.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    // 요청 헤더에서 Authorization 값 확인
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
        System.out.println("JwtRequestFilter here");
        final String requestTokenHeader = request.getHeader("Authorization");
        String userIdx = null;
        String jwtToken = null;

        // 값이 없거나 'Bearer '로 시작하지 않으면 로그를 남기고 다음 필터로 전달한다
        // 토큰이 있으면 Authorization 안에 'Bearer ' 로 시작 (규정임)
        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
            // 토큰 추출
            jwtToken = requestTokenHeader.substring(7);
            log.info("JwtFilter - 추출메서드");
            try {
                // 토큰 검증 by JwtUtil.java
                userIdx = jwtUtil.extractUserName(jwtToken);
                log.info("JwtFilter - userIdx :"+ userIdx);

            } catch (Exception e) {
                System.out.println("JWT 오류");
                logger.warn("JWT Error!");
            }
        }else{
            System.out.println("JWT 없음");
            // logger.warn("JWT does not exist!");
        }

        // username 추출 성공 시, 현재 Security Context에 인증정보가 없는 경우
        if(userIdx != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // 사용자 이름을 가지고 현재 DB에 있는지 검사
            // UserVO 에서 m_id와 m_pw 를 Username 과 Password 로 getter 할 수 있도록 설정했음
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userIdx);
            log.info("JwtRequest - userDetails : "+userDetails.getUsername()+"\n");
            log.info("JwtRequest - userDetails : "+userDetails.getPassword()+"\n");

            if(jwtUtil.validateToken(jwtToken, userDetails)){
                // 인증 객체 생성
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 인증 객체 세부 정보 설정
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Spring Security Context 에 저장
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
        }
        filterChain.doFilter(request, response);
    }
    
}

