package com.bac.content.service.impl;

import java.util.List;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.bac.mapper.TbContentMapper;
import com.bac.pojo.TbContent;
import com.bac.pojo.TbContentExample;
import com.bac.pojo.TbContentExample.Criteria;
import com.bac.content.service.ContentService;

import com.bac.utils.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    /*
    注入redis模板对象
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        //新增广告后,清除redis缓存
        contentMapper.insert(content);
        redisTemplate.boundHashOps("INDEX_CHAE").delete(content.getCategoryId());
    }


    /**
     * 修改
     */
    @Override
    public void update(TbContent content) {
        //修改广告后清楚缓存.
        //1.查询修改前的分类id (因为还没修改数据库,数据库里还是之前的categoryId .)
        Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();

        contentMapper.updateByPrimaryKey(content);

        //2.根据修改之前的categoryId删除 缓存.
        redisTemplate.boundHashOps("INDEX_CHAE").delete(categoryId);
        //3.根据修改之后的categoryId删除 缓存.
        redisTemplate.boundHashOps("INDEX_CHAE").delete(content.getCategoryId());

    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //删除广告后清楚缓存.
            //1.查询修改前的分类id
            TbContent tbContent = contentMapper.selectByPrimaryKey(id);
            Long categoryId = tbContent.getCategoryId();

            contentMapper.deleteByPrimaryKey(id);

            //2.根据categoryId删除缓存.
            redisTemplate.boundHashOps("INDEX_CHAE").delete(categoryId);

        }
    }


    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }

        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /*
     需求:根据内容分类id查询广告内容
     此方法是前台门户系统调用,负责查询广告数据的方法,因此此方法并发压力非常大,为了减轻数据库压力,
     提高查询效率,提高并发能力,需要对此方法加入缓存.
     缓存业务:
     1.每次查询广告数据时,先查询redis服务器缓存.
     2.如果缓存中没有数据,在查询数据库数据,同时把数据放入缓存,下次查询缓存就有数据了
     3.如果缓存中有数据,直接返回即可,不再查询数据库
     缓存服务器:使用redis服务器
     数据结构:hash
     key标识的是导航页的缓存: INDEX_CHAE 首页缓存   FOOD_CHACHE 实体页缓存
     field : categoryId 表示页面哪个区域的缓存, 1,大广告区域缓存, 2.今日推荐缓存.
     value:缓存数据(json字符串)
      */
    public List<TbContent> findContentByCategoryId(Long categoryId) {

        //1.先查询缓存
        String index_chae = (String) redisTemplate.boundHashOps("INDEX_CHAE").get(categoryId);
        //判断缓存是否存在
        if (!StringUtils.isEmpty(index_chae)) {
            //把json字符串转换成json 对象
            List<TbContent> list = JSON.parseArray(index_chae, TbContent.class);
            return list;
        }

        //创建example对象
        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();

        //设置查询参数,根据外键查询广告信息
        criteria.andCategoryIdEqualTo(categoryId);
        //2.从数据库查询.
        List<TbContent> list = contentMapper.selectByExample(example);

        //3.转换成json字符串.放入缓存.
        redisTemplate.boundHashOps("INDEX_CHAE").put(categoryId, JSON.toJSONString(list));

        return list;
    }

}
