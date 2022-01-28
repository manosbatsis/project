//package com.topideal.service.pushback.purchase.impl;
//
//import com.topideal.common.constant.DERP;
//import com.topideal.common.constant.DERP_LOG_POINT;
//import com.topideal.common.constant.DERP_ORDER;
//import com.topideal.common.system.annotation.SystemServiceLog;
//import com.topideal.common.tools.TimeUtils;
//import com.topideal.dao.purchase.*;
//import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
//import com.topideal.entity.vo.purchase.PurchaseOrderModel;
//import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
//import com.topideal.entity.vo.purchase.WarehouseOrderRelModel;
//import com.topideal.json.inventory.j04.BackRootJson;
//import com.topideal.service.common.CommonBusinessService;
//import com.topideal.service.pushback.purchase.ConfirmDepotPushbackService;
//import net.sf.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 采购 确认入仓 回推
// * @author zhanghx
// */
//@Service
//public class ConfirmDepotPushbackServiceImpl implements ConfirmDepotPushbackService {
//	/*打印日志*/
//	private static final Logger logger = LoggerFactory.getLogger(ConfirmDepotPushbackServiceImpl.class);
//	@Autowired
//	private PurchaseWarehouseDao purchaseWarehouseDao;
//	// 采购订单
//	@Autowired
//	private PurchaseOrderDao purchaseOrderDao;
//	// 采购订单表体
//	@Autowired
//	private PurchaseOrderItemDao purchaseOrderItemDao;
//	@Autowired
//	private WarehouseOrderRelDao warehouseOrderRelDao;
//	@Autowired
//	private CommonBusinessService commonBusinessService ;
//	@Autowired
//	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;
//
//	/**
//	 * 确认入仓 回推
//	 * @throws SQLException
//	 */
//	@Override
//	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201152700, model = DERP_LOG_POINT.POINT_13201152700_Label,keyword="code")
//	public void modifyStatus(String json, String keys, String topics, String tags) throws SQLException {
//		// 实例化json对象
//		JSONObject jsonData = JSONObject.fromObject(json);
//		// JSON对象转实体
//		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);
//		// 获取采购入库编码
//		String code = rootJson.getCode();
//		PurchaseWarehouseModel warehouseModel = new PurchaseWarehouseModel();
//		warehouseModel.setCode(code);
//		warehouseModel = purchaseWarehouseDao.searchByModel(warehouseModel);
//		if(warehouseModel==null){
//			logger.error(DERP.MQ_FAILTYPE_04 + "采购入库单不存在"+code);
//			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "采购入库单不存在"+code);
//		}
//		//根据入库单关联表查询采购订单
//		WarehouseOrderRelModel warehouseOrderRelModel = new WarehouseOrderRelModel();
//		warehouseOrderRelModel.setWarehouseId(warehouseModel.getId());
//		warehouseOrderRelModel = warehouseOrderRelDao.searchByModel(warehouseOrderRelModel);
//		PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(warehouseOrderRelModel.getPurchaseOrderId());
//
//		if (purchaseOrder.getRate() == null) {
//			commonBusinessService.saveRate(purchaseOrder, warehouseModel.getInboundDate());
//		}
//
//		// 修改入库单状态
//		PurchaseWarehouseModel pwModel = new PurchaseWarehouseModel();
//		pwModel.setId(warehouseModel.getId());
//		pwModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);// 已入仓
//		pwModel.setWarehouseDate(TimeUtils.getNow());// 入仓时间
//
//		purchaseWarehouseDao.modify(pwModel);
//
//		boolean flag = true;//默认已入库完成
//		PurchaseOrderItemModel itemModel = new PurchaseOrderItemModel();
//		itemModel.setPurchaseOrderId(purchaseOrder.getId());
//		List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(itemModel);
//		for(PurchaseOrderItemModel item: itemList){
//			//根据采购明细id获取已入库数量
//			Map<String,Object> paramMap = new HashMap<String,Object>();
//			paramMap.put("purchaseItemId", item.getId());
//			paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
//			List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
//			Integer warehouseNum = 0;
//			if(numList != null && numList.size() > 0){
//				BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
//				warehouseNum = queryNum.intValue();
//			}
//			if(item.getNum().intValue() > warehouseNum.intValue()) {//入库量小于采购数量，未完结入库
//				flag = false ;
//			}
//		}
//
//		if(flag==true){
//			//更新采购单状态为已完结
//			purchaseOrder.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_007);// 已完结
//			purchaseOrder.setEndDate(TimeUtils.getNow());// 完结时间
//			purchaseOrder.setIsEnd(DERP_ORDER.PURCHASEORDER_ISEND_1);// 是否完结
//			purchaseOrderDao.modify(purchaseOrder);
//
//		}else {
//
//			purchaseOrder.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_005);// 部分入库
//			purchaseOrder.setModifyDate(TimeUtils.getNow());
//			purchaseOrderDao.modify(purchaseOrder);
//
//		}
//		//发送金额调整邮件
//		commonBusinessService.sendAmountComfirmMail(purchaseOrder.getId(), warehouseModel.getId()) ;
//	}
//
//}
