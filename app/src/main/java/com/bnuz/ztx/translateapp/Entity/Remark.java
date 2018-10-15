package com.bnuz.ztx.translateapp.Entity;

import java.io.Serializable;

/**
 * Created by ZTX on 2018/8/27.
 * 商品评论类
 */

public class Remark implements Serializable{
    String remarkName;//用户名
    String remarkBody;//评论主体
    String remarkTime;//评论时间

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getRemarkBody() {
        return remarkBody;
    }

    public void setRemarkBody(String remarkBody) {
        this.remarkBody = remarkBody;
    }

    public String getRemarkTime() {
        return remarkTime;
    }

    public void setRemarkTime(String remarkTime) {
        this.remarkTime = remarkTime;
    }
}
