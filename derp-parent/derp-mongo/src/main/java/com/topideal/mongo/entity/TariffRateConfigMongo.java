package com.topideal.mongo.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TariffRateConfigMongo {
    /**
     * 自增主键
     */
    private Long tariffRateConfigId;
    /**
     * 税率
     */
    private BigDecimal rate;
    /**
     * 创建时间
     */
    private Timestamp createDate;
    /**
     * 修改时间
     */
    private Timestamp modifyDate;
    /**
     * 创建人id
     */
    private Long creater;
    /**
     * 创建人名称
     */
    private String createName;

    public Long getTariffRateConfigId() {
        return tariffRateConfigId;
    }

    public void setTariffRateConfigId(Long tariffRateConfigId) {
        this.tariffRateConfigId = tariffRateConfigId;
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

