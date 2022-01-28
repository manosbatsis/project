package com.topideal.service.timer.impl;

import com.topideal.api.nc.NcClientUtils;
import com.topideal.api.nc.nc12.BillVerifyDetail;
import com.topideal.api.nc.nc12.BillVerifyQueryRoot;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.entity.dto.bill.PaymentBillDTO;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.enums.NcAPIEnum;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.timer.ReceiveBillNcVerifyBackService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: NC核销记录查询接口
 * @Author: Chen Yiluan
 * @Date: 2021/08/19 14:19
 **/
@Service
public class ReceiveBillNcVerifyBackServiceImpl implements ReceiveBillNcVerifyBackService {

    private static final Logger LOG = Logger.getLogger(ReceiveBillNcVerifyBackServiceImpl.class);

    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private ReceiveBillItemDao receiveBillItemDao;
    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao;
    @Autowired
    private ReceiveBillVerifyItemDao receiveBillVerifyItemDao;
    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
    @Autowired
    private TocSettlementReceiveBillItemDao tocSettlementReceiveBillItemDao;
    @Autowired
    private TocSettlementReceiveBillCostItemDao tocSettlementReceiveBillCostItemDao;
    @Autowired
    private TocSettlementReceiveBillVerifyItemDao tocSettlementReceiveBillVerifyItemDao;
    @Autowired
    private PaymentBillDao paymentBillDao;
    @Autowired
    private PaymentVerificateItemDao paymentVerificateItemDao;


    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201160012, model = DERP_LOG_POINT.POINT_13201160012_Label)
    public void saveReceiveVerifyItem(String json, String keys, String topics, String tags) throws Exception {

        JSONObject jsonData = JSONObject.fromObject(json);

        Map<String, Object> merParams = new HashMap<>();

        if(jsonData.containsKey("merchantId") && jsonData.get("merchantId") != null) {
            Long merchantId = Long.valueOf(jsonData.getString("merchantId"));
            merParams.put("merchantId", merchantId);
        }

        List<MerchantInfoMongo> merchantList = merchantInfoMongoDao.findAll(merParams);
        Map<Long, String> merchantMap = new HashMap<>();

        for (MerchantInfoMongo merchantInfoMongo : merchantList) {
            merchantMap.put(merchantInfoMongo.getMerchantId(), merchantInfoMongo.getTopidealCode());
        }

        //查询状态为“待核销、部分核销”的tob应收账单
        ReceiveBillDTO receiveBillDTO = new ReceiveBillDTO();
        List<String> billStatusList = new ArrayList<>();
        billStatusList.add(DERP_ORDER.RECEIVEBILL_BILLSTATUS_02);
        billStatusList.add(DERP_ORDER.RECEIVEBILL_BILLSTATUS_03);
        receiveBillDTO.setBillStatusList(billStatusList);
        List<ReceiveBillModel> receiveBillModels = receiveBillDao.listDTO(receiveBillDTO);

        if (!receiveBillModels.isEmpty()) {
            query2BVerify(receiveBillModels, merchantMap);
        }

        //查询状态为“待核销、部分核销”的toc应收账单
        TocSettlementReceiveBillDTO tocSettlementReceiveBillDTO = new TocSettlementReceiveBillDTO();
        List<String> tocBillStatusList = new ArrayList<>();
        tocBillStatusList.add(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_02);
        tocBillStatusList.add(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_03);
        tocSettlementReceiveBillDTO.setBillStatusList(billStatusList);
        List<TocSettlementReceiveBillModel> tocReceiveBills = tocSettlementReceiveBillDao.listTocBill(tocSettlementReceiveBillDTO);

        if (!tocReceiveBills.isEmpty()) {
            query2CVerify(tocReceiveBills, merchantMap);
        }

        //查询状态为“待付款、部分付款”的应付账单
        PaymentBillDTO paymentBillDTO = new PaymentBillDTO();
        List<String> paymentBillStatusList = new ArrayList<>();
        paymentBillStatusList.add(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_03);
        paymentBillStatusList.add(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_07);
        paymentBillDTO.setBillStatusList(paymentBillStatusList);
        List<PaymentBillModel> paymentBillModels = paymentBillDao.listByDto(paymentBillDTO);

        if (!paymentBillModels.isEmpty()) {
            queryPaymentVerify(paymentBillModels, merchantMap);
        }

    }


    /**
     * @param billVerifyMap 响应信息
     * @param requestText   请求报文
     * @return
     * @Description 推送nc接口12
     */
    private void sendNC12(Map<String, List<BillVerifyDetail>> billVerifyMap, String requestText) {
        //提交NC
        String ncResult = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_12.getUri(), requestText);

        JSONObject resultJson = JSONObject.fromObject(ncResult);
        if (resultJson.getString("code").equals("1002")) {
            throw new RuntimeException(resultJson.getString("msg"));
        }
        JSONArray dataArray = resultJson.getJSONArray("rsadata");

        for (Object object : dataArray) {

            JSONObject voucherBillJson = JSONObject.fromObject(object);

            String ncId = voucherBillJson.getString("id");
            String zdh = voucherBillJson.getString("zdh");
            String billno2 = voucherBillJson.getString("billNo2");
            String busiDate = voucherBillJson.getString("busiDate");
            String state = voucherBillJson.getString("state");
            String verifyDetailCreator = voucherBillJson.getString("verifyDetailCreator");
            String moneyde = voucherBillJson.getString("moneyde");
            String moneycr = voucherBillJson.getString("moneycr");
            String scomment = voucherBillJson.getString("scomment");

            if (StringUtils.isBlank(ncId)) {
                throw new RuntimeException("唯一主键id不能为空");
            }

            if (StringUtils.isBlank(zdh)) {
                throw new RuntimeException("账单号不能为空");
            }

            if (StringUtils.isBlank(billno2)) {
                throw new RuntimeException("对应核销单号不能为空");
            }

            if (StringUtils.isBlank(busiDate)) {
                throw new RuntimeException("处理日期不能为空");
            }

            if (StringUtils.isBlank(verifyDetailCreator)) {
                throw new RuntimeException("核销明细处理人不能为空");
            }

            if (StringUtils.isBlank(state)) {
                throw new RuntimeException("核销记录状态不能为空");
            }

            if (StringUtils.isBlank(moneyde) && StringUtils.isBlank(moneycr)) {
                throw new RuntimeException("借方处理原币金额/贷方处理原币金额不能同时为空");
            }

            if (StringUtils.isNotBlank(scomment) && !(scomment.equals("同币种核销") || scomment.equals("异币种核销"))) {
                continue;
            }

            BillVerifyDetail billVerifyDetail = new BillVerifyDetail();
            billVerifyDetail.setNcId(Integer.valueOf(ncId));
            billVerifyDetail.setZdh(zdh);
            billVerifyDetail.setBillNo2(billno2);
            billVerifyDetail.setBusiDate(busiDate);
            billVerifyDetail.setState(state);
            billVerifyDetail.setVerifyDetailCreator(verifyDetailCreator);
            billVerifyDetail.setMoneycr(new BigDecimal("0"));
            billVerifyDetail.setMoneyde(new BigDecimal("0"));

            if (StringUtils.isNotBlank(moneycr)) {
                billVerifyDetail.setMoneycr(new BigDecimal(moneycr));
            }

            if (StringUtils.isNotBlank(moneyde)) {
                billVerifyDetail.setMoneyde(new BigDecimal(moneyde));
            }


            List<BillVerifyDetail> verifyItemModels = new ArrayList<>();

            if (billVerifyMap.containsKey(zdh)) {
                verifyItemModels.addAll(billVerifyMap.get(zdh));
            }

            verifyItemModels.add(billVerifyDetail);
            billVerifyMap.put(zdh, verifyItemModels);
        }
    }

    /**
     * @param receiveBillModels 待核销、部分核销的应收账单
     * @param merchantMap       公司集合
     * @return
     * @Description 应收账单推送nc接口12
     */
    private void query2BVerify(List<ReceiveBillModel> receiveBillModels, Map<Long, String> merchantMap) throws Exception {
        /**************************tob应收单核销明细获取*********************************/
        Map<Long, List<String>> mer2BBillMap = new HashMap<>();

        for (ReceiveBillModel receiveBillModel : receiveBillModels) {

            List<String> billCodes = new ArrayList<>();

            if (mer2BBillMap.containsKey(receiveBillModel.getMerchantId())) {
                billCodes.addAll(mer2BBillMap.get(receiveBillModel.getMerchantId()));
            }

            billCodes.add(receiveBillModel.getCode());
            mer2BBillMap.put(receiveBillModel.getMerchantId(), billCodes);
        }

        for (Long merchantId : mer2BBillMap.keySet()) {
            BillVerifyQueryRoot billVerifyQueryRoot = new BillVerifyQueryRoot();
            billVerifyQueryRoot.setSourceCode(ApolloUtil.ncSourceType);
            billVerifyQueryRoot.setCorCcode(merchantMap.get(merchantId));
            billVerifyQueryRoot.setType("1");

            List<String> billCodes = mer2BBillMap.get(merchantId);
            billVerifyQueryRoot.setZdh(String.join(",", billCodes));

            JSONObject rootJson = JSONObject.fromObject(billVerifyQueryRoot);
            String clearText = rootJson.toString();

            Map<String, List<BillVerifyDetail>> billVerifyMap = new HashMap<>();

            sendNC12(billVerifyMap, clearText);

            //作废的核销明细id
            List<Long> delIds = new ArrayList<>();
            //新增的核销明细
            List<ReceiveBillVerifyItemModel> newVerifyItems = new ArrayList<>();
            //更新应收账单集合
            List<Long> updateBillIds1 = new ArrayList<>();
            List<Long> updateBillIds2 = new ArrayList<>();

            for (String billCode : billVerifyMap.keySet()) {

                List<BillVerifyDetail> queryVerifyModels = billVerifyMap.get(billCode);

                ReceiveBillModel queryModel = new ReceiveBillModel();
                queryModel.setCode(billCode);

                queryModel = receiveBillDao.searchByModel(queryModel);

                if (queryModel == null) {
                    LOG.info("经分销系统查询无此结算单据");
                    continue;
                }

                //作废的核销唯一id
                List<Long> ncIds = new ArrayList<>();

                //已存在的核销唯一id
                List<Long> existIds = new ArrayList<>();

                for (BillVerifyDetail dto : queryVerifyModels) {
                    if ("2".equals(dto.getState())) {
                        ncIds.add(Long.valueOf(dto.getNcId()));
                    }
                }

                //已核销金额
                BigDecimal verifyAmount = new BigDecimal("0");
                ReceiveBillVerifyItemModel verifyItemModel = new ReceiveBillVerifyItemModel();
                verifyItemModel.setBillId(queryModel.getId());
                List<ReceiveBillVerifyItemModel> verifyItemModels = receiveBillVerifyItemDao.list(verifyItemModel);

                for (ReceiveBillVerifyItemModel itemModel : verifyItemModels) {
                    if (itemModel.getNcId() != null) {
                        existIds.add(itemModel.getNcId());
                        if (ncIds.contains(itemModel.getNcId())) {
                            delIds.add(itemModel.getId());
                            continue;
                        }
                    }

                    verifyAmount = verifyAmount.add(itemModel.getPrice());
                }

                //应收单应收金额
                BigDecimal receivablePrice = receiveBillItemDao.getTotalReceivePrice(queryModel.getId());
                BigDecimal costFree = receiveBillCostItemDao.getTotalReceivePrice(queryModel.getId());
                receivablePrice = receivablePrice == null ? new BigDecimal("0") : receivablePrice;
                costFree = costFree == null ? new BigDecimal("0") : costFree;
                BigDecimal totalPrice = receivablePrice.add(costFree);

                //未核销金额
                BigDecimal nonVerifyAmount = totalPrice.subtract(verifyAmount);

                for (BillVerifyDetail itemDTO : queryVerifyModels) {

                    if ("2".equals(itemDTO.getState()) || existIds.contains(Long.valueOf(itemDTO.getNcId()))) {
                        continue;
                    }

                    if (nonVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
                        break;
                    }

                    BigDecimal verifyPrice = itemDTO.getMoneyde();

                    if (nonVerifyAmount.compareTo(verifyPrice) < 0) {
                        continue;
                    }

                    ReceiveBillVerifyItemModel itemModel = new ReceiveBillVerifyItemModel();

                    itemModel.setBillId(queryModel.getId());
                    itemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_2);
                    itemModel.setCreateDate(TimeUtils.getNow());
                    itemModel.setNcId(Long.valueOf(itemDTO.getNcId()));
                    itemModel.setPrice(verifyPrice);
                    itemModel.setReceiceNo(itemDTO.getBillNo2());
                    itemModel.setVerifier(itemDTO.getVerifyDetailCreator());
                    itemModel.setReceiveDate(TimeUtils.parseFullTime(itemDTO.getBusiDate()));
                    itemModel.setVerifyDate(TimeUtils.getNow());


                    newVerifyItems.add(itemModel);
                    nonVerifyAmount = nonVerifyAmount.subtract(verifyPrice);
                }

                if (nonVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
                    updateBillIds1.add(queryModel.getId());
                } else {
                    updateBillIds2.add(queryModel.getId());
                }

            }

            //删除作废核销明细
            if (!delIds.isEmpty()) {
                receiveBillVerifyItemDao.delete(delIds);
            }

            //批量保存核销明细
            if (!newVerifyItems.isEmpty()) {
                receiveBillVerifyItemDao.batchSave(newVerifyItems);
            }

            //更新应收账单账单状态
            if (!updateBillIds1.isEmpty()) {
                receiveBillDao.batchUpdateBillStatus(updateBillIds1, DERP_ORDER.RECEIVEBILL_BILLSTATUS_04, null);
            }

            if (!updateBillIds2.isEmpty()) {
                receiveBillDao.batchUpdateBillStatus(updateBillIds2, DERP_ORDER.RECEIVEBILL_BILLSTATUS_03, null);
            }

        }
    }

    /**
     * @param receiveBillModels 待核销、部分核销的toc应收账单
     * @param merchantMap       公司集合
     * @return
     * @Description toc应收账单推送nc接口12
     */
    private void query2CVerify(List<TocSettlementReceiveBillModel> receiveBillModels, Map<Long, String> merchantMap) throws Exception {
        /**************************tob应收单核销明细获取*********************************/
        Map<Long, List<String>> mer2BBillMap = new HashMap<>();

        for (TocSettlementReceiveBillModel receiveBillModel : receiveBillModels) {

            List<String> billCodes = new ArrayList<>();

            if (mer2BBillMap.containsKey(receiveBillModel.getMerchantId())) {
                billCodes.addAll(mer2BBillMap.get(receiveBillModel.getMerchantId()));
            }

            billCodes.add(receiveBillModel.getCode());
            mer2BBillMap.put(receiveBillModel.getMerchantId(), billCodes);
        }

        for (Long merchantId : mer2BBillMap.keySet()) {
            BillVerifyQueryRoot billVerifyQueryRoot = new BillVerifyQueryRoot();
            billVerifyQueryRoot.setSourceCode(ApolloUtil.ncSourceType);
            billVerifyQueryRoot.setCorCcode(merchantMap.get(merchantId));
            billVerifyQueryRoot.setType("1");

            List<String> billCodes = mer2BBillMap.get(merchantId);
            billVerifyQueryRoot.setZdh(String.join(",", billCodes));

            JSONObject rootJson = JSONObject.fromObject(billVerifyQueryRoot);
            String clearText = rootJson.toString();

            Map<String, List<BillVerifyDetail>> billVerifyMap = new HashMap<>();

            sendNC12(billVerifyMap, clearText);

            //作废的核销明细id
            List<Long> delIds = new ArrayList<>();
            //新增的核销明细
            List<TocSettlementReceiveBillVerifyItemModel> newVerifyItems = new ArrayList<>();
            //更新应收账单集合
            List<Long> updateBillIds1 = new ArrayList<>();
            List<Long> updateBillIds2 = new ArrayList<>();

            for (String billCode : billVerifyMap.keySet()) {

                List<BillVerifyDetail> queryVerifyModels = billVerifyMap.get(billCode);

                TocSettlementReceiveBillModel queryModel = new TocSettlementReceiveBillModel();
                queryModel.setCode(billCode);

                queryModel = tocSettlementReceiveBillDao.searchByModel(queryModel);

                if (queryModel == null) {
                    LOG.info("经分销系统查询无此结算单据");
                    continue;
                }

                //已存在的核销唯一id
                List<Long> existIds = new ArrayList<>();
                //作废的核销唯一id
                List<Long> ncIds = new ArrayList<>();
                for (BillVerifyDetail dto : queryVerifyModels) {
                    if ("2".equals(dto.getState())) {
                        ncIds.add(Long.valueOf(dto.getNcId()));
                    }
                }

                //已核销金额
                BigDecimal verifyAmount = new BigDecimal("0");
                TocSettlementReceiveBillVerifyItemModel verifyItemModel = new TocSettlementReceiveBillVerifyItemModel();
                verifyItemModel.setBillId(queryModel.getId());
                List<TocSettlementReceiveBillVerifyItemModel> verifyItemModels = tocSettlementReceiveBillVerifyItemDao.list(verifyItemModel);

                for (TocSettlementReceiveBillVerifyItemModel itemModel : verifyItemModels) {
                    if (itemModel.getNcId() != null) {
                        existIds.add(itemModel.getNcId());
                        if (ncIds.contains(itemModel.getNcId())) {
                            delIds.add(itemModel.getId());
                            continue;
                        }
                    }
                    verifyAmount = verifyAmount.add(itemModel.getPrice());
                }

                //应收单应收金额
                BigDecimal receivablePrice = tocSettlementReceiveBillItemDao.getTotalReceivePrice(queryModel.getId());
                BigDecimal costFree = tocSettlementReceiveBillCostItemDao.getTotalReceivePrice(queryModel.getId());
                receivablePrice = receivablePrice == null ? new BigDecimal("0") : receivablePrice;
                costFree = costFree == null ? new BigDecimal("0") : costFree;
                BigDecimal totalPrice = receivablePrice.add(costFree);

                //未核销金额
                BigDecimal nonVerifyAmount = totalPrice.subtract(verifyAmount);

                for (BillVerifyDetail itemDTO : queryVerifyModels) {

                    if ("2".equals(itemDTO.getState()) || existIds.contains(Long.valueOf(itemDTO.getNcId()))) {
                        continue;
                    }

                    if (nonVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
                        break;
                    }

                    BigDecimal verifyPrice = itemDTO.getMoneyde();

                    if (nonVerifyAmount.compareTo(verifyPrice) < 0) {
                        continue;
                    }

                    TocSettlementReceiveBillVerifyItemModel itemModel = new TocSettlementReceiveBillVerifyItemModel();
                    itemModel.setNcId(Long.valueOf(itemDTO.getNcId()));
                    itemModel.setPrice(verifyPrice);
                    itemModel.setReceiceNo(itemDTO.getBillNo2());
                    itemModel.setVerifier(itemDTO.getVerifyDetailCreator());
                    itemModel.setBillId(queryModel.getId());
                    itemModel.setCreateDate(TimeUtils.getNow());
                    itemModel.setReceiveDate(TimeUtils.parseFullTime(itemDTO.getBusiDate()));
                    itemModel.setVerifyDate(TimeUtils.getNow());

                    newVerifyItems.add(itemModel);
                    nonVerifyAmount = nonVerifyAmount.subtract(verifyPrice);
                }

                if (nonVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
                    updateBillIds1.add(queryModel.getId());
                } else {
                    updateBillIds2.add(queryModel.getId());
                }

            }

            //删除作废核销明细
            if (!delIds.isEmpty()) {
                tocSettlementReceiveBillVerifyItemDao.delete(delIds);
            }

            //批量保存核销明细
            if (!newVerifyItems.isEmpty()) {
                tocSettlementReceiveBillVerifyItemDao.batchSave(newVerifyItems);
            }

            //更新应收账单账单状态
            if (!updateBillIds1.isEmpty()) {
                tocSettlementReceiveBillDao.batchUpdateBillStatus(updateBillIds1, DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_04);
            }

            if (!updateBillIds2.isEmpty()) {
                tocSettlementReceiveBillDao.batchUpdateBillStatus(updateBillIds2, DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_03);
            }

        }
    }

    /**
     * @param paymentBillModels 待付款、部分付款的应付账单
     * @param merchantMap       公司集合
     * @return
     * @Description 应付账单推送nc接口12
     */
    private void queryPaymentVerify(List<PaymentBillModel> paymentBillModels, Map<Long, String> merchantMap) throws Exception {
        /**************************tob应收单核销明细获取*********************************/
        Map<Long, List<String>> mer2BBillMap = new HashMap<>();

        for (PaymentBillModel paymentBillModel : paymentBillModels) {

            List<String> billCodes = new ArrayList<>();

            if (mer2BBillMap.containsKey(paymentBillModel.getMerchantId())) {
                billCodes.addAll(mer2BBillMap.get(paymentBillModel.getMerchantId()));
            }

            billCodes.add(paymentBillModel.getCode());
            mer2BBillMap.put(paymentBillModel.getMerchantId(), billCodes);
        }

        for (Long merchantId : mer2BBillMap.keySet()) {
            BillVerifyQueryRoot billVerifyQueryRoot = new BillVerifyQueryRoot();
            billVerifyQueryRoot.setSourceCode(ApolloUtil.ncSourceType);
            billVerifyQueryRoot.setCorCcode(merchantMap.get(merchantId));
            billVerifyQueryRoot.setType("2");

            List<String> billCodes = mer2BBillMap.get(merchantId);
            billVerifyQueryRoot.setZdh(String.join(",", billCodes));

            JSONObject rootJson = JSONObject.fromObject(billVerifyQueryRoot);
            String clearText = rootJson.toString();

            Map<String, List<BillVerifyDetail>> billVerifyMap = new HashMap<>();

            sendNC12(billVerifyMap, clearText);

            //作废的核销明细id
            List<Long> delIds = new ArrayList<>();
            //新增的核销明细
            List<PaymentVerificateItemModel> newVerifyItems = new ArrayList<>();
            //更新应付账单集合
            List<PaymentBillModel> updateModels = new ArrayList<>();

            for (String billCode : billVerifyMap.keySet()) {

                List<BillVerifyDetail> queryVerifyModels = billVerifyMap.get(billCode);

                PaymentBillModel queryModel = new PaymentBillModel();
                queryModel.setCode(billCode);

                queryModel = paymentBillDao.searchByModel(queryModel);

                if (queryModel == null) {
                    LOG.info("经分销系统查询无此结算单据");
                    continue;
                }

                //已存在的核销唯一id
                List<Long> existIds = new ArrayList<>();

                //作废的核销唯一id
                List<Long> ncIds = new ArrayList<>();
                for (BillVerifyDetail dto : queryVerifyModels) {
                    if ("2".equals(dto.getState())) {
                        ncIds.add(Long.valueOf(dto.getNcId()));
                    }
                }

                //已付款金额
                BigDecimal verifyAmount = new BigDecimal("0");
                PaymentVerificateItemModel verifyItemModel = new PaymentVerificateItemModel();
                verifyItemModel.setPaymentId(queryModel.getId());
                List<PaymentVerificateItemModel> verifyItemModels = paymentVerificateItemDao.list(verifyItemModel);

                for (PaymentVerificateItemModel itemModel : verifyItemModels) {
                    if (itemModel.getNcId() != null) {
                        existIds.add(itemModel.getNcId());
                        if (ncIds.contains(itemModel.getNcId())) {
                            delIds.add(itemModel.getId());
                            continue;
                        }
                    }
                    verifyAmount = verifyAmount.add(itemModel.getCurrentVerificateAmount());
                }

                //待付款金额
                BigDecimal nonVerifyAmount = queryModel.getPayableAmount().subtract(verifyAmount);

                for (BillVerifyDetail itemDTO : queryVerifyModels) {

                    if ("2".equals(itemDTO.getState()) || existIds.contains(Long.valueOf(itemDTO.getNcId()))) {
                        continue;
                    }

                    if (nonVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
                        break;
                    }

                    BigDecimal verifyPrice = itemDTO.getMoneycr();

                    if (nonVerifyAmount.compareTo(verifyPrice) < 0) {
                        continue;
                    }

                    PaymentVerificateItemModel itemModel = new PaymentVerificateItemModel();
                    itemModel.setNcId(Long.valueOf(itemDTO.getNcId()));
                    itemModel.setCurrentVerificateAmount(verifyPrice);
                    itemModel.setSerialNo(itemDTO.getBillNo2());
                    itemModel.setDrawee(itemDTO.getVerifyDetailCreator());
                    itemModel.setPaymentDate(TimeUtils.parseFullTime(itemDTO.getBusiDate()));
                    itemModel.setPaymentId(queryModel.getId());
                    itemModel.setBillStatus(DERP_ORDER.PAYMENT_BILL_VERIFICATE_BILL_STATUS_2);
                    itemModel.setCreateDate(TimeUtils.getNow());

                    newVerifyItems.add(itemModel);
                    nonVerifyAmount = nonVerifyAmount.subtract(verifyPrice);
                }

                verifyAmount = queryModel.getPayableAmount().subtract(nonVerifyAmount);
                PaymentBillModel updateModel = new PaymentBillModel();
                updateModel.setId(queryModel.getId());
                updateModel.setSurplusAmount(nonVerifyAmount);
                updateModel.setPaymentAmount(verifyAmount);
                if (nonVerifyAmount.compareTo(new BigDecimal("0")) == 0) {
                    updateModel.setBillStatus(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_04);
                } else {
                    updateModel.setBillStatus(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_07);
                }
                updateModels.add(updateModel);
            }

            //删除作废核销明细
            if (!delIds.isEmpty()) {
                paymentVerificateItemDao.delete(delIds);
            }

            //批量保存核销明细
            if (!newVerifyItems.isEmpty()) {
                paymentVerificateItemDao.batchSave(newVerifyItems);
            }

            //更新应付账单账单状态
            if (!updateModels.isEmpty()) {
                paymentBillDao.batchUpdate(updateModels);
            }

        }
    }

}
