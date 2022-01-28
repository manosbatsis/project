package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 运单表
 * @author lian_
 *
 */
public class WayBillModel extends PageModel implements Serializable{

     //物流公司
     private String logisticsName;
     //电商订单ID
     private Long orderId;
     //提单号
     private String blNo;
     //创建人
     private Long creater;
     //物流公司编码
     private String logisticsCode;
     //运单号
     private String wayBillNo;
     //发货时间
     private Timestamp deliverDate;
     //备注
     private String remark;
     //id
     private Long id;
     //类型 20
     private String type;
     //创建时间
     private Timestamp createDate;
     //商家名称
     private String merchantName;
     //商家ID
     private Long merchantId;
     //表体数据
     private List<WayBillItemModel> itemList;
      //修改时间
      private Timestamp modifyDate;

    public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	/*logisticsName get 方法 */
    public String getLogisticsName(){
        return logisticsName;
    }
    /*logisticsName set 方法 */
    public void setLogisticsName(String  logisticsName){
        this.logisticsName=logisticsName;
    }
    /*orderId get 方法 */
    public Long getOrderId(){
        return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
        this.orderId=orderId;
    }
    /*blNo get 方法 */
    public String getBlNo(){
        return blNo;
    }
    /*blNo set 方法 */
    public void setBlNo(String  blNo){
        this.blNo=blNo;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*logisticsCode get 方法 */
    public String getLogisticsCode(){
        return logisticsCode;
    }
    /*logisticsCode set 方法 */
    public void setLogisticsCode(String  logisticsCode){
        this.logisticsCode=logisticsCode;
    }
    /*wayBillNo get 方法 */
    public String getWayBillNo(){
        return wayBillNo;
    }
    /*wayBillNo set 方法 */
    public void setWayBillNo(String  wayBillNo){
        this.wayBillNo=wayBillNo;
    }
    /*deliverDate get 方法 */
    public Timestamp getDeliverDate(){
        return deliverDate;
    }
    /*deliverDate set 方法 */
    public void setDeliverDate(Timestamp  deliverDate){
        this.deliverDate=deliverDate;
    }
    /*remark get 方法 */
    public String getRemark(){
        return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
        this.remark=remark;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*type get 方法 */
    public String getType(){
        return type;
    }
    /*type set 方法 */
    public void setType(String  type){
        this.type=type;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public List<WayBillItemModel> getItemList() {
		return itemList;
	}
	public void setItemList(List<WayBillItemModel> itemList) {
		this.itemList = itemList;
	}






}

