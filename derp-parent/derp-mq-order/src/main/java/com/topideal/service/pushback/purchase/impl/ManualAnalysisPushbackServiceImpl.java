/*package com.topideal.service.pushback.purchase.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.*;
import com.topideal.entity.vo.purchase.PurchaseAnalysisModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.entity.vo.purchase.WarehouseOrderRelModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.common.CommonBusinessService;
import com.topideal.service.pushback.purchase.ManualAnalysisPushbackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManualAnalysisPushbackServiceImpl implements ManualAnalysisPushbackService {

	打印日志
	private static final Logger logger = LoggerFactory.getLogger(ManualAnalysisPushbackServiceImpl.class);
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;
	// 采购订单
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	// 采购订单表体
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	// 采购入库分析差异表
	@Autowired
	private PurchaseAnalysisDao purchaseAnalysisDao;
	// mongo商家信息Dao
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao ;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201152701, model = DERP_LOG_POINT.POINT_13201152701_Label,keyword="code")
	public void modifyStatus(String json, String keys, String topics, String tags) throws SQLException {
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

		//根据入库单查询关联关系
		WarehouseOrderRelModel relModel = new WarehouseOrderRelModel() ;
		relModel.setWarehouseId(warehouseModel.getId());

		List<WarehouseOrderRelModel> relList = warehouseOrderRelDao.list(relModel);

		for (WarehouseOrderRelModel warehouseOrderRelModel : relList) {
			//查询采购订单
			PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(warehouseOrderRelModel.getPurchaseOrderId());

			if (purchaseOrder.getRate() == null) {
				commonBusinessService.saveRate(purchaseOrder, warehouseModel.getInboundDate());
			}

		    //查询采购单表体 按商品分组合并
			Map<String,Object> purchaseItemMap = new HashMap<String, Object>();
			purchaseItemMap.put("orderId", purchaseOrder.getId());
			List<Map<String, Object>> orderGoodsList = purchaseOrderItemDao.getGoodsNumGroupBy(purchaseItemMap);
			boolean flag = true;//默认已入库完成

			//根据采购单的商家信息在mongo中获取商家的勾稽百分比
			double articulationPercent = 1.00 ;

			Map<String,Object> queryMap = new HashMap<String,Object>() ;
			queryMap.put("merchantId", purchaseOrder.getMerchantId()) ;
			queryMap.put("name", purchaseOrder.getMerchantName()) ;
			MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(queryMap);

			if(merchantInfo != null ) {
				articulationPercent = merchantInfo.getArticulationPercent() ;
			}

			for(Map<String,Object> orderGood : orderGoodsList){
				String goodsNo = (String)orderGood.get("goods_no");
				BigDecimal num = (BigDecimal)orderGood.get("num");

				Map<String, Object> anMap = new HashMap<String, Object>();
				anMap.put("goodsNo", goodsNo);
				anMap.put("orderCode", purchaseOrder.getCode());
				Map<String,Object> analysisMap = purchaseAnalysisDao.getOrderGoodsNumGroupBy(anMap);
				BigDecimal warehouseNum = new BigDecimal(0);
				if(analysisMap!=null
						&& analysisMap.get("warehouse_num") != null) {
					warehouseNum = (BigDecimal)analysisMap.get("warehouse_num");
				}
				//查询勾稽数量如果>=采购数量则完结
				//if(warehouseNum.intValue()<num.intValue()) flag = false;

				
				 //JFX-1114采购订单自动完结状态更新优化
				// 该字段旨在对指定商家设置采购订单与采购入库单自动或手动勾稽时达到设定比例值时，则采购订单的状态自动更新为“已完结”
				 
				//计算勾稽百分比，根据勾稽百分比判断是否完成
				double percent = warehouseNum.doubleValue() / num.doubleValue() ;

				if(percent < articulationPercent) {
					flag = false ;
				}

			}

			if(flag==true){
				//更新采购单状态为已完结
				purchaseOrder.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_007);// 已完结
				purchaseOrder.setEndDate(TimeUtils.getNow());// 完结时间
				purchaseOrder.setIsEnd(DERP_ORDER.PURCHASEORDER_ISEND_1);// 是否完结
				purchaseOrderDao.modify(purchaseOrder);

				//更新差异分析状态为已完结,加上完结入库时间
				PurchaseAnalysisModel purchaseAnalysis = new PurchaseAnalysisModel();
				purchaseAnalysis.setOrderId(purchaseOrder.getId());// 采购订单id
				List<PurchaseAnalysisModel> analysisList = purchaseAnalysisDao.list(purchaseAnalysis);
				for(PurchaseAnalysisModel pModel : analysisList) {
					pModel.setIsEnd(DERP_ORDER.PURCHASEANALYSIS_ISEND_1);// 是否完结
					pModel.setEndDate(TimeUtils.getNow());// 完结入库时间
					purchaseAnalysisDao.modify(pModel);
				}
			}else {

				purchaseOrder.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_005);// 部分入库
				purchaseOrder.setModifyDate(TimeUtils.getNow());
				purchaseOrderDao.modify(purchaseOrder);

			}

			//发送金额调整邮件
			commonBusinessService.sendAmountComfirmMail(purchaseOrder.getId(), warehouseModel.getId()) ;

		}

		// 修改入库单状态
		PurchaseWarehouseModel pwModel = new PurchaseWarehouseModel();
		pwModel.setId(warehouseModel.getId());
		pwModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);// 已入仓
		pwModel.setWarehouseDate(TimeUtils.getNow());// 入仓时间

		purchaseWarehouseDao.modify(pwModel);

	}
}
*/