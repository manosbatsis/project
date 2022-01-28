package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.dao.bill.ReceiveBillDao;
import com.topideal.dao.bill.ReceiveBillVerifyItemDao;
import com.topideal.dao.sale.SaleCreditBillOrderDao;
import com.topideal.dao.sale.SaleCreditOrderDao;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import com.topideal.entity.vo.bill.ReceiveBillVerifyItemModel;
import com.topideal.entity.vo.sale.SaleCreditBillOrderModel;
import com.topideal.entity.vo.sale.SaleCreditOrderModel;
import com.topideal.service.timer.ReceiveVerifyBackfillCreditOrderService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReceiveVerifyBackfillCreditOrderServiceImpl implements ReceiveVerifyBackfillCreditOrderService {

    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private ReceiveBillVerifyItemDao receiveBillVerifyItemDao;
    @Autowired
    private SaleCreditOrderDao saleCreditOrderDao;
    @Autowired
    private SaleCreditBillOrderDao saleCreditBillOrderDao;


    @Override
    public void saveBackfillCredit(String json, String keys, String topics, String tags) throws Exception {

        //更新的赊销单id集合
        List<Long> updateCreditIds = new ArrayList<>();

        JSONObject jsonData = JSONObject.fromObject(json);
        Long merchantId = null ;
        if(jsonData.containsKey("merchantId") && jsonData.get("merchantId") != null) {
            merchantId = Long.valueOf(jsonData.getString("merchantId"));
        }

        /**查询待收保证金状态的赊销单**/
        SaleCreditOrderModel saleCreditOrderModel = new SaleCreditOrderModel();
        saleCreditOrderModel.setStatus(DERP_ORDER.SALECREDIT_STATUS_002);
        saleCreditOrderModel.setMerchantId(merchantId);
        List<SaleCreditOrderModel> saleCreditOrderModels = saleCreditOrderDao.list(saleCreditOrderModel);

        List<String> saleCreditCodes = saleCreditOrderModels.stream().map(e -> e.getCode()).collect(Collectors.toList());

        //查询已核销的指定关联业务单号的赊销单创建的应收单
        ReceiveBillDTO receiveBillDTO = new ReceiveBillDTO();
        receiveBillDTO.setRelCodes(saleCreditCodes);
        receiveBillDTO.setBillStates(DERP_ORDER.RECEIVEBILL_BILLSTATUS_04);
        receiveBillDTO.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_8);

        List<ReceiveBillModel> receiveBillModels = receiveBillDao.listDTO(receiveBillDTO);

        List<Long> receiveBillIds = new ArrayList<>();
        Map<String, Long> creditBillIdMap = new HashMap<>();

        for (ReceiveBillModel receiveBillModel : receiveBillModels) {
            List<String> relCodes = Arrays.asList(receiveBillModel.getRelCode().split(","));
            //如果应收单关联的业务单号包含查询到的赊销单号
            for (String relCode : relCodes) {
                if (saleCreditCodes.contains(relCode)) {
                    receiveBillIds.add(receiveBillModel.getId());
                    creditBillIdMap.put(relCode, receiveBillModel.getId());
                }
            }
        }

        List<ReceiveBillVerifyItemModel> verifyItemModels = receiveBillVerifyItemDao.listByBillIds(receiveBillIds);

        //各应收单的核销金额
        Map<Long, BigDecimal> billVerifyAmountMap = new HashMap<>();
        //各应收单的最后一次收款日期
        Map<Long, Timestamp> billReceiveDateMap = new HashMap<>();

        for (ReceiveBillVerifyItemModel verifyItemModel : verifyItemModels) {

            if (!billReceiveDateMap.containsKey(verifyItemModel.getBillId())) {
                billReceiveDateMap.put(verifyItemModel.getBillId(), verifyItemModel.getReceiveDate());
            }

            BigDecimal verifyAmount = verifyItemModel.getPrice();

            if (billVerifyAmountMap.containsKey(verifyItemModel.getBillId())) {
                verifyAmount = verifyAmount.add(billVerifyAmountMap.get(verifyItemModel.getBillId()));
            }

            billVerifyAmountMap.put(verifyItemModel.getBillId(), verifyAmount);
        }

        List<SaleCreditOrderModel> updateCreditModels = new ArrayList<>();
        for (SaleCreditOrderModel creditOrderModel : saleCreditOrderModels) {
            Long billId = creditBillIdMap.get(creditOrderModel.getCode());
            BigDecimal verifyAmount = billVerifyAmountMap.get(billId);
            Timestamp receiveDate = billReceiveDateMap.get(billId);

            if (verifyAmount == null || receiveDate == null) {
                continue;
            }

            SaleCreditOrderModel updateCreditModel = new SaleCreditOrderModel();
            updateCreditModel.setId(creditOrderModel.getId());
            updateCreditModel.setReceiveMarginDate(receiveDate);
            updateCreditModel.setActualMarginAmount(verifyAmount);
            updateCreditModel.setStatus(DERP_ORDER.SALECREDIT_STATUS_003);

            updateCreditModels.add(updateCreditModel);
            updateCreditIds.add(creditOrderModel.getId());
        }
        if (updateCreditModels.size() > 0) {
            saleCreditOrderDao.batchUpdate(updateCreditModels);
        }

        /**查询待收款状态的赊销收款单**/
        SaleCreditBillOrderModel saleCreditBillOrderModel = new SaleCreditBillOrderModel();
        saleCreditBillOrderModel.setStatus(DERP_ORDER.SALECREDITBILL_STATUS_001);
        saleCreditBillOrderModel.setMerchantId(merchantId);
        List<SaleCreditBillOrderModel> saleCreditBillOrderModels = saleCreditBillOrderDao.list(saleCreditBillOrderModel);

        List<String> saleCreditBillCodes = saleCreditBillOrderModels.stream().map(e -> e.getCode()).collect(Collectors.toList());

        //查询已核销的指定关联业务单号的赊销单创建的应收单
        ReceiveBillDTO receiveBill9DTO = new ReceiveBillDTO();
        receiveBill9DTO.setRelCodes(saleCreditBillCodes);
        receiveBill9DTO.setBillStates(DERP_ORDER.RECEIVEBILL_BILLSTATUS_04);
        receiveBill9DTO.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_9);

        List<ReceiveBillModel> receiveBill9Models = receiveBillDao.listDTO(receiveBill9DTO);

        List<Long> receiveBill9Ids = new ArrayList<>();
        Map<String, Long> credit9BillIdMap = new HashMap<>();

        for (ReceiveBillModel receiveBillModel : receiveBill9Models) {
            List<String> relCodes = Arrays.asList(receiveBillModel.getRelCode().split(","));
            //如果应收单关联的业务单号包含查询到的赊销单号
            for (String relCode : relCodes) {
                if (saleCreditBillCodes.contains(relCode)) {
                    receiveBill9Ids.add(receiveBillModel.getId());
                    credit9BillIdMap.put(relCode, receiveBillModel.getId());
                }
            }
        }

        List<ReceiveBillVerifyItemModel> verifyItem9Models = receiveBillVerifyItemDao.listByBillIds(receiveBill9Ids);

        //各应收单的最后一次收款日期
        Map<Long, Timestamp> billReceiveDate9Map = new HashMap<>();

        for (ReceiveBillVerifyItemModel verifyItemModel : verifyItem9Models) {

            if (!billReceiveDate9Map.containsKey(verifyItemModel.getBillId())) {
                billReceiveDate9Map.put(verifyItemModel.getBillId(), verifyItemModel.getReceiveDate());
            }
        }

        List<SaleCreditBillOrderModel> updateCreditBillModels = new ArrayList<>();

        for (SaleCreditBillOrderModel creditBillOrderModel : saleCreditBillOrderModels) {
            Long billId = credit9BillIdMap.get(creditBillOrderModel.getCode());
            Timestamp receiveDate = billReceiveDate9Map.get(billId);

            if (receiveDate == null) {
                continue;
            }

            SaleCreditBillOrderModel updateCreditBillModel = new SaleCreditBillOrderModel();
            updateCreditBillModel.setId(creditBillOrderModel.getId());
            updateCreditBillModel.setAccountDate(receiveDate);
            updateCreditBillModel.setStatus(DERP_ORDER.SALECREDITBILL_STATUS_002);

            updateCreditBillModels.add(updateCreditBillModel);
        }

        if (updateCreditBillModels.size() > 0) {
            saleCreditBillOrderDao.batchUpdate(updateCreditBillModels);
        }


        /**
         * 3.1）检查该赊销单是否存在待收款的收款单，若存在，更新赊销单的状态为“待收款”；
         * 3.2）若不存在待收款的收款单，检查赊销单的还款本金+实收保证金是否等于订单金额，若相等，更新赊销单状态为“已还款”，否则更新赊销单的状态为“赊销中”；
         * */
        if (updateCreditIds.size() == 0) {
            return;
        }
        List<SaleCreditOrderModel> updateModels = new ArrayList<>();
        List<SaleCreditOrderModel> saleCreditOrderModelList = saleCreditOrderDao.listByIds(updateCreditIds);

        for (SaleCreditOrderModel creditOrderModel : saleCreditOrderModelList) {
            SaleCreditBillOrderModel creditBillOrderModel = new SaleCreditBillOrderModel();
            creditBillOrderModel.setCreditOrderId(creditOrderModel.getId());
            SaleCreditBillOrderModel creditBillOrder = saleCreditBillOrderDao.searchByModel(creditBillOrderModel);

            SaleCreditOrderModel updateModel = new SaleCreditOrderModel();
            updateModel.setId(creditOrderModel.getId());
            if (creditBillOrder != null) {
                updateModel.setStatus(DERP_ORDER.SALECREDIT_STATUS_005);
                updateModels.add(updateModel);
                continue;
            }

            BigDecimal receivePrincipalAmount = creditOrderModel.getReceivePrincipalAmount() == null ? BigDecimal.ZERO : creditOrderModel.getReceivePrincipalAmount();
            BigDecimal actualMarginAmount = creditOrderModel.getActualMarginAmount() == null ? BigDecimal.ZERO : creditOrderModel.getActualMarginAmount();
            BigDecimal totalAmount = receivePrincipalAmount.add(actualMarginAmount);

            if (totalAmount.compareTo(creditOrderModel.getCreditAmount()) == 0) {
                updateModel.setStatus(DERP_ORDER.SALECREDIT_STATUS_007);
            } else {
                updateModel.setStatus(DERP_ORDER.SALECREDIT_STATUS_004);
            }

            updateModels.add(updateModel);
        }

        if (updateModels.size() > 0) {
            saleCreditOrderDao.batchUpdate(updateModels);
        }

    }
}
