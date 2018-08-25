package com.bac.search.listener;

import com.alibaba.fastjson.JSON;
import com.bac.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

/*
创建消息监听器, 实现 MessageListener 接口
审核商品通过后,接收消息后,实现同步索引库的操作.com.bac.manager.controller.GoodsController
1.接收消息
2.把消息转换集合对象
3.使用solr模板把集合保存到索引库即可实现索引库同步.
 */
public class IndexListener implements MessageListener {

    //注入solr模板对象
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void onMessage(Message message) {
        //接收消息
        try {
            if (message instanceof TextMessage) {
                TextMessage m = (TextMessage) message;

                //获取消息
                String itemJson = m.getText();
                //把消息转换成json对象
                List<TbItem> items = JSON.parseArray(itemJson, TbItem.class);
                //同步索引库
                solrTemplate.saveBean(items);
                //提交.
                solrTemplate.commit();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
