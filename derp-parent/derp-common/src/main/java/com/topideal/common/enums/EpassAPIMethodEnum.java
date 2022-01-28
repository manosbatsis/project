package com.topideal.common.enums;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;

/**
 * 跨境宝接口地址方法
 * Created by weixiaolei on 2018/7/4.
 */
public enum EpassAPIMethodEnum {

    EPASS_E01_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400001),"epass.wms.stock.in.purchase.add",DERP_LOG_POINT.POINT_14103400001_Label,"ent_inbound_id"),
	EPASS_E02_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400002),"epass.push.wms.stock.in.confirm",DERP_LOG_POINT.POINT_14103400002_Label,"asn_no"),
	EPASS_E03_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400003),"epass.orders.b2b.add",DERP_LOG_POINT.POINT_14103400003_Label,"order_id"),
	EPASS_E04_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400004),"epass.push.wms.CustTransferCode.Instruct",DERP_LOG_POINT.POINT_14103400004_Label,"order_id"),
	EPASS_E05_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400005),"epass.push.wms.Check.Instruct",DERP_LOG_POINT.POINT_14103400005_Label,"order_id"),
	EPASS_E06_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400006),"epass.push.wms.Check.Comfirm",DERP_LOG_POINT.POINT_14103400006_Label,"order_id"),
	EPASS_E08_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400007),"epass.push.pms.outbound.stock.notify",DERP_LOG_POINT.POINT_14103400007_Label,"order_id"),
	EPASS_E07_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14102400001),"syncGoodsPrd.do",DERP_LOG_POINT.POINT_14102400001_Label,"order_id"),
	//业务模块
    EPASS_E09_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400008),"entryorder.create",DERP_LOG_POINT.POINT_14103400008_Label,"order_id"),
	EPASS_E010_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400009),"deliveryorder.create",DERP_LOG_POINT.POINT_14103400009_Label,"order_id"),
	//业务模块-推送卓普信
	SAPIENCE_E001_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400010),"wms.add.goods.push",DERP_LOG_POINT.POINT_14103400010_Label,"order_id"),
	SAPIENCE_E008_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400011),"wms.financing.query.push",DERP_LOG_POINT.POINT_14103400011_Label,""),
	SAPIENCE_E009_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400012),"wms.financing.apply.push",DERP_LOG_POINT.POINT_14103400012_Label,""),
	SAPIENCE_E010_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400013),"wms.financing.attachment.download",DERP_LOG_POINT.POINT_14103400013_Label,""),
	SAPIENCE_E011_METHOD(Long.valueOf(DERP_LOG_POINT.POINT_14103400014),"wms.financing.status.push",DERP_LOG_POINT.POINT_14103400014_Label,"");

    //接口编码
    private Long point;
    //接口方法
    private String method;
    //接口描述
    private String apiName;
    //关键字名称
    private String keyWordName;

    private EpassAPIMethodEnum(Long point,String method,String apiName,String keyWordName){
        this.point=point;
        this.method=method;
        this.apiName=apiName;
        this.keyWordName=keyWordName;
    }

    public static EpassAPIMethodEnum getEnumByKey(String key) {
        for (EpassAPIMethodEnum epassAPIMethodEnum : EpassAPIMethodEnum.values()) {
            if (StringUtils.equals(key, epassAPIMethodEnum.getMethod())) {
                return epassAPIMethodEnum;
            }
        }
        return null;
    }
    public String getMethod() {
        return method;
    }

    public String getApiName() {
        return apiName;
    }

    public Long getPoint() {
        return point;
    }

    public String getKeyWordName() {
        return keyWordName;
    }

}
