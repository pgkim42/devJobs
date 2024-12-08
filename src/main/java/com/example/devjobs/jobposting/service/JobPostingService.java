package com.example.devjobs.jobposting.service;

import com.example.devjobs.companyprofile.entity.CompanyProfile;
import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.entity.PostingStatus;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface JobPostingService {

    int register(JobPostingDTO dto, MultipartFile jobPostingFolder);

    List<JobPostingDTO> getList();

    JobPostingDTO read(Integer jobCode);

    void remove(Integer jobCode);

    void modifyPartial(Integer jobCode, String title, String content, String recruitJob,
                       Integer recruitField, String salary, String postingStatus,
                       String workExperience, String tag, String jobCategory,
                       LocalDateTime postingDeadline, MultipartFile uploadFile, LocalDateTime lastUpdated);

    default JobPosting dtoToEntity(JobPostingDTO dto) {

        // CompanyProfile 가져오기(기업프로필)
        CompanyProfile companyProfile = new CompanyProfile();
        companyProfile.setCompanyProfileCd(dto.getCompanyProfileCd());

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
                .companyProfile(companyProfile)
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
                .companyProfileCd(entity.getCompanyProfile().getCompanyProfileCd())
                // imgPath 필드는 @Transient로 설정되어 데이터베이스에 저장되지 않으며,
                // getImgPath() 메서드를 통해 imgDirectory와 imgFileName을 결합한 경로를 반환
                .imgPath(entity.getImgPath()) // 전체 파일 경로
                .build();
        return dto;
    }
}