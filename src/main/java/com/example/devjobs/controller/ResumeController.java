package com.example.devjobs.controller;

import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.repository.ResumeRepository;
import com.example.devjobs.resume.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService service;

    @Autowired
    private ResumeRepository repository;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(ResumeDTO dto, @RequestParam(value = "resumeFolder", required = false)MultipartFile resumeFolder){

        int no = service.register(dto, resumeFolder);
        return new ResponseEntity<>(no, HttpStatus.CREATED);

    }

}
