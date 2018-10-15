package com.bnuz.ztx.translateapp.Entity;

/**
 * Created by ZTX on 2018/9/13.
 */

public class ChatMessage {
    int type;//消息类型
    public static final int RECEIVED = 0;//一条收到的消息
    public static final int SENT = 1;//一条发出的消息
    String content;//消息内容

    public ChatMessage(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public ChatMessage(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
