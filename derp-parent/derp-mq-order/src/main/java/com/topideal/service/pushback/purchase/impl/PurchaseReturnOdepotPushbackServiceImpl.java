package com.topideal.service.pushback.purchase.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.purchase.PurchaseReturnOdepotDao;
import com.topideal.dao.purchase.PurchaseReturnOrderDao;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOrderModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.purchase.PurchaseReturnOdepotPushbackService;

import net.sf.json.JSONObject;

@Service
public class PurchaseReturnOdepotPushbackServiceImpl implements PurchaseReturnOdepotPushbackService{

	@Autowired
	PurchaseReturnOdepotDao purchaseReturnOdepotDao ;
	@Autowired
	PurchaseReturnOrderDao purchaseReturnOrderDao ;
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseReturnOdepotPushbackServiceImpl.class);

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201157400,model=DERP_LOG_POINT.POINT_13201157400_Label,keyword="code")
	public boolean modifyStatus(String json, String keys, String topics, String tags) throws Exception {
		Thread.sleep(1000);
		
		/**
		 * 1.采购退货出库页面审核 2. 装载交运-采购退货  3.出库明细-采购退货    三个接口公用库存回推方法
		 */
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
	    // JSON对象转实体
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);
		
		PurchaseReturnOdepotModel purchaseReturnOdepotModel = new PurchaseReturnOdepotModel() ;
		purchaseReturnOdepotModel.setCode(rootJson.getCode());
		
		purchaseReturnOdepotModel = purchaseReturnOdepotDao.searchByModel(purchaseReturnOdepotModel) ;
		
		if(purchaseReturnOdepotModel==null){
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "采购退货出库单不存在"+rootJson.getCode());
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "采购退货出库单不存在"+rootJson.getCode());
		}
		
		PurchaseReturnOrderModel purchaseReturnOrderModel = new PurchaseReturnOrderModel() ;
		purchaseReturnOrderModel.setCode(purchaseReturnOdepotModel.getPurchaseReturnCode());
		
		purchaseReturnOrderModel = purchaseReturnOrderDao.searchByModel(purchaseReturnOrderModel) ;
		
		if(purchaseReturnOrderModel == null){
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "采购退货单不存在"+rootJson.getCode());
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "采购退货单不存在"+rootJson.getCode());
		}
		
		PurchaseReturnOdepotModel updateOdepotModel = new PurchaseReturnOdepotModel() ;
		updateOdepotModel.setId(purchaseReturnOdepotModel.getId());
		updateOdepotModel.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_016);
		
		purchaseReturnOdepotDao.modify(updateOdepotModel) ;
		
		PurchaseReturnOrderModel updateModel = new PurchaseReturnOrderModel() ;
		updateModel.setId(purchaseReturnOrderModel.getId());
		updateModel.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_016);
		
		purchaseReturnOrderDao.modify(updateModel) ;
		
		return true ;
	}

	
}
