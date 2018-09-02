package com.bac.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService{
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //构建角色集合
        List<GrantedAuthority> authorities=new ArrayList();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(username, ""  , authorities);
        //密码也不用了,登录认证交给cas .
        //这里只做角色的权限控制.
    }
}