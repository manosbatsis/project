package com.topideal.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.taobao.api.response.WlbWmsSkuGetResponse;
import com.topideal.api.sync.sy01.SyncGoodsRequest;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;

import net.sf.json.JSONObject;

public interface SynsCLGoodsInfoService {

	/**
	 * 同步菜鸟商品信息并同步给主数据
	 * @param merchantId 
	 * @param itemRsp 
	 * @return
	 * @throws Exception 
	 */
	Map<String, Object> synsCLGoodsInfo(WlbWmsSkuGetResponse itemRsp, MerchantShopRelModel merchantShopRelModel,MerchantInfoModel merchantInfoModel,Long sourceGoodsId) throws Exception;

	/**
	 * 获取需要同步菜鸟商品店铺
	 * @return
	 * @throws SQLException 
	 */
	List<MerchantShopRelModel> getSycnMerchantShop() throws SQLException;

	/**
	 * 查询是否存在商品
	 * @param id
	 * @param string
	 * @return
	 * @throws SQLException 
	 */
	boolean checkMerchandiseExsit(Long merchantId, String goodsNo) throws SQLException;

	void synsCLGoodsCollectionSucc(WlbWmsSkuGetResponse itemRsp) throws Exception;

	void synsCLGoodsCollectionError(String keryword, String errorMsg, JSONObject requestMessage) throws Exception;


}
