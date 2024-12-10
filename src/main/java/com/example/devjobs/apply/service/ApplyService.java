package com.example.devjobs.apply.service;

import com.example.devjobs.apply.dto.ApplyDTO;
import com.example.devjobs.apply.entity.Apply;
import com.example.devjobs.apply.entity.ApplyStatusValidator;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.resume.entity.Resume;

import java.util.List;

public interface ApplyService {

    int register(ApplyDTO dto);

    List<ApplyDTO> getList();

    ApplyDTO read(int code);

    void modify(ApplyDTO dto);

    void remove(Integer dto);

    default ApplyDTO entityToDTO(Apply entity) {

            ApplyDTO dto = ApplyDTO.builder()
                .applyCode(entity.getApplyCode()) // 지원코드
                .submissionDate(entity.getCreateDate()) // 등록일
                .updateDate(entity.getUpdateDate()) // 수정일
                .build();

            // jobCode 설정
            if(entity.getJobCode() != null) {
                dto.setJobCode(entity.getJobCode().getJobCode());
            }
            
            // resumeCode 설정
            if(entity.getResumeCode() != null) {
                dto.setResumeCode(entity.getResumeCode().getResumeCode());
            }

            // applyStatus 설정
            if (entity.getApplyStatus() != null) {
                dto.setApplyStatus(entity.getApplyStatus());
            }

            return dto;

    }

    default Apply dtoToEntity(ApplyDTO dto) {

        Apply entity = Apply.builder()
                .applyCode(dto.getApplyCode()) // 지원코드
                .build();

        if(dto.getJobCode() != null) {
            JobPosting jobPosting = new JobPosting();
            jobPosting.setJobCode(dto.getJobCode());
            entity.setJobCode(jobPosting);
        }

        if(dto.getResumeCode() != null) {
            Resume resume = new Resume();
            resume.setResumeCode(dto.getResumeCode());
            entity.setResumeCode(resume);
        }

        // applyStatus 설정
        if (dto.getApplyStatus() != null) {
            // 입력 값 검증 (유효한 상태값만 허용)
            if (!ApplyStatusValidator.isValid(dto.getApplyStatus())) {
                throw new IllegalArgumentException("Invalid apply status: " + dto.getApplyStatus());
            }
            entity.setApplyStatus(dto.getApplyStatus());
        }

        return entity;
    }
}
