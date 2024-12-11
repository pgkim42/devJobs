package com.example.devjobs.similarPosting.util;

import com.example.devjobs.jobposting.entity.JobPosting;
import com.example.devjobs.jobposting.repository.JobPostingRepository;
import com.example.devjobs.resume.entity.Resume;
import com.example.devjobs.resume.repository.ResumeRepository;
import com.example.devjobs.similarPosting.dto.ChatGPTRequestMsg;
import com.example.devjobs.similarPosting.dto.GPTRequest;
import com.example.devjobs.similarPosting.dto.GPTResponse;
import com.example.devjobs.similarPosting.dto.GPTResponseChoice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GPTUtil {

    // API 주소와 모델
    String apiURL = "https://api.openai.com/v1/chat/completions";

    String model = "gpt-3.5-turbo";

    @Autowired
    ResumeRepository resumeRepository;

    @Autowired
    JobPostingRepository jobPostingRepository;

    // GPT API 인증키
    @Value("${apikey}")
    String secretKey;

    // 매개변수: 질문
    // 리턴값: 답변
    public String apicall(String question) {

        // 필요한 파라미터(질문) 작성
        List<ChatGPTRequestMsg> msglist = new ArrayList<>();
        msglist.add(new ChatGPTRequestMsg("user", question));
//        msglist.add(new ChatGPTRequestMsg("user","자바가 뭐야?"));
//		msglist.add(new ChatGPTRequestMsg("user", "안녕"));

        // 요청 메세지 생성
        GPTRequest requestmsg = new GPTRequest(model, msglist, 300);

        // http 메세지 헤더에 API키와 미디어타입 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // http 통신을 위해 RestTemplate 생성 및 설정
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<GPTRequest> requestEntity = new HttpEntity<>(requestmsg, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.POST, requestEntity, String.class);

        // API 응답 처리
        // json 문자열을 클래스로 변환해주는 매퍼 클래스 생성
        ObjectMapper mapper = new ObjectMapper();

        // 분석할 수 없는 구문을 무시하는 옵션 설정
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // JSON 문자열을 클래스로 변환
        GPTResponse gptResponse = null;
        try {
            gptResponse = mapper.readValue(response.getBody(), GPTResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // GPT 답변 출력
        for(GPTResponseChoice choice : gptResponse.choices) {
            System.out.println(choice.message);
        }

        // 토큰 사용량 출력
        System.out.println("사용한 토큰량:" + gptResponse.usage.totalTokens);

        return gptResponse.choices.get(0).message.content;
    }

    // 매개변수: 사용자의 이력서, 공고리스트
    //
    public void recommendPostings(int resumeCode){

        // 이력서 조회
        Optional<Resume> result = resumeRepository.findById(resumeCode);
        Resume resume;
        if(result.isPresent()) {
            resume = result.get();
            System.out.println(resume);
        }
        
        // 공고 리스트 조회
        List<JobPosting> jobPostingList = jobPostingRepository.findAll();
        for(JobPosting posting : jobPostingList){
            System.out.println(posting);
        }

    }

}
