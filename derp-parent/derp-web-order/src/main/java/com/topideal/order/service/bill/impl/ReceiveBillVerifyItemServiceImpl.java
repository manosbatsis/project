package com.topideal.order.service.bill.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.entity.dto.bill.ReceiveBillVerifyItemDTO;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.mongo.dao.AttachmentInfoMongoDao;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.order.service.bill.ReceiveBillVerifyItemService;
import com.topideal.order.shiro.ShiroUtils;

import com.topideal.order.webapi.bill.form.ReceiveBillVerifyForm;
import com.topideal.order.webapi.bill.form.ReceiveBillVerifyItemForm;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultDefaultValueProcessor;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Description: 应收账单service实现类
 * @Author: Chen Yiluan
 * @Date: 2020/07/27 15:01
 **/
@Service
public class ReceiveBillVerifyItemServiceImpl implements ReceiveBillVerifyItemService {

    @Autowired
    private ReceiveBillVerifyItemDao receiveBillVerifyItemDao;
    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao;
    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private ReceiveBillItemDao receiveBillItemDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao ;
    @Autowired
    private AdvanceBillDao advanceBillDao;
    @Autowired
    private AdvanceBillItemDao advanceBillItemDao;
    @Autowired
    private AdvanceBillOperateItemDao advanceBillOperateItemDao;
    @Autowired
    private AdvanceBillVerifyItemDao advanceBillVerifyItemDao;
    @Autowired
    private ReceiveBillOperateDao receiveBillOperateDao;
    @Autowired
    private TobTemporaryReceiveBillDao tobTemporaryReceiveBillDao;
    @Autowired
    private ReceiveCloseAccountsDao receiveCloseAccountsDao;

    @Override
    public List<ReceiveBillVerifyItemModel> listByBillId(Long billId) throws SQLException {
        ReceiveBillVerifyItemModel itemModel = new ReceiveBillVerifyItemModel();
        itemModel.setBillId(billId);
        return receiveBillVerifyItemDao.list(itemModel);
    }

    @Override
    public Map<String, String> saveVerifyItem(ReceiveBillVerifyItemDTO dto, User user) throws Exception {
        Map<String, String> resMap = new HashedMap();
        String receiveDate = dto.getReceiveDateStr();
        String today = TimeUtils.formatDay(new Date());
        if (today.compareTo(receiveDate) < 0) {
            resMap.put("code", "01");
            resMap.put("message", "收款日期不得晚于当前日期");
            return resMap;
        }

        //核销金额是否大于应收金额
        BigDecimal receivablePrice = receiveBillItemDao.getTotalReceivePrice(dto.getBillId());
        BigDecimal costFree = receiveBillCostItemDao.getTotalReceivePrice(dto.getBillId());
        BigDecimal verifyPrice = receiveBillVerifyItemDao.getTotalVerifyPrice(dto.getBillId());
        receivablePrice = receivablePrice == null ? new BigDecimal("0") : receivablePrice;
        costFree = costFree == null ? new BigDecimal("0") : costFree;
        verifyPrice = verifyPrice == null ? new BigDecimal("0") : verifyPrice;
        BigDecimal totalPrice = receivablePrice.add(costFree);
        BigDecimal tempPrice = verifyPrice.add(dto.getPrice());
        if (totalPrice.abs().compareTo(tempPrice.abs()) == -1) {
            resMap.put("code", "01");
            resMap.put("message", "核销金额不得大于应收金额");
            return resMap;
        }
        Timestamp verifyDate = TimeUtils.getNow();
        ReceiveBillVerifyItemModel verifyItemModel = new ReceiveBillVerifyItemModel();
        verifyItemModel.setBillId(dto.getBillId());
        verifyItemModel.setReceiceNo(dto.getReceiceNo());
        verifyItemModel.setReceiveDate(TimeUtils.parseDay(receiveDate));
        verifyItemModel.setVerifier(user.getName());
        verifyItemModel.setVerifyId(user.getId());
        verifyItemModel.setVerifyDate(verifyDate);
        verifyItemModel.setPrice(dto.getPrice());
        verifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_2);
        receiveBillVerifyItemDao.save(verifyItemModel);

