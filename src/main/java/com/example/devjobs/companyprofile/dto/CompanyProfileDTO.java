package com.example.devjobs.companyprofile.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyProfileDTO {

    private Integer companyProfileCode;  // 기업프로필코드

    private String companyName;  // 기업 이름

    private String companyContent;  // 기업 내용

    private String industry;  // 업종

    private String websiteUrl;  // 기업 사이트 URL

    private LocalDateTime createDate;  // 작성일

    private LocalDateTime updateDate;  // 수정일

    private String userCode;

    private MultipartFile uploadFile;   // 기업로고파일

    private String uploadFileName; // 저장된 로고 파일명



}
