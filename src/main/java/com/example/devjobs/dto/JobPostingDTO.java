package com.example.devjobs.dto;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingDTO {

    String title; // 공고제목

    String description; // 공고내용

    String recruitJob; // 모집직무

    int recruitField; // 모집인원

    String salary; // 급여(옵션)

    LocalDateTime postingDate; // 공고시작일

    LocalDateTime postingDeadline; // 공고마감일

    String postingStatus; // 공고상태

    String workExprerience; // 경력

    String tag; // 태그
    
    String jobCategory; // 직무 카테고리

//    String writer; // 작성자(Members.. PK)
//
//    String companyProfile; // 기업프로필코드(CompanyProfile...PK)

}
