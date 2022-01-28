package com.topideal.entity.dto.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/13 17:09
 * @Description:
 */
public class BuStockLocationTypeConfigDTO extends PageModel implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 公司ID
     */
    private Long merchantId;
    /**
     * 公司名称
     */
    private String merchantName;
    /**
     * 事业部ID
     */
    private Long buId;
    /**
     * 事业部名称
     */
    private String buName;
    /**
     * 库位类型
     */
    private String name;
    /**
     * 状态(1启用,0禁用)
     */
    private String status;
    private String statusLabel;
    /**
     * 创建人ID
     */
    private Long creater;
    /**
     * 创建人名称
     */
    private String createrName;
    /**
     * 创建时间
     */
    private Timestamp createDate;
    /**
     * 修改时间
     */
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
        this.statusLabel = DERP_SYS.getLabelByKey(DERP_SYS.bu_stock_location_type_config_statusList, status);
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
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
}
