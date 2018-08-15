package com.bac.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bac.manager.service.ItemCatService;
import com.bac.pojo.TbItemCat;
import com.bac.utils.BacResult;
import com.bac.utils.PageResult;
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
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbItemCat> findAll() {
        return itemCatService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public PageResult findPage(@PathVariable int pageNum, @PathVariable int pageSize) {
        return itemCatService.findPage(pageNum, pageSize);
    }

    /**
     * 增加
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/add")
    public BacResult add(@RequestBody TbItemCat itemCat) {
        try {
            itemCatService.add(itemCat);
            return new BacResult(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/update")
    public BacResult update(@RequestBody TbItemCat itemCat) {
        try {
            itemCatService.update(itemCat);
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
    public TbItemCat findOne(@PathVariable  Long id) {
        return itemCatService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete/{ids}")
    public BacResult delete(@PathVariable Long[] ids) {
        try {
            itemCatService.delete(ids);
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
    public PageResult search(@RequestBody TbItemCat itemCat, int pageNum, int pageSize) {
        return itemCatService.findPage(itemCat, pageNum, pageSize);
    }

    /*
   需求:根据父id查询子节点
   定义方法:查询商品分类列表.
    */
    @RequestMapping("/findItemCatList/{parentId}")
    public List<TbItemCat> findItemCatList(@PathVariable Long parentId) {
        return itemCatService.findItemCatList(parentId);
    }

}
