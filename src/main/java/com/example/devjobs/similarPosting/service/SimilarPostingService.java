package com.example.devjobs.similarPosting.service;

import com.example.devjobs.jobposting.entity.JobPosting;

import java.util.List;

public interface SimilarPostingService {

    List<JobPosting> recommendSimilarPostings(Integer resumeCode);

}
