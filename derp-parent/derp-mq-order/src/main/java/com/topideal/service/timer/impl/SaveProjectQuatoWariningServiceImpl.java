package com.topideal.service.timer.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.PaymentBillDao;
import com.topideal.dao.bill.PaymentCostItemDao;
import com.topideal.dao.bill.ReceiveBillCostItemDao;
import com.topideal.dao.bill.ReceiveBillDao;
import com.topideal.dao.bill.ReceiveBillItemDao;
import com.topideal.dao.bill.ReceiveBillVerifyItemDao;
import com.topideal.dao.bill.TocSettlementReceiveBillCostItemDao;
import com.topideal.dao.bill.TocSettlementReceiveBillItemDao;
import com.topideal.dao.common.QuotaPeriodConfigDao;
import com.topideal.dao.purchase.ProjectQuotaConfigDao;
import com.topideal.dao.purchase.ProjectQuotaWarningDao;
import com.topideal.dao.purchase.ProjectQuotaWarningItemDao;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.entity.vo.common.QuotaPeriodConfigModel;
import com.topideal.entity.vo.purchase.ProjectQuotaConfigModel;
import com.topideal.entity.vo.purchase.ProjectQuotaWarningItemModel;
import com.topideal.entity.vo.purchase.ProjectQuotaWarningModel;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.dao.CommbarcodeMongoDao;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.ExchangeRateMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.mongo.entity.CommbarcodeMongo;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.ExchangeRateMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.timer.SaveProjectQuatoWariningService;

import net.sf.json.JSONObject;

@Service
public class SaveProjectQuatoWariningServiceImpl implements SaveProjectQuatoWariningService {

