package com.example.devjobs.apply.repository;

import com.example.devjobs.apply.entity.Apply;
import com.example.devjobs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Integer> {

    List<Apply> findByUserCode(User user);

}
