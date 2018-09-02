package com.bac.user.controller;

import java.util.List;

import com.bac.utils.PhoneFormatCheckUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.bac.pojo.TbUser;
import com.bac.user.service.UserService;

import com.bac.utils.PageResult;
import com.bac.utils.BacResult;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(timeout = 1000000000)
    private UserService userService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbUser> findAll() {
        return userService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage/{pageNum}/{pageSize}")
    public PageResult findPage(@PathVariable int pageNum, @PathVariable int pageSize) {
        return userService.findPage(pageNum, pageSize);
    }

    /**
     * 增加
     *
     * @param user
     * @return
     */
    @RequestMapping("/add/{smsCode}")
    public BacResult add(@RequestBody TbUser user, @PathVariable String smsCode) {
        try {
            //验证验证码是否正确
            boolean flag = userService.checkCode(user.getPhone(), smsCode);
            if (!flag) {
                return new BacResult(false, "验证码错误");
            }
            userService.add(user);
            return new BacResult(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param user
     * @return
     */
    @RequestMapping("/update")
    public BacResult update(@RequestBody TbUser user) {
        try {
            userService.update(user);
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
    public TbUser findOne(@PathVariable Long id) {
        return userService.findOne(id);
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
            userService.delete(ids);
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
    public PageResult search(@RequestBody TbUser user, int pageNum, int pageSize) {
        return userService.findPage(user, pageNum, pageSize);
    }

    /*
    需求:根据手机号获取短信验证码
    验证码在手机里,不在代码里,没有返回值 .O(∩_∩)O哈哈~
     */
    @RequestMapping("/getSmsCode/{phone}")
    public BacResult getSmsCode(@PathVariable String phone) {
        try {
            //验证手机号格式是否正确
            if (!PhoneFormatCheckUtils.isChinaPhoneLegal(phone)) {
                return new BacResult(false, "手机号格式不正确");
            }
            //调用服务层服务
            userService.getSmsCode(phone);
            return new BacResult(true, "发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "发送失败");
        }

    }

}
