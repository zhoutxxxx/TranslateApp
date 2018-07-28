package com.bnuz.ztx.translateapp.Util;

/**
 * Created by ZTX on 2018/7/23.
 */

public class EventMessage {
    private boolean isArrived = false;
    private String message;
    private String topic;

    public EventMessage(boolean isArrived, String topic, String message) {
        this.isArrived = isArrived;
        this.message = message;
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isArrived() {
        return isArrived;
    }

    public void setArrived(boolean arrived) {
        isArrived = arrived;
    }
}
