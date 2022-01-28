package com.topideal.entity.dto.base;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class ExchangeRateConfigDTO extends PageModel implements Serializable{


	@ApiModelProperty(value = "ID")
    private Long id;
	@ApiModelProperty(value = "原币种编码")
    private String origCurrencyCode;
	@ApiModelProperty(value = "原币种名称")
    private String origCurrencyName;
	@ApiModelProperty(value = "目标币种编码")
    private String tgtCurrencyCode;
	@ApiModelProperty(value = "目标币种名称")
    private String tgtCurrencyName;
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
	@ApiModelProperty(value = "创建人id")
    private Long creater;
	@ApiModelProperty(value = "创建人名称")
    private String createName;
	@ApiModelProperty(value = "数据来源： WGJ-外管局  DBS-星展银行  SGCJ-手工创建")
    private String dataSource;
	@ApiModelProperty(value = "dataSourceLabel")
    private String dataSourceLabel;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*origCurrencyCode get 方法 */
    public String getOrigCurrencyCode(){
    return origCurrencyCode;
    }
    /*origCurrencyCode set 方法 */
    public void setOrigCurrencyCode(String  origCurrencyCode){
    this.origCurrencyCode=origCurrencyCode;
    }
    /*origCurrencyName get 方法 */
    public String getOrigCurrencyName(){
    return origCurrencyName;
    }
    /*origCurrencyName set 方法 */
    public void setOrigCurrencyName(String  origCurrencyName){
    this.origCurrencyName=origCurrencyName;
    }
    /*tgtCurrencyCode get 方法 */
    public String getTgtCurrencyCode(){
    return tgtCurrencyCode;
    }
    /*tgtCurrencyCode set 方法 */
    public void setTgtCurrencyCode(String  tgtCurrencyCode){
    this.tgtCurrencyCode=tgtCurrencyCode;
    }
    /*tgtCurrencyName get 方法 */
    public String getTgtCurrencyName(){
    return tgtCurrencyName;
    }
    /*tgtCurrencyName set 方法 */
    public void setTgtCurrencyName(String  tgtCurrencyName){
    this.tgtCurrencyName=tgtCurrencyName;
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
    /*dataSource get 方法 */
    public String getDataSource(){
    return dataSource;
    }
    /*dataSource set 方法 */
    public void setDataSource(String  dataSource){
    this.dataSource=dataSource;
    this.dataSourceLabel = DERP_SYS.getLabelByKey(DERP_SYS.exchangeRateConfig_dataSourceList, dataSource);
    }
	public String getDataSourceLabel() {
		return dataSourceLabel;
	}
	public void setDataSourceLabel(String dataSourceLabel) {
		this.dataSourceLabel = dataSourceLabel;
	}






}
