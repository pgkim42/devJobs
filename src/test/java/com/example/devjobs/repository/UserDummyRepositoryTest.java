package com.example.devjobs.repository;


import com.example.devjobs.userdummy.entity.User;
import com.example.devjobs.userdummy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDummyRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindUsers() {
        // 1. 더미 데이터 생성
        Random random = new Random();
        List<String> jobPreferences = List.of("Backend Developer", "Frontend Developer", "Data Scientist", "DevOps Engineer", "Mobile Developer");
        List<String> skillsList = List.of("Java", "Spring", "SQL", "JavaScript", "React", "Python", "Machine Learning", "Docker", "Kubernetes");

        for (int i = 0; i < 20; i++) {
            // 랜덤 직무, 스킬, 경력 생성
            String job = jobPreferences.get(random.nextInt(jobPreferences.size()));
            String skills = String.join(",", random.ints(0, skillsList.size())
                    .distinct()
                    .limit(3 + random.nextInt(3)) // 3~5개의 랜덤 스킬 선택
                    .mapToObj(skillsList::get)
                    .toList());
            int experience = random.nextInt(6); // 0~5년 경력

            // User 엔티티 생성
            User user = User.builder()
                    .jobPreferences(job)
                    .skills(skills)
                    .experienceLevel(experience)
                    .build();

            // 저장
            userRepository.save(user);
        }

        // 2. 저장된 사용자 조회
        List<User> users = userRepository.findAll();

        // 3. 검증
        assertThat(users).hasSize(20); // 저장된 사용자 수
        assertThat(users.get(0).getJobPreferences()).isNotNull();
        assertThat(users.get(0).getSkills()).isNotNull();
        assertThat(users.get(0).getExperienceLevel()).isBetween(0, 5);
    }
}