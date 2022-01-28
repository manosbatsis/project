package com.topideal.service.timer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.dao.purchase.PurchaseWarehouseItemDao;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.timer.GenerateInnerMerchantPaymentBillService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 生成内部公司应付账单
 * @author qiancheng.chen
 *
 */
@Service
public class GenerateInnerMerchantPaymentBillServiceImpl implements GenerateInnerMerchantPaymentBillService{
	@Autowired
	private PaymentBillDao paymentBillDao;
    @Autowired
    private PaymentItemDao paymentItemDao;
    @Autowired
	private PurchaseOrderDao purchaseOrderDao ;
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
    @Autowired
    private PaymentPurchaseRelDao paymentPurchaseRelDao;
    @Autowired
    private PaymentSummaryDao paymentSummaryDao;
    @Autowired
    private PaymentVerificateItemDao paymentVerificateItemDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private SettlementConfigDao settlementConfigDao;
    @Autowired
    private AdvancePaymentBillRelDao advancePaymentBillRelDao ;
    @Autowired
    private AdvancePaymentBillItemDao advancePaymentBillItemDao ;
    @Autowired
    private AdvancePaymentBillDao advancePaymentBillDao ;
    @Autowired
    private AdvancePaymentRecordItemDao advancePaymentRecordItemDao ;
	// 采购入库分析差异表
//	@Autowired
//	private PurchaseAnalysisDao purchaseAnalysisDao;
	@Autowired
	private OperateLogDao operateLogDao;
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;
	/**
	 *  生成内部公司应付账单
	 */
	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_20100000003,model=DERP_LOG_POINT.POINT_20100000003_Label)
	public void generateInnerMerchantPaymentBill(String json, String key, String topics, String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		String startMonth = (String) jsonData.get("startMonth");
		String endMonth = (String) jsonData.get("endMonth");

		//记录应付账单表头数据
		List<PaymentBillModel> paymentBillList = new ArrayList<PaymentBillModel>();
		//记录应付明细数据
		Map<String, List<PaymentItemModel>> paymentBillItemMap = new  HashMap<String, List<PaymentItemModel>>();
		List<PaymentItemModel> paymentBillItemList = new ArrayList<PaymentItemModel>();
		//记录应付汇总数据
		Map<String, List<PaymentSummaryModel>> paymentSummaryMap = new  HashMap<String, List<PaymentSummaryModel>>();
		List<PaymentSummaryModel> paymentSummaryList = new  ArrayList<PaymentSummaryModel>();
		//记录应付核销数据
		Map<String, List<PaymentVerificateItemModel>> paymentVerificateItemMap = new  HashMap<String, List<PaymentVerificateItemModel>>();
		List<PaymentVerificateItemModel> paymentVerificateItemList = new ArrayList<PaymentVerificateItemModel>();
		//记录应收账单-采购订单关联数据
		Map<String, List<Long>> paymentPurchaseRelMap = new  HashMap<String, List<Long>>();

		//1、查询为内部公司的供应商
		Map<String,Object> customerMap = new HashMap<String,Object>();
		customerMap.put("type", DERP_SYS.CUSTOMERINFO_TYPE_1);
		customerMap.put("cusType",  DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
		customerMap.put("status",  DERP_SYS.CUSTOMERINFO_STATUS_1);
		List<CustomerInfoMogo> innerSupplierList = customerInfoMongoDao.findAll(customerMap);
		List<Long> supplierIdList = innerSupplierList.stream().map(CustomerInfoMogo::getCustomerid).collect(Collectors.toList());

		//2、仅对指定公司主体（健太、卓烨、宝信、广旺、轩盛）
		List<Long> merchantIdList = getMerchantIds();

		//3、查询当月是否存在供应商为内部公司的采购订单（单据状态为已入库），且采购订单未有创建应付记录
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("merchantIdList", merchantIdList);
		paramMap.put("supplierIdList", supplierIdList);
		paramMap.put("startMonth", startMonth);
		paramMap.put("endMonth", endMonth);
		List<PurchaseOrderDTO> list = purchaseOrderDao.getNotPaymentBillPurchaseOrder(paramMap);
		//4、以相同“公司+供应商+事业部+采购入库月份+币种+业务模式”为一个应付账单
		Map<String, List<PurchaseOrderDTO>> purchaseOrderMap = list.stream().
				collect(Collectors.groupingBy(p ->+p.getMerchantId()+"-"+p.getBuId()+"-"+p.getSupplierId()+"-"+p.getCurrency()+"-"+p.getBusinessModel()+"-"+p.getInBoundMonth()));

		for(String purchaseKey: purchaseOrderMap.keySet()) {
			List<PurchaseOrderDTO> purchaseOrderList = purchaseOrderMap.get(purchaseKey);
			List<Long> purchaseIdList = purchaseOrderList.stream().map(PurchaseOrderDTO::getId).collect(Collectors.toList());
			PurchaseOrderDTO purchaseOrderDTO = purchaseOrderList.get(0);
			//获取项目配置信息
			SettlementConfigModel settlementConfigModel = new SettlementConfigModel() ;
			settlementConfigModel.setProjectName("经销库存");
            settlementConfigModel = settlementConfigDao.searchByModel(settlementConfigModel) ;

            //查询采购表体信息
            List<PurchaseOrderItemModel> purchaseItemList = purchaseOrderItemDao.getByOrderIds(purchaseIdList);
			//5、获取应付明细 汇总集合
            Map<String,Object> itemAndSummaryParamMap = new HashMap<String, Object>();
            itemAndSummaryParamMap.put("settlementConfigModel", settlementConfigModel);
            itemAndSummaryParamMap.put("purchaseOrderDTO", purchaseOrderDTO);
            itemAndSummaryParamMap.put("purchaseItemList", purchaseItemList);
			Map<String,Object> itemAndSummaryMap = getItemAndSummaryList(itemAndSummaryParamMap);
			List<PaymentItemModel> queryPayBillItemList= (List<PaymentItemModel>)itemAndSummaryMap.get("itemList");
			List<PaymentSummaryModel> summaryList = (List<PaymentSummaryModel>)itemAndSummaryMap.get("summaryMap");
			//6、获取应付核销记录
			List<PaymentVerificateItemModel> paymentVerificateList = getPaymentVerificateList(purchaseOrderDTO);//记录当前核销集合

            //计算应付明细含税总金额
	        BigDecimal totalAmount = queryPayBillItemList.stream().map(PaymentItemModel::getPurchaseTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
	        /**7、以核销金额倒核本次核销记录*/
            BigDecimal veriTotalAmount = totalAmount;
	        for (PaymentVerificateItemModel paymentVerificateItemModel : paymentVerificateList) {
	            BigDecimal verificateAmount = paymentVerificateItemModel.getVerificateAmount();
	            if(veriTotalAmount.compareTo(verificateAmount) >= 0) {
	            	paymentVerificateItemModel.setCurrentVerificateAmount(verificateAmount);
	                veriTotalAmount = veriTotalAmount.subtract(verificateAmount) ;

	            }else {
	            	paymentVerificateItemModel.setCurrentVerificateAmount(veriTotalAmount);
	                verificateAmount = verificateAmount.subtract(veriTotalAmount) ;
	                paymentVerificateItemModel.setVerificateAmount(verificateAmount);
	                veriTotalAmount = veriTotalAmount.subtract(veriTotalAmount) ;
	            }
	        }
	        //8、封装应付账单表头信息
	        PaymentBillModel payBillModel = getPayBillModel(purchaseOrderDTO,totalAmount,veriTotalAmount);
            paymentBillList.add(payBillModel);
            //9.记录应付明细 汇总数据 核销记录
            if(queryPayBillItemList != null && queryPayBillItemList.size() > 0) {
				paymentBillItemMap.put(payBillModel.getCode(),queryPayBillItemList) ;
				paymentBillItemList.addAll(queryPayBillItemList);
			}
            if(summaryList != null && summaryList.size() > 0) {
				paymentSummaryMap.put(payBillModel.getCode(),summaryList);
				paymentSummaryList.addAll(summaryList);
			}
            if(paymentVerificateList != null) {
				paymentVerificateItemMap.put(payBillModel.getCode(), paymentVerificateList) ;
				paymentVerificateItemList.addAll(paymentVerificateList);
			}
            //10、记录应付账单-采购订单关联记录
            paymentPurchaseRelMap.put(payBillModel.getCode(), purchaseIdList);
		}
		//保存
		if(paymentBillItemList.size() > 0) {
			paymentItemDao.batchSave(paymentBillItemList);
		}
		if(paymentSummaryList.size() > 0) {
			paymentSummaryDao.batchSave(paymentSummaryList);
		}
		if(paymentVerificateItemList.size() > 0) {
			paymentVerificateItemDao.batchSave(paymentVerificateItemList);
		}
		if(paymentBillList.size() > 0){
			paymentBillDao.batchSave(paymentBillList);
			//更新
			Map<String,Long> paymentCodeOrderIdMap = paymentBillList.stream().collect(Collectors.toMap(PaymentBillModel::getCode, PaymentBillModel::getId));

			for(PaymentBillModel model : paymentBillList) {
				Long paymentId = paymentCodeOrderIdMap.get(model.getCode());
				//更新应收明细
				List<PaymentItemModel> updateBillItemList = paymentBillItemMap.get(model.getCode());
				if(updateBillItemList != null && updateBillItemList.size() > 0) {
					List<Long> billItemIdList = updateBillItemList.stream().map(PaymentItemModel::getId).collect(Collectors.toList());
					paymentItemDao.batchUpdatePaymentId(billItemIdList, paymentId);
				}
				//更新汇总记录
				List<PaymentSummaryModel> updateSummaryList = paymentSummaryMap.get(model.getCode());
				if(updateSummaryList != null && updateSummaryList.size() > 0) {
					List<Long> summaryIdList = updateSummaryList.stream().map(PaymentSummaryModel::getId).collect(Collectors.toList());
					paymentSummaryDao.batchUpdatePaymentId(summaryIdList, paymentId);
				}
				//更新核销记录
				List<PaymentVerificateItemModel> updateVerificateItemList = paymentVerificateItemMap.get(model.getCode());
				if(updateVerificateItemList != null && updateVerificateItemList.size() > 0) {
					List<Long> VerificateItemIdList = updateVerificateItemList.stream().map(PaymentVerificateItemModel::getId).collect(Collectors.toList());
					paymentVerificateItemDao.batchUpdatePaymentId(VerificateItemIdList, paymentId);
				}
				//保存应付账单-采购订单关联记录
				List<Long> purchaseIdList = paymentPurchaseRelMap.get(model.getCode());
				for(Long purchaseId : purchaseIdList) {
					PaymentPurchaseRelModel paymentPurchaseRelModel = new PaymentPurchaseRelModel();
					paymentPurchaseRelModel.setPaymentId(paymentId);
					paymentPurchaseRelModel.setPurchaseId(purchaseId);
					paymentPurchaseRelModel.setCreateDate(TimeUtils.getNow());
					paymentPurchaseRelDao.save(paymentPurchaseRelModel);
				}
				//保存操作日志
				OperateLogModel saveModel = new OperateLogModel() ;
				saveModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_3);
				saveModel.setCreateDate(TimeUtils.getNow());
				saveModel.setOperateDate(TimeUtils.getNow());
				saveModel.setOperateAction("系统自动生成");
				saveModel.setRelCode(model.getCode());

				operateLogDao.save(saveModel) ;
			}
		}
	}
	/**
	 * 获取商家id集合
	 * @return
	 */
	private List<Long> getMerchantIds(){
		List<Long> merchantIdList = new ArrayList<Long>();
		Map<String,Object> merchantMap = new HashMap<String,Object>();
		merchantMap.put("code", "ERP31194100049");//健太
		MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantMap);
		merchantIdList.add(merchantInfo.getMerchantId());

		merchantMap.clear();
		merchantMap.put("code", "ERP26143500022");//卓烨贸易
		merchantInfo = merchantInfoMongoDao.findOne(merchantMap);
		merchantIdList.add(merchantInfo.getMerchantId());

		merchantMap.clear();
		merchantMap.put("code", "ERP31194100022");//宝信
		merchantInfo = merchantInfoMongoDao.findOne(merchantMap);
		merchantIdList.add(merchantInfo.getMerchantId());

		merchantMap.clear();
		merchantMap.put("code", "ERP31194300027");//广旺贸易
		merchantInfo = merchantInfoMongoDao.findOne(merchantMap);
		merchantIdList.add(merchantInfo.getMerchantId());

		merchantMap.clear();
		merchantMap.put("code", "ERP23112900030");//轩盛
		merchantInfo = merchantInfoMongoDao.findOne(merchantMap);
		merchantIdList.add(merchantInfo.getMerchantId());

		return merchantIdList;
	}
	/**
	 * 获取应付明细 汇总明细集合
	 * @param itemAndSummaryParamMap
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> getItemAndSummaryList(Map<String,Object> itemAndSummaryParamMap) throws Exception{
		SettlementConfigModel settlementConfigModel = (SettlementConfigModel) itemAndSummaryParamMap.get("settlementConfigModel");
		PurchaseOrderDTO purchaseOrderDTO = (PurchaseOrderDTO) itemAndSummaryParamMap.get("purchaseOrderDTO");
		List<PurchaseOrderItemModel> purchaseItemList = (List<PurchaseOrderItemModel>) itemAndSummaryParamMap.get("purchaseItemList");
		List<PaymentItemModel> itemList = new ArrayList<PaymentItemModel>();//记录当前应付明细集合
		Map<Long, PaymentSummaryModel> summaryMap = new HashMap<Long, PaymentSummaryModel>() ;//记录当前汇总集合
		for (PurchaseOrderItemModel itemModel : purchaseItemList) {
			//根据采购明细id获取已入库数量
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("purchaseItemId", itemModel.getId());
			paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
			List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
			BigDecimal warehouseNum = BigDecimal.ZERO;
			if(numList != null && numList.size() > 0){
				warehouseNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
			}
			BigDecimal amount = warehouseNum.multiply(itemModel.getPrice() == null ? BigDecimal.ZERO : itemModel.getPrice());
			BigDecimal rate = new BigDecimal(itemModel.getTaxRate() == null ? 0 : itemModel.getTaxRate()).add(BigDecimal.ONE);//含税单价 = 不含税单价*（1+税率）
			BigDecimal taxPrice = itemModel.getPrice() == null ? BigDecimal.ZERO : itemModel.getPrice().multiply(rate).setScale(8, BigDecimal.ROUND_HALF_UP);
			BigDecimal taxAmount = warehouseNum.multiply(taxPrice);
			BigDecimal tax = taxAmount.subtract(amount);
			//封装应收明细
			PaymentItemModel paymentItem = new PaymentItemModel() ;
            paymentItem.setPurchaseCode(itemModel.getOrderCode());
            paymentItem.setPurchaseId(itemModel.getPurchaseOrderId());
            paymentItem.setGoodsId(itemModel.getGoodsId());
            paymentItem.setGoodsName(itemModel.getGoodsName());
            paymentItem.setGoodsNo(itemModel.getGoodsNo());
            paymentItem.setNum(warehouseNum.intValue());
            paymentItem.setPurchaseAmount(amount);
            paymentItem.setPurchaseTaxAmount(taxAmount);
            paymentItem.setTax(tax);
            paymentItem.setPoNo(itemModel.getPoNo());
            paymentItem.setSettlementAmount(amount);
            paymentItem.setSettlementTax(tax);

            if(settlementConfigModel != null) {
            	paymentItem.setProjectId(settlementConfigModel.getId());
            	paymentItem.setProjectName(settlementConfigModel.getProjectName());
            }
            //记录费用明细
            itemList.add(paymentItem);

            //封装汇总
            PaymentSummaryModel paymentSummaryModel = summaryMap.get(settlementConfigModel.getId());
            if (paymentSummaryModel == null) {
            	paymentSummaryModel = new PaymentSummaryModel();

                if (settlementConfigModel != null) {
                	paymentSummaryModel.setProjectId(settlementConfigModel.getId());
                	paymentSummaryModel.setProjectName(settlementConfigModel.getProjectName());
                	paymentSummaryModel.setParentProjectId(settlementConfigModel.getParentId());
                	paymentSummaryModel.setParentProjectName(settlementConfigModel.getParentProjectName());
                }

                paymentSummaryModel.setSettlementAmount(new BigDecimal(0));
                paymentSummaryModel.setSettlementTaxAmount(new BigDecimal(0));
                paymentSummaryModel.setCurrency(purchaseOrderDTO.getCurrency());
                paymentSummaryModel.setTax(new BigDecimal(0));
            }

            BigDecimal settlementAmount = paymentSummaryModel.getSettlementAmount();
            BigDecimal settlementTaxAmount = paymentSummaryModel.getSettlementTaxAmount();
            BigDecimal settelTax = paymentSummaryModel.getTax();

            settlementAmount = settlementAmount.add(amount);
            settlementTaxAmount = settlementTaxAmount.add(taxAmount);
            settelTax = settelTax.add(tax);

            paymentSummaryModel.setSettlementAmount(settlementAmount);
            paymentSummaryModel.setSettlementTaxAmount(settlementTaxAmount);
            paymentSummaryModel.setTax(settelTax);

            summaryMap.put(settlementConfigModel.getId(), paymentSummaryModel);
        }

		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("itemList",itemList) ;
		resultMap.put("summaryMap",new ArrayList<>(summaryMap.values()));

		return resultMap;
	}
	/**
	 * 获取核销明细集合
	 * @param purchaseOrderDTO
	 * @return
	 * @throws Exception
	 */
	private List<PaymentVerificateItemModel> getPaymentVerificateList(PurchaseOrderDTO purchaseOrderDTO) throws Exception{
		List<PaymentVerificateItemModel> paymentVerificateList = new ArrayList<PaymentVerificateItemModel>();//记录当前核销集合
		/**根据采购订单查询对应预付单*/
		AdvancePaymentBillRelModel relModel = new AdvancePaymentBillRelModel() ;
        relModel.setPurchaseOrderId(purchaseOrderDTO.getId());
        relModel = advancePaymentBillRelDao.searchByModel(relModel) ;

        if(relModel == null) {
        	return new ArrayList<PaymentVerificateItemModel>();
        }
        //采购订单关联预付款单不存在 或者待核销金额为空 或者待核销金额小于等于0, 不需要关联
        AdvancePaymentBillItemModel queryAdvancePaymentItemModel = new AdvancePaymentBillItemModel() ;
        queryAdvancePaymentItemModel.setAdvancePaymentId(relModel.getAdvancePaymentId());
        queryAdvancePaymentItemModel.setPurchaseId(purchaseOrderDTO.getId());
        queryAdvancePaymentItemModel = advancePaymentBillItemDao.searchByModel(queryAdvancePaymentItemModel) ;
        if(queryAdvancePaymentItemModel == null
        		|| queryAdvancePaymentItemModel.getVerificateAmount() == null
        		|| queryAdvancePaymentItemModel.getVerificateAmount().compareTo(new BigDecimal(0)) <= 0) {
        	return new ArrayList<PaymentVerificateItemModel>();
        }

        AdvancePaymentBillModel advancePayment = advancePaymentBillDao.searchById(queryAdvancePaymentItemModel.getAdvancePaymentId());
        //预付款单不为“待核销” 不需要关联
        if(!advancePayment.getBillStatus().equals(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_06)) {
        	return new ArrayList<PaymentVerificateItemModel>();
        }
        //封装应付核销记录
        PaymentVerificateItemModel veriItemModel = new PaymentVerificateItemModel() ;
        veriItemModel.setBillStatus(DERP_ORDER.PAYMENT_BILL_VERIFICATE_BILL_STATUS_1);
        veriItemModel.setRelCode(advancePayment.getCode());
        veriItemModel.setVerificateAmount(queryAdvancePaymentItemModel.getVerificateAmount());

        AdvancePaymentRecordItemModel queryRecodrItemModel = new AdvancePaymentRecordItemModel() ;
        queryAdvancePaymentItemModel.setAdvancePaymentId(advancePayment.getId());
        List<AdvancePaymentRecordItemModel> recordList = advancePaymentRecordItemDao.list(queryRecodrItemModel);

        if(recordList != null && !recordList.isEmpty()) {
        	queryRecodrItemModel = recordList.get(0) ;

        	if(queryRecodrItemModel != null) {
        		veriItemModel.setDrawee(queryRecodrItemModel.getCreatorName());
        		veriItemModel.setDraweeId(queryRecodrItemModel.getCreatorId());
        		veriItemModel.setPaymentDate(queryRecodrItemModel.getPaymentDate());
        		veriItemModel.setSerialNo(queryRecodrItemModel.getSerialNo());
        	}

        }
        paymentVerificateList.add(veriItemModel);

        return paymentVerificateList;
	}
	/**
	 * 封装应付账单表头信息
	 * @param purchaseOrderDTO
	 * @param totalAmount
	 * @param veriTotalAmount
	 * @return
	 * @throws Exception
	 */
	private PaymentBillModel getPayBillModel(PurchaseOrderDTO purchaseOrderDTO, BigDecimal totalAmount, BigDecimal veriTotalAmount) throws Exception{
		Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("customerid", purchaseOrderDTO.getSupplierId());
        customerParams.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1);
        CustomerInfoMogo supplier = customerInfoMongoDao.findOne(customerParams);
        //封装应付订单表头
        PaymentBillModel payBillModel = new PaymentBillModel() ;

        payBillModel.setCode(SmurfsUtils.getID(DERP.UNIQUE_ID_YFZD));
        payBillModel.setSupplierId(purchaseOrderDTO.getSupplierId());//供应商id
        payBillModel.setSupplierName(supplier.getName());//供应商名称
