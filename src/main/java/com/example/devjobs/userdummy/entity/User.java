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
    private Integer userId; // 사용자 ID

    @Column(name = "job_preferences", nullable = false)
    private String jobPreferences; // 선호 직무

    @Column(name = "skills", nullable = false)
    private String skills; // 스킬 (쉼표로 구분된 문자열)

    @Column(name = "experience_level", nullable = false)
    private Integer experienceLevel; // 경력 수준 (년수)
}