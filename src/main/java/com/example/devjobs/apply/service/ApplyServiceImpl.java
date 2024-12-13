package com.example.devjobs.apply.service;

import com.example.devjobs.apply.dto.ApplyDTO;
import com.example.devjobs.apply.entity.Apply;
import com.example.devjobs.apply.entity.ApplyStatusValidator;
import com.example.devjobs.apply.repository.ApplyRepository;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.resume.repository.ResumeRepository;
import com.example.devjobs.user.entity.User;
import com.example.devjobs.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplyServiceImpl implements ApplyService {

    @Autowired
    private ApplyRepository applyRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    // 지원서 등록
    @Override
    public int register(ApplyDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        User loggedInUser = userRepository.findByUserId(currentUserName);
        if (loggedInUser == null) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        Apply entity = dtoToEntity(dto);
        entity.setUserCode(loggedInUser);

        applyRepository.save(entity);

        return entity.getApplyCode();
    }

    // 지원서 목록 조회
    @Override
    public List<ApplyDTO> getList() {
        List<Apply> entityList = applyRepository.findAll();

        List<ApplyDTO> dtoList = entityList.stream()
                .map(entity -> entityToDTO(entity))
                .collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public List<ApplyDTO> getUserApplications(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }

        List<Apply> applications = applyRepository.findByUserCode(user);
        return applications.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }


    // 지원서 단일 조회
    @Override
    public ApplyDTO read(int code) {
        Optional<Apply> result = applyRepository.findById(code);
        return result.map(entity -> entityToDTO(entity)).orElse(null);
    }

    // 지원서 수정
    @Override
    public void modify(ApplyDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        User loggedInUser = userRepository.findByUserId(currentUserName);
        if (loggedInUser == null) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        Optional<Apply> result = applyRepository.findById(dto.getApplyCode());
        if (result.isPresent()) {
            Apply entity = result.get();

            if (!entity.getUserCode().getUserCode().equals(loggedInUser.getUserCode())) {
                throw new SecurityException("작성자만 지원서를 수정할 수 있습니다.");
            }

            // jobCode 업데이트
            if (dto.getJobCode() != null) {
                Optional<JobPosting> jobPosting = jobPostingRepository.findById(dto.getJobCode());
                if (jobPosting.isPresent()) {
                    entity.setJobCode(jobPosting.get());
                }
            }

            // resumeCode 업데이트
            if (dto.getResumeCode() != null) {
                Optional<Resume> resume = resumeRepository.findById(dto.getResumeCode());
                if (resume.isPresent()) {
                    entity.setResumeCode(resume.get());
                }
            }

            // applyStatus 업데이트
            if (dto.getApplyStatus() != null) {
                if (!ApplyStatusValidator.isValid(dto.getApplyStatus())) {
                    throw new IllegalArgumentException("Invalid apply status: " + dto.getApplyStatus());
                }
                entity.setApplyStatus(dto.getApplyStatus());
            }

            applyRepository.save(entity);
        } else {
            throw new IllegalArgumentException("해당 지원서 코드가 존재하지 않습니다.");
        }
    }

    // 지원서 삭제
    @Override
    public void remove(Integer applyCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        User loggedInUser = userRepository.findByUserId(currentUserName);
        if (loggedInUser == null) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        Optional<Apply> result = applyRepository.findById(applyCode);

        if (result.isPresent()) {
            Apply entity = result.get();

            if (!entity.getUserCode().getUserCode().equals(loggedInUser.getUserCode())) {
                throw new SecurityException("작성자만 지원서를 삭제할 수 있습니다.");
            }

            applyRepository.delete(entity);
        } else {
            throw new IllegalArgumentException("해당 지원서 코드가 존재하지 않습니다.");
        }
    }
}