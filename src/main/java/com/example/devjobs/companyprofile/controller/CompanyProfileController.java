package com.example.devjobs.companyprofile.controller;

import com.example.devjobs.companyprofile.dto.CompanyProfileDTO;
import com.example.devjobs.companyprofile.repository.CompanyProfileRepository;
import com.example.devjobs.companyprofile.service.CompanyProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/companyprofile")
public class CompanyProfileController {

    @Autowired
    private CompanyProfileService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestPart("dto") String dtoJson,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompanyProfileDTO dto = objectMapper.readValue(dtoJson, CompanyProfileDTO.class);
            int id = service.register(dto, logoFile);
            return ResponseEntity.status(HttpStatus.CREATED).body("등록 성공 (ID: " + id + ")");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/modify")
    public ResponseEntity<String> modify(
            @RequestPart("dto") String dtoJson,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompanyProfileDTO dto = objectMapper.readValue(dtoJson, CompanyProfileDTO.class);
            service.modify(dto, logoFile);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove/{code}")
    public ResponseEntity<String> remove(@PathVariable("code") int code) {
        try {
            service.remove(code);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
