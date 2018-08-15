package com.bac.manager.service;

import com.bac.pojo.TbBrand;
import com.bac.utils.PageResult;

import java.util.List;
import java.util.Map;

public interface BrandService {


    /*
    需求:查询所有品牌数据
     */
    public List<TbBrand> findAll();

    /*
    返回分页列表
     */
    public PageResult findPage(int pageNum, int pageSize);

    /*
    需求:插入品牌数据
     */
    public void add(TbBrand brand);

    /*
    需求:根据id查品牌,用于回显
     */
    public TbBrand findOne(Long id);

    /*
    需求:修改品牌信息.
     */
    public void update(TbBrand brand);

    /*
    需求:根据id删除品牌数据
     */
    public void delete(long[] ids);

    /*
    需求:查询品牌下拉列表
    查询数据格式:[{id:'1',text:'联想'},{id:'2',text:'华为'}]
    返回值:List<Map>
     */
    public List<Map> findBrandList();


}
