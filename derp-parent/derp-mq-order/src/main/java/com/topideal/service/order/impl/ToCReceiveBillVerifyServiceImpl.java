package com.topideal.service.order.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.service.order.ToCReceiveBillVerifyService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: toc结算单核销toc暂估应收单
 * @Author: Chen Yiluan
 * @Date: 2021/01/07 15:02
 **/
@Service
public class ToCReceiveBillVerifyServiceImpl implements ToCReceiveBillVerifyService {

    @Autowired
    private TocTemporaryReceiveBillDao tocTemporaryReceiveBillDao;
    @Autowired
    private TocTemporaryReceiveBillItemDao tocTemporaryReceiveBillItemDao;
    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
    @Autowired
    private TocSettlementReceiveBillItemDao tocSettlementReceiveBillItemDao;
    @Autowired
    private TocSettlementReceiveBillCostItemDao tocSettlementReceiveBillCostItemDao;
    @Autowired
    private PlatformStatementOrderDao platformStatementOrderDao;
    @Autowired
    private TocTemporaryReceiveBillCostItemDao tocTemporaryReceiveBillCostItemDao;
    @Autowired
    private TocTemporaryCostBillDao tocTemporaryCostBillDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201160003,model=DERP_LOG_POINT.POINT_13201160003_Label, keyword = "billCode")
    public void updateVerifyToCTempBill(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        if(!jsonData.containsKey("billCode")) {
            throw new RuntimeException("核销的To C结算单号为空！");
        }

        String billCode = jsonData.getString("billCode");
        TocSettlementReceiveBillModel billModel = new TocSettlementReceiveBillModel();
        billModel.setCode(billCode);
        TocSettlementReceiveBillModel settlementReceiveBillModel = tocSettlementReceiveBillDao.searchByModel(billModel);

        if (!(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_02.equals(settlementReceiveBillModel.getBillStatus()) ||
                DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_06.equals(settlementReceiveBillModel.getBillStatus()))) {
            throw new RuntimeException("核销的To C结算单的状态不为“待核销”或“已作废”");
        }

        Set<Long> updateTempIds = new HashSet<>(); //修改过的toc 暂估收入表头id
        Set<Long> updateCostIds = new HashSet<>(); //修改过的toc 暂估费用表头id
        //查询应收明细数量
        int billItemNum = tocSettlementReceiveBillItemDao.countByBillId(settlementReceiveBillModel.getId());


        //0-非天猫平台 1-天猫平台
        String type = "1";
//        if ("1000000310".equals(settlementReceiveBillModel.getStorePlatformCode())) {
//            type = "1";
//        }

        int billCostItemNum = tocSettlementReceiveBillCostItemDao.countByBillId(settlementReceiveBillModel.getId(), type);


        int pageSize = 2000;
        List<Long> billIds = new ArrayList<>();
        billIds.add(settlementReceiveBillModel.getId());
        if (DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_02.equals(settlementReceiveBillModel.getBillStatus())) { //待核销
            //分页查询应收明细核销toc暂估
            for (int i = 0; i < billItemNum; ) {
//                List<String> orderCodeList = tocSettlementReceiveBillItemDao.getOrderCodeList(settlementReceiveBillModel.getId(), i, pageSize);
                List<String> externalCodeList = tocSettlementReceiveBillItemDao.getExternalCodeList(settlementReceiveBillModel.getId(), i, pageSize);
                List<Map<String, Object>> statisticsItemMapList = tocSettlementReceiveBillItemDao.statisticsByOrderCodesAndBillIds(billIds, externalCodeList);

                verifyAddAmount(externalCodeList, statisticsItemMapList, updateTempIds, settlementReceiveBillModel);
                int pageSub = (i+pageSize) < billItemNum ? (i+pageSize) : billItemNum;
                i = pageSub;
            }

            //分页查询费用明细核销toc暂估费用明细
            /**（1）对于非天猫平台，根据系统电商订单号查询获取2C应收结算单费用明细，以“订单号+费项” 汇总结算金额进行核销；
             * （2）对于天猫平台，以外部订单号查询查询获取2C应收结算单费用明细，以“订单号+费项” 汇总结算金额进行核销；
             */
            for (int i = 0; i < billCostItemNum; ) {

                List<String> externalCodeList = tocSettlementReceiveBillCostItemDao.getOrderCodeList(settlementReceiveBillModel.getId(), "1", i, pageSize);

                int pageSub = (i+pageSize) < billCostItemNum ? (i+pageSize) : billCostItemNum;
                i = pageSub;

                List<String> distinctOrderCodes = externalCodeList.stream().distinct().collect(Collectors.toList());
                if (distinctOrderCodes.isEmpty()) {
                    continue;
                }
                List<Map<String, Object>> statisticsCostItemMapList = tocSettlementReceiveBillCostItemDao.statisticsByOrderCodesAndBillIds(billIds, type, externalCodeList);

                Map<String, Object> params = new HashMap<>(5);
                List<TocTemporaryReceiveBillCostItemModel> itemListByOrderList = tocTemporaryReceiveBillCostItemDao.getItemListByOrderList(distinctOrderCodes, "1", settlementReceiveBillModel.getMerchantId(), settlementReceiveBillModel.getBuId(), settlementReceiveBillModel.getStorePlatformCode());
                tocTemporaryReceiveBillCostItemDao.updateVerifyCostItems(distinctOrderCodes, "1", settlementReceiveBillModel.getMerchantId(),settlementReceiveBillModel.getBuId(), settlementReceiveBillModel.getStorePlatformCode(), settlementReceiveBillModel.getId());
                // 更新已用于核销的结算单明细
                params.put("billId", null);
                params.put("merchantId", settlementReceiveBillModel.getMerchantId());
                params.put("buId", settlementReceiveBillModel.getBuId());
                params.put("storePlatformCode", settlementReceiveBillModel.getStorePlatformCode());
                params.put("orderCodeList", distinctOrderCodes);
                tocSettlementReceiveBillCostItemDao.batchUpdateTempCostBillId(params);

                // 判断是否存在差异调整单, 有则更新暂估
                tocTemporaryReceiveBillCostItemDao.batchUpdateVerifyCostItemsByDiffItem2(params);

                // 查询现在未核销的数据，生成差异调整单
                params.put("settlementMark", 0);
                List<TocSettlementReceiveBillCostItemDTO> extistTempList = tocSettlementReceiveBillCostItemDao.getExtistTempByMap(params);
                Map<String, List<TocSettlementReceiveBillCostItemDTO>> orderCodeAndTypeMap = extistTempList.stream().collect(
                        Collectors.groupingBy(v->v.getExternalCode() + "_" + v.getBillType()));

                List<TocTemporaryReceiveBillCostItemDTO> withoutSettlementItemList =tocTemporaryReceiveBillCostItemDao.getUnVerifyCostWithoutSettlementItem(params);
                // 更新有暂估没有结算单清空, 更新暂估中的结算单信息
                if(withoutSettlementItemList != null && withoutSettlementItemList.size() > 0) {
                    List<TocTemporaryReceiveBillCostItemDTO> collect = withoutSettlementItemList.stream().filter(entity -> {
                        String key = entity.getExternalCode() + "_" + entity.getOrderType();
                        return orderCodeAndTypeMap.containsKey(key);
                    }).map(entity -> {
                        String key = entity.getExternalCode() + "_" + entity.getOrderType();
                        List<TocSettlementReceiveBillCostItemDTO> tocSettlementReceiveBillCostItemDTOS = orderCodeAndTypeMap.get(key);
                        if (tocSettlementReceiveBillCostItemDTOS != null && tocSettlementReceiveBillCostItemDTOS.size() > 0) {
                            entity.setSettlementCode(tocSettlementReceiveBillCostItemDTOS.get(0).getSettlementCode());
                        }
                        return entity;
                    }).collect(Collectors.toList());
                    if(collect != null && collect.size() > 0){
                        tocTemporaryReceiveBillCostItemDao.updateWithoutSettleItems(collect);
                    }
                }


                List<TocTemporaryReceiveBillCostItemDTO> unVerifyItemList = tocTemporaryReceiveBillCostItemDao.getUnVerifyCostItem(params);

                if(unVerifyItemList == null || unVerifyItemList.isEmpty()) {
                    continue;
                }

                List<TocTemporaryReceiveBillCostItemModel> modelList = new ArrayList<>();
                unVerifyItemList.stream().forEach(entity -> {
                    TocTemporaryReceiveBillCostItemModel costItemModel = new TocTemporaryReceiveBillCostItemModel();
//                    costItemModel.setBillId(entity.getBillId());
                    costItemModel.setMonth(entity.getMonth());
                    costItemModel.setExternalCode(entity.getExternalCode());
                    costItemModel.setOrderCode(entity.getOrderCode());
                    costItemModel.setMerchantId(entity.getMerchantId());
                    costItemModel.setMerchantName(entity.getMerchantName());
                    costItemModel.setBuId(entity.getBuId());
                    costItemModel.setBuName(entity.getBuName());
                    costItemModel.setCustomerId(entity.getCustomerId());
                    costItemModel.setCustomerName(entity.getCustomerName());
                    costItemModel.setShopCode(entity.getShopCode());
                    costItemModel.setShopName(entity.getShopName());
                    costItemModel.setShopTypeCode(entity.getShopTypeCode());
                    costItemModel.setStorePlatformCode(entity.getStorePlatformCode());
                    costItemModel.setSettlementMark(entity.getSettlementMark());
                    costItemModel.setTemporaryCurrency(entity.getTemporaryCurrency());
                    costItemModel.setPlatformProjectId(entity.getPlatformProjectId());
                    costItemModel.setPlatformProjectName(entity.getPlatformProjectName());
                    costItemModel.setParentBrandId(entity.getParentBrandId());
                    costItemModel.setParentBrandCode(entity.getParentBrandCode());
                    costItemModel.setParentBrandName(entity.getParentBrandName());
                    costItemModel.setOrderType("0".equals(entity.getOrderType()) ? DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_2 : DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_3);

                    costItemModel.setProjectId(entity.getProjectId());
                    costItemModel.setProjectName(entity.getProjectName());
                    costItemModel.setTemporaryRmbCost(entity.getTemporaryRmbCost());

                    costItemModel.setSettlementCode(entity.getSettlementCode());
                    costItemModel.setSettlementDate(entity.getSettlementDate());
                    costItemModel.setSettlementItemId(entity.getSettlementItemId());
                    costItemModel.setSettlementMark(entity.getSettlementMark());
                    costItemModel.setSettlementOriCost(entity.getSettlementOriCost());
                    costItemModel.setSettlementRmbCost(entity.getSettlementRmbCost());
                    BigDecimal adjustmentRmbAmout = entity.getTemporaryRmbCost().subtract(entity.getSettlementRmbCost() == null ? BigDecimal.ZERO : entity.getSettlementRmbCost())
                            .subtract(entity.getWriteOffAmount() == null ? BigDecimal.ZERO : entity.getWriteOffAmount())
                            .add(entity.getAdjustmentRmbAmount() == null ? BigDecimal.ZERO : entity.getAdjustmentRmbAmount())
                            .negate();
                    costItemModel.setAdjustmentRmbAmount(adjustmentRmbAmout);
                    modelList.add(costItemModel);
                });
                tocTemporaryReceiveBillCostItemDao.batchSave(modelList);
                // 用差异调整单更新暂估
                tocTemporaryReceiveBillCostItemDao.batchUpdateVerifyCostItemsByDiffItem2(params);
//                verifyAddCostAmount(orderCodeList, type, statisticsItemMapList, updateCostIds, settlementReceiveBillModel);

                // 获取要更新表头的BillId List
                Map<String, Map<String, Object>> statisticsItemMap = new HashMap<>();
                for (Map<String, Object> map : statisticsCostItemMapList) {
                    String orderCode = (String) map.get("externalCode");
                    String billType = (String) map.get("billType");
                    String key = orderCode + "_" + billType;
                    statisticsItemMap.put(key, map);
                }

                for (TocTemporaryReceiveBillCostItemModel tocTemporaryItemModel : itemListByOrderList) {
                    String key = tocTemporaryItemModel.getExternalCode() + "_" + tocTemporaryItemModel.getOrderType();
                    Map<String, Object> itemMap = statisticsItemMap.get(key);
                    if (itemMap != null) {
                        updateCostIds.add(tocTemporaryItemModel.getBillId());
                    }
                }
            }
        } else if (DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_06.equals(settlementReceiveBillModel.getBillStatus())) { //已作废
            //分页查询费用明细核销toc暂估费用明细
            for (int i = 0; i < billCostItemNum; ) {
                List<String> externalCodeList = tocSettlementReceiveBillCostItemDao.getOrderCodeList(settlementReceiveBillModel.getId(), "1", i, pageSize);
                List<Map<String, Object>> statisticsCostItemMapList = tocSettlementReceiveBillCostItemDao.statisticsByOrderCodesAndBillIds(billIds, type, externalCodeList);
                verifyDeleteCostAmount(externalCodeList, type, statisticsCostItemMapList, updateCostIds, settlementReceiveBillModel);
                int pageSub = (i+pageSize) < billCostItemNum ? (i+pageSize) : billCostItemNum;
                i = pageSub;
            }
            //分页查询应收明细核销toc暂估
            for (int i = 0; i < billItemNum; ) {
                List<String> externalCodeList = tocSettlementReceiveBillItemDao.getExternalCodeList(settlementReceiveBillModel.getId(), i, pageSize);
                List<Map<String, Object>> statisticsItemMapList = tocSettlementReceiveBillItemDao.statisticsByOrderCodesAndBillIds(billIds, externalCodeList);
                verifyDeleteAmount(externalCodeList, statisticsItemMapList, updateTempIds, settlementReceiveBillModel);
                int pageSub = (i+pageSize) < billItemNum ? (i+pageSize) : billItemNum;
                i = pageSub;
            }

            //作废成功后更新该toc应收账单对应的平台结算单的标识为未生成应收单，并清空应收单号
            PlatformStatementOrderModel platformStatementOrderModel = new PlatformStatementOrderModel();
            platformStatementOrderModel.setReceiveCode(settlementReceiveBillModel.getCode());
            platformStatementOrderModel.setIsCreateReceive(DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_1);
            List<PlatformStatementOrderModel> platformStatementOrderModels = platformStatementOrderDao.list(platformStatementOrderModel);
            List<Long> platformIds = new ArrayList<>();
            for (PlatformStatementOrderModel statementOrderModel : platformStatementOrderModels) {
                platformIds.add(statementOrderModel.getId());
            }
            if (!platformIds.isEmpty()) {
                platformStatementOrderDao.batchUpdate(platformIds, "", DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_0);
            }
        }

        //指定id集合查询对应的订单数量和总金额
        if (!updateTempIds.isEmpty()) {
            List<Long> tocTempIds = new ArrayList<>(updateTempIds);
            List<Map<String, Object>> settleMapList = tocTemporaryReceiveBillItemDao.countByBillIds(tocTempIds);
            for (Map<String, Object> settleMap : settleMapList) {
                Long billId = (Long) settleMap.get("billId");
                BigDecimal temporaryRmbAmount = (BigDecimal) settleMap.get("temporaryRmbAmount");
                BigDecimal alreadyReceiveAmount = (BigDecimal) settleMap.get("alreadyReceiveAmount");
                Long totalNum = (Long) settleMap.get("totalNum");
                BigDecimal noSettlementAmount = (BigDecimal) settleMap.get("noSettlementAmount");
                Long noSettlementNum = (Long) settleMap.get("noSettlementNum");
                Timestamp settlementDate = (Timestamp) settleMap.get("settlementDate");

                TocTemporaryReceiveBillModel temporaryReceiveBillModel = new TocTemporaryReceiveBillModel();
                temporaryReceiveBillModel.setId(billId);
                temporaryReceiveBillModel.setTotalReceiveAmount(temporaryRmbAmount);
                temporaryReceiveBillModel.setTotalReceiveNum(totalNum);
                temporaryReceiveBillModel.setLastReceiveAmount(noSettlementAmount);
                temporaryReceiveBillModel.setAlreadyReceiveAmount(alreadyReceiveAmount);
                temporaryReceiveBillModel.setAlreadyReceiveNum(totalNum - noSettlementNum);

                if (noSettlementNum.equals(0L)) {
                    temporaryReceiveBillModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_2);
                    temporaryReceiveBillModel.setSettlementEndMonth(TimeUtils.formatMonth(settlementDate));
                } else if (totalNum.equals(noSettlementNum)) {
                    temporaryReceiveBillModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
                    temporaryReceiveBillModel.setSettlementEndMonth(null);
                } else {
                    temporaryReceiveBillModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_1);
                    temporaryReceiveBillModel.setSettlementEndMonth(null);
                }
                tocTemporaryReceiveBillDao.modify(temporaryReceiveBillModel);
            }
        }

        //指定id集合查询对应的订单数量和总金额
        if (!updateCostIds.isEmpty()) {
            List<Long> tocTempIds = new ArrayList<>(updateCostIds);
            List<Map<String, Object>> settleMapList = tocTemporaryReceiveBillCostItemDao.countByBillIds(tocTempIds);
            for (Map<String, Object> settleMap : settleMapList) {
                Long billId = (Long) settleMap.get("billId");
                BigDecimal temporaryRmbAmount = (BigDecimal) settleMap.get("temporaryRmbAmount");
                BigDecimal adjustmentRmbAmount = (BigDecimal) settleMap.get("adjustmentRmbAmount");
                Long totalNum = (Long) settleMap.get("totalNum");
                BigDecimal noSettlementAmount = (BigDecimal) settleMap.get("noSettlementAmount");
                BigDecimal alreadyReceiveAmount = (BigDecimal) settleMap.get("alreadyReceiveAmount");
                Long noSettlementNum = (Long) settleMap.get("noSettlementNum");
                Timestamp settlementDate = (Timestamp) settleMap.get("settlementDate");

                TocTemporaryCostBillModel tocTemporaryCostBillModel = new TocTemporaryCostBillModel();
                tocTemporaryCostBillModel.setId(billId);
                tocTemporaryCostBillModel.setTotalReceiveAmount(temporaryRmbAmount);
                tocTemporaryCostBillModel.setAlreadyReceiveAmount(alreadyReceiveAmount);

                if (noSettlementNum.equals(0L)) {
                    tocTemporaryCostBillModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_2);
                    tocTemporaryCostBillModel.setSettlementEndMonth(TimeUtils.formatMonth(settlementDate));
                } else if (totalNum.equals(noSettlementNum)) {
                    tocTemporaryCostBillModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
                    tocTemporaryCostBillModel.setSettlementEndMonth(null);
                } else {
                    tocTemporaryCostBillModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_1);
                    tocTemporaryCostBillModel.setSettlementEndMonth(null);
                }
                tocTemporaryCostBillModel.setAdjustmentRmbAmount(adjustmentRmbAmount);
                tocTemporaryCostBillDao.modify(tocTemporaryCostBillModel);
            }
        }
    }

    /**
     * @Description toc结算单审核通过：以“订单+商品货号”查询To C暂估应收跟踪表，
     * 找到对应被当前作废的平台结算单核销的电商订单进行回填入To C暂估应收表里
     * @param externalCodeList 待核销的订单集合
     * @param settlementReceiveBillModel 待审核的toc账单
     * @param statisticsItemMapList 待审核的toc账单明细
     * @param updateTemporaryIdList 待更新的toc暂估表头id集合
     */
    private void verifyAddAmount(List<String> externalCodeList, List<Map<String, Object>> statisticsItemMapList,Set<Long> updateTemporaryIdList,
                                 TocSettlementReceiveBillModel settlementReceiveBillModel) throws SQLException {
        tocTemporaryReceiveBillItemDao.updateVerifyItems(externalCodeList, settlementReceiveBillModel.getMerchantId(), settlementReceiveBillModel.getBuId(), settlementReceiveBillModel.getStorePlatformCode(), settlementReceiveBillModel.getId());

        // 更新已用于核销的结算单明细
        Map<String, Object> params = new HashMap<>();
        params.put("billId", null);
        params.put("orderCodeList", externalCodeList);
        params.put("buId", settlementReceiveBillModel.getBuId());
        params.put("merchantId", settlementReceiveBillModel.getMerchantId());
        params.put("storePlatformCode", settlementReceiveBillModel.getStorePlatformCode());
        tocSettlementReceiveBillItemDao.batchUpdateTempBillId(params);

        List<TocTemporaryReceiveBillItemModel> tempItemList = tocTemporaryReceiveBillItemDao.getItemListByOrderList(null,externalCodeList, null, settlementReceiveBillModel.getMerchantId(),
                settlementReceiveBillModel.getBuId(), settlementReceiveBillModel.getStorePlatformCode());

//        List<TocTemporaryReceiveBillItemModel> updateItemList = new ArrayList<>();

        Map<String, Map<String, Object>> statisticsItemMap = new HashMap<>();
        for (Map<String, Object> map : statisticsItemMapList) {
            String orderCode = (String) map.get("externalCode");
            String billType = (String) map.get("billType");
            String key = orderCode + "_" + billType;
            statisticsItemMap.put(key, map);
        }

        for (TocTemporaryReceiveBillItemModel tocTemporaryItemModel : tempItemList) {
            String key = tocTemporaryItemModel.getExternalCode() + "_" + tocTemporaryItemModel.getOrderType();
            Map<String, Object> itemMap = statisticsItemMap.get(key);
            if (itemMap != null) {
                updateTemporaryIdList.add(tocTemporaryItemModel.getBillId());
            }
        }

//        for (TocTemporaryReceiveBillItemModel tocTemporaryItemModel : tempItemList) {
//            String key = tocTemporaryItemModel.getExternalCode() + "_" + tocTemporaryItemModel.getOrderType();
//            Map<String, Object> itemMap = statisticsItemMap.get(key);
//
//            //平台结算货款（原币）:取该电商订单+商品货号+补扣款类型 对应平台结算单中“应收明细”中的结算金额（原币）
//            //平台结算金额（RMB）:电商订单+商品货号+补扣款类型 对应平台结算单中“应收明细”中的结算金额（RMB）；
//            if (itemMap != null) {
//                BigDecimal originalAmount = itemMap.get("originalAmount") == null ? BigDecimal.ZERO : (BigDecimal) itemMap.get("originalAmount");
//                BigDecimal rmbAmount = itemMap.get("rmbAmount") == null ? BigDecimal.ZERO : (BigDecimal) itemMap.get("rmbAmount");
//
//                String newSettlementCode = null;
//                //待核销 +
//                if (StringUtils.isBlank(tocTemporaryItemModel.getSettlementCode())) { //如果暂估单不包含结算单号，说明还未结算
//                    newSettlementCode = settlementReceiveBillModel.getCode();
//                } else {
//                    List<String> settlementCodes = Arrays.asList(tocTemporaryItemModel.getSettlementCode().split(","));
//                    List<String> newSettlementCodes = new ArrayList<>(settlementCodes);
//                    if (!newSettlementCodes.contains(settlementReceiveBillModel.getCode())) {
//                        newSettlementCodes.add(settlementReceiveBillModel.getCode());
//                    }
//                    newSettlementCode = newSettlementCodes.stream().map(r -> r.toString()).collect(Collectors.joining(","));
//                }
//                BigDecimal settlementOriAmount = tocTemporaryItemModel.getSettlementOriAmount() == null ? BigDecimal.ZERO : tocTemporaryItemModel.getSettlementOriAmount(); //已结算的平台结算费用（原币）
//                BigDecimal settlementRmbAmount = tocTemporaryItemModel.getSettlementRmbAmount() == null ? BigDecimal.ZERO : tocTemporaryItemModel.getSettlementRmbAmount(); //已结算的平台结算金额（RMB）
//                settlementOriAmount = settlementOriAmount.add(originalAmount);
//                settlementRmbAmount = settlementRmbAmount.add(rmbAmount);
//
//                TocTemporaryReceiveBillItemModel temp = new TocTemporaryReceiveBillItemModel();
//                temp.setId(tocTemporaryItemModel.getId());
//                temp.setSettlementOriAmount(settlementOriAmount);
//                temp.setSettlementRmbAmount(settlementRmbAmount);
//                temp.setSettlementCode(newSettlementCode);
//                temp.setSettlementMark(tocTemporaryItemModel.getSettlementMark());
//                temp.setSettlementDate(tocTemporaryItemModel.getSettlementDate());
//                temp.setOriginalCurrency(settlementReceiveBillModel.getSettlementCurrency());
//                temp.setModifyDate(TimeUtils.getNow());
//                temp.setBillInDate(settlementReceiveBillModel.getBillInDate());
//                if (!DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_1.equals(tocTemporaryItemModel.getSettlementMark())) {
//                    BigDecimal nonVerifyAmount = tocTemporaryItemModel.getTemporaryRmbAmount().subtract(settlementRmbAmount);
//                    if (tocTemporaryItemModel.getWriteOffAmount() != null) {
//                        nonVerifyAmount = nonVerifyAmount.subtract(tocTemporaryItemModel.getWriteOffAmount());
//                    }
//
//                    if (nonVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
//                        temp.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_2);
//                    }
//                    temp.setSettlementDate(new Timestamp(settlementReceiveBillModel.getSettlementDate().getTime()));
//                }
//
//                updateTemporaryIdList.add(tocTemporaryItemModel.getBillId());
//                updateItemList.add(temp);
//            }
//        }
//        //批量更新暂估明细
//        if (!updateItemList.isEmpty()) {
//            int pageSize = 2000;
//            for (int j = 0; j < updateItemList.size(); ) {
//                int itemPageSub = (j + pageSize) < updateItemList.size() ? (j + pageSize) : updateItemList.size();
//                tocTemporaryReceiveBillItemDao.batchUpdate(updateItemList.subList(j, itemPageSub));
//                j = itemPageSub;
//            }
//        }
    }

    /**
     * @Description toc结算单审核通过：以“订单+平台费项+单据类型”查询To C暂估应收跟踪表，
     * 找到对应被当前作废的平台结算单核销的电商订单进行回填入To C暂估应收表里
     * @param orderCodeList 待核销的订单集合
     * @param type 0-非天猫平台 1-天猫平台
     * @param settlementReceiveBillModel 待审核的toc账单
     * @param statisticsItemMapList 待审核的toc账单明细
     * @param updateTemporaryIdList 待更新的toc暂估表头id集合
     */
    private void verifyAddCostAmount(List<String> orderCodeList, String type, List<Map<String, Object>> statisticsItemMapList,Set<Long> updateTemporaryIdList,
                                 TocSettlementReceiveBillModel settlementReceiveBillModel) throws SQLException {

        List<TocTemporaryReceiveBillCostItemModel> tempItemList = tocTemporaryReceiveBillCostItemDao.getItemListByOrderList(orderCodeList, type, settlementReceiveBillModel.getMerchantId(), settlementReceiveBillModel.getBuId(), settlementReceiveBillModel.getStorePlatformCode());

        List<TocTemporaryReceiveBillCostItemModel> updateItemList = new ArrayList<>();

        Map<String, Map<String, Object>> statisticsItemMap = new HashMap<>();

        for (Map<String, Object> map : statisticsItemMapList) {
            String orderCode = (String) map.get("orderCode");
            String billType = (String) map.get("billType");
            Long platformProjectId = (Long) map.get("platformProjectId");
            String key = orderCode + "_" + billType + "_" + platformProjectId;
            statisticsItemMap.put(key, map);
        }
        for (TocTemporaryReceiveBillCostItemModel tocTemporaryItemModel : tempItemList) {
//            String key = tocTemporaryItemModel.getOrderCode() + "_" + tocTemporaryItemModel.getGoodsId() + "_" + tocTemporaryItemModel.getPlatformProjectId();
            String code = tocTemporaryItemModel.getOrderCode();
            if ("1".equals(type)) {
                code = tocTemporaryItemModel.getExternalCode();
            }

            String key = code + "_" + tocTemporaryItemModel.getOrderType() + "_" + tocTemporaryItemModel.getPlatformProjectId();
            Map<String, Object> itemMap = statisticsItemMap.get(key);

            //平台结算费用（原币）:取该电商订单+商品货号 对应平台结算单中“费用明细”中的结算金额（原币）
            //平台结算费用（RMB）:电商订单+商品货号 对应平台结算单中“费用明细”中的结算金额（RMB）；
            if (itemMap != null) {
                BigDecimal originalAmount = itemMap.get("originalAmount") == null ? BigDecimal.ZERO : (BigDecimal) itemMap.get("originalAmount");
                BigDecimal rmbAmount = itemMap.get("rmbAmount") == null ? BigDecimal.ZERO : (BigDecimal) itemMap.get("rmbAmount");

                String newSettlementCode = null;
                //待核销 +
                if (StringUtils.isBlank(tocTemporaryItemModel.getSettlementCode())) { //如果暂估单不包含结算单号，说明还未结算
                    newSettlementCode = settlementReceiveBillModel.getCode();
                } else {
                    List<String> settlementCodes = Arrays.asList(tocTemporaryItemModel.getSettlementCode().split(","));
                    List<String> newSettlementCodes = new ArrayList<>(settlementCodes);
                    if (!newSettlementCodes.contains(settlementReceiveBillModel.getCode())) {
                        newSettlementCodes.add(settlementReceiveBillModel.getCode());
                    }
                    newSettlementCode = newSettlementCodes.stream().map(r -> r.toString()).collect(Collectors.joining(","));
                }
                BigDecimal settlementOriCost = tocTemporaryItemModel.getSettlementOriCost() == null ? BigDecimal.ZERO : tocTemporaryItemModel.getSettlementOriCost(); //平台结算费用（原币）
                BigDecimal settlementRmbCost = tocTemporaryItemModel.getSettlementRmbCost() == null ? BigDecimal.ZERO : tocTemporaryItemModel.getSettlementRmbCost(); //平台结算费用（RMB）
                settlementOriCost = settlementOriCost.add(originalAmount);
                settlementRmbCost = settlementRmbCost.add(rmbAmount);

                TocTemporaryReceiveBillCostItemModel temp = new TocTemporaryReceiveBillCostItemModel();
                temp.setId(tocTemporaryItemModel.getId());
                temp.setSettlementOriCost(settlementOriCost);
                temp.setSettlementRmbCost(settlementRmbCost);
                temp.setSettlementCode(newSettlementCode);
                temp.setSettlementMark(DERP_ORDER.TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_0);
                if (!DERP_ORDER.TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_1.equals(tocTemporaryItemModel.getSettlementMark())) {
                    BigDecimal nonVerifyAmount = tocTemporaryItemModel.getTemporaryRmbCost().subtract(settlementRmbCost);
                    if (tocTemporaryItemModel.getWriteOffAmount() != null) {
                        nonVerifyAmount = nonVerifyAmount.subtract(tocTemporaryItemModel.getWriteOffAmount());
                    }

                    if (nonVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
                        temp.setSettlementMark(DERP_ORDER.TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_2);
                    }
                    temp.setSettlementDate(new Timestamp(settlementReceiveBillModel.getSettlementDate().getTime()));
                }
                temp.setOriginalCurrency(settlementReceiveBillModel.getSettlementCurrency());
                updateTemporaryIdList.add(tocTemporaryItemModel.getBillId());
                updateItemList.add(temp);
            }
        }
        //批量更新暂估明细
        if (!updateItemList.isEmpty()) {
            int pageSize = 2000;
            for (int j = 0; j < updateItemList.size(); ) {
                int itemPageSub = (j + pageSize) < updateItemList.size() ? (j + pageSize) : updateItemList.size();
                tocTemporaryReceiveBillCostItemDao.batchUpdate(updateItemList.subList(j, itemPageSub));
                j = itemPageSub;
            }
        }
    }

    /**
     * @Description toc结算单作废审核通过：以“订单+商品货号”查询To C暂估应收跟踪表，
     * 找到对应被当前作废的平台结算单核销的电商订单进行删除结算信息
     * @param orderCodeList 待核销的订单集合
     * @param settlementReceiveBillModel 待审核的toc账单
     * @param statisticsItemMapList 待审核的toc账单明细
     * @param updateTemporaryIdList 待更新的toc暂估表头id集合
     */
    private void verifyDeleteAmount(List<String> orderCodeList, List<Map<String, Object>> statisticsItemMapList,Set<Long> updateTemporaryIdList,
                                 TocSettlementReceiveBillModel settlementReceiveBillModel) throws SQLException {
        List<TocTemporaryReceiveBillItemModel> tempItemList = tocTemporaryReceiveBillItemDao.getItemListByOrderList(null, orderCodeList, null, settlementReceiveBillModel.getMerchantId(), settlementReceiveBillModel.getBuId(), settlementReceiveBillModel.getStorePlatformCode());
        List<TocTemporaryReceiveBillItemModel> updateItemList = new ArrayList<>();
        Map<String, Map<String, Object>> statisticsItemMap = new HashMap<>();
        for (Map<String, Object> map : statisticsItemMapList) {
            String externalCode = (String) map.get("externalCode");
            String billType = (String) map.get("billType");
            String key = externalCode + "_" + billType;
            statisticsItemMap.put(key, map);
        }
        for (TocTemporaryReceiveBillItemModel tocTemporaryItemModel : tempItemList) {
            String key = tocTemporaryItemModel.getExternalCode() + "_" + tocTemporaryItemModel.getOrderType();
            Map<String, Object> itemMap = statisticsItemMap.get(key);

            //平台结算货款（原币）:取该电商订单+商品货号 对应平台结算单中“应收明细”中的结算金额（原币）
            //平台结算金额（RMB）:电商订单+商品货号 对应平台结算单中“应收明细”中的结算金额（RMB）；
            if (itemMap != null) {
                BigDecimal originalAmount = itemMap.get("originalAmount") == null ? BigDecimal.ZERO : (BigDecimal) itemMap.get("originalAmount");
                BigDecimal rmbAmount = itemMap.get("rmbAmount") == null ? BigDecimal.ZERO : (BigDecimal) itemMap.get("rmbAmount");

                List<String> newSettlementCodes = new ArrayList<>();
                String newSettlementCode = null;
                if(tocTemporaryItemModel.getSettlementCode() != null) {
                    List<String> settlementCodes = Arrays.asList(tocTemporaryItemModel.getSettlementCode().split(","));
                    newSettlementCodes = new ArrayList<>(settlementCodes);
                    if (!newSettlementCodes.contains(settlementReceiveBillModel.getCode())) {
                        continue;
                    }
                    newSettlementCodes.remove(settlementReceiveBillModel.getCode());
                    newSettlementCode = newSettlementCodes.stream().map(r -> r.toString()).collect(Collectors.joining(","));
                }

                BigDecimal settlementOriAmount = tocTemporaryItemModel.getSettlementOriAmount() == null ? BigDecimal.ZERO : tocTemporaryItemModel.getSettlementOriAmount(); //已结算的平台结算费用（原币）
                BigDecimal settlementRmbAmount = tocTemporaryItemModel.getSettlementRmbAmount() == null ? BigDecimal.ZERO : tocTemporaryItemModel.getSettlementRmbAmount(); //已结算的平台结算金额（RMB）
                settlementOriAmount = settlementOriAmount.subtract(originalAmount);
                settlementRmbAmount = settlementRmbAmount.subtract(rmbAmount);

                TocTemporaryReceiveBillItemModel temp = new TocTemporaryReceiveBillItemModel();
                temp.setId(tocTemporaryItemModel.getId());
                temp.setSettlementItemId(null);
                temp.setSettlementOriAmount(settlementOriAmount);
                temp.setSettlementRmbAmount(settlementRmbAmount);
                temp.setSettlementCode(newSettlementCode);
                temp.setSettlementDate(tocTemporaryItemModel.getSettlementDate());
                temp.setSettlementMark(tocTemporaryItemModel.getSettlementMark());
                if (!DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_1.equals(tocTemporaryItemModel.getSettlementMark())) {
                    if (newSettlementCodes.isEmpty()) {
                        temp.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
                        temp.setSettlementDate(null);
                        temp.setOriginalCurrency(null);
                    } else {
                        BigDecimal nonVerifyAmount = tocTemporaryItemModel.getTemporaryRmbAmount().subtract(settlementRmbAmount);
                        if (tocTemporaryItemModel.getWriteOffAmount() != null) {
                            nonVerifyAmount = nonVerifyAmount.subtract(tocTemporaryItemModel.getWriteOffAmount());
                        }

                        if (nonVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
                            temp.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_2);
                        } else {
                            temp.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
                        }
                        TocSettlementReceiveBillModel billModel = new TocSettlementReceiveBillModel();
                        billModel.setCode(newSettlementCodes.get(0));
                        billModel = tocSettlementReceiveBillDao.searchByModel(billModel);
                        if (billModel != null) {
                            temp.setSettlementDate(new Timestamp(billModel.getSettlementDate().getTime()));
                            temp.setOriginalCurrency(billModel.getSettlementCurrency());
                        }

                    }
                }

                updateItemList.add(temp);
                updateTemporaryIdList.add(tocTemporaryItemModel.getBillId());
            }
        }
        //批量更新暂估明细
        if (!updateItemList.isEmpty()) {
            int pageSize = 2000;
            for (int j = 0; j < updateItemList.size(); ) {
                int itemPageSub = (j + pageSize) < updateItemList.size() ? (j + pageSize) : updateItemList.size();
                tocTemporaryReceiveBillItemDao.batchUpdate(updateItemList.subList(j, itemPageSub));
                j = itemPageSub;
            }
        }
    }

    /**
     * @Description toc结算单作废审核通过：以“订单+商品货号”查询To C暂估应收跟踪表，
     * 找到对应被当前作废的平台结算单核销的电商订单进行删除结算信息
     * @param externalCodeList 待核销的订单集合
     * @param type 0-非天猫平台 1-天猫平台
     * @param settlementReceiveBillModel 待审核的toc账单
     * @param statisticsItemMapList 待审核的toc账单明细
     * @param updateTemporaryIdList 待更新的toc暂估表头id集合
     */
    private void verifyDeleteCostAmount(List<String> externalCodeList, String type, List<Map<String, Object>> statisticsItemMapList,Set<Long> updateTemporaryIdList,
                                    TocSettlementReceiveBillModel settlementReceiveBillModel) throws Exception {
        List<TocTemporaryReceiveBillCostItemModel> tempItemList = tocTemporaryReceiveBillCostItemDao.getItemListByOrderList(externalCodeList, type, settlementReceiveBillModel.getMerchantId(), settlementReceiveBillModel.getBuId(), settlementReceiveBillModel.getStorePlatformCode());
        List<TocTemporaryReceiveBillCostItemModel> updateItemList = new ArrayList<>();
        Map<String, Map<String, Object>> statisticsItemMap = new HashMap<>();
        for (Map<String, Object> map : statisticsItemMapList) {
            String externalCode = (String) map.get("externalCode");
            String billType = (String) map.get("billType");
            Long platformProjectId = (Long) map.get("platformProjectId");
            String key = externalCode + "_" + billType + "_" + platformProjectId;
            statisticsItemMap.put(key, map);
        }

        // 根据平台结算单去删除费用差异调整单
        Map<String, Object> param = new HashMap<>();
        param.put("settlementCode", settlementReceiveBillModel.getCode());
        param.put("type", "ADJUSTMENT");
        List<TocTemporaryReceiveBillCostItemDTO> adjustmentItemList = tocTemporaryReceiveBillCostItemDao.getItemListByMap(param);
        if(adjustmentItemList != null && adjustmentItemList.size() > 0) {
            Map<String, List<TocTemporaryReceiveBillCostItemDTO>> itemListMap = new HashMap<>();
            adjustmentItemList.stream().forEach(entity -> {
                // 代表与差异调整单一对一，则直接删除差异调整单
                if(settlementReceiveBillModel.getCode().equals(entity.getSettlementCode())) {
                    // 删除差异调整单
                    try {
                        tocTemporaryReceiveBillCostItemDao.delete(Arrays.asList(entity.getId()));
                    } catch (SQLException throwables) {
                        throw new RuntimeException(throwables);
                    }
                }else {
                    // 更新差异调整单
                    String newSettlementCode = Arrays.stream(entity.getSettlementCode().split(",")).filter(v -> {
                        return !v.equals(settlementReceiveBillModel.getCode());
                    }).collect(Collectors.joining(","));
                    entity.setSettlementCode(newSettlementCode);
                    entity.setAdjustmentRmbAmount((entity.getAdjustmentRmbAmount() == null ? BigDecimal.ZERO : entity.getAdjustmentRmbAmount())
                            .add(entity.getSettlementRmbCost() == null ? BigDecimal.ZERO : entity.getSettlementRmbCost()));
                    tocTemporaryReceiveBillCostItemDao.updateAdjustmentByDTO(entity);
                }
            });
        }

        for (TocTemporaryReceiveBillCostItemModel tocTemporaryItemModel : tempItemList) {
            String code = tocTemporaryItemModel.getOrderCode();
            if ("1".equals(type)) {
                code = tocTemporaryItemModel.getExternalCode();
            }

            String key = code + "_" + tocTemporaryItemModel.getOrderType() + "_" + tocTemporaryItemModel.getPlatformProjectId();
            Map<String, Object> itemMap = statisticsItemMap.get(key);

            //平台结算费用（原币）:取该电商订单+商品货号 对应平台结算单中“费用明细”中的结算金额（原币）
            //平台结算费用（RMB）:电商订单+商品货号 对应平台结算单中“费用明细”中的结算金额（RMB）；
            if (itemMap != null) {
                BigDecimal originalCost = itemMap.get("originalAmount") == null ? BigDecimal.ZERO : (BigDecimal) itemMap.get("originalAmount");
                BigDecimal rmbCost = itemMap.get("rmbAmount") == null ? BigDecimal.ZERO : (BigDecimal) itemMap.get("rmbAmount");

                String newSettlementCode = null;
                List<String> newSettlementCodes = new ArrayList<>();
                if(tocTemporaryItemModel.getSettlementCode() != null) {
                    List<String> settlementCodes = Arrays.asList(tocTemporaryItemModel.getSettlementCode().split(","));
                    newSettlementCodes = new ArrayList<>(settlementCodes);
                    if (!newSettlementCodes.contains(settlementReceiveBillModel.getCode())) {
                        continue;
                    }
                    newSettlementCodes.remove(settlementReceiveBillModel.getCode());
                    newSettlementCode = newSettlementCodes.stream().map(r -> r.toString()).collect(Collectors.joining(","));
                }

                BigDecimal settlementOriCost = tocTemporaryItemModel.getSettlementOriCost() == null ? BigDecimal.ZERO : tocTemporaryItemModel.getSettlementOriCost(); //平台结算费用（原币）
                BigDecimal settlementRmbCost = tocTemporaryItemModel.getSettlementRmbCost() == null ? BigDecimal.ZERO : tocTemporaryItemModel.getSettlementRmbCost(); //平台结算费用（RMB）
                settlementOriCost = settlementOriCost.subtract(originalCost);
                settlementRmbCost = settlementRmbCost.subtract(rmbCost);

                TocTemporaryReceiveBillCostItemModel temp = new TocTemporaryReceiveBillCostItemModel();
                temp.setSettlementItemId(null);
                temp.setId(tocTemporaryItemModel.getId());
                temp.setSettlementOriCost(settlementOriCost);
                temp.setSettlementRmbCost(settlementRmbCost);
                temp.setSettlementCode(newSettlementCode);
                temp.setSettlementMark(DERP_ORDER.TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_1);
                temp.setAdjustmentRmbAmount(BigDecimal.ZERO);
                if (!DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_1.equals(tocTemporaryItemModel.getSettlementMark())) {
                    if (newSettlementCodes.isEmpty()) {
                        temp.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
                        temp.setSettlementDate(null);
                        temp.setOriginalCurrency(null);
                    } else {
                        BigDecimal nonVerifyAmount = tocTemporaryItemModel.getTemporaryRmbCost().subtract(settlementRmbCost);
                        if (tocTemporaryItemModel.getWriteOffAmount() != null) {
                            nonVerifyAmount = nonVerifyAmount.subtract(tocTemporaryItemModel.getWriteOffAmount());
                        }

                        if (nonVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
                            temp.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_2);
                        } else {
                            temp.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
                        }
                        TocSettlementReceiveBillModel billModel = new TocSettlementReceiveBillModel();
                        billModel.setCode(newSettlementCodes.get(0));
                        billModel = tocSettlementReceiveBillDao.searchByModel(billModel);
                        if (billModel != null) {
                            temp.setSettlementDate(new Timestamp(billModel.getSettlementDate().getTime()));
                            temp.setOriginalCurrency(billModel.getSettlementCurrency());
                        }

                    }
                }
                updateItemList.add(temp);
                updateTemporaryIdList.add(tocTemporaryItemModel.getBillId());
            }
        }
        //批量更新暂估明细
        if (!updateItemList.isEmpty()) {
            int pageSize = 2000;
            for (int j = 0; j < updateItemList.size(); ) {
                int itemPageSub = (j + pageSize) < updateItemList.size() ? (j + pageSize) : updateItemList.size();
                tocTemporaryReceiveBillCostItemDao.batchUpdate(updateItemList.subList(j, itemPageSub));
                j = itemPageSub;
            }
        }

        //根据结算单更新ToC暂估中的差异调整金额
        param.clear();
        param.put("settlementCode", settlementReceiveBillModel.getCode());
        param.put("adjustmentRmbAmount", BigDecimal.ZERO);
        tocTemporaryReceiveBillCostItemDao.updateByMap(param);
    }

}
