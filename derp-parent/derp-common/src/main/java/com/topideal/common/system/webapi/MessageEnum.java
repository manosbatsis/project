package com.topideal.common.system.webapi;

import java.io.Serializable;

/**
 * 提示消息
 *
 */
public enum MessageEnum implements Serializable {

    SUCCESS_10000("10000","操作成功"),
    MESSAGE_10001("10001","用户不存在"),
    MESSAGE_10002("10002","用户已禁用"),
    MESSAGE_10003("10003","密码不正确"),
    MESSAGE_10004("10004","登录用户未绑定公司"),
    MESSAGE_10005("10005","登录用户未绑定本公司"),
    MESSAGE_10006("10006","用户名或密码不正确"),
    MESSAGE_10007("10007","请选择公司登录"),
    MESSAGE_10008("10008","文件不能为空"),
    MESSAGE_10009("10009","数据为空"),
    MESSAGE_10010("10010","公司Id为空"),
    MESSAGE_10011("10011","数据格式不正确"),
    MESSAGE_10012("10012","数据已经存在"),
    MESSAGE_10013("10013","数据已经被使用"),
    MESSAGE_10014("10014","查询结果为空"),
    MESSAGE_10015("10015","数据异常"),
    MESSAGE_10016("10016","该角色已绑定用户，不能删除"),
    MESSAGE_10017("10017","业务异常"),
    MESSAGE_10018("10018", "参数异常"),
    LOGINTIMEOUT_99998("99998","登录超时,请重新登录"),
    ERROR_99999("99999","未知异常");
	

    private MessageEnum(String code, String message){
         this.code=code;
         this.message=message;
    }
    //状态码
    private String code;
    //状态标实
    private String message;

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

}
