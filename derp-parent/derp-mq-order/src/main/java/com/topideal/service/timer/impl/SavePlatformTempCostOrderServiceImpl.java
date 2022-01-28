package com.topideal.service.timer.impl;

import com.alibaba.druid.sql.visitor.functions.If;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.PlatformCostConfigDao;
import com.topideal.dao.common.PlatformCostConfigItemDao;
import com.topideal.dao.common.PlatformSettlementConfigDao;
import com.topideal.dao.platformdata.AlipayMonthlyFeeDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.PlatformCostConfigDTO;
import com.topideal.entity.dto.common.PlatformCostConfigItemDTO;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;
import com.topideal.entity.vo.common.PlatformSettlementConfigModel;
import com.topideal.entity.vo.platformdata.AlipayMonthlyFeeModel;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderReturnIdepotItemModel;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderItemModel;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderModel;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.SavePlatformTempCostOrderService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * 生成平台暂估费用订单
 */
@Service
public class SavePlatformTempCostOrderServiceImpl implements SavePlatformTempCostOrderService {

    public static final Logger LOGGER= LoggerFactory.getLogger(SavePlatformTempCostOrderServiceImpl.class);

    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private PlatformCostConfigDao platformCostConfigDao;
    @Autowired
    private PlatformCostConfigItemDao platformCostConfigItemDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;
    @Autowired
    private PlatformTemporaryCostOrderDao platformTemporaryCostOrderDao;
    @Autowired
    private PlatformTemporaryCostOrderItemDao platformTemporaryCostOrderItemDao;
    @Autowired
    private OrderReturnIdepotDao orderReturnIdepotDao;
    @Autowired
    private OrderReturnIdepotItemDao orderReturnIdepotItemDao;
    @Autowired
    private ReptileConfigMongoDao reptileConfigMongoDao;
    @Autowired
    private MerchantShopRelMongoDao merchantShopRelMongoDao;
    @Autowired
    private PlatformSettlementConfigDao platformSettlementConfigDao;
    @Autowired
    private AlipayMonthlyFeeDao alipayMonthlyFeeDao;


    private void generatePlatformTempCostOrder(String json, String keys, String topics, String tags) throws Exception{
        JSONObject jsonData = JSONObject.fromObject(json);
        Integer merchantId = (Integer) jsonData.get("merchantId");// 商家Id
        String month = (String) jsonData.get("month");// 月份
        String orderType = (String) jsonData.get("orderType");// 单据类型 0-发货订单 1-售后退款单 2-两者

        String storePlatformCodes = (String) jsonData.get("storePlatformCodes");

        List<String> storePlatformCodeList = Arrays.asList(storePlatformCodes.split(","));

        if (StringUtils.isBlank(month)) {
            month = TimeUtils.getLastMonth(new Date());
        }

        if (StringUtils.isBlank(orderType)) {
            orderType = "2";
        }

        // 查询所有商家,若指定了商家则只查本商家
        Map<String, Object> merParams = new HashMap<>();
        if (merchantId != null && merchantId.longValue() > 0) {
            merParams.put("merchantId", Long.valueOf(merchantId));
        }

        List<MerchantInfoMongo> merchantInfoMongos = merchantInfoMongoDao.findAll(merParams);

        for (MerchantInfoMongo merchantInfo : merchantInfoMongos) {

            Map<String, BrandParentMongo> brandParentMap = new HashMap<>();

            //查询该公司+平台的维度的“平台费用配置” 在生效日期范围内且审核时间为最新的一条配置信息
            PlatformCostConfigDTO platformCostConfigDTO = new PlatformCostConfigDTO();
            platformCostConfigDTO.setMerchantId(merchantInfo.getMerchantId());
            platformCostConfigDTO.setMonth(month);
            platformCostConfigDTO.setStatus(DERP_ORDER.PLATFORMCOSTCONFIG_STATUS_1);
            platformCostConfigDTO.setStorePlatformCodes(storePlatformCodeList);
            List<PlatformCostConfigDTO> platformCostConfigModels = platformCostConfigDao.getLatestList(platformCostConfigDTO);

            Map<String, List<PlatformCostConfigDTO>> configDTOMap = new HashMap<>();
            for (PlatformCostConfigDTO costConfigDTO : platformCostConfigModels) {
                Long buId = costConfigDTO.getBuId();
                String storePlatformCode = costConfigDTO.getStorePlatformCode();
                String key = buId + "_" + storePlatformCode;

                List<PlatformCostConfigDTO> costConfigDTOS = new ArrayList<>();
                if (configDTOMap.containsKey(key)) {
                    costConfigDTOS.addAll(configDTOMap.get(key));
                }

                costConfigDTOS.add(costConfigDTO);
                configDTOMap.put(key, costConfigDTOS);
            }

            //根据公司+平台+月份查询已发货的电商订单
            for (String key : configDTOMap.keySet()) {

                List<PlatformCostConfigDTO> costConfigDTOS = configDTOMap.get(key);

                Map<Long, PlatformCostConfigDTO> buConfigMap = new HashMap<>();
                Map<Long, List<PlatformCostConfigItemDTO>> costConfigItemDTOs = new HashMap<>();

                for (PlatformCostConfigDTO costConfigDTO : costConfigDTOS) {
                    buConfigMap.put(costConfigDTO.getBuId(), costConfigDTO);

                    //平台费用配置表体
                    List<PlatformCostConfigItemDTO> configItemDTOS = platformCostConfigItemDao.getConfigById(costConfigDTO.getId());
                    costConfigItemDTOs.put(costConfigDTO.getId(), configItemDTOS);

                }

                if (orderType.matches("0|2")) {
                    //已生成平台暂估费用单的电商订单id
                    List<Long> updateIds = new ArrayList<>();

                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setMerchantId(merchantInfo.getMerchantId());
                    orderDTO.setStatus(DERP_ORDER.ORDER_STATUS_4);
                    orderDTO.setDeliverStartDate(TimeUtils.getFirstDayStartTime(month, "yyyy-MM-dd HH:mm:ss"));
                    orderDTO.setDeliverEndDate(TimeUtils.getLastDayEndTime(month, "yyyy-MM-dd HH:mm:ss"));
                    orderDTO.setIsGenerate(DERP_ORDER.ORDER_IS_GENERATE_0);
                    orderDTO.setStorePlatformName(costConfigDTOS.get(0).getStorePlatformCode());

                    generateOrderTempCost(orderDTO, buConfigMap, brandParentMap, updateIds, costConfigItemDTOs);

                    //批量更新电商订单的是否已生成暂估费用为是
                    if (!updateIds.isEmpty()) {
                        orderDao.batchUpdateStatus(DERP_ORDER.ORDER_IS_GENERATE_1, updateIds);
                    }

                }

                if (orderType.matches("1|2")) {
                    //已生成平台暂估费用单的退款订单id
                    List<Long> updateReturnIds = new ArrayList<>();

                    //退款订单查询
                    OrderReturnIdepotDTO returnIdepotDTO = new OrderReturnIdepotDTO();
                    returnIdepotDTO.setMerchantId(merchantInfo.getMerchantId());
                    returnIdepotDTO.setStatus(DERP_ORDER.ORDER_RETURN_STATUS_013);
                    returnIdepotDTO.setReturnInCreateStartDate(TimeUtils.getFirstDayStartTime(month, "yyyy-MM-dd HH:mm:ss"));
                    returnIdepotDTO.setReturnInCreateEndDate(TimeUtils.getLastDayEndTime(month, "yyyy-MM-dd HH:mm:ss"));
                    returnIdepotDTO.setIsGenerate(DERP_ORDER.ORDER_IS_GENERATE_0);
                    returnIdepotDTO.setStorePlatformCode(costConfigDTOS.get(0).getStorePlatformCode());

                    generateOrderReturnInTempCost(returnIdepotDTO, buConfigMap, brandParentMap, updateReturnIds, costConfigItemDTOs);

                    //批量更新退款订单的是否已生成暂估费用为是
                    if (!updateReturnIds.isEmpty()) {
                        orderReturnIdepotDao.batchUpdateStatus(DERP_ORDER.ORDER_IS_GENERATE_1, updateReturnIds);
                    }
                }
            }

        }
    }

