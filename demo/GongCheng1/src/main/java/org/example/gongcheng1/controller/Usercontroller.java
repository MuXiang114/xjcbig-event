package org.example.gongcheng1.controller;

import jakarta.validation.constraints.Pattern;
import org.example.gongcheng1.pojo.User;
import org.example.gongcheng1.service.Userservice;
import org.example.gongcheng1.utils.JwtUtil;
import org.example.gongcheng1.utils.Md5Util;
import org.example.gongcheng1.utils.ThreadLocalUtil;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.example.gongcheng1.pojo.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Validated
@RestController
@RequestMapping("/user")
public class Usercontroller {

    @Autowired
    private Userservice userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("/register")
    //用户名和密码格式筛选，用户名和密码格式为非空，长度在5-16之间
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username,
                           @Pattern(regexp = "^\\S{5,16}$") String password) {
        //if (username != null && username.length() >= 5 && username.length() <= 16 &&
        //password != null && password.length() >= 5 && password.length() <= 16) {
        //查询用户
        User u = userService.findByUserName(username);
        if (u == null) {
            //没有占用
            //注册
            userService.register(username, password);
            return Result.success();
        } else {
            //占用
            return Result.error("用户名已占用");
        }
        // }else{
        //return Result.error("用户名或密码格式不正确");
        //}
    }

    //登录
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username,
                                @Pattern(regexp = "^\\S{5,16}$") String password) {
        //根据用户名查询用户
        User loginuser = userService.findByUserName(username);
        //判断该用户是否存在
        if (loginuser == null) {

            return Result.error("用户名错误");
        }

        //判断密码是否正确
        if (Md5Util.getMD5String(password).equals(loginuser.getPassword())) {
            //登陆成功
            //添加数据
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginuser.getId());
            claims.put("username", loginuser.getUsername());
            String token = JwtUtil.genToken(claims);
            //token存储到redis中
            stringRedisTemplate.opsForValue().set(token, token,1, TimeUnit.HOURS);
            return Result.success(token);
        } else {
            return Result.error("密码错误");
        }

    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader(name = "Authorization") String token) {
        //根据用户名查询用户
        /*Map<String, Object> map = JwtUtil.parseToken(token);
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);*/
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
        //校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");
        if (!StringUtils.hasLength(oldPwd) && !StringUtils.hasLength(newPwd) && !StringUtils.hasLength(rePwd)) {
            return Result.error("参数不能为空");
        }
        //校验原密码是否正确
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginuser = userService.findByUserName(username);
        if (!loginuser.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码错误");
        }
        //校验new和re密码是否一样
        if (!newPwd.equals(rePwd)) {
            return Result.error("两次输入的密码不一致");
        }

        //更新密码
        userService.updatePwd(newPwd);
        //删除旧的token
        stringRedisTemplate.delete(token);
        return Result.success();
    }
}



