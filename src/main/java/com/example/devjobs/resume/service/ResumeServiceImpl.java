package com.example.devjobs.resume.service;

import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.resume.repository.ResumeRepository;
import com.example.devjobs.user.entity.User;
import com.example.devjobs.user.repository.UserRepository;
import com.example.devjobs.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    ResumeRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileUtil fileUtil;

    /**
     * Skills 문자열을 파싱하여 List<String>으로 변환
     */
    public List<String> parseSkills(String skillString) {
        if (skillString == null || skillString.isEmpty()) {
            return new ArrayList<>();
        }
        return List.of(skillString.split(",")).stream()
                .map(String::trim) // 공백 제거
                .toList();
    }

    @Override
    public int register(ResumeDTO dto, MultipartFile resumeFolder) {
        User loggedInUser = getLoggedInUser();

        String fileName = null;
        if (resumeFolder != null && !resumeFolder.isEmpty()) {
            fileName = fileUtil.fileUpload(resumeFolder, "resume");
        }

        Resume resume = dtoToEntity(dto);
        resume.setUserCode(loggedInUser); // userCode 설정

        List<String> skillList = parseSkills(dto.getSkill());
        resume.setSkill(String.join(",", skillList));

        if (fileName != null) {
            resume.setUploadFileName(fileName);
        }

        repository.save(resume);
        return resume.getResumeCode();
    }

    @Override
    public void modify(Integer resumeCode, String workExperience, String education, String skill,
                       String certifications, String languageSkills, MultipartFile resumeFile,
                       LocalDateTime lastUpdated, String jobCategory) {
        Resume resume = validateOwnership(resumeCode);

        // 전달된 값만 업데이트
        if (workExperience != null) {
            resume.setWorkExperience(workExperience);
        }
        if (education != null) {
            resume.setEducation(education);
        }
        if (skill != null) {
            List<String> skillList = parseSkills(skill);
            resume.setSkill(String.join(",", skillList));
        }
        if (jobCategory != null) {
            resume.setJobCategory(jobCategory);
        }
        if (certifications != null) {
            resume.setCertifications(certifications);
        }
        if (languageSkills != null) {
            resume.setLanguageSkills(languageSkills);
        }
        if (resumeFile != null && !resumeFile.isEmpty()) {
            String fileName = fileUtil.fileUpload(resumeFile, "resume");
            resume.setUploadFileName(fileName);
        }

        resume.setUpdateDate(lastUpdated);
        repository.save(resume);
    }

    @Override
    public void remove(Integer resumeCode) {
        Resume resume = validateOwnership(resumeCode);
        repository.delete(resume);
    }

    @Override
    public List<ResumeDTO> getList() {
        List<Resume> resumes = repository.findAll();
        List<ResumeDTO> resumeDTOs = new ArrayList<>();

        for (Resume resume : resumes) {
            if (resume.getUserCode() == null) {
                System.out.println("Skipping resume with null userCode: " + resume.getResumeCode());
                continue;
            }
            ResumeDTO dto = entityToDTO(resume);
            resumeDTOs.add(dto);
        }
        return resumeDTOs;
    }

    @Override
    public ResumeDTO read(Integer resumeCode) {
        Resume resume = repository.findById(resumeCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 이력서 코드가 존재하지 않습니다."));

        if (resume.getUserCode() == null) {
            throw new SecurityException("해당 이력서의 작성자 정보가 없습니다.");
        }

        return entityToDTO(resume);
    }

    /**
     * 현재 로그인된 사용자 정보 가져오기
     */
    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        User user = userRepository.findByUserId(currentUserName);
        if (user == null) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        return user;
    }

    /**
     * Resume 소유권 검증
     */
    private Resume validateOwnership(Integer resumeCode) {
        User loggedInUser = getLoggedInUser();

        Resume resume = repository.findById(resumeCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 이력서 코드가 존재하지 않습니다."));

        if (resume.getUserCode() == null || !resume.getUserCode().getUserCode().equals(loggedInUser.getUserCode())) {
            throw new SecurityException("작성자만 이력서를 수정하거나 삭제할 수 있습니다.");
        }

        return resume;
    }
}