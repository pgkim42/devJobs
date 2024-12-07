package com.example.devjobs.companyprofile.service;

import com.example.devjobs.companyprofile.dto.CompanyProfileDTO;
import com.example.devjobs.companyprofile.entity.CompanyProfile;

import java.time.LocalDateTime;
import java.util.List;

public interface CompanyProfileService {

    int register(CompanyProfileDTO dto);

    List<CompanyProfileDTO> getList();

    CompanyProfileDTO read(int cd);

    void modify(CompanyProfileDTO dto);

    void remove(int cd);

    default CompanyProfileDTO entityToDTO(CompanyProfile entity) {

        // DTO 객체 생성
        CompanyProfileDTO dto = CompanyProfileDTO.builder()
                .companyProfileCd(entity.getCompanyProfileCd())  // 기업프로필코드
                .companyName(entity.getCompanyName())            // 기업 이름
                .companyContent(entity.getCompanyContent())      // 기업 내용
                .industry(entity.getIndustry())                  // 업종
                .websiteUrl(entity.getWebsiteUrl())              // 기업사이트 URL
                .createDate(entity.getCreatedDate())              // 작성일
                .updateDate(entity.getUpdatedDate())              // 수정일
                .build();

        return dto;

    }

    default CompanyProfile DTOToEntity(CompanyProfileDTO dto) {

        CompanyProfile entity = CompanyProfile.builder()
                .companyProfileCd(dto.getCompanyProfileCd())
                .companyName(dto.getCompanyName())
                .companyContent(dto.getCompanyContent())
                .industry(dto.getIndustry())
                .websiteUrl(dto.getWebsiteUrl())
                .build();

        return entity;
    }
}
