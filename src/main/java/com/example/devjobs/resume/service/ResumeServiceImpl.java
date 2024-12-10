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

import java.util.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    ResumeRepository repository;

    @Autowired
    FileUtil fileUtil;

    // skill부분 list형태로 파싱
    private List<String> parseSkills(String skillString) {
        if (skillString == null || skillString.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(skillString.split(",")) // 콤마로 분리
                .map(String::trim) // 각 항목 공백 제거
                .collect(Collectors.toList());
    }
    @Override
    public int register(ResumeDTO dto, MultipartFile resumeFolder) {

        // 파일 업로드 처리 (폴더 category: resume)
        String fileName = null;
        if (resumeFolder != null && !resumeFolder.isEmpty()) {
            fileName = fileUtil.fileUpload(resumeFolder, "resume");
            System.out.println("파일 업로드 완료: " + fileName);
        }

        // 애플리케이션에서는 받은 dto를 List 형태로 담은 뒤(split), DB에는 join으로 문자열 형태로 전송(join)
        // DTO에서 skill 문자열을 리스트로 변환
        List<String> skillList = parseSkills(dto.getSkill());

        // DTO를 Entity로 변환
        Resume resume = dtoToEntity(dto);

        // 엔티티에 skill 리스트를 다시 문자열로 설정
        resume.setSkill(String.join(",", skillList)); // "java,spring,sql"

        // 업로드된 파일명을 설정
        if (fileName != null) {
            resume.setUploadFileName(fileName);
        }

        // DB에 저장
        repository.save(resume);

        // 저장된 이력서의 ID 반환
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
                       LocalDateTime lastUpdated, String jobCategory) {

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

            if (jobCategory != null) {
                entity.setJobCategory(jobCategory);
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
