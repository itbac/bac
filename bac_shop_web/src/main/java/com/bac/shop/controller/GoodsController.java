package com.bac.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bac.manager.service.GoodsService;
import com.bac.pojo.TbGoods;
import com.bac.utils.BacResult;
import com.bac.utils.PageResult;
import com.bac.vo.Goods;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public PageResult findPage(@PathVariable int pageNum, @PathVariable int pageSize) {
        return goodsService.findPage(pageNum, pageSize);
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    public BacResult add(@RequestBody Goods goods) {
        try {
            goodsService.add(goods);
            return new BacResult(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public BacResult update(@RequestBody TbGoods goods) {
        try {
            goodsService.update(goods);
            return new BacResult(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public TbGoods findOne(@PathVariable Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除 , 逻辑删除,不是真正的删除.
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete/{ids}")
    public BacResult delete(@PathVariable Long[] ids) {
        try {
            goodsService.delete(ids);
            return new BacResult(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/search/{pageNum}/{pageSize}")
    public PageResult search(@RequestBody TbGoods goods, @PathVariable int pageNum, @PathVariable int pageSize) {
        //获取商家id
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        //添加查询条件
        goods.setSellerId(sellerId);
        return goodsService.findPage(goods, pageNum, pageSize);
    }

    /*
    设置上下架状态.
     */
    @RequestMapping("/isMarketable/{status}/{ids}")
    public BacResult isMarketable(@PathVariable String status,@PathVariable Long[] ids) {
        try {
            goodsService.isMarketable(status, ids);
            return new BacResult(true, "上下架成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "上下架失败");
        }
    }


}
