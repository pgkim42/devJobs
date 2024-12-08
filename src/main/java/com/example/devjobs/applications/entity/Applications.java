package com.example.devjobs.applications.entity;

import com.example.devjobs.common.file.BaseEntity;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "createdDate", column = @Column(name = "posting_date"))  // BaseEntity의 createdDate를 posting_date로 덮어쓰기
public class Applications extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applications_code")
    private Integer applicationsCode;

    @ManyToOne
    @JoinColumn(name = "job_code", referencedColumnName = "job_code")
    private JobPosting jobPosting;  // 공고 코드

    @Enumerated(EnumType.STRING)
    @Column(name = "applications_status")
    private ApplicationsStatus applicationsStatus;  // 지원 상태

    @CreationTimestamp
    @Column(name = "submission_date")
    private LocalDateTime submissionDate;  // 지원서 제출일 (BaseEntity에서 관리된 등록일)

    @ManyToOne
    @JoinColumn(name = "resume_code", referencedColumnName = "resume_code")
    private Resume resume;  // 이력서 코드
}