package com.topideal.entity.dto.platformdata;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class OrealPurchaseOrderDTO extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty(value="主键id", required=false)
    private Long id;
    /**
    * 商家ID
    */
	@ApiModelProperty(value="商家ID", required=false)
    private Long merchantId;
    /**
    * 商家名称
    */
	@ApiModelProperty(value="商家名称", required=false)
    private String merchantName;
    /**
    * 事业部ID
    */
	@ApiModelProperty(value="事业部ID", required=false)
    private Long buId;
    /**
    * 事业部名称
    */
	@ApiModelProperty(value="事业部名称", required=false)
    private String buName;
    /**
    * 供应商
    */
	@ApiModelProperty(value="供应商", required=false)
    private String custname;
    /**
    * 订单编号
    */
	@ApiModelProperty(value="订单编号", required=false)
    private String vordercode;
    /**
    * 品牌
    */
	@ApiModelProperty(value="品牌", required=false)
    private String vdef1;
    /**
    * 订单日期
    */
	@ApiModelProperty(value="订单日期", required=false)
    private Timestamp dorderdate;
    /**
    * 业务类型
    */
	@ApiModelProperty(value="业务类型", required=false)
    private String vdef13;
    /**
    * 收货地址
    */
	@ApiModelProperty(value="收货地址", required=false)
    private String adress;
    /**
    * 来源:1.欧莱雅接口
    */
	@ApiModelProperty(value="欧莱雅接口", required=false)
    private String source;
	@ApiModelProperty(value="欧莱雅接口", required=false)
    private String sourceLabel;
    /**
    * 采购订单类型
    */
	@ApiModelProperty(value="采购订单类型", required=false)
    private String docname;
    /**
    * CSR确认单号
    */
	@ApiModelProperty(value="CSR确认单号", required=false)
    private String vdef7;
    /**
     * 订单开始时间
     */
	@ApiModelProperty(value="订单开始时间", required=false)
    private String dorderdateStartDate;
    
    /**
     * 订单开始结束时间
     */
	@ApiModelProperty(value="订单开始结束时间", required=false)
    private String dorderdateEndDate;
    /**
    * 创建日期
    */
	@ApiModelProperty(value="创建日期", required=false)
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty(value="修改时间", required=false)
    private Timestamp modifyDate;

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
    /*custname get 方法 */
    public String getCustname(){
    return custname;
    }
    /*custname set 方法 */
    public void setCustname(String  custname){
    this.custname=custname;
    }
    /*vordercode get 方法 */
    public String getVordercode(){
    return vordercode;
    }
    /*vordercode set 方法 */
    public void setVordercode(String  vordercode){
    this.vordercode=vordercode;
    }
    /*vdef1 get 方法 */
    public String getVdef1(){
    return vdef1;
    }
    /*vdef1 set 方法 */
    public void setVdef1(String  vdef1){
    this.vdef1=vdef1;
    }
    /*dorderdate get 方法 */
    public Timestamp getDorderdate(){
    return dorderdate;
    }
    /*dorderdate set 方法 */
    public void setDorderdate(Timestamp  dorderdate){
    this.dorderdate=dorderdate;
    }
    /*vdef13 get 方法 */
    public String getVdef13(){
    return vdef13;
    }
    /*vdef13 set 方法 */
    public void setVdef13(String  vdef13){
    this.vdef13=vdef13;
    }
    /*adress get 方法 */
    public String getAdress(){
    return adress;
    }
    /*adress set 方法 */
    public void setAdress(String  adress){
    this.adress=adress;
    }
    /*source get 方法 */
    public String getSource(){
    return source;
    }
    /*source set 方法 */
    public void setSource(String  source){
    this.source=source;
    this.sourceLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.orealPurchaseOrder_sourceList, source) ;
    }
    /*docname get 方法 */
    public String getDocname(){
    return docname;
    }
    /*docname set 方法 */
    public void setDocname(String  docname){
    this.docname=docname;
    }
    /*vdef7 get 方法 */
    public String getVdef7(){
    return vdef7;
    }
    /*vdef7 set 方法 */
    public void setVdef7(String  vdef7){
    this.vdef7=vdef7;
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
	public String getSourceLabel() {
		return sourceLabel;
	}
	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}
	public String getDorderdateStartDate() {
		return dorderdateStartDate;
	}
	public void setDorderdateStartDate(String dorderdateStartDate) {
		this.dorderdateStartDate = dorderdateStartDate;
	}
	public String getDorderdateEndDate() {
		return dorderdateEndDate;
	}
	public void setDorderdateEndDate(String dorderdateEndDate) {
		this.dorderdateEndDate = dorderdateEndDate;
	}






}
