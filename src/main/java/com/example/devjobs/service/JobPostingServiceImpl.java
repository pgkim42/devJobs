package com.example.devjobs.service;

import com.example.devjobs.dto.JobPostingDTO;
import com.example.devjobs.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Override
    public int register(JobPostingDTO dto) {



        return 0;
    }
}
