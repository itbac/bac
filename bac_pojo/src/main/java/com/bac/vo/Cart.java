package com.bac.vo;

import com.bac.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/*
购物车包装类
 */
public class Cart implements Serializable {
    //商家id 表示此商品属于哪个商家
    private String sellerId;
    //商家名称
    private String sellerName;
    //购物车明细
    private List<TbOrderItem> orderItems;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(sellerId, cart.sellerId) &&
                Objects.equals(sellerName, cart.sellerName) &&
                Objects.equals(orderItems, cart.orderItems);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sellerId, sellerName, orderItems);
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
