package com.example.devjobs.apply.service;

import com.example.devjobs.apply.dto.ApplyDTO;
import com.example.devjobs.apply.entity.Apply;
import com.example.devjobs.apply.entity.ApplyStatusValidator;
import com.example.devjobs.apply.repository.ApplyRepository;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.resume.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplyServiceImpl implements ApplyService {

    @Autowired
    ApplyRepository applyRepository;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Autowired
    ResumeRepository resumeRepository;

    @Override
    public int register(ApplyDTO dto) {

        Apply entity = dtoToEntity(dto);

        applyRepository.save(entity);

        return entity.getApplyCode();
    }

    @Override
    public List<ApplyDTO> getList() {

        List<Apply> entityList = applyRepository.findAll();
        List<ApplyDTO> dtoList = entityList.stream()
                .map(entity -> entityToDTO(entity))
                .collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public ApplyDTO read(int code) {
        Optional<Apply> result = applyRepository.findById(code);
        if(result.isPresent()) {
            return entityToDTO(result.get());
        } else {
            return null;
        }
    }

    @Override
    public void modify(ApplyDTO dto) {

        Optional<Apply> result = applyRepository.findById(dto.getApplyCode());
        if(result.isPresent()) {
            Apply entity = result.get();

            // jobCode 업데이트
            if(dto.getJobCode() != null) {
                // jobCode가 String인 경우 Integer로 변환(POSTMAN에서 JSON으로 전달 됨)
                Integer jobCode = Integer.valueOf(dto.getJobCode());
                Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobCode);
                jobPosting.ifPresent(entity::setJobCode); // 존재할 경우(ifPresent)만 업데이트(setJobCode)

            }

            // resumeCode 업데이트
            if(dto.getResumeCode() != null) {
                Optional<Resume> resume = resumeRepository.findById(dto.getResumeCode());
                resume.ifPresent(entity::setResumeCode);
            }

            // applyStatus 업데이트
            if (dto.getApplyStatus() != null) {
                // 상태 값 검증
                if (!ApplyStatusValidator.isValid(dto.getApplyStatus())) {
                    throw new IllegalArgumentException("Invalid apply status: " + dto.getApplyStatus());
                }
                entity.setApplyStatus(dto.getApplyStatus()); // String으로 상태 설정
            }

            applyRepository.save(entity);

        }

    }

    @Override
    public void remove(Integer applyCode) {

        Optional<Apply> result = applyRepository.findById(applyCode);

        if(result.isPresent()) {

            applyRepository.delete(result.get());

        }

    }


}
