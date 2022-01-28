package com.topideal.mongo.entity;

public class MerchandiseDepotRelMongo{

    /**
    * 自增长ID
    */
    private Long merchandiseDepotRelId;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 仓库id
    */
    private Long depotId;
    
    

	public Long getMerchandiseDepotRelId() {
		return merchandiseDepotRelId;
	}
	public void setMerchandiseDepotRelId(Long merchandiseDepotRelId) {
		this.merchandiseDepotRelId = merchandiseDepotRelId;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
 


}
