package com.bac.mapper;

import com.bac.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface BrandMapper {
    /*
    需求:查询所有的品牌数据.
     */
    public List<TbBrand> findAll();

    /*
    需求:添加品牌参数
     */
    public void insert(TbBrand brand);

    /*
    需求:修改品牌
     */
    public void updateByPrimaryKey(TbBrand brand);

    /*
    需求:根据id查询品牌数据,用于修改回显.
     */
    public TbBrand findOne(long id);

    /*
    需求:根据id删除品牌数据
     */
    public void delete(long id);

    /*
    需求:查询品牌下拉列表
    查询数据格式:[{id:'1',text:'联想'},{id:'2',text:'华为'}]
    返回值:List<Map>
     */
    public List<Map> findBrandList();

}
