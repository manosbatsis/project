package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleShelfDTO  extends PageModel implements Serializable {

	@ApiModelProperty(value = "销售订单编号")
    private String code;

	@ApiModelProperty(value = "条码")
    private String barcode;


	@ApiModelProperty(value = "上架数量")
    private Integer shelfNum;


	@ApiModelProperty(value = "残损数量")
    private Integer damagedNum;


	@ApiModelProperty(value = "少货数量")
    private Integer lackNum;


	@ApiModelProperty(value = "po")
    private String poNo;


	@ApiModelProperty(value = "上架时间")
    private Timestamp shelfDate;


	@ApiModelProperty(value = "备注")
    private String remark;


	@ApiModelProperty(value = "ID")
    private Long id;

	@ApiModelProperty(value = "订单/出库单id")
    private Long orderId;

	@ApiModelProperty(value = "1 销售   2 销售出库")
    private String orderType;

	@ApiModelProperty(value = "商品id")
    private Long goodsId;

	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "商品名称")
    private String goodsName;

	@ApiModelProperty(value = "理货单位(00-托盘，01-箱，02-件")
    private String tallyingUnit;

	@ApiModelProperty(value = "销售/出库数量")
    private Integer num;

	@ApiModelProperty(value = "已上架数量")
    private Integer totalShelfNum;

	@ApiModelProperty(value = "已计入残损数量")
    private Integer totalDamagedNum;

	@ApiModelProperty(value = "待上架数量")
    private Integer stayShelfNum;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
	
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "已计少货数量")
    private Integer totalLackNum;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;

	@ApiModelProperty(value = "核销表获取状态（0/空-未获取、1-已获取）")
    private String verifyRelState;

	@ApiModelProperty(value = "操作人")
    private String operator;

	@ApiModelProperty(value = "操作人ID")
    private Long operatorId;

	@ApiModelProperty(value = "上架入库单id")
    private Long saleShelfIdepotId;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "事业部id")
    private Long buId;

	@ApiModelProperty(value = "上架单ID")
    private Long shelfId;

	@ApiModelProperty(value = "标准品牌名")
    private String commBrandParentName;

	@ApiModelProperty(value = "币种")
    private String currency;
   
	@ApiModelProperty(value = "单价")
    private BigDecimal price;
  
	@ApiModelProperty(value = "上架单明细")
    private List<SaleShelfDTO> saleShelfDTOList;

    @ApiModelProperty(value = "销售出库单id")
    private Long saleOutDepotId;

    @ApiModelProperty(value = "上架总金额")
    private BigDecimal shelfAmount;
    
    @ApiModelProperty(value = "销售订单id集合")
    private List<Long> orderIds;
    
    @ApiModelProperty(value = "销售出库单单id集合")
    private List<Long> saleOutOrderIds;

    @ApiModelProperty(value = "销售明细id")
    private Long saleItemId;

    public List<SaleShelfDTO> getSaleShelfDTOList() {
        return saleShelfDTOList;
    }

    public void setSaleShelfDTOList(List<SaleShelfDTO> saleShelfDTOList) {
        this.saleShelfDTOList = saleShelfDTOList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getShelfNum() {
        return shelfNum;
    }

    public void setShelfNum(Integer shelfNum) {
        this.shelfNum = shelfNum;
    }

    public Integer getDamagedNum() {
        return damagedNum;
    }

    public void setDamagedNum(Integer damagedNum) {
        this.damagedNum = damagedNum;
    }

    public Integer getLackNum() {
        return lackNum;
    }

    public void setLackNum(Integer lackNum) {
        this.lackNum = lackNum;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public Timestamp getShelfDate() {
        return shelfDate;
    }

    public void setShelfDate(Timestamp shelfDate) {
        this.shelfDate = shelfDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getTotalShelfNum() {
        return totalShelfNum;
    }

    public void setTotalShelfNum(Integer totalShelfNum) {
        this.totalShelfNum = totalShelfNum;
    }

    public Integer getTotalDamagedNum() {
        return totalDamagedNum;
    }

    public void setTotalDamagedNum(Integer totalDamagedNum) {
        this.totalDamagedNum = totalDamagedNum;
    }

    public Integer getStayShelfNum() {
        return stayShelfNum;
    }

    public void setStayShelfNum(Integer stayShelfNum) {
        this.stayShelfNum = stayShelfNum;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getTotalLackNum() {
        return totalLackNum;
    }

    public void setTotalLackNum(Integer totalLackNum) {
        this.totalLackNum = totalLackNum;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public String getVerifyRelState() {
        return verifyRelState;
    }

    public void setVerifyRelState(String verifyRelState) {
        this.verifyRelState = verifyRelState;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Long getSaleShelfIdepotId() {
        return saleShelfIdepotId;
    }

    public void setSaleShelfIdepotId(Long saleShelfIdepotId) {
        this.saleShelfIdepotId = saleShelfIdepotId;
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

    public Long getShelfId() {
        return shelfId;
    }

    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
    }

    public String getCommBrandParentName() {
        return commBrandParentName;
    }

    public void setCommBrandParentName(String commBrandParentName) {
        this.commBrandParentName = commBrandParentName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getSaleOutDepotId() {
        return saleOutDepotId;
    }

    public void setSaleOutDepotId(Long saleOutDepotId) {
        this.saleOutDepotId = saleOutDepotId;
    }

    public BigDecimal getShelfAmount() {
        return shelfAmount;
    }

    public void setShelfAmount(BigDecimal shelfAmount) {
        this.shelfAmount = shelfAmount;
    }

	public List<Long> getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(List<Long> orderIds) {
		this.orderIds = orderIds;
	}

	public List<Long> getSaleOutOrderIds() {
		return saleOutOrderIds;
	}

	public void setSaleOutOrderIds(List<Long> saleOutOrderIds) {
		this.saleOutOrderIds = saleOutOrderIds;
	}

    public Long getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(Long saleItemId) {
        this.saleItemId = saleItemId;
    }
}
