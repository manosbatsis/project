package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 仓库商家关联表 model
 * 
 * @author lian_
 *
 */
public class DepotMerchantRelModel extends PageModel implements Serializable {

	// 仓库ID
	private Long depotId;
	// 主键ID
	private Long id;
	// 商家ID
    private Long merchantId;
	/**
	 * 进出接口指令 1-是 0 - 否
	 */
	private String isInOutInstruction;
	/**
	 * 统计存货跌价 1-是 0-否
	 */
	private String isInvertoryFallingPrice;
	/**
	 * 选品限制 1-仅备案商品 2-仅外仓统一码 3-无限制
	 */
	private String productRestriction;
	/**
	 * 已入定出 1-是 0-否
	 */
	private String isInDependOut;
	/**
	 * 已出定入 1-是 0-否
	 */
	private String isOutDependIn;
    //修改时间
    private Timestamp modifyDate;
    //创建时间
    private Timestamp createDate;
    //合同编码
    private String contractCode;

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* depotId get 方法 */
	public Long getDepotId() {
		return depotId;
	}

	/* depotId set 方法 */
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getIsInOutInstruction() {
		return isInOutInstruction;
	}

	public void setIsInOutInstruction(String isInOutInstruction) {
		this.isInOutInstruction = isInOutInstruction;
	}

	public String getIsInvertoryFallingPrice() {
		return isInvertoryFallingPrice;
	}

	public void setIsInvertoryFallingPrice(String isInvertoryFallingPrice) {
		this.isInvertoryFallingPrice = isInvertoryFallingPrice;
	}

	public String getProductRestriction() {
		return productRestriction;
	}

	public void setProductRestriction(String productRestriction) {
		this.productRestriction = productRestriction;
	}

	public String getIsInDependOut() {
		return isInDependOut;
	}

	public void setIsInDependOut(String isInDependOut) {
		this.isInDependOut = isInDependOut;
	}

	public String getIsOutDependIn() {
		return isOutDependIn;
	}

	public void setIsOutDependIn(String isOutDependIn) {
		this.isOutDependIn = isOutDependIn;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
}
