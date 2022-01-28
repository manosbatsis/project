package com.topideal.service.pushback.sale.impl;

import java.util.HashMap;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.BillOutinDepotDao;
import com.topideal.entity.vo.sale.BillOutinDepotModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.YunJiReturnDetailPushBackBackService;

import net.sf.json.JSONObject;
/**
 * 调拨出库库存加减成功回推
 * 2019/02/27
 * 杨创
 */
@Service
public class YunJiReturnDetailPushBackBackServiceImpl implements YunJiReturnDetailPushBackBackService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(YunJiReturnDetailPushBackBackServiceImpl.class);
	@Autowired 
	private BillOutinDepotDao billOutinDepotDao;// 账单出入库

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201155900,model=DERP_LOG_POINT.POINT_13201155900_Label,keyword="code")
	public boolean updateYunJiReturnDetailPushBack(String json,String keys,String topics,String tags) throws Exception {
		// 将字符串转成json
		Thread.sleep(50);
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");
		
		BillOutinDepotModel billOutinDepotModel=new BillOutinDepotModel();
		billOutinDepotModel.setCode(code);
		billOutinDepotModel = billOutinDepotDao.searchByModel(billOutinDepotModel);
			
		if (billOutinDepotModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的爬虫出入库单据,订单号:code" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的爬虫出入库单据,订单号:" + code);
		}
		// 修改账单出入库为 已入库
		BillOutinDepotModel billOutinDepot=new BillOutinDepotModel();	
		billOutinDepot.setId(billOutinDepotModel.getId());
		billOutinDepot.setState(DERP_ORDER.BILLOUTINDEPOT_STATE_03);
		billOutinDepot.setModifyDate(TimeUtils.getNow());
		billOutinDepotDao.modify(billOutinDepot);

		
		
		return true;
	}
}
