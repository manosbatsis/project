package com.topideal.order.service.bill.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.TobTempVerifyRelDao;
import com.topideal.dao.bill.TobTemporaryReceiveBillDao;
import com.topideal.dao.bill.TobTemporaryReceiveBillItemDao;
import com.topideal.dao.bill.TobTemporaryReceiveBillRebateItemDao;
import com.topideal.dao.sale.ShelfDao;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillRebateItemDTO;
import com.topideal.entity.vo.bill.TobTempVerifyRelModel;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillItemModel;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillModel;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillRebateItemModel;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.order.service.bill.TobTemporaryReceiveBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * tob暂估核销service
 **/
@Service
public class TobTemporaryReceiveBillServiceImpl implements TobTemporaryReceiveBillService {

    @Autowired
    private TobTemporaryReceiveBillDao tobTemporaryReceiveBillDao;
    @Autowired
    private TobTemporaryReceiveBillItemDao tobTemporaryReceiveBillItemDao;
    @Autowired
    private TobTemporaryReceiveBillRebateItemDao tobTemporaryReceiveBillRebateItemDao;
    @Autowired
    private TobTempVerifyRelDao tobTempVerifyRelDao;
    @Autowired
    private ShelfDao shelfDao;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;

    @Override
    public TobTemporaryReceiveBillDTO listToBTempBillTrackByPage(TobTemporaryReceiveBillDTO dto, User user) throws Exception {

        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }

        TobTemporaryReceiveBillDTO billDTO = tobTemporaryReceiveBillDao.listToBTempBillByPage(dto);

        List<TobTemporaryReceiveBillDTO> billDTOS = billDTO.getList();

        for (TobTemporaryReceiveBillDTO tempBillDTO : billDTOS) {
            String outBillWarn = calculateDays(tempBillDTO.getOutBillPlanDate(), tempBillDTO.getOutBillRealDate());
            String confirmWarn = calculateDays(tempBillDTO.getConfirmPlanDate(), tempBillDTO.getConfirmRealDate());
            String invoicingWarn = calculateDays(tempBillDTO.getInvoicingPlanDate(), tempBillDTO.getInvoicingRealDate());
            String paymentWarn = calculateDays(tempBillDTO.getPaymentPlanDate(), tempBillDTO.getPaymentRealDate());

            tempBillDTO.setOutBillWarn(outBillWarn);
            tempBillDTO.setConfirmWarn(confirmWarn);
            tempBillDTO.setInvoicingWarn(invoicingWarn);
            tempBillDTO.setPaymentWarn(paymentWarn);

            if (tempBillDTO.getPaymentPlanDate() != null) {
                int diff = TimeUtils.daysBetween(tempBillDTO.getShelfDate(), tempBillDTO.getPaymentPlanDate());
                tempBillDTO.setPaymentPlanPeriod(diff);
            }

            if (tempBillDTO.getPaymentRealDate() != null) {
                int diff = TimeUtils.daysBetween( tempBillDTO.getShelfDate(), tempBillDTO.getPaymentRealDate());
                tempBillDTO.setPaymentRealPeriod(diff);
            }

            if (tempBillDTO.getPaymentRealDate() != null && tempBillDTO.getPaymentPlanDate() != null) {
                int diff = tempBillDTO.getPaymentPlanPeriod() - tempBillDTO.getPaymentRealPeriod();
                if (diff > 0) {
                    tempBillDTO.setPaymentPeriodWarn("提前" + diff + "天");
                } else {
                    tempBillDTO.setPaymentPeriodWarn("延期" + Math.abs(diff) + "天");
                }
            } else {
                tempBillDTO.setPaymentPeriodWarn(paymentWarn);
            }
        }

