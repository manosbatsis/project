package com.topideal.service.order.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.entity.dto.bill.TocTempCostBillItemMonthlyDTO;
import com.topideal.entity.dto.bill.TocTempReceiveBillItemMonthlyDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillItemDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.service.order.SaveTocTempBillMonthlyService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/18 15:33
 * @Description: 生成toc暂估月结数据
 */
@Service
public class SaveTocTempBillMonthlyServiceImpl implements SaveTocTempBillMonthlyService {

    @Autowired
    private TocTemporaryReceiveBillItemDao tocTemporaryReceiveBillItemDao;
    @Autowired
    private TocTemporaryReceiveBillCostItemDao tocTemporaryReceiveBillCostItemDao;
    @Autowired
    private TocTemporaryReceiveBillItemMonthlyDao tocTemporaryReceiveBillItemMonthlyDao;
    @Autowired
    private TocTemporaryReceiveBillCostItemMonthlyDao tocTemporaryReceiveBillCostItemMonthlyDao;
    @Autowired
    private ReceiveCloseAccountsDao receiveCloseAccountsDao;

    private static final String ORDERTYPE_COST = "0";
    private static final String ORDERTYPE_RECEIVE = "1";

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.compareTo(null));
    }

    @Override
    public void saveToCTempReceiveBillMonthly(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String month = jsonData.getString("month");
        Integer merchantId = (Integer) jsonData.get("merchantId");
        Integer buId = jsonData.containsKey("buId") ? (Integer) jsonData.get("buId") : null;
        Integer customerId = jsonData.containsKey("customerId") ? (Integer) jsonData.get("customerId") : null;
        String shopCode = jsonData.containsKey("shopCode") ? (String) jsonData.get("shopCode") : null;
        String shopTypeCode = jsonData.containsKey("shopTypeCode") ? (String) jsonData.get("shopTypeCode") : null;
        String storePlatformCode = jsonData.containsKey("storePlatformCode") ? (String) jsonData.get("storePlatformCode") : null;

        TocTempReceiveBillItemMonthlyDTO dto = new TocTempReceiveBillItemMonthlyDTO();
        dto.setMerchantId(merchantId.longValue());
        if(buId != null) {
            dto.setBuId(buId.longValue());
        }
        if(customerId != null) {
            dto.setCustomerId(customerId.longValue());
        }
        dto.setShopCode(shopCode);
        dto.setShopTypeCode(shopTypeCode);
        dto.setStorePlatformCode(storePlatformCode);
        dto.setMonth(month);

        //删除月结
        tocTemporaryReceiveBillItemMonthlyDao.deleteByDTO(dto);

        //1.取表2C暂估应收明细表 t_toc_temporary_receive_bill_item 中归属月份小于等于快照当月且未核销的明细
        TocTemporaryReceiveBillItemDTO itemDTO = new TocTemporaryReceiveBillItemDTO();
        itemDTO.setMerchantId(merchantId.longValue());
        if(buId != null) {
            itemDTO.setBuId(buId.longValue());
        }
        itemDTO.setStorePlatformCode(storePlatformCode);
        itemDTO.setShopTypeCode(shopTypeCode);
        if(customerId != null) {
            itemDTO.setCustomerId(customerId.longValue());
        }
        itemDTO.setShopCode(shopCode);
        itemDTO.setMonthBillLastDate(TimeUtils.getLastDayOfMonth(month));
        itemDTO.setMonthEnd(month); //归属月份小于等于快照当月
        itemDTO.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0); // 未核销
        itemDTO.setSmallEqBillInDate(true);
        int unVerifyCountNum = tocTemporaryReceiveBillItemDao.countTempBillNum(itemDTO);

        Integer pageSize = 1000;
        for (int i = 0; i < unVerifyCountNum; ) {
            int pageSub = (i + pageSize) < unVerifyCountNum ? (i + pageSize) : unVerifyCountNum;

            itemDTO.setBegin(i);
            itemDTO.setPageSize(pageSize);
            i = pageSub;

            List<TocTemporaryReceiveBillItemModel> itemListPage = tocTemporaryReceiveBillItemDao.getItemListPage(itemDTO);
            List<TocTemporaryReceiveBillItemMonthlyModel> collect = itemListPage.stream().map(entity -> {
                TocTemporaryReceiveBillItemMonthlyModel monthlyModel = new TocTemporaryReceiveBillItemMonthlyModel();
                BeanUtils.copyProperties(entity, monthlyModel);
                String tempMonth = monthlyModel.getMonth();
                monthlyModel.setTempMonth(tempMonth);
                monthlyModel.setMonth(month);
                monthlyModel.setId(null);
                BigDecimal tempRmbAmount = monthlyModel.getTemporaryRmbAmount() == null ? new BigDecimal("0") : monthlyModel.getTemporaryRmbAmount();
                BigDecimal writeOffAmount = monthlyModel.getWriteOffAmount() == null ? new BigDecimal("0") : monthlyModel.getWriteOffAmount();
                BigDecimal settlementAmount = monthlyModel.getSettlementRmbAmount() == null ? new BigDecimal("0") : monthlyModel.getSettlementRmbAmount();
                monthlyModel.setLastReceiveAmount(tempRmbAmount.subtract(writeOffAmount).subtract(settlementAmount));
                return monthlyModel;
            }).collect(Collectors.toList());

            tocTemporaryReceiveBillItemMonthlyDao.batchSaveByBillItemList(collect);
            itemListPage.clear();
            collect.clear();
        }

        //2.查找已核销但入账月份大于月结最后一天的数据，剔除掉结算信息进行保存
        //得出当月月结未核金额=第(2)点中结算状态为 “未核销” 的未核金额 + 第(3)点中剔除掉被入账月份大于快照当月的结算账单核销的暂估明细金额
        itemDTO.setSettlementMark(null); //所有状态
        itemDTO.setCompareBillInDate(true); // 获取入账时间大于月结月份
