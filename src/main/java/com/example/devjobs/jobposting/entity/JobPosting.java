package com.example.devjobs.jobposting.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "job_posting")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AttributeOverride(name = "createdDate", column = @Column(name = "posting_date"))  // BaseEntity의 createdDate를 posting_date로 덮어쓰기
public class JobPosting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_code", nullable = false)
    private int jobCode;  // 공고코드

    @Column(name = "title", nullable = false)
    private String title;  // 공고제목

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;  // 공고내용

    @Column(name = "recruit_job", nullable = false)
    private String recruitJob;  // 모집직무

    @Column(name = "recruit_field", nullable = false)
    private int recruitField;  // 모집인원

    @Column(name = "salary")
    private String salary;  // 급여

    @Column(name = "posting_deadline", nullable = false)
    private LocalDateTime postingDeadline;  // 공고마감일

//    // 공고 상태 관련...
//    @Enumerated(EnumType.STRING)
//    @Column(name = "posting_status", nullable = false)
//    private PostingStatus postingStatus;  // 공고상태: OPEN, CLOSE
//
//    // PostingStatus enum을 사용하여 OPEN과 CLOSE로 상태를 관리
//    public PostingStatus getPostingStatus() {
//        return postingStatus;
//    }
//
//    public void setPostingStatus(PostingStatus postingStatus) {
//        this.postingStatus = postingStatus;
//    }

    // 공고 상태
//    @Enumerated(EnumType.STRING)
//    @Column(name = "posting_status", nullable = false)
//    private PostingStatus postingStatus;  // 공고상태: 모집중, 마감

    // 공고 상태 관련...
    @Column(name = "posting_status", nullable = false)
    private String postingStatus;  // 공고상태: "모집중", "마감" (String으로 관리)

    @Column(name = "work_experience", nullable = false)
    private String workExperience;  // 경력 (신입, 경력)

    @Column(name = "tag")
    private String tag;  // 태그

    @Column(name = "job_category", nullable = false)
    private String jobCategory;  // 직무 카테고리

//    @Column(name = "img_directory", length = 200)
//    private String imgDirectory; // 파일이 저장된 폴더 경로

    @Column(name = "img_file_name", length = 100)
    private String imgFileName; // 파일명

    @Transient // DB에 저장되지는 않음
    private String imgPath; // 전체 파일 경로 (imgDirectory + imgFileName)

    // imgPath 계산하는 메서드
//    public String getImgPath() {
//        if (imgDirectory != null && imgFileName != null) {
//            return imgDirectory + "/" + imgFileName;  // 경로 결합
//        }
//        return null;
//    }

    // 작성자
//    @ManyToOne
//    Members writer;  // 작성자

    // 기업프로필코드
//    @ManyToOne
//    CompanyProfile companyProfile;  // 기업프로필코드

}
