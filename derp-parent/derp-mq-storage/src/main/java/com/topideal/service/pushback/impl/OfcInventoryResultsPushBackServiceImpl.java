package com.topideal.service.pushback.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.TakesStockResultItemDao;
import com.topideal.dao.TakesStockResultsDao;
import com.topideal.entity.vo.TakesStockResultItemModel;
import com.topideal.entity.vo.TakesStockResultsModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.pushback.OfcInventoryResultsPushBackService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ofc盘点结果库存加减成功回推 
 * @author 杨创 
 * 2019/02/26
 */
@Service
public class
OfcInventoryResultsPushBackServiceImpl implements OfcInventoryResultsPushBackService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OfcInventoryResultsPushBackServiceImpl.class);

	@Autowired
	private TakesStockResultsDao takesStockResultsDao;// 盘点结果表
	@Autowired
	private TakesStockResultItemDao takesStockResultItemDao;// 盘点结果商品表
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;// 商家mongodb

	/**
	 * ofc盘点结果库存加减成功回推 
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201213901, model = DERP_LOG_POINT.POINT_13201213901_Label,keyword ="code")
	public boolean updateOfcInventoryResultsPushBackInfo(String json, String keys, String topics, String tags)
			throws Exception {
		Thread.sleep(3000);// 睡眠3000毫秒
		/**
		 * 说明 ofc盘点结果回推会存在和盘点指令对不上的单 也要进行保存
		 */
		JSONObject jsonData = JSONObject.fromObject(json);		
		@SuppressWarnings("rawtypes")
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");
		
		// 根据盘点指令单号和商家查询盘点结果表
		TakesStockResultsModel takesStockResultsModel = new TakesStockResultsModel();
		takesStockResultsModel.setSourceCode(code);// 来源单据号
		takesStockResultsModel = takesStockResultsDao.searchByModel(takesStockResultsModel);
		if (takesStockResultsModel==null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "ofc盘点结果结果单不存在,来源单据号:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "ofc盘点结果结果单不存在,来源单据号:" + code);
		}	
		// ofc盘点结果已确认就不修改
		if (DERP_STORAGE.TAKESSTOCKRESULT_STATUS_010.equals(takesStockResultsModel.getStatus())) {
			LOGGER.error("ofc盘点结果 已经确认,订单号code" + code);
			throw new RuntimeException("ofc盘点结果 已经确认,订单号 code" + code);
		}		
		// 根据商家id查询商家表
		Long merchantId = takesStockResultsModel.getMerchantId();		
		Map<String, Object> merchantInfoMap = new HashMap<>();
		merchantInfoMap.put("merchantId", merchantId);// 调出仓库id		
		MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantInfoMap);// 商家信息		
		if (merchantInfoMongo==null) {
			LOGGER.error("根据商家id没有查询到对应的商家,单号code:" + code);
			throw new RuntimeException("根据商家id没有查询到对应的商家,单号code:" + code);
		}		
				
		
		// 根据盘点结果id 获取盘点结果对应的商品
		TakesStockResultItemModel takesStockResultItemModel =new TakesStockResultItemModel();
		takesStockResultItemModel.setTakesStockResultId(takesStockResultsModel.getId());
		List<TakesStockResultItemModel> itemList = takesStockResultItemDao.list(takesStockResultItemModel);
		DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");	
		JSONArray sendMailJSONArray = new JSONArray();
		for (TakesStockResultItemModel item : itemList) {
			String type = item.getType();// 1盘盈 2盘亏
			Date productionDate = item.getProductionDate();
			Date overdueDate = item.getOverdueDate();
			// --------------------发送邮件数据商品开始---------------------
			// 发送邮件json
			JSONObject sendMailGoods = new JSONObject();
			sendMailGoods.put("goodsNo", item.getGoodsNo());// 商品货号
			sendMailGoods.put("goodsName", item.getGoodsName());// 商品名称
			sendMailGoods.put("barcode", item.getBarcode());// 商品条形码
			if (item.getSurplusNum() != null) {
				sendMailGoods.put("surplusNum", item.getSurplusNum());// 数量
			} else {
				sendMailGoods.put("surplusNum", " ");// 数量
			}
			if (item.getDeficientNum() != null) {
				sendMailGoods.put("deficientNum", item.getDeficientNum());// 数量
			} else {
				sendMailGoods.put("deficientNum", " ");// 数量
			}
			
			if (DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1.equals(type)) {
				sendMailGoods.put("type", "盘盈");// 盘盈 "1"
			} else {
				sendMailGoods.put("type", "盘亏");// 盘亏"2"
			}
			sendMailGoods.put("batchNo", item.getBatchNo());// 批次号
			sendMailGoods.put("isDamage", item.getIsDamage());// 好坏品 商品分类 （0 好品
																// 1坏品 ）
			if (productionDate != null) {
				sendMailGoods.put("productionDate", dft.format(productionDate));// 生产日期
			}
			if (overdueDate != null) {
				sendMailGoods.put("overdueDate", dft.format(overdueDate));// 失效日期
			}			
			sendMailJSONArray.add(sendMailGoods);
			//// --------------------发送邮件数据商品结束---------------------			
		}
		
		// --------------------发送邮件数据---------------------
		// 发送 盘点结果邮件json
		JSONObject sendMailJson = new JSONObject();
		JSONObject paramJson = new JSONObject();
		paramJson.put("merchantName", merchantInfoMongo.getName());// 商家名称
		paramJson.put("depotName", takesStockResultsModel.getDepotName());// 仓库名称
		paramJson.put("orderCode", takesStockResultsModel.getCode());// 盘点单号
		paramJson.put("list", sendMailJSONArray);//
		sendMailJson.put("mailCode", SmurfsAPICodeEnum.EMAIL_M017.getCode());// 邮件编码
		sendMailJson.put("recipients", "chuang.yang@topideal.com.cn;" + merchantInfoMongo.getInventoryResultEmail());// 收件人
		sendMailJson.put("paramJson", paramJson);// 邮件模板参数 json
		// 调用外部接口发送邮件
		String resultMsg = SmurfsUtils.send(sendMailJson, SmurfsAPIEnum.SMURFS_EMAIL);
		
		// 修改盘点结果单 
		TakesStockResultsModel takesModel = new TakesStockResultsModel();
		takesModel.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_010);// 状态 待确认 009 YQR("010","已确认"),
		takesModel.setId(takesStockResultsModel.getId());
		takesStockResultsDao.modify(takesModel);
		
		LOGGER.info("经分销Ofc盘点结果邮件发送:" + resultMsg);
		return true;

	}
	

}
