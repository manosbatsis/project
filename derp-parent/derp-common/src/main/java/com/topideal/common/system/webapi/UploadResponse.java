package com.topideal.common.system.webapi;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**文件上传返回DTO*/
@ApiModel
public class UploadResponse {

    @ApiModelProperty(value = "成功数量",position = 0)
    private int success;
    @ApiModelProperty(value = "失败数量",position = 0)
    private int failure;
    @ApiModelProperty(value = "失败消息",position = 0)
    private List<ImportMessage> errorList;
    @ApiModelProperty(value = "返回信息",position = 0)
    private List data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public List<ImportMessage> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ImportMessage> errorList) {
        this.errorList = errorList;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
