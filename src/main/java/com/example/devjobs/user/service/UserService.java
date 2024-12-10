package com.example.devjobs.user.service;

import com.example.devjobs.user.dto.request.auth.UpdateUserRequestDto;
import com.example.devjobs.user.dto.response.auth.UserResponseDto;
import com.example.devjobs.user.entity.User;

public interface UserService {

    UserResponseDto getMyPageInfo(String userId);

    void updateUserInfo(String userId, UpdateUserRequestDto dto);

    void deleteUser(String userId);

    User findUserById(String userId);

}
