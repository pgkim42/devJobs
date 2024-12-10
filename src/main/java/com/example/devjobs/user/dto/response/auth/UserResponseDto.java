package com.example.devjobs.user.dto.response.auth;

import com.example.devjobs.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String name;
    private String email;
    private String userId;
    private String type;

    // 기업 회원 관련 데이터
    private String companyCode;
    private String companyType;
    private String companyName;
    private String ceoName;
    private String companyAddress;

    public UserResponseDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.userId = user.getUserId();
        this.type = user.getType();

        // 기업 회원일 경우 추가 데이터 설정
        if ("company".equals(user.getType())) {
            this.companyCode = user.getCompanyCode();
            this.companyType = user.getCompanyType();
            this.companyName = user.getCompanyName();
            this.ceoName = user.getCeoName();
            this.companyAddress = user.getCompanyAddress();
        }
    }
}
