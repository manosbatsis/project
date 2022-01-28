package com.topideal.entity.vo.reporting;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class BuSplitBillAbnormalDetailModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 结算单id
    */
    private Long billId;
    /**
    * 结算单号
    */
    private String billCode;
    /**
    * 订单号
    */
    private String orderCode;
    /**
    * 项目名称
    */
    private String projectName;
    /**
    * 费用项目小类
    */
    private String childProjectName;
    /**
    * 是否红冲 1-是 0-否
    */
    private String hcStatus;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 待分摊金额
    */
    private BigDecimal allocatingPrice;
    /**
    * 异常原因
    */
    private String abnormalReason;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 仓库编码
     */
    private String depotCode;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*billId get 方法 */
    public Long getBillId(){
    return billId;
    }
    /*billId set 方法 */
    public void setBillId(Long  billId){
    this.billId=billId;
    }
    /*billCode get 方法 */
    public String getBillCode(){
    return billCode;
    }
    /*billCode set 方法 */
    public void setBillCode(String  billCode){
    this.billCode=billCode;
    }
    /*orderCode get 方法 */
    public String getOrderCode(){
    return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
    this.orderCode=orderCode;
    }
    /*projectName get 方法 */
    public String getProjectName(){
    return projectName;
    }
    /*projectName set 方法 */
    public void setProjectName(String  projectName){
    this.projectName=projectName;
    }
    /*childProjectName get 方法 */
    public String getChildProjectName(){
    return childProjectName;
    }
    /*childProjectName set 方法 */
    public void setChildProjectName(String  childProjectName){
    this.childProjectName=childProjectName;
    }
    /*hcStatus get 方法 */
    public String getHcStatus(){
    return hcStatus;
    }
    /*hcStatus set 方法 */
    public void setHcStatus(String  hcStatus){
    this.hcStatus=hcStatus;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*allocatingPrice get 方法 */
    public BigDecimal getAllocatingPrice(){
    return allocatingPrice;
    }
    /*allocatingPrice set 方法 */
    public void setAllocatingPrice(BigDecimal  allocatingPrice){
    this.allocatingPrice=allocatingPrice;
    }
    /*abnormalReason get 方法 */
    public String getAbnormalReason(){
    return abnormalReason;
    }
    /*abnormalReason set 方法 */
    public void setAbnormalReason(String  abnormalReason){
    this.abnormalReason=abnormalReason;
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

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }
}
