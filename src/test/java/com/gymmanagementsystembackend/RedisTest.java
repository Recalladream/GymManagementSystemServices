package com.gymmanagementsystembackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymmanagementsystembackend.model.ResultModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    void testString() {
//        // 写入一条String数据
//        redisTemplate.opsForValue().set("name", "虎哥");
//        // 获取string数据
//        Object name = redisTemplate.opsForValue().get("name");
//        System.out.println("name = " + name);
//    }
//
//    @Test
//    void testUser() throws JsonProcessingException {
//        ResultModel user=new ResultModel("虎哥",20);
//        String json=objectMapper.writeValueAsString(user);
//        stringRedisTemplate.opsForValue().set("user",json);
//
//        String jsonuser=stringRedisTemplate.opsForValue().get("user");
//
//        ResultModel o=objectMapper.readValue(jsonuser, ResultModel.class);
//
//        System.out.println(o);
//    }
}
