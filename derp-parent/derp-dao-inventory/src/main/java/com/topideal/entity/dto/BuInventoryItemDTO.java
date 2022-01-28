package com.topideal.entity.dto;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class BuInventoryItemDTO extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty(value = "id")
    private Long id;
    /**
    * 事业部库存ID
    */
	@ApiModelProperty(value = "事业部库存ID")
    private Long buInventoryId;
    /**
    * 商品ID
    */
	@ApiModelProperty(value = "商品ID")
    private Long goodsId;
    /**
    * 商品货号
    */
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;
    /**
    * 批次库存量
    */
	@ApiModelProperty(value = "批次库存量")
    private Integer num;
    /**
    * 批次号
    */
	@ApiModelProperty(value = "批次号")
    private String batchNo;
    /**
    * 生产日期
    */
	@ApiModelProperty(value = "生产日期")
    private Date productionDate;
    /**
    * 失效日期
    */
	@ApiModelProperty(value = "失效日期")
    private Date overdueDate;
    /**
    * 效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上
    */
	@ApiModelProperty(value = "效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上")
    private String effectiveInterval;
    /**
    * 首次上架日期
    */
	@ApiModelProperty(value = "首次上架日期")
    private Date firstShelfDate;
    /**
    * 库存类型  0 正常品  1 残次品
    */
	@ApiModelProperty(value = "库存类型  0 正常品  1 残次品")
    private String type;
    /**
    * 创建人
    */
	@ApiModelProperty(value = "创建人")
    private Long creater;
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
     * 商家id
     */
	@ApiModelProperty(value = "商家id")
    private Long merchantId;

    /**
     * 仓库id
     */
	@ApiModelProperty(value = "仓库id")
    private Long depotId;

    /**
     * 月份
     */
	@ApiModelProperty(value = "月份")
    private String month;

    /**
    * 库存类型  0 正常品  1 残次品
    */
	@ApiModelProperty(value = "库存类型  0 正常品  1 残次品")
    private String typeLabel;
    /**
    * 效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上
    */
	@ApiModelProperty(value = "效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上")
    private String effectiveIntervalLabel;
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
    this.effectiveIntervalLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_effectiveIntervalList, effectiveInterval);
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
    this.typeLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, type);
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

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

	public String getEffectiveIntervalLabel() {
		return effectiveIntervalLabel;
	}

	public void setEffectiveIntervalLabel(String effectiveIntervalLabel) {
		this.effectiveIntervalLabel = effectiveIntervalLabel;
	}






}
