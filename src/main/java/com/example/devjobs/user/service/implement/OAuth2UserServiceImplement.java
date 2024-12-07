package com.example.devjobs.user.service.implement;

import com.example.devjobs.user.entity.CustomOAuth2User;
import com.example.devjobs.user.entity.User;
import com.example.devjobs.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName = request.getClientRegistration().getClientName();

        String userCode = null;  // userCode -> userId로 수정
        String name = null;
        String email = null;
        String type = null;

        // 카카오 로그인 처리
        if (oauthClientName.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");

            if (kakaoAccount != null) {
                email = (String) kakaoAccount.get("email");
                Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
                if (properties != null && properties.containsKey("nickname")) {
                    name = (String) properties.get("nickname");
                }
            }

            userCode = "kakao_" + oAuth2User.getAttributes().get("id");
            type = "kakao";  // 소셜 타입을 "kakao"로 설정
        }

        // 네이버 로그인 처리
        if (oauthClientName.equals("naver")) {
            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
            userCode = "naver_" + responseMap.get("id").substring(0, 14);
            email = responseMap.get("email");
            name = responseMap.get("name");
            type = "naver";  // 소셜 타입을 "naver"로 설정
        }

        // 유저 객체 생성 및 저장
        User user = new User(userCode, email, name, type);
        userRepository.save(user);

        // CustomOAuth2User 객체 생성하여 반환
        return new CustomOAuth2User(userCode, email, name, type);  // 이제 userId를 전달
    }
}
