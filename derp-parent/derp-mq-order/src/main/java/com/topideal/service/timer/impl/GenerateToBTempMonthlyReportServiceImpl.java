package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.entity.vo.bill.*;
import com.topideal.service.timer.GenerateToBTempMonthlyReportService;
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
public class GenerateToBTempMonthlyReportServiceImpl implements GenerateToBTempMonthlyReportService {

    @Autowired
    private TobTemporaryReceiveBillDao tobTemporaryReceiveBillDao;
    @Autowired
    private TobTemporaryReceiveBillItemDao tobTemporaryReceiveBillItemDao;
    @Autowired
    private TobTemporaryReceiveBillRebateItemDao tobTemporaryReceiveBillRebateItemDao;
    @Autowired
    private TobTempVerifyRelDao tobTempVerifyRelDao;
    @Autowired
    private ReceiveCloseAccountsDao receiveCloseAccountsDao;
    @Autowired
    private TobTemporaryReceiveBillItemMonthlyDao tobTemporaryReceiveBillItemMonthlyDao;
    @Autowired
    private TobTemporaryReceiveBillCostItemMonthlyDao tobTemporaryReceiveBillCostItemMonthlyDao;


    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201168201,model=DERP_LOG_POINT.POINT_13201168201_Label)
    public boolean Save2BRevenueMonthly(String json, String keys, String topics, String tags) throws Exception {

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

                //根据商家、事业部、月份的维度查询tob暂估收入明细并生成月结数据
                save2BRevenueMonthlyReport(merId, buId, mon);
            }
        }

        return true;
    }


    private void save2BRevenueMonthlyReport(Long merchantId, Long buId, String month) throws Exception {
        /**
         * （1）取表 t_tob_temporary_receive_bill_item 中上架月份小于等于快照当月的应收暂估明细；
         * （2）根据符合归属月份的应收暂估明细id 查询暂估核销记录表 t_tob_temp_verify_rel 找到入账月份小于等于快照当月的已核销金额记录（仅核销类型为收入明细）；
         * （3）得出未核金额=以暂估明细为维度取 暂估应收金额 - 累计已核销金额；
         * （4）校验若未核金额=0则剔除该暂估明细id的记录，但除以下类型不做剔除：
         *   （4.1）未核金额=0，且核销记录中入账月份为快照当月的暂估明细；
         */

        //删除已生成的收入月结快照
        TobTemporaryReceiveBillItemMonthlyModel monthlyModel = new TobTemporaryReceiveBillItemMonthlyModel();
        monthlyModel.setMerchantId(merchantId);
        monthlyModel.setBuId(buId);
        monthlyModel.setMonth(month);
        tobTemporaryReceiveBillItemMonthlyDao.deleteByModel(monthlyModel);

        //查询应收结算状态为“已上架未结算”/“部分结算”且上架时间小于等于快照月份的应收暂估明细
        List<TobTemporaryReceiveBillItemModel> nonVerifyItemModels = tobTemporaryReceiveBillItemDao.listNonVerifyByCloseAccount(merchantId, buId, month);

        //查询应收结算状态为“已结算”且上架时间小于等于快照月份、核销记录中核销月份有大于等于快照月份的应收暂估明细
        List<TobTemporaryReceiveBillItemModel> verifyItemModels = tobTemporaryReceiveBillItemDao.listAllVerifyByCloseAccount(merchantId, buId, month);

        List<TobTemporaryReceiveBillItemModel> allItemModels = new ArrayList<>();
        allItemModels.addAll(nonVerifyItemModels);
        allItemModels.addAll(verifyItemModels);

        if (allItemModels.size() == 0) {
            return;
        }

        List<Long> itemIds = allItemModels.stream().map(e -> e.getId()).collect(Collectors.toList());

        //根据暂估明细id集合查询入账月份小于等于快照当月的核销记录
        List<TobTempVerifyRelModel> verifyRelModels = tobTempVerifyRelDao.getRelBeforeMonth(itemIds, month, DERP_ORDER.TOBTEMPVERIFYREL_TYPE_0);

        Map<Long, List<TobTempVerifyRelModel>> verifyRelModelsMap = verifyRelModels.stream().collect(Collectors.groupingBy(TobTempVerifyRelModel::getTobItemId));

        List<TobTemporaryReceiveBillItemMonthlyModel> itemMonthlyModels = new ArrayList<>();
        for (TobTemporaryReceiveBillItemModel itemModel : allItemModels) {

            TobTemporaryReceiveBillModel tobTemporaryReceiveBillModel = tobTemporaryReceiveBillDao.searchById(itemModel.getBillId());

            String poNo = itemModel.getPoNo();
            if (!DERP_ORDER.TOBTEMPRECEIVEBILL_SALETYPE_3.equals(tobTemporaryReceiveBillModel.getSaleType())) {
                poNo = tobTemporaryReceiveBillModel.getPoNo();
            }

            //如果暂估明细存在核销明细
            if (verifyRelModelsMap.containsKey(itemModel.getId())) {
                List<TobTempVerifyRelModel> relModels = verifyRelModelsMap.get(itemModel.getId());

                boolean flag = true;
                //已核销金额
                BigDecimal totalVerifyAmount = new BigDecimal("0");

                for (TobTempVerifyRelModel relModel : relModels) {
                    //判断入账时间是否等于刷新月份
                    if (compareDate(relModel.getCreditDate(), month)) {
                        flag = false;
                    }
                    totalVerifyAmount = totalVerifyAmount.add(relModel.getVerifiyAmount());
                }
                //应收金额
                BigDecimal receiveAmount = itemModel.getPrice().multiply(new BigDecimal(itemModel.getShelfNum())).setScale(2, BigDecimal.ROUND_HALF_UP);

                //判断未核销金额是否未0，若为0且所有的入账月份小于刷新月份则不生成快照
                if (flag && totalVerifyAmount.compareTo(receiveAmount) == 0) {
                    continue;
                }

                for (TobTempVerifyRelModel relModel : relModels) {
                    TobTemporaryReceiveBillItemMonthlyModel itemMonthlyModel = new TobTemporaryReceiveBillItemMonthlyModel();
                    itemMonthlyModel.setMerchantId(tobTemporaryReceiveBillModel.getMerchantId());
                    itemMonthlyModel.setMerchantName(tobTemporaryReceiveBillModel.getMerchantName());
                    itemMonthlyModel.setCustomerId(tobTemporaryReceiveBillModel.getCustomerId());
                    itemMonthlyModel.setCustomerName(tobTemporaryReceiveBillModel.getCustomerName());
                    itemMonthlyModel.setBuId(tobTemporaryReceiveBillModel.getBuId());
                    itemMonthlyModel.setBuName(tobTemporaryReceiveBillModel.getBuName());
                    itemMonthlyModel.setShelfCode(tobTemporaryReceiveBillModel.getShelfCode());
                    itemMonthlyModel.setOrderCode(tobTemporaryReceiveBillModel.getOrderCode());
                    itemMonthlyModel.setShelfDate(tobTemporaryReceiveBillModel.getShelfDate());
                    itemMonthlyModel.setCurrency(tobTemporaryReceiveBillModel.getCurrency());
                    itemMonthlyModel.setMonth(month);
                    itemMonthlyModel.setCreditMonth(TimeUtils.formatMonth(relModel.getCreditDate()));
                    itemMonthlyModel.setPoNo(poNo);
                    itemMonthlyModel.setGoodsId(itemModel.getGoodsId());
                    itemMonthlyModel.setGoodsNo(itemModel.getGoodsNo());
                    itemMonthlyModel.setGoodsName(itemModel.getGoodsName());
                    itemMonthlyModel.setParentBrandId(itemModel.getParentBrandId());
                    itemMonthlyModel.setParentBrandCode(itemModel.getParentBrandCode());
                    itemMonthlyModel.setParentBrandName(itemModel.getParentBrandName());
                    itemMonthlyModel.setShelfNum(itemModel.getShelfNum());
                    itemMonthlyModel.setPrice(itemModel.getPrice());
                    itemMonthlyModel.setVerifiedAmount(relModel.getVerifiyAmount());
                    itemMonthlyModel.setReceiveCode(relModel.getReceiveCode());
                    itemMonthlyModel.setReceiveId(relModel.getReceiveId());
                    itemMonthlyModel.setCreateDate(TimeUtils.getNow());
                    itemMonthlyModel.setTempItemId(itemModel.getId());
                    itemMonthlyModels.add(itemMonthlyModel);
                }
            } else {
                TobTemporaryReceiveBillItemMonthlyModel itemMonthlyModel = new TobTemporaryReceiveBillItemMonthlyModel();
                itemMonthlyModel.setMerchantId(tobTemporaryReceiveBillModel.getMerchantId());
                itemMonthlyModel.setMerchantName(tobTemporaryReceiveBillModel.getMerchantName());
                itemMonthlyModel.setCustomerId(tobTemporaryReceiveBillModel.getCustomerId());
                itemMonthlyModel.setCustomerName(tobTemporaryReceiveBillModel.getCustomerName());
                itemMonthlyModel.setBuId(tobTemporaryReceiveBillModel.getBuId());
                itemMonthlyModel.setBuName(tobTemporaryReceiveBillModel.getBuName());
                itemMonthlyModel.setShelfCode(tobTemporaryReceiveBillModel.getShelfCode());
                itemMonthlyModel.setOrderCode(tobTemporaryReceiveBillModel.getOrderCode());
                itemMonthlyModel.setShelfDate(tobTemporaryReceiveBillModel.getShelfDate());
                itemMonthlyModel.setCurrency(tobTemporaryReceiveBillModel.getCurrency());
                itemMonthlyModel.setMonth(month);
                itemMonthlyModel.setPoNo(poNo);
                itemMonthlyModel.setGoodsId(itemModel.getGoodsId());
                itemMonthlyModel.setGoodsNo(itemModel.getGoodsNo());
                itemMonthlyModel.setGoodsName(itemModel.getGoodsName());
                itemMonthlyModel.setParentBrandId(itemModel.getParentBrandId());
                itemMonthlyModel.setParentBrandCode(itemModel.getParentBrandCode());
                itemMonthlyModel.setParentBrandName(itemModel.getParentBrandName());
                itemMonthlyModel.setShelfNum(itemModel.getShelfNum());
                itemMonthlyModel.setPrice(itemModel.getPrice());
                itemMonthlyModel.setCreateDate(TimeUtils.getNow());
                itemMonthlyModel.setTempItemId(itemModel.getId());
                itemMonthlyModels.add(itemMonthlyModel);
            }

        }

        //批量新增
        int pageSize = 1000;
        for (int i = 0; i < itemMonthlyModels.size(); ) {
            int itemPageSub = (i + pageSize) < itemMonthlyModels.size() ? (i + pageSize) : itemMonthlyModels.size();
            tobTemporaryReceiveBillItemMonthlyDao.batchSave(itemMonthlyModels.subList(i, itemPageSub));
            i = itemPageSub;
        }

    }

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201168202,model=DERP_LOG_POINT.POINT_13201168202_Label)
    public boolean Save2BCostMonthly(String json, String keys, String topics, String tags) throws Exception {
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

                //根据商家、事业部、月份的维度查询tob暂估费用明细并生成月结数据
                save2BCostMonthlyReport(merId, buId, mon);
            }
        }

        return true;
    }

    private void save2BCostMonthlyReport(Long merchantId, Long buId, String month) throws Exception {
        /**
         * （1）取表 t_tob_temporary_receive_bill_rebate_item 中上架月份小于等于快照当月的返利暂估明细；
         * （2）根据符合归属月份的返利暂估明细id 查询暂估核销记录表 t_tob_temp_verify_rel 找到入账月份小于等于快照当月的已核销金额记录（仅核销类型为返利）；
         * （3）得出未核金额=基于对应明细的 暂估返利金额 - 已核销金额；
         * （4）校验若未核金额=0则剔除该暂估明细id的记录，但除以下类型不做剔除：
     *        （4.1）未核金额=0，且核销记录中入账月份为快照当月的暂估明细；
         */

        //删除已生成的费用月结快照
        TobTemporaryReceiveBillCostItemMonthlyModel monthlyModel = new TobTemporaryReceiveBillCostItemMonthlyModel();
        monthlyModel.setMerchantId(merchantId);
        monthlyModel.setBuId(buId);
        monthlyModel.setMonth(month);
        tobTemporaryReceiveBillCostItemMonthlyDao.deleteByModel(monthlyModel);

        //查询应收结算状态为“已上架未结算”/“部分结算”且上架时间小于等于快照月份的应收暂估明细
        List<TobTemporaryReceiveBillRebateItemModel> nonVerifyItemModels = tobTemporaryReceiveBillRebateItemDao.listNonVerifyByCloseAccount(merchantId, buId, month);

        //查询应收结算状态为“已结算”且上架时间小于等于快照月份、核销记录中核销月份有大于等于快照月份的应收暂估明细
        List<TobTemporaryReceiveBillRebateItemModel> verifyItemModels = tobTemporaryReceiveBillRebateItemDao.listAllVerifyByCloseAccount(merchantId, buId, month);

        List<TobTemporaryReceiveBillRebateItemModel> allItemModels = new ArrayList<>();
        allItemModels.addAll(nonVerifyItemModels);
        allItemModels.addAll(verifyItemModels);

        if (allItemModels.size() == 0) {
            return;
        }

        List<Long> itemIds = allItemModels.stream().map(e -> e.getId()).collect(Collectors.toList());

        //根据暂估明细id集合查询入账月份小于等于快照当月的核销记录
        List<TobTempVerifyRelModel> verifyRelModels = tobTempVerifyRelDao.getRelBeforeMonth(itemIds, month, DERP_ORDER.TOBTEMPVERIFYREL_TYPE_1);

        Map<Long, List<TobTempVerifyRelModel>> verifyRelModelsMap = verifyRelModels.stream().collect(Collectors.groupingBy(TobTempVerifyRelModel::getTobItemId));

        List<TobTemporaryReceiveBillCostItemMonthlyModel> itemMonthlyModels = new ArrayList<>();
        for (TobTemporaryReceiveBillRebateItemModel itemModel : allItemModels) {

            TobTemporaryReceiveBillModel tobTemporaryReceiveBillModel = tobTemporaryReceiveBillDao.searchById(itemModel.getBillId());

            //如果暂估明细存在核销明细
            if (verifyRelModelsMap.containsKey(itemModel.getId())) {
                List<TobTempVerifyRelModel> relModels = verifyRelModelsMap.get(itemModel.getId());

                boolean flag = true;
                //已核销金额
                BigDecimal totalVerifyAmount = new BigDecimal("0");

                for (TobTempVerifyRelModel relModel : relModels) {
                    //判断入账时间是否等于刷新月份
                    if (compareDate(relModel.getCreditDate(), month)) {
                        flag = false;
                    }
                    totalVerifyAmount = totalVerifyAmount.add(relModel.getVerifiyAmount());
                }
                //应收金额
                BigDecimal receiveAmount = itemModel.getPrice().multiply(new BigDecimal(itemModel.getShelfNum())).setScale(2, BigDecimal.ROUND_HALF_UP);

                //判断未核销金额是否未0，若为0且所有的入账月份小于刷新月份则不生成快照
                if (flag && totalVerifyAmount.compareTo(receiveAmount) == 0) {
                    continue;
                }

                for (TobTempVerifyRelModel relModel : relModels) {
                    TobTemporaryReceiveBillCostItemMonthlyModel itemMonthlyModel = new TobTemporaryReceiveBillCostItemMonthlyModel();
                    itemMonthlyModel.setMerchantId(tobTemporaryReceiveBillModel.getMerchantId());
                    itemMonthlyModel.setMerchantName(tobTemporaryReceiveBillModel.getMerchantName());
                    itemMonthlyModel.setCustomerId(tobTemporaryReceiveBillModel.getCustomerId());
                    itemMonthlyModel.setCustomerName(tobTemporaryReceiveBillModel.getCustomerName());
                    itemMonthlyModel.setBuId(tobTemporaryReceiveBillModel.getBuId());
                    itemMonthlyModel.setBuName(tobTemporaryReceiveBillModel.getBuName());
                    itemMonthlyModel.setShelfCode(tobTemporaryReceiveBillModel.getShelfCode());
                    itemMonthlyModel.setSdCode(itemModel.getRelSdCode());
                    itemMonthlyModel.setShelfDate(tobTemporaryReceiveBillModel.getShelfDate());
                    itemMonthlyModel.setCurrency(tobTemporaryReceiveBillModel.getCurrency());
                    itemMonthlyModel.setPoNo(tobTemporaryReceiveBillModel.getPoNo());
                    itemMonthlyModel.setMonth(month);
                    itemMonthlyModel.setCreditMonth(TimeUtils.formatMonth(relModel.getCreditDate()));
                    itemMonthlyModel.setGoodsId(itemModel.getGoodsId());
                    itemMonthlyModel.setGoodsNo(itemModel.getGoodsNo());
                    itemMonthlyModel.setGoodsName(itemModel.getGoodsName());
                    itemMonthlyModel.setParentBrandId(itemModel.getParentBrandId());
                    itemMonthlyModel.setParentBrandCode(itemModel.getParentBrandCode());
                    itemMonthlyModel.setParentBrandName(itemModel.getParentBrandName());
                    itemMonthlyModel.setShelfNum(itemModel.getShelfNum());
                    itemMonthlyModel.setPrice(itemModel.getPrice());
                    itemMonthlyModel.setVerifiedAmount(relModel.getVerifiyAmount());
                    itemMonthlyModel.setRebateAmount(itemModel.getRebateAmount());
                    itemMonthlyModel.setSdRatio(itemModel.getSdRatio());
                    itemMonthlyModel.setSdTypeId(itemModel.getSdTypeId());
                    itemMonthlyModel.setSdTypeName(itemModel.getSdTypeName());
                    itemMonthlyModel.setReceiveCode(relModel.getReceiveCode());
                    itemMonthlyModel.setReceiveId(relModel.getReceiveId());
                    itemMonthlyModel.setCreateDate(TimeUtils.getNow());
                    itemMonthlyModel.setTempItemId(itemModel.getId());
                    itemMonthlyModels.add(itemMonthlyModel);
                }
            } else {
                TobTemporaryReceiveBillCostItemMonthlyModel itemMonthlyModel = new TobTemporaryReceiveBillCostItemMonthlyModel();
                itemMonthlyModel.setMerchantId(tobTemporaryReceiveBillModel.getMerchantId());
                itemMonthlyModel.setMerchantName(tobTemporaryReceiveBillModel.getMerchantName());
                itemMonthlyModel.setCustomerId(tobTemporaryReceiveBillModel.getCustomerId());
                itemMonthlyModel.setCustomerName(tobTemporaryReceiveBillModel.getCustomerName());
                itemMonthlyModel.setBuId(tobTemporaryReceiveBillModel.getBuId());
                itemMonthlyModel.setBuName(tobTemporaryReceiveBillModel.getBuName());
                itemMonthlyModel.setShelfCode(tobTemporaryReceiveBillModel.getShelfCode());
                itemMonthlyModel.setPoNo(tobTemporaryReceiveBillModel.getPoNo());
                itemMonthlyModel.setSdCode(itemModel.getRelSdCode());
                itemMonthlyModel.setShelfDate(tobTemporaryReceiveBillModel.getShelfDate());
                itemMonthlyModel.setCurrency(tobTemporaryReceiveBillModel.getCurrency());
                itemMonthlyModel.setMonth(month);
                itemMonthlyModel.setGoodsId(itemModel.getGoodsId());
                itemMonthlyModel.setGoodsNo(itemModel.getGoodsNo());
                itemMonthlyModel.setGoodsName(itemModel.getGoodsName());
                itemMonthlyModel.setParentBrandId(itemModel.getParentBrandId());
                itemMonthlyModel.setParentBrandCode(itemModel.getParentBrandCode());
                itemMonthlyModel.setParentBrandName(itemModel.getParentBrandName());
                itemMonthlyModel.setShelfNum(itemModel.getShelfNum());
                itemMonthlyModel.setPrice(itemModel.getPrice());
                itemMonthlyModel.setRebateAmount(itemModel.getRebateAmount());
                itemMonthlyModel.setSdRatio(itemModel.getSdRatio());
                itemMonthlyModel.setSdTypeId(itemModel.getSdTypeId());
                itemMonthlyModel.setSdTypeName(itemModel.getSdTypeName());
                itemMonthlyModel.setCreateDate(TimeUtils.getNow());
                itemMonthlyModel.setTempItemId(itemModel.getId());
                itemMonthlyModels.add(itemMonthlyModel);
            }

        }

        //批量新增
        int pageSize = 1000;
        for (int i = 0; i < itemMonthlyModels.size(); ) {
            int itemPageSub = (i + pageSize) < itemMonthlyModels.size() ? (i + pageSize) : itemMonthlyModels.size();
            tobTemporaryReceiveBillCostItemMonthlyDao.batchSave(itemMonthlyModels.subList(i, itemPageSub));
            i = itemPageSub;
        }

    }

    private boolean compareDate(Timestamp timestamp, String month) {

        String flag = TimeUtils.timeComparisonSize(timestamp, TimeUtils.parse(month, "yyyy-MM"));

        if ("1".equals(flag)) {
            return true;
        }

        return false;
    }
}