        billDTO.setList(billDTOS);
        return billDTO;
    }

    @Override
    public List<Map<String, Object>> listForExportToBTrack(TobTemporaryReceiveBillDTO dto, User user) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                return resultList;
            }
            dto.setBuList(buList);
        }

        List<TobTemporaryReceiveBillDTO> billDTOS = tobTemporaryReceiveBillDao.listForExport(dto);

        for (TobTemporaryReceiveBillDTO tempBillDTO : billDTOS) {
            Map<String, Object> map = new HashMap<>();
            map.put("merchantName", tempBillDTO.getMerchantName());
            map.put("buName", tempBillDTO.getBuName());
            map.put("poNo", tempBillDTO.getPoNo());
            map.put("shelfDate", tempBillDTO.getShelfDate());
            map.put("customerName", tempBillDTO.getCustomerName());
            map.put("receiveAmount", tempBillDTO.getShelfAmount());
            map.put("currency", tempBillDTO.getCurrency());

            if (tempBillDTO.getPaymentPlanDate() != null) {
                int diff = TimeUtils.daysBetween(tempBillDTO.getShelfDate(), tempBillDTO.getPaymentPlanDate());
                map.put("paymentPlanPeriod", diff);
            }

            if (tempBillDTO.getPaymentRealDate() != null) {
                int diff = TimeUtils.daysBetween(tempBillDTO.getShelfDate(), tempBillDTO.getPaymentRealDate());
                map.put("paymentRealPeriod", diff);
            }
            resultList.add(map);
        }

        return resultList;
    }

    @Override
    public TobTemporaryReceiveBillDTO listToBTempBillVerifyByPage(TobTemporaryReceiveBillDTO dto, User user) throws Exception {

        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }

        TobTemporaryReceiveBillDTO billDTO = tobTemporaryReceiveBillDao.listToBTempBillByPage(dto);

        List<TobTemporaryReceiveBillDTO> billDTOS = billDTO.getList();

        List<Long> billIds = new ArrayList<>();

        for (TobTemporaryReceiveBillDTO tempBillDTO : billDTOS) {
            billIds.add(tempBillDTO.getId());
            tempBillDTO.setNonVerifyAmount(tempBillDTO.getShelfAmount());
            tempBillDTO.setNonVerifyRebateAmount(tempBillDTO.getShelfRebateAmount());
        }

        Map<Long, Map<String, Object>> itemAmountMap = new HashMap<>();
        if (!billIds.isEmpty()) {
            //统计tob暂估核销明细、返利明细的已核销金额
            List<Map<String, Object>> itemPriceList = tobTempVerifyRelDao.getRelReceiveBill(billIds);
            for (Map<String, Object> map : itemPriceList) {
                Long billId = (Long) map.get("tobId");

                itemAmountMap.put(billId, map);
            }
        }


        for (TobTemporaryReceiveBillDTO tempBillDTO : billDTOS) {
            Map<String, Object> map = itemAmountMap.get(tempBillDTO.getId()) ;

            if (map != null) {
                BigDecimal receiveAmount = map.get("receiveAmount") == null ? new BigDecimal("0") : (BigDecimal) map.get("receiveAmount");
                BigDecimal rebateAmount = map.get("rebateAmount") == null ? new BigDecimal("0") : (BigDecimal) map.get("rebateAmount");
                tempBillDTO.setVerifyAmount(receiveAmount);
                tempBillDTO.setVerifyRebateAmount(rebateAmount);

                BigDecimal nonVerifyAmount = tempBillDTO.getShelfAmount().subtract(receiveAmount);
                BigDecimal nonVerifyRebateAmount = tempBillDTO.getShelfRebateAmount().subtract(rebateAmount);
                tempBillDTO.setNonVerifyAmount(nonVerifyAmount);
                tempBillDTO.setNonVerifyRebateAmount(nonVerifyRebateAmount);
            }

        }
        billDTO.setList(billDTOS);
        return billDTO;
    }

    @Override
    public TobTemporaryReceiveBillDTO getDetails(Long id) {
        return tobTemporaryReceiveBillDao.searchDTOById(id);
    }

    @Override
    public TobTemporaryReceiveBillItemDTO listToBTempItemByPage(TobTemporaryReceiveBillItemDTO dto) throws Exception {

        TobTemporaryReceiveBillItemDTO tobTemporaryReceiveBillItemDTO = tobTemporaryReceiveBillItemDao.listToBTempItemByPage(dto);
        List<TobTemporaryReceiveBillItemDTO> billItemDTOS = tobTemporaryReceiveBillItemDTO.getList();
        List<Long> tobItemIds = new ArrayList<>();

        for (TobTemporaryReceiveBillItemDTO itemDTO : billItemDTOS) {
            tobItemIds.add(itemDTO.getId());
        }

        Map<Long, Map<String, Object>> itemAmountMap = new HashMap<>();
        if (!tobItemIds.isEmpty()) {
            //统计tob暂估核销明细、返利明细的已核销金额
            List<Map<String, Object>> itemPriceList = tobTempVerifyRelDao.getRelReceiveItemBill(tobItemIds, DERP_ORDER.TOBTEMPVERIFYREL_TYPE_0);
            for (Map<String, Object> map : itemPriceList) {
                Long tobItemId = (Long) map.get("tobItemId");
                itemAmountMap.put(tobItemId, map);
            }
        }

        for (TobTemporaryReceiveBillItemDTO tempBillDTO : billItemDTOS) {
            Map<String, Object> map = itemAmountMap.get(tempBillDTO.getId()) ;

            if (map != null) {
                BigDecimal verifyAmount = map.get("verifyAmount") == null ? new BigDecimal("0") : (BigDecimal) map.get("verifyAmount");
                String receiveCodes = (String) map.get("receiveCodes");
                tempBillDTO.setVerifiyAmount(verifyAmount);
                tempBillDTO.setReceiveCode(receiveCodes);
            }

        }
        tobTemporaryReceiveBillItemDTO.setList(billItemDTOS);
        return tobTemporaryReceiveBillItemDTO;

    }

    @Override
    public TobTemporaryReceiveBillRebateItemDTO listToBTempRebateItemByPage(TobTemporaryReceiveBillRebateItemDTO dto) throws Exception {
        TobTemporaryReceiveBillRebateItemDTO rebateItemDTO = tobTemporaryReceiveBillRebateItemDao.listToBTempRebateItemByPage(dto);
        List<TobTemporaryReceiveBillRebateItemDTO> billItemDTOS = rebateItemDTO.getList();
        List<Long> tobItemIds = new ArrayList<>();

        for (TobTemporaryReceiveBillRebateItemDTO itemDTO : billItemDTOS) {
            tobItemIds.add(itemDTO.getId());
            itemDTO.setVerifyRebateAmount(BigDecimal.ZERO);
        }

        Map<Long, Map<String, Object>> itemAmountMap = new HashMap<>();
        if (!tobItemIds.isEmpty()) {
            //统计tob暂估核销明细、返利明细的已核销金额
            List<Map<String, Object>> itemPriceList = tobTempVerifyRelDao.getRelReceiveItemBill(tobItemIds, DERP_ORDER.TOBTEMPVERIFYREL_TYPE_1);
            for (Map<String, Object> map : itemPriceList) {
                Long tobItemId = (Long) map.get("tobItemId");
                itemAmountMap.put(tobItemId, map);
            }
        }

        for (TobTemporaryReceiveBillRebateItemDTO tempBillDTO : billItemDTOS) {
            Map<String, Object> map = itemAmountMap.get(tempBillDTO.getId()) ;

            if (map != null) {
                BigDecimal verifyAmount = map.get("verifyAmount") == null ? new BigDecimal("0") : (BigDecimal) map.get("verifyAmount");
                String receiveCodes = (String) map.get("receiveCodes");
                tempBillDTO.setVerifyRebateAmount(verifyAmount);
                tempBillDTO.setReceiveCode(receiveCodes);
            }

        }
        rebateItemDTO.setList(billItemDTOS);
        return rebateItemDTO;

    }

    @Override
    public Integer getTempDetailsCount(TobTemporaryReceiveBillDTO dto) throws Exception {

        Integer itemCount = tobTemporaryReceiveBillItemDao.getTempDetailsCount(dto);
        Integer rebateItemCount = tobTemporaryReceiveBillRebateItemDao.getRebateTempDetailsCount(dto);
        Integer total = itemCount + rebateItemCount;
        return total;
    }

    @Override
    public Map<String, List<Map<String, Object>>> exportTempDetails(TobTemporaryReceiveBillDTO dto, User user) throws Exception {

        Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();

        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                return resultMap;
            }
            dto.setBuList(buList);
        }

        List<Map<String, Object>> mainMapList = new ArrayList<>();
        List<Map<String, Object>> itemMapList = new ArrayList<>();

        List<TobTemporaryReceiveBillDTO> billDTOS = tobTemporaryReceiveBillDao.listForExport(dto);

        List<Long> idList = new ArrayList<>();
        Map<Long, TobTemporaryReceiveBillDTO> idMap = new HashMap<>();

        for (TobTemporaryReceiveBillDTO tempBillDTO : billDTOS) {
            idList.add(tempBillDTO.getId());
            idMap.put(tempBillDTO.getId(),  tempBillDTO);
        }

        Map<Long, Map<String, Object>> itemAmountMap = new HashMap<>();
        if (!idList.isEmpty()) {
            //统计tob暂估核销明细、返利明细的已核销金额
            List<Map<String, Object>> itemPriceList = tobTempVerifyRelDao.getRelReceiveBill(idList);
            for (Map<String, Object> map : itemPriceList) {
                Long billId = (Long) map.get("tobId");

                itemAmountMap.put(billId, map);
            }
        }


        for (TobTemporaryReceiveBillDTO tempBillDTO : billDTOS) {
            Map<String, Object> map = new HashMap<>();
            map.put("merchantName", tempBillDTO.getMerchantName());
            map.put("buName", tempBillDTO.getBuName());
            map.put("poNo", tempBillDTO.getPoNo());
            map.put("shelfDate", tempBillDTO.getShelfDate());
            map.put("customerName", tempBillDTO.getCustomerName());
            map.put("status", tempBillDTO.getStatusLabel());
            map.put("saleType", tempBillDTO.getSaleTypeLabel());
            map.put("currency", tempBillDTO.getCurrency());
            map.put("shelfAmount", tempBillDTO.getShelfAmount());
            map.put("shelfRebateAmount", tempBillDTO.getShelfRebateAmount());
            map.put("verifyAmount", BigDecimal.ZERO);
            map.put("nonVerifyAmount", tempBillDTO.getShelfAmount());

            Map<String, Object> verifyMap = itemAmountMap.get(tempBillDTO.getId()) ;

            if (verifyMap != null) {
                BigDecimal receiveAmount = verifyMap.get("receiveAmount") == null ? new BigDecimal("0") : (BigDecimal) verifyMap.get("receiveAmount");

                map.put("verifyAmount", receiveAmount);

                BigDecimal nonVerifyAmount = tempBillDTO.getShelfAmount().subtract(receiveAmount);
                map.put("nonVerifyAmount", nonVerifyAmount);
            }

            mainMapList.add(map);
        }
        resultMap.put("mainList", mainMapList);

        int pageSize = 1000;
        //获取导出核销明细的数量
        Integer itemCount = tobTemporaryReceiveBillItemDao.getTempDetailsCount(dto);
        for (int i = 0; i < itemCount; ) {
            int pageSub = (i+pageSize) < itemCount ? (i+pageSize) : itemCount;
            TobTemporaryReceiveBillItemDTO itemDTO = new TobTemporaryReceiveBillItemDTO();
            itemDTO.setBillIds(idList);
            itemDTO.setBegin(i);
            itemDTO.setPageSize(pageSize);
            TobTemporaryReceiveBillItemDTO tobTemporaryReceiveBillItemDTO = tobTemporaryReceiveBillItemDao.listToBTempItemByPage(itemDTO);
            List<TobTemporaryReceiveBillItemDTO> itemDTOS  = tobTemporaryReceiveBillItemDTO.getList();

            List<Long> tobItemIds = new ArrayList<>();

            for (TobTemporaryReceiveBillItemDTO billItemDTO : itemDTOS) {
                tobItemIds.add(billItemDTO.getId());
            }

            Map<Long, Map<String, Object>> itemAmountMap1 = new HashMap<>();
            if (!tobItemIds.isEmpty()) {
                //统计tob暂估核销明细、返利明细的已核销金额
                List<Map<String, Object>> itemPriceList = tobTempVerifyRelDao.getRelReceiveItemBill(tobItemIds, DERP_ORDER.TOBTEMPVERIFYREL_TYPE_0);
                for (Map<String, Object> map : itemPriceList) {
                    Long tobItemId = (Long) map.get("tobItemId");
                    itemAmountMap1.put(tobItemId, map);
                }
            }

            for (TobTemporaryReceiveBillItemDTO billItemDTO : itemDTOS) {
                TobTemporaryReceiveBillDTO  billDTO = idMap.get(billItemDTO.getBillId());

                BigDecimal price = billItemDTO.getPrice() == null ? BigDecimal.ZERO : billItemDTO.getPrice();
                Integer shelfNum = billItemDTO.getShelfNum() == null ? 0 : billItemDTO.getShelfNum();
                BigDecimal receiveAmount = price.multiply(new BigDecimal(shelfNum)).setScale(2,BigDecimal.ROUND_HALF_UP);


                Map<String, Object> map = new HashMap<>();
                map.put("buName", billDTO.getBuName());
                map.put("customerName", billDTO.getCustomerName());
                map.put("poNo", billDTO.getPoNo());
                map.put("status", billDTO.getStatusLabel());
                map.put("orderCode", billDTO.getOrderCode());
                map.put("shelfCode", billDTO.getShelfCode());
                map.put("shelfDate", billDTO.getShelfDate());
                map.put("goodsName", billItemDTO.getGoodsName());
                map.put("parentBrandName", billItemDTO.getParentBrandName());
                map.put("goodsNo", billItemDTO.getGoodsNo());
                map.put("shelfNum", shelfNum);
                map.put("price", price);
                map.put("currency", billDTO.getCurrency());
                map.put("receiveAmount", receiveAmount);
                map.put("verifyAmount", BigDecimal.ZERO);
                BigDecimal nonVerifyAmount = receiveAmount;
                Map<String, Object> itemMap = itemAmountMap1.get(billItemDTO.getId()) ;

                if (itemMap != null) {
                    BigDecimal verifyAmount = itemMap.get("verifyAmount") == null ? new BigDecimal("0") : (BigDecimal) itemMap.get("verifyAmount");
                    String receiveCodes = (String) itemMap.get("receiveCodes");

                    map.put("verifyAmount", verifyAmount);
                    map.put("receiveCode", receiveCodes);

                    nonVerifyAmount = nonVerifyAmount.subtract(verifyAmount);

                }

                map.put("nonVerifyAmount", nonVerifyAmount);
                itemMapList.add(map);
            }

            i = pageSub;
        }
        resultMap.put("itemMapList", itemMapList);
        return resultMap;
    }

    @Override
    public Map<String, List<Map<String, Object>>> exportTempRebateDetails(TobTemporaryReceiveBillDTO dto, User user) throws Exception {

        Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();

        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                return resultMap;
            }
            dto.setBuList(buList);
        }

        List<Map<String, Object>> mainMapList = new ArrayList<>();
        List<Map<String, Object>> rebateItemMapList = new ArrayList<>();

        List<TobTemporaryReceiveBillDTO> billDTOS = tobTemporaryReceiveBillDao.listForExport(dto);

        List<Long> idList = new ArrayList<>();
        Map<Long, TobTemporaryReceiveBillDTO> idMap = new HashMap<>();

        for (TobTemporaryReceiveBillDTO tempBillDTO : billDTOS) {
            idList.add(tempBillDTO.getId());
            idMap.put(tempBillDTO.getId(),  tempBillDTO);
        }

        Map<Long, Map<String, Object>> itemAmountMap = new HashMap<>();
        if (!idList.isEmpty()) {
            //统计tob暂估核销明细、返利明细的已核销金额
            List<Map<String, Object>> itemPriceList = tobTempVerifyRelDao.getRelReceiveBill(idList);
            for (Map<String, Object> map : itemPriceList) {
                Long billId = (Long) map.get("tobId");

                itemAmountMap.put(billId, map);
            }
        }


        for (TobTemporaryReceiveBillDTO tempBillDTO : billDTOS) {
            Map<String, Object> map = new HashMap<>();
            map.put("merchantName", tempBillDTO.getMerchantName());
            map.put("buName", tempBillDTO.getBuName());
            map.put("poNo", tempBillDTO.getPoNo());
            map.put("shelfDate", tempBillDTO.getShelfDate());
            map.put("customerName", tempBillDTO.getCustomerName());
            map.put("rebateStauts", tempBillDTO.getRebateStatusLabel());
            map.put("saleType", tempBillDTO.getSaleTypeLabel());
            map.put("currency", tempBillDTO.getCurrency());
            map.put("shelfAmount", tempBillDTO.getShelfAmount());
            map.put("shelfRebateAmount", tempBillDTO.getShelfRebateAmount());
            map.put("verifyRebateAmount", BigDecimal.ZERO);
            map.put("nonVerifyRebateAmount", tempBillDTO.getShelfRebateAmount());

            Map<String, Object> verifyMap = itemAmountMap.get(tempBillDTO.getId()) ;

            if (verifyMap != null) {
                BigDecimal rebateAmount = verifyMap.get("rebateAmount") == null ? new BigDecimal("0") : (BigDecimal) verifyMap.get("rebateAmount");

                map.put("verifyRebateAmount", rebateAmount);

                BigDecimal nonVerifyRebateAmount = tempBillDTO.getShelfRebateAmount().subtract(rebateAmount);
                map.put("nonVerifyRebateAmount", nonVerifyRebateAmount);
            }

            mainMapList.add(map);
        }
        resultMap.put("mainList", mainMapList);

        int pageSize = 1000;

        //获取导出核销返利明细的数量
        Integer rebateItemCount = tobTemporaryReceiveBillRebateItemDao.getRebateTempDetailsCount(dto);
        for (int i = 0; i < rebateItemCount; ) {
            int pageSub = (i+pageSize) < rebateItemCount ? (i+pageSize) : rebateItemCount;
            TobTemporaryReceiveBillRebateItemDTO itemDTO = new TobTemporaryReceiveBillRebateItemDTO();
            itemDTO.setBillIds(idList);
            itemDTO.setBegin(i);
            itemDTO.setPageSize(pageSize);
            TobTemporaryReceiveBillRebateItemDTO tobTemporaryReceiveBillRebateItemDTO = tobTemporaryReceiveBillRebateItemDao.listToBTempRebateItemByPage(itemDTO);
            List<TobTemporaryReceiveBillRebateItemDTO> itemDTOS  = tobTemporaryReceiveBillRebateItemDTO.getList();


            List<Long> tobItemIds = new ArrayList<>();

            for (TobTemporaryReceiveBillRebateItemDTO billItemDTO : itemDTOS) {
                tobItemIds.add(billItemDTO.getId());
            }

            Map<Long, Map<String, Object>> itemAmountMap1 = new HashMap<>();
            if (!tobItemIds.isEmpty()) {
                //统计tob暂估核销明细、返利明细的已核销金额
                List<Map<String, Object>> itemPriceList = tobTempVerifyRelDao.getRelReceiveItemBill(tobItemIds, DERP_ORDER.TOBTEMPVERIFYREL_TYPE_1);
                for (Map<String, Object> map : itemPriceList) {
                    Long tobItemId = (Long) map.get("tobItemId");
                    itemAmountMap1.put(tobItemId, map);
                }
            }

            for (TobTemporaryReceiveBillRebateItemDTO billItemDTO : itemDTOS) {
                TobTemporaryReceiveBillDTO  billDTO = idMap.get(billItemDTO.getBillId());

                BigDecimal price = billItemDTO.getPrice() == null ? BigDecimal.ZERO : billItemDTO.getPrice();
                Integer shelfNum = billItemDTO.getShelfNum() == null ? 0 : billItemDTO.getShelfNum();

                Map<String, Object> map = new HashMap<>();
                map.put("buName", billDTO.getBuName());
                map.put("customerName", billDTO.getCustomerName());
                map.put("poNo", billDTO.getPoNo());
                map.put("status", billDTO.getRebateStatusLabel());
                map.put("relSdCode", billItemDTO.getRelSdCode());
                map.put("shelfCode", billDTO.getShelfCode());
                map.put("shelfDate", billDTO.getShelfDate());
                map.put("goodsName", billItemDTO.getGoodsName());
                map.put("parentBrandName", billItemDTO.getParentBrandName());
                map.put("brandName", billItemDTO.getBrandName());
                map.put("goodsNo", billItemDTO.getGoodsNo());
                map.put("shelfNum", shelfNum);
                map.put("price", price);
                map.put("sdTypeName", billItemDTO.getSdTypeName());
                map.put("currency", billDTO.getCurrency());
                map.put("receiveAmount", billItemDTO.getRebateAmount());
                map.put("sdRatio", billItemDTO.getSdRatio());
                BigDecimal nonVerifyAmount = billItemDTO.getRebateAmount();
                Map<String, Object> itemMap = itemAmountMap1.get(billItemDTO.getId()) ;
                map.put("verifyAmount", BigDecimal.ZERO);
                map.put("nonVerifyAmount", nonVerifyAmount);
                if (itemMap != null) {
                    BigDecimal verifyAmount = itemMap.get("verifyAmount") == null ? new BigDecimal("0") : (BigDecimal) itemMap.get("verifyAmount");
                    String receiveCodes = (String) itemMap.get("receiveCodes");
                    map.put("verifyAmount", verifyAmount);
                    map.put("receiveCode", receiveCodes);

                    nonVerifyAmount = nonVerifyAmount.subtract(verifyAmount);
                    map.put("nonVerifyAmount", nonVerifyAmount);
                }
                rebateItemMapList.add(map);
            }

            i = pageSub;
        }

        resultMap.put("rebateItemMapList", rebateItemMapList);

        return resultMap;
    }

    @Override
    public void endTobTemporaryReceiveBill(String ids, String type) throws Exception {
        List<Long> idList = StrUtils.parseIds(ids);

        List<TobTemporaryReceiveBillDTO> tobTemporaryReceiveBillDTOS = tobTemporaryReceiveBillDao.listBillByRelIds(idList);

        for (TobTemporaryReceiveBillDTO receiveBillDTO : tobTemporaryReceiveBillDTOS) {

            if ("0".equals(type) && DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_5.equals(receiveBillDTO.getStatus())) {
                throw new RuntimeException("该暂估已完结核销，不能重复完结！");
            }

            if ("1".equals(type) && DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_5.equals(receiveBillDTO.getRebateStatus())) {
                throw new RuntimeException("该暂估已完结核销，不能重复完结！");
            }

        }

        List<TobTempVerifyRelModel> relModels = new ArrayList<>();
        if ("0".equals(type)) {
            List<TobTemporaryReceiveBillItemModel> itemModels = tobTemporaryReceiveBillItemDao.listByBillIds(idList);

            List<Long> tobItemIds = new ArrayList<>();
            for (TobTemporaryReceiveBillItemModel itemModel : itemModels) {
                tobItemIds.add(itemModel.getId());
            }
            List<Map<String, Object>> itemIdVerifyList = tobTempVerifyRelDao.getRelReceiveItemBill(tobItemIds,DERP_ORDER.TOBTEMPVERIFYREL_TYPE_0);

            Map<Long, BigDecimal> verifyAmountMap = new HashMap<>();
            for (Map<String, Object> map : itemIdVerifyList) {
                Long tobItemId = (Long) map.get("tobItemId");
                BigDecimal verifyAmount = map.get("verifyAmount") == null ? new BigDecimal("0") : (BigDecimal) map.get("verifyAmount");
                verifyAmountMap.put(tobItemId, verifyAmount);
            }

            for (TobTemporaryReceiveBillItemModel itemModel : itemModels) {
                BigDecimal amount = itemModel.getPrice().multiply(new BigDecimal(itemModel.getShelfNum())).setScale(2, BigDecimal.ROUND_HALF_UP);

                BigDecimal verifyAmount = verifyAmountMap.get(itemModel.getId()) == null ? new BigDecimal("0") : (BigDecimal) verifyAmountMap.get(itemModel.getId());

                BigDecimal noVerifyAmount = amount.subtract(verifyAmount);

                if (noVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
                    continue;
                }

                TobTempVerifyRelModel relModel = new TobTempVerifyRelModel();
                relModel.setTobId(itemModel.getBillId());
                relModel.setTobItemId(itemModel.getId());
                relModel.setVerifiyAmount(noVerifyAmount);
                relModel.setGoodsId(itemModel.getGoodsId());
                relModel.setType(DERP_ORDER.TOBTEMPVERIFYREL_TYPE_0);
                relModel.setCreateDate(TimeUtils.getNow());

                relModels.add(relModel);
            }
        }

        if ("1".equals(type)) {
            List<TobTemporaryReceiveBillRebateItemModel> rebateItemModels = tobTemporaryReceiveBillRebateItemDao.listByBillIds(idList);

            List<Long> tobItemIds = new ArrayList<>();
            for (TobTemporaryReceiveBillRebateItemModel itemModel : rebateItemModels) {
                tobItemIds.add(itemModel.getId());
            }
            List<Map<String, Object>> itemIdVerifyList = tobTempVerifyRelDao.getRelReceiveItemBill(tobItemIds,DERP_ORDER.TOBTEMPVERIFYREL_TYPE_1);

            Map<Long, BigDecimal> verifyAmountMap = new HashMap<>();
            for (Map<String, Object> map : itemIdVerifyList) {
                Long tobItemId = (Long) map.get("tobItemId");
                BigDecimal verifyAmount = map.get("verifyAmount") == null ? new BigDecimal("0") : (BigDecimal) map.get("verifyAmount");
                verifyAmountMap.put(tobItemId, verifyAmount);
            }

            for (TobTemporaryReceiveBillRebateItemModel itemModel : rebateItemModels) {

                BigDecimal verifyAmount = verifyAmountMap.get(itemModel.getId()) == null ? new BigDecimal("0") : (BigDecimal) verifyAmountMap.get(itemModel.getId());

                BigDecimal noVerifyAmount = itemModel.getRebateAmount().subtract(verifyAmount);

                if (noVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
                    continue;
                }

                TobTempVerifyRelModel relModel = new TobTempVerifyRelModel();
                relModel.setTobId(itemModel.getBillId());
                relModel.setTobItemId(itemModel.getId());
                relModel.setVerifiyAmount(noVerifyAmount);
                relModel.setGoodsId(itemModel.getGoodsId());
                relModel.setProjectId(itemModel.getProjectId());
                relModel.setType(DERP_ORDER.TOBTEMPVERIFYREL_TYPE_1);
                relModel.setCreateDate(TimeUtils.getNow());

                relModels.add(relModel);
            }
        }

        for (TobTemporaryReceiveBillDTO receiveBillDTO : tobTemporaryReceiveBillDTOS) {
            TobTemporaryReceiveBillModel billModel = new TobTemporaryReceiveBillModel();
            billModel.setId(receiveBillDTO.getId());

            if ("0".equals(type)) {
                billModel.setStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_5);
            }

            if ("1".equals(type)) {
                billModel.setRebateStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_5);
            }

            tobTemporaryReceiveBillDao.modify(billModel);
        }

        tobTempVerifyRelDao.batchSave(relModels);
    }

    @Override
    public void batchDelReceiveBill(String ids) throws Exception {

        List<Long> idList = StrUtils.parseIds(ids);

        List<TobTemporaryReceiveBillDTO> tobTemporaryReceiveBillDTOS = tobTemporaryReceiveBillDao.listBillByRelIds(idList);

        for (TobTemporaryReceiveBillDTO billDTO : tobTemporaryReceiveBillDTOS) {
            if (!DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1.equals(billDTO.getStatus())
                || !DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1.equals(billDTO.getRebateStatus())) {
                throw new RuntimeException("po单号：" + billDTO.getPoNo() + "的结算状态不为“已上架未核销”");
            }
        }

        List<String> shelfCodes = new ArrayList<>();
        for (TobTemporaryReceiveBillDTO billDTO : tobTemporaryReceiveBillDTOS) {
            shelfCodes.add(billDTO.getShelfCode());
        }

        tobTemporaryReceiveBillDao.delete(idList);
        tobTemporaryReceiveBillItemDao.delItemsByBillIds(idList);
        tobTemporaryReceiveBillRebateItemDao.delItemsByBillIds(idList);
        //更新上架单状态
        if (!shelfCodes.isEmpty()) {
            shelfDao.batchUpdateStatus(DERP_ORDER.SHELF_ISGENERATE_0, shelfCodes);
        }
    }

    /**
     * 计算预警天数
     */
    private String calculateDays(Timestamp planTime, Timestamp realTime) {
        /**
         （1）若无实际日期且计划日期＜当前日期，为延期天数预警，延期天数=当前日期 - 计划日期；
         （2）若无实际日期且计划日期 ≥ 当前日期，为剩余天数预警，剩余天数=计划日期 - 当前日期；
         （3）若有实际日期且计划日期＜实际日期，为延期天数预警，延期天数=实际日期 - 计划日期；
         （4）若有实际日期且计划日期 ≥ 实际日期，为提前天数提醒，提前天数=计划日期 - 实际日期；
         （5）若计划日期为空时，则列表页显示为“--”；*/
        if (planTime == null) {
            return "--";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String planDate = sdf.format(planTime);

        if (realTime == null) {
            String today = sdf.format(new Date());
            boolean compare = TimeUtils.dateStrComparison(planDate, today, "yyyy-MM-dd");
            if (compare) {
                int diff = TimeUtils.daysBetween(TimeUtils.getNow(), planTime);
                return "剩余" + diff + "天";
            }

            if (!compare) {
                int diff = TimeUtils.daysBetween(planTime, TimeUtils.getNow());
                return "延期" + diff + "天";
            }
        }

        if (realTime != null) {
            String realDate = sdf.format(realTime);
            boolean compare = TimeUtils.dateStrComparison(planDate, realDate, "yyyy-MM-dd");
            if (compare) {
                int diff = TimeUtils.daysBetween(realTime, planTime);
                return "提前" + diff + "天";
            }

            if (!compare) {
                int diff = TimeUtils.daysBetween(planTime, realTime);
                return "延期" + diff + "天";
            }
        }

        return "--";
    }
}
