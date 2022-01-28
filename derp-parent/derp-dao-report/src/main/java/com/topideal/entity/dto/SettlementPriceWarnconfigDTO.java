package com.topideal.entity.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 标准成本单价预警配置表
 * @author wenyan
 *
 */
public class SettlementPriceWarnconfigDTO extends PageModel implements Serializable{
    /**
    * id
    */
	@ApiModelProperty(value = "id")
    private Long id;
    /**
    * 公司ID
    */
    @ApiModelProperty(value = "公司ID")
    private Long merchantId;
    /**
    * 公司名称
    */
    @ApiModelProperty(value = "公司名称")
    private String merchantName;
    /**
    * 事业部id
    */
    @ApiModelProperty(value = "事业部id")
    private Long buId;
    /**
    * 事业部名称
    */
    @ApiModelProperty(value = "事业部名称")
    private String buName;
    /**
    * 波动范围
    */
    @ApiModelProperty(value = "波动范围")
    private BigDecimal waveRange;
    /**
    * 邮件主题
    */
    @ApiModelProperty(value = "邮件主题")
    private String emailTitle;
    /**
    * 收件人id
    */
    @ApiModelProperty(value = "收件人id")
    private String receiverId;
    /**
    * 收件人名字
    */
    @ApiModelProperty(value = "收件人名字")
    private String receiverName;
    /**
    * 收件人邮箱
    */
    @ApiModelProperty(value = "收件人邮箱")
    private String receiverEmail;
    /**
    * 状态(1启用,0禁用)
    */
    @ApiModelProperty(value = "状态(1启用,0禁用)")
    private String status;
    @ApiModelProperty(value = "状态Label")
    private String statusLabel;
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
    @ApiModelProperty(value = "修改人")
    private Long modifier;
    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    /**
    * 修改人名称
    */
    @ApiModelProperty(value = "修改人名称")
    private String modifierUsername;
	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*waveRange get 方法 */
    public BigDecimal getWaveRange(){
    return waveRange;
    }
    /*waveRange set 方法 */
    public void setWaveRange(BigDecimal  waveRange){
    this.waveRange=waveRange;
    }
    /*emailTitle get 方法 */
    public String getEmailTitle(){
    return emailTitle;
    }
    /*emailTitle set 方法 */
    public void setEmailTitle(String  emailTitle){
    this.emailTitle=emailTitle;
    }
    /*receiverId get 方法 */
    public String getReceiverId(){
    return receiverId;
    }
    /*receiverId set 方法 */
    public void setReceiverId(String  receiverId){
    this.receiverId=receiverId;
    }
    /*receiverName get 方法 */
    public String getReceiverName(){
    return receiverName;
    }
    /*receiverName set 方法 */
    public void setReceiverName(String  receiverName){
    this.receiverName=receiverName;
    }
    /*receiverEmail get 方法 */
    public String getReceiverEmail(){
    return receiverEmail;
    }
    /*receiverEmail set 方法 */
    public void setReceiverEmail(String  receiverEmail){
    this.receiverEmail=receiverEmail;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
	if(StringUtils.isNotBlank(status)){
		this.statusLabel = DERP_ORDER.getLabelByKey(DERP_REPORT.settlementPriceWarnconfig_statusList, status);
	}
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
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
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getModifierUsername() {
		return modifierUsername;
	}
	public void setModifierUsername(String modifierUsername) {
		this.modifierUsername = modifierUsername;
	}
	public List<Long> getBuList() {
		return buList;
	}
	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}
	
}
