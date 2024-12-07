package com.example.devjobs.resume.service;

import com.example.devjobs.resume.dto.CertificationsDTO;
import com.example.devjobs.resume.dto.LanguagesSkillsDTO;
import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.resume.repository.ResumeRepository;
import com.example.devjobs.util.FileUtil;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


//    @Override
//    public List<ResumeDTO> getList() {
//
//        List<Resume> entityList = repository.findAll();
//        List<ResumeDTO> dtoList = entityList.stream()
//                .map(entity -> {
//                    ResumeDTO dto = entityToDTO(entity);
//
//                    // JSON -> List 변환
//                    if(entity.getCertifications() != null) {
//                        dto.setCertifications(convertJsonToList(entity.getCertifications(), CertificationsDTO.class));
//                    }
//                    if(entity.getLanguageSkills() != null) {
//                        dto.setLanguageSkills(convertJsonToList(entity.getLanguageSkills(), LanguagesSkillsDTO.class));;
//                    }
//                    return dto;
//                })
//                .collect(Collectors.toList());
//
//        return dtoList;
//    }
//
//    @Override
//    public ResumeDTO read(Integer resumeCode) {
//        Optional<Resume> result = repository.findById(resumeCode);
//        if (result.isPresent()) {
//            Resume resume = result.get();
//            ResumeDTO dto = entityToDTO(resume);
//
//            if (resume.getLanguageSkills() != null) {
//                dto.setCertifications(convertJsonToList(resume.getCertifications(), CertificationsDTO.class));
//            }
//            if (resume.getLanguageSkills() != null) {
//                dto.setLanguageSkills(convertJsonToList(resume.getLanguageSkills(), LanguagesSkillsDTO.class));
//            }
//
//            return dto;
//        } else {
//            return null;
//        }
//    }

//    @Override
//    public void modify(ResumeDTO dto) {
//        Optional<Resume> result = repository.findById(dto.getResumeCd());
//        if(result.isPresent()){
//            Resume entity = result.get();
//
//            // 각 필드가 null이 아닌 경우에만 수정(개별 수정)
//            if (dto.getWorkExperience() != null) {
//                entity.setWorkExperience(dto.getWorkExperience());
//            }
//            if (dto.getEducation() != null) {
//                entity.setEducation(dto.getEducation());
//            }
//
//            // Certifications 변환 처리 (JSON) .. service의 default 메서드로
//            if (dto.getCertifications() != null) {
//                String certificationsJson = convertListToJson(dto.getCertifications());
//                entity.setCertifications(certificationsJson);
//            }
//            if (dto.getSkill() != null) {
//                entity.setSkill(dto.getSkill());
//            }
//
//            // LanguageSkills 변환 처리 (JSON) .. service의 default 메서드로
//            if (dto.getLanguageSkills() != null) {
//                String languageSkillsJson = convertListToJson(dto.getLanguageSkills());
//                System.out.println("언어능력 JSON확인:" + languageSkillsJson);
//                entity.setLanguageSkills(languageSkillsJson);
//            }
//            if (dto.getUploadFileName() != null) {
//                entity.setUploadFileName(dto.getUploadFileName());
//            }
//
//            // 파일 처리 로직 추가
//            if (dto.getUploadFile() != null && !dto.getUploadFile().isEmpty()) {
//                // 파일 업로드 처리 로직 (파일 저장, 경로 설정 등)
//                // 예시로, 파일 이름만 설정
//                String fileName = fileUtil.fileUpload(dto.getUploadFile(), "resume");
//                entity.setUploadFileName(fileName);
//            }
//
//            repository.save(entity);
//        } else {
//            throw new IllegalArgumentException("해당 이력서 코드가 존재하지 않습니다.");
//        }
//    }
}
