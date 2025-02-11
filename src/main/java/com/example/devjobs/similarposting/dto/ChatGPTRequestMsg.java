package com.example.devjobs.similarposting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/*
 * GPT에게 요청을 보내기 위한 클래스
 * */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatGPTRequestMsg {
	public String role; // 역할
	public String content;	// 질문
}
