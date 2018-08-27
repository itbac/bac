package com.bac.manager.controller;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.bac.pojo.TbGoods;
import com.bac.pojo.TbItem;
import com.bac.vo.Goods;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;

import com.bac.manager.service.GoodsService;

import com.bac.utils.PageResult;
import com.bac.utils.BacResult;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    //注入消息发送模板对象
    @Autowired
    private JmsTemplate jmsTemplate;

    //注入发送消息目的地
    @Autowired
    private ActiveMQTopic activeMQTopic;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public PageResult findPage(@PathVariable int pageNum, @PathVariable int pageSize) {
        return goodsService.findPage(pageNum, pageSize);
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    public BacResult add(@RequestBody Goods goods) {
        try {
            goodsService.add(goods);
            return new BacResult(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public BacResult update(@RequestBody TbGoods goods) {
        try {
            goodsService.update(goods);
            return new BacResult(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public TbGoods findOne(@PathVariable Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除 , 逻辑删除,不是真正的删除.
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete/{ids}")
    public BacResult delete(@PathVariable Long[] ids) {
        try {
            goodsService.delete(ids);
            //发送消息,如果发送对象,就要send方法,需要消息解析器
            //jmsTemplate.convertAndSend("solr_index_delete",ids); 需要消息解析器.
            jmsTemplate.send("solr_index_delete", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    ObjectMessage Message = session.createObjectMessage(ids);

                    return Message;
                }
            });


            return new BacResult(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int pageNum, int pageSize) {
        return goodsService.findPage(goods, pageNum, pageSize);
    }


    /*
   批量修改状态 ,运营商系统审核商品

   用activeMQ消息中间件,同步索引库,静态页面.
    1.审核商品通过后,使用cativeMQ发送消息
    2.使用activeMQ发送消息
    发送消息内容是什么?
    把改变的商品内容发送给搜索服务.bac_search_service  com.bac.search.listener.IndexListener
    */
    @RequestMapping("/updateStatus/{ids}/{status}")
    public BacResult updateStatus(@PathVariable Long[] ids, @PathVariable String status) {
        try {
            goodsService.updateStatus(ids, status);

            //activeMQ发送消息.
            //1.判断状态是否是审核通过的
            if ("1".equals(status)) {
                //查询需要发送的商品信息
                List<TbItem> itemList = goodsService.findItemList(ids);

                //把集合转换json字符串
                String itemJson = JSON.toJSONString(itemList);

                jmsTemplate.send(activeMQTopic, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(itemJson);
                    }
                });
            }
            return new BacResult(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "修改失败");
        }
    }

}
