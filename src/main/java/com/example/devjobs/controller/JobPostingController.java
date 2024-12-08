package com.example.devjobs.controller;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import com.example.devjobs.jobposting.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jobposting")
public class JobPostingController {

    @Autowired
    private JobPostingService service;

    @Autowired
    private JobPostingRepository repository;

    @PostMapping("/register")
//    public ResponseEntity<Integer> register(@ModelAttribute JobPostingDTO dto, Principal principal) {
    // 없으면 폼데이터, @RequestBody-JSON
    public ResponseEntity<Integer> register(JobPostingDTO dto, @RequestParam(value = "jobPostingFolder", required = false) MultipartFile jobPostingFolder) {

//        dto.setTitle(principal.getName());
        int no = service.register(dto, jobPostingFolder);
        return new ResponseEntity<>(no, HttpStatus.CREATED);

    }

    @GetMapping("/list")
    public ResponseEntity<List<JobPostingDTO>> list() {
        List<JobPostingDTO> list = service.getList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/read")
    public ResponseEntity<JobPostingDTO> read(@RequestParam(name = "no") Integer no) {

        System.out.println("직업코드: " + no);

        JobPostingDTO dto = service.read(no);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PatchMapping("/modify")
    public ResponseEntity<String> modifyPartial(
            @RequestParam(required = false) Integer jobCode,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String recruitJob,
            @RequestParam(required = false) Integer recruitField,
            @RequestParam(required = false) String salary,
            @RequestParam(required = false) String postingStatus,
            @RequestParam(required = false) String workExperience,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String jobCategory,
            @RequestParam(required = false) MultipartFile uploadFile,
            @RequestParam(required = false) LocalDateTime lastUpdated,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime postingDeadline
    ) {
        try {
            // 서비스 메소드 호출
            service.modifyPartial(jobCode, title, content, recruitJob, recruitField, salary,
                    postingStatus, workExperience, tag, jobCategory,
                    postingDeadline, uploadFile, lastUpdated);

            return new ResponseEntity<>("JobPosting updated successfully.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update JobPosting.", HttpStatus.BAD_REQUEST);
        }
    }
}
