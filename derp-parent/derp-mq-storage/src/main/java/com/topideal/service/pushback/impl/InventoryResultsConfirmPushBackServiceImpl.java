package com.topideal.service.pushback.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.TakesStockResultItemDao;
import com.topideal.dao.TakesStockResultsDao;
import com.topideal.entity.vo.TakesStockResultsModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.service.pushback.InventoryResultsConfirmPushBackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 仓储盘点结果库存加减成功回推 mq
 */
@Service
public class InventoryResultsConfirmPushBackServiceImpl implements InventoryResultsConfirmPushBackService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryResultsConfirmPushBackServiceImpl.class);
	
	@Autowired
	private TakesStockResultsDao takesStockResultsDao;// 盘点结果表

	/**
	 * 仓储盘点结果回推
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201213902, model = DERP_LOG_POINT.POINT_13201213902_Label,keyword ="code")
	public boolean updateinventoryResultsPushBackInfo(String json, String keys, String topics, String tags) throws Exception {
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
		// 修改为盘点结果表为已提交
		TakesStockResultsModel takesModel = new TakesStockResultsModel();
		takesModel.setId(takesStockResultsModel.getId());
		takesModel.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_010); //YQR("010","已确认"),
		// 修改盘点结果表
		takesStockResultsDao.modify(takesModel);		
		return true;
	}

}