//        itemDTO.setSearchVerifyStatus(true);
        itemDTO.setSmallEqBillInDate(false);
        int verifyCountNum = tocTemporaryReceiveBillItemDao.countTempBillNum(itemDTO);
        for (int i = 0; i < verifyCountNum; ) {
            int pageSub = (i + pageSize) < verifyCountNum ? (i + pageSize) : verifyCountNum;

            itemDTO.setBegin(i);
            itemDTO.setPageSize(pageSize);
            i = pageSub;

            List<TocTemporaryReceiveBillItemModel> itemListPage = tocTemporaryReceiveBillItemDao.getItemListPage(itemDTO);

            // 清空数据中的结算信息
            List<TocTemporaryReceiveBillItemMonthlyModel> collect = itemListPage.stream().map(entity -> {
                TocTemporaryReceiveBillItemMonthlyModel monthlyModel = new TocTemporaryReceiveBillItemMonthlyModel();
                BeanUtils.copyProperties(entity, monthlyModel);
                String tempMonth = monthlyModel.getMonth();
                monthlyModel.setTempMonth(tempMonth);
                monthlyModel.setMonth(month);
                monthlyModel.setId(null);
                monthlyModel.setSettlementCode(null);
                monthlyModel.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
                monthlyModel.setSettlementDate(null);
                monthlyModel.setSettlementOriAmount(null);
                monthlyModel.setSettlementRmbAmount(null);
                BigDecimal tempRmbAmount = monthlyModel.getTemporaryRmbAmount() == null ? new BigDecimal("0") : monthlyModel.getTemporaryRmbAmount();
                BigDecimal writeOffAmount = monthlyModel.getWriteOffAmount() == null ? new BigDecimal("0") : monthlyModel.getWriteOffAmount();
                BigDecimal settlementAmount = monthlyModel.getSettlementRmbAmount() == null ? new BigDecimal("0") : monthlyModel.getSettlementRmbAmount();
                monthlyModel.setLastReceiveAmount(tempRmbAmount.subtract(writeOffAmount).subtract(settlementAmount));
                return monthlyModel;
            }).collect(Collectors.toList());

            tocTemporaryReceiveBillItemMonthlyDao.batchSaveByBillItemList(collect);
            itemListPage.clear();
            collect.clear();
        }
    }

    @Override
    public void saveToCTempCostBillMonthly(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        Integer merchantId = (Integer) jsonData.get("merchantId");
        String month = jsonData.getString("month");
        Integer buId = (Integer) jsonData.get("buId");
        Integer customerId = jsonData.containsKey("customerId") ? (Integer) jsonData.get("customerId") : null;
        String shopCode = jsonData.containsKey("shopCode") ? (String) jsonData.get("shopCode") : null;
        String shopTypeCode = jsonData.containsKey("shopTypeCode") ? (String) jsonData.get("shopTypeCode") : null;
        String storePlatformCode = jsonData.containsKey("storePlatformCode") ? (String) jsonData.get("storePlatformCode") : null;

        TocTempCostBillItemMonthlyDTO dto = new TocTempCostBillItemMonthlyDTO();
        dto.setMonth(month);
        dto.setMerchantId(merchantId.longValue());
        if(buId != null) {
            dto.setBuId(buId.longValue());
        }
        if(customerId != null) {
            dto.setCustomerId(customerId.longValue());
        }
        dto.setShopCode(shopCode);
        dto.setShopTypeCode(shopTypeCode);
        dto.setStorePlatformCode(storePlatformCode);

        //删除月结
        tocTemporaryReceiveBillCostItemMonthlyDao.deleteByDTO(dto);

        //1.取表2C暂估应收明细表 t_toc_temporary_receive_bill_item 中归属月份小于等于快照当月且未核销的明细
        TocTemporaryReceiveBillCostItemDTO itemDTO = new TocTemporaryReceiveBillCostItemDTO();
        if(buId != null) {
            itemDTO.setBuId(buId.longValue());
        }
        itemDTO.setMerchantId(merchantId.longValue());
        itemDTO.setStorePlatformCode(storePlatformCode);
        itemDTO.setShopTypeCode(shopTypeCode);
        if(customerId != null) {
            itemDTO.setCustomerId(customerId.longValue());
        }
        itemDTO.setShopCode(shopCode);
        itemDTO.setMonthEnd(month);
        itemDTO.setMonthBillLastDate(TimeUtils.getLastDayOfMonth(month));
        itemDTO.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0); //未核销
        itemDTO.setSearchTempOrderType(true); //暂估
        itemDTO.setSmallEqBillInDate(true); //无入账月份、或入账月份小于等于月结月份
        int unVerifyCountNum = tocTemporaryReceiveBillCostItemDao.countTempBillNum(itemDTO);
        List<TocTemporaryReceiveBillCostItemModel> itemListPage = new ArrayList<>();
        List<TocTemporaryReceiveBillCostItemMonthlyModel> collect = new ArrayList<>();
        Integer pageSize = 1000;
        for (int i = 0; i < unVerifyCountNum; ) {
            int pageSub = (i + pageSize) < unVerifyCountNum ? (i + pageSize) : unVerifyCountNum;

            itemDTO.setBegin(i);
            itemDTO.setPageSize(pageSize);
            i = pageSub;

            itemListPage = tocTemporaryReceiveBillCostItemDao.getItemListPage(itemDTO);

            collect = itemListPage.stream().map(entity -> {
                TocTemporaryReceiveBillCostItemMonthlyModel monthlyModel = new TocTemporaryReceiveBillCostItemMonthlyModel();
                BeanUtils.copyProperties(entity, monthlyModel);
                String tempMonth = entity.getMonth();
                monthlyModel.setMonth(month);
                monthlyModel.setTempMonth(tempMonth);
                monthlyModel.setId(null);
                BigDecimal tempRmbAmount = monthlyModel.getTemporaryRmbCost() == null ? new BigDecimal("0") : monthlyModel.getTemporaryRmbCost();
                BigDecimal writeOffAmount = monthlyModel.getWriteOffAmount() == null ? new BigDecimal("0") : monthlyModel.getWriteOffAmount();
                BigDecimal settlementAmount = monthlyModel.getSettlementRmbCost() == null ? new BigDecimal("0") : monthlyModel.getSettlementRmbCost();
                BigDecimal adjustmentAmount = monthlyModel.getAdjustmentRmbAmount() == null ? new BigDecimal("0") : monthlyModel.getAdjustmentRmbAmount();
                monthlyModel.setLastReceiveAmount(tempRmbAmount.subtract(writeOffAmount).subtract(settlementAmount).add(adjustmentAmount));
                return monthlyModel;
            }).collect(Collectors.toList());
            tocTemporaryReceiveBillCostItemMonthlyDao.batchSaveByBillItemList(collect);
            itemListPage.clear();
            collect.clear();
        }

        itemListPage.clear();
        collect.clear();

        //2.查找已核销但入账月份大于月结月份最后一天的数据，剔除掉结算信息进行保存
        //得出当月月结未核金额=第(2)点中结算状态为 “未核销” 的未核金额 + 第(3)点中剔除掉被入账月份大于快照当月的结算账单核销的暂估明细金额
        itemDTO.setSettlementMark(null); //所有状态
        itemDTO.setCompareBillInDate(true); //入账月份大于月结月份
