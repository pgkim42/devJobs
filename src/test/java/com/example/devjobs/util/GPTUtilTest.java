package com.example.devjobs.util;

import com.example.devjobs.similarPosting.util.GPTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GPTUtilTest {

    @Autowired
    GPTUtil util;

    @Test
    public void 테스트(){
        util.recommendPostings(1);
    }

}
