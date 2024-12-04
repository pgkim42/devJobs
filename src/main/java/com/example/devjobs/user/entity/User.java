package com.example.devjobs.user.entity;

import com.example.devjobs.common.BaseEntity;
import com.example.devjobs.user.dto.request.auth.SignUpRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @Column(name = "user_code")
    private String userCode; // 소셜 회원가입 시 사용되는 고유ID

    @Column(unique = true)
    private String userId;   // 일반 회원가입 시 사용되는 고유ID

    private String password;

    private String email;

    private String type;

    private String role;

    public User(SignUpRequestDto dto){
        this.userId = dto.getId();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        this.type = "dev";
        this.role = "ROLE_USER";
        // 일반 회원가입 시 userCode는 userId와 동일하게 설정
        this.userCode = "dev_" + dto.getId();
    }

    public User(String userCode, String email, String type){
        this.userCode = userCode;
        this.email = email;
        this.type = type;
        this.role = "ROLE_USER";
    }

    public User(String userCode, String type){
        this.userCode = userCode;
        this.type = type;
        this.role = "ROLE_USER";
    }
}