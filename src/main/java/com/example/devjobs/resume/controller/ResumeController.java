package com.example.devjobs.resume.controller;

import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.repository.ResumeRepository;
import com.example.devjobs.resume.service.ResumeService;
import com.example.devjobs.util.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService service;

    @Autowired

    private ResumeRepository repository;

    @Autowired
    private FileUtil fileUtil;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(
            @RequestPart("dto") String dtoJson, // DTO JSON 데이터를 String으로 받음 // required = false (필수적이지 않음)
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

    @GetMapping("/list")
    public ResponseEntity<List<ResumeDTO>> list(){
        List<ResumeDTO> list = service.getList();
        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    // @requestparam보다는 @pathvariable이 적합(restful api설계 원칙에 부합)
    @GetMapping("/read/{code}")
    public ResponseEntity<ResumeDTO> read(@PathVariable int code){

        System.out.println("이력서코드:" + code);

        ResumeDTO dto = service.read(code);
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @PatchMapping("/modify")
    public ResponseEntity<String> modify(
            @RequestParam(required = false) Integer resumeCode,  // 이력서 코드 (Integer)
            @RequestParam(required = false) String workExperience,  // 경력 (String)
            @RequestParam(required = false) String education,  // 학력 (String)
            @RequestParam(required = false) String skill,  // 스킬 (String)
            @RequestParam(required = false) String jobCategory, // 직무
            @RequestParam(required = false) String certifications,  // 자격증 (JSON String)
            @RequestParam(required = false) String languageSkills,  // 언어 능력 (JSON String)
            @RequestParam(required = false) MultipartFile uploadFileName,  // 이력서 파일 (MultipartFile)
            @RequestParam(required = false) LocalDateTime lastUpdated  // 마지막 수정일 (LocalDateTime)
    ) {
        if (resumeCode == null) {
            return new ResponseEntity<>("Resume Code is required.", HttpStatus.BAD_REQUEST);
        }

        try {
            // 서비스 메소드에서 여러 매개변수를 처리하도록 전달
            service.modify(resumeCode, workExperience, education, skill, certifications, languageSkills, uploadFileName, lastUpdated, jobCategory);

            return new ResponseEntity<>("Resume updated successfully.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update resume.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove/{code}")
    public ResponseEntity remove(@PathVariable("code") int code){
        service.remove(code);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}