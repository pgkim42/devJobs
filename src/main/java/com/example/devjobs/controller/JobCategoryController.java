package com.example.devjobs.controller;

import com.example.devjobs.jobcategory.dto.JobCategoryDTO;
import com.example.devjobs.jobcategory.entity.JobCategory;
import com.example.devjobs.jobcategory.service.JobCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobcategory")
public class JobCategoryController {

    @Autowired
    JobCategoryService service;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody JobCategoryDTO dto) {
        int jobCategory = service.register(dto);
        return new ResponseEntity<>(jobCategory, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<JobCategoryDTO>> list() {
        List<JobCategoryDTO> list = service.getList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity put(@RequestBody JobCategoryDTO dto) {
        service.modify(dto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/remove/{cd}")
    public ResponseEntity remove(@PathVariable("cd") int cd) {
        service.remove(cd);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
