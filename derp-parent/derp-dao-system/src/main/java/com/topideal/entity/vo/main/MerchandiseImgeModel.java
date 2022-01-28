package com.topideal.entity.vo.main;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class MerchandiseImgeModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 商品ID
    */
    private Long goodId;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 图片链接
    */
    private String url;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 修改人
    */
    private Long modifier;
    /**
    * 创建人
    */
    private Long creater;

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
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*goodId get 方法 */
    public Long getGoodId(){
    return goodId;
    }
    /*goodId set 方法 */
    public void setGoodId(Long  goodId){
    this.goodId=goodId;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*url get 方法 */
    public String getUrl(){
    return url;
    }
    /*url set 方法 */
    public void setUrl(String  url){
    this.url=url;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }






}
