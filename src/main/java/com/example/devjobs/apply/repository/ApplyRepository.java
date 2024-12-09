package com.example.devjobs.apply.repository;

import com.example.devjobs.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Integer> {
}
