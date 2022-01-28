package com.topideal.common.enums;

/**
 * @Description: 业务进销存汇总详情类型
 * @Author: Chen Yiluan
 * @Date: 2019/07/30 16:33
 **/
public enum InventorySummaryDetailStatusEnum {

    CRKMX("001","出入库明细"),
    SYMX("002","损益明细");

    //key
    private String key;
    //value
    private String value;

    InventorySummaryDetailStatusEnum(String  key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
