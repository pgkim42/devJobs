package com.example.devjobs.resume.repository;

import com.example.devjobs.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {

    // userCode를 기준으로 Resume 엔티티 조회
    List<Resume> findByUserCode_UserCode(String userCode);

}
