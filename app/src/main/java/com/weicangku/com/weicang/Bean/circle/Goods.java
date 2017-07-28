package com.weicangku.com.weicang.Bean.circle;

import com.weicangku.com.weicang.Bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Goods extends BmobObject {
    //商品名称
    private String goodsName;
    //商品类别id
    private String goodsTypeId;
    //商品的图片
    private int goodsStock;

    public int getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsStock(Integer goodsStock) {
        this.goodsStock = goodsStock;
    }

    private PhontoFiles good_photo_file;
    //商品发布人
    private User user;
    //商品的价格
    private double goodsPrice;
    //商品的发货地址
    private String goodsAddress;
    //收藏商品的人
    private List<String> loveUserIds;

    public String getGoodsContent() {
        return goodsContent;
    }

    public void setGoodsContent(String goodsContent) {
        this.goodsContent = goodsContent;
    }

    private String goodsContent;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }



    public PhontoFiles getGood_photo_file() {
        return good_photo_file;
    }

    public void setGood_photo_file(PhontoFiles good_photo_file) {
        this.good_photo_file = good_photo_file;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsAddress() {
        return goodsAddress;
    }

    public void setGoodsAddress(String goodsAddress) {
        this.goodsAddress = goodsAddress;
    }

    public List<String> getLoveUserIds() {
        if (loveUserIds==null){
            loveUserIds = new ArrayList<>();
        }
        return loveUserIds;
    }

    public void setLoveUserIds(List<String> loveUserIds) {
        this.loveUserIds = loveUserIds;
    }


}

