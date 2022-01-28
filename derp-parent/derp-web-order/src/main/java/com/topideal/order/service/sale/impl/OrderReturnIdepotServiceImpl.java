package com.topideal.order.service.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.sale.OrderReturnIdepotBatchDTO;
import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;
import com.topideal.entity.dto.sale.OrderReturnIdepotItemDTO;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.sale.OrderReturnIdepotService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 电商订单退货service实现类
 */
@Service
public class OrderReturnIdepotServiceImpl implements OrderReturnIdepotService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(OrderReturnIdepotServiceImpl.class);
	// 电商订单退货表头
	@Autowired
	private OrderReturnIdepotDao orderReturnIdepotDao;
	// 电商订单退货表体
	@Autowired
	private OrderReturnIdepotItemDao orderReturnIdepotItemDao;
	//运单表体
	@Autowired
	private OrderReturnIdepotBatchDao orderReturnIdepotBatchDao;
	// 电商订单
	@Autowired
	private OrderDao orderDao;
	// 仓库Mongo
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	//商品信息MongoDB
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao ;
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;//财务经销存关账mongdb
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;
	@Autowired
	private MerchantShopShipperMongoDao merchantShopShipperMongoDao;

	/**
	 * 列表（分页）
	 * @return
	 */
	@Override
	public OrderReturnIdepotDTO listOrderByPage(OrderReturnIdepotDTO dto) throws SQLException {
		return orderReturnIdepotDao.searchDTOByPage(dto);
	}

	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@Override
	public OrderReturnIdepotDTO searchDetail(Long id) throws SQLException {
		OrderReturnIdepotDTO dto = new OrderReturnIdepotDTO();
		dto.setId(id);
		return orderReturnIdepotDao.searchDTOById(id);
	}

	/**
	 * 根据表头ID获取详细信息(包括商品批次信息)
	 * @param id
	 * @return
	 */
	@Override
	public List<OrderReturnIdepotItemDTO> listItemByOrderId(Long id) throws SQLException {
		OrderReturnIdepotItemDTO item = new OrderReturnIdepotItemDTO();
		item.setOreturnIdepotId(id);
		return orderReturnIdepotItemDao.listOrderReturnIdepotItemDTO(item);
	}
	/**
	 * 根据条件获取电商订单信息（包括表体信息）
	 * @return
	 */
	@Override
	public OrderReturnIdepotDTO listOrderAndItemByPage(OrderReturnIdepotDTO dto) throws SQLException {
		OrderReturnIdepotDTO orderData = orderReturnIdepotDao.newDTOListByPage(dto);//查出表头数据
		return orderData;
	}
	/**
	 * 根据条件获取电商订单
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int listOrder(OrderReturnIdepotDTO dto) throws SQLException {
		return orderReturnIdepotDao.queryDTOList(dto);
	}
	/**
	 * 根据ids获取需要导出的数据
	 */
	@Override
	public List<Map<String, Object>> getExportOrderMap(OrderReturnIdepotDTO dto) throws SQLException {
		List<Map<String, Object>> resultMap = orderReturnIdepotDao.getExportOrderDTOMap(dto);
		for(Map<String, Object> map : resultMap) {
			String afterSaleType = (String) map.get("after_sale_type");
			String returnOrderType = (String) map.get("return_order_type");

			String afterSaleTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_returnAfterSaleTypeList, afterSaleType);
			String returnOrderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.orderReturn_returnOderTypeList, returnOrderType);

			map.put("after_sale_type", afterSaleTypeLabel);
			map.put("return_order_type", returnOrderTypeLabel);

			map.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, (String) map.get("store_platform_code")));
			map.put("currency", DERP.getLabelByKey(DERP.currencyCodeList, (String) map.get("currency")));
			map.put("shopTypeName", DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, (String) map.get("shop_type_code")));
			// 脱敏
			map.put("buyer_name", StrUtils.desensitization((String) map.get("buyer_name")));
			map.put("buyer_phone", StrUtils.desensitization((String) map.get("buyer_phone")));
			map.put("return_addr", StrUtils.desensitization((String) map.get("return_addr")));

		}
		return resultMap;
	}

	@Override
	public List<OrderReturnIdepotBatchDTO> listOrderReturnBatchById(List<Long> itemIdList) {
		return orderReturnIdepotBatchDao.listOrderReturnBatchDTOById(itemIdList);
	}

	@Override
	public Map<String,Object> getOrderDataList(Long merchantId) throws SQLException {
		if(null != merchantId && merchantId == 0){
			merchantId = null;
		}
		//结果集
		List<String> dayCountList = new ArrayList<String>();
		List<String> totalCountList = new ArrayList<String>();
		//获取当前时间
		Timestamp now = TimeUtils.getNow();
		String nowStr = TimeUtils.format(now, "yyyy-MM-dd");
		String endDate = nowStr+" 23:59:59";
		//获取15天前的日期
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 14);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = format.format(calendar.getTime())+" 00:00:00";
		//获取某时间段每天的数量
		List<Map<String, Object>> list = orderReturnIdepotDao.getOrderDayList(startDate,endDate,merchantId);
	    //获取时间段之前的数量
	    List<OrderReturnIdepotModel> orderList = orderReturnIdepotDao.getOrderListforDate(startDate,merchantId);
	    Integer beforeCount = orderList.size();
	    Integer maxCount = 0;
	    //遍历组装数据
        int day = 14;
        List<String> pastDaysList = new ArrayList<String>();//获取时间段的时间 MM-dd
        for (int i = 0; i <15; i++) {
        	Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.DAY_OF_YEAR, calendar1.get(Calendar.DAY_OF_YEAR) - day);
            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd");
            String date = format1.format(calendar1.getTime());
            pastDaysList.add(date);
            day--;
            int count = 0;
			for(Map<String, Object> map :list){
				if(map.get("days").equals(date)){
					count = Integer.valueOf(map.get("count").toString());
					break;
				}
			}
			if(count>maxCount){
				maxCount = count;
			}
			dayCountList.add(String.valueOf(count));
			totalCountList.add(String.valueOf(count+beforeCount));
			beforeCount = count+beforeCount;
        }
        //计算y轴的刻度与最大值
        Integer scale1 = new BigDecimal(maxCount).divide(new BigDecimal(10000),0,BigDecimal.ROUND_UP).multiply(new BigDecimal(1000)).intValue();
        Integer yMax1 = scale1*10;
        Integer scale2 = new BigDecimal(beforeCount).divide(new BigDecimal(100000),0,BigDecimal.ROUND_UP).multiply(new BigDecimal(10000)).intValue();
        Integer yMax2 = scale2*10;
	    Map<String,Object> result = new HashMap<String,Object>();
	    result.put("dayCountList", dayCountList);
	    result.put("totalCountList", totalCountList);
	    result.put("pastDaysList", pastDaysList);
	    result.put("scale1", scale1);
	    result.put("yMax1", yMax1);
	    result.put("scale2", scale2);
	    result.put("yMax2", yMax2);
		return result;
	}



	@Override
	public Integer queryListCount(OrderReturnIdepotDTO dto) throws SQLException {
		return orderReturnIdepotDao.queryDTOListCount(dto);
	}
	/**
	 * 获取列表数据（表头）-分页
	 * @return
	 */
	@Override
	public OrderReturnIdepotDTO newListByPage(OrderReturnIdepotDTO dto) throws SQLException {
		return orderReturnIdepotDao.newDTOListByPage(dto);
	}

	/**
	 * 判断输入字段是否为空
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index , String content ,
			String msg ,List<ImportErrorMessage> resultList ) {

		if(StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);

			return true ;

		}else {
			return false ;
		}

	}
	@Override
	public boolean checkConditionsOrder(List<Long> list) throws Exception {
		int count = orderReturnIdepotDao.getImportOrderCountByIds(list) ;
		if(list.size() != count) {
			return false ;
		}else {
			return true ;
		}
	}

	/**
	 * 错误时，设置错误内容
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}

	@Override
	public int delImportOrder(List<Long> list) throws Exception {

		int count = orderReturnIdepotDao.getImportOrderCountByIds(list) ;
		if(list.size() != count) {
			throw new RuntimeException("删除失败，所选项存在非手工导入订单") ;
		}

		int total = 0 ;

		for (Long id : list) {
			OrderReturnIdepotModel OrderReturnIdepotModel = orderReturnIdepotDao.searchById(id) ;
			if(OrderReturnIdepotModel != null) {
				if(!"011".equals(OrderReturnIdepotModel.getStatus())) {
					throw new RuntimeException("删除失败，订单号：" + OrderReturnIdepotModel.getCode() + "状态不为：待审核") ;
				}
				OrderReturnIdepotModel tempModel = new OrderReturnIdepotModel() ;
				tempModel.setId(OrderReturnIdepotModel.getId());
				tempModel.setStatus(DERP.DEL_CODE_006);	// 已删除
				tempModel.setModifyDate(TimeUtils.getNow());
				total += orderReturnIdepotDao.modify(tempModel);
			}
		}

		return total;
	}



	@Override
	public void getOrderGoodsInfo(List<Long> ids,Long merchantId) throws Exception {
		for (Long id : ids) {
			OrderReturnIdepotModel orderReturnIdepotModel = orderReturnIdepotDao.searchById(id);

			if (DERP_ORDER.ORDER_RETURN_STATUS_012.equals(orderReturnIdepotModel.getStatus())) {
				throw new RuntimeException("审核失败，订单："+orderReturnIdepotModel.getCode()+"的单据状态为：已入仓");
			}else if (DERP_ORDER.ORDER_RETURN_STATUS_028.equals(orderReturnIdepotModel.getStatus())) {
				throw new RuntimeException("审核失败，订单："+orderReturnIdepotModel.getCode()+"的单据状态为：入仓中");
			}else if(DERP_ORDER.ORDER_RETURN_STATUS_013.equals(orderReturnIdepotModel.getStatus())) {
				throw new RuntimeException("审核失败，订单："+orderReturnIdepotModel.getCode()+"的单据状态为：已退款");
			}
			/**
			 * 退款单，退款完结时间不能为空，且判断入库时间是否已关账
			 * 退货单，退款入库时间不能为空，且判断入库时间是否已关账/已月结
			 */
			if(DERP_ORDER.ORDERRETURN_RETURN_ORDER_TYPE_0.equals(orderReturnIdepotModel.getReturnOrderType())) {
				//  退款完结时间不能为空
				if (orderReturnIdepotModel.getRefundEndDate() == null) {
					throw new RuntimeException("审核失败，订单："+orderReturnIdepotModel.getCode()+"的退款完结时间不能为空") ;
				}
			}else if(DERP_ORDER.ORDERRETURN_RETURN_ORDER_TYPE_1.equals(orderReturnIdepotModel.getReturnOrderType())){
				//  退货入库时间不能为空
				if (orderReturnIdepotModel.getReturnInDate()==null) {
					throw new RuntimeException("审核失败，订单："+orderReturnIdepotModel.getCode()+"的退货入库时间不能为空") ;
				}
			}
			OrderReturnIdepotItemModel itemModel = new OrderReturnIdepotItemModel() ;
			itemModel.setOreturnIdepotId(id);
			List<OrderReturnIdepotItemModel> itemList = orderReturnIdepotItemDao.list(itemModel);		//获取表体
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			for(OrderReturnIdepotItemModel item:itemList){
				// 获取最大的关账日/月结日期
				closeAccountsMongo.setMerchantId(orderReturnIdepotModel.getMerchantId());
				closeAccountsMongo.setDepotId(orderReturnIdepotModel.getReturnInDepotId());
				closeAccountsMongo.setBuId(item.getBuId());
				String maxdate = "";
				if(DERP_ORDER.ORDERRETURN_RETURN_ORDER_TYPE_0.equals(orderReturnIdepotModel.getReturnOrderType())){//退款单
					//查询关账日期
					maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
					String maxCloseAccountsMonth="";
					if (StringUtils.isNotBlank(maxdate)) {
						// 获取该月份下月时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
						maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
					}
					if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
						// 关账下个月日期大于 完结日期
						if (orderReturnIdepotModel.getRefundEndDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
							throw new RuntimeException("审核失败，订单："+orderReturnIdepotModel.getCode()+"的单据退款完结时间不能小于关账日期") ;
						}
					}
				}else if(DERP_ORDER.ORDERRETURN_RETURN_ORDER_TYPE_1.equals(orderReturnIdepotModel.getReturnOrderType())){//退货单
					maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);

					String maxCloseAccountsMonth="";
					if (StringUtils.isNotBlank(maxdate)) {
						// 获取该月份下月时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
						maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
					}
					if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
						// 关账下个月日期大于 入库日期
						if (orderReturnIdepotModel.getReturnInDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
							throw new RuntimeException("审核失败，订单："+orderReturnIdepotModel.getCode()+"的单据退货入库时间不能小于关账日期/月结") ;
						}
					}
				}
			}
		}
	}

	@Override
	public void examineOrder(List<Long> list ,String topidealCode,User user) throws Exception {
		for (Long id : list) {
			OrderReturnIdepotModel orderReturnIdepotModel = orderReturnIdepotDao.searchById(id);
			if(orderReturnIdepotModel == null){
				throw new RuntimeException("电商退货订单不存在");
			}
			if (DERP_ORDER.ORDER_RETURN_STATUS_012.equals(orderReturnIdepotModel.getStatus())) {
				throw new RuntimeException("电商退货订单已入仓,退货订单编号" + orderReturnIdepotModel.getCode());
			}else if (DERP_ORDER.ORDER_RETURN_STATUS_028.equals(orderReturnIdepotModel.getStatus())) {
				throw new RuntimeException("电商退货订单入仓中,退货订单编号" + orderReturnIdepotModel.getCode());
			}else if (DERP_ORDER.ORDER_RETURN_STATUS_013.equals(orderReturnIdepotModel.getStatus())) {
				throw new RuntimeException("电商退货订单已退款,退货订单编号" + orderReturnIdepotModel.getCode());
			}

			/**
			 * 1、若退款单，点击审核成功直接更新单据状态为“已退款”，无需推送库存加减接口，不涉及库存变动；
			 *
			 * 2、若退货单，点击审核成功直接更新单据状态为“已入库”，需推送库存加减接口，涉及库存变动；
			 */
			if(DERP_ORDER.ORDERRETURN_RETURN_ORDER_TYPE_0.equals(orderReturnIdepotModel.getReturnOrderType())) {
				// 修改单据状态
				OrderReturnIdepotModel oModel = new OrderReturnIdepotModel();
				oModel.setId(orderReturnIdepotModel.getId());
				oModel.setStatus(DERP_ORDER.ORDER_RETURN_STATUS_013);// 已退款
				oModel.setModifyDate(TimeUtils.getNow());
				oModel.setAuditDate(TimeUtils.getNow());
				oModel.setAuditName(user.getName());
				oModel.setAuditor(user.getId());
				orderReturnIdepotDao.modify(oModel);

			}else if(DERP_ORDER.ORDERRETURN_RETURN_ORDER_TYPE_1.equals(orderReturnIdepotModel.getReturnOrderType())) {
				// 修改单据状态
				OrderReturnIdepotModel oModel = new OrderReturnIdepotModel();
				oModel.setId(orderReturnIdepotModel.getId());
				oModel.setStatus(DERP_ORDER.ORDER_RETURN_STATUS_028);// 入仓中
				oModel.setModifyDate(TimeUtils.getNow());
				oModel.setAuditDate(TimeUtils.getNow());
				oModel.setAuditName(user.getName());
				oModel.setAuditor(user.getId());
				orderReturnIdepotDao.modify(oModel);


				Map<String, Object> depotInfo_params = new HashMap<String, Object>();
				depotInfo_params.put("depotId", orderReturnIdepotModel.getReturnInDepotId());// 根据仓库id
				DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
				if(DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType()) && StringUtils.isBlank(orderReturnIdepotModel.getTallyingUnit())){
					throw new RuntimeException("入库仓为海外仓，理货单位不能为空");
				}

				String inventoryUnit = "";//理货单位
				if(StringUtils.isNotBlank(orderReturnIdepotModel.getTallyingUnit())){
					if (DERP.ORDER_TALLYINGUNIT_00.equals(orderReturnIdepotModel.getTallyingUnit())) {
						inventoryUnit = DERP.INVENTORY_UNIT_0;
					} else if (DERP.ORDER_TALLYINGUNIT_01.equals(orderReturnIdepotModel.getTallyingUnit())) {
						inventoryUnit = DERP.INVENTORY_UNIT_1;
					} else if (DERP.ORDER_TALLYINGUNIT_02.equals(orderReturnIdepotModel.getTallyingUnit())) {
						inventoryUnit = DERP.INVENTORY_UNIT_2;
					}
				}

				//获取电商订单明细
				List<OrderReturnIdepotItemDTO> itemList = listItemByOrderId(id);

				if(itemList == null){
					throw new RuntimeException("找不到对应电商退货商品信息");
				}
				List<Long> itemIdList=new ArrayList<>();
				for (int i = 0; i < itemList.size(); i++) {
					itemIdList.add(itemList.get(i).getId());
					if(null==itemList.get(i).getBuId()){
						throw new RuntimeException("找不到对应电商退货订单表体中事业部信息");
					}
				}
				List<OrderReturnIdepotBatchDTO> batchAllList  = listOrderReturnBatchById(itemIdList);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				//扣减销售出库库存量
				Date date = new Date();
				String now1 = sdf.format(date);
				InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
				invetAddOrSubtractRootJson.setMerchantId(orderReturnIdepotModel.getMerchantId().toString());
				invetAddOrSubtractRootJson.setMerchantName(orderReturnIdepotModel.getMerchantName());
				invetAddOrSubtractRootJson.setTopidealCode(topidealCode);
				invetAddOrSubtractRootJson.setDepotId(mongo.getDepotId().toString());
				invetAddOrSubtractRootJson.setDepotName(mongo.getName());
				invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
				invetAddOrSubtractRootJson.setDepotType(mongo.getType());
				invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
				invetAddOrSubtractRootJson.setOrderNo(orderReturnIdepotModel.getCode());
				invetAddOrSubtractRootJson.setBusinessNo(orderReturnIdepotModel.getCode());
				invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0015);
				invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0026);
				invetAddOrSubtractRootJson.setSourceDate(now1);
				invetAddOrSubtractRootJson.setDivergenceDate(TimeUtils.format(orderReturnIdepotModel.getReturnInDate(), "yyyy-MM-dd HH:mm:ss"));	//退货入库时间
				// 获取当前年月
				invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(orderReturnIdepotModel.getReturnInDate()));	//出入时间
				List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
				SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

				for (OrderReturnIdepotBatchDTO item : batchAllList) {
					// 根据电商退货单id和商品id得到商品信息
					OrderReturnIdepotItemModel orderReturnIdepotItemModel = new OrderReturnIdepotItemModel();
					orderReturnIdepotItemModel.setOreturnIdepotId(orderReturnIdepotModel.getId());
					orderReturnIdepotItemModel.setInGoodsId(item.getGoodsId());
					List<OrderReturnIdepotItemModel> returnItemList = orderReturnIdepotItemDao.list(orderReturnIdepotItemModel);
					OrderReturnIdepotItemModel oReturnIdepotModel= returnItemList.get(0);
					// 好品
					if (item.getReturnNum() != null && item.getReturnNum() > 0) {
						InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

						invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
						invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
						invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
						invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
						invetAddOrSubtractGoodsListJson.setType("0");//商品分类 （0 好品 1坏品 ）
						invetAddOrSubtractGoodsListJson.setNum(item.getReturnNum());
						invetAddOrSubtractGoodsListJson.setOperateType("1");// 字符串 0 调减 1调增
						invetAddOrSubtractGoodsListJson.setBatchNo(item.getBatchNo());
						invetAddOrSubtractGoodsListJson.setIsExpire("1");
						// 事业部
						invetAddOrSubtractGoodsListJson.setBuId(String.valueOf(returnItemList.get(0).getBuId()));
						invetAddOrSubtractGoodsListJson.setBuName(returnItemList.get(0).getBuName());
						if (item.getProductionDate()!=null) {
							Date productionDateTimestamp  = item.getProductionDate();
							invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(productionDateTimestamp));
						}
						if (item.getOverdueDate()!=null) {
							Date overdueDateTimestamp = item.getOverdueDate();
							invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
							Timestamp currentDate = new Timestamp(date.getTime());
							if(currentDate.before(item.getOverdueDate())){
								//currentDate 时间比 item.getOverdueDate() 时间早(未过期)
								invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期  （0 是 1 否）
							}else{
								invetAddOrSubtractGoodsListJson.setIsExpire("0");//是否过期  （0 是 1 否）
							}
						}
						if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
							invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);
						}
						invetAddOrSubtractGoodsListJson.setStockLocationTypeId(oReturnIdepotModel.getStockLocationTypeId() == null ? "" : oReturnIdepotModel.getStockLocationTypeId().toString());
						invetAddOrSubtractGoodsListJson.setStockLocationTypeName(oReturnIdepotModel.getStockLocationTypeName());
						invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
					}
					// 坏品
					if (item.getBadGoodsNum() != null && item.getBadGoodsNum() > 0) {
						InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

						invetAddOrSubtractGoodsListJson.setGoodsId(item.getGoodsId().toString());
						invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
						invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
						invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());

						invetAddOrSubtractGoodsListJson.setType("1");	//商品分类 （0 好品 1坏品 ）
						invetAddOrSubtractGoodsListJson.setNum(item.getBadGoodsNum());
						invetAddOrSubtractGoodsListJson.setOperateType("1");// 字符串 0 调减 1调增
						invetAddOrSubtractGoodsListJson.setBatchNo(item.getBatchNo());
						invetAddOrSubtractGoodsListJson.setIsExpire("1");
						// 事业部
						invetAddOrSubtractGoodsListJson.setBuId(String.valueOf(returnItemList.get(0).getBuId()));
						invetAddOrSubtractGoodsListJson.setBuName(returnItemList.get(0).getBuName());
						if (item.getProductionDate()!=null) {
							Date productionDateTimestamp  = item.getProductionDate();
							invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(productionDateTimestamp));
						}
						if (item.getOverdueDate()!=null) {
							Date overdueDateTimestamp = item.getOverdueDate();
							invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
							Timestamp currentDate = new Timestamp(date.getTime());
							if(currentDate.before(item.getOverdueDate())){
								//currentDate 时间比 item.getOverdueDate() 时间早(未过期)
								invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期  （0 是 1 否）
							}else{
								invetAddOrSubtractGoodsListJson.setIsExpire("0");//是否过期  （0 是 1 否）
							}
						}
						if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
							invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);
						}
						invetAddOrSubtractGoodsListJson.setStockLocationTypeId(oReturnIdepotModel.getStockLocationTypeId() == null ? "" : oReturnIdepotModel.getStockLocationTypeId().toString());
						invetAddOrSubtractGoodsListJson.setStockLocationTypeName(oReturnIdepotModel.getStockLocationTypeName());
						invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
					}
					invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
				}

				//库存回推数据
				Map<String, Object> customParam=new HashMap<String, Object>();
				invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.ORDER_RETURN_IDEPOT_PUSH_BACK.getTags());//回调标签
				invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.ORDER_RETURN_IDEPOT_PUSH_BACK.getTopic());//回调主题
				customParam.put("code", orderReturnIdepotModel.getCode());// 电商退货订单内部单号
				invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
				rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
			}
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_22, orderReturnIdepotModel.getCode(), "审核", null, null);
		}
	}

	@Override
	public Map<String, Object> importOrder(User user, List<List<Map<String, String>>> data,String type) throws Exception {

		// 查询商家
		Map<String, Object> merchantMap = new HashMap<String, Object>();
		merchantMap.put("merchantId", user.getMerchantId());
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);
		boolean isTradeMechant = "1".equals(merchant.getRegisteredType()) ? true : false;//是否是内贸主体

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> objects = data.get(0);
		//1-退货导入 2-退款导入
		if("1".equals(type)){
			map = saveReturnOrder(objects, isTradeMechant, user);
		}else if("2".equals(type)){
			map = saveRefundOrder(objects, isTradeMechant, user);
		}

		return map;

	}
	/**
	 * 退货信息
	 * @param objects
	 */
	private Map<String, Object> saveReturnOrder(List<Map<String, String>> objects , Boolean isTradeMechant,User user) throws Exception {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int failure = 0;
		Map<String, OrderReturnIdepotModel> modelMap = new HashMap<String, OrderReturnIdepotModel>();
		//外部单号-退货表体集合i
		Map<String, List<OrderReturnIdepotItemModel>> itemMap = new HashMap<>() ;
		//外部单号货号-退货批次集合
		Map<String, List<OrderReturnIdepotBatchModel>> batchItemMap = new HashMap<>() ;

		Map<String,BuStockLocationTypeConfigMongo> stockLocationMap = new HashMap<String,BuStockLocationTypeConfigMongo>();//记录库存类型信息
		for (int i = 1; i <= objects.size(); i++) {
			Map<String, String> map = objects.get(i - 1);

			String buCode = map.get("事业部编码") ;
			if(checkIsNullOrNot(i, buCode, "事业部不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			buCode = buCode.trim();
			// 获取该事业部信息
			Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
			merchantBuRelParam.put("merchantId", user.getMerchantId());
			merchantBuRelParam.put("buCode", buCode);
			merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
			MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
			if (merchantBuRelMongo == null ) {
				setErrorMsg(i,  "事业部编码为：" + buCode + ",未查到该公司下事业部信息", resultList);
				failure += 1;
				continue;
			}
			// 校验事业部与当前账号所绑定的事业部是否匹配
			boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), merchantBuRelMongo.getBuId());
			if (!userRelateBu) {
				setErrorMsg(i,  "事业部编码为：" + merchantBuRelMongo.getBuCode() + ",用户id：" + user.getId() + ",无权限操作该事业部", resultList);
				failure += 1;
				continue;
			}

			String shopCode = map.get("店铺编码") ;
			if(checkIsNullOrNot(i, shopCode, "店铺编码不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			shopCode = shopCode.trim() ;
			Map<String ,Object> params = new HashMap<>();
			params.put("shopCode", shopCode) ;
			params.put("status", "1") ;
			MerchantShopRelMongo shopRel = merchantShopRelMongoDao.findOne(params);
			if(shopRel == null) {
				setErrorMsg(i, "店铺对应平台信息不存在", resultList);
				failure += 1;
				continue ;
			}

			String indepotCode = map.get("入库仓库编码") ;
			if(checkIsNullOrNot(i, indepotCode, "入库仓库编码不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			indepotCode = indepotCode.trim();
			/**从电商订单取仓库信息*/
			Map<String, Object> depotParmas = new HashMap<>() ;
			depotParmas.put("depotCode", indepotCode);
			depotParmas.put("status", "1");
			DepotInfoMongo inDepotInfo = depotInfoMongoDao.findOne(depotParmas);
			if (inDepotInfo == null) {
				setErrorMsg(i, "入库仓库不存在或未启用", resultList);
				failure += 1;
				continue ;
			}
			// 仓库公司关联表
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", user.getMerchantId());
			depotMerchantRelParam.put("depotId", inDepotInfo.getDepotId());
			DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
			if (depotMerchantRelMongo == null ) {
				setErrorMsg(i, "入库仓库编码为：" + indepotCode + ",未查到该公司下入库仓库信息", resultList);
				failure += 1;
				continue ;
			}
			// 校验公司-仓库-事业部的关联表
			Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
			merchantDepotBuRelParam.put("merchantId", user.getMerchantId());
			merchantDepotBuRelParam.put("depotId", inDepotInfo.getDepotId()); // 出仓仓库
			merchantDepotBuRelParam.put("buId", merchantBuRelMongo.getBuId());
			MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
			if (outMerchantDepotBuRelMongo == null) {
				setErrorMsg(i, "事业部编码为：" + merchantBuRelMongo.getBuCode() + ",入库仓库：" + indepotCode+ ",未查到该公司仓库事业部关联信息", resultList);
				failure += 1;
				continue;
			}
			if(!shopRel.getDepotId().equals(inDepotInfo.getDepotId())){
				setErrorMsg(i, "导入的入库仓库在商家店铺信息表中没有对应的仓库", resultList);
				failure += 1;
				continue ;
			}
			// 退货入库时间
			String returnInDate = map.get("退货入库时间") ;
			if(checkIsNullOrNot(i, returnInDate, "退货入库时间不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			returnInDate = returnInDate.trim();

			if(!TimeUtils.isYmdDate(returnInDate)) {
				setErrorMsg(i, "退货入库时间格式有误,正确格式为:yyyy-MM-dd", resultList);
				failure += 1;
				continue ;
			}
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setDepotId(inDepotInfo.getDepotId());
			closeAccountsMongo.setBuId(merchantBuRelMongo.getBuId());
			String maxdate = "";
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);


			String maxCloseAccountsMonth="";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}
			// 关账日期和退货入库时间比较
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 退货入库时间
				if (TimeUtils.parse(returnInDate, "yyyy-MM-dd").getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(i, "退货入库时间必须大于关账日期/月结日期", resultList);
					failure += 1;
					continue ;
				}
			}
			String goodsNo = map.get("商品货号") ;
			if(checkIsNullOrNot(i, goodsNo, "商品货号不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			goodsNo = goodsNo.trim();

			Map<String, Object> merchandiseParams = new HashMap<>() ;
			merchandiseParams.put("goodsNo", goodsNo) ;
			merchandiseParams.put("merchantId", user.getMerchantId());
			merchandiseParams.put("status",DERP_SYS.MERCHANDISEINFO_STATUS_1);
			List<MerchandiseInfoMogo> merchandises = merchandiseInfoMogoDao.findMerchandiseByDepotId(merchandiseParams,inDepotInfo.getDepotId());
			if(merchandises == null || merchandises.size() < 1) {
				setErrorMsg(i, "入库仓编码："+indepotCode+"，未找到关联商品货号："+goodsNo, resultList);
				failure += 1;
				continue ;
			}
			MerchandiseInfoMogo merchandise = merchandises.get(0);
			String normalNumStr = map.get("退货正品数量") ;
			if(checkIsNullOrNot(i, normalNumStr, "退货正品数量不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			normalNumStr = normalNumStr.trim();
			if(!StringUtils.isNumeric(normalNumStr)){
				setErrorMsg(i, "退货正品数量必须是整数", resultList);
				failure += 1;
				continue ;
			}

			String wornNumStr = map.get("退货残品数量") ;
			if(checkIsNullOrNot(i, wornNumStr, "退货残品数量不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			wornNumStr = wornNumStr.trim();
			if(!StringUtils.isNumeric(normalNumStr)){
				setErrorMsg(i, "退货残品数量必须是整数", resultList);
				failure += 1;
				continue ;
			}

			String batchNo = map.get("批次号") ;
			String produceDate = map.get("生产日期") ;
			String overdueDate = map.get("失效日期") ;

			//批次强校验
			if(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepotInfo.getBatchValidation()) || DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(inDepotInfo.getBatchValidation())) {

				if(checkIsNullOrNot(i, batchNo, "批次效期强校验的仓库,批次不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				batchNo = batchNo.trim() ;

				if(checkIsNullOrNot(i, produceDate, "批次效期强校验的仓库,生产日期不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				produceDate = produceDate.trim() ;

				if(checkIsNullOrNot(i, overdueDate, "批次效期强校验的仓库,失效日期不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				overdueDate = overdueDate.trim() ;

			}

			if(StringUtils.isNotBlank(produceDate) && !isYmdDate(produceDate)) {
				setErrorMsg(i, "生产日期时间格式有误,正确格式为:yyyy-MM-dd", resultList);
				failure += 1;
				continue ;
			}

			if(StringUtils.isNotBlank(overdueDate) && !isYmdDate(overdueDate)) {
				setErrorMsg(i, "失效日期时间格式有误,正确格式为:yyyy-MM-dd", resultList);
				failure += 1;
				continue ;
			}
			String tallyingUnit = map.get("海外仓理货单位") ;
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfo.getType()) && StringUtils.isBlank(tallyingUnit)){
				setErrorMsg(i, "入库仓为海外仓，海外仓理货单位不能为空", resultList);
				failure += 1;
				continue ;
			}
			if(StringUtils.isNotBlank(tallyingUnit)){
				String tallingUnitName = DERP_SYS.getLabelByKey(DERP.order_tallyingUnitList,tallyingUnit);
				if(StringUtils.isBlank(tallingUnitName)){
					setErrorMsg(i, "海外仓理货单位填写有误，理货单位：00-托盘 01-箱 02-件", resultList);
					failure += 1;
					continue ;
				}
			}
			BuStockLocationTypeConfigMongo stockLocationTypeConfigMongo = stockLocationMap.get(shopCode);
			if(stockLocationTypeConfigMongo == null) {
				Long stockLocationTypeId = null;
				//若为单主店则取店铺信息配置表头的“库位类型”；若为“外部店”则取对应的货主事业部的“库位类型”
				if (DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(shopRel.getStoreTypeCode())) {
					stockLocationTypeId = shopRel.getStockLocationTypeId();
				} else if (DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(shopRel.getStoreTypeCode())) {
					params.clear();
					params.put("shopId", shopRel.getShopId());
					params.put("merchantId", user.getMerchantId());
					params.put("buId", merchantBuRelMongo.getBuId());
					List<MerchantShopShipperMongo> shipperList = merchantShopShipperMongoDao.findAll(params);
					if (shipperList != null) {
						if (shipperList.size() > 1) {
							setErrorMsg(i, "店铺编码：" + shopRel.getShopCode() + "为外部店，" + "公司事业部货主信息存在多条，无法获取库位类型", resultList);
							failure += 1;
							continue;
						} else {
							MerchantShopShipperMongo shipperMongo = shipperList.get(0);
							stockLocationTypeId = shipperMongo.getStockLocationTypeId();
						}
					}
				}
				if (stockLocationTypeId != null && stockLocationTypeId.intValue() != 0) {
					params.clear();
					params.put("buStockLocationTypeId", stockLocationTypeId);
					params.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
					stockLocationTypeConfigMongo = buStockLocationTypeConfigMongoDao.findOne(params);

					stockLocationMap.put(shopRel.getShopCode(), stockLocationTypeConfigMongo);
				}
			}

			//导入的数据以“公司+事业部+店铺编码+入库仓库+退货入库时间”维度创建一个退货单。
			String key = user.getMerchantId() +"_"+buCode +"_" + indepotCode +"_" +returnInDate + "_" + shopCode+ "_" + tallyingUnit;
			OrderReturnIdepotModel orderReturnIdepotModelSave = modelMap.get(key);
			/**构造退货表头 start*/
			if(orderReturnIdepotModelSave == null){
				orderReturnIdepotModelSave = new OrderReturnIdepotModel();
				orderReturnIdepotModelSave.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DSDDTH));
				orderReturnIdepotModelSave.setReturnInDepotCode(inDepotInfo.getDepotCode());
				orderReturnIdepotModelSave.setReturnInDepotId(inDepotInfo.getDepotId());
				orderReturnIdepotModelSave.setReturnInDepotName(inDepotInfo.getName());
				orderReturnIdepotModelSave.setReturnInDate(TimeUtils.parse(returnInDate, "yyyy-MM-dd"));//退货入库时间
				orderReturnIdepotModelSave.setAmount(new BigDecimal(0));
				orderReturnIdepotModelSave.setCurrency(DERP.CURRENCYCODE_CNY);
				orderReturnIdepotModelSave.setShopCode(shopRel.getShopCode());
				orderReturnIdepotModelSave.setShopName(shopRel.getShopName());
				orderReturnIdepotModelSave.setShopTypeCode(shopRel.getShopTypeCode()); //运营类型编码
				orderReturnIdepotModelSave.setStorePlatformCode(shopRel.getStorePlatformCode());
				orderReturnIdepotModelSave.setCustomerId(shopRel.getCustomerId());
				orderReturnIdepotModelSave.setCustomerName(shopRel.getCustomerName());
				orderReturnIdepotModelSave.setOrderReturnSource(1);// 导入
				orderReturnIdepotModelSave.setMerchantId(user.getMerchantId());
				orderReturnIdepotModelSave.setMerchantName(user.getMerchantName());
				orderReturnIdepotModelSave.setStatus(DERP_ORDER.ORDER_RETURN_STATUS_011); //待审核
				orderReturnIdepotModelSave.setCreater(user.getId());
				orderReturnIdepotModelSave.setCreateName(user.getName());
				orderReturnIdepotModelSave.setCreateDate(TimeUtils.getNow());
				orderReturnIdepotModelSave.setTotalRefundAmount(BigDecimal.ZERO);
				orderReturnIdepotModelSave.setReturnOrderType(DERP_ORDER.ORDERRETURN_RETURN_ORDER_TYPE_1);//退货单
				orderReturnIdepotModelSave.setReturnInCreateDate(TimeUtils.getNow());
				if(DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfo.getType()) && StringUtils.isNotBlank(tallyingUnit)){
					orderReturnIdepotModelSave.setTallyingUnit(tallyingUnit);
				}

				modelMap.put(key, orderReturnIdepotModelSave) ;
			}
			/**构造退货表头 end*/


			/**构造退货商品表体 start*/
			List<OrderReturnIdepotItemModel> itemList = itemMap.get(key);

			if(itemList == null) {
				itemList = new ArrayList<OrderReturnIdepotItemModel>() ;
			}

			/**相同货号，批次号，合并数量*/
			OrderReturnIdepotItemModel returnItemModel = null ;
			for (OrderReturnIdepotItemModel orderReturnIdepotItemModel : itemList) {
				if(orderReturnIdepotItemModel.getInGoodsNo().equals(goodsNo)) {
					returnItemModel = orderReturnIdepotItemModel ;
					break ;
				}
			}
			if(returnItemModel == null) {
				returnItemModel = new OrderReturnIdepotItemModel() ;
				returnItemModel.setInGoodsCode(merchandise.getGoodsCode());
				returnItemModel.setInGoodsId(merchandise.getMerchandiseId());
				returnItemModel.setInGoodsNo(merchandise.getGoodsNo());
				returnItemModel.setInGoodsName(merchandise.getName());
				returnItemModel.setBarcode(merchandise.getBarcode());
				returnItemModel.setCommbarcode(merchandise.getCommbarcode());
				returnItemModel.setBuId(merchantBuRelMongo.getBuId());
				returnItemModel.setBuName(merchantBuRelMongo.getBuName());
				returnItemModel.setPrice(BigDecimal.ZERO);
				returnItemModel.setDecTotal(BigDecimal.ZERO);
				returnItemModel.setBadGoodsNum(Integer.valueOf(wornNumStr));
				returnItemModel.setReturnNum(Integer.valueOf(normalNumStr));
				returnItemModel.setCreateDate(TimeUtils.getNow());
				returnItemModel.setRefundAmount(BigDecimal.ZERO);
				if(stockLocationTypeConfigMongo != null){
					returnItemModel.setStockLocationTypeId(stockLocationTypeConfigMongo.getBuStockLocationTypeId());
					returnItemModel.setStockLocationTypeName(stockLocationTypeConfigMongo.getName());
				}

				itemList.add(returnItemModel) ;
			}else {
				returnItemModel.setBadGoodsNum(returnItemModel.getBadGoodsNum() + Integer.valueOf(wornNumStr));
				returnItemModel.setReturnNum(returnItemModel.getReturnNum() + Integer.valueOf(normalNumStr));
			}
			itemMap.put(key, itemList) ;
			/**构造退货商品表体 end */

			/**构造退货商品批次表体 start*/
			List<OrderReturnIdepotBatchModel> batchItemList = batchItemMap.get(key+"_"+goodsNo);

			if(batchItemList == null) {
				batchItemList = new ArrayList<OrderReturnIdepotBatchModel>() ;
			}

			OrderReturnIdepotBatchModel batchModel = null ;
			for (OrderReturnIdepotBatchModel orderReturnIdepotBatchModel : batchItemList) {
				if(StringUtils.isNotBlank(batchNo) && orderReturnIdepotBatchModel.getBatchNo().equals(batchNo) && orderReturnIdepotBatchModel.getGoodsNo().equals(goodsNo)) {
					batchModel = orderReturnIdepotBatchModel ;
					break ;
				}
				if(StringUtils.isBlank(batchNo) && orderReturnIdepotBatchModel.getGoodsNo().equals(goodsNo)) {
					batchModel = orderReturnIdepotBatchModel ;
					break ;
				}
			}

			if(batchModel == null) {
				batchModel = new OrderReturnIdepotBatchModel() ;
				batchModel.setGoodsId(merchandise.getMerchandiseId());
				batchModel.setGoodsName(merchandise.getName());
				batchModel.setGoodsNo(merchandise.getGoodsNo());
				batchModel.setBatchNo(batchNo);
				batchModel.setProductionDate(TimeUtils.parse(produceDate, "yyyy-MM-dd"));
				batchModel.setOverdueDate(TimeUtils.parse(overdueDate, "yyyy-MM-dd"));
				batchModel.setReturnNum(Integer.valueOf(normalNumStr));
				batchModel.setBadGoodsNum(Integer.valueOf(wornNumStr));
				batchModel.setCreateDate(TimeUtils.getNow());

				batchItemList.add(batchModel) ;
			}else {
				batchModel.setReturnNum(batchModel.getReturnNum() + Integer.valueOf(normalNumStr));
				batchModel.setBadGoodsNum(batchModel.getBadGoodsNum() + Integer.valueOf(wornNumStr));
			}

			batchItemMap.put(key+"_"+goodsNo, batchItemList);
			/**构造退货商品批次表体 end*/
		}
		/**保存*/
		if(failure == 0) {
			//退货信息
			for (String index : modelMap.keySet()) {
				OrderReturnIdepotModel orderReturnIdepotModel = modelMap.get(index);
				Long orderId = orderReturnIdepotDao.save(orderReturnIdepotModel);

				List<OrderReturnIdepotItemModel> itemList = itemMap.get(index);

				for (OrderReturnIdepotItemModel item : itemList) {
					item.setOreturnIdepotId(orderId);
					Long itemId = orderReturnIdepotItemDao.save(item);

					List<OrderReturnIdepotBatchModel> batchList = batchItemMap.get(index+"_"+item.getInGoodsNo());
					for (OrderReturnIdepotBatchModel batchModel : batchList) {
						batchModel.setItemId(itemId);
						orderReturnIdepotBatchDao.save(batchModel) ;
					}
				}
				success++;
				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_22, orderReturnIdepotModel.getCode(), "导入退货单", null, null);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}

	/**
	 * 退款信息
	 * @param objects
	 */
	private Map<String, Object> saveRefundOrder(List<Map<String, String>> objects , Boolean isTradeMechant,User user) throws Exception {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int failure = 0;
		//外部单号-退款集合
		Map<Integer, OrderReturnIdepotModel> refundExtralCodeMap = new HashMap<>() ;
		//外部单号-退款表体集合
		Map<Integer, List<OrderReturnIdepotItemModel>> refundExtralCodeItemMap = new HashMap<>() ;
		for (int j = 1; j <= objects.size(); j++) {
			Map<String, String> map = objects.get(j - 1);

			/**校验必填*/
			String extralCode = map.get("来源交易单号") ;
			if(checkIsNullOrNot(j, extralCode, "来源交易单号不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			extralCode = extralCode.trim() ;

			OrderModel queryOrder = new OrderModel();
			queryOrder.setExternalCode(extralCode);
			queryOrder.setStatus(DERP_ORDER.ORDER_STATUS_4);	// 已发货
			queryOrder.setMerchantId(user.getMerchantId());
			queryOrder = orderDao.searchByModel(queryOrder);

			if(queryOrder == null){
				// 根据外部单号没有找到对应已发货的电商订单
				setErrorMsg(j, " 根据外部单号"+extralCode+"没有找到对应已发货的电商订单", resultList);
				failure += 1;
				continue ;
			}

			/**从电商订单取仓库信息*/
			Map<String, Object> depotParmas = new HashMap<>() ;
			depotParmas.put("depotId", queryOrder.getDepotId());

			DepotInfoMongo depot = depotInfoMongoDao.findOne(depotParmas);

			/**从电商订单取仓库信息*/

			// 售后类型
			String afterSaleTypeStr = map.get("售后类型") ;
			if(checkIsNullOrNot(j, afterSaleTypeStr, "售后类型不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			afterSaleTypeStr = afterSaleTypeStr.trim();
			if(!DERP_ORDER.ORDER_RETURN_AFTER_SALE_TYPE_00.equals(afterSaleTypeStr) &&
					!DERP_ORDER.ORDER_RETURN_AFTER_SALE_TYPE_01.equals(afterSaleTypeStr)) {
				setErrorMsg(j, "售后类型填写有误，请填写正确编码：00-仅退款 01-退货退款", resultList);
				failure += 1;
				continue ;
			}

			//平台售后单号
			String returnCode = map.get("平台售后单号") ;
			returnCode = StringUtils.isNotBlank(returnCode) ? returnCode.trim():"";

			// 退款完结时间
			String refundEndDate = map.get("退款完结时间") ;
			if(checkIsNullOrNot(j, refundEndDate, "退款完结时间不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			refundEndDate = refundEndDate.trim();

			if(!TimeUtils.isYmdDate(refundEndDate)) {
				setErrorMsg(j, "退款完结时间格式有误,正确格式为:yyyy-MM-dd", resultList);
				failure += 1;
				continue ;
			}

			String refundAmount = map.get("退款金额") ;
			if(checkIsNullOrNot(j, refundAmount, "退款金额不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			if(!NumberUtils.isNumber(refundAmount)) {
				setErrorMsg(j, "退款金额只能是数字", resultList);
				failure += 1;
				continue ;
			}
			if(new BigDecimal(refundAmount).compareTo(BigDecimal.ZERO) == -1) {
				setErrorMsg(j, "退款金额要大于0", resultList);
				failure += 1;
				continue ;
			}
			refundAmount = refundAmount.trim();

			//退款原因
			String refundRemark = map.get("退款原因") ;
			refundRemark = StringUtils.isNotBlank(refundRemark) ? refundRemark.trim():"";

			/**构造退货商品表头 start*/
			// 封装数据
			OrderReturnIdepotModel orderReturnIdepotModelSave = new OrderReturnIdepotModel();
			orderReturnIdepotModelSave.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DSDDTH));
			orderReturnIdepotModelSave.setExternalCode(extralCode);
			orderReturnIdepotModelSave.setReturnCode(returnCode);
			orderReturnIdepotModelSave.setReturnInDepotCode(depot.getDepotCode());
			orderReturnIdepotModelSave.setReturnInDepotId(depot.getDepotId());
			orderReturnIdepotModelSave.setReturnInDepotName(depot.getName());
			orderReturnIdepotModelSave.setReturnInCreateDate(TimeUtils.getNow());
			orderReturnIdepotModelSave.setRefundEndDate(TimeUtils.parse(refundEndDate, "yyyy-MM-dd"));//退款完结时间
			orderReturnIdepotModelSave.setAmount(new BigDecimal(0));
			orderReturnIdepotModelSave.setCurrency(DERP.CURRENCYCODE_CNY);
			orderReturnIdepotModelSave.setStorePlatformCode(queryOrder.getStorePlatformName());
			orderReturnIdepotModelSave.setShopCode(queryOrder.getShopCode());
			orderReturnIdepotModelSave.setShopName(queryOrder.getShopName());
			orderReturnIdepotModelSave.setShopTypeCode(queryOrder.getShopTypeCode()); //运营类型编码
			orderReturnIdepotModelSave.setCustomerId(queryOrder.getCustomerId());
			orderReturnIdepotModelSave.setCustomerName(queryOrder.getCustomerName());
			orderReturnIdepotModelSave.setOrderReturnSource(1);// 导入
			orderReturnIdepotModelSave.setMerchantId(user.getMerchantId());
			orderReturnIdepotModelSave.setMerchantName(user.getMerchantName());
			orderReturnIdepotModelSave.setStatus(DERP_ORDER.ORDER_RETURN_STATUS_011); // 待审核
			orderReturnIdepotModelSave.setCreater(user.getId());
			orderReturnIdepotModelSave.setCreateName(user.getName());
			orderReturnIdepotModelSave.setCreateDate(TimeUtils.getNow());
			orderReturnIdepotModelSave.setAfterSaleType(afterSaleTypeStr);//售后类型
			orderReturnIdepotModelSave.setReturnOrderType(DERP_ORDER.ORDERRETURN_RETURN_ORDER_TYPE_0);//退款信息，是否为退货单为 0
			orderReturnIdepotModelSave.setTotalRefundAmount(new BigDecimal(refundAmount));//退款金额
			orderReturnIdepotModelSave.setRefundRemark(refundRemark);
			orderReturnIdepotModelSave.setOrderId(queryOrder.getId());
			orderReturnIdepotModelSave.setOrderCode(queryOrder.getCode());

			if(isTradeMechant) {//仅对公司主体的注册地类型为 境内的主体计算 内贸税额 、内贸退款金额（不含税）
				/**
				 * 内贸退款金额（不含税）= 订单退款金额/（1+13%），得出的金额保留2位小数，做四舍五入
				 * 内贸税额 = 订单退款金额 - 内贸退款金额（不含税），得出金额保留2位小数，做四舍五入
				 */
				BigDecimal tradeRefundAmount = orderReturnIdepotModelSave.getTotalRefundAmount().divide(new BigDecimal(1.13),2,BigDecimal.ROUND_HALF_UP);
				BigDecimal tradeRefundTax = orderReturnIdepotModelSave.getTotalRefundAmount().subtract(tradeRefundAmount).setScale(2,BigDecimal.ROUND_HALF_UP);
				orderReturnIdepotModelSave.setTradeRefundAmount(tradeRefundAmount);
				orderReturnIdepotModelSave.setTradeRefundTax(tradeRefundTax);
			}
			refundExtralCodeMap.put(j, orderReturnIdepotModelSave) ;
			/**构造退货商品表头 end*/


			/**构造退货商品表体 start*/
			List<OrderReturnIdepotItemModel> itemList =  new ArrayList<OrderReturnIdepotItemModel>();
			//退款完结月份必须大于关帐月份
			OrderItemModel itemModel = new OrderItemModel();
			itemModel.setOrderId(queryOrder.getId());
			List<OrderItemModel> orderItemsList = orderItemDao.list(itemModel);
			BigDecimal itemTotalAmount = orderItemsList.stream().map(OrderItemModel::getDecTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal calRefund = BigDecimal.ZERO;
			for(int k = 0 ; k < orderItemsList.size() ; k++){
				OrderItemModel orderItem = orderItemsList.get(k);
				FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
				closeAccountsMongo.setMerchantId(user.getMerchantId());
				closeAccountsMongo.setDepotId(depot.getDepotId());
				closeAccountsMongo.setBuId(orderItem.getBuId());
				String maxdate = "";

				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);

				String maxCloseAccountsMonth="";
				if (StringUtils.isNotBlank(maxdate)) {
					// 获取该月份下月时间
					String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
					maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
				}
				// 关账日期和退款完结月份比较
				if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
					// 关账下个月日期大于 退款完结月份
					if (TimeUtils.parse(refundEndDate, "yyyy-MM-dd").getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
						setErrorMsg(j, "退款完结月份必须大于关账日期", resultList);
						failure += 1;
						continue ;
					}
				}
				OrderReturnIdepotItemModel returnItemModel = new OrderReturnIdepotItemModel();
				returnItemModel.setInGoodsCode(orderItem.getGoodsCode());
				returnItemModel.setInGoodsId(orderItem.getGoodsId());
				returnItemModel.setInGoodsNo(orderItem.getGoodsNo());
				returnItemModel.setInGoodsName(orderItem.getGoodsName());
				returnItemModel.setBarcode(orderItem.getBarcode());
				returnItemModel.setCommbarcode(orderItem.getCommbarcode());
				returnItemModel.setBuId(orderItem.getBuId());
				returnItemModel.setBuName(orderItem.getBuName());
				returnItemModel.setPrice(BigDecimal.ZERO);
				returnItemModel.setDecTotal(BigDecimal.ZERO);
				returnItemModel.setBadGoodsNum(Integer.valueOf(0));
				returnItemModel.setReturnNum(Integer.valueOf(0));
				returnItemModel.setCreateDate(TimeUtils.getNow());
				returnItemModel.setRefundAmount(BigDecimal.ZERO);
				returnItemModel.setStockLocationTypeId(orderItem.getStockLocationTypeId());
				returnItemModel.setStockLocationTypeName(orderItem.getStockLocationTypeName());

				BigDecimal tempRefund = BigDecimal.ZERO;
				/**
				 * 退款金额摊分到各个商品上
				 * 摊分公式逻辑                     *
				 *      （1）前N-1个商品退款金额 =（商品结算金额/订单所有商品结算总金额）*订单退款金额，得出保留2位小数；                     *
				 *      （2）第N个商品退款金额 = 订单退款金额-前N-1个商品退款金额总和，保留2位小数
				 */
				if(k == orderItemsList.size()-1) {
					tempRefund = orderReturnIdepotModelSave.getTotalRefundAmount().subtract(calRefund).setScale(2, BigDecimal.ROUND_HALF_UP);
				}else{
					BigDecimal refundRatio = orderItem.getDecTotal().divide(itemTotalAmount,8,BigDecimal.ROUND_HALF_UP);
					tempRefund = refundRatio.multiply(orderReturnIdepotModelSave.getTotalRefundAmount()).setScale(2, BigDecimal.ROUND_DOWN);
				}
				returnItemModel.setRefundAmount(tempRefund);
				calRefund = calRefund.add(tempRefund);//累计已分摊退款金额、

				itemList.add(returnItemModel) ;
			}
			refundExtralCodeItemMap.put(j, itemList) ;
			/**构造退货商品表体 end*/
		}
		if(failure == 0) {
			//退款信息
			for (Integer index : refundExtralCodeMap.keySet()) {
				OrderReturnIdepotModel orderReturnIdepotModel = refundExtralCodeMap.get(index);
				Long orderId = orderReturnIdepotDao.save(orderReturnIdepotModel);

				List<OrderReturnIdepotItemModel> itemList = refundExtralCodeItemMap.get(index);
				for (int i = 0; i < itemList.size(); i++) {
					OrderReturnIdepotItemModel returnItemModel = itemList.get(i);
					if (isTradeMechant) {//仅对公司主体的注册地类型为 境内的主体计算 内贸商品退款税额、内贸商品退款金额（不含税）
						/**
						 * 内贸商品退款金额（不含税）= 商品退款金额/（1+13%），得出的金额保留2位小数，做四舍五入
						 * 内贸商品退款税额 = 商品退款金额 - 内贸商品退款金额（不含税），得出金额保留2位小数，做四舍五入
						 */
						BigDecimal tradeRefundAmount = returnItemModel.getRefundAmount().divide(new BigDecimal(1.13), 2, BigDecimal.ROUND_HALF_UP);
						BigDecimal tradeRefundTax = returnItemModel.getRefundAmount().subtract(tradeRefundAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
						returnItemModel.setTradeRefundAmount(tradeRefundAmount);
						returnItemModel.setTradeRefundTax(tradeRefundTax);
					}
					returnItemModel.setOreturnIdepotId(orderId);
					orderReturnIdepotItemDao.save(returnItemModel);
				}
				success++;
				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_22, orderReturnIdepotModel.getCode(), "导入退款单", null, null);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}

	/**
	 * 判断是否yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public boolean isYmdDate(String date) {
		Pattern p = Pattern.compile("\\d{4}\\-\\d{1,2}\\-\\d{1,2}");
		return p.matcher(date).matches();
	}
}
