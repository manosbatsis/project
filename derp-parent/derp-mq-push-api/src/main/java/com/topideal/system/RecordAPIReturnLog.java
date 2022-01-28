package com.topideal.system;

/**
 *  记录推送API结果记录日志
 * Created by weixiaolei on 2018/7/17.
 */
public class RecordAPIReturnLog {
    //埋点
    private String point;
    //消费提示
    private String message;
    //执行方法名
    private String method;
    //模块说明
    private String model;
    //接口描述
    private String desc;
    //记录错误信息
    private String exPMsg;
    //返回报文
    private String returnJson;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReturnJson() {
        return returnJson;
    }

    public void setReturnJson(String returnJson) {
        this.returnJson = returnJson;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getExPMsg() {
        return exPMsg;
    }

    public void setExPMsg(String exPMsg) {
        this.exPMsg = exPMsg;
    }
}
