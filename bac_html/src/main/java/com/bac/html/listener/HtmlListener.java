package com.bac.html.listener;

import com.alibaba.fastjson.JSON;
import com.bac.mapper.TbGoodsDescMapper;
import com.bac.mapper.TbGoodsMapper;
import com.bac.mapper.TbItemCatMapper;
import com.bac.mapper.TbItemMapper;
import com.bac.pojo.*;
import com.bac.utils.FMUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.*;

/**
 * @描述：接收消息,同步静态页面 商品数据:添加,修改,删除,静态页面数据不能及时发生变化,
 * 静态页面数据和数据库数据不一致,因此需要把数据库的数据及时同步到静态页面.
 * 如何同步? 使用activeMQ消息中间件同步静态页面
 * 如何同步?
 * 使用activeMQ消息中间件同步静态页面.
 * 业务流程:
 * 1.商品审核通过,发送消息到activeMQ消息服务器
 * 2.静态页面同步服务监听消息空间
 * 3.发现消息空间有消息后,接收消息
 * 4.根据消息准备模板页面需要的数据
 * 5.生成静态页面,实现静态页面的同步工作
 */
public class HtmlListener implements MessageListener {

    //注入商品spu 的mapper接口代理对象
    @Autowired
    private TbGoodsMapper goodsMapper;

    //注入商品描述对象
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    //注入商品mapper接口代理对象
    @Autowired
    private TbItemMapper itemMapper;

    //注入商品分类接口代理对象
    private TbItemCatMapper itemCatMapper;


    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage m = (TextMessage) message;
                //接收消息
                String itemJson = m.getText();
                //把json格式的数据转换为集合对象.
                List<TbItem> itemList = JSON.parseArray(itemJson, TbItem.class);

                //定义set集合封装goodsId
                Set<Long> nodes = new HashSet();

                //循环sku列表,准备spu数据
                for (TbItem item : itemList) {
                    nodes.add(item.getGoodsId());
                }

                //循环spu的商品id
                for (Long goodsId : nodes) {
                    //查询商品spu的数据
                    TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
                    //查询商品描述对象
                    TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
                    //查询商品sku
                    TbItemExample example = new TbItemExample();
                    //创建criteria
                    TbItemExample.Criteria criteria = example.createCriteria();
                    //设置查询参数
                    criteria.andGoodsIdEqualTo(goodsId);
                    //执行查询
                    List<TbItem> skuList = itemMapper.selectByExample(example);

                    //查询商品分类
                    TbItemCat cat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id());
                    TbItemCat cat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id());
                    TbItemCat cat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());

                    //创建map对象,封装模板需要的数据
                    Map map = new HashMap();
                    map.put("goods", goods);
                    map.put("goodsDesc", goodsDesc);
                    map.put("itemList", skuList);
                    map.put("itemCat1", cat1.getName());
                    map.put("itemcat2", cat2.getName());
                    map.put("itemcat3", cat3.getName());

                    //创建生成静态页面的工具类对象
                    FMUtils fm = new FMUtils();
                    fm.ouputFile("item.ftl", goodsId + ".html", map);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
