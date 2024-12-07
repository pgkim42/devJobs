package com.example.devjobs.controller;

import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.repository.ResumeRepository;
import com.example.devjobs.resume.service.ResumeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService service;

    @Autowired
    private ResumeRepository repository;


//    public ResponseEntity<Integer> register(@RequestBody ResumeDTO dto, @RequestParam(value = "resumeFolder", required = false) MultipartFile resumeFolder) {
//    public ResponseEntity<Integer> register(@RequestParam("dto") ResumeDTO dto, @RequestParam(value = "resumeFolder", required = false) MultipartFile resumeFolder) {
//        System.out.println("Received language_skills: " + dto.getLanguageSkills());

    @PostMapping("/register")
    public ResponseEntity<Integer> register(
            @RequestPart("dto") String dtoJson, // DTO JSON 데이터를 String으로 받음
            @RequestPart(value = "resumeFolder", required = false) MultipartFile resumeFolder
    ) {
        try {
            // JSON 문자열을 로그로 출력
            System.out.println("Received DTO JSON: " + dtoJson);

            // JSON 문자열을 ResumeDTO로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ResumeDTO dto = objectMapper.readValue(dtoJson, ResumeDTO.class);

            System.out.println("Parsed DTO: " + dto);

            if (resumeFolder != null && !resumeFolder.isEmpty()) {
                dto.setUploadFileName(resumeFolder.getOriginalFilename());
                System.out.println("Resume file name: " + resumeFolder.getOriginalFilename());
            }

            int no = service.register(dto, resumeFolder);
            return new ResponseEntity<>(no, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping("/list")
//    public ResponseEntity<List<ResumeDTO>> list(){
//        List<ResumeDTO> list = service.getList();
//        return new ResponseEntity<>(list, HttpStatus.OK);
//
//    }
//
//    @GetMapping("/read")
//    public ResponseEntity<ResumeDTO> read(@RequestParam(name = "no") Integer no){
//
//        System.out.println("이력서코드:" + no);
//
//        ResumeDTO dto = service.read(no);
//        return new ResponseEntity<>(dto, HttpStatus.OK);
//
//    }

    // dtoJson을 String타입으로 받고 objmapper로 변환해서..
    // 포스트맨에서 json형태로 value값 입력해줘야함(form-data에서)
//    @PutMapping("/modify")
//    public ResponseEntity modify(@RequestPart("dto") String dtoJson,
//                                 @RequestPart(value = "uploadFile", required = false) MultipartFile uploadFile) throws JsonProcessingException {
//
//        // JSON 문자열을 ResumeDTO 객체로 변환
//        ObjectMapper obj = new ObjectMapper();
//        ResumeDTO dto = obj.readValue(dtoJson, ResumeDTO.class);
//
//        // 파일이 존재하면 파일을 처리하도록 추가
//        if (uploadFile != null && !uploadFile.isEmpty()) {
//            dto.setUploadFile(uploadFile);  // MultipartFile을 DTO에 설정
//        }
//
//        service.modify(dto);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
