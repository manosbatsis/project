package com.topideal.mongo.entity;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/15 16:51
 * @Description: 事业部库位类型配置表
 */
public class BuStockLocationTypeConfigMongo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long buStockLocationTypeId;
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
     * 事业部ID
     */
    @ApiModelProperty(value = "事业部ID")
    private Long buId;
    /**
     * 事业部名称
     */
    @ApiModelProperty(value = "事业部名称")
    private String buName;
    /**
     * 库位类型
     */
    @ApiModelProperty(value = "库位类型")
    private String name;
    /**
     * 状态(1启用,0禁用)
     */
    @ApiModelProperty(value = "状态(1启用,0禁用)")
    private String status;
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

    public Long getBuStockLocationTypeId() {
        return buStockLocationTypeId;
    }

    public void setBuStockLocationTypeId(Long buStockLocationTypeId) {
        this.buStockLocationTypeId = buStockLocationTypeId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*status get 方法 */
    public String getStatus(){
        return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
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
}
