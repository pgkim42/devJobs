package com.example.devjobs.resume.service;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.resume.dto.CertificationsDTO;
import com.example.devjobs.resume.dto.LanguagesSkillsDTO;
import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.entity.Resume;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService {

    int register(ResumeDTO dto, MultipartFile resumeFolder);

//    List<ResumeDTO> getList();
//
//    ResumeDTO read(Integer resumeCode);
//
//    void modify(ResumeDTO dto);

    // 자격증, 언어능력 >> List에서 Json으로 변환 필요 (DB에 JSON 형태로로 저장) 직렬화?
    default Resume dtoToEntity(ResumeDTO dto) {
        Resume entity = Resume.builder()
                .resumeCd(dto.getResumeCd())                     // 이력서 코드
                .workExperience(dto.getWorkExperience())         // 경력
                .education(dto.getEducation())                   // 학력
                .certifications(convertListToJson(dto.getCertifications()))  // 자격증 JSON으로 변환
                .skill(dto.getSkill())                           // 스킬
                .languageSkills(convertListToJson(dto.getLanguageSkills()))  // 언어능력 JSON으로 변환
                .uploadFileName(dto.getUploadFileName())         // 이력서 파일
                .build();

        return entity;
    }

    // 자격증, 언어능력 >> JSON에서 List로(DTO에 List형태로 되어있기에)
    default ResumeDTO entityToDTO(Resume entity) {
        ResumeDTO dto = ResumeDTO.builder()
                .resumeCd(entity.getResumeCd())                      // 이력서 코드
                .workExperience(entity.getWorkExperience())          // 경력
                .education(entity.getEducation())                    // 학력
                .certifications(convertJsonToList(entity.getCertifications(), CertificationsDTO.class)) // 자격증 JSON -> List
                .skill(entity.getSkill())                            // 스킬
                .languageSkills(convertJsonToList(entity.getLanguageSkills(), LanguagesSkillsDTO.class)) // 언어 능력 JSON -> List
                .uploadFileName(entity.getUploadFileName())          // 이력서 파일
                .createdDate(entity.getCreatedDate())                // 생성일
                .updatedDate(entity.getUpdatedDate())                // 수정일
                .build();

        return dto;
    }

    // List -> JSON 변환
    default String convertListToJson(List<?> list) {
        try {
            if (list != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(list);  // List를 JSON 문자열로 변환
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("List to JSON 변환 실패", e);
        }
        return null;
    }

    // JSON -> List 변환
    default <T> List<T> convertJsonToList(String json, Class<T> clazz) {
        try {
            if (json != null && !json.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)); // JSON 문자열을 List로 변환
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON to List 변환 실패", e);
        }
        return null;
    }

}
