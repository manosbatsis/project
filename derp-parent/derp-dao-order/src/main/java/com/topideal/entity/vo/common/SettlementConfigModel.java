package com.topideal.entity.vo.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class SettlementConfigModel extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty(value = "主键ID")
    private Long id;
    /**
    * 本级费项编码
    */
	@ApiModelProperty(value = "本级费项编码")
    private String projectCode;
    /**
    * 本级费项名称
    */
	@ApiModelProperty(value = "本级费项名称")
    private String projectName;
    /**
    * 层级 1:一级,2:二级
    */
	@ApiModelProperty(value = "层级 1:一级,2:二级")
    private String level;
    /**
    * 上级费项名称
    */
	@ApiModelProperty(value = "上级费项名称")
    private String parentProjectName;
    /**
    * 上级费项id
    */
	@ApiModelProperty(value = "上级费项id")
    private Long parentId;
    /**
    * NC收支费项名称
    */
	@ApiModelProperty(value = "NC收支费项名称")
    private String paymentSubjectName;
    /**
    * NC收支费项id
    */
	@ApiModelProperty(value = "NC收支费项id")
    private Long paymentSubjectId;
    /**
    * 适用客户: 1通用,2指定客户
    */
	@ApiModelProperty(value = "适用客户: 1通用,2指定客户")
    private String suitableCustomer;
    /**
    * 状态(1使用中,0已禁用)
    */
	@ApiModelProperty(value = "状态(1使用中,0已禁用)")
    private String status;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    /**
    * 修改人ID
    */
	@ApiModelProperty(value = "修改人ID")
    private Long modifier;
    /**
    * 修改人名
    */
	@ApiModelProperty(value = "修改人名")
    private String modifierName;
    /**
    * 创建人ID
    */
	@ApiModelProperty(value = "创建人ID")
    private Long creater;
    /**
    * 创建人名称
    */
	@ApiModelProperty(value = "创建人名称")
    private String createrName;

    /**
     * 主数据据编码
     */
	@ApiModelProperty(value = "主数据据编码")
    private String inExpCode;
	 /**
	  * 适用类型:1.To B 2.To C
	  */
	@ApiModelProperty(value = "适用类型:1.To B 2.To C")
	 private String type;
    /**
     * 适用模块:1.应付 2.应收 3.预付 4.预收
     */
	@ApiModelProperty(value = "适用模块:1.应付 2.应收 3.预付 4.预收")
	 private String moduleType;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*projectCode get 方法 */
    public String getProjectCode(){
    return projectCode;
    }
    /*projectCode set 方法 */
    public void setProjectCode(String  projectCode){
    this.projectCode=projectCode;
    }
    /*projectName get 方法 */
    public String getProjectName(){
    return projectName;
    }
    /*projectName set 方法 */
    public void setProjectName(String  projectName){
    this.projectName=projectName;
    }
    /*level get 方法 */
    public String getLevel(){
    return level;
    }
    /*level set 方法 */
    public void setLevel(String  level){
    this.level=level;
    }
    /*parentProjectName get 方法 */
    public String getParentProjectName(){
    return parentProjectName;
    }
    /*parentProjectName set 方法 */
    public void setParentProjectName(String  parentProjectName){
    this.parentProjectName=parentProjectName;
    }
    /*parentId get 方法 */
    public Long getParentId(){
    return parentId;
    }
    /*parentId set 方法 */
    public void setParentId(Long  parentId){
    this.parentId=parentId;
    }
    /*paymentSubjectName get 方法 */
    public String getPaymentSubjectName(){
    return paymentSubjectName;
    }
    /*paymentSubjectName set 方法 */
    public void setPaymentSubjectName(String  paymentSubjectName){
    this.paymentSubjectName=paymentSubjectName;
    }
    /*paymentSubjectId get 方法 */
    public Long getPaymentSubjectId(){
    return paymentSubjectId;
    }
    /*paymentSubjectId set 方法 */
    public void setPaymentSubjectId(Long  paymentSubjectId){
    this.paymentSubjectId=paymentSubjectId;
    }
    /*suitableCustomer get 方法 */
    public String getSuitableCustomer(){
    return suitableCustomer;
    }
    /*suitableCustomer set 方法 */
    public void setSuitableCustomer(String  suitableCustomer){
    this.suitableCustomer=suitableCustomer;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
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
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createrName get 方法 */
    public String getCreaterName(){
    return createrName;
    }
    /*createrName set 方法 */
    public void setCreaterName(String  createrName){
    this.createrName=createrName;
    }

    public String getInExpCode() {
        return inExpCode;
    }

    public void setInExpCode(String inExpCode) {
        this.inExpCode = inExpCode;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }
}
