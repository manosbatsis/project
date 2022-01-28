package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import com.topideal.common.system.ibatis.PageModel;
/**
 * 月结账单
 * @author lian_
 *
 */
public class MonthlyAccountModel extends PageModel implements Serializable{

     //仓库名称
     private String depotName;
     //期末时间
     private Timestamp endDate;
     //仓库id
     private Long depotId;
     //结转时间
     private Timestamp settlementDate;
     //商家名称
     private String merchantName;
     //商家ID
     private Long merchantId;
     //期初时间
     private Timestamp firstDate;
     //创建人
     private Long creater;
     //结转月份
     private String settlementMonth;
     //id
     private Long id;
     //状态
     private String state;
     //创建时间（结算时间）
     private Timestamp createDate;
     //仓库类型标识
     private String  depotType;
     //来源订单编号
     private String orderNo;
     //卓志编码
     private String topidealCode;
     //结算人
     private String planName;
     //新增属性  2018-09-17
     private String snapshotDate;
     //修改时间
     private Timestamp modifyDate;
     
     public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	/*planName get 方法 */
     public String getPlanName(){
         return planName;
     }
     /*planName set 方法 */
     public void setPlanName(String  planName){
         this.planName=planName;
     }

     /*topidealCode get 方法 */
     public String getTopidealCode(){
         return topidealCode;
     }
     /*topidealCode set 方法 */
     public void setTopidealCode(String  topidealCode){
         this.topidealCode=topidealCode;
     }
     /*orderNo get 方法 */
     public String getOrderNo(){
         return orderNo;
     }
     /*orderNo set 方法 */
     public void setOrderNo(String  orderNo){
         this.orderNo=orderNo;
     }
    /*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*endDate get 方法 */
    public Timestamp getEndDate(){
        return endDate;
    }
    /*endDate set 方法 */
    public void setEndDate(Timestamp  endDate){
        this.endDate=endDate;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*settlementDate get 方法 */
    public Timestamp getSettlementDate(){
        return settlementDate;
    }
    /*settlementDate set 方法 */
    public void setSettlementDate(Timestamp  settlementDate){
        this.settlementDate=settlementDate;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*firstDate get 方法 */
    public Timestamp getFirstDate(){
        return firstDate;
    }
    /*firstDate set 方法 */
    public void setFirstDate(Timestamp  firstDate){
        this.firstDate=firstDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*settlementMonth get 方法 */
    public String getSettlementMonth(){
        return settlementMonth;
    }
    /*settlementMonth set 方法 */
    public void setSettlementMonth(String  settlementMonth){
        this.settlementMonth=settlementMonth;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*state get 方法 */
    public String getState(){
        return state;
    }
    /*state set 方法 */
    public void setState(String  state){
        this.state=state;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public String getDepotType() {
		return depotType;
	}
	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}
	public String getSnapshotDate() {
		return snapshotDate;
	}
	public void setSnapshotDate(String snapshotDate) {
		this.snapshotDate = snapshotDate;
	}

    




}

