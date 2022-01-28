package com.topideal.entity.dto;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class AutomaticCheckTaskDTO extends PageModel implements Serializable{

    /**
    * ID
    */
	@ApiModelProperty(value = " ID")
    private Long id;
    /**
    * 任务编码(自生成)
    */
	@ApiModelProperty(value = "任务编码(自生成)")
    private String taskCode;
    /**
    * 任务类型  1:POP流水核对 2:仓库流水核对
    */
	@ApiModelProperty(value = "任务类型  1:POP流水核对 2:仓库流水核对")
    private String taskType;
	@ApiModelProperty(value = "任务类型  1:POP流水核对 2:仓库流水核对Label")
    private String taskTypeLabel;
    /**
    * 核对结果 0:未对平 1:已对平
    */
	@ApiModelProperty(value = "核对结果 0:未对平 1:已对平")
    private String checkResult;
	@ApiModelProperty(value = "核对结果 0:未对平 1:已对平Label")
    private String checkResultLabel;
    /**
    * 处理状态 1:处理中 2:已完成
    */
	@ApiModelProperty(value = "处理状态 1:处理中 2:已完成")
    private String state;
	@ApiModelProperty(value = "处理状态 1:处理中 2:已完成Label")
    private String stateLabel;
    /**
    * 出仓仓库ID
    */
	@ApiModelProperty(value = "出仓仓库ID")
    private Long outDepotId;
    /**
    * 出仓仓库名称
    */
	@ApiModelProperty(value = "出仓仓库名称")
    private String outDepotName;
    /**
    * 出仓仓库编码
    */
	@ApiModelProperty(value = "出仓仓库编码")
    private String outDepotCode;
    /**
    * 电商平台编码：100011111-第e仓 100044998-京东 1000000310-天猫 1000004790-拼多多 1000005237-有赞 1000004941-斑马 1000000390-考拉 1000002612-小红书 1000000687-澳新 1000004058-小小包
    */
	@ApiModelProperty(value = "电商平台编码：100011111-第e仓 100044998-京东 1000000310-天猫 1000004790-拼多多 1000005237-有赞 1000004941-斑马 1000000390-考拉 1000002612-小红书 1000000687-澳新 1000004058-小小包")
    private String storePlatformCode;
	@ApiModelProperty(value = "电商平台Label")
    private String storePlatformLabel;
    /**
    * 店铺编码
    */
	@ApiModelProperty(value = "店铺编码")
    private String shopCode;
    /**
    * 店铺名称
    */
	@ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**
    * 创建人
    */
	@ApiModelProperty(value = "创建人")
    private Long creater;
    /**
    * 创建人名称
    */
	@ApiModelProperty(value = "创建人名称")
    private String createName;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 修改人
    */
	@ApiModelProperty(value = " 修改人")
    private Long modifier;
    /**
    * 修改人名称
    */
	@ApiModelProperty(value = "修改人名称")
    private String modifierName;
    /**
    * 修改日期
    */
	@ApiModelProperty(value = "修改日期")
    private Timestamp modifyDate;
    /**
     * 商家ID
     */
	@ApiModelProperty(value = "商家ID")
     private Long merchantId;
     /**
     * 商家名称
     */
	@ApiModelProperty(value = "商家名称")
     private String merchantName;
     /**
      * 数据源 1：GSS报表 2：菜鸟后台
      */
	@ApiModelProperty(value = "数据源 1：GSS报表 2：菜鸟后台")
     private String dataSource;
	@ApiModelProperty(value = "数据源 1：GSS报表 2：菜鸟后台Label")
     private String dataSourceLabel;
     /**
      * 核对备注
      */
	@ApiModelProperty(value = "核对备注")
      private String remark;

      /**
       * 文件的存储路径
       */
	@ApiModelProperty(value = "文件的存储路径")
      private String storePath;
      /**
       * 源文件存储路径
       */
	@ApiModelProperty(value = "源文件存储路径")
      private String sourcePath;

    /**
     *是否标记过（0-否，1-是）
     */
	@ApiModelProperty(value = "是否标记过（0-否，1-是）")
    private String isMark;
	@ApiModelProperty(value = "是否标记过（0-否，1-是）Label")
    private String isMarkLabel;

	// 核对开始时间
	@ApiModelProperty(value = "核对开始时间")
	private String checkStartDate;
	// 核对结束时间
	@ApiModelProperty(value = "核对结束时间")
	private String checkEndDate; 	 	

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*taskCode get 方法 */
    public String getTaskCode(){
    return taskCode;
    }
    /*taskCode set 方法 */
    public void setTaskCode(String  taskCode){
    this.taskCode=taskCode;
    }
    /*taskType get 方法 */
    public String getTaskType(){
    return taskType;
    }
    /*taskType set 方法 */
    public void setTaskType(String  taskType){
    this.taskType=taskType;
    if(StringUtils.isNotBlank(taskType)){
 	   this.taskTypeLabel= DERP.getLabelByKey(DERP_REPORT.automaticCheckTask_taskTypeList, taskType);
    }
    }
    /*checkResult get 方法 */
    public String getCheckResult(){
    return checkResult;
    }
    /*checkResult set 方法 */
    public void setCheckResult(String  checkResult){
    this.checkResult=checkResult;
    if(StringUtils.isNotBlank(checkResult)){
 	   this.checkResultLabel = DERP.getLabelByKey(DERP_REPORT.automaticCheckTask_checkResultList, checkResult);
    }
    }
    /*state get 方法 */
    public String getState(){
    return state;
    }
    /*state set 方法 */
    public void setState(String  state){
    this.state=state;
    if(StringUtils.isNotBlank(state)){
 	   this.stateLabel = DERP.getLabelByKey(DERP_REPORT.automaticCheckTask_stateList, state);
    }
    }
    /*outDepotId get 方法 */
    public Long getOutDepotId(){
    return outDepotId;
    }
    /*outDepotId set 方法 */
    public void setOutDepotId(Long  outDepotId){
    this.outDepotId=outDepotId;
    }
    /*outDepotName get 方法 */
    public String getOutDepotName(){
    return outDepotName;
    }
    /*outDepotName set 方法 */
    public void setOutDepotName(String  outDepotName){
    this.outDepotName=outDepotName;
    }
    /*outDepotCode get 方法 */
    public String getOutDepotCode(){
    return outDepotCode;
    }
    /*outDepotCode set 方法 */
    public void setOutDepotCode(String  outDepotCode){
    this.outDepotCode=outDepotCode;
    }
    /*storePlatformCode get 方法 */
    public String getStorePlatformCode(){
    return storePlatformCode;
    }
    /*storePlatformCode set 方法 */
    public void setStorePlatformCode(String  storePlatformCode){
    this.storePlatformCode=storePlatformCode;
    if(StringUtils.isNotBlank(storePlatformCode)){
 	   this.storePlatformLabel = DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode);
    }
    }
    /*shopCode get 方法 */
    public String getShopCode(){
    return shopCode;
    }
    /*shopCode set 方法 */
    public void setShopCode(String  shopCode){
    this.shopCode=shopCode;
    }
    /*shopName get 方法 */
    public String getShopName(){
    return shopName;
    }
    /*shopName set 方法 */
    public void setShopName(String  shopName){
    this.shopName=shopName;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifierName get 方法 */
    public String getModifierName(){
    return modifierName;
    }
    /*modifierName set 方法 */
    public void setModifierName(String  modifierName){
    this.modifierName=modifierName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
	public String getTaskTypeLabel() {
		return taskTypeLabel;
	}
	public void setTaskTypeLabel(String taskTypeLabel) {
		this.taskTypeLabel = taskTypeLabel;
	}
	public String getCheckResultLabel() {
		return checkResultLabel;
	}
	public void setCheckResultLabel(String checkResultLabel) {
		this.checkResultLabel = checkResultLabel;
	}
	public String getStateLabel() {
		return stateLabel;
	}
	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}
	public String getStorePlatformLabel() {
		return storePlatformLabel;
	}
	public void setStorePlatformLabel(String storePlatformLabel) {
		this.storePlatformLabel = storePlatformLabel;
	}
	public String getCheckStartDate() {
		return checkStartDate;
	}
	public void setCheckStartDate(String checkStartDate) {
		this.checkStartDate = checkStartDate;
	}
	public String getCheckEndDate() {
		return checkEndDate;
	}
	public void setCheckEndDate(String checkEndDate) {
		this.checkEndDate = checkEndDate;
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
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	    if(dataSource!=null){
	  	   this.dataSourceLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.automaticCheckTask_dataSourceList, dataSource);
	     }
	}
	public String getDataSourceLabel() {
		return dataSourceLabel;
	}
	public void setDataSourceLabel(String dataSourceLabel) {
		this.dataSourceLabel = dataSourceLabel;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStorePath() {
		return storePath;
	}
	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

    public String getIsMark() {
        return isMark;
    }

    public void setIsMark(String isMark) {
        this.isMark = isMark;
        if(StringUtils.isNotBlank(isMark)){
      	   this.isMarkLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.automaticCheckTask_isMarkList, isMark);
         }
    }
	public String getIsMarkLabel() {
		return isMarkLabel;
	}
	public void setIsMarkLabel(String isMarkLabel) {
		this.isMarkLabel = isMarkLabel;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
    
}
