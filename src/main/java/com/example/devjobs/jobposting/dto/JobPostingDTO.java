package com.example.devjobs.jobposting.dto;

import com.example.devjobs.jobposting.entity.PostingStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingDTO {

    private String title;  // 공고제목

    private String description;  // 공고내용

    private String recruitJob;  // 모집직무

    private int recruitField;  // 모집인원

    private String salary;  // 급여

    private LocalDateTime postingDate;  // 공고시작일

    private LocalDateTime postingDeadline;  // 공고마감일

    private String postingStatus;  // 공고상태

    private String workExprerience;  // 경력

    private String tag;  // 태그

    private String jobCategory;  // 직무 카테고리

    // imgFile 관련 ...
    private MultipartFile uploadFile;  // 파일 스트림

    private String imgDirectory;  // 파일이 저장될 경로

    private String imgFileName;  // 업로드된 파일명

    private String imgPath; // 전체 파일 경로 (imgDirectory + imgFileName)

    // 공고 상태 관련 ...
    // PostingStatus를 "모집중", "마감"으로 변환
//    public String getPostingStatus() {
//        if (this.postingStatus != null) {
//            if (this.postingStatus.equals(PostingStatus.OPEN.name())) {
//                return "모집중";
//            } else if (this.postingStatus.equals(PostingStatus.CLOSE.name())) {
//                return "마감";
//            }
//        }
//        return null;
//    }
//
//    // DTO에서 "모집중", "마감" 값을 PostingStatus로 변환
//    // enum 값으로 변환하고, 반대로 PostingStatus enum 값을 "모집중", "마감"으로 변환
//    public void setPostingStatus(String postingStatus) {
//        if ("모집중".equals(postingStatus)) {
//            this.postingStatus = PostingStatus.OPEN.name();
//        } else if ("마감".equals(postingStatus)) {
//            this.postingStatus = PostingStatus.CLOSE.name();
//        }
//    }




//    String writer; // 작성자(Members.. PK)
//
//    String companyProfile; // 기업프로필코드(CompanyProfile...PK)

}
