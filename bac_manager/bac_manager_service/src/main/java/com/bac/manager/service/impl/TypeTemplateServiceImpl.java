package com.bac.manager.service.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bac.mapper.TbSpecificationOptionMapper;
import com.bac.pojo.TbSpecificationOption;
import com.bac.pojo.TbSpecificationOptionExample;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.bac.mapper.TbTypeTemplateMapper;
import com.bac.pojo.TbTypeTemplate;
import com.bac.pojo.TbTypeTemplateExample;
import com.bac.pojo.TbTypeTemplateExample.Criteria;
import com.bac.manager.service.TypeTemplateService;

import com.bac.utils.PageResult;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TbTypeTemplateMapper typeTemplateMapper;
    @Autowired
    private TbSpecificationOptionMapper tbSpecificationOptionMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbTypeTemplate> findAll() {
        return typeTemplateMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbTypeTemplate typeTemplate) {
        typeTemplateMapper.insert(typeTemplate);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbTypeTemplate typeTemplate) {
        typeTemplateMapper.updateByPrimaryKey(typeTemplate);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbTypeTemplate findOne(Long id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            typeTemplateMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbTypeTemplateExample example = new TbTypeTemplateExample();
        Criteria criteria = example.createCriteria();

        if (typeTemplate != null) {
            if (typeTemplate.getName() != null && typeTemplate.getName().length() > 0) {
                criteria.andNameLike("%" + typeTemplate.getName() + "%");
            }
            if (typeTemplate.getSpecIds() != null && typeTemplate.getSpecIds().length() > 0) {
                criteria.andSpecIdsLike("%" + typeTemplate.getSpecIds() + "%");
            }
            if (typeTemplate.getBrandIds() != null && typeTemplate.getBrandIds().length() > 0) {
                criteria.andBrandIdsLike("%" + typeTemplate.getBrandIds() + "%");
            }
            if (typeTemplate.getCustomAttributeItems() != null && typeTemplate.getCustomAttributeItems().length() > 0) {
                criteria.andCustomAttributeItemsLike("%" + typeTemplate.getCustomAttributeItems() + "%");
            }

        }

        Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /*
	查询规格及规格选项数据
	业务:
	1.查询模板表
	2.获取模板表规则属性
	3.把规格属性 转换map对象
	4.获取map对象中规格id
	5.根据规格id查询规格选项
	6.返回结果
	 */
    public List<Map> findSpecList(Long typeId) {
        //1.根据主键id 35,查询模板表tb_type_template
        TbTypeTemplate tbTypeTemplate = typeTemplateMapper.selectByPrimaryKey(typeId);
        //2.获取模板表的规格属性字段spec_ids
        String specIds = tbTypeTemplate.getSpecIds();
        //3.把规格属性转换List<Map>对象   [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        List<Map> list = JSON.parseArray(tbTypeTemplate.getSpecIds(), Map.class);
        //4.循环规格集合,获取每一个规格
        for (Map map : list) {
            //获取map对象中规格id, 如:27
            Long specId = new Long((Integer) map.get("id"));
            //查询规格选项列表
            //根据外键查询规格选项
            //创建example对象
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            //设置外键参数
            criteria.andSpecIdEqualTo(specId);
            //执行查询
            List<TbSpecificationOption> optionsList = tbSpecificationOptionMapper.selectByExample(example);
            //把规格选项集合放入map对象,返回
            //[{"id":27,"text":"网络","optionList":[{},{}]},{"id":32,"text":"机身内存"},"optionList":[{},{}]]
            map.put("optionsList", optionsList);
        }
        return list;
    }

}
