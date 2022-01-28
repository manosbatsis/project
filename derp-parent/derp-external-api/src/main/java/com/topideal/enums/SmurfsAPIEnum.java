package com.topideal.enums;

import com.topideal.common.tools.ApolloUtil;

/**
 * 蓝精灵接口编码
 * Created by weixiaolei on 2018/9/19.
 */
public enum SmurfsAPIEnum {

    SMURFS_NOTE("001","1.0","发送短信"),
    SMURFS_EMAIL("002","1.0","发送邮件"),
    SMURFS_EMAIL_ATTACHMENT("002","2.0","发送邮件（带附件）"),
    SMURFS_UPLOAD_FILE("003", ApolloUtil.smurfsUploadVersion,"上传图片"),
    SMURFS_UNIQUE_ID("004","1.0","生成唯一ID"),
    SMURFS_DERPLOG_REPORT("520","1.0","经分销日志"),
    SMURFS_ORDER_COLLECTION_REPORT("101","1.0","订单采集接口"),
	SMURFS_ORDER_COLLECTION_REPORT2("102","1.0","订单采集接口2.0"),
    SMURFS_WEIXN("010","1.0","企微机器人通知"),
    SMURFS_GET_TOC_SETTLEMENT("605","1.0","获取Toc结算单接口");
	

    private SmurfsAPIEnum(String apiCode,String v,String name){
        this.apiCode=apiCode;
        this.name=name;
        this.v=v;
    }
    //编码
    private String apiCode;
    //接口描述
    private String name;
    //版本号
    private String v;

    public String getApiCode() {
        return apiCode;
    }

    public String getName() {
        return name;
    }

    public String getV() {
        return v;
    }
}
