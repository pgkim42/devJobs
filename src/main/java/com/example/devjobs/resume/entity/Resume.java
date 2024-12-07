package com.example.devjobs.resume.entity;

import com.example.devjobs.common.file.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "resume")
public class Resume extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resumeCd; // 이력서 코드

    @Column(length = 255)
    private String workExperience; // 경력

    @Column(length = 255)
    private String education; // 학력

    // columnDefinition은 해당 컬럼을 JSON 형태로 저장하겠다고 명시하는 것
    @Column(columnDefinition = "json")
    private String certifications; // 자격증 (JSON 형태로 저장)

    @Column(name = "language_skills", columnDefinition = "json")
    private String languageSkills; // 언어 능력 (JSON 형태로 저장)

    @Column(length = 255)
    private String skill; // 스킬

    @Column(name = "upload_file_name", length = 255)
    private String uploadFileName; // 이력서 파일 (파일명 또는 경로)

}
