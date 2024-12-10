package com.example.devjobs.jobposting.controller;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import com.example.devjobs.jobposting.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/jobposting")
public class JobPostingController {

    @Autowired
    private JobPostingService service;

    @Autowired
    private JobPostingRepository repository;


    @PostMapping("/register")
    public ResponseEntity<Integer> register(
            JobPostingDTO dto,
            @RequestParam(value = "jobPostingFolder", required = false) MultipartFile jobPostingFolder) {
        try {
            System.out.println("Incoming DTO: " + dto);
            System.out.println("Incoming File: " + (jobPostingFolder != null ? jobPostingFolder.getOriginalFilename() : "No File"));

            int no = service.register(dto, jobPostingFolder);
            System.out.println("JobPosting registered with ID: " + no);

            return new ResponseEntity<>(no, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error during JobPosting registration: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            @RequestParam(required = false) String skill,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime postingDeadline
    ) {
        try {
            // 서비스 메소드 호출
//            service.modify(jobCode, title, content, recruitJob, recruitField, salary,
//                    postingStatus, workExperience, tag, jobCategory, skill,
//                    postingDeadline, uploadFile, lastUpdated);

            // 서비스 계층 호출
            service.modify(jobCode, title, content, recruitJob, recruitField, salary,
                    postingStatus, workExperience, tag, jobCategory, skill, postingDeadline, uploadFile, LocalDateTime.now());

            return new ResponseEntity<>("JobPosting updated successfully.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update JobPosting.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove/{code}")
    public ResponseEntity remove(@PathVariable("code") Integer code) {
        service.remove(code);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
