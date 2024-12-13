package com.example.devjobs.companyprofile.repository;

import com.example.devjobs.companyprofile.entity.CompanyProfile;
import com.example.devjobs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Integer> {

    CompanyProfile findByUser(User user);


}
