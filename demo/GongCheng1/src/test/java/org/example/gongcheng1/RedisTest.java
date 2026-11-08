package org.example.gongcheng1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {
    
    @Autowired
    private StringRedisTemplate springRedisTemplate;
    @Test
    public void  testSet(){
        //往redis中存储一个键值对
        ValueOperations<String,String> operations = springRedisTemplate.opsForValue();
        operations.set("name","zhangsan");
        operations.set("id","1",15, TimeUnit.SECONDS);
    }

    @Test
    public void testGet(){
        //从redis中获取一个键值对
        ValueOperations<String,String> operations = springRedisTemplate.opsForValue();
        String name = operations.get("name");
        System.out.println(name);
    }
}
