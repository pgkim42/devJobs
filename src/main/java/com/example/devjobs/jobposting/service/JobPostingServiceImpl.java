package com.example.devjobs.jobposting.service;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import com.example.devjobs.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    @Autowired
    JobPostingRepository repository;

    @Autowired
    FileUtil fileUtil;

    public List<String> parseSkills(String skillString) {
        if (skillString == null || skillString.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(skillString.split(","))
                .map(String::trim) // 공백 제거
                .collect(Collectors.toList());
    }

    @Override
    public int register(JobPostingDTO dto, MultipartFile jobPostingFolder) {

        // 파일 업로드 처리 (폴더 category: jobposting)
        String imgFileName = null;
        if (dto.getUploadFile() != null && !dto.getUploadFile().isEmpty()) {
            imgFileName = fileUtil.fileUpload(dto.getUploadFile(), "jobposting");
            System.out.println("파일 업로드 테스트" + imgFileName);
        }

        // 애플리케이션에서는 받은 dto를 List형태로 담은 뒤(split)에 DB에는 join으로 문자열 형태로 전송(join)
        // DTO에서 skill 문자열을 리스트로 변환
        List<String> skillList = parseSkills(dto.getSkill());

        JobPosting jobPosting = dtoToEntity(dto);

        // 엔티티에 skill 리스트를 다시 문자열로 설정
        jobPosting.setSkill(String.join(",", skillList)); // "java,spring,sql"

        // 업로드된 파일명을 설정
        if(imgFileName != null) {
            jobPosting.setImgFileName(imgFileName);
        }

        repository.save(jobPosting);

        return jobPosting.getJobCode(); // 공고의 jobCode 반환

    }

    @Override
    public List<JobPostingDTO> getList() {

        List<JobPosting> entityList = repository.findAll();
        List<JobPostingDTO> dtoList = entityList.stream()
                .map(entity -> entityToDto(entity))
                .collect(Collectors.toList());

        return dtoList;

    }

    @Override
    public JobPostingDTO read(Integer jobCode) {
        Optional<JobPosting> result = repository.findById(jobCode);
        if (result.isPresent()) {
            JobPosting jobPosting = result.get();
            return entityToDto(jobPosting);
        } else {
            return null;
        }
    }

    @Override
    public void modify(Integer jobCode, String title, String content, String recruitJob,
                              Integer recruitField, String salary, String postingStatus,
                              String workExperience, String tag, String jobCategory,
                              String skill, LocalDateTime postingDeadline, MultipartFile uploadFile, LocalDateTime lastUpdated) {

        // jobCode로 기존 JobPosting 검색
        Optional<JobPosting> result = repository.findById(jobCode);

        if (result.isPresent()) {
            JobPosting entity = result.get();

            // 수정할 필드들 업데이트
            if (title != null) {
                entity.setTitle(title);
            }
            if (content != null) {
                entity.setContent(content);
            }
            if (recruitJob != null) {
                entity.setRecruitJob(recruitJob);
            }
            if (recruitField != null) {
                entity.setRecruitField(recruitField);
            }
            if (salary != null) {
                entity.setSalary(salary);
            }
            if (postingStatus != null) {
                entity.setPostingStatus(postingStatus);
            }
            if (workExperience != null) {
                entity.setWorkExperience(workExperience);
            }
            if (tag != null) {
                entity.setTag(tag);
            }
            if (jobCategory != null) {
                entity.setJobCategory(jobCategory);
            }

            if (skill != null) {
                entity.setSkill(skill);
            }

            if (postingDeadline != null) {
                entity.setPostingDeadline(postingDeadline);
            }

            if (uploadFile != null && !uploadFile.isEmpty()) {
                String fileName = fileUtil.fileUpload(uploadFile, "jobposting");
                entity.setImgFileName(fileName);
            }

            entity.setUpdatedDate(lastUpdated);

            repository.save(entity);

        } else {
            throw new IllegalArgumentException("해당 JobPosting 코드가 존재하지 않습니다.");
        }
    }

    @Override
    public void remove(Integer jobCode) {
//        if(jobCode == null) {
//            throw new IllegalArgumentException("Job Code must not be null.");
//        }

        Optional<JobPosting> result = repository.findById(jobCode);

        if(result.isPresent()) {
            // 파일 삭제 처리
            JobPosting entity = result.get();

            if(entity.getImgFileName() != null) {
                fileUtil.deleteFile(entity.getImgFileName()); // 이미지 파일 삭제
            }

            repository.deleteById(jobCode);

         } else {
            throw new IllegalArgumentException("Job Posting 코드가 존재하지 않습니다");
        }
    }
}
