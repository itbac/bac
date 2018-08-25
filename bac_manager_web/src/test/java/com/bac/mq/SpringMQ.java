package com.bac.mq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/*
加载配置文件,测试 spring整合 activeMQ
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/mq-producer.xml")
public class SpringMQ {

    //注入模板对象
    @Autowired
    private JmsTemplate jmsTemplate;

    //注入消息发送目的地,点对点.
   /* @Autowired
    private ActiveMQQueue activeMQQueue;
*/
    //发布订阅
    @Autowired
    private ActiveMQTopic activeMQTopic;


    /*
    需求:测试点对点模式发送
     */
  /*  @Test
    public void sendMessageWithP2P(){
        jmsTemplate.send(activeMQQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("spring整合MQ,8888");
            }
        });
    }*/

    /*
 需求:发布订阅模式发送
  */
    @Test
    public void sendMessageWithPS(){
        jmsTemplate.send(activeMQTopic, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("spring整合MQ,666");
            }
        });
    }



}
