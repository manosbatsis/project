package com.topideal.entity.dto.base;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 汇率管理表
 * @author lian_
 */
public class ExchangeRateDTO extends PageModel implements Serializable{


	@ApiModelProperty(value = "id")
    private Long id;
	@ApiModelProperty(value = "原币种编码")
    private String origCurrencyCode;
	@ApiModelProperty(value = "原币种名称")
    private String origCurrencyName;
	@ApiModelProperty(value = "目标币种编码")
    private String tgtCurrencyCode;
	@ApiModelProperty(value = "目标币种名称")
    private String tgtCurrencyName;
	@ApiModelProperty(value = "汇率")
    private Double rate;
	@ApiModelProperty(value = "平均汇率")
    private Double avgRate;
	@ApiModelProperty(value = "生效年月  格式: yyyy-MM-dd")
    private Date effectiveDate;
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
	@ApiModelProperty(value = "状态 006删除")
    private String status;
	@ApiModelProperty(value = "effectiveDateStr")
    private String effectiveDateStr ;
	@ApiModelProperty(value = "创建人id")
    private Long creater;
	@ApiModelProperty(value = "创建人名称")
    private String createName;
	@ApiModelProperty(value = "数据来源")
    private String dataSource;
	@ApiModelProperty(value = "dataSourceLabel")
    private String dataSourceLabel;

    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
    /*rate get 方法 */
    public Double getRate(){
    return rate;
    }
    /*rate set 方法 */
    public void setRate(Double  rate){
    this.rate=rate;
    }
    
    public Double getAvgRate() {
		return avgRate;
	}
	public void setAvgRate(Double avgRate) {
		this.avgRate = avgRate;
	}
	/*effectiveDate get 方法 */
    public Date getEffectiveDate(){
    return effectiveDate;
    }
    /*effectiveDate set 方法 */
    public void setEffectiveDate(Date  effectiveDate){
    this.effectiveDate=effectiveDate;
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
	public String getEffectiveDateStr() {
		return effectiveDateStr;
	}
	public void setEffectiveDateStr(String effectiveDateStr) {
		this.effectiveDateStr = effectiveDateStr;
	}

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
        this.dataSourceLabel = DERP_SYS.getLabelByKey(DERP_SYS.exchangeRate_dataSourceList, dataSource);
    }

    public String getDataSourceLabel() {
        return dataSourceLabel;
    }

    public void setDataSourceLabel(String dataSourceLabel) {
        this.dataSourceLabel = dataSourceLabel;
    }
}
