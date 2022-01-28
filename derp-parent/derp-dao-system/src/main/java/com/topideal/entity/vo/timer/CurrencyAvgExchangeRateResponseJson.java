package com.topideal.entity.vo.timer;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 获取平均汇率接口响应报文实体
 * @Date: 2020/04/27 14:23
 **/
public class CurrencyAvgExchangeRateResponseJson implements Serializable {

    //接收状态：1:成功；2:失败 必填
    private Integer status;

    //如失败，则返回错误消息
    private String notes;

    //汇率明细
    private List<CurrencyAvgExchangeRateResDetailJson> averageRateList;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<CurrencyAvgExchangeRateResDetailJson> getAverageRateList() {
        return averageRateList;
    }

    public void setAverageRateList(List<CurrencyAvgExchangeRateResDetailJson> averageRateList) {
        this.averageRateList = averageRateList;
    }
}