//        payBillModel.setBankAccount(supplier.getBankAccount());//供应商银行账号
//        payBillModel.setBankAddress(supplier.getBankAddress());//供应商开户行地址
//        payBillModel.setDepositBank(supplier.getDepositBank());//供应商开户银行
//        payBillModel.setBeneficiaryName(supplier.getBeneficiaryName());//供应商银行账户
//        payBillModel.setSwiftCode(supplier.getSwiftCode());//swiftCode
        payBillModel.setPayableAmount(totalAmount);//应付总额
        payBillModel.setPaymentAmount(totalAmount.subtract(veriTotalAmount));//已付总额
        payBillModel.setSurplusAmount(veriTotalAmount);//待付款金额
        payBillModel.setMerchantId(purchaseOrderDTO.getMerchantId());
        payBillModel.setMerchantName(purchaseOrderDTO.getMerchantName());
        payBillModel.setBuId(purchaseOrderDTO.getBuId());//事业部ID
        payBillModel.setBuName(purchaseOrderDTO.getBuName());//事业部名称
        payBillModel.setCurrency(purchaseOrderDTO.getCurrency());//currency
        payBillModel.setCreater("系统创建");
        payBillModel.setCreateDate(TimeUtils.getNow());
        payBillModel.setBillStatus(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_00);
        payBillModel.setNcStatus(DERP_ORDER.PAYMENT_BILL_NCSTATUS_10);
        payBillModel.setBillDate(purchaseOrderDTO.getAttributionDate());

        return payBillModel;
	}
}
