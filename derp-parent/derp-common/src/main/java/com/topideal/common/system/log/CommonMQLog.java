package com.topideal.common.system.log;


/**
 * @Author: Wilson Lau
 * @Date: 2022/1/7 15:19
 * @Description:
 */
public class CommonMQLog {
    //接口名称
    private String model;
    //接口编号
    private Long point;
    //业务单号(关键字)
    private String keyword;
    //关键字段名
    private String keywordName;
    //方法
    private String method;
    //调用时间
    private long startDate;
    //结束时间
    private long endDate;
    //接口说明
    private String desc;
    //操作状态（1 成功  0 失败）
    private Integer state;
    //异常原因
    private String expMsg;
    //主题
    private String topics;
    //标签
    private String tags;

    //商家名称 非必填
    private String merchantName;

    private Object requestMessage;

    private Object returnMessage;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Object getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(Object requestMessage) {
        this.requestMessage = requestMessage;
    }

    public Object getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(Object returnMessage) {
        this.returnMessage = returnMessage;
    }
}
