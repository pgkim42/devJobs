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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    @Autowired
    JobPostingRepository repository;

    @Autowired
    FileUtil fileUtil;

    @Override
    public int register(JobPostingDTO dto, MultipartFile jobPostingFolder) {

        // FileUtil을 사용하여 파일 업로드 처리
//        String imgFileName = fileUtil.fileUpload(dto.getUploadFile());

        // 파일 업로드 처리 (폴더 category: jobposting)
        String imgFileName = null;
        if (dto.getUploadFile() != null && !dto.getUploadFile().isEmpty()) {
            imgFileName = fileUtil.fileUpload(dto.getUploadFile(), "jobposting");
            System.out.println("파일 업로드 테스트" + imgFileName);
        }

        JobPosting jobPosting = dtoToEntity(dto);

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

    // 일부 필드만 업데이트(개별 수정 @PATCH)
    @Override
    public void modifyPartial(Integer jobCode, String title, String content, String recruitJob,
                              Integer recruitField, String salary, String postingStatus,
                              String workExperience, String tag, String jobCategory,
                              LocalDateTime postingDeadline, MultipartFile uploadFile) {

//        if (jobCode == null) {
//            throw new IllegalArgumentException("Job Code must not be null.");
//        }

        Optional<JobPosting> result = repository.findById(jobCode);

        if (result.isPresent()) {
            JobPosting entity = result.get();

            if (title != null) entity.setTitle(title);
            if (content != null) entity.setContent(content);
            if (recruitJob != null) entity.setRecruitJob(recruitJob);
            if (recruitField != null) entity.setRecruitField(recruitField);
            if (salary != null) entity.setSalary(salary);
            if (postingStatus != null) entity.setPostingStatus(postingStatus);
            if (workExperience != null) entity.setWorkExperience(workExperience);
            if (tag != null) entity.setTag(tag);
            if (jobCategory != null) entity.setJobCategory(jobCategory);
            if (postingDeadline != null) entity.setPostingDeadline(postingDeadline);

            // 업로드된 파일이 있을 경우 처리
            if (uploadFile != null && !uploadFile.isEmpty()) {
                // 새 파일 업로드 처리 (폴더: "jobposting")
                String newFileName = fileUtil.fileUpload(uploadFile, "jobposting");

                // 이전 파일 삭제
                if (entity.getImgFileName() != null) {
                    fileUtil.deleteFile(entity.getImgFileName());
                }

                // 새 파일 이름 저장
                entity.setImgFileName(newFileName);
            }

            // 변경된 엔티티 저장
            repository.save(entity);
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
