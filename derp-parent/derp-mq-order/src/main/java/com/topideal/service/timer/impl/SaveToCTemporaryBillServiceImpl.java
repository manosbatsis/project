package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.bill.*;
import com.topideal.dao.common.PlatformSettlementConfigDao;
import com.topideal.dao.platformdata.AlipayMonthlyFeeDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;
import com.topideal.entity.dto.sale.*;
import com.topideal.entity.vo.bill.TocTemporaryCostBillModel;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillItemModel;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillModel;
import com.topideal.entity.vo.common.PlatformSettlementConfigModel;
import com.topideal.entity.vo.platformdata.AlipayMonthlyFeeModel;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderReturnIdepotItemModel;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderItemModel;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.service.timer.SaveToCTemporaryBillService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SaveToCTemporaryBillServiceImpl implements SaveToCTemporaryBillService {

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private MerchantShopRelMongoDao merchantShopRelMongoDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao;
    @Autowired
    private TocTemporaryReceiveBillDao tocTemporaryReceiveBillDao;
    @Autowired
    private TocTemporaryReceiveBillItemDao tocTemporaryReceiveBillItemDao;
    @Autowired
    private TocTemporaryCostBillDao tocTemporaryCostBillDao;
    @Autowired
    private TocTemporaryReceiveBillCostItemDao tocTemporaryReceiveBillCostItemDao;
    @Autowired
    private AlipayMonthlyFeeDao alipayMonthlyFeeDao;
    @Autowired
    private OrderReturnIdepotDao orderReturnIdepotDao;
    @Autowired
    private OrderReturnIdepotItemDao orderReturnIdepotItemDao;
    @Autowired
    private PlatformSettlementConfigDao platformSettlementConfigDao;
    @Autowired
    private PlatformTemporaryCostOrderDao platformTemporaryCostOrderDao;
    @Autowired
    private PlatformTemporaryCostOrderItemDao platformTemporaryCostOrderItemDao;
    @Autowired
    private ReptileConfigMongoDao reptileConfigMongoDao;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;
    @Autowired
    private TocSettlementReceiveBillCostItemDao tocSettlementReceiveBillCostItemDao;
    @Autowired
    private TocSettlementReceiveBillItemDao tocSettlementReceiveBillItemDao;


    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201169310, model = DERP_LOG_POINT.POINT_13201169310_Label)
    public void saveToCTemporaryBill(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String month = jsonData.getString("month");

        //指定公司、运营类型、店铺、客户、平台、事业部触发
        Integer merchantId = (Integer) jsonData.get("merchantId");
        Integer buId = (Integer) jsonData.get("buId");
        Integer customerId = (Integer) jsonData.get("customerId");
        String shopCode = jsonData.get("shopCode") == null ? "" : (String) jsonData.get("shopCode");
        String shopTypeCode = (String) jsonData.get("shopTypeCode");
        String storePlatformCode = (String) jsonData.get("storePlatformCode");
        Integer pageSize = 3000;

        OrderDTO orderDTO = new OrderDTO();
        OrderReturnIdepotDTO returnIdepotDTO = new OrderReturnIdepotDTO();
        if (merchantId != null) {
            orderDTO.setMerchantId(Long.valueOf(merchantId));
            returnIdepotDTO.setMerchantId(Long.valueOf(merchantId));
        }

        if (customerId != null) {
            orderDTO.setCustomerId(Long.valueOf(customerId));
            returnIdepotDTO.setCustomerId(Long.valueOf(customerId));
        }

        orderDTO.setShopCode(shopCode);
        orderDTO.setShopTypeCode(shopTypeCode);
        orderDTO.setStorePlatformName(storePlatformCode);
        orderDTO.setMonth(month);

        returnIdepotDTO.setShopCode(shopCode);
        returnIdepotDTO.setShopTypeCode(shopTypeCode);
        returnIdepotDTO.setStorePlatformCode(storePlatformCode);
        returnIdepotDTO.setMonth(month);

        //删除已生成的数据
        TocTemporaryReceiveBillModel alreadyModel = new TocTemporaryReceiveBillModel();
        if (merchantId != null) {
            alreadyModel.setMerchantId(Long.valueOf(merchantId));
        }

        if (buId != null) {
            alreadyModel.setBuId(Long.valueOf(buId));
        }

        if (customerId != null) {
            alreadyModel.setCustomerId(Long.valueOf(customerId));
        }


        alreadyModel.setShopCode(shopCode);
        alreadyModel.setShopTypeCode(shopTypeCode);
        alreadyModel.setStorePlatformCode(storePlatformCode);
        alreadyModel.setMonth(month);

        List<Long> delBillList = tocTemporaryReceiveBillDao.searchIdListByModel(alreadyModel);
//        tocTemporaryReceiveBillDao.deleteByModel(alreadyModel);
        if(delBillList != null && !delBillList.isEmpty()) {
            tocTemporaryReceiveBillDao.delete(delBillList);
        }

        TocTemporaryReceiveBillItemModel alreadyItemModel = new TocTemporaryReceiveBillItemModel();
        if (merchantId != null) {
            alreadyItemModel.setMerchantId(Long.valueOf(merchantId));
        }

        if (buId != null) {
            alreadyItemModel.setBuId(Long.valueOf(buId));
        }

        if (customerId != null) {
            alreadyItemModel.setCustomerId(Long.valueOf(customerId));
        }

        alreadyItemModel.setShopCode(shopCode);
        alreadyItemModel.setShopTypeCode(shopTypeCode);
        alreadyItemModel.setStorePlatformCode(storePlatformCode);
        alreadyItemModel.setMonth(month);
        tocTemporaryReceiveBillItemDao.deleteByModel(alreadyItemModel);

        //”月份+客户+店铺id+运营类型+平台“为维度统计电商订单的数量
        List<Map<String, Object>> orderItemGroup = orderDao.countOrderByDTO(orderDTO);

        //”月份+客户+店铺id+运营类型+平台“为维度统计退款单的数量
        List<Map<String, Object>> returnItemGroup = orderReturnIdepotDao.countOrderReturnIdepotByDTO(returnIdepotDTO);

        //相同”月份+客户+店铺id+运营类型+平台“维度集合
        Map<String, Map<String, Object>> existKeyMap = new HashMap<>();

        //新增toc暂估id
        List<Long> tocIdList = new ArrayList<>();

        //先用退款明细生成existKeyMap
        for (Map<String, Object> returnItem : returnItemGroup) {
            Long groupMerchantId = (Long) returnItem.get("merchantId");
            Long groupCustomerId = (Long) returnItem.get("customerId");
            String groupShopCode = (String) returnItem.get("shopCode");
            String groupShopTypeCode = (String) returnItem.get("shopTypeCode");
            String groupStorePlatformCode = (String) returnItem.get("storePlatformCode");

            String key = groupMerchantId + "_" + groupCustomerId + "_" + groupShopCode + "_"
                    + groupShopTypeCode + "_" + groupStorePlatformCode;

            existKeyMap.put(key, returnItem);
        }

        for (Map<String, Object> orderItem : orderItemGroup) {
            Long groupMerchantId = (Long) orderItem.get("merchantId");
            Long groupCustomerId = (Long) orderItem.get("customerId");
            String groupShopCode = (String) orderItem.get("shopCode");
            String groupShopTypeCode = (String) orderItem.get("shopTypeCode");
            String groupStorePlatformCode = (String) orderItem.get("storePlatformName");
            Long countNum = (Long) orderItem.get("itemNum");

            String key = groupMerchantId + "_" + groupCustomerId + "_" + groupShopCode + "_"
                    + groupShopTypeCode + "_" + groupStorePlatformCode;

            Map<String, Object> merchantParams = new HashMap<String, Object>();
            merchantParams.put("merchantId", groupMerchantId);
            MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParams);

            //统计”月份+客户+店铺id+运营类型+平台“维度下所有的事业部集合
            OrderDTO groupOrderDto = new OrderDTO();
            groupOrderDto.setMerchantId(groupMerchantId);
            groupOrderDto.setCustomerId(groupCustomerId);
            groupOrderDto.setShopCode(groupShopCode);
            groupOrderDto.setShopTypeCode(groupShopTypeCode);
            groupOrderDto.setStorePlatformName(groupStorePlatformCode);
            groupOrderDto.setMonth(month);
            List<Long> buIds = orderItemDao.listBuByOrder(groupOrderDto);

            List<Long> returnBuIds = new ArrayList<>();
            Long returnCountNum = 0L;
            Map<String, Object> returnItem = existKeyMap.get(key);
            OrderReturnIdepotDTO groupReturnIdepotDTO = new OrderReturnIdepotDTO();
            if (returnItem != null) {
                returnCountNum = (Long) returnItem.get("itemNum");
                groupReturnIdepotDTO.setMerchantId(groupMerchantId);
                groupReturnIdepotDTO.setCustomerId(groupCustomerId);
                groupReturnIdepotDTO.setShopCode(groupShopCode);
                groupReturnIdepotDTO.setShopTypeCode(groupShopTypeCode);
                groupReturnIdepotDTO.setStorePlatformCode(groupStorePlatformCode);
                groupReturnIdepotDTO.setMonth(month);
                returnBuIds = orderReturnIdepotItemDao.listBuByOrder(groupReturnIdepotDTO);
            }
            existKeyMap.remove(key);

            Map<Long, Long> buRelTempMap = new HashMap<>();

            // // 如果参数中有事业部，则过滤掉不等于参数中事业部的数据
            if(alreadyModel.getBuId() != null) {
                buIds = buIds.stream().filter(entity -> {
                    return entity.equals(alreadyModel.getBuId());
                }).collect(Collectors.toList());

                returnBuIds = returnBuIds.stream().filter(entity -> {
                    return entity.equals(alreadyModel.getBuId());
                }).collect(Collectors.toList());
            }

            // 保存表头
            for (Long itemBuId : buIds) {
                if (returnBuIds.contains(itemBuId)) {
                    returnBuIds.remove(itemBuId);
                }

                TocTemporaryReceiveBillModel temporaryReceiveBillModel = new TocTemporaryReceiveBillModel();
                temporaryReceiveBillModel.setMerchantId(groupMerchantId);
                temporaryReceiveBillModel.setMerchantName(merchantInfo.getName());
                if (StringUtils.isNotBlank(groupShopCode)) {
                    Map<String, Object> shopParams = new HashMap<String, Object>();
                    shopParams.put("shopCode", groupShopCode);
                    MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParams);
                    if(shop == null) {
                        throw new RuntimeException("商家和店铺未关联，店铺编码:" + groupShopCode);
                    }
                    temporaryReceiveBillModel.setShopCode(groupShopCode);
                    temporaryReceiveBillModel.setShopName(shop.getShopName());

                }

                if (groupCustomerId != null) {
                    Map<String, Object> customerParams = new HashMap<String, Object>();
                    customerParams.put("customerid", groupCustomerId);
                    CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(customerParams);
                    temporaryReceiveBillModel.setCustomerId(customerInfo.getCustomerid());
                    temporaryReceiveBillModel.setCustomerName(customerInfo.getName());
                }

                Map<String, Object> buParams = new HashMap<>();
                buParams.put("buId", itemBuId);
                buParams.put("merchantId", groupMerchantId);
                MerchantBuRelMongo bu = merchantBuRelMongoDao.findOne(buParams);

                temporaryReceiveBillModel.setBuId(itemBuId);
                temporaryReceiveBillModel.setBuName(bu.getBuName());
                temporaryReceiveBillModel.setShopTypeCode(groupShopTypeCode);
                temporaryReceiveBillModel.setStorePlatformCode(groupStorePlatformCode);
                temporaryReceiveBillModel.setMonth(month);
                temporaryReceiveBillModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
                temporaryReceiveBillModel.setTotalReceiveNum(0L);
                temporaryReceiveBillModel.setTotalReceiveAmount(BigDecimal.ZERO);
                temporaryReceiveBillModel.setAlreadyReceiveNum(0L);
                temporaryReceiveBillModel.setAlreadyReceiveAmount(BigDecimal.ZERO);
                temporaryReceiveBillModel.setLastReceiveAmount(BigDecimal.ZERO);
                Long id = tocTemporaryReceiveBillDao.save(temporaryReceiveBillModel);

                buRelTempMap.put(itemBuId, id);
                tocIdList.add(id);
            }

            // 保存表头
            for (Long itemBuId : returnBuIds) {

                TocTemporaryReceiveBillModel temporaryReceiveBillModel = new TocTemporaryReceiveBillModel();
                temporaryReceiveBillModel.setMerchantId(groupMerchantId);
                temporaryReceiveBillModel.setMerchantName(merchantInfo.getName());
                if (StringUtils.isNotBlank(groupShopCode)) {
                    Map<String, Object> shopParams = new HashMap<String, Object>();
                    shopParams.put("shopCode", groupShopCode);
                    MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParams);
                    if(shop == null) {
                        throw new RuntimeException("商家和店铺未关联，店铺编码:" + groupShopCode);
                    }
                    temporaryReceiveBillModel.setShopCode(groupShopCode);
                    temporaryReceiveBillModel.setShopName(shop.getShopName());

                }

                if (groupCustomerId != null) {
                    Map<String, Object> customerParams = new HashMap<String, Object>();
                    customerParams.put("customerid", groupCustomerId);
                    CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(customerParams);
                    temporaryReceiveBillModel.setCustomerId(customerInfo.getCustomerid());
                    temporaryReceiveBillModel.setCustomerName(customerInfo.getName());
                }

                Map<String, Object> buParams = new HashMap<>();
                buParams.put("buId", itemBuId);
                buParams.put("merchantId", groupMerchantId);
                MerchantBuRelMongo bu = merchantBuRelMongoDao.findOne(buParams);

                temporaryReceiveBillModel.setBuId(itemBuId);
                temporaryReceiveBillModel.setBuName(bu.getBuName());
                temporaryReceiveBillModel.setShopTypeCode(groupShopTypeCode);
                temporaryReceiveBillModel.setStorePlatformCode(groupStorePlatformCode);
                temporaryReceiveBillModel.setMonth(month);
                temporaryReceiveBillModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
                temporaryReceiveBillModel.setTotalReceiveNum(0L);
                temporaryReceiveBillModel.setTotalReceiveAmount(BigDecimal.ZERO);
                temporaryReceiveBillModel.setAlreadyReceiveNum(0L);
                temporaryReceiveBillModel.setAlreadyReceiveAmount(BigDecimal.ZERO);
                temporaryReceiveBillModel.setLastReceiveAmount(BigDecimal.ZERO);
                Long id = tocTemporaryReceiveBillDao.save(temporaryReceiveBillModel);

                buRelTempMap.put(itemBuId, id);
                tocIdList.add(id);
            }

            //电商订单生成暂估收入发货订单明细
            for (int i = 0; i < countNum.intValue(); ) {
                Map<Long, OrderDTO> orderDTOList = new HashMap<>();
                List<Long> orderIdList = new ArrayList<>();
                int pageSub = (i + pageSize) < countNum.intValue() ? (i + pageSize) : countNum.intValue();

                groupOrderDto.setBegin(i);
                groupOrderDto.setPageSize(pageSize);
                //分页查询退款单明细
                List<OrderDTO> orderDTOS = orderDao.listOrderByDTO(groupOrderDto);
                i = pageSub;
                for (OrderDTO dto : orderDTOS) {
                    orderIdList.add(dto.getId());
                    orderDTOList.put(dto.getId(), dto);
                }

                //电商订单对应表体集合
                List<OrderItemDTO> orderItemDTOS = orderItemDao.listItemByOrderDTO(orderIdList);

                //根据电商订单id集合查询每个电商订单结算金额占比最大的商品明细
                //获取externalCode + buId为维度对应的BrandParentInfo Map
                Map<String, BrandParentMongo> maxPriceBrandParentInfoMap = getParentBrandInfoByMaxPriceOrderList(orderDTOList, orderIdList, true, orderItemDao);

                //计算暂估应收金额
                Map<String, TocTemporaryReceiveBillItemModel> temporaryReceiveBillItemModelMap = new HashMap<>();
                BrandParentMongo brandParentMongo = null;
                for (OrderItemDTO itemDTO : orderItemDTOS) {
                    // 如果参数中有事业部，则过滤掉不等于参数中事业部的数据
                    if(alreadyModel.getBuId() != null && !alreadyModel.getBuId().equals(itemDTO.getBuId())) {
                        continue;
                    }

                    OrderDTO order = orderDTOList.get(itemDTO.getOrderId());
                    String tempKey = order.getExternalCode() + "_" + itemDTO.getBuId();

                    //获取母品牌信息
                    brandParentMongo = maxPriceBrandParentInfoMap.get(tempKey);
                    BigDecimal tax = itemDTO.getTax() != null ? itemDTO.getTax() : new BigDecimal("0");
                    BigDecimal wayFrtFee = itemDTO.getWayFrtFee() != null ? itemDTO.getWayFrtFee() : new BigDecimal("0");
                    BigDecimal rmbAmount = itemDTO.getDecTotal().add(tax).add(wayFrtFee);
                    if (temporaryReceiveBillItemModelMap.containsKey(tempKey)) {
                        TocTemporaryReceiveBillItemModel exist = temporaryReceiveBillItemModelMap.get(tempKey);
                        BigDecimal existPrice = exist.getTemporaryRmbAmount().add(rmbAmount);
                        exist.setTemporaryRmbAmount(existPrice);
                        exist.setSaleNum(itemDTO.getNum() + exist.getSaleNum());
                        temporaryReceiveBillItemModelMap.put(tempKey, exist);
                    } else {
                        TocTemporaryReceiveBillItemModel temporaryReceiveBillItemModel = new TocTemporaryReceiveBillItemModel();
                        Long billId = buRelTempMap.get(itemDTO.getBuId());
                        temporaryReceiveBillItemModel.setBillId(billId);
                        temporaryReceiveBillItemModel.setMonth(month);
                        temporaryReceiveBillItemModel.setExternalCode(order.getExternalCode());
                        temporaryReceiveBillItemModel.setOrderCode(order.getCode());
                        temporaryReceiveBillItemModel.setMerchantId(order.getMerchantId());
                        temporaryReceiveBillItemModel.setMerchantName(order.getMerchantName());
                        temporaryReceiveBillItemModel.setBuId(itemDTO.getBuId());
                        temporaryReceiveBillItemModel.setBuName(itemDTO.getBuName());
                        temporaryReceiveBillItemModel.setCustomerId(order.getCustomerId());
                        temporaryReceiveBillItemModel.setCustomerName(order.getCustomerName());
                        temporaryReceiveBillItemModel.setShopCode(order.getShopCode());
                        temporaryReceiveBillItemModel.setShopName(order.getShopName());
                        temporaryReceiveBillItemModel.setShopTypeCode(order.getShopTypeCode());
                        temporaryReceiveBillItemModel.setStorePlatformCode(order.getStorePlatformName());
                        temporaryReceiveBillItemModel.setSaleNum(itemDTO.getNum());
                        temporaryReceiveBillItemModel.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
                        temporaryReceiveBillItemModel.setOrderType(DERP_ORDER.TOCTEMPITEMBILL_ORDERTYPE_0);
                        temporaryReceiveBillItemModel.setTemporaryCurrency(order.getCurrency());
                        temporaryReceiveBillItemModel.setTemporaryRmbAmount(rmbAmount);
                        if(brandParentMongo != null){
                            temporaryReceiveBillItemModel.setParentBrandId(brandParentMongo.getSuperiorParentBrandId());
                            temporaryReceiveBillItemModel.setParentBrandCode(brandParentMongo.getSuperiorParentBrandCode());
                            temporaryReceiveBillItemModel.setParentBrandName(brandParentMongo.getSuperiorParentBrand());
                        }

                        temporaryReceiveBillItemModelMap.put(tempKey, temporaryReceiveBillItemModel);
                    }
                }

                List<TocTemporaryReceiveBillItemModel> addTempBillList = new ArrayList<>(temporaryReceiveBillItemModelMap.values());
                for (int j = 0; j < addTempBillList.size(); ) {
                    int itemPageSub = (j + pageSize) < addTempBillList.size() ? (j + pageSize) : addTempBillList.size();
                    tocTemporaryReceiveBillItemDao.batchSave(addTempBillList.subList(j, itemPageSub));
                    j = itemPageSub;
                }

            }

            //退款单生成暂估收入退款订单明细
            returnCountNum = orderReturnIdepotDao.statisticsDistinctByDTO(groupReturnIdepotDTO);
            if (returnCountNum == 0) {
                continue;
            }
            for (int i = 0; i < returnCountNum.intValue(); ) {
                Map<Long, OrderReturnIdepotDTO> orderDTOList = new HashMap<>();
                List<Long> orderIdList = new ArrayList<>();
                int pageSub = (i + pageSize) < returnCountNum.intValue() ? (i + pageSize) : returnCountNum.intValue();
                groupReturnIdepotDTO.setBegin(i);
                groupReturnIdepotDTO.setPageSize(pageSize);
                i = pageSub;

                List<OrderReturnIdepotDTO> returnIdepotDTOS = orderReturnIdepotDao.listDistinctOrderByDTO(groupReturnIdepotDTO);
                List<String> distinctExternalCodeList = returnIdepotDTOS.stream().map(OrderReturnIdepotDTO::getExternalCode).collect(Collectors.toList());
                groupReturnIdepotDTO.setExternalCodeList(distinctExternalCodeList);
                returnIdepotDTOS = orderReturnIdepotDao.listRefundOrderDTO(groupReturnIdepotDTO);
                if(returnIdepotDTOS == null || returnIdepotDTOS.isEmpty()) {
                    continue;
                }

                for (OrderReturnIdepotDTO dto : returnIdepotDTOS) {
                    orderIdList.add(dto.getId());
                    orderDTOList.put(dto.getId(), dto);
                }

                //电商订单对应表体集合
                List<OrderReturnIdepotItemDTO> orderReturnIdepotItemDTOS = orderReturnIdepotItemDao.listByOrderIds(orderIdList);

                //根据电商订单id集合查询每个电商订单结算金额占比最大的商品明细
                //获取externalCode + buId为维度对应的BrandParentInfo Map
                Map<String, BrandParentMongo> maxPriceBrandParentInfoMap = getParentBrandInfoByMaxPriceOrderList(orderDTOList, orderIdList, true, orderReturnIdepotItemDao);

                //计算暂估应收金额
                Map<String, TocTemporaryReceiveBillItemModel> temporaryReceiveBillItemModelMap = new HashMap<>();
                BrandParentMongo brandParentMongo = null;
                for (OrderReturnIdepotItemDTO itemDTO : orderReturnIdepotItemDTOS) {
                    // 如果参数中有事业部，则过滤掉不等于参数中事业部的数据
                    if(alreadyModel.getBuId() != null && !alreadyModel.getBuId().equals(itemDTO.getBuId())) {
                        continue;
                    }

                    OrderReturnIdepotDTO order = orderDTOList.get(itemDTO.getOreturnIdepotId());
                    String tempKey = order.getExternalCode() + "_" + itemDTO.getBuId();
                    //获取母品牌信息
                    brandParentMongo = maxPriceBrandParentInfoMap.get(tempKey);
                    if (temporaryReceiveBillItemModelMap.containsKey(tempKey)) {
                        TocTemporaryReceiveBillItemModel exist = temporaryReceiveBillItemModelMap.get(tempKey);
                        BigDecimal existPrice = exist.getTemporaryRmbAmount().subtract(itemDTO.getRefundAmount());
                        exist.setTemporaryRmbAmount(existPrice);
                        exist.setSaleNum(itemDTO.getReturnNum() + exist.getSaleNum());
                        temporaryReceiveBillItemModelMap.put(tempKey, exist);
                    } else {
                        TocTemporaryReceiveBillItemModel temporaryReceiveBillItemModel = new TocTemporaryReceiveBillItemModel();
                        Long billId = buRelTempMap.get(itemDTO.getBuId());
                        temporaryReceiveBillItemModel.setBillId(billId);
                        temporaryReceiveBillItemModel.setMonth(month);
                        temporaryReceiveBillItemModel.setExternalCode(order.getExternalCode());
                        temporaryReceiveBillItemModel.setOrderCode(order.getOrderCode());
                        temporaryReceiveBillItemModel.setMerchantId(order.getMerchantId());
                        temporaryReceiveBillItemModel.setMerchantName(order.getMerchantName());
                        temporaryReceiveBillItemModel.setBuId(itemDTO.getBuId());
                        temporaryReceiveBillItemModel.setBuName(itemDTO.getBuName());
                        temporaryReceiveBillItemModel.setCustomerId(order.getCustomerId());
                        temporaryReceiveBillItemModel.setCustomerName(order.getCustomerName());
                        temporaryReceiveBillItemModel.setShopCode(order.getShopCode());
                        temporaryReceiveBillItemModel.setShopName(order.getShopName());
                        temporaryReceiveBillItemModel.setShopTypeCode(order.getShopTypeCode());
                        temporaryReceiveBillItemModel.setStorePlatformCode(order.getStorePlatformCode());
                        temporaryReceiveBillItemModel.setSaleNum(itemDTO.getReturnNum());
                        temporaryReceiveBillItemModel.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
                        temporaryReceiveBillItemModel.setOrderType(DERP_ORDER.TOCTEMPITEMBILL_ORDERTYPE_1);
                        temporaryReceiveBillItemModel.setTemporaryCurrency(order.getCurrency());
                        temporaryReceiveBillItemModel.setTemporaryRmbAmount(itemDTO.getRefundAmount().negate());
                        if(brandParentMongo != null) {
                            temporaryReceiveBillItemModel.setParentBrandId(brandParentMongo.getSuperiorParentBrandId());
                            temporaryReceiveBillItemModel.setParentBrandCode(brandParentMongo.getSuperiorParentBrandCode());
                            temporaryReceiveBillItemModel.setParentBrandName(brandParentMongo.getSuperiorParentBrand());
                        }

                        temporaryReceiveBillItemModelMap.put(tempKey, temporaryReceiveBillItemModel);
                    }

                }

                List<TocTemporaryReceiveBillItemModel> addTempBillList = new ArrayList<>(temporaryReceiveBillItemModelMap.values());
                //相同月份 + 退款单号暂估明细进行合并
                Map<String, List<TocTemporaryReceiveBillItemModel>> collect = addTempBillList.stream().collect(Collectors.groupingBy(entity -> {
                    return entity.getExternalCode() + "_" + entity.getMonth();
                }));
                List<TocTemporaryReceiveBillItemModel> needInsertList = new ArrayList<>();
                collect.forEach((key1, value) -> {
                    BigDecimal sumTempRmbCost = value.stream().map(e -> e.getTemporaryRmbAmount() != null ? e.getTemporaryRmbAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                    Integer sumSaleNum = value.stream().mapToInt(TocTemporaryReceiveBillItemModel::getSaleNum).sum();
                    TocTemporaryReceiveBillItemModel model = value.get(0);
                    model.setTemporaryRmbAmount(sumTempRmbCost);
                    model.setSaleNum(sumSaleNum);
                    needInsertList.add(model);
                });

                for (int j = 0; j < needInsertList.size(); ) {
                    int itemPageSub = (j + pageSize) < needInsertList.size() ? (j + pageSize) : needInsertList.size();
                    tocTemporaryReceiveBillItemDao.batchSave(needInsertList.subList(j, itemPageSub));
                    j = itemPageSub;
                }

                collect.clear();
                addTempBillList.clear();
                needInsertList.clear();
                returnIdepotDTOS.clear();
                orderReturnIdepotItemDTOS.clear();
                orderDTOList.clear();
                temporaryReceiveBillItemModelMap.clear();
            }
        }

        for (String key : existKeyMap.keySet()) {
            Map<String, Object> returnItem = existKeyMap.get(key);

            Long groupMerchantId = (Long) returnItem.get("merchantId");
            Long groupCustomerId = (Long) returnItem.get("customerId");
            String groupShopCode = (String) returnItem.get("shopCode");
            String groupShopTypeCode = (String) returnItem.get("shopTypeCode");
            String groupStorePlatformCode = (String) returnItem.get("storePlatformCode");
            Long returnCountNum = (Long) returnItem.get("itemNum");

            OrderReturnIdepotDTO groupReturnIdepotDTO = new OrderReturnIdepotDTO();
            groupReturnIdepotDTO.setMerchantId(groupMerchantId);
            groupReturnIdepotDTO.setCustomerId(groupCustomerId);
            groupReturnIdepotDTO.setShopCode(groupShopCode);
            groupReturnIdepotDTO.setShopTypeCode(groupShopTypeCode);
            groupReturnIdepotDTO.setStorePlatformCode(groupStorePlatformCode);
            groupReturnIdepotDTO.setMonth(month);
            List<Long> returnBuIds = orderReturnIdepotItemDao.listBuByOrder(groupReturnIdepotDTO);

            Map<String, Object> merchantParams = new HashMap<String, Object>();
            merchantParams.put("merchantId", groupMerchantId);
            MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParams);

            Map<Long, Long> buRelTempMap = new HashMap<>();

            // 如果参数中有事业部，则过滤掉不等于参数中事业部的数据
            if(alreadyModel.getBuId() != null) {
                returnBuIds = returnBuIds.stream().filter(entity -> {
                    return entity.equals(alreadyModel.getBuId());
                }).collect(Collectors.toList());
            }


            for (Long itemBuId : returnBuIds) {
                TocTemporaryReceiveBillModel temporaryReceiveBillModel = new TocTemporaryReceiveBillModel();
                temporaryReceiveBillModel.setMerchantId(groupMerchantId);
                temporaryReceiveBillModel.setMerchantName(merchantInfo.getName());

                if (StringUtils.isNotBlank(groupShopCode)) {
                    Map<String, Object> shopParams = new HashMap<String, Object>();
                    shopParams.put("shopCode", groupShopCode);
                    MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParams);
                    if(shop == null) {
                        throw new RuntimeException("商家和店铺未关联，店铺编码:" + groupShopCode);
                    }
                    temporaryReceiveBillModel.setShopCode(groupShopCode);
                    temporaryReceiveBillModel.setShopName(shop.getShopName());
                }

                if (groupCustomerId != null) {
                    Map<String, Object> customerParams = new HashMap<String, Object>();
                    customerParams.put("customerid", groupCustomerId);
                    CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(customerParams);
                    temporaryReceiveBillModel.setCustomerId(customerInfo.getCustomerid());
                    temporaryReceiveBillModel.setCustomerName(customerInfo.getName());
                }

                Map<String, Object> buParams = new HashMap<>();
                buParams.put("buId", itemBuId);
                buParams.put("merchantId", groupMerchantId);
                MerchantBuRelMongo bu = merchantBuRelMongoDao.findOne(buParams);

                temporaryReceiveBillModel.setBuId(itemBuId);
                temporaryReceiveBillModel.setBuName(bu.getBuName());
                temporaryReceiveBillModel.setShopTypeCode(groupShopTypeCode);
                temporaryReceiveBillModel.setStorePlatformCode(groupStorePlatformCode);
                temporaryReceiveBillModel.setMonth(month);
                temporaryReceiveBillModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
                temporaryReceiveBillModel.setTotalReceiveNum(0L);
                temporaryReceiveBillModel.setTotalReceiveAmount(BigDecimal.ZERO);
                temporaryReceiveBillModel.setAlreadyReceiveNum(0L);
                temporaryReceiveBillModel.setAlreadyReceiveAmount(BigDecimal.ZERO);
                temporaryReceiveBillModel.setLastReceiveAmount(BigDecimal.ZERO);
                Long id = tocTemporaryReceiveBillDao.save(temporaryReceiveBillModel);

                buRelTempMap.put(itemBuId, id);
                tocIdList.add(id);
            }

            //退款单生成暂估收入退款订单明细
            returnCountNum = orderReturnIdepotDao.statisticsDistinctByDTO(groupReturnIdepotDTO);
            if (returnCountNum == 0) {
                continue;
            }
            for (int i = 0; i < returnCountNum.intValue(); ) {
                Map<Long, OrderReturnIdepotDTO> orderDTOList = new HashMap<>();
                List<Long> orderIdList = new ArrayList<>();
                int pageSub = (i + pageSize) < returnCountNum.intValue() ? (i + pageSize) : returnCountNum.intValue();
                groupReturnIdepotDTO.setBegin(i);
                groupReturnIdepotDTO.setPageSize(pageSize);
                i = pageSub;

                List<OrderReturnIdepotDTO> returnIdepotDTOS = orderReturnIdepotDao.listDistinctOrderByDTO(groupReturnIdepotDTO);
                List<String> distinctExternalCodeList = returnIdepotDTOS.stream().map(OrderReturnIdepotDTO::getExternalCode).collect(Collectors.toList());
                groupReturnIdepotDTO.setExternalCodeList(distinctExternalCodeList);
                returnIdepotDTOS = orderReturnIdepotDao.listRefundOrderDTO(groupReturnIdepotDTO);
                if(returnIdepotDTOS == null || returnIdepotDTOS.isEmpty()) {
                    continue;
                }

                for (OrderReturnIdepotDTO dto : returnIdepotDTOS) {
                    orderIdList.add(dto.getId());
                    orderDTOList.put(dto.getId(), dto);
                }

                //电商订单对应表体集合
                List<OrderReturnIdepotItemDTO> orderReturnIdepotItemDTOS = orderReturnIdepotItemDao.listByOrderIds(orderIdList);

                //根据电商订单id集合查询每个电商订单结算金额占比最大的商品明细
                //获取externalCode + buId为维度对应的BrandParentInfo Map
                Map<String, BrandParentMongo> maxPriceBrandParentInfoMap = getParentBrandInfoByMaxPriceOrderList(orderDTOList, orderIdList, true, orderReturnIdepotItemDao);

                //计算暂估应收金额
                Map<String, TocTemporaryReceiveBillItemModel> temporaryReceiveBillItemModelMap = new HashMap<>();
                BrandParentMongo brandParentMongo = null;
                for (OrderReturnIdepotItemDTO itemDTO : orderReturnIdepotItemDTOS) {
                    // 如果参数中有事业部，则过滤掉不等于参数中事业部的数据
                    if(alreadyModel.getBuId() != null && !alreadyModel.getBuId().equals(itemDTO.getBuId())) {
                        continue;
                    }

                    OrderReturnIdepotDTO order = orderDTOList.get(itemDTO.getOreturnIdepotId());
                    String tempKey = order.getExternalCode() + "_" + itemDTO.getBuId();
                    //获取母品牌信息
                    brandParentMongo = maxPriceBrandParentInfoMap.get(tempKey);

                    if (temporaryReceiveBillItemModelMap.containsKey(tempKey)) {
                        TocTemporaryReceiveBillItemModel exist = temporaryReceiveBillItemModelMap.get(tempKey);
                        BigDecimal existPrice = exist.getTemporaryRmbAmount().subtract(itemDTO.getRefundAmount());
                        exist.setTemporaryRmbAmount(existPrice);
                        exist.setSaleNum(itemDTO.getReturnNum() + exist.getSaleNum());
                        temporaryReceiveBillItemModelMap.put(tempKey, exist);
                    } else {
                        TocTemporaryReceiveBillItemModel temporaryReceiveBillItemModel = new TocTemporaryReceiveBillItemModel();
                        Long billId = buRelTempMap.get(itemDTO.getBuId());
                        temporaryReceiveBillItemModel.setBillId(billId);
                        temporaryReceiveBillItemModel.setMonth(month);
                        temporaryReceiveBillItemModel.setExternalCode(order.getExternalCode());
                        temporaryReceiveBillItemModel.setOrderCode(order.getOrderCode());
                        temporaryReceiveBillItemModel.setMerchantId(order.getMerchantId());
                        temporaryReceiveBillItemModel.setMerchantName(order.getMerchantName());
                        temporaryReceiveBillItemModel.setBuId(itemDTO.getBuId());
                        temporaryReceiveBillItemModel.setBuName(itemDTO.getBuName());
                        temporaryReceiveBillItemModel.setCustomerId(order.getCustomerId());
                        temporaryReceiveBillItemModel.setCustomerName(order.getCustomerName());
                        temporaryReceiveBillItemModel.setShopCode(order.getShopCode());
                        temporaryReceiveBillItemModel.setShopName(order.getShopName());
                        temporaryReceiveBillItemModel.setShopTypeCode(order.getShopTypeCode());
                        temporaryReceiveBillItemModel.setStorePlatformCode(order.getStorePlatformCode());
                        temporaryReceiveBillItemModel.setSaleNum(itemDTO.getReturnNum());
                        temporaryReceiveBillItemModel.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
                        temporaryReceiveBillItemModel.setOrderType(DERP_ORDER.TOCTEMPITEMBILL_ORDERTYPE_1);
                        temporaryReceiveBillItemModel.setTemporaryCurrency(order.getCurrency());
                        temporaryReceiveBillItemModel.setTemporaryRmbAmount(itemDTO.getRefundAmount().negate());
                        if(brandParentMongo != null) {
                            temporaryReceiveBillItemModel.setParentBrandId(brandParentMongo.getSuperiorParentBrandId());
                            temporaryReceiveBillItemModel.setParentBrandCode(brandParentMongo.getSuperiorParentBrandCode());
                            temporaryReceiveBillItemModel.setParentBrandName(brandParentMongo.getSuperiorParentBrand());
                        }

                        temporaryReceiveBillItemModelMap.put(tempKey, temporaryReceiveBillItemModel);
                    }

                }

                List<TocTemporaryReceiveBillItemModel> addTempBillList = new ArrayList<>(temporaryReceiveBillItemModelMap.values());
                //相同月份 + 退款单号暂估明细进行合并
                Map<String, List<TocTemporaryReceiveBillItemModel>> collect = addTempBillList.stream().collect(Collectors.groupingBy(entity -> {
                    return entity.getExternalCode() + "_" + entity.getMonth();
                }));
                List<TocTemporaryReceiveBillItemModel> needInsertList = new ArrayList<>();
                collect.forEach((key1, value) -> {
                    BigDecimal sumTempRmbCost = value.stream().map(e -> e.getTemporaryRmbAmount() != null ? e.getTemporaryRmbAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                    Integer sumSaleNum = value.stream().mapToInt(TocTemporaryReceiveBillItemModel::getSaleNum).sum();
                    TocTemporaryReceiveBillItemModel model = value.get(0);
                    model.setTemporaryRmbAmount(sumTempRmbCost);
                    model.setSaleNum(sumSaleNum);
                    needInsertList.add(model);
                });

                for (int j = 0; j < needInsertList.size(); ) {
                    int itemPageSub = (j + pageSize) < needInsertList.size() ? (j + pageSize) : needInsertList.size();
                    tocTemporaryReceiveBillItemDao.batchSave(needInsertList.subList(j, itemPageSub));
                    j = itemPageSub;
                }

                collect.clear();
                addTempBillList.clear();
                needInsertList.clear();
                returnIdepotDTOS.clear();
                orderReturnIdepotItemDTOS.clear();
                orderDTOList.clear();
                temporaryReceiveBillItemModelMap.clear();
            }
        }

        // 清空TOC结算明细中绑定的暂估
        if(delBillList != null && !delBillList.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("tempBillIdList", delBillList);
            tocSettlementReceiveBillItemDao.batchCleanTempBillId(params);
        }
        delBillList.clear();

        //指定id集合查询对应的订单数量和总金额
        if (!tocIdList.isEmpty()) {
            List<Map<String, Object>> settleMapList = tocTemporaryReceiveBillItemDao.countByBillIds(tocIdList);
            for (Map<String, Object> settleMap : settleMapList) {
                Long billId = (Long) settleMap.get("billId");
                BigDecimal temporaryRmbAmount = (BigDecimal) settleMap.get("temporaryRmbAmount");
                Long totalNum = (Long) settleMap.get("totalNum");
                BigDecimal noSettlementAmount = (BigDecimal) settleMap.get("noSettlementAmount");
                Long noSettlementNum = (Long) settleMap.get("noSettlementNum");
                BigDecimal alreadyReceiveAmount = temporaryRmbAmount.subtract(noSettlementAmount);

                TocTemporaryReceiveBillModel billModel = new TocTemporaryReceiveBillModel();
                billModel.setId(billId);
                billModel.setTotalReceiveAmount(temporaryRmbAmount);
                billModel.setTotalReceiveNum(totalNum);
                billModel.setLastReceiveAmount(noSettlementAmount);
                billModel.setAlreadyReceiveAmount(alreadyReceiveAmount);
                billModel.setAlreadyReceiveNum(totalNum - noSettlementNum);

                tocTemporaryReceiveBillDao.modify(billModel);
            }
        }

    }

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201169311, model = DERP_LOG_POINT.POINT_13201169311_Label)
    public void saveToCCostTemporaryBill(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String month = jsonData.getString("month");

        //指定公司、运营类型、店铺、客户、平台、事业部触发
        Integer merchantId = (Integer) jsonData.get("merchantId");
        Integer buId = (Integer) jsonData.get("buId");
        Integer customerId = (Integer) jsonData.get("customerId");
        String shopCode = jsonData.get("shopCode") == null ? "" : (String) jsonData.get("shopCode");
        String shopTypeCode = (String) jsonData.get("shopTypeCode");
        String storePlatformCode = (String) jsonData.get("storePlatformCode");

        //是否是天猫平台
        boolean isTM = false;
//        if (StringUtils.isNotBlank(storePlatformCode) && "1000000310".equals(storePlatformCode)) {
//            isTM = true;
//        }

        //对于天猫平台 (暂时封闭此段天猫生成ToC暂估费用逻辑)
        if(isTM) {
//        if (StringUtils.isBlank(storePlatformCode) || isTM) {
            Map<String, Object> reptileConfigParams = new HashMap<>();
            reptileConfigParams.put("platformType", "4");
            reptileConfigParams.put("merchantId", Long.valueOf(merchantId));
            List<ReptileConfigMongo> reptileConfigs = reptileConfigMongoDao.findAll(reptileConfigParams);

            List<ReptileConfigMongo> existConfigs = new ArrayList<>();

            Map<Long, MerchantShopRelMongo> reptileConfigShopMap = new HashMap<>();

            for (ReptileConfigMongo reptileConfig : reptileConfigs) {
                if (StringUtils.isNotBlank(reptileConfig.getShopCode())) {

                    Map<String, Object> shopParams = new HashMap<String, Object>();
                    shopParams.put("shopCode", reptileConfig.getShopCode());
                    MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParams);

                    if (shop.getBuId() == null || StringUtils.isBlank(shop.getShopCode())
                            || StringUtils.isBlank(shop.getShopTypeCode())
                            || shop.getCustomerId() == null) {
                        continue;
                    }

                    if (StringUtils.isNotBlank(shopCode) && shopCode.equals(reptileConfig.getShopCode())) {
                        existConfigs.add(reptileConfig);
                        reptileConfigShopMap.put(reptileConfig.getReptileId(), shop);
                        break;
                    }

                    existConfigs.add(reptileConfig);
                    reptileConfigShopMap.put(reptileConfig.getReptileId(), shop);
                }
            }

            if (!existConfigs.isEmpty()) {
                generateTMCostBill(existConfigs, reptileConfigShopMap, Long.valueOf(merchantId), month);
            }

        }


        //对于非天猫平台
        if (StringUtils.isBlank(storePlatformCode) || !isTM) {
            PlatformTemporaryCostOrderDTO costOrderDTO = new PlatformTemporaryCostOrderDTO();
            if (merchantId != null) {
                costOrderDTO.setMerchantId(Long.valueOf(merchantId));
            }

            if (customerId != null) {
                costOrderDTO.setCustomerId(Long.valueOf(customerId));
            }

            if (buId != null) {
                costOrderDTO.setBuId(Long.valueOf(buId));
            }

            costOrderDTO.setShopCode(shopCode);
            costOrderDTO.setShopTypeCode(shopTypeCode);
            costOrderDTO.setStorePlatformName(storePlatformCode);
            costOrderDTO.setMonth(month);
            generateCostBill(costOrderDTO);

        }

    }


    //天猫平台暂估费用生成
    private void generateTMCostBill(List<ReptileConfigMongo> reptileConfigs, Map<Long, MerchantShopRelMongo> reptileConfigShopMap,
                                    Long merchantId, String month) throws SQLException {

        PlatformSettlementConfigModel queryConfigModel = new PlatformSettlementConfigModel() ;
        queryConfigModel.setStorePlatformCode("1000000310");
        List<PlatformSettlementConfigModel> configList = platformSettlementConfigDao.list(queryConfigModel);

        List<Long> tocIdList = new ArrayList<>();

        for (ReptileConfigMongo reptileConfig : reptileConfigs) {
            MerchantShopRelMongo shop = reptileConfigShopMap.get(reptileConfig.getReptileId());

            AlipayMonthlyFeeModel alipayMonthlyFeeModel = new AlipayMonthlyFeeModel();
            alipayMonthlyFeeModel.setMerchantId(merchantId);
            alipayMonthlyFeeModel.setUserCode(reptileConfig.getLoginName());
            alipayMonthlyFeeModel.setSettleYearMonth(month.replace("-", ""));

            Integer countNum = alipayMonthlyFeeDao.statisticsByModel(alipayMonthlyFeeModel);

            if (countNum == 0) {
                continue;
            }

            TocTemporaryCostBillModel alreadyModel = new TocTemporaryCostBillModel();
            alreadyModel.setMonth(month);
            alreadyModel.setMerchantId(reptileConfig.getMerchantId());
            alreadyModel.setCustomerId(shop.getCustomerId());
            alreadyModel.setBuId(shop.getBuId());
            alreadyModel.setShopCode(shop.getShopCode());
            alreadyModel.setShopTypeCode(shop.getShopTypeCode());
            alreadyModel.setStorePlatformCode(shop.getStorePlatformCode());
            tocTemporaryCostBillDao.deleteByModel(alreadyModel);

            TocTemporaryReceiveBillCostItemModel alreadyItemModel = new TocTemporaryReceiveBillCostItemModel();
            alreadyItemModel.setMerchantId(reptileConfig.getMerchantId());
            alreadyItemModel.setCustomerId(shop.getCustomerId());
            alreadyItemModel.setBuId(shop.getBuId());
            alreadyItemModel.setShopTypeCode(shop.getShopTypeCode());
            alreadyItemModel.setShopCode(shop.getShopCode());
            alreadyItemModel.setStorePlatformCode(shop.getStorePlatformCode());
            alreadyItemModel.setMonth(month);
            tocTemporaryReceiveBillCostItemDao.deleteByModel(alreadyItemModel);

            //生成toc暂估费用表头
            TocTemporaryCostBillModel costBillModel = new TocTemporaryCostBillModel();
            costBillModel.setMonth(month);
            costBillModel.setMerchantId(reptileConfig.getMerchantId());
            costBillModel.setMerchantName(reptileConfig.getMerchantName());
            costBillModel.setCustomerName(shop.getCustomerName());
            costBillModel.setCustomerId(shop.getCustomerId());
            costBillModel.setBuId(shop.getBuId());
            costBillModel.setBuName(shop.getBuName());
            costBillModel.setShopTypeCode(shop.getShopTypeCode());
            costBillModel.setStorePlatformCode(shop.getStorePlatformCode());
            costBillModel.setShopCode(shop.getShopCode());
            costBillModel.setShopName(shop.getShopName());
            costBillModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
            costBillModel.setTotalReceiveNum(0L);
            costBillModel.setTotalReceiveAmount(BigDecimal.ZERO);
            costBillModel.setAlreadyReceiveNum(0L);
            costBillModel.setAlreadyReceiveAmount(BigDecimal.ZERO);
            Long id = tocTemporaryCostBillDao.save(costBillModel);
            tocIdList.add(id);

            Integer pageSize = 2000;
            for (int i = 0; i < countNum; ) {
                List<TocTemporaryReceiveBillCostItemModel> addCostTempList = new ArrayList<>();

                int pageSub = (i + pageSize) < countNum ? (i + pageSize) : countNum;
                alipayMonthlyFeeModel.setBegin(i);
                alipayMonthlyFeeModel.setPageSize(pageSize);

                AlipayMonthlyFeeModel alipayMonthlyFee = alipayMonthlyFeeDao.searchByPage(alipayMonthlyFeeModel);

                List<AlipayMonthlyFeeModel> alipayMonthlyFeeModels = alipayMonthlyFee.getList();

                for (AlipayMonthlyFeeModel feeModel : alipayMonthlyFeeModels) {

                    PlatformSettlementConfigModel config = null;
                    /**取对应费项配置*/
                    for (PlatformSettlementConfigModel configModel : configList) {
                        if(feeModel.getFeeDesc().indexOf(configModel.getName()) > -1) {
                            config = configModel;
                            break ;
                        }
                    }
                    if (config == null) {
                        throw new RuntimeException("平台费项配置：" + feeModel.getFeeDesc() + "不存在");
                    }

                    TocTemporaryReceiveBillCostItemModel costItemModel = new TocTemporaryReceiveBillCostItemModel();

                    costItemModel.setBillId(id);
                    costItemModel.setMonth(month);
                    costItemModel.setExternalCode(feeModel.getPartnerTransactionId());
                    costItemModel.setMerchantId(feeModel.getMerchantId());
                    costItemModel.setMerchantName(feeModel.getMerchantName());
                    costItemModel.setBuId(shop.getBuId());
                    costItemModel.setBuName(shop.getBuName());
                    costItemModel.setCustomerId(shop.getCustomerId());
                    costItemModel.setCustomerName(shop.getCustomerName());
                    costItemModel.setShopCode(shop.getShopCode());
                    costItemModel.setShopName(shop.getShopName());
                    costItemModel.setShopTypeCode(shop.getShopTypeCode());
                    costItemModel.setStorePlatformCode("1000000310");
                    costItemModel.setSettlementMark(DERP_ORDER.TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_0);
                    costItemModel.setTemporaryCurrency(feeModel.getCurrency());
                    costItemModel.setPlatformProjectId(config.getId());
                    costItemModel.setPlatformProjectName(config.getName());
                    costItemModel.setProjectId(config.getSettlementConfigId());
                    costItemModel.setProjectName(config.getSettlementConfigName());
                    costItemModel.setTemporaryRmbCost(feeModel.getFeeAmount());
                    costItemModel.setOrderType(DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_1);
                    costItemModel.setTemporaryCurrency(DERP.CURRENCYCODE_CNY);
                    costItemModel.setParentBrandName(shop.getSuperiorParentBrandName());
                    costItemModel.setParentBrandCode(shop.getSuperiorParentBrandNcCode());
                    costItemModel.setParentBrandId(shop.getSuperiorParentBrandId());
                    if (feeModel.getFeeAmount().compareTo(new BigDecimal("0")) == -1) {
                        costItemModel.setOrderType(DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_0);
                    }

                    addCostTempList.add(costItemModel);
                }

                i = pageSub;

                //费用明细批量插入
                if (addCostTempList.size() > 0 ) {
                    tocTemporaryReceiveBillCostItemDao.batchSave(addCostTempList);
                }

            }

            // 通过externalCode + platformProjectId + orderType分组查询并合并金额
            TocTemporaryReceiveBillCostItemModel queryItemModel = new TocTemporaryReceiveBillCostItemModel();
            queryItemModel.setBillId(id);
            queryItemModel.setMerchantId(merchantId);
            queryItemModel.setMonth(month);
            queryItemModel.setBuId(shop.getBuId());
            queryItemModel.setStorePlatformCode("1000000310");
            List<TocTemporaryReceiveBillCostItemDTO> sumItemList = tocTemporaryReceiveBillCostItemDao.sumCostGroupByModel(queryItemModel);

            // 获取需要删除的表体Id集合
            if(sumItemList != null && !sumItemList.isEmpty()) {
                List<List<String>> collect = sumItemList.stream()
                        .map(e -> {
                            String ids = e.getIds();
                            String[] split = ids.split(",");

                            List<String> strings = Arrays.asList(split);
                            String firstId = strings.stream().findFirst().get();
                            e.setId(Long.valueOf(firstId));
                            List<String> delIdList = strings.subList(1, strings.size());
                            return delIdList;
                        }).filter(sublist -> !sublist.isEmpty()).collect(Collectors.toList());

                // 更新表体的temporary_rmb_cost金额
                List<TocTemporaryReceiveBillCostItemModel> updateModelList = sumItemList.stream().map(e -> {
                    TocTemporaryReceiveBillCostItemModel model = new TocTemporaryReceiveBillCostItemModel();
                    model.setId(e.getId());
                    model.setTemporaryRmbCost(e.getTemporaryRmbCost());
                    return model;
                }).collect(Collectors.toList());

                if(updateModelList != null && !updateModelList.isEmpty()) {
                    tocTemporaryReceiveBillCostItemDao.batchUpdate(updateModelList);
                }

                // 删除多余的表体
                List<Long> needDelIdList = new LinkedList<>();
                for (List<String> idList : collect) {
                    if(idList != null && !idList.isEmpty()) {
                        needDelIdList.addAll(
                                idList.stream().map(e -> Long.parseLong(e)).collect(Collectors.toList())
                        );
                    }
                }
                if(needDelIdList != null && !needDelIdList.isEmpty()) {
                    tocTemporaryReceiveBillCostItemDao.delete(needDelIdList);
                }
            }
        }

        //指定id集合查询对应的订单数量和总金额
        if (!tocIdList.isEmpty()) {
            List<Map<String, Object>> settleMapList = tocTemporaryReceiveBillCostItemDao.countByBillIds(tocIdList);
            for (Map<String, Object> settleMap : settleMapList) {
                Long billId = (Long) settleMap.get("billId");
                BigDecimal temporaryRmbAmount = (BigDecimal) settleMap.get("temporaryRmbAmount");
                Long totalNum = (Long) settleMap.get("totalNum");
                BigDecimal noSettlementAmount = (BigDecimal) settleMap.get("noSettlementAmount");
                Long noSettlementNum = (Long) settleMap.get("noSettlementNum");
                BigDecimal alreadyReceiveAmount = temporaryRmbAmount.subtract(noSettlementAmount);

                TocTemporaryCostBillModel billModel = new TocTemporaryCostBillModel();
                billModel.setId(billId);
                billModel.setTotalReceiveAmount(temporaryRmbAmount);
                billModel.setTotalReceiveNum(totalNum);
                billModel.setAlreadyReceiveAmount(alreadyReceiveAmount);
                billModel.setAlreadyReceiveNum(totalNum - noSettlementNum);

                tocTemporaryCostBillDao.modify(billModel);
            }
        }
    }

    //非天猫平台暂估费用生成
    private void generateCostBill(PlatformTemporaryCostOrderDTO temporaryCostOrderDTO) throws SQLException {

        List<Long> tocIdList = new ArrayList<>();

        //”月份+客户+店铺id+运营类型+平台 + 事业部“为维度统计平台暂估费用单的数量
        List<Map<String, Object>> tempCostOrderList = platformTemporaryCostOrderDao.countOrderByDTO(temporaryCostOrderDTO);

        for (Map<String, Object> tempCostMap : tempCostOrderList) {
            Long merchantId = (Long) tempCostMap.get("merchantId");
            Long customerId = (Long) tempCostMap.get("customerId");
            Long buId = (Long) tempCostMap.get("buId");
            String shopCode = (String) tempCostMap.get("shopCode");
            String shopTypeCode = (String) tempCostMap.get("shopTypeCode");
            String storePlatformCode = (String) tempCostMap.get("storePlatformCode");
//            Long countNum = (Long) tempCostMap.get("itemNum");

            TocTemporaryCostBillModel alreadyModel = new TocTemporaryCostBillModel();
            alreadyModel.setMonth(temporaryCostOrderDTO.getMonth());
            alreadyModel.setMerchantId(merchantId);
            alreadyModel.setCustomerId(customerId);
            alreadyModel.setBuId(buId);
            alreadyModel.setShopCode(shopCode);
            alreadyModel.setShopTypeCode(shopTypeCode);
            alreadyModel.setStorePlatformCode(storePlatformCode);
            List<Long> delBillList = tocTemporaryCostBillDao.searchIdListByModel(alreadyModel);
//            tocTemporaryCostBillDao.deleteByModel(alreadyModel);
            if(delBillList != null && !delBillList.isEmpty()) {
                tocTemporaryCostBillDao.delete(delBillList);
            }

            TocTemporaryReceiveBillCostItemModel alreadyItemModel = new TocTemporaryReceiveBillCostItemModel();
            alreadyItemModel.setMerchantId(merchantId);
            alreadyItemModel.setCustomerId(customerId);
            alreadyItemModel.setBuId(buId);
            alreadyItemModel.setShopCode(shopCode);
            alreadyItemModel.setShopTypeCode(shopTypeCode);
            alreadyItemModel.setStorePlatformCode(storePlatformCode);
            alreadyItemModel.setMonth(temporaryCostOrderDTO.getMonth());
            tocTemporaryReceiveBillCostItemDao.deleteByModel(alreadyItemModel);
//            tocTemporaryReceiveBillCostItemDao.deletebyModelExcludeAdjustment(alreadyItemModel);


            Map<String, Object> merchantParams = new HashMap<String, Object>();
            merchantParams.put("merchantId", merchantId);
            MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParams);

            Map<String, Object> buParams = new HashMap<>();
            buParams.put("buId", buId);
            buParams.put("merchantId", merchantId);
            MerchantBuRelMongo bu = merchantBuRelMongoDao.findOne(buParams);

            Map<String, Object> shopParams = new HashMap<String, Object>();
            shopParams.put("shopCode", shopCode);
            MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParams);

            Map<String, Object> customerParams = new HashMap<String, Object>();
            customerParams.put("customerid", customerId);
            CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(customerParams);

            PlatformTemporaryCostOrderDTO costOrderDTO = new PlatformTemporaryCostOrderDTO();
            costOrderDTO.setMerchantId(merchantId);
            costOrderDTO.setMerchantName(merchantInfo.getName());
            costOrderDTO.setCustomerId(customerId);
            costOrderDTO.setCustomerName(customerInfo.getName());
            costOrderDTO.setBuId(buId);
            costOrderDTO.setBuName(bu.getBuName());
            costOrderDTO.setShopCode(shopCode);
            costOrderDTO.setShopName(shop.getShopName());
            costOrderDTO.setShopTypeCode(shopTypeCode);
            costOrderDTO.setStorePlatformCode(storePlatformCode);
            costOrderDTO.setMonth(temporaryCostOrderDTO.getMonth());

            //生成toc暂估费用表头
            TocTemporaryCostBillModel costBillModel = new TocTemporaryCostBillModel();
            costBillModel.setMonth(temporaryCostOrderDTO.getMonth());
            costBillModel.setMerchantId(merchantId);
            costBillModel.setMerchantName(merchantInfo.getName());
            costBillModel.setBuId(buId);
            costBillModel.setBuName(bu.getBuName());
            costBillModel.setShopTypeCode(shopTypeCode);
            costBillModel.setStorePlatformCode(storePlatformCode);
            costBillModel.setCustomerId(customerId);
            costBillModel.setCustomerName(customerInfo.getName());
            costBillModel.setShopCode(shopCode);
            costBillModel.setShopName(shop.getShopName());
            costBillModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
            costBillModel.setTotalReceiveNum(0L);
            costBillModel.setTotalReceiveAmount(BigDecimal.ZERO);
            costBillModel.setAlreadyReceiveNum(0L);
            costBillModel.setAlreadyReceiveAmount(BigDecimal.ZERO);
            Long id = tocTemporaryCostBillDao.save(costBillModel);
            tocIdList.add(id);

            Long countNum = platformTemporaryCostOrderDao.statisticsDistinctByDTO(costOrderDTO);
            if (countNum == 0) {
                continue;
            }

            Integer pageSize = 1000;
            Map<Long, PlatformSettlementConfigModel> platformSettlementConfigModelMap = new HashMap<>();

            for (int i = 0; i < countNum.intValue(); ) {
                List<TocTemporaryReceiveBillCostItemModel> addCostTempList = new ArrayList<>();

                List<Long> platformIds = new ArrayList<>();
                int pageSub = (i + pageSize) < countNum.intValue() ? (i + pageSize) : countNum.intValue();

                costOrderDTO.setBegin(i);
                costOrderDTO.setPageSize(pageSize);

                i = pageSub;

                List<PlatformTemporaryCostOrderDTO> distinctOrderList = platformTemporaryCostOrderDao.listDistinctOrderByDTO(costOrderDTO);
                if(distinctOrderList == null || distinctOrderList.isEmpty()) {
                    continue;
                }
                // 获取Distinct orderCode list
                List<String> externalCodeList = distinctOrderList.stream().map(PlatformTemporaryCostOrderDTO::getExternalCode).collect(Collectors.toList());
                costOrderDTO.setExternalCodeList(externalCodeList);

                List<PlatformTemporaryCostOrderDTO> temporaryCostOrderDTOS = platformTemporaryCostOrderDao.listTempOrderDTO(costOrderDTO);
                if(temporaryCostOrderDTOS == null || temporaryCostOrderDTOS.isEmpty()) {
                    continue;
                }

                Map<Long, PlatformTemporaryCostOrderDTO> orderTypeAndOrderIdMap = new HashMap<>();
                for (PlatformTemporaryCostOrderDTO platformTemporaryCostOrder : temporaryCostOrderDTOS) {
                    platformIds.add(platformTemporaryCostOrder.getId());
                    if (DERP_ORDER.PLATFORMTEMPCOSTORDER_ORDERTYPE_1.equals(platformTemporaryCostOrder.getOrderType())) {
                        orderTypeAndOrderIdMap.put(platformTemporaryCostOrder.getId(), platformTemporaryCostOrder);
                    }
                }

                List<PlatformTemporaryCostOrderItemModel> itemModels = platformTemporaryCostOrderItemDao.listItemByPlatformIds(platformIds);

                for (PlatformTemporaryCostOrderItemModel itemModel : itemModels) {
                    TocTemporaryReceiveBillCostItemModel costItemModel = new TocTemporaryReceiveBillCostItemModel();

                    costItemModel.setBillId(id);
                    costItemModel.setMonth(temporaryCostOrderDTO.getMonth());
                    costItemModel.setExternalCode(itemModel.getExternalCode());
                    costItemModel.setOrderCode(itemModel.getOrderCode());
                    costItemModel.setMerchantId(merchantId);
                    costItemModel.setMerchantName(merchantInfo.getName());
                    costItemModel.setBuId(buId);
                    costItemModel.setBuName(bu.getBuName());
                    costItemModel.setCustomerId(customerId);
                    costItemModel.setCustomerName(customerInfo.getName());
                    costItemModel.setShopCode(shop.getShopCode());
                    costItemModel.setShopName(shop.getShopName());
                    costItemModel.setShopTypeCode(shop.getShopTypeCode());
                    costItemModel.setStorePlatformCode(storePlatformCode);
                    costItemModel.setSettlementMark(DERP_ORDER.TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_0);
                    costItemModel.setTemporaryCurrency(temporaryCostOrderDTOS.get(0).getCurrency());
                    costItemModel.setPlatformProjectId(itemModel.getPlatformSettlementId());
                    costItemModel.setPlatformProjectName(itemModel.getPlatformSettlementName());
                    costItemModel.setParentBrandId(itemModel.getParentBrandId());
                    costItemModel.setParentBrandCode(itemModel.getParentBrandCode());
                    costItemModel.setParentBrandName(itemModel.getParentBrandName());
                    costItemModel.setOrderType(DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_1);

                    PlatformTemporaryCostOrderDTO platformTemporaryCostOrderDTO = orderTypeAndOrderIdMap.get(itemModel.getPlatformCostId());
                    if (platformTemporaryCostOrderDTO != null) {
                        costItemModel.setOrderCode(platformTemporaryCostOrderDTO.getOrderCode());
                        costItemModel.setOrderType(DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_0);
                    }

                    PlatformSettlementConfigModel settlementConfigModel = platformSettlementConfigModelMap.get(itemModel.getPlatformSettlementId());

                    if (settlementConfigModel == null) {
                        settlementConfigModel = platformSettlementConfigDao.searchById(itemModel.getPlatformSettlementId());
                    }

                    costItemModel.setProjectId(settlementConfigModel.getSettlementConfigId());
                    costItemModel.setProjectName(settlementConfigModel.getSettlementConfigName());
                    costItemModel.setTemporaryRmbCost(itemModel.getSettlementAmount());

                    if (itemModel.getSettlementAmount().compareTo(new BigDecimal("0")) == -1) {
                        costItemModel.setOrderType(DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_0);
                    }

                    addCostTempList.add(costItemModel);
                }

                // 以同月份+同单据类型+同订单+同事业部+同费项 合并暂估金额
                Map<String, List<TocTemporaryReceiveBillCostItemModel>> collect = addCostTempList.stream().collect(Collectors.groupingBy(entity -> {
                    return entity.getMonth() + "_" + entity.getOrderType() + "_" + entity.getExternalCode() + "_" + entity.getBuId() + "_" + entity.getPlatformProjectId();
                }));
                List<TocTemporaryReceiveBillCostItemModel> needInsertList = new ArrayList<>();
                collect.forEach((key, value) -> {
                    BigDecimal sumTempRmbCost = value.stream().map(e -> e.getTemporaryRmbCost() != null ? e.getTemporaryRmbCost() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                    TocTemporaryReceiveBillCostItemModel model = value.get(0);
                    model.setTemporaryRmbCost(sumTempRmbCost);
                    needInsertList.add(model);
                });

                //费用明细批量插入
                if (needInsertList.size() > 0 ) {
                    for (int j = 0; j < needInsertList.size(); ) {
                        int itemPageSub = (j + pageSize) < needInsertList.size() ? (j + pageSize) : needInsertList.size();
                        tocTemporaryReceiveBillCostItemDao.batchSave(needInsertList.subList(j, itemPageSub));
                        j = itemPageSub;
                    }
//                    tocTemporaryReceiveBillCostItemDao.batchSave(addCostTempList);
                }
                collect.clear();
                needInsertList.clear();
                addCostTempList.clear();
                distinctOrderList.clear();
                externalCodeList.clear();
                itemModels.clear();
                temporaryCostOrderDTOS.clear();
            }

            // 清空TOC结算费用明细中绑定的暂估
            if(delBillList != null && !delBillList.isEmpty()) {
                Map<String, Object> params = new HashMap<>();
                params.put("tempCostBillIdList", delBillList);
                tocSettlementReceiveBillCostItemDao.batchCleanTempCostBillId(params);
            }
            delBillList.clear();
        }

        if (!tocIdList.isEmpty()) {
            List<Map<String, Object>> settleMapList = tocTemporaryReceiveBillCostItemDao.countByBillIds(tocIdList);
            for (Map<String, Object> settleMap : settleMapList) {
                Long billId = (Long) settleMap.get("billId");
                BigDecimal temporaryRmbAmount = (BigDecimal) settleMap.get("temporaryRmbAmount");
                Long totalNum = (Long) settleMap.get("totalNum");
                BigDecimal noSettlementAmount = (BigDecimal) settleMap.get("noSettlementAmount");
                Long noSettlementNum = (Long) settleMap.get("noSettlementNum");
                BigDecimal alreadyReceiveAmount = temporaryRmbAmount.subtract(noSettlementAmount);

                TocTemporaryCostBillModel billModel = new TocTemporaryCostBillModel();
                billModel.setId(billId);
                billModel.setTotalReceiveAmount(temporaryRmbAmount);
                billModel.setTotalReceiveNum(totalNum);
                billModel.setAlreadyReceiveAmount(alreadyReceiveAmount);
                billModel.setAlreadyReceiveNum(totalNum - noSettlementNum);

                tocTemporaryCostBillDao.modify(billModel);
            }
            settleMapList.clear();
        }
        tocIdList.clear();
        tempCostOrderList.clear();
    }

    @Override
    public List<MerchantInfoMongo> getMerchantList(Integer merchantId) {
        Map<String, Object> merParams = new HashMap<>();
        if (merchantId != null && merchantId.longValue() > 0) {
            merParams.put("merchantId", Long.valueOf(merchantId));
        }
        List<MerchantInfoMongo> merchantList = merchantInfoMongoDao.findAll(merParams);
        return merchantList;
    }

    private Map<Long, BrandParentMongo> getBrandParentListByGoods(Set<Long> goodsIdList) {
        Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();
        List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", new ArrayList(goodsIdList));
        for (MerchandiseInfoMogo merchandiseInfo : merchandiseInfoMogos) {
            if (StringUtils.isNotBlank(merchandiseInfo.getCommbarcode())) {
                merchandiseInfoMogoMap.put(merchandiseInfo.getMerchandiseId(), merchandiseInfo);
            }
        }

        Map<Long, BrandParentMongo> goodIdAndBrandParentMap = new HashMap<>();
        merchandiseInfoMogoMap.entrySet().forEach(entity -> {
            BrandParentMongo brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(entity.getValue().getCommbarcode());
            goodIdAndBrandParentMap.put(entity.getKey(), brandParent);
        });

        return goodIdAndBrandParentMap;
    }

    /**
     * 根据电商订单id集合查询每个电商订单结算金额占比最大的商品明细,最终获取到订单事业部和母品牌信息间的Map
     * @param orderIdList 订单id集合
     * @param buIdFlag 是否到事业部的维度
     * @param objectDao orderItemDao / orderReturnIdepotItemDao
     * @return Map<String, BrandParentMongo> key:externalCode_buId, value: BrandParentMongo
     */
    public Map<String, BrandParentMongo> getParentBrandInfoByMaxPriceOrderList(Map orderDTOList, List<Long> orderIdList, Boolean buIdFlag, Object objectDao) {
        Map<String, BrandParentMongo> maxPriceBrandParentInfoMap = new HashMap<>();
        if(objectDao instanceof OrderItemDao) {
            //电商订单
            //根据电商订单id集合查询每个电商订单结算金额占比最大的商品明细
            List<OrderItemModel> maxPriceListByOrderId = orderItemDao.getMaxPriceByOrderId(orderIdList, buIdFlag);
            Set<Long> goodIds = maxPriceListByOrderId.stream().map(e -> e.getGoodsId()).collect(Collectors.toSet());
            //根据商品Id获取对应的母品牌信息
            Map<Long, BrandParentMongo> brandParentListByGoods = getBrandParentListByGoods(goodIds);
            maxPriceListByOrderId.forEach((entity) -> {
                OrderDTO order = (OrderDTO) orderDTOList.get(entity.getOrderId());
                String tempKey = order.getExternalCode() + "_" + entity.getBuId();
                if(!maxPriceBrandParentInfoMap.containsKey(tempKey)) {
                    maxPriceBrandParentInfoMap.put(tempKey, brandParentListByGoods.get(entity.getGoodsId()));
                }
            });
        }else if(objectDao instanceof OrderReturnIdepotItemDao){
            //退货单
            //根据电商订单id集合查询每个电商订单结算金额占比最大的商品明细
            List<OrderReturnIdepotItemModel> maxPriceListByOrderId = orderReturnIdepotItemDao.getMaxPriceByOrderId(orderIdList, buIdFlag);
            Set<Long> goodIds = maxPriceListByOrderId.stream().map(e -> e.getInGoodsId()).collect(Collectors.toSet());
            //根据商品Id获取对应的母品牌信息
            Map<Long, BrandParentMongo> brandParentListByGoods = getBrandParentListByGoods(goodIds);
            maxPriceListByOrderId.forEach((entity) -> {
                OrderReturnIdepotDTO order = (OrderReturnIdepotDTO) orderDTOList.get(entity.getOreturnIdepotId());
                String tempKey = order.getExternalCode() + "_" + entity.getBuId();
                if(!maxPriceBrandParentInfoMap.containsKey(tempKey)) {
                    maxPriceBrandParentInfoMap.put(tempKey, brandParentListByGoods.get(entity.getInGoodsId()));
                }
            });
        }

        return maxPriceBrandParentInfoMap;
    }
}
