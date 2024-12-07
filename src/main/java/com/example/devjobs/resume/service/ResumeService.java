package com.example.devjobs.resume.service;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.entity.Resume;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {

    int register(ResumeDTO dto, MultipartFile resumeFolder);

    default Resume dtoToEntity(ResumeDTO dto) {
        Resume entity = Resume.builder()
                .resumeCd(dto.getResumeCd())                     // 이력서 코드
                .workExperience(dto.getWorkExperience())         // 경력
                .education(dto.getEducation())                   // 학력
                .certifications(dto.getCertifications())         // 자격증
                .skill(dto.getSkill())                           // 스킬
                .languageSkills(dto.getLanguageSkills())         // 언어능력
                .uploadFileName(dto.getUploadFileName())         // 이력서 파일
                .build();

        return entity;
    }

    default ResumeDTO entityToDTO(Resume entity) {
        ResumeDTO dto = ResumeDTO.builder()
                .resumeCd(entity.getResumeCd())                      // 이력서 코드
                .workExperience(entity.getWorkExperience())          // 경력
                .education(entity.getEducation())                    // 학력
                .certifications(entity.getCertifications())          // 자격증
                .skill(entity.getSkill())                            // 스킬
                .languageSkills(entity.getLanguageSkills())          // 언어능력
                .uploadFileName(entity.getUploadFileName())          // 이력서 파일
                .createdDate(entity.getCreatedDate())                // 생성일
                .updatedDate(entity.getUpdatedDate())                // 수정일
                .build();

        return dto;
    }

}
