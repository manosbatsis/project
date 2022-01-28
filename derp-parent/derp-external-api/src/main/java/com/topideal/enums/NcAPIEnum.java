package com.topideal.enums;

/**
 * NC 接口枚举
 * @author gy
 *
 */
public enum NcAPIEnum {

    NcApi_07("07", "结算账单接收接口", "/external/settlement/confirmbill"),
    NcApi_08("08", "结算账单红冲/作废接口", "/external/settlement/concancel"),
    NcApi_10("10", "发票更新接口", "/external/settlement/invoice"),
    NcApi_11("11", "账单状态查询接口", "/external/settlement/state"),
    NcApi_12("12", "核销记录查询接口", "/external/settlement/verify"),
    NcApi_14("14", "凭证查询接口", "/external/settlement/voucher");

    private NcAPIEnum(String apiCode, String name, String uri) {
        this.apiCode = apiCode;
        this.uri = uri;
        this.name = name;
    }

    private String apiCode;
	
	private String uri ;
	
	private String name ;

	public String getApiCode() {
		return apiCode;
	}

	public String getUri() {
		return uri;
	}

	public String getName() {
		return name;
	}
	
	
}
