package com.topideal.entity.dto.sale;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class SaleShelfIdepotItemDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "上架入库单ID")
    private Long sshelfIdepotId;

	@ApiModelProperty(value = "入库商品id")
    private Long inGoodsId;

	@ApiModelProperty(value = "入库商品编码")
    private String inGoodsCode;

	@ApiModelProperty(value = "入库商品货号")
    private String inGoodsNo;

	@ApiModelProperty(value = "入库商品名称")
    private String inGoodsName;

	@ApiModelProperty(value = "条形码")
    private String barcode;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;

	@ApiModelProperty(value = "好品数量")
    private Integer normalNum;

	@ApiModelProperty(value = "坏品数量")
    private Integer damagedNum;

	@ApiModelProperty(value = "入库批次号")
    private String batchNo;

	@ApiModelProperty(value = "生产日期")
    private Date productionDate;

	@ApiModelProperty(value = "失效日期")
    private Date overdueDate;

	@ApiModelProperty(value = "创建人")
    private Long creater;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "调入商品原货号")
    private String inOriginalGoodsNo;

	@ApiModelProperty(value = "调入商品原货号id")
    private Long inOriginalGoodsId;

    @ApiModelProperty(value = "销售明细id")
    private Long saleItemId;

    @ApiModelProperty(value = "销售单价")
    private BigDecimal price;
    /**
     * 销售出库单表体ID
     */
    @ApiModelProperty(value = "销售出库单表体ID")
    private Long saleOutDepotItemId;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*sshelfIdepotId get 方法 */
    public Long getSshelfIdepotId(){
    return sshelfIdepotId;
    }
    /*sshelfIdepotId set 方法 */
    public void setSshelfIdepotId(Long  sshelfIdepotId){
    this.sshelfIdepotId=sshelfIdepotId;
    }
    /*inGoodsId get 方法 */
    public Long getInGoodsId(){
    return inGoodsId;
    }
    /*inGoodsId set 方法 */
    public void setInGoodsId(Long  inGoodsId){
    this.inGoodsId=inGoodsId;
    }
    /*inGoodsCode get 方法 */
    public String getInGoodsCode(){
    return inGoodsCode;
    }
    /*inGoodsCode set 方法 */
    public void setInGoodsCode(String  inGoodsCode){
    this.inGoodsCode=inGoodsCode;
    }
    /*inGoodsNo get 方法 */
    public String getInGoodsNo(){
    return inGoodsNo;
    }
    /*inGoodsNo set 方法 */
    public void setInGoodsNo(String  inGoodsNo){
    this.inGoodsNo=inGoodsNo;
    }
    /*inGoodsName get 方法 */
    public String getInGoodsName(){
    return inGoodsName;
    }
    /*inGoodsName set 方法 */
    public void setInGoodsName(String  inGoodsName){
    this.inGoodsName=inGoodsName;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*normalNum get 方法 */
    public Integer getNormalNum(){
    return normalNum;
    }
    /*normalNum set 方法 */
    public void setNormalNum(Integer  normalNum){
    this.normalNum=normalNum;
    }
    /*damagedNum get 方法 */
    public Integer getDamagedNum(){
    return damagedNum;
    }
    /*damagedNum set 方法 */
    public void setDamagedNum(Integer  damagedNum){
    this.damagedNum=damagedNum;
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

    public String getInOriginalGoodsNo() {
        return inOriginalGoodsNo;
    }

    public void setInOriginalGoodsNo(String inOriginalGoodsNo) {
        this.inOriginalGoodsNo = inOriginalGoodsNo;
    }

    public Long getInOriginalGoodsId() {
        return inOriginalGoodsId;
    }

    public void setInOriginalGoodsId(Long inOriginalGoodsId) {
        this.inOriginalGoodsId = inOriginalGoodsId;
    }

    public Long getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(Long saleItemId) {
        this.saleItemId = saleItemId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getSaleOutDepotItemId() {
        return saleOutDepotItemId;
    }

    public void setSaleOutDepotItemId(Long saleOutDepotItemId) {
        this.saleOutDepotItemId = saleOutDepotItemId;
    }
}
