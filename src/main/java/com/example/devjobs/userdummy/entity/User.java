package com.example.devjobs.userdummy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dummy_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userCd; // 사용자 ID

    @Column(name = "job_category", nullable = false)
    private String jobCategory; // 직무

    @Column(name = "skills", nullable = false)
    private String skills; // 스킬 (쉼표로 구분된 문자열)

    @Column(name = "work_experience", nullable = false)
    private Integer workExperience; // 경력
}