package com.topideal.common.system.webapi;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 响应数据模板
 *
 */
@ApiModel(value = "响应信息")
public class ResponseBean<T> {

    @ApiModelProperty(value = "响应吗",position = 1,required = true)
    private String code;
    @ApiModelProperty(value = "响应消息",position = 2,required = true)
    private String message;
    @ApiModelProperty(value = "异常消息",position = 3)
    private String expMessage="";

    @ApiModelProperty(value = "响应体",position = 4)
    private T data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExpMessage() {
        return expMessage;
    }

    public void setExpMessage(String expMessage) {
        this.expMessage = expMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
