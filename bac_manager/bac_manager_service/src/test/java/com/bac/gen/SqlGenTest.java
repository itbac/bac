package com.bac.gen;

import com.bac.mapper.TbItemMapper;
import com.bac.mapper.TbSpecificationMapper;
import com.bac.pojo.TbItem;
import com.bac.pojo.TbSpecification;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-dao.xml")

public class SqlGenTest {
    //注入mapper接口代理对象
    @Autowired
    private TbSpecificationMapper specificationMapper;

    /*
    需求:测试mybatis逆向工程代码使用方法.
    查询
     */
    @Test
    public void add() {
        PageHelper.startPage(1, 10);
        Page<TbSpecification> pageInfo = (Page<TbSpecification>) specificationMapper.selectByExample(null);
        System.out.println("SpecificationServiceImpl:"+pageInfo);

//        List<TbSpecification> tbSpecifications = specificationMapper.selectByExample(null);
//        System.out.println(tbSpecifications);
    }
}
