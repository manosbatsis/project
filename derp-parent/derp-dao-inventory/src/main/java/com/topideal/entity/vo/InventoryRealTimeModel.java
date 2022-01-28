package com.topideal.entity.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import com.topideal.common.system.ibatis.PageModel;

/**
 * 库存信息表
 * 
 * @author lianchenxing
 *
 */
public class InventoryRealTimeModel extends PageModel implements Serializable {

	// 商家名称
	private String merchantName;

	// 仓库名称
	private String depotName;

	// 商品货号
	private String goodsNo;

	// 商品名称
	private String goodsName;
	// 生产日期
	private String productionDate;
	// 失效日期
	private String  overdueDate;
	// 批次号
	private String batchNo;

	// 库存数量
	private Integer qty;
	
	// 库存可用数量
	private Integer realStockNum;

	// 库存锁定数量
	private Integer lockStockNum;

	// 库存冻结数量
	private Integer realFrozenStockNum;

	// 临保天数
	private Integer overDays;//临保天数
	
	// 库存类型
	private String stockType;

	// 库存状态
	private String storesType;

	// 商品id
	private Long goodsId;
	// 仓库id
	private Long depotId;
	
	//理货单位
	private String uom; 
	// 托盘号
	private String lpn; 


	/* goodsNo get 方法 */
	public String getGoodsNo() {
		return goodsNo;
	}

	/* goodsNo set 方法 */
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	/* depotName get 方法 */
	public String getDepotName() {
		return depotName;
	}

	/* depotName set 方法 */
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	/* batchNo get 方法 */
	public String getBatchNo() {
		return batchNo;
	}

	/* batchNo set 方法 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/* goodsId get 方法 */
	public Long getGoodsId() {
		return goodsId;
	}

	/* goodsId set 方法 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/* depotId get 方法 */
	public Long getDepotId() {
		return depotId;
	}

	/* depotId set 方法 */
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	/* goodsName get 方法 */
	public String getGoodsName() {
		return goodsName;
	}

	/* goodsName set 方法 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Integer getRealStockNum() {
		return realStockNum;
	}

	public void setRealStockNum(Integer realStockNum) {
		this.realStockNum = realStockNum;
	}

	public Integer getLockStockNum() {
		return lockStockNum;
	}

	public void setLockStockNum(Integer lockStockNum) {
		this.lockStockNum = lockStockNum;
	}

	public Integer getRealFrozenStockNum() {
		return realFrozenStockNum;
	}

	public void setRealFrozenStockNum(Integer realFrozenStockNum) {
		this.realFrozenStockNum = realFrozenStockNum;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getStoresType() {
		return storesType;
	}

	public void setStoresType(String storesType) {
		this.storesType = storesType;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getLpn() {
		return lpn;
	}

	public void setLpn(String lpn) {
		this.lpn = lpn;
	}

	public Integer getOverDays() {
		return overDays;
	}

	public void setOverDays(Integer overDays) {
		this.overDays = overDays;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}

	
    
    
	
	
 

}
