package com.topideal.entity.dto.purchase;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel
public class PurchaseReturnOdepotItemDTO extends PageModel implements Serializable{

    @ApiModelProperty(value="id")
    private Long id;

    @ApiModelProperty(value="采购退货出库ID")
    private Long odepotOrderId;

    @ApiModelProperty(value="商品id")
    private Long goodsId;

    @ApiModelProperty(value="商品名称")
    private String goodsName;

    @ApiModelProperty(value="商品货号")
    private String goodsNo;

    @ApiModelProperty(value="条形码")
    private String barcode;

    @ApiModelProperty(value="数量")
    private Integer num;

    @ApiModelProperty(value="批次号")
    private String batchNo;

    @ApiModelProperty(value="生产日期")
   private String productionDate;

    @ApiModelProperty(value="失效日期")
    private String overdueDate;

    @ApiModelProperty(value="备注")
    private String remark;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public Long getOdepotOrderId() {
  return odepotOrderId;
 }

 public void setOdepotOrderId(Long odepotOrderId) {
  this.odepotOrderId = odepotOrderId;
 }

 public Long getGoodsId() {
  return goodsId;
 }

 public void setGoodsId(Long goodsId) {
  this.goodsId = goodsId;
 }

 public String getGoodsName() {
  return goodsName;
 }

 public void setGoodsName(String goodsName) {
  this.goodsName = goodsName;
 }

 public String getGoodsNo() {
  return goodsNo;
 }

 public void setGoodsNo(String goodsNo) {
  this.goodsNo = goodsNo;
 }

 public String getBarcode() {
  return barcode;
 }

 public void setBarcode(String barcode) {
  this.barcode = barcode;
 }

 public Integer getNum() {
  return num;
 }

 public void setNum(Integer num) {
  this.num = num;
 }

 public String getBatchNo() {
  return batchNo;
 }

 public void setBatchNo(String batchNo) {
  this.batchNo = batchNo;
 }

 public String getProductionDate() {
  return productionDate;
 }

 public void setProductionDate(String productionDate) {
  this.productionDate = productionDate;
 }

 public String getOverdueDate() {
  return overdueDate;
 }

 public void setOverdueDate(String overdueDate) {
  this.overdueDate = overdueDate;
 }

 public String getRemark() {
  return remark;
 }

 public void setRemark(String remark) {
  this.remark = remark;
 }
}
