package com.example.devjobs.service;

import com.example.devjobs.companyprofile.entity.CompanyProfile;
import com.example.devjobs.companyprofile.repository.CompanyProfileRepository;
import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import com.example.devjobs.jobposting.service.JobPostingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JobPostingServiceTest {

    @Autowired
    private JobPostingService jobPostingService;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private CompanyProfileRepository companyProfileRepository;

    @Test
    void testCreateJobPosting() {
        // CompanyProfile 생성
        CompanyProfile companyProfile = companyProfileRepository.save(
                CompanyProfile.builder()
                        .companyName("Example Company")
                        .industry("IT")
                        .build()
        );

        // JobPosting 생성
        JobPostingDTO jobPostingDTO = JobPostingDTO.builder()
                .title("Frontend Developer")
                .content("Design and implement user interfaces.")
                .recruitJob("Frontend Developer")
                .recruitField(2)
                .salary("4000-6000 USD")
                .postingDate(LocalDateTime.now())
                .postingDeadline(LocalDateTime.now().plusDays(30))
                .postingStatus("모집중")
                .workExperience("신입")
                .tag("React,JavaScript")
                .jobCategory("IT")
                .skill("React,JavaScript")
                .companyProfileCd(companyProfile.getCompanyProfileCd())
                .build();

        Integer jobCode = jobPostingService.register(jobPostingDTO, null);
        JobPostingDTO createdJobPosting = jobPostingService.read(jobCode);

        System.out.println("=== JobPosting 생성 테스트 ===");
        System.out.println("Job Title: " + createdJobPosting.getTitle());
        System.out.println("Company Profile Code: " + createdJobPosting.getCompanyProfileCd());

        assertThat(createdJobPosting).isNotNull();
        assertThat(createdJobPosting.getTitle()).isEqualTo("Frontend Developer");
    }

    @Test
    void testReadJobPosting() {
        // CompanyProfile 생성
        CompanyProfile companyProfile = companyProfileRepository.save(
                CompanyProfile.builder()
                        .companyName("Example Company")
                        .industry("IT")
                        .build()
        );

        // JobPosting 생성
        JobPostingDTO jobPostingDTO = JobPostingDTO.builder()
                .title("Software Engineer")
                .content("Develop and maintain software systems.")
                .recruitJob("Backend Developer")
                .recruitField(3)
                .salary("5000-7000 USD")
                .postingDate(LocalDateTime.now())
                .postingDeadline(LocalDateTime.now().plusDays(30))
                .postingStatus("모집중")
                .workExperience("경력")
                .tag("Java,Spring,SQL")
                .jobCategory("IT")
                .skill("Java,Spring,SQL")
                .companyProfileCd(companyProfile.getCompanyProfileCd())
                .build();

        Integer jobCode = jobPostingService.register(jobPostingDTO, null);
        JobPostingDTO savedJobPostingDTO = jobPostingService.read(jobCode);

        System.out.println("=== JobPosting 조회 테스트 ===");
        System.out.println("Job Title: " + savedJobPostingDTO.getTitle());
        System.out.println("Company Profile Code: " + savedJobPostingDTO.getCompanyProfileCd());

        assertThat(savedJobPostingDTO).isNotNull();
        assertThat(savedJobPostingDTO.getTitle()).isEqualTo("Software Engineer");
    }

    @Test
    void testUpdateJobPosting() {
        // CompanyProfile 생성
        CompanyProfile companyProfile = companyProfileRepository.save(
                CompanyProfile.builder()
                        .companyName("Example Company")
                        .industry("IT")
                        .build()
        );

        // JobPosting 생성
        JobPostingDTO jobPostingDTO = JobPostingDTO.builder()
                .title("Software Engineer")
                .content("Develop and maintain software systems.")
                .recruitJob("Backend Developer")
                .recruitField(3)
                .salary("5000-7000 USD")
                .postingDate(LocalDateTime.now())
                .postingDeadline(LocalDateTime.now().plusDays(30))
                .postingStatus("모집중")
                .workExperience("경력")
                .tag("Java,Spring,SQL")
                .jobCategory("IT")
                .skill("Java,Spring,SQL")
                .companyProfileCd(companyProfile.getCompanyProfileCd())
                .build();

        Integer jobCode = jobPostingService.register(jobPostingDTO, null);

        // 수정
        String updatedTitle = "Senior Software Engineer";
        jobPostingService.modify(
                jobCode,
                updatedTitle,
                jobPostingDTO.getContent(),
                jobPostingDTO.getRecruitJob(),
                jobPostingDTO.getRecruitField(),
                jobPostingDTO.getSalary(),
                jobPostingDTO.getPostingStatus(),
                jobPostingDTO.getWorkExperience(),
                jobPostingDTO.getTag(),
                jobPostingDTO.getJobCategory(),
                jobPostingDTO.getSkill(),
                jobPostingDTO.getPostingDeadline(),
                null,
                LocalDateTime.now()
        );

        JobPostingDTO updatedJobPosting = jobPostingService.read(jobCode);

        System.out.println("=== JobPosting 수정 테스트 ===");
        System.out.println("Updated Job Title: " + updatedJobPosting.getTitle());

        assertThat(updatedJobPosting).isNotNull();
        assertThat(updatedJobPosting.getTitle()).isEqualTo(updatedTitle);
    }

    @Test
    void testDeleteJobPosting() {
        // CompanyProfile 생성
        CompanyProfile companyProfile = companyProfileRepository.save(
                CompanyProfile.builder()
                        .companyName("Example Company")
                        .industry("IT")
                        .build()
        );

        // JobPosting 생성
        JobPostingDTO jobPostingDTO = JobPostingDTO.builder()
                .title("Software Engineer")
                .content("Develop and maintain software systems.")
                .recruitJob("Backend Developer")
                .recruitField(3)
                .salary("5000-7000 USD")
                .postingDate(LocalDateTime.now())
                .postingDeadline(LocalDateTime.now().plusDays(30))
                .postingStatus("모집중")
                .workExperience("경력")
                .tag("Java,Spring,SQL")
                .jobCategory("IT")
                .skill("Java,Spring,SQL")
                .companyProfileCd(companyProfile.getCompanyProfileCd())
                .build();

        Integer jobCode = jobPostingService.register(jobPostingDTO, null);

        // 삭제
        jobPostingService.remove(jobCode);
        Optional<JobPosting> deletedJobPosting = jobPostingRepository.findById(jobCode);

        System.out.println("=== JobPosting 삭제 테스트 ===");
        System.out.println("Deleted JobPosting Exists: " + deletedJobPosting.isPresent());

        assertThat(deletedJobPosting).isEmpty();
    }
}