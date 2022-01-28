package com.topideal.order.service.bill.impl;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.RectangleReadOnly;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.PdfPageOrientation;
import com.spire.pdf.PdfPageRotateAngle;
import com.spire.pdf.PdfPageSize;
import com.spire.pdf.graphics.PdfMargins;
import com.spire.xls.ExcelVersion;
import com.spire.xls.FileFormat;
import com.spire.xls.ViewMode;
import com.spire.xls.Worksheet;
import com.topideal.api.nc.NcClientUtils;
import com.topideal.api.nc.nc07.Details;
import com.topideal.api.nc.nc07.ReceiveBillJsonRoot;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.*;
import com.topideal.dao.bill.*;
import com.topideal.dao.common.FileTempDao;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.enums.NcAPIEnum;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.order.service.bill.ReceiveBillInvoiceService;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.tools.pdf.Excel2PdfUtils;
import com.topideal.order.tools.pdf.FreemakerUtils;
import com.topideal.order.tools.pdf.PdfUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultDefaultValueProcessor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 应收账单service实现类
 * @Author: Chen Yiluan
 * @Date: 2020/07/27 15:01
 **/
@Service
public class ReceiveBillInvoiceServiceImpl implements ReceiveBillInvoiceService {

    @Autowired
    private ReceiveBillInvoiceDao receiveBillInvoiceDao;
    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private ReceiveBillItemDao receiveBillItemDao;
    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private ReceiveBillOperateDao receiveBillOperateDao;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao;
    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private FileTempDao fileTempDao;
    @Autowired
    private SettlementConfigDao settlementConfigDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
    @Autowired
    private AdvanceBillDao advanceBillDao;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;

    @Override
    public ReceiveBillInvoiceDTO listReceiveBillInvoiceByPage(ReceiveBillInvoiceDTO dto, User user) throws SQLException {

        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        if (buList.isEmpty()) {
            dto.setList(new ArrayList<>());
            dto.setTotal(0);
            return dto;
        }
        dto.setBuList(buList);

        return receiveBillInvoiceDao.searchDTOByPage(dto);
    }

