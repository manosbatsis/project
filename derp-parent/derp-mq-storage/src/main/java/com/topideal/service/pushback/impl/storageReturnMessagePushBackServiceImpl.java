package com.topideal.service.pushback.impl;
import java.util.HashMap;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_STORAGE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.AdjustmentInventoryDao;
import com.topideal.entity.vo.AdjustmentInventoryModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.api.impl.StorageReturnMessagePushServiceImpl;
import com.topideal.service.pushback.storageReturnMessagePushBackService;

import net.sf.json.JSONObject;

/**
 * 仓储退运信息
 * 
 * @author 杨创 2019/02/26
 */
@Service
public class storageReturnMessagePushBackServiceImpl implements storageReturnMessagePushBackService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StorageReturnMessagePushServiceImpl.class);
	@Autowired
	private AdjustmentInventoryDao adjustmentInventoryDao;// 库存调整单	


	
	// 保存退运信息

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201214200, model = DERP_LOG_POINT.POINT_13201214200_Label,keyword ="code")
	public boolean updatestorageReturnMessagePushBackInfo(String json,String keys,String topics,String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		@SuppressWarnings("rawtypes")
		Map classMap = new HashMap();
		Thread.sleep(3000);// 睡眠3000毫秒
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");

		AdjustmentInventoryModel adjustmentInventoryModel = new AdjustmentInventoryModel();
		adjustmentInventoryModel.setCode(code);// 来源单号
		adjustmentInventoryModel = adjustmentInventoryDao.searchByModel(adjustmentInventoryModel);

		if (adjustmentInventoryModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "库存调整单不存在: 订单号code:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "库存调整单不存在: 订单号code:" + code);
		}
		if (DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_019.equals(adjustmentInventoryModel.getStatus())) { //YTZ("019","已调整"),
			LOGGER.error("库存调整单已经是已调整状态: 订单号code:" + code);
			throw new RuntimeException("库存调整单已经是已调整状态: 订单号code:" + code);
		}
		AdjustmentInventoryModel model = new AdjustmentInventoryModel();
		model.setId(adjustmentInventoryModel.getId());
		model.setStatus(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_019); //YTZ("019","已调整"),
		adjustmentInventoryDao.modify(model);	
		
		return true;
	}

}