        ReceiveBillModel receiveBillModel = new ReceiveBillModel();
        receiveBillModel.setId(dto.getBillId());
        if ((receivablePrice.add(costFree)).compareTo(verifyPrice.add(dto.getPrice())) == 0) {
            receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_04);
        } else {
            receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_03);
        }

        //操作日志节点
        ReceiveBillOperateModel receiveBillOperateModel = new ReceiveBillOperateModel();
        receiveBillOperateModel.setBillId(dto.getBillId());
        receiveBillOperateModel.setOperateDate(verifyDate);
        receiveBillOperateModel.setCreateDate(verifyDate);
        receiveBillOperateModel.setOperateId(user.getId());
        receiveBillOperateModel.setOperator(user.getName());
        receiveBillOperateModel.setOperateNode(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_8);
        receiveBillOperateDao.save(receiveBillOperateModel);

        if ((receivablePrice.add(costFree)).compareTo(verifyPrice.add(dto.getPrice())) == 0) {
            TobTemporaryReceiveBillModel tobTemporaryReceiveBillModel = new TobTemporaryReceiveBillModel();
            tobTemporaryReceiveBillModel.setReceiveId(dto.getBillId());
            List<TobTemporaryReceiveBillModel> tobTemporaryReceiveBillModels = tobTemporaryReceiveBillDao.list(tobTemporaryReceiveBillModel);
            for (TobTemporaryReceiveBillModel temporaryReceiveBillModel : tobTemporaryReceiveBillModels) {
                if (DERP_ORDER.TOBTEMPRECEIVEBILL_SALETYPE_1.equals(temporaryReceiveBillModel.getSaleType())) {
                    Timestamp latestVerifyDate = receiveBillVerifyItemDao.getLatestVerifyDate(dto.getBillId());
                    //实际回款日期
                    temporaryReceiveBillModel.setPaymentRealDate(latestVerifyDate);
                }
//                temporaryReceiveBillModel.setStatus(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_5);
            }

            if (!tobTemporaryReceiveBillModels.isEmpty()) {
                tobTemporaryReceiveBillDao.batchUpdate(tobTemporaryReceiveBillModels);
            }
        }

        receiveBillDao.modify(receiveBillModel);

        /**
         * 发送核销提醒邮件
         */
        ReceiveBillModel billModel = receiveBillDao.searchById(dto.getBillId());
        Map<String, Object> attQueryMap = new HashMap<String, Object>();
        attQueryMap.put("relationCode", billModel.getCode());
        List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);

        JSONArray attArray = new JSONArray();
        
        for (AttachmentInfoMongo att : attList) {
        	
        	Map<String, Object> tempMap = new HashMap<String, Object>() ;
        	tempMap.put("attachmentName", att.getAttachmentName()) ;
            String attachmentUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                    + att.getAttachmentName() + "&url=" + URLEncoder.encode(att.getAttachmentUrl());
        	tempMap.put("attachmentUrl", attachmentUrl) ;

            JSONObject attJson = new JSONObject();
            attJson.putAll(tempMap);

            attArray.add(attJson);
        }
        BigDecimal unVeriPrice = receivablePrice.add(costFree).subtract(verifyPrice).subtract(dto.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP));

        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO();

        emailUserDTO.setBuId(billModel.getBuId());
        emailUserDTO.setBuName(billModel.getBuName());
        emailUserDTO.setMerchantId(billModel.getMerchantId());
        emailUserDTO.setMerchantName(billModel.getMerchantName());
        emailUserDTO.setOrderCode(billModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_4);
        emailUserDTO.setSupplier(billModel.getCustomerName());
        emailUserDTO.setAmount(billModel.getCurrency() + "&nbsp;" + receivablePrice.add(costFree).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        emailUserDTO.setVeriAmount(billModel.getCurrency() + "&nbsp;" + dto.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        emailUserDTO.setUnVeriAmount(billModel.getCurrency() + "&nbsp;" + unVeriPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        emailUserDTO.setVerificationId(user.getId());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setAttArray(attArray);
        JsonConfig jsonConfig = new JsonConfig();
        getReceiveOperators(billModel.getId(), emailUserDTO, jsonConfig);
        JSONObject emailJson = JSONObject.fromObject(emailUserDTO, jsonConfig);

        rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                MQErpEnum.SEND_REMINDER_MAIL.getTags());


        /*收款核销跟踪*/
        Map<String, Object> verifyMap = new HashMap<String, Object>();
        verifyMap.put("receiveCodes", billModel.getCode());
        verifyMap.put("orderType", DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_0);
        verifyMap.put("operateType", "0");
        verifyMap.put("notes", "应收账单核销");
        JSONObject verifyJson = JSONObject.fromObject(verifyMap);
        rocketMQProducer.send(verifyJson.toString(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTopic(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTags());

        resMap.put("code", "00");
        resMap.put("message", "保存成功");
        return resMap;
    }

    @Override
    public Map<String, String> saveAdvanceVerifyItem(String advanceIds) throws Exception {

        List<Long> ids = StrUtils.parseIds(advanceIds);
        List<AdvanceBillModel> advanceBillModels = advanceBillDao.listByIds(ids);

        List<ReceiveBillVerifyItemModel> verifyItemModels = new ArrayList<>();

        Map<Long, AdvanceBillModel> advanceBillModelMap = new HashMap<>();

        Map<Long, BigDecimal> priceMap = new HashMap<>();

        //待核销的预收单 id 集合
        List<Long> auditIds = new ArrayList<>();
        //部分核销/已核销的预收单 id 集合
        List<Long> verifyIds = new ArrayList<>();

        for (AdvanceBillModel advanceBillModel : advanceBillModels) {

            if (DERP_ORDER.RECEIVEBILL_BILLSTATUS_02.equals(advanceBillModel.getBillStatus())) {
                auditIds.add(advanceBillModel.getId());
            } else {
                verifyIds.add(advanceBillModel.getId());
            }
            advanceBillModelMap.put(advanceBillModel.getId(), advanceBillModel);
        }

        List<Long> allIds = new ArrayList<>();
        allIds.addAll(auditIds);
        allIds.addAll(verifyIds);
        List<Map<String, Object>> priceList = advanceBillItemDao.listItemPrice(allIds);

        for (Map<String, Object> map : priceList) {
            Long advanceId = (Long) map.get("advanceId");
            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
            priceMap.put(advanceId, totalPrice);
        }

        if (!auditIds.isEmpty()) {
            //如果预收单的订单状态为“待核销”， 核销时间取最新审核时间
            List<Map<String, Object>> auditMapList = advanceBillOperateItemDao.getMaxAuditDate(auditIds);


            for (Map<String, Object> map : auditMapList) {

                Long advanceId = (Long) map.get("advanceId");
                Date operateDate = (Date) map.get("operateDate");

                AdvanceBillModel advanceBillModel = advanceBillModelMap.get(advanceId);

                ReceiveBillVerifyItemModel receiveBillVerifyItemModel = new ReceiveBillVerifyItemModel();
                receiveBillVerifyItemModel.setAdvanceId(advanceId);
                receiveBillVerifyItemModel.setAdvanceCode(advanceBillModel.getCode());
                receiveBillVerifyItemModel.setPrice(priceMap.get(advanceId));
                receiveBillVerifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1);
                receiveBillVerifyItemModel.setReceiveDate(new Timestamp(operateDate.getTime()));
                verifyItemModels.add(receiveBillVerifyItemModel);
            }

        }

        Map<Long, ReceiveBillVerifyItemModel> verifyItemModelMap = new HashMap<>();
        if (!verifyIds.isEmpty()) {
            List<AdvanceBillVerifyItemModel> advanceBillVerifyItemModels = advanceBillVerifyItemDao.getAdvancesByIds(verifyIds);


            //如果预收单的订单状态不为“待核销”， 核销时间取最新一条的核销时间
            for (AdvanceBillVerifyItemModel verifyItemModel : advanceBillVerifyItemModels) {
                ReceiveBillVerifyItemModel receiveBillVerifyItemModel = verifyItemModelMap.get(verifyItemModel.getAdvanceId());

                if (receiveBillVerifyItemModel == null) {
                    AdvanceBillModel advanceBillModel = advanceBillModelMap.get(verifyItemModel.getAdvanceId());

                    receiveBillVerifyItemModel = new ReceiveBillVerifyItemModel();
                    receiveBillVerifyItemModel.setAdvanceId(verifyItemModel.getAdvanceId());
                    receiveBillVerifyItemModel.setAdvanceCode(advanceBillModel.getCode());
                    receiveBillVerifyItemModel.setReceiveDate(new Timestamp(verifyItemModel.getVerifyDate().getTime()));
                    receiveBillVerifyItemModel.setReceiceNo(verifyItemModel.getVerifyNo());
                    receiveBillVerifyItemModel.setVerifier(verifyItemModel.getVerifier());
                    receiveBillVerifyItemModel.setVerifyId(verifyItemModel.getVerifierId());
                    receiveBillVerifyItemModel.setPrice(priceMap.get(verifyItemModel.getAdvanceId()));
                    receiveBillVerifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1);
                } else {
                    String verifyNo = verifyItemModel.getVerifyNo() + "&" + receiveBillVerifyItemModel.getReceiceNo();
                    receiveBillVerifyItemModel.setReceiceNo(verifyNo);
                    if (TimeUtils.timeComparisonSize(receiveBillVerifyItemModel.getReceiveDate(), new Timestamp(verifyItemModel.getVerifyDate().getTime())) == "1") {
                        receiveBillVerifyItemModel.setReceiveDate(new Timestamp(verifyItemModel.getVerifyDate().getTime()));
                        receiveBillVerifyItemModel.setVerifier(verifyItemModel.getVerifier());
                        receiveBillVerifyItemModel.setVerifyId(verifyItemModel.getVerifierId());
                    }
                }
                verifyItemModelMap.put(verifyItemModel.getAdvanceId(),receiveBillVerifyItemModel);
            }
        }

        for (Long advanceId : verifyItemModelMap.keySet()) {
            ReceiveBillVerifyItemModel receiveBillVerifyItemModel = verifyItemModelMap.get(advanceId);
            verifyItemModels.add(receiveBillVerifyItemModel);
        }

        for (ReceiveBillVerifyItemModel verifyItemModel : verifyItemModels) {
            receiveBillVerifyItemDao.save(verifyItemModel);
        }
        return null;
    }

    @Override
    public Map<String, String> saveAPIVerifyItem(ReceiveBillVerifyForm form, User user) throws Exception {

        Map<String, String> resMap = new HashedMap();

        ReceiveBillModel receiveBill = receiveBillDao.searchById(form.getBillId());

        //应收账单的应收金额
        BigDecimal receivablePrice = receiveBillItemDao.getTotalReceivePrice(form.getBillId());
        BigDecimal costFree = receiveBillCostItemDao.getTotalReceivePrice(form.getBillId());
        //应收账单已核销金额
        BigDecimal verifiedPrice = receiveBillVerifyItemDao.getTotalVerifyPrice(form.getBillId());
        //本次核销金额
        BigDecimal verifyPrice = new BigDecimal("0");

        BigDecimal totalPrice = receivablePrice.add(costFree);

        List<ReceiveBillVerifyItemForm> verifyItems = form.getVerifyItems();

        List<Long> advanceIds = new ArrayList<>();
        Map<Long, BigDecimal> verifyAmountMap = new HashMap<>();
        for (ReceiveBillVerifyItemForm verifyItemForm : verifyItems) {

            String receiveDate = verifyItemForm.getReceiveDateStr();
            String today = TimeUtils.formatDay(new Date());
            if (today.compareTo(receiveDate) < 0) {
                resMap.put("code", "01");
                resMap.put("message", "收款日期不得晚于当前日期");
                return resMap;
            }

            if (StringUtils.isBlank(verifyItemForm.getVerifyMonth())) {
                resMap.put("code", "01");
                resMap.put("message", "核销月份不能为空！");
                return resMap;
            }

            ReceiveCloseAccountsModel model = new ReceiveCloseAccountsModel();
            model.setMonth(verifyItemForm.getVerifyMonth());
            model.setBuId(receiveBill.getBuId());
            model.setMerchantId(receiveBill.getMerchantId());
            ReceiveCloseAccountsModel closeAccountsModel = receiveCloseAccountsDao.searchByModel(model);

            if (closeAccountsModel == null) {
                resMap.put("code", "01");
                resMap.put("message", "收款月份:" + verifyItemForm.getVerifyMonth() + "的应收关账记录不存在！");
                return resMap;
            }

            if (DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030.equals(closeAccountsModel.getStatus())) {
                resMap.put("code", "01");
                resMap.put("message", "收款月份:" + verifyItemForm.getVerifyMonth() + "已关账");
                return resMap;
            }
            model.setStatus(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030);
            ReceiveCloseAccountsModel latestClose = receiveCloseAccountsDao.getLatestByModel(model);

            if (latestClose != null && !TimeUtils.dateStrComparison(verifyItemForm.getVerifyMonth(), latestClose.getMonth(), "yyyy-MM")) {
                resMap.put("code", "01");
                resMap.put("message", "收款月份:" + verifyItemForm.getVerifyMonth() + "不能小于应收关帐月份");
                return resMap;
            }

            verifyPrice = verifyPrice.add(verifyItemForm.getPrice());
            verifiedPrice = verifiedPrice.add(verifyItemForm.getPrice());

            if (DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1.equals(verifyItemForm.getType())) {
                advanceIds.add(verifyItemForm.getAdvanceId());
                verifyAmountMap.put(verifyItemForm.getAdvanceId(), verifyItemForm.getPrice());
            }
        }

        //应收金额和核销金额比较
        if (totalPrice.abs().compareTo(verifiedPrice.abs()) == -1) {
            resMap.put("code", "01");
            resMap.put("message", "核销金额不得大于应收金额");
            return resMap;
        }

        //全部核销的预收单id集合
        List<Long> verifyAdvanceIds = new ArrayList<>();

        //查询预收单的状态及待核销金额
        if (advanceIds.size() > 0) {

            List<AdvanceBillModel> advanceBillModels = advanceBillDao.listByIds(advanceIds);

            //预收单的预收金额
            List<Map<String, Object>> priceList = advanceBillItemDao.listItemPrice(advanceIds);

            //预收单的已核销金额
            List<Map<String, Object>> verifyPriceList = receiveBillVerifyItemDao.getTotalVerifyPriceByAdvanceId(advanceIds);

            Map<Long, BigDecimal> advanceAmountMap = new HashMap<>();
            for (Map<String, Object> map : priceList) {
                Long advanceId = (Long) map.get("advanceId");
                BigDecimal advanceAmount = (BigDecimal) map.get("totalPrice");
                advanceAmountMap.put(advanceId, advanceAmount);
            }

            Map<Long, BigDecimal> advanceVerifyAmountMap = new HashMap<>();
            for (Map<String, Object> map : verifyPriceList) {
                if (map == null) {
                    continue;
                }
                Long advanceId = (Long) map.get("advanceId");
                BigDecimal advanceAmount = (BigDecimal) map.get("price");
                advanceVerifyAmountMap.put(advanceId, advanceAmount);
            }


            for (AdvanceBillModel advanceBillModel : advanceBillModels) {

                if (!DERP_ORDER.ADVANCEBILL_BILLSTATUS_02.equals(advanceBillModel.getBillStatus())) {
                    resMap.put("code", "01");
                    resMap.put("message", "预收单：" + advanceBillModel.getCode() + "的状态不为待核销");
                    return resMap;
                }

                //预收单的已核销金额
                BigDecimal verifiedAmount = advanceVerifyAmountMap.get(advanceBillModel.getId()) == null ? BigDecimal.ZERO : advanceVerifyAmountMap.get(advanceBillModel.getId());

                //预收单的待核销金额
                BigDecimal beVerifyAmount = advanceAmountMap.get(advanceBillModel.getId()).subtract(verifiedAmount);

                if (beVerifyAmount.compareTo(verifyAmountMap.get(advanceBillModel.getId())) == -1) {
                    resMap.put("code", "01");
                    resMap.put("message", "预收单：" + advanceBillModel.getCode() + "核销金额不得大于待核销金额");
                    return resMap;
                }

                if (verifyAmountMap.get(advanceBillModel.getId()).compareTo(beVerifyAmount) == 0) {
                    verifyAdvanceIds.add(advanceBillModel.getId());
                }
            }

        }

        Timestamp verifyDate = TimeUtils.getNow();

        for (ReceiveBillVerifyItemForm verifyItemForm : verifyItems) {
            ReceiveBillVerifyItemModel verifyItemModel = new ReceiveBillVerifyItemModel();
            verifyItemModel.setBillId(form.getBillId());
            verifyItemModel.setReceiceNo(verifyItemForm.getReceiceNo());
            verifyItemModel.setReceiveDate(TimeUtils.parseDay(verifyItemForm.getReceiveDateStr()));
            verifyItemModel.setVerifier(user.getName());
            verifyItemModel.setVerifyId(user.getId());
            verifyItemModel.setVerifyDate(verifyDate);
            verifyItemModel.setPrice(verifyItemForm.getPrice());
            verifyItemModel.setType(verifyItemForm.getType());
            verifyItemModel.setAdvanceId(verifyItemForm.getAdvanceId());
            verifyItemModel.setAdvanceCode(verifyItemForm.getReceiceNo());
            verifyItemModel.setVerifyMonth(verifyItemForm.getVerifyMonth());
            receiveBillVerifyItemDao.save(verifyItemModel);

        }

        ReceiveBillModel receiveBillModel = new ReceiveBillModel();
        receiveBillModel.setId(form.getBillId());
        if (totalPrice.compareTo(verifiedPrice) == 0) {
            receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_04);
        } else {
            receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_03);
        }

        //操作日志节点
        ReceiveBillOperateModel receiveBillOperateModel = new ReceiveBillOperateModel();
        receiveBillOperateModel.setBillId(form.getBillId());
        receiveBillOperateModel.setOperateDate(verifyDate);
        receiveBillOperateModel.setCreateDate(verifyDate);
        receiveBillOperateModel.setOperateId(user.getId());
        receiveBillOperateModel.setOperator(user.getName());
        receiveBillOperateModel.setOperateNode(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_8);
        receiveBillOperateDao.save(receiveBillOperateModel);

        if (totalPrice.compareTo(verifiedPrice) == 0) {
            TobTemporaryReceiveBillModel tobTemporaryReceiveBillModel = new TobTemporaryReceiveBillModel();
            tobTemporaryReceiveBillModel.setReceiveId(form.getBillId());
            List<TobTemporaryReceiveBillModel> tobTemporaryReceiveBillModels = tobTemporaryReceiveBillDao.list(tobTemporaryReceiveBillModel);
            for (TobTemporaryReceiveBillModel temporaryReceiveBillModel : tobTemporaryReceiveBillModels) {
                if (DERP_ORDER.TOBTEMPRECEIVEBILL_SALETYPE_1.equals(temporaryReceiveBillModel.getSaleType())) {
                    Timestamp latestVerifyDate = receiveBillVerifyItemDao.getLatestVerifyDate(form.getBillId());
                    //实际回款日期
                    temporaryReceiveBillModel.setPaymentRealDate(latestVerifyDate);
                }
            }

            if (!tobTemporaryReceiveBillModels.isEmpty()) {
                tobTemporaryReceiveBillDao.batchUpdate(tobTemporaryReceiveBillModels);
            }
        }

        receiveBillDao.modify(receiveBillModel);

        //更新预收单状态
        if (verifyAdvanceIds.size() > 0) {
            advanceBillDao.batchUpdateByIds(verifyAdvanceIds, DERP_ORDER.ADVANCEBILL_BILLSTATUS_04);
        }

        /**
         * 发送核销提醒邮件
         */
        ReceiveBillModel billModel = receiveBillDao.searchById(form.getBillId());
        Map<String, Object> attQueryMap = new HashMap<String, Object>();
        attQueryMap.put("relationCode", billModel.getCode());
        List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);

        JSONArray attArray = new JSONArray();

        for (AttachmentInfoMongo att : attList) {

            Map<String, Object> tempMap = new HashMap<String, Object>() ;
            tempMap.put("attachmentName", att.getAttachmentName()) ;
            String attachmentUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                    + att.getAttachmentName() + "&url=" + URLEncoder.encode(att.getAttachmentUrl());
            tempMap.put("attachmentUrl", attachmentUrl) ;

            JSONObject attJson = new JSONObject();
            attJson.putAll(tempMap);

            attArray.add(attJson);
        }
        BigDecimal unVeriPrice = totalPrice.subtract(verifiedPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO();

        emailUserDTO.setBuId(billModel.getBuId());
        emailUserDTO.setBuName(billModel.getBuName());
        emailUserDTO.setMerchantId(billModel.getMerchantId());
        emailUserDTO.setMerchantName(billModel.getMerchantName());
        emailUserDTO.setOrderCode(billModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_4);
        emailUserDTO.setSupplier(billModel.getCustomerName());
        emailUserDTO.setAmount(billModel.getCurrency() + "&nbsp;" + totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        emailUserDTO.setVeriAmount(billModel.getCurrency() + "&nbsp;" + verifyPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        emailUserDTO.setUnVeriAmount(billModel.getCurrency() + "&nbsp;" + unVeriPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        emailUserDTO.setVerificationId(user.getId());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setAttArray(attArray);
        JsonConfig jsonConfig = new JsonConfig();
        getReceiveOperators(billModel.getId(), emailUserDTO, jsonConfig);
        JSONObject emailJson = JSONObject.fromObject(emailUserDTO, jsonConfig);

        rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                MQErpEnum.SEND_REMINDER_MAIL.getTags());

        /*收款核销跟踪*/

        Map<String, Object> verifyMap = new HashMap<String, Object>();
        verifyMap.put("receiveCodes", billModel.getCode());
        verifyMap.put("orderType", DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_0);
        verifyMap.put("operateType", "0");
        verifyMap.put("notes", "应收账单核销");
        JSONObject verifyJson = JSONObject.fromObject(verifyMap);
        rocketMQProducer.send(verifyJson.toString(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTopic(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTags());

        resMap.put("code", "00");
        resMap.put("message", "保存成功");
        return resMap;
    }

    /**
     * 获取应收账单各个节点的操作人
     *
     * @param billId       应收id
     * @param emailUserDTO
     */
    private void getReceiveOperators(Long billId, ReminderEmailUserDTO emailUserDTO, JsonConfig jsonConfig) throws SQLException {
        ReceiveBillOperateModel operateModel = new ReceiveBillOperateModel();
        operateModel.setBillId(billId);
        List<ReceiveBillOperateModel> receiveBillOperateModels = receiveBillOperateDao.list(operateModel);

        Collections.sort(receiveBillOperateModels, new Comparator<ReceiveBillOperateModel>() {
            @Override
            public int compare(ReceiveBillOperateModel o1, ReceiveBillOperateModel o2) {
                return o2.getOperateDate().compareTo(o1.getOperateDate());
            }
        });

        for (ReceiveBillOperateModel model : receiveBillOperateModels) {
            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_0.equals(model.getOperateNode())) {
                if (emailUserDTO.getSubmitId() == null || emailUserDTO.getSubmitId().size() == 0) {
                    emailUserDTO.setSubmitId(Arrays.asList(model.getOperateId().toString()));
                }
            }
            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_1.equals(model.getOperateNode())) {
                if (emailUserDTO.getAuditorId() == null) {
                    emailUserDTO.setAuditorId(model.getOperateId());
                }
            }

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_3.equals(model.getOperateNode())) {
                if (emailUserDTO.getCancelId() == null) {
                    emailUserDTO.setCancelId(model.getOperateId());
                }
            }

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_4.equals(model.getOperateNode())) {
                if (emailUserDTO.getCancelCompleteId() == null) {
                    emailUserDTO.setCancelCompleteId(model.getOperateId());
                }
            }

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_6.equals(model.getOperateNode())) {
                if (emailUserDTO.getDrawerId() == null) {
                    emailUserDTO.setDrawerId(model.getOperateId());
                }
            }

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_7.equals(model.getOperateNode())) {
                if (emailUserDTO.getReminderOrgId() == null) {
                    emailUserDTO.setReminderOrgId(model.getOperateId());
                }
            }

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_8.equals(model.getOperateNode())) {
                if (emailUserDTO.getVerificationId() == null) {
                    emailUserDTO.setVerificationId(model.getOperateId());
                }
            }
        }

        jsonConfig.registerDefaultValueProcessor(Long.class, new DefaultDefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return "";
            }
        });
    }

}
