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
import lombok.Setter;

@Getter
@Setter
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

    private String name;

    private String password;

    @Column(unique = true)
    private String email;

    private String type;

    private String role;

    @Column(name = "company_code", unique = true)
    private String companyCode; // 사업자 등록번호

    private String companyType; // 기업형태 (중소기업, 중견기업)

    private String companyName; // 회사 이름

    private String ceoName; // 대표 이름

    private String companyAddress; // 회사 주소

    public User(SignUpRequestDto dto) {
        this.userId = dto.getUserId();
        this.name = dto.getName();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        this.type = dto.getType();
        this.role = dto.getType().equals("company") ? "ROLE_COMPANY" : "ROLE_USER";
        this.userCode = "dev_" + dto.getUserId();

        // 기업 회원 정보 매핑
        if ("company".equals(dto.getType())) {
            this.companyCode = dto.getCompanyCode();
            this.companyType = dto.getCompanyType();
            this.companyName = dto.getCompanyName();
            this.ceoName = dto.getCeoName();
            this.companyAddress = dto.getCompanyAddress();
        }
    }

    public User( String userCode, String email, String name, String type){
        this.userCode = userCode;
        this.email = email;
        this.name = name;
        this.type = type;
        this.role = "ROLE_USER";
    }

    public User(String userCode, String type){
        this.userCode = userCode;
        this.type = type;
        this.role = "ROLE_USER";
    }

}