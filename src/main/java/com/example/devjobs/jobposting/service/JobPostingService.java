package com.example.devjobs.jobposting.service;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.entity.PostingStatus;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface JobPostingService {

    int register(JobPostingDTO dto);

    List<JobPostingDTO> getList();

    JobPostingDTO read(int jobCode);

    void modify(JobPostingDTO dto);

    void delete(Integer jobCode);

    void modifyPartial(Integer jobCode, String title, String content, String recruitJob,
                       Integer recruitField, String salary, String postingStatus,
                       String workExperience, String tag, String jobCategory,
                       LocalDateTime postingDeadline, MultipartFile uploadFile);

    default JobPosting dtoToEntity(JobPostingDTO dto) {
        // PostingStatus 값이 "모집중"이면 OPEN, "마감"이면 CLOSE로 설정
        // 변환 로직: dtoToEntity에서 "모집중"과 "마감"을
        // PostingStatus.OPEN, PostingStatus.CLOSE로 변환하고,
        // entityToDto에서 enum을 "모집중", "마감"으로 변환하여 반환
//        PostingStatus postingStatus = PostingStatus.valueOf(dto.getPostingStatus());  // Enum 변환

        // JobPosting 객체를 생성
        JobPosting jobPosting = JobPosting.builder()
                .jobCode(dto.getJobCode())
                .title(dto.getTitle())
                .content(dto.getContent())
                .recruitJob(dto.getRecruitJob())
                .recruitField(dto.getRecruitField())
                .salary(dto.getSalary())
                .postingDeadline(dto.getPostingDeadline())
                .postingStatus(dto.getPostingStatus())
                .workExperience(dto.getWorkExperience())
                .tag(dto.getTag())
                .jobCategory(dto.getJobCategory())
                .imgFileName(dto.getImgFileName())
                .build();

        return jobPosting;
    }

    default JobPostingDTO entityToDto(JobPosting entity) {
        JobPostingDTO dto = JobPostingDTO.builder()
                .jobCode(entity.getJobCode())
                .title(entity.getTitle())
                .content(entity.getContent())
                .recruitJob(entity.getRecruitJob())
                .recruitField(entity.getRecruitField())
                .salary(entity.getSalary())
                .postingDate(entity.getCreatedDate())
                .postingDeadline(entity.getPostingDeadline())
                .postingStatus(entity.getPostingStatus())
                .workExperience(entity.getWorkExperience())
                .tag(entity.getTag())
                .jobCategory(entity.getJobCategory())
                .imgFileName(entity.getImgFileName())
                // imgPath 필드는 @Transient로 설정되어 데이터베이스에 저장되지 않으며,
                // getImgPath() 메서드를 통해 imgDirectory와 imgFileName을 결합한 경로를 반환
                .imgPath(entity.getImgPath()) // 전체 파일 경로
                .build();
        return dto;
    }
}