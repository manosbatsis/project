package com.topideal.service.pushback.purchase.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.*;
import com.topideal.entity.vo.purchase.*;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.service.common.CommonBusinessService;
import com.topideal.service.pushback.purchase.DeclareDeliveryPushbackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeclareDeliveryPushbackServiceImpl implements DeclareDeliveryPushbackService{

	/*打印日志*/
	private static final Logger logger = LoggerFactory.getLogger(DeclareDeliveryPushbackServiceImpl.class);
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;
	// 采购订单
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	// 采购订单表体
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	// 采购入库分析差异表
//	@Autowired
//	private PurchaseAnalysisDao purchaseAnalysisDao;
	// mongo商家信息Dao
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private DeclareOrderDao declareOrderDao ;
	@Autowired
	private DeclarePurchaseRelDao declarePurchaseRelDao ;
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201150001, model = DERP_LOG_POINT.POINT_13201150001_Label,keyword="code")
	public void modifyStatus(String json, String keys, String topics, String tags) throws Exception {

		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		// JSON对象转实体
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);
		// 获取采购入库编码
		String code = rootJson.getCode();
		PurchaseWarehouseModel warehouseModel = new PurchaseWarehouseModel();
		warehouseModel.setCode(code);
		warehouseModel = purchaseWarehouseDao.searchByModel(warehouseModel);
		if(warehouseModel==null){
			logger.error(DERP.MQ_FAILTYPE_04 + "采购入库单不存在"+code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "采购入库单不存在"+code);
		}
		//查询预申报单
		DeclareOrderModel queryModel = new DeclareOrderModel();
		queryModel.setCode(warehouseModel.getDeclareOrderCode());//采购单号
		queryModel = declareOrderDao.searchByModel(queryModel);

		if(queryModel == null) {
			logger.error("关联预申报单不存在"+code);
			throw new RuntimeException("关联预申报单不存在"+code);
		}

		DeclarePurchaseRelModel declarePurchaseRelModel = new DeclarePurchaseRelModel();
		declarePurchaseRelModel.setDeclareOrderId(queryModel.getId());// 预申报单ID
		List<DeclarePurchaseRelModel> declarePurchaseRelList = declarePurchaseRelDao.list(declarePurchaseRelModel);

		if (declarePurchaseRelList.isEmpty()) {
			logger.error("预申报单关联采购订单单不存在：" + queryModel.getCode());
			throw new RuntimeException("预申报单关联采购订单单不存在：" + queryModel.getCode());
		}

		for (DeclarePurchaseRelModel relModel : declarePurchaseRelList) {

			PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(relModel.getPurchaseOrderId());

			if (purchaseOrder.getRate() == null) {
				commonBusinessService.saveRate(purchaseOrder, warehouseModel.getInboundDate());
			}

		    //查询采购单表体 按商品分组合并
//			Map<String,Object> purchaseItemMap = new HashMap<String, Object>();
//			purchaseItemMap.put("orderId", purchaseOrder.getId());
//			List<Map<String, Object>> orderGoodsList = purchaseOrderItemDao.getGoodsNumGroupBy(purchaseItemMap);
//			boolean flag = true;//默认已入库完成
//
//			//根据采购单的商家信息在mongo中获取商家的勾稽百分比
//			double articulationPercent = 1.00 ;
//
//			Map<String,Object> queryMap = new HashMap<String,Object>() ;
//			queryMap.put("merchantId", purchaseOrder.getMerchantId()) ;
//			queryMap.put("name", purchaseOrder.getMerchantName()) ;
//			MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(queryMap);
//
//			if(merchantInfo != null ) {
//				articulationPercent = merchantInfo.getArticulationPercent() ;
//			}
//
//			for(Map<String,Object>orderGood:orderGoodsList){
//
//				String goodsNo = (String)orderGood.get("goods_no");
//				BigDecimal num = (BigDecimal)orderGood.get("num");
//
//				Map<String, Object> anMap = new HashMap<String, Object>();
//				anMap.put("goodsNo", goodsNo);
//				anMap.put("orderCode", purchaseOrder.getCode());
//				Map<String,Object> analysisMap = purchaseAnalysisDao.getOrderGoodsNumGroupBy(anMap);
//				BigDecimal warehouseNum = new BigDecimal(0);
//				if(analysisMap!=null
//						&& analysisMap.get("warehouse_num") != null) {
//					warehouseNum = (BigDecimal)analysisMap.get("warehouse_num");
//				}
//
//				double percent = warehouseNum.doubleValue() / num.doubleValue() ;
//
//				if(percent < articulationPercent) {
//					flag = false ;
//				}
//
//			}
			// 修改入库单状态
			PurchaseWarehouseModel pwModel = new PurchaseWarehouseModel();
			pwModel.setId(warehouseModel.getId());
			pwModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);// 已入仓
			pwModel.setWarehouseDate(TimeUtils.getNow());// 入仓时间
			purchaseWarehouseDao.modify(pwModel);

			//是否全部入库
			boolean flag = true;//默认已入库完成
			PurchaseOrderItemModel queryItemModel = new PurchaseOrderItemModel();
			queryItemModel.setPurchaseOrderId(purchaseOrder.getId());
			List<PurchaseOrderItemModel> queryItemList = purchaseOrderItemDao.list(queryItemModel);
			for(PurchaseOrderItemModel purchaseItem : queryItemList){
				//根据采购明细id获取已入库数量
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("purchaseItemId", purchaseItem.getId());
				paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
				List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
				Integer warehouseNum = 0;
				if(numList != null && numList.size() > 0){
					BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
					warehouseNum = queryNum.intValue();
				}
				//只要有一条入库量！=采购数量，则为 部分入库
				if(warehouseNum.intValue() != purchaseItem.getNum()){
					flag = false;
					break;
				}
			}

			if(flag==true){
				//更新采购单状态为已完结
				purchaseOrder.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_007);// 已完结
				purchaseOrder.setEndDate(TimeUtils.getNow());// 完结时间
				purchaseOrder.setIsEnd(DERP_ORDER.PURCHASEORDER_ISEND_1);// 是否完结
				purchaseOrderDao.modify(purchaseOrder);
//
//				//更新差异分析状态为已完结,加上完结入库时间
//				PurchaseAnalysisModel purchaseAnalysis = new PurchaseAnalysisModel();
//				purchaseAnalysis.setOrderId(purchaseOrder.getId());// 采购订单id
//				List<PurchaseAnalysisModel> analysisList = purchaseAnalysisDao.list(purchaseAnalysis);
//				for(PurchaseAnalysisModel pModel : analysisList) {
//					pModel.setIsEnd(DERP_ORDER.PURCHASEANALYSIS_ISEND_1);// 是否完结
//					pModel.setEndDate(TimeUtils.getNow());// 完结入库时间
//					purchaseAnalysisDao.modify(pModel);
//				}
			}else {

				purchaseOrder.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_005);// 部分入库
				purchaseOrder.setModifyDate(TimeUtils.getNow());
				purchaseOrderDao.modify(purchaseOrder);

			}

			//发送金额调整邮件
			commonBusinessService.sendAmountComfirmMail(purchaseOrder.getId(), warehouseModel.getId()) ;
		}

		//修改预申报单为已上架
		DeclareOrderModel updateModel = new DeclareOrderModel() ;
		updateModel.setId(queryModel.getId());
		updateModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_003);

		declareOrderDao.modify(updateModel) ;
	}

}
