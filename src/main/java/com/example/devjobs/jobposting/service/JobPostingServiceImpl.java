package com.example.devjobs.jobposting.service;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import com.example.devjobs.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Autowired
    FileUtil fileUtil;

    @Override
    public int register(JobPostingDTO dto) {

        // FileUtil을 사용하여 파일 업로드 처리
        String imgFileName = fileUtil.fileUpload(dto.getUploadFile(), dto.getImgDirectory());

        // JobPostingDTO -> JobPosting 엔티티로
        JobPosting jobPosting = dtoToEntity(dto);

        // 업로드된 파일명을 설정
        jobPosting.setImgFileName(imgFileName);

        // DB에 저장
        jobPostingRepository.save(jobPosting);

        return jobPosting.getJobCode(); // 공고의 jobCode 반환

    }
}
