package com.bac.user.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    /*
    获取用户名
     */
    @RequestMapping("/loadUserInfo")
    public Map loadUserInfo() {
        //获取用户名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //创建map
        Map map = new HashMap();
        map.put("loginName", name);
        return map;
    }

}
