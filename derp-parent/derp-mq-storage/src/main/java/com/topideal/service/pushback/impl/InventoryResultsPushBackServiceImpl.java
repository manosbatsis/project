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
import com.topideal.service.pushback.InventoryResultsPushBackService;
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
 * 仓储盘点结果库存加减成功回推 mq
 * 
 * @author 杨创 2019/02/26
 */
@Service
public class InventoryResultsPushBackServiceImpl implements InventoryResultsPushBackService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryResultsPushBackServiceImpl.class);
	
	@Autowired
	private TakesStockResultsDao takesStockResultsDao;// 盘点结果表
	@Autowired
	private TakesStockResultItemDao takesStockResultItemDao;// 盘点结果商品表
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;// 商家mongodb

	/**
	 * 仓储盘点结果回推
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201213900, model = DERP_LOG_POINT.POINT_13201213900_Label,keyword ="code")
	public boolean updateinventoryResultsPushBackInfo(String json, String keys, String topics, String tags)
			throws Exception {
		Thread.sleep(3000);// 睡眠3000毫秒
		
		/**
		 * 说明 盘点结果回推会存在和盘点指令对不上的单 也要进行保存
		 */
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");		
		// 根据盘点指令单号查询盘点结果表
		TakesStockResultsModel takesStockResultsModel = new TakesStockResultsModel();
		takesStockResultsModel.setCode(code);// 来源单号
		takesStockResultsModel = takesStockResultsDao.searchByModel(takesStockResultsModel);

		if (takesStockResultsModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "根据单号没有查询到盘点结果,内部单号:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "根据单号没有查询到盘点结果,内部单号:" + code);
		}
		// 盘点结果已确认就不修改
		if (DERP_STORAGE.TAKESSTOCKRESULT_STATUS_010.equals(takesStockResultsModel.getStatus())) {
			LOGGER.error("盘点结果 已经确认,订单号code" + code);
			throw new RuntimeException("盘点结果 已经确认,订单号 code" + code);
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
		
		
		// 根据判断结果id 查询盘点结果商品
		TakesStockResultItemModel takesStockResultItemModel = new TakesStockResultItemModel();
		takesStockResultItemModel.setTakesStockResultId(takesStockResultsModel.getId());
		List<TakesStockResultItemModel> itemList = takesStockResultItemDao.list(takesStockResultItemModel);
		// 存储 经分销盘点结果邮件商品商品信息json数组
		JSONArray sendMailJSONArray = new JSONArray();
		DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");	
		for (TakesStockResultItemModel itemModel : itemList) {
			String type = itemModel.getType();// 1盘盈 2盘亏			
			Date productionDate = itemModel.getProductionDate();
			Date overdueDate = itemModel.getOverdueDate();
			// --------------------发送邮件数据商品开始---------------------
			// 发送邮件json
			JSONObject sendMailGoods = new JSONObject();
			sendMailGoods.put("goodsNo", itemModel.getGoodsNo());// 商品货号
			sendMailGoods.put("goodsName", itemModel.getGoodsName());// 商品名称
			sendMailGoods.put("barcode", itemModel.getBarcode());// 商品条形码
			if (itemModel.getSurplusNum() != null) {
				sendMailGoods.put("surplusNum", itemModel.getSurplusNum());// 数量
			} else {
				sendMailGoods.put("surplusNum", " ");// 数量
			}
			if (itemModel.getDeficientNum() != null) {
				sendMailGoods.put("deficientNum", itemModel.getDeficientNum());// 数量
			} else {
				sendMailGoods.put("deficientNum", " ");// 数量
			}

			if (DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1.equals(type)) {
				sendMailGoods.put("type", "盘盈");// 盘盈 "1"
			} else {
				sendMailGoods.put("type", "盘亏");// 盘亏"2"
			}
			sendMailGoods.put("batchNo", itemModel.getBatchNo());// 批次号
			sendMailGoods.put("isDamage", itemModel.getIsDamage());// 好坏品 商品分类
																	// （0 好品 1坏品
																	// ）
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
		paramJson.put("list", sendMailJSONArray);
		sendMailJson.put("mailCode", SmurfsAPICodeEnum.EMAIL_M017.getCode());// 邮件编码
		sendMailJson.put("recipients", "chuang.yang@topideal.com.cn" + merchantInfoMongo.getInventoryResultEmail());// 收件人
		sendMailJson.put("paramJson", paramJson);// 邮件模板参数 json
		// 调用外部接口发送邮件
		String resultMsg = SmurfsUtils.send(sendMailJson, SmurfsAPIEnum.SMURFS_EMAIL);
		LOGGER.info("经分销盘点结果邮件发送:" + resultMsg);

		// 修改为盘点结果表为已提交
		TakesStockResultsModel takesModel = new TakesStockResultsModel();
		takesModel.setId(takesStockResultsModel.getId());
		takesModel.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_010); //YQR("010","已确认"),
		// 修改盘点结果表
		takesStockResultsDao.modify(takesModel);		
		return true;
	}

}
