//package com.example.devjobs.service;
//
//import com.example.devjobs.jobcategory.dto.JobCategoryDTO;
//import com.example.devjobs.jobcategory.service.JobCategoryService;
//import com.example.devjobs.jobcategory.repository.JobCategoryRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class JobCategoryServiceTest {
//
//    @Autowired
//    private JobCategoryService jobCategoryService;
//
//    @Autowired
//    private JobCategoryRepository jobCategoryRepository;
//
//    // Given-When-Then 패턴을 사용하여 단위 테스트를 구성합니다.
//
//    @Test
//    public void testRegister() {
//        // Given: 테스트를 위한 직무 카테고리 DTO 준비
//        JobCategoryDTO dto = new JobCategoryDTO(null, "백엔드 개발자");
//
//        // When: 직무 카테고리를 등록
//        int categoryCode = jobCategoryService.register(dto);
//
//        // Then: 등록된 직무 카테고리가 DB에 저장되었는지 확인
//        assertNotNull(categoryCode); // 카테고리 코드가 생성되어야 합니다.
//        assertTrue(jobCategoryRepository.existsById(categoryCode)); // 해당 카테고리가 DB에 저장되었는지 확인
//    }
//
//    @Test
//    public void testGetList() {
//        // Given: 직무 카테고리 등록
//        JobCategoryDTO dto = new JobCategoryDTO(null, "프론트엔드 개발자");
//        jobCategoryService.register(dto);
//
//        // When: 직무 카테고리 목록 조회
//        List<JobCategoryDTO> categories = jobCategoryService.getList();
//
//        // Then: 목록이 비어 있지 않아야 하고, 등록된 카테고리가 포함되어야 함
//        assertFalse(categories.isEmpty()); // 리스트가 비어 있지 않음
//        assertEquals("프론트엔드 개발자", categories.get(0).getCategoryName()); // 카테고리 이름이 일치해야 함
//    }
//
//    @Test
//    public void testModify() {
//        // Given: 직무 카테고리 등록 후 수정할 DTO 준비
//        JobCategoryDTO dto = new JobCategoryDTO(null, "시큐리티 엔지니어");
//        int categoryCode = jobCategoryService.register(dto);
//
//        JobCategoryDTO modifyDto = new JobCategoryDTO(categoryCode, "보안 엔지니어");
//
//        // When: 직무 카테고리 수정
//        jobCategoryService.modify(modifyDto);
//
//        // Then: 수정된 직무 카테고리 이름이 반영되었는지 확인
//        JobCategoryDTO updatedCategory = jobCategoryService.getList().stream()
//                .filter(c -> c.getCategoryCode() == categoryCode)
//                .findFirst()
//                .orElse(null);
//
//        assertNotNull(updatedCategory); // 수정된 카테고리가 존재해야 함
//        assertEquals("보안 엔지니어", updatedCategory.getCategoryName()); // 수정된 이름이 반영되었는지 확인
//    }
//
//    @Test
//    public void testDelete() {
//        // Given: 직무 카테고리 등록
//        JobCategoryDTO dto = new JobCategoryDTO(null, "게임 개발자");
//        int categoryCode = jobCategoryService.register(dto);
//
//        // When: 직무 카테고리 삭제
//        jobCategoryService.remove(categoryCode);
//
//        // Then: 삭제된 카테고리가 DB에 존재하지 않아야 함
//        assertFalse(jobCategoryRepository.existsById(categoryCode)); // 삭제된 후 해당 카테고리가 존재하지 않아야 함
//    }
//}