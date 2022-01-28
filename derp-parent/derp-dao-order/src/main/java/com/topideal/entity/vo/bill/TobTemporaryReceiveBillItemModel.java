package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class TobTemporaryReceiveBillItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 账单Id
    */
    private Long billId;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 销售单价
    */
    private BigDecimal price;
    /**
    * 上架好品量
    */
    private Integer shelfNum;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 母品牌
     */
    private String parentBrandName;
     /**
     * 母品牌id
     */
    private Long parentBrandId;
     /**
     * 母品牌编码
     */
    private String parentBrandCode;
    /**
     * po号
     */
    private String poNo;
     
    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*billId get 方法 */
    public Long getBillId(){
    return billId;
    }
    /*billId set 方法 */
    public void setBillId(Long  billId){
    this.billId=billId;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
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
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*shelfNum get 方法 */
    public Integer getShelfNum(){
    return shelfNum;
    }
    /*shelfNum set 方法 */
    public void setShelfNum(Integer  shelfNum){
    this.shelfNum=shelfNum;
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
	public String getParentBrandName() {
		return parentBrandName;
	}
	public void setParentBrandName(String parentBrandName) {
		this.parentBrandName = parentBrandName;
	}
	public Long getParentBrandId() {
		return parentBrandId;
	}
	public void setParentBrandId(Long parentBrandId) {
		this.parentBrandId = parentBrandId;
	}
	public String getParentBrandCode() {
		return parentBrandCode;
	}
	public void setParentBrandCode(String parentBrandCode) {
		this.parentBrandCode = parentBrandCode;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}




}
