package com.topideal.entity.dto.api;

import java.io.Serializable;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/2 18:55
 * @Description:
 */
public class TocSettleBillResponseDTO implements Serializable {

    private Integer state;

    private Integer code;

    private String msg;

    private TocSettleOrderListDTO orderList;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TocSettleOrderListDTO getOrderList() {
        return orderList;
    }

    public void setOrderList(TocSettleOrderListDTO orderList) {
        this.orderList = orderList;
    }
}
