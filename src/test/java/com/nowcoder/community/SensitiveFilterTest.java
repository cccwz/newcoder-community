package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveFilterTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSen(){
        String text="ssjdkdkjd独奏jdkdj坚实的基础赌博";
        System.out.println(sensitiveFilter.filter(text));
    }
}
