package com.topideal.entity.dto;

import java.util.List;
import java.util.Map;

public class TakesStockFrom {
    
	private String id;//盘点申请id
	private String depotId;//
	 //服务类型
    private String serverType;
    //业务场景
    private String model;
    //备注
    private String remark;
     //状态
    private String status;

    
    private List<Map<String, Object>> goodsList;//商品ids

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepotId() {
		return depotId;
	}

	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Map<String, Object>> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<Map<String, Object>> goodsList) {
		this.goodsList = goodsList;
	}

}
