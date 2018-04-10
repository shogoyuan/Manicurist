package com.qiang.manicurists.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：商品实体类
 * 创建人：Qiang
 * 创建时间：2016/6/30 9:55
 * 修改备注：
 */
public class Goods implements Serializable{
    private static final long serialVersionUID = -1123581321345589L;
    //商品名称
    private String goodsName;
    //商品名称
    private String goodsurl;
    //商品价格
    private String goodsPrice;
    //商品折扣前价格
    private String goodsDiscount;
    //多少人喜欢
    private String goodsLike;
    //产品内容
    private String goodsContent;
    //产品需时
    private String goodsTime;
    //产品推荐
    private ArrayList<String> goodsRecommend;
    //预约时间
    private ArrayList<ArrayList<Integer>> goodsBooking;
    //服务范围
    private String goodsServiceConfines;
    //评价
    private ArrayList<Rated> goodsRated;
    //手艺人id
    private int goodsCraftsmenId;
    //手艺人
    private Craftsmen goodsCraftsmen;

    public void setGoodsName(String GoodsName) {
        this.goodsName = GoodsName;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsUrl(String GoodsUrl) {
        this.goodsurl = GoodsUrl;
    }

    public String getGoodsurl() {
        return this.goodsurl;
    }

    public void setGoodsPrice(String GoodsPrice) {
        this.goodsPrice = GoodsPrice;
    }

    public String getGoodsPrice() {
        return this.goodsPrice;
    }

    public void setGoodsDiscount(String GoodsDiscount) {
        this.goodsDiscount = GoodsDiscount;
    }

    public String getGoodsDiscount() {
        return this.goodsDiscount;
    }

    public void setGoodsLike(String GoodsLike) {
        this.goodsLike = GoodsLike;
    }

    public String getGoodsLike() {
        return this.goodsLike;
    }

    public void setGoodsContent(String GoodsContent) {
        this.goodsContent = GoodsContent;
    }

    public String getGoodsContent() {
        return this.goodsContent;
    }

    public void setGoodsTime(String GoodsTime) {
        this.goodsTime = GoodsTime;
    }

    public String getGoodsTime() {
        return this.goodsTime;
    }

    public void setGoodsRecommend(ArrayList<String> GoodsRecommend) {
        this.goodsRecommend = GoodsRecommend;
    }

    public ArrayList<String> getGoodsRecommend() {
        return this.goodsRecommend;
    }

    public void setGoodsBooking(ArrayList<ArrayList<Integer>> GoodsBooking) {
        this.goodsBooking = GoodsBooking;
    }

    public ArrayList<ArrayList<Integer>> getGoodsBooking() {
        return this.goodsBooking;
    }

    public void setGoodsServiceConfines(String GoodsServiceConfines) {
        this.goodsServiceConfines = GoodsServiceConfines;
    }

    public String getGoodsServiceConfines() {
        return this.goodsServiceConfines;
    }

    public void setGoodsRated(ArrayList<Rated> GoodsRated) {
        this.goodsRated = GoodsRated;
    }

    public ArrayList<Rated> getGoodsRated() {
        return this.goodsRated;
    }

    public void setGoodsCraftsmenId(int goodsCraftsmenId) {
        this.goodsCraftsmenId = goodsCraftsmenId;
    }

    public int getGoodsCraftsmenId() {
        return this.goodsCraftsmenId;
    }

    public void setGoodsCraftsmen(Craftsmen GoodsCraftsmen) {
        this.goodsCraftsmen = GoodsCraftsmen;
    }

    public Craftsmen getGoodsCraftsmen() {
        return this.goodsCraftsmen;
    }

}
