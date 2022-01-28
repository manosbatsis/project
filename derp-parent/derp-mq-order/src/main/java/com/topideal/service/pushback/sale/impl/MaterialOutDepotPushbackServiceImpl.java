package com.topideal.service.pushback.sale.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.MaterialOrderDao;
import com.topideal.dao.sale.MaterialOutDepotDao;
import com.topideal.entity.vo.sale.MaterialOrderModel;
import com.topideal.entity.vo.sale.MaterialOutDepotModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.MaterialOutDepotPushbackService;

import net.sf.json.JSONObject;

/**
 * 领料出库回推
 */
@Service
public class MaterialOutDepotPushbackServiceImpl implements MaterialOutDepotPushbackService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MaterialOutDepotPushbackServiceImpl.class);
	// 领料订单
	@Autowired
	private MaterialOrderDao materialOrderDao;
	// 领料出库单
	@Autowired
	private MaterialOutDepotDao materialOutDepotDao;
	
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201120007, model = DERP_LOG_POINT.POINT_13201120007_Label,keyword="code")
	public void modifyStatus(String json, String keys, String topics, String tags) throws Exception {
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		// JSON对象转实体
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);
		// 获取领料出库单编码
		String code = rootJson.getCode();
		// 获取领料单编码
		String materialCode = (String) rootJson.getCustomParam().get("code");
		
		MaterialOrderModel materialOrderModel = new MaterialOrderModel();
		materialOrderModel.setCode(materialCode);		
		materialOrderModel = materialOrderDao.searchByModel(materialOrderModel);
		if (materialOrderModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "领料单不存在,订单编号code:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "领料订单不存在,订单编号code:" + code);
		}
		// 根据领料出库单号查询领料出库清单
		MaterialOutDepotModel outDepotModel = new MaterialOutDepotModel();
		outDepotModel.setCode(code);
		outDepotModel = materialOutDepotDao.searchByModel(outDepotModel);
		if (outDepotModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "领料出库清单不存在,订单号code" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "领料出库清单不存在,订单号code" + code);
		}
		if (null == outDepotModel.getBuId()) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "领料出库清单单号code" + code+"事业部信息值为空");
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "领料出库清单单号code" + code+"事业部信息值为空");
		}

		// 修改领料出库单
		MaterialOutDepotModel mOutDepotModel = new MaterialOutDepotModel();
		mOutDepotModel.setId(outDepotModel.getId());
		mOutDepotModel.setStatus(DERP_ORDER.MATERIALOUTDEPOT_STATUS_018);// 018,已出库
		materialOutDepotDao.modify(mOutDepotModel);
		
		// 修改领料订单
		MaterialOrderModel mModel = new MaterialOrderModel();
		mModel.setId(materialOrderModel.getId());
		mModel.setState(DERP_ORDER.MATERIALORDER_STATE_018);// 018:已出库
		mModel.setModifyDate(TimeUtils.getNow());	// 设置修改时间
		materialOrderDao.modify(mModel);
		
	}
}
