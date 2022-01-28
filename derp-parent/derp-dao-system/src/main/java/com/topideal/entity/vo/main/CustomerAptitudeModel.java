package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 供应商资质
 * @author lian_
 *
 */
public class CustomerAptitudeModel extends PageModel implements Serializable{

     //供货记录
     private String supplyRecord;
     //银行流水
     private String bankFlow;
     //组织机构代码副本
     private String organizationCode;
     //营业执照副本
     private String charteredNo;
     //注册登记证
     private String registrationNo;
     //品牌授权
     private String brandAuthorization;
     //供应商id
     private Long customerId;
     //企业备案表
     private String keepRecord;
     //id
     private Long id;
     //法人身份证
     private String legalCardNo;
     //证明信息
     private String proofInfo;
     //修改人
     private Long modifier;
     //修改时间
     private Timestamp modifyDate;
     //企业备案表修改人名称
     private String keepModifierName;
     //法人身份证修改人名称
     private String legalModifierName;
     //营业执照副本修改人id
     private Long chartModifier;
     //组织机构代码副本修改人id
     private Long orgModifier;
     //注册登记证修改人id
     private Long registModifier;
     //法人身份证修改人id
     private Long legalModifier;
     //银行流水修改人id
     private Long bankModifier;
     //银行流水修改人名称
     private String bankModifierName;
     //营业执照副本修改时间
     private Timestamp chartModifyDate;
     //银行流水修改时间
     private Timestamp bankModifyDate;
     //证明信息修改时间
     private Timestamp proofModifyDate;
     //品牌授权修改人名称
     private Long brandModifier;
     //组织机构代码副本修改时间
     private Timestamp orgModifyDate;
     //组织机构代码副本修改人名称
     private String orgModifierName;
     //证明信息修改人id
     private Long proofModifier;
     //供货记录修改时间
     private Timestamp supplyModifyDate;
     //企业备案表修改时间
     private Timestamp keepModifyDate;
     //证明信息修改人名称
     private String proofModifierName;
     //供货记录修改人id
     private Long supplyModifier;
     //供货记录修改人名称
     private String supplyModifierName;
     // 注册登记证修改时间
     private Timestamp registModifyDate;
     //企业备案表修改人id
     private Long keepModifier;
     //法人身份证修改时间
     private Timestamp legalModifyDate;
     //品牌授权修改人名称
     private String brandModifierName;
     //注册登记证修改人名称
     private String registModifierName;
     //营业执照副本修改人名称
     private String chartModifierName;
     //品牌授权修改时间
     private Timestamp brandModifyDate;
     
     
     public String getKeepModifierName() {
		return keepModifierName;
	}
	public void setKeepModifierName(String keepModifierName) {
		this.keepModifierName = keepModifierName;
	}
	public String getLegalModifierName() {
		return legalModifierName;
	}
	public void setLegalModifierName(String legalModifierName) {
		this.legalModifierName = legalModifierName;
	}
	public Long getChartModifier() {
		return chartModifier;
	}
	public void setChartModifier(Long chartModifier) {
		this.chartModifier = chartModifier;
	}
	public Long getOrgModifier() {
		return orgModifier;
	}
	public void setOrgModifier(Long orgModifier) {
		this.orgModifier = orgModifier;
	}
	public Long getRegistModifier() {
		return registModifier;
	}
	public void setRegistModifier(Long registModifier) {
		this.registModifier = registModifier;
	}
	public Long getLegalModifier() {
		return legalModifier;
	}
	public void setLegalModifier(Long legalModifier) {
		this.legalModifier = legalModifier;
	}
	public Long getBankModifier() {
		return bankModifier;
	}
	public void setBankModifier(Long bankModifier) {
		this.bankModifier = bankModifier;
	}
	public String getBankModifierName() {
		return bankModifierName;
	}
	public void setBankModifierName(String bankModifierName) {
		this.bankModifierName = bankModifierName;
	}
	public Timestamp getChartModifyDate() {
		return chartModifyDate;
	}
	public void setChartModifyDate(Timestamp chartModifyDate) {
		this.chartModifyDate = chartModifyDate;
	}
	public Timestamp getBankModifyDate() {
		return bankModifyDate;
	}
	public void setBankModifyDate(Timestamp bankModifyDate) {
		this.bankModifyDate = bankModifyDate;
	}
	public Timestamp getProofModifyDate() {
		return proofModifyDate;
	}
	public void setProofModifyDate(Timestamp proofModifyDate) {
		this.proofModifyDate = proofModifyDate;
	}
	public Long getBrandModifier() {
		return brandModifier;
	}
	public void setBrandModifier(Long brandModifier) {
		this.brandModifier = brandModifier;
	}
	public Timestamp getOrgModifyDate() {
		return orgModifyDate;
	}
	public void setOrgModifyDate(Timestamp orgModifyDate) {
		this.orgModifyDate = orgModifyDate;
	}
	public String getOrgModifierName() {
		return orgModifierName;
	}
	public void setOrgModifierName(String orgModifierName) {
		this.orgModifierName = orgModifierName;
	}
	public Long getProofModifier() {
		return proofModifier;
	}
	public void setProofModifier(Long proofModifier) {
		this.proofModifier = proofModifier;
	}
	public Timestamp getSupplyModifyDate() {
		return supplyModifyDate;
	}
	public void setSupplyModifyDate(Timestamp supplyModifyDate) {
		this.supplyModifyDate = supplyModifyDate;
	}
	public Timestamp getKeepModifyDate() {
		return keepModifyDate;
	}
	public void setKeepModifyDate(Timestamp keepModifyDate) {
		this.keepModifyDate = keepModifyDate;
	}
	public String getProofModifierName() {
		return proofModifierName;
	}
	public void setProofModifierName(String proofModifierName) {
		this.proofModifierName = proofModifierName;
	}
	public Long getSupplyModifier() {
		return supplyModifier;
	}
	public void setSupplyModifier(Long supplyModifier) {
		this.supplyModifier = supplyModifier;
	}
	public String getSupplyModifierName() {
		return supplyModifierName;
	}
	public void setSupplyModifierName(String supplyModifierName) {
		this.supplyModifierName = supplyModifierName;
	}
	public Timestamp getRegistModifyDate() {
		return registModifyDate;
	}
	public void setRegistModifyDate(Timestamp registModifyDate) {
		this.registModifyDate = registModifyDate;
	}
	public Long getKeepModifier() {
		return keepModifier;
	}
	public void setKeepModifier(Long keepModifier) {
		this.keepModifier = keepModifier;
	}
	public Timestamp getLegalModifyDate() {
		return legalModifyDate;
	}
	public void setLegalModifyDate(Timestamp legalModifyDate) {
		this.legalModifyDate = legalModifyDate;
	}
	public String getBrandModifierName() {
		return brandModifierName;
	}
	public void setBrandModifierName(String brandModifierName) {
		this.brandModifierName = brandModifierName;
	}
	public String getRegistModifierName() {
		return registModifierName;
	}
	public void setRegistModifierName(String registModifierName) {
		this.registModifierName = registModifierName;
	}
	public String getChartModifierName() {
		return chartModifierName;
	}
	public void setChartModifierName(String chartModifierName) {
		this.chartModifierName = chartModifierName;
	}
	public Timestamp getBrandModifyDate() {
		return brandModifyDate;
	}
	public void setBrandModifyDate(Timestamp brandModifyDate) {
		this.brandModifyDate = brandModifyDate;
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
    /*supplyRecord get 方法 */
    public String getSupplyRecord(){
        return supplyRecord;
    }
    /*supplyRecord set 方法 */
    public void setSupplyRecord(String  supplyRecord){
        this.supplyRecord=supplyRecord;
    }
    /*bankFlow get 方法 */
    public String getBankFlow(){
        return bankFlow;
    }
    /*bankFlow set 方法 */
    public void setBankFlow(String  bankFlow){
        this.bankFlow=bankFlow;
    }
    /*organizationCode get 方法 */
    public String getOrganizationCode(){
        return organizationCode;
    }
    /*organizationCode set 方法 */
    public void setOrganizationCode(String  organizationCode){
        this.organizationCode=organizationCode;
    }
    /*charteredNo get 方法 */
    public String getCharteredNo(){
        return charteredNo;
    }
    /*charteredNo set 方法 */
    public void setCharteredNo(String  charteredNo){
        this.charteredNo=charteredNo;
    }
    /*registrationNo get 方法 */
    public String getRegistrationNo(){
        return registrationNo;
    }
    /*registrationNo set 方法 */
    public void setRegistrationNo(String  registrationNo){
        this.registrationNo=registrationNo;
    }
    /*brandAuthorization get 方法 */
    public String getBrandAuthorization(){
        return brandAuthorization;
    }
    /*brandAuthorization set 方法 */
    public void setBrandAuthorization(String  brandAuthorization){
        this.brandAuthorization=brandAuthorization;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
        return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
        this.customerId=customerId;
    }
    /*keepRecord get 方法 */
    public String getKeepRecord(){
        return keepRecord;
    }
    /*keepRecord set 方法 */
    public void setKeepRecord(String  keepRecord){
        this.keepRecord=keepRecord;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*legalCardNo get 方法 */
    public String getLegalCardNo(){
        return legalCardNo;
    }
    /*legalCardNo set 方法 */
    public void setLegalCardNo(String  legalCardNo){
        this.legalCardNo=legalCardNo;
    }
    /*proofInfo get 方法 */
    public String getProofInfo(){
        return proofInfo;
    }
    /*proofInfo set 方法 */
    public void setProofInfo(String  proofInfo){
        this.proofInfo=proofInfo;
    }






}

