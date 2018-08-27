package com.bac.fm.utils;

import com.bac.mapper.TbGoodsDescMapper;
import com.bac.mapper.TbGoodsMapper;
import com.bac.mapper.TbItemCatMapper;
import com.bac.mapper.TbItemMapper;
import com.bac.pojo.*;
import com.bac.utils.FMUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BacHtmlUtils {
    //注入spu 商品mapper接口代理对象
    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    //注入sku 商品mapper接口代理对象
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    //注入分类mapper接口代理对象
    private TbItemCatMapper itemCatMapper;

    public static void main(String[] args) {
        //加载spring配置文件
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath*:spring/*.xml");
        //获取spring容器中 BacHtmlUtils 对象
        BacHtmlUtils htmlUtils = app.getBean(BacHtmlUtils.class);
        //生成html静态方法.
        htmlUtils.getHtml();
    }

    private void getHtml() {
        try {
            //查询所有商品.
            TbGoodsExample example = new TbGoodsExample();
            TbGoodsExample.Criteria criteria = example.createCriteria();
            //审核通过
            criteria.andAuditStatusEqualTo("1");
            //上架
            criteria.andIsMarketableEqualTo("1");
            //未删除
            criteria.andIsDeleteEqualTo("0");
            //测试数据:不允许查空值
            criteria.andCategory1IdIsNotNull();
            criteria.andCategory2IdIsNotNull();
            criteria.andCategory3IdIsNotNull();


            //查询
            List<TbGoods> goodsList = goodsMapper.selectByExample(example);

            //根据商品id查询商品描述信息
            for (TbGoods tbGoods : goodsList) {
                //查询商品描述信息
                TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(tbGoods.getId());

                //创建tb
                TbItemExample example1 = new TbItemExample();
                TbItemExample.Criteria criteria1 = example1.createCriteria();
                //设置查询参数
                criteria1.andGoodsIdEqualTo(tbGoods.getId());

                //查询和商品相关联的sku
                List<TbItem> itemList = itemMapper.selectByExample(example1);
                //创建map封装数据
                Map maps = new HashMap<>();
                maps.put("goods", tbGoods);
                maps.put("goodsDesc", goodsDesc);
                maps.put("itemList", itemList);

                //查询一级分类:
                TbItemCat tbItemCat1 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id());
                TbItemCat tbItemCat2 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id());
                TbItemCat tbItemCat3 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
                maps.put("itemCat1", tbItemCat1.getName());
                maps.put("itemCat2", tbItemCat2.getName());
                maps.put("itemCat3", tbItemCat3.getName());


                //创建生成静态页面的工具类对象
                FMUtils fmUtils = new FMUtils();
                //参数1:ftl模板,
                //参数2:生成的文件名,
                //参数3:封装的参数.
                fmUtils.ouputFile("item.ftl", tbGoods.getId() + ".html", maps);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
