package com.example.devjobs.user.controller;

import com.example.devjobs.user.dto.request.auth.UpdateUserRequestDto;
import com.example.devjobs.user.service.UserService;
import com.example.devjobs.user.service.implement.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/myPage")
    public ResponseEntity<String> updateUserInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdateUserRequestDto dto
    ) {
        userService.updateUserInfo(userDetails.getUsername(), dto);
        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }

    @DeleteMapping("/myPage")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getUsername());
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}

