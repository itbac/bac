package com.bac.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;


public class MessageProducer {

    /*
    activeMQ消息中间件,发送消息测试. 消息生成者.
    模式:点对点.
    */
    @Test
    public void sendMessage() throws JMSException {

        //1.指定消息服务地址:协议,ip,通信端口61616
        String brokerURL = "tcp://192.168.66.67:61616";
        //2.创建工厂对象,连接消息服务器.
        ConnectionFactory cf = new ActiveMQConnectionFactory(brokerURL);
        //3.从工厂对象中获取连接对象
        Connection connection = cf.createConnection();
        //4.开启连接
        connection.start();

        //5.从连接中获取会话对象
        //参数1:true :表示使用事务提交消息确认模式  Session.SESSION_TRANSACTED, false:忽略事务提交确认模式
        //参数2:AUTO_ACKNOWLEDGE 自动确认模式.
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //6.创建消息发送目的地:开辟服务器消息空间,且给消息空间起一个名称
        Queue myQueue = session.createQueue("myQueue");

        //7.创建消息发送者,且指定消息发送目的
        javax.jms.MessageProducer producer = session.createProducer(myQueue);

        //创建消息对象
        TextMessage message = new ActiveMQTextMessage();
        message.setText("年薪百万加油哦.");

        //发送消息
        producer.send(message);

        //关闭资源.
        producer.close();
        session.close();
        connection.close();

    }
/*
需求:发送消息
模式:发布订阅模式
 */
    @Test
    public void sendMessageWithPs() throws JMSException {

        //1.指定消息服务地址:协议,ip,通信端口61616
        String brokerURL = "tcp://192.168.66.67:61616";
        //2.创建工厂对象,连接消息服务器.
        ConnectionFactory cf = new ActiveMQConnectionFactory(brokerURL);
        //3.从工厂对象中获取连接对象
        Connection connection = cf.createConnection();
        //4.开启连接
        connection.start();

        //5.从连接中获取会话对象
        //参数1:true :表示使用事务提交消息确认模式  Session.SESSION_TRANSACTED, false:忽略事务提交确认模式
        //参数2:AUTO_ACKNOWLEDGE 自动确认模式.
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //6.创建消息发送目的地:开辟服务器消息空间,且给消息空间起一个名称
        //点对点模式发送消息,发布订阅模式发布消息, 唯一的区别,就是在于存储消息的目的地数据结构不一样.
        //点对点:queue
        //发布订阅:topic
        //Queue myQueue = session.createQueue("myQueue");
        Topic topic = session.createTopic("myTopic");

        //7.创建消息发送者,且指定消息发送目的
        javax.jms.MessageProducer producer = session.createProducer(topic);

        //创建消息对象
        TextMessage message = new ActiveMQTextMessage();
        message.setText("年薪百万加油哦.888");

        //发送消息
        producer.send(message);

        //关闭资源.
        producer.close();
        session.close();
        connection.close();

    }
}
