package com.topideal.common.system.web;

import java.io.Serializable;

/**
 * 提示消息
 * Created by weixiaolei on 2017/11/8.
 */
public enum StateCodeEnum implements Serializable {

    SUCCESS(200,"操作成功!"),
    SUCCESS_501(501,"库存余量与现存量无差异,请点击按计算库存量结转!"),
    ERROR_301(301,"操作失败！"),
    ERROR_302(302,"数据异常！"),
    ERROR_303(303,"输入信息不完整！"),
    ERROR_304(304,"空指针异常！"),
    ERROR_305(305,"未知异常！"),
    ERROR_306(306,"查询结果为空！"),
    ERROR_307(307,"数据异常！"),
    ERROR_308(308,"用户名或密码不正确！"),
    ERROR_309(309,"用户名已存在！"),
    ERROR_310(310,"供应商已经存在！"),
    ERROR_311(311,"该效期已存在该汇率记录！"),
    ERROR_312(312,"开始时间和结束时间必须是同一年"),
    ERROR_313(313,"不能删除已入仓的单"),
    ERROR_801(801,"登陆失效"),
    ERROR_315(315,"商家名已经存在！"),
    ERROR_316(316,"登录用户没有绑定该公司！"),
    ERROR_317(317,"登录用户没有绑定公司！"),
    ERROR_318(318,"该角色为平台管理员角色，至少绑定一个用户！"),
    ERROR_319(319,"数据已经存在"),
    ERROR_320(320,"该NC收支费项或收支费项编码已存在"),
    MESSAGE_10001(10001,"密码不正确！"),
    MESSAGE_10002(10002,"用户已禁用"),
    MESSAGE_10003(10003,"同一个商家下同个对象类型下的对象名称仅能有一条数据"),
    MESSAGE_10004(10004,"同一商家下同个对象类型下的对象别名不予许重复命名"),
    MESSAGE_10005(10005,"同一平台下仅能维护一个店铺名称信息"),
    MESSAGE_10006(10006,"门店名称不允许重复命名"),
    MESSAGE_10007(10007,"同个商家下仅能维护一个仓库信息"),
    MESSAGE_10008(10008,"商品的外仓统一码为是,不能禁用"),
    MESSAGE_10009(10009,"该角色已绑定用户，不能删除"),
    MESSAGE_10010(10010,"发票日期必须大于关账日期/月结日期"),
    MESSAGE_10011(10011,"付款日期必须大于关账日期/月结日期"),
    MESSAGE_10012(10012,"仅能对同客户的应收账单进行同时合并开票"),
    MESSAGE_10013(10013,"合作商家传值重复"),
    MESSAGE_10014(10014,"包含已经审核数据"),
    MESSAGE_10015(10015,"关区不能为空"),
    MESSAGE_10016(10016,"关区不能为重复"),
    MESSAGE_10017(10017,"合作公司不能是内部公司的商家"),
    ESSAGE_10018(10018,"银行账户不能重复"),

    ERROR_4000(9999,"未知异常！");
	

    private StateCodeEnum(int code, String message){
         this.code=code;
         this.message=message;
    }
    //状态码
    private int code;
    //状态标实
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
