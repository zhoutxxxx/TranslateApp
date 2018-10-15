package com.bnuz.ztx.translateapp.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZTX on 2018/8/11.
 * 商品实体类
 */

public class Promotion implements Serializable{
    String imgUrl;//主图url
    String title;//商品标题
    String subTitle;//商品子标题
    String expressPrice;//快递价格
    String expressSum;//已售数量
    String promotion;//促销
    String fare;//返利
    String whiteBar;//白条
    String select;//已选商品
    String weight;//商品重量
    String RemarkSum;//评论数
    List<Remark> remarks;//Remark类的List
    List<String> imageInformation;//商品信息的详细图片List
    String act;//商品活动
    String money;//商品价格
    String good;//商品好评率

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getExpressPrice() {
        return expressPrice;
    }

    public void setExpressPrice(String expressPrice) {
        this.expressPrice = expressPrice;
    }

    public String getExpressSum() {
        return expressSum;
    }

    public void setExpressSum(String expressSum) {
        this.expressSum = expressSum;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getWhiteBar() {
        return whiteBar;
    }

    public void setWhiteBar(String whiteBar) {
        this.whiteBar = whiteBar;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRemarkSum() {
        return RemarkSum;
    }

    public void setRemarkSum(String remarkSum) {
        RemarkSum = remarkSum;
    }

    public List<Remark> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<Remark> remarks) {
        this.remarks = remarks;
    }

    public List<String> getImageInformation() {
        return imageInformation;
    }

    public void setImageInformation(List<String> imageInformation) {
        this.imageInformation = imageInformation;
    }

}
