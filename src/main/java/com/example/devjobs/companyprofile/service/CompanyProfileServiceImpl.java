package com.example.devjobs.companyprofile.service;

import com.example.devjobs.companyprofile.dto.CompanyProfileDTO;
import com.example.devjobs.companyprofile.entity.CompanyProfile;
import com.example.devjobs.companyprofile.repository.CompanyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyProfileServiceImpl implements CompanyProfileService {

    @Autowired
    CompanyProfileRepository repository;

    @Override
    public int register(CompanyProfileDTO dto) {
        CompanyProfile entity = DTOToEntity(dto);

        repository.save(entity);

        return entity.getCompanyProfileCd();
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
    public CompanyProfileDTO read(int cd) {
        Optional<CompanyProfile> result = repository.findById(cd);
        if (result.isPresent()) {
            return entityToDTO(result.get());
        } else {
            return null;
        }
    }

    @Override
    public void modify(CompanyProfileDTO dto) {
        Optional<CompanyProfile> result = repository.findById(dto.getCompanyProfileCd());
        if (result.isPresent()) {
            CompanyProfile entity = result.get();

            // 각 필드가 null이 아닌 경우에만 수정(개별 수정)
            if (dto.getCompanyName() != null) {
                entity.setCompanyName(dto.getCompanyName());
            }
            if (dto.getCompanyContent() != null) {
                entity.setCompanyContent(dto.getCompanyContent());
            }
            if (dto.getIndustry() != null) {
                entity.setIndustry(dto.getIndustry());
            }
            if (dto.getWebsiteUrl() != null) {
                entity.setWebsiteUrl(dto.getWebsiteUrl());
            }

            repository.save(entity);
        }
    }

    @Override
    public void remove(int cd) {
        repository.deleteById(cd);
    }

}
