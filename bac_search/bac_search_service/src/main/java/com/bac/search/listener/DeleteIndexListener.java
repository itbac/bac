package com.bac.search.listener;

import com.bac.mapper.TbItemMapper;
import com.bac.pojo.TbItem;
import com.bac.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.List;

/*
创建消息监听器, 实现 MessageListener 接口
审核商品通过后,接收消息后,实现同步索引库的操作.com.bac.manager.controller.GoodsController
1.接收消息
2.把消息转换集合对象
3.使用solr模板把集合保存到索引库即可实现索引库同步.
 */
public class DeleteIndexListener implements MessageListener {

    //注入solr模板对象
    @Autowired
    private SolrTemplate solrTemplate;

    //注入itemMapper接口代理对象
    private TbItemMapper itemMapper;

    @Override
    public void onMessage(Message message) {
        try {
            //接收消息
            ObjectMessage objectMessage = (ObjectMessage) message;
            Long[] goodsIds = (Long[]) objectMessage.getObject();

            //定义集合
            List<String> itemIds = new ArrayList<>();

            for (Long goodsId : goodsIds) {
                //创建example查询
                TbItemExample example = new TbItemExample();
                TbItemExample.Criteria criteria = example.createCriteria();
                criteria.andGoodsIdEqualTo(goodsId);
                //查询
                List<TbItem> itemList = itemMapper.selectByExample(example);

                for (TbItem tbItem : itemList) {
                    itemIds.add(tbItem.getId() + "");
                }
            }
            //删除.
            solrTemplate.deleteById(itemIds);

            System.out.println("成功删除索引库中的数据");

        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
