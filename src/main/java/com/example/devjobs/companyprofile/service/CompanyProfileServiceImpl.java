package com.example.devjobs.companyprofile.service;

import com.example.devjobs.companyprofile.dto.CompanyProfileDTO;
import com.example.devjobs.companyprofile.entity.CompanyProfile;
import com.example.devjobs.companyprofile.repository.CompanyProfileRepository;
import com.example.devjobs.user.entity.User;
import com.example.devjobs.user.repository.UserRepository;
import com.example.devjobs.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyProfileServiceImpl implements CompanyProfileService {

    @Autowired
    private CompanyProfileRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileUtil fileUtil;

    @Override
    public int register(CompanyProfileDTO dto, MultipartFile logoFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        User user = userRepository.findByUserId(currentUserName);

        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        if ("company".equals(user.getType())) {
            CompanyProfile entity = dtoToEntity(dto, user);

            if (logoFile != null && !logoFile.isEmpty()) {
                validateFile(logoFile); // 파일 유효성 검사
                String uploadedFileName = fileUtil.fileUpload(logoFile, "companyLogo");
                entity.setUploadFileName(uploadedFileName);
            }

            repository.save(entity);
            return entity.getCompanyProfileCode();
        } else {
            throw new IllegalArgumentException("기업 회원만 프로필을 등록할 수 있습니다.");
        }
    }

    @Override
    public List<CompanyProfileDTO> getList() {
        return repository.findAll().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyProfileDTO read(int code) {
        return repository.findById(code)
                .map(this::entityToDTO)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필 코드가 존재하지 않습니다."));
    }

    @Override
    public void modify(CompanyProfileDTO dto, MultipartFile logoFile) {
        CompanyProfile entity = repository.findById(dto.getCompanyProfileCode())
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필 코드가 존재하지 않습니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        User loggedInUser = userRepository.findByUserId(currentUserName);

        if (!entity.getUser().getUserCode().equals(loggedInUser.getUserCode())) {
            throw new SecurityException("작성자만 수정할 수 있습니다.");
        }

        // 수정 로직
        if (dto.getCompanyDescription() != null) entity.setCompanyDescription(dto.getCompanyDescription());
        if (dto.getIndustry() != null) entity.setIndustry(dto.getIndustry());
        if (dto.getWebsiteUrl() != null) entity.setWebsiteUrl(dto.getWebsiteUrl());
        if (logoFile != null && !logoFile.isEmpty()) {
            validateFile(logoFile);
            if (entity.getUploadFileName() != null) {
                fileUtil.deleteFile(entity.getUploadFileName());
            }
            String uploadedFileName = fileUtil.fileUpload(logoFile, "companyLogo");
            entity.setUploadFileName(uploadedFileName);
        }

        repository.save(entity);
    }

    @Override
    public void remove(int code) {
        CompanyProfile entity = repository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필 코드가 존재하지 않습니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        User loggedInUser = userRepository.findByUserId(currentUserName);

        if (!entity.getUser().getUserCode().equals(loggedInUser.getUserCode())) {
            throw new SecurityException("작성자만 삭제할 수 있습니다.");
        }

        if (entity.getUploadFileName() != null) {
            fileUtil.deleteFile(entity.getUploadFileName());
        }

        repository.deleteById(code);
    }

    private void validateFile(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            if (!contentType.startsWith("image/")) {
                throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
            }
            if (file.getSize() > 5 * 1024 * 1024) { // 5MB 제한
                throw new IllegalArgumentException("파일 크기는 5MB를 초과할 수 없습니다.");
            }
        }
    }
}
