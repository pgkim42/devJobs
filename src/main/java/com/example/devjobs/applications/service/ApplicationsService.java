package com.example.devjobs.applications.service;

import com.example.devjobs.applications.dto.ApplicationsDTO;
import com.example.devjobs.applications.entity.Applications;
import com.example.devjobs.applications.entity.ApplicationsStatus;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.resume.entity.Resume;
import org.springframework.stereotype.Service;

public interface ApplicationsService {

    default ApplicationsDTO entityToDTO(Applications entity) {

            ApplicationsDTO dto = ApplicationsDTO.builder()
                .applicationCode(entity.getApplicationsCode()) // 지원코드
                .jobCode(entity.getJobPosting().getJobCode()) // 공고코드
                .resumeCode(entity.getResume().getResumeCd()) // 이력서코드
                .submissionDate(entity.getSubmissionDate()) // 등록일
                .updateDate(entity.getUpdatedDate()) // 수정일
                .applicationsStatus(entity.getApplicationsStatus().name()) // Enum값 -> String으로 변환
                .build();

            return dto;

    }

    default Applications dtoToEntity(ApplicationsDTO dto) {

        JobPosting jobPosting = new JobPosting();
        jobPosting.setJobCode(dto.getJobCode());

        Resume resume = new Resume();
        resume.setResumeCd(dto.getResumeCode());

        Applications entity = Applications.builder()
                .applicationsCode(dto.getApplicationCode()) // 지원코드
                .jobPosting(jobPosting) // 구인공고
                .resume(resume) // 이력서
                .applicationsStatus(ApplicationsStatus.valueOf(dto.getApplicationsStatus())) // String -> Enum으로 변환
                .build();

        return entity;
    }
}
