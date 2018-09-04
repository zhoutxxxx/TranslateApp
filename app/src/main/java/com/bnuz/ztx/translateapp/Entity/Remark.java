package com.bnuz.ztx.translateapp.Entity;

import java.io.Serializable;

/**
 * Created by ZTX on 2018/8/27.
 */

public class Remark implements Serializable{
    String remarkName;
    String remarkBody;
    String remarkTime;

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
