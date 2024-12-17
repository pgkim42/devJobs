package com.example.devjobs.apply.controller;

import com.example.devjobs.apply.dto.ApplyDTO;
import com.example.devjobs.apply.entity.Apply;
import com.example.devjobs.apply.service.ApplyService;
import com.example.devjobs.user.entity.User;
import com.example.devjobs.user.service.AuthService;
import com.example.devjobs.user.service.UserService;
import com.example.devjobs.user.service.implement.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apply")
public class ApplyController {

    @Autowired
    ApplyService service;
    @Autowired
    private ApplyService applyService;

    @Autowired
    UserService userService;

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
    @PostMapping("applyto/{jobCode}")
    public ResponseEntity<String> applyTo(
            @PathVariable Integer jobCode,
            @RequestParam Integer resumeCode,
            Principal principal) {

        // 인증된 사용자 ID 가져오기
        // principal.getName()을 통해 사용자 ID를 가져올 수 있음 + 사용자 유저는 앞에 dev_ 붙여야함
        String userCode = "dev_" + principal.getName();


        try {
            if (applyService.isDuplicateApplication(jobCode, userCode)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 지원한 공고입니다."); // 409 Conflict
            }

            // 지원하기 서비스 호출
            applyService.applyTo(jobCode, userCode, resumeCode);
            return ResponseEntity.ok("지원이 성공적으로 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            // 잘못된 입력 처리
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 기타 서버 오류 처리
        } return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("지원 처리 중 오류가 발생했습니다.");

    }

    // 로근인한 유저의 지원현황
    @GetMapping("myApply")
    public ResponseEntity<List<Map<String, Object>>> getMyApply(Principal principal) {
        try {
            // 로그인된 유저의 userCode 가져오기
            String userCode = principal.getName();

            // 서비스 호출하여 지원 내역 조회
            List<Map<String, Object>> applyList = applyService.getMyApplyList(userCode);

            return ResponseEntity.ok(applyList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
