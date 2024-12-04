package com.example.devjobs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "job_posting")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPosting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_code", nullable = false)
    int jobCode;  // 공고코드

    @Column(name = "title", nullable = false)
    String title;  // 공고제목

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    String description;  // 공고내용

    @Column(name = "recruit_job", nullable = false)
    String recruitJob;  // 모집직무

    @Column(name = "recruit_field", nullable = false)
    int recruitField;  // 모집인원

    @Column(name = "salary")
    String salary;  // 급여

    @Column(name = "posting_deadline")
    LocalDateTime postingDeadline;  // 공고마감일

    @Enumerated(EnumType.STRING)
    @Column(name = "posting_status", nullable = false)
    PostingStatus postingStatus;  // 공고상태

    @Column(name = "work_experience", nullable = false)
    String workExperience;  // 경력 (신입, 경력)

    @Column(name = "tag")
    String tag;  // 태그

    @Column(name = "job_category", nullable = false)
    String jobCategory;  // 직무 카테고리



    // 작성자
//    @ManyToOne
//    Members writer;  // 작성자

    // 기업프로필코드
//    @ManyToOne
//    CompanyProfile companyProfile;  // 기업프로필코드

}
