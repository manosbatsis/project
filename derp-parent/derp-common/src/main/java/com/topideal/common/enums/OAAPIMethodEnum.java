package com.topideal.common.enums;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/7 15:07
 * @Description: OA接口地址方法
 */
public enum OAAPIMethodEnum {

    /************************************ 采购 ********************************************************/
    PURCHASE_OA_METHOD_001(Long.valueOf(DERP_LOG_POINT.POINT_14103400015),"com.topideal.order.service.purchase.impl.callOAAudit",DERP_LOG_POINT.POINT_14103400015_Label,"");

    //接口编码
    private Long point;
    //接口方法
    private String method;
    //接口描述
    private String apiName;
    //关键字名称
    private String keyWordName;

    private OAAPIMethodEnum(Long point, String method, String apiName, String keyWordName){
        this.point=point;
        this.method=method;
        this.apiName=apiName;
        this.keyWordName=keyWordName;
    }

    public static OAAPIMethodEnum getEnumByKey(String key) {
        for (OAAPIMethodEnum epassAPIMethodEnum : OAAPIMethodEnum.values()) {
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
