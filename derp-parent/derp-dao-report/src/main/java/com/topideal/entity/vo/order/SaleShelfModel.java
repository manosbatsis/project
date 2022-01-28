package com.topideal.entity.vo.order;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 销售上架信息表
 * @author lian_
 *
 */
public class SaleShelfModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 订单/出库单id
    */
    private Long orderId;
    /**
    * 1 销售   2 销售出库
    */
    private String orderType;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品条码
    */
    private String barcode;
    /**
    * 理货单位(00-托盘，01-箱，02-件
    */
    private String tallyingUnit;
    /**
    * 销售/出库数量
    */
    private Integer num;
    /**
    * 已上架数量
    */
    private Integer totalShelfNum;
    /**
    * 已计入残损数量
    */
    private Integer totalDamagedNum;
    /**
    * 待上架数量
    */
    private Integer stayShelfNum;
    /**
    * 本次上架数量
    */
    private Integer shelfNum;
    /**
    *  本次残损数量
    */
    private Integer damagedNum;
    /**
    *  po单号
    */
    private String poNo;
    /**
    * 备注
    */
    private String remark;
    /**
    * 上架时间
    */
    private Timestamp shelfDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;

    /**
     * 修改时间
     */
    private Timestamp modifyDate;

    /**
     * 少货数量
     */
    private Integer lackNum;

    /**
     * 已计少货数量
     */
    private Integer totalLackNum;

    //标准条码
    private String commbarcode;

    //核销表获取状态（0/空-未获取、1-已获取）
    private String verifyRelState;

    //操作人
    private String operator;
    //操作人ID
    private Long operatorId;

    //上架入库单id
    private Long saleShelfIdepotId;

    /**
     * 事业部名称
     */
    private String buName;
    /**
     *  事业部id
     */
    private Long buId;
    /**
     *  上架单ID
     */
    private Long shelfId;
    /**
     * 销售出库单id
     */
    private Long saleOutDepotId;
    /**
     * 销售明细id
     */
    private Long saleItemId;

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
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
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
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    this.tallyingUnit=tallyingUnit;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*totalShelfNum get 方法 */
    public Integer getTotalShelfNum(){
    return totalShelfNum;
    }
    /*totalShelfNum set 方法 */
    public void setTotalShelfNum(Integer  totalShelfNum){
    this.totalShelfNum=totalShelfNum;
    }
    /*totalDamagedNum get 方法 */
    public Integer getTotalDamagedNum(){
    return totalDamagedNum;
    }
    /*totalDamagedNum set 方法 */
    public void setTotalDamagedNum(Integer  totalDamagedNum){
    this.totalDamagedNum=totalDamagedNum;
    }
    /*stayShelfNum get 方法 */
    public Integer getStayShelfNum(){
    return stayShelfNum;
    }
    /*stayShelfNum set 方法 */
    public void setStayShelfNum(Integer  stayShelfNum){
    this.stayShelfNum=stayShelfNum;
    }
    /*shelfNum get 方法 */
    public Integer getShelfNum(){
    return shelfNum;
    }
    /*shelfNum set 方法 */
    public void setShelfNum(Integer  shelfNum){
    this.shelfNum=shelfNum;
    }
    /*damagedNum get 方法 */
    public Integer getDamagedNum(){
    return damagedNum;
    }
    /*damagedNum set 方法 */
    public void setDamagedNum(Integer  damagedNum){
    this.damagedNum=damagedNum;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*shelfDate get 方法 */
    public Timestamp getShelfDate(){
    return shelfDate;
    }
    /*shelfDate set 方法 */
    public void setShelfDate(Timestamp  shelfDate){
    this.shelfDate=shelfDate;
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

    public Integer getLackNum() {
        return lackNum;
    }

    public void setLackNum(Integer lackNum) {
        this.lackNum = lackNum;
    }

    public Integer getTotalLackNum() {
        return totalLackNum;
    }

    public void setTotalLackNum(Integer totalLackNum) {
        this.totalLackNum = totalLackNum;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public String getVerifyRelState() {
        return verifyRelState;
    }

    public void setVerifyRelState(String verifyRelState) {
        this.verifyRelState = verifyRelState;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Long getSaleShelfIdepotId() {
        return saleShelfIdepotId;
    }

    public void setSaleShelfIdepotId(Long saleShelfIdepotId) {
        this.saleShelfIdepotId = saleShelfIdepotId;
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

    public Long getShelfId() {
        return shelfId;
    }

    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
    }

    public Long getSaleOutDepotId() {
        return saleOutDepotId;
    }

    public void setSaleOutDepotId(Long saleOutDepotId) {
        this.saleOutDepotId = saleOutDepotId;
    }

    public Long getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(Long saleItemId) {
        this.saleItemId = saleItemId;
    }
}
