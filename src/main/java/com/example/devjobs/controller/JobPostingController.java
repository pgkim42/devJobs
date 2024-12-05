package com.example.devjobs.controller;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import com.example.devjobs.jobposting.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Integer> register(JobPostingDTO dto) {

//        dto.setTitle(principal.getName());
        int no = service.register(dto);
        return new ResponseEntity<>(no, HttpStatus.CREATED);

    }

    @GetMapping("/list")
    public ResponseEntity<List<JobPostingDTO>> list() {
        List<JobPostingDTO> list = service.getList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/read")
    public ResponseEntity<JobPostingDTO> read(@RequestParam(name = "jobCode") Integer jobCode) {

        System.out.println("직업코드: " + jobCode);

        JobPostingDTO dto = service.read(jobCode);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // 게시물 전체 수정(모든 데이터 입력)
    @PutMapping("/modify")
    public ResponseEntity<String> modify(JobPostingDTO dto) {
        try {
            // JobPostingService에서 수정 처리
            service.modify(dto);

            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
    }

    // 게시글 개별 수정
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
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime postingDeadline
    ) {
        // jobCode가 null인 경우 400 Bad Request 반환
//        if (jobCode == null) {
//            return new ResponseEntity<>("JobCode를 확인하세요.", HttpStatus.BAD_REQUEST);
//        }

        try {
            service.modifyPartial(jobCode, title, content, recruitJob, recruitField, salary,
                    postingStatus, workExperience, tag, jobCategory,
                    postingDeadline, uploadFile);

            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(Integer jobCode) {
        if (jobCode == null){
            return new ResponseEntity<>("JobCode가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        try {
            service.delete(jobCode);
            return new ResponseEntity<>("Job posting deleted successfully.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete the job posting.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
