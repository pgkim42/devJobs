package com.example.devjobs.resume.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LanguagesSkillsDTO {

    private String language;   // 언어
    private String level;      // 능력 수준

}
