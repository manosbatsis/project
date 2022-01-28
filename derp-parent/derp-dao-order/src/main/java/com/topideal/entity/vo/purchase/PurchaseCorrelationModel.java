package com.topideal.entity.vo.purchase;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class PurchaseCorrelationModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //采购订单编码
     private String purchaseCode;
     //数量
     private Integer num;
     //创建人
     private Long creater;
     //id
     private Long id;
     //入库单编码
     private String warehouseCode;
     //创建时间
     private Timestamp createDate;
     //修改时间
     private Timestamp modifyDate;

    /**
     * 事业部名称
     */
    private String buName;
    /**
     *  事业部id
     */
    private Long buId;

    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*purchaseCode get 方法 */
    public String getPurchaseCode(){
        return purchaseCode;
    }
    /*purchaseCode set 方法 */
    public void setPurchaseCode(String  purchaseCode){
        this.purchaseCode=purchaseCode;
    }
    /*num get 方法 */
    public Integer getNum(){
        return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
        this.num=num;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*warehouseCode get 方法 */
    public String getWarehouseCode(){
        return warehouseCode;
    }
    /*warehouseCode set 方法 */
    public void setWarehouseCode(String  warehouseCode){
        this.warehouseCode=warehouseCode;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }
}

