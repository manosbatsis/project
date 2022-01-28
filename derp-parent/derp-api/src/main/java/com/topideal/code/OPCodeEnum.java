package com.topideal.code;

/**
 * 主数据同异常码表
 * Created by weixiaolei on 2018/5/22.
 */
public enum OPCodeEnum {

    SUCCESS(0,"成功"), 
    ERROR_100(100,"服务已关闭"), 
    ERROR_101(101,"超出最大等待数"),
    ERROR_102(102,"等待超时"),
    ERROR_103(103,"非法接入"),
    ERROR_200(200,"数据格式错误"),
    ERROR_201(201,"请求参数错误"),
    ERROR_202(202,"同步数据与现有数据冲突"),
    ERROR_203(203,"更新数据找不到主键"),
    ERROR_300(300,"处理失败"),
    ERROR_301(301,"版本号错误"),
    ERROR_302(302,"请求超时"),
    ERROR_901(901,"数据库异常请重试"),
    ERROR_902(902,"系统异常请重试");
    //码值
    private int code;
    //描述
    private String message;

    private OPCodeEnum(int code,String message){
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
