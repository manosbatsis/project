package com.topideal.entity.vo.order;
import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 调拨订单表体
 * @author lian_
 */
public class TransferOrderItemModel extends PageModel implements Serializable{

	 //调出商品编码
    private String outGoodsCode;
    //调拨订单ID
    private Long transferOrderId;
    //调入商品id
    private Long inGoodsId;
    //调入商品货号
    private String inGoodsNo;
    //调出商品名称
    private String outGoodsName;
    //调出商品id
    private Long outGoodsId;
    //调入商品名称
    private String inGoodsName;
    //调入商品编码
    private String inGoodsCode;
    //调拨数量
    private Integer transferNum;
    //创建人
    private Long creater;
    //id
    private Long id;
    //创建时间
    private Timestamp createDate;
    //调出商品货号
    private String outGoodsNo;
    //品牌类型
    private String brandName;
    //合同号
    private String bargainno;
    //采购单价
    private Double price;
    //毛重
    private String grossWeight;
    //净重
    private String netWeight;
    //箱号
    private String contNo;
    //调入条形码
    private String inBarcode;
    //调出条形码
    private String outBarcode;
    //调入数量
    private Integer inTransferNum;
    //调入单位
    private String inUnit;
    //调出单位
    private String outUnit;
    //备注
    private String remark;
    //修改时间
    private Timestamp modifyDate;

    //调入商品标准条码
    private String inCommbarcode;

    //调出商品标准条码
    private String outCommbarcode;

    //总毛重
    private Double grossWeightSum;
    //总净重
    private Double netWeightSum;
    //序号
    private Integer seq;
    //箱数
    private Integer boxNum;
    //托盘号
    private String torrNo;

    /*bargainno get 方法 */
    public String getBargainno(){
        return bargainno;
    }
    /*bargainno set 方法 */
    public void setBargainno(String  bargainno){
        this.bargainno=bargainno;
    }
    /*remark get 方法 */
    public String getRemark(){
        return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
        this.remark=remark;
    }
    /*inGoodsId get 方法 */
    public Long getInGoodsId(){
        return inGoodsId;
    }
    /*inGoodsId set 方法 */
    public void setInGoodsId(Long  inGoodsId){
        this.inGoodsId=inGoodsId;
    }
    /*outGoodsId get 方法 */
    public Long getOutGoodsId(){
        return outGoodsId;
    }
    /*outGoodsId set 方法 */
    public void setOutGoodsId(Long  outGoodsId){
        this.outGoodsId=outGoodsId;
    }
    /*inGoodsCode get 方法 */
    public String getInGoodsCode(){
        return inGoodsCode;
    }
    /*inGoodsCode set 方法 */
    public void setInGoodsCode(String  inGoodsCode){
        this.inGoodsCode=inGoodsCode;
    }
    /*transferNum get 方法 */
    public Integer getTransferNum(){
        return transferNum;
    }
    /*transferNum set 方法 */
    public void setTransferNum(Integer  transferNum){
        this.transferNum=transferNum;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
    /*brandName get 方法 */
    public String getBrandName(){
        return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
        this.brandName=brandName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*outGoodsCode get 方法 */
    public String getOutGoodsCode(){
        return outGoodsCode;
    }
    /*outGoodsCode set 方法 */
    public void setOutGoodsCode(String  outGoodsCode){
        this.outGoodsCode=outGoodsCode;
    }
    /*inUnit get 方法 */
    public String getInUnit(){
        return inUnit;
    }
    /*inUnit set 方法 */
    public void setInUnit(String  inUnit){
        this.inUnit=inUnit;
    }
    /*transferOrderId get 方法 */
    public Long getTransferOrderId(){
        return transferOrderId;
    }
    /*transferOrderId set 方法 */
    public void setTransferOrderId(Long  transferOrderId){
        this.transferOrderId=transferOrderId;
    }
    /*contNo get 方法 */
    public String getContNo(){
        return contNo;
    }
    /*contNo set 方法 */
    public void setContNo(String  contNo){
        this.contNo=contNo;
    }
    /*outBarcode get 方法 */
    public String getOutBarcode(){
        return outBarcode;
    }
    /*outBarcode set 方法 */
    public void setOutBarcode(String  outBarcode){
        this.outBarcode=outBarcode;
    }
    /*inGoodsNo get 方法 */
    public String getInGoodsNo(){
        return inGoodsNo;
    }
    /*inGoodsNo set 方法 */
    public void setInGoodsNo(String  inGoodsNo){
        this.inGoodsNo=inGoodsNo;
    }
    /*outGoodsName get 方法 */
    public String getOutGoodsName(){
        return outGoodsName;
    }
    /*outGoodsName set 方法 */
    public void setOutGoodsName(String  outGoodsName){
        this.outGoodsName=outGoodsName;
    }


    public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}
	public String getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
	}
	/*inBarcode get 方法 */
    public String getInBarcode(){
        return inBarcode;
    }
    /*inBarcode set 方法 */
    public void setInBarcode(String  inBarcode){
        this.inBarcode=inBarcode;
    }
    /*inGoodsName get 方法 */
    public String getInGoodsName(){
        return inGoodsName;
    }
    /*inGoodsName set 方法 */
    public void setInGoodsName(String  inGoodsName){
        this.inGoodsName=inGoodsName;
    }
    /*inTransferNum get 方法 */
    public Integer getInTransferNum(){
        return inTransferNum;
    }
    /*inTransferNum set 方法 */
    public void setInTransferNum(Integer  inTransferNum){
        this.inTransferNum=inTransferNum;
    }
    /*outUnit get 方法 */
    public String getOutUnit(){
        return outUnit;
    }
    /*outUnit set 方法 */
    public void setOutUnit(String  outUnit){
        this.outUnit=outUnit;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*outGoodsNo get 方法 */
    public String getOutGoodsNo(){
        return outGoodsNo;
    }
    /*outGoodsNo set 方法 */
    public void setOutGoodsNo(String  outGoodsNo){
        this.outGoodsNo=outGoodsNo;
    }

    public String getInCommbarcode() {
        return inCommbarcode;
    }

    public void setInCommbarcode(String inCommbarcode) {
        this.inCommbarcode = inCommbarcode;
    }

    public String getOutCommbarcode() {
        return outCommbarcode;
    }

    public void setOutCommbarcode(String outCommbarcode) {
        this.outCommbarcode = outCommbarcode;
    }

    public Double getGrossWeightSum() {
        return grossWeightSum;
    }

    public void setGrossWeightSum(Double grossWeightSum) {
        this.grossWeightSum = grossWeightSum;
    }

    public Double getNetWeightSum() {
        return netWeightSum;
    }

    public void setNetWeightSum(Double netWeightSum) {
        this.netWeightSum = netWeightSum;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }

    public String getTorrNo() {
        return torrNo;
    }

    public void setTorrNo(String torrNo) {
        this.torrNo = torrNo;
    }
}

