package com.example.devjobs.user.service.implement;

import com.example.devjobs.companyprofile.entity.CompanyProfile;
import com.example.devjobs.companyprofile.repository.CompanyProfileRepository;
import com.example.devjobs.user.entity.User;
import com.example.devjobs.user.repository.UserRepository;
import com.example.devjobs.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyProfileRepository companyProfileRepository;

    // 공통 메서드: 사용자 조회 및 예외 처리
    private User findUserOrThrow(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        return user;
    }

    @Override
    public void deleteUserByCode(String userCode) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // CompanyProfile 삭제
        CompanyProfile companyProfile = companyProfileRepository.findByUser(user);
        if (companyProfile != null) {
            companyProfileRepository.delete(companyProfile);
        }

        // User 삭제
        userRepository.delete(user);
    }


    @Override
    public void updatePassword(String userId, String currentPassword, String newPassword) {
        User user = findUserOrThrow(userId);

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 새 비밀번호 설정 및 저장
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkUserPassword(String userId, String password) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return passwordEncoder.matches(password, user.getPassword());
    }


}
