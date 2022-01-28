package com.topideal.order.service.sale.impl;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.*;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.sale.*;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.entity.vo.sale.WayBillItemModel;
import com.topideal.entity.vo.sale.WayBillModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.json.pushapi.ywms.sale.push.*;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.sale.OrderService;
import com.topideal.order.tools.DownloadExcelUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 电商订单service实现类
 */
@Service
public class OrderServiceImpl implements OrderService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
	// 电商订单表头
	@Autowired
	private OrderDao orderDao;
	// 电商订单表体
	@Autowired
	private OrderItemDao orderItemDao;
	// 电商订单历史表头
	@Autowired
	private OrderHistoryDao orderHistoryDao;
	// 电商订单历史表体
	@Autowired
	private OrderItemHistoryDao orderItemHistoryDao;
	//运单表体
	@Autowired
	private WayBillItemDao wayBillItemDao;
	//运单历史表体
	@Autowired
	private WayBillItemHistoryDao wayBillItemHistoryDao;
	// 仓库Mongo
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 仓库商家关联
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	// 商家店铺Mongo
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao ;
	//商品信息MongoDB
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao ;
	//外部单号
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao ;
	// 运单表
	@Autowired
	private WayBillDao wayBillDao;
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;//财务经销存关账mongdb
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	// 公司仓库事业部关联表
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;
	@Autowired
	private BrandParentMongoDao brandParentMongoDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;
	@Autowired
	private MerchantShopShipperMongoDao merchantShopShipperMongoDao;
	/**
	 * 列表（分页）
	 * @param model
	 * @return
	 */
	@Override
	public OrderModel listOrderByPage(OrderModel model)
			throws SQLException {
		return orderDao.searchByPage(model);
	}

	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@Override
	public OrderModel searchDetail(Long id) throws SQLException {
		OrderModel model = new OrderModel();
		model.setId(id);
		return orderDao.searchById(id);
	}

	/**
	 * 根据表头ID获取详细信息(包括商品批次信息)
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<OrderItemDTO> listItemByOrderId(Long id) throws SQLException {
		OrderItemDTO itemDTO = new OrderItemDTO();
		itemDTO.setOrderId(id);
		List<OrderItemDTO> orderItemList = orderItemDao.listItemDTO(itemDTO);
		if(orderItemList==null || orderItemList.size()==0){
			OrderItemHistoryDTO itemHistoryDTO = new OrderItemHistoryDTO();
			itemHistoryDTO.setOrderId(id);
			List<OrderItemHistoryDTO> orderItemHistoryList = orderItemHistoryDao.listItemDTO(itemHistoryDTO);
			if(orderItemHistoryList!=null && orderItemHistoryList.size()>0){
				for (OrderItemHistoryDTO orderItemHistoryDTO : orderItemHistoryList) {
					itemDTO = new OrderItemDTO();
					BeanUtils.copyProperties(orderItemHistoryDTO, itemDTO);
					orderItemList.add(itemDTO);
				}
			}
		}
		return orderItemList;
	}
	/**
	 * 根据条件获取电商订单信息（包括表体信息）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public OrderDTO listOrderAndItemByPage(OrderDTO dto) throws SQLException {
		OrderDTO orderData = new OrderDTO();
		// 订单时间段 1-近12个月数据 2-12个月以前数据
		if("2".equals(dto.getOrderTimeRange())){
			OrderHistoryDTO orderHistoryParam = new OrderHistoryDTO();
			BeanUtils.copyProperties(dto, orderHistoryParam);
			OrderHistoryDTO orderHistoryDTO = orderHistoryDao.newDtoListByPage(orderHistoryParam);//查询历史表 查出表头数据
			BeanUtils.copyProperties(orderHistoryDTO, orderData);
		}else{
			orderData = orderDao.newDtoListByPage(dto);//查出表头数据
		}
		return orderData;
	}
	/**
	 * 根据条件获取电商订单
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int listOrder(OrderDTO dto) throws SQLException {
		Integer count = 0;
		// 12个月以前数据 查历史表
		if("2".equals(dto.getOrderTimeRange())){
			count = orderHistoryDao.queryDtoListCount(dto);
		}else{
			count = orderDao.queryDtoListCount(dto);
		}
		return count;
	}
	/**
	 * 根据ids获取需要导出的数据
	 */
	@Override
	public List<Map<String, Object>> getExportOrderMap(OrderDTO dto) throws SQLException {
		List<Map<String, Object>> exportMap = new ArrayList<>();
		// 12个月以前数据 查历史表
		if("2".equals(dto.getOrderTimeRange())){
			exportMap = orderHistoryDao.getExportOrderMap(dto);
		}else{
			exportMap = orderDao.getExportOrderMap(dto);
		}
		for (Map<String, Object> map : exportMap) {
			map.put("status", DERP_ORDER.getLabelByKey(DERP_ORDER.order_statusList, (String) map.get("status")));
			map.put("store_platform_name", DERP.getLabelByKey(DERP.storePlatformList, (String) map.get("store_platform_name")));
			map.put("currency", DERP.getLabelByKey(DERP.currencyCodeList, (String) map.get("currency")));
			map.put("shop_type_code", DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, (String) map.get("shop_type_code")));

			Long goodsId = (Long) map.get("goods_id");
			BrandParentMongo brandParentMongo = brandParentMongoDao.getBrandParentMongo(goodsId);
			if(brandParentMongo != null) {
				map.put("brandParentName", brandParentMongo.getName());
				map.put("superiorParentName", brandParentMongo.getSuperiorParentBrand());
			}
		}
		return exportMap;
	}

	@Override
	public List<WayBillItemDTO> listWayBillItemByOrderId(Long id) {
		List<WayBillItemDTO> wayBillItemDTOList = wayBillItemDao.listWayBillItemByOrderId(id);

		if(wayBillItemDTOList==null || wayBillItemDTOList.size()==0){ // 先查当前表、没有再查历史表
			List<WayBillItemHistoryDTO> historyList = wayBillItemHistoryDao.listWayBillItemByOrderId(id);
			if(historyList!=null && historyList.size()>0){
				for (WayBillItemHistoryDTO wayBillItemHistoryDTO : historyList) {
					WayBillItemDTO wayBillItemDTO = new WayBillItemDTO();
					BeanUtils.copyProperties(wayBillItemHistoryDTO, wayBillItemDTO);
					wayBillItemDTOList.add(wayBillItemDTO);
				}
			}
		}

		return wayBillItemDTOList;
	}

	@Override
	public Integer queryListCount(OrderDTO dto) throws SQLException {
		return orderDao.queryDtoListCount(dto);
	}
	/**
	 * 获取列表数据（表头）-分页
	 * @param model
	 * @return
	 */
	@Override
	public OrderDTO newListByPage(OrderDTO dto) throws SQLException {
		return orderDao.newDtoListByPage(dto);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> importOrder(User user, Map<Integer, List<List<Object>>> data) throws Exception {

		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int failure = 0;

		Map<String , OrderModel> externalCodeOrderMap = new HashMap<String , OrderModel>() ;
		List<OrderModel> orderList = new ArrayList<OrderModel>() ;
		Map<String , List<OrderItemModel>> externalCodeOrderItemMap = new HashMap<String , List<OrderItemModel>>();
		List<OrderItemModel> orderItemList = new ArrayList<OrderItemModel>() ;
		String deliverDate = "";
		Map<String , DepotInfoMongo> depotInfoMongoMap = new HashMap<String , DepotInfoMongo>() ;//记录仓库信息
		// 查询商家
		Map<String, Object> merchantMap = new HashMap<String, Object>();
		merchantMap.put("merchantId", user.getMerchantId());
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);
		boolean isTradeMechant = "1".equals(merchant.getRegisteredType()) ? true : false;//是否是内贸主体
		Map<String, MerchantShopRelMongo> merchantShopRelMongoMap = new HashMap<>();//商家店铺关联表

		for (int i = 0; i < 1; i++) {// 表头
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
				//mongo查询参数集合
				Map<String, Object> params = new HashMap<String, Object>();
				//外部交易单号
				String externalCode = map.get("外部交易单号") ;
				if(checkIsNullOrNot(j, externalCode, "外部交易单号不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				externalCode = externalCode.trim() ;

				if(externalCodeOrderMap.containsKey(externalCode)) {
					setErrorMsg(j, "导入外部单号已重复", resultList);
					failure += 1;
					continue ;
				}

				OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
				orderExternalCodeModel.setExternalCode(externalCode);
				orderExternalCodeModel.setOrderSource(1);	// 订单来源  1:电商订单, 2:装载交运 3:销售出库

				orderExternalCodeModel = orderExternalCodeDao.searchByModel(orderExternalCodeModel) ;

				OrderModel orderModel=new OrderModel();
				orderModel.setExternalCode(externalCode);//订单号  ，不可重复  对应外部单号
				orderModel = orderDao.searchByModel(orderModel);

				if(orderExternalCodeModel != null || (orderModel != null && !"006".equals(orderModel.getStatus()))) {
					setErrorMsg(j, "外部单号已存在", resultList);
					failure += 1;
					continue ;
				}

				//出库仓库，校验仓库是否批次强校验
				String depotCode = map.get("出库仓库") ;
				if(checkIsNullOrNot(j, depotCode, "出库仓库不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				depotCode = depotCode.trim() ;
				DepotInfoMongo depot = depotInfoMongoMap.get(depotCode);
				if(depot == null){
					params.put("depotCode", depotCode);
					depot = depotInfoMongoDao.findOne(params);
				}
				if (depot == null) {
					setErrorMsg(j, "出库仓库不存在", resultList);
					failure += 1;
					continue ;
				}
				depotInfoMongoMap.put(externalCode, depot);
				//过滤海外仓
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
					setErrorMsg(j, "不允许手动导入海外仓电商订单", resultList);
					failure += 1;
					continue ;
				}

				//制单时间
				String makingTime = map.get("制单时间") ;
				if(checkIsNullOrNot(j, makingTime, "制单时间不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				makingTime = makingTime.trim();
				if(!isDate(makingTime)) {
					setErrorMsg(j, "制单时间格式有误,正确格式为:yyyy-MM-dd HH:mm:ss", resultList);
					failure += 1;
					continue ;
				}
				if(TimeUtils.daysBetween(TimeUtils.parse(makingTime, "yyyy-MM-dd"), new Date()) < 0) {
					setErrorMsg(j, "制单时间不可超过当前时间", resultList);
					failure += 1;
					continue;
				}
				//发货时间
				deliverDate = map.get("发货时间") ;
				// 发货时间非内贸仓，必填
				if(!DERP_SYS.DEPOTINFO_TYPE_G.equals(depot.getType())){
					if(checkIsNullOrNot(j, deliverDate, "发货时间不能为空", resultList)) {
						failure += 1;
						continue ;
					}
				}
				if(StringUtils.isNotBlank(deliverDate)){
					deliverDate = deliverDate.trim();
					if(!isDate(deliverDate)) {
						setErrorMsg(j, "发货时间格式有误,正确格式为:yyyy-MM-dd HH:mm:ss", resultList);
						failure += 1;
						continue ;
					}
					if(TimeUtils.daysBetween(TimeUtils.parse(deliverDate, "yyyy-MM-dd"), new Date()) < 0) {
						setErrorMsg(j, "发货时间不可超过当前时间", resultList);
						failure += 1;
						continue;
					}
					// 制单时间需<=发货时间
					if(TimeUtils.daysBetween(TimeUtils.parse(makingTime, "yyyy-MM-dd"),TimeUtils.parse(deliverDate, "yyyy-MM-dd") ) < 0) {
						setErrorMsg(j, "制单时间不可超过发货时间", resultList);
						failure += 1;
						continue;
					}
				}

				//店铺编码
				String shopCode = map.get("店铺编码") ;
				if(checkIsNullOrNot(j, shopCode, "店铺编码不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				shopCode = shopCode.trim() ;

				params.clear();
				params.put("shopCode", shopCode) ;
				params.put("status", "1") ;

				MerchantShopRelMongo shopRel = merchantShopRelMongoDao.findOne(params);
				if(shopRel == null) {
					setErrorMsg(j, "店铺对应平台信息不存在", resultList);
					failure += 1;
					continue ;
				}

				if(!shopRel.getDepotId().equals(depot.getDepotId())){
					setErrorMsg(j, "导入的出仓仓库在商家店铺信息表中没有对应的仓库", resultList);
					failure += 1;
					continue ;
				}
				if(!merchantShopRelMongoMap.containsKey(externalCode)){
					merchantShopRelMongoMap.put(externalCode,shopRel);
				}


				//运费
				String wayFrtFee = map.get("运费") ;
				if(checkIsNullOrNot(j, wayFrtFee, "运费不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				wayFrtFee = wayFrtFee.trim() ;
				if(valiteFree(wayFrtFee, j, "运费格式有误", resultList)) {
					failure += 1;
					continue ;
				}
				BigDecimal wayFrtFeeBD = new BigDecimal(wayFrtFee) ;

				//税费
				String taxes = map.get("税费") ;
				if(checkIsNullOrNot(j, taxes, "税费不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				taxes = taxes.trim() ;
				if(valiteFree(taxes, j, "税费格式有误", resultList)) {
					failure += 1;
					continue ;
				}
				BigDecimal taxesBD = new BigDecimal(taxes) ;

				//实付总额
				String payment = map.get("实付总额") ;
				if(checkIsNullOrNot(j, payment, "实付总额不能为空", resultList)) {
					failure += 1;
					continue ;
				}
				payment = payment.trim() ;
				if(valiteFree(payment, j, "实付总额只能为数值且小数点后只能为两位数", resultList)) {
					failure += 1;
					continue ;
				}

				BigDecimal paymentBD = new BigDecimal(payment) ;

				//申报时间
				String declareDate = map.get("申报时间") ;
				if(StringUtils.isNotBlank(declareDate)) {
					if(!isDate(declareDate)) {
						setErrorMsg(j, "申报时间格式有误,正确格式为:yyyy-MM-dd HH:mm:ss", resultList);
						failure += 1;
						continue ;
					}
				}


				declareDate = declareDate.trim();

				//放行时间
				String releaseDate = map.get("放行时间") ;
				if(StringUtils.isNotBlank(releaseDate)) {
					if(!isDate(releaseDate)) {
						setErrorMsg(j, "放行时间格式有误,正确格式为:yyyy-MM-dd HH:mm:ss", resultList);
						failure += 1;
						continue ;
					}
				}
				releaseDate = releaseDate.trim();

				//国检状态
				String inspectStatus = map.get("国检状态") ;
				String[] inspectStatusArr = {"11" , "12" , "13" , "14" , "15"} ;
				if(StringUtils.isNotBlank(inspectStatus)) {
					//校验是否属于国检状态 11：已报国检12：国检放行13：国检审核驳回14：国检抽检15：国检抽检未过
					if(!Arrays.asList(inspectStatusArr).contains(inspectStatus)) {
						setErrorMsg(j, "国检状态值只能是11、12、13、14、15", resultList);
						failure += 1 ;
						continue ;
					}
				}

				//海关状态
				String customsStatus = map.get("海关状态") ;
				String[] customsStatusArr = {"21" , "22" , "23" , "24" , "25" , "26" , "41" , "42"} ;
				if(StringUtils.isNotBlank(customsStatus)) {
					//校验是否属于21：已报海关22：海关单证放行23：报海关失败24：海关查验/转人工/挂起等25：海关单证审核不通过26：海关已接受申报，待货物运抵后处理41：海关货物查扣42：海关货物放行
					if(!Arrays.asList(customsStatusArr).contains(customsStatus)) {
						setErrorMsg(j, "国检状态值只能是21、22、23、24、25、26、41、42", resultList);
						failure += 1 ;
						continue ;
					}
				}

				//进口模式
				String importMode = map.get("进口模式") ;
				String[] importModeArr = {"10" , "20" , "30" , "40" } ;
				if(StringUtils.isNotBlank(importMode)) {
					//校验是否属于10：BBC;20：BC;30：保留; 40：CC
					if(!Arrays.asList(importModeArr).contains(importMode)) {
						setErrorMsg(j, "国检状态值只能是10、20、30、40", resultList);
						failure += 1 ;
						continue ;
					}
				}

				//物流企业名称
				String logisticsName = map.get("物流企业名称") ;

				//运单号
				String wayBillNo = map.get("运单号") ;

				//订购人
				String makerName = map.get("订购人") ;

				//注册号
				String makerRegisterNo = map.get("注册号") ;

				//订购人号码
				String makerTel = map.get("订购人号码") ;

				//收件人
				String receiverName = map.get("收件人") ;

				//收件人号码
				String receiverTel = map.get("收件人号码") ;

				//省
				String receiverProvince = map.get("省") ;

				//城市
				String receiverCity = map.get("城市") ;

				//区
				String receiverArea = map.get("区") ;

				//收件地址
				String receiverAddress = map.get("收件地址") ;

				// 当出库仓类型为内贸仓时，物流企业名称、收件人、收件人号码、省、城市、收件人地址必填
				if(DERP_SYS.DEPOTINFO_TYPE_G.equals(depot.getType())){
					if(checkIsNullOrNot(j, logisticsName, "物流企业名称不能为空", resultList)) {
						failure += 1;
						continue ;
					}else {
						String logisticsNameLabel = DERP_ORDER.getLabelByKey(DERP.logisticsNameList, logisticsName);
						if(StringUtils.isBlank(logisticsNameLabel)){
							setErrorMsg(j, "物流企业名称输入有误", resultList);
							failure += 1;
							continue ;
						}
					}

					if(checkIsNullOrNot(j, receiverName, "收件人名称不能为空", resultList)) {
						failure += 1;
						continue ;
					}
					if(checkIsNullOrNot(j, receiverTel, "收件人号码不能为空", resultList)) {
						failure += 1;
						continue ;
					}
					if(checkIsNullOrNot(j, receiverProvince, "省不能为空", resultList)) {
						failure += 1;
						continue ;
					}
					if(checkIsNullOrNot(j, receiverCity, "城市不能为空", resultList)) {
						failure += 1;
						continue ;
					}
					if(checkIsNullOrNot(j, receiverArea, "区不能为空", resultList)) {
						failure += 1;
						continue ;
					}
					if(checkIsNullOrNot(j, receiverAddress, "收件地址不能为空", resultList)) {
						failure += 1;
						continue ;
					}
				}

				//保费
				String wayIndFee = map.get("保费") ;
				BigDecimal wayIndFeeBD = null ;
				if(StringUtils.isNotBlank(wayIndFee)) {
					if(valiteFree(wayIndFee, j, "保费格式有误", resultList)) {
						failure += 1;
						continue ;
					}else {
						wayIndFee = wayIndFee.trim() ;
						wayIndFeeBD = new BigDecimal(wayIndFee) ;
					}
				}


				//优惠金额
				String discount = map.get("优惠金额") ;
				BigDecimal discountBD = null ;
				if(StringUtils.isNotBlank(discount) ) {
					if(valiteFree(discount, j, "优惠金额格式有误", resultList)) {
					failure += 1;
					continue ;
					}else {
						discount = discount.trim() ;
						discountBD = new BigDecimal(discount) ;
					}
				}

				//封装数据
				orderModel= new OrderModel();
				orderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DSDD));
				orderModel.setCurrency(DERP.CURRENCYCODE_CNY);	// 人民币
				orderModel.setCreater(user.getId());
				orderModel.setCreateDate(TimeUtils.getNow());
				orderModel.setCustomsStatus(customsStatus);
				orderModel.setCustomerId(shopRel.getCustomerId());
				orderModel.setCustomerName(shopRel.getCustomerName());
				orderModel.setDeclareDate(TimeUtils.parse(declareDate, "yyyy-MM-dd HH:mm:ss"));
				if(StringUtils.isNotBlank(deliverDate)){
					orderModel.setDeliverDate(TimeUtils.parse(deliverDate, "yyyy-MM-dd HH:mm:ss"));
				}
				orderModel.setTradingDate(TimeUtils.parse(makingTime, "yyyy-MM-dd HH:mm:ss"));
				orderModel.setDepotId(depot.getDepotId());
				orderModel.setDepotName(depot.getName());
				orderModel.setDiscount(discountBD);
				orderModel.setExternalCode(externalCode);
				orderModel.setImportMode(importMode);
				orderModel.setInspectStatus(inspectStatus);
				orderModel.setLogisticsName(logisticsName);
				orderModel.setMakerName(makerName);
				orderModel.setMakerRegisterNo(makerRegisterNo);
				orderModel.setMakerTel(makerTel);
				orderModel.setMakingTime(TimeUtils.parse(makingTime, "yyyy-MM-dd HH:mm:ss"));
				orderModel.setMerchantId(user.getMerchantId());
				orderModel.setMerchantName(user.getMerchantName());
				orderModel.setOrderSource(2);
				orderModel.setPayment(paymentBD);
				orderModel.setReceiverAddress(receiverAddress);
				orderModel.setReceiverName(receiverName);
				orderModel.setReceiverTel(receiverTel);
				orderModel.setReleaseDate(TimeUtils.parse(releaseDate, "yyyy-MM-dd HH:mm:ss"));
				orderModel.setShopCode(shopCode);
				orderModel.setShopName(shopRel.getShopName());
				orderModel.setShopTypeCode(shopRel.getShopTypeCode()); // 运营类型编码
				orderModel.setStatus(DERP_ORDER.ORDER_STATUS_8);//待审核
				orderModel.setStorePlatformName(shopRel.getStorePlatformCode()) ;
				orderModel.setWayBillNo(wayBillNo);
				orderModel.setTaxes(taxesBD);
				orderModel.setWayFrtFee(wayFrtFeeBD);
				orderModel.setWayIndFee(wayIndFeeBD);
				orderModel.setReceiverProvince(receiverProvince);
				orderModel.setReceiverCity(receiverCity);
				orderModel.setReceiverArea(receiverArea);//区
				orderModel.setIsGenerate(DERP_ORDER.ORDER_IS_GENERATE_0);
				if(isTradeMechant) {//仅对公司主体的注册地类型为 境内的主体计算 内贸税额、内贸实付金额（不含税）
					/**
					 * 内贸实付金额（不含税）= 订单实付金额/（1+13%），得出的金额保留2位小数，做四舍五入
					 * 内贸税额 = 订单实付金额 - 内贸实付金额（不含税），得出金额保留2位小数，做四舍五入
					 */
					BigDecimal tradePayment = paymentBD.divide(new BigDecimal(1.13),2,BigDecimal.ROUND_HALF_UP);//内贸实付金额（不含税
					BigDecimal tradePaymentTax = paymentBD.subtract(tradePayment).setScale(2,BigDecimal.ROUND_HALF_UP);//内贸税额
					orderModel.setTradePayment(tradePayment);
					orderModel.setTradePaymentTax(tradePaymentTax);

				}
				externalCodeOrderMap.put(externalCode, orderModel) ;
				orderList.add(orderModel);
			}
		}

		if(failure == 0) {
			for (int i = 1; i < 2; i++) {// 表体
				Map<String,MerchantBuRelMongo> merchantBuRelMongoMap = new HashMap<String,MerchantBuRelMongo>();//记录事业部信息
				Map<String,MerchantDepotBuRelMongo> merchantDepotBuRelMongoMap = new HashMap<String,MerchantDepotBuRelMongo>();//记录仓库关联事业部信息
				Map<String,String> closeAccountsMaxDateMongoMap = new HashMap<String,String>();//记录月结/关账日期
				Map<String,MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<String,MerchandiseInfoMogo>();//记录商品信息
				Map<String,BuStockLocationTypeConfigMongo> stockLocationMap = new HashMap<String,BuStockLocationTypeConfigMongo>();//记录库存类型信息
				List<List<Object>> objects = data.get(i);
				for (int j = 1; j < objects.size(); j++) {
					Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
					//mongo查询参数集合
					Map<String, Object> params = new HashMap<String, Object>();

					//外部交易单号
					String externalCode = map.get("外部交易单号") ;
					if(checkIsNullOrNot(j, externalCode, "表体商品信息外部交易单号不能为空", resultList)) {
						failure += 1;
						continue ;
					}
					externalCode = externalCode.trim() ;
					DepotInfoMongo depot = depotInfoMongoMap.get(externalCode);
					OrderModel orderModel = externalCodeOrderMap.get(externalCode);

					if(orderModel == null) {
						setErrorMsg(j, "基本信息自编外部单号与商品信息自编外部单号不符", resultList);
						failure += 1;
						continue ;
					}
					// 事业部
					String buCode = map.get("事业部") ;
					if(checkIsNullOrNot(j, buCode, "表体商品信息事业部不能为空", resultList)) {
						failure += 1;
						continue ;
					}
					buCode = buCode.trim() ;
					// 获取该事业部信息
					MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoMap.get(buCode);
					if(merchantBuRelMongo == null){
						Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
						merchantBuRelParam.put("merchantId", user.getMerchantId());
						merchantBuRelParam.put("buCode", buCode);
						merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);	// 启用
						merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
					}
					if(merchantBuRelMongo == null || "".equals(merchantBuRelMongo)){
						setErrorMsg(j, "事业部编码为："+buCode+",未查到该公司下事业部信息", resultList);
						failure += 1;
						continue ;
					}
					merchantBuRelMongoMap.put(buCode,merchantBuRelMongo);
					String key = user.getMerchantId() +"_"+orderModel.getDepotId()+"_"+merchantBuRelMongo.getBuId();
					// 校验公司-仓库-事业部的关联表
					MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoMap.get(key);
					if(outMerchantDepotBuRelMongo == null){
						Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
						merchantDepotBuRelParam.put("merchantId", user.getMerchantId());
						merchantDepotBuRelParam.put("depotId", orderModel.getDepotId());	// 出库仓库
						merchantDepotBuRelParam.put("buId", merchantBuRelMongo.getBuId());
						outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
					}
					if(outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)){
						setErrorMsg(j,  "事业部编码为："+merchantBuRelMongo.getBuCode()+",出仓仓库："+orderModel.getDepotId()+",未查到该公司仓库事业部关联信息", resultList);
						failure += 1;
						continue ;
					}
					merchantDepotBuRelMongoMap.put(key, outMerchantDepotBuRelMongo);
					// 校验事业部与当前账号所绑定的事业部是否匹配
					boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), merchantBuRelMongo.getBuId());
					if(!userRelateBu){
						setErrorMsg(j,  "事业部编码为："+merchantBuRelMongo.getBuCode()+",用户id："+user.getId()+",无权限操作该事业部", resultList);
						failure += 1;
						continue ;
					}
					if(StringUtils.isNotBlank(deliverDate)){
						deliverDate = deliverDate.trim();
						// 获取最大的关账日/月结日期
						String maxdate = closeAccountsMaxDateMongoMap.get(key);
						if(StringUtils.isBlank(maxdate)){
							FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
							closeAccountsMongo.setMerchantId(user.getMerchantId());
							closeAccountsMongo.setDepotId(depot.getDepotId());
							closeAccountsMongo.setBuId(merchantBuRelMongo.getBuId());
							if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
								maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
							}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
								maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
							}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
								maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
							}
							closeAccountsMaxDateMongoMap.put(key, maxdate);
						}
						String maxCloseAccountsMonth="";
						if (StringUtils.isNotBlank(maxdate)) {
							// 获取该月份下月时间
							String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
							maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
						}
						// 关账日期和发货日期比较
						if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
							// 关账下个月日期大于发货日期
							if (Timestamp.valueOf(deliverDate).getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
								setErrorMsg(j, "发货日期必须大于关账日期/月结日期", resultList);
								failure += 1;
								continue ;
							}
						}
					}

					//商品货号
					String goodsNo = map.get("商品货号") ;
					if(checkIsNullOrNot(j, goodsNo, "表体商品信息商品货号不能为空", resultList)) {
						failure += 1;
						continue ;
					}
					goodsNo = goodsNo.trim() ;
					MerchandiseInfoMogo merchandise = merchandiseInfoMogoMap.get(goodsNo);
					if(merchandise == null){
						params.put("goodsNo", goodsNo) ;
						params.put("merchantId", user.getMerchantId());
						params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
						merchandise = merchandiseInfoMogoDao.findOne(params);
					}
					if(merchandise == null) {
						setErrorMsg(j, "商品货号："+goodsNo+"，商品信息不存在", resultList);
						failure += 1;
						continue ;
					}
					merchandiseInfoMogoMap.put(goodsNo, merchandise);
					//销售数量
					String num = map.get("销售数量") ;
					if(checkIsNullOrNot(j, num, "商品货号："+goodsNo+"，销售数量不能为空", resultList)) {
						failure += 1;
						continue ;
					}
					if(!StringUtils.isNumeric(num)) {
						setErrorMsg(j, "商品货号："+goodsNo+"，销售数量不为数字", resultList);
						failure += 1;
						continue ;
					}

					num = num.trim() ;

					//销售单价
					String originalPrice = map.get("销售单价") ;
					if(checkIsNullOrNot(j, originalPrice, "商品货号："+goodsNo+"，销售单价不能为空", resultList)) {
						failure += 1;
						continue ;
					}
					originalPrice = originalPrice.trim() ;
					if(valiteFree(originalPrice, j, "商品货号："+goodsNo+"，销售单价格式有误", resultList)) {
						failure += 1;
						continue ;
					}
					// 结算单价
					String price = map.get("结算单价") ;
					if(checkIsNullOrNot(j, price, "商品货号："+goodsNo+"，结算单价不能为空", resultList)) {
						failure += 1;
						continue ;
					}
					price = price.trim() ;
					if(valiteFree(price, j, "商品货号："+goodsNo+"，结算单价格式有误", resultList)) {
						failure += 1;
						continue ;
					}
					BigDecimal originalPriceBD = new BigDecimal(originalPrice) ;	// 销售单价
					BigDecimal priceBD = new BigDecimal(price) ;	// 结算单价
					BigDecimal numBD = new BigDecimal(num) ;	// 销售数量

					BigDecimal originalDecTotal = originalPriceBD.multiply(numBD);	// 销售总金额
					BigDecimal decTotalBD = priceBD.multiply(numBD);	// 结算总金额

					//商品优惠金额
					String goodsDiscount = map.get("商品优惠金额") ;
					BigDecimal goodsDiscountBD = null ;
					if(StringUtils.isNotBlank(goodsDiscount)) {
						if(valiteFree(goodsDiscount, j, "商品货号："+goodsNo+"，商品优惠金额格式有误", resultList)) {
							failure += 1;
							continue ;
						}else {
							goodsDiscount = goodsDiscount.trim() ;
							goodsDiscountBD = new BigDecimal(goodsDiscount) ;
						}
					}

					//批次号
					String batchNo = map.get("批次号") ;
					//生产日期
					String productionDate = map.get("生产日期") ;
					//失效日期
					String overdueDate = map.get("失效日期") ;
					//出库仓位批次强校验，批次效期必填
					if (depot.getBatchValidation()==null || DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation())){
						if(checkIsNullOrNot(j, batchNo, "批次效期强校验的仓库，商品货号："+goodsNo+"，批次号不能为空", resultList)) {
							failure += 1;
							continue ;
						}
						if(checkIsNullOrNot(j, productionDate, "批次效期强校验的仓库，商品货号："+goodsNo+"，生产日期不能为空", resultList)) {
							failure += 1;
							continue ;
						}
						productionDate = productionDate.trim() ;
						if(!isDate(productionDate) && !isValidDate(productionDate)) {
							setErrorMsg(j, "商品货号："+goodsNo+"，生产日期格式有误", resultList);
							failure += 1;
							continue;
						}
						if(checkIsNullOrNot(j, overdueDate, "批次效期强校验的仓库，商品货号："+goodsNo+"，失效日期不能为空", resultList)) {
							failure += 1;
							continue ;
						}
						overdueDate = overdueDate.trim() ;
						if(!isDate(overdueDate) && !isValidDate(overdueDate)) {
							setErrorMsg(j, "商品货号："+goodsNo+"，失效日期格式有误", resultList);
							failure += 1;
							continue;
						}
					}
					MerchantShopRelMongo shopRelMongo= merchantShopRelMongoMap.get(externalCode);
					BuStockLocationTypeConfigMongo stockLocationTypeConfigMongo = stockLocationMap.get(shopRelMongo.getShopCode());
					if(stockLocationTypeConfigMongo == null){
						Long stockLocationTypeId = null;
						//若为单主店则取店铺信息配置表头的“库位类型”；若为“外部店”则取对应的货主事业部的“库位类型”
						if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(shopRelMongo.getStoreTypeCode())){
							stockLocationTypeId = shopRelMongo.getStockLocationTypeId();
						}else if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(shopRelMongo.getStoreTypeCode())){
							params.clear();
							params.put("shopId",shopRelMongo.getShopId());
							params.put("merchantId",user.getMerchantId());
							params.put("buId", merchantBuRelMongo.getBuId());
							List<MerchantShopShipperMongo> shipperList = merchantShopShipperMongoDao.findAll(params);
							if(shipperList != null ){
								if( shipperList.size() > 1){
									setErrorMsg(j, "店铺编码："+shopRelMongo.getShopCode()+"为外部店，"+ "公司事业部货主信息存在多条，无法获取库位类型", resultList);
									failure += 1;
									continue;
								}else{
									MerchantShopShipperMongo shipperMongo = shipperList.get(0);
									stockLocationTypeId = shipperMongo.getStockLocationTypeId();
								}
							}
						}
						if(stockLocationTypeId != null && stockLocationTypeId.intValue() != 0){
							params.clear();
							params.put("buStockLocationTypeId", stockLocationTypeId);
							params.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
							stockLocationTypeConfigMongo = buStockLocationTypeConfigMongoDao.findOne(params);
							if(stockLocationTypeConfigMongo == null){
								setErrorMsg(j, "店铺编码："+shopRelMongo.getShopCode()+"配置的库位类型输入有误或未启用", resultList);
								failure += 1;
								continue;
							}
							stockLocationMap.put(shopRelMongo.getShopCode(), stockLocationTypeConfigMongo);
						}
					}


					OrderItemModel itemModel = new OrderItemModel() ;
					itemModel.setBarcode(merchandise.getBarcode());
					itemModel.setCommbarcode(merchandise.getCommbarcode());
					itemModel.setCreateDate(TimeUtils.getNow());
					itemModel.setDecTotal(decTotalBD.setScale(2, BigDecimal.ROUND_HALF_UP));	// 结算总金额
					itemModel.setGoodsCode(merchandise.getGoodsCode());
					itemModel.setGoodsDiscount(goodsDiscountBD);
					itemModel.setGoodsId(merchandise.getMerchandiseId());
					itemModel.setGoodsName(merchandise.getName());
					itemModel.setGoodsNo(merchandise.getGoodsNo());
					itemModel.setNum(Integer.valueOf(num));
					itemModel.setPrice(priceBD.setScale(5, BigDecimal.ROUND_HALF_UP));	// 结算单价
					itemModel.setOriginalPrice(originalPriceBD.setScale(5, BigDecimal.ROUND_HALF_UP)); //销售单价
					itemModel.setOriginalDecTotal(originalDecTotal.setScale(2, BigDecimal.ROUND_HALF_UP));	// 销售总金额
					// 事业部
					itemModel.setBuId(merchantBuRelMongo.getBuId());
					itemModel.setBuName(merchantBuRelMongo.getBuName());
					itemModel.setOrderBatchNo(batchNo);
					itemModel.setProductionDate(TimeUtils.parse(productionDate,"yyyy-MM-dd"));
					itemModel.setOverdueDate(TimeUtils.parse(overdueDate,"yyyy-MM-dd"));
					if(stockLocationTypeConfigMongo != null){
						itemModel.setStockLocationTypeId(stockLocationTypeConfigMongo.getBuStockLocationTypeId());
						itemModel.setStockLocationTypeName(stockLocationTypeConfigMongo.getName());
					}

					List<OrderItemModel> itemList = externalCodeOrderItemMap.get(externalCode);
					if(itemList == null) {
						itemList = new ArrayList<OrderItemModel>() ;
					}
					if(isTradeMechant) {//仅对公司主体的注册地类型为 境内的主体 计算内贸商品结算税额 、内贸商品结算金额（不含税）
						/**
						 * 内贸商品结算金额（不含税）= 商品结算金额/（1+13%），得出的金额保留2位小数，做四舍五入
						 * 内贸商品结算税额 = 商品结算金额 - 内贸商品结算金额（不含税），得出金额保留2位小数，做四舍五入
						 */
						BigDecimal tradePayment = itemModel.getDecTotal().divide(new BigDecimal(1.13),2,BigDecimal.ROUND_HALF_UP);
						BigDecimal tradePaymentTax = itemModel.getDecTotal().subtract(tradePayment).setScale(2,BigDecimal.ROUND_HALF_UP);

						itemModel.setTradeDecTotal(tradePayment);
						itemModel.setTradeDecTax(tradePaymentTax);
					}
					orderItemList.add(itemModel);

					itemList.add(itemModel) ;
					externalCodeOrderItemMap.put(externalCode, itemList) ;

				}
			}
		}
		/**
		 * 校验表头表体金额是否一致
		*  表头的实付总额-运费-税费=表体结算单价*数量，若不等于则提示表头表体金额不一致！
		 */
		if(failure == 0) {
			for(String externalCode : externalCodeOrderMap.keySet()) {
				OrderModel orderModel = externalCodeOrderMap.get(externalCode);
				List<OrderItemModel> itemList = externalCodeOrderItemMap.get(externalCode);
				if(itemList == null || itemList.size() < 1){
					setErrorMsg(0, "外部单号："+externalCode+"，不存在表体信息", resultList);
					failure += 1;
					continue;
				}
				BigDecimal itemAmount = BigDecimal.ZERO;
				for(OrderItemModel item : itemList) {
					BigDecimal tempAmount = item.getPrice().multiply(new BigDecimal(item.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
					itemAmount = itemAmount.add(tempAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				itemAmount = itemAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal orderAmount = orderModel.getPayment().subtract(orderModel.getTaxes()).subtract(orderModel.getWayFrtFee()).setScale(2, BigDecimal.ROUND_HALF_UP);
				if(orderAmount.compareTo(itemAmount) != 0) {
					setErrorMsg(0, "外部单号："+externalCode+"，表头表体金额不一致", resultList);
					failure += 1;
					continue;
				}
			}
		}

		//保存数据
		if(failure == 0) {
			int pageLimit = 1000 ;
			//表头分批插入
			int total = orderList.size() ;
			int pageNum = total / pageLimit ;
			if(total % pageLimit != 0) {
				pageNum += 1 ;
			}
			for(int page = 1 ; page <= pageNum ; page ++) {
				int start = (page - 1) * pageLimit ;
				int end = page * pageLimit ;
				if(end > total) {
					end = total ;
				}

				List<OrderModel> subList = orderList.subList(start, end);
				int num = orderDao.batchSave(subList);

				success += num;
			}
			//表体分批插入
			total = orderItemList.size() ;
			pageNum = total / pageLimit ;
			if(total % pageLimit != 0) {
				pageNum += 1 ;
			}
			for(int page = 1 ; page <= pageNum ; page ++) {
				int start = (page - 1) * pageLimit ;
				int end = page * pageLimit ;
				if(end > total) {
					end = total ;
				}
				List<OrderItemModel> subList = orderItemList.subList(start, end);
				orderItemDao.batchSave(subList);
			}

			List<OrderItemModel> updateItemList = new ArrayList<OrderItemModel>();
			Map<String,Long> externalCodeOrderIdMap = orderList.stream().collect(Collectors.toMap(OrderModel::getExternalCode, OrderModel::getId));
			for (String externalCode : externalCodeOrderItemMap.keySet()) {
				List<OrderItemModel> itemList = externalCodeOrderItemMap.get(externalCode);
				for (OrderItemModel orderItemModel : itemList) {
					OrderItemModel tempItemModel = new OrderItemModel();
					tempItemModel.setOrderId(externalCodeOrderIdMap.get(externalCode));
					tempItemModel.setId(orderItemModel.getId());
					updateItemList.add(tempItemModel);
				}
			}
			//表体分批更新
			total = updateItemList.size() ;
			pageNum = total / pageLimit ;
			if(total % pageLimit != 0) {
				pageNum += 1 ;
			}
			for(int page = 1 ; page <= pageNum ; page ++) {
				int start = (page - 1) * pageLimit ;
				int end = page * pageLimit ;
				if(end > total) {
					end = total ;
				}
				List<OrderItemModel> subList = updateItemList.subList(start, end);
				orderItemDao.batchUpdate(subList);
			}
		}

		Map map = new HashMap();
		map.put("success", success);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;

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

	/**
	 * 把两个数组组成一个map
	 *
	 * @param keys
	 *            键数组
	 * @param values
	 *            值数组
	 * @return 键值对应的map
	 */
	private Map<String, String> toMap(Object[] keys, Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				map.put((String) keys[i], values[i].toString());
			}
			return map;
		}
		return null;
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


	/**
	 * 校验费用格式
	 * @param free
	 * @param index
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean valiteFree(String free , int index , String msg ,List<ImportErrorMessage> resultList) {
		String reg = "^[0-9]+(.[0-9]{0,5})?$" ;
		Pattern pattern =Pattern.compile(reg);
		Matcher matcher = pattern.matcher(free);

		if(matcher.matches()) {
			return false ;
		}else {
			setErrorMsg(index, msg, resultList);
			return true ;
		}
	}

	@Override
	public int delImportOrder(List<Long> list) throws Exception {

		int count = orderDao.getImportOrderCountByIds(list) ;

		if(list.size() != count) {
			throw new RuntimeException("删除失败，所选项存在非手工导入订单") ;
		}

		int total = 0 ;

		for (Long id : list) {
			OrderModel orderModel = orderDao.searchById(id) ;

			if(orderModel != null) {

				if(!DERP_ORDER.ORDER_STATUS_8.equals(orderModel.getStatus())) {
					throw new RuntimeException("删除失败，订单号：" + orderModel.getCode() + "状态不为：待审核") ;
				}

				OrderModel tempModel = new OrderModel() ;
				tempModel.setId(orderModel.getId());
				tempModel.setStatus(DERP.DEL_CODE_006);
				tempModel.setModifyDate(TimeUtils.getNow());

				total += orderDao.modify(tempModel);

			}
		}

		return total;
	}

	@Override
	public boolean checkConditionsOrder(List<Long> list) throws Exception {

		int count = orderDao.getImportOrderCountByIds(list) ;

		if(list.size() != count) {
			return false ;
		}else {
			return true ;
		}

	}

	@Override
	public Map<String, Integer> getOrderGoodsInfo(List<Long> ids,Long merchantId) throws Exception {



		Map<String,Integer> map = new HashMap<String,Integer>();
		for (Long id : ids) {
			OrderModel orderModel = orderDao.searchById(id);

			//获取仓库信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", orderModel.getDepotId());
			DepotInfoMongo outDepot = depotInfoMongoDao.findOne(params);

			if(!DERP_ORDER.ORDER_STATUS_8.equals(orderModel.getStatus())) {
				throw new RuntimeException("审核失败，订单："+orderModel.getCode()+"的单据状态不为：待审核") ;
			}
			// 发货日期不能为空（除了内贸仓）
			if(!DERP_SYS.DEPOTINFO_TYPE_G.equals(outDepot.getType()) && orderModel.getDeliverDate()==null){
				throw new RuntimeException("审核失败，订单："+orderModel.getCode()+"的单据发货日期不能为空") ;
			}

			if(DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepot.getType())){//中转仓不校验
				continue;
			}

			OrderItemModel orderItemModel = new OrderItemModel() ;
			orderItemModel.setOrderId(id);
			List<OrderItemModel> itemList = orderItemDao.list(orderItemModel);

			for(OrderItemModel item:itemList){
				// 获取最大的关账日/月结日期
				FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
				closeAccountsMongo.setMerchantId(orderModel.getMerchantId());
				closeAccountsMongo.setDepotId(orderModel.getDepotId());
				closeAccountsMongo.setBuId(item.getBuId());
				String maxdate = "";
				if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
					maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
				}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
					maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
				}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
					maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
				}
				String maxCloseAccountsMonth="";
				if (StringUtils.isNotBlank(maxdate)) {
					// 获取该月份下月时间
					String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
					maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
				}
				if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
					// 关账下个月日期大于 入库日期
					if (orderModel.getDeliverDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
						throw new RuntimeException("审核失败，订单："+orderModel.getCode()+"的单据发货日期不能小于关账日期/月结") ;
					}
				}

				String goodsNo=item.getGoodsNo();
				Long goodsId=item.getGoodsId();
				Integer num = 0;
				if(map.containsKey(goodsId+"-"+orderModel.getDepotId()+"-"+outDepot.getCode()+"-"+goodsNo)){
					num = map.get(goodsId+"-"+orderModel.getDepotId()+"-"+outDepot.getCode()+"-"+goodsNo);
				}
				num+=item.getNum();
				map.put(goodsId+"-"+orderModel.getDepotId()+"-"+outDepot.getCode()+"-"+goodsNo, num);
			}
		}

		return map;
	}

	@Override
	public void auditOrder(List<Long> list ,String topidealCode,Long merchantId) throws Exception {
		for (Long id : list) {
			OrderModel orderModel = orderDao.searchById(id);

			if(orderModel == null){
				throw new RuntimeException("电商订单不存在");
			}
			if (DERP_ORDER.ORDER_STATUS_4.equals(orderModel.getStatus())) {
				throw new RuntimeException("电商订单已经发货,订单编号" + orderModel.getExternalCode());
			}else if (DERP_ORDER.ORDER_STATUS_027.equals(orderModel.getStatus())) {
				throw new RuntimeException("电商订单出库中,订单编号" + orderModel.getExternalCode());
			}
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", orderModel.getDepotId());// 根据仓库id
			DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
			// 若该电商订单的仓库是内贸仓
			if(DERP_SYS.DEPOTINFO_TYPE_G.equals(mongo.getType())) {
				Map<String, Object> queryMap = new HashMap<String, Object>();
				queryMap.put("depotId", mongo.getDepotId());
				queryMap.put("merchantId", merchantId);
				DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(queryMap);

				if(DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRel.getIsInOutInstruction())) {
					OrderModel oModel = new OrderModel();
					oModel.setId(orderModel.getId());
					oModel.setStatus(DERP_ORDER.ORDER_STATUS_3);// 单据状态:3-待发货
					oModel.setModifyDate(TimeUtils.getNow());
					oModel.setAuditDate(TimeUtils.getNow());// 审核时间
					orderDao.modify(oModel);
					// 下推众邦云仓发货单创建接口2.9
					Request request = new Request();
					DeliveryOrder deliveryOrder = new DeliveryOrder();
					deliveryOrder.setDeliveryOrderCode(orderModel.getCode());//取经分销系统生成的电商订单编号
					deliveryOrder.setOrderType("JYCK");
					deliveryOrder.setWarehouseCode(mongo.getCode());// 取众邦云仓仓库编码
					deliveryOrder.setSourcePlatformCode("OTHER");
					deliveryOrder.setCreateTime(TimeUtils.format(orderModel.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
					deliveryOrder.setPlaceOrderTime(TimeUtils.format(orderModel.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
					deliveryOrder.setOperateTime(TimeUtils.format(oModel.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));
					deliveryOrder.setShopNick(orderModel.getShopName());
					deliveryOrder.setLogisticsCode(orderModel.getLogisticsName());

					SenderInfo senderInfo = new SenderInfo();// 发件人信息
					senderInfo.setName("李守信");
					senderInfo.setMobile("18802726584");
					senderInfo.setProvince("广东省");
					senderInfo.setCity("广州市");
					senderInfo.setDetailAddress("花都区花东镇顺祥路15号星慧谷科技园A栋");

					deliveryOrder.setSenderInfo(senderInfo);// 发件人信息

					ReceiverInfo receiverInfo = new ReceiverInfo();
					receiverInfo.setName(orderModel.getReceiverName());
					receiverInfo.setMobile(orderModel.getReceiverTel());
					receiverInfo.setProvince(orderModel.getReceiverProvince());
					receiverInfo.setCity(orderModel.getReceiverCity());
					receiverInfo.setDetailAddress(orderModel.getReceiverAddress());
					receiverInfo.setArea(orderModel.getReceiverArea());
					deliveryOrder.setReceiverInfo(receiverInfo);// 收件人信息

					request.setDeliveryOrder(deliveryOrder);

					// 查询该电商订单下的商品信息
					OrderLines orderLines = new OrderLines();
					List<OrderLine> orderLineList = new ArrayList<>();
					OrderItemModel itemModel = new OrderItemModel();
					itemModel.setOrderId(orderModel.getId());
					List<OrderItemModel> itemList = orderItemDao.list(itemModel);
					for (int i = 0; i < itemList.size(); i++) {
						OrderItemModel orderItemModel = itemList.get(i);
						OrderLine orderLine = new OrderLine();
						orderLine.setOwnerCode(ApolloUtil.ywmsOwnerCode);
						orderLine.setItemCode(orderItemModel.getGoodsNo());//商品货号
						orderLine.setPlanQty(String.valueOf(orderItemModel.getNum()));//商品数量
						orderLine.setActualPrice(String.valueOf(orderItemModel.getOriginalPrice()));//电商订单中商品销售单价
						orderLineList.add(orderLine);
					}
					orderLines.setOrderLine(orderLineList);
					request.setOrderLines(orderLines);// 商品

					OrderPurshRoot root = new OrderPurshRoot();
					root.setRequest(request);
					//将null值转空字符串
					String jsonStr = com.alibaba.fastjson.JSONObject.toJSONString(root, SerializerFeature.WriteMapNullValue,
							SerializerFeature.WriteNullStringAsEmpty) ;

					com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject() ;
					json.put("type", PushYwmsTypeEnum.JYCK.getType()) ;
					json.put("jsonStr", jsonStr) ;
					json.put("method", EpassAPIMethodEnum.EPASS_E010_METHOD.getMethod()) ;
					json.put("order_id", orderModel.getCode()) ;

					/**
					 * 生成冻结数据 并推送库存
					 */
					getInventoryFreezeRootJson(orderModel,itemList);


					try {
						//下推众邦云仓
						rocketMQProducer.send(json.toJSONString(), MQPushApiEnum.PUSH_YWMS.getTopic(),
								MQPushApiEnum.PUSH_YWMS.getTags());
					} catch (Exception e) {
						LOGGER.error("--------电商订单审核下推众邦云仓消息发送失败-------", json.toString());
					}
				}
			}else{
				OrderModel oModel = new OrderModel();
				oModel.setId(orderModel.getId());
				oModel.setStatus(DERP_ORDER.ORDER_STATUS_027);// 单据状态:027-出库中
				oModel.setModifyDate(TimeUtils.getNow());
				oModel.setAuditDate(TimeUtils.getNow());// 审核时间
				orderDao.modify(oModel);

				//获取电商订单明细
				OrderItemModel orderItemModel = new OrderItemModel() ;
				orderItemModel.setOrderId(orderModel.getId());
				List<OrderItemModel> orderItemList = orderItemDao.list(orderItemModel);
				for (int i = 0; i < orderItemList.size(); i++) {
					if(null==orderItemList.get(i).getBuId()){
						throw new RuntimeException("该电商订单表体商品货号："+orderItemList.get(i).getGoodsNo()+"没有存事业部信息,订单编号"+orderModel.getExternalCode());
					}
				}

				//新增外部订单号
				OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
				orderExternalCodeModel.setExternalCode(orderModel.getExternalCode());
				orderExternalCodeModel.setOrderSource(1);	// 订单来源  1:电商订单, 2:装载交运 3:销售出库
				orderExternalCodeModel.setCreateDate(TimeUtils.getNow());

				orderExternalCodeDao.save(orderExternalCodeModel);

				// 新增运单
				WayBillModel wModel = new WayBillModel();
				wModel.setOrderId(orderModel.getId());// 电商订单ID

				String wayBillNo = orderModel.getWayBillNo();

				if(StringUtils.isBlank(wayBillNo)) {
					wayBillNo = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ZDY) ;
				}

				wModel.setWayBillNo(wayBillNo);// 运单号
				wModel.setDeliverDate(orderModel.getDeliverDate());// 发货时间
				wModel.setLogisticsName(orderModel.getLogisticsName());// 物流公司名称
				wModel.setType("20");// 服务类型
				wayBillDao.save(wModel);

				List<WayBillItemModel> itemList = new ArrayList<WayBillItemModel>();
				for (OrderItemModel tempOrderItem : orderItemList) {
					// 运单表体
					WayBillItemModel wayBillItemModel = new WayBillItemModel();
					wayBillItemModel.setBillId(wModel.getId());// '运单号id
					wayBillItemModel.setGoodsId(tempOrderItem.getGoodsId());// 商品ID
					wayBillItemModel.setGoodsNo(tempOrderItem.getGoodsNo());// 商品货号
					wayBillItemModel.setGoodsName(tempOrderItem.getGoodsName());// 商品名称
					wayBillItemModel.setGoodsCode(tempOrderItem.getGoodsCode());// 商品编号
					wayBillItemModel.setBarcode(tempOrderItem.getBarcode());// 商品条形码
					wayBillItemModel.setNum(tempOrderItem.getNum());// 数量
					wayBillItemModel.setPrice(tempOrderItem.getPrice());// 单价
					wayBillItemModel.setCphTaxFee(orderModel.getTaxes());// 税率'
					wayBillItemModel.setBatchNo(tempOrderItem.getOrderBatchNo());
					wayBillItemModel.setProductionDate(tempOrderItem.getProductionDate());
					wayBillItemModel.setOverdueDate(tempOrderItem.getOverdueDate());

					wayBillItemDao.save(wayBillItemModel);
					// 推库存商品
					itemList.add(wayBillItemModel);
				}

				//扣减销售出库库存量
				InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now1 = sdf.format(new Date());
				invetAddOrSubtractRootJson.setMerchantId(orderModel.getMerchantId().toString());
				invetAddOrSubtractRootJson.setMerchantName(orderModel.getMerchantName());
				invetAddOrSubtractRootJson.setTopidealCode(topidealCode);
				invetAddOrSubtractRootJson.setDepotId(orderModel.getDepotId().toString());
				invetAddOrSubtractRootJson.setDepotName(orderModel.getDepotName());
				invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
				invetAddOrSubtractRootJson.setDepotType(mongo.getType());
				invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
				invetAddOrSubtractRootJson.setOrderNo(orderModel.getCode());
				invetAddOrSubtractRootJson.setBusinessNo(orderModel.getExternalCode());// 外部单号
				invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_007);	// 电商订单
				invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_008);
				invetAddOrSubtractRootJson.setSourceDate(now1);
				invetAddOrSubtractRootJson.setShopCode(orderModel.getShopCode());
				invetAddOrSubtractRootJson.setStorePlatformName(orderModel.getStorePlatformName());	// 电商平台名称
				String deliverDate = sdf.format(orderModel.getDeliverDate());// 发货时间
				invetAddOrSubtractRootJson.setDivergenceDate(deliverDate);
				// 获取当前年月
				invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(orderModel.getDeliverDate()));
				List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
				SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
				for (WayBillItemModel item : itemList) {
					WayBillModel wayBillModel = wayBillDao.searchById(item.getBillId());
					// 电商订单表体信息
					OrderItemModel itemModel = new OrderItemModel();
					itemModel.setOrderId(wayBillModel.getOrderId());
					itemModel.setGoodsId(item.getGoodsId());
					List<OrderItemModel> itemAllList = orderItemDao.list(itemModel);
					OrderItemModel oItemModel= itemAllList.get(0);

					InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

					invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(oItemModel.getGoodsId()));//原货号
					invetAddOrSubtractGoodsListJson.setGoodsNo(oItemModel.getGoodsNo());

					invetAddOrSubtractGoodsListJson.setCommbarcode(oItemModel.getCommbarcode());
					invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
					invetAddOrSubtractGoodsListJson.setGoodsName(oItemModel.getGoodsName());


					if (itemAllList.get(0).getBuId() != null && StringUtils.isNotBlank(itemAllList.get(0).getBuName())) {
						invetAddOrSubtractGoodsListJson.setBuId(String.valueOf(itemAllList.get(0).getBuId()));	// 事业部
						invetAddOrSubtractGoodsListJson.setBuName(itemAllList.get(0).getBuName());
					}

					invetAddOrSubtractGoodsListJson.setType("0");
					invetAddOrSubtractGoodsListJson.setNum(item.getNum());
					invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
					invetAddOrSubtractGoodsListJson.setBatchNo(item.getBatchNo());
					if (item.getProductionDate()!=null) {
						invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(item.getProductionDate()));
					}
					if (item.getOverdueDate()!=null) {
						invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(item.getOverdueDate()));
						String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
						invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);//是否过期（0是 1否）
					}else {
						invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）
					}
					invetAddOrSubtractGoodsListJson.setStockLocationTypeId(oItemModel.getStockLocationTypeId() == null ? "" : oItemModel.getStockLocationTypeId().toString());
					invetAddOrSubtractGoodsListJson.setStockLocationTypeName(oItemModel.getStockLocationTypeName());
					invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
				}
				invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
				//库存回推数据
				Map<String, Object> customParam=new HashMap<String, Object>();
				invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTags());//回调标签
				invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTopic());//回调主题
				customParam.put("code", orderModel.getCode());// 电商订单内部单号
				invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
				rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
			}
		}
	}

	/**
	 * 获取冻结数据
	 * @param orderModel
	 * @param itemList
	 */
	private void getInventoryFreezeRootJson(OrderModel orderModel, List<OrderItemModel> itemList) throws Exception{
		//冻结库存
		InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		inventoryFreezeRootJson.setMerchantId(orderModel.getMerchantId().toString());
		inventoryFreezeRootJson.setMerchantName(orderModel.getMerchantName());
		inventoryFreezeRootJson.setDepotId(orderModel.getDepotId().toString());
		inventoryFreezeRootJson.setDepotName(orderModel.getDepotName());
		inventoryFreezeRootJson.setOrderNo(orderModel.getCode());
		inventoryFreezeRootJson.setBusinessNo(orderModel.getExternalCode());// 电商订单
		inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_007);
		inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_007);
		inventoryFreezeRootJson.setSourceDate(now);
		inventoryFreezeRootJson.setOperateType("0");
		List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
		for(OrderItemModel item : itemList){
			InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();
			inventoryFreezeGoodsListJson.setGoodsId(item.getGoodsId().toString());
			inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());
			inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());
			inventoryFreezeGoodsListJson.setDivergenceDate(TimeUtils.format(orderModel.getTradingDate(), "yyyy-MM-dd HH:mm:ss"));
			inventoryFreezeGoodsListJson.setNum(item.getNum());
			inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
		}
		inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
		rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());


	}

	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@Override
	public OrderDTO searchDtoDetail(Long id) throws SQLException {
		OrderDTO dto = new OrderDTO();
		dto.setId(id);
		OrderDTO orderDTO = orderDao.searchDtoById(dto);
		if(orderDTO==null){	// 查询历史表
			OrderHistoryDTO historyDTO = new OrderHistoryDTO();
			historyDTO.setId(id);
			historyDTO = orderHistoryDao.searchDtoById(historyDTO);
			orderDTO = new OrderDTO();
			BeanUtils.copyProperties(historyDTO,orderDTO);
		}
		return orderDTO;
	}
	/**
	 * 根据ID查询历史表获取详情
	 * @param id
	 * @return
	 */
	@Override
	public OrderHistoryDTO searchHistoryDtoDetail(Long id) throws SQLException {
		OrderHistoryDTO dto = new OrderHistoryDTO();
		dto.setId(id);
		return orderHistoryDao.searchDtoById(dto);
	}

	@Override
	public List<MerchantShopRelMongo> getMerchantShopRelList(Long merchantId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", "1") ;
		List<MerchantShopRelMongo> shopList = merchantShopRelMongoDao.findAll(params);	//mongo查询参数集合
		return shopList;
	}

	/**
	 * 获取事业部补录列表数据-导出
	 */
	@Override
	public List<OrderDTO> businessUnitRecordExport(OrderDTO dto) throws SQLException {
		return orderDao.businessUnitRecordExport(dto);
	}
	@Override
	public OrderDTO listBusinessUnitRecordByPage(OrderDTO dto) throws SQLException {
		return orderDao.listBusinessUnitRecordByPage(dto);
	}
	/**
	 * 事业部补录列表--批量更新
	 */
	@Override
	public String modifyBusinessUnitRecord(List<String> list, Long buId,String buName, String topidealCode, Long merchantId,Long userId)
			throws Exception {
		String msg ="";
		Set<String> externalCodeMap = new HashSet<>(); // 记录该外部单号已推过库存
		Map<String,BuStockLocationTypeConfigMongo> stockLocationMap = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			String infoStr = list.get(i);
			String[] info = infoStr.split(":");	// 外部交易单号：商品ID
			String externalCode = info[0];
			String goodsIdStr = info[1];
			Long goodsId = Long.valueOf(goodsIdStr);

			// 若已在下面推过库存，则不往下走了
			if(externalCodeMap.contains(externalCode)){
				continue;
			}
			OrderModel orderModel = new OrderModel();
			orderModel.setExternalCode(externalCode);
			orderModel.setMerchantId(merchantId);// 有一店多货主情况
			orderModel = orderDao.searchByModel(orderModel);

			Map<String,Object> params = new HashMap<>();
			params.put("shopCode", orderModel.getShopCode()) ;
			params.put("status", "1") ;
			MerchantShopRelMongo shopRelMongo = merchantShopRelMongoDao.findOne(params);

			if(orderModel==null){
				msg+="批量更新失败，外部交易单号:"+externalCode+"没查到电商订单";
				continue;
			}
			// 不是出库中
			if(!DERP_ORDER.ORDER_STATUS_027.equals(orderModel.getStatus())){
				msg+="批量更新失败，外部交易单号:"+externalCode+"该电商订单状态不为出库中";
				continue;
			}

			OrderItemModel itemModel = new OrderItemModel();
			itemModel.setOrderId(orderModel.getId());
			itemModel.setGoodsId(goodsId);
			List<OrderItemModel> itemList = orderItemDao.list(itemModel);

			// 校验公司-仓库-事业部的关联表(事业部是否为对应”商家+仓库”下已有关联的事业部)
			Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
			merchantDepotBuRelParam.put("merchantId", merchantId);
			merchantDepotBuRelParam.put("depotId", orderModel.getDepotId());	// 出库仓库
			merchantDepotBuRelParam.put("buId", buId);
			MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
			if(outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)){
				msg+="批量更新失败,外部交易单号："+externalCode+"商品货号为:"+itemList.get(0).getGoodsNo()+"该电商订单的商品事业部:"+buName+"未查到该公司仓库事业部关联信息\n";
				continue;
			}
			// 校验事业部与当前账号所绑定的事业部是否匹配
			boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(userId, outMerchantDepotBuRelMongo.getBuId());
			if(!userRelateBu){
				msg+="批量更新失败,事业部id为："+outMerchantDepotBuRelMongo.getBuId()+",用户id："+userId+",无权限操作该事业部\n";
				continue;
			}

			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(orderModel.getMerchantId());
			closeAccountsMongo.setDepotId(orderModel.getDepotId());
			closeAccountsMongo.setBuId(buId);
			String maxdate = "";
			if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			}
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 发货时间(发货时间不可小于关账日期/结转日期)
				if (orderModel.getDeliverDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					msg+="批量更新失败，外部交易单号："+externalCode+"，商品货号："+itemList.get(0).getGoodsNo()+" 发货时间必须大于关账日期/月结日期\n";
					continue;
				}
			}

			Long stockLocationTypeId = null;
			BuStockLocationTypeConfigMongo stockLocationTypeConfigMongo = stockLocationMap.get(shopRelMongo.getShopCode());
			//若为单主店则取店铺信息配置表头的“库位类型”；若为“外部店”则取对应的货主事业部的“库位类型”
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(shopRelMongo.getStoreTypeCode())){
				stockLocationTypeId = shopRelMongo.getStockLocationTypeId();
			}else if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(shopRelMongo.getStoreTypeCode())){
				params.clear();
				params.put("shopId",shopRelMongo.getShopId());
				params.put("merchantId",merchantId);
				params.put("buId", buId);
				List<MerchantShopShipperMongo> shipperList = merchantShopShipperMongoDao.findAll(params);
				if(shipperList != null ){
					if( shipperList.size() > 1){
						msg+="店铺编码："+shopRelMongo.getShopCode()+"为外部店，"+ "公司事业部货主信息存在多条，无法获取库位类型\n";
						continue;
					}else{
						MerchantShopShipperMongo shipperMongo = shipperList.get(0);
						stockLocationTypeId = shipperMongo.getStockLocationTypeId();
					}
				}
			}
			if(stockLocationTypeId != null && stockLocationTypeId.intValue() != 0){
				params.clear();
				params.put("buStockLocationTypeId", stockLocationTypeId);
				params.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
				stockLocationTypeConfigMongo = buStockLocationTypeConfigMongoDao.findOne(params);

				stockLocationMap.put(shopRelMongo.getShopCode(), stockLocationTypeConfigMongo);
			}

			for (int j = 0; j < itemList.size(); j++) {
				// 修改该电商订单的某个商品的事业部
				OrderItemModel itemParam = new OrderItemModel();
				itemParam.setId(itemList.get(j).getId());
				itemParam.setBuId(buId);
				itemParam.setBuName(buName);
				itemParam.setModifyDate(TimeUtils.getNow());
				if(stockLocationTypeConfigMongo != null){
					itemParam.setStockLocationTypeId(stockLocationTypeConfigMongo.getBuStockLocationTypeId());
					itemParam.setStockLocationTypeName(stockLocationTypeConfigMongo.getName());
				}

				int result = orderItemDao.modify(itemParam);
				if(result<0){
					msg+="批量更新失败,外部交易单号:"+externalCode+"商品货号："+itemList.get(0).getGoodsNo()+"\n";
					continue;
				}
			}


			// 判断该电商订单下所有商品是否都存在事业部
			int isFlag=0;
			OrderItemModel allGoods = new OrderItemModel();
			allGoods.setOrderId(orderModel.getId());
			List<OrderItemModel> allGoodsList = orderItemDao.list(allGoods);
			for (int j = 0; j < allGoodsList.size(); j++) {
				OrderItemModel orderItemModel = allGoodsList.get(j);
				// 若该电商订单下商品有一个商品事业部为空，则不推库存
				if(orderItemModel.getBuId()==null){
					isFlag=isFlag+1;
				}
			}
			if(isFlag!=0){
				// 若该电商订单下商品有一个商品事业部为空，则不推库存
				continue;
			}

			if(!externalCodeMap.contains(externalCode)){
				externalCodeMap.add(externalCode);
			}
			// 推库存
		 	WayBillModel wModel = new WayBillModel();// 运单表头
			wModel.setOrderId(orderModel.getId());// 电商订单ID
			List<WayBillModel> wayBillList= wayBillDao.list(wModel);
			if(wayBillList==null || wayBillList.size()==0){
				msg+="批量更新失败,外部交易单号:"+externalCode+"没有查询到运单信息"+"\n";
				continue;
			}else if(wayBillList.size()>1){
				msg+="批量更新失败,外部交易单号:"+externalCode+"查询到多条运单信息"+"\n";
				continue;
			}

			WayBillItemModel wayBillItemModel = new WayBillItemModel();
			wayBillItemModel.setBillId(wayBillList.get(0).getId());
			// 查询该运单下运单表体
			List<WayBillItemModel> wayBillItemList=wayBillItemDao.list(wayBillItemModel);
			//扣减销售出库库存量
			InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now1 = sdf.format(new Date());
			invetAddOrSubtractRootJson.setMerchantId(orderModel.getMerchantId().toString());
			invetAddOrSubtractRootJson.setMerchantName(orderModel.getMerchantName());
			invetAddOrSubtractRootJson.setTopidealCode(topidealCode);
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", orderModel.getDepotId());// 根据仓库id
			DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
			invetAddOrSubtractRootJson.setDepotId(orderModel.getDepotId().toString());
			invetAddOrSubtractRootJson.setDepotName(orderModel.getDepotName());
			invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
			invetAddOrSubtractRootJson.setDepotType(mongo.getType());
			invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
			invetAddOrSubtractRootJson.setOrderNo(orderModel.getCode());
			invetAddOrSubtractRootJson.setBusinessNo(orderModel.getExternalCode());// 外部单号
			invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_007);	// 电商订单
			invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_008);// 008-电商订单出库
			invetAddOrSubtractRootJson.setSourceDate(now1);
			invetAddOrSubtractRootJson.setShopCode(orderModel.getShopCode());
			invetAddOrSubtractRootJson.setStorePlatformName(orderModel.getStorePlatformName());	// 电商平台名称
			String deliverDate = sdf.format(orderModel.getDeliverDate());// 发货时间
			invetAddOrSubtractRootJson.setDivergenceDate(deliverDate);
			// 获取当前年月
			invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(orderModel.getDeliverDate()));
			List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();

			SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
			for (WayBillItemModel item : wayBillItemList) {
				WayBillModel wayBillModel = wayBillDao.searchById(item.getBillId());
				// 电商订单表体信息
				OrderItemModel orderItem = new OrderItemModel();
				orderItem.setOrderId(wayBillModel.getOrderId());
				orderItem.setGoodsId(item.getGoodsId());
				List<OrderItemModel> orderItemList = orderItemDao.list(orderItem);
				OrderItemModel oItemModel = orderItemList.get(0);

				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

				invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(oItemModel.getGoodsId()));
				invetAddOrSubtractGoodsListJson.setGoodsNo(oItemModel.getGoodsNo());
				invetAddOrSubtractGoodsListJson.setCommbarcode(oItemModel.getCommbarcode());
				invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
				invetAddOrSubtractGoodsListJson.setGoodsName(oItemModel.getGoodsName());

				invetAddOrSubtractGoodsListJson.setBuId(String.valueOf(orderItemList.get(0).getBuId()));	// 取每个商品对应的事业部id
				invetAddOrSubtractGoodsListJson.setBuName(orderItemList.get(0).getBuName());// 取每个商品对应的事业部名称
				invetAddOrSubtractGoodsListJson.setType("0");
				invetAddOrSubtractGoodsListJson.setNum(item.getNum());
				invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
				invetAddOrSubtractGoodsListJson.setBatchNo(item.getBatchNo());
				if (item.getProductionDate()!=null) {
					invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(item.getProductionDate()));
				}
				if (item.getOverdueDate()!=null) {
					invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(item.getOverdueDate()));
					String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
					invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);//是否过期（0是 1否）
				}else {
					invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）
				}
				//海外仓 理货单位必填
				String unit="";
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
					if (DERP.ORDER_TALLYINGUNIT_00.equals(orderModel.getTallyingUnit())) {
						unit=DERP.INVENTORY_UNIT_0;
					}else if (DERP.ORDER_TALLYINGUNIT_01.equals(orderModel.getTallyingUnit())) {
						unit=DERP.INVENTORY_UNIT_1;
					}else if (DERP.ORDER_TALLYINGUNIT_02.equals(orderModel.getTallyingUnit())) {
						unit=DERP.INVENTORY_UNIT_2;
					}
				}
				invetAddOrSubtractGoodsListJson.setUnit(unit);
				invetAddOrSubtractGoodsListJson.setStockLocationTypeId(oItemModel.getStockLocationTypeId() == null ? "" : oItemModel.getStockLocationTypeId().toString());
				invetAddOrSubtractGoodsListJson.setStockLocationTypeName(oItemModel.getStockLocationTypeName());
				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
			}
			invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
			//库存回推数据
			Map<String, Object> customParam=new HashMap<String, Object>();
			invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTags());//回调标签
			invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTopic());//回调主题
			customParam.put("code", orderModel.getCode());// 电商订单内部单号
			invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
			rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		}
		return msg;
	}


	/**
	 * 判断是否yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	private boolean isDate(String date) {
		Pattern p = Pattern.compile("^([1-2]{1}\\d{3})\\-(([0]{1}[1-9]{1})|([1]{1}[0-2]{1}))\\-(([0]{1}[1-9]{1})|([1-2]{1}\\d{1})|([3]{1}[0-1]{1}))\\s(([0-1]{1}\\d{1})|([2]{1}[0-3]))\\:([0-5]{1}\\d{1})\\:([0-5]{1}\\d{1})$");
		return p.matcher(date).matches();
	}

	/**
	 * 判断是否yyyy-MM-dd
	 * @param date
	 * @return
	 */
	private boolean isValidDate(String date) {
		String path="\\d{4}-\\d{1,2}-\\d{1,2}";//定义匹配规则
        Pattern p=Pattern.compile(path);//实例化Pattern
        return p.matcher(date).matches();

	}

	@Override
	public String createExportExcel(FileTaskMongo task, String basePath) throws Exception {
		//解析json参数
		JSONObject jsonData = JSONObject.fromObject(task.getParam());
		OrderDTO dto = (OrderDTO) JSONObject.toBean(jsonData, OrderDTO.class);
		basePath = basePath + "/" + task.getTaskType() + "/" + dto.getMerchantId() + task.getOwnMonth() + TimeUtils.formatFullTime() ;
		LOGGER.info("--------------------电商订单生成Excel---开始----------------------");

		boolean isExist = false;
		if (new File(basePath).exists()) {
			isExist = true;
		}

		int pageSize = 5000; //每页5000
		int maxSize = 300000; //每个文件存放最大记录数

		//获取所有母品牌
		Map<String, BrandParentMongo> brandParentMap = new HashMap<>();
		//获取所有商品信息
		Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();

		Integer count = 0;
		// 12个月以前数据 查历史表
		if("2".equals(dto.getOrderTimeRange())){
			count = orderHistoryDao.getExportOrderCount(dto);
		}else{
			count = orderDao.getExportOrderCount(dto);
		}
		//每30w数据生成一个excel
		Double fileNum = Math.ceil((float) count / maxSize);
		for (int i = 0; i < fileNum ; i++) {
			List<Map<String, Object>> exportMap = new ArrayList<>();
			Integer total = count > maxSize * (i + 1) ? maxSize * (i + 1) : count;

			for (int j = maxSize * i; j < total; ) {
				int pageSub = (j + pageSize) < total ? (j + pageSize) : total;
				dto.setBegin(j);
				dto.setPageSize(pageSize);
				// 12个月以前数据 查历史表
				List<Map<String, Object>> tempList =  new ArrayList<Map<String, Object>>();
				if("2".equals(dto.getOrderTimeRange())){
					tempList = orderHistoryDao.getExportOrderMap(dto);
				}else{
					tempList = orderDao.getExportOrderMap(dto);
				}
				List<Long> goodsIdList = tempList.stream().map(export -> (Long)export.get("goods_id")).collect(Collectors.toList());
				List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", goodsIdList);
				for (MerchandiseInfoMogo merchandiseInfo : merchandiseInfoMogos) {
					if (!merchandiseInfoMogoMap.containsKey(merchandiseInfo.getMerchandiseId()) && StringUtils.isNotBlank(merchandiseInfo.getCommbarcode())) {
						merchandiseInfoMogoMap.put(merchandiseInfo.getMerchandiseId(), merchandiseInfo);
					}
				}
				for (Map<String, Object> map : tempList) {
					map.put("status", DERP_ORDER.getLabelByKey(DERP_ORDER.order_statusList, (String) map.get("status")));
					map.put("store_platform_name", DERP.getLabelByKey(DERP.storePlatformList, (String) map.get("store_platform_name")));
					map.put("currency", DERP.getLabelByKey(DERP.currencyCodeList, (String) map.get("currency")));
					map.put("shop_type_code", DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, (String) map.get("shop_type_code")));

					Long goodsId = (Long) map.get("goods_id");
					MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoMap.get(goodsId);
					if (merchandiseInfo != null) {
						BrandParentMongo brandParent = brandParentMap.get(merchandiseInfo.getCommbarcode());
						if (brandParent == null) {
							brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(merchandiseInfo.getCommbarcode());
						}
						if (brandParent != null) {
							map.put("brandParentName", brandParent.getName());
							map.put("superiorParentName", brandParent.getSuperiorParentBrand());
							brandParentMap.put(merchandiseInfo.getCommbarcode(), brandParent);
						}
					}
				}
				exportMap.addAll(tempList);
				j = pageSub;
			}
			String[] columns={"订单号","外部订单号","平台订单号","单据状态","平台名称","店铺名称","店铺类型","仓库名称","物流企业名称","运单号","包裹重量",
					"交易时间","申报时间","放行时间","发货时间","订单总金额","运费","税费","总优惠金额","订单总佣金","事业部","客户","商品货号","条形码",
					"商品编码","商品名称","数量","销售单价","销售总额","结算单价","结算总额","商品优惠金额","商品佣金","币种","商品税费",
					"内贸商品结算税额","内贸商品结算金额（不含税）","母品牌","标准品牌","库位类型"};
			String[] keys={"code","external_code","ex_order_id","status","store_platform_name", "shop_name","shop_type_code","depot_name",
					"logistics_name","way_bill_no","weight","trading_date","declare_date","release_date","deliver_date","payment","way_frt_fee",
					"taxes","discount","sale_com","bu_name","customer_name","goods_no", "barcode","goods_code","goods_name","num","original_price",
					"original_dec_total","price","dec_total","goods_discount","goods_sale_com","currency","goods_tax","trade_dec_tax","trade_dec_total",
					"superiorParentName","brandParentName","stock_location_type_name"};
			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(task.getRemark(), columns, keys, exportMap);
			//导出文件
			String fileName = task.getRemark() + ".xlsx";
			if(fileNum > 1){
				fileName = task.getRemark() + (i+1) + ".xlsx";
			}
			if (isExist) {
				//删除目录下的子文件
				DownloadExcelUtil.deleteFile(basePath);
				isExist = false;
			}
			//创建目录
			new File(basePath).mkdirs();

			FileOutputStream fileOut = new FileOutputStream(basePath + "/" + fileName);
			wb.write(fileOut);
			fileOut.close();

			LOGGER.info("--------第" + (i+1) + "个文件创建结束-----------");
		}
		LOGGER.info("--------------------电商订单生成Excel---结束----------------------");

		return basePath;
	}
}
