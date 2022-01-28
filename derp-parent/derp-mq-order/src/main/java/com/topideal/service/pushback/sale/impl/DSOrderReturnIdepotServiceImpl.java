package com.topideal.service.pushback.sale.impl;

import java.util.HashMap;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.sale.OrderReturnIdepotDao;
import com.topideal.entity.vo.sale.OrderReturnIdepotModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.DSOrderReturnIdepotService;

import net.sf.json.JSONObject;

/**
 *  电商订单退货-成功回推
 */
@Service
public class DSOrderReturnIdepotServiceImpl implements DSOrderReturnIdepotService {

	/*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(DSOrderReturnIdepotServiceImpl.class);
	
    @Autowired
    private OrderReturnIdepotDao orderReturnIdepotDao ;
    /**
     * 修改电商订单退货
     */
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201155700,model=DERP_LOG_POINT.POINT_13201155700_Label,keyword="code")
	public void updateDSOrderReturnIdepotPushBackInfo(String json, String keys, String topics, String tags) throws Exception {
		//获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");
		OrderReturnIdepotModel model= new OrderReturnIdepotModel();
		model.setCode(code);
		model = orderReturnIdepotDao.searchByModel(model);		
		if (model==null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "根据电商退货订单号没有查到电商退货订单:code"+code);
			throw new  RuntimeException(DERP.MQ_FAILTYPE_04 + "根据电商退货订单号没有查到电商退货订单:code"+code);
		}	
		// 修改电商退货订单状态为已入仓
		OrderReturnIdepotModel orderReturnIdepotModel = new OrderReturnIdepotModel();
		orderReturnIdepotModel.setId(model.getId());
		orderReturnIdepotModel.setStatus("012");
		int modify = orderReturnIdepotDao.modify(orderReturnIdepotModel);
		
	}
    
	
}
