package com.example.devjobs.user.service.implement;

import com.example.devjobs.user.dto.request.auth.UpdateUserRequestDto;
import com.example.devjobs.user.dto.response.auth.UserResponseDto;
import com.example.devjobs.user.entity.User;
import com.example.devjobs.user.repository.UserRepository;
import com.example.devjobs.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findUserById(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public UserResponseDto getMyPageInfo(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // UserResponseDto를 생성하여 반환
        return new UserResponseDto(user);
    }

    @Override
    public void updateUserInfo(String userId, UpdateUserRequestDto dto) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 사용자 정보 업데이트
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword()); // 비밀번호 암호화 필요
        }

        // 기업 회원 정보 업데이트
        if ("company".equals(user.getType())) {
            user.setCompanyCode(dto.getCompanyCode());
            user.setCompanyType(dto.getCompanyType());
            user.setCompanyName(dto.getCompanyName());
            user.setCeoName(dto.getCeoName());
            user.setCompanyAddress(dto.getCompanyAddress());
        }

        userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        userRepository.delete(user);
    }
}
