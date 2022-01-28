package com.topideal.common.system.web;

import java.util.Map;

/**
 * 视图层  响应数据类型
 * Created by weixiaolei on 2018/4/13.
 */
public class ViewResponseBean {

    //响应状态
    private int state;
    //描述文本
    private String message;
    //异常信息
    private String expMessage;
    //数据结果集
    private Object data;
    //额外数据储存
    private Map dataMap;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getExpMessage() {
        return expMessage;
    }

    public void setExpMessage(String expMessage) {
        this.expMessage = expMessage;
    }

    public Map getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map dataMap) {
        this.dataMap = dataMap;
    }
}
