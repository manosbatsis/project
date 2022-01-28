package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class TobTempVerifyRelModel extends PageModel implements Serializable{

    /**
    * id主键
    */
    private Long id;
    /**
    * tob暂估明细id
    */
    private Long tobItemId;
    /**
    * tob暂估id
    */
    private Long tobId;
    /**
    * 应收明细id
    */
    private Long receiveItemId;
    /**
    * 应收账单id
    */
    private Long receiveId;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
     * 母品牌id
     */
    private Long parentBrandId;
    /**
    * 费项id
    */
    private Long projectId;
    /**
    * 应收关联业务单号
    */
    private String receiveCode;
    /**
    * 核销金额
    */
    private BigDecimal verifiyAmount;
    /**
    * 类型 0-明细 1-返利
    */
    private String type;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 入账时间
     */
    private Timestamp creditDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*tobItemId get 方法 */
    public Long getTobItemId(){
    return tobItemId;
    }
    /*tobItemId set 方法 */
    public void setTobItemId(Long  tobItemId){
    this.tobItemId=tobItemId;
    }
    /*tobId get 方法 */
    public Long getTobId(){
    return tobId;
    }
    /*tobId set 方法 */
    public void setTobId(Long  tobId){
    this.tobId=tobId;
    }
    /*receiveItemId get 方法 */
    public Long getReceiveItemId(){
    return receiveItemId;
    }
    /*receiveItemId set 方法 */
    public void setReceiveItemId(Long  receiveItemId){
    this.receiveItemId=receiveItemId;
    }
    /*receiveId get 方法 */
    public Long getReceiveId(){
    return receiveId;
    }
    /*receiveId set 方法 */
    public void setReceiveId(Long  receiveId){
    this.receiveId=receiveId;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*projectId get 方法 */
    public Long getProjectId(){
    return projectId;
    }
    /*projectId set 方法 */
    public void setProjectId(Long  projectId){
    this.projectId=projectId;
    }
    /*receiveCode get 方法 */
    public String getReceiveCode(){
    return receiveCode;
    }
    /*receiveCode set 方法 */
    public void setReceiveCode(String  receiveCode){
    this.receiveCode=receiveCode;
    }
    /*verifiyAmount get 方法 */
    public BigDecimal getVerifiyAmount(){
    return verifiyAmount;
    }
    /*verifiyAmount set 方法 */
    public void setVerifiyAmount(BigDecimal  verifiyAmount){
    this.verifiyAmount=verifiyAmount;
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
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public Long getParentBrandId() {
        return parentBrandId;
    }

    public void setParentBrandId(Long parentBrandId) {
        this.parentBrandId = parentBrandId;
    }

    public Timestamp getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(Timestamp creditDate) {
        this.creditDate = creditDate;
    }
}
