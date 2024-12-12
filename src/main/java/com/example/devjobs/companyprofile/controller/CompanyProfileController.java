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
    CompanyProfileService service;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(
            @RequestPart("dto") String dtoJson,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile) {
        try {
            // JSON 문자열을 DTO로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            CompanyProfileDTO dto = objectMapper.readValue(dtoJson, CompanyProfileDTO.class);

            // 서비스 호출
            int companyProfile = service.register(dto, logoFile);

            System.out.println("File Received: " + (logoFile != null ? logoFile.getOriginalFilename() : "No File"));
            return new ResponseEntity<>(companyProfile, HttpStatus.CREATED); // 성공시 201 코드
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 실패시 400 코드
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<CompanyProfileDTO>> list() {
        List<CompanyProfileDTO> list = service.getList();
        return new ResponseEntity<>(list, HttpStatus.OK); // 성공시 200

    }

    // @requestparam보다는 @pathvariable이 적합(restful api설계 원칙에 부합)
    @GetMapping("/read/{code}")
    public ResponseEntity<CompanyProfileDTO> read(@PathVariable int code){
        CompanyProfileDTO dto = service.read(code);
        return new ResponseEntity<>(dto, HttpStatus.OK); // 성공시 200
    }

//    @RequestPart**를 사용하면 Postman에서 전송하는 JSON 데이터를 반드시 파싱해야 컨트롤러가 이를 DTO로 처리
    @PutMapping("/modify")
    public ResponseEntity<String> modify(
            @RequestPart(value = "dto") String dtoJson,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile) {
        try {
            // JSON 문자열을 DTO 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            CompanyProfileDTO dto = objectMapper.readValue(dtoJson, CompanyProfileDTO.class);

            // 서비스 호출
            service.modify(dto, logoFile);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 성공 시 204
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN); // 작성자 불일치 403
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 잘못된 요청 400
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR); // 서버 에러 500
        }
    }

    // PathVariable이 더 직관적이고 RESTful한 방식(param 보다)
    @DeleteMapping("/remove/{code}")
    public ResponseEntity<String> remove(@PathVariable("code") int code) {
        try {
            service.remove(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 성공시 204
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN); // 작성자 불일치 403
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 잘못된 요청 400
        }
    }

}
