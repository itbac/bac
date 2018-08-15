package com.bac.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bac.manager.service.SellerService;
import com.bac.pojo.TbSeller;
import com.bac.utils.BacResult;
import com.bac.utils.PageResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
            //创建BCrypt加密对象
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //对前端传过来的商家注册信息的密码进行加密,设置到商家表中
            String newPwd = passwordEncoder.encode(seller.getPassword());
            seller.setPassword(newPwd);
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

}
