package com.topideal.entity.vo.reporting;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class VipPoBillTempModel extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    private Long id;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名
    */
    private String merchantName;
    /**
    * 仓库ID
    */
    private Long depotId;
    /**
    * 仓库名
    */
    private String depotName;
    /**
    * PO号
    */
    private String poNo;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 出库数量
    */
    private Integer outDepotAccount;
    /**
    * 入库数量
    */
    private Integer inDepotAccount;
    /**
    * 未结算量
    */
    private Integer unsettledAccount;
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
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*outDepotAccount get 方法 */
    public Integer getOutDepotAccount(){
    return outDepotAccount;
    }
    /*outDepotAccount set 方法 */
    public void setOutDepotAccount(Integer  outDepotAccount){
    this.outDepotAccount=outDepotAccount;
    }
    /*inDepotAccount get 方法 */
    public Integer getInDepotAccount(){
    return inDepotAccount;
    }
    /*inDepotAccount set 方法 */
    public void setInDepotAccount(Integer  inDepotAccount){
    this.inDepotAccount=inDepotAccount;
    }
    /*unsettledAccount get 方法 */
    public Integer getUnsettledAccount(){
    return unsettledAccount;
    }
    /*unsettledAccount set 方法 */
    public void setUnsettledAccount(Integer  unsettledAccount){
    this.unsettledAccount=unsettledAccount;
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
