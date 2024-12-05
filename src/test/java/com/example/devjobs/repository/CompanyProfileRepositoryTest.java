package com.example.devjobs.repository;

import com.example.devjobs.companyprofile.entity.CompanyProfile;
import com.example.devjobs.companyprofile.repository.CompanyProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CompanyProfileRepositoryTest {

    @Autowired
    private CompanyProfileRepository repository;

    @Test
    public void 게시글추가() {
        // 엔티티 생성 및 저장
        CompanyProfile entity = CompanyProfile.builder()
                .companyName("Test")
                .companyContent("Test내용")
                .industry("IT")
                .websiteUrl("https://company.com")
                .logoUrl("https://company.com/logo.png")
                .build();

        CompanyProfile savedEntity = repository.save(entity);

    }

    @Test
    public void 게시글조회() {

        CompanyProfile entity = CompanyProfile.builder()
                .companyName("Test Company")
                .companyContent("Test company content")
                .industry("IT")
                .websiteUrl("https://testcompany.com")
                .logoUrl("https://testcompany.com/logo.png")
                .build();

        CompanyProfile savedEntity = repository.save(entity);

        Optional<CompanyProfile> foundEntity = repository.findById(savedEntity.getCompanyProfileCd());

    }

    @Test
    public void 게시글수정() {

        CompanyProfile entity = CompanyProfile.builder()
                .companyName("Test Company")
                .companyContent("Test company content")
                .industry("IT")
                .websiteUrl("https://testcompany.com")
                .logoUrl("https://testcompany.com/logo.png")
                .build();

        CompanyProfile savedEntity = repository.save(entity);
        CompanyProfile updatedEntity = repository.save(savedEntity);

    }

    @Test
    public void 게시글삭제() {
        // 엔티티 저장
        CompanyProfile entity = CompanyProfile.builder()
                .companyName("Test Company")
                .companyContent("Test company content")
                .industry("IT")
                .websiteUrl("https://testcompany.com")
                .logoUrl("https://testcompany.com/logo.png")
                .build();

        CompanyProfile savedEntity = repository.save(entity);

        repository.deleteById(savedEntity.getCompanyProfileCd());

        // 삭제된 데이터 조회
        Optional<CompanyProfile> deletedEntity = repository.findById(savedEntity.getCompanyProfileCd());

    }
}