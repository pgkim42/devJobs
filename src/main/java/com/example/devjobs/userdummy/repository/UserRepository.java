package com.example.devjobs.userdummy.repository;


import com.example.devjobs.userdummy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}