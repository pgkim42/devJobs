package com.example.devjobs.companyprofile.entity;

import com.example.devjobs.common.file.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "company_profile_cd")
    private Integer companyProfileCd;  // 기업프로필코드

    @Column(name = "company_name", nullable = false)
    private String companyName;  // 기업 이름

    // 긴 텍스트 쓸때 columnDefinition = "TEXT" 사용
    @Column(name = "company_content", columnDefinition = "TEXT") 
    private String companyContent;  // 기업 내용

    @Column(name = "industry")
    private String industry;  // 업종

    @Column(name = "website_url")
    private String websiteUrl;  // 기업 사이트 URL

    @Column(name = "logo_url")
    private String logoUrl;  // 로고 URL

}