package com.example.devjobs.resume.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeDTO {

    private Integer resumeCd;         // 이력서 코드

    private String workExperience;    // 경력

    private String education;         // 학력

    private String certifications;    // 자격증 (JSON 형태로 저장)

    private String skill;             // 스킬

    private String languageSkills;    // 언어 능력 (JSON 형태로 저장)

    private LocalDateTime createdDate; // 생성일

    private LocalDateTime updatedDate; // 수정일

    // 파일 첨부 관련..
    private MultipartFile uploadFile;   // 이력서 파일 (파일명 또는 경로)

    private String uploadFileName;



}
