package com.example.devjobs.applications.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationsDTO {

    private Integer applicationCode; // 지원 코드

    private Integer jobCode; // 구인공고 코드(jobposting PK)

    private String applicationsStatus; // 지원 상태

    private Integer resumeCode; // 이력서 코드(resume PK)

    private LocalDateTime submissionDate; // 작성일

    private LocalDateTime updateDate; // 수정일

}
