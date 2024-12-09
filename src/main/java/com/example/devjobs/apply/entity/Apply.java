package com.example.devjobs.apply.entity;

import com.example.devjobs.common.file.BaseEntity;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "apply")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "createdDate", column = @Column(name = "submission_date"))  // BaseEntity의 createdDate를 submission_date로 덮어쓰기
public class Apply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_code")
    private Integer applyCode;

    @ManyToOne
    @JoinColumn(name = "job_code", referencedColumnName = "job_code")
    private JobPosting jobCode;  // 공고 코드

    @Enumerated(EnumType.STRING)
    @Column(name = "apply_status", columnDefinition = "ENUM('AVAILABLE', 'COMPLETED')")
    private ApplyStatus applyStatus;  // 지원 상태

    @ManyToOne
    @JoinColumn(name = "resume_cd", referencedColumnName = "resume_cd")
    private Resume resumeCd;  // 이력서 코드
}