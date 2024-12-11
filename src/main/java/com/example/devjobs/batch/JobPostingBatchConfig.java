package com.example.devjobs.batch;

import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration // Spring Batch 설정 클래스
public class JobPostingBatchConfig {

    @Autowired
    private JobRepository jobRepository; // Job 저장소 주입 받기

    @Autowired
    private PlatformTransactionManager manager; // 트랜잭션 관리를 위해 주입 받기

    @Autowired
    private JobPostingRepository jobPostingRepository; // JobPosting 데이터베이스 작업을 처리하기 위해 Repository를 주입받습니다.

    // Tasklet: 스텝에서 하나의 작업만 처리하는 방식
    // Tasklet 정의: 마감일이 지난 공고의 상태를 'false'로 변경하는 작업
//    @Bean
//    public Tasklet




}
