package com.example.devjobs.resume.service;

import com.example.devjobs.resume.dto.ResumeDTO;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.resume.repository.ResumeRepository;
import com.example.devjobs.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    ResumeRepository repository;

    @Autowired
    FileUtil fileUtil;

    @Override
    public int register(ResumeDTO dto, MultipartFile resumeFolder) {

        String fileName = null;

        if(dto.getUploadFile() != null && !dto.getUploadFile().isEmpty()) {
            fileName = fileUtil.fileUpload(dto.getUploadFile(), "resume");
            System.out.println("파일 업로드 테스트" + fileName);
        }

        Resume resume = dtoToEntity(dto);

        if(fileName != null) {
            resume.setUploadFileName(fileName);
        }

        repository.save(resume);

        return resume.getResumeCd();
    }
}
