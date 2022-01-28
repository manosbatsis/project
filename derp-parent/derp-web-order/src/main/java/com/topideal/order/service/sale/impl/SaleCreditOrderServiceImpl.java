package com.topideal.order.service.sale.impl;

import com.topideal.api.finance.FinanceUtils;
import com.topideal.api.finance.f2_01.FinanceApplyItemRequest;
import com.topideal.api.finance.f2_01.FinanceApplyRequest;
import com.topideal.api.finance.f2_02.PayBackRedeemItemRequest;
import com.topideal.api.finance.f2_02.PayBackRedeemRequest;
import com.topideal.api.finance.f2_03.PayBackTrialGoodRequest;
import com.topideal.api.finance.f2_03.PayBackTrialRequest;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.ReceiveBillCostItemDao;
import com.topideal.dao.bill.ReceiveBillDao;
import com.topideal.dao.bill.ReceivePaymentSubjectDao;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.bill.ReceiveBillItemDTO;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.dto.sale.*;
import com.topideal.entity.vo.bill.ReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.bill.ReceiveBillService;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.sale.SaleCreditOrderService;
import com.topideal.order.webapi.bill.form.ReceiveBillSubmitForm;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class SaleCreditOrderServiceImpl implements SaleCreditOrderService{
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(SaleDeclareOrderServiceImpl.class);
	@Autowired
	private SaleCreditOrderDao saleCreditOrderDao ;
	@Autowired
	private SaleCreditOrderItemDao saleCreditOrderItemDao ;
	@Autowired
	private SaleCreditBillOrderDao saleCreditBillOrderDao ;
	@Autowired
	private SaleCreditBillOrderItemDao saleCreditBillOrderItemDao ;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;
	@Autowired
	private SaleOrderDao saleOrderDao ;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private RMQProducer rocketMQProducer;// MQ
    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao;
    @Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private SettlementConfigDao settlementConfigDao;
    @Autowired
    private ReceivePaymentSubjectDao receivePaymentSubjectDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private ReceiveBillService receiveBillService;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
    private BrandParentMongoDao brandParentMongoDao;

	@Override
	public SaleCreditOrderDTO listDTOByPage(SaleCreditOrderDTO dto,User user) throws Exception {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		SaleCreditOrderDTO list = saleCreditOrderDao.listDTOByPage(dto);
		if (list.getList().size() > 0) {
			List<SaleCreditOrderDTO> creditOrderList = list.getList();
			/**
			 * 只有在状态“赊销中”的单据才显示到期倒计天数。前端计算即可，公式=当前日期-到期日期，
				1、若为负数，显示为“剩 XX 天到期”，黑字显示
				2、若为0，显示为“今天到期”，蓝字显示
				3、若为正数，显示为“超期 XX 天” 红字显示
			 */

			for(SaleCreditOrderDTO queryDTO: creditOrderList) {
				if(DERP_ORDER.SALECREDIT_STATUS_004.equals(queryDTO.getStatus())) {
					if(queryDTO.getExpireDate() == null) {
						continue;
					}
					Integer stayDays = TimeUtils.daysBetween(queryDTO.getExpireDate(), TimeUtils.getNow());
					queryDTO.setStayDays(stayDays);
				}
			}

		}
		return dto;
	}

	@Override
	public List<Map<String, Object>> getCreditTypeNum(SaleCreditOrderDTO dto,User user) throws Exception {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return null;
			}
			dto.setBuList(buIds);
		}
		List<Map<String, Object>> mapList = saleCreditOrderDao.getCreditTypeNum(dto) ;

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

	@Override
	public SaleCreditOrderDTO searchDetail(Long id) throws Exception {
		SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
		dto.setId(id);
		return saleCreditOrderDao.searchDetail(dto);
	}

	@Override
	public List<SaleCreditOrderItemDTO> searchItemsByOrderId(Long id) throws Exception {
		SaleCreditOrderItemDTO itemModel = new SaleCreditOrderItemDTO();
		itemModel.setCreditOrderId(id);
		return saleCreditOrderItemDao.listDTO(itemModel);
	}

	@Override
	public List<SaleCreditBillOrderModel> searchBillOrderByOrderId(Long id,String status) throws Exception {
		SaleCreditBillOrderModel model = new SaleCreditBillOrderModel();
		model.setCreditOrderId(id);
		model.setStatus(status);
		return saleCreditBillOrderDao.list(model);
	}

	@Override
	public List<SaleCreditBillOrderItemModel> searchBillItemsByOrderId(Long id) throws Exception {
		SaleCreditBillOrderItemModel itemModel = new SaleCreditBillOrderItemModel();
		itemModel.setCreditBillOrderId(id);
		return saleCreditBillOrderItemDao.list(itemModel);
	}

	/**
	 * 赊销申请
	 */
	@Override
	public void saveCreditOrder(SaleCreditOrderDTO dto, User user) throws Exception{
		SaleOrderModel saleModel = saleOrderDao.searchById(dto.getSaleOrderId());
		if(saleModel == null) {
			throw new RuntimeException("销售订单不存在");
		}

		if(dto.getCreditAmount() == null) {
			throw new RuntimeException("订单金额不能为空");
		}
		if(dto.getCreditAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("订单金额必须大于等于0");
		}
		int indexOf = dto.getCreditAmount().toString().indexOf(".");
		if( indexOf != -1) {
			int count = dto.getCreditAmount().toString().length() - 1 - indexOf;
			if (count > 2) {
				throw new RuntimeException("订单金额最多2位小数");
			}
		}

		if(dto.getPayableMarginAmount() == null) {
			throw new RuntimeException("应收保证金不能为空");
		}
		if(dto.getPayableMarginAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("应收保证金必须大于等于0");
		}
		indexOf = dto.getPayableMarginAmount().toString().indexOf(".");
		if( indexOf != -1) {
			int count = dto.getPayableMarginAmount().toString().length() - 1 - indexOf;
			if (count > 2) {
				throw new RuntimeException("应收保证金最多2位小数");
			}
		}
		if(dto.getOwnMonth() == null) {
			throw new RuntimeException("权责月份不能为空");
		}

		//封装赊销单信息
		SaleCreditOrderModel saleCreditOrderModel = new SaleCreditOrderModel();
		saleCreditOrderModel.setSaleOrderId(saleModel.getId());
		saleCreditOrderModel.setSaleOrderCode(saleModel.getCode());
		saleCreditOrderModel.setCreditAmount(dto.getCreditAmount());
		saleCreditOrderModel.setPayableMarginAmount(dto.getPayableMarginAmount());
		saleCreditOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSRZ));
		saleCreditOrderModel.setIsSyncFinance(DERP_ORDER.SALECREDIT_IS_SYNC_FINANCE_0);
		saleCreditOrderModel.setBuId(saleModel.getBuId());
		saleCreditOrderModel.setBuName(saleModel.getBuName());
		saleCreditOrderModel.setCustomerId(saleModel.getCustomerId());
		saleCreditOrderModel.setCustomerName(saleModel.getCustomerName());
		saleCreditOrderModel.setCurrency(saleModel.getCurrency());
		saleCreditOrderModel.setMerchantId(saleModel.getMerchantId());
		saleCreditOrderModel.setMerchantName(saleModel.getMerchantName());
		saleCreditOrderModel.setPoNo(saleModel.getPoNo());
		saleCreditOrderModel.setTotalNum(saleModel.getTotalNum());
		saleCreditOrderModel.setCreater(user.getId());
		saleCreditOrderModel.setCreateName(user.getName());
		saleCreditOrderModel.setCreateDate(TimeUtils.getNow());
		saleCreditOrderModel.setStatus(DERP_ORDER.SALECREDIT_STATUS_001);
		saleCreditOrderModel.setOwnMonth(TimeUtils.formatMonth(TimeUtils.parse(dto.getOwnMonth(), "yyyy-MM")));
		Long num = saleCreditOrderDao.save(saleCreditOrderModel);
		//保存表体信息
		for(SaleCreditOrderItemDTO itemDTO: dto.getItemList()) {
			SaleCreditOrderItemModel itemModel = new SaleCreditOrderItemModel();
			BeanUtils.copyProperties(itemDTO, itemModel);
			Map<String, Object> merchandiseParam = new HashMap<>();
			merchandiseParam.put("merchandiseId", itemDTO.getGoodsId());
			MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseParam);
			itemModel.setGoodsCode(merchandiseInfo.getGoodsCode());
			itemModel.setBarcode(merchandiseInfo.getBarcode());
			itemModel.setCommbarcode(merchandiseInfo.getCommbarcode());
			itemModel.setCreditOrderId(num);
			itemModel.setCreateDate(TimeUtils.getNow());
			saleCreditOrderItemDao.save(itemModel);
		}
		//记录日志
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, saleModel.getCode(), "赊销申请", null, null);

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_10, saleCreditOrderModel.getCode(), "新增赊销单", null, null);

	}
	/**
	 * 提交赊销单
	 */
	@Override
	public void submitOrder(Long id, User user) throws Exception {
		SaleCreditOrderModel creditModel = saleCreditOrderDao.searchById(id);
		//1、校验状态
		if(!DERP_ORDER.SALECREDIT_STATUS_001.equals(creditModel.getStatus())) {
			throw new RuntimeException("单据状态不为“待提交”");
		}

		SettlementConfigModel settlementConfigModel = new SettlementConfigModel();
		settlementConfigModel.setProjectName("商品销售-采销-B");
		settlementConfigModel =	settlementConfigDao.searchByModel(settlementConfigModel);
        if (settlementConfigModel == null) {
        	throw new RuntimeException("费用项目：商品销售-采销-B不存在");
        }

        ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
        if (paymentSubjectModel == null) {
        	throw new RuntimeException("费用项目：商品销售-采销-B的NC收支费项不存在");
        }
		//2、调用3.12 商品同步接口推送单据的商品信息
		SaleCreditOrderItemModel itemModel = new SaleCreditOrderItemModel();
		itemModel.setCreditOrderId(id);
		List<SaleCreditOrderItemModel> itemList = saleCreditOrderItemDao.list(itemModel);
		for(SaleCreditOrderItemModel item :itemList) {
			JSONObject jsonObject = new JSONObject();// 推送内容
			jsonObject.put("merchantId", creditModel.getMerchantId());
			jsonObject.put("customerId", creditModel.getCustomerId());

			jsonObject.put("goodsId", item.getGoodsId());
			jsonObject.put("goodsNo", item.getGoodsNo());
			jsonObject.put("code", creditModel.getCode());
			try {
				rocketMQProducer.send(jsonObject.toString(), MQErpEnum.SEND_FINANCE_GOODS.getTopic(),MQErpEnum.SEND_FINANCE_GOODS.getTags());

			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("-----------------[销售赊销单号：" + creditModel.getCode() + "，商品货号：" + item.getGoodsNo()
						+ "]向金融推送商品信息失败------------------");
			}

		}
		//3、生成应收单
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Map<String, Object> params = new HashMap<>();
        params.put("customerid", creditModel.getCustomerId());
        params.put("cusType", "1");
        CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(params);

 		String nowMonth = sdf.format(new Date());
		ReceiveBillModel receiveBillModel = new ReceiveBillModel();
		receiveBillModel.setCustomerId(creditModel.getCustomerId());
		receiveBillModel.setCustomerName(creditModel.getCustomerName());
		receiveBillModel.setBuId(creditModel.getBuId());
		receiveBillModel.setBuName(creditModel.getBuName());
		receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_00);
		receiveBillModel.setCurrency(creditModel.getCurrency());
		receiveBillModel.setBillDate(TimeUtils.parse(nowMonth, "yyyy-MM"));
		receiveBillModel.setCreater(user.getName());
		receiveBillModel.setCreaterId(user.getId());
		receiveBillModel.setCreateDate(TimeUtils.getNow());
		receiveBillModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSZD));
		receiveBillModel.setMerchantId(user.getMerchantId());
		receiveBillModel.setMerchantName(user.getMerchantName());
		receiveBillModel.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10);
		receiveBillModel.setSortType("1");//分类： 商销收入
		receiveBillModel.setNcChannelCode(customerInfo.getNcChannelCode());//取客户档案对应的NC渠道
		receiveBillModel.setSettlementType("1");
		receiveBillModel.setRelCode(creditModel.getCode());//赊销单号
		receiveBillModel.setPoNo(creditModel.getPoNo());
		receiveBillModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00);//00-待开票
		receiveBillModel.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10);//10-未同步
		receiveBillModel.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_8);
        Long receiveBillId = receiveBillDao.save(receiveBillModel);

        //封装应收表体信息
		ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
        receiveBillCostItemModel.setProjectId(settlementConfigModel.getId());//默认：采销执行收入（主数据ID：FX000001）
        receiveBillCostItemModel.setProjectName(settlementConfigModel.getProjectName());
        receiveBillCostItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
        receiveBillCostItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
        receiveBillCostItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
        receiveBillCostItemModel.setSubjectName(paymentSubjectModel.getSubName());
        receiveBillCostItemModel.setPoNo(creditModel.getPoNo());
        receiveBillCostItemModel.setNum(0);
        receiveBillCostItemModel.setPrice(creditModel.getPayableMarginAmount());//应收保证金金额
        receiveBillCostItemModel.setBillId(receiveBillId);
        receiveBillCostItemModel.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_0);//补款
        receiveBillCostItemModel.setTax(BigDecimal.ZERO);
        receiveBillCostItemModel.setTaxRate(BigDecimal.ZERO.doubleValue());
        receiveBillCostItemModel.setTaxAmount(creditModel.getPayableMarginAmount());
        receiveBillCostItemDao.save(receiveBillCostItemModel);

        //更新赊销单状态
        creditModel.setStatus(DERP_ORDER.SALECREDIT_STATUS_002);
        creditModel.setModifier(user.getId());
        creditModel.setModifierName(user.getName());
        creditModel.setModifyDate(TimeUtils.getNow());
        saleCreditOrderDao.modify(creditModel);

        //记录应收日志
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_11, receiveBillModel.getCode(), "创建应收单", null, "在提交销售赊销单触发创建");//9-应收账单

        //触发应收单审核
        ReceiveBillSubmitForm form = new ReceiveBillSubmitForm();
        form.setBillId(receiveBillId);
        form.setItemList(new ArrayList<ReceiveBillItemDTO>());
        form.setAdvanceIds("");
        Map<String, String> resultMap = receiveBillService.confirmReceiveBill(form,user);
        String resultCode = resultMap.get("code");
        if("01".equals(resultCode)) {
        	String resultMessage = resultMap.get("message");
        	throw new RuntimeException(resultMessage);
        }

	    commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_10, creditModel.getCode(), "提交", null, null);//8-赊销单

	    //触发消息提醒
 		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
 		mailDTO.setBusinessModuleType("6");
 		mailDTO.setTypeName("销售赊销模块");
 		mailDTO.setType("1");// 1：提交 12：收到保证金 13：放款 14：提交还款 15：收到还款")
 		mailDTO.setMerchantId(creditModel.getMerchantId());
 		mailDTO.setMerchantName(creditModel.getMerchantName());
 		mailDTO.setBuId(creditModel.getBuId());
 		mailDTO.setBuName(creditModel.getBuName());
 		mailDTO.setSupplier(creditModel.getCustomerName());
 		mailDTO.setOrderCode(creditModel.getCode());
 		mailDTO.setPoNum(creditModel.getPoNo());
 		mailDTO.setCurrency(creditModel.getCurrency());
 		mailDTO.setAmount(creditModel.getPayableMarginAmount().toString());
 		mailDTO.setUserName(user.getName());
 		try {
 			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),MQErpEnum.SEND_REMINDER_MAIL.getTags());
 		} catch (Exception e) {
 			LOGGER.error("----------------------赊销单[" + creditModel.getCode() + "]提交操作发送邮件失败----------------------");
 		}
	}

	/**
	 * 收到保证金
	 */
	@Override
	public void updateMarginOrder(Long id,String receiveMarginDate, String actualMarginAmount ,String remark, User user) throws Exception {
		if(StringUtils.isBlank(actualMarginAmount)) {
			throw new RuntimeException("实收保证金不能为空");
		}
		if(new BigDecimal(actualMarginAmount).compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("实收保证金必须大于等于0");
		}
		int indexOf = actualMarginAmount.indexOf(".");
		if( indexOf != -1) {
			int count = actualMarginAmount.length() - 1 - indexOf;
			if (count > 2) {
				throw new RuntimeException("实收保证金最多2位小数");
			}
		}

		if(StringUtils.isBlank(receiveMarginDate)) {
			throw new RuntimeException("收款日期不能为空");
		}
		if(!TimeUtils.isYmdDate(receiveMarginDate)) {
			throw new RuntimeException("收款日期格式有误，格式：yyyy-MM-dd");
		}
		Date nowDate = TimeUtils.parseDay(TimeUtils.formatDay());
		Date receiveMarginD = TimeUtils.parseDay(receiveMarginDate);
		if (receiveMarginD.getTime() > nowDate.getTime()) {
			throw new RuntimeException("收款日期不能大于当前日期");
		}

		SaleCreditOrderModel saleCreditOrderModel = saleCreditOrderDao.searchById(id);
		if(saleCreditOrderModel == null) {
			throw new RuntimeException("赊销单不存在");
		}
		if(!DERP_ORDER.SALECREDIT_STATUS_002.equals(saleCreditOrderModel.getStatus())) {
			throw new RuntimeException("赊销单状态不为“待收保证金”");
		}
		if(new BigDecimal(actualMarginAmount).compareTo(saleCreditOrderModel.getCreditAmount())> 0) {
			throw new RuntimeException("实收保证金不能大于赊销金额");
		}
		//更新状态
		saleCreditOrderModel.setActualMarginAmount(new BigDecimal(actualMarginAmount));
		saleCreditOrderModel.setReceiveMarginDate(TimeUtils.parseDay(receiveMarginDate));
		saleCreditOrderModel.setStatus(DERP_ORDER.SALECREDIT_STATUS_003);
		saleCreditOrderModel.setModifier(user.getId());
		saleCreditOrderModel.setModifierName(user.getName());
		saleCreditOrderModel.setModifyDate(TimeUtils.getNow());
		saleCreditOrderDao.modify(saleCreditOrderModel);

		//记录日志
	    commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_10, saleCreditOrderModel.getCode(), "收保证金", null, remark);//8-赊销单

	    //触发消息提醒
 		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
 		mailDTO.setBusinessModuleType("6");
 		mailDTO.setTypeName("销售赊销模块");
 		mailDTO.setType("12");// 1：提交 12：收到保证金 13：放款 14：提交还款 15：收到还款")
 		mailDTO.setMerchantId(saleCreditOrderModel.getMerchantId());
 		mailDTO.setMerchantName(saleCreditOrderModel.getMerchantName());
 		mailDTO.setBuId(saleCreditOrderModel.getBuId());
 		mailDTO.setBuName(saleCreditOrderModel.getBuName());
 		mailDTO.setSupplier(saleCreditOrderModel.getCustomerName());
 		mailDTO.setOrderCode(saleCreditOrderModel.getCode());
 		mailDTO.setPoNum(saleCreditOrderModel.getPoNo());
 		mailDTO.setCurrency(saleCreditOrderModel.getCurrency());
 		mailDTO.setAmount(saleCreditOrderModel.getActualMarginAmount().toString());
 		mailDTO.setUserName(user.getName());
 		try {
 			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),MQErpEnum.SEND_REMINDER_MAIL.getTags());
 		} catch (Exception e) {
 			LOGGER.error("----------------------赊销单[" + saleCreditOrderModel.getCode() + "]收到保证金操作发送邮件失败----------------------");
 		}
	}
	/**
	 * 确认放款
	 */
	@Override
	public void confirmOrder(Long id,String loanDate, String valueDate ,String remark, User user, String sapienceLoanDate) throws Exception {

		Date nowDate = TimeUtils.parseDay(TimeUtils.formatDay());
		if(StringUtils.isBlank(loanDate)) {
			throw new RuntimeException("放款日期不能为空");
		}
		if(!isYmdDate(loanDate)) {
			throw new RuntimeException("放款日期格式有误，格式：yyyy-MM-dd");
		}
		Date loanD = TimeUtils.parseDay(loanDate);
		if (loanD.getTime() > nowDate.getTime()) {
			throw new RuntimeException("放款日期不能大于当前日期");
		}

		if(StringUtils.isBlank(valueDate)) {
			throw new RuntimeException("起息日期不能为空");
		}
		if(!isYmdDate(valueDate)) {
			throw new RuntimeException("起息日期格式有误，格式：yyyy-MM-dd");
		}
		if(StringUtils.isBlank(sapienceLoanDate)) {
			throw new RuntimeException("卓普信放款日期不能为空");
		}
		if(!isYmdDate(sapienceLoanDate)) {
			throw new RuntimeException("卓普信放款日期格式有误，格式：yyyy-MM-dd");
		}

		SaleCreditOrderModel saleCreditOrderModel = saleCreditOrderDao.searchById(id);
		if(saleCreditOrderModel == null) {
			throw new RuntimeException("赊销单不存在");
		}
		if(!DERP_ORDER.SALECREDIT_STATUS_003.equals(saleCreditOrderModel.getStatus())) {
			throw new RuntimeException("赊销单状态不为“待放款”");
		}

		// 商家信息
		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("customerid", saleCreditOrderModel.getCustomerId());
		CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParams);
		// 封装推融资申请接口 实体
		FinanceApplyRequest applyRequest = new FinanceApplyRequest();
		applyRequest.setApplyNo(saleCreditOrderModel.getCode());// 赊销单号
		applyRequest.setBorrower(customer != null ? customer.getCreditCode() : "");// 根据销售订单中客户编码，查询客户档案的营业执照号
		applyRequest.setSourceCode("10");// 默认
		applyRequest.setTenant("JFX");// 默认
		applyRequest.setCapital(saleCreditOrderModel.getMerchantName());// 取销售订单对应的公司简称
		applyRequest.setAmount(saleCreditOrderModel.getCreditAmount());// 取融资申请弹窗的赊销金额
		applyRequest.setBillCurrency(saleCreditOrderModel.getCurrency());// 取赊销单币种字段的编码
		applyRequest.setApplyTime(TimeUtils.formatFullTime());// 取系统当前时间
		applyRequest.setTotalQty(saleCreditOrderModel.getTotalNum());// 取赊销单的汇总商品数量
		applyRequest.setSupplier("1000000544");// 默认
		applyRequest.setWarehouseId("WMS_360_04");// 默认
		applyRequest.setAddType("10");// 默认
		applyRequest.setInterestCurrency(saleCreditOrderModel.getCurrency());// 取赊销单币种字段的编码
		applyRequest.setProduct("XJDC");// 默认
		applyRequest.setPaymentType("offline");// 默认
		applyRequest.setPaymentStatus("1");// 默认
		applyRequest.setPaymentDate(TimeUtils.format(TimeUtils.parseDay(valueDate), "yyyy-MM-dd"));// 取起息日期
		applyRequest.setActualMargin(saleCreditOrderModel.getActualMarginAmount());// 取销售赊销单的实收保证金字段
		// 封装推融资申请接口 实体商品信息
		Map<String, SaleCreditOrderItemModel> itemsMap = new HashMap<String, SaleCreditOrderItemModel>();
		SaleCreditOrderItemModel itemModel = new SaleCreditOrderItemModel();
		itemModel.setCreditOrderId(saleCreditOrderModel.getId());
		List<SaleCreditOrderItemModel> itemList = saleCreditOrderItemDao.list(itemModel);
		List<FinanceApplyItemRequest> itemApplyRequestList = new ArrayList<>();
		for (SaleCreditOrderItemModel item : itemList) {
			FinanceApplyItemRequest itemApplyRequest = new FinanceApplyItemRequest();
			itemApplyRequest.setGoodsNo(item.getGoodsNo());// 商品货号
			itemApplyRequest.setGoodsName(item.getGoodsName());// 商品名称
			itemApplyRequest.setNum(item.getNum());// 销售数量
			itemApplyRequest.setUnitPrice(item.getPrice());// 单价
			itemApplyRequestList.add(itemApplyRequest);

			itemsMap.put(item.getGoodsNo(), item);
		}
		applyRequest.setCustentrystoreList(itemApplyRequestList);// 商品信息

		JSONObject fromObject = JSONObject.fromObject(applyRequest);
		fromObject.put("method", "financing.apply.info.get");
		String finance = FinanceUtils.getFiance(fromObject);
		if (StringUtils.isBlank(finance)) {
			throw new RuntimeException("融资申请接口返回异常！");
		}
		com.alibaba.fastjson.JSONObject finaceJson = com.alibaba.fastjson.JSONObject.parseObject(finance);;
		Integer status = Integer.valueOf(finaceJson.getString("status"));
		if (status == 2) {
			if (finaceJson.get("notes") != null) {
				throw new RuntimeException(finaceJson.getString("notes"));
			}
			throw new RuntimeException("融资申请接口返回异常");
		}

		String expDate = (String) finaceJson.get("expDate");// 融资到期日
		JSONArray itemArr = JSONArray.fromObject(finaceJson.get("saleList"));
		if (itemArr.size() > 0) {
			for (int i = 0; i < itemArr.size(); i++) {
				JSONObject job = itemArr.getJSONObject(i);
				String goodsNo = (String) job.get("goodsNo");
				BigDecimal principal = new BigDecimal(job.getString("principal"));// 本金
				BigDecimal marginAmount = new BigDecimal(job.getString("margin"));// 保证金
				BigDecimal salePrice = new BigDecimal(job.getString("salePrice"));// 到期单价
				BigDecimal saleAmount = new BigDecimal(job.getString("saleAmount"));// 到期金额

				if (itemsMap.get(goodsNo) != null) {
					SaleCreditOrderItemModel saleCreditOrderItemModel = itemsMap.get(goodsNo);
					saleCreditOrderItemModel.setPrincipal(principal);
					saleCreditOrderItemModel.setMarginAmount(marginAmount);
					saleCreditOrderItemModel.setExpirePrice(salePrice);
					saleCreditOrderItemModel.setExpireAmount(saleAmount);
					saleCreditOrderItemDao.modify(saleCreditOrderItemModel);
				}
			}
		}

		saleCreditOrderModel.setLoanDate(TimeUtils.parseDay(loanDate));
		saleCreditOrderModel.setValueDate(TimeUtils.parseDay(valueDate));
		saleCreditOrderModel.setExpireDate(TimeUtils.parseDay(expDate));
		saleCreditOrderModel.setSapienceLoanDate(TimeUtils.parseDay(sapienceLoanDate));
		saleCreditOrderModel.setStatus(DERP_ORDER.SALECREDIT_STATUS_004);
		saleCreditOrderModel.setIsSyncFinance(DERP_ORDER.SALECREDIT_IS_SYNC_FINANCE_1);
		saleCreditOrderModel.setSyncDate(TimeUtils.getNow());
		saleCreditOrderModel.setModifier(user.getId());
		saleCreditOrderModel.setModifierName(user.getName());
		saleCreditOrderModel.setModifyDate(TimeUtils.getNow());
		saleCreditOrderDao.modify(saleCreditOrderModel);

		//记录日志
	    commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_10, saleCreditOrderModel.getCode(), "放款", null, remark);//8-赊销单

	   //触发消息提醒
 		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
 		mailDTO.setBusinessModuleType("6");
 		mailDTO.setTypeName("销售赊销模块");
 		mailDTO.setType("13");// 1：提交 12：收到保证金 13：放款 14：提交还款 15：收到还款")
 		mailDTO.setMerchantId(saleCreditOrderModel.getMerchantId());
 		mailDTO.setMerchantName(saleCreditOrderModel.getMerchantName());
 		mailDTO.setBuId(saleCreditOrderModel.getBuId());
 		mailDTO.setBuName(saleCreditOrderModel.getBuName());
 		mailDTO.setSupplier(saleCreditOrderModel.getCustomerName());
 		mailDTO.setOrderCode(saleCreditOrderModel.getCode());
 		mailDTO.setPoNum(saleCreditOrderModel.getPoNo());
 		mailDTO.setCurrency(saleCreditOrderModel.getCurrency());
 		mailDTO.setAmount(saleCreditOrderModel.getCreditAmount().toString());
 		mailDTO.setShelvesId(user.getId());
 		mailDTO.setUserName(user.getName());
 		try {
 			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),MQErpEnum.SEND_REMINDER_MAIL.getTags());
 		} catch (Exception e) {
 			LOGGER.error("----------------------赊销单[" + saleCreditOrderModel.getCode() + "]确认放款发送邮件失败----------------------");
 		}

	}

	/**
	 * 访问申请还款页面
	 */
	@Override
	public SaleCreditOrderDTO getRepayDetail(Long id ) throws Exception{
		SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
		dto.setId(id);
		dto = saleCreditOrderDao.searchDetail(dto);

		SaleCreditOrderItemDTO queryItemDTO = new SaleCreditOrderItemDTO();
		queryItemDTO.setCreditOrderId(id);
		List<SaleCreditOrderItemDTO> itemList = saleCreditOrderItemDao.listDTO(queryItemDTO);

		Map<String,Object> paramMap =  new HashMap<String, Object>();
		for(SaleCreditOrderItemDTO itemDTO : itemList) {
			paramMap.put("goodsId", itemDTO.getGoodsId());
			paramMap.put("creditOrderId", id);
			Integer totalRedempt = saleCreditBillOrderItemDao.getRedempNum(paramMap);//已赎回数量
			itemDTO.setStayRedempNum(itemDTO.getNum() - (totalRedempt == null ? 0 : totalRedempt));//待赎回数量

			BigDecimal stayRedempAmount = itemDTO.getPrice().multiply(new BigDecimal(itemDTO.getStayRedempNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
			itemDTO.setStayRedempAmount(stayRedempAmount);//待赎回金额
		}
		dto.setItemList(itemList);
		SaleCreditBillOrderModel model = new SaleCreditBillOrderModel();
		model.setCreditOrderId(id);
		List<SaleCreditBillOrderModel> billOrderList = saleCreditBillOrderDao.list(model);
		if(billOrderList != null && billOrderList.size() > 0){
			dto.setIsExistBillOrder("1");
		}else{
			dto.setIsExistBillOrder("0");
		}

		return dto;
	}
	/**
	 * 还款试算
	 */
	@Override
	public SaleCreditBillOrderDTO calRepayAmount(SaleCreditOrderDTO dto, User user, String receiveDate, BigDecimal discountDelayAmount) throws Exception {
		// 1、检查订单金额、起算日期、实际还款日期是否为空，若为空，提示错误“XXX不能为空”
		SaleCreditOrderModel creditModel= saleCreditOrderDao.searchById(dto.getId());
		if (creditModel == null) {
			throw new RuntimeException("赊销单不存在!");
		}
		if (dto.getCreditAmount() == null) {
			throw new RuntimeException("订单金额不能为空!");
		}
		if (dto.getValueDate() == null) {
			throw new RuntimeException("起算日期不能为空!");
		}
		if (StringUtils.isBlank(receiveDate)) {
			throw new RuntimeException("还款日期不能为空!");
		}

		int nullItemNum = 0;
		List<SaleCreditOrderItemDTO> itemList = dto.getItemList();
		if(itemList == null || itemList.size() < 1) {
			throw new RuntimeException("赊销单商品信息为空!");
		}
		for (SaleCreditOrderItemDTO itemDTO : itemList) {
			if(itemDTO.getRedempNum() == null || itemDTO.getRedempNum() < 1) {
				nullItemNum++;
				continue;
			}
			if(itemDTO.getRedempNum() > itemDTO.getStayRedempNum()) {
				throw new RuntimeException("赎回数量不能大于可赎回数量!");
			}
		}
		if(nullItemNum > 0 && itemList.size() == nullItemNum) {
			throw new RuntimeException("请输入本次要赎回的商品数量!");
		}
		SaleCreditBillOrderDTO billDTO = getReplayAmount(dto , receiveDate,discountDelayAmount);
		return billDTO;
	}
	/**
	 * 申请还款
	 */
	@Override
	public SaleCreditBillOrderDTO applyRefund(SaleCreditOrderDTO dto, User user, String receiveDate,
											  BigDecimal discountDelayAmount, String discountReason) throws Exception {
		SaleCreditBillOrderDTO saleCreditBillOrderDTO = queryFinance(dto, "10" ,receiveDate,discountDelayAmount,discountReason);
		return saleCreditBillOrderDTO;
	}
	/**
	 * 提交还款
	 */
	@Override
	public void saveCreditBill(SaleCreditOrderDTO dto, User user, String receiveDate,
							   BigDecimal discountDelayAmount, String discountReason) throws Exception {
		//1、调用3.17还款赎回接口
		SaleCreditBillOrderDTO saleCreditBillOrderDTO = queryFinance(dto, "30" ,receiveDate,discountDelayAmount,discountReason);
		SaleCreditOrderModel creditModel= saleCreditOrderDao.searchById(dto.getId());

		//查询费用项目  应收单有用
		SettlementConfigModel settlementConfigModel = new SettlementConfigModel();
		settlementConfigModel.setProjectName("商品销售-采销-B");
		settlementConfigModel =	settlementConfigDao.searchByModel(settlementConfigModel);
		if (settlementConfigModel == null) {
			throw new RuntimeException("费用项目：商品销售-采销-B不存在");
		}

		ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
		if (paymentSubjectModel == null) {
			throw new RuntimeException("费用项目：商品销售-采销-B的NC收支费项不存在");
		}
		discountDelayAmount = discountDelayAmount == null ? BigDecimal.ZERO:discountDelayAmount;
		//2、生成赊销收款单
		SaleCreditBillOrderModel saleCreditBillOrderModel = new SaleCreditBillOrderModel();
		saleCreditBillOrderModel.setCode(saleCreditBillOrderDTO.getCode());
		saleCreditBillOrderModel.setCreditOrderId(creditModel.getId());
		saleCreditBillOrderModel.setCreditOrderCode(creditModel.getCode());
		saleCreditBillOrderModel.setBuId(creditModel.getBuId());
		saleCreditBillOrderModel.setBuName(creditModel.getBuName());
		saleCreditBillOrderModel.setMerchantId(creditModel.getMerchantId());
		saleCreditBillOrderModel.setMerchantName(creditModel.getMerchantName());
		saleCreditBillOrderModel.setCustomerId(creditModel.getCustomerId());
		saleCreditBillOrderModel.setCustomerName(creditModel.getCustomerName());
		saleCreditBillOrderModel.setCurrency(creditModel.getCurrency());
		saleCreditBillOrderModel.setPoNo(creditModel.getPoNo());
		saleCreditBillOrderModel.setPrincipalAmount(saleCreditBillOrderDTO.getPrincipalAmount());
		saleCreditBillOrderModel.setOccupationAmount(saleCreditBillOrderDTO.getOccupationAmount());
		saleCreditBillOrderModel.setAgencyAmount(saleCreditBillOrderDTO.getAgencyAmount());
		saleCreditBillOrderModel.setDelayAmount(saleCreditBillOrderDTO.getDelayAmount());
		saleCreditBillOrderModel.setReceivableAmount(saleCreditBillOrderDTO.getReceivableAmount());
		saleCreditBillOrderModel.setMarginAmount(saleCreditBillOrderDTO.getMarginAmount());
		saleCreditBillOrderModel.setReceiveDate(TimeUtils.parseDay(receiveDate));
		saleCreditBillOrderModel.setStatus(DERP_ORDER.SALECREDITBILL_STATUS_001);
		saleCreditBillOrderModel.setCreater(user.getId());
		saleCreditBillOrderModel.setCreateName(user.getName());
		saleCreditBillOrderModel.setCreateDate(TimeUtils.getNow());
		saleCreditBillOrderModel.setDiscountDelayAmount(discountDelayAmount);
		saleCreditBillOrderModel.setDiscountReason(discountReason);

		List<SaleCreditBillOrderItemModel> itemList = new ArrayList<SaleCreditBillOrderItemModel>();
		List<ReceiveBillCostItemModel> receiveBillItemList = new ArrayList<ReceiveBillCostItemModel>();
		if (saleCreditBillOrderDTO.getItemList() != null && saleCreditBillOrderDTO.getItemList().size() > 0) {
			for (SaleCreditBillOrderItemDTO itemDto : saleCreditBillOrderDTO.getItemList()) {
				SaleCreditOrderItemModel itemModel = new SaleCreditOrderItemModel();
				itemModel.setCreditOrderId(creditModel.getId());
				itemModel.setGoodsId(itemDto.getGoodsId());
				itemModel = saleCreditOrderItemDao.searchByModel(itemModel);
				//封装赊销收款单表体信息
				SaleCreditBillOrderItemModel billOrderItemModel = new SaleCreditBillOrderItemModel();
				billOrderItemModel.setGoodsId(itemModel.getGoodsId());
				billOrderItemModel.setGoodsNo(itemModel.getGoodsNo());
				billOrderItemModel.setGoodsCode(itemModel.getGoodsCode());
				billOrderItemModel.setGoodsName(itemModel.getGoodsName());
				billOrderItemModel.setCommbarcode(itemModel.getCommbarcode());
				billOrderItemModel.setBarcode(itemModel.getBarcode());
				billOrderItemModel.setPrincipalAmount(itemDto.getPrincipalAmount());
				billOrderItemModel.setOccupationAmount(itemDto.getOccupationAmount());
				billOrderItemModel.setAgencyAmount(itemDto.getAgencyAmount());
				billOrderItemModel.setDelayAmount(itemDto.getDelayAmount());
				billOrderItemModel.setReceivableAmount(itemDto.getReceivableAmount());
				billOrderItemModel.setMarginAmount(itemDto.getMarginAmount());
				billOrderItemModel.setNum(itemDto.getNum());
				billOrderItemModel.setCreateDate(TimeUtils.getNow());
				billOrderItemModel.setDiscountDelayAmount(itemDto.getDiscountDelayAmount() == null ? BigDecimal.ZERO:itemDto.getDiscountDelayAmount());
				itemList.add(billOrderItemModel);

				//封装应收单表体信息
				ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
		        receiveBillCostItemModel.setProjectId(settlementConfigModel.getId());//默认：采销执行收入（主数据ID：FX000001）
		        receiveBillCostItemModel.setProjectName(settlementConfigModel.getProjectName());
		        receiveBillCostItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
		        receiveBillCostItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
		        receiveBillCostItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
		        receiveBillCostItemModel.setSubjectName(paymentSubjectModel.getSubName());
		        receiveBillCostItemModel.setPoNo(creditModel.getPoNo());
		        receiveBillCostItemModel.setNum(itemDto.getNum());
		        receiveBillCostItemModel.setPrice(itemDto.getReceivableAmount());//应收款金额
		        receiveBillCostItemModel.setGoodsId(itemModel.getGoodsId());
		        receiveBillCostItemModel.setGoodsNo(itemModel.getGoodsNo());
		        receiveBillCostItemModel.setGoodsName(itemModel.getGoodsName());
		        receiveBillCostItemModel.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_0);//补款
		        receiveBillCostItemModel.setTax(BigDecimal.ZERO);
		        receiveBillCostItemModel.setTaxRate(BigDecimal.ZERO.doubleValue());
		        receiveBillCostItemModel.setTaxAmount(itemDto.getReceivableAmount());
		        BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(itemDto.getGoodsId());
                if (brandParent != null) {
                    receiveBillCostItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                    receiveBillCostItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                    receiveBillCostItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                }
		        receiveBillItemList.add(receiveBillCostItemModel);
			}
		}

		Long num = saleCreditBillOrderDao.save(saleCreditBillOrderModel);
		for (SaleCreditBillOrderItemModel item : itemList) {
			item.setCreditBillOrderId(num);
			saleCreditBillOrderItemDao.save(item);
		}

		//3、修改赊销单 收款本金金额、还款利息和状态
		BigDecimal receivePrincipalAmount = creditModel.getReceivePrincipalAmount() == null ? saleCreditBillOrderModel.getPrincipalAmount():
		creditModel.getReceivePrincipalAmount().add(saleCreditBillOrderModel.getPrincipalAmount());
		//利息= 资金占用费+代理费+滞纳金 - 滞纳减免金
		BigDecimal interestAmount = saleCreditBillOrderModel.getOccupationAmount().add(saleCreditBillOrderModel.getAgencyAmount())
				.add(saleCreditBillOrderModel.getDelayAmount()).subtract(discountDelayAmount);
		BigDecimal receiveInterestAmount = creditModel.getReceiveInterestAmount() == null ? interestAmount: creditModel.getReceiveInterestAmount().add(interestAmount);

		creditModel.setReceivePrincipalAmount(receivePrincipalAmount);
		creditModel.setReceiveInterestAmount(receiveInterestAmount);
		creditModel.setStatus(DERP_ORDER.SALECREDIT_STATUS_005);
		creditModel.setModifier(user.getId());
		creditModel.setModifierName(user.getName());
		creditModel.setModifyDate(TimeUtils.getNow());
		creditModel.setOwnMonth(TimeUtils.formatMonth(TimeUtils.parse(dto.getOwnMonth(), "yyyy-MM")));
		saleCreditOrderDao.modify(creditModel);

		//4、生成TOB应收单
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Map<String, Object> params = new HashMap<>();
        params.put("customerid", creditModel.getCustomerId());
        params.put("cusType", "1");
        CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(params);

 		String nowMonth = sdf.format(new Date());
		ReceiveBillModel receiveBillModel = new ReceiveBillModel();
		receiveBillModel.setCustomerId(creditModel.getCustomerId());
		receiveBillModel.setCustomerName(creditModel.getCustomerName());
		receiveBillModel.setBuId(creditModel.getBuId());
		receiveBillModel.setBuName(creditModel.getBuName());
		receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_00);
		receiveBillModel.setCurrency(creditModel.getCurrency());
		receiveBillModel.setBillDate(TimeUtils.parse(nowMonth, "yyyy-MM"));
		receiveBillModel.setCreater(user.getName());
		receiveBillModel.setCreaterId(user.getId());
		receiveBillModel.setCreateDate(TimeUtils.getNow());
		receiveBillModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSZD));
		receiveBillModel.setMerchantId(user.getMerchantId());
		receiveBillModel.setMerchantName(user.getMerchantName());
		receiveBillModel.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10);
		receiveBillModel.setSortType("1");//分类： 商销收入
		receiveBillModel.setNcChannelCode(customerInfo.getNcChannelCode());//取客户档案对应的NC渠道
		receiveBillModel.setSettlementType("1");
		receiveBillModel.setRelCode(saleCreditBillOrderModel.getCode());//赊销账单号
		receiveBillModel.setPoNo(creditModel.getPoNo());
		receiveBillModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00);//00-待开票
		receiveBillModel.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10);//10-未同步
		receiveBillModel.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_9);
        Long receiveBillId = receiveBillDao.save(receiveBillModel);

        //保存应收表体信息
        for (ReceiveBillCostItemModel receiveBillCostItemModel : receiveBillItemList) {
        	receiveBillCostItemModel.setBillId(receiveBillId);
			receiveBillCostItemDao.save(receiveBillCostItemModel);
		}
        //记录日志
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_11, receiveBillModel.getCode(), "创建应收单", null, "在销售赊销单提交还款触发创建");//9-应收账单
        //触发应收单审核
        ReceiveBillSubmitForm form = new ReceiveBillSubmitForm();
        form.setBillId(receiveBillId);
        form.setItemList(new ArrayList<ReceiveBillItemDTO>());
        form.setAdvanceIds("");
        Map<String, String> resultMap = receiveBillService.confirmReceiveBill(form,user);
        String resultCode = resultMap.get("code");
        if("01".equals(resultCode)) {
        	String resultMessage = resultMap.get("message");
        	throw new RuntimeException(resultMessage);
        }

        //记录日志
	    commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_10, creditModel.getCode(), "提交还款", null, null);//8-赊销单

	    //触发消息提醒
 		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
 		mailDTO.setBusinessModuleType("6");
 		mailDTO.setTypeName("销售赊销模块");
 		mailDTO.setType("14");// 1：提交 12：收到保证金 13：放款 14：提交还款 15：收到还款")
 		mailDTO.setMerchantId(creditModel.getMerchantId());
 		mailDTO.setMerchantName(creditModel.getMerchantName());
 		mailDTO.setBuId(creditModel.getBuId());
 		mailDTO.setBuName(creditModel.getBuName());
 		mailDTO.setSupplier(creditModel.getCustomerName());
 		mailDTO.setOrderCode(creditModel.getCode());
 		mailDTO.setPoNum(creditModel.getPoNo());
 		mailDTO.setCurrency(creditModel.getCurrency());
 		mailDTO.setAmount(saleCreditBillOrderModel.getReceivableAmount().toString());
 		mailDTO.setUserName(user.getName());
 		try {
 			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),MQErpEnum.SEND_REMINDER_MAIL.getTags());
 		} catch (Exception e) {
 			LOGGER.error("----------------------赊销单[" + creditModel.getCode() + "]提交还款发送邮件失败----------------------");
 		}
	}

	private SaleCreditBillOrderDTO queryFinance(SaleCreditOrderDTO dto, String operate, String receiveDate,
												BigDecimal discountDelayAmount, String discountReason) throws Exception {
		// 1、检查订单金额、起算日期、实际还款日期是否为空，若为空，提示错误“XXX不能为空”
		Map<String, SaleCreditOrderItemDTO> itemsMap = new HashMap<String, SaleCreditOrderItemDTO>();
		SaleCreditOrderModel creditModel= saleCreditOrderDao.searchById(dto.getId());
		if (creditModel == null) {
			throw new RuntimeException("赊销单不存在!");
		}
		if (dto.getCreditAmount() == null) {
			throw new RuntimeException("订单金额不能为空!");
		}
		if (dto.getValueDate() == null) {
			throw new RuntimeException("起算日期不能为空!");
		}
		if (StringUtils.isBlank(receiveDate)) {
			throw new RuntimeException("还款日期不能为空!");
		}
		if(discountDelayAmount != null && discountDelayAmount.compareTo(BigDecimal.ZERO) > 0 && StringUtils.isBlank(discountReason)){
			throw new RuntimeException("滞纳金减免金额不为空，减免原因不能为空!");
		}

		int nullItemNum = 0;
		List<SaleCreditOrderItemDTO> itemList = dto.getItemList();
		if(itemList == null || itemList.size() < 1) {
			throw new RuntimeException("赊销单商品信息为空!");
		}
		for (SaleCreditOrderItemDTO itemDTO : itemList) {
			if(itemDTO.getRedempNum() == null || itemDTO.getRedempNum() < 1) {
				nullItemNum++;
				continue;
			}
			if(itemDTO.getRedempNum() > itemDTO.getStayRedempNum()) {
				throw new RuntimeException("赎回数量不能大于可赎回数量!");
			}
		}
		if(nullItemNum > 0 && itemList.size() == nullItemNum) {
			throw new RuntimeException("请输入本次要赎回的商品数量!");
		}

		// 2、校验通过后，调用3.7企业还款赎回接收接口，status传：申请赎回 传10，确认赎回 传30，其他传入字段见接口说明。
		// 商家信息
		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("customerid", creditModel.getCustomerId());
		CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParams);

		PayBackRedeemRequest payBack = new PayBackRedeemRequest();
		//4.1、检查该销售赊销单是否存在赎回单，若不存在，赊销还款单号=赊销单号-1,（1为流水号，代表第一张还款单）
		//4.2、若存在，则找到最大的流水号，本次赊销还款回单号=最大赊销还款单号+1,（即增加一个序号）
		String applyNo = creditModel.getCode() +"-1";
		SaleCreditBillOrderModel queryBillModel = new SaleCreditBillOrderModel();
		queryBillModel.setCreditOrderId(creditModel.getId());
		List<SaleCreditBillOrderModel> queryList = saleCreditBillOrderDao.list(queryBillModel);
		if(queryList != null && queryList.size() > 0) {
			int index = queryList.size()+1;
			applyNo = creditModel.getCode() + "-" + index;
		}
		payBack.setApplyNo(applyNo);
		payBack.setApplyDate(TimeUtils.formatDay());// 取系统当前时间
		payBack.setRefundDate(receiveDate);// 实际还款日期字段
		payBack.setBorrower(customer != null ? customer.getCreditCode() : "");// 根据销售订单中客户编码，查询客户档案的营业执照号
		payBack.setInterestCurrency(creditModel.getCurrency());// 取销售订单币种字段的编码
		payBack.setRepaymentType("offline");
		payBack.setActualDate(receiveDate);// 实际还款日期字段
		payBack.setOnCredit("0");
		payBack.setStatus(operate);// 申请赎回 传10，确认赎回 传30 20-取消

		List<PayBackRedeemItemRequest> goodList = new ArrayList<PayBackRedeemItemRequest>();
		for (SaleCreditOrderItemDTO itemDTO : itemList) {
			if(itemDTO.getRedempNum() < 1) {
				continue;
			}
			PayBackRedeemItemRequest itemRequest = new PayBackRedeemItemRequest();
			itemRequest.setGoodsName(itemDTO.getGoodsName());
			itemRequest.setGoodsNo(itemDTO.getGoodsNo());
			itemRequest.setQty(itemDTO.getRedempNum());
			itemRequest.setProcurementNo(creditModel.getCode());
			goodList.add(itemRequest);
			itemsMap.put(itemDTO.getGoodsNo(), itemDTO);
		}
		payBack.setGoodList(goodList);
		if("10".equals(operate)){//申请还款，先取消订单，再重新申请，以便重复提交
			PayBackRedeemRequest cancelPayBack = new PayBackRedeemRequest();
			BeanUtils.copyProperties(payBack, cancelPayBack);
			cancelPayBack.setStatus("20");//20-取消
			JSONObject cancelFromObject = JSONObject.fromObject(cancelPayBack);
			cancelFromObject.put("method", "repayment.apply.info.get");
			String cancelFinance = FinanceUtils.getFiance(cancelFromObject);

			// 若失败，提示错误原因
			if (StringUtils.isBlank(cancelFinance)) {
				throw new RuntimeException("取消融资接口返回异常！");
			}
//			com.alibaba.fastjson.JSONObject cancelFinaceJson = com.alibaba.fastjson.JSONObject.parseObject(cancelFinance);
//			Integer status = Integer.valueOf(cancelFinaceJson.getString("status"));
//			if (status == 2) {
//				if (cancelFinaceJson.get("notes") != null) {
//					throw new RuntimeException(cancelFinaceJson.getString("notes"));
//				}
//				throw new RuntimeException("取消融资接口返回异常");
//			}
		}

		JSONObject fromObject = JSONObject.fromObject(payBack);
		fromObject.put("method", "repayment.apply.info.get");
		String finance = FinanceUtils.getFiance(fromObject);

		// 3、若失败，提示错误原因
		if (StringUtils.isBlank(finance)) {
			throw new RuntimeException("融资赎回接口返回异常！");
		}
		com.alibaba.fastjson.JSONObject finaceJson = com.alibaba.fastjson.JSONObject.parseObject(finance);
		Integer status = Integer.valueOf(finaceJson.getString("status"));
		if (status == 2) {
			if (finaceJson.get("notes") != null) {
				throw new RuntimeException(finaceJson.getString("notes"));
			}
			throw new RuntimeException("融资赎回接口返回异常");
		}
		BigDecimal totalPrincipal = new BigDecimal(finaceJson.getString("totalPrincipal"));// 应还本金
		BigDecimal totalOccupation = new BigDecimal(finaceJson.getString("totalOccupation"));// 应还利息 资金占用费
		BigDecimal totalAgencyFee = new BigDecimal(finaceJson.getString("totalAgentFee"));// 代理费
		BigDecimal totalDelayFee = new BigDecimal(finaceJson.getString("totalDelayFee"));// 滞纳金
		BigDecimal totalAmount = new BigDecimal(finaceJson.getString("totalAmount"));// 应还款金额
		BigDecimal totalMargin = new BigDecimal(finaceJson.getString("totalMargin"));// 保证金

		if(discountDelayAmount != null && totalDelayFee.compareTo(BigDecimal.ZERO) <= 0 && discountDelayAmount.compareTo(BigDecimal.ZERO) > 0){
			throw new RuntimeException("滞纳金为0，滞纳减免金不能大于0");
		}

		List<SaleCreditBillOrderItemDTO> itemDTOList = new ArrayList<SaleCreditBillOrderItemDTO>();
		if (finaceJson.containsKey("goodList") && finaceJson.get("goodList") != null && finaceJson.get("goodList") != "") {
			JSONArray itemArr = JSONArray.fromObject(finaceJson.get("goodList"));
			if (itemArr.size() > 0) {
				for (int i = 0; i < itemArr.size(); i++) {
					JSONObject job = itemArr.getJSONObject(i);
					String goodsNo = (String) job.get("goodId");
					BigDecimal principal = new BigDecimal(job.getString("principal"));// 应还本金
					BigDecimal occupation = new BigDecimal(job.getString("occupationFee"));// 应还利息 资金占用费
					BigDecimal agencyFee = new BigDecimal(job.getString("agencyFee"));// 代理费
					BigDecimal delayFee = new BigDecimal(job.getString("delayFee"));// 滞纳金
					BigDecimal amout = new BigDecimal(job.getString("amount"));// 应还款金额
					BigDecimal margin = new BigDecimal(job.getString("margin"));// 保证金
					if (itemsMap.get(goodsNo) != null) {
						SaleCreditOrderItemDTO creditItemDTO = itemsMap.get(goodsNo);

						SaleCreditBillOrderItemDTO billOrderItemDTO = new SaleCreditBillOrderItemDTO();
						billOrderItemDTO.setGoodsId(creditItemDTO.getGoodsId());
						billOrderItemDTO.setGoodsNo(goodsNo);
						billOrderItemDTO.setGoodsName(creditItemDTO.getGoodsName());
						billOrderItemDTO.setGoodsCode(creditItemDTO.getGoodsCode());
						billOrderItemDTO.setBarcode(creditItemDTO.getBarcode());
						billOrderItemDTO.setCommbarcode(creditItemDTO.getCommbarcode());
						billOrderItemDTO.setPrincipalAmount(principal);
						billOrderItemDTO.setOccupationAmount(occupation);
						billOrderItemDTO.setAgencyAmount(agencyFee);
						billOrderItemDTO.setDelayAmount(delayFee);
						billOrderItemDTO.setReceivableAmount(amout);
						billOrderItemDTO.setMarginAmount(margin);
						billOrderItemDTO.setNum(creditItemDTO.getRedempNum());//赎回数量
						billOrderItemDTO.setDiscountDelayAmount(BigDecimal.ZERO);

						itemDTOList.add(billOrderItemDTO);
					}
				}
			}
		}
		if(discountDelayAmount != null && discountDelayAmount.compareTo(BigDecimal.ZERO) > 0){
			/**
			 * 1.前N-1行：则表体的滞纳金减免金额=(表体对应商品接口回传的滞纳金/表头接口回传的滞纳金)*表头的滞纳金减免金额
			 * 2.第N行：表头的滞纳金减免金额 - 前N-1行表体的滞纳金减免金额之和。
			 */
			BigDecimal alreadyCalAmount = BigDecimal.ZERO;
			for(int i =0; i< itemDTOList.size(); i++){
				SaleCreditBillOrderItemDTO billOrderItemDTO = itemDTOList.get(i);
				BigDecimal receiveAmount = billOrderItemDTO.getReceivableAmount();
				BigDecimal discountItemAmount = BigDecimal.ZERO;
				if(i == itemDTOList.size()-1){
					discountItemAmount = discountDelayAmount.subtract(alreadyCalAmount).setScale(2,BigDecimal.ROUND_HALF_UP);
				}else{
					discountItemAmount =  billOrderItemDTO.getDelayAmount().divide(totalDelayFee, 2 ,BigDecimal.ROUND_HALF_UP)
							.multiply(discountDelayAmount).setScale(2,BigDecimal.ROUND_HALF_UP);
					alreadyCalAmount = alreadyCalAmount.add(discountItemAmount);
				}
				billOrderItemDTO.setDiscountDelayAmount(discountItemAmount);
				billOrderItemDTO.setReceivableAmount(receiveAmount.subtract(discountItemAmount).setScale(2,BigDecimal.ROUND_HALF_UP));

			}
			totalAmount = totalAmount.subtract(discountDelayAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		SaleCreditBillOrderDTO billDTO = new SaleCreditBillOrderDTO();
		billDTO.setPrincipalAmount(totalPrincipal);
		billDTO.setOccupationAmount(totalOccupation);
		billDTO.setAgencyAmount(totalAgencyFee);
		billDTO.setDelayAmount(totalDelayFee);
		billDTO.setReceivableAmount(totalAmount);
		billDTO.setMarginAmount(totalMargin);
		billDTO.setCode(applyNo);
		billDTO.setItemList(itemDTOList);
		billDTO.setDiscountDelayAmount(discountDelayAmount);
		billDTO.setDiscountReason(discountReason);
		return billDTO;
	}
	/**
	 * 收到还款
	 */
	@Override
	public void confirmCreditBill(Long billId , String accountDate ,String remark , User user) throws Exception {
		if(billId == null) {
			throw new RuntimeException("请选择收款单号");
		}
		if (StringUtils.isBlank(accountDate)) {
			throw new RuntimeException("到账日期不能为空");
		}
		SaleCreditBillOrderModel billModel = saleCreditBillOrderDao.searchById(billId);
		if(billModel == null) {
			throw new RuntimeException("赊销收款单不存在");
		}
		SaleCreditOrderModel creditModel = saleCreditOrderDao.searchById(billModel.getCreditOrderId());
		if(DERP_ORDER.SALECREDIT_STATUS_004.equals(creditModel.getStatus()) && DERP_ORDER.SALECREDIT_STATUS_005.equals(creditModel.getStatus())) {
			throw new RuntimeException("赊销单状态不为“赊销中”或“待收款”");
		}
		//更新赊销收款单状态
		billModel.setStatus(DERP_ORDER.SALECREDITBILL_STATUS_002);
		billModel.setReceiveDate(TimeUtils.parseDay(accountDate));
		billModel.setModifier(user.getId());
		billModel.setModifierName(user.getName());
		billModel.setModifyDate(TimeUtils.getNow());
		saleCreditBillOrderDao.modify(billModel);

		/**
		 * 赊销单状态：
		 * 		1、检查该赊销单是否存在待收款的收款单，若存在，更新赊销单的状态为“待收款”
		 * 		2、若不存在待收款的收款单，检查赊销单的还款本金+实收保证金是否等于订单金额，若相等，更新赊销单状态为“已收款”，否则更新赊销单的状态为“赊销中”
		 */
		String creditStatus = "";
		SaleCreditBillOrderModel queryBillModel = new SaleCreditBillOrderModel();
		queryBillModel.setCreditOrderId(creditModel.getId());
		queryBillModel.setStatus(DERP_ORDER.SALECREDITBILL_STATUS_001);
		List<SaleCreditBillOrderModel> billList = saleCreditBillOrderDao.list(queryBillModel);
		if(billList != null && billList.size() > 0) {
			creditStatus = DERP_ORDER.SALECREDIT_STATUS_005;
		}else {
			BigDecimal receiveAmount = creditModel.getReceivePrincipalAmount().add(creditModel.getActualMarginAmount());//还款本金+实收保证金
			if(receiveAmount.compareTo(creditModel.getCreditAmount()) == 0) {
				creditStatus = DERP_ORDER.SALECREDIT_STATUS_007;
			}else {
				creditStatus = DERP_ORDER.SALECREDIT_STATUS_004;
			}
		}
		creditModel.setStatus(creditStatus);
		creditModel.setReceiveDate(TimeUtils.parseDay(accountDate));
		creditModel.setModifier(user.getId());
		creditModel.setModifierName(user.getName());
		creditModel.setModifyDate(TimeUtils.getNow());
		saleCreditOrderDao.modify(creditModel);

		//记录日志
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_10, creditModel.getCode(), "收到还款", null, remark);//8-赊销单

		//触发消息提醒
 		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
 		mailDTO.setBusinessModuleType("6");
 		mailDTO.setTypeName("销售赊销模块");
 		mailDTO.setType("15");// 1：提交 12：收到保证金 13：放款 14：提交还款 15：收到还款")
 		mailDTO.setMerchantId(creditModel.getMerchantId());
 		mailDTO.setMerchantName(creditModel.getMerchantName());
 		mailDTO.setBuId(creditModel.getBuId());
 		mailDTO.setBuName(creditModel.getBuName());
 		mailDTO.setSupplier(creditModel.getCustomerName());
 		mailDTO.setOrderCode(creditModel.getCode());
 		mailDTO.setPoNum(creditModel.getPoNo());
 		mailDTO.setCurrency(creditModel.getCurrency());
 		mailDTO.setAmount(billModel.getReceivableAmount().toString());
 		mailDTO.setUserName(user.getName());
 		try {
 			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),MQErpEnum.SEND_REMINDER_MAIL.getTags());
 		} catch (Exception e) {
 			LOGGER.error("----------------------赊销单[" + creditModel.getCode() + "]收到还款发送邮件失败----------------------");
 		}

	}
	/**
	 * 导出结算单
	 * @param id
	 * @throws Exception
	 */
	@Override
	public SaleCreditOrderExportDTO exportCreditOrder(Long id) throws Exception {
		if(id == null) {
			throw new RuntimeException("请选择赊销单");
		}
		List<SaleCreditBillOrderDTO> billDTOList = new ArrayList<SaleCreditBillOrderDTO>();
		SaleCreditOrderModel creditModel = saleCreditOrderDao.searchById(id);
		//赊销账单
		SaleCreditBillOrderModel queryBillModel = new SaleCreditBillOrderModel();
		queryBillModel.setCreditOrderId(id);
		List<SaleCreditBillOrderModel> billList = saleCreditBillOrderDao.list(queryBillModel);

		// 根据商家id获取商家信息
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", creditModel.getMerchantId());
		MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchantParams);

		SaleCreditOrderExportDTO exportDTO = new SaleCreditOrderExportDTO();
		exportDTO.setCode(creditModel.getCode());
		exportDTO.setMerchantName(merchant.getEnglishName());
		exportDTO.setCreditAmount(creditModel.getCreditAmount()== null ? BigDecimal.ZERO :creditModel.getCreditAmount());
		exportDTO.setValueDate(creditModel.getValueDate());
		exportDTO.setExpireDate(creditModel.getExpireDate());
		exportDTO.setCurrency(creditModel.getCurrency());
		exportDTO.setActualMarginAmount(creditModel.getActualMarginAmount() == null ? BigDecimal.ZERO : creditModel.getActualMarginAmount());
		exportDTO.setReceivePrincipalAmount(creditModel.getReceivePrincipalAmount() == null ? BigDecimal.ZERO : creditModel.getReceivePrincipalAmount());
		exportDTO.setReceiveDate(creditModel.getReceiveDate());
		exportDTO.setReceiveInterestAmount(creditModel.getReceiveInterestAmount() == null ? BigDecimal.ZERO : creditModel.getReceiveInterestAmount());
		exportDTO.setBankAccount(merchant.getBankAccount());
		exportDTO.setBeneficiaryName(merchant.getBeneficiaryName());
		exportDTO.setBankAddress(merchant.getBankAddress());
		exportDTO.setDepositBank(merchant.getDepositBank());
		exportDTO.setSwiftCode(merchant.getSwiftCode());
		exportDTO.setPoNo(creditModel.getPoNo());

		for(SaleCreditBillOrderModel billModel : billList) {
			SaleCreditBillOrderDTO billDTO = new SaleCreditBillOrderDTO();
			BeanUtils.copyProperties(billModel, billDTO);
			billDTO.setInterestDay(TimeUtils.daysBetween(creditModel.getValueDate(),billModel.getReceiveDate()) +1);//计息天数：收款时间-起息时间+1

			billDTOList.add(billDTO);
		}

		exportDTO.setItemList(billDTOList);
		return exportDTO;

	}
	/**
	 * 删除赊销单
	 * @param id
	 * @throws Exception
	 */
	@Override
	public void deleteCreditOrder(String ids  ,User user) throws Exception{
		List<Long> creditIds = StrUtils.parseIds(ids);
		for(Long crediId : creditIds) {
			SaleCreditOrderModel creditModel = saleCreditOrderDao.searchById(crediId);
			if(!DERP_ORDER.SALECREDIT_STATUS_001.equals(creditModel.getStatus())) {
				throw new RuntimeException("赊销单状态不为“待提交”");
			}

			//逻辑删除赊销单
			creditModel.setStatus(DERP.DEL_CODE_006);
			saleCreditOrderDao.modify(creditModel);
		}
	}
	//导出试算结果
	@Override
	public SaleCreditOrderExportDTO exportCalResult(String json) throws Exception {
		// 解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		Long creditId = Long.valueOf(jsonObj.getString("id"));
		String creditAmount = (String) jsonObj.get("creditAmount");
		String valueDate = (String) jsonObj.get("valueDate");
		String receiveDate = (String) jsonObj.get("receiveDate");
		String discountDelayAmount = (String) jsonObj.get("discountDelayAmount");

		// 1、检查订单金额、起算日期、实际还款日期是否为空，若为空，提示错误“XXX不能为空”
		SaleCreditOrderModel creditModel= saleCreditOrderDao.searchById(creditId);
		if (creditModel == null) {
			throw new RuntimeException("赊销单不存在!");
		}
		if (StringUtils.isBlank(creditAmount)) {
			throw new RuntimeException("订单金额不能为空!");
		}
		if (StringUtils.isBlank(valueDate)) {
			throw new RuntimeException("起算日期不能为空!");
		}
		if (StringUtils.isBlank(receiveDate)) {
			throw new RuntimeException("还款日期不能为空!");
		}

		// 解析表体数据
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		if(itemArr == null || itemArr.size() < 1 ) {
			throw new RuntimeException("赊销单商品信息为空!");
		}
		int nullItemNum = 0;
		List<SaleCreditOrderItemDTO> itemList = new ArrayList<SaleCreditOrderItemDTO>();
		for (int i = 0; i < itemArr.size(); i++) {
			JSONObject job = itemArr.getJSONObject(i);
			Integer redempNum = Integer.valueOf(job.getString("redempNum")) ;
			Integer stayRedempNum = Integer.valueOf(job.getString("stayRedempNum"));

			if(redempNum < 1) {
				nullItemNum++;
				continue;
			}
			if(redempNum > stayRedempNum ) {
				throw new RuntimeException("赎回数量不能大于可赎回数量!");
			}
		}
		if(nullItemNum > 0 && itemArr.size() == nullItemNum) {
			throw new RuntimeException("请输入本次要赎回的商品数量!");
		}

		for (int i = 0; i < itemArr.size(); i++) {
			JSONObject job = itemArr.getJSONObject(i);
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsNo = (String) job.get("goodsNo");
			String goodsName = (String) job.get("goodsName");
			Integer redempNum = Integer.valueOf(job.getString("redempNum")) ;

			SaleCreditOrderItemDTO itemModel = new SaleCreditOrderItemDTO();
			itemModel.setGoodsId(goodsId);
			itemModel.setGoodsNo(goodsNo);
			itemModel.setGoodsName(goodsName);
			itemModel.setRedempNum(redempNum);

			itemList.add(itemModel);
		}

		SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
		dto.setId(creditId);
		dto.setCreditAmount(new BigDecimal(creditAmount));
		dto.setValueDate(TimeUtils.parseDay(valueDate));
		dto.setItemList(itemList);
		List<SaleCreditBillOrderDTO> billDTOList = new ArrayList<SaleCreditBillOrderDTO>();
		//当前试算结果
		SaleCreditBillOrderDTO nowBillDTO = getReplayAmount(dto ,receiveDate,StringUtils.isBlank(discountDelayAmount)? BigDecimal.ZERO:new BigDecimal(discountDelayAmount));

		// 根据商家id获取商家信息
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", creditModel.getMerchantId());
		MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchantParams);

		SaleCreditOrderExportDTO exportDTO = new SaleCreditOrderExportDTO();
		exportDTO.setCode(creditModel.getCode());
		exportDTO.setMerchantName(merchant.getEnglishName());
		exportDTO.setCreditAmount(creditModel.getCreditAmount());
		exportDTO.setValueDate(creditModel.getValueDate());
		exportDTO.setExpireDate(creditModel.getExpireDate());
		exportDTO.setCurrency(creditModel.getCurrency());
		exportDTO.setActualMarginAmount(creditModel.getActualMarginAmount());
		exportDTO.setReceivePrincipalAmount(creditModel.getReceivePrincipalAmount());
		exportDTO.setReceiveDate(creditModel.getReceiveDate());
		exportDTO.setReceiveInterestAmount(creditModel.getReceiveInterestAmount());
		exportDTO.setBankAccount(merchant.getBankAccount());
		exportDTO.setBeneficiaryName(merchant.getBeneficiaryName());
		exportDTO.setBankAddress(merchant.getBankAddress());
		exportDTO.setDepositBank(merchant.getDepositBank());
		exportDTO.setSwiftCode(merchant.getSwiftCode());
		exportDTO.setPoNo(creditModel.getPoNo());

		//已保存赊销账单
		SaleCreditBillOrderModel queryBillModel = new SaleCreditBillOrderModel();
		queryBillModel.setCreditOrderId(dto.getId());
		List<SaleCreditBillOrderModel> billList = saleCreditBillOrderDao.list(queryBillModel);
		for(SaleCreditBillOrderModel billModel : billList) {
			SaleCreditBillOrderDTO billDTO = new SaleCreditBillOrderDTO();
			BeanUtils.copyProperties(billModel, billDTO);
			billDTO.setInterestDay(TimeUtils.daysBetween(creditModel.getValueDate(),billModel.getReceiveDate()) +1);//计息天数：收款时间-起息时间+1

			billDTOList.add(billDTO);
		}
		billDTOList.add(nowBillDTO);

		exportDTO.setItemList(billDTOList);
		return exportDTO;

	}

	private SaleCreditBillOrderDTO getReplayAmount(SaleCreditOrderDTO dto, String receiveDate,BigDecimal discountDelayAmount) throws Exception {
		//赊销单
		SaleCreditOrderModel creditModel= saleCreditOrderDao.searchById(dto.getId());

		// 2、校验通过后，调用3.16还款试算接口
		// 商家信息
		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("customerid", creditModel.getCustomerId());
		CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParams);

		PayBackTrialRequest trialRequest = new PayBackTrialRequest();
		trialRequest.setRefundDate(receiveDate);
		trialRequest.setInterestCurrency(creditModel.getCurrency());
		trialRequest.setBorrower(customer != null ? customer.getCreditCode() : "");

		List<PayBackTrialGoodRequest> triaGoodsList = new ArrayList<>();
		for (SaleCreditOrderItemDTO item :dto.getItemList()) {
			if(item.getRedempNum() < 1) {
				continue;
			}
			PayBackTrialGoodRequest goodRequest = new PayBackTrialGoodRequest();
			goodRequest.setGoodsNo(item.getGoodsNo());
			goodRequest.setGoodsName(item.getGoodsName());
			goodRequest.setQty(item.getRedempNum());
			goodRequest.setProcurementNo(creditModel.getCode());
			triaGoodsList.add(goodRequest);
		}

		trialRequest.setGoodList(triaGoodsList);

		JSONObject fromObject = JSONObject.fromObject(trialRequest);
		fromObject.put("method", "redemption.cost.count.get");
		String finance = FinanceUtils.getFiance(fromObject);

		// 3、调用成功后显示相关信息，若失败，提示错误原因
		if (StringUtils.isBlank(finance)) {
			throw new RuntimeException("还款试算接口返回异常！");
		}

		com.alibaba.fastjson.JSONObject finaceJson = com.alibaba.fastjson.JSONObject.parseObject(finance);;
		Integer status = Integer.valueOf(finaceJson.getString("status"));
		if (status == 2) {
			if (finaceJson.get("notes") != null) {
				throw new RuntimeException(finaceJson.getString("notes"));
			}
			throw new RuntimeException("还款试算接口返回异常");
		}

		BigDecimal totalPrincipal = new BigDecimal(finaceJson.getString("totalPrincipal"));// 应还本金
		BigDecimal totalOccupation = new BigDecimal(finaceJson.getString("totalOccupation"));// 应还利息 资金占用费
		BigDecimal totalAgencyFee = new BigDecimal(finaceJson.getString("totalAgencyFee"));// 代理费
		BigDecimal totalDelayFee = new BigDecimal(finaceJson.getString("totalDelayFee"));// 滞纳金
		BigDecimal totalAmount = new BigDecimal(finaceJson.getString("totalAmount"));// 应还款金额
		BigDecimal margin = new BigDecimal(finaceJson.getString("margin"));// 保证金

		totalAmount = totalAmount.subtract(discountDelayAmount == null ? BigDecimal.ZERO :discountDelayAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

		SaleCreditBillOrderDTO  billDTO = new SaleCreditBillOrderDTO();
		billDTO.setPrincipalAmount(totalPrincipal);
		billDTO.setOccupationAmount(totalOccupation);
		billDTO.setAgencyAmount(totalAgencyFee);
		billDTO.setDelayAmount(totalDelayFee);
		billDTO.setReceivableAmount(totalAmount);
		billDTO.setMarginAmount(margin);
		billDTO.setReceiveDate(TimeUtils.parseDay(receiveDate));

		return billDTO;
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
