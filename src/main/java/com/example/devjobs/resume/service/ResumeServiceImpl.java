package com.example.devjobs.resume.service;

import com.example.devjobs.resume.dto.CertificationsDTO;
import com.example.devjobs.resume.dto.LanguagesSkillsDTO;
import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.resume.repository.ResumeRepository;
import com.example.devjobs.util.FileUtil;
import com.example.devjobs.util.JsonUtil;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    ResumeRepository repository;

    @Autowired
    FileUtil fileUtil;

//    @Autowired
//    ResumeService service;

    @Override
    public int register(ResumeDTO dto, MultipartFile resumeFolder) {
        String fileName = null;

        if (resumeFolder != null && !resumeFolder.isEmpty()) {
            fileName = fileUtil.fileUpload(resumeFolder, "resume");
            System.out.println("파일 업로드 완료: " + fileName);
        }

        dto.setUploadFileName(fileName);

        Resume resume = dtoToEntity(dto);
        repository.save(resume);

        return resume.getResumeCd();
    }

    @Override
    public List<ResumeDTO> getList() {

        List<Resume> entityList = repository.findAll();
        List<ResumeDTO> dtoList = entityList.stream()
                .map(entity -> {
                    ResumeDTO dto = entityToDTO(entity);

                    if (entity.getCertifications() != null) {
                        dto.setCertifications(JsonUtil.convertJsonToList(entity.getCertifications(), CertificationsDTO.class));
                    }

                    if (entity.getLanguageSkills() != null) {
                        dto.setLanguageSkills(JsonUtil.convertJsonToList(entity.getLanguageSkills(), LanguagesSkillsDTO.class));
                    }

                    return dto;

                })

                .collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public ResumeDTO read(Integer resumeCode) {

        Optional<Resume> result = repository.findById(resumeCode);

        if (result.isPresent()) {
            Resume resume = result.get();
            return entityToDTO(resume);
        } else {
            throw new IllegalArgumentException("해당 이력서 코드가 존재하지 않습니다.");
        }

    }

    @Override
    public void modify(Integer resumeCd, String workExperience, String education, String skill,
                       String certifications, String languageSkills, MultipartFile resumeFile,
                       LocalDateTime lastUpdated) {

        // 이력서 코드로 기존 이력서 검색
        Optional<Resume> result = repository.findById(resumeCd);

        if (result.isPresent()) {
            Resume entity = result.get();

            // 수정할 필드들 업데이트
            if (workExperience != null) {
                entity.setWorkExperience(workExperience);
            }

            if (education != null) {
                entity.setEducation(education);
            }

            if (skill != null) {
                entity.setSkill(skill);
            }

            // 자격증 JSON 문자열을 List로 변환 후 저장
            if (certifications != null) {
                List<CertificationsDTO> certList = JsonUtil.convertJsonToList(certifications, CertificationsDTO.class);
                entity.setCertifications(JsonUtil.convertListToJson(certList));
            }

            // 언어 능력 JSON 문자열을 List로 변환 후 저장
            if (languageSkills != null) {
                List<LanguagesSkillsDTO> langSkillsList = JsonUtil.convertJsonToList(languageSkills, LanguagesSkillsDTO.class);
                entity.setLanguageSkills(JsonUtil.convertListToJson(langSkillsList));
            }

            // 이력서 파일 업데이트
            if (resumeFile != null && !resumeFile.isEmpty()) {
                String fileName = fileUtil.fileUpload(resumeFile, "resume");
                entity.setUploadFileName(fileName);
            }

            // 마지막 수정일 업데이트 (BaseEntity에서 관리되므로 추가하지 않아도 됨)
            entity.setUpdatedDate(lastUpdated);

            // 수정된 엔티티 저장
            repository.save(entity);
        } else {
            throw new IllegalArgumentException("해당 이력서 코드가 존재하지 않습니다.");
        }
    }

    @Override
    public void remove(Integer resumeCode) {

        Optional<Resume> result = repository.findById(resumeCode);

        if (result.isPresent()) {
            // 파일 삭제 처리
            Resume entity = result.get();

            if (entity.getUploadFileName() != null) {
                fileUtil.deleteFile(entity.getUploadFileName()); // 파일 삭제
            }

            repository.deleteById(resumeCode);
        } else {
            throw new IllegalArgumentException("resume코드가 존재하지 않습니다");
        }

    }

}
