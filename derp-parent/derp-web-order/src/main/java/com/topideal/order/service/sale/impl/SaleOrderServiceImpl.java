package com.topideal.order.service.sale.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.OperateLogDao;
import com.topideal.dao.common.FileTempDao;
import com.topideal.dao.common.SdSaleConfigDao;
import com.topideal.dao.common.SdSaleConfigItemDao;
import com.topideal.dao.purchase.*;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.CustomsDeclareItemDTO;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.dto.common.SdSaleConfigDTO;
import com.topideal.entity.dto.purchase.FirstCustomsInfoDTO;
import com.topideal.entity.dto.purchase.PurchaseOrderExportDTO;
import com.topideal.entity.dto.sale.*;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.entity.vo.common.SdSaleConfigItemModel;
import com.topideal.entity.vo.purchase.*;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.json.inventory.j07.InventoryWriteOffRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.sale.SaleOrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 销售订单service实现类
 */
@Service
public class SaleOrderServiceImpl implements SaleOrderService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(SaleOrderServiceImpl.class);
	// 销售订单表头
	@Autowired
	private SaleOrderDao saleOrderDao;
	// 销售订单表体
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	// 采购订单表头
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	// 销售出库表头
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	// 销售出库表体
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	// 销售出库分析表
	@Autowired
	private SaleAnalysisDao saleAnalysisDao;
	// 仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 供应商
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	private CustomerMerchantRelMongoDao customerMerchantRelMongoDao;
	// lbx来源
	@Autowired
	private LbxNoMongoDao lbxNoMongoDao;
	// 商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private BrandMongoDao brandMongoDao;
	@Autowired
	private SaleShelfDao saleShelfDao;
	// 采销执行Mongo
	@Autowired
	private PurchaseCommissionMongoDao purchaseCommissionMongoDao;
	// 采购入库单
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;
	// 采购入库单表体
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;
	// 勾稽表
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;
	// 批次入库单
	@Autowired
	private PurchaseWarehouseBatchDao purchaseWarehouseBatchDao;
	// 采购订单表体
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	// 销售单_po关联表
	@Autowired
	private SalePoRelDao salePoRelDao;
	// 财务经销存关账mongdb
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	// 仓库公司关联表 mongo
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	// 上架入库单表头
	@Autowired
	private SaleShelfIdepotDao saleShelfIdepotDao;
	// 上架入库单表体
	@Autowired
	private SaleShelfIdepotItemDao saleShelfIdepotItemDao;
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	// 公司仓库事业部关联表
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	// 预售单表头
	@Autowired
	private PreSaleOrderDao preSaleOrderDao;
	// 预售单表体
	@Autowired
	private PreSaleOrderItemDao preSaleOrderItemDao;
	// 预售勾稽
	@Autowired
	private PreSaleOrderCorrelationDao preSaleOrderCorrelationDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private PlatformPurchaseOrderDao platformPurchaseOrderDao;
	// 商家
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;
	@Autowired
	private ShelfDao shelfDao;
	@Autowired
	private UnitMongoDao unitMongoDao;
	@Autowired
	private RMQProducer rocketMQProducer;// MQ
	@Autowired
	private CommbarcodeMongoDao commbarcodeMongoDao;
	@Autowired
	private CountryOriginMongoDao countryOriginMongoDao;
	@Autowired
	private CustomerSalePriceMongoDao customerSalePriceMongoDao;
	@Autowired
	DepotCustomsRelMongoDao depotCustomsRelMongoDao;
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao;
	@Autowired
	private FileTempDao fileTempDao;
	@Autowired
	private SaleFinancingOrderDao financingOrderDao;
	@Autowired
	private SaleFinancingOrderItemDao financingOrderItemDao;
	@Autowired
	private SdSaleConfigDao sdSaleConfigDao;
	@Autowired
	private SdSaleConfigItemDao sdSaleConfigItemDao;
	@Autowired
	private SaleSdOrderDao saleSdOrderDao;
	@Autowired
	private SaleSdOrderItemDao saleSdOrderItemDao;
	@Autowired
	private SaleDeclareOrderDao saleDeclareOrderDao ;
	@Autowired
	private SaleCreditOrderDao salCreditOrderDao ;
	@Autowired
	private MerchandiseExternalWarehouseMongoDao merchandiseExternalWarehouseMongoDao ;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private SupplierMerchandisePriceMongoDao supplierMerchandisePriceMongoDao ;
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao ;
	@Autowired
	private OperateLogDao operateLogDao;
	@Autowired
	private SaleDeclareOrderItemDao saleDeclareOrderItemDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;

	/**
	 * 列表（分页）
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SaleOrderDTO listSaleOrderByPage(SaleOrderDTO dto, User user) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		// 根据上架时间检索
		if (StringUtils.isNotBlank(dto.getShelfDateStartDate()) || StringUtils.isNotBlank(dto.getShelfDateStartDate())) {
			ShelfDTO shelfDto = new ShelfDTO();
			if (StringUtils.isNotBlank(dto.getShelfDateStartDate())) {
				shelfDto.setShelfStartDate(dto.getShelfDateStartDate());
			}
			if (StringUtils.isNotBlank(dto.getShelfDateEndDate())) {
				shelfDto.setShelfEndDate(dto.getShelfDateEndDate());
			}
			List<ShelfDTO> shelfList = shelfDao.listDTO(shelfDto);
			if (shelfList != null && shelfList.size() > 0) {
				List<Long> saleIds = new ArrayList<Long>();
				for (ShelfDTO shelf : shelfList) {
					saleIds.add(shelf.getSaleOrderId());
				}
				if (saleIds.size() > 0) {
					dto.setIds(StringUtils.join(saleIds, ","));
				}
			} else {// 上架时间检索为空时，直接返回空
				SaleOrderDTO saleDto = new SaleOrderDTO();
				saleDto.setList(new ArrayList<SaleOrderDTO>());
				saleDto.setTotal(0);
				return saleDto;
			}
		}
		if (dto.getPoNo().indexOf(",") > 0) {
			List<String> poNos = new ArrayList<String>();
			for (String poNo : dto.getPoNo().split(",")) {
				if (StringUtils.isNotBlank(poNo)) {
					poNos.add(poNo);
				}
			}
			if (!poNos.isEmpty()) {
				dto.setPoNos(poNos);
				dto.setPoNo(null);
			}
		}
		SaleOrderDTO list = saleOrderDao.queryDTOListByPage(dto);
		if (list.getList().size() > 0) {
			List<SaleOrderDTO> saleOrderList = list.getList();
			for (SaleOrderDTO saleOrderDto : saleOrderList) {
				Map<String, Object> depotParam = new HashMap<>();
				depotParam.put("depotId", saleOrderDto.getOutDepotId());
				DepotInfoMongo outDepotInfo = depotInfoMongoDao.findOne(depotParam);
				depotParam.put("merchantId", saleOrderDto.getMerchantId());
				depotParam.put("depotId", saleOrderDto.getOutDepotId());
				DepotMerchantRelMongo outDepot = depotMerchantRelMongoDao.findOne(depotParam);
				if (outDepot != null) {
					saleOrderDto.setOutDepotIsInOutInstruction(outDepot.getIsInOutInstruction());
					saleOrderDto.setOutDepotBatchValidation(outDepotInfo.getBatchValidation());
					saleOrderDto.setOutDepotType(outDepotInfo.getType());
				}
				// 查询上架月份是否已关账
				Map<String, String> result = checkExistFinanceClose(user, saleOrderDto.getId());
				if (result.get("code").equals("00")) {
					saleOrderDto.setHasFinanceClose("0");
				} else {
					saleOrderDto.setHasFinanceClose("1");
				}
				SaleCreditOrderModel creditModel = new SaleCreditOrderModel();
				creditModel.setSaleOrderId(saleOrderDto.getId());

				List<SaleCreditOrderModel> creditList = salCreditOrderDao.list(creditModel);
				if(creditList != null && creditList.size() > 0) {
					saleOrderDto.setSaleCreditCode(creditList.get(0).getCode());
				}
				//是否上架 ['1', '3', '4'].includes(row.businessModel) && ['031', '018', '019'].includes(row.state) && row.isGenerateDeclare === '0'
				boolean isShelf = false;//销售单是否可以上架

				boolean notShelf = false;//出库单是否全部上架
				SaleOutDepotModel outModel = new SaleOutDepotModel();
				outModel.setSaleOrderId(saleOrderDto.getId());
				List<SaleOutDepotModel> outList = saleOutDepotDao.list(outModel);
				for(SaleOutDepotModel outDepotModel : outList) {
					if(!DERP_ORDER.SALEOUTDEPOT_STATUS_026.equals(outDepotModel.getStatus())) {//出库单状态不为【已上架】，说明未全部上架
						notShelf = true;
						break;
					}
				}
				//销售单可上架条件： 业务类型为：整批结算，实销实结，采销执行；且状态为：已出库，部分上架，部分出库；且存在出库单未上架
				if(!DERP_ORDER.SALEORDER_BUSINESSMODEL_2.equals(saleOrderDto.getBusinessModel()) &&
					(DERP_ORDER.SALEORDER_STATE_031.equals(saleOrderDto.getState()) ||
					DERP_ORDER.SALEORDER_STATE_018.equals(saleOrderDto.getState())||
					DERP_ORDER.SALEORDER_STATE_019.equals(saleOrderDto.getState())) &&
					DERP_ORDER.SALEDECLARE_ISGENERATE_0.equals(saleOrderDto.getIsGenerateDeclare()) && notShelf) {
					isShelf = true;
				}
				saleOrderDto.setIsShelf(isShelf);
				//统计销售单总上架量、总上架金额， 部分出库，部分上架，已上架单据才需要统计
				if(DERP_ORDER.SALEORDER_STATE_031.equals(saleOrderDto.getState()) || DERP_ORDER.SALEORDER_STATE_026.equals(saleOrderDto.getState())
						|| DERP_ORDER.SALEORDER_STATE_019.equals(saleOrderDto.getState())) {
					Map<String,Object> shelfMap = saleOrderItemDao.getShelfNumAndAmountByOrderId(saleOrderDto.getId());
					BigDecimal shelfNum = (BigDecimal) shelfMap.get("shelfNum");
					BigDecimal shelfAmount = (BigDecimal) shelfMap.get("shelfAmount");
					saleOrderDto.setShelfNum(shelfNum.intValue());
					saleOrderDto.setShelfAmount(shelfAmount.setScale(2 ,BigDecimal.ROUND_HALF_UP));
				}else {
					saleOrderDto.setShelfNum(0);
					saleOrderDto.setShelfAmount(BigDecimal.ZERO);
				}
			}
		}

		return list;
	}

	/**
	 * 根据id获取详情
	 *
	 * @param id
	 * @return
	 */
	@Override
	public SaleOrderDTO searchDetail(Long id) throws SQLException {
		SaleOrderDTO dto = new SaleOrderDTO();
		dto.setId(id);
		return saleOrderDao.searchDTOById(id);
	}

	/**
	 * 修改销售订单
	 */
	@Override
	public String modifySaleOrder(SaleOrderDTO dto, User user, String rejectReason) throws Exception {
		SaleOrderModel saleOrderModel =  new SaleOrderModel();
		// 审核结果 0-通过，1-驳回， 驳回时不进行审核操作
		String auditResult = dto.getAuditResult();// 审核结果
		// 审核时，审核结果不能为空
		if ("2".equals(auditResult) && StringUtils.isBlank(auditResult)) {
			throw new RuntimeException("请选择审核结果");
		}
		if ("1".equals(auditResult)) {
			if(StringUtils.isBlank(rejectReason)){
				throw new RuntimeException("驳回原因不能为空");
			}
			SaleOrderModel queryModel = saleOrderDao.searchById(dto.getId());
			BeanUtils.copyProperties(dto, saleOrderModel);
			saleOrderModel.setCode(queryModel.getCode());
			saleOrderModel.setState(DERP_ORDER.SALEORDER_STATE_008);// 待提交
			saleOrderModel.setAuditDate(TimeUtils.getNow());
			saleOrderModel.setAuditor(user.getId());
			saleOrderModel.setAuditName(user.getName());
			saleOrderModel.setModifyDate(TimeUtils.getNow());
			saleOrderModel.setModifier(user.getId());
			saleOrderModel.setModifierUsername(user.getName());
			saleOrderDao.modify(saleOrderModel);

			// 审核结果选择驳回并触发邮件提醒提交人
			ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
			mailDTO.setBusinessModuleType("3");
			mailDTO.setTypeName("销售");
			mailDTO.setType("2");// 1：提交 2：审核 3：上架 4：核销 5：开票 6：作废审核 7：盖章发票 8：审核完毕 9：作废完成 10：金额修改 11：金额确认
			mailDTO.setMerchantId(saleOrderModel.getMerchantId());
			mailDTO.setMerchantName(saleOrderModel.getMerchantName());
			mailDTO.setBuId(saleOrderModel.getBuId());
			mailDTO.setBuName(saleOrderModel.getBuName());
			mailDTO.setSupplier(saleOrderModel.getCustomerName());
			mailDTO.setOrderCode(saleOrderModel.getCode());
			mailDTO.setPoNum(saleOrderModel.getPoNo());
			mailDTO.setSubmitId(Arrays.asList(String.valueOf(saleOrderModel.getSubmiter())));
			mailDTO.setAuditorId(user.getId());
			mailDTO.setReviewerId(saleOrderModel.getAmountConfirmer());
			mailDTO.setModifyId(saleOrderModel.getAdjuster());
			mailDTO.setUserName(user.getName());
			mailDTO.setStatus("已驳回");

			List<String> userIds = new ArrayList<String>();
			userIds.add(String.valueOf(user.getId()));
			userIds.add(String.valueOf(saleOrderModel.getSubmiter()));
			mailDTO.setUserId(userIds);
			try {

				rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(),
						MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());
			} catch (Exception e) {
				LOGGER.error(
						"----------------------销售单[" + saleOrderModel.getCode() + "]审核驳回发送邮件失败----------------------");
			}
		}else{
			saleOrderModel = saveSaleOrder(dto, user,"2");//0-保存 1-提交 2-审核
			// 平台采购订单转销售订单（平台采购订单保存销售订单号）
			String platformPurchaseIds = dto.getPlatformPurchaseIds();// 平台采购订单号

			if (StringUtils.isNotBlank(platformPurchaseIds) && dto.getId() == null) {
				String[] platformPurchaseIdStr = platformPurchaseIds.split(",");
				for (String platformPurchaseId : platformPurchaseIdStr) {
					PlatformPurchaseOrderModel platformPurchaseOrder = platformPurchaseOrderDao
							.searchById(Long.parseLong(platformPurchaseId));
					String saleCode = "";
					if (platformPurchaseOrder != null)
						saleCode = platformPurchaseOrder.getSaleCode();
					PlatformPurchaseOrderModel updateModel = new PlatformPurchaseOrderModel();
					updateModel.setId(Long.parseLong(platformPurchaseId));
					updateModel.setResaleDate(TimeUtils.getNow());
					updateModel.setResaleName(user.getName());
					updateModel.setResaler(user.getId());
					updateModel.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_3);// 3:已转销售
					if (StringUtils.isNotBlank(saleCode)) {
						saleCode = saleCode + "," + saleOrderModel.getCode();
					} else {
						saleCode = saleOrderModel.getCode();
					}
					updateModel.setSaleCode(saleCode);// 销售单号
					platformPurchaseOrderDao.modify(updateModel);
				}
			}

			if ("0".equals(auditResult)) {
				// 获取出仓仓库信息
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("depotId", dto.getOutDepotId());
				DepotInfoMongo outDepot = depotInfoMongoDao.findOne(params1);
				Map<String, Object> msgMap = auditSaleOrder(saleOrderModel, outDepot, user.getId(), user.getName(),
						user.getMerchantId(), user.getTopidealCode(), user.getMerchantName());
				String errorMsg = (String) msgMap.get("msg");
				if (StringUtils.isNotBlank(errorMsg)) { // 如果有错误信息
					return errorMsg;
				}
			}
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrderModel.getCode(), "审核", null, "1".equals(auditResult)?"审核驳回，驳回原因："+rejectReason:"审核通过");
		return "审核成功&" + saleOrderModel.getId();
	}

	/**
	 * 根据表头ID获取表体(包括商品信息)
	 *
	 * @param id
	 * @return
	 */
	@Override
	public List<SaleOrderItemDTO> listItemByOrderId(Long id) throws SQLException {
		SaleOrderItemDTO saleOrderItem = new SaleOrderItemDTO();
		saleOrderItem.setOrderId(id);
		List<SaleOrderItemDTO> itemList = saleOrderItemDao.listSaleOrderItemDTO(saleOrderItem);
		for(SaleOrderItemDTO itemDTO : itemList){
			SaleShelfModel saleShelfModel = new SaleShelfModel();
			saleShelfModel.setOrderId(id);
			saleShelfModel.setSaleItemId(itemDTO.getId());
			List<SaleShelfModel> saleShelfList = saleShelfDao.list(saleShelfModel);
			Integer shelfNum = saleShelfList.stream().map(SaleShelfModel :: getShelfNum).reduce(0, Integer::sum);
			//上架总金额=上架好品量*销售单价
			BigDecimal shelfAmount = BigDecimal.ZERO;
			if(itemDTO.getPrice() != null) {
				shelfAmount = new BigDecimal(shelfNum).multiply(itemDTO.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			itemDTO.setShelfNum(shelfNum);
			itemDTO.setShelfAmount(shelfAmount);

		}
		return itemList;
	}

	/**
	 * 删除销售订单
	 *
	 * @param ids
	 * @return
	 * @throws RuntimeException
	 */
	@Override
	public boolean delSaleOrder(List ids ,User user) throws SQLException, RuntimeException {
		boolean flag = false;
		// 判断是否有已审核的订单
		for (Object id : ids) {
			// 获取销售订单信息
			SaleOrderModel saleOrder = saleOrderDao.searchById(Long.parseLong(id.toString()));
			// 单据状态不是待提交，
			if (saleOrder.getState() != null && !saleOrder.getState().equals(DERP_ORDER.SALEORDER_STATE_008)) {
				flag = true;
				break;
			}
		}
		if (flag) {
			throw new RuntimeException("删除失败，存在状态不为待提交的订单");
		}
		int num = saleOrderDao.delSaleOrder(ids);
		for (Object id : ids) {
			// 获取销售订单信息
			SaleOrderModel saleOrder = saleOrderDao.searchById(Long.parseLong(id.toString()));
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrder.getCode(), "删除", null, null);

			PlatformPurchaseOrderDTO platformPurchaseOrderDTO = new PlatformPurchaseOrderDTO();
			platformPurchaseOrderDTO.setSaleCode(saleOrder.getCode());
			List<PlatformPurchaseOrderDTO> platformPurchaseOrderList = platformPurchaseOrderDao.listDTO(platformPurchaseOrderDTO);
			for(PlatformPurchaseOrderDTO tempPlatform : platformPurchaseOrderList){
				String saleCode = tempPlatform.getSaleCode();
				Set<String> saleCodeSet = new HashSet<>();
				if(saleCode.indexOf(",") > -1){
					String[] saleCodeArr = saleCode.split(",");
					for(String sc : saleCodeArr){
						if(!saleOrder.getCode().equals(sc)){
							saleCodeSet.add(sc);
						}
					}
					saleCode = StringUtils.join(saleCodeSet,",");
					tempPlatform.setSaleCode(saleCode);
				}else{
					tempPlatform.setSaleCode(null);
					tempPlatform.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_2);
				}
				PlatformPurchaseOrderModel updatePlatformModel = new PlatformPurchaseOrderModel();
				BeanUtils.copyProperties(tempPlatform, updatePlatformModel);
				platformPurchaseOrderDao.modifyWherSubmit(updatePlatformModel);

			}
		}


		return num >= 1;
	}

	/**
	 * 审核订单
	 *
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> auditSaleOrder(SaleOrderModel saleOrderModel, DepotInfoMongo depot, Long userId,
			String name, Long merchantId, String topidealCode, String merchantName) throws Exception {
		int num = 0;
		StringBuffer sb = new StringBuffer();
		String msg = null;

		// 对订单做校验 ， 如不符合要求抛出异常
		if (depot == null) {
			throw new RuntimeException(saleOrderModel.getCode() + " 审核失败，出仓仓库不存在");
		}
		// 执行审批:业务主体为采销执行，执行采销执行审批，否则执行正常审批
		String temp = "";
		temp = auditSales(saleOrderModel, userId, name, merchantId, topidealCode, merchantName);

		if (StringUtils.isNotBlank(temp)) {
			sb.append(temp);
		} else {
			num++;
		}
		msg = sb.toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", num);
		map.put("msg", msg);
		return map;
	}

	/**
	 * 中转仓出库
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean confirmSaleOrderStockOut(List list, Long userId, String name, String topidealCode, Long merchantId,
			String outDepotDateStr) throws Exception {
		for (Object id : list) {
			// 获取销售订单信息
			SaleOrderModel saleOrder = saleOrderDao.searchById(Long.parseLong(id.toString()));
			if (saleOrder == null) {
				throw new RuntimeException("出库失败，销售订单不存在");
			}

			// 获取仓库信息
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", saleOrder.getOutDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params1);
			if (depot == null) {
				throw new RuntimeException("出库失败，仓库不存在");
			}
			// 不是中转仓
			if (!DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
				throw new RuntimeException("出库失败，仓库不是中转仓");
			}
			// 单据状态不是已审核
			if (!DERP_ORDER.SALEORDER_STATE_003.equals(saleOrder.getState()) && !DERP_ORDER.SALEORDER_STATE_019.equals(saleOrder.getState())) {
				throw new RuntimeException("出库失败，单据状态不为：已审核/部分出库");
			}
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			String sDate = sdf1.format(new Date());
			Date nowDate = sdf1.parse(sDate);
			Date outDepotD = sdf1.parse(outDepotDateStr);
			// 出库日期必须不能大于当前日期
			if (outDepotD.compareTo(nowDate) == 1) {
				throw new RuntimeException("出库日期不能大于当前日期");
			}
			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(saleOrder.getMerchantId());
			closeAccountsMongo.setDepotId(saleOrder.getOutDepotId());
			closeAccountsMongo.setBuId(saleOrder.getBuId());
			String maxdate = "";
			if (closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {// 查询关账日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,
						DERP.CLOSEACCOUNTFLAG1);
			} else if (closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null) {// 查询月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,
						DERP.CLOSEACCOUNTFLAG2);
			} else if (closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null) {// 获取最大的关账日/月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,
						DERP.CLOSEACCOUNTFLAG3);
			}
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 中转仓出库时间(中转仓出库时间不可小于关账日期/结转日期)
				if (Timestamp.valueOf(outDepotDateStr).getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					throw new RuntimeException("中转仓出库时间必须大于关账日期/月结日期");
				}
			}
			// 获取商品信息
			SaleOrderItemModel saleOrderItem = new SaleOrderItemModel();
			saleOrderItem.setOrderId(saleOrder.getId());
			List<SaleOrderItemModel> saleOrderItemList = saleOrderItemDao.list(saleOrderItem);
			//获取销售出库数量
			Map<String,Integer> outNumMap = new HashMap<String, Integer>();
			SaleOutDepotDTO saleOutDTO = new SaleOutDepotDTO();
			saleOutDTO.setSaleOrderCode(saleOrder.getCode());
			List<SaleOutDepotDTO> saleOutList = saleOutDepotDao.queryDTODetailsList(saleOutDTO);
			for(SaleOutDepotDTO queryOutDTO : saleOutList) {
				Integer transferNum = queryOutDTO.getTransferNum()==null ? 0:queryOutDTO.getTransferNum();// 好品数量
				Integer wornNum = queryOutDTO.getWornNum()==null ? 0:queryOutDTO.getWornNum();	// 坏品数量
				Integer totalNum = (outNumMap.get(queryOutDTO.getGoodsNo()) == null ? 0 :outNumMap.get(queryOutDTO.getGoodsNo()))+transferNum + wornNum;

				outNumMap.put(queryOutDTO.getGoodsNo(), totalNum);//记录已出库数量
			}
			// 生成销售出库单
			SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
			saleOutDepotModel.setCreateDate(TimeUtils.getNow());
			saleOutDepotModel.setCreater(userId);
			saleOutDepotModel.setCustomerId(saleOrder.getCustomerId());
			saleOutDepotModel.setCustomerName(saleOrder.getCustomerName());
			saleOutDepotModel.setDeliverDate(TimeUtils.parseFullTime(outDepotDateStr)); // 将中转仓出库时间填入相应销售出库单的发货时间字段中
			saleOutDepotModel.setMerchantId(saleOrder.getMerchantId());
			saleOutDepotModel.setMerchantName(saleOrder.getMerchantName());
			saleOutDepotModel.setSaleOrderCode(saleOrder.getCode());
			saleOutDepotModel.setOutDepotId(saleOrder.getOutDepotId());
			saleOutDepotModel.setOutDepotName(saleOrder.getOutDepotName());
			saleOutDepotModel.setPoNo(saleOrder.getPoNo());
			saleOutDepotModel.setSaleOrderId(saleOrder.getId());
			saleOutDepotModel.setSaleType(saleOrder.getBusinessModel());
			saleOutDepotModel.setStatus(DERP_ORDER.SALEORDER_STATE_027);// 出库中
			saleOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSCK));
			// 事业部
			saleOutDepotModel.setBuId(saleOrder.getBuId());
			saleOutDepotModel.setBuName(saleOrder.getBuName());
			saleOutDepotModel.setCurrency(saleOrder.getCurrency());
			saleOutDepotDao.save(saleOutDepotModel);
			for (SaleOrderItemModel saleItem : saleOrderItemList) {
				SaleOutDepotItemModel item = new SaleOutDepotItemModel();
				item.setCreateDate(TimeUtils.getNow());
				item.setCreater(userId);
				item.setGoodsCode(saleItem.getGoodsCode());
				item.setGoodsId(saleItem.getGoodsId());
				item.setGoodsName(saleItem.getGoodsName());
				item.setGoodsNo(saleItem.getGoodsNo());
				item.setCommbarcode(saleItem.getCommbarcode()); // 标准条码
				item.setBarcode(saleItem.getBarcode());

				Integer outNum = outNumMap.get(item.getGoodsNo()) == null ? 0 : outNumMap.get(item.getGoodsNo());
				item.setTransferNum(saleItem.getNum()-outNum);// 出库量
				item.setSaleNum(saleItem.getNum());
				item.setSaleOutDepotId(saleOutDepotModel.getId());

				saleOutDepotItemDao.save(item);

			}
			//修改销售订单状态
			SaleOrderModel updateSaleOrderModel = new SaleOrderModel();
			updateSaleOrderModel.setId(Long.parseLong(id.toString()));
			updateSaleOrderModel.setState(DERP_ORDER.SALEORDER_STATE_027);
			saleOrderDao.modify(updateSaleOrderModel);

			// 扣减销售出库库存量
			String saleOutDepotCode = saleOutDepotModel.getCode();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now1 = sdf.format(new Date());
			InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
			invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTopic());
			invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTags());
			Map<String, Object> customParam = new HashMap<String, Object>();
			customParam.put("saleId", saleOrder.getId());
			invetAddOrSubtractRootJson.setCustomParam(customParam);
			invetAddOrSubtractRootJson.setMerchantId(saleOrder.getMerchantId().toString());
			invetAddOrSubtractRootJson.setMerchantName(saleOrder.getMerchantName());
			invetAddOrSubtractRootJson.setTopidealCode(topidealCode);
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", saleOrder.getOutDepotId());// 根据仓库id
			DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
			invetAddOrSubtractRootJson.setDepotId(saleOrder.getOutDepotId().toString());
			invetAddOrSubtractRootJson.setDepotName(saleOrder.getOutDepotName());
			invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
			invetAddOrSubtractRootJson.setDepotType(mongo.getType());
			invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
			invetAddOrSubtractRootJson.setOrderNo(saleOutDepotCode);
			invetAddOrSubtractRootJson.setBusinessNo(saleOrder.getCode());
			invetAddOrSubtractRootJson.setBuId(String.valueOf(saleOrder.getBuId()));// 事业部
			invetAddOrSubtractRootJson.setBuName(saleOrder.getBuName());
			// invetAddOrSubtractRootJson.setSource(SourceStatusEnum.XSCK.getKey());
			invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_003);
			// 中转仓出库时根据销售类型设置出库单类型是购销、采销执行，调库存扣减接口时单据类型传采销执行出
			if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrder.getBusinessModel())
					|| DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrder.getBusinessModel())) {
				// invetAddOrSubtractRootJson.setSourceType(InventoryStatusEnum.XSCK.getKey());
				invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
			} else if (DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOrder.getBusinessModel())) {
				// invetAddOrSubtractRootJson.setSourceType(InventoryStatusEnum.CGZX.getKey());
				invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0021);
			}

			invetAddOrSubtractRootJson.setSourceDate(now1);// 单据时间
			invetAddOrSubtractRootJson.setDivergenceDate(outDepotDateStr); // 出入时间
			// 获取当前年月
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
			Date outDepotDate = TimeUtils.StringToDate(outDepotDateStr);
			String outDepotDateStr2 = sdf2.format(outDepotDate);
			invetAddOrSubtractRootJson.setOwnMonth(outDepotDateStr2);// 归属月份
			List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
			for (SaleOrderItemModel item : saleOrderItemList) {
				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

				invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
				invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
				invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
				invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
				invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());


				invetAddOrSubtractGoodsListJson.setType("0");
				invetAddOrSubtractGoodsListJson.setNum(item.getNum());
				invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
				invetAddOrSubtractGoodsListJson.setStockLocationTypeId(saleOrder.getStockLocationTypeId() == null ? "" : saleOrder.getStockLocationTypeId().toString());
				invetAddOrSubtractGoodsListJson.setStockLocationTypeName(saleOrder.getStockLocationTypeName());
				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
			}
			try {
				invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
				rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(),
						MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
						MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
			} catch (Exception e) {
				LOGGER.error("----------------------销售出库单[" + saleOutDepotCode + "]扣减库存失败----------------------");
			}
			User user = new User();
			user.setId(userId);
			user.setName(name);
			user.setTopidealCode(topidealCode);
			user.setMerchantId(merchantId);
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrder.getCode(), "中转仓出库", null, null);
		}
		return true;
	}

	@Override
	public boolean confirmEndStockOut(List list, Long userId, String name) throws SQLException, RuntimeException {
		int num = 0;
		for (Object id : list) {
			String saleStatus = "";
			// 获取销售订单信息
			SaleOrderModel saleOrder = saleOrderDao.searchById(Long.parseLong(id.toString()));
			if(StringUtils.isNotBlank(saleOrder.getWriteOffStatus())){
				throw new RuntimeException("销售订单红冲状态不为空");
			}
			/**
			 * 手动触发完结出库的逻辑为：订单单据状态必须为部分出库，可触发手动完结；
			 */
			if (saleOrder == null || !DERP_ORDER.SALEORDER_STATE_019.equals(saleOrder.getState())) {
				throw new RuntimeException("完结失败，单据状态必须为部分出库");
			} else {
				/**
				 * 1、没有上架单，已出库；
				 * 2、上架数量！=出库数量，部分上架；
				 * 3、上架数量=出库数量，已上架
				 */
				//获取销售订单上架数量
				SaleShelfModel shelfModel = new SaleShelfModel();
				shelfModel.setOrderId(saleOrder.getId());
				List<SaleShelfModel> saleShelfList = saleShelfDao.list(shelfModel);
				Integer totalShelfNum = 0;// 上架数量
				for(SaleShelfModel saleshelfModel : saleShelfList) {
					Integer transferNum = saleshelfModel.getShelfNum() == null ? 0: saleshelfModel.getShelfNum();// 好品数量
					Integer wornNum = saleshelfModel.getDamagedNum() == null ? 0:saleshelfModel.getDamagedNum();	// 坏品数量
					Integer lackNum = saleshelfModel.getLackNum() == null ? 0 :saleshelfModel.getLackNum();	// 缺货数量
					totalShelfNum = totalShelfNum + transferNum + wornNum + lackNum;
				}
				//获取销售出库数量
				Integer totalOutNum = 0;//总出库数量
				SaleOutDepotDTO saleOutDTO = new SaleOutDepotDTO();
				saleOutDTO.setSaleOrderCode(saleOrder.getCode());
				List<SaleOutDepotDTO> saleOutList = saleOutDepotDao.queryDTODetailsList(saleOutDTO);
				for(SaleOutDepotDTO queryOutDTO : saleOutList) {
					Integer transferNum = queryOutDTO.getTransferNum()==null ? 0:queryOutDTO.getTransferNum();// 好品数量
					Integer wornNum = queryOutDTO.getWornNum()==null ? 0:queryOutDTO.getWornNum();	// 坏品数量
					totalOutNum = totalOutNum + transferNum + wornNum;
				}

				if(totalShelfNum.equals(0)) {
					saleStatus = DERP_ORDER.SALEORDER_STATE_018;//已出库
				}else if(totalShelfNum.equals(totalOutNum)){
					saleStatus = DERP_ORDER.SALEORDER_STATE_026;//已上架
				}else {
					saleStatus = DERP_ORDER.SALEORDER_STATE_031;//部分上架
				}

			}
			// 修改单据状态
			saleOrder.setState(saleStatus);
			saleOrder.setEndDate(TimeUtils.getNow()); // 设置已完结时间
			num += saleOrderDao.modify(saleOrder);
			// 修改差异分析表状态
			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
			saleAnalysisModel.setOrderId(saleOrder.getId());
			List<SaleAnalysisModel> saleAnalysisList = saleAnalysisDao.list(saleAnalysisModel);
			for (SaleAnalysisModel saleAnalysis : saleAnalysisList) {
				saleAnalysis.setIsEnd("1");
				saleAnalysis.setEndDate(TimeUtils.getNow());
				saleAnalysisDao.modify(saleAnalysis);
			}
			// 获取仓库信息
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", saleOrder.getOutDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params1);
			// 销售类型不为代销且出库仓不为中转仓，推库存解冻
			if (!DERP_ORDER.SALEORDER_BUSINESSMODEL_2.equals(saleOrder.getBusinessModel())
					&& !DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
				// 释放并减少冻结量
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String now = sdf.format(new Date());
				InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
				inventoryFreezeRootJson.setMerchantId(saleOrder.getMerchantId().toString());
				inventoryFreezeRootJson.setMerchantName(saleOrder.getMerchantName());
				inventoryFreezeRootJson.setDepotId(saleOrder.getOutDepotId().toString());
				inventoryFreezeRootJson.setDepotName(saleOrder.getOutDepotName());
				inventoryFreezeRootJson.setOrderNo(saleOrder.getCode());
				inventoryFreezeRootJson.setBusinessNo(saleOrder.getCode());
				// inventoryFreezeRootJson.setSource(SourceStatusEnum.XSDD.getKey());
				inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
				// inventoryFreezeRootJson.setSourceType(InventoryStatusEnum.XSDD.getKey());
				inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_002);
				inventoryFreezeRootJson.setSourceDate(now);
				inventoryFreezeRootJson.setOperateType("1");
				List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
				// 获取销售表体（商品信息）
				SaleOrderItemModel saleOrderItem = new SaleOrderItemModel();
				saleOrderItem.setOrderId(saleOrder.getId());
				List<SaleOrderItemModel> itemList = saleOrderItemDao.list(saleOrderItem);
				for (SaleOrderItemModel item : itemList) {
					InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();

					inventoryFreezeGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
					inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());
					inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());


					inventoryFreezeGoodsListJson.setDivergenceDate(now);
					/*
					 * 释放库存逻辑： A、当前销售订单商品数量>累计销售出库数量+累计退货数量，释放并减少冻结量， 释放冻结量=销售订单数量—累计销售出库数量-累计退货数量。
					 * B、当前销售订单商品数量<=累计销售出库数量+累计退货数量，释放冻结量=0，不插入冻结表。(不用调用释放接口)
					 */
					// 获取销售订单的商品的累计出库数量
					Map<String, Object> paramMap = new HashMap<>();
					paramMap.put("saleItemId",item.getId());
					Integer totalOutDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(paramMap);
					// 获取销售订单该商品已经退货的数据（过滤已删除的销退单）
					// List<SaleReturnOrderRelModel> SaleReturnOrderRelList =
					// saleReturnOrderRelDao.listSaleReturnOrderRel(saleOrder.getId(),item.getGoodsId());
					// 获取该订单累计的退货数量
					Integer totalReturnNum = 0;
//					for(SaleReturnOrderRelModel sroRel : SaleReturnOrderRelList ){
//						totalReturnNum += sroRel.getNum();
//					}
					if (item.getNum() > totalOutDepotNum + totalReturnNum) {
						inventoryFreezeGoodsListJson.setNum(item.getNum() - totalOutDepotNum - totalReturnNum);
						inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
					}
				}
				try {
					// 有表体才需要推库存
					if (inventoryFreezeGoodsListJsonList != null && inventoryFreezeGoodsListJsonList.size() > 0) {
						inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
						rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(),
								MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),
								MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
					}
				} catch (Exception e) {
					LOGGER.error(
							"----------------------销售订单[" + saleOrder.getCode() + "]完结出库释放库存失败----------------------");
				}
			}
			User user = new User();
			user.setId(userId);
			user.setName(name);
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrder.getCode(), "完结出库", null, null);

		}
		return num >= 1;
	}

	@Override
	public Map<String, String> differenceRatio(Long id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Double percent = 0D;
		SaleOrderModel saleOrder = saleOrderDao.searchById(id);
		// 单据状态不是已审核，
		if (saleOrder == null || DERP_ORDER.SALEORDER_STATE_001.equals(saleOrder.getState())) {
			throw new RuntimeException("完结失败，单据状态为：待审核");
		} else if (saleOrder == null || DERP.DEL_CODE_006.equals(saleOrder.getState())) {
			throw new RuntimeException("完结失败，单据状态为：已删除");
		}
		if(StringUtils.isNotBlank(saleOrder.getWriteOffStatus())){
			throw new RuntimeException("完结失败，销售订单红冲状态不为空");
		}
		SaleOutDepotModel saleOutDepot = new SaleOutDepotModel();
		saleOutDepot.setSaleOrderId(saleOrder.getId());
		List<SaleOutDepotModel> list = saleOutDepotDao.list(saleOutDepot);
		Integer totalNum = 0;
		for (SaleOutDepotModel saleOutDepotModel : list) {
			if(saleOutDepotModel.getStatus().equals(DERP_ORDER.SALEOUTDEPOT_STATUS_018) ||
				saleOutDepotModel.getStatus().equals(DERP_ORDER.SALEOUTDEPOT_STATUS_026)||
				saleOutDepotModel.getStatus().equals(DERP_ORDER.SALEOUTDEPOT_STATUS_031)) {

				SaleOutDepotItemModel saleOutDepotItem = new SaleOutDepotItemModel();
				saleOutDepotItem.setSaleOutDepotId(saleOutDepotModel.getId());
				List<SaleOutDepotItemModel> itemList = saleOutDepotItemDao.list(saleOutDepotItem);
				for (SaleOutDepotItemModel item : itemList) {
					Integer transferNumInt = item.getTransferNum() == null ? 0 : item.getTransferNum();
					Integer wornNumInt = item.getWornNum() == null ? 0 : item.getWornNum();
					totalNum += transferNumInt + wornNumInt;
				}
			}
		}
		percent = new BigDecimal(totalNum).divide(new BigDecimal(saleOrder.getTotalNum()), 3, BigDecimal.ROUND_UP)
				.multiply(new BigDecimal(100)).doubleValue();
		String percentStr = percent + "%";
		if (percent.toString().endsWith(".0")) {
			percentStr = percent.toString().substring(0, percent.toString().lastIndexOf(".")) + "%";
		}
		map.put("orderCode", saleOrder.getCode());
		map.put("percent", percentStr);
		return map;
	}

	/**
	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
	 *
	 * @param ids
	 * @param type 用于判断是哪个业务调用； 1：审核 2：中转仓出库
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Integer> getOrderGoodsInfo(List<Long> ids, String type) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		if ("1".equals(type)) {// 审核
			for (Long id : ids) {
				// 根据id获取信息
				SaleOrderModel saleOrder = saleOrderDao.searchById(id);
				// 单据状态不是待审核
				if (!DERP_ORDER.SALEORDER_STATE_001.equals(saleOrder.getState())) {
					throw new RuntimeException("审核失败，订单：" + saleOrder.getCode() + "的单据状态不为：待审核");
				}
				// 获取仓库信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("depotId", saleOrder.getOutDepotId());
				DepotInfoMongo outDepot = depotInfoMongoDao.findOne(params);
				if (DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepot.getType())) {// 中转仓不校验
					continue;
				}
				// 获取销售表体（商品信息）
				SaleOrderItemModel saleOrderItem = new SaleOrderItemModel();
				saleOrderItem.setOrderId(saleOrder.getId());
				List<SaleOrderItemModel> itemList = saleOrderItemDao.list(saleOrderItem);
				for (SaleOrderItemModel item : itemList) {
					Integer num = 0;
					if (map.containsKey(item.getGoodsId() + "-" + saleOrder.getOutDepotId() + "-" + outDepot.getCode()
							+ "-" + item.getGoodsNo() + "-" + saleOrder.getTallyingUnit())) {
						num = map.get(item.getGoodsId() + "-" + saleOrder.getOutDepotId() + "-" + outDepot.getCode()
								+ "-" + item.getGoodsNo() + "-" + saleOrder.getTallyingUnit());
					}
					num += item.getNum();
					map.put(item.getGoodsId() + "-" + saleOrder.getOutDepotId() + "-" + outDepot.getCode() + "-"
							+ item.getGoodsNo() + "-" + saleOrder.getTallyingUnit(), num);
				}
			}
		} else if ("2".equals(type)) {// 中转仓出库
			for (Long id : ids) {
				// 根据id获取信息
				SaleOrderModel saleOrder = saleOrderDao.searchById(id);
				// 单据状态不是已审核
				if (!DERP_ORDER.SALEORDER_STATE_003.equals(saleOrder.getState())) {
					throw new RuntimeException("出库失败，订单：" + saleOrder.getCode() + "的单据状态不为：已审核");
				}
				// 获取仓库信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("depotId", saleOrder.getOutDepotId());
				DepotInfoMongo outDepot = depotInfoMongoDao.findOne(params);
				// 不是中转仓
				if (!DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepot.getType())) {
					throw new RuntimeException("出库失败，订单：" + saleOrder.getCode() + "的仓库不是中转仓");
				}
				// 获取销售表体（商品信息）
				SaleOrderItemModel saleOrderItem = new SaleOrderItemModel();
				saleOrderItem.setOrderId(saleOrder.getId());
				List<SaleOrderItemModel> itemList = saleOrderItemDao.list(saleOrderItem);
				for (SaleOrderItemModel item : itemList) {
					Integer num = 0;
					if (map.containsKey(item.getGoodsId() + "," + saleOrder.getOutDepotId() + "," + outDepot.getCode()
							+ "," + item.getGoodsNo() + "," + saleOrder.getTallyingUnit())) {
						num = map.get(item.getGoodsId() + "," + saleOrder.getOutDepotId() + "," + outDepot.getCode()
								+ "," + item.getGoodsNo() + "," + saleOrder.getTallyingUnit());
					}
					num += item.getNum();
					map.put(item.getGoodsId() + "," + saleOrder.getOutDepotId() + "," + outDepot.getCode() + ","
							+ item.getGoodsNo() + "," + saleOrder.getTallyingUnit(), num);
				}
			}
		}

		return map;
	}

	/**
	 * 导入购销
	 */
	@Override
	public Map importSaleOrder(List<List<Map<String, String>>> data, User user) throws Exception {

		int success = 0;
		int pass = 0;
		int failure = 0;
		String contractNo = ""; // 合同号
		List<Map<String, String>> msgList = new ArrayList<Map<String, String>>();
		// ps:序号是表头与表体关联的标识 序号与表头是1对1，表头与表体是1对多
		Map<String, SaleOrderModel> modelMap = new HashMap<String, SaleOrderModel>();
		Map<String, List<SaleOrderItemModel>> itemMap = new HashMap<String, List<SaleOrderItemModel>>();
		// 用于统计销售订单的总商品数量
		Map<String, Integer> totalMap = new HashMap<String, Integer>();
		// 用于统计销售订单的总商品价格
		Map<String, BigDecimal> totalAmountMap = new HashMap<String, BigDecimal>();
		// 检查某个销售订单编号+该仓库公司关联的信息
		Map<String, DepotMerchantRelMongo> checkGoodsNoByDepotMap = new HashMap<>();
		// 检查某个销售订单编号+该销售订单下的商品是否重复
		Set<String> checkGoodsNoBySalecodeSet = new HashSet<>();
		// 出仓仓库信息
//		DepotInfoMongo outDepotInfo = null;
		Map<String,DepotInfoMongo > outDepotInfoMap = new HashMap<>();
		String poNo = null; // 导入进来的PO单号
//		String[] poResult = null;
		Boolean salePriceManage = false;
		// 查询商家
		Map<String, Object> merchantMap = new HashMap<String, Object>();
		merchantMap.put("merchantId", user.getMerchantId());
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);
		boolean isTradeMechant = "1".equals(merchant.getRegisteredType());//是否是内贸主体
		for (int i = 0; i < 1; i++) {// 表头
			List<Map<String, String>> objects = data.get(i);
			for (int j = 0; j < objects.size(); j++) {
				try {
					Map<String, String> msg = new HashMap<String, String>();
					Map<String, String> map = objects.get(j);
					String index = map.get("自编销售订单号");
					if (index == null || "".equals(index)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "自编销售订单号不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}

					String code = map.get("销售客户ID");
					if (code == null || "".equals(code)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "销售客户ID不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}

					String outDepotCode = map.get("出仓仓库自编码");
					if (outDepotCode == null || "".equals(outDepotCode)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "出仓仓库自编码不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					// 获取出仓仓库信息
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("depotCode", outDepotCode);
					params.put("status", "1");
					DepotInfoMongo outDepotInfo = depotInfoMongoDao.findOne(params);
					if (null == outDepotInfo) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "出仓仓库不存在或未启用");
						msgList.add(msg);
						failure += 1;
						continue;
					}

					// 仓库公司关联表
					Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
					depotMerchantRelParam.put("merchantId", user.getMerchantId());
					depotMerchantRelParam.put("depotId", outDepotInfo.getDepotId());
					DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
					if (depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "仓库ID为：" + outDepotInfo.getDepotId() + ",未查到该公司下调出仓库信息");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					String key = index;
					if (!checkGoodsNoByDepotMap.containsKey(key)) {
						checkGoodsNoByDepotMap.put(key, depotMerchantRelMongo);
					}
					if(!outDepotInfoMap.containsKey(key)){
						outDepotInfoMap.put(key, outDepotInfo);
					}
					String buCode = map.get("事业部");
					if (buCode == null || "".equals(buCode)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "事业部不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					// 获取该事业部信息
					Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
					merchantBuRelParam.put("merchantId", user.getMerchantId());
					merchantBuRelParam.put("buCode", buCode);
					merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
					MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
					if (merchantBuRelMongo == null || "".equals(merchantBuRelMongo)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "事业部编码为：" + buCode + ",未查到该公司下事业部信息");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					// 校验公司-仓库-事业部的关联表
					Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
					merchantDepotBuRelParam.put("merchantId", user.getMerchantId());
					merchantDepotBuRelParam.put("depotId", outDepotInfo.getDepotId()); // 出仓仓库
					merchantDepotBuRelParam.put("buId", merchantBuRelMongo.getBuId());
					MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao
							.findOne(merchantDepotBuRelParam);
					if (outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "事业部编码为：" + merchantBuRelMongo.getBuCode() + ",出仓仓库：" + outDepotCode
								+ ",未查到该公司仓库事业部关联信息");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					// 校验事业部与当前账号所绑定的事业部是否匹配
					boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), merchantBuRelMongo.getBuId());
					if (!userRelateBu) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg",
								"事业部编码为：" + merchantBuRelMongo.getBuCode() + ",用户id：" + user.getId() + ",无权限操作该事业部");
						msgList.add(msg);
						failure += 1;
						continue;
					}

					String businessModel = map.get("销售类型");

					if (businessModel == null || "".equals(businessModel)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "销售类型不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					businessModel = businessModel.trim();
					if ("购销-整批结算".equals(businessModel)) {
						businessModel = "1";
					} else if ("购销-实销实结".equals(businessModel)) {
						businessModel = "4";
					} else if ("采销执行".equals(businessModel)) {
						businessModel = "3";
					} else {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "请输入正确的销售类型");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					// 当销售类型为采销执行类型时，出仓仓库可选仅为中转仓
					if ("3".equals(businessModel) && !DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepotInfo.getType())) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "当销售类型为采销执行类型时，出仓仓库可选仅为中转仓,当前出仓仓库类型为:" + outDepotInfo.getType());
						msgList.add(msg);
						failure += 1;
						continue;
					}
					// 当销售类型为购销-实销实结、购销-整批结算类型时，出仓仓库可选仅为保税仓、海外仓、中转仓
					if (("1".equals(businessModel) || "4".equals(businessModel))
							&& (!DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepotInfo.getType())
									&& !DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepotInfo.getType())
									&& !DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepotInfo.getType()))) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg",
								"当销售类型为购销-实销实结、购销-整批结算类型时，出仓仓库可选仅为保税仓、海外仓、中转仓,当前出仓仓库类型为:" + outDepotInfo.getType());
						msgList.add(msg);
						failure += 1;
						continue;
					}
					// 当出仓仓库为保税仓时，是否同关区必填；其他情况下非必填；
					DepotInfoMongo inDepotInfo = null;
					String isSameArea = map.get("是否同关区");
					String inDepotCode = map.get("入仓仓库自编码");

					if ((outDepotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A) && "是".equals(isSameArea))
							|| (outDepotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A) && "否".equals(isSameArea)
									&& DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(businessModel))
							|| (outDepotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_D)
									&& DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(businessModel))) {
						if (inDepotCode == null || "".equals(inDepotCode)) {
							msg.put("row", String.valueOf(j + 2));
							msg.put("msg", "入仓仓库自编码不能为空");
							msgList.add(msg);
							failure += 1;
							continue;
						}
						// 获取入仓仓库信息
						Map<String, Object> params1 = new HashMap<String, Object>();
						params1.put("depotCode", inDepotCode);
						params1.put("status", "1");
						inDepotInfo = depotInfoMongoDao.findOne(params1);
						if (null == inDepotInfo) {
							msg.put("row", String.valueOf(j + 2));
							msg.put("msg", "入仓仓库不存在或未启用");
							msgList.add(msg);
							failure += 1;
							continue;
						}
					}

					if (outDepotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)) {
						if (isSameArea == null || "".equals(isSameArea)) {
							msg.put("row", String.valueOf(j + 2));
							msg.put("msg", "当出仓仓库为保税仓时，是否同关区不能为空");
							msgList.add(msg);
							failure += 1;
							continue;
						}
						// 1、当出仓仓库为保税仓，且是否同关区为“是”时，入库仓库必填，且入库仓库仅能为备查库
						if ("是".equals(isSameArea)) {
							if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepotInfo.getType())) {
								msg.put("row", String.valueOf(j + 2));
								msg.put("msg", "入仓仓库只能为备查库");
								msgList.add(msg);
								failure += 1;
								continue;
							}
						} else if ("否".equals(isSameArea)) {
							/*
							 * 2、当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-整批结算时，入库仓库禁用； 3、当出仓仓库为保税仓，且是否同关区为“否”
							 * 且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
							 */
							if (DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(businessModel)) {
								if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepotInfo.getType())) {
									msg.put("row", String.valueOf(j + 2));
									msg.put("msg", "入仓仓库必须为备查库");
									msgList.add(msg);
									failure += 1;
									continue;
								}
							}
						}
					}
					/**
					 * 1、当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
					 * 2、当出仓仓库为海外仓且销售类型为购销-整批结算时，入库仓库禁用，不予选择；
					 * 3、当出仓仓库为中转仓，且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
					 */
					else if ((DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepotInfo.getType())
							&& DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(businessModel))
							|| (DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepotInfo.getType())
									&& DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(businessModel))) {
						if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepotInfo.getType())) {
							msg.put("row", String.valueOf(j + 2));
							msg.put("msg", "入仓仓库必须为备查库");
							msgList.add(msg);
							failure += 1;
							continue;
						}
					}

					poNo = map.get("PO单号");
					if(StringUtils.isNotBlank(poNo) && !checkPoNo(poNo)){
						//输入时仅限中文、大小写字母、数字和“-”这4种字符
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "PO号仅限中文、大小写字母、数字和“-”这4种字符");
						msgList.add(msg);
						failure += 1;
						continue;
					}

					String currency = map.get("销售币种");
					currency = currency.trim();
					if (currency == null || "".equals(currency)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "销售币种不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					} else {
						String currencyLabel = DERP_ORDER.getLabelByKey(DERP.currencyCodeList, currency);
						if (StringUtils.isBlank(currencyLabel)) {
							msg.put("row", String.valueOf(j + 2));
							msg.put("msg", "销售币种输入有误");
							msgList.add(msg);
							failure += 1;
							continue;
						}
					}

//					poResult = poNo.split("&");
					/**
					 * 1、当销售类型为购销-实销实结、采销执行时，PO单号非必填； 2、当销售类型为购销-整批结算时，PO单号必填项；
					 */
					if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(businessModel)) {
						// 判断购销PO单号是否为空
						if (poNo == null || "".equals(poNo)) {
							msg.put("row", String.valueOf(j + 2));
							msg.put("msg", "销售类型为购销-整批结算时，PO号不能为空");
							msgList.add(msg);
							failure += 1;
							continue;
						}
					}
					if (modelMap.containsKey(index)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "表头自编销售订单号：" + index + "出现重复");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					/**库位类型*/
					String stockLocationName = map.get("库位类型");
					//通过“公司+事业部”查询公司事业部是否启用了库位管理，若启用则该字段必填
					if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(merchantBuRelMongo.getStockLocationManage()) && StringUtils.isBlank(stockLocationName)){
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"已开启库位管理，库位类型不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}else if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1.equals(merchantBuRelMongo.getStockLocationManage()) && StringUtils.isNotBlank(stockLocationName)){
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"未开启库位管理，库位类型不允许填写");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					BuStockLocationTypeConfigMongo stockLocationMongo = null;
					if(org.apache.commons.lang3.StringUtils.isNotBlank(stockLocationName)){
						Map<String,Object> stockLocationMap = new HashMap<>();
						stockLocationMap.put("merchantId", user.getMerchantId());
						stockLocationMap.put("buId", merchantBuRelMongo.getBuId());
						stockLocationMap.put("name", stockLocationName);
						stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
						stockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);

						if(stockLocationMongo == null){
							msg.put("row", String.valueOf(j + 2));
							msg.put("msg", "当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"库位类型："+stockLocationName+"，不存在");
							msgList.add(msg);
							failure += 1;
							continue;
						}
					}

					// 注入数据
					SaleOrderModel saleOrderModel = new SaleOrderModel();
//					saleOrderModel.setCode(CodeGeneratorUtil.getNo("XSO"));
					saleOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSO));
					saleOrderModel.setMerchantId(user.getMerchantId());
					saleOrderModel.setMerchantName(user.getMerchantName());
					saleOrderModel.setTopidealCode(user.getTopidealCode());
					saleOrderModel.setBusinessModel(businessModel);
					saleOrderModel.setCreateDate(TimeUtils.getNow());
					saleOrderModel.setCreater(user.getId());
					saleOrderModel.setCreateName(user.getName());
					// 获取客户信息
					Map<String, Object> customerMap = new HashMap<String, Object>();
					customerMap.put("code", code);
					customerMap.put("status", "1");
					CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(customerMap);
					if (customerInfo == null) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "客户信息不存在或未启用");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					Map<String, Object> params2 = new HashMap<String, Object>();
					params2.put("code", code);
					params2.put("merchantId", user.getMerchantId());
					CustomerMerchantRelMongo customer = customerMerchantRelMongoDao.findOne(params2);
					if (customer == null) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "客户信息不存在或未启用");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					if (StringUtils.isNotBlank(isSameArea)) {
						if ("是".equals(isSameArea)) {
							isSameArea = "1";
						} else if ("否".equals(isSameArea)) {
							isSameArea = "0";
						} else {
							msg.put("row", String.valueOf(j + 2));
							msg.put("msg", "是否同关区，填‘是’或‘否’即可");
							msgList.add(msg);
							failure += 1;
							continue;
						}
					}
					saleOrderModel.setOrderType(DERP_ORDER.SALEORDER_ORDERTYPE_2);// 单据标识 1-预售转销 2-非预售转销
					saleOrderModel.setBuId(merchantBuRelMongo.getBuId()); // 事业部ID
					saleOrderModel.setBuName(merchantBuRelMongo.getBuName());// 事业部名称
					saleOrderModel.setIsSameArea(isSameArea);
					saleOrderModel.setCurrency(currency); // 币种
					saleOrderModel.setCustomerId(customer.getCustomerId());
					saleOrderModel.setCustomerName(customer.getName());
					saleOrderModel.setPoNo(poNo);
					saleOrderModel.setOutDepotId(outDepotInfo.getDepotId());
					saleOrderModel.setOutDepotName(outDepotInfo.getName());
					saleOrderModel.setOutDepotCode(outDepotInfo.getCode());
					saleOrderModel.setInspectNo(outDepotInfo.getInspectNo());
					saleOrderModel.setCustomsNo(outDepotInfo.getCustomsNo());
					if (inDepotInfo != null) {
						saleOrderModel.setInDepotId(inDepotInfo.getDepotId());
						saleOrderModel.setInDepotName(inDepotInfo.getName());
						saleOrderModel.setInDepotCode(inDepotInfo.getCode());
					}

					saleOrderModel.setState(DERP_ORDER.SALEORDER_STATE_008);
					if(stockLocationMongo != null){
						saleOrderModel.setStockLocationTypeId(stockLocationMongo.getBuStockLocationTypeId());
						saleOrderModel.setStockLocationTypeName(stockLocationMongo.getName());
					}

					// 调入客户卓志编码
					String inCustomerTopNo = "";
					if (customer.getCustomerId() != null && customer.getCustomerId().longValue() > 0) {
						Map<String, Object> cMap = new HashMap<String, Object>();
						cMap.put("customerid", customer.getCustomerId());
						CustomerInfoMogo inCustomerInfo = customerInfoMongoDao.findOne(cMap);
						if (inCustomerInfo != null)
							inCustomerTopNo = inCustomerInfo.getMainId();
					}
					if (inDepotInfo != null) {
						// 同关区
						boolean isSame = !StringUtils.isEmpty(outDepotInfo.getCustomsNo()) && inDepotInfo != null
								&& !StringUtils.isEmpty(inDepotInfo.getCustomsNo())
								&& outDepotInfo.getCustomsNo().equals(inDepotInfo.getCustomsNo());

						// 1.调出调入公司不同,出和入的仓库编码相同 -业务场景：40 ：账册内货权转移 服务类型：G0：库内调拨服务
						if (!user.getTopidealCode().equals(inCustomerTopNo)
								&& outDepotInfo.getCode().equals(inDepotInfo.getCode())) {
							saleOrderModel.setModel("40");
							saleOrderModel.setServeTypes("G0");
						}
						// 2.调出调入公司不同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：50： 账册内货权转移调仓 服务类型：E0：区内调拨调出服务
						else if (!user.getTopidealCode().equals(inCustomerTopNo) && isSame
								&& !outDepotInfo.getCode().equals(inDepotInfo.getCode())) {
							saleOrderModel.setModel("50");
							saleOrderModel.setServeTypes("E0");
						}
						// 3.调出调入公司相同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：10： 账册内调仓 服务类型：E0：区内调拨调出服务
						else if (user.getTopidealCode().equals(inCustomerTopNo) && isSame
								&& !outDepotInfo.getCode().equals(inDepotInfo.getCode())) {
							saleOrderModel.setModel("10");
							saleOrderModel.setServeTypes("E0");
						}
						// 4.调出调入公司相同，出入仓库不同关区，出仓和入库仓库编码不同-业务场景：10 ：账册内调仓 服务类型：E0：区内调拨调出服务
						else if (user.getTopidealCode().equals(inCustomerTopNo)
								&& !(isSame || (StringUtils.isEmpty(outDepotInfo.getCustomsNo()) && inDepotInfo != null
										&& StringUtils.isEmpty(inDepotInfo.getCustomsNo())))
								&& !outDepotInfo.getCode().equals(inDepotInfo.getCode())) {
							saleOrderModel.setModel("10");
							saleOrderModel.setServeTypes("E0");
						}
					}

					modelMap.put(index, saleOrderModel);
				} catch (Exception e) {
					e.printStackTrace();
					failure += 1;
					continue;
				}
			}

		}
		if (failure == 0) {
			for (int i = 1; i < 2; i++) {// 表体
				List<Map<String, String>> objects = data.get(i);
				Map<String, SaleOrderItemModel> saleItemMap = new HashMap<>();
				for (int j = 0; j < objects.size(); j++) {
					Map<String, String> msg = new HashMap<String, String>();
					Map<String, String> map = objects.get(j);
					String index = map.get("自编销售订单号");
					if (index == null || "".equals(index)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "自编销售订单号不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					String outGoodsCode = map.get("销出商品编号");
					String outGoodsNo = map.get("销出商品货号");
					if (outGoodsNo == null || "".equals(outGoodsNo)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "销出商品货号不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					String outGoodsName = map.get("销出商品名称");

					String num = map.get("销售数量");
					num = num.trim(); // 去掉空格后的值
					if (num == null || "".equals(num)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "销售数量不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					if (Integer.valueOf(num) < 1) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "销售数量必须大于0");
						msgList.add(msg);
						failure += 1;
						continue;
					}

					String taxRate = map.get("税率");
					if(isTradeMechant && StringUtils.isBlank(taxRate)){
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "当前公司主体公司注册地类型为：“境内”，税率必填");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					if(StringUtils.isNotBlank(taxRate)){
						if(!isNumeric(taxRate)){
							msg.put("row", String.valueOf(j + 2));
							msg.put("msg", "税率必须是整数或小数");
							msgList.add(msg);
							failure += 1;
							continue;
						}

						int indexOf = taxRate.indexOf(".");
						// 如果是小数
						if (indexOf != -1) {
							int count = taxRate.length() - 1 - indexOf;
							if (count > 2) {
								msg.put("row", String.valueOf(j + 2));
								msg.put("msg", "商品货号：" + outGoodsNo + "，税率小数点后只能为两位数");
								msgList.add(msg);
								failure += 1;
								continue;
							}
						}
					}
					taxRate = taxRate.trim(); // 去掉空格后的值

					// 注入数据
					SaleOrderItemModel saleOrderItem = new SaleOrderItemModel();

					// 获取商品信息(取对应公司仓库选品配置,不用管代理公司了)
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("goodsNo", outGoodsNo);
					params1.put("merchantId", user.getMerchantId());
					MerchandiseInfoMogo outGoods = merchandiseInfoMogoDao.findOne(params1);

					if (outGoods == null) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "自编销售订单号:" + index + "，销出商品货号:" + outGoodsNo + "，该销出商品不存在");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					String key = index;
					if (outDepotInfoMap.containsKey(key)) {
						DepotInfoMongo outDepot = outDepotInfoMap.get(index);
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("goodsNo", outGoodsNo);
						paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);	// 状态(1使用中,0已禁用)
						paramMap.put("merchantId", user.getMerchantId());
						List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(paramMap, outDepot.getDepotId());
						if(merchandiseList == null || merchandiseList.size() < 1) {
							msg.put("row", String.valueOf(j + 2));
							msg.put("msg", "自编销售订单号:" + index + "出库仓："+outDepot.getName()+"，销出商品货号:" + outGoodsNo + "，未找到启用商品");
							msgList.add(msg);
							failure += 1;
							continue;
						}

					}
					if (!modelMap.containsKey(index)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "自编销售订单号:" + index + "在表头不存在");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					// 是否开启销售价格管理
					SaleOrderModel saleOrder = modelMap.get(index);
					Boolean  isOpenSalePriceManage = getSalePriceManage(saleOrder.getBuId(), saleOrder.getCustomerId(), user);

					//金额必填
					String amount = map.get("销售总金额");
					if (StringUtils.isBlank(amount)) {
						msg.put("row", String.valueOf(j + 2));
						msg.put("msg", "商品货号：" + outGoodsNo + "，销售总金额不能为空");
						msgList.add(msg);
						failure += 1;
						continue;
					} else if(StringUtils.isNotBlank(amount)){
						if(!isNumeric(amount)){
							msg.put("row", String.valueOf(j + 2));
							msg.put("msg", "销售金额必须是整数或小数");
							msgList.add(msg);
							failure += 1;
							continue;
						}
						int indexOf = amount.indexOf(".");
						// 如果是小数
						if (indexOf != -1) {
							int count = amount.length() - 1 - indexOf;
							if (count > 2) {
								msg.put("row", String.valueOf(j + 2));
								msg.put("msg", "商品货号：" + outGoodsNo + "，销售总金额小数点后只能为两位数");
								msgList.add(msg);
								failure += 1;
								continue;
							}
						}
						amount = amount.trim();
					}
					//获取对应品牌信息
					Map<String, Object> params3 = new HashMap<String, Object>();
					params3.put("brandId", outGoods.getBrandId());
					BrandMongo brandMongo = brandMongoDao.findOne(params3);
					if (brandMongo != null) {
						saleOrderItem.setBrandName(brandMongo.getName());
					}
					saleOrderItem.setCommbarcode(outGoods.getCommbarcode());// 标准条码
					saleOrderItem.setBarcode(outGoods.getBarcode());
					saleOrderItem.setGoodsCode(outGoods.getCode());
					saleOrderItem.setGoodsId(outGoods.getMerchandiseId());
					saleOrderItem.setGoodsName(outGoods.getName());
					saleOrderItem.setGoodsNo(outGoods.getGoodsNo());
					saleOrderItem.setNum(Integer.valueOf(num));

					// 序号
					saleOrderItem.setSeq(j);


						amount = StringUtils.isBlank(amount) && isOpenSalePriceManage ? "0" : amount;
						BigDecimal price = new BigDecimal(amount).divide(new BigDecimal(num), 8,BigDecimal.ROUND_HALF_UP);
						BigDecimal totalAmount = new BigDecimal(amount);
						// 是否开启销售价格管理，计算销售金额，记录销售单价
						if (isOpenSalePriceManage) {
							HashMap<String, Object> customerSalePriceMap = new HashMap<>();
							customerSalePriceMap.put("merchantId", user.getMerchantId());
							customerSalePriceMap.put("customerId", saleOrder.getCustomerId());
							customerSalePriceMap.put("commbarcode", outGoods.getCommbarcode());
							customerSalePriceMap.put("buId", saleOrder.getBuId());
							customerSalePriceMap.put("currency", saleOrder.getCurrency());
							customerSalePriceMap.put("status", DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003);
							List<CustomerSalePriceMongo> mList = customerSalePriceMongoDao.findAll(customerSalePriceMap);

							if (mList.isEmpty()) {
								msg.put("row", String.valueOf(j + 2));
								msg.put("msg", "该公司事业部已开启销售价格管理，货号 ：" + outGoods.getGoodsNo() + " 标准条码在销售价格管理无数据");
								msgList.add(msg);
								failure += 1;
								continue;
							}
							//存储有效的销售价格*数量保留两位销售和页面传来总金额进行比较
							List<BigDecimal> salePriceAmountList = new ArrayList<>();
							Map<BigDecimal, BigDecimal> salePriceAmountPriceMap = new HashMap<>();
							for (CustomerSalePriceMongo tempMongo : mList) {
								if (TimeUtils.parseFullTime(tempMongo.getEffectiveDate() + " 00:00:00").getTime() <= TimeUtils.getNow().getTime()
									&& TimeUtils.parseFullTime(tempMongo.getExpiryDate() + " 23:59:59").getTime() >= TimeUtils.getNow().getTime()) {
//									salePriceMongo = tempMongo;
									BigDecimal salePriceAmount= tempMongo.getSalePrice().multiply(new BigDecimal(num)).setScale(2, BigDecimal.ROUND_HALF_UP);
									salePriceAmountList.add(salePriceAmount);
									salePriceAmountPriceMap.put(salePriceAmount, tempMongo.getSalePrice().setScale(8, BigDecimal.ROUND_HALF_UP));
								}
							}
							BigDecimal finalTotalAmount = totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
							Boolean flagPrice = salePriceAmountList.stream().anyMatch(saleAmount-> finalTotalAmount.compareTo(saleAmount)==0);
							if(! flagPrice) {
								msg.put("row", String.valueOf(j + 2));
								msg.put("msg", "该公司事业部已开启销售价格管理，货号 ：" + outGoods.getGoodsNo() + " 销售单价在销售价格管理不存在");
								msgList.add(msg);
								failure += 1;
								continue;
							}
//							price = finalTotalAmount.divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);
							price = salePriceAmountPriceMap.get(finalTotalAmount);

						}
						saleOrderItem.setPrice(price);
						saleOrderItem.setAmount(totalAmount);
						if(StringUtils.isNotBlank(taxRate)){
						BigDecimal taxRateB = new BigDecimal(taxRate);
						BigDecimal taxPrice = saleOrderItem.getPrice().multiply(taxRateB.add(BigDecimal.ONE)).setScale(8,BigDecimal.ROUND_HALF_UP);
						BigDecimal taxAmount = taxPrice.multiply(new BigDecimal(saleOrderItem.getNum())).setScale(2,BigDecimal.ROUND_HALF_UP);
						BigDecimal tax = taxAmount.subtract(saleOrderItem.getAmount());
						saleOrderItem.setTaxRate(taxRateB.doubleValue());
						saleOrderItem.setTaxPrice(taxPrice);
						saleOrderItem.setTaxAmount(taxAmount);
						saleOrderItem.setTax(tax);
					}else{
						saleOrderItem.setTaxRate(BigDecimal.ZERO.doubleValue());
						saleOrderItem.setTaxPrice(saleOrderItem.getPrice());
						saleOrderItem.setTaxAmount(saleOrderItem.getAmount());
						saleOrderItem.setTax(BigDecimal.ZERO);
					}

					// 记录表体信息
					List<SaleOrderItemModel> itemModelList = new ArrayList<SaleOrderItemModel>();
					if (itemMap.containsKey(index)) {
						itemModelList = itemMap.get(index);
					}
					if(itemModelList.size()>0) {
						boolean flag = true;
						for (SaleOrderItemModel sItemModel : itemModelList) {
							//如果商品货号、单价相同，则数量增加
							if(sItemModel.getGoodsId().equals(saleOrderItem.getGoodsId()) && sItemModel.getPrice().compareTo(saleOrderItem.getPrice()) == 0){
								int sum = sItemModel.getNum() + saleOrderItem.getNum();
								sItemModel.setNum(sum);
								sItemModel.setAmount(new BigDecimal(sum).multiply(saleOrderItem.getPrice()));
								sItemModel.setTaxAmount(new BigDecimal(sum).multiply(saleOrderItem.getTaxPrice()));
								flag = false;
								break;
							}
						}

						if(flag){
							itemModelList.add(saleOrderItem);
						}

					}else{
						itemModelList.add(saleOrderItem);
					}
					itemMap.put(index, itemModelList);
					// 记录总商品数量
					Integer totalNum = 0;
					if (totalMap.containsKey(index)) {
						totalNum = totalMap.get(index);
					}
					totalNum += Integer.valueOf(num);
					totalMap.put(index, totalNum);
					if (totalAmountMap.containsKey(index)) {
						totalAmount = totalAmount.add(totalAmountMap.get(index));
					}
					totalAmountMap.put(index, totalAmount);

				}
			}
		}
		if (failure == 0) {
			LOGGER.debug("<=========购销导入保存数据开始===========");
			// 保存数据
			for (Entry<String, SaleOrderModel> entry : modelMap.entrySet()) {
				Map<String, String> msg = new HashMap<String, String>();
				String index = entry.getKey();
				SaleOrderModel saleOrder = entry.getValue();
				// 校验表头
				Integer totalNum = totalMap.get(index);
				if (totalNum == null || totalNum == 0) {
					msg.put("row", "");
					msg.put("msg", "自编销售订单号：" + index + "的商品信息为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				// 校验表体
				List<SaleOrderItemModel> itemList = itemMap.get(index);
				if (itemList == null || itemList.size() == 0) {
					msg.put("row", "");
					msg.put("msg", "自编销售订单号：" + index + "的商品信息为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				if (failure == 0) {
					saleOrder.setTotalNum(totalNum);
					saleOrder.setTotalAmount(totalAmountMap.get(index));
					saleOrder.setOrderSource(DERP_ORDER.SALEORDER_ORDERSOURCE_1);
					saleOrder.setContractNo(contractNo);
					saleOrder.setAmountStatus(DERP_ORDER.SALEORDER_AMOUMTSTATUS_0);
					LOGGER.debug("<=========表头导入===========" + index);
					saleOrderDao.save(saleOrder);

					// 在新增销售订单号之后再去保存到销售单_po关联表中（因为这里需要销售单id、销售单号）
					SalePoRelModel salePoRelModel = new SalePoRelModel();
					salePoRelModel.setPoNo(saleOrder.getPoNo());
					salePoRelModel.setState("0"); // 类型:0:销售订单1:销售退订单
					salePoRelModel.setOrderId(saleOrder.getId());
					salePoRelModel.setOrderCode(saleOrder.getCode());
					salePoRelModel.setMerchantId(user.getMerchantId()); // 公司ID
					salePoRelModel.setMerchantName(user.getMerchantName()); // 公司名称
					salePoRelModel.setCreateDate(TimeUtils.getNow());
					// 不存在则保存到销售单_po关联表中去
					salePoRelDao.save(salePoRelModel);

					int seq = 1;
					for (SaleOrderItemModel item : itemList) {
						item.setOrderId(saleOrder.getId());
						item.setSeq(seq++);
						saleOrderItemDao.save(item);
						LOGGER.debug("<=========保存表体===========" + index + "&" + item.getGoodsNo());
						success++;
					}

					commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrder.getCode(), "导入销售订单", null, null);
				}
			}
			LOGGER.debug("<=========购销导入保存数据结束===========");
		}
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("msgList", msgList);
		return map;
	}

	/**
	 * 把两个数组组成一个map
	 *
	 * @param keys   键数组
	 * @param values 值数组
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
	 * 根据条件获取销售订单信息
	 *
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SaleOrderDTO> listSaleOrder(SaleOrderDTO dto, User user) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buIds.isEmpty()) {
				return new ArrayList<SaleOrderDTO>();
			}
			dto.setBuList(buIds);
		}
		// 根据上架时间检索
		if (StringUtils.isNotBlank(dto.getShelfDateStartDate())
				|| StringUtils.isNotBlank(dto.getShelfDateStartDate())) {
			ShelfDTO shelfDto = new ShelfDTO();
			if (StringUtils.isNotBlank(dto.getShelfDateStartDate())) {
				shelfDto.setShelfStartDate(dto.getShelfDateStartDate());
			}
			if (StringUtils.isNotBlank(dto.getShelfDateEndDate())) {
				shelfDto.setShelfEndDate(dto.getShelfDateEndDate());
			}
			List<ShelfDTO> shelfList = shelfDao.listDTO(shelfDto);
			if (shelfList != null && shelfList.size() > 0) {
				List<Long> saleIds = new ArrayList<Long>();
				for (ShelfDTO shelf : shelfList) {
					saleIds.add(shelf.getSaleOrderId());
				}
				if (saleIds.size() > 0) {
					dto.setIds(StringUtils.join(saleIds, ","));
				}
			} else {// 上架时间检索为空时，直接返回空
				return new ArrayList<SaleOrderDTO>();
			}
		}
		if (StringUtils.isNotBlank(dto.getPoNo()) && dto.getPoNo().indexOf(",") > 0) {
			List<String> poNos = new ArrayList<String>();
			for (String poNo : dto.getPoNo().split(",")) {
				if (StringUtils.isNotBlank(poNo)) {
					poNos.add(poNo);
				}
			}
			if (!poNos.isEmpty()) {
				dto.setPoNos(poNos);
				dto.setPoNo(null);
			}
		}
		List<SaleOrderDTO> saleOrderList = saleOrderDao.queryDTOList(dto);
		for (SaleOrderDTO saleOrder : saleOrderList) {
			Long customerId = saleOrder.getCustomerId();
			Map<String, Object> cMap = new HashMap<String, Object>();
			cMap.put("customerid", customerId);
			CustomerInfoMogo inCustomer = customerInfoMongoDao.findOne(cMap);
			if (inCustomer != null) {
				if (DERP_SYS.CUSTOMERINFO_TYPE_1.equals(inCustomer.getType())) {
					saleOrder.setIsInnerCustomer("是");
				} else {
					saleOrder.setIsInnerCustomer("否");
				}
			}
		}
		return saleOrderList;
	}

	@Override
	public SaleOrderDTO checkOrderState(Long id) throws RuntimeException, SQLException {
		SaleOrderDTO saleOrder = saleOrderDao.searchDTOById(id);
		if(StringUtils.isNotBlank(saleOrder.getWriteOffStatus())){
			throw new RuntimeException("销售订单红冲状态不为空，不允许生成采购单");
		}
		if (!DERP_ORDER.SALEORDER_STATE_003.equals(saleOrder.getState())
				&& !DERP_ORDER.SALEORDER_STATE_001.equals(saleOrder.getState())) {
			throw new RuntimeException("只有待审核、已审核状态下才能生成采购单");
		}

		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", saleOrder.getOutDepotId());
		DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(depotInfo_params);
		if (depotInfo == null) {
			throw new RuntimeException("出仓仓库不存在，不能生成采购订单");
		}

		SaleOrderItemDTO saleOrderItem = new SaleOrderItemDTO();
		saleOrderItem.setOrderId(id);
		List<SaleOrderItemDTO> itemList = saleOrderItemDao.listSaleOrderItemDTO(saleOrderItem);
		saleOrder.setItemList(itemList);

		return saleOrder;
	}

	@Override
	public Map<String,Object> listShelfListByOrderId(Long saleId, String saleOutCode) throws SQLException {
		// 要返回的结果集
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<SaleShelfDTO> list = new ArrayList<SaleShelfDTO>();
		SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
		saleOutDepotModel.setCode(saleOutCode);
		saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
		if (saleOutDepotModel == null ) {
			throw new RuntimeException("销售出库单不存在");
		}
		// 获取已上架的数据
		SaleShelfModel model = new SaleShelfModel();
		model.setSaleOutDepotId(saleOutDepotModel.getId());
		List<SaleShelfModel> shelfList = saleShelfDao.list(model);
		/*
		 * 遍历计算商品已上架、已计入残损的数据
		 */
		Map<Long, Integer> goodsShelfMap = new HashMap<Long, Integer>();// 商品对应的上架数量
		Map<Long, Integer> goodsDamagedMap = new HashMap<Long, Integer>();// 商品对应的残损数量
		Map<Long, Integer> goodsLackMap = new HashMap<Long, Integer>();// 商品对应的缺货数量
		Map<String, SaleShelfDTO> ssModelMap = new HashMap<String, SaleShelfDTO>();// 上架信息 key:商品id_理货单位
		for (SaleShelfModel ssModel : shelfList) {
			if(ssModel.getSaleItemId() == null){
				throw new RuntimeException("对应上架单的销售id为空，请及时维护");
			}
			Integer shelfNum = ssModel.getShelfNum();
			if (goodsShelfMap.containsKey(ssModel.getSaleItemId())) {
				shelfNum += goodsShelfMap.get(ssModel.getSaleItemId());
			}
			goodsShelfMap.put(ssModel.getSaleItemId(), shelfNum);
			Integer damagedNum = ssModel.getDamagedNum();
			if (goodsDamagedMap.containsKey(ssModel.getSaleItemId())) {
				damagedNum += goodsDamagedMap.get(ssModel.getSaleItemId());
			}
			goodsDamagedMap.put(ssModel.getSaleItemId(), damagedNum);
			// 缺货数量
			Integer lackNum = ssModel.getLackNum();
			if (goodsLackMap.containsKey(ssModel.getSaleItemId())) {
				lackNum += goodsLackMap.get(ssModel.getSaleItemId());
			}
			goodsLackMap.put(ssModel.getSaleItemId(), lackNum);
		}
		// 获取销售出库商品信息

		SaleOutDepotItemModel saleOutDepotItem = new SaleOutDepotItemModel();
		saleOutDepotItem.setSaleOutDepotId(saleOutDepotModel.getId());
		List<SaleOutDepotItemModel> itemList = saleOutDepotItemDao.list(saleOutDepotItem);
		// 销售商品信息匹配上架数据
		for (SaleOutDepotItemModel item : itemList) {// 相同商品要合成一条
			if(item.getSaleItemId() == null){
				throw new RuntimeException("对应出库单的销售明细id为空，请及时维护");
			}
			SaleShelfDTO ssDTO = new SaleShelfDTO();
			if (ssModelMap.containsKey(item.getSaleItemId() + "_" + item.getTallyingUnit())) {// 存在数量相加
				ssDTO = ssModelMap.get(item.getSaleItemId() + "_" + item.getTallyingUnit());
				Integer transferNumInt = item.getTransferNum() == null ? 0 : item.getTransferNum();
				Integer wornNumInt = item.getWornNum() == null ? 0 : item.getWornNum();
				ssDTO.setNum(ssDTO.getNum() + transferNumInt + wornNumInt);
				Integer totalDamagedNum = 0;
				if (goodsDamagedMap.containsKey(item.getSaleItemId())) {
					totalDamagedNum = goodsDamagedMap.get(item.getSaleItemId());
				}
				ssDTO.setTotalDamagedNum(totalDamagedNum);
				Integer totalShelfNum = 0;
				if (goodsShelfMap.containsKey(item.getSaleItemId())) {
					totalShelfNum = goodsShelfMap.get(item.getSaleItemId());
				}
				ssDTO.setTotalShelfNum(totalShelfNum);
				// 少货数量
				Integer totalLackNum = 0;
				if (goodsLackMap.containsKey(item.getSaleItemId())) {
					totalLackNum = goodsLackMap.get(item.getSaleItemId());
				}
				ssDTO.setTotalLackNum(totalLackNum);

				ssDTO.setStayShelfNum(ssDTO.getNum() - totalShelfNum - totalDamagedNum - totalLackNum);
			} else {// 不存在则新增记录
				SaleOrderItemModel saleItemModel = saleOrderItemDao.searchById(item.getSaleItemId());

				ssDTO.setBarcode(item.getBarcode());
				ssDTO.setDamagedNum(0);
				ssDTO.setGoodsId(item.getGoodsId());
				ssDTO.setGoodsName(item.getGoodsName());
				ssDTO.setGoodsNo(item.getGoodsNo());
				ssDTO.setCommbarcode(item.getCommbarcode());
				Integer transferNumInt = item.getTransferNum() == null ? 0 : item.getTransferNum();
				Integer wornNumInt = item.getWornNum() == null ? 0 : item.getWornNum();
				ssDTO.setNum(transferNumInt + wornNumInt);
				ssDTO.setOrderId(saleId); // 销售订单ID
				ssDTO.setOrderType(DERP_ORDER.SALESHELF_ORDERTYPE_1); // 销售订单
				ssDTO.setShelfNum(0);
				ssDTO.setTallyingUnit(item.getTallyingUnit());
				Integer totalDamagedNum = 0;
				if (goodsDamagedMap.containsKey(item.getSaleItemId())) {
					totalDamagedNum = goodsDamagedMap.get(item.getSaleItemId());
				}
				ssDTO.setTotalDamagedNum(totalDamagedNum);
				Integer totalShelfNum = 0;
				if (goodsShelfMap.containsKey(item.getSaleItemId())) {
					totalShelfNum = goodsShelfMap.get(item.getSaleItemId());
				}
				ssDTO.setTotalShelfNum(totalShelfNum);
				// 少货数量
				Integer totalLackNum = 0;
				if (goodsLackMap.containsKey(item.getSaleItemId())) {
					totalLackNum = goodsLackMap.get(item.getSaleItemId());
				}
				ssDTO.setTotalLackNum(totalLackNum);
				ssDTO.setLackNum(0);
				ssDTO.setPrice(saleItemModel.getPrice());
				ssDTO.setSaleItemId(saleItemModel.getId());

				Integer outNum = transferNumInt + wornNumInt;
				ssDTO.setStayShelfNum(outNum - totalShelfNum - totalDamagedNum - totalLackNum);
			}
			ssModelMap.put(item.getSaleItemId() + "_" + item.getTallyingUnit(), ssDTO);
		}
		for (Map.Entry<String, SaleShelfDTO> entry : ssModelMap.entrySet()) {
			list.add(entry.getValue());
		}
		resultMap.put("shelfList", list);
		resultMap.put("saleDeclareOrderCode", saleOutDepotModel.getSaleDeclareOrderCode());
		return resultMap;
	}

	/**
	 * 销售上架-页面
	 *
	 * @param json
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveSaleShelf(ShelfDTO dto, User user) throws Exception {
		// 查询销售订单
		SaleOrderModel saleOrderModel = new SaleOrderModel();
		saleOrderModel.setCode(dto.getSaleOrderCode());
		saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
		if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
			throw new RuntimeException("销售订单红冲状态不为空，不允许上架");
		}

		// 查询销售出库单
		SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
		saleOutDepotModel.setCode(dto.getSaleOutDepotCode());
		saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);

		// 上架日期不能大于当前日期
		Date nowDate = TimeUtils.parseDay(TimeUtils.formatDay());
		if (dto.getShelfDate().getTime() > nowDate.getTime()) {
			throw new RuntimeException("上架日期不能大于当前日期");
		}
		// 上架日期要大于等于出库日期
		String deliverStr = TimeUtils.formatDay(saleOutDepotModel.getDeliverDate());
		if (dto.getShelfDate().getTime() < TimeUtils.parseDay(deliverStr).getTime()) {
			throw new RuntimeException("上架日期要大于等于出库日期");
		}

		// 获取最大的关账日/月结日期
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(saleOrderModel.getMerchantId());
		closeAccountsMongo.setDepotId(saleOrderModel.getOutDepotId());
		closeAccountsMongo.setBuId(saleOrderModel.getBuId());

		String maxdate = "";
		if (closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {// 查询关账日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
		} else if (closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null) {// 查询月结日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG2);
		} else if (closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null) {// 获取最大的关账日/月结日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
		}
		String maxCloseAccountsMonth = "";
		if (StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (StringUtils.isNotBlank(maxCloseAccountsMonth) && dto.getShelfDate() != null) {
			// 上架日期必须大于关账日期
			if (dto.getShelfDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new RuntimeException("上架日期必须大于关账日期/月结日期");
			}
		}

		// 生成上架单表头
		ShelfModel shelfModel = new ShelfModel();
		shelfModel.setCode(SmurfsUtils.getID(DERP.UNIQUE_ID_SJD));
		shelfModel.setSaleOrderCode(dto.getSaleOrderCode());
		shelfModel.setState(DERP_ORDER.SHELF_STATUS_1);
		shelfModel.setMerchantId(saleOrderModel.getMerchantId());
		shelfModel.setMerchantName(saleOrderModel.getMerchantName());
		shelfModel.setCustomerId(saleOrderModel.getCustomerId());
		shelfModel.setCustomerName(saleOrderModel.getCustomerName());
		shelfModel.setBuId(saleOrderModel.getBuId());
		shelfModel.setBuName(saleOrderModel.getBuName());
		shelfModel.setCurrency(saleOrderModel.getCurrency());
		shelfModel.setOperator(user.getName());
		shelfModel.setOperatorId(user.getId());
		shelfModel.setCreateDate(new Timestamp(System.currentTimeMillis()));
		shelfModel.setModifyDate(new Timestamp(System.currentTimeMillis()));
		shelfModel.setOutDepotId(saleOrderModel.getOutDepotId());
		shelfModel.setOutDepotName(saleOrderModel.getOutDepotName());
		shelfModel.setSaleOrderId(saleOrderModel.getId());
		shelfModel.setShelfDate(dto.getShelfDate());
		shelfModel.setPoNo(dto.getPoNo());
		shelfModel.setSaleOutDepotCode(dto.getSaleOutDepotCode());
		shelfModel.setSaleOutDepotId(saleOutDepotModel.getId());
		shelfDao.save(shelfModel);

		// 获取已上架的数据
		SaleShelfModel checkModel = new SaleShelfModel();
		checkModel.setSaleOutDepotId(saleOutDepotModel.getId());
		List<SaleShelfModel> shelfList = saleShelfDao.list(checkModel);
		// 统计销售订单商品已上架数量
		Integer saleShelfNum = 0;
		Map<Long, Map<String, Integer>> goodsShelfMap = new HashMap<>();
		for (SaleShelfModel ssModel : shelfList) {
			if(ssModel.getSaleItemId() == null){
				throw new RuntimeException("对应上架单的销售id为空，请及时维护");
			}
			Integer totalShelfNum = ssModel.getShelfNum() == null ? 0 : ssModel.getShelfNum();
			Integer totalDamagedNum = ssModel.getDamagedNum()== null ? 0 : ssModel.getDamagedNum();
			Integer totalLackNum = ssModel.getLackNum()== null ? 0 : ssModel.getLackNum();
			Map<String, Integer> shelMap = new HashMap<>();
			if (goodsShelfMap.containsKey(ssModel.getSaleItemId())) {
				shelMap = goodsShelfMap.get(ssModel.getSaleItemId());
				totalShelfNum += shelMap.get("totalShelfNum");
				totalDamagedNum += shelMap.get("totalDamagedNum");
				totalLackNum += shelMap.get("totalLackNum");
			}
			shelMap.put("totalShelfNum", totalShelfNum);
			shelMap.put("totalDamagedNum", totalDamagedNum);
			shelMap.put("totalLackNum", totalLackNum);
			goodsShelfMap.put(ssModel.getSaleItemId(), shelMap);

			saleShelfNum = saleShelfNum + totalShelfNum + totalDamagedNum + totalLackNum;
		}

		// 检查上架数量不能都为0
		boolean flag = true;// 是否都为0 默认true-是 false-否
		for (SaleShelfDTO saleShelfDTO : dto.getItemList()) {
			if (saleShelfDTO.getShelfNum() > 0 || saleShelfDTO.getDamagedNum() > 0 || saleShelfDTO.getLackNum() > 0) {
				flag = false;
				break;
			}
		}
		if (flag == true) {
			throw new RuntimeException("本次上架数量|本次残损数量|本次少货数量必须有一个大于0");
		}

		int total = 0;// 统计本次上架总量用于上架表头
		List<SaleShelfModel> addShelfList = new ArrayList<>();// 本次上架明细用于生成上架入库
		for (SaleShelfDTO saleShelfDTO : dto.getItemList()) {

			if (saleShelfDTO.getShelfNum() <= 0 && saleShelfDTO.getDamagedNum() <= 0 && saleShelfDTO.getLackNum() <= 0) {
				continue;
			}

			Map<String, Integer> shelMap = goodsShelfMap.get(saleShelfDTO.getSaleItemId());
			Integer totalShelfNum = shelMap == null ? 0 : shelMap.get("totalShelfNum");
			Integer totalDamagedNum = shelMap == null ? 0 : shelMap.get("totalDamagedNum");
			Integer totalLackNum = shelMap == null ? 0 : shelMap.get("totalLackNum");

			// 待上架量=出库量-上架数量-残损数量-少货数量
			Integer stayShelfNum = saleShelfDTO.getNum() - totalShelfNum - totalDamagedNum - totalLackNum;
			Integer bcNum = saleShelfDTO.getShelfNum() + saleShelfDTO.getDamagedNum() + saleShelfDTO.getLackNum();// 本次上架量
			if (bcNum > stayShelfNum) {
				throw new RuntimeException("上架数量+残损数量+少货数量不能大于待上架数量");
			}

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("merchandiseId", saleShelfDTO.getGoodsId());
			paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);	// 状态(1使用中,0已禁用)
			paramMap.put("merchantId", user.getMerchantId());
			MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(paramMap);

			// 组装销售上架信息表里的数据
			SaleShelfModel ssModel = new SaleShelfModel();
			ssModel.setShelfId(shelfModel.getId());
			ssModel.setBuId(saleOrderModel.getBuId());// 事业部
			ssModel.setBuName(saleOrderModel.getBuName());
			ssModel.setGoodsId(saleShelfDTO.getGoodsId());
			ssModel.setGoodsName(goods.getName());
			ssModel.setGoodsNo(goods.getGoodsNo());
			ssModel.setBarcode(goods.getBarcode());
			ssModel.setCommbarcode(goods.getCommbarcode());
			ssModel.setOrderId(saleOrderModel.getId());
			ssModel.setOrderType("1");
			ssModel.setNum(saleShelfDTO.getNum());// 出库量
			ssModel.setTotalShelfNum(totalShelfNum);// 已记上架量
			ssModel.setTotalDamagedNum(totalDamagedNum);// 已记残损量
			ssModel.setTotalLackNum(totalLackNum);// 已计缺货数量
			ssModel.setStayShelfNum(stayShelfNum);// 待上架量
			ssModel.setShelfNum(saleShelfDTO.getShelfNum());// 本次上架量
			ssModel.setDamagedNum(saleShelfDTO.getDamagedNum());// 本次残损量
			ssModel.setLackNum(saleShelfDTO.getLackNum());// 本次缺货数量
			ssModel.setTallyingUnit(saleOrderModel.getTallyingUnit());
			ssModel.setPoNo(dto.getPoNo());
			ssModel.setRemark(saleShelfDTO.getRemark());
			ssModel.setShelfDate(dto.getShelfDate());
			ssModel.setCreateDate(TimeUtils.getNow()); // 操作时间
			ssModel.setOperatorId(user.getId()); // 操作人ID
			ssModel.setOperator(user.getName()); // 操作人名字
			ssModel.setModifyDate(TimeUtils.getNow());
			ssModel.setSaleOutDepotId(dto.getSaleOutDepotId());
			ssModel.setSaleItemId(saleShelfDTO.getSaleItemId());
			saleShelfDao.save(ssModel);

			addShelfList.add(ssModel);
			// 用于计算上架总量
			total += bcNum;
		}
		shelfModel.setTotalShelfNum(total);
		shelfDao.modify(shelfModel);

		saleShelfNum = saleShelfNum+total;//已上架量+本次上架量

		/**
		 * 更新销售单、销售出库单状态----------------------------------------------------
		 * 1.出库单的每个商品都已全部上架==已上架，否则部分上架
		 */

		/**
		 * 销售出库单：
		 *	当前出库单的出库数量 != 当前上架数量，部分上架
		 *	当前出库单的出库数量 == 当前上架数量，已上架
		 *  上架数量 == 0，已出库
		 */
		//获取销售出库数量
		Integer totalOutNum = 0;//销售出库数量
		SaleOutDepotItemModel saleOutItemModel = new SaleOutDepotItemModel();
		saleOutItemModel.setSaleOutDepotId(saleOutDepotModel.getId());
		List<SaleOutDepotItemModel> saleOutItemList = saleOutDepotItemDao.list(saleOutItemModel);
		for(SaleOutDepotItemModel queryOutItemDTO : saleOutItemList) {
			Integer transferNum = queryOutItemDTO.getTransferNum()==null ? 0:queryOutItemDTO.getTransferNum();// 好品数量
			Integer wornNum = queryOutItemDTO.getWornNum()==null ? 0:queryOutItemDTO.getWornNum();	// 坏品数量
			totalOutNum =  totalOutNum + transferNum + wornNum;
		}

		String saleOutStatus = "";
		if(saleShelfNum.equals(totalOutNum)) {
			saleOutStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_026;//已上架
		}else if(saleShelfNum.equals(0)){
			saleOutStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_018;//已出库
		}else {
			saleOutStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_031;//部分上架
		}
		SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
		sOutDepotModel.setId(saleOutDepotModel.getId());
		sOutDepotModel.setStatus(saleOutStatus);
		saleOutDepotDao.modify(sOutDepotModel);

		/** 更新销售订单状态：
		 * 		有出库单，总出库数量！=总销售数量，部分出库；
		 * 		有出库单，没有上架单，总出库数量=总销售数量，已出库；
		 * 		有出库单，总出库数量=总销售数量，总上架数量！=总出库数量，部分上架；
		 * 		有出库单，总出库数量=总销售数量，总上架数量=总出库数量，已上架
		 */
		String saleStatus = "";
		Map<String,Object> queryMap = new HashMap<>();
		queryMap.put("code",saleOrderModel.getCode());
		List<Map<String, Object>> goodsNotOutNumList = saleOutDepotDao.getGoodsNotOutNumList(queryMap);
		boolean allOut = true;
		for (Map<String, Object> goodsMap : goodsNotOutNumList) {
			BigDecimal diffnumbg = (BigDecimal) goodsMap.get("diffnum");
			if (diffnumbg.intValue() > 0) {
				allOut = false;// 存在未出库的量
				break;
			}
		}

		Integer noShelfNum = 0;
		boolean allShelf = true;
		List<Map<String, Object>> goodsNotShelNumList = saleOutDepotDao.getGoodsNotShelNumList(queryMap);
		// 遍历出库单商品未上架量，若全部商品未上架量都为0则销售单状态为已上架，若存在未上量大于0的商品则销售单状态为部分上架
		for (Map<String, Object> goodsMap : goodsNotShelNumList) {
			BigDecimal diffnumbg = (BigDecimal) goodsMap.get("diffnum");
			if (diffnumbg.intValue() > 0) {
				// 存在未上架的量
				noShelfNum = noShelfNum + diffnumbg.intValue();
				allShelf = false;
			}
		}
		//销售订单状态为 已出库（完结出库的情况，销售量！=出库量，但是已经完结出库了），意味着不允许再出库，此时只需要判断 上架和、部分上架状态
		if(saleOrderModel.getState().equals(DERP_ORDER.SALEORDER_STATE_018)|| saleOrderModel.getState().equals(DERP_ORDER.SALEORDER_STATE_031)) {
			if(allShelf) {
				saleStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_026;//已上架
			}else{
				saleStatus = DERP_ORDER.SALEORDER_STATE_031;//部分上架
			}
		}else {
			if(allOut){
				if(allShelf) {
					saleStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_026;//已上架
				}else{
					saleStatus = DERP_ORDER.SALEORDER_STATE_031;//部分上架
				}
			}else {
				//存在未出库的量，部分出库
				saleStatus = DERP_ORDER.SALEORDER_STATE_019;//部分出库
			}
		}

		SaleOrderModel saleOrderUpate = new SaleOrderModel();
		saleOrderUpate.setId(saleOrderModel.getId());
		saleOrderUpate.setState(saleStatus);
		saleOrderUpate.setModifyDate(TimeUtils.getNow());
		saleOrderUpate.setModifier(user.getId());
		saleOrderUpate.setModifierUsername(user.getName());
		saleOrderDao.modify(saleOrderUpate);

		if (allOut == true && saleOrderUpate.getState().equals(DERP_ORDER.SALEORDER_STATE_026)) {
			// 更新差异分析表状态

			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
			saleAnalysisModel.setOrderId(saleOrderModel.getId());
			List<SaleAnalysisModel> saleAnalysisList = saleAnalysisDao.list(saleAnalysisModel);
			for (SaleAnalysisModel saleAnalysis : saleAnalysisList) {
				//已出库数量
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("saleItemId",saleAnalysis.getSaleItemId());
				Integer totalOutDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(paramMap);

				saleAnalysis.setIsEnd("1");
				saleAnalysis.setEndDate(TimeUtils.getNow());
				saleAnalysis.setSurplus(saleAnalysis.getSaleNum() - totalOutDepotNum);
				saleAnalysisDao.modify(saleAnalysis);
			}
		}
		//销售预申报出库
		if(saleOutDepotModel.getSaleDeclareOrderId() != null) {
			//获取预申报全部出库单
			SaleOutDepotDTO declareOutDepotDTO = new SaleOutDepotDTO();
			declareOutDepotDTO.setSaleDeclareOrderId(saleOutDepotModel.getSaleDeclareOrderId());
			List<SaleOutDepotDTO> declareOutDepotList = saleOutDepotDao.listSaleOutDepot(declareOutDepotDTO);
			if(declareOutDepotList != null && declareOutDepotList.size() > 0) {
				String declareStatus = DERP_ORDER.SALEDECLARE_STATUS_007;//已上架
				//所有出库单“已上架”，预申报单状态“已上架”；否则“部分上架”
				for(SaleOutDepotDTO declareOutDepot: declareOutDepotList) {
					if(!DERP_ORDER.SALEOUTDEPOT_STATUS_026.equals(declareOutDepot.getStatus())) {
						declareStatus = DERP_ORDER.SALEDECLARE_STATUS_005;//部分上架
						break;
					}
				}

				SaleDeclareOrderModel declareModel = new SaleDeclareOrderModel();
				declareModel.setId(saleOutDepotModel.getSaleDeclareOrderId());
				declareModel.setStatus(declareStatus);

				SaleDeclareOrderModel queryDeclareModel = saleDeclareOrderDao.searchById(saleOutDepotModel.getSaleDeclareOrderId());
				if(DERP_ORDER.SALEDECLARE_STATUS_004.equals(queryDeclareModel.getStatus()) && DERP_ORDER.SALEDECLARE_STATUS_005.equals(declareStatus)) {
					//当前预申报状态为【已出库】，需要更新为【部分上架】，记录第一次上架日期
					declareModel.setFirstShelfDate(dto.getShelfDate());

				}else if(DERP_ORDER.SALEDECLARE_STATUS_005.equals(queryDeclareModel.getStatus()) && DERP_ORDER.SALEDECLARE_STATUS_007.equals(declareStatus)) {
					//当前预申报状态为【部分上架】，需要更新为【已上架】，记录完结上架日期
					declareModel.setEndShelfDate(dto.getShelfDate());
				}
				else if(DERP_ORDER.SALEDECLARE_STATUS_004.equals(queryDeclareModel.getStatus()) && DERP_ORDER.SALEDECLARE_STATUS_007.equals(declareStatus)) {
					//当前预申报状态为【已出库】，需要更新为【已上架】，记录首次上架日期和完结上架日期
					declareModel.setFirstShelfDate(dto.getShelfDate());
					declareModel.setEndShelfDate(dto.getShelfDate());
				}
				saleDeclareOrderDao.modify(declareModel);
				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, saleOutDepotModel.getSaleDeclareOrderCode(), "上架", null, null);
				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrderModel.getCode(), "预申报上架", null, null);

			}
		}else {
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrderModel.getCode(), "页面上架", null, null);
		}

		/**
		 * --------------------------生成上架入库单-----------------------------------------------
		 */
		boolean idepotFlag = false;// 是否生成上架入库单标识 true-生成 false-不生成
		// 4.购销-实销实结 或销售类型为购销-整批结算且入库仓库为代客管理的仓库 并且上架量/损货量大于0则生成上架入库单
		if (DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderModel.getBusinessModel())) {
			for (SaleShelfModel shelItem : addShelfList) {
				if (shelItem.getShelfNum().intValue() > 0 || shelItem.getDamagedNum().intValue() > 0) {
					idepotFlag = true;
					break;
				}
			}
		} else if (saleOrderModel.getInDepotId() != null
				&& DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderModel.getBusinessModel())) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", saleOrderModel.getInDepotId());
			DepotInfoMongo inDepot = depotInfoMongoDao.findOne(params);// 入库仓
			// 入库仓库是否为代客管理仓
			if (DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_1.equals(inDepot.getIsValetManage())) {
				for (SaleShelfModel shelItem : addShelfList) {
					if (shelItem.getShelfNum().intValue() > 0 || shelItem.getDamagedNum().intValue() > 0) {
						idepotFlag = true;
						break;
					}
				}
			}

		}

		if (idepotFlag == true) {
			InvetAddOrSubtractRootJson subtractRootJson = saveShelfIdepot(shelfModel, saleOrderModel, user,
					addShelfList);
			String rootjson = JSONObject.fromObject(subtractRootJson).toString();
			System.out.println("报文:" + rootjson);
			SendResult sendResult = rocketMQProducer.send(rootjson,
					MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
					MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
			System.out.println("发送结果:" + sendResult);
		}

		// 生成销售SD单
		generateSaleSdOrder(shelfModel, addShelfList, user);

		// 发送邮件
		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
		mailDTO.setBusinessModuleType("3");
		mailDTO.setTypeName("销售");
		mailDTO.setType("3");// 1：提交 2：审核 3：上架 4：核销 5：开票 6：作废审核 7：盖章发票 8：审核完毕 9：作废完成 10：金额修改 11：金额确认
		mailDTO.setMerchantId(saleOrderModel.getMerchantId());
		mailDTO.setMerchantName(saleOrderModel.getMerchantName());
		mailDTO.setBuId(saleOrderModel.getBuId());
		mailDTO.setBuName(saleOrderModel.getBuName());
		mailDTO.setSupplier(saleOrderModel.getCustomerName());
		mailDTO.setOrderCode(saleOrderModel.getCode());
		mailDTO.setPoNum(saleOrderModel.getPoNo());
		mailDTO.setStatus("金额未确认");
		mailDTO.setSubmitId(Arrays.asList(String.valueOf(saleOrderModel.getSubmiter())));
		mailDTO.setAuditorId(saleOrderModel.getAuditor());
		mailDTO.setReviewerId(saleOrderModel.getAmountConfirmer());
		mailDTO.setModifyId(saleOrderModel.getAdjuster());
		mailDTO.setShelvesId(user.getId());
		mailDTO.setUserName(user.getName());

		List<String> userIds = new ArrayList<String>();
		userIds.add(String.valueOf(user.getId()));
		mailDTO.setUserId(userIds);
		try {
			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
					MQErpEnum.SEND_REMINDER_MAIL.getTags());
		} catch (Exception e) {
			LOGGER.error("----------------------销售单[" + saleOrderModel.getCode() + "]上架操作发送邮件失败----------------------");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", saleOrderUpate.getId());
		map.put("state", sOutDepotModel.getStatus());
		map.put("shelfCode", shelfModel.getCode());

		return map;
	}

	@Override
	public SaleShelfModel listSaleShelfByPage(SaleShelfModel model) throws SQLException {
		return saleShelfDao.searchByPage(model);
	}

	@Override
	public boolean shelfIsEnd(Long id) throws Exception {
		SaleOrderModel saleOrderModel =  saleOrderDao.searchById(id);
		if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
			throw new RuntimeException("销售订单红冲状态不为空，不允许上架");
		}
		// 获取上架数据
		SaleShelfModel saleShelfModel = new SaleShelfModel();
		saleShelfModel.setOrderId(id);
		List<SaleShelfModel> saleShelfList = saleShelfDao.list(saleShelfModel);
		/*
		 * 遍历计算商品已上架、已计入残损的数据
		 */
		Map<Long, Integer> goodsShelfMap = new HashMap<Long, Integer>();// 商品对应的上架+残损的数量
		for (SaleShelfModel ssModel : saleShelfList) {
			if(ssModel.getSaleItemId() == null){
				throw new RuntimeException("对应上架单的销售明细id为空，请及时维护");
			}
			Integer shelfNum = ssModel.getShelfNum();
			if (goodsShelfMap.containsKey(ssModel.getSaleItemId())) {
				shelfNum += goodsShelfMap.get(ssModel.getSaleItemId());
			}
			Integer damagedNum = ssModel.getDamagedNum();
			Integer lackNum = ssModel.getLackNum();// 少货数量
			goodsShelfMap.put(ssModel.getSaleItemId(), shelfNum + damagedNum + lackNum);
		}

		// 获取销售订单商品数量
		SaleOrderItemModel itemModel = new SaleOrderItemModel();
		itemModel.setOrderId(id);
		List<SaleOrderItemModel> itemList = saleOrderItemDao.list(itemModel);
		Integer differentNum = null;// 差异量
		String isNotAllShelf = "1";// 0 未完全上架,1 完全上架
		// 销售商品信息匹配上架数据，计算差异量
		for (SaleOrderItemModel item : itemList) {
			if (!goodsShelfMap.containsKey(item.getId())) {// 存在未上架的商品
				isNotAllShelf = "0";
				break;
			}
			Integer shelfNum = goodsShelfMap.get(item.getId());
			differentNum = item.getNum() - shelfNum;
			if (differentNum <= 0) {// 上架数量大于出库数量（后面不限制上架数量时用）
				// differentNum = 0;
				isNotAllShelf = "1";
			} else {// 有一个没完全上架就结束循环
				isNotAllShelf = "0";
				break;
			}
		}
		// 完全上架 返回的是true
		return "1".equals(isNotAllShelf);
	}

	/**
	 * 销售订单审核逻辑
	 *
	 * @param saleOrder
	 * @param userId
	 * @param name
	 * @param merchantId
	 * @param topidealCode
	 * @param merchantName
	 * @return
	 * @throws Exception
	 */
	private String auditSales(SaleOrderModel saleOrder, Long userId, String name, Long merchantId, String topidealCode,
			String merchantName) throws Exception {
		// todo:本周不上线
		// 服务类型 E0：区内调拨调出服务 F0：区内调拨调入服务 G0：库内调拨
		Map<String, String> ServeTypesMap = new HashMap<>();
		ServeTypesMap.put("E0", "01");
		ServeTypesMap.put("F0", "F0");
		ServeTypesMap.put("G0", "02");
		// 业务场景10：账册内调仓,20：账册内货号变更,30：账册内货号变更调仓,40：账册内货权转移,50：账册内货权转移调仓,60：区内跨海关账册调入,70：区内跨海关账册调出,80：非实物调拨
		Map<String, String> busiSceneMap = new HashMap<>();
		busiSceneMap.put("10", "05");
		busiSceneMap.put("20", "20");
		busiSceneMap.put("30", "30");
		busiSceneMap.put("40", "04");
		busiSceneMap.put("50", "02");
		busiSceneMap.put("60", "60");
		busiSceneMap.put("70", "70");
		busiSceneMap.put("80", "80");

		String msg = "";

		// 出库仓
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depotId", saleOrder.getOutDepotId());
		DepotInfoMongo depot = depotInfoMongoDao.findOne(params);

		// 仓库公司关联表
		Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
		depotMerchantRelParam.put("merchantId", merchantId);
		depotMerchantRelParam.put("depotId", saleOrder.getOutDepotId());
		DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
		if (depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)) {
			msg += "单号：" + saleOrder.getCode() + ",仓库ID为：" + saleOrder.getOutDepotId() + ",未查到该公司下调出仓库信息,审核失败\n";
			return msg;
		}

		// 获取销售表体（商品信息）
		SaleOrderItemModel saleOrderItem = new SaleOrderItemModel();
		saleOrderItem.setOrderId(saleOrder.getId());
		List<SaleOrderItemModel> itemList = saleOrderItemDao.list(saleOrderItem);

		// 销售类型为购销-整批结算或购销-实销实结 且出库仓不为中转仓，推库存冻结
		if (!DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
			if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrder.getBusinessModel())
					|| DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrder.getBusinessModel())) {
				// 冻结库存
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now = sdf.format(new Date());
				InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
				inventoryFreezeRootJson.setMerchantId(saleOrder.getMerchantId().toString());
				inventoryFreezeRootJson.setMerchantName(saleOrder.getMerchantName());
				inventoryFreezeRootJson.setDepotId(saleOrder.getOutDepotId().toString());
				inventoryFreezeRootJson.setDepotName(saleOrder.getOutDepotName());
				inventoryFreezeRootJson.setOrderNo(saleOrder.getCode());
				inventoryFreezeRootJson.setBusinessNo(saleOrder.getCode());
				// inventoryFreezeRootJson.setSource(SourceStatusEnum.XSDD.getKey());
				inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
				// inventoryFreezeRootJson.setSourceType(InventoryStatusEnum.XSDD.getKey());
				inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_002);
				inventoryFreezeRootJson.setSourceDate(now);
				inventoryFreezeRootJson.setOperateType("0");
				List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
				for (SaleOrderItemModel item : itemList) {
					InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();

					inventoryFreezeGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
					inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());
					inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());


					inventoryFreezeGoodsListJson.setDivergenceDate(now);
					inventoryFreezeGoodsListJson.setNum(item.getNum());
					// 海外仓冻结加理货单位
					if ("c".equals(depot.getType())) {
						if ("00".equals(saleOrder.getTallyingUnit())) {
							inventoryFreezeGoodsListJson.setUnit("0");
						} else if ("01".equals(saleOrder.getTallyingUnit())) {
							inventoryFreezeGoodsListJson.setUnit("1");
						} else if ("02".equals(saleOrder.getTallyingUnit())) {
							inventoryFreezeGoodsListJson.setUnit("2");
						}
					}
					inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
				}
				inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
				rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(),
						MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),
						MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
			}
		}
		// 预售转销后审核销售订单》往预售勾稽明细表插入销售数量
		// 判断该销售单是否是标识为预售转销(若有同个商品存在多个预售单中，按审核时间分配)
		Long preSaleOrderCorrelationResult = null;
		if (DERP_ORDER.SALEORDER_ORDERTYPE_1.equals(saleOrder.getOrderType())) {
			preSaleOrderCorrelationResult = saveCorrelationForAudit(saleOrder, itemList, userId, name);
			if (preSaleOrderCorrelationResult == null) {
				msg += "单号：" + saleOrder.getCode() + ",审核出错\n";
			}
		}

		// 修改单据状态
		saleOrder.setState(DERP_ORDER.SALEORDER_STATE_003);
		saleOrder.setAuditDate(TimeUtils.getNow());
		saleOrder.setAuditor(userId);
		saleOrder.setAuditName(name);
		saleOrder.setModifyDate(TimeUtils.getNow());
		saleOrder.setModifier(userId);
		saleOrder.setModifierUsername(name);
		int flag = saleOrderDao.modify(saleOrder);

		if (flag == 0) {
			msg += "单号：" + saleOrder.getCode() + ",审核出错\n";
		}
		return msg;
	}

	/**
	 * 预售转销后审核销售订单》往预售勾稽明细表插入销售数量
	 *
	 * @param saleOrder
	 * @param itemList
	 * @return
	 * @throws Exception
	 */
	private Long saveCorrelationForAudit(SaleOrderModel saleOrder, List<SaleOrderItemModel> itemList, Long userId,
			String name) throws Exception {
		Long preSaleOrderCorrelationResult = null; // 标识是否成功
		// 判断该销售单是否是标识为预售转销(若有同个商品存在多个预售单中，按审核时间分配)
		String[] preSaleOrderRelCodeStr = saleOrder.getPreSaleOrderRelCode().split(",");
		List preSaleOrderRelCodeList = new ArrayList();
		for (int m = 0; m < preSaleOrderRelCodeStr.length; m++) {
			preSaleOrderRelCodeList.add(preSaleOrderRelCodeStr[m]);
		}
		// 先去校验某个商品在
		for (int i = 0; i < itemList.size(); i++) {
			Integer preNumInt = preSaleOrderCorrelationDao.getPreNumByPreSaleId(preSaleOrderRelCodeList,
					itemList.get(i).getGoodsId());
			Integer preNum = preNumInt == null ? 0 : preNumInt;
			Integer saleNumInt = preSaleOrderCorrelationDao.getSaleNumByPreSaleId(preSaleOrderRelCodeList,
					itemList.get(i).getGoodsId());
			Integer saleNum = saleNumInt == null ? 0 : saleNumInt;
			Integer stayNum = preNum - saleNum;
			if (stayNum < itemList.get(i).getNum()) {
				throw new RuntimeException("审核失败，商品货号" + itemList.get(i).getGoodsNo() + "待销量不足");
			}
		}

		// 根据审核时间排序预售单
		List<PreSaleOrderDTO> preSaleOrderModelList = preSaleOrderDao.listPreSaleByAuditDate(preSaleOrderRelCodeList);
		for (int i = 0; i < itemList.size(); i++) {
			SaleOrderItemModel saleItemModel = itemList.get(i);
			int wfpNum = 0; // 未分配完的数量
			for (int j = 0; j < preSaleOrderModelList.size(); j++) {
				PreSaleOrderDTO preSaleOrderDTO = preSaleOrderModelList.get(j);
				// 往预售勾稽明细表插入销售数量
				PreSaleOrderCorrelationModel correlationModel = new PreSaleOrderCorrelationModel();
				correlationModel.setMerchantId(preSaleOrderDTO.getMerchantId());
				correlationModel.setMerchantName(preSaleOrderDTO.getMerchantName());
				correlationModel.setPreSaleOrderId(preSaleOrderDTO.getId());
				correlationModel.setPreSaleOrderCode(preSaleOrderDTO.getCode());
				correlationModel.setSaleOrderCode(saleOrder.getCode());// 销售单单号
				correlationModel.setGoodsId(saleItemModel.getGoodsId());
				correlationModel.setGoodsName(saleItemModel.getGoodsName());
				correlationModel.setGoodsNo(saleItemModel.getGoodsNo());
				correlationModel.setGoodsCode(saleItemModel.getGoodsCode());
				correlationModel.setBarcode(saleItemModel.getBarcode());
				correlationModel.setCommbarcode(saleItemModel.getCommbarcode());
				correlationModel.setCreateDate(TimeUtils.getNow());
				correlationModel.setAuditDate(TimeUtils.getNow());
				correlationModel.setAuditName(name);
				correlationModel.setAuditor(userId);

				// 查询某个预售单下是否有该商品
				PreSaleOrderCorrelationModel paramCorrelation = new PreSaleOrderCorrelationModel();
				paramCorrelation.setGoodsId(saleItemModel.getGoodsId());
				paramCorrelation.setPreSaleOrderCode(preSaleOrderDTO.getCode());
				List<PreSaleOrderCorrelationModel> result = preSaleOrderCorrelationDao.list(paramCorrelation);
				// 该预售单下没有该商品，循环下一个预售单
				if (result == null || result.size() == 0) {
					continue;// 遍历下一个预售单的该商品
				}
				// 某个预售单的某个商品的预售数量
				Integer preNumInt = preSaleOrderCorrelationDao
						.getPreNumByPreSaleId(Arrays.asList(preSaleOrderDTO.getCode()), saleItemModel.getGoodsId());
				Integer preNum = preNumInt == null ? 0 : preNumInt;
				// 某个预售单的某个商品的该预售单已销售量
				Integer saleNumInt = preSaleOrderCorrelationDao
						.getSaleNumByPreSaleId(Arrays.asList(preSaleOrderDTO.getCode()), saleItemModel.getGoodsId());
				Integer saleNum = saleNumInt == null ? 0 : saleNumInt;
				// 待销量
				Integer staySaleNum = preNum - saleNum;
				int subNum = 0;
				int bcSaleNum = 0;// 插入勾稽表销售量
				if (wfpNum != 0) { // 若上个商品在上个预售单未分配完，则继续分配
					subNum = staySaleNum - wfpNum;// 待销量-上个预售单未分配完的数量（往勾稽表插入的销售量）
					bcSaleNum = wfpNum;
				} else {
					subNum = staySaleNum - saleItemModel.getNum();// 待销量-本次销售量（往勾稽表插入的销售量）
					bcSaleNum = saleItemModel.getNum();
				}

				if (subNum < 0) { // 分配不足
					wfpNum = Math.abs(subNum);// 未分配完的量（取绝对值）
					correlationModel.setSaleNum(staySaleNum);// 销售量
					preSaleOrderCorrelationResult = preSaleOrderCorrelationDao.save(correlationModel);
					continue;// 遍历下一个预售单商品
				} else if (subNum == 0) { // 刚好分配完
					correlationModel.setSaleNum(staySaleNum);// 销售量
					preSaleOrderCorrelationResult = preSaleOrderCorrelationDao.save(correlationModel);
					break;// 销售单下一个商品
				} else { // 足够分配
					correlationModel.setSaleNum(bcSaleNum);// 销售量
					preSaleOrderCorrelationResult = preSaleOrderCorrelationDao.save(correlationModel);
					break;// 销售单下一个商品
				}
			}
		}
		// 遍历该销售单的预售单，检验某个预售单下商品是否待销量全为0，改状态为已完结
		for (int i = 0; i < preSaleOrderModelList.size(); i++) {
			PreSaleOrderItemModel preSaleOrderItemModel = new PreSaleOrderItemModel();
			preSaleOrderItemModel.setOrderId(preSaleOrderModelList.get(i).getId());
			List<PreSaleOrderItemModel> preSaleOrderItemList = preSaleOrderItemDao.list(preSaleOrderItemModel);
			int isZeroNum = 0;// 记录某个预售单下所有商品是否待销量全为0
			for (int j = 0; j < preSaleOrderItemList.size(); j++) {
				// 某个预售单的某个商品的预售数量
				Integer preNumInt = preSaleOrderCorrelationDao.getPreNumByPreSaleId(
						Arrays.asList(preSaleOrderModelList.get(i).getCode()),
						preSaleOrderItemList.get(j).getGoodsId());
				Integer preNum = preNumInt == null ? 0 : preNumInt;
				// 某个预售单的某个商品的该预售单已销售量
				Integer saleNumInt = preSaleOrderCorrelationDao.getSaleNumByPreSaleId(
						Arrays.asList(preSaleOrderModelList.get(i).getCode()),
						preSaleOrderItemList.get(j).getGoodsId());
				Integer saleNum = saleNumInt == null ? 0 : saleNumInt;
				// 待销量
				Integer staySaleNum = preNum - saleNum;
				if (staySaleNum == 0) {
					isZeroNum = isZeroNum + 1;
				}
				if (isZeroNum == preSaleOrderItemList.size()) {
					// 改状态为已完结
					PreSaleOrderModel model = new PreSaleOrderModel();
					model.setId(preSaleOrderModelList.get(i).getId());
					model.setState(DERP_ORDER.PRESALEORDER_STATE_007);// 已完结
					preSaleOrderDao.modify(model);
				}
			}
		}
		return preSaleOrderCorrelationResult;
	}

	/**
	 * 采销执行审批
	 *
	 * @param userId
	 * @param name
	 * @param topidealCode
	 * @return
	 * @throws Exception
	 */
	@Override
	public String auditPurchase(SaleOrderModel model, Long userId, String name, String topidealCode) throws Exception {

		String msg = "";
		SaleOrderModel saleOrder = new SaleOrderModel();
		if (model.getId() != null) {
			saleOrder = saleOrderDao.searchByModel(model);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("supplierId", saleOrder.getSupplierId());
		params.put("customerId", saleOrder.getCustomerId());
		params.put("merchantId", saleOrder.getMerchantId());

		PurchaseCommissionMongo purchaseCommissionMongo = purchaseCommissionMongoDao.findOne(params);
		if (purchaseCommissionMongo == null) {
			msg = "供应商ID：" + saleOrder.getSupplierId() + ",客户ID:" + saleOrder.getCustomerId() + "在采销执行佣金配置表没找到对应信息\n";
			return msg;
		}
		// 获取销售表体（商品信息）
		SaleOrderItemModel saleOrderItem = new SaleOrderItemModel();
		saleOrderItem.setOrderId(saleOrder.getId());
		List<SaleOrderItemModel> itemList = saleOrderItemDao.list(saleOrderItem);

		double totalAmount = 0.0;
		for (SaleOrderItemModel item : itemList) {
			// 修改商品销售单价以及总金额
			BigDecimal price = item.getPrice();
			Integer itemNum = item.getNum();
			// 销售单价
			double priceDouble = price.doubleValue() * (1 - purchaseCommissionMongo.getSaleRebate());
			price = new BigDecimal(priceDouble);
			// 销售总金额
			double amountDouble = priceDouble * itemNum.intValue();

			item.setPrice(price);
			item.setAmount(new BigDecimal(amountDouble));

			saleOrderItemDao.modify(item);

			totalAmount += amountDouble;

		}

		// 修改单据状态
		saleOrder.setTotalAmount(new BigDecimal(totalAmount));
		saleOrder.setState(DERP_ORDER.SALEORDER_STATE_003);
		saleOrder.setAuditDate(TimeUtils.getNow());
		saleOrder.setAuditor(userId);
		saleOrder.setAuditName(name);
		saleOrder.setModifyDate(TimeUtils.getNow());
		saleOrder.setModifier(userId);
		saleOrder.setModifierUsername(name);
		saleOrderDao.modify(saleOrder);

		// 自动生成采购订单，自动生成的采购订单状态为“已审核”
		PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();

//		purchaseOrderModel.setCode(CodeGeneratorUtil.getNo("CGO"));
		purchaseOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGO));
		purchaseOrderModel.setSupplierId(purchaseCommissionMongo.getSupplierId());
		purchaseOrderModel.setSupplierName(purchaseCommissionMongo.getSupplierName());
		purchaseOrderModel.setCurrency(saleOrder.getCurrency());
		//purchaseOrderModel.setPaySubject(saleOrder.getPaySubject());
		purchaseOrderModel.setBusinessModel(saleOrder.getBusinessModel());
		purchaseOrderModel.setDepotId(saleOrder.getOutDepotId());
		purchaseOrderModel.setDepotName(saleOrder.getOutDepotName());
		purchaseOrderModel.setPoNo(saleOrder.getPoNo());
		purchaseOrderModel.setMerchantId(saleOrder.getMerchantId());
		purchaseOrderModel.setMerchantName(saleOrder.getMerchantName());
		purchaseOrderModel.setStatus(DERP_ORDER.SALEORDER_STATE_003);
		purchaseOrderModel.setCreateDate(TimeUtils.getNow());
		purchaseOrderModel.setCreater(userId);

		Long id = purchaseOrderDao.save(purchaseOrderModel);
		if (id != null) {
			// 新增表体
			for (SaleOrderItemModel item : itemList) {

				Map<String, Object> queryParams = new HashMap<String, Object>();
				queryParams.put("merchandiseId", item.getGoodsId());
				MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(queryParams);

				PurchaseOrderItemModel purchaseOrderItemModel = new PurchaseOrderItemModel();
				purchaseOrderItemModel.setPurchaseOrderId(id);
				purchaseOrderItemModel.setGoodsCode(item.getGoodsCode());
				purchaseOrderItemModel.setGoodsName(item.getGoodsName());
				purchaseOrderItemModel.setGoodsNo(item.getGoodsNo());
				purchaseOrderItemModel.setGoodsId(item.getGoodsId());
				purchaseOrderItemModel.setBarcode(item.getBarcode());

				// 采购数量
				int purchaseNum = item.getNum();
				// 转换采购单价
				double price = item.getPrice().doubleValue();
				double purchasePrice = price / (1 + purchaseCommissionMongo.getPurchaseCommission());
				// 小数位后3位舍弃
				BigDecimal purchasePriceBd = new BigDecimal(purchasePrice).setScale(3, RoundingMode.DOWN);

				purchaseOrderItemModel.setNum(purchaseNum);
				purchaseOrderItemModel.setPrice(purchasePriceBd);
				purchaseOrderItemModel.setAmount(new BigDecimal(purchasePrice * purchaseNum));

				purchaseOrderItemModel.setCreateDate(TimeUtils.getNow());
				purchaseOrderItemModel.setCreater(userId);// 创建人id
				purchaseOrderItemModel.setCreateName(name);// 创建人用户名

				purchaseOrderItemModel.setGrossWeight(mogo.getGrossWeight()); // 毛重
				purchaseOrderItemModel.setNetWeight(mogo.getNetWeight()); // 净重
				purchaseOrderItemModel.setGrossWeightSum(mogo.getGrossWeight() * purchaseNum); // 总毛重
				purchaseOrderItemModel.setNetWeightSum(mogo.getNetWeight() * purchaseNum); // 总净重
				purchaseOrderItemModel.setConstituentRatio(mogo.getComponent()); // 成分百分比
				purchaseOrderItemModel.setBrandName(item.getBrandName()); // 品牌

				purchaseOrderItemDao.save(purchaseOrderItemModel);
			}

		} else {
			msg = "单号：" + saleOrder.getCode() + "，自动生成采购单失败\n";
			return msg;
		}

		purchaseOrderModel = new PurchaseOrderModel();
		purchaseOrderModel.setId(id);
		purchaseOrderModel = purchaseOrderDao.getDetails(purchaseOrderModel);

		Map<String, Object> depotInfo = new HashMap<String, Object>();
		depotInfo.put("depotId", saleOrder.getOutDepotId());
		DepotInfoMongo depotInfoMongo=depotInfoMongoDao.findOne(depotInfo);


		// 添加采购入库单
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String now = sdf.format(new Date());
		PurchaseWarehouseModel purchaseWarehouse = new PurchaseWarehouseModel();
//		purchaseWarehouse.setCode(CodeGeneratorUtil.getNo("CGRK"));// 编码
		purchaseWarehouse.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGRK));// 编码
		purchaseWarehouse.setDepotId(purchaseOrderModel.getDepotId());// 仓库id
		purchaseWarehouse.setDepotName(purchaseOrderModel.getDepotName());// 仓库名称
		purchaseWarehouse.setCreater(userId);// 创建人
		purchaseWarehouse.setMerchantId(purchaseOrderModel.getMerchantId());// 公司id
		purchaseWarehouse.setMerchantName(purchaseOrderModel.getMerchantName());// 公司名称
		//purchaseWarehouse.setLbxNo(purchaseOrderModel.getLbxNo());// lbx单号
		purchaseWarehouse.setCrossStatus("1");// 采购勾稽状态 是
		purchaseWarehouse.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028);// 入库中
		purchaseWarehouse.setInboundDate(Timestamp.valueOf(now));
		purchaseWarehouse.setContractNo(purchaseOrderModel.getContractNo());// 合同号
		purchaseWarehouse.setBusinessModel(purchaseOrderModel.getBusinessModel()); // 设置业务

		if(depotInfoMongo!=null){
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())){
				purchaseWarehouse.setTallyingUnit(saleOrder.getTallyingUnit());;// 海外仓理货单位
			}
		}

		Long result_id = purchaseWarehouseDao.save(purchaseWarehouse);
		if (result_id != null) {
			for (PurchaseOrderItemModel item : purchaseOrderModel.getItemList()) {
				// 添加表体
				PurchaseWarehouseItemModel itemModel = new PurchaseWarehouseItemModel();
				itemModel.setWarehouseId(result_id);// 采购入库单id
				itemModel.setGoodsId(item.getGoodsId());// 商品id
				itemModel.setGoodsNo(item.getGoodsNo());// 商品货号
				itemModel.setGoodsName(item.getGoodsName());// 商品名称
				itemModel.setBarcode(item.getBarcode());// 条形码
				itemModel.setGrossWeight(item.getGrossWeight());// 毛重
				itemModel.setNetWeight(item.getNetWeight());// 净重
				itemModel.setPurchaseNum(item.getNum());// 应收数量
				itemModel.setTallyingNum(item.getNum());// 实收数量
				itemModel.setCreater(userId);// 创建人
				itemModel.setCreateName(name);// 创建人用户名
				// 根据商品货号获取商品id
				Map<String, Object> merchandiseInfo_params = new HashMap<String, Object>();
				merchandiseInfo_params.put("merchandiseId", item.getGoodsId());
				MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseInfo_params);
				if (merchandiseInfo != null) {
					itemModel.setVolume(merchandiseInfo.getVolume());// 体积
					itemModel.setLength(merchandiseInfo.getLength());// 长
					itemModel.setWidth(merchandiseInfo.getWidth());// 宽
					itemModel.setHeight(merchandiseInfo.getHeight());// 高

				}
				if(depotInfoMongo!=null){
					if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())){
						itemModel.setTallyingUnit(saleOrder.getTallyingUnit());;// 海外仓理货单位
					}
				}
				Long itemId = purchaseWarehouseItemDao.save(itemModel);
				// 如果批次不为空 批次为空 保存一份采购入库单
				/* if (null != item.getBatchNo() && !"".equals(item.getBatchNo())) { */
				PurchaseWarehouseBatchModel batch = new PurchaseWarehouseBatchModel();
				batch.setItemId(itemId);
				batch.setGoodsId(item.getGoodsId());
				batch.setBatchNo(item.getBatchNo());
				batch.setWornNum(0);
				batch.setExpireNum(0);
				batch.setNormalNum(item.getNum());
				batch.setCreater(userId);
				purchaseWarehouseBatchDao.save(batch);
				/* } */
			}
			// 添加勾稽关系
			WarehouseOrderRelModel warehouseOrderRel = new WarehouseOrderRelModel();
			warehouseOrderRel = new WarehouseOrderRelModel();
			warehouseOrderRel.setWarehouseId(result_id);// 采购入库单id
			warehouseOrderRel.setPurchaseOrderId(id);// 采购订单id
			warehouseOrderRel.setCreater(userId);// 创建人
			warehouseOrderRelDao.save(warehouseOrderRel);

			InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
			inventoryRoot.setBackTopic(MQPushBackOrderEnum.ON_THE_WAY_PUSH_BACK.getTopic());
			inventoryRoot.setBackTags(MQPushBackOrderEnum.ON_THE_WAY_PUSH_BACK.getTags());
			Map<String, Object> customParam = new HashMap<String, Object>();
			customParam.put("userId", userId);
			inventoryRoot.setCustomParam(customParam);
			// 增加库存

			inventoryRoot.setMerchantId(purchaseWarehouse.getMerchantId().toString());
			inventoryRoot.setMerchantName(purchaseWarehouse.getMerchantName());
			inventoryRoot.setTopidealCode(topidealCode);
			inventoryRoot.setBusinessNo(purchaseWarehouse.getCode());
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", purchaseWarehouse.getDepotId());// 根据仓库id
			DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
			inventoryRoot.setDepotId(purchaseWarehouse.getDepotId().toString());
			inventoryRoot.setDepotName(purchaseWarehouse.getDepotName());
			inventoryRoot.setDepotCode(mongo.getCode());
			inventoryRoot.setDepotType(mongo.getType());
			inventoryRoot.setIsTopBooks(mongo.getIsTopBooks());
			inventoryRoot.setOrderNo(purchaseWarehouse.getCode());
			// inventoryRoot.setSource(SourceStatusEnum.CGRK.getKey());
			inventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_001);
			// inventoryRoot.setSourceType(InventoryStatusEnum.CGZX.getKey());
			inventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0021);
			inventoryRoot.setSourceDate(now);
			// 获取当前年月
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
			String now2 = sdf2.format(new Date());
			inventoryRoot.setOwnMonth(now2);
			inventoryRoot.setDivergenceDate(now);
			int depot_flag = 0;
			if (mongo != null && mongo.getType().equals("c")) {
				depot_flag = 1;
			}
			List<InvetAddOrSubtractGoodsListJson> goodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
			List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.getGoodsAndBatch2(result_id);
			for (PurchaseWarehouseBatchModel bModel : batchList) {
				// 正常数量
				InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
				listJson.setGoodsId(bModel.getGoodsId().toString());
				listJson.setGoodsName(bModel.getGoodsName());
				listJson.setGoodsNo(bModel.getGoodsNo());
				listJson.setNum(bModel.getPurchaseNum());
				listJson.setBarcode(bModel.getBarcode());
				// 海外仓必填
				if (depot_flag == 1) {
					listJson.setUnit("0");// 0 托盘 1箱 2 件
				}
				listJson.setOperateType("1");// 字符串 0 调减 1调增
				listJson.setStockLocationTypeId(purchaseOrderModel.getStockLocationTypeId() == null ? "" : purchaseOrderModel.getStockLocationTypeId().toString());
				listJson.setStockLocationTypeName(purchaseOrderModel.getStockLocationTypeName());
				goodsList.add(listJson);
			}
			try {
				inventoryRoot.setGoodsList(goodsList);
				rocketMQProducer.send(JSONObject.fromObject(inventoryRoot).toString(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
			} catch (Exception e) {
				LOGGER.error("----------------------" + purchaseWarehouse.getCode() + "采购订单中转仓入库增加库存失败----------------------");
				msg = "单号：" + saleOrder.getCode() + "，自动生成采购订单中转仓入库增加库存失败";

			}
		} else {
			msg = "单号：" + saleOrder.getCode() + "，自动生成采购入库单失败";

		}

		return "审核成功";
	}

	@Override
	public List checkExistByPo(String poNo, Long orderId, Long merchantId,Long buId) throws Exception {
		List<SalePoRelModel> salePoRelList = new ArrayList<>();
		List<String> poList = new ArrayList<>();
		//以“公司+客商”维度，除【已红冲】、【已删除】非正常单外，系统的正常单是否存在相同的po，存在则报错提示：系统已存在相同的po号
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("state", "0");
		paramMap.put("poNo", poNo);
		paramMap.put("buId", buId);
		paramMap.put("merchantId", merchantId);
		if (orderId != null) {
			// 若是编辑销售单，则要排除本身
			paramMap.put("orderId", orderId);
		}
		// 判断po号是否在销售单_po关联表中已存在
		salePoRelList = salePoRelDao.getAllByNoDelete(paramMap);
		if (salePoRelList.size() > 0) {
			poList.add(salePoRelList.get(0).getPoNo());
		}
		return poList;
	}

	@Override
	public boolean saveOrModifyTempOrder(SaleOrderDTO dto, User user) throws Exception {
		SaleOrderModel saleOrderModel = saveSaleOrder(dto, user, "0");//0-保存 1-提交 2-审核

		// 平台采购订单转销售订单（平台采购订单保存销售订单号）
		String platformPurchaseIds = dto.getPlatformPurchaseIds();// 平台采购订单号
		if (StringUtils.isNotBlank(platformPurchaseIds) && dto.getId() == null) {
			String[] platformPurchaseIdStr = platformPurchaseIds.split(",");
			for (String platformPurchaseId : platformPurchaseIdStr) {
				PlatformPurchaseOrderModel platformPurchaseOrder = platformPurchaseOrderDao
						.searchById(Long.parseLong(platformPurchaseId));
				String saleCode = "";
				if (platformPurchaseOrder != null)
					saleCode = platformPurchaseOrder.getSaleCode();
				PlatformPurchaseOrderModel updateModel = new PlatformPurchaseOrderModel();
				updateModel.setId(Long.parseLong(platformPurchaseId));
				updateModel.setResaleDate(TimeUtils.getNow());
				updateModel.setResaleName(user.getName());
				updateModel.setResaler(user.getId());
				updateModel.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_3);// 3:已转销售
				if (StringUtils.isNotBlank(saleCode)) {
					saleCode = saleCode + "," + saleOrderModel.getCode();
				} else {
					saleCode = saleOrderModel.getCode();
				}
				updateModel.setSaleCode(saleCode);// 销售单号
				updateModel.setModifyDate(TimeUtils.getNow());
				platformPurchaseOrderDao.modify(updateModel);
			}
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrderModel.getCode(), "保存", null, null);
		return true;
	}

	/**
	 * 上架导入（新）
	 *
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> importSaleShelf(List<List<Map<String, String>>> data, User user) throws Exception {

		// 1.导入前数据检查 拼装上架明细
		Map<String, Object> retMap = checkshelImport(data.get(0), user);
		// 存储校验通过的上架明细 key=销售单号+po+上架时间 value=List[上架明细]
		Map<String, List<Map<String, Object>>> saleShelListMap = (Map<String, List<Map<String, Object>>>) retMap
				.get("saleShelListMap");
		List<Map<String, String>> msgList = (List<Map<String, String>>) retMap.get("msgList");// 检查失败消息
		Integer success = (Integer) retMap.get("success");// 检查成功条数
		Integer failure = (Integer) retMap.get("failure");// 检查失败条数

		// 存在检查未通过的数据，结束导入
		if (failure > 0) {
			retMap.put("msgList", msgList);
			retMap.put("success", 0);
			retMap.put("failure", failure);
			return retMap;
		}

		// 2.生成上架单 返回上架单表头和上架表体集合
		List<Map<String, Object>> shelListAll = createShelForImport(saleShelListMap, user);

		// 3.上架明细生成上架入库单 返回上架入库单加库存报文
		List<InvetAddOrSubtractRootJson> subtractRootJsonList = createShelfIdepotForImport(shelListAll, user);

		// 4、生成销售SD
		List<String> shelfCodeList = new ArrayList<String>();
		for (Map<String, Object> shelMap : shelListAll) {
			ShelfModel shelfModel = (ShelfModel) shelMap.get("shel");// 上架单表头
			List<SaleShelfModel> saleShelIemList = (List<SaleShelfModel>) shelMap.get("saleShelIemList");// 上架表体明细
			generateSaleSdOrder(shelfModel, saleShelIemList, user);

			SaleOrderModel saleOrderModel = saleOrderDao.searchById(shelfModel.getSaleOrderId());
			// 发送邮件
			ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
			mailDTO.setBusinessModuleType("3");
			mailDTO.setTypeName("销售");
			mailDTO.setType("3");// 1：提交 2：审核 3：上架 4：核销 5：开票 6：作废审核 7：盖章发票 8：审核完毕 9：作废完成 10：金额修改 11：金额确认
			mailDTO.setMerchantId(shelfModel.getMerchantId());
			mailDTO.setMerchantName(shelfModel.getMerchantName());
			mailDTO.setBuId(shelfModel.getBuId());
			mailDTO.setBuName(shelfModel.getBuName());
			mailDTO.setSupplier(shelfModel.getCustomerName());
			mailDTO.setOrderCode(shelfModel.getSaleOrderCode());
			mailDTO.setPoNum(shelfModel.getPoNo());
			mailDTO.setStatus("金额未确认");
			mailDTO.setShelvesId(user.getId());
			mailDTO.setSubmitId(Arrays.asList(String.valueOf(saleOrderModel.getSubmiter())));
			mailDTO.setAuditorId(saleOrderModel.getAuditor());
			mailDTO.setReviewerId(saleOrderModel.getAmountConfirmer());
			mailDTO.setModifyId(saleOrderModel.getAdjuster());
			mailDTO.setUserName(user.getName());

			List<String> userIds = new ArrayList<String>();
			userIds.add(String.valueOf(user.getId()));
			mailDTO.setUserId(userIds);
			try {
				rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(),MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());
			} catch (Exception e) {
				LOGGER.error("----------------------销售单[" + shelfModel.getSaleOrderCode()+ "]导入上架发送邮件失败----------------------");
			}
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, shelfModel.getSaleOrderCode(), "导入上架", null, null);
			shelfCodeList.add(shelfModel.getCode());
		}
		retMap.put("msgList", msgList);
		retMap.put("success", success);
		retMap.put("failure", failure);
		retMap.put("subtractRootJsonList", subtractRootJsonList);
		retMap.put("shelfCodeList", shelfCodeList.stream().distinct().collect(Collectors.toList()));
		return retMap;
	}

	/**
	 * 导入前数据检查 拼装上架明细
	 */
	public Map<String, Object> checkshelImport(List<Map<String, String>> dataList, User user) throws Exception {
		Map<String, Object> retMap = new HashMap<>();
		List<Map<String, String>> msgList = new ArrayList<>();// 存检查失败消息
		int failure = 0;// 统计检查失败数量
		int success = 0;// 统计检查通过数量

		/** ==========合并相同单号+条码的上架数量用于检查待上架量是否足够start================== */
		// 合并统计相同单号条码的上架数量 key=code+barcode value=sumnum
		Map<String, Integer> codeGoodsSumMap = new HashMap<>();
		/** ==========合并相同单号+条码的上架数量用于检查待上架量是否足够end================== */

		// 存储校验通过的上架明细 key=销售单号+po+上架时间 value=List[上架明细]
		Map<String, List<Map<String, Object>>> saleShelListMap = new HashMap<>();
		//记录出库单+条码+po+上架日期的上架单表体，合并相同上架商品
		Map<String,Object> shelfGoodsMap = new HashMap<String, Object>();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		/**
		 * =====================================参数校验,拼装导入数据start=============================
		 */
		Map<String, Boolean> checkRepeatGoods = new HashMap<>();
		for (int i = 0; i < dataList.size(); i++) {// --------------001检查start
			Map<String, String> msg = new HashMap<String, String>();// 失败消息
			Map<String, String> map = dataList.get(i);
			/** 1.校验必填字段 */
			String saleOutCode = map.get("出库清单编号");
			saleOutCode = saleOutCode.trim();
			if (StringUtils.isBlank(saleOutCode)) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "出库清单编号不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			String poNo = map.get("PO号");
			poNo = poNo.trim();
			if (StringUtils.isBlank(poNo)) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "PO号不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			String goodsNo = map.get("商品货号");
			goodsNo = goodsNo.trim();
			if (StringUtils.isBlank(goodsNo)) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "商品货号不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			String priceStr= map.get("销售单价");

			String shelfNumStr = map.get("本次上架数量");
			shelfNumStr = shelfNumStr.trim();
			if (StringUtils.isBlank(shelfNumStr)) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "本次上架数量不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			String damagedNumStr = map.get("本次残损数量");
			damagedNumStr = damagedNumStr.trim();
			if (StringUtils.isBlank(damagedNumStr)) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "本次残损数量不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			String lackNumStr = map.get("本次少货数量");
			lackNumStr = lackNumStr.trim();
			if (StringUtils.isBlank(lackNumStr)) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "本次少货数量不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			String shelfDateStr = map.get("本次上架日期");
			shelfDateStr = shelfDateStr.trim();
			if (StringUtils.isBlank(shelfDateStr)) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "本次上架日期不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			Date shelfDate = null;
			try {
				shelfDate = formatter.parse(shelfDateStr);
			} catch (ParseException e) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "上架日期格式不正确，要求 yyyy-MM-dd");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			if (!shelfDateStr.equals(formatter.format(shelfDate))) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "上架日期格式不正确，要求 yyyy-MM-dd");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			shelfDateStr = shelfDateStr + " 00:00:00";

			String remark = map.get("备注");
			remark = remark.trim();

			Integer shelfNum = StringUtils.isNotBlank(shelfNumStr) ? Integer.valueOf(shelfNumStr) : 0;
			Integer damagedNum = StringUtils.isNotBlank(damagedNumStr) ? Integer.valueOf(damagedNumStr) : 0;
			Integer lackNum = StringUtils.isNotBlank(lackNumStr) ? Integer.valueOf(lackNumStr) : 0;
			if (shelfNum <= 0 && damagedNum <= 0 && lackNum <= 0) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "本次上架数量、本次残损数量、本次少货数量不能同时为0");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			/** 检查销售单出库单 1.是否存在 2.状态 3.销售类型 */
			SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
			saleOutDepotModel.setMerchantId(user.getMerchantId());
			saleOutDepotModel.setCode(saleOutCode);
			saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
			if (saleOutDepotModel == null) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "该销售出库单不存在：" + saleOutCode);
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if (!saleOutDepotModel.getStatus().equals(DERP_ORDER.SALEOUTDEPOT_STATUS_018) &&
				!saleOutDepotModel.getStatus().equals(DERP_ORDER.SALEOUTDEPOT_STATUS_031)) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "销售出库单：" + saleOutCode + "非已出库/部分上架状态");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			/** 检查销售单 1.是否存在 2.状态 3.销售类型 */
			SaleOrderModel saleModel = new SaleOrderModel();
			saleModel.setMerchantId(user.getMerchantId());
			saleModel.setCode(saleOutDepotModel.getSaleOrderCode());
			SaleOrderModel saleOrderModel = saleOrderDao.searchByModel(saleModel);

			if (saleOrderModel == null) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "该销售订单不存在：" + saleOutDepotModel.getSaleOrderCode());
				msgList.add(msg);
				failure += 1;
				continue;
			}

			if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "该销售订单红冲状态不为空，不允许上架");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if (!saleOrderModel.getState().equals(DERP_ORDER.SALEORDER_STATE_018) && !saleOrderModel.getState().equals(DERP_ORDER.SALEORDER_STATE_019)
					&& !saleOrderModel.getState().equals(DERP_ORDER.SALEORDER_STATE_031)) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "销售订单号：" + saleOutDepotModel.getSaleOrderCode() + ",PO号：" + poNo + "需当订单状态为已出库、部分出库或部分上架才可上架");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if (DERP_ORDER.SALEDECLARE_ISGENERATE_1.equals(saleOrderModel.getIsGenerateDeclare())) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "该销售订单:" + saleOutDepotModel.getSaleOrderCode()+"已生成销售预申报单，无法导入上架");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if (!DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderModel.getBusinessModel())
					&& !DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderModel.getBusinessModel())) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "销售单号为：" + saleOutDepotModel.getSaleOrderCode() + ",销售类型为购销-整批结算或购销-实销实结才可进行上架");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if(!poNo.equals(saleOrderModel.getPoNo())){
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "销售订单：" + saleOutDepotModel.getSaleOrderCode()+"不存在该po号");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			Boolean isRepeat = checkRepeatGoods.get(saleOutCode+"_"+goodsNo);
			if(isRepeat == null){
				SaleOutDepotDTO saleOutDTO = new SaleOutDepotDTO();
				saleOutDTO.setCode(saleOutCode);
				saleOutDTO.setGoodsNo(goodsNo);
				List<SaleOutDepotDTO> saleOutList = saleOutDepotDao.queryDTODetailsList(saleOutDTO);
				if(saleOutList != null && saleOutList.size() > 0){
					List<Long> saleItemIds = saleOutList.stream().map(SaleOutDepotDTO::getSaleItemId).distinct().collect(Collectors.toList());
					if(saleItemIds.size() > 1){
						isRepeat = true;
					}else{
						isRepeat = false;
					}
					checkRepeatGoods.put(saleOutCode+"_"+goodsNo, isRepeat);
				}else{
					msg.put("row", String.valueOf(i + 1));
					msg.put("msg",  ",货号:"+goodsNo + "在出库单：" + saleOutCode + "未找到对应商品");
					msgList.add(msg);
					failure += 1;
					continue;
				}
			}
			if(isRepeat && StringUtils.isBlank(priceStr)){
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "出库单：" + saleOutCode + ",货号:"+goodsNo+" 存在多个价格，销售单价不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if(StringUtils.isBlank(priceStr)){
				SaleOutDepotItemModel outItemModel = new SaleOutDepotItemModel();
				outItemModel.setSaleOutDepotId(saleOutDepotModel.getId());
				outItemModel.setGoodsNo(goodsNo);
				outItemModel = saleOutDepotItemDao.searchByModel(outItemModel);

				SaleOrderItemModel saleOrderItemModel = saleOrderItemDao.searchById(outItemModel.getSaleItemId());

				priceStr = saleOrderItemModel.getPrice().toPlainString();
			}
			SaleOrderItemModel tempSaleOrderItemDTO = new SaleOrderItemModel();
			tempSaleOrderItemDTO.setOrderId(saleOutDepotModel.getSaleOrderId());
			tempSaleOrderItemDTO.setGoodsNo(goodsNo);
			tempSaleOrderItemDTO.setPrice(new BigDecimal(priceStr));
			List<SaleOrderItemModel> tempSaleOrderItemList = saleOrderItemDao.list(tempSaleOrderItemDTO);
			if(tempSaleOrderItemList != null && tempSaleOrderItemList.size() > 1){
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "出库单：" + saleOutCode + ",货号:"+goodsNo+" 相同销售单价存在多条，不允许导入");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if(tempSaleOrderItemList == null || tempSaleOrderItemList.size() < 1){
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "货号:"+goodsNo+" 销售单价:"+priceStr + "，在出库单：" + saleOutCode + "不存在");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			// 检查待上架量是否足够 待上架量=出库量-已上架量
			Integer sumNum = 0 ;
			// 合并统计相同单号+商品货号+单价
			if (codeGoodsSumMap.containsKey(saleOutCode + "_" +goodsNo + "_"+ priceStr)) {
				sumNum = codeGoodsSumMap.get(saleOutCode + "_" +goodsNo + "_"+ priceStr);
				sumNum = sumNum + shelfNum + damagedNum + lackNum;
				codeGoodsSumMap.put(saleOutCode + "_" +goodsNo + "_"+ priceStr, sumNum);
			} else {
				sumNum = shelfNum + damagedNum + lackNum;
				codeGoodsSumMap.put(saleOutCode + "_" +goodsNo + "_"+ priceStr, shelfNum + damagedNum + lackNum);
			}
			Map<String,Object> queryMap = new HashMap<>();
			queryMap.put("saleOutCode",saleOutCode);
			queryMap.put("goodsNo",goodsNo);
			queryMap.put("price",priceStr);
			Integer noShelfNum = 0;
			List<Map<String, Object>> goodsNotShelNumList = saleOutDepotDao.getGoodsNotShelNumList(queryMap);
			// 遍历出库单商品未上架量，若全部商品未上架量都为0则销售单状态为已上架，若存在未上量大于0的商品则销售单状态为部分上架
			for (Map<String, Object> goodsMap : goodsNotShelNumList) {
				BigDecimal diffnumbg = (BigDecimal) goodsMap.get("diffnum");
				if (diffnumbg.intValue() > 0) {
					// 存在未上架的量
					noShelfNum = noShelfNum + diffnumbg.intValue();
				}
			}
			if (sumNum.intValue() > noShelfNum.intValue()) {
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "待上架余量不足,出库单号：" + saleOutCode + "，商品货号：" + goodsNo+"，销售单价："+ priceStr + "剩余上架量：" + noShelfNum);
				msgList.add(msg);
				failure += 1;
				continue;
			}

			// 上架日期不能大于当前日期
			Date nowDate = TimeUtils.parseDay(TimeUtils.formatDay());
			Date shelfD = TimeUtils.parseDay(shelfDateStr);
			String deliverStr = TimeUtils.formatDay(saleOutDepotModel.getDeliverDate());
			if (shelfD.getTime() > nowDate.getTime()) {
//				throw new RuntimeException("上架日期不能大于当前日期");
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "上架日期不能大于当前日期");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			// 上架日期要大于等于出库日期
			if (shelfD.getTime() < TimeUtils.parseDay(deliverStr).getTime()) {
//				throw new RuntimeException("上架日期要大于等于出库日期");
				msg.put("row", String.valueOf(i + 1));
				msg.put("msg", "上架日期要大于等于出库日期");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			// 检查关账日期/月结转日期
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(saleOrderModel.getMerchantId());
			closeAccountsMongo.setDepotId(saleOrderModel.getOutDepotId());
			closeAccountsMongo.setBuId(saleOrderModel.getBuId());
			String maxMonth = "";
			if (closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {// 查询关账日期
				maxMonth = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			} else if (closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null) {// 查询月结日期
				maxMonth = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			} else if (closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null) {// 获取最大的关账日/月结日期
				maxMonth = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			}
			if (StringUtils.isNotBlank(maxMonth)) {
				// 获取最大关账月份下一个月1日时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxMonth + "-01 00:00:00"));
				String maxCloseNextMonthDate = nextMonth + "-01 00:00:00";
				// 关账下个月日期大于 入库日期
				if (Timestamp.valueOf(shelfDateStr).getTime() < Timestamp.valueOf(maxCloseNextMonthDate).getTime()) {
					msg.put("row", String.valueOf(i + 1));
					msg.put("msg", "上架时间必须大于关账日期/月结日期");
					msgList.add(msg);
					failure += 1;
					continue;
				}
			}
			// ==================生成上架入库单的校验start
			// 4.购销-实销实结 并且上架量/损货量大于0则生成上架入库单
			boolean idepotFlag = false;// 是否生成上架入库单标识 true-生成 false-不生成
			if (DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderModel.getBusinessModel())) {
				if (shelfNum.intValue() > 0 || damagedNum.intValue() > 0) {
					idepotFlag = true;
				}
			}
			if (idepotFlag == true) {
				// 2.1检查销售单事业部
				if (saleOrderModel.getBuId() == null) {
					msg.put("row", String.valueOf(i + 1));
					msg.put("msg", "销售单事业部为空");
					msgList.add(msg);
					failure += 1;
					continue;
				}

				// 2.2检查入库仓库
				Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
				depotMerchantRelParam.put("merchantId", saleOrderModel.getMerchantId());
				depotMerchantRelParam.put("depotId", saleOrderModel.getInDepotId()); // 入库仓库
				DepotMerchantRelMongo inDepotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
				if (inDepotMerchantRelMongo == null) {
					msg.put("row", String.valueOf(i + 1));
					msg.put("msg", "销售单号" + saleOrderModel.getCode() + ",入库仓库编码" + saleOrderModel.getInDepotCode()+ ",未查到该公司下入库仓库信息");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				// 2.4检查入库仓库批次效期强校验
				Map<String, Object> depotInfo_params = new HashMap<String, Object>();
				depotInfo_params.put("depotId", saleOrderModel.getOutDepotId());// 根据出仓仓库id
				DepotInfoMongo outDepotMongo = depotInfoMongoDao.findOne(depotInfo_params);
				// 检查批次效期强校验：1-是 0-否
				if (DERP_SYS.BATCHVALIDATION_1.equals(outDepotMongo.getBatchValidation())) {
					SaleOutDepotItemModel outDepotItemModel = new SaleOutDepotItemModel();
					outDepotItemModel.setSaleOutDepotId(saleOutDepotModel.getId());
					List<SaleOutDepotItemModel> outDepotItemList = saleOutDepotItemDao.list(outDepotItemModel);
					boolean flag = true;// 批次强校验是否通过
					for (SaleOutDepotItemModel outDepotItem : outDepotItemList) {
						if (StringUtils.isBlank(outDepotItem.getTransferBatchNo())
								|| outDepotItem.getProductionDate() == null || outDepotItem.getOverdueDate() == null) {
							flag = false;
							break;
						}
					}
					if (flag == false) {
						msg.put("row", String.valueOf(i + 1));
						msg.put("msg", outDepotMongo.getName() + "批次效期强校验,销售单号" + saleOrderModel.getCode() + ",出库单"+ saleOutCode + "不符");
						msgList.add(msg);
						failure += 1;
						continue;
					}
				}
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("merchantId", saleOrderModel.getMerchantId());
				paramMap.put("goodsNo", goodsNo);
				paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1); // 状态(1使用中,0已禁用)
				MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(paramMap);

				paramMap.clear();
				paramMap.put("merchantId", saleOrderModel.getMerchantId());
				paramMap.put("barcode",merchandiseInfoMogo.getBarcode());
				paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1); // 状态(1使用中,0已禁用)
				List<MerchandiseInfoMogo> merchandiseInfoMogoList = merchandiseInfoMogoDao.findMerchandiseByDepotId(paramMap, saleOrderModel.getInDepotId());
				if(merchandiseInfoMogoList ==null || merchandiseInfoMogoList.size() < 1){
					msg.put("row", String.valueOf(i + 1));
					msg.put("msg", "商品货号："+goodsNo + "对应的条形码，未关联销售出库单:" + saleOutCode + "对应的销售单的入库仓");
					msgList.add(msg);
					failure += 1;
					continue;
				}

			}
			success++;// 检查通过
			// ==================生成上架入库单的校验end

			//拼装临时上架明细，合并相同商品start
			String key = saleOutCode + "-" + poNo+ "-" + shelfDateStr+ "-" + goodsNo.trim() + "-" + priceStr;
			if (shelfGoodsMap.containsKey(key)) {
				Map<String, Object> saleShelItemMap = (Map<String, Object>) shelfGoodsMap.get(key);
				Integer shelfNumSum = (Integer)saleShelItemMap.get("shelfNum") + shelfNum ;
				Integer damagedNumSum = (Integer)saleShelItemMap.get("damagedNum") + damagedNum ;
				Integer lackNumSum = (Integer)saleShelItemMap.get("lackNum") + lackNum ;

				saleShelItemMap.put("shelfNum", shelfNumSum);// 本次上架数量
				saleShelItemMap.put("damagedNum", damagedNumSum);// 本次残损数量
				saleShelItemMap.put("lackNum", lackNumSum);// 本次少货数量

				shelfGoodsMap.put(key, saleShelItemMap);
			} else {
				Map<String, Object> saleShelItemMap = new HashMap<String, Object>();
				saleShelItemMap.put("code", saleOutDepotModel.getSaleOrderCode().trim());// 销售单号
				saleShelItemMap.put("poNo", poNo.trim());// po号
				saleShelItemMap.put("goodsNo", goodsNo.trim());// 条码
				saleShelItemMap.put("shelfDateStr", shelfDateStr.trim());// 上架时间 YYYY-MM-DD HH:mm:ss
				saleShelItemMap.put("shelfNum", shelfNum);// 本次上架数量
				saleShelItemMap.put("damagedNum", damagedNum);// 本次残损数量
				saleShelItemMap.put("lackNum", lackNum);// 本次少货数量
				saleShelItemMap.put("remark", remark.trim());// 备注
				saleShelItemMap.put("saleOutCode", saleOutCode);// 销售出库单号
				saleShelItemMap.put("price", priceStr);// 销售单价

				shelfGoodsMap.put(key, saleShelItemMap);
			}
			//拼装临时上架明细，合并相同商品end
		}
		// 拼装临时上架明细 销售单号+po号+上架时间一样的生成一个上架单
		for(String shelfKey  : shelfGoodsMap.keySet()) {
			Map<String, Object> saleShelMap = (Map<String, Object>) shelfGoodsMap.get(shelfKey);

			String saleOutCode = shelfKey.split("-")[0];
			String poNo = shelfKey.split("-")[1];
			String shelfDateStr = shelfKey.split("-")[2];

			String key = saleOutCode + poNo + shelfDateStr;
			if (saleShelListMap.containsKey(key)) {
				List<Map<String, Object>> saleShelList = saleShelListMap.get(key);
				saleShelList.add(saleShelMap);
				saleShelListMap.put(key, saleShelList);
			} else {
				List<Map<String, Object>> saleShelList = new ArrayList<>();
				saleShelList.add(saleShelMap);
				saleShelListMap.put(key, saleShelList);
			}

		}
		 // --------------001检查end
		/**
		 * =====================================参数校验,拼装导入数据end=============================
		 */
		retMap.put("saleShelListMap", saleShelListMap);
		retMap.put("msgList", msgList);
		retMap.put("success", success);
		retMap.put("failure", failure);
		return retMap;
	}

	/**
	 * 2.上架导入--生成上架单 返回上架单表头和上架表体集合
	 */
	public List<Map<String, Object>> createShelForImport(Map<String, List<Map<String, Object>>> saleShelListMap,User user) throws Exception {

		// 存储生成完的上架单表头和表体用于后面生成上架入库单 List[Map<上架单key=shel、上架单表体key=saleShelIemList>]
		List<Map<String, Object>> shelListAll = new ArrayList<>();
		// 遍历拼装好的临时上架明细生成上架单
		for (List<Map<String, Object>> saleShelList : saleShelListMap.values()) {
			Map<String, Object> saleShelMap = saleShelList.get(0);// 获取第一个生成表头
			String code = (String) saleShelMap.get("code");
			String poNo = (String) saleShelMap.get("poNo");
			String shelfDateStr = (String) saleShelMap.get("shelfDateStr");
			String saleOutCode = (String) saleShelMap.get("saleOutCode");//销售出库单号
			// 查询销售单
			SaleOrderModel saleOrderModel = new SaleOrderModel();
			saleOrderModel.setCode(code);
			saleOrderModel.setMerchantId(user.getMerchantId());
			saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
			// 查询销售出库单
			SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
			saleOutDepotModel.setCode(saleOutCode);
			saleOutDepotModel.setMerchantId(user.getMerchantId());
			saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
			// 保存上架单表头
			ShelfModel shelfModel = new ShelfModel();
			shelfModel.setCode(SmurfsUtils.getID(DERP.UNIQUE_ID_SJD));
			shelfModel.setSaleOrderCode(code);
			shelfModel.setState(DERP_ORDER.SHELF_STATUS_1);
			shelfModel.setPoNo(poNo);
			shelfModel.setMerchantId(user.getMerchantId());
			shelfModel.setMerchantName(user.getMerchantName());
			shelfModel.setCustomerId(saleOutDepotModel.getCustomerId());
			shelfModel.setCustomerName(saleOutDepotModel.getCustomerName());
			shelfModel.setBuId(saleOutDepotModel.getBuId());
			shelfModel.setBuName(saleOutDepotModel.getBuName());
			shelfModel.setShelfDate(TimeUtils.parseFullTime(shelfDateStr + " 00:00:00"));
			shelfModel.setCurrency(saleOutDepotModel.getCurrency());
			shelfModel.setOperatorId(user.getId());
			shelfModel.setOperator(user.getName());
			shelfModel.setCreateDate(TimeUtils.getNow());
			shelfModel.setOutDepotId(saleOutDepotModel.getOutDepotId());
			shelfModel.setOutDepotName(saleOutDepotModel.getOutDepotName());
			shelfModel.setSaleOrderId(saleOutDepotModel.getSaleOrderId());
			shelfModel.setSaleOutDepotId(saleOutDepotModel.getId());
			shelfModel.setSaleOutDepotCode(saleOutCode);
			shelfDao.save(shelfModel);
			// 保存上架明细
			List<SaleShelfModel> saleShelIemList = new ArrayList<>();// 上架明细用于上架入库
			Integer benShelCount = 0;// 统计本次上架总量
			for (Map<String, Object> tempSaleShelMap : saleShelList) {
				String goodsNo = (String) tempSaleShelMap.get("goodsNo");
				String price = (String) tempSaleShelMap.get("price");
				Integer shelfNum = (Integer) tempSaleShelMap.get("shelfNum");// 本次上架数量
				Integer damagedNum = (Integer) tempSaleShelMap.get("damagedNum");// 本次残损数量
				Integer lackNum = (Integer) tempSaleShelMap.get("lackNum");// 本次少货数量
				String remark = (String) tempSaleShelMap.get("remark");// 备注

				// 获取销售出库单商品
				SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
				saleOrderItemModel.setOrderId(saleOutDepotModel.getSaleOrderId());
				saleOrderItemModel.setGoodsNo(goodsNo);
				saleOrderItemModel.setPrice(new BigDecimal(price));
				saleOrderItemModel = saleOrderItemDao.searchByModel(saleOrderItemModel);

				SaleOutDepotItemModel saleOutDepotItemModel = new SaleOutDepotItemModel();
				saleOutDepotItemModel.setSaleItemId(saleOrderItemModel.getId());
				List<SaleOutDepotItemModel>  saleOutItemList = saleOutDepotItemDao.list(saleOutDepotItemModel);
				Integer outNum = saleOutItemList.stream().map(SaleOutDepotItemModel :: getTransferNum).reduce(0, Integer::sum);
				Integer wornNum = saleOutItemList.stream().map(SaleOutDepotItemModel :: getWornNum).reduce(0, Integer::sum);
				outNum = outNum + wornNum;

				// 获取出库单号+条码出库量
//				Integer outNum = 0;
//				Map<String, Object> codePoMap = new HashMap<>();
//				codePoMap.put("code", saleOutCode);
//				codePoMap.put("barcode", barcode);
//				List<SaleOutDepotItemDTO> saleOutDepotItemList = saleOutDepotDao.getSaleOutDepotCount(codePoMap);
//				if (saleOutDepotItemList != null && saleOutDepotItemList.size() > 0) {
//					outNum = saleOutDepotItemList.get(0).getTransferNum() + saleOutDepotItemList.get(0).getWornNum();
//				}
//
				Integer totalShelfNum = 0;
				SaleShelfDTO saleShelfDTO = new SaleShelfDTO();
				saleShelfDTO.setSaleItemId(saleOrderItemModel.getId());
				saleShelfDTO = saleShelfDao.getTotalShelf(saleShelfDTO);
				if (saleShelfDTO != null) {
					totalShelfNum = saleShelfDTO.getShelfNum() + saleShelfDTO.getDamagedNum()+ saleShelfDTO.getLackNum(); // 已上架总数量
				}

				SaleShelfModel saleShelfModel = new SaleShelfModel();
				saleShelfModel.setOrderId(saleOutDepotModel.getSaleOrderId());
				saleShelfModel.setOrderType(DERP_ORDER.SALESHELF_ORDERTYPE_1);
				saleShelfModel.setGoodsId(saleOrderItemModel.getGoodsId());
				saleShelfModel.setGoodsNo(saleOrderItemModel.getGoodsNo());
				saleShelfModel.setGoodsName(saleOrderItemModel.getGoodsName());
				saleShelfModel.setBarcode(saleOrderItemModel.getBarcode());
				saleShelfModel.setCommbarcode(saleOrderItemModel.getCommbarcode());
				saleShelfModel.setTallyingUnit(saleOrderItemModel.getTallyingUnit());
				saleShelfModel.setNum(outNum+wornNum);// 出库数量
				if (saleShelfDTO != null) {
					saleShelfModel.setTotalShelfNum(saleShelfDTO.getShelfNum());// 已上架数量
					saleShelfModel.setTotalDamagedNum(saleShelfDTO.getDamagedNum());// 已计入残损数量
					saleShelfModel.setTotalLackNum(saleShelfDTO.getLackNum());// 已计少货数量
				} else {
					saleShelfModel.setTotalShelfNum(0);// 已上架数量
					saleShelfModel.setTotalDamagedNum(0);// 已计入残损数量
					saleShelfModel.setTotalLackNum(0);// 已计少货数量
				}

				saleShelfModel.setStayShelfNum(outNum - totalShelfNum);// 待上架量
				saleShelfModel.setShelfNum(shelfNum);// 本次上架数量
				saleShelfModel.setDamagedNum(damagedNum);// 本次残损数量
				saleShelfModel.setLackNum(lackNum);// 少货数量
				saleShelfModel.setPoNo(poNo);
				saleShelfModel.setRemark(remark);
				saleShelfModel.setShelfDate(TimeUtils.parseFullTime(shelfDateStr + " 00:00:00"));
				saleShelfModel.setCreateDate(TimeUtils.getNow()); // 操作时间
				saleShelfModel.setOperatorId(user.getId()); // 操作人ID
				saleShelfModel.setOperator(user.getName()); // 操作人名字
				saleShelfModel.setBuId(saleOutDepotModel.getBuId());// 事业部
				saleShelfModel.setBuName(saleOutDepotModel.getBuName());
				saleShelfModel.setShelfId(shelfModel.getId());
				saleShelfModel.setSaleOutDepotId(saleOutDepotModel.getId());
				saleShelfModel.setSaleItemId(saleOrderItemModel.getId());
				saleShelfDao.save(saleShelfModel);

				saleShelIemList.add(saleShelfModel);
				benShelCount = benShelCount + shelfNum + damagedNum + lackNum;
			}
			// 更新上架单表头上架总量
			ShelfModel shelfUpdate = new ShelfModel();
			shelfUpdate.setId(shelfModel.getId());
			shelfUpdate.setTotalShelfNum(benShelCount);// 上架总量
			shelfDao.modify(shelfUpdate);

			// 存储生成完的上架单表头和表体用于后面生成上架入库单 List[Map<上架单key=shel、上架单表体key=saleShelIemList>]
			Map<String, Object> shelMap = new HashMap<>();
			shelMap.put("shel", shelfModel);
			shelMap.put("saleShelIemList", saleShelIemList);
			shelListAll.add(shelMap);

			/**
			 * 更新销售单状态 1.出库单的每个商品都已全部上架==已上架，否则部分上架
			 */
			/**
			 * 销售出库单：
			 *	出库数量 != 上架数量，部分上架
			 *	出库数量 == 上架数量，已上架
			 *  上架数量 == 0，已出库
			 */
			SaleShelfModel saleShelfModel = new SaleShelfModel();
			saleShelfModel.setSaleOutDepotId(saleOutDepotModel.getId());
			List<SaleShelfModel> saleShelfList = saleShelfDao.list(saleShelfModel);
			Integer totalShelfNum = 0;// 出库单对应的上架数量
			for(SaleShelfModel saleshelfModel : saleShelfList) {
				Integer transferNum = saleshelfModel.getShelfNum() == null ? 0: saleshelfModel.getShelfNum();// 好品数量
				Integer wornNum = saleshelfModel.getDamagedNum() == null ? 0:saleshelfModel.getDamagedNum();	// 坏品数量
				Integer lackNum = saleshelfModel.getLackNum() == null ? 0 :saleshelfModel.getLackNum();	// 缺货数量
				totalShelfNum = totalShelfNum + transferNum + wornNum + lackNum;
			}
			//获取销售出库数量
			Integer totalOutNum = 0;//销售出库数量
			SaleOutDepotItemModel saleOutItemModel = new SaleOutDepotItemModel();
			saleOutItemModel.setSaleOutDepotId(saleOutDepotModel.getId());
			List<SaleOutDepotItemModel> saleOutItemList = saleOutDepotItemDao.list(saleOutItemModel);
			for(SaleOutDepotItemModel queryOutItemDTO : saleOutItemList) {
				Integer transferNum = queryOutItemDTO.getTransferNum()==null ? 0:queryOutItemDTO.getTransferNum();// 好品数量
				Integer wornNum = queryOutItemDTO.getWornNum()==null ? 0:queryOutItemDTO.getWornNum();	// 坏品数量
				totalOutNum =  totalOutNum + transferNum + wornNum;
			}

			String saleOutStatus = "";
			if(totalShelfNum.equals(totalOutNum)) {
				saleOutStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_026;//已上架
			}else if(totalShelfNum.equals(0)){
				saleOutStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_018;//已出库
			}else {
				saleOutStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_031;//部分上架
			}
			SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
			sOutDepotModel.setId(saleOutDepotModel.getId());
			sOutDepotModel.setStatus(saleOutStatus);
			sOutDepotModel.setModifyDate(TimeUtils.getNow());
			saleOutDepotDao.modify(sOutDepotModel);

			/** 更新销售订单状态：
			 * 		有出库单，出库数量！=销售数量，部分出库；
			 * 		有出库单，没有上架单，出库数量=销售数量，已出库；
			 * 		有出库单，出库数量=销售数量，上架数量！=出库数量，部分上架；
			 * 		有出库单，出库数量=销售数量，上架数量=出库数量，已上架
			 */
			String saleStatus = "";
			Map<String,Object> queryMap = new HashMap<>();
			queryMap.put("code",saleOrderModel.getCode());
			List<Map<String, Object>> goodsNotOutNumList = saleOutDepotDao.getGoodsNotOutNumList(queryMap);
			boolean allOut = true;
			for (Map<String, Object> goodsMap : goodsNotOutNumList) {
				BigDecimal diffnumbg = (BigDecimal) goodsMap.get("diffnum");
				if (diffnumbg.intValue() > 0) {
					allOut = false;// 存在未出库的量
					break;
				}
			}

			Integer noShelfNum = 0;
			boolean allShelf = true;
			List<Map<String, Object>> goodsNotShelNumList = saleOutDepotDao.getGoodsNotShelNumList(queryMap);
			// 遍历出库单商品未上架量，若全部商品未上架量都为0则销售单状态为已上架，若存在未上量大于0的商品则销售单状态为部分上架
			for (Map<String, Object> goodsMap : goodsNotShelNumList) {
				BigDecimal diffnumbg = (BigDecimal) goodsMap.get("diffnum");
				if (diffnumbg.intValue() > 0) {
					// 存在未上架的量
					noShelfNum = noShelfNum + diffnumbg.intValue();
					allShelf = false;
				}
			}
			//销售订单状态为 已出库（完结出库的情况，销售量！=出库量，但是已经完结出库了），意味着不允许再出库，此时只需要判断 上架和、部分上架状态
			if(saleOrderModel.getState().equals(DERP_ORDER.SALEORDER_STATE_018) || saleOrderModel.getState().equals(DERP_ORDER.SALEORDER_STATE_031)) {
				if(allShelf) {
					saleStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_026;//已上架
				}else{
					saleStatus = DERP_ORDER.SALEORDER_STATE_031;//部分上架
				}
			}else {
				if(allOut){
					if(allShelf) {
						saleStatus = DERP_ORDER.SALEOUTDEPOT_STATUS_026;//已上架
					}else{
						saleStatus = DERP_ORDER.SALEORDER_STATE_031;//部分上架
					}
				}else {
					//存在未出库的量，部分出库
					saleStatus = DERP_ORDER.SALEORDER_STATE_019;//部分出库
				}
			}

			SaleOrderModel saleOrderUpate = new SaleOrderModel();
			saleOrderUpate.setId(saleOrderModel.getId());
			saleOrderUpate.setState(saleStatus);
			saleOrderUpate.setModifyDate(TimeUtils.getNow());
			saleOrderUpate.setModifier(user.getId());
			saleOrderUpate.setModifierUsername(user.getName());
			saleOrderDao.modify(saleOrderUpate);

			if (saleOrderUpate.getState().equals(DERP_ORDER.SALEORDER_STATE_026)) {
				// 更新差异分析表状态
				SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
				saleAnalysisModel.setOrderId(saleOrderModel.getId());
				List<SaleAnalysisModel> saleAnalysisList = saleAnalysisDao.list(saleAnalysisModel);
				for (SaleAnalysisModel saleAnalysis : saleAnalysisList) {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("saleOrderId", saleOrderModel.getId());
					paramMap.put("goodsNo", saleAnalysis.getGoodsNo());
					paramMap.put("merchantId", saleOrderModel.getMerchantId());
					Integer outDepotNum = saleOutDepotDao.getTransferNum(paramMap);//已出库数量

					saleAnalysis.setSurplus(saleAnalysis.getSaleNum() - outDepotNum);
					saleAnalysis.setIsEnd("1");
					saleAnalysis.setEndDate(TimeUtils.getNow());
					saleAnalysisDao.modify(saleAnalysis);
				}
			}
		}
		/**
		 * =====================================生成上架单end=====================================
		 */
		return shelListAll;
	}

	/**
	 * 3.上架导入--上架明细生成上架入库单 返回上架入库单加库存报文 上架单集合 shelListAll
	 * [Map<上架单key=shel、上架单表体key=saleShelIemList>] 返回上架入库单加库存报文
	 */
	private List<InvetAddOrSubtractRootJson> createShelfIdepotForImport(List<Map<String, Object>> shelListAll,User user) throws Exception {
		List<InvetAddOrSubtractRootJson> subtractRootJsonList = new ArrayList<>();
		// shelListAll:List[Map<上架单key=shel、上架单表体key=saleShelIemList>]
		for (Map<String, Object> shelMap : shelListAll) {
			ShelfModel shelfModel = (ShelfModel) shelMap.get("shel");// 上架单表头
			List<SaleShelfModel> saleShelIemList = (List<SaleShelfModel>) shelMap.get("saleShelIemList");// 上架表体明细
			// 查询销售单
			SaleOrderModel saleOrder = saleOrderDao.searchById(shelfModel.getSaleOrderId());
			boolean idepotFlag = false;// 是否生成上架入库单标识 true-生成 false-不生成
			// 4.购销-实销实结 或销售类型为购销-整批结算且入库仓库为代客管理的仓库 并且上架量/损货量大于0则生成上架入库单
			if (DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrder.getBusinessModel())) {
				for (SaleShelfModel shelItem : saleShelIemList) {
					if (shelItem.getShelfNum().intValue() > 0 || shelItem.getDamagedNum().intValue() > 0) {
						idepotFlag = true;
						break;
					}
				}
			} else if (saleOrder.getInDepotId() != null
					&& DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrder.getBusinessModel())) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("depotId", saleOrder.getInDepotId());
				DepotInfoMongo inDepot = depotInfoMongoDao.findOne(params);// 入库仓
				// 入库仓库是否为代客管理仓
				if (DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_1.equals(inDepot.getIsValetManage())) {
					for (SaleShelfModel shelItem : saleShelIemList) {
						if (shelItem.getShelfNum().intValue() > 0 || shelItem.getDamagedNum().intValue() > 0) {
							idepotFlag = true;
							break;
						}
					}
				}

			}

			if (idepotFlag == true) {
				InvetAddOrSubtractRootJson subtractRootJson = saveShelfIdepot(shelfModel, saleOrder, user,
						saleShelIemList);
				subtractRootJsonList.add(subtractRootJson);
			}
		}
		return subtractRootJsonList;
	}

	/**
	 * 生成上架入库单并返回加库存报文-上架导入和页面上架共用
	 */
	public InvetAddOrSubtractRootJson saveShelfIdepot(ShelfModel shelfModel, SaleOrderModel saleOrder, User user,
			List<SaleShelfModel> shelfList) throws Exception {
		if(saleOrder.getInDepotId() == null){
			throw new RuntimeException("上架入库失败，销售订单："+saleOrder.getCode() +"入库仓为空");
		}
		SaleOutDepotModel outDepotModel = saleOutDepotDao.searchById(shelfModel.getSaleOutDepotId());

		// 保存上架入库单表头
		SaleShelfIdepotModel saleShelfIdepotModel = new SaleShelfIdepotModel();
		saleShelfIdepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_SJRK)); // 上架入库单号
		saleShelfIdepotModel.setSaleOrderId(saleOrder.getId());
		saleShelfIdepotModel.setSaleOrderCode(saleOrder.getCode());
		saleShelfIdepotModel.setMerchantId(saleOrder.getMerchantId());
		saleShelfIdepotModel.setMerchantName(saleOrder.getMerchantName());
		saleShelfIdepotModel.setCustomerId(saleOrder.getCustomerId());
		saleShelfIdepotModel.setCustomerName(saleOrder.getCustomerName());
		saleShelfIdepotModel.setInDepotId(saleOrder.getInDepotId());
		saleShelfIdepotModel.setInDepotName(saleOrder.getInDepotName());
		saleShelfIdepotModel.setInDepotCode(saleOrder.getInDepotCode());
		saleShelfIdepotModel.setOutDepotId(saleOrder.getOutDepotId());
		saleShelfIdepotModel.setOutDepotCode(saleOrder.getOutDepotCode());
		saleShelfIdepotModel.setOutDepotName(saleOrder.getOutDepotName());
		saleShelfIdepotModel.setBuId(saleOrder.getBuId()); // 事业部ID
		saleShelfIdepotModel.setBuName(saleOrder.getBuName()); // 事业部名字
		saleShelfIdepotModel.setSaleOutDepotId(outDepotModel.getId());
		saleShelfIdepotModel.setSaleOutCode(outDepotModel.getCode());
		saleShelfIdepotModel.setSaleShelfId(shelfModel.getId()); // 销售上架单id
		saleShelfIdepotModel.setPoNo(shelfModel.getPoNo()); // 对应该上架入库单生成的上架PO单号
		saleShelfIdepotModel.setState(DERP_ORDER.SALESHELFIDEPOT_STATUS_028); // 入库中
		saleShelfIdepotModel.setShelfDate(shelfModel.getShelfDate()); // 上架入库时间
		saleShelfIdepotModel.setOperator(user.getName());
		saleShelfIdepotModel.setOperatorId(user.getId());
		saleShelfIdepotModel.setOperatorDate(TimeUtils.getNow());
		saleShelfIdepotModel.setCreater(user.getId());
		saleShelfIdepotModel.setCreateDate(TimeUtils.getNow());
		saleShelfIdepotModel.setModifyDate(TimeUtils.getNow());
		saleShelfIdepotDao.save(saleShelfIdepotModel);

		// 获取入库仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", saleShelfIdepotModel.getInDepotId());// 根据入库仓库id
		DepotInfoMongo depotMongo = depotInfoMongoDao.findOne(depotInfo_params);

		// 封装加库存报文头
		InvetAddOrSubtractRootJson subtractRootJson = new InvetAddOrSubtractRootJson();
		subtractRootJson.setMerchantId(String.valueOf(saleOrder.getMerchantId()));
		subtractRootJson.setMerchantName(saleOrder.getMerchantName());
		subtractRootJson.setTopidealCode(saleOrder.getTopidealCode());
		subtractRootJson.setBuId(String.valueOf(saleOrder.getBuId()));
		subtractRootJson.setBuName(saleOrder.getBuName());
		subtractRootJson.setDepotId(String.valueOf(depotMongo.getDepotId()));
		subtractRootJson.setDepotName(depotMongo.getName());
		subtractRootJson.setDepotCode(depotMongo.getDepotCode());
		subtractRootJson.setDepotType(depotMongo.getDepotType());
		subtractRootJson.setIsTopBooks(depotMongo.getIsTopBooks());
		subtractRootJson.setOrderNo(saleShelfIdepotModel.getCode());
		subtractRootJson.setBusinessNo(saleOrder.getCode());
		subtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0017);// 库存单据来源:上架入库
		subtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0029);// 库存单据类型:上架入库
		subtractRootJson.setSourceDate(TimeUtils.formatFullTime());
		subtractRootJson.setDivergenceDate(TimeUtils.formatFullTime(shelfModel.getShelfDate()));// 取入库时间（上架时间）
		subtractRootJson.setOwnMonth(TimeUtils.formatMonth(shelfModel.getShelfDate()));

		List<InvetAddOrSubtractGoodsListJson> subtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		for (SaleShelfModel saleShelfModel : shelfList) {// 上架明细001-----------start
			// 上架明细正常量和残损量都为0则跳过
			if (saleShelfModel.getShelfNum().intValue() <= 0 && saleShelfModel.getDamagedNum().intValue() <= 0) {
				continue;
			}

			/**
			 * 代客管理仓-- 直接取销售单商品货号
			 *
			 * 非代客管理仓逻辑如下： 选品限制--获取商品 1、对应该上架入库单关联的销售订单在做上架时，根据上架商品货号的标准条码+入库仓库的选品限制
			 * 查询到符合条件的商品货号， 以转换后的商品货号形成上架入库单的入库商品信息；
			 * 2、若找到多个符合条件的货号时，日志报错，入库失败。报错提示文案：销售订单号+PO号+上架商品货号查到多个入库货号，
			 */
			// 查询入库仓库公司关联信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", saleOrder.getInDepotId());
			DepotInfoMongo inDepot = depotInfoMongoDao.findOne(params);// 入库仓

			Map<String, Object> merchandiseParamMap = new HashMap<String, Object>();
			merchandiseParamMap.put("goodsNo", saleShelfModel.getGoodsNo());
			merchandiseParamMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1); // 状态(1使用中,0已禁用)
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseParamMap);

			if (inDepot != null && !DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_1.equals(inDepot.getIsValetManage())) {
				merchandiseParamMap.clear();
				merchandiseParamMap.put("barcode", merchandiseInfoMogo.getBarcode());
				merchandiseParamMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1); // 状态(1使用中,0已禁用)
				merchandiseParamMap.put("merchantId", saleOrder.getMerchantId());

				List<MerchandiseInfoMogo> merchandiseInfoMogoList = merchandiseInfoMogoDao.findMerchandiseByDepotId(merchandiseParamMap, inDepot.getDepotId());
				if(merchandiseInfoMogoList == null || merchandiseInfoMogoList.size() < 1){
					throw new RuntimeException("商品货号："+ saleShelfModel.getGoodsNo() + "对应的条形码，未关联入库仓:"+ inDepot.getName());
				}
				merchandiseInfoMogo = merchandiseInfoMogoList.get(0);

			}

			/**
			 * 查询出库单商品剩余可分配出库余量=出库量-上架入库量 按先失效先分配最后分配无批次效期规则排序
			 */
			Map<String, Object> surplusMap = new HashMap<>();
			surplusMap.put("saleOutDepotId", outDepotModel.getId());
			surplusMap.put("saleItemId", saleShelfModel.getSaleItemId());
			List<Map<String, Object>> outDepotItemList = saleOutDepotItemDao.listCountSurplusNum(surplusMap);
			// 上架商品数量循环分配到出库批次上
			int damagedNum = saleShelfModel.getDamagedNum();// 上架残损量
			int shelfNum = saleShelfModel.getShelfNum();// 上架正常量
			String type1 = "1";// type 1-好品 2-坏品
			String type2 = "2";// type 1-好品 2-坏品
			Map<String, SaleShelfIdepotItemModel> allotNumAllMap = new LinkedHashMap<>();// 存储分配好的上架入库明细
			allotNum(damagedNum, type2, allotNumAllMap, outDepotItemList);// 分配坏品上架入库表体
			allotNum(shelfNum, type1, allotNumAllMap, outDepotItemList);// 分配好品上架入库表体

			// 遍历分配好的上架入库明细生成上架入库表体拼装加库存报文
			for (SaleShelfIdepotItemModel shelfIdepotItem : allotNumAllMap.values()) {// 上架入库表体002----------start
				shelfIdepotItem.setSshelfIdepotId(saleShelfIdepotModel.getId());// 上架入库单id
				shelfIdepotItem.setInGoodsId(merchandiseInfoMogo.getMerchandiseId());
				shelfIdepotItem.setInGoodsNo(merchandiseInfoMogo.getGoodsNo());
				shelfIdepotItem.setInGoodsName(merchandiseInfoMogo.getName());
				shelfIdepotItem.setInGoodsCode(merchandiseInfoMogo.getGoodsCode());
				shelfIdepotItem.setBarcode(merchandiseInfoMogo.getBarcode());
				shelfIdepotItem.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
				Integer normalNum1 = shelfIdepotItem.getNormalNum() == null ? 0 :shelfIdepotItem.getNormalNum();
				Integer damagedNum1 = shelfIdepotItem.getDamagedNum() == null ? 0 :shelfIdepotItem.getDamagedNum();
				shelfIdepotItem.setNormalNum(normalNum1);
				shelfIdepotItem.setDamagedNum(damagedNum1);
				shelfIdepotItem.setCreater(user.getId());
				shelfIdepotItem.setCreateDate(TimeUtils.getNow());
				saleShelfIdepotItemDao.save(shelfIdepotItem);

				// 封装加库存报文表体
				InvetAddOrSubtractGoodsListJson subtractGoods = new InvetAddOrSubtractGoodsListJson();
				// 计算是否过期 字符串 （0 是 1 否）
				String isExpire = DERP.ISEXPIRE_1;
				if (shelfIdepotItem.getOverdueDate() != null) {
					isExpire = TimeUtils.isNotIsExpireByDate(shelfIdepotItem.getOverdueDate());// 判断是否过期是否过期（0是 1否）
				}

				subtractGoods.setGoodsId(String.valueOf(shelfIdepotItem.getInGoodsId()));
				subtractGoods.setGoodsNo(shelfIdepotItem.getInGoodsNo());
				subtractGoods.setGoodsName(shelfIdepotItem.getInGoodsName());
				subtractGoods.setBarcode(shelfIdepotItem.getBarcode());
				subtractGoods.setCommbarcode(shelfIdepotItem.getCommbarcode());
				subtractGoods.setBuId(String.valueOf(saleOrder.getBuId()));
				subtractGoods.setBuName(saleOrder.getBuName());
				subtractGoods.setBatchNo(shelfIdepotItem.getBatchNo());
				subtractGoods.setProductionDate(TimeUtils.formatDay(shelfIdepotItem.getProductionDate()));
				subtractGoods.setOverdueDate(TimeUtils.formatDay(shelfIdepotItem.getOverdueDate()));
				subtractGoods.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
				subtractGoods.setIsExpire(isExpire);
				subtractGoods.setStockLocationTypeId(saleOrder.getStockLocationTypeId() == null ? "" : saleOrder.getStockLocationTypeId().toString());
				subtractGoods.setStockLocationTypeName(saleOrder.getStockLocationTypeName());
				if (shelfIdepotItem.getNormalNum() != null && shelfIdepotItem.getNormalNum().intValue() > 0) {
					subtractGoods.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);// 好品
					subtractGoods.setNum(shelfIdepotItem.getNormalNum());
					subtractGoodsListJsonList.add(subtractGoods);
				}
				if (shelfIdepotItem.getDamagedNum() != null && shelfIdepotItem.getDamagedNum().intValue() > 0) {
					InvetAddOrSubtractGoodsListJson subtractGoods2 = new InvetAddOrSubtractGoodsListJson();
					BeanUtils.copyProperties(subtractGoods, subtractGoods2);
					subtractGoods2.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);// 坏品
					subtractGoods2.setNum(shelfIdepotItem.getDamagedNum());
					subtractGoodsListJsonList.add(subtractGoods2);
				}
			} // 上架入库表体002----------end

		} // 上架表体001-----------end
		subtractRootJson.setGoodsList(subtractGoodsListJsonList);
		// 库存回推数据
		Map<String, Object> customParam = new HashMap<String, Object>();
		subtractRootJson.setBackTags(MQPushBackOrderEnum.SALE_SHELF_IN_DEPOT_PUSH_BACK.getTags());// 回调标签
		subtractRootJson.setBackTopic(MQPushBackOrderEnum.SALE_SHELF_IN_DEPOT_PUSH_BACK.getTopic());// 回调主题
		customParam.put("code", saleShelfIdepotModel.getCode());// 上架入库单号
		subtractRootJson.setCustomParam(customParam);// 自定义回调参数

		return subtractRootJson;
	}

	/**
	 * -上架导入和页面上架共用--分配上架量到出库批次上封装上架入库明细返回 type 1-好品 2-坏品 allotNumAllMap
	 * 存储分配好的上架入库明细 key=出库id+批次号+生产日+失效日期 value=SaleShelfIdepotItemModel
	 */
	private void allotNum(int shelNum, String type, Map<String, SaleShelfIdepotItemModel> allotNumAllMap,
			List<Map<String, Object>> outDepotItemList) {
		if (shelNum <= 0)
			return;
		// 循环将上架量分配到出库批次上
		for (Map<String, Object> outDepotItem : outDepotItemList) {
			int outnum = (int) outDepotItem.get("outnum");// 可分配出库余量
			Long outDepotId = (Long) outDepotItem.get("id");// 出库表体id
			String transferBatchNo = (String) outDepotItem.get("transfer_batch_no");
			Date productionDate = (Date) outDepotItem.get("production_date");
			Date overdueDate = (Date) outDepotItem.get("overdue_date");
			Long saleItemId = (Long) outDepotItem.get("sale_item_id");// 销售明细id
			String key = outDepotId + transferBatchNo + TimeUtils.formatDay(productionDate)+ TimeUtils.formatDay(overdueDate);
			if (outnum <= 0)
				continue;
			if (shelNum <= 0)
				continue;

			int benNum = 0;// 本次分配数量
			// 1.上架量<=出库可分配量则本次分配数量为上架量
			if (shelNum <= outnum) {
				benNum = shelNum;
			}
			// 2.上架量>出库可分配量则本次分配数量为出库量余量
			else {
				benNum = outnum;
			}
			outnum = outnum - benNum;// 剩余出库余量
			shelNum = shelNum - benNum;// 剩余未分配上架量
			outDepotItem.put("outnum", outnum);

			SaleShelfIdepotItemModel idepotItem = null;
			if (allotNumAllMap.containsKey(key)) {
				idepotItem = allotNumAllMap.get(key);
				Integer normalNum = idepotItem.getNormalNum() == null ? 0 : idepotItem.getNormalNum();
				Integer damagedNum = idepotItem.getDamagedNum() == null ? 0 : idepotItem.getDamagedNum();
				if (type.equals("1")) {
					idepotItem.setNormalNum(benNum + normalNum);// 好品
				} else {
					idepotItem.setDamagedNum(benNum + damagedNum);// 坏品
				}
			} else {
				// 拼装上架入库表体
				idepotItem = new SaleShelfIdepotItemModel();
				idepotItem.setSaleOutDepotItemId(outDepotId);
				idepotItem.setBatchNo(transferBatchNo);
				idepotItem.setProductionDate(productionDate);
				idepotItem.setOverdueDate(overdueDate);
				idepotItem.setSaleItemId(saleItemId);
				if (type.equals("1")) {
					idepotItem.setNormalNum(benNum);// 好品
				} else {
					idepotItem.setDamagedNum(benNum);// 坏品
				}
			}
			allotNumAllMap.put(key, idepotItem);
		}
	}

	@Override
	public List<PendingShelfSaleOrderVo> getPendingShelfSaleOrders(Map<String, Object> map) throws SQLException {
		return saleOrderDao.getPendingShelfSaleOrders(map);
	}

	@Override
	public List<PendingCheckOrderVo> getPendingCheckOrders(Map<String, Object> map) throws SQLException {
		return saleOrderDao.getPendingCheckOrders(map);
	}

	@Override
	public Integer countPendingCheckOrders(Map<String, Object> map) throws SQLException {
		return saleOrderDao.countPendingCheckOrders(map);
	}

	@Override
	public Integer countPendingShelfOrders(Map<String, Object> map) throws SQLException {
		return saleOrderDao.countPendingShelfOrders(map);
	}

	@Override
	public SaleOrderDTO saleGetListSaleOrderByPage(SaleOrderDTO dto, String codes, User user) throws SQLException {
		List<String> codeList = null;
		if (!StringUtils.isEmpty(codes)) {
			codeList = Arrays.asList(codes.split(","));
		}
		dto.setCodeList(codeList);
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		dto = saleOrderDao.saleGetListSaleOrderByPage(dto);
		return dto;
	}

	/**
	 * 预售转销--》访问预售单编辑页面
	 *
	 * @param json
	 * @return
	 */
	@Override
	public SaleOrderDTO preSaleOrderToSaleEdit(String json) {
		// 解析json
		JSONObject jsonObj = JSONObject.fromObject(json);

		Long customerId = Long.valueOf(jsonObj.getString("customerId"));
		String businessModel = String.valueOf(jsonObj.get("businessModel"));
		Long outDepotId = Long.valueOf(jsonObj.getString("outDepotId"));
		Long buId = Long.valueOf(jsonObj.getString("buId"));
		String preSaleOrderCode = String.valueOf(jsonObj.get("preSaleOrderCode"));
		String poNo = String.valueOf(jsonObj.get("poNo"));
		Long merchantId = Long.valueOf(jsonObj.getString("merchantId"));
		String merchantName = String.valueOf(jsonObj.get("merchantName"));
		String preSaleOrderIds = String.valueOf(jsonObj.get("ids"));// 关联的预售单id

		SaleOrderDTO saleOrderDTO = new SaleOrderDTO();
		saleOrderDTO.setPreSaleOrderIds(preSaleOrderIds);
		saleOrderDTO.setCustomerId(customerId);
		saleOrderDTO.setBusinessModel(businessModel);
		saleOrderDTO.setOutDepotId(outDepotId);
		saleOrderDTO.setBuId(buId);
		saleOrderDTO.setPreSaleOrderRelCode(preSaleOrderCode);
		saleOrderDTO.setPoNo(poNo);
		saleOrderDTO.setOrderType(DERP_ORDER.SALEORDER_ORDERTYPE_1);// 单据标识 1 预售转销 2 非预售转销
		saleOrderDTO.setMerchantId(merchantId);
		saleOrderDTO.setMerchantName(merchantName);

		List<SaleOrderItemDTO> itemList = new ArrayList<>();
		// 解析表体数据
		Double billWeight = 0.0;
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		for (int i = 0; i < itemArr.size(); i++) {
			SaleOrderItemDTO itemModel = new SaleOrderItemDTO();
			JSONObject job = itemArr.getJSONObject(i);
			Long goodsId = Long.valueOf(job.getString("id")); // 商品ID
			String priceStr = String.valueOf(job.get("price"));
			BigDecimal priceb = new BigDecimal(0.0);
			if (StringUtils.isNotBlank(priceStr)) {
				priceb = new BigDecimal(priceStr);
			}
			Integer num = Integer.valueOf((String) job.get("num"));// 本次销售数量
			String brandName = String.valueOf(job.get("brandName"));

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", goodsId);// 商品id
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(params);

			if (merchandiseInfoMogo != null) {
				itemModel.setGrossWeight(merchandiseInfoMogo.getGrossWeight());
				itemModel.setNetWeight(merchandiseInfoMogo.getNetWeight());
				itemModel.setGrossWeightSum(merchandiseInfoMogo.getGrossWeight() * num);
				itemModel.setNetWeightSum(
						merchandiseInfoMogo.getNetWeight() == null ? 0.0 : merchandiseInfoMogo.getNetWeight() * num);
				billWeight = billWeight + (merchandiseInfoMogo.getGrossWeight() == null ? 0.0
						: merchandiseInfoMogo.getGrossWeight() * num);// 提单毛重
			}
			itemModel.setGoodsId(goodsId);
			itemModel.setGoodsName(merchandiseInfoMogo.getName());
			itemModel.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
			itemModel.setGoodsCode(merchandiseInfoMogo.getGoodsCode());
			itemModel.setBarcode(merchandiseInfoMogo.getBarcode());
			itemModel.setBrandName(brandName);
			itemModel.setPrice(priceb);
			itemModel.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
			itemModel.setNum(num);// 本次销售数量
			// 求总金额
			BigDecimal amountBd = new BigDecimal(num).multiply(priceb);
			itemModel.setAmount(amountBd);
			itemList.add(itemModel);
		}
		saleOrderDTO.setBillWeight(billWeight);// 提单毛重
		saleOrderDTO.setItemList(itemList);
		return saleOrderDTO;
	}

	/**
	 * 新增内部销售单--带出采购单某些信息以及选择的销售出库仓
	 *
	 * @param purchaseId
	 * @param outDepotId
	 * @return
	 */
	@Override
	public SaleOrderDTO getSaleOrderByPurchaseId(Long purchaseId, Long outDepotId, Long merchantId, String merchantName)
			throws SQLException {
		// 带出勾选采购单中的客户、事业部、PO号、销售出库仓、参照订单，销售类型为购销-整批结算、当出库仓为卓志保税仓时，关区默认为跨关区
		PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(purchaseId);
		// 查询商家
		Map<String, Object> merchantMap = new HashMap<String, Object>();
		merchantMap.put("merchantId", purchaseOrderModel.getMerchantId());// 采购单的商家id
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);

		Map<String, Object> cusParams = new HashMap<String, Object>();
		cusParams.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_1);// 类型：客户
		cusParams.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1);// 1-使用中
		cusParams.put("type", DERP_SYS.CUSTOMERINFO_TYPE_1);// 客户类型 1内部
		cusParams.put("mainId", merchant.getTopidealCode());// 主数据客户id(采购单商家的卓志编码)
		List<CustomerInfoMogo> customerList = customerInfoMongoDao.findAll(cusParams);
		if (customerList == null || customerList.size() == 0) {
			LOGGER.info("卓志编码:" + merchant.getTopidealCode() + "客户信息表没找到对应信息");
			throw new RuntimeException("卓志编码:" + merchant.getTopidealCode() + "客户信息表没找到对应信息");
		} else if (customerList.size() > 1) {
			LOGGER.info("卓志编码:" + merchant.getTopidealCode() + "客户信息表找到多条对应信息");
			throw new RuntimeException("卓志编码:" + merchant.getTopidealCode() + "客户信息表找到多条对应信息");
		}
		cusParams.clear();
		CustomerInfoMogo customerInfoMogo = customerList.get(0);
		cusParams.put("customerId", customerInfoMogo.getCustomerid());
		cusParams.put("merchantId", merchantId);// 当前登录的公司
		cusParams.put("status", "1");
		CustomerMerchantRelMongo customerMerchantRelMongo = customerMerchantRelMongoDao.findOne(cusParams);
		if (customerMerchantRelMongo == null) {
			LOGGER.info("客户:" + customerInfoMogo.getName() + "在客户信息关联表没找到对应信息");
			throw new RuntimeException("客户:" + customerInfoMogo.getName() + "在客户信息关联表没找到对应信息");
		}

		SaleOrderDTO saleOrderDTO = new SaleOrderDTO();
		saleOrderDTO.setMerchantId(merchantId);
		saleOrderDTO.setMerchantName(merchantName);
		saleOrderDTO.setCustomerId(customerMerchantRelMongo.getCustomerId());// 客户id
		saleOrderDTO.setBuId(purchaseOrderModel.getBuId());
		saleOrderDTO.setPoNo(purchaseOrderModel.getPoNo());
		saleOrderDTO.setOutDepotId(outDepotId);
		saleOrderDTO.setReferToOrder(purchaseOrderModel.getCode());// 参照订单
		saleOrderDTO.setBusinessModel(DERP_ORDER.SALEORDER_BUSINESSMODEL_1);// 销售类型为购销-整批结算

		// 采购单商品
		PurchaseOrderItemModel purchaseOrderItemModel = new PurchaseOrderItemModel();
		purchaseOrderItemModel.setPurchaseOrderId(purchaseId);
		List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(purchaseOrderItemModel);

		// 查询入库仓库公司关联信息
		Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
		depotMerchantRelParam.put("merchantId", merchantId);
		depotMerchantRelParam.put("depotId", outDepotId); // 销售出库仓
		DepotMerchantRelMongo inDepotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
		if (inDepotMerchantRelMongo == null || "".equals(inDepotMerchantRelMongo)) {
			LOGGER.info("销售出库仓ID：" + outDepotId + ",未查到该公司下入库仓库信息");
			throw new RuntimeException("销售出库仓ID：" + outDepotId + ",未查到该公司下入库仓库信息");
		}
		List<SaleOrderItemDTO> saleOrderItemList = new ArrayList<>();
		// 遍历采购商品
		for (int i = 0; i < itemList.size(); i++) {
			SaleOrderItemDTO saleOrderItemDTO = new SaleOrderItemDTO();
			PurchaseOrderItemModel itemModel = itemList.get(i);// 采购单商品
			// 商品货号+采购订单的公司查询商品信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("goodsNo", itemModel.getGoodsNo());
			params.put("merchantId", purchaseOrderModel.getMerchantId());// 采购单公司
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(params);

			Map<String, Object> paramMap = new HashMap<String, Object>();
			List<MerchandiseInfoMogo> inMerchandiseList = new ArrayList<>();
			// 若选品限制是备案商品
			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1
					.equals(inDepotMerchantRelMongo.getProductRestriction())) {
				paramMap.clear();
				paramMap.put("commbarcode", merchandiseInfoMogo.getCommbarcode());
				paramMap.put("isRecord", DERP_SYS.MERCHANDISEINFO_ISRECORD_1);// 是备案商品
				paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1); // 状态(1使用中,0已禁用)
				paramMap.put("merchantId", merchantId);
				inMerchandiseList = merchandiseInfoMogoDao.findAll(paramMap);
			}
			// 若选品限制是外仓统一码
			else if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2
					.equals(inDepotMerchantRelMongo.getProductRestriction())) {
				paramMap.clear();
				paramMap.put("commbarcode", merchandiseInfoMogo.getCommbarcode());
				paramMap.put("outDepotFlag", DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);// 是外仓统一码
				paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1); // 状态(1使用中,0已禁用)
				paramMap.put("merchantId", merchantId);
				inMerchandiseList = merchandiseInfoMogoDao.findAll(paramMap);
			}
			// 若无限制
			else if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_3
					.equals(inDepotMerchantRelMongo.getProductRestriction())) {
				paramMap.clear();
				paramMap.put("commbarcode", merchandiseInfoMogo.getCommbarcode());
				paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1); // 状态(1使用中,0已禁用)
				paramMap.put("merchantId", merchantId);
				inMerchandiseList = merchandiseInfoMogoDao.findAll(paramMap);
			}
			if (inMerchandiseList.size() > 1) {
				for (int j = 0; j < inMerchandiseList.size(); j++) {
					saleOrderItemDTO = new SaleOrderItemDTO();
					MerchandiseInfoMogo merchandise = inMerchandiseList.get(j);
					if (merchandise != null && merchandise.getBrandId() != null) {
						Map<String, Object> params3 = new HashMap<String, Object>();
						params3.put("brandId", merchandise.getBrandId());
						BrandMongo brandMongo = brandMongoDao.findOne(params3);
						if (brandMongo != null) {
							saleOrderItemDTO.setBrandName(brandMongo.getName());// 品牌
						}
					}
					saleOrderItemDTO.setGoodsId(merchandise.getMerchandiseId());
					saleOrderItemDTO.setGoodsNo(merchandise.getGoodsNo());
					saleOrderItemDTO.setGoodsCode(merchandise.getGoodsCode());
					saleOrderItemDTO.setGoodsName(merchandise.getName());
					saleOrderItemDTO.setBarcode(merchandise.getBarcode());
					saleOrderItemDTO.setPrice(itemModel.getPrice());
					saleOrderItemDTO.setNum(itemModel.getNum());// 采购单商品数量
					saleOrderItemDTO.setAmount(itemModel.getPrice().multiply(new BigDecimal(itemModel.getNum())));// 总金额

					// 带上备案价格
					saleOrderItemDTO.setFilingPrice(merchandise.getFilingPrice());
					// 带上毛重净重
					saleOrderItemDTO.setGrossWeight(itemModel.getGrossWeight());
					saleOrderItemDTO.setNetWeight(itemModel.getNetWeight());

					saleOrderItemList.add(saleOrderItemDTO);
				}
			} else if (inMerchandiseList.size() == 1) {
				MerchandiseInfoMogo merchandise = inMerchandiseList.get(0);
				if (merchandise != null && merchandise.getBrandId() != null) {
					Map<String, Object> params3 = new HashMap<String, Object>();
					params3.put("brandId", merchandise.getBrandId());
					BrandMongo brandMongo = brandMongoDao.findOne(params3);
					if (brandMongo != null) {
						saleOrderItemDTO.setBrandName(brandMongo.getName());// 品牌
					}
				}

				// 带上备案价格
				saleOrderItemDTO.setFilingPrice(merchandise.getFilingPrice());
				// 带上毛重净重
				saleOrderItemDTO.setGrossWeight(itemModel.getGrossWeight());
				saleOrderItemDTO.setNetWeight(itemModel.getNetWeight());

				saleOrderItemDTO.setGoodsId(merchandise.getMerchandiseId());
				saleOrderItemDTO.setGoodsNo(merchandise.getGoodsNo());
				saleOrderItemDTO.setGoodsCode(merchandise.getGoodsCode());
				saleOrderItemDTO.setGoodsName(merchandise.getName());
				saleOrderItemDTO.setBarcode(merchandise.getBarcode());
				saleOrderItemDTO.setPrice(itemModel.getPrice());
				saleOrderItemDTO.setNum(itemModel.getNum());// 采购单商品数量
				saleOrderItemDTO.setAmount(itemModel.getPrice().multiply(new BigDecimal(itemModel.getNum())));// 总金额
				saleOrderItemList.add(saleOrderItemDTO);
			}
		}
		saleOrderDTO.setOwnSaleType("1");// 是新增内部销售单
		saleOrderDTO.setItemList(saleOrderItemList);
		return saleOrderDTO;
	}

	/**
	 * 根据“供应商-公司-客户”查询是否存在加价比例设置
	 *
	 * @param saleIds
	 * @param supplierId
	 * @return
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> checkPurchaseCommission(String saleIds, Long supplierId, User user) throws RuntimeException, SQLException {
		Map<String, Object> resultMap = new HashMap<>();
		Set<Long> checkCustomerSet = new HashSet<>();
		String[] idArr = saleIds.split(",");
		int isSameCustomer = 0;// 0：客户不同 1：客户相同
		Long customerId = null;

		for (int i = 0; i < idArr.length; i++) {
			SaleOrderModel saleOrder = saleOrderDao.searchById(Long.parseLong(idArr[i]));
			if (checkCustomerSet.contains(saleOrder.getCustomerId())) {
				isSameCustomer = 1;
			} else {
				isSameCustomer = 0;
				customerId = saleOrder.getCustomerId();
				checkCustomerSet.add(customerId);
			}
		}
		if (idArr.length == 1) {
			isSameCustomer = 1;
		}
		if (isSameCustomer == 1) {
			// “供应商-公司-客户”查询是否存在加价比例设置
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchantId", user.getMerchantId());
			params.put("configType", DERP_SYS.PURCHASE_COMMISSION_CONFIGTYPE_2);// 2-公司加价比例
			params.put("customerId", customerId);
			params.put("supplierId", supplierId);
			PurchaseCommissionMongo purchaseCommissionMongo = purchaseCommissionMongoDao.findOne(params);
			if (purchaseCommissionMongo != null) { // 存在配置
				resultMap.put("purchaseCommission", purchaseCommissionMongo.getPurchaseCommission());
			} else {
				resultMap.put("purchaseCommission", 0);
			}
		} else {
			resultMap.put("purchaseCommission", 0);
		}
		return resultMap;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, Object> modifyPlatFormPurchaseToSales(String platformPurchaseIds, String outDepotId, String buId,String platformSalesNum, User user) throws Exception {
		Map<String, Object> salesReturnMap = new HashMap<String, Object>();
		// PO号字符串
		List<String> poSb = new ArrayList<String>();
		/** 拆分前端输入转销数量 前端platformBarcode_toSalesNum; */
		Map<String, Integer> itemsToSalesNumMap = new HashMap<String, Integer>();
		Map<String, BigDecimal> itemsPriceMap = new HashMap<String, BigDecimal>();
		if (StringUtils.isNotBlank(platformSalesNum)) {
			String[] tempArr = platformSalesNum.split(",");
			for (String goodsIdToSalesNum : tempArr) {
				String[] arr = goodsIdToSalesNum.split("__");

				String saleGoodsNo = arr[0];
				String toSalesNum = arr[1];
				String poNo = arr[2];
				String price = arr[3];

				Integer tempSalesNum = Integer.valueOf(toSalesNum);
				if (itemsToSalesNumMap.get(saleGoodsNo) != null) {
					tempSalesNum += itemsToSalesNumMap.get(saleGoodsNo);
				}
				itemsToSalesNumMap.put(saleGoodsNo, tempSalesNum);

				if(!poSb.contains(poNo)) {
					poSb.add(poNo);
				}

				if(!itemsPriceMap.containsKey(saleGoodsNo)) {
					itemsPriceMap.put(saleGoodsNo, new BigDecimal(price));
				}
			}
		}

		SaleOrderDTO returnDto = new SaleOrderDTO();
		returnDto.setBuId(Long.valueOf(buId));
		returnDto.setOutDepotId(Long.valueOf(outDepotId));
		returnDto.setMerchantId(user.getMerchantId());
		returnDto.setMerchantName(user.getMerchantName());
		returnDto.setPoNo(StringUtils.join(poSb, "&"));

		List<Long> idsList = (List<Long>) StrUtils.parseIds(platformPurchaseIds);
		PlatformPurchaseOrderModel platformPurchaseOrder = platformPurchaseOrderDao.searchById(idsList.get(0));
		returnDto.setCustomerId(platformPurchaseOrder.getCustomerId());
		returnDto.setCurrency(platformPurchaseOrder.getCurrency());

		BigDecimal taxRate = BigDecimal.ZERO;
		HashMap<String, Object> customerMerchantRelMap = new HashMap<>();
		customerMerchantRelMap.put("merchantId", user.getMerchantId());
		customerMerchantRelMap.put("customerId", platformPurchaseOrder.getCustomerId());
		customerMerchantRelMap.put("status", DERP_SYS.CUSTOMER_MERCHANT_REL_status1);
		CustomerMerchantRelMongo customerMerchantRelMongo = customerMerchantRelMongoDao.findOne(customerMerchantRelMap);
		if (customerMerchantRelMongo != null) {
			//若 出库仓为保税仓、香港仓，客户配置的销售类型为采销执行时，不取客户表配置的销售类型，设为默认值 购销-整批结算
			Map<String, Object> depotParam = new HashMap<>();
			depotParam.put("depotId", returnDto.getOutDepotId());
			DepotInfoMongo outDepotInfo = depotInfoMongoDao.findOne(depotParam);
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepotInfo.getType())
					|| DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepotInfo.getType()))
					&& DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(customerMerchantRelMongo.getBusinessModel()) ) {
				returnDto.setBusinessModel(DERP_ORDER.SALEORDER_BUSINESSMODEL_1);
			}else {
				returnDto.setBusinessModel(customerMerchantRelMongo.getBusinessModel());
			}
			taxRate = customerMerchantRelMongo.getRate();
		}else {
			returnDto.setBusinessModel(DERP_ORDER.SALEORDER_BUSINESSMODEL_1);
		}

		List<SaleOrderItemDTO> saleOrderItemList = new ArrayList<SaleOrderItemDTO>();
		int seq = 1;
		for (String goodsNo : itemsToSalesNumMap.keySet()) {
			Integer toSalesNum = itemsToSalesNumMap.get(goodsNo);

			Map<String, Object> merchandiseInfoParams = new HashMap<String, Object>();
			merchandiseInfoParams.put("merchantId", user.getMerchantId());
			merchandiseInfoParams.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
			merchandiseInfoParams.put("goodsNo", goodsNo);
			List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findAll(merchandiseInfoParams);
			if (merchandiseList == null || merchandiseList.isEmpty()) {
				throw new RuntimeException("转销售货号："+goodsNo+" 找不到对应商品");
			}
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseList.get(0);

			BigDecimal price = itemsPriceMap.get(goodsNo);

			SaleOrderItemDTO saleOrderItemDTO = new SaleOrderItemDTO();
			saleOrderItemDTO.setBarcode(merchandiseInfoMogo.getBarcode());
			saleOrderItemDTO.setGoodsCode(merchandiseInfoMogo.getGoodsCode());
			saleOrderItemDTO.setGoodsId(merchandiseInfoMogo.getMerchandiseId());
			saleOrderItemDTO.setGoodsName(merchandiseInfoMogo.getName());
			saleOrderItemDTO.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
			saleOrderItemDTO.setGrossWeight(merchandiseInfoMogo.getGrossWeight());
			saleOrderItemDTO.setNetWeight(merchandiseInfoMogo.getNetWeight());
			saleOrderItemDTO.setNum(toSalesNum);
			saleOrderItemDTO.setPrice(price);

			BigDecimal amount = new BigDecimal(toSalesNum).multiply(price);
			BigDecimal taxAmount = amount.multiply(taxRate.add(BigDecimal.ONE));
			saleOrderItemDTO.setAmount(amount);
			saleOrderItemDTO.setTaxAmount(taxAmount);
			saleOrderItemDTO.setTax(taxAmount.subtract(amount));
			saleOrderItemDTO.setTaxRate(taxRate.doubleValue());

			if (saleOrderItemDTO.getGrossWeight() != null) {
				BigDecimal grossWeightSum = new BigDecimal(saleOrderItemDTO.getGrossWeight()).multiply(new BigDecimal(toSalesNum)).setScale(5, BigDecimal.ROUND_HALF_UP);
				saleOrderItemDTO.setGrossWeightSum(grossWeightSum.doubleValue());
			}
			if (saleOrderItemDTO.getNetWeight() != null) {
				BigDecimal netWeightSum = new BigDecimal(saleOrderItemDTO.getNetWeight()).multiply(new BigDecimal(toSalesNum)).setScale(5, BigDecimal.ROUND_HALF_UP);
				saleOrderItemDTO.setNetWeightSum(netWeightSum.doubleValue());
			}
			saleOrderItemDTO.setSeq(seq);
			seq++;
			saleOrderItemList.add(saleOrderItemDTO);
		}
		//排序
		saleOrderItemList = saleOrderItemList.stream().sorted(Comparator.comparing(SaleOrderItemDTO::getSeq)).collect(Collectors.toList());
		salesReturnMap.put("saleOrderDTO", returnDto);
		salesReturnMap.put("saleOrderItemList", saleOrderItemList);

		return salesReturnMap;
	}

	/*
	 * 商品导入
	 */
	@Override
	public Map importSaleGoods(List<List<Map<String, String>>> data, User user, SaleOrderDTO dto,String salePriceManage) {
		List<Map<String, String>> msgList = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> stringList = new ArrayList<Map<String, Object>>();
		List<String> idList = new ArrayList<>();
		ArrayList<String> containerList = new ArrayList<>(); // 存一次导入时序号的集合
		ArrayList<String> containerGoodsList = new ArrayList<>(); // 存一次导入时货号的集合
		// 检查某个销售订单编号+该仓库公司关联的信息
		Map<String, DepotMerchantRelMongo> checkGoodsNoByDepotMap = new HashMap<>();
		Map<String, BigDecimal> customerSalePriceMongoMap = new HashMap<>();
		Map<String, List<String>> salePriceMap = new HashMap<>();
		Integer success = 0;
		Integer pass = 0;
		Integer failure = 0;
		List<Map<String, String>> objects = data.get(0);
		BigDecimal price = null;
		if(dto.getOutDepotId() == null){
			throw new RuntimeException("请选择出库仓");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depotId", dto.getOutDepotId());
		DepotInfoMongo outDepotInfo = depotInfoMongoDao.findOne(params);
		// 仓库公司关联表
		Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
		depotMerchantRelParam.put("merchantId", user.getMerchantId());
		depotMerchantRelParam.put("depotId", outDepotInfo.getDepotId());
		DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
		if (depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)) {
			throw new RuntimeException("仓库ID为：" + outDepotInfo.getDepotId() + ",未查到该公司下调出仓库信息");
		}
		for (int j = 0; j < objects.size(); j++) {
			boolean isFlag = true;
			Map<String, String> msg = new HashMap<String, String>();
			Map<String, String> map = objects.get(j);
			String seq = map.get("序号");
			seq = seq.trim();
			if (seq == null || "".equals(seq)) {
				isFlag = false;
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "存在序号为空的导入数据！");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if (!isInteger(seq)) {
				isFlag = false;
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "存在非数值的序号！");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if (containerList.contains(seq)) {
				isFlag = false;
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "存在序号重复的导入数据！");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			containerList.add(seq);
			String goodsNo = map.get("商品货号");
			goodsNo = goodsNo.trim();
			if (goodsNo == null || "".equals(goodsNo)) {
				isFlag = false;
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "商品货号不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			String num = map.get("销售数量");
			num = num.trim();
			if (num == null || "".equals(num)) {
				isFlag = false;
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "销售数量不能为空");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			if (!StringUtils.isNumeric(num)) {
				isFlag = false;
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "销售数量只能是整数");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			String amount = map.get("销售总金额");
			//开启销售价格管理，销售金额非必填
			if (StringUtils.isBlank(amount)) {
				isFlag = false;
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "销售总金额不能为空！");
				msgList.add(msg);
				failure += 1;
				continue;
			} else{
				int indexOf = amount.indexOf(".");
				// 如果是小数
				if (indexOf != -1) {
					int count = amount.length() - 1 - indexOf;
					if (count > 2) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "商品货号：" + goodsNo + "，销售总金额小数点后只能为两位数");
						msgList.add(msg);
						failure += 1;
						continue;
					}
				}
				if (new BigDecimal(amount).compareTo(BigDecimal.ZERO) < 0) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "商品货号：" + goodsNo + "，销售总金额不能为负数");
					msgList.add(msg);
					failure += 1;
					continue;
				}
			}

			HashMap<String, Object> goodsMap = new HashMap<>();
			goodsMap.put("goodsNo", goodsNo);
			String barcode = map.get("条形码");
			barcode = barcode.trim();
			String barcodeStr = "";
			if (StringUtils.isNotBlank(barcode)) {
				goodsMap.put("barcode", barcode);
				barcodeStr += " + 条形码";
			}
			goodsMap.put("merchantId", user.getMerchantId());
			goodsMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
			List<MerchandiseInfoMogo> goodsList = merchandiseInfoMogoDao.findMerchandiseByDepotId(goodsMap,dto.getOutDepotId());
			if (goodsList == null || goodsList.size() < 1) {
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "导入的商品货号" + barcodeStr + "没有关联此出仓仓库！");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			MerchandiseInfoMogo goods = goodsList.get(0);

			price = new BigDecimal(amount).divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);
			if ("true".equals(salePriceManage)) {
				HashMap<String, Object> customerSalePriceMap = new HashMap<>();
				customerSalePriceMap.put("merchantId", user.getMerchantId());
				customerSalePriceMap.put("customerId", dto.getCustomerId());
				customerSalePriceMap.put("commbarcode", goods.getCommbarcode());
				customerSalePriceMap.put("currency", dto.getCurrency());
				customerSalePriceMap.put("buId", dto.getBuId());
				customerSalePriceMap.put("status", DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003);
				List<CustomerSalePriceMongo> mList = customerSalePriceMongoDao.findAll(customerSalePriceMap);

				if (mList.isEmpty()) {
					msg.put("row", String.valueOf(j + 1));
					msg.put("msg", "该公司事业部已开启销售价格管理，货号 ：" + goodsNo + " 标准条码在销售价格管理无数据");
					msgList.add(msg);
					failure += 1;
					continue;
				}
				//箱托数量，默认为1
				Integer tempNum = 1;
				// 理货单位不为空，且不等于件，校验是否存在箱、托、件 转换数据
				if (StringUtils.isNotBlank(dto.getTallyingUnit()) && !DERP.ORDER_TALLYINGUNIT_02.equals(dto.getTallyingUnit())) {
					if (goods.getTorrToUnit() < 1 && DERP.ORDER_TALLYINGUNIT_00.equals(dto.getTallyingUnit())) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "商品货号：" + goods.getGoodsNo() + "托、件 转换数据未维护！");
						msgList.add(msg);
						failure += 1;
						continue;
					}
					if (goods.getBoxToUnit() < 1 && DERP.ORDER_TALLYINGUNIT_01.equals(dto.getTallyingUnit())) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "商品货号：" + goods.getGoodsNo() + "箱、件 转换数据未维护！");
						msgList.add(msg);
						failure += 1;
						continue;
					}

				}

				if (StringUtils.isNotBlank(dto.getTallyingUnit()) && !DERP.ORDER_TALLYINGUNIT_02.equals(dto.getTallyingUnit())) {
					if (DERP.ORDER_TALLYINGUNIT_01.equals(dto.getTallyingUnit()) && goods.getBoxToUnit() > 0) {
						tempNum = goods.getBoxToUnit();
					} else if (DERP.ORDER_TALLYINGUNIT_00.equals(dto.getTallyingUnit()) && goods.getTorrToUnit() > 0) {
						tempNum = goods.getTorrToUnit();
					}
				}

				List<String> priceList = new ArrayList<String>();
				if(salePriceMap.containsKey(seq+"_"+goodsNo)) {
					priceList = salePriceMap.get(seq+"_"+goodsNo);
				}
				//存储有效的销售价格*数量保留两位销售和页面传来总金额进行比较
				List<BigDecimal> salePriceAmountList=new ArrayList<>();
				Map<BigDecimal, BigDecimal> salePriceAmountPriceMap = new HashMap<>();
				for (CustomerSalePriceMongo tempMongo : mList) {
					if (TimeUtils.parseFullTime(tempMongo.getEffectiveDate() + " 00:00:00").getTime() <= TimeUtils.getNow().getTime()
							&& TimeUtils.parseFullTime(tempMongo.getExpiryDate() + " 23:59:59").getTime() >= TimeUtils.getNow().getTime()) {
						BigDecimal tempPrice = tempMongo.getSalePrice().multiply(new BigDecimal(tempNum));//计算箱托之后的价格
						BigDecimal totalSaleAmount= tempPrice.multiply(new BigDecimal(num)).setScale(2, BigDecimal.ROUND_HALF_UP);
						salePriceAmountList.add(totalSaleAmount);

						String temPrice = tempPrice.setScale(8, BigDecimal.ROUND_HALF_UP).toString();
						if(!priceList.contains(temPrice)) {
							priceList.add(temPrice);
						}
						salePriceAmountPriceMap.put(totalSaleAmount, tempMongo.getSalePrice().setScale(8, BigDecimal.ROUND_HALF_UP));
					}
				}
				BigDecimal amountBig=new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP);

				//查询导入的销售价格是否存在有效的销售价格管理里面
				Boolean flagPrice=salePriceAmountList.stream().anyMatch(saleAmount ->amountBig.compareTo(saleAmount)==0);
				if(!flagPrice) {
					msg.put("row", String.valueOf(j + 2));
					msg.put("msg", "该公司事业部已开启销售价格管理，货号 ：" + goods.getGoodsNo() + " 销售单价在销售价格管理不存在");
					msgList.add(msg);
					failure += 1;
					continue;
				}
//				price = amountBig.divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);;
				price = salePriceAmountPriceMap.get(amountBig);
				salePriceMap.put(seq+"_"+goodsNo, priceList);
			}
			customerSalePriceMongoMap.put(seq+"_"+goodsNo, price);

		}

		if (failure == 0) {
			for (int j = 0; j < objects.size(); j++) {
				Map<String, String> map = objects.get(j);
				String seq = map.get("序号");
				seq = seq.trim();
				String goodsNo = map.get("商品货号");
				goodsNo = goodsNo.trim();
				String num = map.get("销售数量");
				num = num.trim();
				String amount = map.get("销售总金额");
				amount = amount.trim();
				String barcode = map.get("条形码");
				barcode = barcode.trim();

				Map<String, Object> stringMap = new HashMap<String, Object>();
				stringMap.put("seq", seq);
				stringMap.put("goodsNo", goodsNo);
				stringMap.put("num", num);
				HashMap<String, Object> goodsMap = new HashMap<>();
				goodsMap.put("goodsNo", goodsNo);
				goodsMap.put("merchantId", user.getMerchantId());
				MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(goodsMap);

				/**
				 * 查询商品是否绑定仓库
				 */
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("goodsNo", goodsNo);
				paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);	// 状态(1使用中,0已禁用)
				paramMap.put("merchantId", user.getMerchantId());
				List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(paramMap, depotMerchantRelMongo.getDepotId());
				if(merchandiseList == null || merchandiseList.size() < 1){
					goodsNo = "未关联出仓仓库";
				}

				price = customerSalePriceMongoMap.get(seq+"_"+goodsNo);
				List<String> priceList = new ArrayList<String>();
				if("true".equals(salePriceManage)) {

					priceList =salePriceMap.get(seq+"_"+goodsNo);
				}

				BigDecimal taxRate = BigDecimal.ZERO;
				BigDecimal tax = BigDecimal.ZERO;
				BigDecimal taxAmount = BigDecimal.ZERO;

				HashMap<String, Object> customerMerchantRelMap = new HashMap<>();
				customerMerchantRelMap.put("merchantId", user.getMerchantId());
				customerMerchantRelMap.put("customerId", dto.getCustomerId());
				customerMerchantRelMap.put("status", DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003);
				CustomerMerchantRelMongo customerMerchantRelMongo = customerMerchantRelMongoDao.findOne(customerMerchantRelMap);
				if (customerMerchantRelMongo != null) {
					taxRate = customerMerchantRelMongo.getRate();
					tax = taxAmount.subtract(new BigDecimal(amount));
					tax = tax.setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				taxAmount = new BigDecimal(amount).multiply(taxRate.add(BigDecimal.ONE));
				taxAmount = taxAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

				stringMap.put("price", price.toPlainString());
				stringMap.put("amount", amount);
				stringMap.put("taxRate", taxRate.toString());
				stringMap.put("tax", tax.toString());
				stringMap.put("taxAmount", taxAmount.toString());
				stringMap.put("goodsId", goods.getMerchandiseId() + "");
				stringMap.put("priceList", priceList);
				stringList.add(stringMap);

				idList.add(goods.getMerchandiseId() + "");
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msgList", msgList);
		map.put("idList", idList);
		map.put("stringList", stringList);
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		return map;
	}

	public static boolean isInteger(String str) {
		try {
			Integer.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// 金额调整
	public boolean amountAdjust(String json, User user) throws SQLException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		SaleOrderModel saleOrderModel = new SaleOrderModel();
		// 解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		// 判断传过来的id是否为空
		String idStr = jsonObj.getString("orderId");
		String currency = jsonObj.getString("currency");
		String totalAmount = jsonObj.getString("totalAmount");
		totalAmount = totalAmount.trim();
		if (StringUtils.isNotBlank(idStr)) {
			Long orderId = Long.valueOf(idStr);
			saleOrderModel = saleOrderDao.searchById(orderId);
			if (saleOrderModel == null) {
				throw new RuntimeException("金额调整失败，销售订单不存在");
			}
			// 判断状态是不是已审核
			if (DERP_ORDER.SALEORDER_STATE_001.equals(saleOrderModel.getState())) {
				throw new RuntimeException("金额调整失败，该销售单待审核");
			}
			if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
				throw new RuntimeException("金额调整失败，该销售单红冲状态不为空");
			}
		}

		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		List<Long> itemIds = new ArrayList<Long>();
		for (int i = 0; i < itemArr.size(); i++) {
			JSONObject job = itemArr.getJSONObject(i);
			Long id = Long.valueOf(job.getString("id"));
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsCode = (String) job.get("goodsCode");
			String goodsNo = (String) job.get("goodsNo");

			String numStr = job.getString("num");
			Integer num = Integer.valueOf(numStr.trim());
			String price = (String) job.get("price");
			price = price.trim();
			String amount = (String) job.get("amount");
			amount = amount.trim();
			String taxAmount = (String) job.get("taxAmount");
			taxAmount = taxAmount.trim();

			BigDecimal tax = BigDecimal.ZERO;
			if (StringUtils.isNotBlank(taxAmount)) {
				tax = new BigDecimal(taxAmount).subtract(new BigDecimal(amount));
			}
			BigDecimal taxPrice = new BigDecimal(taxAmount).divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);
			// 注入数据
			SaleOrderItemModel itemModel = new SaleOrderItemModel();
			itemModel.setId(id);
			itemModel.setGoodsCode(goodsCode);
			itemModel.setGoodsId(goodsId);
			itemModel.setGoodsNo(goodsNo);
			itemModel.setNum(num);
			itemModel.setPrice(new BigDecimal(price));
			itemModel.setAmount(new BigDecimal(amount));
			itemModel.setTaxAmount(new BigDecimal(taxAmount));
			itemModel.setTax(tax);
			itemModel.setTaxPrice(taxPrice);
			saleOrderItemDao.modify(itemModel);

			//修改销售出库单 销售单价
			SaleOutDepotItemModel updateSaleOutItem = new SaleOutDepotItemModel();
			updateSaleOutItem.setSaleItemId(id);
			updateSaleOutItem.setPrice(new BigDecimal(price));
			saleOutDepotItemDao.modify(updateSaleOutItem);

			//修改销售预申报 销售单价
			SaleDeclareOrderItemModel updateSaleDeclareItem = new SaleDeclareOrderItemModel();
			updateSaleDeclareItem.setSaleItemId(id);
			updateSaleDeclareItem.setPrice(new BigDecimal(price));
			saleDeclareOrderItemDao.modify(updateSaleDeclareItem);
		}
		saleOrderModel.setCurrency(currency);
		saleOrderModel.setTotalAmount(new BigDecimal(totalAmount));
		// 对于标识“确认不通过”且进行了金额修改操作保存成功后，需更新确认标识为“待确认”
		if (saleOrderModel.getAmountConfirmStatus().equals(DERP_ORDER.SALEORDER_AMOUMT_CONFIRM_STATUS_2)) {
			saleOrderModel.setAmountConfirmStatus(DERP_ORDER.SALEORDER_AMOUMT_CONFIRM_STATUS_0);
		}
		saleOrderModel.setAmountStatus(DERP_ORDER.SALEORDER_AMOUMTSTATUS_1);
		saleOrderModel.setAdjuster(user.getId());
		saleOrderModel.setAdjusterUsername(user.getName());
		saleOrderModel.setAdjustDate(TimeUtils.parseFullTime(sf.format(new Date())));
		saleOrderModel.setModifyDate(TimeUtils.getNow());
		saleOrderModel.setModifier(user.getId());
		saleOrderModel.setModifierUsername(user.getName());
		int num = saleOrderDao.modify(saleOrderModel);

		// 若金额修改触发“确认”按钮则邮件提醒金额审核人；
		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
		mailDTO.setBusinessModuleType("3");
		mailDTO.setTypeName("销售");
		mailDTO.setType("10");// 1：提交 2：审核 3：上架 4：核销 5：开票 6：作废审核 7：盖章发票 8：审核完毕 9：作废完成 10：金额修改 11：金额确认
		mailDTO.setMerchantId(saleOrderModel.getMerchantId());
		mailDTO.setMerchantName(saleOrderModel.getMerchantName());
		mailDTO.setBuId(saleOrderModel.getBuId());
		mailDTO.setBuName(saleOrderModel.getBuName());
		mailDTO.setSupplier(saleOrderModel.getCustomerName());
		mailDTO.setOrderCode(saleOrderModel.getCode());
		mailDTO.setPoNum(saleOrderModel.getPoNo());
		mailDTO.setStatus("金额未确认");
		mailDTO.setSubmitId(Arrays.asList(String.valueOf(saleOrderModel.getSubmiter())));
		mailDTO.setAuditorId(saleOrderModel.getAuditor());
		mailDTO.setReviewerId(saleOrderModel.getAmountConfirmer());
		mailDTO.setModifyId(user.getId());
		mailDTO.setUserName(user.getName());
		ShelfModel shelfModel= new ShelfModel();
		shelfModel.setSaleOrderId(saleOrderModel.getId());
		List<ShelfModel> shelfList = shelfDao.list(shelfModel);
		if(shelfList != null && shelfList.size() > 0) {
			mailDTO.setShelvesId(shelfList.get(0).getOperatorId());
		}
		List<String> userIds = new ArrayList<String>();
		userIds.add(String.valueOf(user.getId()));
		userIds.add(String.valueOf(saleOrderModel.getSubmiter()));
		mailDTO.setUserId(userIds);
		try {
			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
					MQErpEnum.SEND_REMINDER_MAIL.getTags());
		} catch (Exception e) {
			LOGGER.error("----------------------销售单[" + saleOrderModel.getCode() + "]金额调整发送邮件失败----------------------");
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrderModel.getCode(), "金额调整", null, null);
		return true;
	}
	/**
	 * 访问打托出库页面
	 */
	public SaleOrderDTO listSaleOutBySaleId(Long id) throws Exception{
		SaleOrderDTO  saleOrderDTO = saleOrderDao.searchDTOById(id);
		if(StringUtils.isNotBlank(saleOrderDTO.getWriteOffStatus())){
			throw new RuntimeException("销售订单红冲状态不为空，不允许上架");
		}
		SaleOrderItemDTO saleOrderItem = new SaleOrderItemDTO();
		saleOrderItem.setOrderId(id);

		Map<Long,Integer> outNumMap = new HashMap<Long, Integer>();//记录已出库数量
		//获取销售出库数量
		SaleOutDepotDTO saleOutDTO = new SaleOutDepotDTO();
		saleOutDTO.setSaleOrderCode(saleOrderDTO.getCode());
		List<SaleOutDepotDTO> saleOutList = saleOutDepotDao.queryDTODetailsList(saleOutDTO);
		for(SaleOutDepotDTO queryOutDTO : saleOutList) {
			Integer transferNum = queryOutDTO.getTransferNum()==null ? 0:queryOutDTO.getTransferNum();// 好品数量
			Integer wornNum = queryOutDTO.getWornNum()==null ? 0:queryOutDTO.getWornNum();	// 坏品数量
			Integer totalNum = (outNumMap.get(queryOutDTO.getSaleItemId()) == null ? 0 : outNumMap.get(queryOutDTO.getSaleItemId()))+ transferNum + wornNum;

			outNumMap.put(queryOutDTO.getSaleItemId(), totalNum);//记录已出库数量
		}
		List<SaleOrderItemDTO> itemList = saleOrderItemDao.listSaleOrderItemDTO(saleOrderItem);
		for(SaleOrderItemDTO itemDTO : itemList) {
			Integer outNum = outNumMap.get(itemDTO.getId()) == null ? 0 : outNumMap.get(itemDTO.getId());
			itemDTO.setStayOutNum(itemDTO.getNum() - outNum);//待出库量
		}
		saleOrderDTO.setItemList(itemList);
		return saleOrderDTO;
	}

	// 打托出库
	public boolean saveSaleOrderOut(SaleOutDepotDTO saleOutDTO, User user) throws Exception {
		String outCode = null; // 销售订单出库单号
		// 获取销售订单信息
		SaleOrderModel model = saleOrderDao.searchById(saleOutDTO.getSaleOrderId());
		if (model == null) {
			throw new RuntimeException("该销售订单不存在，销售订单：" + saleOutDTO.getSaleOrderCode());
		}
		if (model.getBuId() == null) {
			throw new RuntimeException("该销售订单没有事业部信息");
		}
		// 销售订单没有出仓库信息，不能进行出库打托
		if (model.getOutDepotId() == null) {
			throw new RuntimeException("该销售订单没有出仓库信息，不能进行出库打托");
		}
		if(StringUtils.isNotBlank(model.getWriteOffStatus())){
			throw new RuntimeException("销售订单红冲状态不为空，不允许出库打托");
		}
		// 获取出仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", model.getOutDepotId());// 根据仓库id
		DepotInfoMongo depotMongo = depotInfoMongoDao.findOne(depotInfo_params);
		if (depotMongo == null) {
			throw new RuntimeException("出仓库ID：" + model.getOutDepotId() + "没找到仓库信息");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = sdf.format(new Date());
		Date nowDate = sdf.parse(sDate);
		// 出库日期必须不能大于当前日期
		if (saleOutDTO.getDeliverDate().compareTo(nowDate) == 1) {
			throw new RuntimeException("出库日期不能大于当前日期");
		}

		// 获取该商家下最后的关账月份
		// 获取最大的关账日/月结日期
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(model.getMerchantId());
		closeAccountsMongo.setDepotId(model.getOutDepotId());
		closeAccountsMongo.setBuId(model.getBuId());
		String maxdate = "";
		if (closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {// 查询关账日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
		} else if (closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null) {// 查询月结日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG2);
		} else if (closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null) {// 获取最大的关账日/月结日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
		}
		String maxCloseAccountsMonth = "";
		if (org.apache.commons.lang3.StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(maxCloseAccountsMonth)&& saleOutDTO.getDeliverDate() != null) {
			// 出库日期必须大于关账日期
			if (saleOutDTO.getDeliverDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth)
					.getTime()) {
				throw new RuntimeException("出库日期必须大于关账日期/月结日期");
			}
		}
		// 新增销售订单出库表
		SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
		sOutDepotModel.setSaleOrderId(saleOutDTO.getSaleOrderId());// 销售退货ID
		sOutDepotModel.setMerchantId(user.getMerchantId());// 商家ID
		sOutDepotModel.setMerchantName(user.getMerchantName());// 商家名称
		sOutDepotModel.setOutDepotId(model.getOutDepotId());// 出仓库id
		sOutDepotModel.setOutDepotName(model.getOutDepotName());// 出仓库名称
		sOutDepotModel.setCustomerId(model.getCustomerId());// 客户id
		sOutDepotModel.setCustomerName(model.getCustomerName());// 客户名称
		sOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_027);// 017-待出库 018-已出库 027-出库中
		sOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSCK));// 销售退货出库单号
		sOutDepotModel.setSaleOrderCode(model.getCode());// 销售订单编号
		sOutDepotModel.setSaleType(model.getBusinessModel());// 销售类型 1 购销-整批结算 2 代销 3 采销执行 4.购销-实销实结
		sOutDepotModel.setPoNo(model.getPoNo());
		sOutDepotModel.setLbxNo(model.getLbxNo());
		sOutDepotModel.setBuId(model.getBuId());// 事业部
		sOutDepotModel.setBuName(model.getBuName());
		sOutDepotModel.setDeliverDate(saleOutDTO.getDeliverDate());
		sOutDepotModel.setCreater(user.getId());
		sOutDepotModel.setCreateDate(TimeUtils.getNow());
		sOutDepotModel.setCurrency(model.getCurrency());

		Long id = saleOutDepotDao.save(sOutDepotModel);
		outCode = sOutDepotModel.getCode(); // 销售订单出库单号

		// 修改销售订单状态
		model.setState(DERP_ORDER.SALEORDER_STATE_027);// 出库中
		model.setModifyDate(TimeUtils.getNow());
		model.setModifier(user.getId());
		model.setModifierUsername(user.getName());
		saleOrderDao.modify(model);

		int outZeroNum = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		List<SaleOutDepotItemModel> saveOutItemList = new ArrayList<SaleOutDepotItemModel>();
		List<SaleOutDepotItemDTO> outDTOItemList = saleOutDTO.getItemList();
		Map<String,List<SaleOutDepotItemDTO>> outDTOItemMap = outDTOItemList.stream().collect(Collectors.groupingBy(o->o.getSaleItemId()+"_"+(StringUtils.isBlank(o.getTransferBatchNo())? "":o.getTransferBatchNo() )
				+"_"+(StringUtils.isBlank(o.getProductionDateStr())? "":o.getProductionDateStr()) +"_"
				+(StringUtils.isBlank(o.getOverdueDateStr())? "":o.getOverdueDateStr())));
		for (String key : outDTOItemMap.keySet()) {
			List<SaleOutDepotItemDTO> queryItemList = outDTOItemMap.get(key);
			SaleOutDepotItemDTO outDepotItem = queryItemList.get(0);
			if(outDepotItem.getSaleItemId() == null){
				throw new RuntimeException("销售明细id为空，请及时维护");
			}
			Integer transferNum = queryItemList.stream().mapToInt(SaleOutDepotItemDTO :: getTransferNum).sum();
			Integer wornNum = queryItemList.stream().mapToInt(SaleOutDepotItemDTO :: getWornNum).sum() ;
			if (transferNum == 0 && wornNum == 0) {
				outZeroNum++;
				continue;
			}
			if (outDepotItem.getGoodsId() == null) {
				throw new RuntimeException("该销售订单没有商品信息，不能进行出库打托");
			}
			// 获取商品信息
			params.put("merchandiseId", outDepotItem.getGoodsId());
			params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
			MerchandiseInfoMogo outGoods = merchandiseInfoMogoDao.findOne(params);
			if (outGoods == null) {
				throw new RuntimeException("商品ID：" + outDepotItem.getGoodsId() + "没找到商品信息");
			}

			SaleOrderItemModel saleItemModel = saleOrderItemDao.searchById(outDepotItem.getSaleItemId());
			if(saleItemModel == null) {
				throw new RuntimeException("该销售订单没有商品货号为："+outGoods.getGoodsNo()+" 的商品，不能进行出库打托");
			}

			SaleOutDepotItemModel saleOutDepotItemModel = new SaleOutDepotItemModel();
			saleOutDepotItemModel.setGoodsId(outDepotItem.getGoodsId());
			saleOutDepotItemModel.setGoodsName(outDepotItem.getGoodsName());
			saleOutDepotItemModel.setGoodsCode(outDepotItem.getGoodsCode());
			saleOutDepotItemModel.setGoodsNo(outDepotItem.getGoodsNo());
			saleOutDepotItemModel.setTransferNum(transferNum); // 正常品数量
			saleOutDepotItemModel.setWornNum(wornNum); // 坏品数量
			saleOutDepotItemModel.setCreateDate(TimeUtils.getNow());
			saleOutDepotItemModel.setCreater(user.getId());
			saleOutDepotItemModel.setBarcode(outGoods.getBarcode());
			saleOutDepotItemModel.setCommbarcode(outGoods.getCommbarcode());
			saleOutDepotItemModel.setSaleOutDepotId(id); // 销售订单出库ID
			saleOutDepotItemModel.setTallyingUnit(model.getTallyingUnit());
			saleOutDepotItemModel.setSaleNum(outDepotItem.getSaleNum());
			saleOutDepotItemModel.setTransferBatchNo(outDepotItem.getTransferBatchNo());// 批次号
			saleOutDepotItemModel.setProductionDate(TimeUtils.parse(outDepotItem.getProductionDateStr(),"yyyy-MM-dd")); // 生产日期
			saleOutDepotItemModel.setOverdueDate(TimeUtils.parse(outDepotItem.getOverdueDateStr(),"yyyy-MM-dd"));// 失效日期
			saleOutDepotItemModel.setSaleNum(saleItemModel.getNum());
			saleOutDepotItemModel.setSaleItemId(outDepotItem.getSaleItemId());

			//是否批次强校验
			String transferBatchNo = outDepotItem.getTransferBatchNo();
			String productionDate = outDepotItem.getProductionDateStr();
			String overDueDate = outDepotItem.getOverdueDateStr();
			if (depotMongo.getBatchValidation()==null || DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depotMongo.getBatchValidation())){
				if(StringUtils.isBlank(transferBatchNo)) {
					throw new RuntimeException("出库仓为批次强校验，批次号不能为空") ;
				}
				if(StringUtils.isBlank(productionDate)) {
					throw new RuntimeException("出库仓为批次强校验，生产日期不能为空") ;
				}
				if(!TimeUtils.isYmdDate(productionDate)) {
					throw new RuntimeException("商品货号："+outDepotItem.getGoodsNo()+" 生产日期格式有误，正确格式为:yyyy-MM-dd") ;
				}
				if(StringUtils.isBlank(overDueDate)) {
					throw new RuntimeException("出库仓为批次强校验，失效日期不能为空") ;
				}
				if(!TimeUtils.isYmdDate(overDueDate)) {
					throw new RuntimeException("商品货号："+outDepotItem.getGoodsNo()+" 失效日期格式有误，正确格式为:yyyy-MM-dd") ;
				}
			}
			saleOutDepotItemModel.setTransferBatchNo(transferBatchNo);
			if(StringUtils.isNotBlank(productionDate)){
				saleOutDepotItemModel.setProductionDate(TimeUtils.parse(productionDate,"yyyy-MM-dd"));
			}
			if(StringUtils.isNotBlank(overDueDate)){
				saleOutDepotItemModel.setOverdueDate(TimeUtils.parse(overDueDate,"yyyy-MM-dd"));
			}
			saveOutItemList.add(saleOutDepotItemModel);

		}
		if(outZeroNum == outDTOItemMap.keySet().size()) {
			throw new RuntimeException("出库失败，至少一个货号出库数量大于0") ;
		}
		//校验 出库数量 是否大于 销售数量
		Map<Long,List<SaleOutDepotItemModel>> outDepotItemMap =saveOutItemList.stream().collect(Collectors.groupingBy(SaleOutDepotItemModel::getSaleItemId));
		for(Long saleItemId : outDepotItemMap.keySet()) {
//			Long goodsId = Long.valueOf(goodsIdAndGoodsNo.split("_")[0]);
//			String goodsNo = goodsIdAndGoodsNo.split("_")[1];
			List<SaleOutDepotItemModel> goodsList = outDepotItemMap.get(saleItemId);
			Integer totalTransferNum = goodsList.stream().filter(s->s.getTransferNum() != null).mapToInt(SaleOutDepotItemModel :: getTransferNum).sum();//好品量
			Integer totalWornNum = goodsList.stream().filter(s->s.getWornNum() != null).mapToInt(SaleOutDepotItemModel :: getWornNum).sum();//坏品量
			SaleOrderItemModel saleItemModel = saleOrderItemDao.searchById(saleItemId);
			Integer saleNum = saleItemModel.getNum();
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("saleItemId",saleItemId);
			Integer alreadyOutDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(paramMap);//已出库数量
			//校验 出库数量 是否大于 销售数量
			Integer outNum = alreadyOutDepotNum + totalTransferNum + totalWornNum;
			if(outNum.intValue() > saleNum.intValue()) {
				throw new RuntimeException("商品货号："+saleItemModel.getGoodsNo()+" 当前出库量大于待出库量，待出库量："+(saleNum - alreadyOutDepotNum) +"，不能进行出库打托") ;
			}

		}
		//保存商品信息
		for(SaleOutDepotItemModel outItemModel : saveOutItemList) {
			Long flag = saleOutDepotItemDao.save(outItemModel);
			if (flag == null) {
				return false;
			}
		}

		//修改销售订单状态
		SaleOrderModel updateSaleOrderModel = new SaleOrderModel();
		updateSaleOrderModel.setId(saleOutDTO.getSaleOrderId());
		updateSaleOrderModel.setState(DERP_ORDER.SALEORDER_STATE_027);
		saleOrderDao.modify(updateSaleOrderModel);

		// 释放并减少冻结量
		String inventoryUnit = "";
		if (DERP.ORDER_TALLYINGUNIT_00.equals(model.getTallyingUnit())) {
			inventoryUnit = DERP.INVENTORY_UNIT_0;
		} else if (DERP.ORDER_TALLYINGUNIT_01.equals(model.getTallyingUnit())) {
			inventoryUnit = DERP.INVENTORY_UNIT_1;
		} else if (DERP.ORDER_TALLYINGUNIT_02.equals(model.getTallyingUnit())) {
			inventoryUnit = DERP.INVENTORY_UNIT_2;
		}
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
		List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf1.format(new Date());
		String nowStr = sdf1.format(new Date());
		//中转仓没有冻结，所以不用释放
		if (!DERP_SYS.DEPOTINFO_TYPE_D.equals(depotMongo.getType())) {
			// 获取销售表体（商品信息）
			SaleOutDepotItemModel saleOutItem = new SaleOutDepotItemModel();
			saleOutItem.setSaleOutDepotId(id);
			List<SaleOutDepotItemModel> outItemList = saleOutDepotItemDao.list(saleOutItem);
			for (SaleOutDepotItemModel item : outItemList) {
				InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();

				inventoryFreezeGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
				inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());
				inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());

				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotMongo.getType())) {
					inventoryFreezeGoodsListJson.setUnit(inventoryUnit);

				}
				inventoryFreezeGoodsListJson.setDivergenceDate(now);

				Integer transferNum = item.getTransferNum()==null?0:item.getTransferNum();// 好品数量
				Integer wornNum = item.getWornNum()==null?0:item.getWornNum();	// 坏品数量

				inventoryFreezeGoodsListJson.setNum(transferNum + wornNum);// 释放冻结量=销售出库数量
				inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
			}
			inventoryFreezeRootJson.setMerchantId(model.getMerchantId().toString());
			inventoryFreezeRootJson.setMerchantName(model.getMerchantName());
			inventoryFreezeRootJson.setDepotId(model.getOutDepotId().toString());
			inventoryFreezeRootJson.setDepotName(model.getOutDepotName());
			inventoryFreezeRootJson.setOrderNo(sOutDepotModel.getCode());
			inventoryFreezeRootJson.setBusinessNo(model.getCode());
			inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
			inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
			inventoryFreezeRootJson.setSourceDate(nowStr);
			inventoryFreezeRootJson.setOperateType("1");// 操作类型 0 冻结 1解冻
			// 有表体才需要推库存
			if (inventoryFreezeGoodsListJsonList != null && inventoryFreezeGoodsListJsonList.size() > 0) {
				inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
			}
		}

		// 扣减销售出库库存量
		String saleOutDepotCode = sOutDepotModel.getCode();
		Date date = new Date();
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
		// 获取销售出库的商品信息
		SaleOutDepotItemModel saleOutDepotItem = new SaleOutDepotItemModel();
		saleOutDepotItem.setSaleOutDepotId(sOutDepotModel.getId());
		List<SaleOutDepotItemModel> itemList = saleOutDepotItemDao.list(saleOutDepotItem);
		for (SaleOutDepotItemModel item : itemList) {
			// 好品
			if (item.getTransferNum() != null && item.getTransferNum() > 0) {
				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

				invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
				invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
				invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());
				invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
				invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());


				invetAddOrSubtractGoodsListJson.setType("0"); // 商品分类 （0 好品 1坏品 ）
				invetAddOrSubtractGoodsListJson.setNum(item.getTransferNum()); // 好品数量
				invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
				invetAddOrSubtractGoodsListJson.setBatchNo(item.getTransferBatchNo());
				// 计算是否过期 字符串 （0 是 1 否）
				String isExpire = DERP.ISEXPIRE_1;
				if (item.getOverdueDate() != null) {
					isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());// 判断是否过期是否过期（0是 1否）
				}
				invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);// 是否过期 （0 是 1 否）

				if (item.getProductionDate() != null) {
					Date productionDateTimestamp = item.getProductionDate();
					invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(productionDateTimestamp));
				}
				if (item.getOverdueDate() != null) {
					Date overdueDateTimestamp = item.getOverdueDate();
					invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
					Timestamp currentDate = new Timestamp(date.getTime());
					if (currentDate.before(item.getOverdueDate())) {
						// currentDate 时间比 item.getOverdueDate() 时间早(未过期)
						invetAddOrSubtractGoodsListJson.setIsExpire("1");
					} else {
						invetAddOrSubtractGoodsListJson.setIsExpire("0");
					}
				}
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotMongo.getType())) {
					invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);
				}
				invetAddOrSubtractGoodsListJson.setStockLocationTypeId(model.getStockLocationTypeId() == null ? "" : model.getStockLocationTypeId().toString());
				invetAddOrSubtractGoodsListJson.setStockLocationTypeName(model.getStockLocationTypeName());
				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
			}
			// 坏品
			if (item.getWornNum() != null && item.getWornNum() > 0) {
				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

				invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
				invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
				invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
				invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
				invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());


				invetAddOrSubtractGoodsListJson.setType("1"); // 商品分类 （0 好品 1坏品 ）
				invetAddOrSubtractGoodsListJson.setNum(item.getWornNum()); // 坏品数量
				invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
				invetAddOrSubtractGoodsListJson.setBatchNo(item.getTransferBatchNo());
				// 计算是否过期 字符串 （0 是 1 否）
				String isExpire = DERP.ISEXPIRE_1;
				if (item.getOverdueDate() != null) {
					isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());// 判断是否过期是否过期（0是 1否）
				}
				invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);// 是否过期 （0 是 1 否）
				if (item.getProductionDate() != null) {
					Date productionDateTimestamp = item.getProductionDate();
					invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(productionDateTimestamp));
				}
				if (item.getOverdueDate() != null) {
					Date overdueDateTimestamp = item.getOverdueDate();
					invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
					Timestamp currentDate = new Timestamp(date.getTime());
					if (currentDate.before(item.getOverdueDate())) {
						// currentDate 时间比 item.getOverdueDate() 时间早(未过期)
						invetAddOrSubtractGoodsListJson.setIsExpire("1");
					} else {
						invetAddOrSubtractGoodsListJson.setIsExpire("0");
					}
				}
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotMongo.getType())) {
					invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);

				}
				invetAddOrSubtractGoodsListJson.setStockLocationTypeId(model.getStockLocationTypeId() == null ? "" : model.getStockLocationTypeId().toString());
				invetAddOrSubtractGoodsListJson.setStockLocationTypeName(model.getStockLocationTypeName());
				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
			}
		}

		invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTopic());
		invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTags());
		Map<String, Object> customParam = new HashMap<String, Object>();
		customParam.put("saleId", sOutDepotModel.getSaleOrderId()); // 销售订单ID
		invetAddOrSubtractRootJson.setCustomParam(customParam);
		invetAddOrSubtractRootJson.setMerchantId(sOutDepotModel.getMerchantId().toString());
		invetAddOrSubtractRootJson.setMerchantName(sOutDepotModel.getMerchantName());
		invetAddOrSubtractRootJson.setTopidealCode(user.getTopidealCode()); // 公司编码
		invetAddOrSubtractRootJson.setDepotId(sOutDepotModel.getOutDepotId().toString());
		invetAddOrSubtractRootJson.setDepotName(sOutDepotModel.getOutDepotName());
		invetAddOrSubtractRootJson.setDepotCode(depotMongo.getCode());
		invetAddOrSubtractRootJson.setDepotType(depotMongo.getType());
		invetAddOrSubtractRootJson.setIsTopBooks(depotMongo.getIsTopBooks());
		invetAddOrSubtractRootJson.setOrderNo(saleOutDepotCode);
		invetAddOrSubtractRootJson.setBusinessNo(sOutDepotModel.getSaleOrderCode()); // 销售订单Code
		invetAddOrSubtractRootJson.setBuId(String.valueOf(sOutDepotModel.getBuId())); // 事业部
		invetAddOrSubtractRootJson.setBuName(sOutDepotModel.getBuName());
		invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
		if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(model.getBusinessModel())
				|| DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(model.getBusinessModel())) {
			invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
		} else if (DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(model.getBusinessModel())) {
			invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0021);
		}
		invetAddOrSubtractRootJson.setSourceDate(sdf.format(new Date())); // 单据时间
		invetAddOrSubtractRootJson.setDivergenceDate(sdf1.format(sOutDepotModel.getDeliverDate())); // 发货时间
		// 获取当前年月
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
		String now2 = sdf2.format(sOutDepotModel.getDeliverDate()); // 发货时间
		invetAddOrSubtractRootJson.setOwnMonth(now2); // 归属月份
		invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);

		//中转仓没有冻结，所以不用释放
		if (!DERP_SYS.DEPOTINFO_TYPE_D.equals(depotMongo.getType())) {
			// 释放冻结库存
			rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(),
					MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),
					MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
		}
		// 减库存
		rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(),
				MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
				MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, model.getCode(), "打托出库", null, null);
		return true;
	}

	/**
	 * 商品导出
	 *
	 * @param json
	 * @return
	 */
	@Override
	public List<SaleOrderItemDTO> exportListItem(String json) {
		List<SaleOrderItemDTO> itemList = new ArrayList<>();
		// 解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		// 解析表体数据
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		List<Long> itemIds = new ArrayList<Long>();
		for (int i = 0; i < itemArr.size(); i++) {
			JSONObject job = itemArr.getJSONObject(i);
			if (job.isNullObject() || job.isEmpty()) {
				continue;
			}

			Integer seq = StringUtils.isBlank((String) job.get("seq")) ? null : Integer.valueOf((String) job.get("seq"));
			String goodsNo = (String) job.get("goodsNo");
			String numStr = job.getString("num");
			String amountStr = (String) job.get("amount"); // 销售总金额

			String barcode = (String) job.get("barcode");// 条形码

			Integer num = StringUtils.isBlank(numStr) ? null : Integer.valueOf(numStr.trim());
			BigDecimal amount = StringUtils.isBlank(amountStr.trim()) ? null : new BigDecimal(amountStr.trim());

			// 注入数据
			SaleOrderItemDTO itemDTO = new SaleOrderItemDTO();
			itemDTO.setSeq(seq); // 加序号
			itemDTO.setGoodsNo(goodsNo);
			itemDTO.setNum(num);
			itemDTO.setAmount(amount);
			itemDTO.setBarcode(barcode);

			itemList.add(itemDTO);

		}
		return itemList;
	}

	/**
	 *
	 * 根据标准条码获取标准品牌
	 *
	 * @param commbarcode
	 * @return
	 */
	@Override
	public String getBrandParentName(String commbarcode) {
		String brandPareName = "";
		// 查询标准条码中的标准品牌
		Map<String, Object> commbarcodeParams = new HashMap<String, Object>();
		commbarcodeParams.put("commbarcode", commbarcode);
		CommbarcodeMongo commbar = commbarcodeMongoDao.findOne(commbarcodeParams);
		if (commbar != null)
			brandPareName = commbar.getCommBrandParentName();
		return brandPareName;
	}

	/**
	 * 根据销售编码和商品货号获取商品信息
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public SaleOrderItemModel getItemByCodeAndNo(SaleOrderItemModel model) throws SQLException {
		return saleOrderItemDao.searchByModel(model);
	}

	/**
	 * 判断订单商品上架月份是否已关账
	 *
	 * @param user
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, String> checkExistFinanceClose(User user, Long id) throws SQLException {
		Map<String, String> result = new HashMap<>();

		SaleOrderModel saleOrder = saleOrderDao.searchById(id);
		// 查询最大关账月份
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(saleOrder.getMerchantId());
		closeAccountsMongo.setDepotId(saleOrder.getOutDepotId());
		closeAccountsMongo.setBuId(saleOrder.getBuId());
		String maxDate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,
				DERP.CLOSEACCOUNTFLAG1);
		String maxCloseAccountsMonth = "";
		if (StringUtils.isNotBlank(maxDate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		//已出库，判断出库时间是否关账
		if(DERP_ORDER.SALEORDER_STATE_018.equals(saleOrder.getState())){
			SaleOutDepotModel outModel = new SaleOutDepotModel();
			outModel.setSaleOrderId(id);
			List<SaleOutDepotModel> saleOutList = saleOutDepotDao.list(outModel);
			if (StringUtils.isNotBlank(maxCloseAccountsMonth) && saleOutList != null && saleOutList.size() > 0) {
				saleOutList = saleOutList.stream().sorted(Comparator.comparing(SaleOutDepotModel::getDeliverDate).reversed()).collect(Collectors.toList());
				for (SaleOutDepotModel saleOut : saleOutList) {
					if (saleOut.getDeliverDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
						result.put("code", "01");
						result.put("message", TimeUtils.formatMonth(saleOut.getShelfDate()) + "月出库数据已关账，不予修改金额");
						return result;
					}
				}
			}
		}else if(DERP_ORDER.SALEORDER_STATE_031.equals(saleOrder.getState()) || DERP_ORDER.SALEORDER_STATE_026.equals(saleOrder.getState())){
			//部分上架、已上架，判断上架时间是否关账
			ShelfModel shelfModel = new ShelfModel();
			shelfModel.setSaleOrderId(id);
			List<ShelfModel> shelfList = shelfDao.list(shelfModel);// 上架数据
			if (StringUtils.isNotBlank(maxCloseAccountsMonth) && shelfList != null && shelfList.size() > 0) {
				shelfList = shelfList.stream().sorted(Comparator.comparing(ShelfModel::getShelfDate).reversed()).collect(Collectors.toList());
				for (ShelfModel shelf : shelfList) {
					if (shelf.getShelfDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
						result.put("code", "01");
						result.put("message", TimeUtils.formatMonth(shelf.getShelfDate()) + "月上架数据已关账，不予修改金额");
						return result;
					}
				}
			}

		}

		if (saleOrder.getCreateDate().getTime() < Timestamp.valueOf("2020-01-01 00:00:00").getTime()) {
			result.put("code", "01");
			result.put("message", "2020-01-01前的单据不予修改金额");
			return result;
		}

		result.put("code", "00");
		return result;
	}

	/**
	 * 判断是否存在采购单
	 *
	 * @param dto
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, String> checkExistPurchase(SaleOrderDTO dto, User user) throws SQLException {
		Map<String, String> result = new HashMap<>();
		CustomerInfoMogo supplierMongo = null;
		// 销售单客户是否为内部公司
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerid", dto.getCustomerId());
		params.put("type", "1");// 1:内部,2:外部
		CustomerInfoMogo cusMongo = customerInfoMongoDao.findOne(params);
		if (cusMongo == null) {
			result.put("code", "01");
			return result;
		}
		/**
		 * 查询客户关联的内部公司下是否存在对应的采购订单
		 * 条件：采购订单的公司=当前销售单客户的内部公司编码，采购订单的PO号=销售订单的PO号，采购订单的供应商为内部公司且内部公司编码=当前销售单的公司编码
		 */
		PurchaseOrderExportDTO purchaseModel = new PurchaseOrderExportDTO();
		purchaseModel.setMerchantId(cusMongo.getInnerMerchantId());
		purchaseModel.setPoNo(dto.getPoNo());
		List<PurchaseOrderExportDTO> purList = purchaseOrderDao.getDetailsByExport(purchaseModel);
		if (purList != null && purList.size() > 0) {
			for (PurchaseOrderExportDTO purModel : purList) {
				params.put("customerid", purModel.getSupplierId());
				params.put("type", "1");// 1:内部,2:外部
				params.put("status", "1");// 1使用中,0已禁用
				params.put("cusType", "2");// 1 客户,2 供应商
				supplierMongo = customerInfoMongoDao.findOne(params);
				if (supplierMongo != null && supplierMongo.getInnerMerchantId() != dto.getMerchantId()) {
					result.put("code", "01");
					return result;
				}
			}
		}

		result.put("code", "00");
		result.put("innerMerchantName", cusMongo.getInnerMerchantName());
		return result;
	}

	@Override
	public String createPurchaseOrder(Long saleId, Long buId, Long depotId, User user) throws Exception {

		SaleOrderModel saleModel = saleOrderDao.searchById(saleId);
		if (saleModel == null) {
			throw new RuntimeException("销售订单不存在");
		}

		// 获取客户的内部公司编码
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerid", saleModel.getCustomerId());
		CustomerInfoMogo customer = customerInfoMongoDao.findOne(params);

		// 获取公司对应的供应商编码
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("innerMerchantId", user.getMerchantId());
		params2.put("type", "1");// 1:内部,2:外部
		params2.put("status", "1");// 1使用中,0已禁用
		params2.put("cusType", "2");// 1 客户,2 供应商
		CustomerInfoMogo customer2 = customerInfoMongoDao.findOne(params2);

		// 查询事业部信息
		Map<String, Object> buParams = new HashMap<String, Object>();
		buParams.put("merchantId", customer.getInnerMerchantId());
		buParams.put("buId", buId);
		buParams.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buParams);
		if (merchantBuRelMongo == null) {
			throw new RuntimeException("事业部ID为：" + buId + ",未查到" + customer.getInnerMerchantName() + "该公司下事业部信息");
		}
		// 查询是否存在该公司关联的仓库
		Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
		depotMerchantRelParam.put("merchantId", customer.getInnerMerchantId());
		depotMerchantRelParam.put("depotId", depotId);
		DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
		if (depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)) {
			throw new RuntimeException("仓库ID为：" + depotId + ",未查到" + customer.getInnerMerchantName() + "下调出仓库信息");
		}
		// 查询仓库信息
		Map<String, Object> depotParam = new HashMap<String, Object>();
		depotParam.put("depotId", depotId);
		DepotInfoMongo depotMongo = depotInfoMongoDao.findOne(depotParam);

		if (null != depotMongo) {
			// 校验公司-仓库-事业部的关联表
			Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
			merchantDepotBuRelParam.put("merchantId", customer.getInnerMerchantId());
			merchantDepotBuRelParam.put("depotId", depotMongo.getDepotId()); // 出仓仓库
			merchantDepotBuRelParam.put("buId", buId);
			MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao
					.findOne(merchantDepotBuRelParam);
			if (outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)) {
				throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",入仓仓库："
						+ depotMongo.getDepotCode() + ",未查到" + customer.getInnerMerchantName() + "仓库事业部关联信息");
			}
		}
		PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
		purchaseOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGO));
		purchaseOrderModel.setSupplierId(customer2.getCustomerid());
		purchaseOrderModel.setSupplierName(customer2.getName());
		purchaseOrderModel.setMerchantId(customer.getInnerMerchantId());
		purchaseOrderModel.setMerchantName(customer.getInnerMerchantName());
		purchaseOrderModel.setPoNo(saleModel.getPoNo());
		purchaseOrderModel.setDepotId(depotId);
		if (depotMongo != null) {
			purchaseOrderModel.setDepotName(depotMongo.getName());
		}
		purchaseOrderModel.setTallyingUnit(saleModel.getTallyingUnit());
		purchaseOrderModel.setBuId(buId);
		if (merchantBuRelMongo != null) {
			purchaseOrderModel.setBuName(merchantBuRelMongo.getBuName());
		}
		purchaseOrderModel.setBusinessModel(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_1);// 默认 购销
		purchaseOrderModel.setGrossWeight(saleModel.getBillWeight());
		//purchaseOrderModel.setTransport(saleModel.getTransport());
		purchaseOrderModel.setStandardCaseTeu(saleModel.getTeu());
		purchaseOrderModel.setTrainNumber(saleModel.getTrainno());
		purchaseOrderModel.setCarrier(saleModel.getCarrier());
		purchaseOrderModel.setTorrNum(saleModel.getTorusNumber());
		purchaseOrderModel.setCurrency(saleModel.getCurrency());
		purchaseOrderModel.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_001);
		purchaseOrderModel.setTopidealCode(user.getTopidealCode());
		purchaseOrderModel.setCreater(user.getId());
		purchaseOrderModel.setCreateName(user.getName());
		purchaseOrderModel.setCreateDate(TimeUtils.getNow());
		purchaseOrderModel.setAmountAdjustmentStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_0);

		// 解析表体数据
		SaleOrderItemDTO saleOrderItem = new SaleOrderItemDTO();
		saleOrderItem.setOrderId(saleId);
		List<SaleOrderItemDTO> itemSaleList = saleOrderItemDao.listSaleOrderItemDTO(saleOrderItem);
		List<PurchaseOrderItemModel> itemList = new ArrayList<PurchaseOrderItemModel>();
		if (itemSaleList != null && itemSaleList.size() > 0) {
			for (SaleOrderItemDTO itemSale : itemSaleList) {
				// 获取商品信息 ,销售订单商品货号对应的标准条码+采购入库仓的选品限制+采购订单归属公司查找该公司下的商品
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
				paramMap.put("barcode", itemSale.getBarcode());
				paramMap.put("merchantId", purchaseOrderModel.getMerchantId());
				MerchandiseInfoMogo merchandise = null;
				List<MerchandiseInfoMogo> mongList = merchandiseInfoMogoDao.findMerchandiseByDepotId(paramMap, depotId);
				if (mongList == null || mongList.size() < 1) { // 商品不存在保存失败
					throw new RuntimeException("商品货号：" + itemSale.getGoodsNo() + "的条码：" + itemSale.getBarcode()+"未与入库仓："+depotMongo.getName()+"绑定");
				}
				// 存在商品有多个货号满足条件的情况下优先取货号相同的那条
				for (MerchandiseInfoMogo merchandiseM : mongList) {
					if (merchandiseM.getGoodsNo().equals(itemSale.getGoodsNo())) {
						merchandise = merchandiseM;
					}
				}
				if (merchandise == null) {
					merchandise = mongList.get(0);
				}

				// 注入数据
				PurchaseOrderItemModel itemModel = new PurchaseOrderItemModel();
				itemModel.setGoodsCode(merchandise.getGoodsCode());
				itemModel.setGoodsId(merchandise.getMerchandiseId());
				itemModel.setGoodsName(merchandise.getName());
				itemModel.setGoodsNo(merchandise.getGoodsNo());
				itemModel.setNum(itemSale.getNum());
				itemModel.setBrandName(itemSale.getBrandName());
				itemModel.setBoxNo(itemSale.getBoxNo());
				itemModel.setContractNo(itemSale.getContractNo());
				itemModel.setRemark(itemSale.getRemark());
				itemModel.setUnit(String.valueOf(itemSale.getUnit()));
				itemModel.setPrice(itemSale.getPrice());
				itemModel.setAmount(itemSale.getAmount());
				itemModel.setBarcode(merchandise.getBarcode());
				itemModel.setPurchaseUnit(itemSale.getTallyingUnit());
				itemModel.setTaxRate(Double.valueOf(0));
				itemModel.setTax(BigDecimal.ZERO);
				itemModel.setTaxPrice(itemSale.getPrice());
				itemModel.setTaxAmount(itemSale.getAmount());
				itemModel.setGrossWeight(itemSale.getGrossWeight());
				itemModel.setNetWeight(itemSale.getNetWeight());
				itemModel.setGrossWeightSum(itemSale.getGrossWeightSum());
				itemModel.setNetWeightSum(itemSale.getNetWeightSum());
				itemModel.setCreater(user.getId());
				itemModel.setCreateName(user.getName());
				itemModel.setCreateDate(TimeUtils.getNow());
				itemList.add(itemModel);
			}
		}

		if (itemList != null && itemList.size() > 0) {
			Long id = purchaseOrderDao.save(purchaseOrderModel);
			for (PurchaseOrderItemModel item : itemList) {
				item.setPurchaseOrderId(id);
				purchaseOrderItemDao.save(item);
			}
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "销售单生成采购订单", null, null);

		return "生成采购订单成功";
	}

	@Override
	public Map<String, String> checkExistPurchaseByPO(String poNo, User user) throws SQLException {
		Map<String, String> result = new HashMap<String, String>();
		PurchaseOrderExportDTO purchaseModel = new PurchaseOrderExportDTO();
		purchaseModel.setMerchantId(user.getMerchantId());
		purchaseModel.setPoNo(poNo);
		List<PurchaseOrderExportDTO> purList = purchaseOrderDao.getDetailsByExport(purchaseModel);
		if (purList != null && purList.size() > 0) {
			result.put("code", "01");
			return result;
		}
		result.put("code", "00");
		return result;
	}

	@Override
	public Long GeneratePurchaseOrder(String json, User user) throws Exception {
		// 解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		Long saleOrderId = Long.valueOf(jsonObj.getString("saleOrderId"));
		Long supplierId = Long.valueOf(jsonObj.getString("supplierId"));
		String poNo = (String) jsonObj.get("poNo");

		Map<String, Object> customer_params = new HashMap<String, Object>();
		customer_params.put("customerid", supplierId);
		CustomerInfoMogo customer = customerInfoMongoDao.findOne(customer_params);
		if (customer == null) {
			throw new RuntimeException("供应商不存在");
		}

		SaleOrderModel saleModel = saleOrderDao.searchById(Long.valueOf(saleOrderId));
		if (saleModel == null) {
			throw new RuntimeException("销售订单不存在");
		}

		PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
		purchaseOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGO));
		purchaseOrderModel.setSupplierId(supplierId);
		purchaseOrderModel.setSupplierName(customer.getName());
		purchaseOrderModel.setMerchantId(saleModel.getMerchantId());
		purchaseOrderModel.setMerchantName(saleModel.getMerchantName());
		purchaseOrderModel.setPoNo(poNo);
		purchaseOrderModel.setDepotId(saleModel.getOutDepotId());
		purchaseOrderModel.setDepotName(saleModel.getOutDepotName());
		purchaseOrderModel.setBuId(saleModel.getBuId());
		purchaseOrderModel.setBuName(saleModel.getBuName());
		purchaseOrderModel.setBusinessModel(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_1);// 默认 购销
		purchaseOrderModel.setGrossWeight(saleModel.getBillWeight());
		//purchaseOrderModel.setTransport(saleModel.getTransport());
		purchaseOrderModel.setStandardCaseTeu(saleModel.getTeu());
		purchaseOrderModel.setTrainNumber(saleModel.getTrainno());
		purchaseOrderModel.setCarrier(saleModel.getCarrier());
		purchaseOrderModel.setTorrNum(saleModel.getTorusNumber());
		purchaseOrderModel.setCurrency(saleModel.getCurrency());
		purchaseOrderModel.setTallyingUnit(saleModel.getTallyingUnit());
		purchaseOrderModel.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_001);
		purchaseOrderModel.setTopidealCode(user.getTopidealCode());
		purchaseOrderModel.setCreater(user.getId());
		purchaseOrderModel.setCreateName(user.getName());
		purchaseOrderModel.setCreateDate(TimeUtils.getNow());
		purchaseOrderModel.setAmountAdjustmentStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_0);

		// 解析表体数据
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		List<PurchaseOrderItemModel> itemList = new ArrayList<PurchaseOrderItemModel>();
		for (int i = 0; i < itemArr.size(); i++) {
			JSONObject job = itemArr.getJSONObject(i);
			if (job.isNullObject() || job.isEmpty()) {
				continue;
			}

			Long id = Long.valueOf(job.getString("id"));
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsNo = (String) job.get("goodsNo");
			String numStr = job.getString("num");
			Integer num = Integer.valueOf(numStr.trim());
			String price = (String) job.get("price");
			price = price.trim();
			String amount = (String) job.get("amount"); // 销售总金额
			amount = amount.trim();

			SaleOrderItemModel saleItemModel = saleOrderItemDao.searchById(id);
			if (saleItemModel == null) {
				throw new RuntimeException("商品货号：" + goodsNo + "，在销售订单不存在");
			}

			// 注入数据
			PurchaseOrderItemModel itemModel = new PurchaseOrderItemModel();
			itemModel.setBarcode(saleItemModel.getBarcode());
			itemModel.setGoodsCode(saleItemModel.getGoodsCode());
			itemModel.setGoodsId(goodsId);
			itemModel.setGoodsName(saleItemModel.getGoodsName());
			itemModel.setGoodsNo(saleItemModel.getGoodsNo());
			itemModel.setNum(num);
			itemModel.setBrandName(saleItemModel.getBrandName());
			itemModel.setBoxNo(saleItemModel.getBoxNo());
			itemModel.setContractNo(saleItemModel.getContractNo());
			itemModel.setRemark(saleItemModel.getRemark());
			itemModel.setUnit(saleItemModel.getUnit() == null ? "" : String.valueOf(saleItemModel.getUnit()));
			itemModel.setPrice(new BigDecimal(price));
			itemModel.setAmount(new BigDecimal(amount));
			itemModel.setPurchaseUnit(saleItemModel.getTallyingUnit());
			itemModel.setGrossWeight(saleItemModel.getGrossWeight());
			itemModel.setNetWeight(saleItemModel.getNetWeight());
			itemModel.setGrossWeightSum(saleItemModel.getGrossWeightSum());
			itemModel.setNetWeightSum(saleItemModel.getNetWeightSum());
			itemModel.setTaxRate(Double.valueOf(0));
			itemModel.setTax(BigDecimal.ZERO);
			itemModel.setTaxPrice(new BigDecimal(price));
			itemModel.setTaxAmount(new BigDecimal(amount));
			itemModel.setCreater(user.getId());
			itemModel.setCreateName(user.getName());
			itemModel.setCreateDate(TimeUtils.getNow());
			itemList.add(itemModel);

		}
		Long id = null;
		if (itemList != null && itemList.size() > 0) {
			id = purchaseOrderDao.save(purchaseOrderModel);
			for (PurchaseOrderItemModel item : itemList) {
				item.setPurchaseOrderId(id);
				purchaseOrderItemDao.save(item);
			}
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "销售单生成采购订单", null, null);
		return id;
	}

	/**
	 * 金额确认
	 */
	@Override
	public boolean amountConfirm(String json, User user) throws SQLException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		SaleOrderModel saleOrderModel = new SaleOrderModel();
		// 解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		// 判断传过来的id是否为空
		String idStr = jsonObj.getString("orderId");
		String amountConfirmState = jsonObj.getString("amountConfirmState");
		if (StringUtils.isNotBlank(idStr)) {
			Long orderId = Long.valueOf(idStr);
			saleOrderModel = saleOrderDao.searchById(orderId);
			if (saleOrderModel == null) {
				throw new RuntimeException("金额确认失败，销售订单不存在");
			}
			// 判断状态是不是已审核
			if (DERP_ORDER.SALEORDER_STATE_001.equals(saleOrderModel.getState())) {
				throw new RuntimeException("金额确认失败，该销售单待审核");
			}
			if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
				throw new RuntimeException("金额确认失败，该销售单红冲状态不为空");
			}
		}
		// 金额确认并标识为“确认不通过”时，需要更新调整状态为“未调整”
		if (amountConfirmState.equals(DERP_ORDER.SALEORDER_AMOUMT_CONFIRM_STATUS_2)) {
			saleOrderModel.setAmountStatus(DERP_ORDER.SALEORDER_AMOUMTSTATUS_0);
		}
		saleOrderModel.setAmountConfirmStatus(amountConfirmState);
		saleOrderModel.setAmountConfirmer(user.getId());
		saleOrderModel.setAmountConfirmUsername(user.getName());
		saleOrderModel.setAmountConfirmDate(TimeUtils.parseFullTime(sf.format(new Date())));
		saleOrderModel.setModifyDate(TimeUtils.getNow());
		saleOrderModel.setModifier(user.getId());
		saleOrderModel.setModifierUsername(user.getName());
		int num = saleOrderDao.modify(saleOrderModel);

		// 若金额审核触发“确认不通过”按钮则邮件提醒金额修改人
		if (DERP_ORDER.SALEORDER_AMOUMT_CONFIRM_STATUS_2.equals(saleOrderModel.getAmountConfirmStatus())) {
			ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
			mailDTO.setBusinessModuleType("3");
			mailDTO.setTypeName("销售");
			mailDTO.setType("11");// 1：提交 2：审核 3：上架 4：核销 5：开票 6：作废审核 7：盖章发票 8：审核完毕 9：作废完成 10：金额修改 11：金额确认
			mailDTO.setMerchantId(saleOrderModel.getMerchantId());
			mailDTO.setMerchantName(saleOrderModel.getMerchantName());
			mailDTO.setBuId(saleOrderModel.getBuId());
			mailDTO.setBuName(saleOrderModel.getBuName());
			mailDTO.setSupplier(saleOrderModel.getCustomerName());
			mailDTO.setOrderCode(saleOrderModel.getCode());
			mailDTO.setPoNum(saleOrderModel.getPoNo());
			mailDTO.setStatus("金额确认不通过");
			// 金额调整人为空时，发邮件给单据创建人
			mailDTO.setCreateId(saleOrderModel.getCreater());
			mailDTO.setReviewerId(user.getId());
			mailDTO.setSubmitId(Arrays.asList(String.valueOf(saleOrderModel.getSubmiter())));
			mailDTO.setAuditorId(user.getId());
			mailDTO.setModifyId(saleOrderModel.getAdjuster());
			mailDTO.setUserName(user.getName());
			ShelfModel shelfModel= new ShelfModel();
			shelfModel.setSaleOrderId(saleOrderModel.getId());
			List<ShelfModel> shelfList = shelfDao.list(shelfModel);
			if(shelfList != null && shelfList.size() > 0) {
				mailDTO.setShelvesId(shelfList.get(0).getOperatorId());
			}
			List<String> userIds = new ArrayList<String>();
		    userIds.add(String.valueOf(user.getId()));
		    if (saleOrderModel.getAdjuster() == null) {
				userIds.add(String.valueOf(saleOrderModel.getCreater()));
			} else {
				userIds.add(String.valueOf(saleOrderModel.getAdjuster()));
			}
		    mailDTO.setUserId(userIds);
			try {

				rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(),MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());
			} catch (Exception e) {
				LOGGER.error("----------------------销售单[" + saleOrderModel.getCode()+ "]金额确认不通过发送邮件失败----------------------");
			}
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrderModel.getCode(), "金额确认", null, null);
		return true;
	}

	/**
	 * 提交
	 *
	 * @param json
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Long updateSaleOrder(SaleOrderDTO dto, User user) throws Exception {

		SaleOrderModel saleOrderModel = saveSaleOrder(dto, user,"1");//0-保存 1-提交 2-审核

		// 平台采购订单转销售订单（平台采购订单保存销售订单号）
		String platformPurchaseIds = dto.getPlatformPurchaseIds();// 平台采购订单号
		// 平台采购单转销售
		if (StringUtils.isNotBlank(platformPurchaseIds) && dto.getId() == null) {
			String[] platformPurchaseIdStr = platformPurchaseIds.split(",");
			for (String platformPurchaseId : platformPurchaseIdStr) {
				PlatformPurchaseOrderModel platformPurchaseOrder = platformPurchaseOrderDao
						.searchById(Long.parseLong(platformPurchaseId));
				String saleCode = "";
				if (platformPurchaseOrder != null)
					saleCode = platformPurchaseOrder.getSaleCode();
				PlatformPurchaseOrderModel updateModel = new PlatformPurchaseOrderModel();
				updateModel.setId(Long.parseLong(platformPurchaseId));
				updateModel.setResaleDate(TimeUtils.getNow());
				updateModel.setResaleName(user.getName());
				updateModel.setResaler(user.getId());
				updateModel.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_3);// 3:已转销售
				if (StringUtils.isNotBlank(saleCode)) {
					saleCode = saleCode + "," + saleOrderModel.getCode();
				} else {
					saleCode = saleOrderModel.getCode();
				}
				updateModel.setSaleCode(saleCode);// 销售单号
				platformPurchaseOrderDao.modify(updateModel);
			}
		}
		// 当提交人制销售单完成触发“提交”按钮邮件提醒审核人
		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
		mailDTO.setBusinessModuleType("3");
		mailDTO.setTypeName("销售");
		mailDTO.setType("1");
		mailDTO.setMerchantId(saleOrderModel.getMerchantId());
		mailDTO.setMerchantName(saleOrderModel.getMerchantName());
		mailDTO.setBuId(saleOrderModel.getBuId());
		mailDTO.setBuName(saleOrderModel.getBuName());
		mailDTO.setSupplier(saleOrderModel.getCustomerName());
		mailDTO.setOrderCode(saleOrderModel.getCode());
		mailDTO.setPoNum(saleOrderModel.getPoNo());
		mailDTO.setStatus("已提交");
		mailDTO.setSubmitId(Arrays.asList(String.valueOf(user.getId())));
		mailDTO.setAuditorId(saleOrderModel.getAuditor());
		mailDTO.setReviewerId(saleOrderModel.getAmountConfirmer());
		mailDTO.setModifyId(saleOrderModel.getAdjuster());
		mailDTO.setUserName(user.getName());
		List<String> userIds = new ArrayList<String>();
		userIds.add(String.valueOf(user.getId()));
		mailDTO.setUserId(userIds);
		try {
			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),MQErpEnum.SEND_REMINDER_MAIL.getTags());
		} catch (Exception e) {
			LOGGER.error("----------------------销售单[" + saleOrderModel.getCode() + "]提交发送邮件失败----------------------");
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrderModel.getCode(), "提交", null, null);
		return saleOrderModel.getId();
	}

	private SaleOrderModel saveSaleOrder(SaleOrderDTO dto, User user,String operate) throws Exception {
		CustomerInfoMogo customer = null;
		if (dto.getCustomerId() != null) {
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("customerid", dto.getCustomerId());
			customer = customerInfoMongoDao.findOne(params2);
		}

		SaleOrderModel saleOrderModel = new SaleOrderModel();
		if (dto.getId() != null) {
			saleOrderModel = saleOrderDao.searchById(dto.getId());
			// 判断状态是不是已审核
			if (DERP_ORDER.SALEORDER_STATE_003.equals(saleOrderModel.getState())) {
				throw new RuntimeException("保存失败，该销售单已审核");
			}

			// 若是修改，还得删除销售单_po关联表中对应的数据
			SalePoRelModel sobpm = new SalePoRelModel();
			sobpm.setOrderId(dto.getId());
			sobpm.setState("0");
			sobpm.setMerchantId(user.getMerchantId()); // 公司ID
			salePoRelDao.delbyPoNoAndOrderId(sobpm);
		}

		// 获取该事业部信息
		Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
		merchantBuRelParam.put("merchantId", user.getMerchantId());
		merchantBuRelParam.put("buId", dto.getBuId());
		merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
		if (merchantBuRelMongo == null || "".equals(merchantBuRelMongo)) {
			throw new RuntimeException("事业部ID为：" + dto.getBuId() + ",未查到该公司下事业部信息");
		}

		// 用户事业部
		boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), dto.getBuId());
		if (!userRelateBu) {
			throw new RuntimeException(
					"事业部编码为：" + merchantBuRelMongo.getBuCode() + ",用户id：" + user.getId() + ",无权限操作该事业部");
		}


		// 获取出仓仓库信息
		DepotInfoMongo outDepot = null;
		if (dto.getOutDepotId() != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", dto.getOutDepotId());
			outDepot = depotInfoMongoDao.findOne(params);
		}

		if (null != outDepot) {
			// 校验公司-仓库-事业部的关联表
			Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
			merchantDepotBuRelParam.put("merchantId", user.getMerchantId());
			merchantDepotBuRelParam.put("depotId", outDepot.getDepotId()); // 出仓仓库
			merchantDepotBuRelParam.put("buId", dto.getBuId());
			MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao
					.findOne(merchantDepotBuRelParam);
			if (outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)) {
				throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",出仓仓库："
						+ outDepot.getDepotCode() + ",未查到该公司仓库事业部关联信息");
			}
		}

		// 仓库公司关联表
		String isInOutInstruction = "";// 进出接口指令 1-是 0 - 否
		if (outDepot != null) {
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", user.getMerchantId());
			depotMerchantRelParam.put("depotId", dto.getOutDepotId());
			DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
			if (depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)) {
				throw new RuntimeException("仓库ID为：" + dto.getOutDepotId() + ",未查到该公司下调出仓库信息");
			}
			isInOutInstruction = depotMerchantRelMongo.getIsInOutInstruction();// 进出接口指令 1-是 0 - 否
		}

		DepotInfoMongo inDepot = null;
		if (dto.getInDepotId() != null) {
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", dto.getInDepotId());
			inDepot = depotInfoMongoDao.findOne(params1);
		}

		if(StringUtils.isNotBlank(dto.getPoNo()) && !checkPoNo(dto.getPoNo())){
			//输入时仅限中文、大小写字母、数字和“-”这4种字符
			throw new RuntimeException("保存失败，PO号仅限中文、大小写字母、数字和“-”这4种字符");
		}

		// -----------------------购销类型参数校验---------------------------------
		if (dto.getId() != null && DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(dto.getBusinessModel())) {
			// 把归属月份和原销出仓仓库修改为空
			saleOrderDao.updateMonthAndDepot(dto.getId());
		}
		/**
		 * 提交、审核时 1、当出仓仓库为保税仓，是否同关区必填，且是否同关区为“是”时，入库仓库必填，且入库仓库仅能为备查库
		 * 2、当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-整批结算时，入库仓库禁用； 3、当出仓仓库为保税仓，且是否同关区为“否”
		 * 且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库； 4、当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
		 * 5、当出仓仓库为海外仓且销售类型为购销-整批结算时，入库仓库禁用，不予选择
		 * 6、当出仓仓库为中转仓，且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
		 */
		if (!"0".equals(operate)) {
			if (dto.getOutDepotId() == null) {
				throw new RuntimeException("保存失败，请选择出仓仓库");
			}
			if (DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType())) {
				if (StringUtils.isBlank(dto.getIsSameArea())) {
					throw new RuntimeException("保存失败，是否同关区不能为空");
				} else {
					// 是否同关区为“是”时，入库仓库必填，且入库仓库仅能为备查库；
					if (DERP.ISSAMEAREA_1.equals(dto.getIsSameArea())) {
						if (dto.getInDepotId() == null) {
							throw new RuntimeException("保存失败，入库仓库不能为空");
						} else if (inDepot == null) {
							throw new RuntimeException("保存失败，没有找到入库仓库信息");
						} else if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
							throw new RuntimeException("保存失败，入库仓库仅能为备查库");
						}
					}
					// 是否同关区为“否” 且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
					else if (DERP.ISSAMEAREA_0.equals(dto.getIsSameArea()) && DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(dto.getBusinessModel())) {
						if (dto.getInDepotId() == null) {
							throw new RuntimeException("保存失败，入库仓库不能为空");
						} else if (inDepot == null) {
							throw new RuntimeException("保存失败，没有找到入库仓库信息");
						} else if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
							throw new RuntimeException("保存失败，入库仓库只能选备查库");
						}
					}
				}
			} else if (DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepot.getType())
					&& DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(dto.getBusinessModel())) {
				// 当出仓仓库为中转仓，且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
				if (dto.getInDepotId() == null) {
					throw new RuntimeException("保存失败，入库仓库不能为空");
				} else if (inDepot == null) {
					throw new RuntimeException("保存失败，没有找到入库仓库信息");
				} else if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
					throw new RuntimeException("保存失败，入库仓库只能选备查库");
				}
			} else if (DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())) {
				// 当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
				if (DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(dto.getBusinessModel())) {
					if (dto.getInDepotId() == null) {
						throw new RuntimeException("保存失败，入库仓库不能为空");
					} else if (inDepot == null) {
						throw new RuntimeException("保存失败，没有找到入库仓库信息");
					} else if (!DERP_SYS.DEPOTINFO_TYPE_B.equals(inDepot.getType())) {
						throw new RuntimeException("保存失败，入库仓库只能选备查库");
					}
				}
			}

			/**
			 * 1、当销售类型为购销-实销实结时，PO单号非必填； 2、当销售类型为购销-整批结算、采销执行时，PO单号必填项
			 */
			if ((DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(dto.getBusinessModel())
					|| DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(dto.getBusinessModel())) && StringUtils.isBlank(dto.getPoNo())) {
				throw new RuntimeException("保存失败，PO单号不能为空");
			}

			if (StringUtils.isNotBlank(dto.getLbxNo())) {// lbx不为空，需要校验唯一
				boolean flag = saleOrderDao.lbxNoIsExist(dto.getLbxNo(), dto.getId());
				if (flag) {
					throw new RuntimeException("保存失败，lbx单号已存在");
				}
			}
			//校验同一货号价格是否相同
//			List<String> checkGoodsNoPrice = new ArrayList<String>();
//			for (SaleOrderItemDTO item : dto.getItemList()) {
//				String goodsNoPrice = item.getGoodsId()+"_"+item.getPrice();
//				if(checkGoodsNoPrice.contains(goodsNoPrice)){
//					throw new RuntimeException("同一商品货号："+item.getGoodsNo()+"，销售单价相同") ;
//				}else{
//					checkGoodsNoPrice.add(goodsNoPrice);
//				}
//			}
			//通过“公司+事业部”查询公司事业部是否启用了库位管理，若启用则该字段必填
			if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(merchantBuRelMongo.getStockLocationManage()) && dto.getStockLocationTypeId() == null){
				throw new RuntimeException("当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"已开启库位管理，库位类型不能为空") ;
			}else if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1.equals(merchantBuRelMongo.getStockLocationManage()) && dto.getStockLocationTypeId() != null){
				throw new RuntimeException("当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"未开启库位管理，库位类型不允许填写") ;
			}

		}
		BuStockLocationTypeConfigMongo stockLocationMongo = null;
		if(dto.getStockLocationTypeId() != null){
			Map<String,Object> stockLocationMap = new HashMap<>();
			stockLocationMap.put("buStockLocationTypeId", dto.getStockLocationTypeId());
			stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
			stockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);
			if(stockLocationMongo == null){
				throw new RuntimeException("库位类型输入有误") ;
			}
		}

		// -----------------------菜鸟仓参数校验---------------------------------
		if (StringUtils.isNotBlank(dto.getLbxNo()) && outDepot.getName().contains("菜鸟")) {
			Map<String, Object> lbx_params = new HashMap<String, Object>();
			lbx_params.put("lbxNo", dto.getLbxNo());
			lbx_params.put("type", "XSO");
			// 删除lbx号
			lbxNoMongoDao.remove(lbx_params);

			LbxNoMongo lbxNoMongo = new LbxNoMongo();
			lbxNoMongo.setLbxNo(dto.getLbxNo());
			lbxNoMongo.setType("XSO");
			// 往mongodb插入lbx号
			lbxNoMongoDao.insert(lbxNoMongo);
		}

		// 保存表头数据
		saleOrderModel.setMerchantId(user.getMerchantId());
		saleOrderModel.setMerchantName(user.getMerchantName());
		saleOrderModel.setOrderType(dto.getOrderType());// 单据标识 1-预售转销 2-非预售转销
		saleOrderModel.setPreSaleOrderRelCode(dto.getPreSaleOrderRelCode());// 关联的预售单号
		saleOrderModel.setBuId(dto.getBuId()); // 事业部ID
		saleOrderModel.setBuName(merchantBuRelMongo.getBuName());// 事业部名称
		saleOrderModel.setBusinessModel(dto.getBusinessModel());
		saleOrderModel.setCustomerId(dto.getCustomerId());
		saleOrderModel.setCustomerName(customer != null ? customer.getName() : "");
		saleOrderModel.setLbxNo(dto.getLbxNo());
		saleOrderModel.setPoNo(dto.getPoNo());
		saleOrderModel.setOutDepotId(dto.getOutDepotId());
		saleOrderModel.setTransport(dto.getTransport());
		saleOrderModel.setPaymentTerms(dto.getPaymentTerms());
		saleOrderModel.setTradeTerms(dto.getTradeTerms());
		saleOrderModel.setApprovalNo(dto.getApprovalNo());
        saleOrderModel.setRemark(dto.getRemark());
		if (outDepot != null) {
			saleOrderModel.setOutDepotName(outDepot.getName());
			saleOrderModel.setOutDepotCode(outDepot.getCode());
			saleOrderModel.setInspectNo(outDepot.getInspectNo());
			saleOrderModel.setCustomsNo(outDepot.getCustomsNo());
		}
		if (inDepot != null) {
			saleOrderModel.setInDepotId(dto.getInDepotId());
			saleOrderModel.setInDepotName(inDepot.getName());
			saleOrderModel.setInDepotCode(inDepot.getCode());
		}
		if(stockLocationMongo != null){
			saleOrderModel.setStockLocationTypeId(stockLocationMongo.getBuStockLocationTypeId());
			saleOrderModel.setStockLocationTypeName(stockLocationMongo.getName());
		}
		saleOrderModel.setIsSameArea(dto.getIsSameArea());
		saleOrderModel.setTallyingUnit(dto.getTallyingUnit());
		saleOrderModel.setCurrency(dto.getCurrency());
		// 调入客户卓志编码
		String inCustomerTopNo = "";
		if (customer != null) {
			inCustomerTopNo = customer.getMainId();
		}
		// 同关区
		boolean isSame = outDepot != null && !StringUtils.isEmpty(outDepot.getCustomsNo()) && inDepot != null
				&& !StringUtils.isEmpty(inDepot.getCustomsNo())
				&& outDepot.getCustomsNo().equals(inDepot.getCustomsNo());

		// 1.调出调入公司不同,出和入的仓库编码相同 -业务场景：40 ：账册内货权转移 服务类型：G0：库内调拨服务
		if (!user.getTopidealCode().equals(inCustomerTopNo) && inDepot != null
				&& outDepot.getCode().equals(inDepot.getCode())) {
			saleOrderModel.setModel("40");
			saleOrderModel.setServeTypes("G0");
		}
		// 2.调出调入公司不同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：50： 账册内货权转移调仓 服务类型：E0：区内调拨调出服务
		else if (!user.getTopidealCode().equals(inCustomerTopNo) && isSame
				&& !outDepot.getCode().equals(inDepot.getCode())) {
			saleOrderModel.setModel("50");
			saleOrderModel.setServeTypes("E0");
		}
		// 3.调出调入公司相同，出入仓库同关区，出仓和入库仓库编码不同-业务场景：10： 账册内调仓 服务类型：E0：区内调拨调出服务
		else if (user.getTopidealCode().equals(inCustomerTopNo) && isSame
				&& !outDepot.getCode().equals(inDepot.getCode())) {
			saleOrderModel.setModel("10");
			saleOrderModel.setServeTypes("E0");
		}
		// 4.调出调入公司相同，出入仓库不同关区，出仓和入库仓库编码不同-业务场景：10 ：账册内调仓 服务类型：E0：区内调拨调出服务
		else if (user.getTopidealCode().equals(inCustomerTopNo)
				&& !(isSame || (StringUtils.isEmpty(outDepot.getCustomsNo()) && inDepot != null
						&& StringUtils.isEmpty(inDepot.getCustomsNo())))
				&& !outDepot.getCode().equals(inDepot.getCode())) {
			saleOrderModel.setModel("10");
			saleOrderModel.setServeTypes("E0");
		}

		Integer totalNum = 0;
		BigDecimal totalAmount = BigDecimal.ZERO;
		List<SaleOrderItemModel> itemList = new ArrayList<SaleOrderItemModel>();
		Map<Long,SaleOrderItemModel> itemMap = new HashMap<Long,SaleOrderItemModel>();
		//校验同一货号价格是否相同
		List<String> checkGoodsNoPrice = new ArrayList<String>();
		for (SaleOrderItemDTO item : dto.getItemList()) {
			String goodsNoPrice = item.getGoodsNo()+"_"+item.getPrice();
			if(checkGoodsNoPrice.contains(goodsNoPrice)){
				throw new RuntimeException("同一商品货号："+item.getGoodsNo()+"，销售单价不允许相同") ;
			}else{
				checkGoodsNoPrice.add(goodsNoPrice);
			}
			// 注入数据
			SaleOrderItemModel itemModel = new SaleOrderItemModel();
			itemModel.setBarcode(item.getBarcode());
			itemModel.setGoodsCode(item.getGoodsCode());
			itemModel.setGoodsId(item.getGoodsId());
			itemModel.setGoodsName(item.getGoodsName());
			itemModel.setGoodsNo(item.getGoodsNo());
			itemModel.setNum(item.getNum());
			itemModel.setOrderId(dto.getId());
			itemModel.setPrice(item.getPrice());
			itemModel.setAmount(item.getAmount());
			itemModel.setTaxAmount(item.getTaxAmount());// 销售总金额（含税）
			itemModel.setTaxRate(item.getTaxRate());// 税率
			itemModel.setTax(item.getTax());// 税额
			BigDecimal taxPrice = null;
			if(item.getTaxAmount() != null) {
				taxPrice = item.getTaxAmount().divide(new BigDecimal(item.getNum()), 8, RoundingMode.HALF_UP);
			}
			itemModel.setTaxPrice(taxPrice);// 销售单价（含税）
			int index = 1;
			if (item.getSeq() != null) {
				itemModel.setSeq(item.getSeq());
			} else {
				itemModel.setSeq(index++);
			}
			if(!"0".equals(operate) && itemModel.getGoodsId() == null){
				throw new RuntimeException("条码为:"+itemModel.getBarcode()+"的商品货号未关联出仓仓库");
			}

			if(itemModel.getGoodsId() != null){
				// 获取商品信息
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("merchandiseId", item.getGoodsId());
				paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
				List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(paramMap,dto.getOutDepotId());
				if (merchandiseList == null || merchandiseList.size() < 1) {
					String msg = "";
					if(outDepot != null){
						msg = "出库仓："+ outDepot.getName();
					}
					throw new RuntimeException(msg +"未找到商品货号为：" + item.getGoodsNo() + "的商品");
				}
				MerchandiseInfoMogo merchandise = merchandiseList.get(0);
				itemModel.setCommbarcode(merchandise.getCommbarcode()); // 标准条码
			}
			itemList.add(itemModel);
			totalNum = totalNum + item.getNum();
			totalAmount =  totalAmount.add(item.getAmount());
		}
		saleOrderModel.setTotalAmount(totalAmount);
		saleOrderModel.setTotalNum(totalNum);
		// 目的地和收件信息
		saleOrderModel.setOrderSource(DERP_ORDER.SALEORDER_ORDERSOURCE_1);
		saleOrderModel.setTopidealCode(user.getTopidealCode());
		// 新增保存 或 审核驳回
		if ((dto.getId() == null && "0".equals(operate))) {
			saleOrderModel.setAmountStatus(DERP_ORDER.SALEORDER_AMOUMTSTATUS_0);
			saleOrderModel.setState(DERP_ORDER.SALEORDER_STATE_008);// 待提交
		} else if ("1".equals(operate)) {// 提交
			saleOrderModel.setAmountStatus(DERP_ORDER.SALEORDER_AMOUMTSTATUS_0);

			saleOrderModel.setSubmiter(user.getId());
			saleOrderModel.setSubmiterName(user.getName());
			saleOrderModel.setSubmitDate(TimeUtils.getNow());

			saleOrderModel.setState(DERP_ORDER.SALEORDER_STATE_001);// 待审核
		}
		saleOrderModel.setItemList(itemList);

		List<Long> reportIds = new ArrayList<>();
		Long num = null;// 记录新增id
		if (dto.getId() != null) {
			saleOrderModel.setId(dto.getId());
			saleOrderModel.setModifyDate(TimeUtils.getNow());
			saleOrderModel.setModifier(user.getId());
			saleOrderModel.setModifierUsername(user.getName());
			saleOrderDao.modifyWithNull(saleOrderModel);

			//删除原表体
			SaleOrderItemModel delItemModel = new SaleOrderItemModel();
			delItemModel.setOrderId(dto.getId());
			List<SaleOrderItemModel> reportDelItemList = saleOrderItemDao.list(delItemModel);
			for (SaleOrderItemModel saleOrderItemModel : reportDelItemList) {
				reportIds.add(saleOrderItemModel.getId());//记录要删除的表体
			}
			//有商品的先删除
			if(reportIds != null && reportIds.size() > 0) {
				saleOrderItemDao.delete(reportIds);
			}
			num = dto.getId();
		} else {
			saleOrderModel.setCreateDate(TimeUtils.getNow());
			saleOrderModel.setCreater(user.getId());
			saleOrderModel.setCreateName(user.getName());
			saleOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSO));// 销售单号;
			num = saleOrderDao.save(saleOrderModel);// 记录新增id
		}
		//保存表体信息
		for (SaleOrderItemModel itemModel : saleOrderModel.getItemList()) {
			itemModel.setOrderId(num);
			saleOrderItemDao.save(itemModel);
		}

		if (StringUtils.isNotBlank(dto.getPoNo())) {
			// 在新增销售订单号之后再去保存到销售单_po关联表中（因为这里需要销售单id、销售单号）
			SalePoRelModel salePoRelModel = new SalePoRelModel();
			salePoRelModel.setPoNo(dto.getPoNo());
			salePoRelModel.setState("0"); // 类型:0:销售订单1:销售退订单
			salePoRelModel.setOrderId(num);
			salePoRelModel.setOrderCode(saleOrderModel.getCode());
			salePoRelModel.setMerchantId(user.getMerchantId()); // 公司ID
			salePoRelModel.setMerchantName(user.getMerchantName()); // 公司名称
			salePoRelModel.setCreateDate(TimeUtils.getNow());
			// 不存在则保存到销售单_po关联表中去
			salePoRelDao.save(salePoRelModel);
		}

		return saleOrderModel;
	}

	/**
	 * 是否启用销售价格管理
	 */
	@Override
	public Boolean getSalePriceManage(Long buId, Long customerId, User user) throws Exception {
		boolean flag = false;
		if (buId != null && customerId != null) {
			// 以（公司+事业部）+（公司+客户）查询是否 同时 启用销售价格管理
			Map<String, Object> merchantBuRelMap = new HashMap<String, Object>();
			merchantBuRelMap.put("buId", buId);
			merchantBuRelMap.put("merchantId", user.getMerchantId());
			merchantBuRelMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
			MerchantBuRelMongo merchantBuRel = merchantBuRelMongoDao.findOne(merchantBuRelMap);
			if (DERP_SYS.SALE_PRICE_MANAGE_1.equals(merchantBuRel.getSalePriceManage())) {
				flag = true;
			}

			if (flag) {
				Map<String, Object> customerMerchantRelMap = new HashMap<String, Object>();
				customerMerchantRelMap.put("customerId", customerId);
				customerMerchantRelMap.put("merchantId", user.getMerchantId());
				customerMerchantRelMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1);
				CustomerMerchantRelMongo customerMerchantRelMongoRel = customerMerchantRelMongoDao
						.findOne(customerMerchantRelMap);
				flag = customerMerchantRelMongoRel != null
						&& DERP_SYS.SALE_PRICE_MANAGE_1.equals(customerMerchantRelMongoRel.getSalePriceManage());
			}
		}
		return flag;
	}

	/**
	 * 获取销售价格管理的商品单价
	 *
	 * @param merchantId
	 * @return
	 */
	@Override
	public List<String> getSalePrice(String json, Long merchantId) throws Exception {
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = JSONObject.fromObject(json);
		if(jsonObj.get("customerId") == null || StringUtils.isBlank(jsonObj.getString("customerId"))){
			return result;
		}
		Long customerId = Long.valueOf(jsonObj.getString("customerId"));
		if(jsonObj.get("currency") == null || StringUtils.isBlank(jsonObj.getString("currency"))){
			return result;
		}
		String currency = (String) jsonObj.get("currency");
		String unitId = (String) jsonObj.get("unitId");
		if(jsonObj.get("goodId") == null || StringUtils.isBlank(jsonObj.getString("goodId"))){
			return result;
		}
		Long goodId = Long.valueOf(jsonObj.getString("goodId"));
		Long buId = Long.valueOf(jsonObj.getString("buId"));

		HashMap<String, Object> merchandiseMap = new HashMap<>();
		merchandiseMap.put("merchandiseId", goodId);
		merchandiseMap.put("status", "1");
		MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseMap);

		HashMap<String, Object> customerSalePriceMap = new HashMap<>();
		customerSalePriceMap.put("merchantId", merchantId);
		customerSalePriceMap.put("customerId", customerId);
		customerSalePriceMap.put("commbarcode", merchandiseInfo.getCommbarcode());
		customerSalePriceMap.put("currency", currency);
		customerSalePriceMap.put("status", DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003);
		customerSalePriceMap.put("buId", buId);
		List<CustomerSalePriceMongo> mList = customerSalePriceMongoDao.findAll(customerSalePriceMap);
		// 若不存在报价信息，该商品的销售单价默认为空
		if (mList.isEmpty()) {
			return result;
		}

		for (CustomerSalePriceMongo tempMongo : mList) {
			if (TimeUtils.parseFullTime(tempMongo.getEffectiveDate() + " 00:00:00").getTime() <= TimeUtils.getNow().getTime()
				&& TimeUtils.parseFullTime(tempMongo.getExpiryDate() + " 23:59:59").getTime() >= TimeUtils.getNow().getTime()) {

				BigDecimal price = tempMongo.getSalePrice();
				// 理货单位不为空，且不为件时，进行箱、件、托转换
				if (StringUtils.isNotBlank(unitId) && !DERP.ORDER_TALLYINGUNIT_02.equals(unitId)) {
					if (DERP.ORDER_TALLYINGUNIT_01.equals(unitId) && merchandiseInfo.getBoxToUnit() > 0) {
						price = price.multiply(new BigDecimal(merchandiseInfo.getBoxToUnit()));
					} else if (DERP.ORDER_TALLYINGUNIT_00.equals(unitId) && merchandiseInfo.getTorrToUnit() > 0) {
						price = price.multiply(new BigDecimal(merchandiseInfo.getTorrToUnit()));
					} else {
						throw new RuntimeException("商品货号：" + merchandiseInfo.getGoodsNo() + "箱、托、件 转换数据未维护！");
					}
				}
				String priceStr = price.setScale(8,BigDecimal.ROUND_HALF_UP).toString();
				if(!result.contains(priceStr)) {
					result.add(priceStr);
				}
			}
		}

		return result;
	}

	/**
	 * 导出清关资料
	 *
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, FirstCustomsInfoDTO> exportDepotCustomsDeclares(Long id, List<Long> fileTempIds, String type)
			throws Exception {
		Map<String, FirstCustomsInfoDTO> resultMap = new HashMap<>();
		// 仓库模板
		if (fileTempIds == null || fileTempIds.isEmpty()) {
			return null;
		}

		SaleOrderModel model = saleOrderDao.searchById(id);
		if (model == null) {
			return null;
		}

		for (Long fileTempId : fileTempIds) {
			FileTempModel fileTempModel = fileTempDao.searchById(fileTempId);
			if (fileTempModel == null || StringUtils.isEmpty(fileTempModel.getToUrl())) {
				continue;
			}

			// 南沙仓模板（之前）
			if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_ZONGHEYICANG.equals(fileTempModel.getToUrl())) {
				FirstCustomsInfoDTO firstCustomsInfo = getZONGHEYICANGFirstCustomsInfoDTO(id);
				resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
			} else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPTIANJIN.equals(fileTempModel.getToUrl()) || // 天津模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPONLINE.equals(fileTempModel.getToUrl()) ||// 唯品线上模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPQINGDAO.equals(fileTempModel.getToUrl()) ||// 青岛模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPCHONGQING.equals(fileTempModel.getToUrl()) ||// 重庆模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPZHENGZHOU.equals(fileTempModel.getToUrl()) ||// 郑州模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPNANSHA.equals(fileTempModel.getToUrl()) ||// 唯品南沙模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDHUANGPU.equals(fileTempModel.getToUrl()) ||// 京东黄埔模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDSHANGHAI.equals(fileTempModel.getToUrl()) ||// 京东上海模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDNANSHA.equals(fileTempModel.getToUrl()) ||// 京东南沙模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDQINGDAO.equals(fileTempModel.getToUrl()) ||// 京东青岛模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDZHENGZHOU.equals(fileTempModel.getToUrl()) ||// 京东郑州模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDNINGBO.equals(fileTempModel.getToUrl()) ||// 京东宁波模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDXIAMEN.equals(fileTempModel.getToUrl()) ||// 京东厦门模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDTIANJIN.equals(fileTempModel.getToUrl()) ||// 京东天津模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDJINYI.equals(fileTempModel.getToUrl()) ||// 京东金义模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDHEBEI.equals(fileTempModel.getToUrl()) ||// 京东河北模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDINANSHA.equals(fileTempModel.getToUrl()) ||// 天猫南沙模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDININGBO.equals(fileTempModel.getToUrl()) ||// 天猫宁波模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDITIANJIN.equals(fileTempModel.getToUrl()) ||// 天猫天津模板
					   DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDIZHENGZHOU.equals(fileTempModel.getToUrl())) {// 天猫郑州模板
				FirstCustomsInfoDTO firstCustomsInfo = getFirstCustomsInfoDTO(id);
				resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
			}
		}

		return resultMap;
	}

	/**
	 * 封装综合一仓模板实体
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private FirstCustomsInfoDTO getZONGHEYICANGFirstCustomsInfoDTO(Long id) throws Exception {
		SaleOrderDTO saleOrderDTO = saleOrderDao.searchDTOById(id);
		SaleOrderItemDTO itemDTO = new SaleOrderItemDTO();
		itemDTO.setOrderId(id);
		List<SaleOrderItemDTO> itemList = saleOrderItemDao.listSaleOrderItemDTO(itemDTO);

		// 根据商家id获取商家信息
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", saleOrderDTO.getMerchantId());
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantParams);

		// 根据“公司+仓库”获取关系表
		Map<String, Object> depotMerchantParams = new HashMap<String, Object>();
		depotMerchantParams.put("depotId", saleOrderDTO.getOutDepotId());
		depotMerchantParams.put("merchantId", saleOrderDTO.getMerchantId());
		DepotMerchantRelMongo depotMerchant = depotMerchantRelMongoDao.findOne(depotMerchantParams);

		// 根据“入库仓库+关区”获取关系表
		Map<String, Object> depotCustomsParams = new HashMap<String, Object>();
		depotCustomsParams.put("depotId", saleOrderDTO.getInDepotId());
		depotCustomsParams.put("customsAreaId", saleOrderDTO.getInCustomsId());
		DepotCustomsRelMongo depotcustoms = depotCustomsRelMongoDao.findOne(depotCustomsParams);

		FirstCustomsInfoDTO firstCustomsInfoDTO = new FirstCustomsInfoDTO();

		if (saleOrderDTO != null) {
			if (depotMerchant != null) {
				firstCustomsInfoDTO.setSignNo(depotMerchant.getContractCode());// 合同号
			}
			firstCustomsInfoDTO.setContractNo(saleOrderDTO.getContractNo());// 合同号
			firstCustomsInfoDTO.setSigningDate(saleOrderDTO.getCreateDate());// 签订日期
			firstCustomsInfoDTO.setOrderDate(saleOrderDTO.getCreateDate());// 订单日期
			firstCustomsInfoDTO.setSigningAddr("中国广州市");// 签订地点
			if (merchant != null) {
				firstCustomsInfoDTO.setFirstParty(merchant.getFullName());// 甲方 公司全名
				firstCustomsInfoDTO.setFirstPartyAddr(merchant.getRegisteredAddress());// 甲方地址
			}
			firstCustomsInfoDTO.setSecondParty("广东卓志跨境电商供应链服务有限公司");// 乙方
			firstCustomsInfoDTO.setRequirement("无");// 特殊操作要求
			firstCustomsInfoDTO.setPayment("T/T");// 付款方式
			firstCustomsInfoDTO.setPortDest(saleOrderDTO.getPortDes());// 卸货港
			firstCustomsInfoDTO.setPayRules(saleOrderDTO.getPayRules());// 付款条约
			firstCustomsInfoDTO.setMark("N/M ");// 唛头
			if (depotcustoms != null) {
				firstCustomsInfoDTO.seteAddressee(depotcustoms.getRecCompanyEnname());// Consignee
				firstCustomsInfoDTO.setAddressee(depotcustoms.getRecCompanyName());// 收货人
			}
			firstCustomsInfoDTO.setShipDate(saleOrderDTO.getCreateDate());// 装船时间
			firstCustomsInfoDTO.setPortLoading(saleOrderDTO.getOutdepotAddr());// 装货港
			firstCustomsInfoDTO.setConsignee("NO");// 境外收货人
			firstCustomsInfoDTO.setInvoiceNo(saleOrderDTO.getInvoiceNo());// 发票号
			firstCustomsInfoDTO.setPack(saleOrderDTO.getPack());// 包装

			Set<String> originCountrySet = new HashSet<String>();
			List<CustomsDeclareItemDTO> customsDeclareItemList = new ArrayList<CustomsDeclareItemDTO>();

			Integer totalNum = 0;
			Double totalGrossWeight = 0.0;
			Double totalNetWeight = 0.0;
			BigDecimal totalAmount = BigDecimal.ZERO;
			for (SaleOrderItemDTO item : itemList) {
				CustomsDeclareItemDTO customsDeclareItemDTO = new CustomsDeclareItemDTO();
				customsDeclareItemDTO.setGoodsName(item.getGoodsName());// 商品名称
				customsDeclareItemDTO.setNum(item.getNum());// 商品数量

				BigDecimal price = item.getDeclarePrice() == null ? BigDecimal.ZERO : item.getDeclarePrice();
				customsDeclareItemDTO.setPrice(price);// 单价
				customsDeclareItemDTO
						.setAmount(price.multiply(new BigDecimal(item.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP));// 总价
				customsDeclareItemDTO.setTotalNetWeight(item.getNetWeightSum() == null ? 0.0 : item.getNetWeightSum());// 毛重
				customsDeclareItemDTO
						.setTotalGrossWeight(item.getGrossWeightSum() == null ? 0.0 : item.getGrossWeightSum());// 净重
				// 根据商品id获取商品信息
				Map<String, Object> merchandiseParams = new HashMap<String, Object>();
				merchandiseParams.put("merchandiseId", item.getGoodsId());
				MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseParams);

				if (merchandise.getUnit() != null) {
					// 根据单位id获取单位信息
					Map<String, Object> unitParams = new HashMap<String, Object>();
					unitParams.put("unitId", merchandise.getUnit());
					UnitMongo unit = unitMongoDao.findOne(unitParams);
					if (unit != null) {
						customsDeclareItemDTO.setUnit(unit.getName());// 单位
					}

				}
				// 根据国家id获取国家信息
				if (merchandise.getCountyId() != null) {
					Map<String, Object> countryParams = new HashMap<String, Object>();
					countryParams.put("countryOriginId", merchandise.getCountyId());
					CountryOriginMongo countryOrigin = countryOriginMongoDao.findOne(countryParams);
					if (countryOrigin != null) {
						customsDeclareItemDTO.setCountryName(countryOrigin.getName());// 原产国
						originCountrySet.add(countryOrigin.getName());
					}
				}
				customsDeclareItemDTO.setGoodsSpec(merchandise.getSpec());// 商品规格
				customsDeclareItemDTO.setConstituentRatio(merchandise.getComponent());// 成分占比
				customsDeclareItemList.add(customsDeclareItemDTO);

				totalNum = totalNum + item.getNum();
				totalGrossWeight = totalGrossWeight + customsDeclareItemDTO.getTotalGrossWeight();
				totalNetWeight = totalNetWeight + customsDeclareItemDTO.getTotalNetWeight();
				totalAmount = totalAmount.add(customsDeclareItemDTO.getAmount());
			}
			String originCountry = "";
			if (originCountrySet != null && originCountrySet.size() > 0) {
				originCountry = StringUtils.join(originCountrySet, ";");
			}

			firstCustomsInfoDTO.setCountry(originCountry);// 原产国 用分号隔开
			firstCustomsInfoDTO.setItemList(customsDeclareItemList);
			firstCustomsInfoDTO.setTotalNum(totalNum);
			firstCustomsInfoDTO.setTotalGrossWeight(totalGrossWeight);
			firstCustomsInfoDTO.setTotalNetWeight(totalNetWeight);
			firstCustomsInfoDTO.setTotalAmount(totalAmount);
		}

		return firstCustomsInfoDTO;
	}

	/**
	 * 封装导出模板实体
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private FirstCustomsInfoDTO getFirstCustomsInfoDTO(Long id) throws Exception {
		SaleOrderDTO saleOrderDTO = saleOrderDao.searchDTOById(id);
		SaleOrderItemDTO itemDTO = new SaleOrderItemDTO();
		itemDTO.setOrderId(id);
		List<SaleOrderItemDTO> itemList = saleOrderItemDao.listSaleOrderItemDTO(itemDTO);
		// 根据商家id获取商家信息
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", saleOrderDTO.getMerchantId());
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantParams);

		// 根据“公司+仓库”获取关系表
		Map<String, Object> depotMerchantParams = new HashMap<String, Object>();
		depotMerchantParams.put("depotId", saleOrderDTO.getInDepotId());
		depotMerchantParams.put("merchantId", saleOrderDTO.getMerchantId());
		DepotMerchantRelMongo depotMerchant = depotMerchantRelMongoDao.findOne(depotMerchantParams);

		FirstCustomsInfoDTO firstCustomsInfoDTO = new FirstCustomsInfoDTO();
		if (saleOrderDTO != null) {
			if (depotMerchant != null) {
				firstCustomsInfoDTO.setSignNo(depotMerchant.getContractCode());// 合同号
			}
			firstCustomsInfoDTO.setTransportation(saleOrderDTO.getTransportLabel());// 运输方式
			firstCustomsInfoDTO.setCustomsContractNo(saleOrderDTO.getContractNo());// 报关合同号（excel中的提运单号）
			firstCustomsInfoDTO.setPoNo(saleOrderDTO.getPoNo());// po号（excel中的合同号）
			firstCustomsInfoDTO.setContractNo(saleOrderDTO.getContractNo());
			firstCustomsInfoDTO.setInvoiceNo(saleOrderDTO.getInvoiceNo());// 发票号
			firstCustomsInfoDTO.setCreateDate(saleOrderDTO.getCreateDate());// 创建日期（excel中的发票日期）
			firstCustomsInfoDTO.setPayRules(saleOrderDTO.getTradeTermsLabel());// 付款条约
			firstCustomsInfoDTO.setPack(saleOrderDTO.getPack());// 包装方式
			// 托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱
			String packType = DERP_ORDER.getLabelByKey(DERP_ORDER.order_torrpacktypeList,saleOrderDTO.getTorrPackType());
			firstCustomsInfoDTO.setPalletMaterial(packType);// 托盘材质
			if (merchant != null) {
				firstCustomsInfoDTO.setSecondParty(merchant.getFullName());// 当前公司全名
				firstCustomsInfoDTO.setSecondPartyAddr(merchant.getEnglishRegisteredAddress());// 当前公司地址（英文）
				firstCustomsInfoDTO.setSecondPartyTelephone(merchant.getTel());// 当前公司联系方式

				firstCustomsInfoDTO.setFirstParty(merchant.getFullName());// 当前公司全名
				firstCustomsInfoDTO.setFirstPartyAddr(merchant.getRegisteredAddress());// 当前公司地址
				firstCustomsInfoDTO.setFirstPartyEnAddr(merchant.getEnglishRegisteredAddress());// 当前公司地址（英文）

				firstCustomsInfoDTO.setEmail(merchant.getEmail());
				firstCustomsInfoDTO.setMerchantEnName(merchant.getEnglishName());// 当前公司全名 英文

				if ("ERP31194100049".equals(merchant.getCode())) {// 健太
					firstCustomsInfoDTO.setShopName("jdwqt");
				} else if ("ERP26143500022".equals(merchant.getCode())) {// 卓烨贸易
					firstCustomsInfoDTO.setShopName("jdwtwt");
				} else if ("ERP31194300027".equals(merchant.getCode())) {// 广旺
					firstCustomsInfoDTO.setShopName("jdwah");
				}
			}
			firstCustomsInfoDTO.setTorrNum(saleOrderDTO.getTorusNumber());// 托数
			firstCustomsInfoDTO.setOrderDate(saleOrderDTO.getCreateDate());// 订单日期

			firstCustomsInfoDTO.setDestination(saleOrderDTO.getInPlatformCustoms());// 目的地 入库关区
																					// saleOrderDTO.getInPlatformCustoms()
			firstCustomsInfoDTO.setBoxNum(saleOrderDTO.getBoxNum());// 箱数
			// 客户名称全称
			firstCustomsInfoDTO.setCustomerName(saleOrderDTO.getCustomerName());
			firstCustomsInfoDTO.setDestination(saleOrderDTO.getInPlatformCustoms());
			// 装船时间 创建时间前3天
//			Calendar shipCal = Calendar.getInstance();
//			shipCal.setTime(saleOrderDTO.getCreateDate());
//			shipCal.add(Calendar.DATE, -3);
//			firstCustomsInfoDTO.setShipDate(new Timestamp(shipCal.getTime().getTime()));// 装货时间
//			// 发货时间 创建时间后3天
//			if ("deliverDate".equals(type)) {
//			} else {
//				Calendar deliverCal = Calendar.getInstance();
//				deliverCal.setTime(saleOrderDTO.getCreateDate());
//				deliverCal.add(Calendar.DATE, 3);
//				firstCustomsInfoDTO.setDeliverDate(new Timestamp(deliverCal.getTime().getTime()));// 发货时间
//			}
			firstCustomsInfoDTO.setDeliverDate(saleOrderDTO.getDeliveryDate());// 发货时间
			firstCustomsInfoDTO.setPortLoading(saleOrderDTO.getOutdepotAddr());// 装货港
			firstCustomsInfoDTO.setCode(TimeUtils.format(saleOrderDTO.getCreateDate(), "yyyyMMdd"));// 编码
			firstCustomsInfoDTO.setLbxNo(saleOrderDTO.getLbxNo());// lbx编码
			firstCustomsInfoDTO.setTeu(saleOrderDTO.getTeu());
			// 装运时间 创建时间后1个月
			Calendar loadingCal = Calendar.getInstance();
			loadingCal.setTime(saleOrderDTO.getCreateDate());
			loadingCal.add(Calendar.MONTH, 1);
			firstCustomsInfoDTO.setLoadingDate(new Timestamp(loadingCal.getTime().getTime()));// 装运时间

			if(saleOrderDTO.getInCustomsId() != null) {
				Map<String, Object> depotCustomsRelParams = new HashMap<String, Object>();
				depotCustomsRelParams.put("depotId", saleOrderDTO.getInDepotId());
				depotCustomsRelParams.put("customsAreaId", saleOrderDTO.getInCustomsId());
				DepotCustomsRelMongo depotCustomsRel = depotCustomsRelMongoDao.findOne(depotMerchantParams);
				if(depotCustomsRel != null) {
					firstCustomsInfoDTO.setOnlineRegisterNo(depotCustomsRel.getOnlineRegisterNo());	//电子商务账号
				}

			}

			Double rate = null;
			// 销售币种不为人民币，则按订单创建日期取对应的汇率进行换算成人民币
			if (!DERP.CURRENCYCODE_CNY.equals(saleOrderDTO.getCurrency())) {
				Map<String, Object> exchangeRateParams = new HashMap<String, Object>();
				exchangeRateParams.put("origCurrencyCode", saleOrderDTO.getCurrency());
				exchangeRateParams.put("tgtCurrencyCode", DERP.CURRENCYCODE_CNY);
				exchangeRateParams.put("effectiveDate", TimeUtils.format(saleOrderDTO.getCreateDate(), "yyyy-MM-dd"));
				ExchangeRateMongo exchangeRateMongo = exchangeRateMongoDao.findOne(exchangeRateParams);
				if (exchangeRateMongo != null) {
					rate = exchangeRateMongo.getRate();
				}
			}
			Double usdRate = null;
			// 销售币种不为美元，则按订单创建日期取对应的汇率进行换算成美元
			if (!DERP.CURRENCYCODE_USD.equals(saleOrderDTO.getCurrency())) {
				Map<String, Object> exchangeRateParams = new HashMap<String, Object>();
				exchangeRateParams.put("origCurrencyCode", saleOrderDTO.getCurrency());
				exchangeRateParams.put("tgtCurrencyCode", DERP.CURRENCYCODE_USD);
				exchangeRateParams.put("effectiveDate", TimeUtils.format(saleOrderDTO.getCreateDate(), "yyyy-MM-dd"));
				ExchangeRateMongo exchangeRateMongo = exchangeRateMongoDao.findOne(exchangeRateParams);
				if (exchangeRateMongo != null) {
					usdRate = exchangeRateMongo.getRate();
				}
			}
			Set<String> originCountrySet = new HashSet<String>();// 原产国集合
			Set<String> brandSet = new HashSet<String>();// 商品品牌集合
			String goodNo = "";
			String goodName = "";
			Double netWeight = 0.0;
			Integer totalNum = 0;
			Integer totalBoxNum = 0;
			Double totalGrossWeight = 0.0;
			Double totalNetWeight = 0.0;
			BigDecimal totalAmount = BigDecimal.ZERO;
			BigDecimal totalUsAmount = BigDecimal.ZERO;
			List<CustomsDeclareItemDTO> customsDeclareItemList = new ArrayList<CustomsDeclareItemDTO>();
			for (SaleOrderItemDTO item : itemList) {
				CustomsDeclareItemDTO customsDeclareItemDTO = new CustomsDeclareItemDTO();
				// 1、取销售单中的商品货号，若为美赞商品需查询库位映射表转成原货号；
				// 2、以“条形码+入库关区”，找到商品表中外仓备案商品，入库关区为空，则为商品空，找到多条取其一；
				if(saleOrderDTO.getInCustomsId() == null) {
					continue;
				}
				Map<String, Object> merchandiseExternalParams = new HashMap<String, Object>();
				// 原货号
				merchandiseExternalParams.put("barcode", item.getBarcode());// 条形码
				merchandiseExternalParams.put("customsAreaId", saleOrderDTO.getInCustomsId());//入库仓平台关区
				List<MerchandiseExternalWarehouseMongo> goodsList = merchandiseExternalWarehouseMongoDao.findAll(merchandiseExternalParams);
				if (goodsList != null && goodsList.size() > 0) {
					MerchandiseExternalWarehouseMongo goods = goodsList.get(0);
					goodNo = goods.getGoodsNo();
					goodName = goods.getGoodsName();
					netWeight = goods.getNetWeight();
					customsDeclareItemDTO.setUnit(goods.getUnit());// 备案单位

					// 根据国家id获取国家信息
					customsDeclareItemDTO.setCountryName(goods.getCountyName());// 原产国
					originCountrySet.add(goods.getCountyName());

					// 查询商品品牌
					customsDeclareItemDTO.setBrandName(goods.getBrandName());// 商品品牌
					brandSet.add(goods.getBrandName());

					customsDeclareItemDTO.setBarcode(item.getBarcode());// 条形码
					customsDeclareItemDTO.setGoodsSpec(goods.getSpec());// 规格
					customsDeclareItemDTO.setDeclareFactor(goods.getDeclareFactor());// 申报要素
					customsDeclareItemDTO.setConstituentRatio(goods.getComponent());// 成分占比
					customsDeclareItemDTO.setCustomsGoodsRecordNo(goods.getCustomsGoodsRecordNo());// 海关商品备案号
					customsDeclareItemDTO.setHsCode(goods.getHsCode());// hs编码
					customsDeclareItemDTO.setShelfLifeDays(goods.getShelfLifeDays());// 保质天数
					customsDeclareItemDTO.setProductTypeName(goods.getProductTypeName());// 商品类别 取找到的商品货号在商品表的“二级分类”
					customsDeclareItemDTO.setManufacturer(goods.getManufacturer());// 生产企业 取找到的商品货号在商品表的生产企业
					customsDeclareItemDTO.setMaterialAccount(goods.getMaterialAccount());//账册备案料号
					customsDeclareItemDTO.setSaleGoodNames(goods.getSaleGoodNames());//售卖商品名称（中文）
					customsDeclareItemDTO.setEmsCode(goods.getEmsCode());//EMS编码
					customsDeclareItemDTO.setBoxToUnit(goods.getBoxToUnit());//箱规
					customsDeclareItemDTO.setJinerxiang(goods.getJinerxiang());
					//长*宽*高
					customsDeclareItemDTO.setVolume(goods.getLength() +"*"+goods.getWidth()+"*"+goods.getHeight());
				} else {
					goodNo = "";
					goodName = "";
					netWeight = 0.0;
				}

				Double netWeightSum = 0.0;
				netWeightSum = new BigDecimal(netWeight).multiply(new BigDecimal(item.getNum())).stripTrailingZeros().doubleValue();

				Double grossWeightSum = item.getGrossWeightSum() == null ? 0.0 : item.getGrossWeightSum();
				Double grossWeight = new BigDecimal(grossWeightSum).divide(new BigDecimal(item.getNum()), 5, BigDecimal.ROUND_HALF_UP).doubleValue();
				customsDeclareItemDTO.setTotalNetWeight(netWeightSum);// 总净重
				customsDeclareItemDTO.setTotalGrossWeight(grossWeightSum);// 总毛重
				customsDeclareItemDTO.setNetWeight(netWeight);// 净重
				customsDeclareItemDTO.setGrossWeight(grossWeight);// 毛重
				customsDeclareItemDTO.setNum(item.getNum());// 商品数量
				customsDeclareItemDTO.setContractNo(saleOrderDTO.getPoNo());// 合同号 销售订单的po号
				customsDeclareItemDTO.setPalletNo(item.getTorrNo());// 托盘号
				customsDeclareItemDTO.setCartons(item.getBoxNum());// 箱数
				customsDeclareItemDTO.setGoodsNo(goodNo);// 商品货号
				customsDeclareItemDTO.setGoodsName(goodName);// 商品名称

				// 单价
				BigDecimal price = BigDecimal.ZERO;
				if (rate != null) {
					price = item.getPrice().multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);// 人民币单价
				} else {
					price = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				customsDeclareItemDTO.setPrice(price);// 单价
				customsDeclareItemDTO.setAmount(price.multiply(new BigDecimal(item.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP));// 总价

				// 美元单价
				BigDecimal usdPrice = BigDecimal.ZERO;
				if (usdRate != null) {
					usdPrice = item.getPrice().multiply(new BigDecimal(usdRate)).setScale(2, BigDecimal.ROUND_HALF_UP);// 美元单价
				} else {
					usdPrice = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				customsDeclareItemDTO.setUsPrice(usdPrice);// 美元单价
				customsDeclareItemDTO.setUsAmount(usdPrice.multiply(new BigDecimal(item.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP));// 美元总价

				customsDeclareItemList.add(customsDeclareItemDTO);

				totalNum = totalNum + item.getNum();
				totalGrossWeight = totalGrossWeight + customsDeclareItemDTO.getTotalGrossWeight();
				totalNetWeight = totalNetWeight + customsDeclareItemDTO.getTotalNetWeight();
				totalAmount = totalAmount.add(customsDeclareItemDTO.getAmount());
				totalUsAmount = totalUsAmount.add(customsDeclareItemDTO.getUsAmount());
				totalBoxNum = totalBoxNum + (customsDeclareItemDTO.getCartons() == null ? 0 : customsDeclareItemDTO.getCartons());
			}
			String originCountry = "";
			if (originCountrySet != null && originCountrySet.size() > 0) {
				originCountry = StringUtils.join(originCountrySet, ";");
			}
			firstCustomsInfoDTO.setCountry(originCountry);// 原产国 用分号隔开
			String brands = "";
			if (brandSet != null && brandSet.size() > 0) {
				brands = StringUtils.join(brandSet, ";");
			}
			firstCustomsInfoDTO.setBrands(brands);// 品牌 用分号隔开
			firstCustomsInfoDTO.setItemList(customsDeclareItemList);
			firstCustomsInfoDTO.setTotalNum(totalNum);
			firstCustomsInfoDTO.setTotalGrossWeight(totalGrossWeight);
			firstCustomsInfoDTO.setTotalNetWeight(totalNetWeight);
			firstCustomsInfoDTO.setTotalAmount(totalAmount);
			firstCustomsInfoDTO.setTotalUsAmount(totalUsAmount);
			firstCustomsInfoDTO.setTotalBoxNum(totalBoxNum);
		}

		return firstCustomsInfoDTO;
	}

	/**
	 * 查询融资申请、融资赎回详情
	 *
	 * @param user
	 * @return
	 */
	@Override
	public SaleFinancingOrderDTO getFinanceDetail(Long orderId, User user) throws Exception {
		SaleOrderModel saleModel = saleOrderDao.searchById(orderId);
		if(!DERP_ORDER.SALEORDER_STATE_026.equals(saleModel.getState())) {
			throw new RuntimeException("销售订单状态必须为“已上架”");
		}
		SaleFinancingOrderDTO dto = new SaleFinancingOrderDTO();
		dto.setOrderId(orderId);
		dto = financingOrderDao.searchByDTO(dto);
		if (dto != null) {// 融资赎回
			SaleFinancingOrderItemDTO itemDto = new SaleFinancingOrderItemDTO();
			itemDto.setOrderId(dto.getId());
			List<SaleFinancingOrderItemDTO> itemList = financingOrderItemDao.listByDTO(itemDto);
			dto.setItemList(itemList);

		} else {// 融资申请
			List<SaleFinancingOrderItemDTO> itemList = new ArrayList<SaleFinancingOrderItemDTO>();
			// 宝丰 公司信息
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("code", "ERP19095700016");
			MerchantInfoMongo merchantMongo = merchantMongoDao.findOne(param);

			PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
			purchaseOrderModel.setMerchantId(merchantMongo.getMerchantId());
			purchaseOrderModel.setPoNo(saleModel.getPoNo());
			purchaseOrderModel.setBuId(saleModel.getBuId());
			purchaseOrderModel.setCurrency(saleModel.getCurrency());
			purchaseOrderModel.setBusinessModel(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_3);
			purchaseOrderModel.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_007);
			List<PurchaseOrderModel> purchaseOrderList = purchaseOrderDao.list(purchaseOrderModel);
			if(purchaseOrderList != null && purchaseOrderList.size() > 0){
				purchaseOrderModel = purchaseOrderList.get(0);
			}else{
				purchaseOrderModel = null;
			}

			SaleOrderItemModel item = new SaleOrderItemModel();
			item.setOrderId(orderId);
			List<SaleOrderItemModel> saleItemList = saleOrderItemDao.list(item);
			BigDecimal totalAmount = BigDecimal.ZERO;
			Map<String,SaleFinancingOrderItemDTO> itemMap = new HashMap<>();
			for (SaleOrderItemModel itemModel : saleItemList) {
				SaleFinancingOrderItemDTO itemDTO = null;
				if(itemMap.containsKey(itemModel.getGoodsNo())){
					itemDTO = itemMap.get(itemModel.getGoodsNo());
					Integer num = itemDTO.getNum() + itemModel.getNum();
					itemDTO.setNum(num);

				}else{
					itemDTO = new SaleFinancingOrderItemDTO();
					itemDTO.setGoodsId(itemModel.getGoodsId());
					itemDTO.setGoodsNo(itemModel.getGoodsNo());
					itemDTO.setGoodsCode(itemModel.getGoodsCode());
					itemDTO.setGoodsName(itemModel.getGoodsName());
					itemDTO.setNum(itemModel.getNum());
					itemDTO.setContractNo(itemModel.getContractNo());
					itemDTO.setRemark(itemModel.getRemark());
				}
				/**
				 * 查询宝丰公司（ERP19095700016）是否存在对应PO号的采购单，条件：
				 * 公司=宝丰，采购单PO号=当前销售单PO号，采购单状态=已完结，采购单事业部=销售单事业部，采购币种=销售单币种，采购单业务模式=采销执行。
				 */
				BigDecimal amount = itemDTO.getAmount() == null ? BigDecimal.ZERO:itemDTO.getAmount();
//				if (purchaseOrderModel != null) {
//					// 根据标准条码 查询宝丰对应的商品信息，然后查找采购单是否有对应商品，只取其中一条
//					Map<String, Object> merchadiseMap = new HashMap<String, Object>();
//					merchadiseMap.put("commbarcode", itemModel.getCommbarcode());
//					merchadiseMap.put("merchantId", merchantMongo.getMerchantId());
//					merchadiseMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
//					List<MerchandiseInfoMogo> merchandisList = merchandiseInfoMogoDao.findAll(merchadiseMap);
//					if (merchandisList != null && merchandisList.size() > 0) {
//						for (MerchandiseInfoMogo merchandise : merchandisList) {
//							PurchaseOrderItemModel purchaseItemModel = new PurchaseOrderItemModel();
//							purchaseItemModel.setPurchaseOrderId(purchaseOrderModel.getId());
//							purchaseItemModel.setBarcode(merchandise.getMerchandiseId());
//							purchaseItemModel.setNum(itemDTO.getNum());
//							purchaseItemModel = purchaseOrderItemDao.searchByModel(purchaseItemModel);
//							if (purchaseItemModel != null) {
//								itemDTO.setAmount(amount.add(purchaseItemModel.getTaxAmount()));
//								totalAmount = totalAmount.add(purchaseItemModel.getTaxAmount() == null ? BigDecimal.ZERO
//										: purchaseItemModel.getTaxAmount());
//								break;
//							}
//
//						}
//					}
////					itemList.add(itemDTO);
//				}
				itemMap.put(itemModel.getGoodsNo(), itemDTO);
			}

			dto = new SaleFinancingOrderDTO();
			dto.setCustomerName(saleModel.getCustomerName());
			dto.setPoNo(saleModel.getPoNo());
			dto.setCurrency(DERP.getLabelByKey(DERP.currencyCodeList, saleModel.getCurrency()));
			dto.setBuName(saleModel.getBuName());
			dto.setCreateName(user.getName());
			dto.setCreateDate(TimeUtils.getNow());
			dto.setSaleAmount(totalAmount);
			dto.setItemList(new ArrayList<>(itemMap.values()));

		}
		return dto;
	}

	/**
	 * 生成销售sd
	 * @throws Exception
	 */
	private void generateSaleSdOrder(ShelfModel shelfModel, List<SaleShelfModel> saleShelfList, User user) throws Exception {
		Map<String, List<SaleSdOrderItemModel>> resultMap = new HashMap<String, List<SaleSdOrderItemModel>>();
		Map<String, BigDecimal> amountMap = new HashMap<String, BigDecimal>();
		Map<String, Integer> numMap = new HashMap<String, Integer>();

		//查询上架单是否存在销售SD单，若存在，先删除，再重新生成
		SaleSdOrderModel querySaleSdOrder = new SaleSdOrderModel();
		querySaleSdOrder.setOrderCode(shelfModel.getCode());
		List<SaleSdOrderModel> querySaleSdOrderList = saleSdOrderDao.list(querySaleSdOrder);
		if(querySaleSdOrderList != null && querySaleSdOrderList.size() > 0){
			for(SaleSdOrderModel delSdModel:querySaleSdOrderList){
				delSdModel.setState(DERP.DEL_CODE_006);
				delSdModel.setModifier(user.getId());
				delSdModel.setModifiyName(user.getName());
				delSdModel.setModifyDate(TimeUtils.getNow());
				saleSdOrderDao.modify(delSdModel);
			}

		}

		// 1、以“公司+事业部+客户”查询销售SD配置表 ,上架日期 在生效日期范围内，若无则结束；
		SdSaleConfigDTO config = new SdSaleConfigDTO();
		config.setMerchantId(shelfModel.getMerchantId());
		config.setBuId(shelfModel.getBuId());
		config.setCustomerId(shelfModel.getCustomerId());
		config.setOrderDate(shelfModel.getShelfDate());
		config.setStatus(DERP_ORDER.SDPURCHASE_STATUS_1);
		List<SdSaleConfigDTO> configList = sdSaleConfigDao.listDTO(config);
		if (configList == null || configList.size() <= 0) {
			return;
		}
		// 审核时间为最新的一条配置信息
		configList = configList.stream().sorted(Comparator.comparing(SdSaleConfigDTO::getAuditDate).reversed()).collect(Collectors.toList());
		config = configList.get(0);

		List<SdSaleConfigItemModel> sdConfigItemList = new ArrayList<>();
		//获取单比例配置
		SdSaleConfigItemModel queryItemModel = new SdSaleConfigItemModel() ;
		queryItemModel.setSaleConfigId(config.getId());
		queryItemModel.setSdType(DERP_ORDER.SDTYPECONFIG_TYPE_1);
		List<SdSaleConfigItemModel> simpleConfigItemList = sdSaleConfigItemDao.list(queryItemModel) ;
		if(simpleConfigItemList != null && simpleConfigItemList.size() > 0){
			sdConfigItemList.addAll(simpleConfigItemList);
		}
		List<String> commBarcodeList = saleShelfList.stream().map(SaleShelfModel::getCommbarcode).distinct().collect(Collectors.toList());
		for(String commbarcode : commBarcodeList){
			//获取多比例配置
			queryItemModel.setSaleConfigId(config.getId());
			queryItemModel.setSdType(DERP_ORDER.SDTYPECONFIG_TYPE_2);
			queryItemModel.setCommbarcode(commbarcode);
			List<SdSaleConfigItemModel> multipleConfigItemList = sdSaleConfigItemDao.list(queryItemModel) ;
			if(multipleConfigItemList != null && multipleConfigItemList.size() > 0){
				sdConfigItemList.addAll(multipleConfigItemList);
			}
		}
		Map<String,SaleSdOrderModel> orderMap = new HashMap<>();
		Map<String,List<SaleSdOrderItemModel>> orderItemMap = new HashMap<>();
		for(SdSaleConfigItemModel itemConfig : sdConfigItemList){
			String key = itemConfig.getSaleConfigId()+"_"+itemConfig.getSdTypeId();
			if(!orderMap.containsKey(key)){
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
				sdOrderModel.setIsWriteOff("0");

				orderMap.put(key, sdOrderModel);
			}
			List<SaleSdOrderItemModel> itemList = orderItemMap.get(key);
			for(SaleShelfModel shelfItem : saleShelfList){
				//好品量为0，跳过
				if(shelfItem.getShelfNum().intValue() < 1){
					continue;
				}
				if(DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(itemConfig.getSdType()) && !itemConfig.getCommbarcode().equals(shelfItem.getCommbarcode())) {
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

				if(itemList == null){
					itemList = new ArrayList<>();
				}
				itemList.add(sdItemModel);
			}
			orderItemMap.put(key, itemList);
		}
		for(String key : orderMap.keySet()){
			SaleSdOrderModel saleSdOrderModel = orderMap.get(key);
			List<SaleSdOrderItemModel> sdOrderItemList = orderItemMap.get(key);
			if(sdOrderItemList == null || sdOrderItemList.size()< 1){
				continue;
			}
			BigDecimal totalSdAmount = sdOrderItemList.stream().map(SaleSdOrderItemModel :: getSdAmount ).reduce(BigDecimal.ZERO, BigDecimal::add);
			Integer totalSdNum = sdOrderItemList.stream().mapToInt(SaleSdOrderItemModel :: getNum ).sum();
			saleSdOrderModel.setTotalSdAmount(totalSdAmount);
			saleSdOrderModel.setTotalSdNum(totalSdNum);
			Long num = saleSdOrderDao.save(saleSdOrderModel) ;

			for(SaleSdOrderItemModel sdItemModel : sdOrderItemList){
				sdItemModel.setSaleSdOrderId(num);
				saleSdOrderItemDao.save(sdItemModel) ;
			}
		}
	}
	@Override
	public Boolean checkCreateDeclare(String ids) throws Exception{
		List<Long> saleIds = StrUtils.parseIds(ids);
		List<String> checkOrder = new ArrayList<String>();
		for(Long saleId :saleIds) {
			SaleOrderModel model = saleOrderDao.searchById(saleId);
			if(StringUtils.isNotBlank(model.getWriteOffStatus())){
				throw new RuntimeException("销售订单红冲状态不为空，不能生成预申报单");
			}
			//销售订单状态必须为【已审核】或【部分出库】
			if(!DERP_ORDER.SALEORDER_STATE_003.equals(model.getState()) &&!DERP_ORDER.SALEORDER_STATE_019.equals(model.getState())) {
				throw new RuntimeException("销售订单状态必须为“已审核”或“部分出库”");
			}

			String key = model.getMerchantId()+model.getBuId()+model.getOutDepotId()+ model.getCurrency();
			if(checkOrder.size() < 1){
				checkOrder.add(key);
			}else if (!checkOrder.contains(key)) {
				throw new RuntimeException("请选择相同公司+事业部+出库仓库+销售币种的销售单据");
			}
		}
		return true;
	}

	//修改po
	@Override
	public void modifyPoNo(Long orderId, User user , String poNo, String remark) throws Exception{
		SaleOrderModel saleOrder = saleOrderDao.searchById(orderId);
		if(StringUtils.isNotBlank(saleOrder.getWriteOffStatus())){
			throw new RuntimeException("订单红冲状态不为空，修改PO号失败");
		}
		//订单状态为待审核、已审核、部分出库、已出库 才可以修改
		if(!(DERP_ORDER.SALEORDER_STATE_001.equals(saleOrder.getState()) || DERP_ORDER.SALEORDER_STATE_003.equals(saleOrder.getState()) ||
				DERP_ORDER.SALEORDER_STATE_019.equals(saleOrder.getState()) || DERP_ORDER.SALEORDER_STATE_018.equals(saleOrder.getState()))){
			throw new RuntimeException("订单状态为待审核、已审核、部分出库、已出库 才可以修改PO号");
		}
		if(StringUtils.isNotBlank(poNo) && !checkPoNo(poNo)){
			//输入时仅限中文、大小写字母、数字和“-”这4种字符
			throw new RuntimeException("PO号仅限中文、大小写字母、数字和“-”这4种字符");
		}
		//检查该销售订单有没有生成上架单，若有则提示该销售订单存在上架单不能修改PO号
		SaleShelfModel shelfModel = new SaleShelfModel();
		shelfModel.setOrderId(orderId);
		List<SaleShelfModel> shelfList = saleShelfDao.list(shelfModel);// 上架数据
		if(shelfList != null && shelfList.size() > 0){
			throw new RuntimeException("该销售订单存在上架单不能修改PO号");
		}

		//检查该销售订单有没有出库单，校验出库单的出库月份是否已关账
		SaleOutDepotModel outModel = new SaleOutDepotModel();
		outModel.setSaleOrderId(orderId);
		List<SaleOutDepotModel> saleOutList = saleOutDepotDao.list(outModel);
		if(saleOutList != null && saleOutList.size() > 0) {
			// 查询最大关账月份
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(saleOrder.getMerchantId());
			closeAccountsMongo.setDepotId(saleOrder.getOutDepotId());
			closeAccountsMongo.setBuId(saleOrder.getBuId());
			String maxDate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxDate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				for (SaleOutDepotModel saleOut : saleOutList) {
					if (saleOut.getDeliverDate() !=null && saleOut.getDeliverDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
						throw new RuntimeException("销售出库单：" + saleOut.getCode() + "，出库日期：" + saleOut.getDeliverDate() + " 月数据已关账，不予修改PO号");
					}
				}
			}
		}

		//更新销售订单 po
		saleOrder.setPoNo(poNo);
		saleOrder.setModifier(user.getId());
		saleOrder.setModifierUsername(user.getName());
		saleOrder.setModifyDate(TimeUtils.getNow());
		saleOrderDao.modify(saleOrder);

		//更新出库单
		for (SaleOutDepotModel saleOut : saleOutList) {
			saleOut.setPoNo(poNo);
			saleOut.setModifyDate(TimeUtils.getNow());
			saleOutDepotDao.modify(saleOut);
		}

		//如果预申报
		if(DERP_ORDER.SALEDECLARE_ISGENERATE_1.equals(saleOrder.getIsGenerateDeclare())) {
			//更新预申报单
			SaleDeclareOrderDTO declareDTO = new SaleDeclareOrderDTO();
			declareDTO.setSaleOrderIds(saleOrder.getId().toString());
			List<SaleDeclareOrderDTO> declareList = saleDeclareOrderDao.listSaleDeclareOrder(declareDTO);
			for(SaleDeclareOrderDTO declare : declareList) {
				SaleDeclareOrderModel declareModel = new SaleDeclareOrderModel();
				declareModel.setPoNo(poNo);
				declareModel.setId(declare.getId());
				declareModel.setModifier(user.getId());
				declareModel.setModifierUsername(user.getName());
				declareModel.setModifyDate(TimeUtils.getNow());
				saleDeclareOrderDao.modify(declareModel);
				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, declareModel.getCode(), "修改PO号", null, remark);
			}
		}

		//更新销售po关联表
		if (StringUtils.isNotBlank(poNo)) {
			// 先删除销售单_po关联表中对应的数据
			SalePoRelModel sobpm = new SalePoRelModel();
			sobpm.setOrderId(saleOrder.getId());
			sobpm.setState("0");
			sobpm.setMerchantId(user.getMerchantId()); // 公司ID
			salePoRelDao.delbyPoNoAndOrderId(sobpm);
			// 再新增到销售单_po关联表中
			if (poNo.indexOf("&") != -1) {
				String[] poNos = poNo.split("&"); // 将多个po拆分出来
				for (int i = 0; i < poNos.length; i++) {
					String po = poNos[i];
					SalePoRelModel salePoRelModel = new SalePoRelModel();
					salePoRelModel.setPoNo(po);
					salePoRelModel.setState("0"); // 类型:0:销售订单1:销售退订单
					salePoRelModel.setOrderId(saleOrder.getId());
					salePoRelModel.setOrderCode(saleOrder.getCode());
					salePoRelModel.setMerchantId(user.getMerchantId()); // 公司ID
					salePoRelModel.setMerchantName(user.getMerchantName()); // 公司名称
					salePoRelModel.setCreateDate(TimeUtils.getNow());
					// 不存在则保存到销售单_po关联表中去
					salePoRelDao.save(salePoRelModel);
				}
			} else { // 如果只一个po单号
				SalePoRelModel salePoRelModel = new SalePoRelModel();
				salePoRelModel.setPoNo(poNo);
				salePoRelModel.setState("0"); // 类型:0:销售订单1:销售退订单
				salePoRelModel.setOrderId(saleOrder.getId());
				salePoRelModel.setOrderCode(saleOrder.getCode());
				salePoRelModel.setMerchantId(user.getMerchantId()); // 公司ID
				salePoRelModel.setMerchantName(user.getMerchantName()); // 公司名称
				salePoRelModel.setCreateDate(TimeUtils.getNow());
				// 不存在则保存到销售单_po关联表中去
				salePoRelDao.save(salePoRelModel);
			}
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrder.getCode(), "修改PO号", null, remark);
	}
	//反审
	@Override
	public void reverseAudit(Long orderId, String remark , User user)throws Exception{
		SaleOrderModel saleOrder = saleOrderDao.searchById(orderId);
		//仅在订单状态为已审核且生成预申报单标识为否下 才可以反审
		if(!(DERP_ORDER.SALEORDER_STATE_003.equals(saleOrder.getState()) && DERP_ORDER.SALEDECLARE_ISGENERATE_0.equals(saleOrder.getIsGenerateDeclare()))){
			throw new RuntimeException("订单状态为【已审核】且生成预申报单标识为【否】下才可以反审");
		}
		if(StringUtils.isNotBlank(saleOrder.getWriteOffStatus())){
			throw new RuntimeException("订单红冲状态不为空，反审失败");
		}
		//出库仓信息
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depotId", saleOrder.getOutDepotId());
		DepotInfoMongo depot = depotInfoMongoDao.findOne(params);

		// 获取销售表体（商品信息）
		SaleOrderItemModel saleOrderItem = new SaleOrderItemModel();
		saleOrderItem.setOrderId(saleOrder.getId());
		List<SaleOrderItemModel> itemList = saleOrderItemDao.list(saleOrderItem);
		// 销售类型为购销-整批结算或购销-实销实结 且出库仓不为中转仓，推库存解冻
		if (!DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
			if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrder.getBusinessModel()) || DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrder.getBusinessModel())) {
				// 解冻
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now = sdf.format(new Date());
				InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
				inventoryFreezeRootJson.setMerchantId(saleOrder.getMerchantId().toString());
				inventoryFreezeRootJson.setMerchantName(saleOrder.getMerchantName());
				inventoryFreezeRootJson.setDepotId(saleOrder.getOutDepotId().toString());
				inventoryFreezeRootJson.setDepotName(saleOrder.getOutDepotName());
				inventoryFreezeRootJson.setOrderNo(saleOrder.getCode());
				inventoryFreezeRootJson.setBusinessNo(saleOrder.getCode());
				inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
				inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_002);
				inventoryFreezeRootJson.setSourceDate(now);
				inventoryFreezeRootJson.setOperateType("1");//操作类型  （0减，1加）
				inventoryFreezeRootJson.setIsReverse("1");//是否为反审  （0-否，1-是）,反审需要删除唯一标识表
				List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
				for (SaleOrderItemModel item : itemList) {
					InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();

					inventoryFreezeGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
					inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());
					inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());


					inventoryFreezeGoodsListJson.setDivergenceDate(now);
					inventoryFreezeGoodsListJson.setNum(item.getNum());
					// 海外仓冻结加理货单位
					if ("c".equals(depot.getType())) {
						if ("00".equals(saleOrder.getTallyingUnit())) {
							inventoryFreezeGoodsListJson.setUnit("0");
						} else if ("01".equals(saleOrder.getTallyingUnit())) {
							inventoryFreezeGoodsListJson.setUnit("1");
						} else if ("02".equals(saleOrder.getTallyingUnit())) {
							inventoryFreezeGoodsListJson.setUnit("2");
						}
					}
					inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
				}
				inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
				rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
			}
		}

		// 修改单据状态
		saleOrder.setState(DERP_ORDER.SALEORDER_STATE_008);
		saleOrder.setModifyDate(TimeUtils.getNow());
		saleOrder.setModifier(user.getId());
		saleOrder.setModifierUsername(user.getName());
		int flag = saleOrderDao.modify(saleOrder);

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrder.getCode(), "反审", null, remark);

	}

	@Override
	public List<SaleOrderItemModel> getStockOutItemList(List<Long> list , User user) throws Exception {
//		Map<String,SaleOrderItemModel> itemMap = new HashMap<String, SaleOrderItemModel>();
		List<SaleOrderItemModel> itemList = new ArrayList<SaleOrderItemModel>();
		for (Long id : list) {
			// 获取销售订单信息
			SaleOrderModel saleOrder = saleOrderDao.searchById(id);
			// 获取商品信息
			SaleOrderItemModel saleOrderItem = new SaleOrderItemModel();
			saleOrderItem.setOrderId(saleOrder.getId());
			List<SaleOrderItemModel> saleOrderItemList = saleOrderItemDao.list(saleOrderItem);
			//获取销售出库数量
			Map<Long, Integer> outNumMap = new HashMap<Long, Integer>();
			SaleOutDepotDTO saleOutDTO = new SaleOutDepotDTO();
			saleOutDTO.setSaleOrderCode(saleOrder.getCode());
			List<SaleOutDepotDTO> saleOutList = saleOutDepotDao.queryDTODetailsList(saleOutDTO);
			for(SaleOutDepotDTO queryOutDTO : saleOutList) {
				Integer transferNum = queryOutDTO.getTransferNum()==null ? 0:queryOutDTO.getTransferNum();// 好品数量
				Integer wornNum = queryOutDTO.getWornNum()==null ? 0:queryOutDTO.getWornNum();	// 坏品数量
				Integer totalNum = (outNumMap.get(queryOutDTO.getSaleItemId()) == null ? 0 :outNumMap.get(queryOutDTO.getSaleItemId()))+transferNum + wornNum;

				outNumMap.put(queryOutDTO.getSaleItemId(), totalNum);//记录已出库数量
			}
			for (SaleOrderItemModel saleItem : saleOrderItemList) {
				Integer outNum = outNumMap.get(saleItem.getId()) == null ? 0 : outNumMap.get(saleItem.getId());//已出库

				saleItem.setNum(saleItem.getNum()-outNum);// 剩余出库量

				itemList.add(saleItem);
			}
		}
		return itemList;
	}

	//	获取表体信息
	@Override
	public List<SaleOrderItemDTO> listSaleOrderItemDTO(SaleOrderItemDTO dto) throws Exception{
		return saleOrderItemDao.listSaleOrderItemDTO(dto);
	}

	/**
	 * 判断是否为数字或小数
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str){
		String pattern = "^[\\-]?[\\d]+(\\.[\\d]+)?$";
		Pattern p = Pattern.compile(pattern);
		Matcher isNum = p.matcher(str);
		return isNum.matches();
	}

	@Override
	public Boolean getPurchasePriceManage(Long id, Long supplierId, User user) throws Exception {
		SaleOrderModel model = saleOrderDao.searchById(id);
		if(model == null){
			throw new RuntimeException("销售订单不存在");
		}

		/**查询是否开启采购价格管理*/
		boolean purchasePriceManage = false;
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("buId", model.getBuId()) ;
		queryMap.put("merchantId", user.getMerchantId()) ;
		queryMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;
		MerchantBuRelMongo merchantBuRel = merchantBuRelMongoDao.findOne(queryMap);
		//事业部是否开启
		purchasePriceManage = !org.apache.commons.lang3.StringUtils.isBlank(merchantBuRel.getPurchasePriceManage()) && !DERP_SYS.PURCHASE_PRICE_MANAGE_0.equals(merchantBuRel.getPurchasePriceManage());
		if(purchasePriceManage){
			//供应商是否开启
			queryMap = new HashMap<String, Object>() ;
			queryMap.put("customerId", supplierId) ;
			queryMap.put("merchantId", user.getMerchantId()) ;
			queryMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;
			CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(queryMap);

			purchasePriceManage = customerRel != null && DERP_SYS.PURCHASE_PRICE_MANAGE_1.equals(customerRel.getPurchasePriceManage());
		}
		return purchasePriceManage;
	}

	@Override
	public Map<String, List<BigDecimal>> getPurchasePrice(Long id, Long supplierId , User user) throws Exception {
		Map<String, List<BigDecimal>> supplyPriceMap = new HashMap<String, List<BigDecimal>>();
		//获取商品信息
		SaleOrderModel model = saleOrderDao.searchById(id);
		if(model == null){
			throw new RuntimeException("销售订单不存在");
		}
		SaleOrderItemModel queryItem = new SaleOrderItemModel();
		queryItem.setOrderId(id);
		List<SaleOrderItemModel> itemList = saleOrderItemDao.list(queryItem);
		for(SaleOrderItemModel item : itemList) {
			Map<String, Object> smPriceMap = new HashMap<String, Object>() ;
			smPriceMap.put("merchantId", model.getMerchantId()) ;
			smPriceMap.put("customerId", supplierId) ;
			smPriceMap.put("commbarcode", item.getCommbarcode()) ;
			smPriceMap.put("buId", model.getBuId());
			smPriceMap.put("currency", model.getCurrency()) ;
			smPriceMap.put("state", DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003);
			List<SupplierMerchandisePriceMongo> smList = supplierMerchandisePriceMongoDao.findAll(smPriceMap);

			if(smList.isEmpty()) {
				return null;
			}

			for (SupplierMerchandisePriceMongo tempMongo : smList) {
				String effectiveDate = tempMongo.getEffectiveDate();
				String expiryDate = tempMongo.getExpiryDate();
				//是否在有效期内
				if(TimeUtils.parseFullTime(effectiveDate + " 00:00:00").getTime() <= TimeUtils.getNow().getTime()
						&& TimeUtils.parseFullTime(expiryDate + " 23:59:59").getTime() >= TimeUtils.getNow().getTime()) {
					BigDecimal supplyPrice = tempMongo.getSupplyPrice();
					// 理货单位不为空，且不为件时，进行箱、件、托转换
					if (StringUtils.isNotBlank(model.getTallyingUnit()) && !DERP.ORDER_TALLYINGUNIT_02.equals(model.getTallyingUnit())) {
						HashMap<String, Object> merchandiseMap = new HashMap<>();
						merchandiseMap.put("merchandiseId", item.getGoodsId());
						merchandiseMap.put("status", "1");

						MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseMap);
						if (DERP.ORDER_TALLYINGUNIT_01.equals(model.getTallyingUnit())) {
							if(merchandiseInfo.getBoxToUnit() == null || merchandiseInfo.getBoxToUnit() == 0) {
								throw new RuntimeException( "货号：" + merchandiseInfo.getGoodsNo() + "箱转件未维护");
							}
							supplyPrice = supplyPrice.multiply(new BigDecimal(merchandiseInfo.getBoxToUnit())).setScale(8, BigDecimal.ROUND_HALF_UP);

						} else if (DERP.ORDER_TALLYINGUNIT_00.equals(model.getTallyingUnit())) {
							if(merchandiseInfo.getTorrToUnit() == null || merchandiseInfo.getTorrToUnit() == 0) {
								throw new RuntimeException( "货号：" + merchandiseInfo.getGoodsNo() + "托转件未维护");
							}
							supplyPrice = supplyPrice.multiply(new BigDecimal(merchandiseInfo.getTorrToUnit())).setScale(8, BigDecimal.ROUND_HALF_UP);
						}
					}
					List<BigDecimal> priceList = new ArrayList<BigDecimal>();
					if(supplyPriceMap.containsKey(item.getGoodsNo())) {
						priceList =  supplyPriceMap.get(item.getGoodsNo());
					}
					if(!priceList.contains(supplyPrice)) {
						priceList.add(supplyPrice);
					}
					supplyPriceMap.put(item.getGoodsNo(), priceList);
				}
			}
		}
		return supplyPriceMap;
	}
	/**
	 * 批量驳回
	 */
	@Override
	public void batchRejection(String ids, User user) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		for(Long id : idList) {
			SaleOrderModel model = saleOrderDao.searchById(id);
			if(model == null){
				throw new RuntimeException("销售订单不存在");
			}

			if(!DERP_ORDER.SALEORDER_STATE_001.equals(model.getState())){
				throw new RuntimeException("销售订单:"+model.getCode()+"状态不为“待审核”");
			}

			model.setModifier(user.getId());
			model.setModifierUsername(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			model.setState(DERP_ORDER.SALEORDER_STATE_008);
			saleOrderDao.modify(model);

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, model.getCode(), "驳回", null, null);
		}

	}
	private boolean checkPoNo(String str){
		//输入时仅限中文、大小写字母、数字和“-”这4种字符
		String path="^[\\d\\w\\-\\u4e00-\\u9fa5]+";//定义匹配规则
		Pattern p=Pattern.compile(path);//实例化Pattern
		return p.matcher(str).matches();
	}

	@Override
	public List<Map<String, Object>> getStatusNum(SaleOrderDTO dto, User user) {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return null;
			}
			dto.setBuList(buIds);
		}
		List<Map<String, Object>> mapList = saleOrderDao.getStatusNum(dto) ;

		Long total = new Long(0) ;

		for (Map<String, Object> object : mapList) {
			if(object.get("num") != null) {
				total += (Long) object.get("num") ;
			}
		}

		Map<String, Object> totalMap = new HashMap<String, Object>() ;
		totalMap.put("total", total) ;
		mapList.add(totalMap) ;

		return mapList;
	}

	//申请判断是否可以红冲
	@Override
	public boolean validateApplyWriteOff(Long id) throws Exception {
		SaleOrderModel saleOrderModel = saleOrderDao.searchById(id);
		if(saleOrderModel == null){
			throw new RuntimeException("销售订单不存在");
		}
		if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
			throw new RuntimeException("【已红冲】或【待红冲】的销售订单不能申请红冲");
		}
		if(!(DERP_ORDER.SALEORDER_STATE_018.equals(saleOrderModel.getState()) ||
				DERP_ORDER.SALEORDER_STATE_031.equals(saleOrderModel.getState())||
				DERP_ORDER.SALEORDER_STATE_026.equals(saleOrderModel.getState()))){
			throw new RuntimeException("只有【已出库】、【部分上架】、【已上架】的订单才能申请红冲");
		}
		SaleShelfIdepotModel shelfIdpotModel = new SaleShelfIdepotModel();
		shelfIdpotModel.setSaleOrderId(saleOrderModel.getId());
		List<SaleShelfIdepotModel> shelfIdepotList = saleShelfIdepotDao.list(shelfIdpotModel);
		for(SaleShelfIdepotModel tempIdepot : shelfIdepotList){
			if(DERP_ORDER.SALESHELFIDEPOT_STATUS_011.equals(tempIdepot.getState()) ||
			   DERP_ORDER.SALESHELFIDEPOT_STATUS_028.equals(tempIdepot.getState())){
				throw new RuntimeException("请先删除该订单关联的【待入库】或【入库中】的上架入库单");
			}
		}

		SaleReturnOrderModel returnOrderModel = new SaleReturnOrderModel();
		returnOrderModel.setSaleOrderRelCode(saleOrderModel.getCode());
		List<SaleReturnOrderModel> returnOrderList = saleReturnOrderDao.list(returnOrderModel);
		if(returnOrderList != null && returnOrderList.size() > 0){
			return true;
		}
		return false;
	}

	@Override
	public void saveWriteOff(Long id, User user, String reason) throws Exception {
		SaleOrderModel saleOrderModel = saleOrderDao.searchById(id);
		if(saleOrderModel == null){
			throw new RuntimeException("销售订单不存在");
		}
		if(StringUtils.isBlank(reason)){
			throw new RuntimeException("申请红冲原因不能为空");
		}
		SaleOrderModel updateOrderModel = new SaleOrderModel();
		updateOrderModel.setId(id);
		updateOrderModel.setWriteOffStatus(DERP_ORDER.SALEORDER_WRITEOFF_STATUS_001);
		updateOrderModel.setModifier(user.getId());
		updateOrderModel.setModifierUsername(user.getName());
		updateOrderModel.setModifyDate(TimeUtils.getNow());
		saleOrderDao.modify(updateOrderModel);

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrderModel.getCode(), "申请红冲", null, reason);

	}

	@Override
	public String showAuditWriteOff(Long id) throws Exception {
		SaleOrderModel saleOrderModel = saleOrderDao.searchById(id);
		if(saleOrderModel == null){
			throw new RuntimeException("销售订单不存在");
		}
		if(!DERP_ORDER.SALEORDER_WRITEOFF_STATUS_001.equals(saleOrderModel.getWriteOffStatus())){
			throw new RuntimeException("销售订单红冲状态不是【待红冲】");
		}

		OperateLogModel operateLogModel = new OperateLogModel();
		operateLogModel.setOperateAction("申请红冲");
		operateLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_5);
		operateLogModel.setRelCode(saleOrderModel.getCode());
		OperateLogModel latestOperate = operateLogDao.getLatestOperateLog(operateLogModel);

		return latestOperate.getOperateRemark();
	}

	//审核判断是否可以红冲
	@Override
	public Map<String,Object> validateAuditWriteOff(Long id, String writeOffDate, String auditResult, User user) throws Exception {
		Map<String,Object> resultMap = new HashMap<>();
		SaleOrderModel saleOrderModel = saleOrderDao.searchById(id);
		if(saleOrderModel == null){
			throw new RuntimeException("销售订单不存在");
		}
		if(!DERP_ORDER.SALEORDER_WRITEOFF_STATUS_001.equals(saleOrderModel.getWriteOffStatus())){
			throw new RuntimeException("销售订单红冲状态不是【待红冲】");
		}
		if(StringUtils.isBlank(writeOffDate)){
			throw new RuntimeException("归属日期不能为空");
		}
		if(StringUtils.isBlank(auditResult)){
			throw new RuntimeException("请选择审核结果");
		}
		// 归属日期不能大于当前日期
		if (TimeUtils.parseDay(writeOffDate).getTime() > TimeUtils.parseDay(TimeUtils.formatDay()).getTime()) {
			throw new RuntimeException("归属日期不能大于当前日期");
		}
		// 获取最大的关账/月结日期
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(user.getMerchantId());
		closeAccountsMongo.setBuId(saleOrderModel.getBuId());
		closeAccountsMongo.setDepotId(saleOrderModel.getOutDepotId());
		String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
		String maxCloseAccountsMonth = "";
		if (org.apache.commons.lang3.StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
			// 归属日期日期必须大于关账日期
			if (TimeUtils.parseDay(writeOffDate).getTime() < TimeUtils.parseDay(maxCloseAccountsMonth).getTime()) {
				throw new RuntimeException("归属时间必须大于关账/月结日期");
			}
		}
		Timestamp maxAuditDate = null;
		//获取最大出库日期
		SaleOutDepotModel outDepotModel = new SaleOutDepotModel();
		outDepotModel.setSaleOrderId(saleOrderModel.getId());
		List<SaleOutDepotModel> outDepotList = saleOutDepotDao.list(outDepotModel);
		if( outDepotList!= null && outDepotList.size() > 0){
			outDepotList = outDepotList.stream().sorted(Comparator.comparing(SaleOutDepotModel:: getDeliverDate).reversed()).collect(Collectors.toList());
			maxAuditDate = outDepotList.get(0).getDeliverDate();
		}
		//获取最大上架日期
		ShelfModel shelfModel = new ShelfModel();
		shelfModel.setSaleOrderId(saleOrderModel.getId());
		List<ShelfModel> shelfList = shelfDao.list(shelfModel);
		if( shelfList!= null && shelfList.size() > 0){
			shelfList = shelfList.stream().sorted(Comparator.comparing(ShelfModel:: getShelfDate).reversed()).collect(Collectors.toList());
			//出库时间小于上架时间，取上架时间
			if(maxAuditDate.getTime() < shelfList.get(0).getShelfDate().getTime()){
				maxAuditDate = shelfList.get(0).getShelfDate();
			}

		}

		String maxAuditDateStr = TimeUtils.formatDay(maxAuditDate);
		if(TimeUtils.parseDay(maxAuditDateStr).getTime() > TimeUtils.parseDay(writeOffDate).getTime()){
			throw new RuntimeException("归属日期必须大于等于 最大出库时间/最大上架时间");
		}

		//是否存在上架入库单
		SaleShelfIdepotModel shelfIdpotModel = new SaleShelfIdepotModel();
		shelfIdpotModel.setSaleOrderId(saleOrderModel.getId());
		List<SaleShelfIdepotModel> shelfIdepotList = saleShelfIdepotDao.list(shelfIdpotModel);
		Map<String,Map<String,Object>> shelfIdepotMap = new HashMap<>();
		if(shelfIdepotList != null && shelfIdepotList.size() > 0){
			for(SaleShelfIdepotModel tempIdepot : shelfIdepotList){
				if(DERP_ORDER.SALESHELFIDEPOT_STATUS_011.equals(tempIdepot.getState()) ||
						DERP_ORDER.SALESHELFIDEPOT_STATUS_028.equals(tempIdepot.getState())){
					throw new RuntimeException("请先删除该订单关联的【待入库】或【入库中】的上架入库单");
				}
			}
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("saleOrderId", saleOrderModel.getId());
			List<SaleShelfIdepotItemDTO> shelfIdepotItemList = saleShelfIdepotItemDao.getItemsGroupByBatch(paramMap);


			for(SaleShelfIdepotItemDTO queryIdepotItem : shelfIdepotItemList){
				String goodsNo = queryIdepotItem.getInGoodsNo();
				Long goodsId = queryIdepotItem.getInGoodsId();

				Map<String,Object> saveIndepotItem = shelfIdepotMap.get(goodsNo);
				if(saveIndepotItem != null){
					Integer totalNormalNum = (Integer) saveIndepotItem.get("normalNum");
					Integer totalDamagedNum = (Integer) saveIndepotItem.get("damagedNum");

					Integer normalNum = queryIdepotItem.getNormalNum() + totalNormalNum;
					Integer damageNum = queryIdepotItem.getDamagedNum() + totalDamagedNum;

					saveIndepotItem.put("normalNum", normalNum);
					saveIndepotItem.put("damagedNum", damageNum);

				}else{
					saveIndepotItem = new HashMap<>();
					saveIndepotItem.put("goodsId", goodsId);
					saveIndepotItem.put("goodsNo", goodsNo);
					saveIndepotItem.put("unit", saleOrderModel.getTallyingUnit());
					saveIndepotItem.put("normalNum", queryIdepotItem.getNormalNum());
					saveIndepotItem.put("damagedNum", queryIdepotItem.getDamagedNum());
					saveIndepotItem.put("batchNo", queryIdepotItem.getBatchNo());
					saveIndepotItem.put("productionDate", queryIdepotItem.getProductionDate());
					saveIndepotItem.put("overdueDate", queryIdepotItem.getOverdueDate());
					shelfIdepotMap.put(goodsNo, saveIndepotItem);
				}
			}
		}
		if(!shelfIdepotMap.isEmpty()){
			resultMap.put("shelfIdepotItemList", new ArrayList(shelfIdepotMap.values()));
		}else{
			resultMap.put("shelfIdepotItemList", null);
		}

		//是否有销售退货单
		SaleReturnOrderModel returnOrderModel = new SaleReturnOrderModel();
		returnOrderModel.setSaleOrderRelCode(saleOrderModel.getCode());
		List<SaleReturnOrderModel> returnOrderList = saleReturnOrderDao.list(returnOrderModel);
		if(returnOrderList != null && returnOrderList.size() > 0){
			resultMap.put("isRelSaleReturn", true);
		}else{
			resultMap.put("isRelSaleReturn", false);
		}

		return resultMap;
	}

	@Override
	public void auditWriteOff(Long id, String writeOffDate, String auditResult, User user) throws Exception {
		SaleOrderModel saleOrderModel = saleOrderDao.searchById(id);
		if(saleOrderModel == null){
			throw new RuntimeException("销售订单不存在");
		}
		if(!DERP_ORDER.SALEORDER_WRITEOFF_STATUS_001.equals(saleOrderModel.getWriteOffStatus())){
			throw new RuntimeException("销售订单红冲状态不是【待红冲】");
		}
		if(StringUtils.isBlank(writeOffDate)){
			throw new RuntimeException("归属日期不能为空");
		}
		if(StringUtils.isBlank(auditResult)){
			throw new RuntimeException("请选择审核结果");
		}
		if("0".equals(auditResult)){
			//修改销售订单红冲状态
			saleOrderModel.setWriteOffStatus(null);
			saleOrderModel.setModifier(user.getId());
			saleOrderModel.setModifierUsername(user.getName());
			saleOrderModel.setModifyDate(TimeUtils.getNow());
			saleOrderDao.modifyWithNull(saleOrderModel);
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrderModel.getCode(), "审核红冲", null, "驳回");
			return;
		}
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", saleOrderModel.getOutDepotId());// 根据仓库id
		DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params); 	// 获取仓库信息
		// 海外仓冻结加理货单位
		String inventoryUnit = "";
		if (DERP.ORDER_TALLYINGUNIT_00.equals(saleOrderModel.getTallyingUnit())) {
			inventoryUnit = DERP.INVENTORY_UNIT_0;
		} else if (DERP.ORDER_TALLYINGUNIT_01.equals(saleOrderModel.getTallyingUnit())) {
			inventoryUnit = DERP.INVENTORY_UNIT_1;
		} else if (DERP.ORDER_TALLYINGUNIT_02.equals(saleOrderModel.getTallyingUnit())) {
			inventoryUnit = DERP.INVENTORY_UNIT_2;
		}
		//红冲销售出库单
		Map<String, Object> resultMap = writeOffOutDepot( id, user, depot, inventoryUnit, writeOffDate);
		Map<Long,Long> outItemNewAndOldMap = (Map<Long,Long>) resultMap.get("outItemNewAndOldMap");
		Map<Long, SaleOutDepotModel> outModelNewAndOldMap = (Map<Long, SaleOutDepotModel>) resultMap.get("outModelNewAndOldMap");

		List<InventoryWriteOffRootJson> saleOutInventoryWriteOffRootJsonList = (List<InventoryWriteOffRootJson>) resultMap.get("inventoryWriteOffRootJsonList");
		if(saleOutInventoryWriteOffRootJsonList != null && saleOutInventoryWriteOffRootJsonList.size() > 0){
			for(InventoryWriteOffRootJson rootJson : saleOutInventoryWriteOffRootJsonList){
				rocketMQProducer.send(JSONObject.fromObject(rootJson).toString(), MQInventoryEnum.INVENTORY_WRITE_OFF.getTopic(),MQInventoryEnum.INVENTORY_WRITE_OFF.getTags());
			}
		}
		//红冲上架入库单
		List<InventoryWriteOffRootJson> saleIdepotInventoryWriteOffRootJsonList = writeOffShelfIdepot(id, user, depot, inventoryUnit, writeOffDate,outItemNewAndOldMap,outModelNewAndOldMap);
		if(saleIdepotInventoryWriteOffRootJsonList != null && saleIdepotInventoryWriteOffRootJsonList.size() > 0){
			for(InventoryWriteOffRootJson rootJson : saleIdepotInventoryWriteOffRootJsonList){
				rocketMQProducer.send(JSONObject.fromObject(rootJson).toString(), MQInventoryEnum.INVENTORY_WRITE_OFF.getTopic(),MQInventoryEnum.INVENTORY_WRITE_OFF.getTags());
			}
		}

		//修改销售订单红冲状态
		SaleOrderModel updateSaleOrderModel = new SaleOrderModel();
		updateSaleOrderModel.setId(id);
		updateSaleOrderModel.setWriteOffStatus(DERP_ORDER.SALEORDER_WRITEOFF_STATUS_002);
		updateSaleOrderModel.setModifier(user.getId());
		updateSaleOrderModel.setModifierUsername(user.getName());
		updateSaleOrderModel.setModifyDate(TimeUtils.getNow());
		saleOrderDao.modify(updateSaleOrderModel);

		//修改采购转销售的关联单号和状态
		PlatformPurchaseOrderDTO platformPurchaseOrderDTO = new PlatformPurchaseOrderDTO();
		platformPurchaseOrderDTO.setSaleCode(saleOrderModel.getCode());
		List<PlatformPurchaseOrderDTO> platformPurchaseOrderList = platformPurchaseOrderDao.listDTO(platformPurchaseOrderDTO);
		for(PlatformPurchaseOrderDTO tempPlatform : platformPurchaseOrderList){
			String saleCode = tempPlatform.getSaleCode();
			Set<String> saleCodeSet = new HashSet<>();
			if(saleCode.indexOf(",") > -1){
				String[] saleCodeArr = saleCode.split(",");
				for(String sc : saleCodeArr){
					if(!saleOrderModel.getCode().equals(sc)){
						saleCodeSet.add(sc);
					}
				}
				saleCode = StringUtils.join(saleCodeSet,",");
				tempPlatform.setSaleCode(saleCode);
			}else{
				tempPlatform.setSaleCode(null);
				tempPlatform.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_2);
			}
			PlatformPurchaseOrderModel updatePlatformModel = new PlatformPurchaseOrderModel();
			BeanUtils.copyProperties(platformPurchaseOrderDTO, updatePlatformModel);
			platformPurchaseOrderDao.modifyWherSubmit(updatePlatformModel);

		}

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleOrderModel.getCode(), "审核红冲", null, "通过");

	}

	private Map<String, Object> writeOffOutDepot(Long saleOrderId, User user, DepotInfoMongo depot, String inventoryUnit, String writeOffDate) throws Exception {
		SaleOutDepotModel outDepotModel = new SaleOutDepotModel();
		outDepotModel.setSaleOrderId(saleOrderId);
		List<SaleOutDepotModel> outDepotList = saleOutDepotDao.list(outDepotModel);
		Map<String, List<SaleOutDepotItemModel>> outDepotItemMap = new HashMap<>();
		List<InventoryWriteOffRootJson> inventoryWriteOffRootJsonList = new ArrayList<>();
		Map<Long,SaleOutDepotModel> outModelNewAndOldMap = new HashMap<>();
		for(SaleOutDepotModel queryOutDepotModel : outDepotList){
			String saleOutDepotCode = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSCK);

			SaleOutDepotItemModel outItemModel = new SaleOutDepotItemModel();
			outItemModel.setSaleOutDepotId(queryOutDepotModel.getId());
			List<SaleOutDepotItemModel> queryOutItemList = saleOutDepotItemDao.list(outItemModel);
			for (SaleOutDepotItemModel item : queryOutItemList) {
				Integer transferNum = item.getTransferNum() == null ? 0 : item.getTransferNum();
				Integer wornNum = item.getWornNum() == null ? 0 : item.getWornNum();

				item.setSaleOutDepotId(null);
				item.setTransferNum(transferNum * (-1));
				item.setWornNum(wornNum * (-1));
				item.setCreater(user.getId());
				item.setCreateDate(TimeUtils.getNow());
			}
			Long originOrderId = queryOutDepotModel.getId();
			String originalOrderCode = queryOutDepotModel.getCode();

			queryOutDepotModel.setCode(saleOutDepotCode);
			queryOutDepotModel.setDeliverDate(TimeUtils.parseDay(writeOffDate));
			queryOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_027);
			queryOutDepotModel.setIsWriteOff(DERP_ORDER.SALEOUTDEPOT_ISWRITEOFF_1);
			queryOutDepotModel.setCreater(user.getId());
			queryOutDepotModel.setCreateDate(TimeUtils.getNow());
			queryOutDepotModel.setOriginalSaleOutOrderId(originOrderId);
			queryOutDepotModel.setOriginalSaleOutOrderCode(originalOrderCode);

			outDepotItemMap.put(saleOutDepotCode, queryOutItemList);

			//封装红冲加减库存报文
			InventoryWriteOffRootJson inventoryWriteOffRootJson = new InventoryWriteOffRootJson();
			inventoryWriteOffRootJson.setBackTopic(MQPushBackOrderEnum.SALE_WRITE_OFF_PUSH_BACK.getTopic());
			inventoryWriteOffRootJson.setBackTags(MQPushBackOrderEnum.SALE_WRITE_OFF_PUSH_BACK.getTags());
			Map<String, Object> customParam = new HashMap<String, Object>();
			customParam.put("orderType", "1");//1-销售出库单 2-上架入库单
			customParam.put("saleOrderId",  saleOrderId.toString());
			inventoryWriteOffRootJson.setCustomParam(customParam);
			inventoryWriteOffRootJson.setMerchantId(queryOutDepotModel.getMerchantId().toString());
			inventoryWriteOffRootJson.setMerchantName(queryOutDepotModel.getMerchantName());
			inventoryWriteOffRootJson.setOrderNo(saleOutDepotCode);
			inventoryWriteOffRootJson.setSourceOrderNo(originalOrderCode);
			inventoryWriteOffRootJson.setDivergenceDate(TimeUtils.formatFullTime(TimeUtils.parseFullTime(writeOffDate+" 00:00:00")));	// 发货时间

			inventoryWriteOffRootJsonList.add(inventoryWriteOffRootJson);
		}
		Map<Long,Long> outItemNewAndOldMap = new HashMap<>();
		for(SaleOutDepotModel saveOutDepot : outDepotList){
			Long oldOutId = saveOutDepot.getId();
			saveOutDepot.setId(null);
			Long num = saleOutDepotDao.save(saveOutDepot);

			saveOutDepot.setId(num);
			outModelNewAndOldMap.put(oldOutId , saveOutDepot);

			List<SaleOutDepotItemModel> itemList = outDepotItemMap.get(saveOutDepot.getCode());
			for(SaleOutDepotItemModel item : itemList){
				Long itemOldId = item.getId();
				item.setId(null);
				item.setSaleOutDepotId(num);
				Long itemNewId = saleOutDepotItemDao.save(item);

				outItemNewAndOldMap.put(itemOldId, itemNewId);
			}
		}
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("inventoryWriteOffRootJsonList",inventoryWriteOffRootJsonList);
		resultMap.put("outItemNewAndOldMap",outItemNewAndOldMap);
		resultMap.put("outModelNewAndOldMap",outModelNewAndOldMap);

		return resultMap;
	}

	private List<InventoryWriteOffRootJson> writeOffShelfIdepot(Long saleOrderId, User user, DepotInfoMongo depot,String inventoryUnit,
																 String writeOffDate,Map<Long,Long> outItemNewAndOldMap,Map<Long,SaleOutDepotModel> outModelNewAndOldMap) throws Exception{
		SaleShelfIdepotModel shelfIdpotModel = new SaleShelfIdepotModel();
		shelfIdpotModel.setSaleOrderId(saleOrderId);
		List<SaleShelfIdepotModel> shelfIdepotList = saleShelfIdepotDao.list(shelfIdpotModel);
		Map<String, List<SaleShelfIdepotItemModel>> saleShelfIdepotItemMap = new HashMap<>();
		List<InventoryWriteOffRootJson> inventoryWriteOffRootJsonList = new ArrayList<>();
		for(SaleShelfIdepotModel shelfIdepotModel : shelfIdepotList){
			String saleShelfIdepotCode = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_SJRK);
			SaleShelfIdepotItemModel shelfIdpotItemModel = new SaleShelfIdepotItemModel();
			shelfIdpotItemModel.setSshelfIdepotId(shelfIdepotModel.getId());
			List<SaleShelfIdepotItemModel> shelfIdepotItemList = saleShelfIdepotItemDao.list(shelfIdpotItemModel);
			for (SaleShelfIdepotItemModel item : shelfIdepotItemList) {
				Integer normalNum = item.getNormalNum() == null ? 0 : item.getNormalNum();
				Integer damagedNum = item.getDamagedNum() == null ? 0 : item.getDamagedNum();

				item.setId(null);
				item.setSshelfIdepotId(null);
				item.setNormalNum(normalNum * (-1));
				item.setDamagedNum(damagedNum * (-1));
				item.setCreater(user.getId());
				item.setCreateDate(TimeUtils.getNow());

				Long oldSaleOutItemId = item.getSaleOutDepotItemId();
				item.setSaleOutDepotItemId(outItemNewAndOldMap.get(oldSaleOutItemId));
			}
			Long originOrderId = shelfIdepotModel.getId();
			String originalOrderCode = shelfIdepotModel.getCode();

			shelfIdepotModel.setId(null);
			shelfIdepotModel.setCode(saleShelfIdepotCode);
			shelfIdepotModel.setShelfDate(TimeUtils.parseDay(writeOffDate));
			shelfIdepotModel.setState(DERP_ORDER.SALESHELFIDEPOT_STATUS_028);
			shelfIdepotModel.setIsWriteOff(DERP_ORDER.SALEOUTDEPOT_ISWRITEOFF_1);
			shelfIdepotModel.setCreater(user.getId());
			shelfIdepotModel.setCreateDate(TimeUtils.getNow());
			shelfIdepotModel.setOriginalShelfIdepotId(originOrderId);
			shelfIdepotModel.setOriginalShelfIdepotCode(originalOrderCode);

			SaleOutDepotModel saleOutDepotModel = outModelNewAndOldMap.get(shelfIdepotModel.getSaleOutDepotId());
			shelfIdepotModel.setSaleOutDepotId(saleOutDepotModel.getId());
			shelfIdepotModel.setSaleOutCode(saleOutDepotModel.getCode());

			saleShelfIdepotItemMap.put(saleShelfIdepotCode, shelfIdepotItemList);

			//封装红冲加减库存报文
			InventoryWriteOffRootJson inventoryWriteOffRootJson = new InventoryWriteOffRootJson();
			inventoryWriteOffRootJson.setBackTopic(MQPushBackOrderEnum.SALE_WRITE_OFF_PUSH_BACK.getTopic());
			inventoryWriteOffRootJson.setBackTags(MQPushBackOrderEnum.SALE_WRITE_OFF_PUSH_BACK.getTags());
			Map<String, Object> customParam = new HashMap<String, Object>();
			customParam.put("orderType", "2");//1-销售出库单 2-上架入库单
			customParam.put("saleOrderId", saleOrderId.toString());
			inventoryWriteOffRootJson.setCustomParam(customParam);
			inventoryWriteOffRootJson.setMerchantId(shelfIdepotModel.getMerchantId().toString());
			inventoryWriteOffRootJson.setMerchantName(shelfIdepotModel.getMerchantName());
			inventoryWriteOffRootJson.setOrderNo(saleShelfIdepotCode);
			inventoryWriteOffRootJson.setSourceOrderNo(originalOrderCode);
			inventoryWriteOffRootJson.setDivergenceDate(TimeUtils.formatFullTime(TimeUtils.parseFullTime(writeOffDate+" 00:00:00")));	// 发货时间

			inventoryWriteOffRootJsonList.add(inventoryWriteOffRootJson);

		}
		for(SaleShelfIdepotModel shelfIdepotModel : shelfIdepotList){
			Long num = saleShelfIdepotDao.save(shelfIdepotModel);
			List<SaleShelfIdepotItemModel> itemList = saleShelfIdepotItemMap.get(shelfIdepotModel.getCode());
			for(SaleShelfIdepotItemModel item : itemList){
				item.setSshelfIdepotId(num);
				saleShelfIdepotItemDao.save(item);
			}
		}

		return inventoryWriteOffRootJsonList;
	}

}
