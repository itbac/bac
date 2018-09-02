package com.bac.controller;

import org.apache.activemq.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.Map;

@RestController
public class QueueController {

    //注入发消息模板对象
    @Autowired
    private JmsTemplate jmsTemplate;

    /*
    需求:发送消息给mq消息服务器
     */
    @RequestMapping("/sendSms/{code}")
    public String sendSms(@PathVariable String code) {
        jmsTemplate.convertAndSend("queue", code);
        return "success";
    }



    /*
  需求:发送消息,测试发送短信
   */
    @RequestMapping("/send")
    public String send() {
        Map<String, String> smsMap = new HashMap<>();
        smsMap.put("phone","17776261820");
        smsMap.put("sign_name","周怡斌");
        smsMap.put("template_code","SMS_142381138");
        smsMap.put("code","888888");
        jmsTemplate.convertAndSend("sms", smsMap);
        return "success";
    }
}
