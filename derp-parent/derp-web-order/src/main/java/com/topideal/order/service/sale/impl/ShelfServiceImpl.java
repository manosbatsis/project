package com.topideal.order.service.sale.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.TobTemporaryReceiveBillDao;
import com.topideal.dao.bill.TobTemporaryReceiveBillItemDao;
import com.topideal.dao.bill.TobTemporaryReceiveBillRebateItemDao;
import com.topideal.dao.common.SdSaleConfigDao;
import com.topideal.dao.common.SdSaleConfigItemDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.SdSaleConfigDTO;
import com.topideal.entity.dto.sale.SaleOutDepotDTO;
import com.topideal.entity.dto.sale.SaleShelfDTO;
import com.topideal.entity.dto.sale.SaleShelfIdepotDTO;
import com.topideal.entity.dto.sale.ShelfDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillModel;
import com.topideal.entity.vo.common.SdSaleConfigItemModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.http.HttpClientUtil;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.order.service.sale.SaleOrderService;
import com.topideal.order.service.sale.ShelfService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShelfServiceImpl implements ShelfService{
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(ShelfServiceImpl.class);
	@Autowired
	private ShelfDao shelfDao ;
	@Autowired
	private SaleShelfDao saleShelfDao ;
	@Autowired
	private SdSaleConfigDao sdSaleConfigDao;
	@Autowired
	private SdSaleConfigItemDao sdSaleConfigItemDao;
	@Autowired
	private SaleSdOrderDao saleSdOrderDao;
	@Autowired
	private SaleSdOrderItemDao saleSdOrderItemDao;
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private SaleOrderService saleOrderService;
	@Autowired
	private SaleOrderDao saleOrderDao;
	@Autowired
	private RMQProducer rocketMQProducer;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private SaleShelfIdepotDao saleShelfIdepotDao;
	@Autowired
	private SaleShelfIdepotItemDao saleShelfIdepotItemDao;
	@Autowired
    private TobTemporaryReceiveBillDao tobTemporaryReceiveBillDao;
    @Autowired
    private TobTemporaryReceiveBillItemDao tobTemporaryReceiveBillItemDao;
    @Autowired
    private TobTemporaryReceiveBillRebateItemDao tobTemporaryReceiveBillRebateItemDao;
    @Autowired
	private SaleDeclareOrderDao saleDeclareOrderDao;

	@Override
	public ShelfDTO listShelf(ShelfDTO shelfDTO, User user) throws SQLException {
		if (shelfDTO.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				shelfDTO.setList(new ArrayList<>());
				shelfDTO.setTotal(0);
				return shelfDTO;
			}
			shelfDTO.setBuList(buList);
		}
		return shelfDao.searchByPage1(shelfDTO);
	}

	@Override
	public ShelfDTO searchDetail(Long id)  throws SQLException {
		ShelfDTO dto = shelfDao.searchShelfModelById(id);
		if(dto.getSaleOutDepotId() != null) {
			SaleOutDepotModel outModel = saleOutDepotDao.searchById(dto.getSaleOutDepotId());
			dto.setSaleDeclareOrderCode(outModel.getSaleDeclareOrderCode());
		}

		return dto;
	}

	@Override
	public Integer getOrderCount(ShelfDTO shelfDTO, User user) throws SQLException{
		if (shelfDTO.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				return 0;
			}
			shelfDTO.setBuList(buList);
		}
		return shelfDao.getOrderCount(shelfDTO);
	}

	@Override
	public List<Map<String, Object>> getExportList(ShelfDTO shelfDTO, User user) {
		if (shelfDTO.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				return new ArrayList<>();
			}
			shelfDTO.setBuList(buList);
		}
		return shelfDao.getExportList(shelfDTO);
	}

	@Override
	public List<String> generateSaleSdOrder(String ids, User user) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		Map<String,List<SaleSdOrderItemModel>> resultMap =  new HashMap<String, List<SaleSdOrderItemModel>>();
		Map<String,BigDecimal> amountMap =  new HashMap<String, BigDecimal>();
		Map<String,Integer> numMap =  new HashMap<String, Integer>();
		List<String> shelfCodeList = new ArrayList<String>();

		for(Long id :idList) {
			ShelfModel shelfModel = shelfDao.searchById(id);

			SaleOrderModel saleOrderModel = saleOrderDao.searchById(shelfModel.getSaleOrderId());
			if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
				throw new RuntimeException("上架单：" + shelfModel.getCode() + " 对应的销售订单红冲状态不为空，无法生成销售SD单");
			}
			// 查询最大关账月份
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(shelfModel.getMerchantId());
			closeAccountsMongo.setBuId(shelfModel.getBuId());
			String maxDate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxDate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth) && shelfModel.getShelfDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new RuntimeException("单据:" + shelfModel.getCode() + "所在上架月份" + TimeUtils.formatMonth(shelfModel.getShelfDate()) + "已经关账，无法生成销售SD单");
			}

			//校验该上架单是否已有存在关联销售SD单数据，存在至少一条则触发失败
			SaleSdOrderModel querySaleSdOrder = new SaleSdOrderModel();
			querySaleSdOrder.setOrderCode(shelfModel.getCode());
			List<SaleSdOrderModel> querySaleSdOrderList = saleSdOrderDao.list(querySaleSdOrder);
			if (querySaleSdOrderList != null && querySaleSdOrderList.size() > 0) {
				throw new RuntimeException("上架单：" + shelfModel.getCode() + " 已存在销售SD单");
			}

			List<SaleShelfDTO> saleShelfList = saleShelfDao.getSaleShelfModelById(id);

			//1、以“公司+事业部+客户”查询销售SD配置表 ,上架日期 在生效日期范围内，若无则结束；
			SdSaleConfigDTO config = new SdSaleConfigDTO();
			config.setMerchantId(shelfModel.getMerchantId());
			config.setBuId(shelfModel.getBuId());
			config.setCustomerId(shelfModel.getCustomerId());
			config.setOrderDate(shelfModel.getShelfDate());
			config.setStatus(DERP_ORDER.SDPURCHASE_STATUS_1);
			List<SdSaleConfigDTO> configList = sdSaleConfigDao.listDTO(config);
			if (configList == null || configList.size() <= 0) {
				throw new RuntimeException("公司：" + shelfModel.getMerchantName() + "+事业部：" + shelfModel.getBuName() + "+客户：" + shelfModel.getCustomerName() + "未找到对应销售SD配置");
			}
			//审核时间为最新的一条配置信息
			configList = configList.stream().sorted(Comparator.comparing(SdSaleConfigDTO::getAuditDate).reversed()).collect(Collectors.toList());
			config = configList.get(0);

			List<SdSaleConfigItemModel> sdConfigItemList = new ArrayList<>();
			//获取单比例配置
			SdSaleConfigItemModel queryItemModel = new SdSaleConfigItemModel();
			queryItemModel.setSaleConfigId(config.getId());
			queryItemModel.setSdType(DERP_ORDER.SDTYPECONFIG_TYPE_1);
			List<SdSaleConfigItemModel> simpleConfigItemList = sdSaleConfigItemDao.list(queryItemModel);
			if (simpleConfigItemList != null && simpleConfigItemList.size() > 0) {
				sdConfigItemList.addAll(simpleConfigItemList);
			}
			List<String> commBarcodeList = saleShelfList.stream().map(SaleShelfDTO::getCommbarcode).distinct().collect(Collectors.toList());
			for (String commbarcode : commBarcodeList) {
				//获取多比例配置
				queryItemModel.setSaleConfigId(config.getId());
				queryItemModel.setSdType(DERP_ORDER.SDTYPECONFIG_TYPE_2);
				queryItemModel.setCommbarcode(commbarcode);
				List<SdSaleConfigItemModel> multipleConfigItemList = sdSaleConfigItemDao.list(queryItemModel);
				if (multipleConfigItemList != null && multipleConfigItemList.size() > 0) {
					sdConfigItemList.addAll(multipleConfigItemList);
				}
			}
			Map<String, SaleSdOrderModel> orderMap = new HashMap<>();
			Map<String, List<SaleSdOrderItemModel>> orderItemMap = new HashMap<>();
			for (SdSaleConfigItemModel itemConfig : sdConfigItemList) {
				String key = itemConfig.getSaleConfigId()+"_"+itemConfig.getSdTypeId();
				if (!orderMap.containsKey(key)) {
					SaleSdOrderModel sdOrderModel = new SaleSdOrderModel();
					sdOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSSD));
					sdOrderModel.setBuId(shelfModel.getBuId());
					sdOrderModel.setBuName(shelfModel.getBuName());
					sdOrderModel.setMerchantId(shelfModel.getMerchantId());
					sdOrderModel.setMerchantName(shelfModel.getMerchantName());
					sdOrderModel.setCustomerId(shelfModel.getCustomerId());
					sdOrderModel.setCustomerName(shelfModel.getCustomerName());
					sdOrderModel.setCurrency(shelfModel.getCurrency());
					sdOrderModel.setOrderId(shelfModel.getId());
					sdOrderModel.setOrderCode(shelfModel.getCode());
					sdOrderModel.setOwnDate(shelfModel.getShelfDate());
					sdOrderModel.setBusinessId(shelfModel.getSaleOrderId());
					sdOrderModel.setBusinessCode(shelfModel.getSaleOrderCode());
					sdOrderModel.setTotalSdAmount(amountMap.get(key));
					sdOrderModel.setTotalSdNum(numMap.get(key));
					sdOrderModel.setSdTypeId(itemConfig.getSdTypeId());
					sdOrderModel.setSdType(itemConfig.getSdTypeName());
					sdOrderModel.setSdTypeName(itemConfig.getSdSimpleName());
					sdOrderModel.setPoNo(shelfModel.getPoNo());
					sdOrderModel.setCreater(user.getId());
					sdOrderModel.setCreateName(user.getName());
					sdOrderModel.setCreateDate(TimeUtils.getNow());
					sdOrderModel.setState("001");
					sdOrderModel.setOrderSource(DERP_ORDER.SALE_SD_ORDER_ORDERSOURCE_1);
					sdOrderModel.setOrderType(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1);//数据类型 1-上架单 2-销售退入库单

					orderMap.put(key, sdOrderModel);
				}
				for (SaleShelfDTO shelfItem : saleShelfList) {
					//好品量为0，跳过
					if (shelfItem.getShelfNum().intValue() < 1) {
						continue;
					}
					if (DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(itemConfig.getSdType()) && !itemConfig.getCommbarcode().equals(shelfItem.getCommbarcode())) {
						continue;
					}
					SaleOrderItemModel saleOrderItem = saleOrderItemDao.searchById(shelfItem.getSaleItemId());

					/**
					 * 2、若销售SD配置记录中存在单比例配置，则上架单中所有商品均按照对应单比例配置的“SD类型+SD比例”进行生成的销售SD单，
					 * 各商品销售SD类型金额 = 上架好品数量*销售单价*SD比例
					 *
					 * 3、若销售SD配置记录中存在多比例配置，根据上架单中的商品标准条码查询是否存在对应多比例配置的“SD类型+SD比例”，若有则生成销售SD单，
					 * 商品销售SD类型金额 = 上架好品数量*销售单价*SD比例；
					 */
					SaleSdOrderItemModel sdItemModel = new SaleSdOrderItemModel();
					sdItemModel.setGoodsId(shelfItem.getGoodsId());
					sdItemModel.setGoodsNo(shelfItem.getGoodsNo());
					sdItemModel.setGoodsName(shelfItem.getGoodsName());
					sdItemModel.setBarcode(shelfItem.getBarcode());
					sdItemModel.setCommbarcode(shelfItem.getCommbarcode());
					sdItemModel.setNum(shelfItem.getShelfNum());
					sdItemModel.setPrice(saleOrderItem.getPrice());
					sdItemModel.setAmount(saleOrderItem.getPrice().multiply(new BigDecimal(shelfItem.getShelfNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
					sdItemModel.setSdRatio(itemConfig.getProportion().doubleValue());

					// SD金额 = 上架好品数量*销售单价*SD比例
					BigDecimal sdAmount = saleOrderItem.getPrice().multiply(new BigDecimal(shelfItem.getShelfNum()))
							.multiply(itemConfig.getProportion()).setScale(2, BigDecimal.ROUND_HALF_UP);
					BigDecimal sdPrice = sdAmount.divide(new BigDecimal(shelfItem.getShelfNum()), 8, BigDecimal.ROUND_HALF_UP);
					sdItemModel.setSdPrice(sdPrice);
					sdItemModel.setSdAmount(sdAmount);
					sdItemModel.setSaleItemId(saleOrderItem.getId());

					List<SaleSdOrderItemModel> itemList = orderItemMap.get(key);
					if (itemList == null) {
						itemList = new ArrayList<>();
					}
					itemList.add(sdItemModel);
					orderItemMap.put(key, itemList);
				}
			}
			for (String key : orderMap.keySet()) {
				SaleSdOrderModel saleSdOrderModel = orderMap.get(key);
				List<SaleSdOrderItemModel> sdOrderItemList = orderItemMap.get(key);
				if(sdOrderItemList == null || sdOrderItemList.size()< 1){
					continue;
				}
				BigDecimal totalSdAmount = sdOrderItemList.stream().map(SaleSdOrderItemModel::getSdAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
				Integer totalSdNum = sdOrderItemList.stream().mapToInt(SaleSdOrderItemModel::getNum).sum();
				saleSdOrderModel.setTotalSdAmount(totalSdAmount);
				saleSdOrderModel.setTotalSdNum(totalSdNum);
				Long num = saleSdOrderDao.save(saleSdOrderModel);

				for (SaleSdOrderItemModel sdItemModel : sdOrderItemList) {
					sdItemModel.setSaleSdOrderId(num);
					saleSdOrderItemDao.save(sdItemModel);
				}
			}
			shelfCodeList.add(shelfModel.getCode());
		}
		return shelfCodeList.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public List<ShelfDTO> listShelfDTO(ShelfDTO shelfDTO) throws Exception {
		return shelfDao.listDTO(shelfDTO);
	}

	@Override
	public void saveShelfIdepot(String shelfIds, User user) throws Exception {

		List<Long> shelfIdList = StrUtils.parseIds(shelfIds);
		//校验单据
		for(Long shelfId : shelfIdList) {
			// 上架单表头
			ShelfModel shelfModel = shelfDao.searchById(shelfId);
			SaleOrderModel saleOrderModel = saleOrderDao.searchById(shelfModel.getSaleOrderId());
			if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
				throw new RuntimeException("上架单：" + shelfModel.getCode() + " 对应的销售订单红冲状态不为空，无法生成上架入库单");
			}
			//查询是否生成过上架入库单
			SaleShelfIdepotModel saleShelfIdepotModel = new SaleShelfIdepotModel();
			saleShelfIdepotModel.setSaleShelfId(shelfId);
			List<SaleShelfIdepotModel> saleShelfIdepotList = saleShelfIdepotDao.list(saleShelfIdepotModel);
			if(saleShelfIdepotList != null && saleShelfIdepotList.size() > 0) {
				throw new RuntimeException("上架单："+shelfModel.getCode()+"，已存在对应上架入库单，无法重复生成");
			}
			// 上架表体明细
			SaleShelfModel saleShelfModel = new SaleShelfModel();
			saleShelfModel.setShelfId(shelfId);
			List<SaleShelfModel> saleShelIemList = saleShelfDao.list(saleShelfModel);

			//1、判断对应销售订单的入库仓是否为空
			if(saleOrderModel.getInDepotId() == null) {
				throw new RuntimeException("上架单："+shelfModel.getCode()+"，对应的销售订单入库仓库为空，上架入库单生成失败");
			}

			// 2、判断销售类型是否为购销-实销实结 或 购销-整批结算
			if (!DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderModel.getBusinessModel())
					&& !DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderModel.getBusinessModel())) {
				throw new RuntimeException("上架单："+shelfModel.getCode()+"，销售类型不为【购销-实销实结或购销-整批结算】，上架入库单生成失败");
			}

			//3、销售类型为【购销-整批结算】时，判断入库仓库是否为代客管理的仓库
			if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderModel.getBusinessModel())) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("depotId", saleOrderModel.getInDepotId());
				DepotInfoMongo inDepot = depotInfoMongoDao.findOne(params);// 入库仓
				// 入库仓库是否为代客管理仓
				if (!DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_1.equals(inDepot.getIsValetManage())) {
					throw new RuntimeException("上架单："+shelfModel.getCode()+"，销售类型为【购销-整批结算】时入库仓库不为代客管理仓，上架入库单生成失败");
				}
			}
			//4、检查关账日期/月结转日期
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(saleOrderModel.getMerchantId());
			closeAccountsMongo.setBuId(saleOrderModel.getBuId());
			String maxMonth = "";
			// 查询关账日期
			maxMonth = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			if (StringUtils.isNotBlank(maxMonth)) {
				// 获取最大关账月份下一个月1日时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxMonth + "-01 00:00:00"));
				String maxCloseNextMonthDate = nextMonth + "-01 00:00:00";
				// 关账下个月日期大于 入库日期
				if (shelfModel.getShelfDate().getTime() < Timestamp.valueOf(maxCloseNextMonthDate).getTime()) {
					throw new RuntimeException("上架单："+shelfModel.getCode()+"，上架归属月份已关账，上架入库单生成失败");
				}
			}
			//5、判断上架量/损货量是否大于0
			Integer totalShelfNum = saleShelIemList.stream().filter(s -> s.getShelfNum() != null).mapToInt(SaleShelfModel::getShelfNum).sum();
			Integer totalDamagedNum = saleShelIemList.stream().filter(s -> s.getDamagedNum() != null).mapToInt(SaleShelfModel::getDamagedNum).sum();
			if(totalShelfNum <= 0 && totalDamagedNum <= 0) {
				throw new RuntimeException("上架单："+shelfModel.getCode()+"，上架总好品量或总损货量小于0，上架入库单生成失败");
			}
		}
		//开始生成上架入库单
		for(Long shelfId : shelfIdList) {
			// 上架单表头
			ShelfModel shelfModel = shelfDao.searchById(shelfId);
			// 上架表体明细
			SaleShelfModel saleShelfModel = new SaleShelfModel();
			saleShelfModel.setShelfId(shelfId);
			List<SaleShelfModel> saleShelIemList = saleShelfDao.list(saleShelfModel);
			// 查询销售单
			SaleOrderModel saleOrderModel = saleOrderDao.searchById(shelfModel.getSaleOrderId());

			InvetAddOrSubtractRootJson subtractRootJson = saleOrderService.saveShelfIdepot(shelfModel, saleOrderModel, user,saleShelIemList);
			String rootjson = JSONObject.fromObject(subtractRootJson).toString();
			System.out.println("报文:" + rootjson);
			SendResult sendResult = rocketMQProducer.send(rootjson,MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
			System.out.println("发送结果:" + sendResult);

		}

	}

	private List<String> checkBeforeDel(List<Long> shelfIdList, User user) throws Exception {
		List<String> result =  new ArrayList<String>();
		List<Long> saleShelfId =  new ArrayList<Long>();
		//开始生成上架入库单
		for(Long shelfId : shelfIdList) {
			// 上架单表头
			ShelfModel shelfModel = shelfDao.searchById(shelfId);
			// 查询销售单
			SaleOrderModel saleOrderModel = saleOrderDao.searchById(shelfModel.getSaleOrderId());
			if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
				throw new RuntimeException("上架单：" + shelfModel.getCode() + " 对应的销售订单红冲状态不为空，无法删除");
			}

			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(saleOrderModel.getMerchantId());
			closeAccountsMongo.setDepotId(saleOrderModel.getInDepotId());
			closeAccountsMongo.setBuId(saleOrderModel.getBuId());
			String maxMonth = "";
			//1、必须勾选选择上架单进行删除，对上架单单据状态为“已上架” 操作删除时校验上架归属月份是否已关账，若已关账则不予删除，无需校验月结结转状态；
			if(DERP_ORDER.SHELF_STATUS_1.equals(shelfModel.getState())) {
				// 查询关账日期
				maxMonth = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
				if (StringUtils.isNotBlank(maxMonth)) {
					// 获取最大关账月份下一个月1日时间
					String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxMonth + "-01 00:00:00"));
					String maxCloseNextMonthDate = nextMonth + "-01 00:00:00";
					// 关账下个月日期大于 入库日期
					if (shelfModel.getShelfDate().getTime() < Timestamp.valueOf(maxCloseNextMonthDate).getTime()) {
						throw new RuntimeException("上架单："+shelfModel.getCode()+"，上架归属月份已关账，删除失败");
					}
				}
			}
			//2、对上架单单据状态为“已入库” 操作删除时校验上架归属月份是否已结转已关账（已结转仅判断入库仓库的结转状态即可），若已结转或已关账则不予删除；
			if(DERP_ORDER.SHELF_STATUS_2.equals(shelfModel.getState())) {
				// 查询关账日期
				maxMonth = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
				if (StringUtils.isNotBlank(maxMonth)) {
					// 获取最大关账月份下一个月1日时间
					String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxMonth + "-01 00:00:00"));
					String maxCloseNextMonthDate = nextMonth + "-01 00:00:00";
					// 关账下个月日期大于 入库日期
					if (shelfModel.getShelfDate().getTime() < Timestamp.valueOf(maxCloseNextMonthDate).getTime()) {
						throw new RuntimeException("上架单："+shelfModel.getCode()+"，上架归属月份已月结/已关账，删除失败");
					}
				}
				saleShelfId.add(shelfId);
			}
			/**
			 * 3、再校验该上架单是否已生成ToB暂估收入，对于暂估收入状态为“已上架未结算”时可同步触发删除ToB暂估收入指令；若暂估收入状态不为“已上架未结算”时，报错不予删除上架单；
			 * 4、再校验该上架单是否已生成销售SD单和ToB暂估费用，对于暂估费用状态为“已上架未结算”时，可同步触发删除销售SD单、ToB暂估费用指令；
			 * 若暂估收费用状态不为“已上架未结算”时，报错不予删除上架单；
			 */
			TobTemporaryReceiveBillModel  billModel = new TobTemporaryReceiveBillModel();
			billModel.setShelfCode(shelfModel.getCode());
			List<TobTemporaryReceiveBillModel> billList = tobTemporaryReceiveBillDao.list(billModel);

			billList.stream().filter(t -> !DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1.equals(t.getStatus())
	                || !DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1.equals(t.getRebateStatus())).forEach(
	                		t -> {
	                			throw new RuntimeException("上架单号：" + t.getShelfCode() + "的暂估收入状态/暂估费用状态 不为“已上架未核销”");
	        });

		}
		if(saleShelfId.size() > 0) {
			SaleShelfIdepotDTO saleShelfIdepotDTO = new SaleShelfIdepotDTO();
			saleShelfIdepotDTO.setSaleShelfIdList(saleShelfId);
			List<SaleShelfIdepotDTO> saleShelfIdepotList = saleShelfIdepotDao.listDTO(saleShelfIdepotDTO);
			if(saleShelfIdepotList != null && saleShelfIdepotList.size() > 0) {
				result.addAll(saleShelfIdepotList.stream().map(SaleShelfIdepotDTO::getCode).collect(Collectors.toList()));
			}
		}
		return result;
	}

	@Override
	public void delShelfOrder(String shelfIds, User user , String token) throws Exception {
		List<Long> shelfIdList = StrUtils.parseIds(shelfIds);
		//删除前校验，获取需要回滚的单号
		List<String> rollBackCodes = checkBeforeDel(shelfIdList,user);

		//----------删除  start-----------
		List<Long> billIdList =  new ArrayList<Long>();//toB暂估id集合
		List<Long> shelfIdepotIdList =  new ArrayList<Long>();//上架入库单id
		List<Long> saleSdIdList =  new ArrayList<Long>();//销售SD单id
		List<Long> saleOrderIdList =  new ArrayList<Long>();//销售单id
		List<Long> saleOutIdList =  new ArrayList<Long>();//销售出库单id
		List<Long> saleDeclareIdList =  new ArrayList<Long>();//销售预申报单id
		for(Long shelfId : shelfIdList) {
			// 上架单表头
			ShelfModel shelfModel = shelfDao.searchById(shelfId);
			TobTemporaryReceiveBillModel  billModel = new TobTemporaryReceiveBillModel();
			billModel.setShelfCode(shelfModel.getCode());
			List<TobTemporaryReceiveBillModel> billList = tobTemporaryReceiveBillDao.list(billModel);
			//toB暂估id集合
			billIdList.addAll(billList.stream().map(TobTemporaryReceiveBillModel :: getId).collect(Collectors.toList()));

			//上架入库单
			SaleShelfIdepotModel shelfIdepotModel = new SaleShelfIdepotModel();
			shelfIdepotModel.setSaleShelfId(shelfId);
			List<SaleShelfIdepotModel> shelfIdepotList = saleShelfIdepotDao.list(shelfIdepotModel);
			shelfIdepotIdList.addAll(shelfIdepotList.stream().map(SaleShelfIdepotModel::getId).collect(Collectors.toList()));

			//销售sd单
			SaleSdOrderModel saleSdOrderModel = new SaleSdOrderModel();
			saleSdOrderModel.setOrderId(shelfId);
			List<SaleSdOrderModel> saleSdOrderList = saleSdOrderDao.list(saleSdOrderModel);
			saleSdIdList.addAll(saleSdOrderList.stream().map(SaleSdOrderModel::getId).collect(Collectors.toList()));
			//销售单id
			saleOrderIdList.add(shelfModel.getSaleOrderId());
			//销售出库单id
			saleOutIdList.add(shelfModel.getSaleOutDepotId());
			//销售预申报单id
			SaleOutDepotModel saleOutDepot = new SaleOutDepotModel();
			saleOutDepot = saleOutDepotDao.searchById(shelfModel.getSaleOutDepotId());
			if(saleOutDepot.getSaleDeclareOrderId() != null && !saleDeclareIdList.contains(saleOutDepot.getSaleDeclareOrderId())) {

				saleDeclareIdList.add(saleOutDepot.getSaleDeclareOrderId());
			}
		}

		//删除toB暂估收入、费用收入数据
		if(billIdList != null && billIdList.size() > 0) {
			tobTemporaryReceiveBillDao.delete(billIdList);
	        tobTemporaryReceiveBillItemDao.delItemsByBillIds(billIdList);
	        tobTemporaryReceiveBillRebateItemDao.delItemsByBillIds(billIdList);
		}
		//删除上架单、上架单表体、上架入库单、上架入库单表体、销售sd单、销售SD单表体
		shelfDao.delete(shelfIdList);
		saleShelfDao.delItemsByShelfIds(shelfIdList);
		if(shelfIdepotIdList != null && shelfIdepotIdList.size() > 0) {
			saleShelfIdepotDao.delete(shelfIdepotIdList);
			saleShelfIdepotItemDao.delItemByShelfIdepotIds(shelfIdepotIdList);
		}
		if(saleSdIdList != null && saleSdIdList.size() > 0) {
			saleSdOrderDao.delete(saleSdIdList);
			saleSdOrderItemDao.delItemBySaleSdIds(saleSdIdList);
		}
		//----------删除  end-----------

		//----------------回滚单据 start -------------
		if(rollBackCodes != null && rollBackCodes.size() > 0) {
			String rollBackCodesStr = StringUtils.join(rollBackCodes,",");
			String url = ApolloUtil.inventoryWebhost + "/webapi/inventory/inventoryDetails/inventoryRollBack.asyn?token="+token;
//			String url = "http://127.0.0.1:9040/webapi/inventory/inventoryDetails/inventoryRollBack.asyn?token="+token;

			LOGGER.info("请求:调用库存回滚接口url："+ url);
			LOGGER.info("调用库存回滚，参数:"+ rollBackCodesStr);

			String result = HttpClientUtil.doPost(url, rollBackCodesStr, "utf-8");
			LOGGER.info("调用库存回滚接口，响应:"+result);

			// 3、若失败，提示错误原因
			if (StringUtils.isBlank(result)) {
				throw new RuntimeException("调用库存回滚接口返回异常！");
			}
			JSONObject resultJson = JSONObject.fromObject(result);
			String status = resultJson.getString("code");
			if (!MessageEnum.SUCCESS_10000.getCode().equals(status)) {
				if (resultJson.get("message") != null) {
					throw new RuntimeException(resultJson.getString("message"));
				}
			}
		}
		//----------------回滚单据 end -------------


		//----------------修改状态 start -------------
		/**
		 * 更新销售单：
		 * 	累计出库数量 < 销售数量，部分出库
		 *  上架数量为0：
		 * 		累计出库数量=销售数量，已出库
		 *  上架数量大于0：
		 *  	累计出库数量=销售数量，上架数量小于销售数量，部分上架
		 */
		for(Long saleOrderId: saleOrderIdList) {
			String updateState = "";
			// 查询销售单
			SaleOrderModel saleOrderModel = saleOrderDao.searchById(saleOrderId);

			//获取销售出库数量
			SaleOutDepotDTO saleOutDTO = new SaleOutDepotDTO();
			saleOutDTO.setSaleOrderCode(saleOrderModel.getCode());
			List<SaleOutDepotDTO> saleOutDetailList = saleOutDepotDao.queryDTODetailsList(saleOutDTO);
			Integer transferNum = saleOutDetailList.stream().filter(s -> s.getTransferNum() != null).mapToInt(SaleOutDepotDTO::getTransferNum).sum();
			Integer wornNum = saleOutDetailList.stream().filter(s -> s.getWornNum() != null).mapToInt(SaleOutDepotDTO::getWornNum).sum();
			Integer totalOutNum = transferNum + wornNum;//累计销售出库数量

			//若累计出库数量 < 销售数量，部分出库
			if(totalOutNum.intValue() < saleOrderModel.getTotalNum().intValue()) {
				updateState = DERP_ORDER.SALEORDER_STATE_019;//部分出库

			}else {//若累计出库数量 >= 销售数量，判断上架量
				SaleShelfModel saleShelfModel = new SaleShelfModel();
				saleShelfModel.setOrderId(saleOrderId);
				List<SaleShelfModel> saleShelfList = saleShelfDao.list(saleShelfModel);
				Integer shelfNum = saleShelfList.stream().filter(s -> s.getShelfNum() != null).mapToInt(SaleShelfModel::getShelfNum).sum();
				Integer damagedNum = saleShelfList.stream().filter(s -> s.getDamagedNum() != null).mapToInt(SaleShelfModel::getDamagedNum).sum();
				Integer lackNum = saleShelfList.stream().filter(s -> s.getLackNum() != null).mapToInt(SaleShelfModel::getLackNum).sum();
				Integer totalShelfNum = shelfNum + damagedNum + lackNum;// 销售订单累计上架数量
				if(totalShelfNum > 0) {
					updateState = DERP_ORDER.SALEORDER_STATE_031;//部分上架
				}else {
					updateState = DERP_ORDER.SALEORDER_STATE_018;//已出库
				}
			}
			//更新销售状态
			SaleOrderModel updateModel = new SaleOrderModel();
			updateModel.setId(saleOrderId);
			updateModel.setState(updateState);
			updateModel.setModifier(user.getId());
			updateModel.setModifierUsername(user.getName());
			updateModel.setModifyDate(TimeUtils.getNow());
			saleOrderDao.modify(updateModel);

		}
		/**
		 * 更新销售出库单：
		 *  存在上架单：部分上架
		 * 	不存在上架单： 已出库
		 */
		for(Long saleOutId: saleOutIdList) {
			String updateState = "";
			SaleShelfModel saleShelfModel = new SaleShelfModel();
			saleShelfModel.setSaleOutDepotId(saleOutId);
			List<SaleShelfModel> saleShelfList = saleShelfDao.list(saleShelfModel);
			if(saleShelfList != null && saleShelfList.size() > 0) {//存在上架单
				updateState = DERP_ORDER.SALEOUTDEPOT_STATUS_031;//部分上架
			}else {
				updateState = DERP_ORDER.SALEOUTDEPOT_STATUS_018;//已出库
			}
			//更新销售出库单状态
			SaleOutDepotModel updateModel = new SaleOutDepotModel();
			updateModel.setId(saleOutId);
			updateModel.setStatus(updateState);
			updateModel.setModifyDate(TimeUtils.getNow());
			saleOutDepotDao.modify(updateModel);

		}
		/**
		 * 更新销售预申报单：
		 *  存在上架单：部分上架
		 * 	不存在上架单： 已出库
		 */
		for(Long saleDeclareId: saleDeclareIdList) {
			int shelfNum = 0;
			SaleOutDepotModel saleOutDepot = new SaleOutDepotModel();
			saleOutDepot.setSaleDeclareOrderId(saleDeclareId);
			List<SaleOutDepotModel> saleOutList = saleOutDepotDao.list(saleOutDepot);

			if(saleOutList != null && saleOutList.size() > 0) {
				List<Long> saleOutDepotIds = saleOutList.stream().map(SaleOutDepotModel::getId).collect(Collectors.toList());
				SaleShelfDTO saleShelfDTO = new SaleShelfDTO();
				saleShelfDTO.setSaleOutOrderIds(saleOutDepotIds);
				List<SaleShelfDTO> saleShelfList = saleShelfDao.listDTO(saleShelfDTO);
				if(saleShelfList != null && saleShelfList.size() > 0) {
					shelfNum = saleShelfList.size();
				}
			}

			String updateState = "";
			if(shelfNum > 0) {
				updateState = DERP_ORDER.SALEDECLARE_STATUS_005;//部分上架
			}else {
				updateState = DERP_ORDER.SALEDECLARE_STATUS_004;//已出库
			}
			//更新销售预申报单状态
			SaleDeclareOrderModel updateModel = new SaleDeclareOrderModel();
			updateModel.setId(saleDeclareId);
			updateModel.setStatus(updateState);
			updateModel.setModifier(user.getId());
			updateModel.setModifierUsername(user.getName());
			updateModel.setModifyDate(TimeUtils.getNow());
			saleDeclareOrderDao.modify(updateModel);
		}
		//----------------修改状态 end -------------
	}

}
