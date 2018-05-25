package com.bnuz.ztx.translateapp.Entity;

/**
 * Created by ZTX on 2018/5/19.
 */

public class BannerEntity {
    String imageUrl;
    String targetUrl;
    String date;
    Integer imgageType;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getImgageType() {
        return imgageType;
    }

    public void setImgageType(Integer imgageType) {
        this.imgageType = imgageType;
    }
}
