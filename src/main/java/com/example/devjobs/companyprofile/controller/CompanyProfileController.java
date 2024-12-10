package com.example.devjobs.companyprofile.controller;

import com.example.devjobs.companyprofile.dto.CompanyProfileDTO;
import com.example.devjobs.companyprofile.repository.CompanyProfileRepository;
import com.example.devjobs.companyprofile.service.CompanyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companyprofile")
public class CompanyProfileController {

    @Autowired
    CompanyProfileService service;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody CompanyProfileDTO dto){
        int companyProfile = service.register(dto);
        return new ResponseEntity<>(companyProfile, HttpStatus.CREATED); // 성공시 201 코드
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

    // 첨부파일 없어서 그냥 JSON으로, postman에서 code값 넣어주고 호출 해야함
    @PutMapping("/modify")
    public ResponseEntity<String> modify(@RequestBody CompanyProfileDTO dto) {
        try {
            service.modify(dto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 성공시 204
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN); // 작성자 불일치 403
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 잘못된 요청 400
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
