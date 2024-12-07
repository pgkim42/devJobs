package com.example.devjobs.resume.service;

import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.resume.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    ResumeRepository repository;

    @Override
    public int register(ResumeDTO dto) {
        Resume entity = dtoToEntity(dto);



        return 0;
    }
}
