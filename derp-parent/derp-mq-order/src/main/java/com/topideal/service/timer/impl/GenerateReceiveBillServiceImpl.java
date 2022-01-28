package com.topideal.service.timer.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.dao.common.FileTempDao;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.dao.sale.SaleOrderItemDao;
import com.topideal.dao.sale.SaleReturnOrderItemDao;
import com.topideal.dao.sale.SaleShelfDao;
import com.topideal.dao.sale.ShelfDao;
import com.topideal.entity.dto.bill.AdvanceBillItemDTO;
import com.topideal.entity.dto.common.FileTempDTO;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.entity.vo.sale.SaleReturnOrderItemModel;
import com.topideal.entity.vo.sale.SaleShelfModel;
import com.topideal.entity.vo.sale.ShelfModel;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.service.timer.GenerateReceiveBillService;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.BaseUtils.Operator;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Description: 自动生成应收账单
 * @Author: Chen Yiluan
 * @Date: 2021/06/21 10:09
 **/
@Service
public class GenerateReceiveBillServiceImpl implements GenerateReceiveBillService {

    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private CustomerMerchantRelMongoDao customerMerchantRelMongoDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private ShelfDao shelfDao;
    @Autowired
    private SaleShelfDao saleShelfDao;
    @Autowired
    private SaleOrderItemDao saleOrderItemDao;
    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private ReceiveBillItemDao receiveBillItemDao;
    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao;
    @Autowired
    private SettlementConfigDao settlementConfigDao;
    @Autowired
    private BrandMongoDao brandMongoDao;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;
    @Autowired
    private ReceivePaymentSubjectDao receivePaymentSubjectDao;
    @Autowired
    private SaleReturnOrderItemDao saleReturnOrderItemDao;
    @Autowired
    private AdvanceBillItemDao advanceBillItemDao;
    @Autowired
    private AdvanceBillOperateItemDao advanceBillOperateItemDao;
    @Autowired
    private AdvanceBillVerifyItemDao advanceBillVerifyItemDao;
    @Autowired
    private ReceiveBillVerifyItemDao receiveBillVerifyItemDao;
    @Autowired
    private TobTemporaryReceiveBillDao tobTemporaryReceiveBillDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private FileTempDao fileTempDao;
    @Autowired
    private ReceiveBillOperateDao receiveBillOperateDao;
    @Autowired
    private ReceiveExternalCodeDao receiveExternalCodeDao ;


    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201160011, model = DERP_LOG_POINT.POINT_13201160011_Label)
    public List<String> saveReceiveBill(String json, String keys, String topics, String tags) throws Exception {

        JSONObject jsonData = JSONObject.fromObject(json);
        String month = jsonData.getString("month");
        Integer merchantId = (Integer) jsonData.get("merchantId");
        String merchantName = jsonData.getString("merchantName");

        //查询该公司关联的为内部公司的客户
        Map<String, Object> customerRelParams = new HashMap<>();
        customerRelParams.put("merchantId", Long.valueOf(merchantId));
        customerRelParams.put("status", "1");
        List<CustomerMerchantRelMongo> customerMerchantRelMongos = customerMerchantRelMongoDao.findAll(customerRelParams);

        List<Long> customerIds = new ArrayList<>();
        for (CustomerMerchantRelMongo customerMerchantRelMongo : customerMerchantRelMongos) {
            customerIds.add(customerMerchantRelMongo.getCustomerId());
        }

        List<Long> innerCustomerIds = new ArrayList<>();

        if (!customerIds.isEmpty()) {
            Map<String, Object> customerParams1 = new HashMap<>();
            customerParams1.put("customerid", customerIds);

            Map<Operator,Map<String,Object>> params = new HashMap<>();
            params.put(Operator.in, customerParams1);

            Map<String, Object> customerParams2 = new HashMap<>();
            customerParams2.put("type", "1");
            params.put(Operator.eq, customerParams2);

            List<CustomerInfoMogo> customerInfoMogos = customerInfoMongoDao.findAllCustomers(params);

            for (CustomerInfoMogo customerInfoMogo : customerInfoMogos) {
                innerCustomerIds.add(customerInfoMogo.getCustomerid());
            }
        }

        if (!innerCustomerIds.isEmpty()) {

            List<ReceiveBillModel> receiveBillModels = new ArrayList<>();

            //查询上架单客户为内部公司的交易数据进行创建应收，
            List<ShelfModel> shelfModels = shelfDao.getReceiveShelfList(Long.valueOf(merchantId), innerCustomerIds, month);

            Map<String, List<ShelfModel>> customerBuMap = new HashMap<>();
            
            //应收账单外部单号来源表
            List<ReceiveExternalCodeModel> externalList = new ArrayList<ReceiveExternalCodeModel>() ;

            for (ShelfModel shelfModel : shelfModels) {
            	
            	/**校验是否存在应收账单外部单号来源表*/
            	ReceiveExternalCodeModel queryModel = new ReceiveExternalCodeModel(); 
            	
            	queryModel.setBuId(shelfModel.getBuId());
            	queryModel.setCurrency(shelfModel.getCurrency());
            	queryModel.setExternalCode(shelfModel.getCode());
            	queryModel.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_1);
            	
            	queryModel = receiveExternalCodeDao.searchByModel(queryModel) ;
            	
            	if(queryModel != null) {
            		continue ;
            	}
            	
                String key = shelfModel.getBuId() + "_" + shelfModel.getCustomerId() + "_" + shelfModel.getCurrency();

                List<ShelfModel> shelfModelList = new ArrayList<>();

                if (customerBuMap.containsKey(key)) {
                    shelfModelList.addAll(customerBuMap.get(key));
                }

                shelfModelList.add(shelfModel);
                
                /**保存应收账单外部单号来源表*/
                ReceiveExternalCodeModel saveModel = new ReceiveExternalCodeModel(); 
                
                saveModel.setBuId(shelfModel.getBuId());
                saveModel.setCurrency(shelfModel.getCurrency());
                saveModel.setExternalCode(shelfModel.getCode());
                saveModel.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_1);
                saveModel.setCreateDate(TimeUtils.getNow());
                
                externalList.add(saveModel) ;

                customerBuMap.put(key, shelfModelList);
            }

            SettlementConfigModel configModel = new SettlementConfigModel();
            configModel.setProjectName("经销业务TO B收入");
            configModel.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
            configModel.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
            SettlementConfigModel toBSettlementConfigModel = settlementConfigDao.searchByModel(configModel);
            if (toBSettlementConfigModel == null) {
                throw new RuntimeException("没有找到“经销业务TO B收入”的费项配置");
            }

            ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(toBSettlementConfigModel.getPaymentSubjectId());

            //以相同“客户+事业部+上架月份”为一个应收账单
            for (String key : customerBuMap.keySet()) {

                List<ReceiveBillItemModel> receiveBillItemModels = new ArrayList<>();
                List<ReceiveBillCostItemModel> receiveBillCostItemModels = new ArrayList<>();

                List<String> saleOrderCodes = new ArrayList<>();
                List<String> shelfCodes = new ArrayList<>();

                List<Timestamp> shelfDateList = new ArrayList<>();
                List<String> poNoList = new ArrayList<>();

                List<ShelfModel> shelfModelList = customerBuMap.get(key);

                for (ShelfModel shelfModel : shelfModelList) {
                	
                    poNoList.add(shelfModel.getPoNo());
                    shelfDateList.add(shelfModel.getShelfDate());

                    shelfCodes.add(shelfModel.getCode());
                    saleOrderCodes.add(shelfModel.getSaleOrderCode());

                    SaleShelfModel saleShelfModel = new SaleShelfModel();
                    saleShelfModel.setShelfId(shelfModel.getId());
                    List<SaleShelfModel> saleShelfModels = saleShelfDao.list(saleShelfModel);

                    SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
                    saleOrderItemModel.setOrderId(shelfModel.getSaleOrderId());
                    //查询销售订单表体（取上架商品对应销售金额）
                    List<SaleOrderItemModel> saleOrderItemModels = saleOrderItemDao.list(saleOrderItemModel);
                    Map<String, BigDecimal> goodsMap = new HashMap<>();
                    Map<String, Double> taxMap = new HashMap<>();
                    for (SaleOrderItemModel itemModel : saleOrderItemModels) {
                        goodsMap.put(itemModel.getGoodsNo(), itemModel.getPrice());
                        taxMap.put(itemModel.getGoodsNo(), itemModel.getTaxRate());
                    }

                    List<Map<String, Object>> alreadyVerifyItems = receiveBillItemDao.verifyItemList(shelfModel.getCode());

                    Map<Long, Map<String, Object>> goodsIdsAlreadyVerifyMap = new HashMap<>();
                    for (Map<String, Object> map : alreadyVerifyItems) {
                        Long goodsId = (Long) map.get("goodsId");
                        goodsIdsAlreadyVerifyMap.put(goodsId, map);
                    }

                    for (SaleShelfModel saleShelf : saleShelfModels) {
                        BigDecimal verifyAmount = new BigDecimal("0");
                        Integer verifyNum = 0;
                        if (goodsIdsAlreadyVerifyMap.containsKey(saleShelf.getGoodsId())) {
                            Map<String, Object> map = goodsIdsAlreadyVerifyMap.get(saleShelf.getGoodsId());
                            verifyAmount = map.get("verifyAmount") != null ? (BigDecimal) map.get("verifyAmount") : new BigDecimal("0");
                            BigDecimal verifyNumBD = map.get("verifyNum") != null ? (BigDecimal) map.get("verifyNum") : new BigDecimal("0");
                            verifyNum = verifyNumBD.intValue();
                        }

                        Integer shelfNum = 0;
                        if (saleShelf.getShelfNum() != null && saleShelf.getShelfNum() != 0) {
                            shelfNum += saleShelf.getShelfNum();
                        }

                        shelfNum -= verifyNum;
                        if (shelfNum != 0) {
                            BigDecimal price = goodsMap.get(saleShelf.getGoodsNo()) == null ? BigDecimal.ZERO : goodsMap.get(saleShelf.getGoodsNo());
                            BigDecimal amount = price.multiply(new BigDecimal(shelfNum));

//                            amount = amount.subtract(verifyAmount);

                            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                                continue;
                            }

                            ReceiveBillItemModel receiveBillItemModel = new ReceiveBillItemModel();
                            receiveBillItemModel.setCode(shelfModel.getCode());
                            receiveBillItemModel.setProjectName(toBSettlementConfigModel.getProjectName());
                            receiveBillItemModel.setProjectId(toBSettlementConfigModel.getId());
                            receiveBillItemModel.setPaymentSubjectId(toBSettlementConfigModel.getPaymentSubjectId());
                            receiveBillItemModel.setPaymentSubjectName(toBSettlementConfigModel.getPaymentSubjectName());
                            if (paymentSubjectModel != null) {
                                //科目
                                receiveBillItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                                receiveBillItemModel.setSubjectName(paymentSubjectModel.getSubName());
                            }
                            receiveBillItemModel.setPoNo(saleShelf.getPoNo());
                            receiveBillItemModel.setGoodsId(saleShelf.getGoodsId());
                            receiveBillItemModel.setGoodsNo(saleShelf.getGoodsNo());
                            receiveBillItemModel.setGoodsName(saleShelf.getGoodsName());
                            receiveBillItemModel.setPrice(amount);
                            receiveBillItemModel.setNum(shelfNum);
                            BrandMongo brand = brandMongoDao.getBrandMongo(saleShelf.getGoodsId());
                            if (brand != null) {
                                receiveBillItemModel.setBrandId(brand.getBrandId());
                                receiveBillItemModel.setBrandName(brand.getName());
                            }

                            if (taxMap.get(saleShelf.getGoodsNo()) != null) {
                                Double taxRate = taxMap.get(saleShelf.getGoodsNo());
                                receiveBillItemModel.setTaxRate(taxRate);

                                BigDecimal tax = amount.multiply(new BigDecimal(taxRate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                                BigDecimal taxAmount = amount.add(tax);
                                receiveBillItemModel.setTax(tax);
                                receiveBillItemModel.setTaxAmount(taxAmount);
                            }


                            BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(saleShelf.getGoodsId());
                            if (brandParent != null) {
                                receiveBillItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                                receiveBillItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                                receiveBillItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                            }
                            receiveBillItemModels.add(receiveBillItemModel);
                        }
                    }

                    //已存在的销售退明细
                    /*List<Long> itemIds = new ArrayList<>();
                    //根据PO号关联查询到的则取销售退货单表体PO对应的退货商品信息；
                    List<SaleReturnOrderItemModel> saleReturnOrderItemModels = saleReturnOrderItemDao.statisticsByPoNos(poNoList);
                    for (SaleReturnOrderItemModel saleReturnOrderItemModel : saleReturnOrderItemModels) {
                        String poNos = saleReturnOrderItemModel.getPoNo();

                        *//*List<String> poList = Arrays.asList(poNos.split(","));
                        poList.retainAll(poNoList);*//*
                        //如果该明细的po与上架单的po不一致，或者该明细已生成过费用明细了则不继续生成
                        if (StringUtils.isBlank(poNos) || itemIds.contains(saleReturnOrderItemModel.getId())) {
                            continue;
                        }
                        itemIds.add(saleReturnOrderItemModel.getId());

                        ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
                        receiveBillCostItemModel.setProjectId(toBSettlementConfigModel.getId());
                        receiveBillCostItemModel.setProjectName(toBSettlementConfigModel.getProjectName());
                        receiveBillCostItemModel.setGoodsId(saleReturnOrderItemModel.getInGoodsId());
                        receiveBillCostItemModel.setGoodsNo(saleReturnOrderItemModel.getInGoodsNo());
                        receiveBillCostItemModel.setGoodsName(saleReturnOrderItemModel.getInGoodsName());
                        receiveBillCostItemModel.setPoNo(StringUtils.join(shelfCodes.toArray(), "&"));
                        receiveBillCostItemModel.setBillType(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1);
                        receiveBillCostItemModel.setPaymentSubjectId(toBSettlementConfigModel.getPaymentSubjectId());
                        receiveBillCostItemModel.setPaymentSubjectName(toBSettlementConfigModel.getPaymentSubjectName());

                        if (paymentSubjectModel != null) {
                            receiveBillCostItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                            receiveBillCostItemModel.setSubjectName(paymentSubjectModel.getSubName());
                        }

                        Integer costNum = saleReturnOrderItemModel.getReturnNum() + saleReturnOrderItemModel.getBadGoodsNum();
                        BigDecimal price = saleReturnOrderItemModel.getPrice() != null ? saleReturnOrderItemModel.getPrice() : BigDecimal.ZERO;
                        BigDecimal amount = price.multiply(new BigDecimal(costNum));
                        receiveBillCostItemModel.setNum(costNum);
                        receiveBillCostItemModel.setPrice(amount);
                        BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(saleReturnOrderItemModel.getInGoodsId());
                        if (brandParent != null) {
                            receiveBillCostItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                            receiveBillCostItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                            receiveBillCostItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                        }
                        receiveBillCostItemModels.add(receiveBillCostItemModel);
                    }
                    */
                    
                }

                if (receiveBillItemModels.isEmpty()) {
                    continue;
                }

                ReceiveBillModel receiveBillModel = new ReceiveBillModel();
                receiveBillModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSZD));
                receiveBillModel.setCustomerId(shelfModelList.get(0).getCustomerId());
                receiveBillModel.setCustomerName(shelfModelList.get(0).getCustomerName());
                receiveBillModel.setBuId(shelfModelList.get(0).getBuId());
                receiveBillModel.setBuName(shelfModelList.get(0).getBuName());
                receiveBillModel.setCurrency(shelfModelList.get(0).getCurrency());
                receiveBillModel.setSortType(DERP_ORDER.RECEIVEBILL_SORTTYPE_1);
                receiveBillModel.setIsAddWorn(DERP_ORDER.RECEIVEBILL_ISADDWORN_1);
                receiveBillModel.setRelCode(StringUtils.join(shelfCodes.toArray(), "&"));
                receiveBillModel.setPoNo(StringUtils.join(poNoList.toArray(), "&"));
                Collections.sort(shelfDateList);
                receiveBillModel.setBillDate(shelfDateList.get(shelfDateList.size()-1));
                receiveBillModel.setMerchantId(Long.valueOf(merchantId));
                receiveBillModel.setMerchantName(merchantName);
                receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_01);
                receiveBillModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00);
                receiveBillModel.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10);
                receiveBillModel.setSaleModel(DERP_ORDER.RECEIVEBILL_SALEMODEL_009);
                receiveBillModel.setSettlementType("1");
                receiveBillModel.setCreater("系统自动生成");
                receiveBillModel.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_1);

                Long billId = receiveBillDao.save(receiveBillModel);

                receiveBillModels.add(receiveBillModel);

                for (ReceiveBillItemModel itemModel : receiveBillItemModels) {
                    itemModel.setBillId(billId);
                    itemModel.setCreateDate(TimeUtils.getNow());
                }

                for (ReceiveBillCostItemModel itemModel : receiveBillCostItemModels) {
                    itemModel.setBillId(billId);
                    itemModel.setCreateDate(TimeUtils.getNow());
                }

                if (!receiveBillItemModels.isEmpty()) {
                    receiveBillItemDao.batchSave(receiveBillItemModels);
                }

                if (!receiveBillCostItemModels.isEmpty()) {
                    receiveBillCostItemDao.batchSave(receiveBillCostItemModels);
                }

                //保存核销记录
                if (!saleOrderCodes.isEmpty()) {
                    List<ReceiveBillVerifyItemModel> verifyItemModels = getVerifyItems(saleOrderCodes);
                    for (ReceiveBillVerifyItemModel verifyItemModel : verifyItemModels) {
                        verifyItemModel.setBillId(billId);
                        receiveBillVerifyItemDao.save(verifyItemModel);
                    }
                }

                //生成操作日志
                ReceiveBillOperateModel receiveBillOperateModel = new ReceiveBillOperateModel();
                receiveBillOperateModel.setBillId(billId);
                receiveBillOperateModel.setOperateDate(TimeUtils.getNow());
                receiveBillOperateModel.setOperateNode(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_0);
                receiveBillOperateModel.setCreateDate(TimeUtils.getNow());
                receiveBillOperateModel.setOperator("系统自动生成");
                receiveBillOperateDao.save(receiveBillOperateModel);

            }

            for (ReceiveExternalCodeModel receiveExternalCodeModel : externalList) {
            	receiveExternalCodeDao.save(receiveExternalCodeModel) ;
			}


            List<String> receiveBillCodes = new ArrayList<>();
            //触发应收单审核邮件提醒功能
            for (ReceiveBillModel receiveBillModel : receiveBillModels) {
                receiveBillCodes.add(receiveBillModel.getCode());
                BigDecimal itemPrice = receiveBillItemDao.getTotalReceivePrice(receiveBillModel.getId());
                BigDecimal costPrice = receiveBillCostItemDao.getTotalReceivePrice(receiveBillModel.getId());

                if (itemPrice == null) {
                    itemPrice = new BigDecimal(0);
                }

                if (costPrice == null) {
                    costPrice = new BigDecimal(0);
                }

                BigDecimal receiveAmount = itemPrice.add(costPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

                //封装发送邮件JSON
                ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

                emailUserDTO.setBuId(receiveBillModel.getBuId());
                emailUserDTO.setBuName(receiveBillModel.getBuName());
                emailUserDTO.setMerchantId(receiveBillModel.getMerchantId());
                emailUserDTO.setMerchantName(receiveBillModel.getMerchantName());
                emailUserDTO.setOrderCode(receiveBillModel.getCode());
                emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1);
                emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                        DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1));
                emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1);
                emailUserDTO.setSupplier(receiveBillModel.getCustomerName());
                emailUserDTO.setAmount(receiveBillModel.getCurrency() + "&nbsp;" + receiveAmount.toPlainString());

                JSONObject emailJson = JSONObject.fromObject(emailUserDTO) ;

                rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());
            }

            return receiveBillCodes;

        }
        return null;

    }



    @Override
    public void saveReceiveInvoice(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String receiveCodes = jsonData.getString("receiveCodes");
        Integer merchantId = (Integer) jsonData.get("merchantId");

        List<ReceiveBillModel> receiveBillModels = receiveBillDao.getBillsByCodesAndStatus(receiveCodes, Long.valueOf(merchantId));

        if (receiveBillModels != null && receiveBillModels.size() > 0) {
            for (ReceiveBillModel receiveBillModel : receiveBillModels) {
                FileTempDTO fileTempDTO = new FileTempDTO();
                fileTempDTO.setMerchantId(receiveBillModel.getMerchantId());
                List<Long> customerIds = new ArrayList<>();
                customerIds.add(receiveBillModel.getCustomerId());

                List<FileTempDTO> fileTempDTOS = fileTempDao.listFileTempInfo(fileTempDTO);

                if (fileTempDTOS.isEmpty()) {
                    continue;
                }



















            }







        }

    }

    private List<ReceiveBillVerifyItemModel> getVerifyItems(List<String> orderCodes) throws SQLException {
        List<ReceiveBillVerifyItemModel> verifyItemModels = new ArrayList<>();
        List<Long> advanceIds = new ArrayList<>();

        List<AdvanceBillItemDTO> advanceBillItemDTOS = advanceBillItemDao.listWithoutVerify(orderCodes);

        for (AdvanceBillItemDTO itemDTO : advanceBillItemDTOS) {
            advanceIds.add(itemDTO.getAdvanceId());
        }

        Map<Long, AdvanceBillOperateItemModel> auditDateMap = new HashMap<>();
        Map<Long, AdvanceBillVerifyItemModel> verifyDateMap = new HashMap<>();
        if (!advanceIds.isEmpty()) {

            //预收款单的审核通过的最新时间
            List<AdvanceBillOperateItemModel> operateItemModels = advanceBillOperateItemDao.getLatestAuditModelByAdvanceIds(advanceIds);

            for (AdvanceBillOperateItemModel operateItemModel : operateItemModels) {
                auditDateMap.put(operateItemModel.getAdvanceId(), operateItemModel);
            }

            //预收款单的核销收款的最新时间
            List<AdvanceBillVerifyItemModel> verifyModelByAdvanceIds = advanceBillVerifyItemDao.getLatestVerifyModelByAdvanceIds(advanceIds);

            for (AdvanceBillVerifyItemModel verifyItemModel : verifyModelByAdvanceIds) {
                verifyDateMap.put(verifyItemModel.getAdvanceId(), verifyItemModel);
            }

        }

        for (AdvanceBillItemDTO itemDTO : advanceBillItemDTOS) {
            ReceiveBillVerifyItemModel receiveBillVerifyItemModel = new ReceiveBillVerifyItemModel();
            receiveBillVerifyItemModel.setAdvanceId(itemDTO.getId());
            receiveBillVerifyItemModel.setAdvanceCode(itemDTO.getAdvanceCode());
            receiveBillVerifyItemModel.setPrice(itemDTO.getAmount());
            receiveBillVerifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1);
            receiveBillVerifyItemModel.setReceiceNo(itemDTO.getRelCode());

            AdvanceBillOperateItemModel operateItemModel = auditDateMap.get(itemDTO.getAdvanceId());
            receiveBillVerifyItemModel.setVerifyDate(new Timestamp(operateItemModel.getOperateDate().getTime()));
            receiveBillVerifyItemModel.setVerifyId(operateItemModel.getOperateId());
            receiveBillVerifyItemModel.setVerifier(operateItemModel.getOperater());

            AdvanceBillVerifyItemModel verifyItemModel = verifyDateMap.get(itemDTO.getAdvanceId());
            if (verifyItemModel != null) {
                receiveBillVerifyItemModel.setReceiveDate(new Timestamp(verifyItemModel.getVerifyDate().getTime()));
            }
            verifyItemModels.add(receiveBillVerifyItemModel);
        }

        return verifyItemModels;
    }

    @Override
    public List<MerchantInfoMongo> getMerchantList(Integer merchantId) {
        Map<String, Object> merParams = new HashMap<>();
        if (merchantId != null && merchantId.longValue() > 0) {
            merParams.put("merchantId", Long.valueOf(merchantId));
            List<MerchantInfoMongo> merchantList = merchantInfoMongoDao.findAll(merParams);
            return merchantList;
        } else {
            List<MerchantInfoMongo> merchantList = new ArrayList<>();

            List<String> topIdeaCodes = new ArrayList<>();
            topIdeaCodes.add("1000000204");
            topIdeaCodes.add("0000138");
            topIdeaCodes.add("0000134");
            topIdeaCodes.add("1000000645");
            topIdeaCodes.add("1000052958");
            topIdeaCodes.add("1000000726");

            for (String topIdeaCode : topIdeaCodes) {
                merParams.put("topidealCode", topIdeaCode);
                MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merParams);

                if (merchantInfo != null) {
                    merchantList.add(merchantInfo);
                }
            }

            return merchantList;
        }

    }
}
