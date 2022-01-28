package com.topideal.api.op.v2_2;


/**
 * 理货确认请求实体类
 * @author 杨创
 *2018/4/3
 */
public class TallyConfirmRequest {
    //预申报单号
    private String entInboundId;
    //合同号
    private String bargainNo;
    //指令值  1：确认；2：驳回
    private Integer instructions;
    //确认时间
    private String date;
    //授权码
    private String authorizationCode;
    //是否推送OP
    private String fpostop;
    

    public String getEntInboundId() {
        return entInboundId;
    }

    public void setEntInboundId(String entInboundId) {
        this.entInboundId = entInboundId;
    }

    public String getBargainNo() {
        return bargainNo;
    }

    public void setBargainNo(String bargainNo) {
        this.bargainNo = "";
    }

    public Integer getInstructions() {
        return instructions;
    }

    public void setInstructions(Integer instructions) {
        this.instructions = instructions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

	public String getFpostop() {
		return fpostop;
	}

	public void setFpostop(String fpostop) {
		this.fpostop = fpostop;
	}
}