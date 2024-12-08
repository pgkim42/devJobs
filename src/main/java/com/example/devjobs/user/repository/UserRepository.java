package com.example.devjobs.user.repository;

import com.example.devjobs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUserCode (String userCode);

    boolean existsByUserId(String userId);

    boolean existsByCompanyCode(String companyCode);

    User findByUserId(String userId);

}
