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

        try {
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        User user = null;
        String userCode = null;
        String nickname = null;
        String email = null;

        if(oauthClientName.equals("kakao")){
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            if (kakaoAccount != null) {
                email = (String) kakaoAccount.get("email");

                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile != null) {
                    nickname = (String) profile.get("nickname");
                }
            }

            userCode = "kakao_" + oAuth2User.getAttributes().get("id");
            user = new User(userCode, email, nickname,"kakao");
        }

        if(oauthClientName.equals("naver")){
            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
            userCode = "naver_" + responseMap.get("id").substring(0, 14);
            email = responseMap.get("email");
            nickname = responseMap.get("name");
            user = new User(userCode, email, nickname,"naver");
        }

        userRepository.save(user); // User ID는 이미 수동으로 설정됨

        return new CustomOAuth2User(userCode);
    }
}