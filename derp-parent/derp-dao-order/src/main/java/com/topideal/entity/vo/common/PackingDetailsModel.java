package com.topideal.entity.vo.common;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class PackingDetailsModel extends PageModel implements Serializable{

    /**
    * id
    */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
    * 关联订单id
    */
    @ApiModelProperty(value = "关联订单id")
    private Long orderId;
    /**
    * 单据类型 1-申报单 2-调拨订单
    */
    @ApiModelProperty(value = "单据类型 1-申报单 2-调拨订单")
    private String orderType;
    /**
    * 商品货号
    */
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    /**
    * 条形码
    */
    @ApiModelProperty(value = "条形码")
    private String barcode;
    /**
    * 托盘号
    */
    @ApiModelProperty(value = "托盘号")
    private String torrNo;
    /**
    * 箱数
    */
    @ApiModelProperty(value = "箱数")
    private Integer boxNum;
    /**
    * 件数
    */
    @ApiModelProperty(value = "件数")
    private Integer piecesNum;
    /**
    * 柜号
    */
    @ApiModelProperty(value = "柜号")
    private String cabinetNo;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",hidden = true)
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间",hidden = true)
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*orderId get 方法 */
    public Long getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
    this.orderId=orderId;
    }
    /*orderType get 方法 */
    public String getOrderType(){
    return orderType;
    }
    /*orderType set 方法 */
    public void setOrderType(String  orderType){
    this.orderType=orderType;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*torrNo get 方法 */
    public String getTorrNo(){
    return torrNo;
    }
    /*torrNo set 方法 */
    public void setTorrNo(String  torrNo){
    this.torrNo=torrNo;
    }
    /*boxNum get 方法 */
    public Integer getBoxNum(){
    return boxNum;
    }
    /*boxNum set 方法 */
    public void setBoxNum(Integer  boxNum){
    this.boxNum=boxNum;
    }
    /*piecesNum get 方法 */
    public Integer getPiecesNum(){
    return piecesNum;
    }
    /*piecesNum set 方法 */
    public void setPiecesNum(Integer  piecesNum){
    this.piecesNum=piecesNum;
    }
    /*cabinetNo get 方法 */
    public String getCabinetNo(){
    return cabinetNo;
    }
    /*cabinetNo set 方法 */
    public void setCabinetNo(String  cabinetNo){
    this.cabinetNo=cabinetNo;
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






}
