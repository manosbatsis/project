package com.topideal.service.pushback.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.AdjustmentInventoryDao;
import com.topideal.entity.vo.AdjustmentInventoryModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.StorageadJustmentInventoryAuditService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
/**
 * 库存调整单页面审核库存加减成功回推
 * 杨创 
 * 2019/02/26
 */
@Service
public class StorageadJustmentInventoryAuditServiceImpl implements StorageadJustmentInventoryAuditService{
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StorageadJustmentInventoryAuditServiceImpl.class);
	//类型调整单
	@Autowired
	private AdjustmentInventoryDao adjustmentInventoryDao;

	
	@SuppressWarnings("rawtypes")
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201214100, model = DERP_LOG_POINT.POINT_13201214100_Label,keyword ="code")
	public boolean updateAdjustmentType(String json,String keys,String topics,String tags) throws Exception {
		//解析json
		JSONObject jsonData = JSONObject.fromObject(json);
		Thread.sleep(3000);// 睡眠3000毫秒
		Map classMap = new HashMap();
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
