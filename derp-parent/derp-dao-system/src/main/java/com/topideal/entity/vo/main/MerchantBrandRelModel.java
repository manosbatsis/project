package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商家品牌关联表
 * @author lian_
 *
 */
public class MerchantBrandRelModel extends PageModel implements Serializable{

     //id
     private Long id;
     //商家ID
     private Long merchantId;
     //品牌id
     private Long brandId;
     //创建日期
     private Timestamp createDate;

    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*brandId get 方法 */
    public Long getBrandId(){
        return brandId;
    }
    /*brandId set 方法 */
    public void setBrandId(Long  brandId){
        this.brandId=brandId;
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

