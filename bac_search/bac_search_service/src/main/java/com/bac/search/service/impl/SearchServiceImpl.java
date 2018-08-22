package com.bac.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bac.pojo.TbItem;
import com.bac.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    //注入solr模板对象,
    @Autowired
    private SolrTemplate solrTemplate;

    /*
    需求:根据关键词实现索引库搜索.
     */

    @Override
    public Map searchList(Map searchMap) {

        //创建query查询对象,直接高亮查询对象
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //获取关键词
        String keywords = (String) searchMap.get("keywords");
        //创建Criteria对象
        Criteria criteria = null;

        //判断关键词不为空
        //item_keywords 是复制域字段
        if (!StringUtils.isEmpty(keywords)) {
            criteria = new Criteria("item_keywords").is(keywords);
        } else {
            criteria = new Criteria().expression("*:*");
        }

   /*     //1.创建高亮对象设置高亮.
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_titel");
        highlightOptions.setSimplePrefix("<font color='red'>");
        highlightOptions.setSimplePostfix("</font>");

        //2.把高亮对象添加到查询对象中
        query.setHighlightOptions(highlightOptions);*/

        //把条件设置到query查询对象中.
        query.addCriteria(criteria);
        //查询
        HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //创建map对象
        Map map=new HashMap();
        map.put("rows",highlightPage.getContent());

        return map;
    }
}
