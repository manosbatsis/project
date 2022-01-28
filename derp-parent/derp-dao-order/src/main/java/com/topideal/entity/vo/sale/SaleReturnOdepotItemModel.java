package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
/**
 * 销售退货出库表体
 * @author lian_
 *
 */
public class SaleReturnOdepotItemModel extends PageModel implements Serializable{

     //失效日期
     private Date overdueDate;
     //退货批次
     private String returnBatchNo;
     //退出商品编码
     private String outGoodsCode;
     //退出商品名称
     private String outGoodsName;
     //生产日期
     private Date productionDate;
     //退出商品id
     private Long outGoodsId;
     //退货正常品数量
     private Integer returnNum;
     //销售退货出库ID
     private Long sreturnOdepotId;
     //创建人
     private Long creater;
     //id
     private Long id;
     //退出商品货号
     private String outGoodsNo;
     //创建时间
     private Timestamp createDate;
     private Timestamp modifyDate;
     //条形码
     private String barcode;
     //坏货数量
     private Integer wornNum;
     //过期数量
     private Integer expireNum;

     //po单号
     private String poNo;

     //标准条码
     private String commbarcode;

     public Integer getWornNum() {
		return wornNum;
	}
	public void setWornNum(Integer wornNum) {
		this.wornNum = wornNum;
	}
	public Integer getExpireNum() {
		return expireNum;
	}
	public void setExpireNum(Integer expireNum) {
		this.expireNum = expireNum;
	}
	/*barcode get 方法 */
     public String getBarcode(){
         return barcode;
     }
     /*barcode set 方法 */
     public void setBarcode(String  barcode){
         this.barcode=barcode;
     }
    /*overdueDate get 方法 */
    public Date getOverdueDate(){
        return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
        this.overdueDate=overdueDate;
    }
    /*returnBatchNo get 方法 */
    public String getReturnBatchNo(){
        return returnBatchNo;
    }
    /*returnBatchNo set 方法 */
    public void setReturnBatchNo(String  returnBatchNo){
        this.returnBatchNo=returnBatchNo;
    }
    /*outGoodsCode get 方法 */
    public String getOutGoodsCode(){
        return outGoodsCode;
    }
    /*outGoodsCode set 方法 */
    public void setOutGoodsCode(String  outGoodsCode){
        this.outGoodsCode=outGoodsCode;
    }
    /*outGoodsName get 方法 */
    public String getOutGoodsName(){
        return outGoodsName;
    }
    /*outGoodsName set 方法 */
    public void setOutGoodsName(String  outGoodsName){
        this.outGoodsName=outGoodsName;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
        return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
        this.productionDate=productionDate;
    }
    /*outGoodsId get 方法 */
    public Long getOutGoodsId(){
        return outGoodsId;
    }
    /*outGoodsId set 方法 */
    public void setOutGoodsId(Long  outGoodsId){
        this.outGoodsId=outGoodsId;
    }
    /*returnNum get 方法 */
    public Integer getReturnNum(){
        return returnNum;
    }
    /*returnNum set 方法 */
    public void setReturnNum(Integer  returnNum){
        this.returnNum=returnNum;
    }
    /*sreturnOdepotId get 方法 */
	public Long getSreturnOdepotId() {
		return sreturnOdepotId;
	}
	/*sreturnOdepotId set 方法 */
	public void setSreturnOdepotId(Long sreturnOdepotId) {
		this.sreturnOdepotId = sreturnOdepotId;
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
    /*outGoodsNo get 方法 */
    public String getOutGoodsNo(){
        return outGoodsNo;
    }
    /*outGoodsNo set 方法 */
    public void setOutGoodsNo(String  outGoodsNo){
        this.outGoodsNo=outGoodsNo;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }
}

