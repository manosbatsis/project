package com.topideal.entity.dto.base;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 税率配置DTO
 */
public class TariffRateConfigDTO extends PageModel implements Serializable {

    /**
     * 自增主键
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 税率
     */
    @ApiModelProperty(value = "税率")
    private BigDecimal rate;
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
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    private Long creater;
    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    private String createName;

    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*rate get 方法 */
    public BigDecimal getRate(){
        return rate;
    }
    /*rate set 方法 */
    public void setRate(BigDecimal  rate){
        this.rate=rate;
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



}
