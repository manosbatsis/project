package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 盘点详情表
 * @author lian_
 *
 */
public class TakesStockItemModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //创建时间
     private Timestamp createTime;
     //商品id
     private Long goodsId;
     //盘点id
     private Long takesStockId;
     //id
     private Long id;
     //商品编码
     private String goodsCode;
     //商品名称
     private String goodsName;
     //商品条形码
     private String barcode;

    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*createTime get 方法 */
    public Timestamp getCreateTime(){
        return createTime;
    }
    /*createTime set 方法 */
    public void setCreateTime(Timestamp  createTime){
        this.createTime=createTime;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*takesStockId get 方法 */
    public Long getTakesStockId(){
        return takesStockId;
    }
    /*takesStockId set 方法 */
    public void setTakesStockId(Long  takesStockId){
        this.takesStockId=takesStockId;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*goodsCode get 方法 */
    public String getGoodsCode(){
        return goodsCode;
    }
    /*goodsCode set 方法 */
    public void setGoodsCode(String  goodsCode){
        this.goodsCode=goodsCode;
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






}

