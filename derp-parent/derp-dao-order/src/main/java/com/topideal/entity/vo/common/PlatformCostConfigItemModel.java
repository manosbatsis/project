package com.topideal.entity.vo.common;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class PlatformCostConfigItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 平台费用配置ID
    */
    private Long platformConfigId;
    /**
    * 平台费项id
    */
    private Long platformSettlementId;
    /**
    * 平台费项名称
    */
    private String platformSettlementName;
    /**
     映射系统费项
     */
    private String settlementConfigName;
    /**
    * 比例
    */
    private Double ratio;
    /**
    * 创建人id
    */
    private Long creater;
    /**
    * 创建人名称
    */
    private String createName;
    /**
    * 创建时间
    */
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
    /*platformSettlementId get 方法 */
    public Long getPlatformSettlementId(){
    return platformSettlementId;
    }
    /*platformSettlementId set 方法 */
    public void setPlatformSettlementId(Long  platformSettlementId){
    this.platformSettlementId=platformSettlementId;
    }
    /*platformSettlementName get 方法 */
    public String getPlatformSettlementName(){
    return platformSettlementName;
    }
    /*platformSettlementName set 方法 */
    public void setPlatformSettlementName(String  platformSettlementName){
    this.platformSettlementName=platformSettlementName;
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







}
