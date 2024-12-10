package com.example.devjobs.user.filter;

import com.example.devjobs.user.entity.User;
import com.example.devjobs.user.provider.JwtProvider;
import com.example.devjobs.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String path = request.getRequestURI();
            System.out.println("Incoming Request Path: " + path);

            if (path.startsWith("/api/v1/auth")) {
                System.out.println("Path does not require authentication. Skipping filter.");
                filterChain.doFilter(request, response);
                return;
            }

            // 요청에서 Bearer Token 추출
            String token = parseBearerToken(request);
            System.out.println("Authorization Header: " + request.getHeader("Authorization"));
            System.out.println("Extracted Token: " + token);

            if (token == null) {
                System.out.println("No token found. Proceeding without authentication.");
                filterChain.doFilter(request, response);
                return;
            }

            // 토큰 검증 후 userId 추출
            String userId = jwtProvider.validate(token);
            System.out.println("Validated User ID: " + userId);

            if (userId == null) {
                System.out.println("Token validation failed. Proceeding without authentication.");
                filterChain.doFilter(request, response);
                return;
            }

            // User 객체를 DB에서 찾기
            User user = userRepository.findByUserId(userId);
            System.out.println("Fetched User from DB: " + user);

            if (user == null) {
                System.out.println("No user found for the given token. Proceeding without authentication.");
                filterChain.doFilter(request, response);
                return;
            }

            // 역할에 "ROLE_" 접두사를 추가
            String role = user.getRole();
            System.out.println("Decoded Token User ID: " + userId);
            System.out.println("Decoded Token Role: " + role);
            System.out.println("Expected Role: ROLE_COMPANY");

            // 권한 설정
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
            AbstractAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 인증 설정
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

            System.out.println("Authentication successfully set for user: " + userId);

        } catch (Exception exception) {
            System.out.println("Exception occurred in JwtAuthenticationFilter: " + exception.getMessage());
            exception.printStackTrace();
        }

        // 요청을 다음 필터로 전달
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authorization);

        if (!StringUtils.hasText(authorization)) {
            return null;
        }

        if (!authorization.startsWith("Bearer ")) {
            return null;
        }

        return authorization.substring(7); // Bearer 이후의 토큰 값 반환
    }
}