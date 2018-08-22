package com.bac.utils;

import com.alibaba.fastjson.JSON;
import com.bac.mapper.TbItemMapper;
import com.bac.pojo.TbItem;
import com.bac.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtils {

    //注入商品的mapper接口代理对象.
    @Autowired
    private TbItemMapper itemMapper;

    //注入solr模板对象,实现索引库数据导入
    @Autowired
    private SolrTemplate solrTemplate;

    /*
    需求:导入数据库到索引库
     */
    public void importDatabaseToSolrIndex() {
        //创建example对象.
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        //设置查询参数,查询已启用的商品.
        criteria.andStatusEqualTo("1");
        //查询数据库数据
        List<TbItem> list = itemMapper.selectByExample(example);
        // System.out.println(list);

        //封装规格属性的数据.
        //规格数据是动态域数据,因此规格数据使用map来进行封装
        for (TbItem item : list) {
            //获取规格数据
            String spec = item.getSpec();
            //把规格数据字符串转换map对象
            Map<String, String> maps = (Map<String, String>) JSON.parse(spec);
            //设置规格属性的值.
            item.setSpecMap(maps);
        }

        //保存数据到索引库
        solrTemplate.saveBeans(list);
        //提交
        solrTemplate.commit();

    }

    //微服务.
    //java -jar xx.jar
    public static void main(String[] args) {

        //加载spring配置文件
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath*:spring/*.xml");

        //从spring容器中拿到对象,调用导入方法.
        SolrUtils bean = app.getBean(SolrUtils.class);
        bean.importDatabaseToSolrIndex();


    }
}
