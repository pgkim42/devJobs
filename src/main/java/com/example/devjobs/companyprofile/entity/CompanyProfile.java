package com.example.devjobs.companyprofile.entity;

import com.example.devjobs.common.BaseEntity;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "company_profile")
public class CompanyProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_profile_code")
    private Integer companyProfileCode;  // 기업프로필코드

    @Column(name = "company_name", nullable = false)
    private String companyName;  // 기업 이름

    // 긴 텍스트 쓸때 columnDefinition = "TEXT" 사용
    @Column(name = "company_content", columnDefinition = "TEXT") 
    private String companyContent;  // 기업 내용

    @Column(name = "industry")
    private String industry;  // 업종

    @Column(name = "website_url")
    private String websiteUrl;  // 기업 사이트 URL

    @ManyToOne
    @JoinColumn(name = "user_code")
    User userCode;

    @Column(name = "logo_file_name", length = 255) // 로고 파일명 저장
    private String uploadFileName; // 저장된 로고 파일명

    // jobPosting이 companyProfile의 code를 참조하고 있기에 cascade로 jobposting의 게시글까지 삭제됨
    @OneToMany(mappedBy = "companyProfile", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<JobPosting> jobPostings;

}