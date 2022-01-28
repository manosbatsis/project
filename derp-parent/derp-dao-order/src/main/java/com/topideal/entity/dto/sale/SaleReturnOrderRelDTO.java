package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 销售退货订单商品信息
 * @author wenyan
 *
 */
public class SaleReturnOrderRelDTO extends PageModel implements Serializable{

    //销售订单id
    private Long saleId;
    //商品id
    private Long goodsId;
    //退货数量
    private Integer num;
    //id
    private Long id;
    //销售退订单id
    private Long saleReturnId;
    //创建时间
    private Timestamp createDate;

   /*saleId get 方法 */
   public Long getSaleId(){
       return saleId;
   }
   /*saleId set 方法 */
   public void setSaleId(Long  saleId){
       this.saleId=saleId;
   }
   /*goodsId get 方法 */
   public Long getGoodsId(){
       return goodsId;
   }
   /*goodsId set 方法 */
   public void setGoodsId(Long  goodsId){
       this.goodsId=goodsId;
   }
   /*num get 方法 */
   public Integer getNum(){
       return num;
   }
   /*num set 方法 */
   public void setNum(Integer  num){
       this.num=num;
   }
   /*id get 方法 */
   public Long getId(){
       return id;
   }
   /*id set 方法 */
   public void setId(Long  id){
       this.id=id;
   }
   /*saleReturnId get 方法 */
   public Long getSaleReturnId(){
       return saleReturnId;
   }
   /*saleReturnId set 方法 */
   public void setSaleReturnId(Long  saleReturnId){
       this.saleReturnId=saleReturnId;
   }
   /*createDate get 方法 */
   public Timestamp getCreateDate(){
       return createDate;
   }
   /*createDate set 方法 */
   public void setCreateDate(Timestamp  createDate){
       this.createDate=createDate;
   }
}