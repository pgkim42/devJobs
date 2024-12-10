package com.example.devjobs.resume.service;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.resume.dto.CertificationsDTO;
import com.example.devjobs.resume.dto.LanguagesSkillsDTO;
import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface ResumeService {

    int register(ResumeDTO dto, MultipartFile resumeFolder);

    List<ResumeDTO> getList();

    ResumeDTO read(Integer resumeCode);

    // 이력서 수정 메소드 추가
    void modify(Integer resumeCd, String workExperience, String education, String skill,
                String certifications, String languageSkills, MultipartFile resumeFile,
                LocalDateTime lastUpdated, String jobCategory);

    void remove(Integer resumeCode);

    // 자격증, 언어능력 >> List에서 Json으로 변환 필요 (DB에 JSON 형태로로 저장) 직렬화?
    default Resume dtoToEntity(ResumeDTO dto) {
        Resume entity = Resume.builder()
                .resumeCd(dto.getResumeCode())                     // 이력서 코드
                .workExperience(dto.getWorkExperience())         // 경력
                .education(dto.getEducation())                   // 학력
                .certifications(JsonUtil.convertListToJson(dto.getCertifications()))  // 자격증 JSON으로 변환
                .skill(dto.getSkill())                           // 스킬
                .jobCategory(dto.getJobCategory())              // 직무
                .languageSkills(JsonUtil.convertListToJson(dto.getLanguageSkills()))  // 언어능력 JSON으로 변환
                .uploadFileName(dto.getUploadFileName())         // 이력서 파일
                .build();

        return entity;
    }

    // 자격증, 언어능력 >> JSON에서 List로(DTO에 List형태로 되어있기에)
    default ResumeDTO entityToDTO(Resume entity) {
        ResumeDTO dto = ResumeDTO.builder()
                .resumeCode(entity.getResumeCd())                      // 이력서 코드
                .workExperience(entity.getWorkExperience())          // 경력
                .education(entity.getEducation())                    // 학력
                .certifications(JsonUtil.convertJsonToList(entity.getCertifications(), CertificationsDTO.class)) // 자격증 JSON -> List
                .skill(entity.getSkill())                            // 스킬
                .jobCategory(entity.getJobCategory())               // 직무
                .languageSkills(JsonUtil.convertJsonToList(entity.getLanguageSkills(), LanguagesSkillsDTO.class)) // 언어 능력 JSON -> List
                .uploadFileName(entity.getUploadFileName())          // 이력서 파일
                .createdDate(entity.getCreatedDate())                // 생성일
                .updatedDate(entity.getUpdatedDate())                // 수정일
                .build();

        return dto;
    }

}
