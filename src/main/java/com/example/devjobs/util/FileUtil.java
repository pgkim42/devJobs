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

    // 파일 경로는 환경에 따라 바뀔 수 있으므로
    // 환경파일에 저장하고 클래스에서 불러다 써야함!
    // 예: 로컬 컴퓨터 - c:/uploadfile
    //     서버 컴퓨터 - d:/imagefile

    // 이미지 파일을 저장할 경로
    @Value("${filepath}")
    private String filepath;

    // 파일 업로드 메서드 (단일 파일 업로드)
    public String fileUpload(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        try {
            // 파일 경로와 이름 설정
            Path copyOfLocation = Paths.get(filepath + File.separator + StringUtils.cleanPath(multipartFile.getOriginalFilename()));

            // 파일을 지정한 경로에 저장
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);

            // 업로드된 파일 이름 반환
            return multipartFile.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 여러 파일 업로드 메서드 (다수 파일 업로드)
    public List<String> fileUploadMultiple(List<MultipartFile> multipartFiles) {
        List<String> uploadedFileNames = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String fileName = fileUpload(file);
            if (fileName != null) {
                uploadedFileNames.add(fileName);
            }
        }

        return uploadedFileNames;
    }
}
