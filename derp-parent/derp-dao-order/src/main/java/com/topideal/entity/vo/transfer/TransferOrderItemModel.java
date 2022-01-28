package com.topideal.entity.vo.transfer;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 调拨订单表体
 * @author lian_
 *
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
    private Double grossWeight;
    //净重
    private Double netWeight;
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
    
    //拓展字段
  	// 单位
//  	private String outUnit;
  	// 工厂编码
  	private String outFactoryNo;

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
  	
  	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	private String zcode;//自编码

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getInTransferNum() {
		return inTransferNum;
	}
	public void setInTransferNum(Integer inTransferNum) {
		this.inTransferNum = inTransferNum;
	}
	public String getInBarcode() {
		return inBarcode;
	}
	public void setInBarcode(String inBarcode) {
		this.inBarcode = inBarcode;
	}
	public String getInUnit() {
		return inUnit;
	}
	public void setInUnit(String inUnit) {
		this.inUnit = inUnit;
	}
	
	/*contNo get 方法 */
    public String getContNo(){
        return contNo;
    }
    /*contNo set 方法 */
    public void setContNo(String  contNo){
        this.contNo=contNo;
    }
    /*grossWeight get 方法 */
    public Double getGrossWeight(){
        return grossWeight;
    }
    /*grossWeight set 方法 */
    public void setGrossWeight(Double  grossWeight){
        this.grossWeight=grossWeight;
    }
    /*netWeight get 方法 */
    public Double getNetWeight(){
        return netWeight;
    }
    /*netWeight set 方法 */
    public void setNetWeight(Double  netWeight){
        this.netWeight=netWeight;
    }
    /*price get 方法 */
    public Double getPrice(){
        return price;
    }
    /*price set 方法 */
    public void setPrice(Double  price){
        this.price=price;
    }
  	/*brandName get 方法 */
    public String getBrandName(){
        return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
        this.brandName=brandName;
    }
    /*bargainno get 方法 */
    public String getBargainno(){
        return bargainno;
    }
    /*bargainno set 方法 */
    public void setBargainno(String  bargainno){
        this.bargainno=bargainno;
    }
   /*outGoodsCode get 方法 */
   public String getOutGoodsCode(){
       return outGoodsCode;
   }
   /*outGoodsCode set 方法 */
   public void setOutGoodsCode(String  outGoodsCode){
       this.outGoodsCode=outGoodsCode;
   }
   /*transferOrderId get 方法 */
   public Long getTransferOrderId(){
       return transferOrderId;
   }
   /*transferOrderId set 方法 */
   public void setTransferOrderId(Long  transferOrderId){
       this.transferOrderId=transferOrderId;
   }
   /*inGoodsId get 方法 */
   public Long getInGoodsId(){
       return inGoodsId;
   }
   /*inGoodsId set 方法 */
   public void setInGoodsId(Long  inGoodsId){
       this.inGoodsId=inGoodsId;
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
   /*outGoodsId get 方法 */
   public Long getOutGoodsId(){
       return outGoodsId;
   }
   /*outGoodsId set 方法 */
   public void setOutGoodsId(Long  outGoodsId){
       this.outGoodsId=outGoodsId;
   }
   /*inGoodsName get 方法 */
   public String getInGoodsName(){
       return inGoodsName;
   }
   /*inGoodsName set 方法 */
   public void setInGoodsName(String  inGoodsName){
       this.inGoodsName=inGoodsName;
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
   /*creater get 方法 */
   public Long getCreater(){
       return creater;
   }
   /*creater set 方法 */
   public void setCreater(Long  creater){
       this.creater=creater;
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
   /*outGoodsNo get 方法 */
   public String getOutGoodsNo(){
       return outGoodsNo;
   }
   /*outGoodsNo set 方法 */
   public void setOutGoodsNo(String  outGoodsNo){
       this.outGoodsNo=outGoodsNo;
   }
	public String getOutUnit() {
		return outUnit;
	}
	public void setOutUnit(String outUnit) {
		this.outUnit = outUnit;
	}
	public String getOutBarcode() {
		return outBarcode;
	}
	public void setOutBarcode(String outBarcode) {
		this.outBarcode = outBarcode;
	}
	public String getOutFactoryNo() {
		return outFactoryNo;
	}
	public void setOutFactoryNo(String outFactoryNo) {
		this.outFactoryNo = outFactoryNo;
	}
	public String getZcode() {
		return zcode;
	}
	public void setZcode(String zcode) {
		this.zcode = zcode;
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

