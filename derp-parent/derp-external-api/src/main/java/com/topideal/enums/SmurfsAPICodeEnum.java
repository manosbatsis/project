package com.topideal.enums;


/**
 * 蓝精灵 模块编码
 * Created by weixiaolei on 2018/9/19.
 */
public enum SmurfsAPICodeEnum {

    EMAIL_M010("M010", "系统运维数据统计表"),
    EMAIL_M014("M014", "经分销日志监控提醒"),
    EMAIL_M011("M011", "消费失败预警"),
    EMAIL_M013("M013", "智能重推日志多次失败提醒"),
    EMAIL_M017("M017", "经分销盘点结果"),
    EMAIL_M018("M018", "经分销收到发票未付款的采购订单"),
    EMAIL_M019("M019", "经分销同步数据功能"),
    EMAIL_M020("M020", "经分销标准成本单价波动预警"),
    EMAIL_M021("M021", "经分销标准成本单价波动预警附件"),
    EMAIL_M024("M024", "经分销异常监控预警邮件"),
    EMAIL_W035("W035", "经分销内部交易金额数量差异邮件"),
    EMAIL_W036("W036", "经分销调拨单出入仓数据异常预警"),
    EMAIL_W037("W037", "经分销电商订单时间数据异常预警"),
    EMAIL_W038("W038", "经分销生成Toc结算单异常预警"),
    /*EMAIL_M022("M022", "经分销应收账单审核邮件"),
    EMAIL_M023("M023", "经分销应收账单核销邮件"),
    EMAIL_M025("M025", "经分销应收账单作废邮件"),
    EMAIL_M026("M026", "经分销应收账单盖章邮件"),
    EMAIL_M027("M027", "经分销应收账单审核完成邮件"),
    EMAIL_M028("M028", "经分销应收账单作废完成邮件"),
    EMAIL_M029("M029", "经分销应收账单开票邮件"),*/
    UPLOAD_00002("00002", "上传供应商资质图片"),
    UPLOAD_00003("00003", "上传商品图片"),
    UPLOAD_FILE("T002", "上传附件"),
    UNIQUE_ID_ERP_GOODS("ID001", "ERP"),
    UNIQUE_ID_ERP_CUSTOMER("ID002", "ERP"),
    UNIQUE_ID_CGRK("ID003", "CGRK"),
    UNIQUE_ID_DSDD("ID004", "DSDD"),
    UNIQUE_ID_XSTR("ID005", "XSTR"),
    UNIQUE_ID_XSTC("ID006", "XSTC"),
    UNIQUE_ID_DBCK("ID007", "DBCK"),
    UNIQUE_ID_DBRK("ID008", "DBRK"),
    UNIQUE_ID_WPHC("ID009", "WPHC"),
    UNIQUE_ID_LXTZO("ID0010", "LXTZO"),
    UNIQUE_ID_PDJG("ID0011", "PDJG"),
    UNIQUE_ID_KCTZ("ID0012", "KCTZ"),
    UNIQUE_ID_ATT("ID0013", "ATT"),
    UNIQUE_ID_CGO("ID0014", "CGO"),
    UNIQUE_ID_XSTO("ID0015", "XSTO"),
    UNIQUE_ID_DBO("ID0016", "DBO"),
    UNIQUE_ID_AI("ID0017", "AI"),
    UNIQUE_ID_PDO("ID0018", "PDO"),
    UNIQUE_ID_XSCO("ID0019", "XSCO"),
    UNIQUE_ID_XSCK("ID0020", "XSCK"),
    UNIQUE_ID_ZDY("ID0021", "ZDY"),
    UNIQUE_ID_YJSY("ID0022", "YJSY"),
    UNIQUE_ID_XSO("ID0023", "XSO"),
    UNIQUE_ID_SYB("ID0024", "SYB"),
    UNIQUE_ID_CGOD("ID0025", "CGOD"),
    UNIQUE_ID_DXCK("ID0026", "DXCK"),// 美赞臣出库单号
    UNIQUE_ID_DSDDTH("ID0027", "DSDDTH"),// 电商订单退货
    UNIQUE_ID_HDRW("ID0028", "HDRW"),// 核对任务
    UNIQUE_ID_SJRK("ID0029", "SJRK"),// 上架入库单号
    UNIQUE_ID_ZDCK("ID0030", "ZDCK"),// 账单出库单号
    UNIQUE_ID_ZDRK("ID0031", "ZDRK"),// 账单入库单号
    UNIQUE_ID_CGTH("ID0032", "CGTH"),// 采购退货单号
    UNIQUE_ID_CGTC("ID0033", "CGTC"),// 采购退货出库单号
    UNIQUE_ID_ZDJS("ID0034", "ZDJS"),// 账单结算单单号
    UNIQUE_ID_YSD("ID0035", "YSD"),// 预售单单号
    UNIQUE_ID_SYBYK("ID0036", "SYBYK"),// 事业部移库单号
    UNIQUE_ID_YSZD("ID0037", "YSZD"),// 应收账单单号
    UNIQUE_ID_JSXM("ID0038", "JSXM"),// 项目ID
    UNIQUE_ID_JCXMXL("ID0039", "JCXMXL"),//小类ID
    UNIQUE_ID_FPMB("ID0040", "FPMB"),//发票模板编码
    UNIQUE_INVOICE_HNFH("I0001", "HNFH20"),//宝信发票编码前缀
    UNIQUE_INVOICE_QTOP("I0002", "QTOP20"),//健太发票编码前缀
    UNIQUE_INVOICE_TWKL("I0003", "TWKL20"),//卓烨发票编码前缀
    UNIQUE_INVOICE_YSTA("I0004", "YSTA20"),//元森泰发票编码前缀
    UNIQUE_INVOICE_ABHG("I0005", "ABHG20"),//广旺发票编码前缀
    UNIQUE_INVOICE_RYBZ("I0006", "RYBZ20"),//润佰发票编码前缀
    UNIQUE_INVOICE_KWTZD("ID0041", "KWTZD"),//库位调整单号
    UNIQUE_INVOICE_BNO("I0008", "BNO"),//采购链路批次
    UNIQUE_INVOICE_DCRZ("I00081", "DCRZ"),//融资订单号
    UNIQUE_ID_PTFYD("ID0042", "FYD"),//平台费用单
    UNIQUE_ID_SJD("ID0043", "SJD"), //上架单号
    UNIQUE_ID_CGSD("ID0044", "CGSD"),//采购SD
    UNIQUE_ID_PTJS("ID0045", "PTJS"), //toc结算单
    UNIQUE_ID_LLD("ID0046", "LLD"), //领料单
    UNIQUE_ID_LLCK("ID0047", "LLCK"), //领料出库单
    WORK_WEIXN_W001("W001", "企微机器人通知接口"),//蓝精灵推送日志
    UNIQUE_ID_YSKD("ID0048", "YSKD"),// 预收款单单号;
    UNIQUE_ID_XSSD("ID0049", "XSSD"),// 销售sd编码前缀;
    UNIQUE_ID_YFKD("ID0050", "YFKD"),// 预付款单单号;
    UNIQUE_ID_YFZD("ID0051", "YFZD"),// 应付帐单单号;
    UNIQUE_ID_ZGFY("ID0052", "ZGFY"),// 平台暂估费用单号;
    UNIQUE_ID_XSOD("ID0053", "XSOD"),// 销售预申报单号;
    UNIQUE_ID_XSRZ("ID0054", "XSRZ"),// 赊销单;
    UNIQUE_ID_CGFP("ID0055", "CGFP"),// 采购发票;
    UNIQUE_ID_XSTOD("ID0056", "XSTOD"),// 销售退预申报单号;
    UNIQUE_ID_WJRW("ID0057", "WJRW");// 文件任务;

    SmurfsAPICodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    //模板编码
    private final String code;
    //模板名称
    private final String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    // 根据传进来的名称获取编码
    public static String getCodeByName(String name) {
        String code = "";
        for (SmurfsAPICodeEnum codeEnum : SmurfsAPICodeEnum.values()) {
            if (codeEnum.getName().equals(name)) {
                code = codeEnum.getCode();
                break;
            }
        }
        return code;
    }
}
