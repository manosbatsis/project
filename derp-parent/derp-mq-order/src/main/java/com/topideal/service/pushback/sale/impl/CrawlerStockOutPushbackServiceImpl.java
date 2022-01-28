//package com.topideal.service.pushback.sale.impl;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.topideal.common.constant.DERP;
//import com.topideal.common.constant.DERP_ORDER;
//import com.topideal.common.system.annotation.SystemServiceLog;
//import com.topideal.common.tools.TimeUtils;
//import com.topideal.dao.sale.SaleAnalysisDao;
//import com.topideal.dao.sale.SaleOrderDao;
//import com.topideal.dao.sale.SaleOrderItemDao;
//import com.topideal.dao.sale.SaleOutDepotDao;
//import com.topideal.dao.sale.SaleOutDepotItemDao;
//import com.topideal.entity.vo.sale.SaleAnalysisModel;
//import com.topideal.entity.vo.sale.SaleOrderItemModel;
//import com.topideal.entity.vo.sale.SaleOrderModel;
//import com.topideal.entity.vo.sale.SaleOutDepotItemModel;
//import com.topideal.entity.vo.sale.SaleOutDepotModel;
//import com.topideal.json.inventory.j04.BackRootJson;
//import com.topideal.service.pushback.sale.CrawlerStockOutPushBackService;
//
//import net.sf.json.JSONObject;
//
///**
// * 爬虫生成出库单库存扣减成功回调
// */
//@Service
//public class CrawlerStockOutPushbackServiceImpl implements CrawlerStockOutPushBackService {
//	/* 打印日志 */
//	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerStockOutPushbackServiceImpl.class);
//	// 销售订单
//	@Autowired
//	private SaleOrderDao saleOrderDao;
//	// 销售出库清单
//	@Autowired
//	private SaleOutDepotDao saleOutDepotDao;
//	// 销售出库清单对应的商品
//	@Autowired
//	private SaleOutDepotItemDao saleOutDepotItemDao;
//	// 销售订单表体
//	@Autowired
//	private SaleOrderItemDao saleOrderItemDao;
//	// 销售出库分析表
//	@Autowired
//	private SaleAnalysisDao saleAnalysisDao;
//	
//	@Override
//	@SystemServiceLog(point = "13201153800", model = "爬虫生成出库单库存扣减成功回推",keyword="code")
//	public boolean modifyStatus(String json, String keys, String topics, String tags) throws Exception {
//		Thread.sleep(100);
//		JSONObject jsonData = JSONObject.fromObject(json);
//		Map classMap = new HashMap();
//		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
//		Map<String, Object> customParam = rootJson.getCustomParam();
//		String code =  (String) customParam.get("code");
//		SaleOutDepotModel saleOutDepot= new SaleOutDepotModel();
//		saleOutDepot.setCode(code);
//		SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepot);
//		if (saleOutDepotModel == null) {
//			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售出库单,出库单号:" + code);
//			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售出库单,出库单号:" + code);
//		}
//		//检查出库单状态
//		if(!saleOutDepotModel.getStatus().equals(DERP_ORDER.SALEOUTDEPOT_STATUS_027)){
//			LOGGER.error("出库单非出库中状态,出库单号:" + code);
//			throw new RuntimeException("出库单非出库中状态,出库单号:" + code);
//		}
//		//获取销售订单
//		SaleOrderModel saleOrderModel = saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId());
//		if(saleOrderModel == null) {
//			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售订单,销售订单号:" + code);
//			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售订单,销售订单号:" + code);
//		}
//		if (null == saleOrderModel.getBuId()) {
//			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售订单单号code" + code+"事业部信息值为空");
//			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售订单单号code" + code+"事业部信息值为空");
//		}
//		
//		// 根据销售出库单id查询销售出库订单商品
//		SaleOutDepotItemModel saleOutDepotItemModel = new SaleOutDepotItemModel();
//		saleOutDepotItemModel.setSaleOutDepotId(saleOutDepotModel.getId());
//		List<SaleOutDepotItemModel> list = saleOutDepotItemDao.list(saleOutDepotItemModel);	
//		for (SaleOutDepotItemModel itemModel : list) {
//			//查询销售单此商品的数量，合并相同商品数量
//			Map<String,Object> orderGoodsMap = new HashMap<String, Object>();
//			orderGoodsMap.put("code", saleOrderModel.getCode());
//			orderGoodsMap.put("goodsId", itemModel.getGoodsId());
//			Integer goodsNumSum = saleOrderItemDao.getGoodsNumBySum(orderGoodsMap);
//			
//			//生成差异分析信息
//			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
//			saleAnalysisModel.setOrderId(saleOrderModel.getId());
//			saleAnalysisModel.setGoodsId(itemModel.getGoodsId());
//			saleAnalysisModel = saleAnalysisDao.searchByModel(saleAnalysisModel);
//			if(saleAnalysisModel != null){
//				Integer transferNum = itemModel.getTransferNum();//出库数量
//				if(saleAnalysisModel.getOutDepotNum() != null){
//					transferNum = transferNum+saleAnalysisModel.getOutDepotNum();
//				}
//				saleAnalysisModel.setIsEnd("0");
//				saleAnalysisModel.setSaleOutDepotCode(saleOutDepotModel.getCode());
//				saleAnalysisModel.setSaleOutDepotId(saleOutDepotModel.getId());
//				saleAnalysisModel.setOutDepotNum(transferNum);
//				saleAnalysisModel.setSurplus(goodsNumSum-transferNum);
//				saleAnalysisDao.modify(saleAnalysisModel);
//			}else{
//				saleAnalysisModel = new SaleAnalysisModel();
//				saleAnalysisModel.setCreateDate(TimeUtils.getNow());
//				saleAnalysisModel.setCustomerId(saleOrderModel.getCustomerId());
//				saleAnalysisModel.setCustomerName(saleOrderModel.getCustomerName());
//				saleAnalysisModel.setEndDate(TimeUtils.getNow());
//				saleAnalysisModel.setGoodsId(itemModel.getGoodsId());
//				saleAnalysisModel.setGoodsName(itemModel.getGoodsName());
//				saleAnalysisModel.setGoodsNo(itemModel.getGoodsNo());
//				saleAnalysisModel.setIsEnd("0");
//				saleAnalysisModel.setOrderCode(saleOrderModel.getCode());
//				saleAnalysisModel.setOrderId(saleOrderModel.getId());
//				saleAnalysisModel.setOutDepotNum(itemModel.getTransferNum());
//				saleAnalysisModel.setSaleNum(goodsNumSum);
//				saleAnalysisModel.setSaleOutDepotCode(saleOutDepotModel.getCode());
//				saleAnalysisModel.setSaleOutDepotId(saleOutDepotModel.getId());
//				saleAnalysisModel.setSurplus(goodsNumSum - itemModel.getTransferNum());
//				saleAnalysisModel.setMerchantId(saleOrderModel.getMerchantId());
//				saleAnalysisModel.setMerchantName(saleOrderModel.getMerchantName());
//				saleAnalysisModel.setSaleType(saleOrderModel.getBusinessModel());//销售类型 1购销 2代销
//				saleAnalysisModel.setBuId(saleOrderModel.getBuId()); // 事业部Id
//				saleAnalysisModel.setBuName(saleOrderModel.getBuName());// 事业部名称
//				saleAnalysisDao.save(saleAnalysisModel);
//			}
//		}
//		// 修改销售出库单
//		SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
//		sOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_018);// '状态 017,待出库,018,已出库'
//		sOutDepotModel.setId(saleOutDepotModel.getId());
//		saleOutDepotDao.modify(sOutDepotModel);		
//		//判断是否能自动完结(不能影响其他流程的运转)
//		boolean flag = true;//是否完结    true：完结    false：未完结
//		try{
//			//获取销售订单所有的商品信息
//			SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
//			saleOrderItemModel.setOrderId(saleOrderModel.getId());
//			List<SaleOrderItemModel> sItemList = saleOrderItemDao.list(saleOrderItemModel);
//			//获取销售订单对应的所有出库单
//			SaleOutDepotModel sodModel = new SaleOutDepotModel();
//			sodModel.setSaleOrderId(saleOrderModel.getId());
//			List<SaleOutDepotModel> saleOutDepotList = saleOutDepotDao.list(sodModel);
//			//遍历，计算销售订单中所有商品与对应的出库单（1对多，数量累加）出库的数量一致
//			for(SaleOrderItemModel item:sItemList){
//				Integer outNum = 0;//某商品出库总量
//				for(SaleOutDepotModel saleOutDepot1:saleOutDepotList){
//					SaleOutDepotItemModel saleOutDepotItem = new SaleOutDepotItemModel();
//					saleOutDepotItem.setSaleOutDepotId(saleOutDepot1.getId());
//					saleOutDepotItem.setGoodsId(item.getGoodsId());
//					List<SaleOutDepotItemModel> saleOutDepotItemList = saleOutDepotItemDao.list(saleOutDepotItem);
//					for(SaleOutDepotItemModel sodItem:saleOutDepotItemList){
//						outNum +=sodItem.getTransferNum();
//					}
//				}
//				if(item.getNum() != outNum){
//					LOGGER.info(item.getGoodsNo()+"数量不等，不能完结。销售数量：出库数量("+item.getNum()+":"+outNum+")");
//					flag = false;
//					break;
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			flag = false;
//		}
//		
//		// 修改销售订单 为已出库saleOrderModel
//		SaleOrderModel sModel = new SaleOrderModel();
//		sModel.setId(saleOrderModel.getId());
//		if("2".equals(saleOrderModel.getOrderSource()) || flag){
//			sModel.setState(DERP_ORDER.SALEORDER_STATE_007);//订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库
//			sModel.setEndDate(TimeUtils.getNow());	// 设置已完结时间
//			//修改差异分析表状态
//			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
//			saleAnalysisModel.setOrderId(saleOrderModel.getId());
//			List<SaleAnalysisModel> saleAnalysisList = saleAnalysisDao.list(saleAnalysisModel);
//			for(SaleAnalysisModel saleAnalysis :saleAnalysisList){
//				saleAnalysis.setIsEnd("1");
//				saleAnalysis.setEndDate(TimeUtils.getNow());
//				saleAnalysisDao.modify(saleAnalysis);
//			}
//		}else{
//			sModel.setState(DERP_ORDER.SALEORDER_STATE_018);//订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库
//		}
//		saleOrderDao.modify(sModel);
//		return true;
//	}
//
//}
