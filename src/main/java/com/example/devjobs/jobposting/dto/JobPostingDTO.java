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

    private Integer jobCode; // 공고번호

    private String title;  // 공고제목

    private String content;  // 공고내용

    private String recruitJob;  // 모집직무

    private int recruitField;  // 모집인원

    private String salary;  // 급여

    private LocalDateTime postingDate;  // 공고시작일

    private LocalDateTime postingDeadline;  // 공고마감일

    private String postingStatus;  // 공고상태

    private String workExperience;  // 경력

    private String tag;  // 태그(스킬)

    private String jobCategory;  // 직무 카테고리

    // imgFile 관련 ...
    private MultipartFile uploadFile;  // 파일 스트림

    private String imgFileName;  // 업로드된 파일명

    private String imgPath; // 전체 파일 경로 (imgDirectory + imgFileName)

    private Integer companyProfileCd; // 기업프로필코드

//    String writer; // 작성자(Members.. PK)
//


}
