package com.bac.cart.controller;


public class CartController {

    /*
    需求:添加购物车
    业务场景:
    1.未登录 --- 添加购物车 cookie
    2.登录 --- 添加购物车 redis
    业务分析:
    1.查询购物车列表(redis, cookie)
    2.把新的商品添加购物车原来的购物车里面
    3.如果没有登录,使用cookie存储购物车
    4.一旦登录,必须把cookie购物车合并到redis购物车
    5.如果登录状态,添加redis购物车
    6.购物车删除,清空,数量修改.
     */



}
