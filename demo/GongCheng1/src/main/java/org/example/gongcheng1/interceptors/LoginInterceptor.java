package org.example.gongcheng1.interceptors;

import org.example.gongcheng1.pojo.Result;
import org.example.gongcheng1.utils.JwtUtil;
import org.example.gongcheng1.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
//拦截器
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");
        //验证token
        try {
            //从redis获取相同的令牌
            String RedisToken = stringRedisTemplate.opsForValue().get(token);
            if (RedisToken == null){
                //token失效
                throw new RuntimeException("token失效");
            }
            Map<String, Object> claims = JwtUtil.parseToken(token);
            //把业务数据存储到threadLocal
            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;

        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       //运行后清空threadlocal中的数据
        ThreadLocalUtil.remove();


    }
}
