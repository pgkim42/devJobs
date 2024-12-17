package com.example.devjobs.apply.repository;

import com.example.devjobs.apply.entity.Apply;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Integer> {

    List<Apply> findByUserCode(User user);

    // 파라미터를 기초타입x 엔티티타입으로 전달
//    Optional<Apply> findByJobCodeAndUserCode(Integer jobCode, String userCode);
//    Optional<Apply> findByJobCodeAndUserCode(@Param("jobCode") JobPosting jobCode, @Param("userCode") User userCode);

    List<Apply> findByJobCodeAndUserCode(@Param("jobCode") JobPosting jobCode, @Param("userCode") User userCode);

}
