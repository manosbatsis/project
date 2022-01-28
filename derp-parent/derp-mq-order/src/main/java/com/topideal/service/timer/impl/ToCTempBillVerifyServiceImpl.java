package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillItemDTO;
import com.topideal.entity.vo.bill.TocTemporaryCostBillModel;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillModel;
import com.topideal.service.timer.ToCTempBillVerifyService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: toc暂估应收单核销toc结算单 service
 * @Author: Chen Yiluan
 * @Date: 2021/01/07 10:21
 **/
@Service
public class ToCTempBillVerifyServiceImpl implements ToCTempBillVerifyService {

    public static final Logger LOGGER= LoggerFactory.getLogger(ToCTempBillVerifyServiceImpl.class);

    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
    @Autowired
    private TocTemporaryReceiveBillItemDao tocTemporaryReceiveBillItemDao;
    @Autowired
    private TocSettlementReceiveBillItemDao tocSettlementReceiveBillItemDao;
    @Autowired
    private TocSettlementReceiveBillCostItemDao tocSettlementReceiveBillCostItemDao;
    @Autowired
    private TocTemporaryReceiveBillDao tocTemporaryReceiveBillDao;
    @Autowired
    private TocTemporaryReceiveBillCostItemDao tocTemporaryReceiveBillCostItemDao;
    @Autowired
    private TocTemporaryCostBillDao tocTemporaryCostBillDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201160000,model=DERP_LOG_POINT.POINT_13201160000_Label)
    public void updateVerifyToCTempBill(String json, String keys, String topics, String tags) throws Exception {
        LOGGER.info("===================To C暂估收入核销toc结算单应收明细 开始=================="+json);
        JSONObject jsonData = JSONObject.fromObject(json);
        String month = null;
        if(jsonData.containsKey("month")) {
            month = jsonData.getString("month");
        } else {
            Date today = new Date();
            month = TimeUtils.getLastMonth(today);
        }

        Integer merchantId = (Integer) jsonData.get("merchantId");
        Integer jsonBuId = (Integer) jsonData.get("buId");
        Integer jsonCustomerId = (Integer) jsonData.get("customerId");
        String shopCode = jsonData.get("shopCode") == null ? "" : (String) jsonData.get("shopCode");
        String shopTypeCode = (String) jsonData.get("shopTypeCode");
        String storePlatformCode = (String) jsonData.get("storePlatformCode");


        Long merId = (merchantId != null) ? Long.valueOf(merchantId) : null;
        Long buId = (jsonBuId != null) ? Long.valueOf(jsonBuId) : null;
        Long customerId = (jsonCustomerId != null) ? Long.valueOf(jsonCustomerId) : null;

        //查询对应的toc暂估
        TocTemporaryReceiveBillModel tempBllModel = new TocTemporaryReceiveBillModel();
        tempBllModel.setMonth(month);
        tempBllModel.setMerchantId(merId);
        tempBllModel.setCustomerId(customerId);
        tempBllModel.setBuId(buId);
        tempBllModel.setStorePlatformCode(storePlatformCode);
        tempBllModel.setShopCode(shopCode);
        tempBllModel.setShopTypeCode(shopTypeCode);
        List<TocTemporaryReceiveBillModel> temporaryReceiveBillModels = tocTemporaryReceiveBillDao.list(tempBllModel);
        if (temporaryReceiveBillModels == null || temporaryReceiveBillModels.size() == 0) {
            return;
        }

        List<Long> tocIds = new ArrayList<>();
        //遍历toc暂估
        for (TocTemporaryReceiveBillModel temporaryReceiveBillModel : temporaryReceiveBillModels) {

            tocIds.add(temporaryReceiveBillModel.getId());

            Integer pageSize = 3000; //分页数量
            //统计暂估应收明细数量分页
            TocTemporaryReceiveBillItemDTO itemDTO = new TocTemporaryReceiveBillItemDTO();
            itemDTO.setBillId(temporaryReceiveBillModel.getId());
            int tempBillNum = tocTemporaryReceiveBillItemDao.countTempBillNum(itemDTO);
            Map<String, Object> params = new HashMap<>(5);
            for (int i = 0; i < tempBillNum; ) {
                int pageSub = (i+pageSize) < tempBillNum ? (i+pageSize) : tempBillNum;
                itemDTO.setBegin(i);
                itemDTO.setPageSize(pageSize);
                //待核销的电商订单单号集合
//                List<String> orderCodeList = tocTemporaryReceiveBillItemDao.getOrderCodesPage(itemDTO);
                //获取外部订单号集合
                List<String> externalCodeList = tocTemporaryReceiveBillItemDao.getExternalCodesPage(itemDTO);

//                List<String> distinctOrderCodes = orderCodeList.stream().distinct().collect(Collectors.toList());
                List<String> distinctOrderCodes = externalCodeList.stream().distinct().collect(Collectors.toList());
                i = pageSub;

                if (distinctOrderCodes.isEmpty()) {
                    continue;
                }

                // 核销
                tocTemporaryReceiveBillItemDao.updateVerifyItems(distinctOrderCodes, temporaryReceiveBillModel.getMerchantId()
                        , temporaryReceiveBillModel.getBuId(), temporaryReceiveBillModel.getStorePlatformCode(), null);

                // 更新已用于核销的结算单明细
                params.clear();
                params.put("billId", temporaryReceiveBillModel.getId());
                params.put("orderCodeList", distinctOrderCodes);
                params.put("buId", temporaryReceiveBillModel.getBuId());
                params.put("merchantId", temporaryReceiveBillModel.getMerchantId());
                params.put("storePlatformCode", temporaryReceiveBillModel.getStorePlatformCode());
                tocSettlementReceiveBillItemDao.batchUpdateTempBillId(params);
            }

        }

        //指定id集合查询对应的订单数量和总金额
        if (!tocIds.isEmpty()) {
            List<Map<String, Object>> settleMapList = tocTemporaryReceiveBillItemDao.countByBillIds(tocIds);
            for (Map<String, Object> settleMap : settleMapList) {
                Long billId = (Long) settleMap.get("billId");
                BigDecimal temporaryRmbAmount = (BigDecimal) settleMap.get("temporaryRmbAmount");
                Long totalNum = (Long) settleMap.get("totalNum");
                BigDecimal noSettlementAmount = (BigDecimal) settleMap.get("noSettlementAmount");
                BigDecimal alreadyReceiveAmount = (BigDecimal) settleMap.get("alreadyReceiveAmount");
                Long noSettlementNum = (Long) settleMap.get("noSettlementNum");
                Timestamp settlementDate = (Timestamp) settleMap.get("settlementDate");
                TocTemporaryReceiveBillModel billModel = new TocTemporaryReceiveBillModel();
                billModel.setId(billId);
                billModel.setTotalReceiveAmount(temporaryRmbAmount);
                billModel.setTotalReceiveNum(totalNum);
                billModel.setLastReceiveAmount(noSettlementAmount);
                billModel.setAlreadyReceiveAmount(alreadyReceiveAmount);
                billModel.setAlreadyReceiveNum(totalNum - noSettlementNum);

                if (noSettlementNum.equals(0L)) {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_2);
                    billModel.setSettlementEndMonth(TimeUtils.formatMonth(settlementDate));
                } else if (totalNum.equals(noSettlementNum)) {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
                    billModel.setSettlementEndMonth(null);
                } else {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_1);
                    billModel.setSettlementEndMonth(null);
                }

                tocTemporaryReceiveBillDao.modify(billModel);
            }
        }
    }

    @Override
    @SystemServiceLog(point=DERP_LOG_POINT.POINT_13201160021,model=DERP_LOG_POINT.POINT_13201160021_Label)
    public void updateVerifyToCTempCostBill(String json, String keys, String topics, String tags) throws Exception {
        LOGGER.info("===================To C暂估收入核销toc结算单应收明细 开始=================="+json);
        JSONObject jsonData = JSONObject.fromObject(json);
        String month = null;
        if(jsonData.containsKey("month")) {
            month = jsonData.getString("month");
        } else {
            Date today = new Date();
            month = TimeUtils.getLastMonth(today);
        }

        Integer merchantId = (Integer) jsonData.get("merchantId");
        Integer jsonBuId = (Integer) jsonData.get("buId");
        Integer jsonCustomerId = (Integer) jsonData.get("customerId");
        String shopCode = jsonData.get("shopCode") == null ? "" : (String) jsonData.get("shopCode");
        String shopTypeCode = (String) jsonData.get("shopTypeCode");
        String storePlatformCode = (String) jsonData.get("storePlatformCode");


        Long merId = (merchantId != null) ? Long.valueOf(merchantId) : null;
        Long buId = (jsonBuId != null) ? Long.valueOf(jsonBuId) : null;
        Long customerId = (jsonCustomerId != null) ? Long.valueOf(jsonCustomerId) : null;

        //查询对应的toc暂估
        TocTemporaryCostBillModel tempBllModel = new TocTemporaryCostBillModel();
        tempBllModel.setMonth(month);
        tempBllModel.setMerchantId(merId);
        tempBllModel.setCustomerId(customerId);
        tempBllModel.setBuId(buId);
        tempBllModel.setStorePlatformCode(storePlatformCode);
        tempBllModel.setShopCode(shopCode);
        tempBllModel.setShopTypeCode(shopTypeCode);
        List<TocTemporaryCostBillModel> tocTemporaryCostBillModels = tocTemporaryCostBillDao.list(tempBllModel);
        if (tocTemporaryCostBillModels == null || tocTemporaryCostBillModels.size() == 0) {
            return;
        }

        List<Long> tocIds = new ArrayList<>();
        //遍历toc暂估
        for (TocTemporaryCostBillModel temporaryCostBillModel : tocTemporaryCostBillModels) {

            tocIds.add(temporaryCostBillModel.getId());
            //分页数量
            Integer pageSize = 3000;

            //统计暂估费用明细数量分页
            TocTemporaryReceiveBillCostItemDTO costItemDTO = new TocTemporaryReceiveBillCostItemDTO();
            costItemDTO.setBillId(temporaryCostBillModel.getId());
            int costBillNum = tocTemporaryReceiveBillCostItemDao.countTempBillNum(costItemDTO);

            for (int i = 0; i < costBillNum; ) {
                int pageSub = (i+pageSize) < costBillNum ? (i+pageSize) : costBillNum;
                costItemDTO.setBegin(i);
                costItemDTO.setPageSize(pageSize);
                i = pageSub;

                //待核销的电商订单单号集合
//                List<String> orderCodeList = tocTemporaryReceiveBillCostItemDao.getOrderCodesPage(costItemDTO);
                List<String> externalCodeList = tocTemporaryReceiveBillCostItemDao.getExternalCodesPage(costItemDTO);

//                List<String> distinctOrderCodes = orderCodeList.stream().distinct().collect(Collectors.toList());
                List<String> distinctOrderCodes = externalCodeList.stream().distinct().collect(Collectors.toList());

                if (distinctOrderCodes.isEmpty()) {
                    continue;
                }
                Map<String, Object> params = new HashMap<>(5);
                tocTemporaryReceiveBillCostItemDao.updateVerifyCostItems(distinctOrderCodes, "1", temporaryCostBillModel.getMerchantId(), temporaryCostBillModel.getBuId(), temporaryCostBillModel.getStorePlatformCode(), null);
                // 更新已用于核销的结算单明细
                params.put("billId", temporaryCostBillModel.getId());
                params.put("orderCodeList", distinctOrderCodes);
                params.put("buId", temporaryCostBillModel.getBuId());
                params.put("merchantId", temporaryCostBillModel.getMerchantId());
                params.put("storePlatformCode", temporaryCostBillModel.getStorePlatformCode());
                tocSettlementReceiveBillCostItemDao.batchUpdateTempCostBillId(params);

                // 判断是否存在差异调整单, 有则更新暂估
                tocTemporaryReceiveBillCostItemDao.batchUpdateVerifyCostItemsByDiffItem2(params);

                // 查询现在未核销的数据，生成差异调整单
                // 相同订单号+单据类型至少存在一个费项有结算数据
                params.put("settlementMark", 0);

                // 暂估未结算
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

                // 用差异调整单更新暂估
                tocTemporaryReceiveBillCostItemDao.batchUpdateVerifyCostItemsByDiffItem2(params);

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

                modelList.clear();
                unVerifyItemList.clear();
                extistTempList.clear();
                withoutSettlementItemList.clear();
                externalCodeList.clear();
                distinctOrderCodes.clear();
            }

        }

        //指定id集合查询对应的订单数量和总金额
        if (!tocIds.isEmpty()) {
            List<Map<String, Object>> settleMapList = tocTemporaryReceiveBillCostItemDao.countByBillIds(tocIds);
            for (Map<String, Object> settleMap : settleMapList) {
                Long billId = (Long) settleMap.get("billId");
                BigDecimal temporaryRmbAmount = (BigDecimal) settleMap.get("temporaryRmbAmount");
                BigDecimal adjustmentRmbAmount = (BigDecimal) settleMap.get("adjustmentRmbAmount");
                Long totalNum = (Long) settleMap.get("totalNum");
                BigDecimal noSettlementAmount = (BigDecimal) settleMap.get("noSettlementAmount");
                BigDecimal alreadyReceiveAmount = (BigDecimal) settleMap.get("alreadyReceiveAmount");
                Long noSettlementNum = (Long) settleMap.get("noSettlementNum");
                Timestamp settlementDate = (Timestamp) settleMap.get("settlementDate");

                TocTemporaryCostBillModel billModel = new TocTemporaryCostBillModel();
                billModel.setId(billId);
                billModel.setTotalReceiveAmount(temporaryRmbAmount);
                billModel.setAlreadyReceiveAmount(alreadyReceiveAmount);

                if (noSettlementNum.equals(0L)) {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_2);
                    billModel.setSettlementEndMonth(TimeUtils.formatMonth(settlementDate));
                } else if (totalNum.equals(noSettlementNum)) {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
                    billModel.setSettlementEndMonth(null);

                } else {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_1);
                    billModel.setSettlementEndMonth(null);
                }
                billModel.setAdjustmentRmbAmount(adjustmentRmbAmount);
                tocTemporaryCostBillDao.modify(billModel);
            }
            settleMapList.clear();
        }
        tocIds.clear();
        tocTemporaryCostBillModels.clear();
    }
}
