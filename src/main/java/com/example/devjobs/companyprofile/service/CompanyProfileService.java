package com.example.devjobs.companyprofile.service;

import com.example.devjobs.companyprofile.dto.CompanyProfileDTO;
import com.example.devjobs.companyprofile.entity.CompanyProfile;
import com.example.devjobs.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface CompanyProfileService {

    int register(CompanyProfileDTO dto, MultipartFile logoFile);

    List<CompanyProfileDTO> getList();

    CompanyProfileDTO read(int code);

    void modify(CompanyProfileDTO dto, MultipartFile logoFile);

    void remove(int code);

    default CompanyProfileDTO entityToDTO(CompanyProfile entity) {

        // DTO 객체 생성
        CompanyProfileDTO dto = CompanyProfileDTO.builder()
                .companyProfileCode(entity.getCompanyProfileCode())  // 기업프로필코드
                .companyName(entity.getCompanyName())            // 기업 이름
                .companyContent(entity.getCompanyContent())      // 기업 내용
                .industry(entity.getIndustry())                  // 업종
                .websiteUrl(entity.getWebsiteUrl())              // 기업사이트 URL
                .createDate(entity.getCreateDate())              // 작성일
                .updateDate(entity.getUpdateDate())              // 수정일
                .userCode(entity.getUserCode().getUserCode())   // 유저
                .uploadFileName(entity.getUploadFileName())     // 기업로고 파일
                .build();

        return dto;

    }

    default CompanyProfile dtoToEntity(CompanyProfileDTO dto) {

        // User 객체 생성
        User user = new User();
        user.setUserCode(dto.getUserCode());

        CompanyProfile entity = CompanyProfile.builder()
                .companyProfileCode(dto.getCompanyProfileCode())
                .companyName(dto.getCompanyName())
                .companyContent(dto.getCompanyContent())
                .industry(dto.getIndustry())
                .websiteUrl(dto.getWebsiteUrl())
                .userCode(user) // User 설정
                .uploadFileName(dto.getUploadFileName())
                .build();

        return entity;
    }
}
