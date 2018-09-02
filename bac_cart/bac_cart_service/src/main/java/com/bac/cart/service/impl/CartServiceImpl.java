package com.bac.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bac.cart.service.CartService;
import com.bac.mapper.TbItemMapper;
import com.bac.pojo.TbItem;
import com.bac.pojo.TbOrderItem;
import com.bac.vo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    //注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    //注入商品mapper接口代理对象
    @Autowired
    private TbItemMapper itemMapper;

    /*
查询redis购物车
参数:String userId
返回值:List<Cart>
redis购物车数据结构:hash
key :resid_cart
field:userI
value:json(list<Cart>)
 */

    public List<Cart> findRedisCartList(String userId) {
        //从redis中查询购物车数据
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("redis_cart").get(userId);
        //判断
        if (cartList == null || cartList.size() == 0) {
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    /*
   需求:组装购物车的结构.
   参数1:List<Cart> cartList 原来的购物车列表
   参数2:商品id
   参数3:购买的商品数量num
   返回值:List<Cart>
   组装购物车商品数据结构业务:
   1.根据商品id查询商品
   2.判断商品是否存在
   3.判断商品是否上架(可以卖)
   4.判断是否有相同的商家,判断购买是否有相同的商家,相同的商品.
   5.如果有相同的商家,判断购买是否有相同商家中相同的商品
   6.如果有相同商家,相同的商品:商品数量相加,总价格从新计算.
   7.如果有相同商家,没有相同的商品,新增的商品添加到此商家的购物车
   8.没有相同的商家,新建商家,添加商品数据
    */
    public List<Cart> addCartLsit(List<Cart> cartList, Long itemId, Integer num) {
        //1.根据商品id查询商品
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        //2.判断商品是否存在
        if (item == null) {
            throw new RuntimeException("此商品不存在");
        }
        //3.判断商品是否上架(可以卖)
        if (!item.getStatus().equals("1")) {
            throw new RuntimeException("此商品不可用");
        }
        //4.判断是否有相同的商家
        //cart对象代表一个商家
        Cart cart = this.isSameSeller(cartList, item.getSellerId());

        //判断购物车对象是否为空
        //如果不为空,表示有相同的商家.
        if (cart != null) {
            //有相同商家
            //获取此商家购物车明细
            List<TbOrderItem> orderItemList = cart.getOrderItems();
            //判断是否有相同的商品
            TbOrderItem orderItem = this.isSameItem(orderItemList, itemId);

            //判断是否为空.有相同的商品
            if (orderItem != null) {
                //有相同的商品
                //数量相加,
                orderItem.setNum(orderItem.getNum() + num);
                //计算总价格
                orderItem.setTotalFee(new BigDecimal(orderItem.getNum() * orderItem.getPrice().doubleValue()));

            } else {
                //没有相同的商品
                //创建购物商品对象
                TbOrderItem orderItem1 = this.createOrderItem(item, itemId, num);

                //把新的商品添加到此商家的购物车明细中
                orderItemList.add(orderItem1);


            }
        } else {
            //没有相同的商家
            cart = new Cart();
            //设置此商家的购物车数据
            cart.setSellerId(item.getSellerId());
            cart.setSellerName(item.getSeller());
            //设置购物车明细
            //创建购物商品对象
            TbOrderItem orderItem1 = this.createOrderItem(item, itemId, num);


            //新建一个购物明细集合
            List<TbOrderItem> orderItems = new ArrayList<>();

            orderItems.add(orderItem1);

            cart.setOrderItems(orderItems);

            //把此商家对象添加回去购物列表
            cartList.add(cart);


        }
        return cartList;
    }

    /*
  添加redis购物车
   */
    public void addRedisCart(List<Cart> cartList, String username) {
        //redis模板对象添加购物车
        redisTemplate.boundHashOps("redis_cart").put(username, cartList);

    }

    /*
    需求:创建新的购物商品对象.
     */
    private TbOrderItem createOrderItem(TbItem item, Long itemId, Integer num) {

        TbOrderItem orderItem1 = new TbOrderItem();
        orderItem1.setGoodsId(item.getGoodsId());
        orderItem1.setItemId(itemId);
        orderItem1.setSellerId(item.getSellerId());
        orderItem1.setTitle(item.getTitle());
        orderItem1.setNum(item.getNum());
        orderItem1.setTotalFee(new BigDecimal(num * item.getPrice().doubleValue()));
        orderItem1.setPicPath(item.getImage());

        return orderItem1;

    }

    /*
    需求:判断相同的商家中是否存在相同的商品.
     */
    private TbOrderItem isSameItem(List<TbOrderItem> orderItems, Long itemId) {
        //循环购物车明细集合,判断是否有相同的商品
        for (TbOrderItem orderItem : orderItems) {
            //比较商品id是否相等,如果相等,表示有相同的商品
            if (orderItem.getItemId() == itemId.longValue()) {
                return orderItem;
            }
        }
        return null;
    }

    /*
    需求:判断是否有相同商家
     */
    private Cart isSameSeller(List<Cart> cartList, String sellerId) {
        //循环购物车列表,判断是否有相同商家
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }
        return null;
    }
}
