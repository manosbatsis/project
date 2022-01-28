package com.topideal.service.pushback.purchase.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseWarehouseDao;
import com.topideal.dao.purchase.PurchaseWarehouseItemDao;
import com.topideal.dao.purchase.WarehouseOrderRelDao;
import com.topideal.entity.vo.purchase.*;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.pushback.purchase.YwmsPurchaseWarehousePushbackService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YwmsPurchaseWarehousePushbackServiceImpl implements YwmsPurchaseWarehousePushbackService{

	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
//	@Autowired
//	private PurchaseAnalysisDao purchaseAnalysisDao;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201108000, model = DERP_LOG_POINT.POINT_13201108000_Label, keyword = "code")
	public void modifyStatus(String json, String keys, String topics, String tags) throws Exception {
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		// JSON对象转实体
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);

		// 获取入库单编码
		String code = rootJson.getCode();
		PurchaseWarehouseModel purchaseWarehouseModel = new PurchaseWarehouseModel();
		purchaseWarehouseModel.setCode(code);
		purchaseWarehouseModel = purchaseWarehouseDao.searchByModel(purchaseWarehouseModel);

		// 修改入库单状态
		PurchaseWarehouseModel model = new PurchaseWarehouseModel();
		model.setId(purchaseWarehouseModel.getId());
		model.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);// 已入仓
		model.setWarehouseDate(TimeUtils.getNow());// 入仓时间
		purchaseWarehouseDao.modify(model);

		// 采购入库关联采购订单表
		List<WarehouseOrderRelModel> relList = warehouseOrderRelDao.listOrderByAudthTime(purchaseWarehouseModel.getId());

		if (relList != null && relList.size() == 1) {
			WarehouseOrderRelModel relModel = relList.get(0);
			// 根据采购单id判断预申报单是否是1对1
			DeclarePurchaseRelModel dpModel = new DeclarePurchaseRelModel();
			dpModel.setPurchaseOrderId(relModel.getPurchaseOrderId());
			List<Long> ids = new ArrayList<Long>();
			ids.add(purchaseWarehouseModel.getId());
			// 获取采购订单
			PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(relModel.getPurchaseOrderId());

			// 判断是否满足商家勾稽百分比
			// 根据采购订单id获取商品详情
			PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
			purchaseOrderModel.setId(relModel.getPurchaseOrderId());
			purchaseOrderModel = purchaseOrderDao.getDetails(purchaseOrder);
			double total = 0.0;
			int index = 0;
			for (PurchaseOrderItemModel pModel : purchaseOrderModel.getItemList()) {
				// 根据采购订单id和商品信息查询勾稽入库数量
//				PurchaseAnalysisModel purchaseAnalysis = new PurchaseAnalysisModel();
//				purchaseAnalysis.setOrderId(relModel.getPurchaseOrderId());// 采购订单id
//				purchaseAnalysis.setGoodsId(pModel.getGoodsId());// 商品id
//				List<PurchaseAnalysisModel> purchaseAnalysisList = purchaseAnalysisDao.list(purchaseAnalysis);
//				// 每一个商品对应的勾稽入库数量
//				Integer warehouseNum = 0;
//				for (PurchaseAnalysisModel paModel : purchaseAnalysisList) {
//					if (null != paModel.getWarehouseNum() && paModel.getWarehouseNum() != 0) {
//						PurchaseWarehouseModel purchaseWarehouse = purchaseWarehouseDao
//								.searchById(paModel.getWarehouseId());
//						// 已入仓的才会加入百分比计算
//						if (purchaseWarehouse.getState().equals(DERP_ORDER.PURCHASEWAREHOUSE_STATE_012)) {
//							warehouseNum += paModel.getWarehouseNum();
//						}
//					}
//				}
				//根据采购明细id获取已入库数量
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("purchaseItemId", pModel.getId());
				paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
				List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
				Integer warehouseNum = 0;
				if(numList != null && numList.size() > 0){
					BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
					warehouseNum = queryNum.intValue();
				}
				// 总勾稽入库数量/采购数量
				double temp = (double) warehouseNum / pModel.getNum();
				total += temp ;
				index++;
			}

			//根据采购单的商家信息在mongo中获取商家的勾稽百分比
			double articulationPercent = 1.00 ;

			Map<String,Object> queryMap = new HashMap<String,Object>() ;
			queryMap.put("merchantId", purchaseWarehouseModel.getMerchantId()) ;
			queryMap.put("name", purchaseWarehouseModel.getMerchantName()) ;
			MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(queryMap);

			if(merchantInfo != null ) {
				articulationPercent = merchantInfo.getArticulationPercent() ;
			}

			double result = 0.0;
			if (index != 0) {
				result = total / index;
			}
			// 自动完结
			if (result >= articulationPercent) {
				purchaseOrderModel.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_007);// 已完结
				purchaseOrderModel.setEndDate(TimeUtils.getNow());// 完结时间
				purchaseOrderModel.setIsEnd(DERP_ORDER.PURCHASEORDER_ISEND_1);// 是否完结
				purchaseOrderDao.modify(purchaseOrderModel);
//				PurchaseAnalysisModel purchaseAnalysis = new PurchaseAnalysisModel();
//				purchaseAnalysis.setOrderId(relModel.getPurchaseOrderId());// 采购订单id
//				List<PurchaseAnalysisModel> analysisList = purchaseAnalysisDao.list(purchaseAnalysis);
//				// 加上完结入库时间
//				for (PurchaseAnalysisModel pModel : analysisList) {
//					pModel.setIsEnd(DERP_ORDER.PURCHASEORDER_ISEND_1);// 是否完结
//					pModel.setEndDate(TimeUtils.getNow());// 完结入库时间
//					purchaseAnalysisDao.modify(pModel);
//				}
			}

		}
	}

}
