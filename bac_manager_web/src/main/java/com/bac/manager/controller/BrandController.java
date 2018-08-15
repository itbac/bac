package com.bac.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bac.manager.service.BrandService;
import com.bac.pojo.TbBrand;
import com.bac.utils.BacResult;
import com.bac.utils.PageResult;
import org.junit.Test;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    //    注入远程服务的service对象
    //timeout 超时时间,防止服务调用超时.
    @Reference(timeout = 1000000)
    private BrandService brandService;

    /*
    需求:查询所有品牌
     */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        //调用远程service服务方法.
        List<TbBrand> brandList = brandService.findAll();
        return brandList;
    }

    /**
     * 分页 ,返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public PageResult findPage(@PathVariable int pageNum, @PathVariable int pageSize) {
        return brandService.findPage(pageNum, pageSize);
    }


    /*
  需求:插入品牌
   */
    @RequestMapping("/add")
    public BacResult add(@RequestBody TbBrand brand) {

        try {
            brandService.add(brand);
            return new BacResult(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "增加失败");
        }
    }

    /*
    修改品牌
     */
    @RequestMapping("/update")
    public BacResult update(@RequestBody TbBrand brand) {
        try {
            brandService.update(brand);
            return new BacResult(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "修改失败");
        }
    }

    /*
    根据id获取品牌信息
     */
    @RequestMapping("/findOne")
    public TbBrand findOne(long id) {
        return brandService.findOne(id);
    }

    /*
   根据id删除品牌信息
   批量删除.
    */
    @RequestMapping("/delete/{ids}")
    public BacResult delete(@PathVariable long[] ids) {
        try {

            brandService.delete(ids);
            return new BacResult(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "删除失败");
        }
    }


    /*
   需求:查询品牌下拉列表
   后端查询返回数据格式:List<Map> = [{id=1, text=联想}, {id=2, text=华为}]
   后端转换成json数组格式传到前端       [{id:'1',text:'联想'},{id:'2',text:'华为'}]
   返回值:List<Map>
    */
    @RequestMapping("/findBrandList")
    public List<Map> findBrandList() {
        List<Map> brandList = brandService.findBrandList();
        return brandList;
    }


}
