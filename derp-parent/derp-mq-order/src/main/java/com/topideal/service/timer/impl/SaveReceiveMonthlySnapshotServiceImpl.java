package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.entity.vo.bill.*;
import com.topideal.service.timer.SaveReceiveMonthlySnapshotService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SaveReceiveMonthlySnapshotServiceImpl implements SaveReceiveMonthlySnapshotService {

    @Autowired
    private ReceiveCloseAccountsDao receiveCloseAccountsDao;
    @Autowired
    private BillMonthlySnapshotDao billMonthlySnapshotDao;
    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private ReceiveBillVerifyItemDao receiveBillVerifyItemDao;
    @Autowired
    private ReceiveBillItemDao receiveBillItemDao;
    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao;
    @Autowired
    private ReceiveBillInvoiceDao receiveBillInvoiceDao;
    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
    @Autowired
    private TocSettlementReceiveBillVerifyItemDao tocSettlementReceiveBillVerifyItemDao;

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201168203, model = DERP_LOG_POINT.POINT_13201168203_Label)
    public boolean SaveReceiveMonthlySnapshot(String json, String keys, String topics, String tags) throws Exception {

        JSONObject jsonData = JSONObject.fromObject(json);
        //公司
        Integer merchantId = (Integer) jsonData.get("merchantId");

        //月结月份
        String month = (String) jsonData.get("month");

        List<String> months = new ArrayList<>();

        if (StringUtils.isBlank(month)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            //当前月份
            month = sdf.format(new Date());

            //上个月
            String upMonth = TimeUtils.getLastMonth(new Date());
            months.add(upMonth);
        }
        months.add(month);

        for (String mon : months) {
            ReceiveCloseAccountsModel closeAccountsModel = new ReceiveCloseAccountsModel();
            closeAccountsModel.setStatus(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_029);
            closeAccountsModel.setMonth(mon);
            if (merchantId != null && merchantId > 0) {
                closeAccountsModel.setMerchantId(Long.valueOf(merchantId));
            }

            List<ReceiveCloseAccountsModel> closeAccountsModels = receiveCloseAccountsDao.list(closeAccountsModel);

            for (ReceiveCloseAccountsModel receiveCloseAccountsModel : closeAccountsModels) {
                Long merId = receiveCloseAccountsModel.getMerchantId();
                Long buId = receiveCloseAccountsModel.getBuId();

                //根据商家、事业部、月份的维度查询tob应收账单并生成月结快照
                save2BReceiveMonthlySnapshot(merId, buId, mon);

                //根据商家、事业部、月份的维度查询toc应收账单并生成月结快照
                save2CReceiveMonthlySnapshot(merId, buId, mon);

            }
        }

        return true;
    }

    /**
     * 2C 根据公司、事业部、月结月份生成月结快照
     * @param merchantId
     * @param buId
     * @param month
     * @throws Exception
     */
    private void save2CReceiveMonthlySnapshot(Long merchantId, Long buId, String month) throws Exception {

        //删除公司、事业部、月结月份的月结快照
        BillMonthlySnapshotModel billMonthlySnapshotModel = new BillMonthlySnapshotModel();
        billMonthlySnapshotModel.setMerchantId(merchantId);
        billMonthlySnapshotModel.setBuId(buId);
        billMonthlySnapshotModel.setMonth(month);
        billMonthlySnapshotModel.setBillType(DERP_ORDER.BILLMONTHLYSNAPSHOT_BILLTYPE_1);
        billMonthlySnapshotDao.deleteByModel(billMonthlySnapshotModel);


        List<TocSettlementReceiveBillModel> allReceiveBillModels = new ArrayList<>();
        //查询该公司+事业部、入账月份小于等于月结月份维度下的待核销、部分核销、作废待审的toc应收账单
        List<TocSettlementReceiveBillModel> beVerifyReceiveModels = tocSettlementReceiveBillDao.listByMonthlySnapshot(merchantId, buId, month);
        allReceiveBillModels.addAll(beVerifyReceiveModels);

        //查询该公司+事业部、入账月份小于等于月结月份、核销月份大于月结月份的已核销的toc应收账单
        List<TocSettlementReceiveBillModel> verifyReceiveModels = tocSettlementReceiveBillDao.listByAllVerifyMonthlySnapshot(merchantId, buId, month);
        allReceiveBillModels.addAll(verifyReceiveModels);

        List<Long> billIds = allReceiveBillModels.stream().map(receiveBillModel -> receiveBillModel.getId()).collect(Collectors.toList());

        if (billIds.size() == 0) {
            return;
        }

        Map<Long, BigDecimal> verifyAmountMap = new HashMap<>();

        //查询toc应收账单收款核销记录中核销月份小于等于应收月份的已核销金额
        List<Map<String, Object>> verifyAmountMapList = tocSettlementReceiveBillVerifyItemDao.getVerifyAmountByBillIds(billIds, month);

        for (Map<String, Object> map : verifyAmountMapList) {
            Long billId = (Long) map.get("billId");
            BigDecimal totalItemPrice = (BigDecimal) map.get("amount");
            verifyAmountMap.put(billId, totalItemPrice);
        }

        List<BillMonthlySnapshotModel> monthlySnapshotModels = new ArrayList<>();

        for (TocSettlementReceiveBillModel receiveBillModel : allReceiveBillModels) {

            BillMonthlySnapshotModel monthlySnapshotModel = new BillMonthlySnapshotModel();
            monthlySnapshotModel.setMerchantId(receiveBillModel.getMerchantId());
            monthlySnapshotModel.setMerchantName(receiveBillModel.getMerchantName());
            monthlySnapshotModel.setCustomerId(receiveBillModel.getCustomerId());
            monthlySnapshotModel.setCustomerName(receiveBillModel.getCustomerName());
            monthlySnapshotModel.setStorePlatformCode(receiveBillModel.getStorePlatformCode());
            monthlySnapshotModel.setBuId(receiveBillModel.getBuId());
            monthlySnapshotModel.setBuName(receiveBillModel.getBuName());
            monthlySnapshotModel.setMonth(month);
            monthlySnapshotModel.setCreditMonth(TimeUtils.formatMonth(receiveBillModel.getBillInDate()));
            monthlySnapshotModel.setReceiveCode(receiveBillModel.getCode());
            monthlySnapshotModel.setBillType(DERP_ORDER.BILLMONTHLYSNAPSHOT_BILLTYPE_1);
            monthlySnapshotModel.setCurrency(receiveBillModel.getSettlementCurrency());
            monthlySnapshotModel.setShopTypeCode(receiveBillModel.getShopTypeCode());
            monthlySnapshotModel.setBillDate(new Timestamp(receiveBillModel.getSettlementDate().getTime()));

            if (receiveBillModel.getInvoiceId() != null) {
                ReceiveBillInvoiceModel receiveBillInvoiceModel = receiveBillInvoiceDao.searchById(receiveBillModel.getInvoiceId());
                monthlySnapshotModel.setInvoiceDate(receiveBillInvoiceModel.getInvoiceDate());
                monthlySnapshotModel.setInvoiceNo(receiveBillInvoiceModel.getInvoiceNo());
            }

            //累计核销金额
            BigDecimal verifyAmount = verifyAmountMap.get(receiveBillModel.getId()) == null ? BigDecimal.ZERO : verifyAmountMap.get(receiveBillModel.getId());

            //未核销金额=应收金额-累计已核销金额
            BigDecimal nonVerifyAmount = receiveBillModel.getReceivableAmount().subtract(verifyAmount);

            monthlySnapshotModel.setReceivableAmount(receiveBillModel.getReceivableAmount());
            monthlySnapshotModel.setNonverifyAmount(nonVerifyAmount);

            monthlySnapshotModels.add(monthlySnapshotModel);

        }

        //批量新增月结快照
        int pageSize = 1000;
        for (int i = 0; i < monthlySnapshotModels.size(); ) {
            int itemPageSub = (i + pageSize) < monthlySnapshotModels.size() ? (i + pageSize) : monthlySnapshotModels.size();
            billMonthlySnapshotDao.batchSave(monthlySnapshotModels.subList(i, itemPageSub));
            i = itemPageSub;
        }

    }


    /**
     * 2B 根据公司、事业部、月结月份生成月结快照
     * @param merchantId
     * @param buId
     * @param month
     * @throws Exception
     */
    private void save2BReceiveMonthlySnapshot(Long merchantId, Long buId, String month) throws Exception {

        //删除公司、事业部、月结月份的月结快照
        BillMonthlySnapshotModel billMonthlySnapshotModel = new BillMonthlySnapshotModel();
        billMonthlySnapshotModel.setMerchantId(merchantId);
        billMonthlySnapshotModel.setBuId(buId);
        billMonthlySnapshotModel.setMonth(month);
        billMonthlySnapshotModel.setBillType(DERP_ORDER.BILLMONTHLYSNAPSHOT_BILLTYPE_0);
        billMonthlySnapshotDao.deleteByModel(billMonthlySnapshotModel);


        List<ReceiveBillModel> allReceiveBillModels = new ArrayList<>();
        //查询该公司+事业部、入账月份小于等于月结月份维度下的待核销、部分核销、作废待审的应收账单
        List<ReceiveBillModel> beVerifyReceiveModels = receiveBillDao.listByMonthlySnapshot(merchantId, buId, month);
        allReceiveBillModels.addAll(beVerifyReceiveModels);

        //查询该公司+事业部、入账月份小于等于月结月份、核销月份大于月结月份的已核销的应收账单
        List<ReceiveBillModel> verifyReceiveModels = receiveBillDao.listByAllVerifyMonthlySnapshot(merchantId, buId, month);
        allReceiveBillModels.addAll(verifyReceiveModels);

        List<Long> billIds = allReceiveBillModels.stream().map(receiveBillModel -> receiveBillModel.getId()).collect(Collectors.toList());

        if (billIds.size() == 0) {
            return;
        }

        Map<Long, BigDecimal> itemAmountMap = new HashMap<>();
        Map<Long, BigDecimal> costItemAmountMap = new HashMap<>();
        Map<Long, BigDecimal> verifyAmountMap = new HashMap<>();

        //应收账单的收入金额
        List<Map<String, Object>> itemAmountMapList = receiveBillItemDao.listItemPrice(billIds);

        for (Map<String, Object> map : itemAmountMapList) {
            Long billId = (Long) map.get("billId");
            BigDecimal totalItemPrice = (BigDecimal) map.get("totalPrice");
            itemAmountMap.put(billId, totalItemPrice);
        }

        //应收账单的费用金额
        List<Map<String, Object>> costItemAmountMapList = receiveBillCostItemDao.listCostPrice(billIds);

        for (Map<String, Object> map : costItemAmountMapList) {
            Long billId = (Long) map.get("billId");
            BigDecimal totalItemPrice = (BigDecimal) map.get("totalPrice");
            costItemAmountMap.put(billId, totalItemPrice);
        }

        //查询应收账单收款核销记录中核销月份小于等于应收月份的已核销金额
        List<Map<String, Object>> verifyAmountMapList = receiveBillVerifyItemDao.getVerifyAmountByBillIds(billIds, month);

        for (Map<String, Object> map : verifyAmountMapList) {
            Long billId = (Long) map.get("billId");
            BigDecimal totalItemPrice = (BigDecimal) map.get("amount");
            verifyAmountMap.put(billId, totalItemPrice);
        }

        List<BillMonthlySnapshotModel> monthlySnapshotModels = new ArrayList<>();

        for (ReceiveBillModel receiveBillModel : allReceiveBillModels) {

            BillMonthlySnapshotModel monthlySnapshotModel = new BillMonthlySnapshotModel();
            monthlySnapshotModel.setMerchantId(receiveBillModel.getMerchantId());
            monthlySnapshotModel.setMerchantName(receiveBillModel.getMerchantName());
            monthlySnapshotModel.setCustomerId(receiveBillModel.getCustomerId());
            monthlySnapshotModel.setCustomerName(receiveBillModel.getCustomerName());
            monthlySnapshotModel.setBuId(receiveBillModel.getBuId());
            monthlySnapshotModel.setBuName(receiveBillModel.getBuName());
            monthlySnapshotModel.setMonth(month);
            monthlySnapshotModel.setCreditMonth(TimeUtils.formatMonth(receiveBillModel.getCreditDate()));
            monthlySnapshotModel.setReceiveCode(receiveBillModel.getCode());
            monthlySnapshotModel.setBillType(DERP_ORDER.BILLMONTHLYSNAPSHOT_BILLTYPE_0);
            monthlySnapshotModel.setCurrency(receiveBillModel.getCurrency());
            monthlySnapshotModel.setBillDate(receiveBillModel.getBillDate());

            if (receiveBillModel.getInvoiceId() != null) {
                ReceiveBillInvoiceModel receiveBillInvoiceModel = receiveBillInvoiceDao.searchById(receiveBillModel.getInvoiceId());
                monthlySnapshotModel.setInvoiceDate(receiveBillInvoiceModel.getInvoiceDate());
                monthlySnapshotModel.setInvoiceNo(receiveBillInvoiceModel.getInvoiceNo());
            }

            //收入金额
            BigDecimal itemAmount = itemAmountMap.get(receiveBillModel.getId()) == null ? BigDecimal.ZERO : itemAmountMap.get(receiveBillModel.getId());
            //费用金额
            BigDecimal costItemAmount = costItemAmountMap.get(receiveBillModel.getId()) == null ? BigDecimal.ZERO : costItemAmountMap.get(receiveBillModel.getId());
            //累计核销金额
            BigDecimal verifyAmount = verifyAmountMap.get(receiveBillModel.getId()) == null ? BigDecimal.ZERO : verifyAmountMap.get(receiveBillModel.getId());

            //应收金额 = 收入+费用金额
            BigDecimal receiveAmount = itemAmount.add(costItemAmount);
            //未核销金额=应收金额-累计已核销金额
            BigDecimal nonVerifyAmount = receiveAmount.subtract(verifyAmount);

            monthlySnapshotModel.setReceivableAmount(receiveAmount);
            monthlySnapshotModel.setNonverifyAmount(nonVerifyAmount);

            monthlySnapshotModels.add(monthlySnapshotModel);

        }

        //批量新增月结快照
        int pageSize = 1000;
        for (int i = 0; i < monthlySnapshotModels.size(); ) {
            int itemPageSub = (i + pageSize) < monthlySnapshotModels.size() ? (i + pageSize) : monthlySnapshotModels.size();
            billMonthlySnapshotDao.batchSave(monthlySnapshotModels.subList(i, itemPageSub));
            i = itemPageSub;
        }

    }


}
