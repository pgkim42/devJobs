package com.example.devjobs.user.service;

import com.example.devjobs.user.dto.request.auth.UpdateUserRequestDto;
import com.example.devjobs.user.dto.response.auth.UserResponseDto;
import com.example.devjobs.user.entity.User;

public interface UserService {

    void deleteUser(String userId); // 사용자 삭제

    void deleteUserByCode(String userCode); // 사용자 조회

    void updatePassword(String userId, String currentPassword, String newPassword); // 비밀번호 변경

    boolean checkUserPassword(String userId, String password);

    boolean removeFromSocialPlatform(String name); // 소셜 연동 해제

}

