package com.bac.manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.bac.pojo.TbSeller;
import com.bac.manager.service.SellerService;

import com.bac.utils.PageResult;
import com.bac.utils.BacResult;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbSeller> findAll() {
        return sellerService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public PageResult findPage(@PathVariable int pageNum, @PathVariable int pageSize) {
        return sellerService.findPage(pageNum, pageSize);
    }

    /**
     * 增加
     *
     * @param seller
     * @return
     */
    @RequestMapping("/add")
    public BacResult add(@RequestBody TbSeller seller) {
        try {
            sellerService.add(seller);
            return new BacResult(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param seller
     * @return
     */
    @RequestMapping("/update")
    public BacResult update(@RequestBody TbSeller seller) {
        try {
            sellerService.update(seller);
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
    public TbSeller findOne(@PathVariable String id) {
        return sellerService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete/{ids}")
    public BacResult delete(@PathVariable String[] ids) {
        try {
            sellerService.delete(ids);
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
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbSeller seller, int pageNum, int pageSize) {
        return sellerService.findPage(seller, pageNum, pageSize);
    }

    /*
        更改商家状态: 默认未审核0,通过1,未通过2,关闭商家3
        @param  sellerId
        @param  status
    */
    @RequestMapping("/updateStatus/{sellerId}/{status}")
    public BacResult updateStatus(@PathVariable String sellerId, @PathVariable String status) {
        try {
            sellerService.updateStatus(sellerId, status);
            return new BacResult(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "失败");
        }
    }

}
