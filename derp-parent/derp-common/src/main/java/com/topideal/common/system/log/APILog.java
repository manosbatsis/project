package com.topideal.common.system.log;

/**
 * API接口通讯日志
 * Created by weixiaolei on 2018/8/6.
 */
public class APILog {

    //接口名称
    private String model;
    //请求地址
    private String url;
    //接口编号
    private Long point;
    //业务单号(关键字)
    private String keyword;
    //关键字段名
    private String keywordName;
    //
    private String requestMethod;
    //接收日志
    private long receiveData;
    //接口说明
    private String desc;
    //操作状态（1 成功  0 失败）
    private Integer state;
    //异常原因
    private String expMsg;
    //经分销给外部系统制定编码
    private String derpCode;
    //商家名称 非必填
    private String merchantName;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public long getReceiveData() {
        return receiveData;
    }

    public void setReceiveData(long receiveData) {
        this.receiveData = receiveData;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getExpMsg() {
        return expMsg;
    }

    public void setExpMsg(String expMsg) {
        this.expMsg = expMsg;
    }

    public String getDerpCode() {
        return derpCode;
    }

    public void setDerpCode(String derpCode) {
        this.derpCode = derpCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
