package com.topideal.entity.vo.inventory;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class BuInventoryItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 事业部库存ID
    */
    private Long buInventoryId;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 批次库存量
    */
    private Integer num;
    /**
    * 批次号
    */
    private String batchNo;
    /**
    * 生产日期
    */
    private Date productionDate;
    /**
    * 失效日期
    */
    private Date overdueDate;
    /**
    * 效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上
    */
    private String effectiveInterval;
    /**
    * 首次上架日期
    */
    private Date firstShelfDate;
    /**
    * 库存类型  0 正常品  1 残次品
    */
    private String type;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 商家id
     */
    private Long merchantId;

    /**
     * 仓库id
     */
    private Long depotId;

    /**
     * 月份
     */
    private String month;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*buInventoryId get 方法 */
    public Long getBuInventoryId(){
    return buInventoryId;
    }
    /*buInventoryId set 方法 */
    public void setBuInventoryId(Long  buInventoryId){
    this.buInventoryId=buInventoryId;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
    return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
    this.batchNo=batchNo;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
    return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
    this.productionDate=productionDate;
    }
    /*overdueDate get 方法 */
    public Date getOverdueDate(){
    return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
    this.overdueDate=overdueDate;
    }
    /*effectiveInterval get 方法 */
    public String getEffectiveInterval(){
    return effectiveInterval;
    }
    /*effectiveInterval set 方法 */
    public void setEffectiveInterval(String  effectiveInterval){
    this.effectiveInterval=effectiveInterval;
    }
    /*firstShelfDate get 方法 */
    public Date getFirstShelfDate(){
    return firstShelfDate;
    }
    /*firstShelfDate set 方法 */
    public void setFirstShelfDate(Date  firstShelfDate){
    this.firstShelfDate=firstShelfDate;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
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