    private static final String Label = null;
	@Autowired
    private ProjectQuotaConfigDao projectQuotaConfigDao ;
    @Autowired
    private ProjectQuotaWarningDao projectQuotaWarningDao ;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao ;
    @Autowired
    private CommbarcodeMongoDao commbarcodeMongoDao ;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao ;
    @Autowired
    private PurchaseOrderDao purchaseOrderDao ;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao ;
    @Autowired
    private ExchangeRateMongoDao exchangeRateMongoDao ;
    @Autowired
    private ProjectQuotaWarningItemDao projectQuotaWarningItemDao ;
    @Autowired
    private ReceiveBillItemDao receiveBillItemDao ;
    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao ;
    @Autowired
    private TocSettlementReceiveBillItemDao tocSettlementReceiveBillItemDao ;
    @Autowired
    private TocSettlementReceiveBillCostItemDao tocSettlementReceiveBillCostItemDao ;
    @Autowired
    private  PaymentBillDao paymentBillDao;
    @Autowired
    private  PaymentCostItemDao paymentCostItemDao; 
    @Autowired
    private QuotaPeriodConfigDao quotaPeriodConfigDao ;
	

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201110002,model=DERP_LOG_POINT.POINT_13201110002_Label)
    public void saveProjectQuatoWariningService(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonObject = JSONObject.fromObject(json);
        Long buId = null ;
        if(jsonObject.get("buId") != null)buId = jsonObject.getLong("buId") ;
        Long superiorBrandId = null ;
        if(jsonObject.get("superiorBrandId") != null)superiorBrandId = jsonObject.getLong("superiorBrandId") ;
        String currentDate = null ;
        if(jsonObject.get("date") != null) currentDate = jsonObject.getString("date");
        /**
         * 获取最近生效项目额度配置
         */
        Map<String, Object> queryMap = new HashMap<>() ;
        queryMap.put("superiorParentBrandId", superiorBrandId) ;
        queryMap.put("buId", buId) ;
        queryMap.put("currentDate", currentDate) ;
        List<ProjectQuotaConfigModel> configModelList = projectQuotaConfigDao.getLatestConfigList(queryMap) ;
        for (ProjectQuotaConfigModel configModel : configModelList) {
        	Timestamp effectiveDate = configModel.getEffectiveDate();
        	Timestamp expirationDate = configModel.getExpirationDate();
        	Timestamp now = TimeUtils.getNow();
        	// 当前时间要不在生效日期和失效日期之间 不进行生成数据 
        	if (!(expirationDate.getTime()>now.getTime()&&now.getTime()>effectiveDate.getTime())) {
				continue;
			}
        	
        	// 删除表头和表体信息
        	delProjectQuotaConfig(configModel);         
            // 累计采购金额                                   
            queryMap.clear() ;
            queryMap.put("superiorParentBrandId", configModel.getSuperiorParentBrandId()) ;
            List<BrandParentMongo> parentMongoDaoAll = brandParentMongoDao.findAll(queryMap);
            if(parentMongoDaoAll == null || parentMongoDaoAll.isEmpty()){
                continue;
            }            
            //查询期初信息
            QuotaPeriodConfigModel queryQuotaPeriodModel = new QuotaPeriodConfigModel() ;
            queryQuotaPeriodModel.setBuId(configModel.getBuId());
            queryQuotaPeriodModel.setConfigObjectId(configModel.getSuperiorParentBrandId());
            queryQuotaPeriodModel.setQuotaType(DERP_ORDER.QUOTACONFIG_quotaType_1);            
            queryQuotaPeriodModel = quotaPeriodConfigDao.searchByModel(queryQuotaPeriodModel) ;
            List<Long> idsList = new ArrayList<>() ;
            for (BrandParentMongo brandParentMongo : parentMongoDaoAll) {
                queryMap.clear() ;
                queryMap.put("commBrandParentId", brandParentMongo.getBrandParentId()) ;
                List<CommbarcodeMongo> commbarcodeMongoList = commbarcodeMongoDao.findAll(queryMap);
                if(commbarcodeMongoList == null || commbarcodeMongoList.isEmpty()){
                    continue;
                }
                for (CommbarcodeMongo commbarcodeMongo: commbarcodeMongoList) {

                    queryMap.clear();
                    queryMap.put("commbarcode", commbarcodeMongo.getCommbarcode()) ;
                    queryMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1) ;

                    /**查询对应标准条码商品*/
                    List<MerchandiseInfoMogo> merchantList = merchandiseInfoMogoDao.findAll(queryMap);

                    List<Long> tempList = merchantList.stream().map(MerchandiseInfoMogo::getMerchandiseId)
                            .collect(Collectors.toList());
                    idsList.addAll(tempList) ;
                }

            }

            /**查询内部供应商信息*/
            queryMap.clear();
            queryMap.put("type", DERP_SYS.CUSTOMERINFO_TYPE_1) ;
            queryMap.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_2) ;
            queryMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;

            List<CustomerInfoMogo> customerList = customerInfoMongoDao.findAll(queryMap);
            List<Long> customerIdList = customerList.stream().map(CustomerInfoMogo::getCustomerid)
                    .collect(Collectors.toList());

            Map<String, Object> queryOrderMap = new HashMap<>() ;
            queryOrderMap.put("buId", configModel.getBuId()) ;
            queryOrderMap.put("customerIdList", customerIdList) ;
            queryOrderMap.put("goodsIdList", idsList) ;            
            if(configModel.getEffectiveDate() != null) {
            	queryOrderMap.put("effectiveDate", configModel.getEffectiveDate() ) ;
            }             
            /**累计采购冻结金额  取采购订单金额 以“事业部+母品牌”取系统已创建的采购订单且未存在账单状态为“待付款”或“部分付款”、“已付款”的应付账单的采购数据  */
            List<Map<String, Object>> purchaseOrderList = new ArrayList<Map<String,Object>>() ;            
            if(!idsList.isEmpty()) {
            	purchaseOrderList = purchaseOrderDao.getProjectWarnList(queryOrderMap) ;
            }            
            List<ProjectQuotaWarningItemModel> itemModelList = new ArrayList<ProjectQuotaWarningItemModel>() ;           
            BigDecimal totalPurchaseAmount= getpurchaseOrderList(configModel,purchaseOrderList,itemModelList);
            //------------------------------------------------------------------------------------    
            /**以“事业部+母品牌”取系统已创建的应付账单（账单状态仅取待付款、部分付款），应付帐单的创建日期需大于对应额度记录配置中的“期初开始日期；*/           
            queryOrderMap.put("billStatusType", "1");
            List<Map<String, Object>> paymentBillList = paymentBillDao.getProjectWarnList(queryOrderMap);
            //应付账单表体(账单状态仅取待付款、部分付款)
            BigDecimal totalPaymentBillAmount = getPaymentBillList(configModel,paymentBillList,itemModelList,DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_1);           
            //应付账单费用(账单状态仅取待付款、部分付款)
            queryOrderMap.put("parentBrandId", configModel.getSuperiorParentBrandId());
            List<Map<String, Object>> paymentBillCostList = paymentCostItemDao.getProjectWarnList(queryOrderMap);
            BigDecimal totalPaymentBillCostAmount= getPaymentBillCostList(configModel,paymentBillCostList,itemModelList,DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_1);
            //累计采购冻结金额 
            BigDecimal purchaseAmount=totalPurchaseAmount.add(totalPaymentBillAmount).add(totalPaymentBillCostAmount);
            
            queryOrderMap.put("billStatusType", "2");
            //应付账单表体(账单状态仅取账单状态为“已付款)
            List<Map<String, Object>> paymentBillListFk = paymentBillDao.getProjectWarnList(queryOrderMap);
            BigDecimal totalPaymentBillAmountFK = getPaymentBillList(configModel,paymentBillListFk,itemModelList,DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_3);    
           //应付账单费用(账单状态仅取账单状态为“已付款)
            List<Map<String, Object>> paymentBillCostListFk = paymentCostItemDao.getProjectWarnList(queryOrderMap);
            BigDecimal totalPaymentBillCostAmountFk= getPaymentBillCostList(configModel,paymentBillCostListFk,itemModelList,DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_3);
            queryOrderMap.remove("billStatusType");
            //累计付款金额
            BigDecimal addPaymentAmount=totalPaymentBillAmountFK.add(totalPaymentBillCostAmountFk);
            //------------------------------------------------------------------------------------------
            /**获取应收信息*/
            /**查询内部客户信息*/
            queryMap.clear();
            queryMap.put("type", DERP_SYS.CUSTOMERINFO_TYPE_1) ;
            queryMap.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_1) ;
            queryMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;

            customerList = customerInfoMongoDao.findAll(queryMap);
            customerIdList = customerList.stream().map(CustomerInfoMogo::getCustomerid)
                    .collect(Collectors.toList());

            /**获取需要过滤掉 客户为内部公司 对应状态为 “已核销” 的应收单 的数据*/
            Map<String, Object> queryBillMap = new HashMap<>() ;
            queryBillMap.put("buId", configModel.getBuId()) ;
            queryBillMap.put("customerIdList", customerIdList) ;            
            if(configModel.getEffectiveDate() != null) {
            	queryBillMap.put("effectiveDate", configModel.getEffectiveDate() ) ;
            } 
            
            /**查询TO B结算金额*/              
            queryBillMap.put("parentBrandId", configModel.getSuperiorParentBrandId()) ;
            List<Map<String, Object>> billItemList = receiveBillItemDao.getProjectWarnList(queryBillMap) ;
            
            BigDecimal totalBillAmountTOB=getbillItemListTOB(configModel,billItemList,itemModelList);

             /**TO B查询费用金额*/
            List<Map<String, Object>> billCostItemList = receiveBillCostItemDao.getProjectWarnList(queryBillMap) ;
            BigDecimal totalBillAmountCost =getbillCostItemList(configModel,billCostItemList,itemModelList);                               
             /**查询TO C结算金额 不用过滤内部客户*/  
             queryBillMap.remove("customerIdList");
             List<Map<String, Object>> billItemListTOC = tocSettlementReceiveBillItemDao.getProjectWarnList(queryBillMap);    
             BigDecimal totalBillAmountTOC=getProjectWarnListTOC(configModel,billItemListTOC,itemModelList);
                
            /**查询TO C费用金额 不用过滤内部客户*/  
            List<Map<String, Object>> billCostItemListTOC= tocSettlementReceiveBillCostItemDao.getProjectWarnList(queryBillMap);
            BigDecimal totalBillAmountCostTOC=getProjectWarnListCostTOC(configModel,billCostItemListTOC,itemModelList);                               
            BigDecimal projectQuota = configModel.getProjectQuota();
            BigDecimal periodQuota = new BigDecimal(0);
            
            if(queryQuotaPeriodModel != null) {
            	periodQuota = queryQuotaPeriodModel.getPeriodQuota() ;
            }
            //销售已回款金额
            BigDecimal totalBillAmount=totalBillAmountTOB.add(totalBillAmountCost).add(totalBillAmountTOC).add(totalBillAmountCostTOC);
            projectQuota = projectQuota.subtract(periodQuota) ;
            //1、可用额度=项目总额 - 期初已使用额度 -累计采购冻结金额 -累计采购已付金额 + 累计销售已回款金额
            BigDecimal availableAmount = projectQuota.subtract(purchaseAmount).subtract(addPaymentAmount).add(totalBillAmount);

            ProjectQuotaWarningModel saveModel = new ProjectQuotaWarningModel() ;
            saveModel.setBuId(configModel.getBuId());
            saveModel.setBuName(configModel.getBuName());
            saveModel.setCurrency(configModel.getCurrency());
            saveModel.setSuperiorParentBrandId(configModel.getSuperiorParentBrandId());
            saveModel.setSuperiorParentBrand(configModel.getSuperiorParentBrand());
            saveModel.setProjectQuota(configModel.getProjectQuota());
            saveModel.setQuotaType(configModel.getQuotaType());
            saveModel.setPurchaseAmount(purchaseAmount);
            saveModel.setAddPaymentAmount(addPaymentAmount);
            saveModel.setSalesCollectedAmount(totalBillAmount);
            saveModel.setAvailableAmount(availableAmount);
            saveModel.setCreateDate(TimeUtils.getNow());
            saveModel.setPeriodQuota(periodQuota);

            Long warningId = projectQuotaWarningDao.save(saveModel);

            for (ProjectQuotaWarningItemModel item :itemModelList) {
                item.setWaringId(warningId);

                projectQuotaWarningItemDao.save(item) ;
            }
        }
    }
    
    /**
     * 取应付账单费用金额金额
     * @param configModel
     * @param paymentBillCostList
     * @param itemModelList
     * @return
     */
    private BigDecimal getPaymentBillCostList(ProjectQuotaConfigModel configModel,
			List<Map<String, Object>> paymentBillCostList, List<ProjectQuotaWarningItemModel> itemModelList,String type) {
    	BigDecimal totalPaymentBillCostAmount = new BigDecimal(0) ;
        for (Map<String, Object> tempMap: paymentBillCostList) {
 
            String code = judgeIsNullOrNotReturnObj(tempMap.get("code"), String.class);
            String currency = judgeIsNullOrNotReturnObj(tempMap.get("currency"), String.class);
            //BigDecimal num = judgeIsNullOrNotReturnObj(tempMap.get("num"), BigDecimal.class);
            BigDecimal amount = judgeIsNullOrNotReturnObj(tempMap.get("cost_amount"), BigDecimal.class);
            Long merchantId = judgeIsNullOrNotReturnObj(tempMap.get("merchant_id"), Long.class);
            String merchantName = judgeIsNullOrNotReturnObj(tempMap.get("merchant_name"), String.class);
            Timestamp createDate = (Timestamp) tempMap.get("create_date");
            Long supplierId = (Long) tempMap.get("supplier_id");
            String supplierName = (String) tempMap.get("supplier_name");
            String status = (String) tempMap.get("bill_status");
            String poNo = (String) tempMap.get("po_no");
            // 获取订单名称
            String statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.paymentBill_billStatusList, status);
            // 获取汇率 
            Map<String, Object>rateMap =getRate(configModel,currency,createDate); 
            Double rate = (Double) rateMap.get("rate");
            Timestamp effectiveDate = (Timestamp) rateMap.get("effectiveDate");
            BigDecimal paymentBillAmount = amount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP) ;

            ProjectQuotaWarningItemModel itemModel = new ProjectQuotaWarningItemModel() ;
            itemModel.setMerchantId(merchantId);
            itemModel.setMerchantName(merchantName);
            itemModel.setType(type);
            itemModel.setPoNo(poNo);
            itemModel.setCode(code);
            //itemModel.setNum(num.intValue());
            itemModel.setAmount(amount);
            itemModel.setCurrency(currency);
            itemModel.setRate(rate);
            itemModel.setRateDate(effectiveDate);
            itemModel.setCreateDate(TimeUtils.getNow());
            itemModel.setOccupationAmount(paymentBillAmount);
            itemModel.setCustomerId(supplierId);
            itemModel.setCustomerName(supplierName);
            //itemModel.setOrderType(orderType);                
            itemModel.setStatusName(statusLabel);
            itemModel.setOrderCreateDate(createDate);
            itemModelList.add(itemModel) ;

            totalPaymentBillCostAmount = totalPaymentBillCostAmount.add(paymentBillAmount) ;
        }
		return totalPaymentBillCostAmount;
	}

	/**
     * 取应付账单表体金额
     * @param configModel
     * @param paymentBillList
     * @param itemModelList
     */
    private BigDecimal getPaymentBillList(ProjectQuotaConfigModel configModel, List<Map<String, Object>> paymentBillList,
			List<ProjectQuotaWarningItemModel> itemModelList,String type) {
    	BigDecimal totalPaymentBillAmount = new BigDecimal(0) ;
        for (Map<String, Object> tempMap: paymentBillList) {
 
            String code = judgeIsNullOrNotReturnObj(tempMap.get("code"), String.class);
            String currency = judgeIsNullOrNotReturnObj(tempMap.get("currency"), String.class);
            BigDecimal num = judgeIsNullOrNotReturnObj(tempMap.get("num"), BigDecimal.class);
            BigDecimal amount = judgeIsNullOrNotReturnObj(tempMap.get("settlement_amount"), BigDecimal.class);
            Long merchantId = judgeIsNullOrNotReturnObj(tempMap.get("merchant_id"), Long.class);
            String merchantName = judgeIsNullOrNotReturnObj(tempMap.get("merchant_name"), String.class);
            Timestamp createDate = (Timestamp) tempMap.get("create_date");
            Long supplierId = (Long) tempMap.get("supplier_id");
            String supplierName = (String) tempMap.get("supplier_name");
            String status = (String) tempMap.get("bill_status");
            String poNo = (String) tempMap.get("po_no");
            // 获取订单名称
            String statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.paymentBill_billStatusList, status);
            // 获取汇率 
            Map<String, Object>rateMap =getRate(configModel,currency,createDate); 
            Double rate = (Double) rateMap.get("rate");
            Timestamp effectiveDate = (Timestamp) rateMap.get("effectiveDate");
            BigDecimal paymentBillAmount = amount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP) ;

            ProjectQuotaWarningItemModel itemModel = new ProjectQuotaWarningItemModel() ;
            itemModel.setMerchantId(merchantId);
            itemModel.setMerchantName(merchantName);
            itemModel.setType(type);
            itemModel.setPoNo(poNo);
            itemModel.setCode(code);
            itemModel.setNum(num.intValue());
            itemModel.setAmount(amount);
            itemModel.setCurrency(currency);
            itemModel.setRate(rate);
            itemModel.setRateDate(effectiveDate);
            itemModel.setCreateDate(TimeUtils.getNow());
            itemModel.setOccupationAmount(paymentBillAmount);
            itemModel.setCustomerId(supplierId);
            itemModel.setCustomerName(supplierName);
            //itemModel.setOrderType(orderType);                
            itemModel.setStatusName(statusLabel);
            itemModel.setOrderCreateDate(createDate);
            itemModelList.add(itemModel) ;

            totalPaymentBillAmount = totalPaymentBillAmount.add(paymentBillAmount) ;
        }
		return totalPaymentBillAmount;
		
	}
	/**
     * TOB费用金额
     * @param configModel
     * @param billCostItemList
     * @param itemModelList
     * @return
     */
    private BigDecimal getbillCostItemList(ProjectQuotaConfigModel configModel,
			List<Map<String, Object>> billCostItemList, List<ProjectQuotaWarningItemModel> itemModelList) {
    		
    	BigDecimal totalBillAmount = new BigDecimal(0) ;
    	for (Map<String, Object> tempMap : billCostItemList) {
             String code = judgeIsNullOrNotReturnObj(tempMap.get("code"), String.class);
             String currency = judgeIsNullOrNotReturnObj(tempMap.get("currency"), String.class);
             BigDecimal amount = judgeIsNullOrNotReturnObj(tempMap.get("amount"), BigDecimal.class);
             Long merchantId = judgeIsNullOrNotReturnObj(tempMap.get("merchant_id"), Long.class);
             String merchantName = judgeIsNullOrNotReturnObj(tempMap.get("merchant_name"), String.class);
             Timestamp createDate = (Timestamp) tempMap.get("create_date");
             Long customerId = (Long) tempMap.get("customer_id");
             String customerName = (String) tempMap.get("customer_name");
             String billStatus = (String) tempMap.get("bill_status");
             String billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_billStatusList, billStatus); 
             if(amount.compareTo(new BigDecimal(0)) == 0){
                 continue;
             }

          // 获取汇率 
             Map<String, Object>rateMap =getRate(configModel,currency,createDate); 
             Double rate = (Double) rateMap.get("rate");
             Timestamp effectiveDate = (Timestamp) rateMap.get("effectiveDate");
             BigDecimal occupationAmount = amount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP) ;

             ProjectQuotaWarningItemModel itemModel = new ProjectQuotaWarningItemModel() ;
             itemModel.setMerchantId(merchantId);
             itemModel.setMerchantName(merchantName);
             itemModel.setType(DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_2);
             itemModel.setReceiveType("2");
             itemModel.setCode(code);
             itemModel.setAmount(amount);
             itemModel.setCurrency(currency);
             itemModel.setRate(rate);
             itemModel.setRateDate(effectiveDate);
             itemModel.setCreateDate(TimeUtils.getNow());
             itemModel.setOccupationAmount(occupationAmount);
             itemModel.setCustomerId(customerId);
             itemModel.setCustomerName(customerName);
             itemModel.setOrderType("TO B");                
             itemModel.setStatusName(billStatusLabel);
             itemModel.setOrderCreateDate(createDate);
             itemModelList.add(itemModel) ;

             totalBillAmount = totalBillAmount.add(occupationAmount) ;
         }
		return totalBillAmount;
	}
	/**
     * 获取tob结算金额
     * @param configModel
     * @param billItemList
     * @param itemModelList
     * @return
     */
    private BigDecimal getbillItemListTOB(ProjectQuotaConfigModel configModel, List<Map<String, Object>> billItemList,
			List<ProjectQuotaWarningItemModel> itemModelList) {
    	BigDecimal totalBillAmount = new BigDecimal(0) ;
    	for (Map<String, Object> tempMap : billItemList) {

            String code = judgeIsNullOrNotReturnObj(tempMap.get("code"), String.class);
            String currency = judgeIsNullOrNotReturnObj(tempMap.get("currency"), String.class);
            BigDecimal num = judgeIsNullOrNotReturnObj(tempMap.get("num"), BigDecimal.class);
            BigDecimal amount = judgeIsNullOrNotReturnObj(tempMap.get("amount"), BigDecimal.class);
            Long merchantId = judgeIsNullOrNotReturnObj(tempMap.get("merchant_id"), Long.class);
            String merchantName = judgeIsNullOrNotReturnObj(tempMap.get("merchant_name"), String.class);
            Timestamp createDate = (Timestamp) tempMap.get("create_date");
            Long customerId = (Long) tempMap.get("customer_id");
            String customerName = (String) tempMap.get("customer_name");
            String billStatus = (String) tempMap.get("bill_status");
            String billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_billStatusList, billStatus);
            // 获取汇率 
            Map<String, Object>rateMap =getRate(configModel,currency,createDate); 
            Double rate = (Double) rateMap.get("rate");
            Timestamp effectiveDate = (Timestamp) rateMap.get("effectiveDate");
            BigDecimal occupationAmount = amount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP) ;

            ProjectQuotaWarningItemModel itemModel = new ProjectQuotaWarningItemModel() ;
            itemModel.setMerchantId(merchantId);
            itemModel.setMerchantName(merchantName);
            itemModel.setType(DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_2);
            itemModel.setReceiveType("1");
            itemModel.setCode(code);
            itemModel.setNum(num.intValue());
            itemModel.setAmount(amount);
            itemModel.setCurrency(currency);
            itemModel.setRate(rate);
            itemModel.setRateDate(effectiveDate);
            itemModel.setCreateDate(TimeUtils.getNow());
            itemModel.setOccupationAmount(occupationAmount);
            itemModel.setCustomerId(customerId);
            itemModel.setCustomerName(customerName);
            itemModel.setOrderType("TO B");                
            itemModel.setStatusName(billStatusLabel);
            itemModel.setOrderCreateDate(createDate);
            itemModelList.add(itemModel) ;

            totalBillAmount = totalBillAmount.add(occupationAmount) ;
        }
		return totalBillAmount;
	}
	/**
     * 获取采购金额
     * @param configModel
     * @param purchaseOrderList
     * @param itemModelList
     * @return
     */
    private BigDecimal getpurchaseOrderList(ProjectQuotaConfigModel configModel,
			List<Map<String, Object>> purchaseOrderList, List<ProjectQuotaWarningItemModel> itemModelList) {
    	 BigDecimal totalPurchaseAmount = new BigDecimal(0) ;
         for (Map<String, Object> tempMap: purchaseOrderList) {
             String code = judgeIsNullOrNotReturnObj(tempMap.get("code"), String.class);
             String poNo = judgeIsNullOrNotReturnObj(tempMap.get("po_no"), String.class);
             String currency = judgeIsNullOrNotReturnObj(tempMap.get("currency"), String.class);
             BigDecimal num = judgeIsNullOrNotReturnObj(tempMap.get("num"), BigDecimal.class);
             BigDecimal amount = judgeIsNullOrNotReturnObj(tempMap.get("amount"), BigDecimal.class);
             Long merchantId = judgeIsNullOrNotReturnObj(tempMap.get("merchant_id"), Long.class);
             String merchantName = judgeIsNullOrNotReturnObj(tempMap.get("merchant_name"), String.class);
             Timestamp createDate = (Timestamp) tempMap.get("create_date");
             Long supplierId = (Long) tempMap.get("supplier_id");
             String supplierName = (String) tempMap.get("supplier_name");
             String status = (String) tempMap.get("status");
             // 获取订单名称
             String statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_statusList, status);
             // 获取汇率 
             Map<String, Object>rateMap =getRate(configModel,currency,createDate); 
             Double rate = (Double) rateMap.get("rate");
             Timestamp effectiveDate = (Timestamp) rateMap.get("effectiveDate");
             BigDecimal occupationAmount = amount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP) ;

             ProjectQuotaWarningItemModel itemModel = new ProjectQuotaWarningItemModel() ;
             itemModel.setMerchantId(merchantId);
             itemModel.setMerchantName(merchantName);
             itemModel.setType(DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_1);
             itemModel.setPoNo(poNo);
             itemModel.setCode(code);
             itemModel.setNum(num.intValue());
             itemModel.setAmount(amount);
             itemModel.setCurrency(currency);
             itemModel.setRate(rate);
             itemModel.setRateDate(effectiveDate);
             itemModel.setCreateDate(TimeUtils.getNow());
             itemModel.setOccupationAmount(occupationAmount);
             itemModel.setCustomerId(supplierId);
             itemModel.setCustomerName(supplierName);
             //itemModel.setOrderType(orderType);                
             itemModel.setStatusName(statusLabel);
             itemModel.setOrderCreateDate(createDate);
             itemModelList.add(itemModel) ;

             totalPurchaseAmount = totalPurchaseAmount.add(occupationAmount) ;
         }
		return totalPurchaseAmount;
	}
	/**
     * 获取TOC费用金额
     * @param configModel
     * @param billItemList
     * @param itemModelList
     * @return
     */
    private BigDecimal getProjectWarnListCostTOC(ProjectQuotaConfigModel configModel,
			List<Map<String, Object>> billCostItemListTOC, List<ProjectQuotaWarningItemModel> itemModelList) {
    	BigDecimal totalBillAmount = new BigDecimal(0) ;
    	for (Map<String, Object> tempMap : billCostItemListTOC) {

            String code = judgeIsNullOrNotReturnObj(tempMap.get("code"), String.class);
            String currency = judgeIsNullOrNotReturnObj(tempMap.get("settlement_currency"), String.class);
            BigDecimal amount = judgeIsNullOrNotReturnObj(tempMap.get("rmb_amount"), BigDecimal.class);
            Long merchantId = judgeIsNullOrNotReturnObj(tempMap.get("merchant_id"), Long.class);
            String merchantName = judgeIsNullOrNotReturnObj(tempMap.get("merchant_name"), String.class);
            Timestamp createDate = (Timestamp) tempMap.get("create_date");
            Long customerId = (Long) tempMap.get("customer_id");
            String customerName = (String) tempMap.get("customer_name");
            String billStatus = (String) tempMap.get("bill_status");
            String billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocReceiveBill_billStatusList, billStatus); 
            if(amount.compareTo(new BigDecimal(0)) == 0){
                continue;
            }
            
            // 获取汇率 
            Map<String, Object>rateMap =getRate(configModel,currency,createDate); 
            Double rate = (Double) rateMap.get("rate");
            Timestamp effectiveDate = (Timestamp) rateMap.get("effectiveDate");
            BigDecimal occupationAmount = amount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP) ;

            ProjectQuotaWarningItemModel itemModel = new ProjectQuotaWarningItemModel() ;
            itemModel.setMerchantId(merchantId);
            itemModel.setMerchantName(merchantName);
            itemModel.setType(DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_2);
            itemModel.setReceiveType("2");
            itemModel.setCode(code);
            itemModel.setAmount(amount);
            itemModel.setCurrency(currency);
            itemModel.setRate(rate);
            itemModel.setRateDate(effectiveDate);
            itemModel.setCreateDate(TimeUtils.getNow());
            itemModel.setOccupationAmount(occupationAmount);
            itemModel.setCustomerId(customerId);
            itemModel.setCustomerName(customerName);
            itemModel.setOrderType("TO C");                
            itemModel.setStatusName(billStatusLabel);
            itemModel.setOrderCreateDate(createDate);
            itemModelList.add(itemModel) ;

            totalBillAmount = totalBillAmount.add(occupationAmount) ;
        }
        
		return totalBillAmount;
	}
	/**
     * 获取TOC 结算金额
     * @param configModel
     * @param billItemList
     * @param itemModelList
     * @return
     */
    private BigDecimal getProjectWarnListTOC(ProjectQuotaConfigModel configModel,
			List<Map<String, Object>> billItemListTOC, List<ProjectQuotaWarningItemModel> itemModelList) {
    	BigDecimal totalBillAmount = new BigDecimal(0) ;
    	for (Map<String, Object> tempMap : billItemListTOC) {
            String code = judgeIsNullOrNotReturnObj(tempMap.get("code"), String.class);
            String currency = judgeIsNullOrNotReturnObj(tempMap.get("settlement_currency"), String.class);
            BigDecimal num = judgeIsNullOrNotReturnObj(tempMap.get("num"), BigDecimal.class);
            BigDecimal amount = judgeIsNullOrNotReturnObj(tempMap.get("rmb_amount"), BigDecimal.class);
            Long merchantId = judgeIsNullOrNotReturnObj(tempMap.get("merchant_id"), Long.class);
            String merchantName = judgeIsNullOrNotReturnObj(tempMap.get("merchant_name"), String.class);
            Timestamp createDate = (Timestamp) tempMap.get("create_date");
            Long customerId = (Long) tempMap.get("customer_id");
            String customerName = (String) tempMap.get("customer_name");
            String billStatus = (String) tempMap.get("bill_status");
            String billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocReceiveBill_billStatusList, billStatus);
          
            
            // 获取汇率 
            Map<String, Object>rateMap =getRate(configModel,currency,createDate); 
            Double rate = (Double) rateMap.get("rate");
            Timestamp effectiveDate = (Timestamp) rateMap.get("effectiveDate");
            BigDecimal occupationAmount = amount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP) ;

            ProjectQuotaWarningItemModel itemModel = new ProjectQuotaWarningItemModel() ;
            itemModel.setMerchantId(merchantId);
            itemModel.setMerchantName(merchantName);
            itemModel.setType(DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_2);
            itemModel.setReceiveType("1");
            itemModel.setCode(code);
            itemModel.setNum(num.intValue());
            itemModel.setAmount(amount);
            itemModel.setCurrency(currency);
            itemModel.setRate(rate);
            itemModel.setRateDate(effectiveDate);
            itemModel.setCreateDate(TimeUtils.getNow());
            itemModel.setOccupationAmount(occupationAmount);
            itemModel.setCustomerId(customerId);
            itemModel.setCustomerName(customerName);
            itemModel.setOrderType("TO C");                
            itemModel.setStatusName(billStatusLabel);
            itemModel.setOrderCreateDate(createDate);
            itemModelList.add(itemModel) ;

            totalBillAmount = totalBillAmount.add(occupationAmount) ;
        }
		return totalBillAmount;
	}
	/**
     * 获取汇率
     * @param configModel
     * @param currency
     * @param createDate
     * @return
     */
    private Map<String, Object> getRate(ProjectQuotaConfigModel configModel, String currency, Timestamp date) {
    	
    	Map<String, Object>rateMap=new HashMap<>();    	
    	if(!configModel.getCurrency().equals(currency)){
            // 当月的
            String month = TimeUtils.formatMonth(date); 
            // 当月最后一天数据
            String effectiveDate = TimeUtils.getLastDayOfMonth(month);
            Map<String, Object> queryRateMap = new HashMap<>() ;
            queryRateMap.put("origCurrencyCode", currency) ;
            queryRateMap.put("tgtCurrencyCode", configModel.getCurrency()) ;
            queryRateMap.put("effectiveDate", effectiveDate) ;
            queryRateMap.put("status", "1") ;

            ExchangeRateMongo rateMongo = exchangeRateMongoDao.findLastRateByParams(queryRateMap);
            if(rateMongo == null){
                throw new DerpException("没有找到汇率，原币：" + currency + " 本币：" + configModel.getCurrency()) ;
            }
            Double rate = rateMongo.getAvgRate();
            String effectiveDateStr = rateMongo.getEffectiveDate();
            Timestamp effectiveDateTime = TimeUtils.parse(effectiveDateStr,"yyyy-MM-dd");
            rateMap.put("effectiveDate", effectiveDateTime);
            rateMap.put("rate", rate);

        }else{
        	Double rate = 1.00 ;
        	rateMap.put("rate", rate);
        }
		return rateMap;
	}
	/**
     * 删除表头和表体信息
     * @param configModel
     * @throws Exception 
     */
    private void delProjectQuotaConfig(ProjectQuotaConfigModel configModel) throws Exception {
    	 ProjectQuotaWarningModel queryModel = new ProjectQuotaWarningModel() ;
         queryModel.setBuId(configModel.getBuId());
         queryModel.setSuperiorParentBrandId(configModel.getSuperiorParentBrandId());
         queryModel = projectQuotaWarningDao.searchByModel(queryModel) ;            
         // 删除表头和表体数据
         if(queryModel != null){
             Long warningId = queryModel.getId();
             ProjectQuotaWarningItemModel queryItemModel = new ProjectQuotaWarningItemModel() ;
             queryItemModel.setWaringId(warningId);
             List<ProjectQuotaWarningItemModel> itemList = projectQuotaWarningItemDao.list(queryItemModel);
             if(!itemList.isEmpty()){
                 List<Long> ids = itemList.stream().map(ProjectQuotaWarningItemModel::getId).collect(Collectors.toList());

                 projectQuotaWarningItemDao.delete(ids) ;
             }
             List<Long> warningList = new ArrayList<>() ;
             warningList.add(warningId) ;
             projectQuotaWarningDao.delete(warningList) ;
         }
		
	}

	@SuppressWarnings("unchecked")
	private <T>T judgeIsNullOrNotReturnObj(Object obj , Class<T> clazz){
        if(obj == null) {
            return null ;
        }

        return (T)obj ;
    }
}
