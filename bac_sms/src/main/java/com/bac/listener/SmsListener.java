package com.bac.listener;

import com.bac.utils.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

import java.util.Map;

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
}
