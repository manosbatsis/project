package com.topideal.service.order.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.BillOutinDepotDao;
import com.topideal.dao.sale.CrawlerBillDao;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderReturnIdepotDao;
import com.topideal.dao.sale.PlatformTemporaryCostOrderDao;
import com.topideal.dao.sale.PlatformTemporaryCostOrderItemDao;
import com.topideal.dao.sale.WayBillDao;
import com.topideal.dao.sale.YunjiDeliveryDetailDao;
import com.topideal.dao.sale.YunjiReturnDetailDao;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.BillOutinDepotModel;
import com.topideal.entity.vo.sale.CrawlerBillModel;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.entity.vo.sale.OrderReturnIdepotModel;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderModel;
import com.topideal.entity.vo.sale.WayBillModel;
import com.topideal.entity.vo.sale.YunjiDeliveryDetailModel;
import com.topideal.entity.vo.sale.YunjiReturnDetailModel;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.service.order.OrderRollbackDataService;

import net.sf.json.JSONObject;

/**
 业务单据回滚
 */
@Service
public class OrderRollbackDataServiceImpl implements OrderRollbackDataService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderRollbackDataServiceImpl.class);

	// 电商订单
	@Autowired
	private OrderDao orderDao;
	// 电商订单
	@Autowired
	private OrderReturnIdepotDao orderReturnIdepotDao;
	// 运单表
	@Autowired
	private WayBillDao wayBillDao;
	@Autowired
	private BillOutinDepotDao billOutinDepotDao;//账单出入库
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;// 业务唯一表
	@Autowired
	private YunjiDeliveryDetailDao yunjiDeliveryDetailDao;// 云集出库
	@Autowired
	private YunjiReturnDetailDao yunjiReturnDetailDao;// 云集入库
	@Autowired
	private CrawlerBillDao crawlerBillDao;// 唯品账单	
	@Autowired
    private RMQLogProducer rMQLogProducer;// 日志mq
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;//财务经销存关账mongdb
	@Autowired
    private PlatformTemporaryCostOrderDao platformTemporaryCostOrderDao;
	@Autowired
    private PlatformTemporaryCostOrderItemDao platformTemporaryCostOrderItemDao;
	/**
	 * 业务单据回滚
	 * 
	 * @throws Exception
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201159600, model = DERP_LOG_POINT.POINT_13201159600_Label, keyword = "orderNo")
	public boolean synsOrderRollbackData(String json, String keys,String topics,String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		String orderNo = (String) jsonData.get("orderNo");
		String source = (String) jsonData.get("source");
		if (StringUtils.isBlank(orderNo))throw new RuntimeException("orderNo不能为空");	
		if (StringUtils.isBlank(source))throw new RuntimeException("source不能为空");	
		if ("1".equals(source)) {// 电商订单业务回滚
			dSDDRollback(orderNo);
		}else if ("2".equals(source)) {
			billOutinDepot(orderNo);// 账单出入库回滚
		}
		
		// 3.推送日志关闭 日志监控  
		JSONObject logJSONObject=new JSONObject();
		logJSONObject.put("orderNo", orderNo);// 加减明细ids
		logJSONObject.put("source", source);// 1.电商订单 2.账单出入库
		logJSONObject.put("describe", "日志监控关闭订单加减明细失败数据和冻结解冻失败数据");	
		LOGGER.info("推送日志mq关闭日志"+logJSONObject.toString());
        SendResult sendLogResult = rMQLogProducer.send(logJSONObject.toString(), MQLogEnum.LOG_CONSUME_COLSE.getTopic(),MQLogEnum.LOG_CONSUME_COLSE.getTags());
		return true;
	}
	/**
	 * 2.账单出入库回滚
	 * @param orderNo
	 * @throws SQLException 
	 */
	private void billOutinDepot(String orderNo) throws SQLException {
		// 查询电商订单
		BillOutinDepotModel orderQuery =new BillOutinDepotModel();
		orderQuery.setCode(orderNo);
		orderQuery = billOutinDepotDao.searchByModel(orderQuery);

		if (orderQuery==null) {
			throw new RuntimeException("没有找到对应的账单出入库订单");
		}
		
		// 修改账单出入库订单为已删除
		BillOutinDepotModel orderModel=new BillOutinDepotModel();
		orderModel.setState("006");
		orderModel.setModifyDate(TimeUtils.getNow());
		orderModel.setId(orderQuery.getId());
		billOutinDepotDao.modify(orderModel);
		// 来源修改云集数据为 未使用
		if (DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_1.equals(orderQuery.getBillSource())) {// 来源云集
			if (DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_0.equals(orderQuery.getOperateType())) {//调减 修改云集出库数据
				YunjiDeliveryDetailModel model =new YunjiDeliveryDetailModel();
				model.setBillOutinCode(orderNo);
				yunjiDeliveryDetailDao.updateNotUsed(model);
			}else if (DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_1.equals(orderQuery.getOperateType())) {// 调增修改云集退货数据
				YunjiReturnDetailModel model =new YunjiReturnDetailModel();
				model.setBillOutinCode(orderNo);
				yunjiReturnDetailDao.updateNotUsed(model);
			}

		}else if (DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_2.equals(orderQuery.getBillSource())) {// 来源唯品
			CrawlerBillModel model =new CrawlerBillModel();
			model.setOutCode(orderNo);
			crawlerBillDao.updateNotUsed(model);
		}
		
	}
	/**
	 * 1.电商订单回滚操作
	 * @param jsonData
	 * @throws SQLException 
	 */
	private void dSDDRollback(String orderNo) throws SQLException {
		if (StringUtils.isBlank(orderNo))return;
		// 查询电商订单
		OrderModel orderQuery=new OrderModel();
		orderQuery.setCode(orderNo);
		orderQuery = orderDao.searchByModel(orderQuery);	
		if (orderQuery==null) {
			throw new RuntimeException("没有找到对应的电商订单");
		}
		
		// 修改电商订单为已删除
		OrderModel orderModel=new OrderModel();
		orderModel.setStatus("006");
		orderModel.setModifyDate(TimeUtils.getNow());
		orderModel.setId(orderQuery.getId());
		orderDao.modify(orderModel);// 修改电商订单
		// 来源是第e仓的 修改运单单据单号
		WayBillModel wayBillQuery=null;
		if (!"4".equals(orderQuery.getOrderSource().toString())) {
			wayBillQuery =new WayBillModel();
			wayBillQuery.setOrderId(orderQuery.getId());
			wayBillQuery = wayBillDao.searchByModel(wayBillQuery);
		}
		String externalCode = orderQuery.getExternalCode();
		// 修改运单号 运单号拼接A
		if (wayBillQuery!=null) {
			WayBillModel wayBillModel=new WayBillModel();
			wayBillModel.setId(wayBillQuery.getId());
			wayBillModel.setWayBillNo(wayBillQuery.getWayBillNo()+"A");
			wayBillModel.setModifyDate(TimeUtils.getNow());
			wayBillDao.modify(wayBillModel);			
		}
		// 来源第e仓 单号拼接删除唯一
		if ("3".equals(orderQuery.getOrderSource().toString())) {
			externalCode=externalCode+orderQuery.getMerchantId();
		}
		// 删除唯一表
		OrderExternalCodeModel orderExternalCodeQuery=new OrderExternalCodeModel();
		orderExternalCodeQuery.setExternalCode(externalCode);
		List<OrderExternalCodeModel> orderExternalCodeList = orderExternalCodeDao.list(orderExternalCodeQuery);
		List ids=new ArrayList();
		for (OrderExternalCodeModel externalModel : orderExternalCodeList) {
			ids.add(externalModel.getId());
		}
		if (ids.size()>0) {
			orderExternalCodeDao.delete(ids);
		}
		// 删除电商订单退货 类型是退款的单据  (状态删除)
		OrderReturnIdepotModel orderReturnIdepot=new  OrderReturnIdepotModel();
		orderReturnIdepot.setOrderCode(orderNo);
		orderReturnIdepot.setStatus(DERP_ORDER.ORDER_RETURN_STATUS_013);
		List<OrderReturnIdepotModel> list = orderReturnIdepotDao.list(orderReturnIdepot);
		List<String>orderCodeList=new ArrayList<>();
		orderCodeList.add(orderNo);
		for (OrderReturnIdepotModel orderReturnIdepotModel : list) {
			OrderReturnIdepotModel modifyOrder=new  OrderReturnIdepotModel();
			modifyOrder.setId(orderReturnIdepotModel.getId());
			modifyOrder.setStatus("006");
			modifyOrder.setModifyDate(TimeUtils.getNow());
			orderReturnIdepotDao.modify(modifyOrder);
			orderCodeList.add(orderReturnIdepotModel.getCode());
		}

			platformTemporaryCostOrderDao.deleteByOrderCode(orderCodeList);
			platformTemporaryCostOrderItemDao.deleteByOrderCode(orderCodeList);

		
	}

}









