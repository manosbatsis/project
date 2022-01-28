package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class SaleCreditBillOrderItemModel extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "赊销收款单ID")
    private Long creditBillOrderId;

	@ApiModelProperty(value = "商品ID")
    private Long goodsId;

	@ApiModelProperty(value = "商品编码")
    private String goodsCode;

	@ApiModelProperty(value = "商品名称")
    private String goodsName;

	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;

	@ApiModelProperty(value = "条形码")
    private String barcode;

	@ApiModelProperty(value = "赎回数量")
    private Integer num;

	@ApiModelProperty(value = "本金")
    private BigDecimal principalAmount;

	@ApiModelProperty(value = "保证金")
    private BigDecimal marginAmount;

	@ApiModelProperty(value = "资金占用费")
    private BigDecimal occupationAmount;

	@ApiModelProperty(value = "代理费")
    private BigDecimal agencyAmount;

	@ApiModelProperty(value = "滞纳金")
    private BigDecimal delayAmount;
 
	@ApiModelProperty(value = "应收款金额")
    private BigDecimal receivableAmount;
 
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "滞纳金减免金额")
    private BigDecimal discountDelayAmount;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*creditBillOrderId get 方法 */
    public Long getCreditBillOrderId(){
    return creditBillOrderId;
    }
    /*creditBillOrderId set 方法 */
    public void setCreditBillOrderId(Long  creditBillOrderId){
    this.creditBillOrderId=creditBillOrderId;
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
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*principalAmount get 方法 */
    public BigDecimal getPrincipalAmount(){
    return principalAmount;
    }
    /*principalAmount set 方法 */
    public void setPrincipalAmount(BigDecimal  principalAmount){
    this.principalAmount=principalAmount;
    }
    /*marginAmount get 方法 */
    public BigDecimal getMarginAmount(){
    return marginAmount;
    }
    /*marginAmount set 方法 */
    public void setMarginAmount(BigDecimal  marginAmount){
    this.marginAmount=marginAmount;
    }
    /*occupationAmount get 方法 */
    public BigDecimal getOccupationAmount(){
    return occupationAmount;
    }
    /*occupationAmount set 方法 */
    public void setOccupationAmount(BigDecimal  occupationAmount){
    this.occupationAmount=occupationAmount;
    }
    /*agencyAmount get 方法 */
    public BigDecimal getAgencyAmount(){
    return agencyAmount;
    }
    /*agencyAmount set 方法 */
    public void setAgencyAmount(BigDecimal  agencyAmount){
    this.agencyAmount=agencyAmount;
    }
    /*delayAmount get 方法 */
    public BigDecimal getDelayAmount(){
    return delayAmount;
    }
    /*delayAmount set 方法 */
    public void setDelayAmount(BigDecimal  delayAmount){
    this.delayAmount=delayAmount;
    }
    /*receivableAmount get 方法 */
    public BigDecimal getReceivableAmount(){
    return receivableAmount;
    }
    /*receivableAmount set 方法 */
    public void setReceivableAmount(BigDecimal  receivableAmount){
    this.receivableAmount=receivableAmount;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }

    public BigDecimal getDiscountDelayAmount() {
        return discountDelayAmount;
    }

    public void setDiscountDelayAmount(BigDecimal discountDelayAmount) {
        this.discountDelayAmount = discountDelayAmount;
    }
}
