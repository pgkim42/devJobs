package com.example.devjobs.user.entity;

import com.example.devjobs.apply.entity.Apply;
import com.example.devjobs.common.BaseEntity;
import com.example.devjobs.companyprofile.entity.CompanyProfile;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.user.dto.request.auth.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
@Table(name = "user")
@Builder
public class User extends BaseEntity {

    @Id
    @Column(name = "user_code")
    private String userCode; // 고유ID

    @Column(unique = true)
    private String userId;   // 일반 회원 로그인 시 사용되는 ID

    private String name;

    private String password;

    @Column(unique = true)
    private String email;

    private String type;

    private String role;

    private String companyProfileCode;

    public User(SignUpRequestDto dto) {
        this.userId = dto.getUserId();
        this.name = dto.getName();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        this.type = dto.getType();
        this.role = dto.getType().equals("company") ? "ROLE_COMPANY" : "ROLE_USER";

        // userCode 설정
        if ("company".equals(dto.getType())) {
            this.userCode = "com_" + dto.getUserId();
        } else {
            this.userCode = "dev_" + dto.getUserId();
        }

        //
        if (role.equals("company")) {
            this.companyProfileCode = dto.getCompanyCode();
        }

    }


//    public User( String userCode, String userId, String email, String name, String type){
//        this.userCode = userCode;
//        this.userId = userId;
//        this.email = email;
//        this.name = name;
//        this.type = type;
//        this.role = "ROLE_USER";
//    }

    public User( String userCode, String email, String name, String type){
        this.userCode = userCode;
        this.userId = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
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

    // (cascade를 위한 @OneToMany)
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private CompanyProfile companyProfile;

    @OneToMany(mappedBy = "userCode", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Resume> resumes;

    @OneToMany(mappedBy = "userCode", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Apply> applies;

    @OneToMany(mappedBy = "userCode", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<JobPosting> jobPostings;

}