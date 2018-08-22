package com.bac.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.io.IOException;

public class MessageConsumer {
    public static void main(String[] args) throws JMSException, IOException {

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

        //指定接收消息目的地,此消息目的必须和发送消息目的地一致.
        Queue myQueue = session.createQueue("myQueue");

        //指定消费接收者
        javax.jms.MessageConsumer consumer = session.createConsumer(myQueue);

        //监听模式接收消息.
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage m = (TextMessage) message;

                        //获取消息
                        String text = m.getText();
                        System.out.println("接收消息是:" + text);
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //等待输入,让端口阻塞状态
        System.in.read();

        //关闭资源.
        session.close();
        connection.close();

    }
}
