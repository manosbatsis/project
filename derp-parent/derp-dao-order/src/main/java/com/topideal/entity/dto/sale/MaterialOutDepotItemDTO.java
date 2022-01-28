package com.topideal.entity.dto.sale;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@ApiModel
public class MaterialOutDepotItemDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "领料出库ID")
    private Long materialOutDepotId;

	@ApiModelProperty(value = "商品id")
    private Long goodsId;

	@ApiModelProperty(value = "商品编码")
    private String goodsCode;

	@ApiModelProperty(value = "商品名称")
    private String goodsName;

	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "正常品数量")
    private Integer transferNum;

	@ApiModelProperty(value = "坏品数量")
    private Integer wornNum;

	@ApiModelProperty(value = "调拨批次")
    private String transferBatchNo;

	@ApiModelProperty(value = "生产日期")
    private Date productionDate;

	@ApiModelProperty(value = "失效日期")
    private Date overdueDate;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "创建人")
    private Long creater;

	@ApiModelProperty(value = "条形码")
    private String barcode;

	@ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件")
    private String tallyingUnit;
	@ApiModelProperty(value = "理货单位(中文)")
	private String tallyingUnitLabel;

	@ApiModelProperty(value = "领取数量")
    private Integer materialNum;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;
    /*id get 方法 */
    public Long getId(){
    	return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    	this.id=id;
    }
    /*materialOutDepotId get 方法 */
    public Long getMaterialOutDepotId(){
    	return materialOutDepotId;
    }
    /*materialOutDepotId set 方法 */
    public void setMaterialOutDepotId(Long  materialOutDepotId){
    	this.materialOutDepotId=materialOutDepotId;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    	return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    	this.goodsId=goodsId;
    }
    /*goodsCode get 方法 */
    public String getGoodsCode(){
    	return goodsCode;
    }
    /*goodsCode set 方法 */
    public void setGoodsCode(String  goodsCode){
    	this.goodsCode=goodsCode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    	return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    	}
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    	this.goodsNo=goodsNo;
    }
    /*transferNum get 方法 */
    public Integer getTransferNum(){
    	return transferNum;
    }
    /*transferNum set 方法 */
    public void setTransferNum(Integer  transferNum){
    	this.transferNum=transferNum;
    }
    /*wornNum get 方法 */
    public Integer getWornNum(){
    	return wornNum;
    }
    /*wornNum set 方法 */
    public void setWornNum(Integer  wornNum){
    	this.wornNum=wornNum;
    }
    /*transferBatchNo get 方法 */
    public String getTransferBatchNo(){
    	return transferBatchNo;
    }
    /*transferBatchNo set 方法 */
    public void setTransferBatchNo(String  transferBatchNo){
    	this.transferBatchNo=transferBatchNo;
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
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    	return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    	this.createDate=createDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
    	return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    	this.creater=creater;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    	return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    	this.barcode=barcode;
    }
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    	return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    	this.tallyingUnit=tallyingUnit;
    	this.tallyingUnitLabel = DERP_ORDER.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);

    }
    /*materialNum get 方法 */
    public Integer getMaterialNum(){
    	return materialNum;
    }
    /*materialNum set 方法 */
    public void setMaterialNum(Integer  materialNum){
    	this.materialNum=materialNum;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    	return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    	this.modifyDate=modifyDate;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    	this.commbarcode=commbarcode;
    }
	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}
}
