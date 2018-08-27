package com.bac.controller;

import com.bac.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    //注入环境变量值,获取配置文件中的值.
    @Autowired
    private Environment env;

    @RequestMapping("/hello")
    public String showHello() {
        return "hello,spring boot! url="+env.getProperty("url");
    }

    @RequestMapping("/hello2")
    public TbItem showHello2() {
        //创建对象
        TbItem item = new TbItem();
        item.setId(1000000L);
        item.setTitle("手机");
        item.setSellPoint("非常好使");
        return item;
    }
}
