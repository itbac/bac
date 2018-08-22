package com.bac.solrj;

import com.bac.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-solr.xml")
public class MySolr {

    //注入solr模板
    @Autowired
    private SolrTemplate solrTemplate;

    /*
    需求:测试, 索引库的添加操作.
    1.如果id存在 ,就是修改.
    2.如果id不存在, 就是添加.
     */
    @Test
    public void addIndex() {
        //创建商品对象
        TbItem item = new TbItem();
        item.setId(100000000001L);
        item.setTitle("最好的标题1");
        item.setSellPoint("最大的卖点1");
        item.setBrand("伟大的品牌1");
        //保存到索引库
        solrTemplate.saveBean(item);
        //提交
        solrTemplate.commit();
    }

    /*
显示记录数据
 */
    private void showList(List<TbItem> list) {
        for (TbItem item : list) {
            System.out.println(item.getTitle() + "***" + item.getPrice());
        }
    }

    /*
    需求:根据id删除  传入字符串,不用加L 字母.
     */
    @Test
    public void findByid() {
        // solrTemplate.deleteById("100000000001");
        //根据id删除
        //批量删除

        //根据查询删除,查询所有,就会删除所有.
        Query query = new SimpleQuery("*:*");

        solrTemplate.delete(query);

        //提交
        solrTemplate.commit();
    }

    /*
     需求:循环插入100条测试数据,用于分页查询
     */
    @Test
    public void testAddList() {
        List<TbItem> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            TbItem item = new TbItem();
            item.setId(i + 1L);
            item.setBrand("华为");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为2号专卖店");
            item.setTitle("华为Mate" + i);
            item.setPrice(new BigDecimal(2000 + i));
            list.add(item);
        }

        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    /*
    需求:分页查询.
     */
    @Test
    public void queryIndexForPage() {
        //创建查询对象,封装查询对象
        Query query = new SimpleQuery("*:*");

        //设置分页查询条件.
        //设置分页查询起始位置.
        query.setOffset(10);
        //设置每页显示条数
        query.setRows(20);

        //查询
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        //获取查询总记录数
        long totalElements = scoredPage.getTotalElements();
        System.out.println("总记录数" + totalElements);

        List<TbItem> list = scoredPage.getContent();

        //显示记录数据,查询多少页就显示多少条.
        showList(list);
    }


    /*
    Criteria用于对条件的封装:
     */
    @Test
    public void testPageQueryMutil() {
        //标题含有2 或 5 .
        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_title").contains("2");
        criteria.and("item_title").contains("5");

        query.addCriteria(criteria);

        //设置分页查询条件.
        //设置分页查询起始位置.
        query.setOffset(10);
        //设置每页显示条数
        query.setRows(20);

        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        System.out.println("总记录数:" + page.getTotalElements());
        List<TbItem> content = page.getContent();
        showList(content);
    }

    /*
    需求:高亮查询
     */
    @Test
    public void queryIndexForHighLight() {
        //创建高亮查询对象,封装查询对象.
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //创建criteria对象,设置查询条件
        Criteria criteria = new Criteria("item_title").is("黑妹");
        //把条件对象添加到query对象中
        query.addCriteria(criteria);

        //1.创建高亮对象设置高亮.
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_titel");
        highlightOptions.setSimplePrefix("<font color='red'>");
        highlightOptions.setSimplePostfix("</font>");

        //2.把高亮对象添加到查询对象中
        query.setHighlightOptions(highlightOptions);

        //查询
        HighlightPage<TbItem> tbItems = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //获取查询总记录数
        long totalElements = tbItems.getTotalElements();
        System.out.println("总记录数" + totalElements);

        //获取总记录
        List<TbItem> list = tbItems.getContent();
        for (TbItem item : list) {
            //获取高亮
            List<HighlightEntry.Highlight> highlights = tbItems.getHighlights(item);
            String name = highlights.get(0).getSnipplets().get(0);
            System.out.println(name);
        }


    }
}
