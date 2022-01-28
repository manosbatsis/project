package com.topideal.service.timer.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import com.sun.org.apache.xpath.internal.objects.XString;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.dao.bill.*;
import com.topideal.dao.bill.impl.TocSettlementReceiveBillDaoImpl;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.vo.bill.ReceiveBillInvoiceModel;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import com.topideal.entity.vo.bill.ReceiveBillVerificationModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.CustomerMerchantRelMongoDao;
import com.topideal.mongo.dao.EmailConfigMongoDao;
import com.topideal.mongo.entity.CustomerMerchantRelMongo;
import com.topideal.service.timer.SaveReceiveBillVerificationService;

import net.sf.json.JSONObject;

/**
 * @author 杨创 2020/07/30
 */
@Service
public class SaveReceiveBillVerificationServiceImpl implements SaveReceiveBillVerificationService {
    @Autowired
    private ReceiveBillVerificationDao receiveBillVerificationDao;// 收款核销跟踪
    @Autowired
    private ReceiveBillDao receiveBillDao;//应收账单表
    @Autowired
    private CustomerMerchantRelMongoDao customerMerchantRelMongoDao;// 商家客户关系表
    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao;//
    @Autowired
    private ReceiveBillInvoiceDao receiveBillInvoiceDao;
    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
    @Autowired
    private TocSettlementReceiveBillCostItemDao tocSettlementReceiveBillCostItemDao;
    @Autowired
    private TocSettlementReceiveBillItemDao tocSettlementReceiveBillItemDao;
    @Autowired
    private TocSettlementReceiveBillVerifyItemDao tocSettlementReceiveBillVerifyItemDao;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveReceiveBillVerificationServiceImpl.class);


    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201168200, model = DERP_LOG_POINT.POINT_13201168200_Label)
    public boolean saveReceiveBillVerification(String json, String keys, String topics, String tags) throws Exception {
        LOGGER.info("收款核销跟踪 生成和刷新json:" + json);
        JSONObject jsonData = JSONObject.fromObject(json);
        String receiveCodes = (String) jsonData.get("receiveCodes");
        String orderType = (String) jsonData.get("orderType");

        List<String> receiveCodeList = null;
        if (StringUtils.isNotBlank(receiveCodes)) {
            receiveCodeList = Arrays.asList(receiveCodes.split(","));
        }

        //tob应收单生成核销跟踪记录
        if (StringUtils.isBlank(orderType) || DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_0.equals(orderType)) {
            save2BReceiveBillVerification(receiveCodeList);
        } else if(StringUtils.isBlank(orderType) || DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_1.equals(orderType)) { //toc应收单生成核销跟踪记录
            save2CReceiveBillVerification(receiveCodeList);
        }

        return true;
    }

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201168200, model = DERP_LOG_POINT.POINT_13201168200_Label)
    public boolean delReceiveBillVerification(String json, String keys, String topics, String tags) throws Exception {
        LOGGER.info("收款核销跟踪 作废删除json:" + json);
        JSONObject jsonData = JSONObject.fromObject(json);
        String receiveCodes = (String) jsonData.get("receiveCodes");
        String orderType = (String) jsonData.get("orderType");

        List<String> receiveCodeList = null;
        if (StringUtils.isNotBlank(receiveCodes)) {
            receiveCodeList = Arrays.asList(receiveCodes.split(","));
        }

        //tob应收单
        if (StringUtils.isBlank(orderType) || DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_0.equals(orderType)) {
            List<Long> receiveIdList = new ArrayList<>();

            //获取已经作废应收账单的收款核销
            List<ReceiveBillModel> ZfAnd006BillList = receiveBillDao.getZfAnd006ReceiveBill(receiveCodeList);
            for (ReceiveBillModel model : ZfAnd006BillList) {
                receiveIdList.add(model.getId());
            }

            if (receiveIdList.size() > 0) {
                // 删除原来的收款核销表
                receiveBillVerificationDao.deleteByReceiveId(receiveIdList, DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_0);
            }
        } else if(StringUtils.isBlank(orderType) || DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_1.equals(orderType)) { //toc应收单
            List<Long> receiveIdList = new ArrayList<>();
            List<TocSettlementReceiveBillModel> ZfAnd006BillList = tocSettlementReceiveBillDao.getZfAnd006ReceiveBill(receiveCodeList);

            for (TocSettlementReceiveBillModel model : ZfAnd006BillList) {
                receiveIdList.add(model.getId());
            }

            if (receiveIdList.size() > 0) {
                // 删除原来的收款核销表
                receiveBillVerificationDao.deleteByReceiveId(receiveIdList, DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_1);
            }

        }

        return true;
    }

    public int getOverdueDay(String accountPeriod, String receiveDateStr, String billDateStr) {
        /**账期0:预售货款,1:信用账期-7天,2:信用账期-15天,3:信用账期-30天,4:信用账期-40天,5:信用账期-45天,6:信用账期-50天,7:信用账期-60天,8:信用账期-90天,9信用账期-90天以 */
        int days = 0;
        if (DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_0.equals(accountPeriod)) {
            days = 0;
        } else if (DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_1.equals(accountPeriod)) {
            days = 7;
        } else if (DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_2.equals(accountPeriod)) {
            days = 15;
        } else if (DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_3.equals(accountPeriod)) {
            days = 30;
        } else if (DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_4.equals(accountPeriod)) {
            days = 40;
        } else if (DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_5.equals(accountPeriod)) {
            days = 45;
        } else if (DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_6.equals(accountPeriod)) {
            days = 50;
        } else if (DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_7.equals(accountPeriod)) {
            days = 60;
        } else if (DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_8.equals(accountPeriod)) {
            days = 90;
        }
        int overdueDayNum = TimeUtils.getOverdueDay(receiveDateStr, billDateStr, days);
        if (overdueDayNum < 0) overdueDayNum = 0;// overdueDayNum<0 说明没有逾期
        return overdueDayNum;
    }


    private void save2BReceiveBillVerification(List<String> receiveCodeList) throws Exception {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("receiveCodeList", receiveCodeList);
        // 存储新增的  收款核销跟踪数据
        List<ReceiveBillVerificationModel> billVerificationList = new ArrayList<ReceiveBillVerificationModel>();
        //查询 应收账单  状态为02-待核销 03-部分核销 04-已核销 05-作废待审并且收款核销跟踪(没有核销/没有完全核销的)应收账单
        List<ReceiveBillModel> receiveBillList = receiveBillDao.getReceiveBillList(paramMap);
        List<Long> receiveIdList = new ArrayList<>();
        for (ReceiveBillModel model : receiveBillList) {
            // 查询开票日期
            Long invoiceId = model.getInvoiceId();
            ReceiveBillInvoiceModel receiveBillInvoice = null;
            if (invoiceId != null) {
                receiveBillInvoice = receiveBillInvoiceDao.searchById(invoiceId);
            }
            if (receiveBillInvoice == null) receiveBillInvoice = new ReceiveBillInvoiceModel();
            //应收账单对应的表体结算金额（不含税）
            BigDecimal totalReceivePrice = new BigDecimal(0);
            List<Map<String, Object>> sumReceivePriceList = receiveBillDao.getSumReceivePrice(model.getId());
            for (Map<String, Object> map : sumReceivePriceList) {
                BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
                if (totalPrice == null) totalPrice = new BigDecimal(0);
                totalReceivePrice = totalReceivePrice.add(totalPrice);
            }
            // 应收账单对应的 应收账单费用明细表费用金额（不含税）
            BigDecimal costPrice = receiveBillCostItemDao.getTotalReceivePrice(model.getId());
            if (costPrice == null) costPrice = new BigDecimal(0);
            BigDecimal receivePrice = totalReceivePrice.add(costPrice);// 计算应收金额
            // 本期已核销金额
            Timestamp receiveDate = null;// 最近付款日志
            BigDecimal collectedPrice = new BigDecimal(0);
            List<Map<String, Object>> sumCollectedPriceList = receiveBillDao.getSumCollectedPrice(model.getId());
            for (Map<String, Object> map : sumCollectedPriceList) {
                BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
                if (totalPrice == null) totalPrice = new BigDecimal(0);
                collectedPrice = collectedPrice.add(totalPrice);
                receiveDate = (Timestamp) map.get("receive_date");
            }
            // 计算本期未核销金额
            BigDecimal uncollectedPrice = receivePrice.subtract(collectedPrice);
            //计算客户账期

            Long customerId = model.getCustomerId();
            if (customerId == null) {
                LOGGER.error("应收账单客户id不能为空,应收账单号:" + model.getCode());
                throw new RuntimeException("应收账单客户id不能为空,应收账单号:" + model.getCode());
            }
            Map<String, Object> customerReMap = new HashMap<String, Object>();
            customerReMap.put("customerId", customerId);
            customerReMap.put("merchantId", model.getMerchantId());
            CustomerMerchantRelMongo customerMerchantRelMongo = customerMerchantRelMongoDao.findOne(customerReMap);
            if (customerMerchantRelMongo == null) {
                LOGGER.error("应收账单没有对应的商家客户信息,应收账单号:" + model.getCode());
                throw new RuntimeException("应收账单没有对应的商家客户信息,应收账单号:" + model.getCode());
            }

            String accountPeriod = customerMerchantRelMongo.getAccountPeriod();

            //如果账期 没有配置 默认为0
            if (StringUtils.isBlank(accountPeriod)) accountPeriod = DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_0;
            //计算逾期天数
            int overdueDayNum = 0;
            Timestamp billDate = model.getBillDate();
            String billDateStr = TimeUtils.format(billDate, "yyyy-MM-dd");
            if (uncollectedPrice.compareTo(new BigDecimal(0)) == 0) {// 未核销为0  账期逾期天数=最近的收款日期 - （账单日期+客户账期天数）
                if (receiveDate == null) {
                    LOGGER.error("应收账单已经完全核销最近收款金额不能为空,应收账单号:" + model.getCode());
                    //throw new RuntimeException("应收账单已经完全核销最近收款金额不能为空,应收账单号"+model.getCode());
                } else {
                    String receiveDateStr = TimeUtils.format(receiveDate, "yyyy-MM-dd");
                    overdueDayNum = getOverdueDay(accountPeriod, receiveDateStr, billDateStr);
                }

            } else {//未核销不为0 //账期逾期天数= 当前日期 - （账单日期+客户账期天数）
                String receiveDateStr = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd");
                overdueDayNum = getOverdueDay(accountPeriod, receiveDateStr, billDateStr);
            }

            ReceiveBillVerificationModel verificationModel = new ReceiveBillVerificationModel();
            verificationModel.setReceiveId(model.getId());
            verificationModel.setReceiveCode(model.getCode());
            verificationModel.setMerchantId(model.getMerchantId());
            verificationModel.setMerchantName(model.getMerchantName());
            verificationModel.setCustomerId(model.getCustomerId());
            verificationModel.setCustomerName(model.getCustomerName());
            verificationModel.setReceivePrice(receivePrice);
            verificationModel.setUncollectedPrice(uncollectedPrice);
            verificationModel.setCurrency(model.getCurrency());
            verificationModel.setBillDate(billDate);
            verificationModel.setInvoiceDate(receiveBillInvoice.getInvoiceDate());// 开单日期
            verificationModel.setAccountPeriod(accountPeriod);
            verificationModel.setAccountOverdueDays(overdueDayNum);
            verificationModel.setBuId(model.getBuId());
            verificationModel.setBuName(model.getBuName());
            verificationModel.setBillStatus(model.getBillStatus());
            verificationModel.setBillType(DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_0);
            verificationModel.setCreditMonth(TimeUtils.formatMonth(model.getCreateDate()));
            String invoiceNo = receiveBillInvoice.getInvoiceNo();//发票号
            // 如果发票号为空 没有开票 否则已经开票
            if (StringUtils.isBlank(invoiceNo)) {
                verificationModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILLVERIFICATION_INVOICESTATUS_0);
            } else {
                verificationModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILLVERIFICATION_INVOICESTATUS_1);
            }
            verificationModel.setInvoiceNo(invoiceNo);

            String overdueStatus = null;
            if (overdueDayNum == 0) {//未逾期
                overdueStatus = DERP_ORDER.RECEIVEBILLVERIFICATION_OVERDUESTATUS_0;
            } else {
                overdueStatus = DERP_ORDER.RECEIVEBILLVERIFICATION_OVERDUESTATUS_1;
            }
            verificationModel.setOverdueStatus(overdueStatus);
            billVerificationList.add(verificationModel);
            receiveIdList.add(model.getId());

        }
        //获取已经作废应收账单的收款核销
        List<ReceiveBillModel> ZfAnd006BillList = receiveBillDao.getZfAnd006ReceiveBill(new ArrayList<>());
        for (ReceiveBillModel model : ZfAnd006BillList) {
            receiveIdList.add(model.getId());
        }


        if (receiveIdList.size() > 0) {
            // 删除原来的收款核销表
            receiveBillVerificationDao.deleteByReceiveId(receiveIdList, DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_0);
        }

        // 循环新增
        for (ReceiveBillVerificationModel model : billVerificationList) {
            receiveBillVerificationDao.save(model);
        }
    }

    private void save2CReceiveBillVerification(List<String> receiveCodeList) throws Exception {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("receiveCodeList", receiveCodeList);
        // 存储新增的  收款核销跟踪数据
        List<ReceiveBillVerificationModel> billVerificationList = new ArrayList<ReceiveBillVerificationModel>();
        //查询 应收账单  状态为02-待核销 03-部分核销 04-已核销 05-作废待审并且收款核销跟踪(没有核销/没有完全核销的)应收账单
        List<TocSettlementReceiveBillModel> receiveBillList = tocSettlementReceiveBillDao.getReceiveBillList(paramMap);
        List<Long> receiveIdList = new ArrayList<>();
        for (TocSettlementReceiveBillModel model : receiveBillList) {
            // 查询开票日期
            ReceiveBillInvoiceModel receiveBillInvoice = null;
            if (model.getInvoiceId() != null) {
                receiveBillInvoice = receiveBillInvoiceDao.searchById(model.getInvoiceId());
            }

            //应收账单对应的表体结算金额（不含税）
            BigDecimal itemTotalPrice = tocSettlementReceiveBillItemDao.getTotalReceivePrice(model.getId());
            itemTotalPrice = itemTotalPrice == null ? BigDecimal.ZERO : itemTotalPrice;

            // 应收账单对应的 应收账单费用明细表费用金额（不含税）
            BigDecimal costTotalPrice = tocSettlementReceiveBillCostItemDao.getTotalReceivePrice(model.getId());
            costTotalPrice = costTotalPrice == null ? BigDecimal.ZERO : costTotalPrice;

            // 计算应收金额
            BigDecimal receivePrice = itemTotalPrice.add(costTotalPrice);

            // 已核销金额
            BigDecimal collectedPrice = tocSettlementReceiveBillVerifyItemDao.getAllPriceByBillIds(Arrays.asList(model.getId()));

            // 计算本期未核销金额
            BigDecimal uncollectedPrice = receivePrice.subtract(collectedPrice);

            //最新的收款日期
            Timestamp receiveDate = tocSettlementReceiveBillVerifyItemDao.getLatestReceiveDate(model.getId());

            //计算客户账期
            Long customerId = model.getCustomerId();
            if (customerId == null) {
                LOGGER.error("应收账单客户id不能为空,应收账单号:" + model.getCode());
                throw new RuntimeException("应收账单客户id不能为空,应收账单号:" + model.getCode());
            }
            Map<String, Object> customerReMap = new HashMap<String, Object>();
            customerReMap.put("customerId", customerId);
            customerReMap.put("merchantId", model.getMerchantId());
            CustomerMerchantRelMongo customerMerchantRelMongo = customerMerchantRelMongoDao.findOne(customerReMap);
            if (customerMerchantRelMongo == null) {
                LOGGER.error("应收账单没有对应的商家客户信息,应收账单号:" + model.getCode());
                throw new RuntimeException("应收账单没有对应的商家客户信息,应收账单号:" + model.getCode());
            }

            String accountPeriod = customerMerchantRelMongo.getAccountPeriod();

            //如果账期 没有配置 默认为0
            if (StringUtils.isBlank(accountPeriod)) accountPeriod = DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_0;
            //计算逾期天数
            int overdueDayNum = 0;
            Date billDate = model.getSettlementDate();
            String billDateStr = TimeUtils.format(billDate, "yyyy-MM-dd");
            if (uncollectedPrice.compareTo(new BigDecimal(0)) == 0) {// 未核销为0  账期逾期天数=最近的收款日期 - （账单日期+客户账期天数）
                if (receiveDate == null) {
                    LOGGER.error("应收账单已经完全核销最近收款金额不能为空,应收账单号:" + model.getCode());
                    //throw new RuntimeException("应收账单已经完全核销最近收款金额不能为空,应收账单号"+model.getCode());
                } else {
                    String receiveDateStr = TimeUtils.format(receiveDate, "yyyy-MM-dd");
                    overdueDayNum = getOverdueDay(accountPeriod, receiveDateStr, billDateStr);
                }

            } else {//未核销不为0 //账期逾期天数= 当前日期 - （账单日期+客户账期天数）
                String receiveDateStr = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd");
                overdueDayNum = getOverdueDay(accountPeriod, receiveDateStr, billDateStr);
            }

            ReceiveBillVerificationModel verificationModel = new ReceiveBillVerificationModel();
            verificationModel.setReceiveId(model.getId());
            verificationModel.setReceiveCode(model.getCode());
            verificationModel.setMerchantId(model.getMerchantId());
            verificationModel.setMerchantName(model.getMerchantName());
            verificationModel.setCustomerId(model.getCustomerId());
            verificationModel.setCustomerName(model.getCustomerName());
            verificationModel.setReceivePrice(receivePrice);
            verificationModel.setUncollectedPrice(uncollectedPrice);
            verificationModel.setCurrency(model.getSettlementCurrency());
            verificationModel.setBillDate(new Timestamp(billDate.getTime()));
            verificationModel.setAccountPeriod(accountPeriod);
            verificationModel.setAccountOverdueDays(overdueDayNum);
            verificationModel.setBuId(model.getBuId());
            verificationModel.setBuName(model.getBuName());
            verificationModel.setBillStatus(model.getBillStatus());
            verificationModel.setBillType(DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_1);
            verificationModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILLVERIFICATION_INVOICESTATUS_0);
            verificationModel.setCreditMonth(TimeUtils.formatMonth(model.getBillInDate()));
            if (receiveBillInvoice != null) {
                verificationModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILLVERIFICATION_INVOICESTATUS_1);
                verificationModel.setInvoiceNo(receiveBillInvoice.getInvoiceNo());
                verificationModel.setInvoiceDate(receiveBillInvoice.getInvoiceDate());// 开单日期
            }

            String overdueStatus = null;
            if (overdueDayNum == 0) {//未逾期
                overdueStatus = DERP_ORDER.RECEIVEBILLVERIFICATION_OVERDUESTATUS_0;
            } else {
                overdueStatus = DERP_ORDER.RECEIVEBILLVERIFICATION_OVERDUESTATUS_1;
            }
            verificationModel.setOverdueStatus(overdueStatus);
            billVerificationList.add(verificationModel);
            receiveIdList.add(model.getId());

        }
        //获取已经作废应收账单的收款核销
        List<TocSettlementReceiveBillModel> ZfAnd006BillList = tocSettlementReceiveBillDao.getZfAnd006ReceiveBill(new ArrayList<>());
        for (TocSettlementReceiveBillModel model : ZfAnd006BillList) {
            receiveIdList.add(model.getId());
        }

        if (receiveIdList.size() > 0) {
            // 删除原来的收款核销表
            receiveBillVerificationDao.deleteByReceiveId(receiveIdList, DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_1);
        }

        // 循环新增
        for (ReceiveBillVerificationModel model : billVerificationList) {
            receiveBillVerificationDao.save(model);
        }
    }


}
