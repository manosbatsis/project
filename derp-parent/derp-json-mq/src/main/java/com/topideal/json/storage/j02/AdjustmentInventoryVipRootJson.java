package com.topideal.json.storage.j02;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: 唯品红冲库存调整ROOT实体
 * @Author: Chen Yiluan
 * @Date: 2019/07/16 16:54
 **/
public class AdjustmentInventoryVipRootJson implements Serializable {

    private String code; //库存调整单号

    private String merchantId; // 商家ID

    private String merchantName;// 商家名称

    private String depotId;// 仓库ID

    private String depotName;// 仓库名称

    private String model;// 业务类别 1-销毁 2-月结损益 3-其他出入 4-唯品红冲  5-国检抽样 6-唯品报废  7-唯品盘点 必填

    private String sourceCode;// 来源单据号

    private String poNo; //po号

    private String  sourceTime;//单据所属日期

    private String months;// 归属月份

    private List<AdjustmentInventoryVipGoodsListJson> goodsList; // 库存调整详情
    
	// po单时间
	private String poDate;
	
	// 币种
	private String currency;
	//备注
	private String remark;
	
	
	

    public String getPoDate() {
		return poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(String sourceTime) {
        this.sourceTime = sourceTime;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public List<AdjustmentInventoryVipGoodsListJson> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<AdjustmentInventoryVipGoodsListJson> goodsList) {
        this.goodsList = goodsList;
    }

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    
}
