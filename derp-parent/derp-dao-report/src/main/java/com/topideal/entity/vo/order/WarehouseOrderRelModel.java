package com.topideal.entity.vo.order;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;


/**
 * 采购入库关联采购订单表
 * @author zhanghx
 */
public class WarehouseOrderRelModel extends PageModel implements Serializable {

	// 创建人
	private Long creater;
	// id
	private Long id;
	// 采购入库单ID
	private Long warehouseId;
	// 采购订单ID
	private Long purchaseOrderId;
	// 创建时间
	private Timestamp createDate;
	//修改时间
	private Timestamp modifyDate;

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* warehouseId get 方法 */
	public Long getWarehouseId() {
		return warehouseId;
	}

	/* warehouseId set 方法 */
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	/* purchaseOrderId get 方法 */
	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}

	/* purchaseOrderId set 方法 */
	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

}
