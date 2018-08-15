package com.bac.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bac.manager.service.BrandService;
import com.bac.mapper.BrandMapper;
import com.bac.pojo.TbBrand;
import com.bac.utils.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

//Service导入的是dubbo的注解. 把这个类自动注册到zookeeper注册中心
@Service
public class BrandServiceImpl implements BrandService {

    //注入mapper接口代理对象
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.findAll();
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        //分页,结果封装到PageHelper提供的Page类
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.findAll();

        //从Page类获取数据,封装到实体类.
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(TbBrand brand) {
        brandMapper.insert(brand);
    }

    /*
    根据id查品牌
     */
    public TbBrand findOne(Long id) {
        return brandMapper.findOne(id);
    }

    @Override
    public void update(TbBrand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    /*
    批量删除.遍历ids.调用删除方法.
     */
    public void delete(long[] ids) {
        for (long id : ids) {
            brandMapper.delete(id);
        }

    }

    /*
   需求:查询品牌下拉列表
   查询数据格式:[{id:'1',text:'联想'},{id:'2',text:'华为'}]
   返回值:List<Map>
    */
    public List<Map> findBrandList() {
        return brandMapper.findBrandList();
    }


}
