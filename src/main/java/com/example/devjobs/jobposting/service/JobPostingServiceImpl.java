package com.example.devjobs.jobposting.service;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import com.example.devjobs.user.entity.User;
import com.example.devjobs.user.repository.UserRepository;
import com.example.devjobs.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    UserRepository userRepository;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated User: " + (authentication != null ? authentication.getName() : "No Authentication"));

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        System.out.println("Current Username: " + currentUserName);

        User user = userRepository.findByUserId(currentUserName);
        System.out.println("Fetched User from DB: " + user);

        if (user == null) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        // 파일 업로드 처리
        String imgFileName = null;
        if (dto.getUploadFile() != null && !dto.getUploadFile().isEmpty()) {
            imgFileName = fileUtil.fileUpload(dto.getUploadFile(), "jobposting");
            System.out.println("Uploaded File Name: " + imgFileName);
        }

        JobPosting jobPosting = dtoToEntity(dto);
        jobPosting.setUserCode(user); // 로그인한 사용자의 userCode 설정

        // Skills 파싱 및 설정
        if (dto.getSkill() != null) {
            List<String> skillList = parseSkills(dto.getSkill());
            jobPosting.setSkill(String.join(",", skillList)); // 파싱한 스킬 리스트를 다시 문자열로 설정
        }

        if (imgFileName != null) {
            jobPosting.setImgFileName(imgFileName);
        }

        System.out.println("JobPosting Entity before Save: " + jobPosting);

        repository.save(jobPosting);
        System.out.println("JobPosting successfully saved with ID: " + jobPosting.getJobCode());

        return jobPosting.getJobCode();
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
                       Integer recruitField, String salary, boolean postingStatus,
                       Integer workExperience, String tag, String jobCategory,
                       String skill, LocalDateTime postingDeadline, MultipartFile uploadFile, LocalDateTime lastUpdated) {

        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        User loggedInUser = userRepository.findByUserId(currentUserName);
        if (loggedInUser == null) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        // 기존 JobPosting 검색
        Optional<JobPosting> result = repository.findById(jobCode);

        if (result.isPresent()) {
            JobPosting entity = result.get();

            // 작성자와 로그인된 사용자 비교
            if (!entity.getUserCode().getUserCode().equals(loggedInUser.getUserCode())) {
                throw new SecurityException("작성자만 게시글을 수정할 수 있습니다.");
            }

            // 전달된 값만 업데이트
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
            
            // boolean은 null값 가질 수 없음
            entity.setPostingStatus(postingStatus);
            
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
                List<String> skillList = parseSkills(skill); // Skills 파싱
                entity.setSkill(String.join(",", skillList)); // 파싱한 스킬 리스트를 다시 문자열로 설정
            }
            if (postingDeadline != null) {
                entity.setPostingDeadline(postingDeadline);
            }

            // 업로드된 파일이 있으면 처리
            if (uploadFile != null && !uploadFile.isEmpty()) {
                String fileName = fileUtil.fileUpload(uploadFile, "jobposting");
                entity.setImgFileName(fileName);
            }

            entity.setUpdateDate(lastUpdated);

            repository.save(entity);

        } else {
            throw new IllegalArgumentException("해당 JobPosting 코드가 존재하지 않습니다.");
        }
    }

    @Override
    public void remove(Integer jobCode) {
        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        User loggedInUser = userRepository.findByUserId(currentUserName);
        if (loggedInUser == null) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        // 삭제하려는 JobPosting 검색
        Optional<JobPosting> result = repository.findById(jobCode);

        if (result.isPresent()) {
            JobPosting entity = result.get();

            // 작성자와 로그인된 사용자 비교
            if (!entity.getUserCode().getUserCode().equals(loggedInUser.getUserCode())) {
                throw new SecurityException("작성자만 게시글을 삭제할 수 있습니다.");
            }

            repository.deleteById(jobCode);
        } else {
            throw new IllegalArgumentException("해당 JobPosting 코드가 존재하지 않습니다.");
        }
    }
}
