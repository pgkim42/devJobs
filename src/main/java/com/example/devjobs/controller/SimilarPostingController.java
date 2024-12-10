package com.example.devjobs.controller;

import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.similarPosting.service.SimilarPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/simlilarposting")
public class SimilarPostingController {

    @Autowired
    private SimilarPostingService similarPostingService;

    // ResponseEntity의 제네릭 타입을 ?로 변경하여 반환할 수 있는 데이터 타입을 유연하게 처리
    @GetMapping("/similar/{resumeCd}")
    public ResponseEntity<?> similarPostings(@PathVariable Integer resumeCd) {
        // 유사 공고 추천
        List<JobPosting> similar = similarPostingService.recommendSimilarPostings(resumeCd);

        // 유사 공고가 없을 경우 텍스트 메시지 반환
        if (similar.isEmpty()) {
            return new ResponseEntity<>("추천된 유사 공고가 없습니다.", HttpStatus.OK);
        }

        return new ResponseEntity<>(similar, HttpStatus.OK);

    }

}
