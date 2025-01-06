package com.ict.finalpj.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ict.finalpj.common.util.JwtUtil;
import com.ict.finalpj.domain.user.service.MyUserDetailService;
import com.ict.finalpj.jwt.JwtRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final JwtUtil jwtUtil;
    private final MyUserDetailService userDetailService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, JwtUtil jwtUtil, MyUserDetailService userDetailService) {
        log.info("Security Config 생성\n");
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }

    // 서버에 들어오는 모든 요청은 SecurityFilterChain을 거친다.
    // jwtRequestFilter 가 먼저 실행된다
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Security Filter Chain 호출 \n");
        http   
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())

            // 요청별 권한 설정 = Interceptor 기능
            .authorizeHttpRequests(authorize -> authorize
                // 특정 URL 에 인증없이 허용
                .requestMatchers("/upload/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/api/**").permitAll()

                // 나머지는 인증 필요-
                .anyRequest().authenticated())

            // oauth2Login 설정
            .oauth2Login(oauth2 -> oauth2
                // 로그인 성공 시 호출
                .successHandler(oAuth2AuthenticationSuccessHandler())
                // 인증된 사용자에 대한 정보를 제공하는 api endpoint (사용자 정보를 가져오는 역할)
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService()))
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 origin 설정하기
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // config.setAllowedOriginPatterns(Arrays.asList("*"));

        // 허용할 http 메서드 설정
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 허용할 header 설정
        // config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowedHeaders(Arrays.asList("*"));

        // 인증정보 허용
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(){
        return new OAuth2AuthenticationSuccessHandler(jwtUtil, userDetailService);
    }

    @Bean
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(){
        return new CustomerOAuth2UserService();
    }
}

