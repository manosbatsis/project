package com.topideal.mongo.entity;

public class MerchantDepotBuRelMongo {

    /**
    * 公司id
    */
    private Long merchantId;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 仓库ID
    */
    private Long depotId;

    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }






}
