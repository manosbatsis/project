package com.topideal.entity.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 盘点结果详情表
 * @author lian_
 *
 */
public class TakesStockResultItemModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //失效日期
     private Date overdueDate;
     //批次号
     private String batchNo;
     //是否坏品
     private String isDamage;
     //商品id
     private Long goodsId;
     //盘盈数量
     private Integer surplusNum;
     //调整类型
     private String type;
     //生成日期
     private Date productionDate;
     //创建时间
     private Timestamp createDate;
     //盘亏数量
     private Integer deficientNum;
     //id
     private Long id;
     //商品编码
     private String goodsCode;
     //商品名称
     private String goodsName;
     //商品条形码
     private String barcode;
     //盘点结果id
     private Long takesStockResultId;
     //总调整数量
     private Integer adjustTotal;
     //系统数量
     private Integer systemNum;
     //差异数量
     private Integer differencesNum;
     //实盘数量
     private Integer realQty;
     //原因描述
     private String reasonDes;
     //盘点原因代码
     private String reasonCode;
     //理货单位
     private String tallyUnit;
     //修改时间
     private Timestamp modifyDate;

     //标准条码
     private String commbarcode;

    //结算单价
    private BigDecimal settlementPrice;

    //事业部名称
    private String buName;
    // 创建人id
    private Long createUserId;

    // 创建人名称
    private String createUsername;

    /**
     *  事业部id
     */
    private Long buId;

    /**
     * 事业部库位类型ID
     */
    private Long stockLocationTypeId;
    /**
     * 库位类型
     */
    private String stockLocationTypeName;
    
    public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
     
     public String getTallyUnit() {
		return tallyUnit;
	}
	public void setTallyUnit(String tallyUnit) {
		this.tallyUnit = tallyUnit;
	}
	public String getReasonDes() {
		return reasonDes;
	}
	public void setReasonDes(String reasonDes) {
		this.reasonDes = reasonDes;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	/*differencesNum get 方法 */
     public Integer getDifferencesNum(){
         return differencesNum;
     }
     /*differencesNum set 方法 */
     public void setDifferencesNum(Integer  differencesNum){
         this.differencesNum=differencesNum;
     }
     /*systemNum get 方法 */
     public Integer getSystemNum(){
         return systemNum;
     }
     /*systemNum set 方法 */
     public void setSystemNum(Integer  systemNum){
         this.systemNum=systemNum;
     }
     /*realQty get 方法 */
     public Integer getRealQty(){
         return realQty;
     }
     /*realQty set 方法 */
     public void setRealQty(Integer  realQty){
         this.realQty=realQty;
     }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*overdueDate get 方法 */
    public Date getOverdueDate(){
        return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
        this.overdueDate=overdueDate;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
        return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
        this.batchNo=batchNo;
    }
    /*isDamage get 方法 */
    public String getIsDamage(){
        return isDamage;
    }
    /*isDamage set 方法 */
    public void setIsDamage(String  isDamage){
        this.isDamage=isDamage;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*surplusNum get 方法 */
    public Integer getSurplusNum(){
        return surplusNum;
    }
    /*surplusNum set 方法 */
    public void setSurplusNum(Integer  surplusNum){
        this.surplusNum=surplusNum;
    }
    /*type get 方法 */
    public String getType(){
        return type;
    }
    /*type set 方法 */
    public void setType(String  type){
        this.type=type;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
        return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
        this.productionDate=productionDate;
    }
    /*createTime get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createTime set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
    /*deficientNum get 方法 */
    public Integer getDeficientNum(){
        return deficientNum;
    }
    /*deficientNum set 方法 */
    public void setDeficientNum(Integer  deficientNum){
        this.deficientNum=deficientNum;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
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
    /*barcode get 方法 */
    public String getBarcode(){
        return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
        this.barcode=barcode;
    }
    /*takesStockResultId get 方法 */
    public Long getTakesStockResultId(){
        return takesStockResultId;
    }
    /*takesStockResultId set 方法 */
    public void setTakesStockResultId(Long  takesStockResultId){
        this.takesStockResultId=takesStockResultId;
    }
    /*adjustTotal get 方法 */
    public Integer getAdjustTotal(){
        return adjustTotal;
    }
    /*adjustTotal set 方法 */
    public void setAdjustTotal(Integer  adjustTotal){
        this.adjustTotal=adjustTotal;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public BigDecimal getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(BigDecimal settlementPrice) {
        this.settlementPrice = settlementPrice;
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
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUsername() {
		return createUsername;
	}
	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

    public Long getStockLocationTypeId() {
        return stockLocationTypeId;
    }

    public void setStockLocationTypeId(Long stockLocationTypeId) {
        this.stockLocationTypeId = stockLocationTypeId;
    }

    public String getStockLocationTypeName() {
        return stockLocationTypeName;
    }

    public void setStockLocationTypeName(String stockLocationTypeName) {
        this.stockLocationTypeName = stockLocationTypeName;
    }
}

