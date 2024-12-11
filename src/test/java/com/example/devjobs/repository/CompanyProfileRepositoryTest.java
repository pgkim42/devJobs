//package com.example.devjobs.repository;
//
//import com.example.devjobs.companyprofile.dto.CompanyProfileDTO;
//import com.example.devjobs.companyprofile.entity.CompanyProfile;
//import com.example.devjobs.companyprofile.repository.CompanyProfileRepository;
//import com.example.devjobs.companyprofile.service.CompanyProfileService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class CompanyProfileRepositoryTest {
//
//    @Autowired
//    private CompanyProfileRepository repository;
//
//    @Autowired
//    private CompanyProfileService service;
//
//    @Test
//    public void 게시글추가() {
//        // 엔티티 생성 및 저장
//        CompanyProfile entity = CompanyProfile.builder()
//                .companyName("Test")
//                .companyContent("Test내용")
//                .industry("IT")
//                .websiteUrl("https://company.com")
//                .build();
//
//        CompanyProfile savedEntity = repository.save(entity);
//
//    }
//
//    @Test
//    public void 게시글수정() {
//
//        Optional<CompanyProfile> result = repository.findById(2);
//        if(result.isPresent()){
//            CompanyProfile entity = result.get();
//            entity.setCompanyName("수정test");
//            entity.setCompanyContent("수정test2");
//            repository.save(entity);
//
//        }
//    }
//
//    @Test
//    public void 게시글삭제() {
//        // 엔티티 저장
//        repository.deleteById(5);
//
//    }
//}