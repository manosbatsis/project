package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class TocSettlementReceiveBillCostItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 账单Id
    */
    private Long billId;
    /**
    * 外部订单号
    */
    private String externalCode;
    /**
    * 费项id
    */
    private Long projectId;
    /**
    * 费项名称
    */
    private String projectName;
    /**
     * 平台费项id
     */
    private Long platformProjectId;
    /**
     * 平台费项名称
     */
    private String platformProjectName;
    /**
    * 结算金额（原币）
    */
    private BigDecimal originalAmount;
    /**
    * 结算金额（RMB）
    */
    private BigDecimal rmbAmount;
    /**
    * 结算汇率
    */
    private BigDecimal settlementRate;
    /**
    * 母品牌
    */
    private String parentBrandName;
    /**
    * 母品牌id
    */
    private Long parentBrandId;
    /**
    * 母品牌编码
    */
    private String parentBrandCode;
    /**
    * 数量
    */
    private Integer num;
    /**
     * 补扣款类型 0-补款 1-扣款
     */
    private String billType;
    /**
     * 数据来源 0-导入 1-手动添加
     */
    private String dataSource;
    /**
     * 备注
     */
    private String remark;    
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 系统订单号
     */
    private String orderCode;
    /**
     * 暂估费用表头id
     */
    private Long tempCostBillId;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*billId get 方法 */
    public Long getBillId(){
    return billId;
    }
    /*billId set 方法 */
    public void setBillId(Long  billId){
    this.billId=billId;
    }
    /*externalCode get 方法 */
    public String getExternalCode(){
    return externalCode;
    }
    /*externalCode set 方法 */
    public void setExternalCode(String  externalCode){
    this.externalCode=externalCode;
    }
    /*projectId get 方法 */
    public Long getProjectId(){
    return projectId;
    }
    /*projectId set 方法 */
    public void setProjectId(Long  projectId){
    this.projectId=projectId;
    }
    /*projectName get 方法 */
    public String getProjectName(){
    return projectName;
    }
    /*projectName set 方法 */
    public void setProjectName(String  projectName){
    this.projectName=projectName;
    }
    /*originalAmount get 方法 */
    public BigDecimal getOriginalAmount(){
    return originalAmount;
    }
    /*originalAmount set 方法 */
    public void setOriginalAmount(BigDecimal  originalAmount){
    this.originalAmount=originalAmount;
    }
    /*rmbAmount get 方法 */
    public BigDecimal getRmbAmount(){
    return rmbAmount;
    }
    /*rmbAmount set 方法 */
    public void setRmbAmount(BigDecimal  rmbAmount){
    this.rmbAmount=rmbAmount;
    }
    /*settlementRate get 方法 */
    public BigDecimal getSettlementRate(){
    return settlementRate;
    }
    /*settlementRate set 方法 */
    public void setSettlementRate(BigDecimal  settlementRate){
    this.settlementRate=settlementRate;
    }
    /*parentBrandName get 方法 */
    public String getParentBrandName(){
    return parentBrandName;
    }
    /*parentBrandName set 方法 */
    public void setParentBrandName(String  parentBrandName){
    this.parentBrandName=parentBrandName;
    }
    /*parentBrandId get 方法 */
    public Long getParentBrandId(){
    return parentBrandId;
    }
    /*parentBrandId set 方法 */
    public void setParentBrandId(Long  parentBrandId){
    this.parentBrandId=parentBrandId;
    }
    /*parentBrandCode get 方法 */
    public String getParentBrandCode(){
    return parentBrandCode;
    }
    /*parentBrandCode set 方法 */
    public void setParentBrandCode(String  parentBrandCode){
    this.parentBrandCode=parentBrandCode;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    
    public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
	public Long getPlatformProjectId() {
		return platformProjectId;
	}
	public void setPlatformProjectId(Long platformProjectId) {
		this.platformProjectId = platformProjectId;
	}
	public String getPlatformProjectName() {
		return platformProjectName;
	}
	public void setPlatformProjectName(String platformProjectName) {
		this.platformProjectName = platformProjectName;
	}

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getTempCostBillId() {
        return tempCostBillId;
    }

    public void setTempCostBillId(Long tempCostBillId) {
        this.tempCostBillId = tempCostBillId;
    }
}
