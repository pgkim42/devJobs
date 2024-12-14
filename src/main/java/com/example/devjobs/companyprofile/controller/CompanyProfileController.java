package com.example.devjobs.companyprofile.controller;

import com.example.devjobs.companyprofile.dto.CompanyProfileDTO;
import com.example.devjobs.companyprofile.dto.CompanyProfileUpdateDTO;
import com.example.devjobs.companyprofile.entity.CompanyProfile;
import com.example.devjobs.companyprofile.repository.CompanyProfileRepository;
import com.example.devjobs.companyprofile.service.CompanyProfileService;
import com.example.devjobs.util.FileUtil;
import com.example.devjobs.util.S3FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/companyprofile")
public class CompanyProfileController {

    @Autowired
    private CompanyProfileService service;

//    @Autowired
//    private FileUtil fileUtil;

    @Autowired
    private S3FileUtil fileUtil;

    @Value("${file.upload-dir:C:/uploadfile/}") // 기본 파일 저장 경로
    private String fileUploadDir;

    @Autowired
    private CompanyProfileRepository companyProfileRepository;

    // userCode기반으로 companyProfileCode 반환하는 API
    @GetMapping("/current")
    public ResponseEntity<Integer> getCurrentCompanyProfileCode() {
        try {
            int companyProfileCode = service.getCurrentCompanyProfileCode();
            return ResponseEntity.ok(companyProfileCode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/logo")
    public ResponseEntity<?> uploadLogo(
            @RequestParam("logo") MultipartFile logo,
            @RequestParam("companyProfileCode") Integer companyProfileCode) {

        if (logo.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }

        if (logo.getSize() > 10 * 1024 * 1024) { // 10MB 크기 제한
            return ResponseEntity.badRequest().body("파일 크기는 최대 10MB까지 가능합니다.");
        }

        try {
            // 회사 프로필 코드로 회사 프로필 조회
            CompanyProfile companyProfile = companyProfileRepository.findById(companyProfileCode)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회사 프로필 코드입니다."));

            // 파일 업로드
            String savedPath = fileUtil.fileUpload(logo);
            if (savedPath == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
            }

            companyProfile.setUploadFileName(savedPath);
            companyProfileRepository.save(companyProfile);

            return ResponseEntity.ok(Map.of("logoUrl", savedPath));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로고 업로드 중 오류 발생");
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

    @PutMapping("/modify")
    public ResponseEntity<String> modify(@RequestBody CompanyProfileUpdateDTO updateDTO) {
        try {
            System.out.println("수신한 수정 요청 데이터: {}" + updateDTO);
            service.modify(updateDTO); // Service 호출
            return ResponseEntity.noContent().build(); // 성공 시 204 반환
        } catch (Exception e) {
            System.out.println("기업 정보 수정 중 오류: {}" + e.getMessage());
            return ResponseEntity.badRequest().body("수정 중 오류 발생: " + e.getMessage());
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
