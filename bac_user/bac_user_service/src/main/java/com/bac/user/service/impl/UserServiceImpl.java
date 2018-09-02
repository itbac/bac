package com.bac.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bac.mapper.TbUserMapper;
import com.bac.pojo.TbUser;
import com.bac.pojo.TbUserExample;
import com.bac.pojo.TbUserExample.Criteria;
import com.bac.user.service.UserService;
import com.bac.utils.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbUser> findAll() {
        return userMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbUser user) {
        //使用spring提供的工具类加密
        String newPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        //设置密码加密
        user.setPassword(newPwd);
        //设置创建时间,修改时间
        Date date = new Date();
        user.setCreated(date);
        user.setUpdated(date);
        userMapper.insertSelective(user);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbUser user) {
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbUser findOne(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            userMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbUser user, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();

        if (user != null) {
            if (user.getUsername() != null && user.getUsername().length() > 0) {
                criteria.andUsernameLike("%" + user.getUsername() + "%");
            }
            if (user.getPassword() != null && user.getPassword().length() > 0) {
                criteria.andPasswordLike("%" + user.getPassword() + "%");
            }
            if (user.getPhone() != null && user.getPhone().length() > 0) {
                criteria.andPhoneLike("%" + user.getPhone() + "%");
            }
            if (user.getEmail() != null && user.getEmail().length() > 0) {
                criteria.andEmailLike("%" + user.getEmail() + "%");
            }
            if (user.getSourceType() != null && user.getSourceType().length() > 0) {
                criteria.andSourceTypeLike("%" + user.getSourceType() + "%");
            }
            if (user.getNickName() != null && user.getNickName().length() > 0) {
                criteria.andNickNameLike("%" + user.getNickName() + "%");
            }
            if (user.getName() != null && user.getName().length() > 0) {
                criteria.andNameLike("%" + user.getName() + "%");
            }
            if (user.getStatus() != null && user.getStatus().length() > 0) {
                criteria.andStatusLike("%" + user.getStatus() + "%");
            }
            if (user.getHeadPic() != null && user.getHeadPic().length() > 0) {
                criteria.andHeadPicLike("%" + user.getHeadPic() + "%");
            }
            if (user.getQq() != null && user.getQq().length() > 0) {
                criteria.andQqLike("%" + user.getQq() + "%");
            }
            if (user.getIsMobileCheck() != null && user.getIsMobileCheck().length() > 0) {
                criteria.andIsMobileCheckLike("%" + user.getIsMobileCheck() + "%");
            }
            if (user.getIsEmailCheck() != null && user.getIsEmailCheck().length() > 0) {
                criteria.andIsEmailCheckLike("%" + user.getIsEmailCheck() + "%");
            }
            if (user.getSex() != null && user.getSex().length() > 0) {
                criteria.andSexLike("%" + user.getSex() + "%");
            }

        }

        Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    //注入jsm模板对象
    @Autowired
    private JmsTemplate jmsTemplate;

    //注入sms参数.在dao层
    @Value("${sign_name}")
    private String sign_name;
    @Value("${template_code}")
    private String template_code;

    /*
   需求:根据手机号获取短信验证码
   验证码在手机里,不在代码里,没有返回值 .O(∩_∩)O哈哈~
   1.生成6位数的验证码.
   2.把验证码存储到redis服务器
   3.设置验证码过期时间 5分钟
   4.发送消息
    */
    public void getSmsCode(String phone)  {

        //1.生成6位数的验证码.
        //0.02345644554
        long code = (long) (Math.random() * 1000000);
        //如果生成的6位数,前面包含0,变成整数后就不够6位,可以在后面补0.
        while (code < 100000) {
            code = code * 10;
        }
        ;
        String codeStr = Long.toString(code);
        //2.把验证码存储到redis服务器.
        redisTemplate.boundHashOps("smsCode").put(phone, codeStr);
        //3.设置验证码过期时间 5分钟
        redisTemplate.boundHashOps("smsCode").expire(5, TimeUnit.MINUTES);

        String sname = null;

        try {
            //4.处理配置文件中sign_name编码乱码
            sname = new String(sign_name.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        //创建map,封装发送的数据
        Map<String, String> maps = new HashMap<>();
        //手机号
        maps.put("phone", phone);
        //签名
        maps.put("sign_name", sname);
        //模板code
        maps.put("template_code", template_code);
        //验证码
        maps.put("code", codeStr);

        //4.发送消息
        jmsTemplate.convertAndSend("sms", maps);

    }

    /*
    需求:验证验证码是否匹配
     */
    public boolean checkCode(String phone, String smsCode) {
        //取出redis服务器中的验证码
        String code = (String) redisTemplate.boundHashOps("smsCode").get(phone);
        if (code.equals(smsCode)) {
            return true;
        }
        return false;
    }


}
