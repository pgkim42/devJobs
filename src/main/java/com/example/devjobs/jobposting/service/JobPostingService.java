package com.example.devjobs.jobposting.service;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.entity.PostingStatus;

public interface JobPostingService  {

    int register(JobPostingDTO dto);

    default JobPosting dtoToEntity(JobPostingDTO dto) {

        return JobPosting.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .recruitJob(dto.getRecruitJob())
                .recruitField(dto.getRecruitField())
                .salary(dto.getSalary())
                .postingDeadline(dto.getPostingDeadline())
                .postingStatus(PostingStatus.valueOf(dto.getPostingStatus()))
                .workExperience(dto.getWorkExprerience())
                .tag(dto.getTag())
                .jobCategory(dto.getJobCategory())
                .imgPath(dto.getImgPath()) // 파일 이름
                .build();

    }

    default JobPostingDTO entityToDto(JobPosting entity) {
        return JobPostingDTO.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .recruitJob(entity.getRecruitJob())
                .recruitField(entity.getRecruitField())
                .salary(entity.getSalary())
                .postingDeadline(entity.getPostingDeadline())
                .postingStatus(entity.getPostingStatus().name()) // Enum -> String 변환
                .workExprerience(entity.getWorkExperience())
                .tag(entity.getTag())
                .jobCategory(entity.getJobCategory())
                .imgPath(entity.getImgPath()) // 파일 이름
                .build();
    }

}
