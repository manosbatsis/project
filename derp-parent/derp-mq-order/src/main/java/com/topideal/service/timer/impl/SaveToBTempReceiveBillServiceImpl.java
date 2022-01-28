package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.dao.common.SdSaleTypeDao;
import com.topideal.dao.common.SdSaleVerifyConfigDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.dto.sale.SaleReturnOrderDTO;
import com.topideal.entity.dto.sale.ShelfDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.entity.vo.common.SdSaleTypeModel;
import com.topideal.entity.vo.common.SdSaleVerifyConfigModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.timer.SaveToBTempReceiveBillService;
import jdk.nashorn.internal.ir.LiteralNode;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 生成tob暂估核销数据
 * @Author: Chen Yiluan
 * @Date: 2021/04/23 18:03
 **/
@Service
public class SaveToBTempReceiveBillServiceImpl implements SaveToBTempReceiveBillService {

    @Autowired
    private ShelfDao shelfDao;
    @Autowired
    private TobTemporaryReceiveBillDao tobTemporaryReceiveBillDao;
    @Autowired
    private AccountingReminderConfigDao accountingReminderConfigDao;
    @Autowired
    private AccountingReminderItemDao accountingReminderItemDao;
    @Autowired
    private SaleShelfDao saleShelfDao;
    @Autowired
    private SaleOrderItemDao saleOrderItemDao;
    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private ReceiveBillOperateDao receiveBillOperateDao;
    @Autowired
    private ReceiveBillInvoiceDao receiveBillInvoiceDao;
    @Autowired
    private ReceiveBillVerifyItemDao receiveBillVerifyItemDao;
    @Autowired
    private TobTemporaryReceiveBillItemDao tobTemporaryReceiveBillItemDao;
    @Autowired
    private TobTemporaryReceiveBillRebateItemDao tobTemporaryReceiveBillRebateItemDao;
    @Autowired
    private SaleSdOrderDao saleSdOrderDao;
    @Autowired
    private SaleSdOrderItemDao saleSdOrderItemDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private TobTempVerifyRelDao tobTempVerifyRelDao;
    @Autowired
    private ReceiveBillItemDao receiveBillItemDao;
    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao;
    @Autowired
    private SdSaleVerifyConfigDao sdSaleVerifyConfigDao;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private SaleShelfIdepotDao saleShelfIdepotDao;
    @Autowired
    private SaleShelfIdepotItemDao saleShelfIdepotItemDao;
    @Autowired
    private SdSaleTypeDao sdSaleTypeDao;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;
    @Autowired
    private SaleReturnOrderDao saleReturnOrderDao;
    @Autowired
    private SaleReturnOrderItemDao saleReturnOrderItemDao;
    @Autowired
    private SaleReturnIdepotDao saleReturnIdepotDao;
    @Autowired
    private SaleReturnIdepotItemDao saleReturnIdepotItemDao;
    @Autowired
    private SaleOrderDao saleOrderDao;

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201160006, model = DERP_LOG_POINT.POINT_13201160006_Label)
    public boolean saveToBTempReceiveBill(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String orderCodes = (String) jsonData.get("orderCodes");
        Integer merchantId = (Integer) jsonData.get("merchantId");
        String dataSource = (String) jsonData.get("dataSource");// 1-上架单 2-类型为“退货退款”的购销销售退货订单
        String refresh = (String) jsonData.get("refresh"); //1-刷新
        String month = (String) jsonData.get("month"); //指定月份
        String saleSdCode = (String) jsonData.get("saleSdCode"); //修改的sd单号

        if (StringUtils.isNotBlank(refresh) && "1".equals(refresh) && StringUtils.isBlank(month)) {
            //默认上个月
            month = TimeUtils.getLastMonth(new Date());
        }

        /***************************上架单*****************************/
        if (StringUtils.isBlank(dataSource) || "1".equals(dataSource)) {

            //生成暂估的所有上架单号
            String generateShelfCodes = "";

            //刷新
            if (StringUtils.isNotBlank(refresh) && "1".equals(refresh)) {

                //查询指定月份生成的tob暂估单且状态为“已上架未结算”的所有上架单号
                TobTemporaryReceiveBillDTO receiveBillDTO = new TobTemporaryReceiveBillDTO();
                receiveBillDTO.setShelfMonth(month);
                receiveBillDTO.setStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
                receiveBillDTO.setRebateStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);

                receiveBillDTO.setOrderType(DERP_ORDER.TOBTEMPRECEIVEBILL_ORDERTYPE_1);
                List<TobTemporaryReceiveBillDTO> receiveBillDTOS = tobTemporaryReceiveBillDao.listByDto(receiveBillDTO);

                //tob暂估id
                List<Long> tobIds = receiveBillDTOS.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());

                //对应的上架单号集合
                List<String> updateShelfCodes = receiveBillDTOS.stream().map(e -> e.getShelfCode()).distinct().collect(Collectors.toList());

                //删除指定月份生成的tob暂估单且状态为“已上架未结算”的所有暂估单及对应表体
                if (!tobIds.isEmpty()) {
                    tobTemporaryReceiveBillDao.delete(tobIds);
                    tobTemporaryReceiveBillItemDao.delItemsByBillIds(tobIds);
                    tobTemporaryReceiveBillRebateItemDao.delItemsByBillIds(tobIds);
                }

                if (!updateShelfCodes.isEmpty()) {
                    //更新上架单状态
                    shelfDao.batchUpdateStatus(DERP_ORDER.SHELF_ISGENERATE_0, updateShelfCodes);
                }

                //查询指定月份的所有上架单号
                ShelfDTO shelfDTO = new ShelfDTO();
                shelfDTO.setShelfMonth(month);
                shelfDTO.setIsGenerate(DERP_ORDER.SHELF_ISGENERATE_0);
                List<ShelfDTO> shelfDTOList = shelfDao.listToBTempDTO(shelfDTO);
                generateShelfCodes = shelfDTOList.stream().map(e -> e.getCode()).collect(Collectors.joining(","));
            }

            //指定单号生成暂估
            if (StringUtils.isNotBlank(orderCodes)) {
                generateShelfCodes = orderCodes + "," + generateShelfCodes;
            }

            ShelfDTO shelfDTO = new ShelfDTO();
            //指定单号生成
            if (StringUtils.isNotBlank(generateShelfCodes)) {
                shelfDTO.setShelfCodes(generateShelfCodes);
//                shelfDTO.setIsGenerate(DERP_ORDER.SHELF_ISGENERATE_0);
                List<ShelfDTO> shelfDTOList = shelfDao.listToBTempDTO(shelfDTO);
                generateShelfToBTempOrder(shelfDTOList, saleSdCode);
            } else { //定时器生成

                Map<String, Object> merParams = new HashMap<>();
                if (merchantId != null && merchantId.longValue() > 0) {
                    merParams.put("merchantId", Long.valueOf(merchantId));
                }
                List<MerchantInfoMongo> merchantList = merchantInfoMongoDao.findAll(merParams);

                for (MerchantInfoMongo merchantInfo : merchantList) {
                    shelfDTO.setMerchantId(merchantInfo.getMerchantId());
                    shelfDTO.setIsGenerate(DERP_ORDER.SHELF_ISGENERATE_0);
                    List<ShelfDTO> shelfDTOS = shelfDao.listToBTempDTO(shelfDTO);

                    generateShelfToBTempOrder(shelfDTOS, saleSdCode);
                }
            }

        }

        /*********类型为“退货退款”的购销销售退货订单*************/
        if (StringUtils.isBlank(dataSource) || "2".equals(dataSource)) {
            //生成暂估的所有销售退货订单
            String generateSaleReturnCodes = "";

            //刷新
            if (StringUtils.isNotBlank(refresh) && "1".equals(refresh)) {

                //查询指定月份生成的tob暂估单且状态为“已上架未结算”的所有销售退货单号
                TobTemporaryReceiveBillDTO receiveBillDTO = new TobTemporaryReceiveBillDTO();
                receiveBillDTO.setShelfMonth(month);
                receiveBillDTO.setStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
                receiveBillDTO.setRebateStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
                receiveBillDTO.setOrderType(DERP_ORDER.TOBTEMPRECEIVEBILL_ORDERTYPE_2);
                List<TobTemporaryReceiveBillDTO> receiveBillDTOS = tobTemporaryReceiveBillDao.listByDto(receiveBillDTO);

                //tob暂估id
                List<Long> tobIds = receiveBillDTOS.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());

                //对应的上架单号集合
                List<String> updateReturnCodes = receiveBillDTOS.stream().map(e -> e.getOrderCode()).distinct().collect(Collectors.toList());

                //删除指定月份生成的tob暂估单且状态为“已上架未结算”的所有暂估单及对应表体
                if (!tobIds.isEmpty()) {
                    tobTemporaryReceiveBillDao.delete(tobIds);
                    tobTemporaryReceiveBillItemDao.delItemsByBillIds(tobIds);
                    tobTemporaryReceiveBillRebateItemDao.delItemsByBillIds(tobIds);
                }

                //更新销售退货单状态
                if (!updateReturnCodes.isEmpty()) {
                    saleReturnOrderDao.batchUpdateStatus(DERP_ORDER.SHELF_ISGENERATE_0, updateReturnCodes);
                }

                //查询指定月份的所有销售退货单号
                SaleReturnOrderDTO saleReturnOrderDTO = new SaleReturnOrderDTO();
                saleReturnOrderDTO.setReturnInMonth(month);
                saleReturnOrderDTO.setIsGenerateTemp(DERP_ORDER.SHELF_ISGENERATE_0);
                List<SaleReturnOrderDTO> saleReturnOrderDTOS = saleReturnOrderDao.listToBTempDTO(saleReturnOrderDTO);
                generateSaleReturnCodes = saleReturnOrderDTOS.stream().map(e -> e.getCode()).collect(Collectors.joining(","));
            }

            //指定单号生成暂估
            if (StringUtils.isNotBlank(orderCodes)) {
                generateSaleReturnCodes = orderCodes + "," + generateSaleReturnCodes;
            }

            SaleReturnOrderDTO saleReturnOrderDTO = new SaleReturnOrderDTO();
            //指定单号生成
            if (StringUtils.isNotBlank(generateSaleReturnCodes)) {
                saleReturnOrderDTO.setReturnCodes(generateSaleReturnCodes);
//                saleReturnOrderDTO.setIsGenerateTemp(DERP_ORDER.SHELF_ISGENERATE_0);
                List<SaleReturnOrderDTO> saleReturnOrderDTOList = saleReturnOrderDao.listToBTempDTO(saleReturnOrderDTO);

                generateSaleReturnToBTempOrder(saleReturnOrderDTOList, saleSdCode);
            } else { //定时器生成

                Map<String, Object> merParams = new HashMap<>();
                if (merchantId != null && merchantId.longValue() > 0) {
                    merParams.put("merchantId", Long.valueOf(merchantId));
                }
                List<MerchantInfoMongo> merchantList = merchantInfoMongoDao.findAll(merParams);

                for (MerchantInfoMongo merchantInfo : merchantList) {
                    saleReturnOrderDTO.setMerchantId(merchantInfo.getMerchantId());
                    saleReturnOrderDTO.setIsGenerateTemp(DERP_ORDER.SHELF_ISGENERATE_0);
                    List<SaleReturnOrderDTO> saleReturnOrderDTOList = saleReturnOrderDao.listToBTempDTO(saleReturnOrderDTO);

                    generateSaleReturnToBTempOrder(saleReturnOrderDTOList, saleSdCode);
                }
            }
        }

        return true;
    }

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201160010, model = DERP_LOG_POINT.POINT_13201160010_Label)
    public boolean updateVerifyToBTempBill(String json, String keys, String topics, String tags) throws Exception {

        JSONObject jsonData = JSONObject.fromObject(json);
        //商家id
        Integer merchantId = (Integer) jsonData.get("merchantId");
        //应收单号
        String receiveCode = (String) jsonData.get("receiveCode");
        //上架单号集合
        String orderCodes = (String) jsonData.get("orderCodes");
        //审核类型 0-审核通过 1-作废通过
        String auditType = (String) jsonData.get("auditType");

        ReceiveBillModel receiveBillModel = new ReceiveBillModel();
        TobTemporaryReceiveBillDTO tobTemporaryReceiveBillDTO = new TobTemporaryReceiveBillDTO();

        List<Long> updateTobIds = new ArrayList<>();
        //指定应收账单核销tob暂估（审核通过 / 作废通过）
        if (StringUtils.isNotBlank(receiveCode)) {
            receiveBillModel.setCode(receiveCode);

            //判断是否是作废，删除该应收单核销的关联关系
            if ("1".equals(auditType)) {
                ReceiveBillModel delReceive = receiveBillDao.searchByModel(receiveBillModel);

                TobTempVerifyRelModel tobTempVerifyRelModel = new TobTempVerifyRelModel();
                tobTempVerifyRelModel.setReceiveId(delReceive.getId());
                List<TobTempVerifyRelModel> relModels = tobTempVerifyRelDao.list(tobTempVerifyRelModel);

                for (TobTempVerifyRelModel relModel : relModels) {
                    updateTobIds.add(relModel.getTobId());
                }

                tobTempVerifyRelDao.batchDelByReceiveId(delReceive.getId());

                //更新表头
                if (!updateTobIds.isEmpty()) {
                    updateTobTemp(updateTobIds);
                }
                return true;
            }
        }

        if (merchantId != null && merchantId.longValue() > 0) {
            receiveBillModel.setMerchantId(Long.valueOf(merchantId));
            tobTemporaryReceiveBillDTO.setMerchantId(Long.valueOf(merchantId));
        }

        //待核销的tob暂估明细
        if (StringUtils.isNotBlank(orderCodes)) {
            tobTemporaryReceiveBillDTO.setShelfCodes(orderCodes);
        }

        //红冲暂估自动核销
        verifyWriteOffItems(tobTemporaryReceiveBillDTO);

        //“上架单”创建的应收单核销tob暂估
        shelfItemVerifyItems(receiveBillModel, tobTemporaryReceiveBillDTO, updateTobIds);

        //“账单出库单”创建的应收单核销tob暂估
        billOutItemVerifyItems(receiveBillModel, tobTemporaryReceiveBillDTO, updateTobIds);

        //“销售退款单”创建的应收单核销tob暂估
        saleReturnInOrderItemVerifyItems(receiveBillModel, tobTemporaryReceiveBillDTO, updateTobIds);

        //“销售订单”创建的应收单核销tob暂估
        if (StringUtils.isNotBlank(receiveCode)) {
            receiveBillModel.setCode(receiveCode);
            receiveBillModel.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_4);
            ReceiveBillModel receive = receiveBillDao.searchByModel(receiveBillModel);

            if (receive != null && DERP_ORDER.RECEIVEBILL_ORDERTYPE_4.equals(receive.getOrderType())) {
                //“销售订单”创建的应收单核销tob暂估
                saleOrderItemVerifyItems(receive, tobTemporaryReceiveBillDTO, updateTobIds);
            }

        }

        //暂估返利核销
        verifyRebateItems(receiveBillModel, updateTobIds, receiveBillModel.getMerchantId());

        //更新表头
        if (!updateTobIds.isEmpty()) {
            updateTobTemp(updateTobIds);
        }

        return true;
    }


    /**
     * @Description 上架单生成tob暂估
     * @Param shelfDTOList
     */
    private void generateShelfToBTempOrder(List<ShelfDTO> shelfDTOList, String saleSdCode) throws Exception {
        //生成的tob暂估记录对应的上架单集合
        List<String> shelfCodes = new ArrayList<>();
        Map<Long, BrandParentMongo> brandParentMongoMap = new HashMap<>();

        for (ShelfDTO shelf : shelfDTOList) {

            if (DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(shelf.getBusinessModel()) &&
                    DERP_ORDER.SHELF_STATUS_1.equals(shelf.getState())) {
                continue;
            }

            //查询暂估是否已经生成
            TobTemporaryReceiveBillModel billModel = new TobTemporaryReceiveBillModel();
            billModel.setShelfCode(shelf.getCode());
            TobTemporaryReceiveBillModel exist = tobTemporaryReceiveBillDao.searchByModel(billModel);

            if (exist == null) {
                shelfCodes.add(shelf.getCode());

                SaleOrderModel saleOrderModel = saleOrderDao.searchById(shelf.getSaleOrderId());

                TobTemporaryReceiveBillModel tobTemporaryReceiveBillModel = new TobTemporaryReceiveBillModel();
                List<TobTemporaryReceiveBillItemModel> itemModels = new ArrayList<>();
                List<TobTemporaryReceiveBillRebateItemModel> rebateItemModels = new ArrayList<>();

                tobTemporaryReceiveBillModel.setSaleType(DERP_ORDER.TOBTEMPRECEIVEBILL_SALETYPE_2);
                tobTemporaryReceiveBillModel.setOrderType(DERP_ORDER.TOBTEMPRECEIVEBILL_ORDERTYPE_1);

                // 对于销售类型为 购销-整批结算 的暂估核销记录，
                // 根据“公司+事业部+客户”查询“账期提醒配置”表中提醒类型为“收款”且审核时间为最新的账期配置，计算记录各节点计划日期；
                if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(shelf.getBusinessModel())) {
                    tobTemporaryReceiveBillModel.setSaleType(DERP_ORDER.TOBTEMPRECEIVEBILL_SALETYPE_1);


                    //以“商家+事业部+客户”维度查询最新一条已审核的账期提醒配置
                    AccountingReminderConfigModel accountingReminderConfigModel = new AccountingReminderConfigModel();
                    accountingReminderConfigModel.setMerchantId(shelf.getMerchantId());
                    accountingReminderConfigModel.setCustomerId(shelf.getCustomerId());
                    accountingReminderConfigModel.setBuId(shelf.getBuId());
                    accountingReminderConfigModel.setStatus(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_STATUS_1);
                    accountingReminderConfigModel.setReminderType(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_REMINDER_TYPE_1);
                    AccountingReminderConfigModel newestAccountConfig = accountingReminderConfigDao.getLatestAuditDetail(accountingReminderConfigModel);

                    if (newestAccountConfig != null) {
                        AccountingReminderItemModel reminderItemModel = new AccountingReminderItemModel();
                        reminderItemModel.setConfigId(newestAccountConfig.getId());
                        List<AccountingReminderItemModel> reminderItemModels = accountingReminderItemDao.list(reminderItemModel);

                        Map<String, AccountingReminderItemModel> accountingReminderItemModelMap = new HashMap<>();
                        for (AccountingReminderItemModel accountingReminderItemModel : reminderItemModels) {
                            accountingReminderItemModelMap.put(accountingReminderItemModel.getNode(), accountingReminderItemModel);
                        }

                        //出账单日期
                        if (accountingReminderItemModelMap.get(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_2) != null) {
                            AccountingReminderItemModel accountingReminderItemModel = accountingReminderItemModelMap.get(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_2);
                            Integer nodeEffective = accountingReminderItemModel.getNodeEffective();
                            boolean isNatural = false;
                            if (DERP_ORDER.ACCOUNTINGREMINDERITEM_BENCHMARK_UNIT_1.equals(accountingReminderItemModel.getBenchmarkUnit())) {
                                isNatural = true;
                            }
                            Timestamp outBillPlanTime = TimeUtils.addDateByWorkDay(shelf.getShelfDate(), nodeEffective, isNatural);
                            tobTemporaryReceiveBillModel.setOutBillPlanDate(outBillPlanTime);
                        }

                        //账单确定日期
                        if (accountingReminderItemModelMap.get(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_3) != null) {
                            AccountingReminderItemModel accountingReminderItemModel = accountingReminderItemModelMap.get(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_3);
                            Integer nodeEffective = accountingReminderItemModel.getNodeEffective();
                            boolean isNatural = false;
                            if (DERP_ORDER.ACCOUNTINGREMINDERITEM_BENCHMARK_UNIT_1.equals(accountingReminderItemModel.getBenchmarkUnit())) {
                                isNatural = true;
                            }
                            Timestamp calTime = tobTemporaryReceiveBillModel.getOutBillPlanDate();
                            if (calTime == null) {
                                calTime = shelf.getShelfDate();
                            }
                            Timestamp confirmPlanDate = TimeUtils.addDateByWorkDay(calTime, nodeEffective, isNatural);
                            tobTemporaryReceiveBillModel.setConfirmPlanDate(confirmPlanDate);
                        }

                        //开票日期
                        if (accountingReminderItemModelMap.get(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_4) != null) {
                            AccountingReminderItemModel accountingReminderItemModel = accountingReminderItemModelMap.get(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_4);
                            Integer nodeEffective = accountingReminderItemModel.getNodeEffective();
                            boolean isNatural = false;
                            if (DERP_ORDER.ACCOUNTINGREMINDERITEM_BENCHMARK_UNIT_1.equals(accountingReminderItemModel.getBenchmarkUnit())) {
                                isNatural = true;
                            }
                            Timestamp calTime = tobTemporaryReceiveBillModel.getConfirmPlanDate();
                            if (calTime == null) {
                                calTime = tobTemporaryReceiveBillModel.getOutBillPlanDate();
                            }
                            if (calTime == null) {
                                calTime = shelf.getShelfDate();
                            }
                            Timestamp invoicingPlanDate = TimeUtils.addDateByWorkDay(calTime, nodeEffective, isNatural);
                            tobTemporaryReceiveBillModel.setInvoicingPlanDate(invoicingPlanDate);
                        }

                        //回款日期
                        if (accountingReminderItemModelMap.get(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_5) != null) {
                            AccountingReminderItemModel accountingReminderItemModel = accountingReminderItemModelMap.get(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_5);
                            Integer nodeEffective = accountingReminderItemModel.getNodeEffective();
                            boolean isNatural = false;
                            if (DERP_ORDER.ACCOUNTINGREMINDERITEM_BENCHMARK_UNIT_1.equals(accountingReminderItemModel.getBenchmarkUnit())) {
                                isNatural = true;
                            }
                            Timestamp calTime = tobTemporaryReceiveBillModel.getInvoicingPlanDate();
                            if (calTime == null) {
                                calTime = tobTemporaryReceiveBillModel.getConfirmPlanDate();
                            }
                            if (calTime == null) {
                                calTime = tobTemporaryReceiveBillModel.getOutBillPlanDate();
                            }
                            if (calTime == null) {
                                calTime = shelf.getShelfDate();
                            }
                            Timestamp paymentPlanDate = TimeUtils.addDateByWorkDay(calTime, nodeEffective, isNatural);
                            tobTemporaryReceiveBillModel.setPaymentPlanDate(paymentPlanDate);
                        }
                    }
                }

                tobTemporaryReceiveBillModel.setMerchantId(shelf.getMerchantId());
                tobTemporaryReceiveBillModel.setMerchantName(shelf.getMerchantName());
                tobTemporaryReceiveBillModel.setCustomerId(shelf.getCustomerId());
                tobTemporaryReceiveBillModel.setCustomerName(shelf.getCustomerName());
                tobTemporaryReceiveBillModel.setBuId(shelf.getBuId());
                tobTemporaryReceiveBillModel.setBuName(shelf.getBuName());
                tobTemporaryReceiveBillModel.setCurrency(saleOrderModel.getCurrency());
                tobTemporaryReceiveBillModel.setPoNo(shelf.getPoNo());
                tobTemporaryReceiveBillModel.setShelfDate(shelf.getShelfDate());
                tobTemporaryReceiveBillModel.setShelfCode(shelf.getCode());
                tobTemporaryReceiveBillModel.setOrderCode(shelf.getSaleOrderCode());
                tobTemporaryReceiveBillModel.setIsWriteOff(shelf.getIsWriteOff());
                tobTemporaryReceiveBillModel.setOriginalShelfCode(shelf.getOriginalShelfCode());
                tobTemporaryReceiveBillModel.setStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
                tobTemporaryReceiveBillModel.setRebateStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);

                //应收金额
                BigDecimal receiveAmount = new BigDecimal("0");
                //返利金额
                BigDecimal rebateReceiveAmount = new BigDecimal("0");

                //状态为“已入库”时取 上架入库单明细， “已上架”取 上架单明细
                if (DERP_ORDER.SHELF_STATUS_1.equals(shelf.getState())) {
                    SaleShelfModel saleShelfModel = new SaleShelfModel();
                    saleShelfModel.setShelfId(shelf.getId());
                    List<SaleShelfModel> saleShelfModels = saleShelfDao.list(saleShelfModel);
                    for (SaleShelfModel saleOrder : saleShelfModels) {
                        TobTemporaryReceiveBillItemModel temporaryReceiveBillItemModel = new TobTemporaryReceiveBillItemModel();
                        SaleOrderItemModel iModel = saleOrderItemDao.searchById(saleOrder.getSaleItemId());
                        if (iModel != null) {
                            BigDecimal amount = new BigDecimal(saleOrder.getShelfNum()).multiply(iModel.getPrice());
                            receiveAmount = receiveAmount.add(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
                            temporaryReceiveBillItemModel.setPrice(iModel.getPrice());
                        }

                        temporaryReceiveBillItemModel.setGoodsId(saleOrder.getGoodsId());
                        temporaryReceiveBillItemModel.setGoodsNo(saleOrder.getGoodsNo());
                        temporaryReceiveBillItemModel.setGoodsName(saleOrder.getGoodsName());
                        temporaryReceiveBillItemModel.setShelfNum(saleOrder.getShelfNum());
                        BrandParentMongo brandParent = brandParentMongoMap.get(saleOrder.getGoodsId());
                        if (brandParent == null) {
                            brandParent = brandParentMongoDao.getBrandParentMongo(saleOrder.getGoodsId());
                        }

                        if (brandParent != null) {
                            brandParentMongoMap.put(saleOrder.getGoodsId(), brandParent);
                            temporaryReceiveBillItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                            temporaryReceiveBillItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                            temporaryReceiveBillItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                        }

                        itemModels.add(temporaryReceiveBillItemModel);
                    }
                } else if (DERP_ORDER.SHELF_STATUS_2.equals(shelf.getState())) {
                    SaleShelfIdepotModel saleShelfIdepotModel = new SaleShelfIdepotModel();
                    saleShelfIdepotModel.setSaleShelfId(shelf.getId());
                    SaleShelfIdepotModel shelfIdepotModel = saleShelfIdepotDao.searchByModel(saleShelfIdepotModel);

                    if (shelfIdepotModel != null) {
                        SaleShelfIdepotItemModel shelfIdepotItemModel = new SaleShelfIdepotItemModel();
                        shelfIdepotItemModel.setSshelfIdepotId(shelfIdepotModel.getId());
                        List<SaleShelfIdepotItemModel> saleShelfModels = saleShelfIdepotItemDao.list(shelfIdepotItemModel);
                        for (SaleShelfIdepotItemModel saleOrder : saleShelfModels) {
                            if (saleOrder.getNormalNum() == null || saleOrder.getNormalNum().intValue() == 0) {
                                continue;
                            }
                            TobTemporaryReceiveBillItemModel temporaryReceiveBillItemModel = new TobTemporaryReceiveBillItemModel();
                            SaleOrderItemModel itemModel = saleOrderItemDao.searchById(saleOrder.getSaleItemId());
                            if (itemModel != null) {
                                BigDecimal amount = new BigDecimal(saleOrder.getNormalNum()).multiply(itemModel.getPrice());
                                receiveAmount = receiveAmount.add(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
                                temporaryReceiveBillItemModel.setPrice(itemModel.getPrice());
                            }

                            temporaryReceiveBillItemModel.setGoodsId(saleOrder.getInGoodsId());
                            temporaryReceiveBillItemModel.setGoodsNo(saleOrder.getInGoodsNo());
                            temporaryReceiveBillItemModel.setGoodsName(saleOrder.getInGoodsName());
                            temporaryReceiveBillItemModel.setShelfNum(saleOrder.getNormalNum());

                            BrandParentMongo brandParent = brandParentMongoMap.get(saleOrder.getInGoodsId());
                            if (brandParent == null) {
                                brandParent = brandParentMongoDao.getBrandParentMongo(saleOrder.getInGoodsId());
                            }

                            if (brandParent != null) {
                                brandParentMongoMap.put(saleOrder.getInGoodsId(), brandParent);
                                temporaryReceiveBillItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                                temporaryReceiveBillItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                                temporaryReceiveBillItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                            }
                            itemModels.add(temporaryReceiveBillItemModel);
                        }
                    }
                }

                tobTemporaryReceiveBillModel.setShelfAmount(receiveAmount);
                tobTemporaryReceiveBillModel.setCreateDate(TimeUtils.getNow());

                SaleSdOrderModel sdOrderModel = new SaleSdOrderModel();
                sdOrderModel.setOrderId(shelf.getId());
                sdOrderModel.setOrderType(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1);
                List<SaleSdOrderModel> saleSdOrderModels = saleSdOrderDao.list(sdOrderModel);

                for (SaleSdOrderModel saleSdOrderModel : saleSdOrderModels) {
                    SaleSdOrderItemModel saleSdOrderItemModel = new SaleSdOrderItemModel();
                    saleSdOrderItemModel.setSaleSdOrderId(saleSdOrderModel.getId());
                    List<SaleSdOrderItemModel> saleSdOrderItemModels = saleSdOrderItemDao.list(saleSdOrderItemModel);

                    for (SaleSdOrderItemModel sdOrderItemModel : saleSdOrderItemModels) {
                        TobTemporaryReceiveBillRebateItemModel receiveBillRebateItemModel = new TobTemporaryReceiveBillRebateItemModel();
                        receiveBillRebateItemModel.setRelSdCode(saleSdOrderModel.getCode());
                        receiveBillRebateItemModel.setGoodsId(sdOrderItemModel.getGoodsId());
                        receiveBillRebateItemModel.setGoodsNo(sdOrderItemModel.getGoodsNo());
                        receiveBillRebateItemModel.setGoodsName(sdOrderItemModel.getGoodsName());
                        receiveBillRebateItemModel.setShelfNum(sdOrderItemModel.getNum());
                        receiveBillRebateItemModel.setPrice(sdOrderItemModel.getPrice());
                        receiveBillRebateItemModel.setSdTypeId(saleSdOrderModel.getSdTypeId());
                        receiveBillRebateItemModel.setSdTypeName(saleSdOrderModel.getSdTypeName());
                        receiveBillRebateItemModel.setRebateAmount(sdOrderItemModel.getSdAmount());
                        receiveBillRebateItemModel.setSdRatio(sdOrderItemModel.getSdRatio());
                        receiveBillRebateItemModel.setSdRatio(sdOrderItemModel.getSdRatio());
                        receiveBillRebateItemModel.setIsWriteOff(saleSdOrderModel.getIsWriteOff());
                        receiveBillRebateItemModel.setOriginalSaleSdOrderCode(saleSdOrderModel.getOriginalSaleSdOrderCode());

                        BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(sdOrderItemModel.getGoodsId());
                        if (brandParent != null) {
                            receiveBillRebateItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                            receiveBillRebateItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                            receiveBillRebateItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                            receiveBillRebateItemModel.setBrandId(brandParent.getBrandParentId());
                            receiveBillRebateItemModel.setBrandName(brandParent.getName());
                        }

                        //费项
                        SdSaleTypeModel sdSaleType = sdSaleTypeDao.searchById(saleSdOrderModel.getSdTypeId());
                        if (sdSaleType != null) {
                            receiveBillRebateItemModel.setProjectId(sdSaleType.getProjectId());
                            receiveBillRebateItemModel.setProjectName(sdSaleType.getProjectName());
                        }
                        rebateItemModels.add(receiveBillRebateItemModel);
                        rebateReceiveAmount = rebateReceiveAmount.add(sdOrderItemModel.getSdAmount());
                    }
                }

                tobTemporaryReceiveBillModel.setShelfRebateAmount(rebateReceiveAmount);

                if (itemModels.isEmpty() && rebateItemModels.isEmpty()) {
                    continue;
                }

                //保存tob暂估
                Long id = tobTemporaryReceiveBillDao.save(tobTemporaryReceiveBillModel);

                //批量生成表体
                if (!itemModels.isEmpty()) {
                    for (TobTemporaryReceiveBillItemModel temporaryReceiveBillItemModel : itemModels) {
                        temporaryReceiveBillItemModel.setBillId(id);
                    }
                    tobTemporaryReceiveBillItemDao.batchSave(itemModels);
                }
                if (!rebateItemModels.isEmpty()) {
                    for (TobTemporaryReceiveBillRebateItemModel billRebateItemModel : rebateItemModels) {
                        billRebateItemModel.setBillId(id);
                    }
                    tobTemporaryReceiveBillRebateItemDao.batchSave(rebateItemModels);
                }

            } else { // 更新返利明细

                BigDecimal rebateReceiveAmount = new BigDecimal("0");

                //根据销售sd单号查询销售sd单
                SaleSdOrderModel sdOrderModel = new SaleSdOrderModel();
                sdOrderModel.setOrderId(shelf.getId());
                sdOrderModel.setOrderType(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1);
                List<SaleSdOrderModel> saleSdOrderModels = saleSdOrderDao.list(sdOrderModel);

                //已生成的销售sd单
                TobTemporaryReceiveBillRebateItemModel rebateItemModel = new TobTemporaryReceiveBillRebateItemModel();
                rebateItemModel.setBillId(exist.getId());
                List<TobTemporaryReceiveBillRebateItemModel> rebateItemModels = tobTemporaryReceiveBillRebateItemDao.list(rebateItemModel);

                //待修改的返利明细
                Map<String, TobTemporaryReceiveBillRebateItemModel> updateRebateItemModelMap = new HashMap<>();
                //已生成的销售sd单号集合
                List<String> sdCodes = new ArrayList<>();
                for (TobTemporaryReceiveBillRebateItemModel billRebateItemModel : rebateItemModels) {

                    //若修改的sd单号存在则不归类到已生成的销售sd单号集合中
                    if (StringUtils.isNotBlank(saleSdCode) && billRebateItemModel.getRelSdCode().equals(saleSdCode)) {

                        String key = billRebateItemModel.getRelSdCode() + "_" + billRebateItemModel.getGoodsId();
                        updateRebateItemModelMap.put(key, billRebateItemModel);

                        continue;
                    }

                    sdCodes.add(billRebateItemModel.getRelSdCode());
                    rebateReceiveAmount = rebateReceiveAmount.add(billRebateItemModel.getRebateAmount());
                }

                List<TobTemporaryReceiveBillRebateItemModel> updateRebateItems = new ArrayList<>();
                for (SaleSdOrderModel saleSdOrderModel : saleSdOrderModels) {

                    //如果sd单已生成返利则不生成
                    if (sdCodes.contains(saleSdOrderModel.getCode())) {
                        continue;
                    }

                    SaleSdOrderItemModel saleSdOrderItemModel = new SaleSdOrderItemModel();
                    saleSdOrderItemModel.setSaleSdOrderId(saleSdOrderModel.getId());
                    List<SaleSdOrderItemModel> saleSdOrderItemModels = saleSdOrderItemDao.list(saleSdOrderItemModel);

                    //如果是修改的sd单
                    if (StringUtils.isNotBlank(saleSdCode) && saleSdOrderModel.getCode().equals(saleSdCode)) {
                        for (SaleSdOrderItemModel sdOrderItemModel : saleSdOrderItemModels) {
                            String key = saleSdOrderModel.getCode() + "_" + sdOrderItemModel.getGoodsId();
                            TobTemporaryReceiveBillRebateItemModel receiveBillRebateItemModel = updateRebateItemModelMap.get(key);

                            if (receiveBillRebateItemModel != null) {
                                receiveBillRebateItemModel.setShelfNum(sdOrderItemModel.getNum());
                                receiveBillRebateItemModel.setPrice(sdOrderItemModel.getPrice());
                                receiveBillRebateItemModel.setRebateAmount(sdOrderItemModel.getSdAmount());
                                receiveBillRebateItemModel.setSdRatio(sdOrderItemModel.getSdRatio());

                                tobTemporaryReceiveBillRebateItemDao.modify(receiveBillRebateItemModel);

                                rebateReceiveAmount = rebateReceiveAmount.add(sdOrderItemModel.getSdAmount());
                            }
                        }

                    } else {
                        for (SaleSdOrderItemModel sdOrderItemModel : saleSdOrderItemModels) {
                            TobTemporaryReceiveBillRebateItemModel receiveBillRebateItemModel = new TobTemporaryReceiveBillRebateItemModel();
                            receiveBillRebateItemModel.setBillId(exist.getId());
                            receiveBillRebateItemModel.setRelSdCode(saleSdOrderModel.getCode());
                            receiveBillRebateItemModel.setGoodsId(sdOrderItemModel.getGoodsId());
                            receiveBillRebateItemModel.setGoodsNo(sdOrderItemModel.getGoodsNo());
                            receiveBillRebateItemModel.setGoodsName(sdOrderItemModel.getGoodsName());
                            receiveBillRebateItemModel.setShelfNum(sdOrderItemModel.getNum());
                            receiveBillRebateItemModel.setPrice(sdOrderItemModel.getPrice());
                            receiveBillRebateItemModel.setSdTypeId(saleSdOrderModel.getSdTypeId());
                            receiveBillRebateItemModel.setSdTypeName(saleSdOrderModel.getSdTypeName());
                            receiveBillRebateItemModel.setRebateAmount(sdOrderItemModel.getSdAmount());
                            receiveBillRebateItemModel.setSdRatio(sdOrderItemModel.getSdRatio());
                            receiveBillRebateItemModel.setIsWriteOff(saleSdOrderModel.getIsWriteOff());
                            receiveBillRebateItemModel.setOriginalSaleSdOrderCode(saleSdOrderModel.getOriginalSaleSdOrderCode());

                            BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(sdOrderItemModel.getGoodsId());
                            if (brandParent != null) {
                                receiveBillRebateItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                                receiveBillRebateItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                                receiveBillRebateItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                                receiveBillRebateItemModel.setBrandId(brandParent.getBrandParentId());
                                receiveBillRebateItemModel.setBrandName(brandParent.getName());
                            }
                            //费项
                            SdSaleTypeModel sdSaleType = sdSaleTypeDao.searchById(saleSdOrderModel.getSdTypeId());
                            if (sdSaleType != null) {
                                receiveBillRebateItemModel.setProjectId(sdSaleType.getProjectId());
                                receiveBillRebateItemModel.setProjectName(sdSaleType.getProjectName());
                            }

                            updateRebateItems.add(receiveBillRebateItemModel);
                            rebateReceiveAmount = rebateReceiveAmount.add(sdOrderItemModel.getSdAmount());
                        }
                    }

                }

                if (!updateRebateItems.isEmpty()) {
                    tobTemporaryReceiveBillRebateItemDao.batchSave(updateRebateItems);
                }

                TobTemporaryReceiveBillModel updateModel = new TobTemporaryReceiveBillModel();
                updateModel.setId(exist.getId());
                updateModel.setShelfRebateAmount(rebateReceiveAmount);
                tobTemporaryReceiveBillDao.modify(updateModel);

            }
        }

        //更新上架单状态
        if (!shelfCodes.isEmpty()) {
            shelfDao.batchUpdateStatus(DERP_ORDER.SHELF_ISGENERATE_1, shelfCodes);
        }
    }


    /**
     * @Description 销售退货单生成tob暂估
     * @Param shelfDTOList
     */
    private void generateSaleReturnToBTempOrder(List<SaleReturnOrderDTO> saleReturnOrderDTOS, String saleSdCode) throws Exception {
        //生成的tob暂估记录对应的销售退货单集合
        List<String> saleReturnCodes = new ArrayList<>();
        Map<Long, BrandParentMongo> brandParentMongoMap = new HashMap<>();

        for (SaleReturnOrderDTO saleReturnOrderDTO : saleReturnOrderDTOS) {

            //查询暂估是否已经生成
            TobTemporaryReceiveBillModel billModel = new TobTemporaryReceiveBillModel();
            billModel.setOrderCode(saleReturnOrderDTO.getCode());
            TobTemporaryReceiveBillModel exist = tobTemporaryReceiveBillDao.searchByModel(billModel);

            //对应的销售退货入库单
            SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();
            saleReturnIdepotModel.setOrderId(saleReturnOrderDTO.getId());
            SaleReturnIdepotModel returnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);

            if (returnIdepotModel == null) {
                continue;
            }

            if (exist == null) {
                saleReturnCodes.add(saleReturnOrderDTO.getCode());

                TobTemporaryReceiveBillModel tobTemporaryReceiveBillModel = new TobTemporaryReceiveBillModel();
                List<TobTemporaryReceiveBillItemModel> itemModels = new ArrayList<>();
                List<TobTemporaryReceiveBillRebateItemModel> rebateItemModels = new ArrayList<>();

                tobTemporaryReceiveBillModel.setSaleType(DERP_ORDER.TOBTEMPRECEIVEBILL_SALETYPE_3);
                tobTemporaryReceiveBillModel.setOrderType(DERP_ORDER.TOBTEMPRECEIVEBILL_ORDERTYPE_2);
                tobTemporaryReceiveBillModel.setMerchantId(saleReturnOrderDTO.getMerchantId());
                tobTemporaryReceiveBillModel.setMerchantName(saleReturnOrderDTO.getMerchantName());
                tobTemporaryReceiveBillModel.setCustomerId(saleReturnOrderDTO.getCustomerId());
                tobTemporaryReceiveBillModel.setCustomerName(saleReturnOrderDTO.getCustomerName());
                tobTemporaryReceiveBillModel.setBuId(saleReturnOrderDTO.getBuId());
                tobTemporaryReceiveBillModel.setBuName(saleReturnOrderDTO.getBuName());
                tobTemporaryReceiveBillModel.setCurrency(saleReturnOrderDTO.getCurrency());
                tobTemporaryReceiveBillModel.setPoNo(saleReturnOrderDTO.getPoNo());
                tobTemporaryReceiveBillModel.setOrderCode(saleReturnOrderDTO.getCode());
                tobTemporaryReceiveBillModel.setStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
                tobTemporaryReceiveBillModel.setRebateStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
                tobTemporaryReceiveBillModel.setShelfDate(returnIdepotModel.getReturnInDate());
                tobTemporaryReceiveBillModel.setShelfCode(returnIdepotModel.getCode());

                //应收金额
                BigDecimal receiveAmount = new BigDecimal("0");
                //返利金额
                BigDecimal rebateReceiveAmount = new BigDecimal("0");

                //销售退货单表体
                SaleReturnOrderItemModel itemModel = new SaleReturnOrderItemModel();
                itemModel.setOrderId(saleReturnOrderDTO.getId());
                List<SaleReturnOrderItemModel> saleReturnOrderItemModels = saleReturnOrderItemDao.list(itemModel);

                //po+商品id对应的销售价格
                Map<String , BigDecimal> poGoodsPriceMap = new HashMap<>();
                for (SaleReturnOrderItemModel saleReturnOrderItemModel : saleReturnOrderItemModels) {
                    String key = saleReturnOrderItemModel.getPoNo() + "_" + saleReturnOrderItemModel.getInGoodsId();
                    poGoodsPriceMap.put(key, saleReturnOrderItemModel.getPrice());
                }

                //对应销售退货入库单表体
                SaleReturnIdepotItemModel idepotItemModel = new SaleReturnIdepotItemModel();
                idepotItemModel.setSreturnIdepotId(returnIdepotModel.getId());
                List<SaleReturnIdepotItemModel> idepotItemModels = saleReturnIdepotItemDao.list(idepotItemModel);

                for (SaleReturnIdepotItemModel saleReturnIdepotItemModel : idepotItemModels) {
                    TobTemporaryReceiveBillItemModel receiveBillItemModel = new TobTemporaryReceiveBillItemModel();
                    receiveBillItemModel.setGoodsId(saleReturnIdepotItemModel.getInGoodsId());
                    receiveBillItemModel.setGoodsNo(saleReturnIdepotItemModel.getInGoodsNo());
                    receiveBillItemModel.setGoodsName(saleReturnIdepotItemModel.getInGoodsName());
                    receiveBillItemModel.setPoNo(saleReturnIdepotItemModel.getPoNo());

                    Integer num = saleReturnIdepotItemModel.getReturnNum() == null ? 0 : saleReturnIdepotItemModel.getReturnNum();
                    Integer wornNum = saleReturnIdepotItemModel.getWornNum() == null ? 0 : saleReturnIdepotItemModel.getWornNum();
                    num += wornNum;

                    BigDecimal price = poGoodsPriceMap.get(saleReturnIdepotItemModel.getPoNo() + "_" + saleReturnIdepotItemModel.getInGoodsId()) == null ? BigDecimal.ZERO : poGoodsPriceMap.get(saleReturnIdepotItemModel.getPoNo() + "_" + saleReturnIdepotItemModel.getInGoodsId());

                    receiveBillItemModel.setShelfNum(num);
                    receiveBillItemModel.setPrice(price.negate());

                    BrandParentMongo brandParent = brandParentMongoMap.get(saleReturnIdepotItemModel.getInGoodsId());
                    if (brandParent == null) {
                        brandParent = brandParentMongoDao.getBrandParentMongo(saleReturnIdepotItemModel.getInGoodsId());
                    }

                    if (brandParent != null) {
                        brandParentMongoMap.put(saleReturnIdepotItemModel.getInGoodsId(), brandParent);
                        receiveBillItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                        receiveBillItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                        receiveBillItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                    }
                    receiveAmount = receiveAmount.add(new BigDecimal(num).multiply(price.negate()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    itemModels.add(receiveBillItemModel);
                }

                //销售退货单对应的销售sd单
                SaleSdOrderModel sdOrderModel = new SaleSdOrderModel();
                sdOrderModel.setOrderId(returnIdepotModel.getId());
                sdOrderModel.setOrderType(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2);
                List<SaleSdOrderModel> saleSdOrderModels = saleSdOrderDao.list(sdOrderModel);

                for (SaleSdOrderModel saleSdOrderModel : saleSdOrderModels) {
                    SaleSdOrderItemModel saleSdOrderItemModel = new SaleSdOrderItemModel();
                    saleSdOrderItemModel.setSaleSdOrderId(saleSdOrderModel.getId());
                    List<SaleSdOrderItemModel> saleSdOrderItemModels = saleSdOrderItemDao.list(saleSdOrderItemModel);

                    for (SaleSdOrderItemModel sdOrderItemModel : saleSdOrderItemModels) {
                        TobTemporaryReceiveBillRebateItemModel receiveBillRebateItemModel = new TobTemporaryReceiveBillRebateItemModel();
                        receiveBillRebateItemModel.setRelSdCode(saleSdOrderModel.getCode());
                        receiveBillRebateItemModel.setGoodsId(sdOrderItemModel.getGoodsId());
                        receiveBillRebateItemModel.setGoodsNo(sdOrderItemModel.getGoodsNo());
                        receiveBillRebateItemModel.setGoodsName(sdOrderItemModel.getGoodsName());
                        receiveBillRebateItemModel.setShelfNum(sdOrderItemModel.getNum());
                        receiveBillRebateItemModel.setPrice(sdOrderItemModel.getPrice().negate());
                        receiveBillRebateItemModel.setSdTypeId(saleSdOrderModel.getSdTypeId());
                        receiveBillRebateItemModel.setSdTypeName(saleSdOrderModel.getSdTypeName());
                        receiveBillRebateItemModel.setRebateAmount(sdOrderItemModel.getSdAmount().negate());
                        receiveBillRebateItemModel.setSdRatio(sdOrderItemModel.getSdRatio());
                        receiveBillRebateItemModel.setIsWriteOff(saleSdOrderModel.getIsWriteOff());
                        receiveBillRebateItemModel.setOriginalSaleSdOrderCode(saleSdOrderModel.getOriginalSaleSdOrderCode());

                        BrandParentMongo brandParent = brandParentMongoMap.get(sdOrderItemModel.getGoodsId());
                        if (brandParent == null) {
                            brandParent = brandParentMongoDao.getBrandParentMongo(sdOrderItemModel.getGoodsId());
                        }

                        if (brandParent != null) {
                            brandParentMongoMap.put(sdOrderItemModel.getGoodsId(), brandParent);
                            receiveBillRebateItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                            receiveBillRebateItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                            receiveBillRebateItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                            receiveBillRebateItemModel.setBrandId(brandParent.getBrandParentId());
                            receiveBillRebateItemModel.setBrandName(brandParent.getName());
                        }

                        //费项
                        SdSaleTypeModel sdSaleType = sdSaleTypeDao.searchById(saleSdOrderModel.getSdTypeId());
                        if (sdSaleType != null) {
                            receiveBillRebateItemModel.setProjectId(sdSaleType.getProjectId());
                            receiveBillRebateItemModel.setProjectName(sdSaleType.getProjectName());
                        }
                        rebateItemModels.add(receiveBillRebateItemModel);
                        rebateReceiveAmount = rebateReceiveAmount.add(sdOrderItemModel.getSdAmount().negate());
                    }
                }

                tobTemporaryReceiveBillModel.setShelfAmount(receiveAmount);
                tobTemporaryReceiveBillModel.setShelfRebateAmount(rebateReceiveAmount);

                if (itemModels.isEmpty() && rebateItemModels.isEmpty()) {
                    continue;
                }

                //保存tob暂估
                Long id = tobTemporaryReceiveBillDao.save(tobTemporaryReceiveBillModel);

                //批量生成表体
                if (!itemModels.isEmpty()) {
                    for (TobTemporaryReceiveBillItemModel temporaryReceiveBillItemModel : itemModels) {
                        temporaryReceiveBillItemModel.setBillId(id);
                    }
                    tobTemporaryReceiveBillItemDao.batchSave(itemModels);
                }
                if (!rebateItemModels.isEmpty()) {
                    for (TobTemporaryReceiveBillRebateItemModel billRebateItemModel : rebateItemModels) {
                        billRebateItemModel.setBillId(id);
                    }
                    tobTemporaryReceiveBillRebateItemDao.batchSave(rebateItemModels);
                }

            } else { // 更新返利明细

                BigDecimal rebateReceiveAmount = new BigDecimal("0");

                //根据销售sd单号查询销售sd单
                SaleSdOrderModel sdOrderModel = new SaleSdOrderModel();
                sdOrderModel.setOrderId(returnIdepotModel.getId());
                sdOrderModel.setOrderType(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2);
                List<SaleSdOrderModel> saleSdOrderModels = saleSdOrderDao.list(sdOrderModel);

                //已生成的销售sd单
                TobTemporaryReceiveBillRebateItemModel rebateItemModel = new TobTemporaryReceiveBillRebateItemModel();
                rebateItemModel.setBillId(exist.getId());
                List<TobTemporaryReceiveBillRebateItemModel> rebateItemModels = tobTemporaryReceiveBillRebateItemDao.list(rebateItemModel);

                //待修改的返利明细
                Map<String, TobTemporaryReceiveBillRebateItemModel> updateRebateItemModelMap = new HashMap<>();
                //已生成的销售sd单号集合
                List<String> sdCodes = new ArrayList<>();
                for (TobTemporaryReceiveBillRebateItemModel billRebateItemModel : rebateItemModels) {

                    //若修改的sd单号存在则不归类到已生成的销售sd单号集合中
                    if (StringUtils.isNotBlank(saleSdCode) && billRebateItemModel.getRelSdCode().equals(saleSdCode)) {

                        String key = billRebateItemModel.getRelSdCode() + "_" + billRebateItemModel.getGoodsId();
                        updateRebateItemModelMap.put(key, billRebateItemModel);

                        continue;
                    }

                    sdCodes.add(billRebateItemModel.getRelSdCode());
                    rebateReceiveAmount = rebateReceiveAmount.add(billRebateItemModel.getRebateAmount());
                }

                List<TobTemporaryReceiveBillRebateItemModel> updateRebateItems = new ArrayList<>();
                for (SaleSdOrderModel saleSdOrderModel : saleSdOrderModels) {

                    //如果sd单已生成返利则不生成
                    if (sdCodes.contains(saleSdOrderModel.getCode())) {
                        continue;
                    }

                    SaleSdOrderItemModel saleSdOrderItemModel = new SaleSdOrderItemModel();
                    saleSdOrderItemModel.setSaleSdOrderId(saleSdOrderModel.getId());
                    List<SaleSdOrderItemModel> saleSdOrderItemModels = saleSdOrderItemDao.list(saleSdOrderItemModel);

                    //如果是修改的sd单
                    if (StringUtils.isNotBlank(saleSdCode) && saleSdOrderModel.getCode().equals(saleSdCode)) {
                        for (SaleSdOrderItemModel sdOrderItemModel : saleSdOrderItemModels) {
                            String key = saleSdOrderModel.getCode() + "_" + sdOrderItemModel.getGoodsId();
                            TobTemporaryReceiveBillRebateItemModel receiveBillRebateItemModel = updateRebateItemModelMap.get(key);

                            if (receiveBillRebateItemModel != null) {
                                receiveBillRebateItemModel.setShelfNum(sdOrderItemModel.getNum());
                                receiveBillRebateItemModel.setPrice(sdOrderItemModel.getPrice());
                                receiveBillRebateItemModel.setRebateAmount(sdOrderItemModel.getSdAmount());
                                receiveBillRebateItemModel.setSdRatio(sdOrderItemModel.getSdRatio());
                                tobTemporaryReceiveBillRebateItemDao.modify(receiveBillRebateItemModel);

                                rebateReceiveAmount = rebateReceiveAmount.add(sdOrderItemModel.getSdAmount());
                            }
                        }

                    } else {
                        for (SaleSdOrderItemModel sdOrderItemModel : saleSdOrderItemModels) {
                            TobTemporaryReceiveBillRebateItemModel receiveBillRebateItemModel = new TobTemporaryReceiveBillRebateItemModel();
                            receiveBillRebateItemModel.setBillId(exist.getId());
                            receiveBillRebateItemModel.setRelSdCode(saleSdOrderModel.getCode());
                            receiveBillRebateItemModel.setGoodsId(sdOrderItemModel.getGoodsId());
                            receiveBillRebateItemModel.setGoodsNo(sdOrderItemModel.getGoodsNo());
                            receiveBillRebateItemModel.setGoodsName(sdOrderItemModel.getGoodsName());
                            receiveBillRebateItemModel.setShelfNum(sdOrderItemModel.getNum());
                            receiveBillRebateItemModel.setPrice(sdOrderItemModel.getPrice().negate());
                            receiveBillRebateItemModel.setSdTypeId(saleSdOrderModel.getSdTypeId());
                            receiveBillRebateItemModel.setSdTypeName(saleSdOrderModel.getSdTypeName());
                            receiveBillRebateItemModel.setRebateAmount(sdOrderItemModel.getSdAmount().negate());
                            receiveBillRebateItemModel.setSdRatio(sdOrderItemModel.getSdRatio());
                            receiveBillRebateItemModel.setIsWriteOff(saleSdOrderModel.getIsWriteOff());
                            receiveBillRebateItemModel.setOriginalSaleSdOrderCode(saleSdOrderModel.getOriginalSaleSdOrderCode());

                            BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongo(sdOrderItemModel.getGoodsId());
                            if (brandParent != null) {
                                receiveBillRebateItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                                receiveBillRebateItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                                receiveBillRebateItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                                receiveBillRebateItemModel.setBrandId(brandParent.getBrandParentId());
                                receiveBillRebateItemModel.setBrandName(brandParent.getName());
                            }
                            //费项
                            SdSaleTypeModel sdSaleType = sdSaleTypeDao.searchById(saleSdOrderModel.getSdTypeId());
                            if (sdSaleType != null) {
                                receiveBillRebateItemModel.setProjectId(sdSaleType.getProjectId());
                                receiveBillRebateItemModel.setProjectName(sdSaleType.getProjectName());
                            }

                            updateRebateItems.add(receiveBillRebateItemModel);
                            rebateReceiveAmount = rebateReceiveAmount.add(sdOrderItemModel.getSdAmount().negate());
                        }
                    }

                }

                if (!updateRebateItems.isEmpty()) {
                    tobTemporaryReceiveBillRebateItemDao.batchSave(updateRebateItems);
                }

                TobTemporaryReceiveBillModel updateModel = new TobTemporaryReceiveBillModel();
                updateModel.setId(exist.getId());
                updateModel.setShelfRebateAmount(rebateReceiveAmount);
                tobTemporaryReceiveBillDao.modify(updateModel);

            }
        }

        //更新销售退货单状态
        if (!saleReturnCodes.isEmpty()) {
            saleReturnOrderDao.batchUpdateStatus(DERP_ORDER.SHELF_ISGENERATE_1, saleReturnCodes);
        }
    }

    /**
     * @Description tob应收暂估明细红冲暂估自动核销
     */
    private void verifyWriteOffItems(TobTemporaryReceiveBillDTO tobTemporaryReceiveBillDTO) throws Exception {

        List<String> shelfCodes = new ArrayList<>();

        if (StringUtils.isNotBlank(tobTemporaryReceiveBillDTO.getShelfCodes())) {
            shelfCodes = Arrays.asList(tobTemporaryReceiveBillDTO.getShelfCodes().split(","));
        }

        List<TobTemporaryReceiveBillDTO> tobTemporaryReceiveBillDTOS = tobTemporaryReceiveBillDao.listByShelfCodes(shelfCodes, DERP_ORDER.TOBTEMPRECEIVEBILL_ISWRITEOFF_1, tobTemporaryReceiveBillDTO.getMerchantId());

        List<String> oriShelfCode = new ArrayList<>();
        Map<String, TobTemporaryReceiveBillDTO> oriTobDTOMap = new HashMap<>();

        for (TobTemporaryReceiveBillDTO billDTO : tobTemporaryReceiveBillDTOS) {
            oriShelfCode.add(billDTO.getOriginalShelfCode());
            oriTobDTOMap.put(billDTO.getOriginalShelfCode(), billDTO);
        }

        if (tobTemporaryReceiveBillDTOS.size() == 0) {
            return;
        }

        List<Long> updateIds = new ArrayList<>();
        List<TobTemporaryReceiveBillDTO> oriTobTempDTOs = tobTemporaryReceiveBillDao.listByShelfCodes(oriShelfCode, DERP_ORDER.TOBTEMPRECEIVEBILL_ISWRITEOFF_0, tobTemporaryReceiveBillDTO.getMerchantId());

        for (TobTemporaryReceiveBillDTO billDTO : oriTobTempDTOs) {
            TobTemporaryReceiveBillDTO oriBillDTO = oriTobDTOMap.get(billDTO.getShelfCode());

            if (oriBillDTO == null) {
                continue;
            }
            updateIds.add(billDTO.getId());
            updateIds.add(oriBillDTO.getId());
        }

        if (updateIds.size() == 0) {
            return;
        }

        List<TobTemporaryReceiveBillItemModel> itemModels = tobTemporaryReceiveBillItemDao.listByBillIds(updateIds);

        List<TobTempVerifyRelModel> relModels = new ArrayList<>();
        for (TobTemporaryReceiveBillItemModel itemModel : itemModels) {

            TobTempVerifyRelModel relModel = new TobTempVerifyRelModel();
            relModel.setTobId(itemModel.getBillId());
            relModel.setTobItemId(itemModel.getId());
            relModel.setVerifiyAmount(itemModel.getPrice().multiply(new BigDecimal(itemModel.getShelfNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
            relModel.setGoodsId(itemModel.getGoodsId());
            relModel.setType(DERP_ORDER.TOBTEMPVERIFYREL_TYPE_0);
            relModel.setCreateDate(TimeUtils.getNow());

            relModels.add(relModel);
        }

        if (relModels.size() > 0) {
            tobTempVerifyRelDao.batchSave(relModels);
        }

        tobTemporaryReceiveBillDao.batchUpdateStatus(updateIds, DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_5, null);
    }

    /**
     * @Description “上架单”创建的应收单核销tob暂估明细
     */
    private void shelfItemVerifyItems(ReceiveBillModel receiveBillModel, TobTemporaryReceiveBillDTO tobTemporaryReceiveBillDTO, List<Long> updateTobIds) throws Exception {
        //查询单据类型为“上架单”，状态为“待核销、部分核销、已核销“且应收明细“部分核销、未核销”的应收账单明细
        receiveBillModel.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_1);
        List<ReceiveBillItemDTO> shelfReceiveItems = receiveBillItemDao.listVerifyItems(receiveBillModel);

        Map<String, List<ReceiveBillItemDTO>> shelfReceiveBillMap = new HashMap<>();
        //以应收明细中的“上架单号+商品货号”查询To B暂估核销列表中的暂估应收明细，找到匹配的“上架单号+商品货号”数据记录进行核销
        for (ReceiveBillItemDTO shelfReceiveItem : shelfReceiveItems) {
            String key = shelfReceiveItem.getCode() + "_" + shelfReceiveItem.getGoodsId();

            List<ReceiveBillItemDTO> existItemModes = new ArrayList<>();
            if (shelfReceiveBillMap.containsKey(key)) {
                existItemModes.addAll(shelfReceiveBillMap.get(key));
            }
            existItemModes.add(shelfReceiveItem);
            shelfReceiveBillMap.put(key, existItemModes);
        }

        List<TobTemporaryReceiveBillItemDTO> toBeVerifyItems = tobTemporaryReceiveBillItemDao.listToBeVerifyItems(tobTemporaryReceiveBillDTO);

        Map<String, List<TobTemporaryReceiveBillItemDTO>> toBeVerifyItemMap = new HashMap<>();
        for (TobTemporaryReceiveBillItemDTO toBeVerifyItem : toBeVerifyItems) {
            String key = toBeVerifyItem.getShelfCode() + "_" + toBeVerifyItem.getGoodsId();

            List<TobTemporaryReceiveBillItemDTO> existItemModes = new ArrayList<>();
            if (toBeVerifyItemMap.containsKey(key)) {
                existItemModes.addAll(toBeVerifyItemMap.get(key));
            }
            existItemModes.add(toBeVerifyItem);
            toBeVerifyItemMap.put(key, existItemModes);
        }

        //tob应收核销暂估关联关系
        List<TobTempVerifyRelModel> tobTempVerifyRelModels = new ArrayList<>();
        for (String key : shelfReceiveBillMap.keySet()) {

            if (!toBeVerifyItemMap.containsKey(key)) {
                continue;
            }

            List<ReceiveBillItemDTO> receiveBillItemDTOS = shelfReceiveBillMap.get(key);

            List<TobTemporaryReceiveBillItemDTO> tobTemporaryReceiveBillItemDTOS = toBeVerifyItemMap.get(key);

            for (ReceiveBillItemDTO receiveBillItemDTO : receiveBillItemDTOS) {
                //应收明细待核销金额
                BigDecimal beVerifyAmount = receiveBillItemDTO.getBeVerifyAmount();

                for (TobTemporaryReceiveBillItemDTO tobTempItem : tobTemporaryReceiveBillItemDTOS) {

                    if (beVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }

                    //暂估待核销金额
                    BigDecimal tobTempBeVerifyAmount = tobTempItem.getBeVerifyAmount();

                    if (tobTempBeVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }

                    TobTempVerifyRelModel tobTempVerifyRelModel = new TobTempVerifyRelModel();
                    tobTempVerifyRelModel.setReceiveItemId(receiveBillItemDTO.getId());
                    tobTempVerifyRelModel.setReceiveId(receiveBillItemDTO.getBillId());
                    tobTempVerifyRelModel.setTobItemId(tobTempItem.getId());
                    tobTempVerifyRelModel.setTobId(tobTempItem.getBillId());
                    tobTempVerifyRelModel.setGoodsId(tobTempItem.getGoodsId());
                    tobTempVerifyRelModel.setType(DERP_ORDER.TOBTEMPVERIFYREL_TYPE_0);
                    tobTempVerifyRelModel.setReceiveCode(receiveBillItemDTO.getReceiveCode());
                    tobTempVerifyRelModel.setCreditDate(receiveBillItemDTO.getCreditDate());

                    if (beVerifyAmount.abs().compareTo(tobTempBeVerifyAmount.abs()) > -1) {
                        tobTempVerifyRelModel.setVerifiyAmount(tobTempBeVerifyAmount);
                        tobTempItem.setBeVerifyAmount(BigDecimal.ZERO);
                        beVerifyAmount = beVerifyAmount.subtract(tobTempBeVerifyAmount);
                    } else {
                        tobTempVerifyRelModel.setVerifiyAmount(beVerifyAmount);
                        tobTempBeVerifyAmount = tobTempBeVerifyAmount.subtract(beVerifyAmount);
                        tobTempItem.setBeVerifyAmount(tobTempBeVerifyAmount);
                        beVerifyAmount = BigDecimal.ZERO;
                    }
                    tobTempVerifyRelModels.add(tobTempVerifyRelModel);

                    updateTobIds.add(tobTempItem.getBillId());
                }
            }

        }

        int pageSize = 1000;
        //批量新增tob应收核销暂估关联关系
        for (int i = 0; i < tobTempVerifyRelModels.size(); ) {
            int pageSub = (i + pageSize) < tobTempVerifyRelModels.size() ? (i + pageSize) : tobTempVerifyRelModels.size();
            tobTempVerifyRelDao.batchSave(tobTempVerifyRelModels.subList(i, pageSub));
            i = pageSub;
        }
    }

    /**
     * @Description “账单出库单”创建的应收单核销tob暂估明细
     * @Param
     */
    private void billOutItemVerifyItems(ReceiveBillModel receiveBillModel, TobTemporaryReceiveBillDTO tobTemporaryReceiveBillDTO, List<Long> updateTobIds) throws Exception {
        List<Long> billIds = new ArrayList<>();
        List<Long> tobBillIds = new ArrayList<>();
        Map<String, List<ReceiveBillItemDTO>> billOutReceiveBillMap = new HashMap<>();

        //查询单据类型为“账单出库单”，状态为“待核销、部分核销、已核销“且应收明细“部分核销、未核销”的应收账单明细
        receiveBillModel.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_2);
        List<ReceiveBillItemDTO> billOutReceiveItems = receiveBillItemDao.listVerifyItems(receiveBillModel);

        for (ReceiveBillItemDTO billItemDTO : billOutReceiveItems) {
            if (!billIds.contains(billItemDTO.getBillId())) {
                billIds.add(billItemDTO.getBillId());
            }
        }

        if(billIds.isEmpty()){
            return;
        }

        List<ReceiveBillDTO> receiveBillDTOS = receiveBillDao.listBillByRelIds(billIds);
        Map<Long, ReceiveBillDTO> receiveBillMap = new HashMap<>();

        for (ReceiveBillDTO receiveBillDTO : receiveBillDTOS) {
            receiveBillMap.put(receiveBillDTO.getId(), receiveBillDTO);
        }

        //以应收明细中的“客户+事业部+PO+商品货号”查询To B暂估核销列表中的暂估应收明细，找到匹配的“客户+事业部+PO+商品货号”数据记录进行核销
        for (ReceiveBillItemDTO billOutReceiveItem : billOutReceiveItems) {
            ReceiveBillDTO receiveBillDTO = receiveBillMap.get(billOutReceiveItem.getBillId());

            String key = receiveBillDTO.getMerchantId() + "_" + receiveBillDTO.getCustomerId() + "_"
                    + receiveBillDTO.getBuId() + "_" + billOutReceiveItem.getPoNo() + "_"
                    + billOutReceiveItem.getGoodsId();

            List<ReceiveBillItemDTO> existItemModes = new ArrayList<>();
            if (billOutReceiveBillMap.containsKey(key)) {
                existItemModes.addAll(billOutReceiveBillMap.get(key));
            }
            existItemModes.add(billOutReceiveItem);
            billOutReceiveBillMap.put(key, existItemModes);

        }

        List<TobTemporaryReceiveBillItemDTO> billOutToBeVerifyItems = tobTemporaryReceiveBillItemDao.listToBeVerifyItems(tobTemporaryReceiveBillDTO);
        Map<Long, TobTemporaryReceiveBillDTO> tobTemporaryReceiveBillDTOMap = new HashMap<>();
        for (TobTemporaryReceiveBillItemDTO toBeVerifyItem : billOutToBeVerifyItems) {
            tobBillIds.add(toBeVerifyItem.getBillId());
        }

        if (tobBillIds.size() > 0) {
            List<TobTemporaryReceiveBillDTO> tobTemporaryReceiveBillDTOS = tobTemporaryReceiveBillDao.listBillByRelIds(tobBillIds);
            for (TobTemporaryReceiveBillDTO receiveBillDTO : tobTemporaryReceiveBillDTOS) {
                tobTemporaryReceiveBillDTOMap.put(receiveBillDTO.getId(), receiveBillDTO);
            }
        }

        Map<String, List<TobTemporaryReceiveBillItemDTO>> billOutToBeVerifyItemMap = new HashMap<>();
        for (TobTemporaryReceiveBillItemDTO toBeVerifyItem : billOutToBeVerifyItems) {
            TobTemporaryReceiveBillDTO receiveBillDTO = tobTemporaryReceiveBillDTOMap.get(toBeVerifyItem.getBillId());
            String key = receiveBillDTO.getMerchantId() + "_" + receiveBillDTO.getCustomerId() + "_"
                    + receiveBillDTO.getBuId() + "_" + receiveBillDTO.getPoNo() + "_"
                    + toBeVerifyItem.getGoodsId();

            List<TobTemporaryReceiveBillItemDTO> existItemModes = new ArrayList<>();
            if (billOutToBeVerifyItemMap.containsKey(key)) {
                existItemModes.addAll(billOutToBeVerifyItemMap.get(key));
            }
            existItemModes.add(toBeVerifyItem);
            billOutToBeVerifyItemMap.put(key, existItemModes);
        }

        //tob应收核销暂估关联关系
        List<TobTempVerifyRelModel> billOutTobTempVerifyRelModels = new ArrayList<>();
        for (String key : billOutReceiveBillMap.keySet()) {

            if (!billOutToBeVerifyItemMap.containsKey(key)) {
                continue;
            }

            List<ReceiveBillItemDTO> receiveBillItemDTOS = billOutReceiveBillMap.get(key);

            List<TobTemporaryReceiveBillItemDTO> tobTemporaryReceiveBillItemDTOS = billOutToBeVerifyItemMap.get(key);

            for (ReceiveBillItemDTO receiveBillItemDTO : receiveBillItemDTOS) {
                //应收明细待核销金额
                BigDecimal beVerifyAmount = receiveBillItemDTO.getBeVerifyAmount();

                for (TobTemporaryReceiveBillItemDTO tobTempItem : tobTemporaryReceiveBillItemDTOS) {

                    if (beVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                        break;
                    }

                    //暂估待核销金额
                    BigDecimal tobTempBeVerifyAmount = tobTempItem.getBeVerifyAmount();

                    if (tobTempBeVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }

                    TobTempVerifyRelModel tobTempVerifyRelModel = new TobTempVerifyRelModel();
                    tobTempVerifyRelModel.setReceiveItemId(receiveBillItemDTO.getId());
                    tobTempVerifyRelModel.setReceiveId(receiveBillItemDTO.getBillId());
                    tobTempVerifyRelModel.setTobItemId(tobTempItem.getId());
                    tobTempVerifyRelModel.setTobId(tobTempItem.getBillId());
                    tobTempVerifyRelModel.setGoodsId(tobTempItem.getGoodsId());
                    tobTempVerifyRelModel.setType(DERP_ORDER.TOBTEMPVERIFYREL_TYPE_0);
                    tobTempVerifyRelModel.setReceiveCode(receiveBillItemDTO.getReceiveCode());
                    tobTempVerifyRelModel.setCreditDate(receiveBillItemDTO.getCreditDate());

                    if (beVerifyAmount.abs().compareTo(tobTempBeVerifyAmount.abs()) > -1) {
                        tobTempVerifyRelModel.setVerifiyAmount(tobTempBeVerifyAmount);
                        tobTempItem.setBeVerifyAmount(BigDecimal.ZERO);
                        beVerifyAmount = beVerifyAmount.subtract(tobTempBeVerifyAmount);
                    } else {
                        tobTempVerifyRelModel.setVerifiyAmount(beVerifyAmount);
                        tobTempBeVerifyAmount = tobTempBeVerifyAmount.subtract(beVerifyAmount);
                        tobTempItem.setBeVerifyAmount(tobTempBeVerifyAmount);
                        beVerifyAmount = BigDecimal.ZERO;
                    }
                    billOutTobTempVerifyRelModels.add(tobTempVerifyRelModel);
                    updateTobIds.add(tobTempItem.getBillId());
                }
            }

        }

        int pageSize = 1000;
        //批量新增tob应收核销暂估关联关系
        for (int i = 0; i < billOutTobTempVerifyRelModels.size(); ) {
            int pageSub = (i + pageSize) < billOutTobTempVerifyRelModels.size() ? (i + pageSize) : billOutTobTempVerifyRelModels.size();
            tobTempVerifyRelDao.batchSave(billOutTobTempVerifyRelModels.subList(i, pageSub));
            i = pageSub;
        }
    }


    /**
     * @Description “销售订单”创建的应收单核销tob暂估明细
     */
    private void saleOrderItemVerifyItems(ReceiveBillModel receiveBillModel, TobTemporaryReceiveBillDTO tobTemporaryReceiveBillDTO, List<Long> updateTobIds) throws Exception {

        List<String> saleOrderCodes = Arrays.asList(receiveBillModel.getRelCode().split("&"));
        List<ShelfModel> allShelfModels = new ArrayList<>();

        //销售订单对应的上架单
        Map<String, List<ShelfModel>> saleCodeShelfMap = new HashMap<>();
        for (String saleOrderCode : saleOrderCodes) {
            ShelfModel shelfModel = new ShelfModel();
            shelfModel.setSaleOrderCode(saleOrderCode);

            List<ShelfModel> shelfModels = shelfDao.list(shelfModel);

            saleCodeShelfMap.put(saleOrderCode, shelfModels);
            allShelfModels.addAll(shelfModels);
        }

        Collections.sort(allShelfModels, new Comparator<ShelfModel>() {
            @Override
            public int compare(ShelfModel o1, ShelfModel o2) {
                return o1.getShelfDate().compareTo(o2.getShelfDate());
            }
        });

        //所有上架订单号
        List<String> shelfCodes = allShelfModels.stream().map(e -> e.getCode()).collect(Collectors.toList());

        //查询单据类型为“销售订单”，状态为“待核销、部分核销、已核销“且应收明细“部分核销、未核销”的应收账单明细
        receiveBillModel.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_4);
        List<ReceiveBillItemDTO> saleOrderReceiveItems = receiveBillItemDao.listVerifyItems(receiveBillModel);

        Map<String, List<Long>> shelfReceiveBillMap = new HashMap<>();
        Map<Long, ReceiveBillItemDTO> receiveBillItemDTOMap = new HashMap<>();
        //以应收明细中的“上架单号+商品货号”查询To B暂估核销列表中的暂估应收明细，找到匹配的“上架单号+商品货号”数据记录进行核销
        for (ReceiveBillItemDTO shelfReceiveItem : saleOrderReceiveItems) {

            List<ShelfModel> shelfModels = saleCodeShelfMap.get(shelfReceiveItem.getCode());

            if (shelfModels != null && shelfModels.size() > 0) {

                for (ShelfModel shelfModel : shelfModels) {
                    String key = shelfModel.getCode() + "_" + shelfReceiveItem.getGoodsId();

                    List<Long> existItemIds = new ArrayList<>();
                    if (shelfReceiveBillMap.containsKey(key)) {
                        existItemIds.addAll(shelfReceiveBillMap.get(key));
                    }
                    existItemIds.add(shelfReceiveItem.getId());
                    shelfReceiveBillMap.put(key, existItemIds);
                }
            }
            receiveBillItemDTOMap.put(shelfReceiveItem.getId(), shelfReceiveItem);
        }

        List<TobTempVerifyRelModel> tobTempVerifyRelModels = new ArrayList<>();
        for (ShelfModel shelfModel : allShelfModels) {

            tobTemporaryReceiveBillDTO.setShelfCode(shelfModel.getCode());
            List<TobTemporaryReceiveBillItemDTO> toBeVerifyItems = tobTemporaryReceiveBillItemDao.listToBeVerifyItems(tobTemporaryReceiveBillDTO);

            Map<String, List<TobTemporaryReceiveBillItemDTO>> toBeVerifyItemMap = new HashMap<>();
            for (TobTemporaryReceiveBillItemDTO toBeVerifyItem : toBeVerifyItems) {
                String key = toBeVerifyItem.getShelfCode() + "_" + toBeVerifyItem.getGoodsId();

                List<TobTemporaryReceiveBillItemDTO> existItemModes = new ArrayList<>();
                if (toBeVerifyItemMap.containsKey(key)) {
                    existItemModes.addAll(toBeVerifyItemMap.get(key));
                }
                existItemModes.add(toBeVerifyItem);
                toBeVerifyItemMap.put(key, existItemModes);
            }

            //tob应收核销暂估关联关系
            for (String key : shelfReceiveBillMap.keySet()) {

                if (!toBeVerifyItemMap.containsKey(key)) {
                    continue;
                }

                List<Long> receiveBillItemIds = shelfReceiveBillMap.get(key);

                List<TobTemporaryReceiveBillItemDTO> tobTemporaryReceiveBillItemDTOS = toBeVerifyItemMap.get(key);

                for (Long itemId : receiveBillItemIds) {
                    ReceiveBillItemDTO receiveBillItemDTO = receiveBillItemDTOMap.get(itemId);

                    //应收明细待核销金额
                    BigDecimal beVerifyAmount = receiveBillItemDTO.getBeVerifyAmount();

                    for (TobTemporaryReceiveBillItemDTO tobTempItem : tobTemporaryReceiveBillItemDTOS) {

                        if (beVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                            continue;
                        }

                        //暂估待核销金额
                        BigDecimal tobTempBeVerifyAmount = tobTempItem.getBeVerifyAmount();

                        if (tobTempBeVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                            continue;
                        }

                        TobTempVerifyRelModel tobTempVerifyRelModel = new TobTempVerifyRelModel();
                        tobTempVerifyRelModel.setReceiveItemId(receiveBillItemDTO.getId());
                        tobTempVerifyRelModel.setReceiveId(receiveBillItemDTO.getBillId());
                        tobTempVerifyRelModel.setTobItemId(tobTempItem.getId());
                        tobTempVerifyRelModel.setTobId(tobTempItem.getBillId());
                        tobTempVerifyRelModel.setGoodsId(tobTempItem.getGoodsId());
                        tobTempVerifyRelModel.setType(DERP_ORDER.TOBTEMPVERIFYREL_TYPE_0);
                        tobTempVerifyRelModel.setReceiveCode(receiveBillItemDTO.getReceiveCode());
                        tobTempVerifyRelModel.setCreditDate(receiveBillItemDTO.getCreditDate());


                        if (beVerifyAmount.abs().compareTo(tobTempBeVerifyAmount.abs()) > -1) {
                            tobTempVerifyRelModel.setVerifiyAmount(tobTempBeVerifyAmount);
                            tobTempItem.setBeVerifyAmount(BigDecimal.ZERO);
                            beVerifyAmount = beVerifyAmount.subtract(tobTempBeVerifyAmount);
                        } else {
                            tobTempVerifyRelModel.setVerifiyAmount(beVerifyAmount);
                            tobTempBeVerifyAmount = tobTempBeVerifyAmount.subtract(beVerifyAmount);
                            tobTempItem.setBeVerifyAmount(tobTempBeVerifyAmount);
                            beVerifyAmount = BigDecimal.ZERO;
                        }
                        tobTempVerifyRelModels.add(tobTempVerifyRelModel);

                        updateTobIds.add(tobTempItem.getBillId());
                    }
                    receiveBillItemDTO.setBeVerifyAmount(beVerifyAmount);
                    receiveBillItemDTOMap.put(itemId, receiveBillItemDTO);
                }

            }
        }


        int pageSize = 1000;
        //批量新增tob应收核销暂估关联关系
        for (int i = 0; i < tobTempVerifyRelModels.size(); ) {
            int pageSub = (i + pageSize) < tobTempVerifyRelModels.size() ? (i + pageSize) : tobTempVerifyRelModels.size();
            tobTempVerifyRelDao.batchSave(tobTempVerifyRelModels.subList(i, pageSub));
            i = pageSub;
        }
    }

    /**
     * @Description “销售退货入库单”创建的应收单核销tob暂估明细
     */
    private void saleReturnInOrderItemVerifyItems(ReceiveBillModel receiveBillModel, TobTemporaryReceiveBillDTO tobTemporaryReceiveBillDTO, List<Long> updateTobIds) throws Exception {
        //查询单据类型为“销售退货入库单”，状态为“待核销、部分核销、已核销“且应收明细“部分核销、未核销”的应收账单明细
        receiveBillModel.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_7);
        List<ReceiveBillItemDTO> returnInReceiveItems = receiveBillItemDao.listVerifyItems(receiveBillModel);

        Map<String, List<ReceiveBillItemDTO>> returnInReceiveBillMap = new HashMap<>();
        //以应收明细中的“销售退货入库单号+商品货号”查询To B暂估核销列表中的暂估应收明细，找到匹配的“销售退货入库单号+商品货号+po号”数据记录进行核销
        for (ReceiveBillItemDTO returnInReceiveItem : returnInReceiveItems) {
            String key = returnInReceiveItem.getCode() + "_" + returnInReceiveItem.getGoodsId() + "_" + returnInReceiveItem.getPoNo();

            List<ReceiveBillItemDTO> existItemModes = new ArrayList<>();
            if (returnInReceiveBillMap.containsKey(key)) {
                existItemModes.addAll(returnInReceiveBillMap.get(key));
            }
            existItemModes.add(returnInReceiveItem);
            returnInReceiveBillMap.put(key, existItemModes);
        }

        List<TobTemporaryReceiveBillItemDTO> toBeVerifyItems = tobTemporaryReceiveBillItemDao.listToBeVerifyItems(tobTemporaryReceiveBillDTO);

        Map<String, List<TobTemporaryReceiveBillItemDTO>> toBeVerifyItemMap = new HashMap<>();
        for (TobTemporaryReceiveBillItemDTO toBeVerifyItem : toBeVerifyItems) {
            String key = toBeVerifyItem.getShelfCode() + "_" + toBeVerifyItem.getGoodsId() + "_" + toBeVerifyItem.getPoNo();

            List<TobTemporaryReceiveBillItemDTO> existItemModes = new ArrayList<>();
            if (toBeVerifyItemMap.containsKey(key)) {
                existItemModes.addAll(toBeVerifyItemMap.get(key));
            }
            existItemModes.add(toBeVerifyItem);
            toBeVerifyItemMap.put(key, existItemModes);
        }

        //tob应收核销暂估关联关系
        List<TobTempVerifyRelModel> tobTempVerifyRelModels = new ArrayList<>();
        for (String key : returnInReceiveBillMap.keySet()) {

            if (!toBeVerifyItemMap.containsKey(key)) {
                continue;
            }

            List<ReceiveBillItemDTO> receiveBillItemDTOS = returnInReceiveBillMap.get(key);

            List<TobTemporaryReceiveBillItemDTO> tobTemporaryReceiveBillItemDTOS = toBeVerifyItemMap.get(key);

            for (ReceiveBillItemDTO receiveBillItemDTO : receiveBillItemDTOS) {
                //应收明细待核销金额
                BigDecimal beVerifyAmount = receiveBillItemDTO.getBeVerifyAmount();

                for (TobTemporaryReceiveBillItemDTO tobTempItem : tobTemporaryReceiveBillItemDTOS) {

                    if (beVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }

                    //暂估待核销金额
                    BigDecimal tobTempBeVerifyAmount = tobTempItem.getBeVerifyAmount();

                    if (tobTempBeVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }

                    TobTempVerifyRelModel tobTempVerifyRelModel = new TobTempVerifyRelModel();
                    tobTempVerifyRelModel.setReceiveItemId(receiveBillItemDTO.getId());
                    tobTempVerifyRelModel.setReceiveId(receiveBillItemDTO.getBillId());
                    tobTempVerifyRelModel.setTobItemId(tobTempItem.getId());
                    tobTempVerifyRelModel.setTobId(tobTempItem.getBillId());
                    tobTempVerifyRelModel.setGoodsId(tobTempItem.getGoodsId());
                    tobTempVerifyRelModel.setType(DERP_ORDER.TOBTEMPVERIFYREL_TYPE_0);
                    tobTempVerifyRelModel.setReceiveCode(receiveBillItemDTO.getReceiveCode());
                    tobTempVerifyRelModel.setCreditDate(receiveBillItemDTO.getCreditDate());

                    if (beVerifyAmount.abs().compareTo(tobTempBeVerifyAmount.abs()) > -1) {
                        tobTempVerifyRelModel.setVerifiyAmount(tobTempBeVerifyAmount);
                        tobTempItem.setBeVerifyAmount(BigDecimal.ZERO);
                        beVerifyAmount = beVerifyAmount.subtract(tobTempBeVerifyAmount);
                    } else {
                        tobTempVerifyRelModel.setVerifiyAmount(beVerifyAmount);
                        tobTempBeVerifyAmount = tobTempBeVerifyAmount.subtract(beVerifyAmount);
                        tobTempItem.setBeVerifyAmount(tobTempBeVerifyAmount);
                        beVerifyAmount = BigDecimal.ZERO;
                    }
                    tobTempVerifyRelModels.add(tobTempVerifyRelModel);

                    updateTobIds.add(tobTempItem.getBillId());
                }
            }

        }

        int pageSize = 1000;
        //批量新增tob应收核销暂估关联关系
        for (int i = 0; i < tobTempVerifyRelModels.size(); ) {
            int pageSub = (i + pageSize) < tobTempVerifyRelModels.size() ? (i + pageSize) : tobTempVerifyRelModels.size();
            tobTempVerifyRelDao.batchSave(tobTempVerifyRelModels.subList(i, pageSub));
            i = pageSub;
        }
    }


    /**
     * @Description 应收账单费用明细核销tob暂估返利明细
     * @Param
     */
    private void verifyRebateItems(ReceiveBillModel receiveBillModel, List<Long> updateTobIds, Long merchantId) throws Exception {

        //tob应收暂估返利明细红冲暂估自动核销
        verifyWriteOffRebateItems(merchantId);

        /**暂估返利说明：
          1.仅对“上架单”、“账单出库单”、“销售订单”、“销售退订单”创建的应收单以费用明细中的数据进行ToB暂估返利的核销；
          2.根据应收账单“公司+事业部+客户”查询“销售SD核销配置表”找到对应的客户已启用状态下的销售SD核销方式配置记录，
            1)若为“按PO核销”，则根据应收账单中费用明细记录的“PO+标准条码+结算费项+母品牌+销售币种”查询“To B暂估核销表 - 返利核销明细”对应的“PO+标准条码+映射费项+母品牌+销售币种”并回填结算金额，
             更新已结算暂估返利金额、待结算暂估返利金额； 若存在一个应收账单对应多个暂估对应时，优先对已上架的明细进行核销，直至已核销返利金额=暂估返利金额，
             再进行下一个上架单的返利金额核销；若应收账单中费用明细记录无商品维度，则按照“PO+结算费项+母品牌”+销售币种查询“To B暂估核销表 - 返利核销明细”对应未被核销的“事业部+客户+PO+映射费项+母品牌+销售币种”进行核销；
            2)若为“按上架倒核”，则以应收账单中费用明细记录的“标准条码+结算费项+母品牌+销售币种”查询“标准条码+映射费项”并回填结算金额；按照先上架先核销的顺序进行核销，
              直至已核销返利金额=暂估返利金额，再进行下一个上架单的返利金额核销；若应收账单中费用明细记录无商品维度,则按照“结算费项”查询“To B暂估核销表 - 返利核销明细”对应未被核销的“事业部+客户+映射费项+母品牌+销售币种”进行核销即可；*/

        /**对于应收单结算费用类型为“补款”类型时，仅核销暂估费用类型中的“购销退货”类型暂估费用，且需以负数形式进核销，
         * 核销金额计算为  {暂估费用金额-（- 补款金额）}；
         * 对于应收单结算费用类型为“扣款”类型时，仅核销暂估费用类型中的非“购销退货”类型暂估费用，且需以正数形式进核销，
         * 核销金额计算为 {暂估费用金额-（扣款金额）}；*/


        //查询单据状态为“上架单、账单出库单”，状态为“待核销、已核销、部分核销”的应收账单
        List<ReceiveBillModel> auditReceiveBills = receiveBillDao.getAuditBills(receiveBillModel);

        Map<Long, ReceiveBillModel> receiveBillMap = new HashMap<>();
        Map<String, List<ReceiveBillModel>> keyReceiveBillMap = new HashMap<>();
        for (ReceiveBillModel receiveBill : auditReceiveBills) {
            String key = receiveBill.getMerchantId() + "_" + receiveBill.getCustomerId() + "_" + receiveBill.getBuId();

            List<ReceiveBillModel> receiveBillModels = new ArrayList<>();
            if (keyReceiveBillMap.containsKey(key)) {
                receiveBillModels.addAll(keyReceiveBillMap.get(key));
            }
            receiveBillModels.add(receiveBill);
            keyReceiveBillMap.put(key, receiveBillModels);
            receiveBillMap.put(receiveBill.getId(), receiveBill);
        }

        // 查询销售SD核销方式配置记录
        for (String key : keyReceiveBillMap.keySet()) {
            List<ReceiveBillModel> receiveBillModels = keyReceiveBillMap.get(key);

            SdSaleVerifyConfigModel sdSaleConfigModel = new SdSaleVerifyConfigModel();
            sdSaleConfigModel.setMerchantId(receiveBillModels.get(0).getMerchantId());
            sdSaleConfigModel.setCustomerId(receiveBillModels.get(0).getCustomerId());
            sdSaleConfigModel.setBuId(receiveBillModels.get(0).getBuId());
            sdSaleConfigModel.setStatus(DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_1);
            SdSaleVerifyConfigModel sdSaleConfig = sdSaleVerifyConfigDao.searchByModel(sdSaleConfigModel);

            if (sdSaleConfig == null) {
                continue;
            }

            List<Long> ids = new ArrayList<>();
            for (ReceiveBillModel model : receiveBillModels) {
                ids.add(model.getId());
            }

            verifyCostItems(ids, sdSaleConfig, DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_0, receiveBillMap, receiveBillModels, updateTobIds);
            verifyCostItems(ids, sdSaleConfig, DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1, receiveBillMap, receiveBillModels, updateTobIds);

        }
    }

    /**
     * @Description tob应收暂估返利明细红冲暂估自动核销
     */
    private void verifyWriteOffRebateItems(Long merchantId) throws Exception {

        /**红冲暂估自动核销逻辑：暂估生成时判断销售SD单单据类型是否为“红冲”类型，
         * 若是则根据红冲单据中关联的“原销售SD单”查询结算状态为“未结算”的原销售SD单号进行一对一自动核销（整单完结核销，不校验各自上架单的数量、金额、商品等，各自核销金额为暂估金额），均更新状态为已核销；
         * 若查询不到符合条件的原销售SD单号则红冲核销失败，红冲暂估状态为“未结算”；*/

        List<Long> updateIds = new ArrayList<>();

        //已核销过的sd单单号
        List<String> verifySdCodes = tobTemporaryReceiveBillRebateItemDao.getVerifySdCodes(merchantId);

        //未核销的红冲sd单
        List<TobTemporaryReceiveBillRebateItemModel> rebateItemModels = tobTemporaryReceiveBillRebateItemDao.getWriteOffNonVerifyItems(merchantId, new ArrayList<>(), DERP_ORDER.TOBTEMPRECEIVEBILLRABATEITEM_ISWRITEOFF_1);

        List<String> oriSdCodes = new ArrayList<>();

        Map<String, TobTemporaryReceiveBillRebateItemModel> oriTobDTOMap = new HashMap<>();

        for (TobTemporaryReceiveBillRebateItemModel receiveBillRebateItemModel : rebateItemModels) {
            oriSdCodes.add(receiveBillRebateItemModel.getOriginalSaleSdOrderCode());
            oriTobDTOMap.put(receiveBillRebateItemModel.getOriginalSaleSdOrderCode(), receiveBillRebateItemModel);
        }

        if (oriSdCodes.size() == 0) {
            return;
        }

        //未核销的非红冲sd单
        List<TobTemporaryReceiveBillRebateItemModel> oriRebateItemModels = tobTemporaryReceiveBillRebateItemDao.getWriteOffNonVerifyItems(merchantId, oriSdCodes, DERP_ORDER.TOBTEMPRECEIVEBILLRABATEITEM_ISWRITEOFF_0);

        for (TobTemporaryReceiveBillRebateItemModel billDTO : oriRebateItemModels) {
            TobTemporaryReceiveBillRebateItemModel oriBillDTO = oriTobDTOMap.get(billDTO.getRelSdCode());

            if (oriBillDTO == null || (verifySdCodes != null && verifySdCodes.contains(billDTO.getRelSdCode()))) {
                continue;
            }
            updateIds.add(billDTO.getBillId());
            updateIds.add(oriBillDTO.getBillId());
        }

        if (updateIds.size() == 0) {
            return;
        }


        List<TobTempVerifyRelModel> relModels = new ArrayList<>();
        List<TobTemporaryReceiveBillRebateItemModel> tobTemporaryReceiveBillRebateItemModels = tobTemporaryReceiveBillRebateItemDao.listByBillIds(updateIds);

        for (TobTemporaryReceiveBillRebateItemModel itemModel : tobTemporaryReceiveBillRebateItemModels) {

            if (verifySdCodes.contains(itemModel.getRelSdCode()) || verifySdCodes.contains(itemModel.getOriginalSaleSdOrderCode())) {
                continue;
            }

            TobTempVerifyRelModel relModel = new TobTempVerifyRelModel();
            relModel.setTobId(itemModel.getBillId());
            relModel.setTobItemId(itemModel.getId());
            relModel.setVerifiyAmount(itemModel.getRebateAmount());
            relModel.setGoodsId(itemModel.getGoodsId());
            relModel.setProjectId(itemModel.getProjectId());
            relModel.setType(DERP_ORDER.TOBTEMPVERIFYREL_TYPE_1);
            relModel.setCreateDate(TimeUtils.getNow());

            relModels.add(relModel);
        }

        if (relModels.size() > 0) {
            tobTempVerifyRelDao.batchSave(relModels);
        }

        if (updateIds.size() > 0) {
            updateTobTemp(updateIds);
        }

    }

    private void verifyCostItems(List<Long> ids, SdSaleVerifyConfigModel sdSaleConfig, String billType, Map<Long, ReceiveBillModel> receiveBillMap,
                                 List<ReceiveBillModel> receiveBillModels, List<Long> updateTobIds) throws Exception {
        //待核销的应收账单明细
        List<ReceiveBillCostItemDTO> receiveBillCostItemDTOS = receiveBillCostItemDao.getBeVerifyCostItems(ids, billType);

        Map<Long, String> receiveCostCommbarcodeMap = new HashMap<>();
        if (DERP_ORDER.SDSALEVERIFYCONFIG_ISMERCHANDISEVERIFY_1.equals(sdSaleConfig.getIsMerchandiseVerify())) {
            List<Long> receiveCostGoodsIds = new ArrayList<>();
            for (ReceiveBillCostItemDTO itemDTO : receiveBillCostItemDTOS) {
                if (itemDTO.getGoodsId() != null) {
                    receiveCostGoodsIds.add(itemDTO.getGoodsId());
                }
            }

            if (!receiveCostGoodsIds.isEmpty()) {
                List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", receiveCostGoodsIds);

                for (MerchandiseInfoMogo merchandiseInfoMogo : merchandiseInfoMogos) {
                    receiveCostCommbarcodeMap.put(merchandiseInfoMogo.getMerchandiseId(), merchandiseInfoMogo.getCommbarcode());
                }
            }
        }


        Map<String, List<ReceiveBillCostItemDTO>> receiveBillCostWithGoodsMap = new HashMap<>(); //费用明细中有商品维度集合
        Map<String, List<ReceiveBillCostItemDTO>> receiveBillCostWithoutMap = new HashMap<>(); //费用明细中无商品维度集合
        for (ReceiveBillCostItemDTO itemDTO : receiveBillCostItemDTOS) {
            List<ReceiveBillCostItemDTO> receiveBillCostItemDTOList = new ArrayList<>();

            String groupKey = String.valueOf(itemDTO.getProjectId()) + "_" + String.valueOf(itemDTO.getParentBrandId()) + "_" + itemDTO.getCurrency();
            //是否按po核销
            if (DERP_ORDER.SDSALEVERIFYCONFIG_VERIFYTYPE_0.equals(sdSaleConfig.getVerifyType())) {
                groupKey = groupKey + "_" + itemDTO.getPoNo();
            }

            if (DERP_ORDER.SDSALEVERIFYCONFIG_ISMERCHANDISEVERIFY_1.equals(sdSaleConfig.getIsMerchandiseVerify()) && itemDTO.getGoodsId() != null) {
                groupKey = groupKey + "_" + receiveCostCommbarcodeMap.get(itemDTO.getGoodsId());

                if (receiveBillCostWithGoodsMap.containsKey(groupKey)) {
                    receiveBillCostItemDTOList.addAll(receiveBillCostWithGoodsMap.get(groupKey));
                }

                receiveBillCostItemDTOList.add(itemDTO);
                receiveBillCostWithGoodsMap.put(groupKey, receiveBillCostItemDTOList);
            } else {
                if (receiveBillCostWithoutMap.containsKey(groupKey)) {
                    receiveBillCostItemDTOList.addAll(receiveBillCostWithoutMap.get(groupKey));
                }

                receiveBillCostItemDTOList.add(itemDTO);
                receiveBillCostWithoutMap.put(groupKey, receiveBillCostItemDTOList);
            }

        }

        TobTemporaryReceiveBillDTO temporaryReceiveBillDTO = new TobTemporaryReceiveBillDTO();
        temporaryReceiveBillDTO.setMerchantId(receiveBillModels.get(0).getMerchantId());
        temporaryReceiveBillDTO.setCustomerId(receiveBillModels.get(0).getCustomerId());
        temporaryReceiveBillDTO.setBuId(receiveBillModels.get(0).getBuId());
        temporaryReceiveBillDTO.setBillType(billType);
        List<TobTemporaryReceiveBillRebateItemDTO> receiveBillRebateItemDTOS = tobTemporaryReceiveBillRebateItemDao.getVerifyRebateItems(temporaryReceiveBillDTO);
        Map<Long, String> rebateItemCommbarcodeMap = new HashMap<>();

        Map<String, List<TobTemporaryReceiveBillRebateItemDTO>> rebateItemDTOMap = new HashMap<>();
        if (DERP_ORDER.SDSALEVERIFYCONFIG_ISMERCHANDISEVERIFY_1.equals(sdSaleConfig.getIsMerchandiseVerify())) {
            List<Long> rebateItemGoodsIds = new ArrayList<>();
            for (TobTemporaryReceiveBillRebateItemDTO rebateItemDTO : receiveBillRebateItemDTOS) {
                rebateItemGoodsIds.add(rebateItemDTO.getGoodsId());
            }
            List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", rebateItemGoodsIds);

            for (MerchandiseInfoMogo merchandiseInfoMogo : merchandiseInfoMogos) {
                rebateItemCommbarcodeMap.put(merchandiseInfoMogo.getMerchandiseId(), merchandiseInfoMogo.getCommbarcode());
            }

        }

        for (TobTemporaryReceiveBillRebateItemDTO rebateItemDTO : receiveBillRebateItemDTOS) {
            List<TobTemporaryReceiveBillRebateItemDTO> receiveBillRebateItemDTOList = new ArrayList<>();
            String groupKey = String.valueOf(rebateItemDTO.getProjectId()) + "_" + String.valueOf(rebateItemDTO.getParentBrandId()) + "_" + rebateItemDTO.getCurrency();
            //是否按po核销
            if (DERP_ORDER.SDSALEVERIFYCONFIG_VERIFYTYPE_0.equals(sdSaleConfig.getVerifyType())) {
                groupKey = groupKey + "_" + rebateItemDTO.getPoNo();
            }

            if (DERP_ORDER.SDSALEVERIFYCONFIG_ISMERCHANDISEVERIFY_1.equals(sdSaleConfig.getIsMerchandiseVerify())) {
                groupKey = groupKey + "_" + receiveCostCommbarcodeMap.get(rebateItemDTO.getGoodsId());
            }

            if (rebateItemDTOMap.containsKey(groupKey)) {
                receiveBillRebateItemDTOList.addAll(rebateItemDTOMap.get(groupKey));
            }

            receiveBillRebateItemDTOList.add(rebateItemDTO);
            rebateItemDTOMap.put(groupKey, receiveBillRebateItemDTOList);
        }

        //若需要按商品维度核销，则先核销应收费用明细有商品维度的明细，再核销没有商品维度的明细
        if (DERP_ORDER.SDSALEVERIFYCONFIG_ISMERCHANDISEVERIFY_1.equals(sdSaleConfig.getIsMerchandiseVerify())) {

            List<TobTempVerifyRelModel> tobTempVerifyRelModels = new ArrayList<>();

            for (String groupKey : receiveBillCostWithGoodsMap.keySet()) {
                List<ReceiveBillCostItemDTO> receiveBillCostItemDTOList = receiveBillCostWithGoodsMap.get(groupKey);
                List<TobTemporaryReceiveBillRebateItemDTO> rebateItemDTOS = rebateItemDTOMap.get(groupKey);

                if (rebateItemDTOS == null) {
                    continue;
                }

                for (ReceiveBillCostItemDTO costItemDTO : receiveBillCostItemDTOList) {
                    //应收明细待核销金额
                    BigDecimal beVerifyAmount = costItemDTO.getPrice().subtract(costItemDTO.getVerifiyAmount());

                    for (TobTemporaryReceiveBillRebateItemDTO tobTempItem : rebateItemDTOS) {

                        if (beVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                            break;
                        }

                        //暂估待核销金额
                        BigDecimal tobTempBeVerifyAmount = tobTempItem.getRebateAmount().subtract(tobTempItem.getVerifyAmount());

                        if (tobTempBeVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                            continue;
                        }

                        TobTempVerifyRelModel tobTempVerifyRelModel = new TobTempVerifyRelModel();
                        tobTempVerifyRelModel.setReceiveItemId(costItemDTO.getId());
                        tobTempVerifyRelModel.setReceiveId(costItemDTO.getBillId());
                        tobTempVerifyRelModel.setTobItemId(tobTempItem.getId());
                        tobTempVerifyRelModel.setTobId(tobTempItem.getBillId());
                        tobTempVerifyRelModel.setGoodsId(tobTempItem.getGoodsId());
                        tobTempVerifyRelModel.setProjectId(tobTempItem.getProjectId());
                        tobTempVerifyRelModel.setType(DERP_ORDER.TOBTEMPVERIFYREL_TYPE_1);
                        tobTempVerifyRelModel.setReceiveCode(receiveBillMap.get(costItemDTO.getBillId()).getCode());
                        tobTempVerifyRelModel.setCreditDate(receiveBillMap.get(costItemDTO.getBillId()).getCreditDate());
                        tobTempVerifyRelModel.setParentBrandId(tobTempItem.getParentBrandId());

                        if (beVerifyAmount.abs().compareTo(tobTempBeVerifyAmount.abs()) > -1) {
                            tobTempVerifyRelModel.setVerifiyAmount(tobTempBeVerifyAmount);
                            tobTempItem.setVerifyAmount(tobTempItem.getRebateAmount());
                            beVerifyAmount = beVerifyAmount.subtract(tobTempBeVerifyAmount);
                        } else {
                            tobTempVerifyRelModel.setVerifiyAmount(beVerifyAmount);
                            tobTempBeVerifyAmount = tobTempItem.getVerifyAmount().add(beVerifyAmount);
                            tobTempItem.setVerifyAmount(tobTempBeVerifyAmount);
                            beVerifyAmount = BigDecimal.ZERO;
                        }
                        tobTempVerifyRelModels.add(tobTempVerifyRelModel);
                        updateTobIds.add(tobTempItem.getBillId());
                    }

                }

            }
            if (!tobTempVerifyRelModels.isEmpty()) {
                tobTempVerifyRelDao.batchSave(tobTempVerifyRelModels);
            }


            if (receiveBillCostWithoutMap.size() > 0) {
                List<TobTemporaryReceiveBillRebateItemDTO> receiveBillRebateItemDTOList = tobTemporaryReceiveBillRebateItemDao.getVerifyRebateItems(temporaryReceiveBillDTO);
                rebateItemDTOMap.clear();
                for (TobTemporaryReceiveBillRebateItemDTO rebateItemDTO : receiveBillRebateItemDTOList) {
                    List<TobTemporaryReceiveBillRebateItemDTO> rebateItemDTOList = new ArrayList<>();
                    String groupKey = String.valueOf(rebateItemDTO.getProjectId()) + "_" + String.valueOf(rebateItemDTO.getParentBrandId()) + "_" + rebateItemDTO.getCurrency();;
                    //是否按po核销
                    if (DERP_ORDER.SDSALEVERIFYCONFIG_VERIFYTYPE_0.equals(sdSaleConfig.getVerifyType())) {
                        groupKey = groupKey + "_" + rebateItemDTO.getPoNo();
                    }

                    if (rebateItemDTOMap.containsKey(groupKey)) {
                        rebateItemDTOList.addAll(rebateItemDTOMap.get(groupKey));
                    }

                    rebateItemDTOList.add(rebateItemDTO);
                    rebateItemDTOMap.put(groupKey, rebateItemDTOList);
                }
            }
        }

        List<TobTempVerifyRelModel> tobTempVerifyRelModels = new ArrayList<>();
        for (String groupKey : receiveBillCostWithoutMap.keySet()) {
            List<ReceiveBillCostItemDTO> receiveBillCostItemDTOList = receiveBillCostWithoutMap.get(groupKey);
            List<TobTemporaryReceiveBillRebateItemDTO> rebateItemDTOS = rebateItemDTOMap.get(groupKey);

            if (rebateItemDTOS == null) {
                continue;
            }

            for (ReceiveBillCostItemDTO costItemDTO : receiveBillCostItemDTOList) {
                //应收明细已核销金额
                BigDecimal beVerifyAmount = costItemDTO.getPrice().subtract(costItemDTO.getVerifiyAmount());

                for (TobTemporaryReceiveBillRebateItemDTO tobTempItem : rebateItemDTOS) {

                    if (beVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                        break;
                    }

                    //暂估待核销金额
                    BigDecimal tobTempBeVerifyAmount = tobTempItem.getRebateAmount().subtract(tobTempItem.getVerifyAmount());

                    if (tobTempBeVerifyAmount.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }

                    TobTempVerifyRelModel tobTempVerifyRelModel = new TobTempVerifyRelModel();
                    tobTempVerifyRelModel.setReceiveItemId(costItemDTO.getId());
                    tobTempVerifyRelModel.setReceiveId(costItemDTO.getBillId());
                    tobTempVerifyRelModel.setTobItemId(tobTempItem.getId());
                    tobTempVerifyRelModel.setTobId(tobTempItem.getBillId());
                    tobTempVerifyRelModel.setGoodsId(tobTempItem.getGoodsId());
                    tobTempVerifyRelModel.setProjectId(tobTempItem.getProjectId());
                    tobTempVerifyRelModel.setType(DERP_ORDER.TOBTEMPVERIFYREL_TYPE_1);
                    tobTempVerifyRelModel.setReceiveCode(receiveBillMap.get(costItemDTO.getBillId()).getCode());
                    tobTempVerifyRelModel.setCreditDate(receiveBillMap.get(costItemDTO.getBillId()).getCreditDate());
                    tobTempVerifyRelModel.setParentBrandId(tobTempItem.getParentBrandId());

                    if (beVerifyAmount.abs().compareTo(tobTempBeVerifyAmount.abs()) > -1) {
                        tobTempVerifyRelModel.setVerifiyAmount(tobTempBeVerifyAmount);
                        tobTempItem.setVerifyAmount(tobTempItem.getRebateAmount());
                        beVerifyAmount = beVerifyAmount.subtract(tobTempBeVerifyAmount);
                    } else {
                        tobTempVerifyRelModel.setVerifiyAmount(beVerifyAmount);
                        tobTempBeVerifyAmount = tobTempItem.getVerifyAmount().add(beVerifyAmount);
                        tobTempItem.setVerifyAmount(tobTempBeVerifyAmount);
                        beVerifyAmount = BigDecimal.ZERO;
                    }
                    tobTempVerifyRelModels.add(tobTempVerifyRelModel);
                    updateTobIds.add(tobTempItem.getBillId());
                }

            }

        }

        if (!tobTempVerifyRelModels.isEmpty()) {
            tobTempVerifyRelDao.batchSave(tobTempVerifyRelModels);
        }
    }


    private void updateTobTemp(List<Long> tobIds) throws Exception {

        List<Long> billIds = tobIds.stream().distinct().collect(Collectors.toList());

        List<Map<String, Object>> itemPriceList = tobTempVerifyRelDao.getRelReceiveBill(billIds);

        List<TobTemporaryReceiveBillModel> updateTobTempModels = new ArrayList<>();

        if (itemPriceList.size() == 0) {

            for (Long tobId : billIds) {
                TobTemporaryReceiveBillModel tobTemporaryReceiveBillModel = tobTemporaryReceiveBillDao.searchById(tobId);
                tobTemporaryReceiveBillModel.setOutBillRealDate(null);
                tobTemporaryReceiveBillModel.setInvoicingRealDate(null);
                tobTemporaryReceiveBillModel.setConfirmRealDate(null);
                tobTemporaryReceiveBillModel.setPaymentRealDate(null);
                tobTemporaryReceiveBillModel.setOutBillRealDate(null);
                tobTemporaryReceiveBillModel.setStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
                tobTemporaryReceiveBillModel.setRebateStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
                tobTemporaryReceiveBillModel.setReceiveCode(null);
                tobTemporaryReceiveBillModel.setReceiveId(null);
                updateTobTempModels.add(tobTemporaryReceiveBillModel);
            }

        }

        for (Map<String, Object> map : itemPriceList) {

            Long tobId = (Long) map.get("tobId");
            String receiveIds = (String) map.get("receiveIds");
            BigDecimal receiveAmount = map.get("receiveAmount") != null ? (BigDecimal) map.get("receiveAmount") : BigDecimal.ZERO;
            BigDecimal rebateAmount = map.get("rebateAmount") != null ? (BigDecimal) map.get("rebateAmount") : BigDecimal.ZERO;

            TobTemporaryReceiveBillModel tobTemporaryReceiveBillModel = tobTemporaryReceiveBillDao.searchById(tobId);

            if (StringUtils.isNotBlank(receiveIds)) {

                List<Long> billIdList = Arrays.asList(receiveIds.split(",")).stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());

                ReceiveBillOperateModel operateModel = receiveBillOperateDao.getLatestOperate(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_1, billIdList);

                ReceiveBillModel relReceive = receiveBillDao.searchById(operateModel.getBillId());
                if (relReceive != null) {
                    tobTemporaryReceiveBillModel.setOutBillRealDate(relReceive.getCreateDate());

                    //开票实际日期
                    if (relReceive.getInvoiceId() != null) {
                        ReceiveBillInvoiceModel invoiceModel = new ReceiveBillInvoiceModel();
                        invoiceModel.setId(relReceive.getInvoiceId());
                        ReceiveBillInvoiceModel receiveBillInvoiceModel = receiveBillInvoiceDao.searchByModel(invoiceModel);
                        tobTemporaryReceiveBillModel.setInvoicingRealDate(receiveBillInvoiceModel.getCreateDate());
                    }

                    //账单确认日期
                    if (!(DERP_ORDER.RECEIVEBILL_BILLSTATUS_00.equals(relReceive.getBillStatus()) ||
                            DERP_ORDER.RECEIVEBILL_BILLSTATUS_01.equals(relReceive.getBillStatus()))) {
                        ReceiveBillOperateModel audit = receiveBillOperateDao.getLatestOperate(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_1, Arrays.asList(relReceive.getId()));
                        if (audit != null) {
                            tobTemporaryReceiveBillModel.setConfirmRealDate(audit.getOperateDate());
                        }
                    }

                    //回款实际日期
                    if (DERP_ORDER.RECEIVEBILL_BILLSTATUS_04.equals(relReceive.getBillStatus())) {
                        Timestamp verifyTime = receiveBillVerifyItemDao.getLatestVerifyDate(relReceive.getId());
                        tobTemporaryReceiveBillModel.setPaymentRealDate(verifyTime);
                    }

                    tobTemporaryReceiveBillModel.setReceiveCode(relReceive.getCode());
                    tobTemporaryReceiveBillModel.setReceiveId(relReceive.getId());

                }
            }

            if (tobTemporaryReceiveBillModel.getShelfAmount().compareTo(receiveAmount) == 0 &&
                    receiveAmount.compareTo(BigDecimal.ZERO) != 0) {
                tobTemporaryReceiveBillModel.setStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_5);
            } else if (receiveAmount.compareTo(BigDecimal.ZERO) == 0) {
                tobTemporaryReceiveBillModel.setStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
            } else if (tobTemporaryReceiveBillModel.getShelfAmount().compareTo(receiveAmount) != 0) {
                tobTemporaryReceiveBillModel.setStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_2);
            }

            if (tobTemporaryReceiveBillModel.getShelfRebateAmount().compareTo(rebateAmount) == 0&&
                    rebateAmount.compareTo(BigDecimal.ZERO) != 0) {
                tobTemporaryReceiveBillModel.setRebateStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_5);
            } else if (rebateAmount.compareTo(BigDecimal.ZERO) == 0) {
                tobTemporaryReceiveBillModel.setRebateStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
            } else if (tobTemporaryReceiveBillModel.getShelfRebateAmount().compareTo(rebateAmount) != 0) {
                tobTemporaryReceiveBillModel.setRebateStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_2);
            }
            updateTobTempModels.add(tobTemporaryReceiveBillModel);

            billIds.remove(tobId);
        }

        for (Long tobId : billIds) {
            TobTemporaryReceiveBillModel tobTemporaryReceiveBillModel = tobTemporaryReceiveBillDao.searchById(tobId);
            tobTemporaryReceiveBillModel.setStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
            tobTemporaryReceiveBillModel.setRebateStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1);
            updateTobTempModels.add(tobTemporaryReceiveBillModel);
        }

        if (!updateTobTempModels.isEmpty()) {
            tobTemporaryReceiveBillDao.batchUpdate(updateTobTempModels);
        }
    }

}
