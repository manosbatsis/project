package com.topideal.entity.vo.storage;

import java.io.Serializable;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 盘点结果表
 * @author zhanghx
 */
public class TakesStockResultsModel extends PageModel implements Serializable {

	// 盘点仓库名称
	private String depotName;
	// 盘点单号
	private String code;
	// 盘点指令id
	private Long takesStockId;
	// 盘点仓库id
	private Long depotId;
	// 盘点指令单号
	private String takesStockCode;
	// 备注
	private String remark;
	// 确认时间
	private Timestamp confirmTime;
	// 驳回原因
	private String dismissRemark;
	// 提交人id
	private Long confirmUserId;
	// 提交人名称
	private String confirmUsername;
	// 接收时间
	private Timestamp createDate;
	// 服务类型
	private String serverType;
	// 业务场景
	private String model;
	// id
	private Long id;
	// 状态 024-盘点结果待确认  025-盘点结果已确认 021-已作废 023-待入库 022-入库中 010-已入库 006-已删除
	private String status;
	// 商家id
	private Long merchantId;
	// 商家名称
	private String merchantName;
	// 归属时间
	private Timestamp sourceTime;
	// 来源状态 1.op盘点结果 2 ofc盘点结果
	private String sourceType;
	// 来源单据号
	private String sourceCode;
    //修改时间
    private Timestamp modifyDate;

	//po单号
	private String poNo;

	//po时间
	private Timestamp poDate;

	//币种
	private String currency;

	//入库确认人id
	private Long inConfirmUserId;
	//入库确认人名称
	private String inConfirmUsername;
	//入库确认人id
	private Timestamp inConfirmTime;

	// 创建人id
	private Long createUserId;

	// 创建人名称
	private String createUsername;
   
   public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Timestamp getSourceTime() {
		return sourceTime;
	}

	public void setSourceTime(Timestamp sourceTime) {
		this.sourceTime = sourceTime;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	/* depotName get 方法 */
	public String getDepotName() {
		return depotName;
	}

	/* depotName set 方法 */
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* takesStockId get 方法 */
	public Long getTakesStockId() {
		return takesStockId;
	}

	/* takesStockId set 方法 */
	public void setTakesStockId(Long takesStockId) {
		this.takesStockId = takesStockId;
	}

	/* depotId get 方法 */
	public Long getDepotId() {
		return depotId;
	}

	/* depotId set 方法 */
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	/* takesStockCode get 方法 */
	public String getTakesStockCode() {
		return takesStockCode;
	}

	/* takesStockCode set 方法 */
	public void setTakesStockCode(String takesStockCode) {
		this.takesStockCode = takesStockCode;
	}

	/* remark get 方法 */
	public String getRemark() {
		return remark;
	}

	/* remark set 方法 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/* confirmTime get 方法 */
	public Timestamp getConfirmTime() {
		return confirmTime;
	}

	/* confirmTime set 方法 */
	public void setConfirmTime(Timestamp confirmTime) {
		this.confirmTime = confirmTime;
	}

	/* dismissRemark get 方法 */
	public String getDismissRemark() {
		return dismissRemark;
	}

	/* dismissRemark set 方法 */
	public void setDismissRemark(String dismissRemark) {
		this.dismissRemark = dismissRemark;
	}

	/* confirmUserId get 方法 */
	public Long getConfirmUserId() {
		return confirmUserId;
	}

	/* confirmUserId set 方法 */
	public void setConfirmUserId(Long confirmUserId) {
		this.confirmUserId = confirmUserId;
	}

	/* confirmUsername get 方法 */
	public String getConfirmUsername() {
		return confirmUsername;
	}

	/* confirmUsername set 方法 */
	public void setConfirmUsername(String confirmUsername) {
		this.confirmUsername = confirmUsername;
	}

	/* createTime get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createTime set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* serverType get 方法 */
	public String getServerType() {
		return serverType;
	}

	/* serverType set 方法 */
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	/* model get 方法 */
	public String getModel() {
		return model;
	}

	/* model set 方法 */
	public void setModel(String model) {
		this.model = model;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Timestamp getPoDate() {
		return poDate;
	}

	public void setPoDate(Timestamp poDate) {
		this.poDate = poDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Long getInConfirmUserId() {
		return inConfirmUserId;
	}

	public void setInConfirmUserId(Long inConfirmUserId) {
		this.inConfirmUserId = inConfirmUserId;
	}

	public String getInConfirmUsername() {
		return inConfirmUsername;
	}

	public void setInConfirmUsername(String inConfirmUsername) {
		this.inConfirmUsername = inConfirmUsername;
	}

	public Timestamp getInConfirmTime() {
		return inConfirmTime;
	}

	public void setInConfirmTime(Timestamp inConfirmTime) {
		this.inConfirmTime = inConfirmTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}
}
