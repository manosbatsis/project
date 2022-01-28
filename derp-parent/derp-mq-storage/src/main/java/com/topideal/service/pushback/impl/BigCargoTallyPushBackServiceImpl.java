package com.topideal.service.pushback.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.AdjustmentTypeDao;
import com.topideal.entity.vo.AdjustmentTypeModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.BigCargoTallyPushBackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
/**
 * 大货理货库存加减成功回推
 * 杨创
 * 2019/02/26
 */
@Service
public class BigCargoTallyPushBackServiceImpl implements BigCargoTallyPushBackService{
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BigCargoTallyPushBackServiceImpl.class);
	//类型调整单
	@Autowired
	private AdjustmentTypeDao adjustmentTypeDao;
	

	
	@SuppressWarnings("rawtypes")
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201214600, model = DERP_LOG_POINT.POINT_13201214600_Label,keyword ="code")
	public boolean updateBigCargoTally(String json,String keys,String topics,String tags) throws Exception {
		//解析json
		JSONObject jsonData = JSONObject.fromObject(json);
		Thread.sleep(3000);// 睡眠3000毫秒
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");	
		
		// 根据类型调整单单号查询类型调整单表
		AdjustmentTypeModel adjustmentTypeModel = new AdjustmentTypeModel();
		adjustmentTypeModel.setCode(code);
		adjustmentTypeModel = adjustmentTypeDao.searchByModel(adjustmentTypeModel);
		if (adjustmentTypeModel==null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "类型调整单不存在,来源单据code:"+code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "类型调整单不存在,来源单据code:"+code);
		}
		if (DERP_STORAGE.ADJUSTMENTTYPE_STATUS_019.equals(adjustmentTypeModel.getStatus())) { //YTZ("019","已调整"),
			LOGGER.error("类型调整单状态已经是已调整,来源单据code:"+code);
			throw new RuntimeException("类型调整单状态已经是已调整,来源单据code:"+code);
		}	
		
		AdjustmentTypeModel model =new AdjustmentTypeModel();

		model.setStatus(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_019); //YTZ("019","已调整"),
		model.setId(adjustmentTypeModel.getId());
		
		//修改库存调整单状态
		adjustmentTypeDao.modify(model);			
		return true;
	}

}
