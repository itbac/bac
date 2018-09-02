package com.bac.listener;

import com.bac.utils.SmsUtils;
import org.apache.activemq.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Map;

@Component
public class SmsListener {

    //注入工具类对象
    @Autowired
    private SmsUtils smsUtils;

    @JmsListener(destination = "sms")
    public void sendSms(Map<String, String> smsMap) {
        try {
            //消息map中包含：手机号，签名，模板code,验证码
            String phone = smsMap.get("phone");
            String sign_name = smsMap.get("sign_name");
            String template_code = smsMap.get("template_code");
            String code = smsMap.get("code");
            //调用发送短信方法
            smsUtils.sendSms(phone, sign_name, template_code, code);
            System.out.println(phone);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
}
