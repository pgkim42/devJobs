package com.example.devjobs.jobposting.repository;

import com.example.devjobs.jobposting.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Integer> {

    // 공고마감일이 현재 시간 이전이고, 공고상태가 true인 공고 조회
    List<JobPosting> findByPostingDeadlineBeforeAndPostingStatusTrue(LocalDateTime now);

}
