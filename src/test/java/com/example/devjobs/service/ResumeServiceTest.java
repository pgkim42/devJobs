package com.example.devjobs.service;

import com.example.devjobs.resume.dto.CertificationsDTO;
import com.example.devjobs.resume.dto.LanguagesSkillsDTO;
import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.resume.repository.ResumeRepository;
import com.example.devjobs.resume.service.ResumeService;
import com.example.devjobs.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ResumeServiceTest {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ResumeRepository resumeRepository;

    @Test
    void testRegisterResume() {
        // Resume 등록
        ResumeDTO resumeDTO = ResumeDTO.builder()
                .workExperience("5 years in Software Development")
                .education("Bachelor's Degree in Computer Science")
                .certifications(List.of(
                        CertificationsDTO.builder().certificationName("AWS Certified").issueDate("2022-05").issuer("Amazon").build(),
                        CertificationsDTO.builder().certificationName("Java SE 11").issueDate("2021-11").issuer("Oracle").build()
                ))
                .languageSkills(List.of(
                        LanguagesSkillsDTO.builder().language("English").level("Advanced").build(),
                        LanguagesSkillsDTO.builder().language("Korean").level("Intermediate").build()
                ))
                .skill("Java,Spring,SQL")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        int resumeCd = resumeService.register(resumeDTO, null);
        ResumeDTO savedResume = resumeService.read(resumeCd);

        System.out.println("=== Resume 등록 테스트 ===");
        System.out.println("Resume Code: " + savedResume.getResumeCode());
        System.out.println("Work Experience: " + savedResume.getWorkExperience());
        System.out.println("Certifications: " + savedResume.getCertifications());
        System.out.println("Languages: " + savedResume.getLanguageSkills());

        assertThat(savedResume).isNotNull();
        assertThat(savedResume.getWorkExperience()).isEqualTo("5 years in Software Development");
    }

    @Test
    void testReadResume() {
        // Resume 조회
        List<Resume> resumes = resumeRepository.findAll();
        System.out.println("=== Resume 조회 테스트 ===");
        System.out.println("Total Resumes: " + resumes.size());

        for (Resume resume : resumes) {
            System.out.println("Resume Code: " + resume.getResumeCd());
            System.out.println("Work Experience: " + resume.getWorkExperience());
        }

        assertThat(resumes).isNotEmpty();
    }

    @Test
    void testModifyResume() {
        // Resume 등록
        ResumeDTO resumeDTO = ResumeDTO.builder()
                .workExperience("2 years in QA")
                .education("Master's Degree in Information Technology")
                .skill("Python, Selenium")
                .certifications(List.of(
                        CertificationsDTO.builder().certificationName("ISTQB Certified Tester").issueDate("2020-12").issuer("ISTQB").build()
                ))
                .languageSkills(List.of(
                        LanguagesSkillsDTO.builder().language("English").level("Fluent").build()
                ))
                .build();

        int resumeCd = resumeService.register(resumeDTO, null);

        // 수정
        resumeService.modify(
                resumeCd,
                "4 years in Automation Testing",
                null,
                "Python, Selenium, Java",
                JsonUtil.convertListToJson(List.of(
                        CertificationsDTO.builder().certificationName("Certified Scrum Master").issueDate("2022-01").issuer("Scrum Alliance").build()
                )),
                JsonUtil.convertListToJson(List.of(
                        LanguagesSkillsDTO.builder().language("French").level("Beginner").build()
                )),
                null,
                LocalDateTime.now()
        );

        ResumeDTO modifiedResume = resumeService.read(resumeCd);

        System.out.println("=== Resume 수정 테스트 ===");
        System.out.println("Updated Work Experience: " + modifiedResume.getWorkExperience());
        System.out.println("Updated Skills: " + modifiedResume.getSkill());
        System.out.println("Updated Certifications: " + modifiedResume.getCertifications());
        System.out.println("Updated Languages: " + modifiedResume.getLanguageSkills());

        assertThat(modifiedResume.getWorkExperience()).isEqualTo("4 years in Automation Testing");
        assertThat(modifiedResume.getSkill()).isEqualTo("Python, Selenium, Java");
    }

    @Test
    void testRemoveResume() {
        // Resume 등록
        ResumeDTO resumeDTO = ResumeDTO.builder()
                .workExperience("1 year in Web Development")
                .education("Diploma in Web Design")
                .skill("HTML, CSS, JavaScript")
                .build();

        int resumeCd = resumeService.register(resumeDTO, null);

        // 삭제
        resumeService.remove(resumeCd);

        Optional<Resume> deletedResume = resumeRepository.findById(resumeCd);

        System.out.println("=== Resume 삭제 테스트 ===");
        System.out.println("Deleted Resume Exists: " + deletedResume.isPresent());

        assertThat(deletedResume).isEmpty();
    }

    @Test
    void testGetListResumes() {
        // 리스트 조회
        List<ResumeDTO> resumeList = resumeService.getList();

        System.out.println("=== Resume 리스트 조회 테스트 ===");
        System.out.println("Total Resumes: " + resumeList.size());

        for (ResumeDTO dto : resumeList) {
            System.out.println("Resume Code: " + dto.getResumeCode());
            System.out.println("Work Experience: " + dto.getWorkExperience());
        }

        assertThat(resumeList).isNotEmpty();
    }
}