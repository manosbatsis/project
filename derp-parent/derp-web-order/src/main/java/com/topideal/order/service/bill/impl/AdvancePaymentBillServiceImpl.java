package com.topideal.order.service.bill.impl;

import com.alibaba.fastjson.JSONObject;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.NumberToCN;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.AdvancePaymentBillDao;
import com.topideal.dao.bill.AdvancePaymentBillItemDao;
import com.topideal.dao.bill.AdvancePaymentBillRelDao;
import com.topideal.dao.bill.AdvancePaymentRecordItemDao;
import com.topideal.dao.bill.OperateLogDao;
import com.topideal.dao.bill.ReceivePaymentSubjectDao;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.entity.dto.bill.AdvancePaymentBillDTO;
import com.topideal.entity.dto.bill.AdvancePaymentBillExportDTO;
import com.topideal.entity.dto.bill.AdvancePaymentBillItemDTO;
import com.topideal.entity.dto.bill.AdvancePaymentBillItemExportDTO;
import com.topideal.entity.dto.bill.AdvancePaymentRecordItemDTO;
import com.topideal.entity.dto.bill.AdvancePaymentRecordItemExportDTO;
import com.topideal.entity.dto.bill.OperateLogDTO;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.entity.vo.bill.AdvancePaymentBillItemModel;
import com.topideal.entity.vo.bill.AdvancePaymentBillModel;
import com.topideal.entity.vo.bill.AdvancePaymentBillRelModel;
import com.topideal.entity.vo.bill.AdvancePaymentRecordItemModel;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.mongo.dao.AttachmentInfoMongoDao;
import com.topideal.mongo.dao.ContractNoMongoDao;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.mongo.entity.ContractNoMongo;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.order.service.bill.AdvancePaymentBillService;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.webService.OAUtils;
import com.topideal.webService.oa.o_01.CreatRequestIdRequest;
import com.topideal.webService.oa.o_01.CtreatRequestIdItemRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdvancePaymentBillServiceImpl implements AdvancePaymentBillService {

    @Autowired
    private AdvancePaymentBillDao advancePaymentBillDao ;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao ;
    @Autowired
    private PurchaseOrderDao purchaseOrderDao ;
    @Autowired
    private AdvancePaymentBillItemDao advancePaymentBillItemDao ;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao ;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private SettlementConfigDao settlementConfigDao ;
    @Autowired
    private PurchaseOrderItemDao purchaseOrderItemDao ;
    @Autowired
    private OperateLogDao operateLogDao ;
    @Autowired
    private AdvancePaymentRecordItemDao advancePaymentRecordItemDao ;
    @Autowired
    private ReceivePaymentSubjectDao receivePaymentSubjectDao ;
    @Autowired
    private ContractNoMongoDao contractNoMongoDao ;
    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao ;
    @Autowired
    private AdvancePaymentBillRelDao advancePaymentBillRelDao ;
    @Autowired
    private CommonBusinessService commonBusinessService ;
	//外部单号
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao ;
    @SuppressWarnings("unchecked")
	@Override
    public AdvancePaymentBillDTO listAdvancePaymentBill(AdvancePaymentBillDTO dto, User user) {

        if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            //关联ID为空时，返回空列表
            if(buIds.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }

            dto.setBuIds(buIds);

        }

        dto = advancePaymentBillDao.getListByPage(dto) ;

        return dto;
    }

    @Override
    public void getCheckData(List<Long> idList) throws SQLException {

        Long supplierId = null ;
        Long buId = null ;
        String currency = null ;

        for (Long purchaseId: idList) {

            PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(purchaseId);

            if(!(DERP_ORDER.PURCHASEORDER_STATUS_002.equals(purchaseOrderModel.getStatus())
                || DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrderModel.getStatus()))){
                throw new DerpException("只能选择 【待审核】【已审核】的采购订单") ;
            }

            if(supplierId == null){
                supplierId = purchaseOrderModel.getSupplierId() ;
            }

            if(buId == null){
                buId = purchaseOrderModel.getBuId() ;
            }

            if(currency == null){
                currency = purchaseOrderModel.getCurrency() ;
            }

            if(supplierId != null && supplierId.longValue() != purchaseOrderModel.getSupplierId().longValue()){

                throw new DerpException("所选采购单不为相同供应商") ;

            }

            if(buId != null && buId.longValue() != purchaseOrderModel.getBuId().longValue()){

                throw new DerpException("所选采购单不为相同事业部") ;

            }

            if(currency != null && !currency.equals(purchaseOrderModel.getCurrency())){
                throw new DerpException("所选采购单不为相同币种") ;
            }

            AdvancePaymentBillRelModel queryModel = new AdvancePaymentBillRelModel() ;
            queryModel.setPurchaseOrderId(purchaseOrderModel.getId());

            queryModel = advancePaymentBillRelDao.searchByModel(queryModel) ;

            if(queryModel != null){
                throw new DerpException("采购订单：" + purchaseOrderModel.getCode() + "已生成预付单") ;
            }

        }

    }

    @Override
    public Long saveOrModifyAdvancePaymentBill(AdvancePaymentBillDTO dto, User user) throws SQLException {

        AdvancePaymentBillModel model = new AdvancePaymentBillModel() ;

        BeanUtils.copyProperties(dto, model);

        // 通过商家id获取商家信息
        Map<String, Object> merchanto_params = new HashMap<String, Object>();
        merchanto_params.put("merchantId", model.getMerchantId());
        MerchantInfoMongo merchantMogo = merchantInfoMongoDao.findOne(merchanto_params);

        if (merchantMogo == null) {
            throw new DerpException("商家不存在") ;
        }
        if (model.getId()!=null) {
        	AdvancePaymentBillModel paymentBillModel = advancePaymentBillDao.searchById(model.getId());
        	  if(paymentBillModel == null) {
      			throw new DerpException("预付单号不存在") ;
      		}
      		if(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_01.equals(paymentBillModel)) {
      			throw new DerpException("预付单号状态是审核中 不能审核") ;
      		}
		}
      

        model.setMerchantName(merchantMogo.getName());

        Map<String, Object> customer_params = new HashMap<String, Object>();
        customer_params.put("customerid", model.getSupplierId());
        CustomerInfoMogo customer = customerInfoMongoDao.findOne(customer_params);

        if (customer == null) {
            throw new DerpException("供应商不存在") ;
        }

        model.setSupplierName(customer.getName());

        //查询事业部名称
        Map<String, Object> relParams = new HashMap<String, Object>() ;
        relParams.put("merchantId", model.getMerchantId());
        relParams.put("buId", model.getBuId());
        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(relParams);
        if(merchantBuRelMongo != null) {
            model.setBuName(merchantBuRelMongo.getBuName());
        }

        String operateAction = "" ;
        
        if(model.getId() == null){
            model.setCreateDate(TimeUtils.getNow());
            model.setCreater(user.getName());
            model.setBillStatus(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_00);

            advancePaymentBillDao.save(model) ;
            
            operateAction = "新增" ;
            
        }else{
            model.setModifyDate(TimeUtils.getNow());

            advancePaymentBillDao.modify(model) ;
            
            operateAction = "编辑" ;
        }
        
        /**记录操作日志*/
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_2, model.getCode(), operateAction, null, null);

        if(dto.getId() != null){

            AdvancePaymentBillItemModel queryItem = new AdvancePaymentBillItemModel() ;

            queryItem.setAdvancePaymentId(dto.getId());

            List<AdvancePaymentBillItemModel> itemList = advancePaymentBillItemDao.list(queryItem);

            List<Long> ids = itemList.stream().map(AdvancePaymentBillItemModel::getId).collect(Collectors.toList());

            if(ids != null && !ids.isEmpty()){
                advancePaymentBillItemDao.delete(ids) ;
            }

            AdvancePaymentBillRelModel queryRelItem = new AdvancePaymentBillRelModel() ;
            queryRelItem.setAdvancePaymentId(dto.getId());
            
            List<AdvancePaymentBillRelModel> relList = advancePaymentBillRelDao.list(queryRelItem);
            
            List<Long> relIds = relList.stream().map(AdvancePaymentBillRelModel::getId).collect(Collectors.toList());
            
            if(relIds != null && !relIds.isEmpty()){
            	advancePaymentBillRelDao.delete(relIds) ;
            }
        }

        List<AdvancePaymentBillItemDTO> itemList = dto.getItemList();

        BigDecimal purchaseAmount = new BigDecimal(0) ;
        BigDecimal prepaymentAmount = new BigDecimal(0) ;
        
        Set<String> purcodeSet = new HashSet<>() ;
        
        for (AdvancePaymentBillItemDTO itemDto: itemList) {
        	
        	boolean isExsit = purcodeSet.contains(itemDto.getPurchaseCode());
        	
        	if(isExsit) {
        		throw new DerpException("付款汇总，存在相同采购订单记录") ;
        	}else {
        		purcodeSet.add(itemDto.getPurchaseCode()) ;
        	}

            AdvancePaymentBillItemModel itemModel = new AdvancePaymentBillItemModel() ;

            BeanUtils.copyProperties(itemDto, itemModel);

            Long projectId = itemDto.getProjectId();

            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(projectId);

            if(settlementConfigModel != null){
                itemModel.setProjectName(settlementConfigModel.getProjectName());
                itemModel.setParentProjectId(settlementConfigModel.getParentId());
                itemModel.setParentProjectName(settlementConfigModel.getParentProjectName());
            }

            itemModel.setCreateDate(TimeUtils.getNow());
            itemModel.setAdvancePaymentId(model.getId());
            itemModel.setVerificateAmount(itemModel.getPrepaymentAmount());
            
            purchaseAmount = purchaseAmount.add(itemModel.getPurchaseAmount()) ;
            prepaymentAmount = prepaymentAmount.add(itemModel.getPrepaymentAmount()) ;

            advancePaymentBillItemDao.save(itemModel) ;
            
            /**保存关联关系*/
            AdvancePaymentBillRelModel relModel = new AdvancePaymentBillRelModel() ;
            relModel.setAdvancePaymentId(model.getId());
            relModel.setPurchaseOrderId(itemDto.getPurchaseId());
            relModel.setCreateDate(TimeUtils.getNow());
            
            advancePaymentBillRelDao.save(relModel) ;

        }
        
        AdvancePaymentBillModel updateModel = new AdvancePaymentBillModel() ;
        
        updateModel.setId(model.getId());
        updateModel.setPrepaymentAmount(prepaymentAmount);
        updateModel.setVerificationAmount(prepaymentAmount);
        updateModel.setPurchaseAmount(purchaseAmount);
        
        advancePaymentBillDao.modify(updateModel) ;
        
        return model.getId() ;
        
    }

    @Override
    public AdvancePaymentBillDTO getAddPageInfo(List<Long> idList) throws Exception {

        String currency = null;
        Long supplierId = null ;
        Long merchantId = null ;
        String merchantName = null ;
        Long buId = null ;
        String buName = null ;

        AdvancePaymentBillDTO dto = new AdvancePaymentBillDTO() ;
        List<AdvancePaymentBillItemDTO> itemList = new ArrayList<AdvancePaymentBillItemDTO>() ;

        BigDecimal totalAmount = new BigDecimal(0);
        
        SettlementConfigModel queryModel = new SettlementConfigModel() ;
        queryModel.setProjectName("采购货款");
        
        queryModel = settlementConfigDao.searchByModel(queryModel) ;

        for (Long purchaseId:idList) {

            PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(purchaseId);
            
            if(currency == null) {
            	currency = purchaseOrderModel.getCurrency() ;
            }
            
            if(supplierId == null) {
            	supplierId = purchaseOrderModel.getSupplierId() ;
            }
            
            if(merchantId == null) {
            	merchantId = purchaseOrderModel.getMerchantId() ;
            }
            
            if(merchantName == null) {
            	merchantName = purchaseOrderModel.getMerchantName() ;
            }
            
            if(buId == null) {
            	buId = purchaseOrderModel.getBuId() ;
            }
            
            if(buName == null) {
            	buName = purchaseOrderModel.getBuName() ;
            }

            PurchaseOrderItemModel queryItemModel = new PurchaseOrderItemModel() ;
            queryItemModel.setPurchaseOrderId(purchaseId);

            List<PurchaseOrderItemModel> tempItemList = purchaseOrderItemDao.list(queryItemModel);

            BigDecimal amount = new BigDecimal(0);

            if(!tempItemList.isEmpty()){
                amount = tempItemList.stream().map(PurchaseOrderItemModel::getTaxAmount)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                
                totalAmount = totalAmount.add(amount) ;
            }
            
            AdvancePaymentBillItemDTO itemDto = new AdvancePaymentBillItemDTO() ;
            
            itemDto.setCurrency(currency);
            itemDto.setPurchaseAmount(amount);
            itemDto.setPrepaymentAmount(amount);
            itemDto.setPurchaseCode(purchaseOrderModel.getCode());
            itemDto.setPurchaseId(purchaseOrderModel.getId());
            
            if(queryModel != null) {
            	itemDto.setProjectId(queryModel.getId());
            	itemDto.setProjectName(queryModel.getProjectName());
            }
            
            itemList.add(itemDto) ;
        }
        
        Map<String, Object> customer_params = new HashMap<String, Object>();
        customer_params.put("customerid", supplierId);
        CustomerInfoMogo supplier = customerInfoMongoDao.findOne(customer_params);

        dto.setSupplierId(supplierId);
        dto.setSupplierName(supplier.getName());
        dto.setDepositBank(supplier.getDepositBank());
        dto.setBankAccount(supplier.getBankAccount());
        dto.setBankAddress(supplier.getBankAddress());
        dto.setBeneficiaryName(supplier.getBeneficiaryName());
        dto.setSwiftCode(supplier.getSwiftCode());
        dto.setCode(SmurfsUtils.getID(DERP.UNIQUE_ID_YFKD));
        dto.setPrepaymentAmount(totalAmount);
        dto.setPurchaseAmount(totalAmount);
        dto.setPrepaymentAmountStr(NumberToCN.number2CNMontrayUnit(totalAmount));
        dto.setMerchantId(merchantId);
        dto.setMerchantName(merchantName);
        dto.setBuId(buId);
        dto.setBuName(buName);
        
        dto.setItemList(itemList);

        return dto;
    }

	@Override
	public AdvancePaymentBillDTO getDetail(long id) throws SQLException {
		
		AdvancePaymentBillModel paymentBill = advancePaymentBillDao.searchById(id);
		
		AdvancePaymentBillDTO dto = new AdvancePaymentBillDTO() ;
		
		BeanUtils.copyProperties(paymentBill, dto);
		
		if(paymentBill.getPrepaymentAmount() != null) {
			dto.setPrepaymentAmountStr(NumberToCN.number2CNMontrayUnit(paymentBill.getPrepaymentAmount()));
		}
		
		return dto;
	}

	@Override
	public void delAdvanceBill(User user, List<Long> list) throws SQLException {
		
		for (Long advancePaymentId : list) {
			
			AdvancePaymentBillModel paymentModel = advancePaymentBillDao.searchById(advancePaymentId);
			
			if(!(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_00.equals(paymentModel.getBillStatus())
					|| DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_02.equals(paymentModel.getBillStatus()))) {
				throw new DerpException("仅对【待提交】 【已驳回】的预付款单可操作删除") ;
			}
			
			paymentModel.setBillStatus(DERP.DEL_CODE_006);
			paymentModel.setModifyDate(TimeUtils.getNow());
			
			advancePaymentBillDao.modify(paymentModel) ;
			
			AdvancePaymentBillItemModel queryModel = new AdvancePaymentBillItemModel() ;
			
			queryModel.setAdvancePaymentId(paymentModel.getId());
			List<AdvancePaymentBillItemModel> itemList = advancePaymentBillItemDao.list(queryModel);
			
			List<Long> idsList = itemList.stream().map(AdvancePaymentBillItemModel::getId).collect(Collectors.toList());
			
			if(idsList != null && !idsList.isEmpty()){
				advancePaymentBillItemDao.delete(idsList) ;
			}
			
			/**删除关联关系*/
			AdvancePaymentBillRelModel queryRelItem = new AdvancePaymentBillRelModel() ;
            queryRelItem.setAdvancePaymentId(paymentModel.getId());
            
            List<AdvancePaymentBillRelModel> relList = advancePaymentBillRelDao.list(queryRelItem);
            
            List<Long> relIds = relList.stream().map(AdvancePaymentBillRelModel::getId).collect(Collectors.toList());
            
            if(relIds != null && !relIds.isEmpty()){
            	advancePaymentBillRelDao.delete(relIds) ;
            }
			
			/**记录操作日志*/
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_2, paymentModel.getCode(), "删除", null, null);
			
		}
		
	}

	@Override
	public void submitInvalidBill(List<Long> list, String invalidRemark, User user) throws SQLException {
		
		for (Long advancePaymentId : list) {
			
			AdvancePaymentBillModel paymentModel = advancePaymentBillDao.searchById(advancePaymentId);
			
			if(!DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_03.equals(paymentModel.getBillStatus())) {
				throw new DerpException("仅对【待付款】状态下的预付款单可操作“作废”") ;
			}
			
			BigDecimal paymentAmount = paymentModel.getPaymentAmount();
			
			if(paymentAmount != null
					&& paymentAmount.compareTo(new BigDecimal(0)) > 0) {
				throw new DerpException("作废失败，已付金额大于0") ;
			}
			
			paymentModel.setBillStatus(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_04);
			paymentModel.setModifierId(user.getId());
			paymentModel.setModifier(user.getName());
			paymentModel.setModifyDate(TimeUtils.getNow());
			
			advancePaymentBillDao.modify(paymentModel) ;
			
			/**记录操作日志*/
			
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_2, paymentModel.getCode(), "提交作废", null, invalidRemark);
			
		}
		
	}

	@Override
	public void savePayment(AdvancePaymentRecordItemDTO dto, User user) throws SQLException {
		
		AdvancePaymentBillModel paymentModel = advancePaymentBillDao.searchById(dto.getAdvancePaymentId());
		
		if(!DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_03.equals(paymentModel.getBillStatus())) {
			throw new DerpException("仅当单据状态为【待付款】时可操作付款") ;
		}
		
		BigDecimal tempPaymentAmount = paymentModel.getPaymentAmount();
		
		if(tempPaymentAmount == null) {
			tempPaymentAmount = new BigDecimal(0) ;
		}
		
		tempPaymentAmount = tempPaymentAmount.add(dto.getPaymentAmount()) ;
		
		if(tempPaymentAmount.compareTo(paymentModel.getPrepaymentAmount()) == 1) {
			throw new DerpException("本次付款金额+已付金额不能大于预付总额") ;
		}
		
		int daysBetween = TimeUtils.daysBetween(new Date(), dto.getPaymentDate());
		
		if(daysBetween > 0) {
			throw new DerpException("本次付款日期不得晚于当前日期") ;
		}
		
		AdvancePaymentRecordItemModel model = new AdvancePaymentRecordItemModel() ;
		
		BeanUtils.copyProperties(dto, model);
		
		model.setCreateDate(TimeUtils.getNow());
		
		advancePaymentRecordItemDao.save(model) ;
		
		BigDecimal paymentAmount = paymentModel.getPaymentAmount();
		
		if(paymentAmount == null) {
			paymentAmount = new BigDecimal(0) ;
		}
		
		paymentAmount = paymentAmount.add(dto.getPaymentAmount()) ;
		
		paymentModel.setPaymentAmount(paymentAmount);
		paymentModel.setModifyDate(TimeUtils.getNow());
		
		if(tempPaymentAmount.compareTo(paymentModel.getPrepaymentAmount()) == 0) {
			paymentModel.setBillStatus(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_06);
		}
		
		advancePaymentBillDao.modify(paymentModel) ;
		
		 /**记录操作日志*/
		
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_2, paymentModel.getCode(), "录入付款记录", null, null);
	}

	@Override
	public void modifyAuditMethod(User user, Long id, String auditMethod) throws Exception {
		
		AdvancePaymentBillModel paymentModel = advancePaymentBillDao.searchById(id);
		
		if(paymentModel == null) {
			throw new DerpException("预付单号不存在") ;
		}
		if(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_01.equals(paymentModel)) {
			throw new DerpException("预付单号状态是审核中 不能审核") ;
		}
		
		boolean empty = new EmptyCheckUtils().addObject(paymentModel.getBankAccount())
			.addObject(paymentModel.getBeneficiaryName())
			.addObject(paymentModel.getDepositBank())
			.addObject(paymentModel.getCurrency())
			.addObject(paymentModel.getPaymentReason())
			.addObject(paymentModel.getExpectedPaymentDate())
			.addObject(paymentModel.getCurrency())
			.addObject(paymentModel.getPrepaymentAmount())
			.empty();
		
		if(empty) {
			throw new DerpException("必填项为空，请检查") ;
		}
		
		AdvancePaymentBillItemModel queryModel = new AdvancePaymentBillItemModel() ;
		
		queryModel.setAdvancePaymentId(paymentModel.getId());
		List<AdvancePaymentBillItemModel> itemList = advancePaymentBillItemDao.list(queryModel);
		
		if(itemList == null 
				|| itemList.isEmpty()) {
			throw new DerpException("付款汇总为空，请检查") ;
		}
		
		AdvancePaymentBillModel updateModel = new AdvancePaymentBillModel() ;
		
		/**推送OA审批**/
		if(DERP_ORDER.ADVANCE_PAYMENT_BILL_AUDIT_METHOD_1.equals(auditMethod)) {
			
			// 通过商家id获取商家信息
	        Map<String, Object> merchanto_params = new HashMap<String, Object>();
	        merchanto_params.put("merchantId", paymentModel.getMerchantId());
	        MerchantInfoMongo merchantMogo = merchantInfoMongoDao.findOne(merchanto_params);
			
			Map<String, Object> customer_params = new HashMap<String, Object>();
	        customer_params.put("customerid", paymentModel.getSupplierId());
	        CustomerInfoMogo supplier = customerInfoMongoDao.findOne(customer_params);
			
	        Map<String, Object> relParams = new HashMap<String, Object>() ;
			relParams.put("buId", paymentModel.getBuId());
			relParams.put("merchantId", user.getMerchantId());
			relParams.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;
			MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(relParams);
			if(merchantBuRelMongo == null) {
				throw new DerpException("商家事业部关联不存在") ;
			}
	        
			String sqsj = TimeUtils.format(new Date(), "yyyy-MM-dd");
			String title = "采购请款-供应商:" + supplier.getName() + "-" + user.getName() + "-" + sqsj ;
			
			CreatRequestIdRequest model = new CreatRequestIdRequest() ;
			
			model.setWbxtdjh(paymentModel.getCode());
			model.setCwzz(merchantMogo.getTopidealCode());
			model.setSqr(user.getName());
			model.setSqsj(sqsj);
			model.setGysxz("1");
			model.setGys(supplier.getMainId());
			model.setSyb(merchantBuRelMongo.getBuCode());
			model.setSkyhzh(paymentModel.getBeneficiaryName());
			model.setSkyhzh1(paymentModel.getBankAccount());
			model.setSkyhkhh(paymentModel.getDepositBank());
			model.setSwiftcode(paymentModel.getSwiftCode());
			model.setJsbz(paymentModel.getCurrency());
			model.setQkyy(paymentModel.getPaymentReason());
			model.setTitle(title);
			
			BigDecimal prepaymentAmount = paymentModel.getPrepaymentAmount() ;
			prepaymentAmount = prepaymentAmount.setScale(2) ;
			
			model.setYbjehz(prepaymentAmount.toString());
			model.setYbjedx(NumberToCN.number2CNMontrayUnit(prepaymentAmount));
			
			Timestamp expectedPaymentDate = paymentModel.getExpectedPaymentDate() ;
			model.setYjfksj(TimeUtils.format(expectedPaymentDate, "yyyy-MM-dd"));
			
			//获取附件
			Map<String, Object> queryAttMap = new HashMap<String, Object>() ;
			queryAttMap.put("relationCode", paymentModel.getCode()) ;
            queryAttMap.put("status", DERP_ORDER.ATTACHMENT_STATUS_001) ;
			
			List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(queryAttMap);
			
			StringBuffer sb = new StringBuffer() ;
			for (AttachmentInfoMongo attachmentInfoMongo : attList) {
				
				sb.append("<p><a href=\""+attachmentInfoMongo.getAttachmentUrl()+"\" target=\"_blank\">" + attachmentInfoMongo.getAttachmentName()+ "</a></p>") ;
			}
			
			if(sb.length() > 0) {
				model.setFj(sb.toString());
			}
			
			List<CtreatRequestIdItemRequest> requestItemList = new ArrayList<CtreatRequestIdItemRequest>() ;
			
			for (AdvancePaymentBillItemModel advancePaymentBillItemModel : itemList) {
				
				CtreatRequestIdItemRequest item = new CtreatRequestIdItemRequest() ;
				
				BigDecimal itemAmount = advancePaymentBillItemModel.getPrepaymentAmount();
				itemAmount = itemAmount.setScale(2) ;
				
				item.setZy("预付" + advancePaymentBillItemModel.getProjectName());
				item.setJebhs(itemAmount.toString());
				item.setSe("0");
				item.setJehs(itemAmount.toString());
				
				PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(advancePaymentBillItemModel.getPurchaseId());
				
				item.setPoh(purchaseOrder.getPoNo());
				
				SettlementConfigModel settlementConfig = 
						settlementConfigDao.searchById(advancePaymentBillItemModel.getProjectId());
				
				if(settlementConfig == null) {
					throw new DerpException("根据费项ID：" + advancePaymentBillItemModel.getProjectId() + 
							"费项名：" + advancePaymentBillItemModel.getProjectName() + "查询费项配置不存在") ;
				}
				
				ReceivePaymentSubjectModel ncSubject = 
						receivePaymentSubjectDao.searchById(settlementConfig.getPaymentSubjectId());
				
				item.setSzfx(ncSubject.getCode());
				
				requestItemList.add(item) ;
			}
			
			model.setItemList(requestItemList); 
			
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(paymentModel.getCode());
			orderExternalCodeModel.setOrderSource(10);	// 1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库 6.采购退货 7.采购入库 8.销售退 9.应付 10 预付 
			try {
				orderExternalCodeDao.save(orderExternalCodeModel);
			} catch (Exception e) {
				throw new DerpException("已经推送oa（不能重复推送）") ;
			}
			/**推送OA*/
			String result = OAUtils.getOARequestId(model, user.getCode());
			//保存OA单号
			updateModel.setRequestId(result);
			
			//保存到唯一单号表
			Map<String, Object> queryMap = new HashMap<String, Object>() ;
			queryMap.put("orderCode", paymentModel.getCode()) ;
			
			ContractNoMongo contractNoMongo = contractNoMongoDao.findOne(queryMap);
			
			if(contractNoMongo == null) {
				contractNoMongo = new ContractNoMongo() ;
				
				contractNoMongo.setContractNo(result);
				contractNoMongo.setOrderCode(paymentModel.getCode());
				contractNoMongo.setType(DERP.UNIQUE_ID_YFKD);
				
				contractNoMongoDao.insert(contractNoMongo); 
				
			}else {
				
				contractNoMongo.setContractNo(result);
				
				JSONObject json = (JSONObject)JSONObject.toJSON(contractNoMongo) ;
				
				contractNoMongoDao.update(queryMap, json) ;
				
			}
			
		}
		
		updateModel.setId(paymentModel.getId());
		updateModel.setAuditMethod(auditMethod);
		updateModel.setModifierId(user.getId());
		updateModel.setModifier(user.getName());
		updateModel.setBillStatus(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_01);
		updateModel.setModifyDate(TimeUtils.getNow());
		
		advancePaymentBillDao.modify(updateModel) ;
		
		 /**记录操作日志*/
		
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_2, paymentModel.getCode(), "提交审核", null, null);
		
	}

	@Override
	public void auditAdvancePayment(User user, Long id, String invalidRemark, String isPassed) throws SQLException {
		
		AdvancePaymentBillModel paymentModel = advancePaymentBillDao.searchById(id);
		
		if(paymentModel == null) {
			throw new DerpException("预付单号不存在") ;
		}
		
//		if(DERP_ORDER.ADVANCE_PAYMENT_BILL_AUDIT_METHOD_1.equals(paymentModel.getAuditMethod())) {
//			throw new DerpException("该预付单审批方式为：OA审批") ;
//		}
		
		if(!(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_01.equals(paymentModel.getBillStatus())
				|| DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_04.equals(paymentModel.getBillStatus()))) {
			throw new DerpException("仅当单据状态为【审核中】【待作废】时可操作审核") ;
		}
		
		String billStatus = null ;
		String result = null ;
		String msg = "" ;
		
		/****
		 * 1、预付审核

		*（1）通过时，更新预付款单状态为“待付款”；

		 *（2）驳回时，更新预付款单状态为“已驳回”；

		 *2、作废审核

		 *（1）通过时，更新预付款单状态为“已作废”；取消采购订单与预付款单的关联关系；

		 *（2）驳回时，预付款单状态不变，依旧为“待付款”；
		 */
		
		/**若为审核*/
		if(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_01.equals(paymentModel.getBillStatus())) {
			
			msg = "审核" ;
			
			if("1".equals(isPassed)) {
				billStatus = DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_03 ;
				result = "审核通过" ;
			}else {
				billStatus = DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_02 ;
				result = "审核驳回" ;
			}
			
		}else if(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_04.equals(paymentModel.getBillStatus())) {
			
			msg = "作废" ;
			
			if("1".equals(isPassed)) {
				
				billStatus = DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_05 ;
				result = "作废通过" ;
				
				/**删除关联关系*/
				AdvancePaymentBillRelModel queryRelItem = new AdvancePaymentBillRelModel() ;
	            queryRelItem.setAdvancePaymentId(paymentModel.getId());
	            
	            List<AdvancePaymentBillRelModel> relList = advancePaymentBillRelDao.list(queryRelItem);
	            
	            List<Long> relIds = relList.stream().map(AdvancePaymentBillRelModel::getId).collect(Collectors.toList());
	            
	            if(relIds != null && !relIds.isEmpty()){
	            	advancePaymentBillRelDao.delete(relIds) ;
	            }
				
			}else {
				billStatus = DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_03 ;
				result = "作废驳回" ;
			}
			
		}
		
		AdvancePaymentBillModel updateModel = new AdvancePaymentBillModel() ;
		updateModel.setId(id);
		updateModel.setBillStatus(billStatus);
		updateModel.setModifierId(user.getId());
		updateModel.setModifier(user.getName());
		updateModel.setBillStatus(billStatus);
		updateModel.setModifyDate(TimeUtils.getNow());
		
		advancePaymentBillDao.modify(updateModel) ;
		
		/**记录操作日志*/
		
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_2, paymentModel.getCode(), msg, result, invalidRemark);
	}

	@Override
	public List<AdvancePaymentBillItemDTO> getPaymentItems(Long id) throws SQLException {
		
		/**查询付款明细*/
		AdvancePaymentBillItemModel queryItemModel = new AdvancePaymentBillItemModel() ;
		queryItemModel.setAdvancePaymentId(id);
		
		List<AdvancePaymentBillItemModel> itemList = advancePaymentBillItemDao.list(queryItemModel);
		List<AdvancePaymentBillItemDTO> itemDtoList = new ArrayList<AdvancePaymentBillItemDTO>() ;
		
		for (AdvancePaymentBillItemModel itemModel : itemList) {
			
			AdvancePaymentBillItemDTO dtoModel = new AdvancePaymentBillItemDTO() ;
			BeanUtils.copyProperties(itemModel, dtoModel);
			
			itemDtoList.add(dtoModel) ;
			
		}
		
		return itemDtoList ;
	}

	@Override
	public List<OperateLogDTO> listOperateLog(Long id) throws SQLException {
		
		AdvancePaymentBillModel paymentModel = advancePaymentBillDao.searchById(id);
		
		if(paymentModel == null) {
			throw new DerpException("预付单号不存在") ;
		}
		
		/**查询操作记录*/
		OperateLogModel queryOperateLogModel = new OperateLogModel() ;
		queryOperateLogModel.setRelCode(paymentModel.getCode());
		
		List<OperateLogModel> operateList = operateLogDao.list(queryOperateLogModel);
		List<OperateLogDTO> operateDtoList = new ArrayList<OperateLogDTO>() ;
		
		for (OperateLogModel operateLogModel : operateList) {
			
			OperateLogDTO dtoModel = new OperateLogDTO() ;
			BeanUtils.copyProperties(operateLogModel, dtoModel);
			
			if(dtoModel.getOperateDate() != null) {
				dtoModel.setOperateDateStr(TimeUtils.format(dtoModel.getOperateDate(), "yyyy-MM-dd HH:mm:ss"));
			}
			
			operateDtoList.add(dtoModel) ;
			
		}
		
		return operateDtoList;
	}

	@Override
	public List<AdvancePaymentRecordItemDTO> getRecordItemList(Long id) throws SQLException {
		
		/**查询付款记录*/
		AdvancePaymentRecordItemModel queryRecordItemModel = new AdvancePaymentRecordItemModel() ;
		queryRecordItemModel.setAdvancePaymentId(id);
		
		List<AdvancePaymentRecordItemModel> recordItemList = advancePaymentRecordItemDao.list(queryRecordItemModel);
		List<AdvancePaymentRecordItemDTO> recordItemDtoList = new ArrayList<AdvancePaymentRecordItemDTO>() ;
		
		for (AdvancePaymentRecordItemModel advancePaymentRecordItemModel : recordItemList) {
			
			AdvancePaymentRecordItemDTO dtoModel = new AdvancePaymentRecordItemDTO() ;
			BeanUtils.copyProperties(advancePaymentRecordItemModel, dtoModel);
			
			recordItemDtoList.add(dtoModel) ;
		}
		
		return recordItemDtoList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PurchaseOrderDTO listPurchaseOrderPage(PurchaseOrderDTO dto, User user) throws SQLException {
		
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			//关联ID为空时，返回空列表
			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}

			dto.setBuIds(buIds);

		}
		
		dto = purchaseOrderDao.getAdvancePaymentListByPage(dto) ;
		
		List<PurchaseOrderDTO> list = dto.getList();
		
		for (PurchaseOrderDTO d : list) {
			// 设值采购总金额
			PurchaseOrderItemModel itemModel = new PurchaseOrderItemModel();
			itemModel.setPurchaseOrderId(d.getId());
			List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(itemModel);
			BigDecimal amount = new BigDecimal(0);
			
			for (int i = 0; i < itemList.size(); i++) {
				PurchaseOrderItemModel item = itemList.get(i);
				
				if(item.getTaxAmount() == null) {
					continue ;
				}
				
				amount = amount.add(item.getTaxAmount());
			}
			
			d.setGoodsAmount(amount);
		}
		
		return dto;
	}

	@Override
	public List<AdvancePaymentBillExportDTO> exportExcel(AdvancePaymentBillExportDTO exportDTO, User user) throws SQLException {
		
		if(exportDTO.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            //关联ID为空时，返回空列表
            if(buIds.isEmpty()) {
                return new ArrayList<AdvancePaymentBillExportDTO>();
            }

            exportDTO.setBuIds(buIds);

        }
		
		List<AdvancePaymentBillExportDTO> exportList = advancePaymentBillDao.getExportExcel(exportDTO) ;
		
		for (AdvancePaymentBillExportDTO advancePaymentBillExpotDTO : exportList) {
			
			/**查询付款明细*/
			AdvancePaymentBillItemModel queryItemModel = new AdvancePaymentBillItemModel() ;
			queryItemModel.setAdvancePaymentId(advancePaymentBillExpotDTO.getId());
			
			List<AdvancePaymentBillItemModel> itemList = advancePaymentBillItemDao.list(queryItemModel);
			List<AdvancePaymentBillItemExportDTO> itemDTOList = new ArrayList<AdvancePaymentBillItemExportDTO>() ;
			
			for (AdvancePaymentBillItemModel itemModel : itemList) {
				
				AdvancePaymentBillItemExportDTO itemDTO = new AdvancePaymentBillItemExportDTO() ;
				
				BeanUtils.copyProperties(itemModel, itemDTO);
				
				itemDTO.setAdvancePaymentCode(advancePaymentBillExpotDTO.getCode());
				
				itemDTOList.add(itemDTO) ;
			}
			
			/**查询付款记录*/
			AdvancePaymentRecordItemModel queryRecordItemModel = new AdvancePaymentRecordItemModel() ;
			queryRecordItemModel.setAdvancePaymentId(advancePaymentBillExpotDTO.getId());
			
			List<AdvancePaymentRecordItemModel> recordItemList = advancePaymentRecordItemDao.list(queryRecordItemModel);
			List<AdvancePaymentRecordItemExportDTO> recordItemDtoList = new ArrayList<AdvancePaymentRecordItemExportDTO>() ;
			
			for (AdvancePaymentRecordItemModel recordItem : recordItemList) {
				
				AdvancePaymentRecordItemExportDTO recordExportDTO = new AdvancePaymentRecordItemExportDTO() ;
				
				BeanUtils.copyProperties(recordItem, recordExportDTO);
				
				recordExportDTO.setAdvancePaymentCode(advancePaymentBillExpotDTO.getCode());
				
				recordItemDtoList.add(recordExportDTO) ;
			}
			
			advancePaymentBillExpotDTO.setItemList(itemDTOList);
			advancePaymentBillExpotDTO.setRecordItemList(recordItemDtoList);
		}
		
		return exportList;
	}

	@Override
	public String getDetailPoNo(Long id) throws SQLException {
		
		String poNos = null ;
		
		/**查询付款明细*/
		AdvancePaymentBillItemModel queryItemModel = new AdvancePaymentBillItemModel() ;
		queryItemModel.setAdvancePaymentId(id);
		
		List<AdvancePaymentBillItemModel> itemList = advancePaymentBillItemDao.list(queryItemModel);
		
		for (AdvancePaymentBillItemModel advancePaymentBillItemModel : itemList) {
			
			Long purchaseId = advancePaymentBillItemModel.getPurchaseId();
			PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(purchaseId);
			
			String poNo = purchaseOrder.getPoNo();
			
			if(StringUtils.isBlank(poNo)) {
				continue ;
			}
			
			if(StringUtils.isBlank(poNos)) {
				poNos = poNo ;
			}else {
				poNos += "," + poNo ;
			}
			
		}
		
		return poNos;
	}

	@Override
	public AdvancePaymentBillDTO changeMerchantInfo(AdvancePaymentBillDTO detail) {
		Long merchantId = detail.getMerchantId();
		
		Map<String, Object> queryMap = new HashMap<>() ;
		
		queryMap.put("merchantId", merchantId) ;
		
		MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(queryMap);
		
		detail.setMerchantName(merchant.getFullName());
		
		return detail;
	}
}
