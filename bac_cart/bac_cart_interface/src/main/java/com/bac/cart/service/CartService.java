package com.bac.cart.service;

import com.bac.vo.Cart;

import java.util.List;

public interface CartService {
    /*
    查询redis购物车
    参数:String userId
    返回值:List<Cart>
     */
    public List<Cart> findRedisCartList(String userId);

    /*
      需求:组装购物车的结构.
      参数1:List<Cart> cartList 原来的购物车列表
      参数2:商品id
      参数3:购买的商品数量num
      返回值:List<Cart>
       */
    public List<Cart> addCartLsit(List<Cart> cartList, Long itemId, Integer num);

    /*
    添加redis购物车
     */
    public void addRedisCart(List<Cart> cartList, String username);
}
