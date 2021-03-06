package com.bac.manager.service;

import java.util.List;

import com.bac.pojo.TbGoods;

import com.bac.pojo.TbItem;
import com.bac.utils.PageResult;
import com.bac.vo.Goods;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface GoodsService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbGoods> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add(Goods goods);


    /**
     * 修改
     */
    public void update(TbGoods goods);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public TbGoods findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize);


    /*
    批量修改审核状态,
     */
    public void updateStatus(Long[] ids, String status);

    //定义是否上架方法
    public void isMarketable(String status, Long[] ids);


    //需求:查询所有审核通过的sku商品
    public List<TbItem> findItemList(Long[] ids);
}
