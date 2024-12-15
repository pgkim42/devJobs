package com.example.devjobs.kakaomap.controller;

import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.service.JobPostingService;
import com.example.devjobs.kakaomap.service.KakaoMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// react에서 호출하는 컨트롤러
@RestController
@RequestMapping("/kakao-map")
public class KakaoMapController {

    @Autowired
    private KakaoMapService kakaoMapService;

    @Autowired
    private JobPostingService jobPostingService;

   // 주소를 받아 좌표를 반환 [ @param address 사용자 입력 주소, @return 위도 및 경도 정보 ]
    @GetMapping("/coordinates/{jobCode}")
    public ResponseEntity<?> getCoordinatesForJobPosting(@PathVariable Integer jobCode) {
        Optional<JobPosting> jobPostingOptional = jobPostingService.getbyId(jobCode);
        if (jobPostingOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("구인공고를 찾을 수 없습니다.");
        }

        JobPosting jobPosting = jobPostingOptional.get();
        if (jobPosting.getAddress() == null || jobPosting.getAddress().isEmpty()) {
            return ResponseEntity.badRequest().body("구인공고에 등록 된 주소를 찾을 수 없습니다");
        }

        try {
            String coordinatesJson = kakaoMapService.getCoordinates(jobPosting.getAddress());
            return ResponseEntity.ok(coordinatesJson);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch coordinates: " + e.getMessage());
        }
    }


}