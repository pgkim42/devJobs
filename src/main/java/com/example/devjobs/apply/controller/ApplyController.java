package com.example.devjobs.apply.controller;

import com.example.devjobs.apply.dto.ApplyDTO;
import com.example.devjobs.apply.service.ApplyService;
import com.example.devjobs.user.service.implement.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
public class ApplyController {

    @Autowired
    ApplyService service;
    @Autowired
    private ApplyService applyService;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody ApplyDTO dto) {

        int Apply = service.register(dto);
        return new ResponseEntity<>(Apply, HttpStatus.CREATED); // 성공시 201

    }

    @GetMapping("/list")
    public ResponseEntity<List<ApplyDTO>> getUserApplications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        String currentUserName = authentication.getName(); // 현재 로그인한 사용자의 ID 가져오기
        List<ApplyDTO> applications = service.getUserApplications(currentUserName); // 사용자의 지원서 목록 조회

        return new ResponseEntity<>(applications, HttpStatus.OK); // 200 OK
    }

//    @GetMapping("/list")
//    public ResponseEntity<List<ApplyDTO>> getList() {
//        List<ApplyDTO> list = service.getList();
//        return new ResponseEntity<>(list, HttpStatus.OK); // 성공시 200
//    }

    @GetMapping("/read/{code}")
    public ResponseEntity<ApplyDTO> read(@PathVariable int code) {
        ApplyDTO dto = service.read(code);
        return new ResponseEntity(dto, HttpStatus.OK); // 성공시 204
    }

    @PutMapping("/modify")
    public ResponseEntity modify(@RequestBody ApplyDTO dto) {
        service.modify(dto);
        return new ResponseEntity(dto, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/remove/{code}")
    public ResponseEntity remove(@PathVariable("code") Integer code) {
        service.remove(code);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 공고 지원 api
    @PostMapping("applyto/{code}")
    public ResponseEntity<String> applyTo(
            @PathVariable("code") Integer jobCode,
            @RequestBody ApplyDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("사용자가 인증되지 않았습니다.");
        }

        // SecurityContext에서 Principal 추출
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            throw new IllegalArgumentException("인증된 사용자 정보를 찾을 수 없습니다.");
        }

        // UserDetailsImpl에서 userCode 추출
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        String userCode = userDetails.getUser().getUserCode();

        applyService.applyTo(jobCode, userCode, dto);
        return ResponseEntity.ok("지원이 완료 되었습니다.");
    }
}
