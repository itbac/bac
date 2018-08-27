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
   需求:接收消息,不要有返回值,否则会发6次消息.
        如果application.properties配置了外置activeMQ要先启动.
     */
    @JmsListener(destination = "queue")
    public void receive(Message message) {
        try {
            TextMessage m = (TextMessage) message;
            String text = m.getText();
            System.out.println("接收到的消息:" + text);

        } catch (JMSException e) {
            e.printStackTrace();
        }


    }

    /*
  需求:发送消息,测试发送短信
   */
    @RequestMapping("/send")
    public String send() {
        Map<String, String> smsMap = new HashMap<>();
        smsMap.put("phone","13922213530");
        smsMap.put("sign_name","黑马");
        smsMap.put("template_code","SMS_125028677");
        smsMap.put("code","888888");
        jmsTemplate.convertAndSend("sms", smsMap);
        return "success";
    }
}
