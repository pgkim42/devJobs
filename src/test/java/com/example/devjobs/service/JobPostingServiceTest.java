package com.example.devjobs.service;

import com.example.devjobs.jobposting.dto.JobPostingDTO;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import com.example.devjobs.jobposting.service.JobPostingService;
import com.example.devjobs.jobposting.service.JobPostingServiceImpl;
import com.example.devjobs.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@SpringBootTest
public class JobPostingServiceTest {

    @Autowired
    private JobPostingServiceImpl service;

    @Test
    void JobPostingServiceTest() throws IOException {
        // 실제 파일 경로
        Path realFilePath = Paths.get("C:\\upload\\test.jpg");

        // 실제 파일 객체 생성
        File file = realFilePath.toFile();

        // MockMultipartFile 생성 (MultipartFile로 변환)
        MultipartFile mockFile = new MockMultipartFile(
                "jobPostingFolder", // 파라미터 이름
                file.getName(),     // 파일 이름
                "image/jpeg",       // MIME 타입
                Files.readAllBytes(file.toPath()) // 파일 데이터
        );

        // JobPostingDTO 생성
        JobPostingDTO dto = JobPostingDTO.builder()
                .title("공고 테스트")
                .content("내용 테스트")
                .recruitJob("모집 직종")
                .recruitField(5)
                .salary("추후협의")
                .postingDeadline(null)
                .postingStatus("OPEN")
                .workExperience("경력")
                .tag("Java")
                .jobCategory("Backend")
                .build();

        // register 메서드 호출
        int result = service.register(dto, mockFile);

        // 결과 출력 (또는 검증)
        System.out.println("등록된 JobCode: " + result);
    }
}