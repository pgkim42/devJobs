package com.example.devjobs.resume.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationsDTO {

    private String certificateName;  // 자격증 이름
    private String issueDate;       // 발급일

}