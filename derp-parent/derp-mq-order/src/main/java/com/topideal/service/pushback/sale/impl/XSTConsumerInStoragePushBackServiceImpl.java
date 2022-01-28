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
import com.topideal.dao.sale.SaleReturnIdepotDao;
import com.topideal.dao.sale.SaleReturnOrderDao;
import com.topideal.entity.vo.sale.SaleReturnIdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOrderModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.XSTConsumerInStoragePushBackService;

import net.sf.json.JSONObject;
/**
 * 销售退消费者退货入库库存加减成功回推
 * 2019/07/25
 * 文艳
 */
@Service
public class XSTConsumerInStoragePushBackServiceImpl implements XSTConsumerInStoragePushBackService{
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(XSTConsumerInStoragePushBackServiceImpl.class);
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;//销售退货订单
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;// 销售退货入库单
	
	// 销售退入库
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201155100,model=DERP_LOG_POINT.POINT_13201155100_Label,keyword="code")
	public boolean updateXSTConsumerInStoragePushBack(String json,String keys,String topics,String tags) throws Exception {
		// 不论是否是菜鸟仓传的code都是销售退货订单的code
		// 将字符串转成json 
		Thread.sleep(50);
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");
		// 销售退货订单
		SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();		
		saleReturnOrderModel.setCode(code);
		saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);
		
		if (saleReturnOrderModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售退货订单,订单号:code" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售退货订单,订单号:code" + code);
		}		
		// 根据销售退货入库单号 和 销售退货编号 查询销售退货入库表
		SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();		
		saleReturnIdepotModel.setOrderId(saleReturnOrderModel.getId());		
		saleReturnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);						
		if (saleReturnIdepotModel==null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售退货入库单不存在code:"+code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售退货入库单不存在code:"+code);
		}
		
		// 修改销售退货入库
    	SaleReturnIdepotModel sReturnIdepotModel = new SaleReturnIdepotModel();   	
    	sReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_012);//'状态   DRC("011","待入仓"),YRC("012","已入仓"),  
    	sReturnIdepotModel.setId(saleReturnIdepotModel.getId());
    	saleReturnIdepotDao.modify(sReturnIdepotModel);
    
    	    	   	
    	//修改销售退货订单状态
    	SaleReturnOrderModel saleReturnOrderModel1 = new SaleReturnOrderModel();
    	//判断是否已完结
//		if(StatusEnum.YRC.getKey().equals(sReturnIdepotModel.getStatus())){
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_007);
	//	}
		saleReturnOrderModel1.setId(saleReturnOrderModel.getId());
		saleReturnOrderDao.modify(saleReturnOrderModel1);	
		return true;
	}
}
