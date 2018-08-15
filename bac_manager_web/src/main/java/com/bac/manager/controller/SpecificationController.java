package com.bac.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bac.manager.service.SpecificationService;
import com.bac.utils.BacResult;
import com.bac.utils.PageResult;
import com.bac.vo.Specification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    //注入远程service服务对象
    @Reference(timeout = 100000000)
    private SpecificationService specificationService;

    /*
    需求:查询规格分页列表数据
     */
    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public PageResult findPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {

        //调用远程服务方法
        return specificationService.findPage(pageNum, pageSize);


    }

    /*
    需求:实现规格,规格选项保存
    参数:Specification包装类对象
    返回:bacResu lt
    注意:angularJS前台传递的参数都是json格式
    逆向分析: specification 的json格式数据
    specification = {specification:{},specificationOptionList:[{},{}....]}
    前端代码:
    angularJS就是需要封装specification的json数据,把数据组装好了以后
    直接发送给后端代码.
     */
    @RequestMapping("add")
    public BacResult add(@RequestBody Specification specification) {
        try {
            //调用远程服务的方法.
            specificationService.add(specification);
            return new BacResult(true, "保存成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "保存失败");
        }

    }

    @RequestMapping("/findOne/{id}")
    public Specification findOne(@PathVariable Long id) {

        return specificationService.findOne(id);
    }


    /*
    需求:修改规格及规格选项数据.
     */
    @RequestMapping("/update")
    public BacResult update(@RequestBody Specification specification) {
        try {
            specificationService.update(specification);
            return new BacResult(true, "修改成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "修改失败");
        }

    }

    /*
   需求:删除规格及规格选项数据.
    */
    @RequestMapping("del/{ids}")
    public BacResult delete(@PathVariable Long[] ids) {

        try {
            specificationService.delete(ids);
            return new BacResult(true, "删除成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "删除失败");
        }
    }

    /*
    需求:查询规格值,进行下拉列表展示
     */
    @RequestMapping("/findSpecList")
    public List<Map> findSpecList() {
        return specificationService.findSpecList();
    }
}
