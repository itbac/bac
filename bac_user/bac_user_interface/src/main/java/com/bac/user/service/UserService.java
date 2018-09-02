package com.bac.user.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.bac.pojo.TbUser;

import com.bac.utils.PageResult;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface UserService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbUser> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add(TbUser user);


    /**
     * 修改
     */
    public void update(TbUser user);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public TbUser findOne(Long id);


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
    public PageResult findPage(TbUser user, int pageNum, int pageSize);

    /*
    需求:根据手机号获取短信验证码
    验证码在手机里,不在代码里,没有返回值 .O(∩_∩)O哈哈~
     */
    public void getSmsCode(String phone) throws UnsupportedEncodingException;

    /*
    需求:验证验证码是否匹配
     */
    public boolean checkCode(String phone, String smsCode);


}