    private void generateTMPlatformTempCostOrder(String json, String keys, String topics, String tags) throws Exception{
        JSONObject jsonData = JSONObject.fromObject(json);
        Integer merchantId = (Integer) jsonData.get("merchantId");// 商家Id
        String month = (String) jsonData.get("month");// 月份
        String orderType = (String) jsonData.get("orderType");// 单据类型 0-发货订单 1-售后退款单 2-两者

        if (StringUtils.isBlank(month)) {
            month = TimeUtils.getLastMonth(new Date());
        }

        if (StringUtils.isBlank(orderType)) {
            orderType = "2";
        }

        // 查询所有商家,若指定了商家则只查本商家
        Map<String, Object> merParams = new HashMap<>();
        if (merchantId != null && merchantId.longValue() > 0) {
            merParams.put("merchantId", Long.valueOf(merchantId));
        }

        List<MerchantInfoMongo> merchantInfoMongos = merchantInfoMongoDao.findAll(merParams);
        String storePlatformCode = "1000000310";
        for (MerchantInfoMongo merchantInfo : merchantInfoMongos) {
                Map<String, BrandParentMongo> brandParentMap = new HashMap<>();
                List<Long> updateIds = new ArrayList<>();
                List<Long> updateReturnIds = new ArrayList<>();
                Map<String, Object> param = new HashMap<>();
                param.put("merchantId", merchantInfo.getMerchantId());
                param.put("storePlatformCode", storePlatformCode);
                param.put("month", month);

                // 获取天猫Order生成平台暂估
                subGenerateTMPlatformTempCostOrder(param, brandParentMap, updateIds, updateReturnIds);
            }
//        }
    }

