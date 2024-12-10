package com.example.devjobs.companyprofile.dto;

import lombok.*;

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

}
