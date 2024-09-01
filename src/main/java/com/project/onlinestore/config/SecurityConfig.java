package com.project.onlinestore.config;

import com.project.onlinestore.jwt.filter.JwtAccessDeniedHandler;
import com.project.onlinestore.jwt.filter.JwtAuthenticationEntryPoint;
import com.project.onlinestore.jwt.filter.JwtTokenFilter;
import com.project.onlinestore.jwt.util.JwtTokenUtils;
import com.project.onlinestore.jwt.util.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${jwt.secret-key}")
    private String key;

    private final JwtTokenUtils jwtTokenUtils;

    /*
    CustomUserDetails -> CustomUserDetailsService ->
    JwtTokenProvider -> JwtTokenFilter -> WebSecurityConfig
    */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/*/user/*/join", "/api/*/user/login", "/api/*/user/token/refresh", "/api/*/item/test", "/*").permitAll()
                                .requestMatchers( "/api/*/item/search", "/api/*/item/detail/*").permitAll()
                                .requestMatchers("/api/*/review/view/*").permitAll()
                                .requestMatchers("/api/*/user/logout", "/api/*/user/cart/*", "/api/*/user/cart/*/*", "/api/*/user/cart/*/*/*", "/api/*/user/setting/*", "/api/*/user/setting/*/*", "/api/*/user/setting/*/*/*").authenticated()
                                .requestMatchers("/api/*/item/**", "/api/*/like/*/*").authenticated()
                                .requestMatchers("/api/*/review/*", "/api/*/review/*/*").authenticated()
                                .requestMatchers("/api/*/order/*", "/api/*/order/*/*", "/api/*/order/*/*/*", "/api/*/order/*/*/*/*").authenticated()
                                .requestMatchers("/api/movie", "/demo/api/data").permitAll()
                        )   // TODO: ROLE로 나눠보기
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtTokenFilter(key, userDetailsService, jwtTokenUtils), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(jwtAccessDeniedHandler)    // 인증이 실패했을 때 실행
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)) // 인가가 실패했을 때 실행
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
