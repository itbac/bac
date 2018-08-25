package com.bac.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bac.pojo.TbItem;
import com.bac.search.service.SearchService;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
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

        //1.主 关键词查询
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
        //把条件设置到query查询对象中.
        query.addCriteria(criteria);

        //2.根据分类来进行过滤查询
        //从searchMap获取分类参数值
        String category = (String) searchMap.get("categoty");
        //判断分类是否为空
        if (!StringUtils.isEmpty(category)) {
            //创建criteria对象
            Criteria criteria1 = new Criteria("item_category").is(category);
            //创建过滤查询对象
            FilterQuery filterQuery = new SimpleHighlightQuery(criteria1);
            //把过滤条件添加到查询对象中
            query.addFilterQuery(filterQuery);
        }
        //3.根据品牌进行过滤查询
        //从searchMap获取品牌参数值
        String brand = (String) searchMap.get("brand");
        //判断品牌是否为空
        if (!StringUtils.isEmpty(brand)) {
            //创建criteria对象
            Criteria criteria1 = new Criteria("item_brand").is(brand);
            //创建过滤查询对象
            FilterQuery filterQuery = new SimpleHighlightQuery(criteria1);
            //把过滤条件添加到查询对象中
            query.addFilterQuery(filterQuery);
        }
        //4.根据规格参数进行过滤查询
        //从searchMap获取规格参数值
        Map<String, String> specMap = (Map) searchMap.get("spec");
        //判断规格属性是否存在
        if (searchMap != null) {
            //循环遍历specMap
            for (String key : specMap.keySet()) {
                //创建criteria对象
                Criteria criteria1 = new Criteria("item_spec_" + key).is(specMap.get(key));
                //创建过滤查询对象
                FilterQuery filterQuery = new SimpleHighlightQuery(criteria1);
                //把过滤条件添加到查询对象中
                query.addFilterQuery(filterQuery);
            }
        }
        //5.价格过滤查询
        //从searchMap获取价格数据
        //0-500元,500-1000元,1500-2000元,2000-3000元,3000-*元以上.
        String price = (String) searchMap.get("price");
        //判断价格是否为空
        if (!StringUtils.isEmpty(price)) {
            //截取价格
            String[] prices = price.split("-");

            //判断价格区间
            if (!prices[0].equals("0")) {
                //创建criteria对象
                Criteria criteria1 = new Criteria("item_price").greaterThanEqual(prices[0]);
                //创建过滤查询对象
                FilterQuery filterQuery = new SimpleHighlightQuery(criteria1);
                //把过滤条件添加到查询对象中
                query.addFilterQuery(filterQuery);
            }
            if (!prices[1].equals("*")) {
                //创建criteria对象
                Criteria criteria1 = new Criteria("item_price").lessThanEqual(prices[1]);
                //创建过滤查询对象
                FilterQuery filterQuery = new SimpleHighlightQuery(criteria1);
                //把过滤条件添加到查询对象中
                query.addFilterQuery(filterQuery);
            }
        }
        //6.排序查询.
        //获取排序字段
        String sortField = (String) searchMap.get("sortField");
        //获取排序的方法
        String sort = (String) searchMap.get("sort");
        //判断排序字段是否有值
        if (!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sort)) {
            if (sort.equals("ASC")) {
                //创建排序对象
                Sort sort1 = new Sort(Sort.Direction.ASC, "item_" + sortField);
                //排序 添加查询对象中
                query.addSort(sort1);
            } else if (sort.equals("DESC")) {
                //创建排序对象
                Sort sort1 = new Sort(Sort.Direction.DESC, "item_" + sortField);
                //排序 添加查询对象中
                query.addSort(sort1);
            }
        }
        //7.分页查询
        //获取当前页页码值
        Integer page = (Integer) searchMap.get("page");
        //获取每页显示的条数
        Integer pageSize = (Integer) searchMap.get("pageSize");

        //判断当前页为空
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 40;
        }

        //计算查询的起始页
        int starNo = (page - 1) * pageSize;
        //设置分页
        query.setOffset(starNo);
        query.setRows(pageSize);


        //8.1).创建高亮对象设置高亮.
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_titel");
        highlightOptions.setSimplePrefix("<font color='red'>");
        highlightOptions.setSimplePostfix("</font>");

        //8.2).把高亮对象添加到查询对象中
        query.setHighlightOptions(highlightOptions);


        //分页高亮查询
        HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //获取总记录数
        long totalElements = highlightPage.getTotalElements();
        //获取总页码数= 总记录数/每页大小.
        int totalPages = highlightPage.getTotalPages();
        //获取总记录
        List<TbItem> list = highlightPage.getContent();

        //循环记录,获取高亮字段.
        for (TbItem item : list) {
            //获取高亮
            List<HighlightEntry.Highlight> highlights = highlightPage.getHighlights(item);
            //判断高亮是否存在
            if (highlights != null && highlights.size() > 0) {
                //获取高亮字段
                String highLighTitle = highlights.get(0).getSnipplets().get(0);
                //把高亮字段添加到对象
                item.setTitle(highLighTitle);
            }

        }


        //创建map对象,封装查询结果
        Map map = new HashMap();
        map.put("page", page);
        map.put("total", totalElements);
        map.put("totalPages", totalPages);
        map.put("rows", list);
        return map;
    }
}
