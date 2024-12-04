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

    @Autowired
    private JobPostingRepository repository;

    @Autowired
    private FileUtil fileUtil;

    @Test
    void JobPostingServiceTest() throws IOException {

        // 실제경로
        Path realFilePath = Paths.get("C:\\upload\\test.jpg");

        //실제 파일ㅇ르 객체로 불러오기
        File file = realFilePath.toFile();

        // MultipartFile로 변환
        MultipartFile mockFile = new MockMultipartFile(
                "file",  // 파라미터 이름
                file.getName(),
                "image/jpeg", // MIME 타입(예시)
                Files.readAllBytes(file.toPath()) //파일 내용
        );

        JobPostingDTO dto = JobPostingDTO.builder()
                .title("공고 테스트")
                .description("내용 테스트")
                .recruitJob("모집 직종")
                .recruitField(5)
                .salary("추후협의")
                .postingDeadline(null)
                .postingStatus("OPEN")
                .workExprerience("경력")
                .tag("Java")
                .jobCategory("Backend")
                .uploadFile(mockFile) // 실제 파일을 DTO에 포함시킴
                .imgDirectory("C:\\upload") // 실제 파일이 저장된 디렉토리
                .imgFileName(file.getName())
                .build();

        int result = service.register(dto);

    }


}
