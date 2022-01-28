package com.topideal.json.pushapi.op.v2_1;

import java.util.List;

/**
 * 采购入库实体类
 * @author yangchuang
 *2018/4/3
 */
public class PurchaseOrderRoot {
    //卓志内部编号（卓志提供）  商家信息-->卓志编码  FZZBM
    private String customerId;
    //企业入仓编号（采购编号，唯一）   单据编号FBILLNO
    private String entInboundId;
    //电商平台编号（卓志提供）   商家信息-->卓志编码
    private String cbepcomcode;
    //合同号（可以暂时对应采购编号） FHTDH
    private String  contractNo;
    //订单状态  1：正常；2：取消
    private int status=1;
    //仓库ID，由WMS生成    可空
    private String warehouseId;
    //资金方   可空   用于代采业务
    private String foundpCode;
    //商品列表
    private List<PurchaseGoodsItem> purchaseGoodList;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getEntInboundId() {
		return entInboundId;
	}
	public void setEntInboundId(String entInboundId) {
		this.entInboundId = entInboundId;
	}
	public String getCbepcomcode() {
		return cbepcomcode;
	}
	public void setCbepcomcode(String cbepcomcode) {
		this.cbepcomcode = cbepcomcode;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getFoundpCode() {
		return foundpCode;
	}
	public void setFoundpCode(String foundpCode) {
		this.foundpCode = foundpCode;
	}
	public List<PurchaseGoodsItem> getPurchaseGoodList() {
		return purchaseGoodList;
	}
	public void setPurchaseGoodList(List<PurchaseGoodsItem> purchaseGoodList) {
		this.purchaseGoodList = purchaseGoodList;
	}

   
	
}
