package com.bac.manager.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bac.mapper.*;
import com.bac.pojo.*;
import com.bac.vo.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.bac.pojo.TbGoodsExample.Criteria;
import com.bac.manager.service.GoodsService;

import com.bac.utils.PageResult;
import org.springframework.util.StringUtils;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;
    //注入品牌mapper接口代理对象
    @Autowired
    private TbBrandMapper brandMapper;
    //注入分类mapper接口代理对象
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbSellerMapper sellerMapper;


    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        //添加条件查询,查询未删除的商品
        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();

        //设置非删除状态1. 被删除的状态就不查询了.
        criteria.andIsDeleteEqualTo("0");
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        //保存spu货品表对象

        //1.获取spu货品对象
        TbGoods tbGoods = goods.getGoods();
        tbGoods.setAuditStatus("0"); //设置未申请状态.
        goodsMapper.insertSelective(tbGoods);

        //获取spu货品描述表信息
        //获取描述表对象
        TbGoodsDesc goodsDesc = goods.getGoodsDesc();

        //2.获取商品表的id主键,设置为商品描述表的外键.
        goodsDesc.setGoodsId(tbGoods.getId());
        //3.保存,商品描述.
        goodsDescMapper.insertSelective(goodsDesc);

        //保存sku商品表数据
        //获取sku列表集合对象
        List<TbItem> itemList = goods.getItemList();


        //判断是否启用了规格
        if ("1".equals(tbGoods.getIsEnableSpec())) {
            //循环sku列表,保存商品数据
            for (TbItem tbItem : itemList) {
                //获取前台传递spec规格属性参数
                //{"网络":"联通4G","内存":"16G"}
                String spec = tbItem.getSpec();
                //把json字符串转换成map对象
                Map<String, String> specMap = (Map<String, String>) JSON.parse(spec);

                //定义字符变量,拼接规格值
                String specStr = "";
                //循环map
                //联通4G,16G
                for (String key : specMap.keySet()) {
                    specStr += "  " + specMap.get(key);
                }
                //设置标题
                //sku+spu
                tbItem.setTitle(tbGoods.getGoodsName() + specStr);


                //抽取公共的代码,添加商品属性.
                this.saveItem(tbGoods, tbItem, goodsDesc);


                //保存操作
                itemMapper.insertSelective(tbItem);

            }
        } else {
            //没有起用规格
            //自己创建一个商品对象进行保存
            TbItem tbItem = new TbItem();


            ////抽取公共的代码,添加商品属性.
            this.saveItem(tbGoods, tbItem, goodsDesc);


            //保存操作
            itemMapper.insertSelective(tbItem);
        }


    }

    /*
        添加商品属性.
     */
    private void saveItem(TbGoods tbGoods, TbItem tbItem, TbGoodsDesc goodsDesc) {
        //设置标题
        //spu
        tbItem.setTitle(tbGoods.getGoodsName());
        //买点
        tbItem.setSellPoint(tbGoods.getCaption());
        //设置图片地址
        //从描述对象中图片地址
        //[{"color":"白色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmNXEWAWuHOAAjlKdWCzvg949.jpg"},{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmNXEuAB_ujAAETwD7A1Is158.jpg"},{"color":"蓝色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmNXFWANtjTAAFa4hmtWek619.jpg"}]
        String itemImages = goodsDesc.getItemImages();
        //判断图片地址是否为空,
        if (!StringUtils.isEmpty(itemImages)) {
            List<Map> imageList = JSON.parseArray(itemImages, Map.class);
            tbItem.setImage((String) imageList.get(0).get("url"));
        }
        ;
        //设置三级分类节点id,表示此商品属于哪个分类
        tbItem.setCategoryid(tbGoods.getCategory3Id());
        //创建时间
        Date date = new Date();
        tbItem.setCreateTime(date);
        tbItem.setUpdateTime(date);

        //设置货品id,sku id
        tbItem.setGoodsId(tbGoods.getId());
        tbItem.setSellerId(tbGoods.getSellerId());

        //查询分类名称,根据节点id
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
        tbItem.setCategory(itemCat.getName());
        //查询品牌名称
        TbBrand brand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());
        tbItem.setBrand(brand.getName());

        //商家名称
        TbSeller seller = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
        tbItem.setSeller(seller.getNickName());

    }


    /**
     * 修改
     */
    @Override
    public void update(TbGoods goods) {
        goodsMapper.updateByPrimaryKey(goods);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbGoods findOne(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除.
     * 逻辑删除,不是真正的删除.是修改状态.
     * 1  表示删除状态. 0表示 非删除状态.
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //根据id 查出商品对象
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            //设置状态1 ,表示删除,逻辑删除.
            tbGoods.setIsDelete("1");
            //修改
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();


        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                // criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
                //根据商家id查询商品,要精确匹配,不能模糊查询
                criteria.andSellerIdEqualTo(goods.getSellerId());

            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                //商品名称 ,条件查询
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                //设置状态,条件查询. 未申请,申请中,审核通过,已驳回.
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }


        }

        //设置非删除状态0. 被删除的状态就不查询了.
        criteria.andIsDeleteEqualTo("0");

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /*
   批量修改状态  ,运营商系统审核商品
    */
    public void updateStatus(Long[] ids, String status) {
        //循环id
        for (Long id : ids) {
            //根据id查询商品对象
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            //设置状态
            tbGoods.setAuditStatus(status);
            //更新
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

    //定义是否上架方法
    public void isMarketable(String status, Long[] ids) {
        //循环id
        for (Long id : ids) {
            //根据id查询商品对象
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            //设置状态, 与上面方法设置的字段不同.
            tbGoods.setIsMarketable(status);
            //更新
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }


}
