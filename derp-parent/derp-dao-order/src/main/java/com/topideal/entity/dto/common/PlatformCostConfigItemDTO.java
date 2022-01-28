package com.topideal.entity.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class PlatformCostConfigItemDTO implements Serializable {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "平台费用配置ID")
    private Long platformConfigId;

    /**
     * 平台费项id
     */
    @ApiModelProperty(value = "平台费项id")
    private Long platformSettlementId;
    /**
     * 平台费项名称
     */
    @ApiModelProperty(value = "平台费项名称")
    private String platformSettlementName;

    /**
     映射系统费项
     */
    @ApiModelProperty(value="映射系统费项")
    private String settlementConfigName;

    /**
     * 比例
     */
    @ApiModelProperty(value = "比例")
    private Double ratio;
    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
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


    public String getSettlementConfigName() {
        return settlementConfigName;
    }

    public void setSettlementConfigName(String settlementConfigName) {
        this.settlementConfigName = settlementConfigName;
    }

    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*platformConfigId get 方法 */
    public Long getPlatformConfigId(){
        return platformConfigId;
    }
    /*platformConfigId set 方法 */
    public void setPlatformConfigId(Long  platformConfigId){
        this.platformConfigId=platformConfigId;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
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

    public Long getPlatformSettlementId() {
        return platformSettlementId;
    }

    public void setPlatformSettlementId(Long platformSettlementId) {
        this.platformSettlementId = platformSettlementId;
    }

    public String getPlatformSettlementName() {
        return platformSettlementName;
    }

    public void setPlatformSettlementName(String platformSettlementName) {
        this.platformSettlementName = platformSettlementName;
    }
}