//        itemDTO.setSearchVerifyStatus(true);
        itemDTO.setSearchTempOrderType(true);
        itemDTO.setSmallEqBillInDate(false);
        int verifyCountNum = tocTemporaryReceiveBillCostItemDao.countTempBillNum(itemDTO);

        for (int i = 0; i < verifyCountNum; ) {
            int pageSub = (i + pageSize) < verifyCountNum ? (i + pageSize) : verifyCountNum;

            itemDTO.setBegin(i);
            itemDTO.setPageSize(pageSize);
            i = pageSub;

            itemListPage = tocTemporaryReceiveBillCostItemDao.getItemListPage(itemDTO);

            // 清空数据中的结算信息
            collect = itemListPage.stream().map(entity -> {
                TocTemporaryReceiveBillCostItemMonthlyModel monthlyModel = new TocTemporaryReceiveBillCostItemMonthlyModel();
                BeanUtils.copyProperties(entity, monthlyModel);
                String tempMonth = entity.getMonth();
                monthlyModel.setMonth(month);
                monthlyModel.setTempMonth(tempMonth);
                monthlyModel.setId(null);
                monthlyModel.setSettlementCode(null);
                monthlyModel.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
                monthlyModel.setSettlementDate(null);
                monthlyModel.setSettlementOriCost(null);
                monthlyModel.setSettlementRmbCost(null);
                monthlyModel.setAdjustmentRmbAmount(null);
                monthlyModel.setSettlementItemId(null);
                BigDecimal tempRmbAmount = monthlyModel.getTemporaryRmbCost() == null ? new BigDecimal("0") : monthlyModel.getTemporaryRmbCost();
                BigDecimal writeOffAmount = monthlyModel.getWriteOffAmount() == null ? new BigDecimal("0") : monthlyModel.getWriteOffAmount();
                BigDecimal settlementAmount = monthlyModel.getSettlementRmbCost() == null ? new BigDecimal("0") : monthlyModel.getSettlementRmbCost();
                BigDecimal adjustmentAmount = monthlyModel.getAdjustmentRmbAmount() == null ? new BigDecimal("0") : monthlyModel.getAdjustmentRmbAmount();
                monthlyModel.setLastReceiveAmount(tempRmbAmount.subtract(writeOffAmount).subtract(settlementAmount).add(adjustmentAmount));
                return monthlyModel;
            }).collect(Collectors.toList());

            tocTemporaryReceiveBillCostItemMonthlyDao.batchSaveByBillItemList(collect);
            itemListPage.clear();
            collect.clear();
        }
        itemListPage = null;
        collect = null;
    }

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_20100000005, model = DERP_LOG_POINT.POINT_20100000005_Label)
    public void SaveToCTempMonthly(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String orderType = jsonData.containsKey("orderType") ? jsonData.getString("orderType") : null;
        Integer buId = jsonData.containsKey("buId") ? (Integer) jsonData.get("buId") : null;
        String month = jsonData.containsKey("month") ? jsonData.getString("month") : null;
        //公司
        Integer merchantId = (Integer) jsonData.get("merchantId");

        List<String> months = new ArrayList<>();
        if (StringUtils.isBlank(month)) {
            //当前月
            month = TimeUtils.formatMonth(new Date());
            jsonData.put("month", month);

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

            if (buId != null && buId > 0) {
                closeAccountsModel.setBuId(Long.valueOf(buId));
            }

            List<ReceiveCloseAccountsModel> closeAccountsModels = receiveCloseAccountsDao.list(closeAccountsModel);

            for (ReceiveCloseAccountsModel receiveCloseAccountsModel : closeAccountsModels) {
                Long closeMerId = receiveCloseAccountsModel.getMerchantId();
                Long closeBuId = receiveCloseAccountsModel.getBuId();

                jsonData.put("merchantId", closeMerId);
                jsonData.put("buId", closeBuId);
                jsonData.put("month", mon);
                //根据商家、事业部、月份的维度查询toc应收账单并生成月结快照
                if (StringUtils.isBlank(orderType) || ORDERTYPE_RECEIVE.equals(orderType)) {
                    //生成toc暂估收入月结
                    saveToCTempReceiveBillMonthly(jsonData.toString(), keys, topics, tags);
                }

                if(StringUtils.isBlank(orderType) || ORDERTYPE_COST.equals(orderType)){
                    //生成toc暂估费用月结
                    saveToCTempCostBillMonthly(jsonData.toString(), keys, topics, tags);
                }
            }
        }
    }
}
