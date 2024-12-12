package com.example.devjobs.user.handler;

import com.example.devjobs.user.entity.CustomOAuth2User;
import com.example.devjobs.user.provider.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // CustomOAuth2User 객체에서 필요한 정보를 가져옴
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 사용자 정보 가져오기
        String userId = oAuth2User.getName(); // 사용자 ID
        String userCode = oAuth2User.getUserCode(); // userCode
        String email = oAuth2User.getEmail(); // email
        String name = oAuth2User.getName(); // name
        String type = oAuth2User.getType(); // type

        // Role 설정
        String role = type.equals("company") ? "ROLE_COMPANY" : "ROLE_USER";

        // JWT 토큰 생성
        String token = jwtProvider.create(userId, role, userCode);

        try {
            // URL 인코딩 적용
            String encodedToken = URLEncoder.encode(token, "UTF-8");
            String encodedUserCode = URLEncoder.encode(userCode, "UTF-8");
            String encodedEmail = URLEncoder.encode(email, "UTF-8");
            String encodedName = URLEncoder.encode(name, "UTF-8");
            String encodedType = URLEncoder.encode(type, "UTF-8");

            // URL 리디렉션에 인코딩된 값들을 포함
            String redirectUrl = String.format("http://localhost:3000/auth/oauth-response/%s/%s/%s/%s/%s/%s",
                    encodedToken, encodedUserCode, encodedEmail, encodedName, encodedType, 3600);

            // 리디렉션
            response.sendRedirect(redirectUrl);

        } catch (UnsupportedEncodingException e) {
            // 예외 처리
            response.sendRedirect("http://localhost:3000/error");
        }
    }
}