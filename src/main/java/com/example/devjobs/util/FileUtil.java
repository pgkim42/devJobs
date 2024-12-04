package com.example.devjobs.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtil {

    @Value("${filepath}")
    private String filepath;  // 파일 저장 경로

    // 파일 업로드 메서드 (단일 파일 업로드)
    public String fileUpload(MultipartFile multipartFile, String imgDirectory) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        try {
            // imgDirectory가 절대경로라면 filepath와 결합하지 않음
            Path uploadDirectory;
            if (imgDirectory.startsWith("C:\\") || imgDirectory.startsWith("/")) {
                // 절대경로가 이미 주어졌다면, 그 경로를 그대로 사용
                uploadDirectory = Paths.get(imgDirectory);
            } else {
                // 상대경로라면, filepath와 결합
                uploadDirectory = Paths.get(filepath, imgDirectory);
            }

            // 디렉토리가 존재하지 않으면 생성
            if (!Files.exists(uploadDirectory)) {
                Files.createDirectories(uploadDirectory); // 부모 디렉토리까지 함께 생성
            }

            // 파일 경로 설정
            Path copyOfLocation = uploadDirectory.resolve(StringUtils.cleanPath(multipartFile.getOriginalFilename()));

            // 파일을 지정한 경로에 저장
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);

            // 업로드된 파일 이름 반환
            return multipartFile.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}