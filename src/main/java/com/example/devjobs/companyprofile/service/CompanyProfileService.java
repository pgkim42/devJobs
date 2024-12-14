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

    int getCurrentCompanyProfileCode();

    default CompanyProfileDTO entityToDTO(CompanyProfile entity) {
        return CompanyProfileDTO.builder()
                .companyProfileCode(entity.getCompanyProfileCode())
                .companyDescription(entity.getCompanyDescription())
                .industry(entity.getIndustry())
                .websiteUrl(entity.getWebsiteUrl())
                .companyCode(entity.getCompanyCode())
                .companyType(entity.getCompanyType())
                .companyName(entity.getCompanyName())
                .ceoName(entity.getCeoName())
                .companyAddress(entity.getCompanyAddress())
                .userCode(entity.getUser().getUserCode())
                .uploadFileName(entity.getUploadFileName())
                .build();
    }

    default CompanyProfile dtoToEntity(CompanyProfileDTO dto, User user) {
        return CompanyProfile.builder()
                .companyProfileCode(dto.getCompanyProfileCode())
                .companyDescription(dto.getCompanyDescription())
                .industry(dto.getIndustry())
                .websiteUrl(dto.getWebsiteUrl())
                .companyCode(dto.getCompanyCode())
                .companyType(dto.getCompanyType())
                .companyName(dto.getCompanyName())
                .ceoName(dto.getCeoName())
                .companyAddress(dto.getCompanyAddress())
                .uploadFileName(dto.getUploadFileName())
                .user(user) // User 설정
                .build();
    }
}
