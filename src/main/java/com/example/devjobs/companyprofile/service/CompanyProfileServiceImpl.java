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
    CompanyProfileRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileUtil fileUtil;

    @Override
    public int register(CompanyProfileDTO dto, MultipartFile logoFile) {
        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        User user = userRepository.findByUserId(currentUserName);

        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        CompanyProfile entity = dtoToEntity(dto);
        entity.setUserCode(user); // 로그인한 사용자의 User 설정

        if(logoFile != null && !logoFile.isEmpty()) {
            String uploadedFileName = fileUtil.fileUpload(logoFile, "companyLogo");
            entity.setUploadFileName(uploadedFileName);
        }

        repository.save(entity);

        return entity.getCompanyProfileCode();
    }

    @Override
    public List<CompanyProfileDTO> getList() {
        List<CompanyProfile> entityList = repository.findAll();
        List<CompanyProfileDTO> dtoList = entityList.stream()
                .map(entity -> entityToDTO(entity))
                .collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public CompanyProfileDTO read(int code) {
        Optional<CompanyProfile> result = repository.findById(code);
        if (result.isPresent()) {
            return entityToDTO(result.get());
        } else {
            return null;
        }
    }

    @Override
    public void modify(CompanyProfileDTO dto, MultipartFile logoFile) {
        // 게시글 번호 확인
        if (dto.getCompanyProfileCode() == null) {
            throw new IllegalArgumentException("기업 프로필 코드가 필요합니다.");
        }

        Optional<CompanyProfile> result = repository.findById(dto.getCompanyProfileCode());
        if (result.isPresent()) {
            CompanyProfile entity = result.get();

            // 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
            }

            String currentUserName = authentication.getName();
            User loggedInUser = userRepository.findByUserId(currentUserName);
            if (loggedInUser == null || !entity.getUserCode().getUserCode().equals(loggedInUser.getUserCode())) {
                throw new SecurityException("작성자만 게시글을 수정할 수 있습니다.");
            }

            // 수정 작업 수행
            if (dto.getCompanyName() != null) entity.setCompanyName(dto.getCompanyName());
            if (dto.getCompanyContent() != null) entity.setCompanyContent(dto.getCompanyContent());
            if (dto.getIndustry() != null) entity.setIndustry(dto.getIndustry());
            if (dto.getWebsiteUrl() != null) entity.setWebsiteUrl(dto.getWebsiteUrl());

            // 파일 업데이트 처리
            if (logoFile != null && !logoFile.isEmpty()) {
                if (entity.getUploadFileName() != null) {
                    fileUtil.deleteFile(entity.getUploadFileName());
                }
                String uploadedFileName = fileUtil.fileUpload(logoFile, "companyLogo");
                entity.setUploadFileName(uploadedFileName);
            }

            repository.save(entity);
        } else {
            throw new IllegalArgumentException("해당 기업 프로필 코드가 존재하지 않습니다.");
        }
    }

    @Override
    public void remove(int code) {
        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        String currentUserName = authentication.getName();
        User loggedInUser = userRepository.findByUserId(currentUserName);

        if (loggedInUser == null) {
            throw new IllegalArgumentException("로그인된 사용자 정보를 찾을 수 없습니다.");
        }

        // 삭제하려는 게시글 검색
        Optional<CompanyProfile> result = repository.findById(code);

        if (result.isPresent()) {
            CompanyProfile entity = result.get();

            // 게시글 작성자와 로그인한 사용자 비교
            if (!entity.getUserCode().getUserCode().equals(loggedInUser.getUserCode())) {
                throw new SecurityException("작성자만 게시글을 삭제할 수 있습니다.");
            }

            // 게시글 삭제
            repository.deleteById(code);
        } else {
            throw new IllegalArgumentException("해당 기업프로필 코드가 존재하지 않습니다.");
        }
    }

}
