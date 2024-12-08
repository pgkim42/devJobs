package com.example.devjobs.applications.repository;

import com.example.devjobs.applications.entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationsRepository extends JpaRepository<Applications, Integer> {
}
