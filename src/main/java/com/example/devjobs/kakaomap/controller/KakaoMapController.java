package com.example.devjobs.kakaomap.controller;

import com.example.devjobs.kakaomap.service.KakaoMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoMapController {

    @Autowired
    private KakaoMapService kakaoMapService;

    @GetMapping("/address-to-coordinates")
    public ResponseEntity<?> getCoordinates(@RequestParam String address) {
        String result = kakaoMapService.getCoordinates(address);
        return ResponseEntity.ok(result); // JSON 응답 반환
    }
}