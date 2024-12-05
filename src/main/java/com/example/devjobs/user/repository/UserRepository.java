package com.example.devjobs.user.repository;

import com.example.devjobs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUserCode (String userCode);

    boolean existsByNickname(String nickname);

    boolean existsByUserId(String userId);

    User findByUserId(String userId);



}

