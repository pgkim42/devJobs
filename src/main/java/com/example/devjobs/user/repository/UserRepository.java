package com.example.devjobs.user.repository;

import com.example.devjobs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUserId(String userId);

    User findByUserCode(String userCode);

    User findByUserId(String userId);
}