    private void subGenerateTMPlatformTempCostOrder(Map<String, Object> param, Map<String, BrandParentMongo> brandParentMap, List<Long> updateIds, List<Long> updateReturnIds) throws Exception {
        String storePlatformCode = (String) param.get("storePlatformCode");
        Long merchantId = (Long) param.get("merchantId");
        String month = (String) param.get("month");

        Map<String, Object> reptileConfigParams = new HashMap<>();
        reptileConfigParams.put("platformType", "4");
        reptileConfigParams.put("merchantId", param.get("merchantId"));

        List<ReptileConfigMongo> reptileConfigs = reptileConfigMongoDao.findAll(reptileConfigParams);
        if (reptileConfigs == null || reptileConfigs.isEmpty()) {
            return;
        }

        PlatformSettlementConfigModel queryConfigModel = new PlatformSettlementConfigModel() ;
        queryConfigModel.setStorePlatformCode(storePlatformCode);
        List<PlatformSettlementConfigModel> configList = platformSettlementConfigDao.list(queryConfigModel);

        for (ReptileConfigMongo reptileConfig : reptileConfigs) {
            Map<String, Object> shopParams = new HashMap<String, Object>();
            shopParams.put("shopCode", reptileConfig.getShopCode());

            AlipayMonthlyFeeModel alipayMonthlyFeeModel = new AlipayMonthlyFeeModel();
            alipayMonthlyFeeModel.setMerchantId(merchantId);
            alipayMonthlyFeeModel.setUserCode(reptileConfig.getLoginName());
            alipayMonthlyFeeModel.setSettleYearMonth(month.replace("-", ""));

            // 统计所有的数量 (distinct partner_transaction_id)
            Integer countNum = alipayMonthlyFeeDao.statisticsDistinctByModel(alipayMonthlyFeeModel);
            if (countNum == 0) {
                continue;
            }

            List<PlatformTemporaryCostOrderModel> platformTemporaryCostOrderModels = new ArrayList<>();
            List<PlatformTemporaryCostOrderItemModel> platformTemporaryCostOrderItemModels = new ArrayList<>();

            Integer pageSize = 2000;
            for (int i = 0; i < countNum; ) {
                List<Long> goodsIdList = new ArrayList<>();
                Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();

                int pageSub = (i + pageSize) < countNum ? (i + pageSize) : countNum;
                alipayMonthlyFeeModel.setBegin(i);
                alipayMonthlyFeeModel.setPageSize(pageSize);
                i = pageSub;

                AlipayMonthlyFeeModel alipayMonthlyFee = alipayMonthlyFeeDao.searchDistinctByPage(alipayMonthlyFeeModel);
                List<AlipayMonthlyFeeModel> totalList = alipayMonthlyFee.getList();
                List<String> transactionIdList = totalList.stream().map(entity -> {return entity.getPartnerTransactionId();}).collect(Collectors.toList());

                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("transactionIdList", transactionIdList);
                queryMap.put("merchantId", merchantId);
                queryMap.put("userCode", reptileConfig.getLoginName());
                queryMap.put("settleYearMonth", alipayMonthlyFeeModel.getSettleYearMonth());
                List<AlipayMonthlyFeeModel> alipayMonthlyFeeModels = alipayMonthlyFeeDao.listByMap(queryMap);

                if(alipayMonthlyFeeModels == null || alipayMonthlyFeeModels.size() <= 0) {
                    continue;
                }

                Map<String, PlatformSettlementConfigModel> feeDescAndconfigMap = new HashMap<>();
                // 校验平台费项配置
                alipayMonthlyFeeModels.stream().forEach(entity -> {
                    PlatformSettlementConfigModel config = null;
                    /**取对应费项配置*/
                    for (PlatformSettlementConfigModel configModel : configList) {
                        if(entity.getFeeDesc().indexOf(configModel.getName()) > -1) {
                            config = configModel;
                            break ;
                        }
                    }
                    if (config == null) {
                        throw new RuntimeException("平台费项配置：" + entity.getFeeDesc() + "不存在");
                    }

                    feeDescAndconfigMap.put(entity.getFeeDesc(), config);
                });

                // 根据orderType 进行分组
                Map<String, List<AlipayMonthlyFeeModel>> groupByOrderTypeMap = alipayMonthlyFeeModels.stream().collect(Collectors.groupingBy(entity -> {
                    String coseBillOrderType = DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_1;
                    if (entity.getFeeAmount().compareTo(new BigDecimal("0")) == -1) {
                        coseBillOrderType = DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_0;
                    }
                    return coseBillOrderType;
                }));

                // 发货天猫
                List<AlipayMonthlyFeeModel> alipayMonthlyFeeOrderList = groupByOrderTypeMap.get(DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_1);
                if(alipayMonthlyFeeOrderList != null && alipayMonthlyFeeOrderList.size() > 0) {

                    Map<String, List<AlipayMonthlyFeeModel>> transactionIdAndModelMap = alipayMonthlyFeeOrderList.stream()
                            .collect(Collectors.groupingBy(e -> e.getPartnerTransactionId() + "_" + feeDescAndconfigMap.get(e.getFeeDesc()).getSettlementConfigId()));
                    Map<String, List<AlipayMonthlyFeeModel>> feeDescAndIdMap = new HashMap<>();
                    transactionIdAndModelMap.forEach((key, value) -> {
                        AlipayMonthlyFeeModel alipayMonthlyFeeModel1 = value.get(0);
                        alipayMonthlyFeeModel1.setFeeAmount(value.stream().map(AlipayMonthlyFeeModel::getFeeAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

                        if(feeDescAndIdMap.containsKey(alipayMonthlyFeeModel1.getPartnerTransactionId())) {
                            feeDescAndIdMap.get(alipayMonthlyFeeModel1.getPartnerTransactionId()).add(alipayMonthlyFeeModel1);
                        }else {
                            List<AlipayMonthlyFeeModel> list = new ArrayList<>();
                            list.add(alipayMonthlyFeeModel1);
                            feeDescAndIdMap.put(alipayMonthlyFeeModel1.getPartnerTransactionId(), list);
                        }
                    });
                    Set<String> alipayMonthlyFeeOrderTransactionIdSet = alipayMonthlyFeeOrderList.stream().map(AlipayMonthlyFeeModel::getPartnerTransactionId).collect(Collectors.toSet());
                    List<OrderDTO> orderDTOs = new ArrayList<>();
                    Map<String, Object> paramMap = new HashMap<>(5);
                    paramMap.put("externalCodeList", alipayMonthlyFeeOrderTransactionIdSet);
                    paramMap.put("merchantId", merchantId);
                    paramMap.put("status", DERP_ORDER.ORDER_STATUS_4);
                    paramMap.put("isGenerate", DERP_ORDER.ORDER_IS_GENERATE_0);
                    paramMap.put("storePlatformCode", storePlatformCode);
                    List<OrderDTO> subOrderDTOList = orderDao.selectDTOListByMap(paramMap);
                    if(subOrderDTOList != null && subOrderDTOList.size() > 0) {
                        orderDTOs.addAll(subOrderDTOList);
                    }

                    //根据电商订单id集合查询每个电商订单结算金额占比最大的商品明细
                    Map<Long, OrderDTO> orderDTOMap = new HashMap<>();
                    List<Long> orderIds = new ArrayList<>();
                    //生成电商订单的电商订单号
                    List<String> orderCodeList = new ArrayList<>();

                    for (OrderDTO dto : orderDTOs) {
                        orderIds.add(dto.getId());
                        orderDTOMap.put(dto.getId(), dto);
                        orderCodeList.add(dto.getCode());
                    }

                    List<OrderItemModel> orderItemModels = new ArrayList<>();
                    if(orderIds != null && orderIds.size() > 0) {
                        orderItemModels = orderItemDao.getMaxPriceByOrderId(orderIds, false);
                    }

                    for (OrderItemModel orderItemModel : orderItemModels) {
                        if (!goodsIdList.contains(orderItemModel.getGoodsId())) {
                            goodsIdList.add(orderItemModel.getGoodsId());
                        }
                    }

                    if (!goodsIdList.isEmpty()) {
                        List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", goodsIdList);
                        for (MerchandiseInfoMogo merchandiseInfo : merchandiseInfoMogos) {
                            if (StringUtils.isNotBlank(merchandiseInfo.getCommbarcode())) {
                                merchandiseInfoMogoMap.put(merchandiseInfo.getMerchandiseId(), merchandiseInfo);
                            }
                        }
                    }

                    platformTemporaryCostOrderModels.clear();
                    platformTemporaryCostOrderItemModels.clear();
                    updateIds.clear();
                    for (OrderItemModel orderItemModel : orderItemModels) {

                        OrderDTO order = orderDTOMap.get(orderItemModel.getOrderId());
                        updateIds.add(orderItemModel.getOrderId());

                        //生成平台暂估费用单
                        PlatformTemporaryCostOrderModel costOrderModel = new PlatformTemporaryCostOrderModel();
//                        costOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ZGFY));
                        costOrderModel.setOrderCode(order.getCode());
                        costOrderModel.setExternalCode(order.getExternalCode());
                        costOrderModel.setOrderType(DERP_ORDER.PLATFORMTEMPCOSTORDER_ORDERTYPE_0);
                        costOrderModel.setCurrency(order.getCurrency());
                        costOrderModel.setMerchantId(order.getMerchantId());
                        costOrderModel.setMerchantName(order.getMerchantName());
                        costOrderModel.setDepotId(order.getDepotId());
                        costOrderModel.setDepotName(order.getDepotName());
                        costOrderModel.setStorePlatformCode(order.getStorePlatformName());
                        costOrderModel.setShopCode(order.getShopCode());
                        costOrderModel.setShopName(order.getShopName());
                        costOrderModel.setCustomerId(order.getCustomerId());
                        costOrderModel.setCustomerName(order.getCustomerName());
                        costOrderModel.setBuId(orderItemModel.getBuId());
                        costOrderModel.setBuName(orderItemModel.getBuName());
                        costOrderModel.setShopTypeCode(order.getShopTypeCode());
                        costOrderModel.setDeliverDate(order.getDeliverDate());
                        costOrderModel.setCreateDate(TimeUtils.getNow());
                        platformTemporaryCostOrderModels.add(costOrderModel);

                        MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoMap.get(orderItemModel.getGoodsId());
                        BrandParentMongo brandParent = null;
                        if (merchandiseInfo != null) {
                            brandParent = brandParentMap.get(merchandiseInfo.getCommbarcode());
                            if (brandParent == null) {
                                brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(merchandiseInfo.getCommbarcode());
                            }

                            if (brandParent != null) {
                                brandParentMap.put(merchandiseInfo.getCommbarcode(), brandParent);
                            }
                        }

                        List<AlipayMonthlyFeeModel> list = feeDescAndIdMap.get(order.getExternalCode());
                        for (AlipayMonthlyFeeModel model : list) {
                            PlatformTemporaryCostOrderItemModel costOrderItemModel = new PlatformTemporaryCostOrderItemModel();
                            costOrderItemModel.setOrderCode(order.getCode());
                            costOrderItemModel.setExternalCode(order.getExternalCode());
                            costOrderItemModel.setBuId(orderItemModel.getBuId());
                            costOrderItemModel.setBuName(orderItemModel.getBuName());
                            costOrderItemModel.setAmount(order.getPayment());

                            costOrderItemModel.setRatio(model.getFeeAmount().divide(order.getPayment(), 2, BigDecimal.ROUND_HALF_UP).doubleValue());

                            costOrderItemModel.setSettlementAmount(model.getFeeAmount());
                            if (brandParent != null) {
                                costOrderItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                                costOrderItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                                costOrderItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                                costOrderItemModel.setBrandParentId(brandParent.getBrandParentId());
                                costOrderItemModel.setBrandParentName(brandParent.getName());
                            }

                            PlatformSettlementConfigModel platformSettlementConfigModel = feeDescAndconfigMap.get(model.getFeeDesc());
                            costOrderItemModel.setPlatformSettlementId(platformSettlementConfigModel.getId());
                            costOrderItemModel.setPlatformSettlementName(platformSettlementConfigModel.getName());
                            costOrderItemModel.setCreateDate(TimeUtils.getNow());
                            platformTemporaryCostOrderItemModels.add(costOrderItemModel);
                        }
                    }

                    //批量新增暂估费用单
                    if (platformTemporaryCostOrderModels.size() > 0) {
                        platformTemporaryCostOrderDao.batchSave(platformTemporaryCostOrderModels);
                    }


                    //批量新增暂估费用明细
                    if (platformTemporaryCostOrderItemModels.size() > 0) {
                        for (int j = 0; j < platformTemporaryCostOrderItemModels.size(); ) {
                            int itemPageSub = (j + pageSize) < platformTemporaryCostOrderItemModels.size() ? (j + pageSize) : platformTemporaryCostOrderItemModels.size();
                            platformTemporaryCostOrderItemDao.batchSave(platformTemporaryCostOrderItemModels.subList(j, itemPageSub));
                            j = itemPageSub;
                        }
                    }

                    //根据表头表体的电商订单号批量更新暂估费用明细关联的费用明细id
                    if (orderCodeList.size() > 0) {
                        platformTemporaryCostOrderItemDao.batchUpdateIds(orderCodeList, DERP_ORDER.PLATFORMTEMPCOSTORDER_ORDERTYPE_0);
                    }

                    // 批量跟新电商订单 已生成暂估的状态
                    if (!updateIds.isEmpty()) {
                        orderDao.batchUpdateStatus(DERP_ORDER.ORDER_IS_GENERATE_1, updateIds);
                    }

                    alipayMonthlyFeeOrderList.clear();
                    platformTemporaryCostOrderItemModels.clear();
                    orderCodeList.clear();
                    updateIds.clear();
                    alipayMonthlyFeeOrderTransactionIdSet.clear();
                    subOrderDTOList.clear();
                    orderDTOs.clear();
                    orderItemModels.clear();
                }

                // 天猫退货
                List<AlipayMonthlyFeeModel> alipayMonthlyFeeReturnList = groupByOrderTypeMap.get(DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_0);
                if(alipayMonthlyFeeReturnList != null && alipayMonthlyFeeReturnList.size() > 0) {
                    Set<String> alipayMonthlyFeeReturnTransactionIdSet = alipayMonthlyFeeReturnList.stream().map(AlipayMonthlyFeeModel::getPartnerTransactionId).collect(Collectors.toSet());
//                    Map<String, List<AlipayMonthlyFeeModel>> transactionIdAndModelReturnMap = alipayMonthlyFeeReturnList.stream().collect(Collectors.groupingBy(AlipayMonthlyFeeModel::getPartnerTransactionId));

                    Map<String, List<AlipayMonthlyFeeModel>> transactionIdAndModelReturnMap = alipayMonthlyFeeReturnList.stream()
                            .collect(Collectors.groupingBy(e -> e.getPartnerTransactionId() + "_" + feeDescAndconfigMap.get(e.getFeeDesc()).getSettlementConfigId()));
                    Map<String, List<AlipayMonthlyFeeModel>> feeDescAndIdMap = new HashMap<>();
                    transactionIdAndModelReturnMap.forEach((key, value) -> {
                        AlipayMonthlyFeeModel alipayMonthlyFeeModel1 = value.get(0);
                        alipayMonthlyFeeModel1.setFeeAmount(value.stream().map(AlipayMonthlyFeeModel::getFeeAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

                        if(feeDescAndIdMap.containsKey(alipayMonthlyFeeModel1.getPartnerTransactionId())) {
                            feeDescAndIdMap.get(alipayMonthlyFeeModel1.getPartnerTransactionId()).add(alipayMonthlyFeeModel1);
                        }else {
                            List<AlipayMonthlyFeeModel> list = new ArrayList<>();
                            list.add(alipayMonthlyFeeModel1);
                            feeDescAndIdMap.put(alipayMonthlyFeeModel1.getPartnerTransactionId(), list);
                        }
                    });

                    List<OrderReturnIdepotDTO> orderReturnDTOs = new ArrayList<>();
                    List<Long> orderIds = new ArrayList<>();
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("externalCodeList", alipayMonthlyFeeReturnTransactionIdSet);
                    paramMap.put("merchantId", merchantId);
                    paramMap.put("isGenerate", DERP_ORDER.ORDER_IS_GENERATE_0);
                    paramMap.put("status", DERP_ORDER.ORDER_RETURN_STATUS_013);
                    paramMap.put("StorePlatformCode", storePlatformCode);
                    List<OrderReturnIdepotDTO> subReturnDTOList = orderReturnIdepotDao.selectDTOListByMap(paramMap);

                    if(subReturnDTOList != null && subReturnDTOList.size() > 0) {
                        orderReturnDTOs.addAll(subReturnDTOList);
                    }

                    orderIds.clear();
                    Map<Long, OrderReturnIdepotDTO> orderReturnDTOMap = new HashMap<>();
                    //生成电商订单的电商订单号
                    List<String> orderCodeList = new ArrayList<>();
                    for (OrderReturnIdepotDTO dto : orderReturnDTOs) {
                        orderIds.add(dto.getId());
                        orderReturnDTOMap.put(dto.getId(), dto);
                        orderCodeList.add(dto.getCode());
                    }

                    //根据电商订单id集合查询每个电商订单结算金额占比最大的商品明细
                    List<OrderReturnIdepotItemModel> returnOrderItemModels= new ArrayList<>();
                    if(orderIds != null && orderIds.size() > 0) {
                        returnOrderItemModels = orderReturnIdepotItemDao.getMaxPriceByOrderId(orderIds, false);
                    }
                    for (OrderReturnIdepotItemModel orderItemModel : returnOrderItemModels) {

                        if (!goodsIdList.contains(orderItemModel.getInGoodsId())) {
                            goodsIdList.add(orderItemModel.getInGoodsId());
                        }
                    }

                    if (!goodsIdList.isEmpty()) {
                        List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", goodsIdList);
                        for (MerchandiseInfoMogo merchandiseInfo : merchandiseInfoMogos) {
                            if (StringUtils.isNotBlank(merchandiseInfo.getCommbarcode())) {
                                merchandiseInfoMogoMap.put(merchandiseInfo.getMerchandiseId(), merchandiseInfo);
                            }
                        }
                    }

                    platformTemporaryCostOrderModels.clear();
                    platformTemporaryCostOrderItemModels.clear();
                    updateReturnIds.clear();
                    for (OrderReturnIdepotItemModel orderItemModel : returnOrderItemModels) {

                        OrderReturnIdepotDTO order = orderReturnDTOMap.get(orderItemModel.getOreturnIdepotId());
                        updateReturnIds.add(orderItemModel.getOreturnIdepotId());

                        //生成平台暂估费用单
                        PlatformTemporaryCostOrderModel costOrderModel = new PlatformTemporaryCostOrderModel();
//                        costOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ZGFY));
                        costOrderModel.setOrderCode(order.getOrderCode());
                        costOrderModel.setReturnOrderCode(order.getCode());
                        costOrderModel.setExternalCode(order.getExternalCode());
                        costOrderModel.setOrderType(DERP_ORDER.PLATFORMTEMPCOSTORDER_ORDERTYPE_1);
                        costOrderModel.setCurrency(order.getCurrency());
                        costOrderModel.setMerchantId(order.getMerchantId());
                        costOrderModel.setMerchantName(order.getMerchantName());
                        costOrderModel.setDepotId(order.getReturnInDepotId());
                        costOrderModel.setDepotName(order.getReturnInDepotName());
                        costOrderModel.setStorePlatformCode(order.getStorePlatformCode());
                        costOrderModel.setShopCode(order.getShopCode());
                        costOrderModel.setShopName(order.getShopName());
                        costOrderModel.setCustomerId(order.getCustomerId());
                        costOrderModel.setCustomerName(order.getCustomerName());
                        costOrderModel.setBuId(orderItemModel.getBuId());
                        costOrderModel.setBuName(orderItemModel.getBuName());
                        costOrderModel.setShopTypeCode(order.getShopTypeCode());
                        costOrderModel.setDeliverDate(order.getRefundEndDate());
                        costOrderModel.setCreateDate(TimeUtils.getNow());
                        platformTemporaryCostOrderModels.add(costOrderModel);

                        MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoMap.get(orderItemModel.getInGoodsId());
                        BrandParentMongo brandParent = null;
                        if (merchandiseInfo != null) {
                            brandParent = brandParentMap.get(merchandiseInfo.getCommbarcode());
                            if (brandParent == null) {
                                brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(merchandiseInfo.getCommbarcode());
                            }

                            if (brandParent != null) {
                                brandParentMap.put(merchandiseInfo.getCommbarcode(), brandParent);
                            }
                        }
                        List<AlipayMonthlyFeeModel> list = feeDescAndIdMap.get(order.getExternalCode());

                        for (AlipayMonthlyFeeModel model : list) {
                            PlatformTemporaryCostOrderItemModel costOrderItemModel = new PlatformTemporaryCostOrderItemModel();
                            costOrderItemModel.setOrderCode(order.getCode());
                            costOrderItemModel.setExternalCode(order.getExternalCode());
                            costOrderItemModel.setBuId(orderItemModel.getBuId());
                            costOrderItemModel.setBuName(orderItemModel.getBuName());
                            costOrderItemModel.setAmount(order.getTotalRefundAmount().negate());
                            costOrderItemModel.setSettlementAmount(model.getFeeAmount());
                            costOrderItemModel.setRatio(model.getFeeAmount().divide(order.getTotalRefundAmount(), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
                            if (brandParent != null) {
                                costOrderItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                                costOrderItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                                costOrderItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                                costOrderItemModel.setBrandParentId(brandParent.getBrandParentId());
                                costOrderItemModel.setBrandParentName(brandParent.getName());
                            }
                            PlatformSettlementConfigModel platformSettlementConfigModel = feeDescAndconfigMap.get(model.getFeeDesc());
                            costOrderItemModel.setPlatformSettlementId(platformSettlementConfigModel.getId());
                            costOrderItemModel.setPlatformSettlementName(platformSettlementConfigModel.getName());
                            costOrderItemModel.setCreateDate(TimeUtils.getNow());

                            platformTemporaryCostOrderItemModels.add(costOrderItemModel);
                        }
                    }

                    //批量新增暂估费用单
                    if (platformTemporaryCostOrderModels.size() > 0) {
                        platformTemporaryCostOrderDao.batchSave(platformTemporaryCostOrderModels);
                    }


                    //批量新增暂估费用明细
                    if (platformTemporaryCostOrderItemModels.size() > 0) {
                        for (int j = 0; j < platformTemporaryCostOrderItemModels.size(); ) {
                            int itemPageSub = (j + pageSize) < platformTemporaryCostOrderItemModels.size() ? (j + pageSize) : platformTemporaryCostOrderItemModels.size();
                            platformTemporaryCostOrderItemDao.batchSave(platformTemporaryCostOrderItemModels.subList(j, itemPageSub));
                            j = itemPageSub;
                        }
                    }

                    //根据表头表体的电商订单号批量更新暂估费用明细关联的费用明细id
                    if (orderCodeList.size() > 0) {
                        platformTemporaryCostOrderItemDao.batchUpdateIds(orderCodeList, DERP_ORDER.PLATFORMTEMPCOSTORDER_ORDERTYPE_1);
                    }

                    if (!updateReturnIds.isEmpty()) {
                        orderReturnIdepotDao.batchUpdateStatus(DERP_ORDER.ORDER_IS_GENERATE_1, updateReturnIds);
                    }

                    alipayMonthlyFeeReturnList.clear();
                    updateReturnIds.clear();
                    orderCodeList.clear();
                    platformTemporaryCostOrderItemModels.clear();
                    platformTemporaryCostOrderModels.clear();
                    subReturnDTOList.clear();
                    transactionIdAndModelReturnMap.clear();
                }

                alipayMonthlyFeeModels.clear();
                totalList.clear();
                transactionIdList.clear();
                groupByOrderTypeMap.clear();
            }
        }
    }

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201120006, model = DERP_LOG_POINT.POINT_13201120006_Label)
    public void savePlatformTempCostOrder(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String storePlatformCodes = (String) jsonData.get("storePlatformCodes");

        List<String> storePlatformCodeList = Arrays.asList(storePlatformCodes.split(","));

        if (storePlatformCodeList.isEmpty() || storePlatformCodeList.contains(DERP.STOREPLATFORM_1000000310)) {
            // 生成天猫平台暂估费用
            generateTMPlatformTempCostOrder(json, keys, topics, tags);
        }

        // 生成非天猫平台暂估费用
        generatePlatformTempCostOrder(json, keys, topics, tags);
    }

    /**
     * @param orderDTO
     * @param brandParentMap     已查询母品牌集合
     * @param buConfigMap        事业部平台费用配置集合
     * @param updateIds          生成的电商订单id集合
     * @param costConfigItemDTOs 平台费用配置表体
     * @Description 电商订单生成平台暂估费用单
     */
    private void generateOrderTempCost(OrderDTO orderDTO, Map<Long, PlatformCostConfigDTO> buConfigMap,
                                       Map<String, BrandParentMongo> brandParentMap, List<Long> updateIds,
                                       Map<Long, List<PlatformCostConfigItemDTO>> costConfigItemDTOs) throws Exception {

        int pageSize = 1000;

        Integer count = orderDao.queryDtoListCount(orderDTO);

        //电商订单生成平台暂估费用单
        for (int i = 0; i < count; ) {

            List<Long> goodsIdList = new ArrayList<>();
            Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();

            //生成电商订单的电商订单号
            List<String> orderCodeList = new ArrayList<>();

            int pageSub = (i + pageSize) < count ? (i + pageSize) : count;
            orderDTO.setBegin(i);
            orderDTO.setPageSize(pageSize);
            OrderDTO orderDTOPage = orderDao.newDtoListByPage(orderDTO);
            List<OrderDTO> orderDTOS = orderDTOPage.getList();
            i = pageSub;
            if (orderDTOS.size() == 0) {
                continue;
            }

            Map<Long, OrderDTO> orderDTOMap = new HashMap<>();
            List<PlatformTemporaryCostOrderModel> platformTemporaryCostOrderModels = new ArrayList<>();
            List<PlatformTemporaryCostOrderItemModel> platformTemporaryCostOrderItemModels = new ArrayList<>();
            List<Long> orderIds = new ArrayList<>();

            for (OrderDTO dto : orderDTOS) {
                orderIds.add(dto.getId());
                orderDTOMap.put(dto.getId(), dto);
                orderCodeList.add(dto.getCode());
            }

            //根据电商订单id集合查询每个电商订单结算金额占比最大的商品明细
            List<OrderItemModel> orderItemModels = orderItemDao.getMaxPriceByOrderId(orderIds, false);

            for (OrderItemModel orderItemModel : orderItemModels) {

                PlatformCostConfigDTO costConfigDTO = buConfigMap.get(orderItemModel.getBuId());

                if (costConfigDTO == null) {
                    continue;
                }

                if (!goodsIdList.contains(orderItemModel.getGoodsId())) {
                    goodsIdList.add(orderItemModel.getGoodsId());
                }

            }

            if (!goodsIdList.isEmpty()) {
                List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", goodsIdList);
                for (MerchandiseInfoMogo merchandiseInfo : merchandiseInfoMogos) {
                    if (StringUtils.isNotBlank(merchandiseInfo.getCommbarcode())) {
                        merchandiseInfoMogoMap.put(merchandiseInfo.getMerchandiseId(), merchandiseInfo);
                    }
                }
            }

            for (OrderItemModel orderItemModel : orderItemModels) {

                PlatformCostConfigDTO costConfigDTO = buConfigMap.get(orderItemModel.getBuId());

                if (costConfigDTO == null) {
                    continue;
                }

                List<PlatformCostConfigItemDTO> configItemDTOS = costConfigItemDTOs.get(costConfigDTO.getId());

                OrderDTO order = orderDTOMap.get(orderItemModel.getOrderId());
                updateIds.add(orderItemModel.getOrderId());

                //生成平台暂估费用单
                PlatformTemporaryCostOrderModel costOrderModel = new PlatformTemporaryCostOrderModel();
//                costOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ZGFY));
                costOrderModel.setOrderCode(order.getCode());
                costOrderModel.setExternalCode(order.getExternalCode());
                costOrderModel.setOrderType(DERP_ORDER.PLATFORMTEMPCOSTORDER_ORDERTYPE_0);
                costOrderModel.setCurrency(order.getCurrency());
                costOrderModel.setMerchantId(order.getMerchantId());
                costOrderModel.setMerchantName(order.getMerchantName());
                costOrderModel.setDepotId(order.getDepotId());
                costOrderModel.setDepotName(order.getDepotName());
                costOrderModel.setStorePlatformCode(order.getStorePlatformName());
                costOrderModel.setShopCode(order.getShopCode());
                costOrderModel.setShopName(order.getShopName());
                costOrderModel.setCustomerId(order.getCustomerId());
                costOrderModel.setCustomerName(order.getCustomerName());
                costOrderModel.setBuId(orderItemModel.getBuId());
                costOrderModel.setBuName(orderItemModel.getBuName());
                costOrderModel.setShopTypeCode(order.getShopTypeCode());
                costOrderModel.setDeliverDate(order.getDeliverDate());
                costOrderModel.setCreateDate(TimeUtils.getNow());
                platformTemporaryCostOrderModels.add(costOrderModel);

                MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoMap.get(orderItemModel.getGoodsId());
                BrandParentMongo brandParent = null;
                if (merchandiseInfo != null) {
                    brandParent = brandParentMap.get(merchandiseInfo.getCommbarcode());
                    if (brandParent == null) {
                        brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(merchandiseInfo.getCommbarcode());
                    }

                    if (brandParent != null) {
                        brandParentMap.put(merchandiseInfo.getCommbarcode(), brandParent);
                    }
                }

                for (PlatformCostConfigItemDTO itemDTO : configItemDTOS) {
                    PlatformTemporaryCostOrderItemModel costOrderItemModel = new PlatformTemporaryCostOrderItemModel();
                    costOrderItemModel.setOrderCode(order.getCode());
                    costOrderItemModel.setExternalCode(order.getExternalCode());
                    costOrderItemModel.setBuId(orderItemModel.getBuId());
                    costOrderItemModel.setBuName(orderItemModel.getBuName());
                    costOrderItemModel.setAmount(order.getPayment());
                    costOrderItemModel.setRatio(itemDTO.getRatio());
                    costOrderItemModel.setSettlementAmount(order.getPayment().multiply(new BigDecimal(itemDTO.getRatio().toString())));
                    if (brandParent != null) {
                        costOrderItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                        costOrderItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                        costOrderItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                        costOrderItemModel.setBrandParentId(brandParent.getBrandParentId());
                        costOrderItemModel.setBrandParentName(brandParent.getName());
                    }

                    costOrderItemModel.setPlatformSettlementId(itemDTO.getPlatformSettlementId());
                    costOrderItemModel.setPlatformSettlementName(itemDTO.getPlatformSettlementName());
                    costOrderItemModel.setCreateDate(TimeUtils.getNow());

                    platformTemporaryCostOrderItemModels.add(costOrderItemModel);
                }

            }

            //批量新增暂估费用单
            if (platformTemporaryCostOrderModels.size() > 0) {
                platformTemporaryCostOrderDao.batchSave(platformTemporaryCostOrderModels);
            }


            //批量新增暂估费用明细
            if (platformTemporaryCostOrderItemModels.size() > 0) {
                for (int j = 0; j < platformTemporaryCostOrderItemModels.size(); ) {
                    int itemPageSub = (j + pageSize) < platformTemporaryCostOrderItemModels.size() ? (j + pageSize) : platformTemporaryCostOrderItemModels.size();
                    platformTemporaryCostOrderItemDao.batchSave(platformTemporaryCostOrderItemModels.subList(j, itemPageSub));
                    j = itemPageSub;
                }
            }

            //根据表头表体的电商订单号批量更新暂估费用明细关联的费用明细id
            if (orderCodeList.size() > 0) {
                platformTemporaryCostOrderItemDao.batchUpdateIds(orderCodeList, DERP_ORDER.PLATFORMTEMPCOSTORDER_ORDERTYPE_0);
            }

        }
    }


    /**
     * @param returnIdepotDTO
     * @param brandParentMap     已查询母品牌集合
     * @param buConfigMap        事业部平台费用配置集合
     * @param updateReturnIds    生成的退款订单id集合
     * @param costConfigItemDTOs 平台费用配置表体
     * @Description 退款订单生成平台暂估费用单
     */
    private void generateOrderReturnInTempCost(OrderReturnIdepotDTO returnIdepotDTO, Map<Long, PlatformCostConfigDTO> buConfigMap,
                                               Map<String, BrandParentMongo> brandParentMap, List<Long> updateReturnIds,
                                               Map<Long, List<PlatformCostConfigItemDTO>> costConfigItemDTOs) throws Exception {

        int pageSize = 1000;

        Integer returnCount = orderReturnIdepotDao.queryDTOListCount(returnIdepotDTO);
        //退款单生成平台暂估费用单
        for (int i = 0; i < returnCount; ) {

            List<Long> goodsIdList = new ArrayList<>();
            Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();

            //生成电商订单的电商订单号
            List<String> orderCodeList = new ArrayList<>();

            int pageSub = (i + pageSize) < returnCount ? (i + pageSize) : returnCount;
            returnIdepotDTO.setBegin(i);
            returnIdepotDTO.setPageSize(pageSize);
            OrderReturnIdepotDTO orderReturnIdepotDTO = orderReturnIdepotDao.newDTOListByPage(returnIdepotDTO);
            List<OrderReturnIdepotDTO> orderDTOS = orderReturnIdepotDTO.getList();
            i = pageSub;
            if (orderDTOS.size() == 0) {
                continue;
            }

            List<Long> orderIds = new ArrayList<>();
            Map<Long, OrderReturnIdepotDTO> orderDTOMap = new HashMap<>();
            List<PlatformTemporaryCostOrderModel> platformTemporaryCostOrderModels = new ArrayList<>();
            List<PlatformTemporaryCostOrderItemModel> platformTemporaryCostOrderItemModels = new ArrayList<>();

            for (OrderReturnIdepotDTO dto : orderDTOS) {
                orderIds.add(dto.getId());
                orderDTOMap.put(dto.getId(), dto);
                orderCodeList.add(dto.getCode());
            }

            //根据电商订单id集合查询每个电商订单结算金额占比最大的商品明细
            List<OrderReturnIdepotItemModel> orderItemModels = orderReturnIdepotItemDao.getMaxPriceByOrderId(orderIds, false);

            for (OrderReturnIdepotItemModel orderItemModel : orderItemModels) {

                PlatformCostConfigDTO costConfigDTO = buConfigMap.get(orderItemModel.getBuId());

                if (costConfigDTO == null) {
                    continue;
                }

                if (!goodsIdList.contains(orderItemModel.getInGoodsId())) {
                    goodsIdList.add(orderItemModel.getInGoodsId());
                }

            }

            if (!goodsIdList.isEmpty()) {
                List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", goodsIdList);
                for (MerchandiseInfoMogo merchandiseInfo : merchandiseInfoMogos) {
                    if (StringUtils.isNotBlank(merchandiseInfo.getCommbarcode())) {
                        merchandiseInfoMogoMap.put(merchandiseInfo.getMerchandiseId(), merchandiseInfo);
                    }
                }
            }

            for (OrderReturnIdepotItemModel orderItemModel : orderItemModels) {

                PlatformCostConfigDTO costConfigDTO = buConfigMap.get(orderItemModel.getBuId());

                if (costConfigDTO == null) {
                    continue;
                }

                List<PlatformCostConfigItemDTO> configItemDTOS = costConfigItemDTOs.get(costConfigDTO.getId());

                OrderReturnIdepotDTO order = orderDTOMap.get(orderItemModel.getOreturnIdepotId());
                updateReturnIds.add(orderItemModel.getOreturnIdepotId());

                //生成平台暂估费用单
                PlatformTemporaryCostOrderModel costOrderModel = new PlatformTemporaryCostOrderModel();
//                costOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ZGFY));
                costOrderModel.setOrderCode(order.getOrderCode());
                costOrderModel.setReturnOrderCode(order.getCode());
                costOrderModel.setExternalCode(order.getExternalCode());
                costOrderModel.setOrderType(DERP_ORDER.PLATFORMTEMPCOSTORDER_ORDERTYPE_1);
                costOrderModel.setCurrency(order.getCurrency());
                costOrderModel.setMerchantId(order.getMerchantId());
                costOrderModel.setMerchantName(order.getMerchantName());
                costOrderModel.setDepotId(order.getReturnInDepotId());
                costOrderModel.setDepotName(order.getReturnInDepotName());
                costOrderModel.setStorePlatformCode(order.getStorePlatformCode());
                costOrderModel.setShopCode(order.getShopCode());
                costOrderModel.setShopName(order.getShopName());
                costOrderModel.setCustomerId(order.getCustomerId());
                costOrderModel.setCustomerName(order.getCustomerName());
                costOrderModel.setBuId(orderItemModel.getBuId());
                costOrderModel.setBuName(orderItemModel.getBuName());
                costOrderModel.setShopTypeCode(order.getShopTypeCode());
                costOrderModel.setDeliverDate(order.getRefundEndDate());
                costOrderModel.setCreateDate(TimeUtils.getNow());
                platformTemporaryCostOrderModels.add(costOrderModel);

                MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoMap.get(orderItemModel.getInGoodsId());
                BrandParentMongo brandParent = null;
                if (merchandiseInfo != null) {
                    brandParent = brandParentMap.get(merchandiseInfo.getCommbarcode());
                    if (brandParent == null) {
                        brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(merchandiseInfo.getCommbarcode());
                    }

                    if (brandParent != null) {
                        brandParentMap.put(merchandiseInfo.getCommbarcode(), brandParent);
                    }
                }

                for (PlatformCostConfigItemDTO itemDTO : configItemDTOS) {
                    PlatformTemporaryCostOrderItemModel costOrderItemModel = new PlatformTemporaryCostOrderItemModel();
                    costOrderItemModel.setOrderCode(order.getCode());
                    costOrderItemModel.setExternalCode(order.getExternalCode());
                    costOrderItemModel.setBuId(orderItemModel.getBuId());
                    costOrderItemModel.setBuName(orderItemModel.getBuName());
                    costOrderItemModel.setAmount(order.getTotalRefundAmount().negate());
                    costOrderItemModel.setRatio(itemDTO.getRatio());
                    costOrderItemModel.setSettlementAmount(order.getTotalRefundAmount().negate().multiply(new BigDecimal(itemDTO.getRatio().toString())));
                    if (brandParent != null) {
                        costOrderItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                        costOrderItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                        costOrderItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                        costOrderItemModel.setBrandParentId(brandParent.getBrandParentId());
                        costOrderItemModel.setBrandParentName(brandParent.getName());
                    }

                    costOrderItemModel.setPlatformSettlementId(itemDTO.getPlatformSettlementId());
                    costOrderItemModel.setPlatformSettlementName(itemDTO.getPlatformSettlementName());
                    costOrderItemModel.setCreateDate(TimeUtils.getNow());

                    platformTemporaryCostOrderItemModels.add(costOrderItemModel);
                }

            }

            //批量新增暂估费用单
            if (platformTemporaryCostOrderModels.size() > 0) {
                platformTemporaryCostOrderDao.batchSave(platformTemporaryCostOrderModels);
            }


            //批量新增暂估费用明细
            if (platformTemporaryCostOrderItemModels.size() > 0) {
                for (int j = 0; j < platformTemporaryCostOrderItemModels.size(); ) {
                    int itemPageSub = (j + pageSize) < platformTemporaryCostOrderItemModels.size() ? (j + pageSize) : platformTemporaryCostOrderItemModels.size();
                    platformTemporaryCostOrderItemDao.batchSave(platformTemporaryCostOrderItemModels.subList(j, itemPageSub));
                    j = itemPageSub;
                }
            }

            //根据表头表体的电商订单号批量更新暂估费用明细关联的费用明细id
            if (orderCodeList.size() > 0) {
                platformTemporaryCostOrderItemDao.batchUpdateIds(orderCodeList, DERP_ORDER.PLATFORMTEMPCOSTORDER_ORDERTYPE_1);
            }

        }
    }


}