    @Override
    public List<Map<String,Object>> listForExport(ReceiveBillInvoiceDTO dto, User user) {

        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        if (buList.isEmpty()) {
            return new ArrayList<>();
        }
        dto.setBuList(buList);

        List<Map<String,Object>> resultList = receiveBillInvoiceDao.listForExport(dto);
        for (Map<String,Object> map : resultList) {
            String status = (String) map.get("status");
            map.put("statusLabel", DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBillInvoice_statusList, status));
            String currency = (String) map.get("currency");
            map.put("currencyLabel", DERP.getLabelByKey(DERP.currencyCodeList, currency));
        }
        return resultList;
    }

    @Override
    public List<ReceiveBillDTO> listBillByInvoiceId(String id) throws SQLException {
        ReceiveBillInvoiceModel invoiceModel = receiveBillInvoiceDao.searchById(Long.valueOf(id));
        if (invoiceModel == null || StringUtils.isBlank(invoiceModel.getInvoiceRelIds())) {
            throw new RuntimeException("发票不存在");
        }
        List<Long> ids = StrUtils.parseIds(invoiceModel.getInvoiceRelIds());
        List<ReceiveBillDTO> receiveBillDTOS = receiveBillDao.listBillByRelIds(ids);
        Map<Long, ReceiveBillDTO> receiveBillDTOMap = new LinkedHashMap<>();
        for (ReceiveBillDTO billDTO : receiveBillDTOS) {
            ids.add(billDTO.getId());
            billDTO.setReceivablePrice(BigDecimal.ZERO);
            receiveBillDTOMap.put(billDTO.getId(), billDTO);
        }

        if (!ids.isEmpty()) {
            //获取应收账单的应收明细金额
            List<Map<String, Object>> itemPriceList = receiveBillItemDao.listItemPrice(ids);
            for (Map<String, Object> map : itemPriceList) {
                Long billId = (Long) map.get("billId");
                BigDecimal totalItemPrice = (BigDecimal) map.get("totalPrice");
                ReceiveBillDTO receiveBillTemp = receiveBillDTOMap.get(billId);
                if (totalItemPrice != null) {
                    receiveBillTemp.setReceivablePrice(totalItemPrice);
                }
            }
            //获取应收账单的费用金额
            List<Map<String, Object>> costPriceList = receiveBillCostItemDao.listCostPrice(ids);
            for (Map<String, Object> map : costPriceList) {
                Long billId = (Long) map.get("billId");
                BigDecimal totalCostPrice = (BigDecimal) map.get("totalPrice");
                ReceiveBillDTO receiveBillTemp = receiveBillDTOMap.get(billId);
                if (totalCostPrice != null) {
                    BigDecimal receivablePrice = receiveBillTemp.getReceivablePrice().add(totalCostPrice);
                    receiveBillTemp.setReceivablePrice(receivablePrice);
                }
            }
        }
        return receiveBillDTOS;
    }

    @Override
    public ReceiveBillInvoiceDTO searchByDTO(ReceiveBillInvoiceDTO dto) throws SQLException {
        return receiveBillInvoiceDao.searchByDTO(dto);
    }

    @Override
    public Map<String, String> modifyStatus(Long id, User user) throws Exception {
        ReceiveBillInvoiceModel exist = receiveBillInvoiceDao.searchById(id);
        Map<String, String> result = new HashMap<>();
        if (exist == null) {
            result.put("code", "01");
            result.put("message", "发票不存在！");
            return result;
        }
        String relIds = exist.getInvoiceRelIds();
        String type = exist.getInvoiceType();
        List<String> submitIds = new ArrayList<>();

        if (DERP_ORDER.RECEIVEBILLINVOICE_INVOICETYPE_0.equals(type)) { //tob
            List<ReceiveBillModel> receiveBillModels = new ArrayList<>();
            if (StringUtils.isNotBlank(relIds)) {
                ReceiveBillModel receiveBillModel = new ReceiveBillModel();
                receiveBillModel.setInvoiceId(id);
                receiveBillModels = receiveBillDao.list(receiveBillModel);
                /*if (receiveBillModels == null || receiveBillModels.size() == 0 || StringUtils.isBlank(relIds) ) {
                    result.put("code", "01");
                    result.put("message", "发票没有关联应收账单！");
                    return result;
                }*/
                if (receiveBillModels != null && !receiveBillModels.isEmpty()) {
                    List<Long> relIdList = new ArrayList<>();
                    for (ReceiveBillModel rel : receiveBillModels) {
                        relIdList.add(rel.getId());
                        ReceiveBillOperateModel operateModel = receiveBillOperateDao.getLatestOperate(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_0, Arrays.asList(rel.getId()));
                        if (operateModel != null && operateModel.getOperateId() != null) {
                            submitIds.add(String.valueOf(operateModel.getOperateId()));
                        }
                    }
                    receiveBillDao.batchUpdateBillStatus(relIdList, null, DERP_ORDER.RECEIVEBILL_INVOICESTATUS_03);
                }

            }

            ReceiveBillInvoiceModel invoiceModel = new ReceiveBillInvoiceModel();
            invoiceModel.setId(id);
            invoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_03);
            receiveBillInvoiceDao.modify(invoiceModel);

            //操作日志节点
            if (receiveBillModels != null && !receiveBillModels.isEmpty()) {
                for (ReceiveBillModel rel : receiveBillModels) {
                    ReceiveBillOperateModel receiveBillOperateModel = new ReceiveBillOperateModel();
                    receiveBillOperateModel.setBillId(rel.getId());
                    receiveBillOperateModel.setOperateDate(TimeUtils.getNow());
                    receiveBillOperateModel.setCreateDate(TimeUtils.getNow());
                    receiveBillOperateModel.setOperateId(user.getId());
                    receiveBillOperateModel.setOperator(user.getName());
                    receiveBillOperateModel.setOperateNode(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_7);
                    receiveBillOperateDao.save(receiveBillOperateModel);
                }

                /**
                 * 发送签章提醒邮件
                 */
                Map<String, Object> attQueryMap = new HashMap<String, Object>() ;
                attQueryMap.put("relationCode", exist.getInvoiceNo()) ;
                List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);
                JSONArray attArray = new JSONArray() ;
                for (AttachmentInfoMongo att : attList) {
                    if (!DERP.DEL_CODE_006.equals(att.getStatus())) {
                        Map<String, Object> tempMap = new HashMap<String, Object>() ;
                        tempMap.put("attachmentName", att.getAttachmentName()) ;
                        tempMap.put("attachmentUrl", att.getAttachmentUrl()) ;
                        JSONObject attJson = new JSONObject() ;
                        attJson.putAll(tempMap);
                        attArray.add(attJson) ;
                    }
                }

                //封装发送邮件JSON
                ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

                emailUserDTO.setBuId(receiveBillModels.get(0).getBuId());
                emailUserDTO.setBuName(receiveBillModels.get(0).getBuName());
                emailUserDTO.setMerchantId(exist.getMerchantId());
                emailUserDTO.setMerchantName(exist.getMerchantName());
                emailUserDTO.setOrderCode(exist.getInvoiceNo());
                emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1);
                emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                        DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1));
                emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_7);
                emailUserDTO.setSupplier(exist.getCustomerName());
                emailUserDTO.setAmount(exist.getCurrency() + "&nbsp;" +exist.getInvoiceAmount().toPlainString());
                emailUserDTO.setReminderOrgId(user.getId());
                emailUserDTO.setUserName(user.getName());
                emailUserDTO.setSubmitId(submitIds);
                emailUserDTO.setAttArray(attArray);

                JsonConfig jsonConfig = new JsonConfig();
                getReceiveOperators(receiveBillModels.get(0).getId(), emailUserDTO, jsonConfig);

                JSONObject emailJson = JSONObject.fromObject(emailUserDTO) ;

                rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());
            }



        } else if (DERP_ORDER.RECEIVEBILLINVOICE_INVOICETYPE_1.equals(type)) { //toc
            List<TocSettlementReceiveBillModel> tocSettlementReceiveBillModels = new ArrayList<>();
            if (StringUtils.isNotBlank(relIds)) {
                TocSettlementReceiveBillModel receiveBillModel = new TocSettlementReceiveBillModel();
                receiveBillModel.setInvoiceId(id);
                tocSettlementReceiveBillModels = tocSettlementReceiveBillDao.list(receiveBillModel);
                if (tocSettlementReceiveBillModels == null || tocSettlementReceiveBillModels.size() == 0 || StringUtils.isBlank(relIds) ) {
                    result.put("code", "01");
                    result.put("message", "发票没有关联toc应收账单！");
                    return result;
                }
                List<Long> relIdList = new ArrayList<>();
                for (TocSettlementReceiveBillModel rel : tocSettlementReceiveBillModels) {
                    relIdList.add(rel.getId());
                }
                tocSettlementReceiveBillDao.batchUpdateInvoiceStatus(relIdList, DERP_ORDER.RECEIVEBILL_INVOICESTATUS_03);

                Map<String, Object> attQueryMap = new HashMap<String, Object>() ;
                attQueryMap.put("relationCode", exist.getInvoiceNo()) ;
                List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);
                JSONArray attArray = new JSONArray() ;
                for (AttachmentInfoMongo att : attList) {
                    if (!DERP.DEL_CODE_006.equals(att.getStatus())) {
                        Map<String, Object> tempMap = new HashMap<String, Object>() ;
                        tempMap.put("attachmentName", att.getAttachmentName()) ;
                        tempMap.put("attachmentUrl", att.getAttachmentUrl()) ;
                        JSONObject attJson = new JSONObject() ;
                        attJson.putAll(tempMap);
                        attArray.add(attJson) ;
                    }
                }

                //封装发送邮件JSON
                ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

                emailUserDTO.setBuId(tocSettlementReceiveBillModels.get(0).getBuId());
                emailUserDTO.setBuName(tocSettlementReceiveBillModels.get(0).getBuName());
                emailUserDTO.setMerchantId(exist.getMerchantId());
                emailUserDTO.setMerchantName(exist.getMerchantName());
                emailUserDTO.setOrderCode(exist.getInvoiceNo());
                emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9);
                emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                        DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9));
                emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_7);
                emailUserDTO.setSupplier(exist.getCustomerName());
                emailUserDTO.setAmount(exist.getCurrency() + "&nbsp;" +exist.getInvoiceAmount().toPlainString());
                emailUserDTO.setReminderOrgId(user.getId());
                emailUserDTO.setUserName(user.getName());
                emailUserDTO.setAttArray(attArray);
                emailUserDTO.setStorePlatformName(DERP.getLabelByKey(DERP.storePlatformList, tocSettlementReceiveBillModels.get(0).getStorePlatformCode()));

                JSONObject emailJson = JSONObject.fromObject(emailUserDTO) ;

                rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());

            }

            ReceiveBillInvoiceModel invoiceModel = new ReceiveBillInvoiceModel();
            invoiceModel.setId(id);
            invoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_03);
            receiveBillInvoiceDao.modify(invoiceModel);
        } else if (DERP_ORDER.RECEIVEBILLINVOICE_INVOICETYPE_2.equals(type)) { //预收
            List<AdvanceBillModel> advanceBillModels = new ArrayList<>();
            if (StringUtils.isNotBlank(relIds)) {
                AdvanceBillModel advanceBillModel = new AdvanceBillModel();
                advanceBillModel.setInvoiceId(id);
                advanceBillModels = advanceBillDao.list(advanceBillModel);
                if (advanceBillModels == null || advanceBillModels.size() == 0 || StringUtils.isBlank(relIds) ) {
                    result.put("code", "01");
                    result.put("message", "发票没有关联预收账单！");
                    return result;
                }
                List<Long> relIdList = new ArrayList<>();
                for (AdvanceBillModel rel : advanceBillModels) {
                    relIdList.add(rel.getId());
                }
                advanceBillDao.batchUpdateInvoiceStatus(relIdList, DERP_ORDER.RECEIVEBILL_INVOICESTATUS_03);

                Map<String, Object> attQueryMap = new HashMap<String, Object>() ;
                attQueryMap.put("relationCode", exist.getInvoiceNo()) ;
                List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);
                JSONArray attArray = new JSONArray() ;
                for (AttachmentInfoMongo att : attList) {
                    if (!DERP.DEL_CODE_006.equals(att.getStatus())) {
                        Map<String, Object> tempMap = new HashMap<String, Object>() ;
                        tempMap.put("attachmentName", att.getAttachmentName()) ;
                        tempMap.put("attachmentUrl", att.getAttachmentUrl()) ;
                        JSONObject attJson = new JSONObject() ;
                        attJson.putAll(tempMap);
                        attArray.add(attJson) ;
                    }
                }

                //封装发送邮件JSON
                ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

                emailUserDTO.setBuId(advanceBillModels.get(0).getBuId());
                emailUserDTO.setBuName(advanceBillModels.get(0).getBuName());
                emailUserDTO.setMerchantId(exist.getMerchantId());
                emailUserDTO.setMerchantName(exist.getMerchantName());
                emailUserDTO.setOrderCode(exist.getInvoiceNo());
                emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10);
                emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                        DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10));
                emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_7);
                emailUserDTO.setSupplier(exist.getCustomerName());
                emailUserDTO.setAmount(exist.getCurrency() + "&nbsp;" +exist.getInvoiceAmount().toPlainString());
                emailUserDTO.setReminderOrgId(user.getId());
                emailUserDTO.setUserName(user.getName());
                emailUserDTO.setAttArray(attArray);

                JSONObject emailJson = JSONObject.fromObject(emailUserDTO) ;

                rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());

            }

            ReceiveBillInvoiceModel invoiceModel = new ReceiveBillInvoiceModel();
            invoiceModel.setId(id);
            invoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_03);
            receiveBillInvoiceDao.modify(invoiceModel);
        }

        result.put("code", "00");
        result.put("status", exist.getStatus());
        result.put("message", "成功");
        return result;
    }

    @Override
    public List<ReceiveBillToNCDTO> listReceiveBillsToNCById(String id, String dataSource) throws SQLException {
        List<Long> billIds = new ArrayList<>();
        Map<Long, ReceiveBillModel> receiveBillModelMap = new HashMap<>();
        //dataSource=1 ： 数据来源为应收账单， 其他为发票（发票关联多个应收账单）
        if ("1".equals(dataSource)) {
            ReceiveBillModel receiveBillModel = receiveBillDao.searchById(Long.valueOf(id));
            billIds.add(receiveBillModel.getId());
            receiveBillModelMap.put(receiveBillModel.getId(), receiveBillModel);
        } else {
            ReceiveBillModel receiveBillModel = new ReceiveBillModel();
            receiveBillModel.setInvoiceId(Long.valueOf(id));
            List<ReceiveBillModel> receiveBillModels = receiveBillDao.list(receiveBillModel);
            for (ReceiveBillModel model : receiveBillModels) {
                billIds.add(model.getId());
                receiveBillModelMap.put(model.getId(), model);
            }
        }

        //应收明细
        List<ReceiveBillItemDTO> receiveBillItemDTOS = receiveBillItemDao.synNcItemByIds(billIds);
        List<ReceiveBillCostItemDTO> receiveBillCostItemDTOS = receiveBillCostItemDao.synNcItemByIds(billIds);
        List<ReceiveBillToNCDTO> receiveBillToNCDTOS = new ArrayList<>();
        for (ReceiveBillItemDTO itemDTO : receiveBillItemDTOS) {
            ReceiveBillToNCDTO ncdto = new ReceiveBillToNCDTO();
            ReceiveBillModel model = receiveBillModelMap.get(itemDTO.getBillId());
            ncdto.setBillCode(model.getCode());
            ncdto.setBuId(model.getBuId());
            ncdto.setBuName(model.getBuName());
            ncdto.setCurrency(model.getCurrency());
            ncdto.setSaleModel(model.getSaleModel());
            ncdto.setNcChannelCode(model.getNcChannelCode());
            ncdto.setSettlementType(model.getSettlementType());
            ncdto.setParentBrandName(itemDTO.getParentBrandName());
            ncdto.setPaymentSubjectName(itemDTO.getPaymentSubjectName());
            ncdto.setPrice(itemDTO.getPrice());
            ncdto.setBillId(itemDTO.getBillId());
            receiveBillToNCDTOS.add(ncdto);
        }
        for (ReceiveBillCostItemDTO itemDTO : receiveBillCostItemDTOS) {
            ReceiveBillToNCDTO ncdto = new ReceiveBillToNCDTO();
            ReceiveBillModel model = receiveBillModelMap.get(itemDTO.getBillId());
            ncdto.setBillCode(model.getCode());
            ncdto.setBuId(model.getBuId());
            ncdto.setBuName(model.getBuName());
            ncdto.setCurrency(model.getCurrency());
            ncdto.setSaleModel(model.getSaleModel());
            ncdto.setNcChannelCode(model.getNcChannelCode());
            ncdto.setSettlementType(model.getSettlementType());
            ncdto.setParentBrandName(itemDTO.getParentBrandName());
            ncdto.setPaymentSubjectName(itemDTO.getPaymentSubjectName());
            ncdto.setPrice(itemDTO.getPrice());
            ncdto.setBillId(itemDTO.getBillId());
            receiveBillToNCDTOS.add(ncdto);
        }
        Collections.sort(receiveBillToNCDTOS, new Comparator<ReceiveBillToNCDTO>() {
            @Override
            public int compare(ReceiveBillToNCDTO o1, ReceiveBillToNCDTO o2) {
                return o1.getBillCode().compareTo(o2.getBillCode());
            }
        });
        return receiveBillToNCDTOS;
    }

    @Override
    public Map<String, String> synNC(String id, String dataSource, User user) throws Exception {
        try {
            Map<String, String> result = new HashMap<>();
            List<Long> billIds = new ArrayList<>();
            List<ReceiveBillModel> receiveBillModels = new ArrayList<>();

            ReceiveBillInvoiceModel receiveBillInvoiceModel = null;
            if ("1".equals(dataSource)) {
                ReceiveBillModel model = receiveBillDao.searchById(Long.valueOf(id));
                receiveBillModels.add(model);
                billIds.add(model.getId());
                if (model.getInvoiceId()!=null) {
                	receiveBillInvoiceModel = receiveBillInvoiceDao.searchById(model.getInvoiceId());
				}

                if (!DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10.equals(model.getNcStatus())) {
                    result.put("code", "01");
                    result.put("message", "应收账单" + model.getCode() + "已同步，不能重复同步！");
                    return result;
                }
                
            } else {
                receiveBillInvoiceModel = receiveBillInvoiceDao.searchById(Long.valueOf(id));
                ReceiveBillModel receiveBillModel = new ReceiveBillModel();
                receiveBillModel.setInvoiceId(Long.valueOf(id));
                receiveBillModels = receiveBillDao.list(receiveBillModel);
                for (ReceiveBillModel model : receiveBillModels) {
                    billIds.add(model.getId());
                    if (!DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10.equals(model.getNcStatus())) {
                        result.put("code", "01");
                        result.put("message", "应收账单" + model.getCode() + "已同步，不能重复同步！");
                        return result;
                    }
                }
            }
            /*if (receiveBillInvoiceModel == null) {
                result.put("code", "01");
                result.put("message", "发票不存在！");
                return result;
            }*/

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
            SimpleDateFormat month = new SimpleDateFormat("MM月") ;
            Timestamp synDate = TimeUtils.getNow();

            //应收明细
            List<ReceiveBillItemDTO> receiveBillItemDTOS = receiveBillItemDao.synNcItemByIds(billIds);
            Map<Long, List<ReceiveBillItemDTO>> ReceiveBillItemDTOList = new HashMap<>();
            for (ReceiveBillItemDTO itemDTO : receiveBillItemDTOS) {
                if (ReceiveBillItemDTOList.containsKey(itemDTO.getBillId())) {
                    ReceiveBillItemDTOList.get(itemDTO.getBillId()).add(itemDTO);
                } else {
                    List<ReceiveBillItemDTO> itemDTOS = new ArrayList<>();
                    itemDTOS.add(itemDTO);
                    ReceiveBillItemDTOList.put(itemDTO.getBillId(), itemDTOS);
                }
            }
            //费用明细
            List<ReceiveBillCostItemDTO> receiveBillCostItemDTOS = receiveBillCostItemDao.synNcItemByIds(billIds);
            Map<Long, List<ReceiveBillCostItemDTO>> receiveBillCostItemDTOSList = new HashMap<>();
            for (ReceiveBillCostItemDTO itemDTO : receiveBillCostItemDTOS) {
                if (receiveBillCostItemDTOSList.containsKey(itemDTO.getBillId())) {
                    receiveBillCostItemDTOSList.get(itemDTO.getBillId()).add(itemDTO);
                } else {
                    List<ReceiveBillCostItemDTO> itemDTOS = new ArrayList<>();
                    itemDTOS.add(itemDTO);
                    receiveBillCostItemDTOSList.put(itemDTO.getBillId(), itemDTOS);
                }
            }
            for (ReceiveBillModel model : receiveBillModels) {

                //客户
                Map<String, Object> customerParam = new HashMap<>();
                customerParam.put("customerid", model.getCustomerId());
                CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParam);
                if (customer == null) {
                    result.put("code", "01");
                    result.put("message", "客户不存在！");
                    return result;
                }

                //事业部
                Map<String, Object> buMap = new HashMap<>();
                buMap.put("buId", model.getBuId());
                buMap.put("merchantId", model.getMerchantId());
                buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
                MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);
                if (merchantBuRelMongo == null) {
                    result.put("code", "01");
                    result.put("message", "事业部不存在！");
                    return result;
                }

                BigDecimal totalPrice = new BigDecimal("0");
                List<ReceiveBillItemDTO> itemDTOS = ReceiveBillItemDTOList.get(model.getId());
                List<ReceiveBillCostItemDTO> costItemDTOS = receiveBillCostItemDTOSList.get(model.getId());
                List<Details> detailsList = new ArrayList<Details>() ;
                String billMonth = model.getBillDate() != null ? month.format(model.getBillDate()) : "";
                int index = 1;
                //表体
                if (itemDTOS != null) {
                    for (ReceiveBillItemDTO itemDTO : itemDTOS) {
                        if (itemDTO.getPrice().compareTo(new BigDecimal("0")) == 0) {
                            continue;
                        }
                        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(itemDTO.getProjectId());
                        if (settlementConfigModel == null) {
                            result.put("code", "01");
                            result.put("message", "费项配置不存在！");
                            return result;
                        }
                        Details details = new Details() ;
                        String parentBrandName = StringUtils.isNotBlank(itemDTO.getParentBrandName()) ? itemDTO.getParentBrandName() : "";
                        String abstracts = billMonth
                                + DERP_ORDER.getLabelByKey(DERP_ORDER.platform_codeList , model.getNcPlatformCode())
                                + parentBrandName + settlementConfigModel.getProjectName();
                        details.setSindex(String.valueOf(index));
                        details.setInExpCode(settlementConfigModel.getInExpCode());
                        details.setCurrency(model.getCurrency());
                        details.setAmount(itemDTO.getPrice());
                        details.setRate("0%");
                        details.setTax(new BigDecimal("0.00"));
                        details.setAbstracts(abstracts);
                        if (receiveBillInvoiceModel != null) {
                            details.setInvoiceCode(receiveBillInvoiceModel.getInvoiceNo());
                            details.setInvoiceDate(dateFormat.format(receiveBillInvoiceModel.getInvoiceDate()));
                        }

                        details.setDetpCode(merchantBuRelMongo.getBuCode());
                        details.setChannelCode(model.getNcChannelCode());
                        details.setSaleCode("009");
                        details.setBrandCode(itemDTO.getParentBrandCode());
//                        details.setPlatCode(model.getNcPlatformCode());
                        details.setSystemOrder(itemDTO.getCode());
                        details.setExternalOrder(itemDTO.getPoNo());
                        detailsList.add(details) ;
                        index++;
                        totalPrice = totalPrice.add(itemDTO.getPrice());
                    }
                }
                if (costItemDTOS != null) {
                    for (ReceiveBillCostItemDTO itemDTO : costItemDTOS) {
                        if (itemDTO.getPrice().compareTo(new BigDecimal("0")) == 0) {
                            continue;
                        }
                        Details details = new Details() ;
                        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(itemDTO.getProjectId());
                        if (settlementConfigModel == null) {
                            result.put("code", "01");
                            result.put("message", "费项配置不存在！");
                            return result;
                        }

                        String parentBrandName = StringUtils.isNotBlank(itemDTO.getParentBrandName()) ? itemDTO.getParentBrandName() : "";
                        String abstracts = billMonth
                                + DERP_ORDER.getLabelByKey(DERP_ORDER.platform_codeList , model.getNcPlatformCode())
                                + parentBrandName + settlementConfigModel.getProjectName();
                        details.setSindex(String.valueOf(index));
                        details.setInExpCode(settlementConfigModel.getInExpCode());
                        details.setCurrency(model.getCurrency());
                        details.setAmount(itemDTO.getPrice());
                        details.setRate("0%");
                        details.setTax(new BigDecimal("0.00"));
                        details.setAbstracts(abstracts);
                        if (receiveBillInvoiceModel != null) {
                            details.setInvoiceCode(receiveBillInvoiceModel.getInvoiceNo());
                            details.setInvoiceDate(dateFormat.format(receiveBillInvoiceModel.getInvoiceDate()));
                        }

                        details.setDetpCode(merchantBuRelMongo.getBuCode());
                        details.setChannelCode(model.getNcChannelCode());
                        details.setSaleCode("009");
                        details.setBrandCode(itemDTO.getParentBrandCode());
//                        details.setPlatCode(model.getNcPlatformCode());
                        detailsList.add(details) ;
                        details.setExternalOrder(itemDTO.getPoNo());
                        index++;
                        totalPrice = totalPrice.add(itemDTO.getPrice());
                    }
                }

                if (detailsList.size() == 0) {
                    result.put("code", "01");
                    result.put("message", "表体不存在！");
                    return result;
                }

                //表头
                ReceiveBillJsonRoot root = new ReceiveBillJsonRoot();
                root.setConfirmBillId(model.getCode());
                root.setSourceCode(ApolloUtil.ncSourceType);
                root.setType("1");
                root.setCorCcode(user.getTopidealCode());
                root.setCusCcode(customer.getMainId());
                root.setYearMonth(sdf.format(model.getCreditDate()));
                if (model.getCreditDate() != null) {
                    root.setCreated(format.format(model.getCreditDate()));
                }
                root.setTotalAmount(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                root.setTaxAmount(new BigDecimal("0"));
                root.setCurrency(model.getCurrency());
                root.setChecked("1");
                root.setInvoiced("1");
                root.setRemark("");
                root.setDetails(detailsList);

                JSONObject json = JSONObject.fromObject(root);
                String clearText = json.toString() ;
                //提交NC
                String ncResult = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_07.getUri(), clearText);

                JSONObject resultJson =JSONObject.fromObject(ncResult);
                if (resultJson.containsKey("code") && resultJson.getString("code").equals("1001")) {
                    ReceiveBillModel updateModel = new ReceiveBillModel() ;
                    updateModel.setId(model.getId());
                    updateModel.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_11);
                    updateModel.setNcSynDate(synDate);
                    updateModel.setSynchronizerId(user.getId());
                    updateModel.setSynchronizer(user.getName());
                    receiveBillDao.modify(updateModel) ;
                } else if (resultJson.containsKey("code") && resultJson.getString("code").equals("1002")) {
                    result.put("code", "01");
                    result.put("message", resultJson.getString("msg"));
                    return result;
                } else {
                    throw new RuntimeException(resultJson.toString());
                }
            }
            result.put("code", "00");
            result.put("message", "成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("提交NC失败");
        }
    }

    @Override
    public List<Map<String, Object>> listForBillExport(ReceiveBillInvoiceDTO dto)throws Exception {
        List<Map<String, Object>> resultMap = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //根据查询条件查询满足条件的应收发票
        List<ReceiveBillInvoiceDTO> dtos = receiveBillInvoiceDao.listDTO(dto);
        List<Long> billIds = new ArrayList<>();
        Map<Long, ReceiveBillModel> receiveBillModelMap = new HashMap<>();
        Map<Long, ReceiveBillInvoiceDTO> receiveBillInvoiceDTOMap = new HashMap<>();
        for (ReceiveBillInvoiceDTO invoiceDTO : dtos) {
            receiveBillInvoiceDTOMap.put(invoiceDTO.getId(), invoiceDTO);
            ReceiveBillModel receiveBillModel = new ReceiveBillModel();
            receiveBillModel.setInvoiceId(invoiceDTO.getId());
            //应收发票对应的应收账单
            List<ReceiveBillModel> receiveBillModels = receiveBillDao.list(receiveBillModel);
            for (ReceiveBillModel model : receiveBillModels) {
                billIds.add(model.getId());
                receiveBillModelMap.put(model.getId(), model);
            }
        }

        //查询应收明细
        List<ReceiveBillItemDTO> receiveBillItemDTOS = receiveBillItemDao.synNcItemByIds(billIds);
        //查询费用明细
        List<ReceiveBillCostItemDTO> receiveBillCostItemDTOS = receiveBillCostItemDao.synNcItemByIds(billIds);

        for (ReceiveBillItemDTO itemDTO : receiveBillItemDTOS) {
            Map<String, Object> map = new HashMap<>();
            ReceiveBillModel receiveBillModel = receiveBillModelMap.get(itemDTO.getBillId());
            ReceiveBillInvoiceDTO receiveBillInvoiceDTO = receiveBillInvoiceDTOMap.get(receiveBillModel.getInvoiceId());
            map.put("merchantName", receiveBillModel.getMerchantName());
            map.put("billCode", receiveBillModel.getCode());
            map.put("invoiceNo", receiveBillInvoiceDTO.getInvoiceNo());
            map.put("customer", receiveBillModel.getCustomerName());
            map.put("month", sdf.format(receiveBillModel.getBillDate()));
            map.put("invoiceDate", receiveBillInvoiceDTO.getInvoiceDate());
            map.put("currency", receiveBillModel.getCurrency());
            map.put("buName", receiveBillModel.getBuName());
            map.put("settlementType", DERP_ORDER.getLabelByKey(DERP_ORDER.settlement_typeList, receiveBillModel.getSettlementType()));
            map.put("saleModel", DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_saleModeList, receiveBillModel.getSaleModel()));
            map.put("ncPlatformName", DERP_ORDER.getLabelByKey(DERP_ORDER.platform_codeList , receiveBillModel.getNcPlatformCode()));
            map.put("ncChannelName", DERP_ORDER.getLabelByKey(DERP_ORDER.channel_codeList , receiveBillModel.getNcChannelCode()));
            map.put("brandName", itemDTO.getParentBrandName());
            map.put("paymentSubjectName", itemDTO.getPaymentSubjectName());
            map.put("subjectName", itemDTO.getSubjectName());
            map.put("price", itemDTO.getPrice());
            map.put("synchronizer", receiveBillInvoiceDTO.getSynchronizer());
            resultMap.add(map);
        }
        for (ReceiveBillCostItemDTO itemDTO : receiveBillCostItemDTOS) {
            Map<String, Object> map = new HashMap<>();
            ReceiveBillModel receiveBillModel = receiveBillModelMap.get(itemDTO.getBillId());
            ReceiveBillInvoiceDTO receiveBillInvoiceDTO = receiveBillInvoiceDTOMap.get(receiveBillModel.getInvoiceId());
            map.put("merchantName", receiveBillModel.getMerchantName());
            map.put("billCode", receiveBillModel.getCode());
            map.put("invoiceNo", receiveBillInvoiceDTO.getInvoiceNo());
            map.put("customer", receiveBillModel.getCustomerName());
            map.put("month", sdf.format(receiveBillModel.getBillDate()));
            map.put("invoiceDate", receiveBillInvoiceDTO.getInvoiceDate());
            map.put("currency", receiveBillModel.getCurrency());
            map.put("buName", receiveBillModel.getBuName());
            map.put("settlementType", DERP_ORDER.getLabelByKey(DERP_ORDER.settlement_typeList, receiveBillModel.getSettlementType()));
            map.put("saleModel", DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_saleModeList, receiveBillModel.getSaleModel()));
            map.put("ncPlatformName", DERP_ORDER.getLabelByKey(DERP_ORDER.platform_codeList , receiveBillModel.getNcPlatformCode()));
            map.put("ncChannelName", DERP_ORDER.getLabelByKey(DERP_ORDER.channel_codeList , receiveBillModel.getNcChannelCode()));
            map.put("brandName", itemDTO.getParentBrandName());
            map.put("paymentSubjectName", itemDTO.getPaymentSubjectName());
            map.put("subjectName", itemDTO.getSubjectName());
            map.put("price", itemDTO.getPrice());
            map.put("synchronizer", receiveBillInvoiceDTO.getSynchronizer());
            resultMap.add(map);
        }
        return resultMap;
    }

    @Override
    public String exportNcBillPDF(List<Long> ids) throws Exception {
        //创建目录
        String basePath = ApolloUtilDB.orderBasepath+"/temp/" + System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat monthSdf = new SimpleDateFormat("yyyy年MM月");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查询应收明细
        List<ReceiveBillItemDTO> receiveBillItemDTOS = receiveBillItemDao.synNcItemByIds(ids);
        //查询费用明细
        List<ReceiveBillCostItemDTO> receiveBillCostItemDTOS = receiveBillCostItemDao.synNcItemByIds(ids);
        Map<Long, List<ReceiveBillItemDTO>> receiveBillItemDTOSMap = new HashMap<>();
        Map<Long, List<ReceiveBillCostItemDTO>> receiveBillCostItemDTOSMap = new HashMap<>();
        if (receiveBillItemDTOS != null) {
            for (ReceiveBillItemDTO itemDTO : receiveBillItemDTOS) {
                if (receiveBillItemDTOSMap.containsKey(itemDTO.getBillId())) {
                    receiveBillItemDTOSMap.get(itemDTO.getBillId()).add(itemDTO);
                } else {
                    List<ReceiveBillItemDTO> itemDTOS = new ArrayList<>();
                    itemDTOS.add(itemDTO);
                    receiveBillItemDTOSMap.put(itemDTO.getBillId(), itemDTOS);
                }
            }
        }

        if (receiveBillCostItemDTOS != null) {
            for (ReceiveBillCostItemDTO itemDTO : receiveBillCostItemDTOS) {
                if (receiveBillCostItemDTOSMap.containsKey(itemDTO.getBillId())) {
                    receiveBillCostItemDTOSMap.get(itemDTO.getBillId()).add(itemDTO);
                } else {
                    List<ReceiveBillCostItemDTO> itemDTOS = new ArrayList<>();
                    itemDTOS.add(itemDTO);
                    receiveBillCostItemDTOSMap.put(itemDTO.getBillId(), itemDTOS);
                }
            }
        }

        //遍历应收单生成pdf(一个应收单一个pdf)
        try {
            for (Long id : ids) {
                Map<String, Object> dataMap = new HashMap<>();
                List<Map<String, Object>> itemList = new ArrayList<>();
                ReceiveBillModel receiveBillModel = receiveBillDao.searchById(id);
                List<ReceiveBillItemDTO> itemDTOS = receiveBillItemDTOSMap.get(id);
                List<ReceiveBillCostItemDTO> costItemDTOS = receiveBillCostItemDTOSMap.get(id);
                Map<String, Object> merchantParam = new HashMap<>();
                merchantParam.put("merchantId", receiveBillModel.getMerchantId());
                MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantParam);
                Map<String, Object> customerParam = new HashMap<>();
                customerParam.put("customerid", receiveBillModel.getCustomerId());
                CustomerInfoMogo customerInfoMongo = customerInfoMongoDao.findOne(customerParam);
                BigDecimal totalPrice = new BigDecimal("0");
                BigDecimal totalItemPrice = new BigDecimal("0");
                Integer totalNum = 0;
                Integer totalItemNum = 0;
                if (itemDTOS != null) {
                    for (ReceiveBillItemDTO itemDTO : itemDTOS) {
                        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(itemDTO.getProjectId());
                        Map<String, Object> map = new HashMap<>();
                        if (settlementConfigModel != null) {
                            map.put("projectName", toNullStr(settlementConfigModel.getProjectName()));
                        }
                        map.put("parentBrandName", toNullStr(itemDTO.getParentBrandName()));
                        map.put("paymentSubjectName", toNullStr(itemDTO.getPaymentSubjectName()));
                        map.put("subjectName", toNullStr(itemDTO.getSubjectCode() + "\\" + itemDTO.getSubjectName()));
                        map.put("receivePrice", itemDTO.getPrice()==null?"" : itemDTO.getPrice());
                        map.put("num", itemDTO.getNum()==null?"" : itemDTO.getNum());
                        map.put("poNo", toNullStr(itemDTO.getPoNo()));
                        itemList.add(map);
                        if (itemDTO.getPrice() != null) {
                            totalItemPrice = totalItemPrice.add(itemDTO.getPrice());
                        }
                        if (itemDTO.getNum() != null) {
                            totalItemNum += itemDTO.getNum();
                        }
                    }
                }

                if (costItemDTOS != null) {
                    for (ReceiveBillCostItemDTO itemDTO : costItemDTOS) {
                        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(itemDTO.getProjectId());
                        Map<String, Object> map = new HashMap<>();
                        if (settlementConfigModel != null) {
                            map.put("projectName", toNullStr(settlementConfigModel.getProjectName()));
                        }
                        map.put("parentBrandName", toNullStr(itemDTO.getParentBrandName()));
                        map.put("paymentSubjectName", toNullStr(itemDTO.getPaymentSubjectName()));
                        map.put("subjectName", toNullStr(itemDTO.getSubjectCode() + "\\" + itemDTO.getSubjectName()));
                        map.put("receivePrice", itemDTO.getPrice()==null?"" : itemDTO.getPrice());
                        map.put("num", itemDTO.getNum()==null?"" : itemDTO.getNum());
                        map.put("poNo", toNullStr(itemDTO.getPoNo()));
                        itemList.add(map);
                        if (itemDTO.getPrice() != null) {
                            totalPrice = totalPrice.add(itemDTO.getPrice());
                        }
                        if (itemDTO.getNum() != null) {
                            totalNum += itemDTO.getNum();
                        }
                    }
                }
                dataMap.put("goodsList", itemList);
                dataMap.put("merchantName", toNullStr(merchantInfoMongo.getFullName()));
                dataMap.put("billCode", toNullStr(receiveBillModel.getCode()));
                dataMap.put("billDate", receiveBillModel.getBillDate() != null ? sdf.format(receiveBillModel.getBillDate()) : "");
                dataMap.put("createDate", receiveBillModel.getCreateDate() == null ? "" : dateFormat.format(receiveBillModel.getCreateDate()));
                dataMap.put("billMonth", receiveBillModel.getBillDate() != null ? monthSdf.format(receiveBillModel.getBillDate()) : "");
                dataMap.put("customer", toNullStr(customerInfoMongo.getName()));
                dataMap.put("buName", toNullStr(receiveBillModel.getBuName()));
                dataMap.put("billStatus", toNullStr(DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_billStatusList, receiveBillModel.getBillStatus())));
                dataMap.put("invoiceStatus", toNullStr(DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_invoiceStatusList, receiveBillModel.getInvoiceStatus())));
                dataMap.put("settlementType", toNullStr(DERP_ORDER.getLabelByKey(DERP_ORDER.settlement_typeList, receiveBillModel.getSettlementType())));
                dataMap.put("saleModel", toNullStr(DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_saleModeList, receiveBillModel.getSaleModel())));
                dataMap.put("ncPlatform", toNullStr(DERP_ORDER.getLabelByKey(DERP_ORDER.platform_codeList , receiveBillModel.getNcPlatformCode())));
                dataMap.put("ncChannel", toNullStr(DERP_ORDER.getLabelByKey(DERP_ORDER.channel_codeList , receiveBillModel.getNcChannelCode())));
                dataMap.put("currency", receiveBillModel.getCurrency());
                dataMap.put("period", toNullStr(receiveBillModel.getPeriod()));
                dataMap.put("voucherCode", toNullStr(receiveBillModel.getVoucherCode()));
                dataMap.put("voucherStatus", toNullStr(DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_voucherStatusList , receiveBillModel.getVoucherStatus())));
                dataMap.put("totalItemNum", totalItemNum);
                dataMap.put("totalItemAmount", totalItemPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                dataMap.put("totalItemAmountLabel", NumberToCN.number2CNMontrayUnit(totalItemPrice.setScale(2, BigDecimal.ROUND_HALF_UP)));

                dataMap.put("totalCostNum", totalNum);
                dataMap.put("totalCostAmount", totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                dataMap.put("totalCostAmountLabel", NumberToCN.number2CNMontrayUnit(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP)));

                totalPrice = totalPrice.add(totalItemPrice);
                totalNum += totalItemNum;
                dataMap.put("totalNum", totalNum);
                dataMap.put("totalAmount", totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                dataMap.put("totalAmountLabel", NumberToCN.number2CNMontrayUnit(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP)));

                if (receiveBillModel.getInvoiceId() != null) {
                    ReceiveBillInvoiceModel invoiceModel = receiveBillInvoiceDao.searchById(receiveBillModel.getInvoiceId());
                    dataMap.put("invoiceNo", toNullStr(invoiceModel.getInvoiceNo()));
                    dataMap.put("invoiceDate", invoiceModel.getInvoiceDate() == null ? "" : dateFormat.format(invoiceModel.getInvoiceDate()));
                }

                Workbook workbook = DownloadExcelUtil.exportTemplateExcel("RECEIVEPDF", dataMap);

                File baseFile = new File(basePath) ;
                if(!baseFile.exists()) {
                    baseFile.mkdirs() ;
                }

                String tempPDFFilePath = basePath + File.separator + receiveBillModel.getCode() + ".pdf" ;
                RectangleReadOnly pageSize = (RectangleReadOnly) PageSize.A4;
                FileOutputStream fos = new FileOutputStream(tempPDFFilePath);
                Excel2PdfUtils.Excel2Pdf(workbook, fos, false, pageSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return basePath;
    }

    @Override
    public Map<String, String> validateSynNC(String id) throws SQLException {
        Map<String, String> resultMap = new HashMap<>();
        ReceiveBillModel receiveBillModel = new ReceiveBillModel();
        receiveBillModel.setInvoiceId(Long.valueOf(id));
        List<ReceiveBillModel> receiveBillModels = receiveBillDao.list(receiveBillModel);
        if (receiveBillModels == null || receiveBillModels.isEmpty()) {
            resultMap.put("code", "01");
            resultMap.put("msg", "该发票没有关联的应收账单");
            return resultMap;
        }

        for (ReceiveBillModel model : receiveBillModels) {
            if (!DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10.equals(model.getNcStatus())) {
                resultMap.put("code", "01");
                resultMap.put("msg", model.getCode()+"已同步");
                return resultMap;
            }
        }

        resultMap.put("code", "00");
        resultMap.put("msg", "校验通过");
        return resultMap;
    }

    @Override
    public Map<String, Object> replaceInvoiceFile(String id, MultipartFile file, User user) throws Exception {
        Map<String, Object> result = new HashMap<>();
        ReceiveBillInvoiceModel invoiceModel = receiveBillInvoiceDao.searchById(Long.valueOf(id));
        if (StringUtils.isBlank(invoiceModel.getInvoicePath())) {
            result.put("code", "01");
            result.put("message", "发票文件不存在");
            return result;
        }

        //上传文件到 蓝精灵服务器
        String fileName = file.getOriginalFilename();
        byte[] fileBytes = file.getBytes();
        Long fileSize = file.getSize();
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileTypeCode", SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
        jsonObject.put("fileName",fileName);
        jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
        jsonObject.put("fileExt",ext);
        jsonObject.put("fileSize",String.valueOf(fileSize));
        String resultJson= SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

        JSONObject jsonObj = JSONObject.fromObject(resultJson);

        if(jsonObj.getInt("code")!= 200){
            result.put("code" , jsonObj.getInt("code")) ;
            result.put("message", jsonObj.getString("msg"));

            return result ;
        }

        //返回文件服务器存储URL
        String url = jsonObj.getString("url") ;

        //更新发票文件路径
        ReceiveBillInvoiceModel receiveBillInvoiceModel = new ReceiveBillInvoiceModel();
        receiveBillInvoiceModel.setId(invoiceModel.getId());
        receiveBillInvoiceModel.setInvoicePath(url);
        receiveBillInvoiceDao.modify(receiveBillInvoiceModel);

        //逻辑删除之前的发票文件
        Map<String,Object> params = new HashMap<String,Object>() ;
        Map<String,Object> data = new HashMap<String,Object>() ;
        params.put("attachmentCode", invoiceModel.getInvoiceNo() + "-invoice") ;

        data.put("status", DERP_ORDER.ATTACHMENT_STATUS_006) ;
        data.put("modifyDate", new Date()) ;

        attachmentInfoMongoDao.update(params, data);

        //附件信息写入MongoDB
        AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo() ;
        attachmentInfoMongo.setAttachmentName(fileName); 		//附件名
        attachmentInfoMongo.setAttachmentSize(fileSize); 		//附件大小
        attachmentInfoMongo.setAttachmentType(ext);		       	//附件类型
        attachmentInfoMongo.setCreator(user.getId());			//上传者
        attachmentInfoMongo.setCreatorName(user.getName());
        attachmentInfoMongo.setCreateDate(new Date());
        attachmentInfoMongo.setRelationCode(invoiceModel.getInvoiceNo() + "-invoice");
        attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
        attachmentInfoMongo.setAttachmentUrl(url);              //设置Url
        attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
        attachmentInfoMongo.setModule(SourceStatusEnum.YSZD.getKey());

        attachmentInfoMongoDao.insert(attachmentInfoMongo);

        result.put("code", "00");
        result.put("message", "替换发票成功");
        return result;
    }

    private String toNullStr(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str;
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
                    if (model.getOperateId() != null) {
                        emailUserDTO.setSubmitId(Arrays.asList(model.getOperateId().toString()));
                    }
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
