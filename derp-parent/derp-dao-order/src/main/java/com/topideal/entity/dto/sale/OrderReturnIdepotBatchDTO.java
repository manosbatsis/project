package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class OrderReturnIdepotBatchDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "主键ID")
    private Long id;
   
	@ApiModelProperty(value = "电商订单退货商品列表id")
    private Long itemId;
  
	@ApiModelProperty(value = "商品ID")
    private Long goodsId;
  
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "商品名称")
    private String goodsName;

	@ApiModelProperty(value = "退货正常品数量")
    private Integer returnNum;

	@ApiModelProperty(value = "退货残品数量")
    private Integer badGoodsNum;
  
	@ApiModelProperty(value = "批次号")
    private String batchNo;
 
	@ApiModelProperty(value = "生产日期")
    private Date productionDate;

	@ApiModelProperty(value = "失效日期")
    private Date overdueDate;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "条形码")
    private String barcode;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*itemId get 方法 */
    public Long getItemId(){
    return itemId;
    }
    /*itemId set 方法 */
    public void setItemId(Long  itemId){
    this.itemId=itemId;
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
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*returnNum get 方法 */
    public Integer getReturnNum(){
    return returnNum;
    }
    /*returnNum set 方法 */
    public void setReturnNum(Integer  returnNum){
    this.returnNum=returnNum;
    }
    /*badGoodsNum get 方法 */
    public Integer getBadGoodsNum(){
    return badGoodsNum;
    }
    /*badGoodsNum set 方法 */
    public void setBadGoodsNum(Integer  badGoodsNum){
    this.badGoodsNum=badGoodsNum;
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
