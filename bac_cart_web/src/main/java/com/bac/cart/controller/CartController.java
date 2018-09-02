package com.bac.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.bac.cart.service.CartService;
import com.bac.utils.BacResult;
import com.bac.utils.CookieUtil;
import com.bac.vo.Cart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    //注入远程购物车对象
    @Reference(timeout = 1000000)
    private CartService cartService;

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
    @RequestMapping("/addGoodsToCartList/{itemId}/{num}")
    public BacResult addGoodsToCartList(@PathVariable Long itemId,
                                        @PathVariable Integer num,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        List<Cart> cartList = null;
        try {
            //获取用户登录信息
            String username = SecurityContextHolder.getContext().getAuthentication().getName();


            //1.查询购物车的数据
            cartList = this.findCarList(request);
            //2.添加购物车数据,把新的商品添加购物车cartList购物车列表中
            //组装购物车数据结构
            //List<Cart>
            cartList = cartService.addCartLsit(cartList, itemId, num);

            //判断用户是否处于登录状态.
            if (username.equals("anonymousUser")) {
                //未登录状态.
                //把购物车数据添加到cookie购物车
                CookieUtil.setCookie(request, response, "cookie_cart",
                        JSON.toJSONString(cartList), 26000, true);

            } else {
                //登录状态
                //把购物车数据添加到redis购物车
                cartService.addRedisCart(cartList, username);


            }
            return new BacResult(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new BacResult(false, "添加失败");
        }


    }

    /*
    需求：查询购物车列表，在购物车列表的基础上实现购物车操作，添加。
    业务分析：
    1.未登录
            查询cookie购物车
    2.登录状态   查询redis购物车
     */
    @RequestMapping("/findCarList")
    public List<Cart> findCarList(HttpServletRequest request) {

        //获取用户登录信息
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //判断用户是否处于登录状态.
        if (userId.equals("anonymousUser")) {
            //此时处于未登录状态.
            //获取cookie购物车中购物车数据.
            String cookie_cart = CookieUtil.getCookieValue(request, "cookie_cart", true);

            //判断cookie购物车数据是否为空.
            if (!StringUtils.isNotBlank(cookie_cart)) {
                //org.apache.commons.lang3.StringUtils;
                //isNotBlank  表示 NOT NULL ""  "   "
                //isNotEmpty  表示 NOT NULL ""
                cookie_cart = "[]";
            }
            //把cookie购物车数据转换购物车集合对象.
            List<Cart> cartsList = JSON.parseArray(cookie_cart, Cart.class);
            return cartsList;


        } else {
            //否则处于登录状态.
            //查询redis购物车.
            List<Cart> redisCartList = cartService.findRedisCartList(userId);
            return redisCartList;

        }

    }


}
