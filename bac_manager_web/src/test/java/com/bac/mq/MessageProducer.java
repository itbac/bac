package com.bac.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

public class MessageProducer {
    public static void main(String[] args) throws JMSException {

        //指定消息服务地址:协议,ip,端口
        String brokerURL = "tcp://192.168.66.67:61616";
        //创建工厂对象,连接消息服务器.
        ConnectionFactory cf = new ActiveMQConnectionFactory(brokerURL);
        //从工厂对象中获取连接对象
        Connection connection = cf.createConnection();
        //开启连接
        connection.start();

        //从连接中获取回话对象
        //参数1:true :表示使用事务提交消息确认模式Session.SESSION_TRANSACTED, false:忽略事务提交确认模式
        //参数2:AUTO_ACKNOWLEDGE 自动确认模式.
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //创建消息发送目的地:开辟服务器消息空间,且给消息空间起一个名称
        Queue myQueue = session.createQueue("myQueue");

        //创建消息发送者,且指定消息发送目的
        javax.jms.MessageProducer producer = session.createProducer(myQueue);

        //创建消息对象
        TextMessage message = new ActiveMQTextMessage();
        message.setText("唐僧师徒打怪升级.");

        //发送消息
        producer.send(message);

        //关闭资源.
        producer.close();
        session.close();
        connection.close();

    }
}
