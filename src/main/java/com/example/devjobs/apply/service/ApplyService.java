package com.example.devjobs.apply.service;

import com.example.devjobs.apply.dto.ApplyDTO;
import com.example.devjobs.apply.entity.Apply;
import com.example.devjobs.apply.entity.ApplyStatus;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.resume.entity.Resume;

import java.util.List;

public interface ApplyService {

    int register(ApplyDTO dto);

    List<ApplyDTO> getList();

    ApplyDTO read(int cd);

    void modify(ApplyDTO dto);

    void remove(Integer dto);

    default ApplyDTO entityToDTO(Apply entity) {

            ApplyDTO dto = ApplyDTO.builder()
                .applyCode(entity.getApplyCode()) // 지원코드
                .submissionDate(entity.getSubmissionDate()) // 등록일
                .updateDate(entity.getUpdatedDate()) // 수정일
                .build();

            // jobCode 설정
            if(entity.getJobCode() != null) {
                dto.setJobCode(entity.getJobCode().getJobCode());
            }
            
            // resumeCode 설정
            if(entity.getResumeCd() != null) {
                dto.setResumeCd(entity.getResumeCd().getResumeCd());
            }

            if(entity.getApplyStatus() != null) {
                dto.setApplyStatus(entity.getApplyStatus().name());
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

        if(dto.getResumeCd() != null) {
            Resume resume = new Resume();
            resume.setResumeCd(dto.getResumeCd());
            entity.setResumeCd(resume);
        }

        if(dto.getApplyStatus() != null) {
            entity.setApplyStatus(ApplyStatus.valueOf(dto.getApplyStatus()));
        }

        return entity;
    }
}
