package com.example.devjobs.user.dto.request.auth;

import lombok.Data;

@Data
public class RemoveUserRequest {
    private String userId;
    private String password;
}