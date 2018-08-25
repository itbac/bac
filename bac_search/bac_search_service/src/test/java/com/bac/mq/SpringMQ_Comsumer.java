package com.bac.mq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class SpringMQ_Comsumer {
    /*
    测试:点对点接收消息,或者订阅模式,在配置文件中设置.
     */
    @Test
    public void receiveMessage(){
        //1.加载配置文件,配置文件加载消息监听器IndexListener,消息监听器有输出语句.
        ApplicationContext app =new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-mq-comsumer.xml");
        //2.设置通信阻塞状态
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
