package com.topideal.order.service.bill.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.RectangleReadOnly;
import com.topideal.api.nc.NcClientUtils;
import com.topideal.api.nc.nc07.Details;
import com.topideal.api.nc.nc07.ReceiveBillJsonRoot;
import com.topideal.api.nc.nc08.ReceiveHcInvalidRoot;
import com.topideal.api.nc.nc10.InvoiceUpdateRoot;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.bill.*;
import com.topideal.dao.common.FileTempDao;
import com.topideal.dao.common.PlatformSettlementConfigDao;
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
import com.topideal.mongo.entity.*;
import com.topideal.order.service.bill.ToCReceiveBillService;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.tools.ExcelConvertHTMLUtils;
import com.topideal.order.tools.pdf.Excel2PdfUtils;
import com.topideal.order.tools.pdf.FreemakerUtils;
import com.topideal.order.tools.pdf.PdfUtils;
import com.topideal.order.webapi.bill.form.TocReceiveBillInvoiceForm;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: toc应收账单
 * @Author: Chen Yiluan
 * @Date: 2020/12/28 12:00
 **/
@Service
public class ToCReceiveBillServiceImpl implements ToCReceiveBillService {

    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;
    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
    @Autowired
    private MerchantShopRelMongoDao merchantShopRelMongoDao;
    @Autowired
    private TocSettlementReceiveBillCostItemDao tocSettlementReceiveBillCostItemDao;
    @Autowired
    private TocSettlementReceiveBillItemDao tocSettlementReceiveBillItemDao;
    @Autowired
    private TocSettlementReceiveBillAuditItemDao tocSettlementReceiveBillAuditItemDao;
    @Autowired
    private SettlementConfigDao settlementConfigDao;
    @Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao;
    @Autowired
    private ReceivePaymentSubjectDao receivePaymentSubjectDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private PlatformStatementOrderDao platformStatementOrderDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private ReceiveInvoicenoDao receiveInvoicenoDao;
    @Autowired
    private ReceiveBillInvoiceDao receiveBillInvoiceDao;
    @Autowired
    private FileTempDataMongoDao fileTempDataMongoDao;
    @Autowired
    private FileTempDao fileTempDao;
    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao;
    @Autowired
    private TocSettlementReceiveBillVerifyItemDao tocSettlementReceiveBillVerifyItemDao;
    @Autowired
    private PlatformSettlementConfigDao platformSettlementConfigDao;
    @Autowired
    private ReceiveCloseAccountsDao receiveCloseAccountsDao;

    @Override
    public TocSettlementReceiveBillDTO listReceiveBillByPage(User user,TocSettlementReceiveBillDTO dto) throws SQLException {
        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
        TocSettlementReceiveBillDTO tocSettlementReceiveBillDTO = tocSettlementReceiveBillDao.listReceiveBillByPage(dto);
        /*List<TocSettlementReceiveBillDTO> tocSettlementReceiveBillDTOS = tocSettlementReceiveBillDTO.getList();
        List<Long> ids = new ArrayList<>();
        Map<Long, TocSettlementReceiveBillDTO> receiveBillDTOMap = new LinkedHashMap<>();
        for (TocSettlementReceiveBillDTO billDTO : tocSettlementReceiveBillDTOS) {
            ids.add(billDTO.getId());
            billDTO.setReceivablePrice(BigDecimal.ZERO);
            receiveBillDTOMap.put(billDTO.getId(), billDTO);
        }

        if (!ids.isEmpty()) {
            //获取应收账单的应收明细金额
            List<Map<String, Object>> itemPriceList = tocSettlementReceiveBillItemDao.listItemPrice(ids);
            for (Map<String, Object> map : itemPriceList) {
                Long billId = (Long) map.get("billId");
                BigDecimal originalAmount = (BigDecimal) map.get("originalAmount");
                TocSettlementReceiveBillDTO receiveBillTemp = receiveBillDTOMap.get(billId);
                if (originalAmount != null) {
                    receiveBillTemp.setReceivablePrice(originalAmount);
                }
            }
            //获取应收账单的费用金额
            List<Map<String, Object>> costPriceList = tocSettlementReceiveBillCostItemDao.listCostPrice(ids);
            for (Map<String, Object> map : costPriceList) {
                Long billId = (Long) map.get("billId");
                BigDecimal originalAmount = (BigDecimal) map.get("originalAmount");
                TocSettlementReceiveBillDTO receiveBillTemp = receiveBillDTOMap.get(billId);
                if (originalAmount != null) {
                    BigDecimal receivablePrice = receiveBillTemp.getReceivablePrice().add(originalAmount);
                    receiveBillTemp.setReceivablePrice(receivablePrice);
                }
            }

        }
        List<TocSettlementReceiveBillDTO> tocSettlementReceiveBillDTOList = new ArrayList<>(receiveBillDTOMap.values());
        tocSettlementReceiveBillDTO.setList(tocSettlementReceiveBillDTOList);*/
        return tocSettlementReceiveBillDTO;
    }

    @Override
    public void saveTocSettlementBill(TocSettlementReceiveBillModel model, MultipartFile file) throws Exception {

        TocSettlementReceiveBillModel searchModel = model;
        if (StringUtils.isBlank(model.getCode())) {
            searchModel = tocSettlementReceiveBillDao.searchById(model.getId());
        }
        //保存文件
        String basePath = ApolloUtilDB.orderBasepath;//excel文件存放目录
        basePath += "/TOCBILL/" + searchModel.getMerchantId() ;

        //创建目录
        File outFil = new File(basePath);
        if(!outFil.exists()) {
            outFil.mkdirs() ;
        }

        String fileName = basePath + "/" + model.getCode() + "平台结算单.xlsx";

        //获取文件输入流
        byte buffer[] = new byte[1024];
        InputStream ins = file.getInputStream();
        int len = 0 ;

        //获取输出流
        FileOutputStream fos = new FileOutputStream(fileName) ;

        while((len = ins.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }

        fos.close();
        ins.close();
        model.setFilePath(fileName);
        if (model.getId() == null) {
            tocSettlementReceiveBillDao.save(model);
        } else {
            model.setCode(searchModel.getCode());
            tocSettlementReceiveBillDao.modify(model);
        }

        //推送mq保存校验
        Map<String, Object> sendMap = new HashMap<String, Object>();
        sendMap.put("billCode", model.getCode());
        JSONObject sendJson = JSONObject.fromObject(sendMap);
        SendResult send = rocketMQProducer.send(sendJson.toString(), MQOrderEnum.TOC_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOC_RECEIVE_BILL_GENERATE.getTags());
        System.out.println(send);
    }

    @Override
    public List<SelectBean> getCustomerListByShop(MerchantShopRelMongo mongo) throws Exception {
        Map<String, Object> shopParams = new HashMap<String, Object>();
        shopParams.put("status", "1");

        shopParams.put("shopTypeCode", mongo.getShopTypeCode());
        shopParams.put("storePlatformCode", mongo.getStorePlatformCode());
        if (StringUtils.isNotBlank(mongo.getShopCode())) {
            shopParams.put("shopCode", mongo.getShopCode());
        }
        List<MerchantShopRelMongo> shopRelMongoList = merchantShopRelMongoDao.findAll(shopParams);
        List<SelectBean> selectBeans = new ArrayList<>();
        for (MerchantShopRelMongo shopRelMongo : shopRelMongoList) {
            SelectBean selectBean = new SelectBean();
            selectBean.setValue(String.valueOf(shopRelMongo.getCustomerId()));
            selectBean.setLabel(shopRelMongo.getCustomerName());
            selectBeans.add(selectBean);
        }
        return selectBeans;
    }

    @Override
    public TocSettlementReceiveBillDTO searchDTOById(Long id) throws SQLException {
        TocSettlementReceiveBillDTO tocSettlementReceiveBillDTO = tocSettlementReceiveBillDao.searchDTOById(id);
        return tocSettlementReceiveBillDTO;
    }

    @Override
    public List<Map<String, Object>> receiveTotalById(Long id) throws SQLException {
        return tocSettlementReceiveBillItemDao.receiveTotalById(id);
    }

    @Override
    public List<Map<String, Object>> deductionTotalById(Long id) throws SQLException {
        List<Map<String, Object>> deductionMapList = tocSettlementReceiveBillCostItemDao.deductionTotalById(id);
        Map<Long, Map<String, Object>> resultMap = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map<String, Object> map : deductionMapList) {
            Long projectId = (Long) map.get("project_id");
            String projectName = (String) map.get("project_name");
            String billType = (String) map.get("bill_type");
            BigDecimal totalNum = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
            BigDecimal totalPrice = map.get("totalPrice") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalPrice");
            BigDecimal totalRmbPrice = map.get("totalRmbPrice") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalRmbPrice");
            if (resultMap.containsKey(projectId)) {
                Map<String, Object> costMap = resultMap.get(projectId);
                BigDecimal existNum = (BigDecimal) costMap.get("totalNum");
                BigDecimal existPrice = (BigDecimal) costMap.get("totalPrice");
                BigDecimal existRmbPrice = (BigDecimal) costMap.get("totalRmbPrice");
                if (DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0.equals(billType)) {
                    totalPrice = existPrice.add(totalPrice);
                    totalRmbPrice = existRmbPrice.add(totalRmbPrice);
                } else {
                    totalPrice = existPrice.subtract(totalPrice);
                    totalRmbPrice = existRmbPrice.subtract(totalRmbPrice);
                }
                totalNum = totalNum.add(existNum);
                costMap.put("totalNum", totalNum);
                costMap.put("totalPrice", totalPrice);
                costMap.put("totalRmbPrice", totalRmbPrice);
            } else {
                if(projectId != null) {
                    SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(projectId);
                    if (settlementConfigModel != null) {
                        map.put("parentProjectName",settlementConfigModel.getParentProjectName());
                    }
                }
                if (DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1.equals(billType)) {
                    totalPrice = totalPrice.negate();
                    totalRmbPrice = totalRmbPrice.negate();
                }
                map.put("totalNum", totalNum);
                map.put("totalPrice", totalPrice);
                map.put("projectName", projectName);
                map.put("projectId", projectId);
                map.put("totalRmbPrice", totalRmbPrice);
                resultMap.put(projectId, map);
            }
        }
        for (Long key : resultMap.keySet()) {
            resultList.add(resultMap.get(key));
        }
        return resultList;
    }

    @Override
    public TocSettlementReceiveBillItemDTO listReceiveItemByPage(TocSettlementReceiveBillItemDTO dto) throws SQLException {
        return tocSettlementReceiveBillItemDao.listReceiveItemByPage(dto);
    }

    @Override
    public TocSettlementReceiveBillCostItemDTO listReceiveCostItemByPage(TocSettlementReceiveBillCostItemDTO dto) throws SQLException {
        return tocSettlementReceiveBillCostItemDao.listReceiveCostItemByPage(dto);
    }

    @Override
    public boolean delToCReceiveBill(List<Long> ids) throws Exception {
        //待更新为未生成应收单的平台结算单id
        List<Long> platformIds = new ArrayList<>();
        for (Long id : ids) {
            // 根据id获取信息
            TocSettlementReceiveBillModel model = tocSettlementReceiveBillDao.searchById(id);
            // 判断状态是否待提交
            if (!(model.getBillStatus().equals(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_00) ||
                    model.getBillStatus().equals(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_08))) {
                throw new RuntimeException("toC结算单：" + model.getCode() + "不是待提交或者生成失败状态，不能删除");
            }
            PlatformStatementOrderModel platformStatementOrderModel = new PlatformStatementOrderModel();
            platformStatementOrderModel.setReceiveCode(model.getCode());
            List<PlatformStatementOrderModel> statementOrderModels = platformStatementOrderDao.list(platformStatementOrderModel);
            for (PlatformStatementOrderModel statementOrderModel : statementOrderModels) {
                platformIds.add(statementOrderModel.getId());
            }
        }
        int num = tocSettlementReceiveBillDao.delete(ids);
        if (num > 0) {
            for (Long id : ids) {
                tocSettlementReceiveBillItemDao.delItems(id);
                tocSettlementReceiveBillCostItemDao.delCostItems(id, null);
            }
            //对关联的平台结算进行解绑关联关系
            if (!platformIds.isEmpty()) {
                platformStatementOrderDao.batchUpdate(platformIds, "", DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_0);
            }
        }
        return true;
    }

    @Override
    public List<SelectBean> getBrandSelectBeans() {
        List<SelectBean> brandSelectBeans = new ArrayList<>();
        List<BrandSuperiorMongo> brandList = brandSuperiorMongoDao.findAll(new HashMap<>());
        if (brandList != null && brandList.size() > 0) {
            for (BrandSuperiorMongo brandSuperiorMongo : brandList) {
                SelectBean selectBean = new SelectBean();
                selectBean.setValue(String.valueOf(brandSuperiorMongo.getBrandSuperiorId()));
                selectBean.setLabel(brandSuperiorMongo.getName());
                brandSelectBeans.add(selectBean);
            }
        }
        return brandSelectBeans;
    }

    @Override
    public Map<String, String> confirmReceiveBill(User user,Long id) throws Exception {
        Map<String, String> retMap = new HashMap<String, String>();
        TocSettlementReceiveBillModel receiveBillModel = tocSettlementReceiveBillDao.searchById(id);
        if (!DERP_ORDER.RECEIVEBILL_BILLSTATUS_00.equals(receiveBillModel.getBillStatus())) {
            retMap.put("code", "01");
            retMap.put("message", "此账单账单状态不是待提交");
            return retMap;
        }
        receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_01);
        tocSettlementReceiveBillDao.modify(receiveBillModel);
        TocSettlementReceiveBillAuditItemModel receiveBillAuditItemModel = new TocSettlementReceiveBillAuditItemModel();
        receiveBillAuditItemModel.setBillId(id);
        receiveBillAuditItemModel.setSubmitId(user.getId());
        receiveBillAuditItemModel.setSubmitter(user.getName());
        receiveBillAuditItemModel.setSubmitDate(TimeUtils.getNow());
        receiveBillAuditItemModel.setAuditType(DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITTYPE_0);
        tocSettlementReceiveBillAuditItemDao.save(receiveBillAuditItemModel);

        /**
         * 发送审核提醒邮件
         */
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(receiveBillModel.getBuId());
        emailUserDTO.setBuName(receiveBillModel.getBuName());
        emailUserDTO.setMerchantId(receiveBillModel.getMerchantId());
        emailUserDTO.setMerchantName(receiveBillModel.getMerchantName());
        emailUserDTO.setOrderCode(receiveBillModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1);
        emailUserDTO.setSupplier(receiveBillModel.getCustomerName());
        emailUserDTO.setAmount("CNY&nbsp;" + receiveBillModel.getReceivableAmount().toPlainString());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setSubmitId(Arrays.asList(String.valueOf(user.getId())));
        emailUserDTO.setStorePlatformName(DERP.getLabelByKey(DERP.storePlatformList, receiveBillModel.getStorePlatformCode()));

        JSONObject emailJson = JSONObject.fromObject(emailUserDTO) ;

        rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                MQErpEnum.SEND_REMINDER_MAIL.getTags());

        retMap.put("code", "00");
        retMap.put("message", "成功");
        return retMap;
    }

    @Override
    public Map<String, String> auditReceiveBill(User user,TocSettlementReceiveBillAuditItemModel model, String billInDate) throws Exception {
        Map<String, String> retMap = new HashMap<String, String>();
        TocSettlementReceiveBillModel receiveBillModel = tocSettlementReceiveBillDao.searchById(model.getBillId());
        if(StringUtils.isNotBlank(billInDate)) {
            //可选日期必须小于等于当前日期，且大于应收关帐月份；
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = dateFormat1.parse(billInDate);
            receiveBillModel.setBillInDate(new java.sql.Date(parse.getTime()));

            if(new Date().before(receiveBillModel.getBillInDate())) {
                retMap.put("code", "01");
                retMap.put("message", "关账日期必须小于等于当前日期");
                return retMap;
            }

            Map<String, Object> param = new HashMap<>();
            param.put("merchantId", receiveBillModel.getMerchantId());
            param.put("buId", receiveBillModel.getBuId());
            param.put("status", DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030);
            ReceiveCloseAccountsDTO latestMap = receiveCloseAccountsDao.getLatestExcludeIdByMap(param);

//            ReceiveCloseAccountsModel receiveCloseAccountsModel = new ReceiveCloseAccountsModel();
//            receiveCloseAccountsModel.setMonth(TimeUtils.formatStrToStr(billInDate, "yyyy-MM-dd", "yyyy-MM"));
//            receiveCloseAccountsModel.setMerchantId(receiveBillModel.getMerchantId());
//            receiveCloseAccountsModel.setBuId(receiveBillModel.getBuId());
//            receiveCloseAccountsModel.setStatus(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030);
//            ReceiveCloseAccountsModel latestMap = receiveCloseAccountsDao.searchByModel(receiveCloseAccountsModel);

            if(latestMap == null) {
                retMap.put("code", "01");
                retMap.put("message", "没有对应的关账记录");
                return retMap;
            }
            if(latestMap.getMonth().compareTo(TimeUtils.formatStrToStr(billInDate, "yyyy-MM-dd", "yyyy-MM")) >= 0) {
                retMap.put("code", "01");
                retMap.put("message", "关账日期大于应收关账月份");
                return retMap;
            }
        }

        if (!(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_01.equals(receiveBillModel.getBillStatus())
                || DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_05.equals(receiveBillModel.getBillStatus()))) {
            retMap.put("code", "01");
            retMap.put("message", "此账单状态不是待审核/作废审批");
            return retMap;
        }
        Timestamp auditTime = TimeUtils.getNow();
        boolean isNcSyn = false; //是否同步NC
        boolean isSubmitAudit = true;

        ReceiveBillInvoiceModel invoiceModel = null;
        List<Long> relIdList = new ArrayList<>();
        List<String> relCodeList = new ArrayList<>(); //关联的其他应收发票单号集合

        //1.提交审核
        if (DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_01.equals(receiveBillModel.getBillStatus())) {
            /**1.1审核通过：
             * a.更新应收单状态为“待核销”；
             * b.核销toc暂估应收单
             */
            if (DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITRESULT_00.equals(model.getAuditResult())) {
                receiveBillModel.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_02);
                if(receiveBillModel.getBillInDate() == null) {
                    retMap.put("code", "01");
                    retMap.put("message", "入账月份不能为空");
                    return retMap;
                }
            } else {//1.2驳回：更新应收账单的账单状态为“待提交“
                receiveBillModel.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_00);
            }
        } else if (DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_05.equals(receiveBillModel.getBillStatus())) {//2.作废审核
            isSubmitAudit = false;
            /**2.1审核通过：
             * a.更新应收单状态为“已作废”；
             * b.当nc状态为“已同步”时，触发nc 4.8接口
             * c.核销toc暂估应收单:找到对应被当前作废的平台结算单核销的电商订单进行删除结算信息//todo
             */
            if (DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITRESULT_00.equals(model.getAuditResult())) {
                receiveBillModel.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_06);
                if (!DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_10.equals(receiveBillModel.getNcStatus())) {
                    isNcSyn = true;
                }

                if (DERP_ORDER.RECEIVEBILL_INVOICESTATUS_01.equals(receiveBillModel.getInvoiceStatus()) ||
                        DERP_ORDER.RECEIVEBILL_INVOICESTATUS_03.equals(receiveBillModel.getInvoiceStatus())) {
                    invoiceModel = receiveBillInvoiceDao.searchById(receiveBillModel.getInvoiceId());
                    if (invoiceModel == null || invoiceModel.getInvoiceRelIds() == null) {
                        retMap.put("code", "01");
                        retMap.put("message", "找不到应收账单对应的发票");
                        return retMap;
                    }
                    TocSettlementReceiveBillModel billModel = new TocSettlementReceiveBillModel();
                    billModel.setInvoiceId(receiveBillModel.getInvoiceId());
                    List<TocSettlementReceiveBillModel> relInvoiceModels = tocSettlementReceiveBillDao.list(billModel);
                    for (TocSettlementReceiveBillModel relModel : relInvoiceModels) {
                        relIdList.add(relModel.getId());
                        relCodeList.add(relModel.getCode());
                    }
                    if (relInvoiceModels != null) { //更新审核账单的发票状态为已作废
                        receiveBillModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_02);
                    }
                    if (relInvoiceModels != null && relInvoiceModels.size() > 0) {
                        relIdList.remove(model.getBillId());
                        relCodeList.remove(receiveBillModel.getCode());
                        if (relIdList.size() > 0) {
                            //批量更新关联应收账单的开票状态更新为“待开票”, 清空发票关联id
                            tocSettlementReceiveBillDao.batchUpdateInvoiceStatus(relIdList, DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00);
                        }
                    }
                }
            } else {//2.2驳回：更新应收账单的账单状态为“待核销“
                receiveBillModel.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_02);
            }
        }
        tocSettlementReceiveBillDao.modify(receiveBillModel);
        //保存审批记录
        TocSettlementReceiveBillAuditItemModel receiveBillAuditItemModel = new TocSettlementReceiveBillAuditItemModel();
        receiveBillAuditItemModel.setBillId(model.getBillId());
        receiveBillAuditItemModel.setAuditId(user.getId());
        receiveBillAuditItemModel.setAuditor(user.getName());
        receiveBillAuditItemModel.setAuditDate(auditTime);
        receiveBillAuditItemModel.setAuditResult(model.getAuditResult());
        receiveBillAuditItemModel.setAuditRemark(model.getAuditRemark());
        tocSettlementReceiveBillAuditItemDao.updateAuditItem(receiveBillAuditItemModel);

        //相应的发票上加盖作废戳
        if (invoiceModel != null && DERP_ORDER.RECEIVEBILLAUDIT_AUDITRESULT_00.equals(model.getAuditResult())) {

            //在发票文件增加已作废图标
            String updateUrl = addWatermark(invoiceModel.getInvoiceNo(), user, invoiceModel.getInvoicePath());

            ReceiveBillInvoiceModel receiveBillInvoiceModel = new ReceiveBillInvoiceModel();
            receiveBillInvoiceModel.setId(receiveBillModel.getInvoiceId());
            receiveBillInvoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_02);
            receiveBillInvoiceModel.setInvoicePath(updateUrl);
            if (isNcSyn) {
                receiveBillInvoiceModel.setSynchronizerId(user.getId());
                receiveBillInvoiceModel.setSynchronizer(user.getName());
            }
            receiveBillInvoiceDao.modify(receiveBillInvoiceModel);
        }

        //推送NC 4.8接口
        if (isNcSyn) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            ReceiveHcInvalidRoot invalidRoot = new ReceiveHcInvalidRoot();
            invalidRoot.setConfirmBillId(receiveBillModel.getCode());
            invalidRoot.setSourceCode(ApolloUtil.ncSourceType);
            invalidRoot.setState("2");
            invalidRoot.setCancelTime(sdf.format(auditTime));
            invalidRoot.setRemark(model.getAuditRemark());
            JSONObject json = JSONObject.fromObject(invalidRoot);
            String clearText = json.toString();
            //提交NC
            NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_08.getUri(), clearText);

            if (relCodeList.size() > 0) {
                for (String relCode : relCodeList) {
                    InvoiceUpdateRoot updateRoot = new InvoiceUpdateRoot();
                    updateRoot.setSourceCode(ApolloUtil.ncSourceType);
                    updateRoot.setConfirmBillId(relCode);
                    List<com.topideal.api.nc.nc10.Details> detailsList = new ArrayList<>();
                    com.topideal.api.nc.nc10.Details details = new com.topideal.api.nc.nc10.Details();
                    details.setSindex("1");
                    details.setInvoiceCode(invoiceModel.getInvoiceNo());
                    details.setType("3"); //1=蓝票，2=红冲，3=作废
                    details.setInvoiceDate(dateFormat.format(invoiceModel.getInvoiceDate()));
                    detailsList.add(details);
                    updateRoot.setDetails(detailsList);
                    JSONObject updateJson = JSONObject.fromObject(updateRoot);
                    String updateText = updateJson.toString();
                    //提交NC
                    String updateResult = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_10.getUri(), updateText);

                    JSONObject updateResultJson = JSONObject.fromObject(updateResult);
                    if (updateResultJson.containsKey("code") && updateResultJson.getString("code").equals("1002")) {
                        throw new RuntimeException(updateResultJson.getString("msg"));
                    }
                }
            }
        }

        // 发送MQ记录核销跟踪
        if(DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITRESULT_00.equals(model.getAuditResult())) {
            //发送MQ生成核销跟踪
            Map<String, Object> param = new HashMap<>();
            param.put("operateType", DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITTYPE_0);
            param.put("receiveCodes", receiveBillModel.getCode());
            param.put("orderType", DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_1);
            JSONObject verifyJson = JSONObject.fromObject(param) ;
            rocketMQProducer.send(verifyJson.toString(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTopic(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTags());
        }

        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(receiveBillModel.getBuId());
        emailUserDTO.setBuName(receiveBillModel.getBuName());
        emailUserDTO.setMerchantId(receiveBillModel.getMerchantId());
        emailUserDTO.setMerchantName(receiveBillModel.getMerchantName());
        emailUserDTO.setOrderCode(receiveBillModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9));
        emailUserDTO.setSupplier(receiveBillModel.getCustomerName());
        emailUserDTO.setAmount("CNY&nbsp;" + receiveBillModel.getReceivableAmount().toPlainString());
        emailUserDTO.setAuditorId(user.getId());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setStorePlatformName(DERP.getLabelByKey(DERP.storePlatformList, receiveBillModel.getStorePlatformCode()));

        if (isSubmitAudit) {
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2);
        } else {//发送作废完成提醒
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_6);
        }

        if (DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITRESULT_01.equals(model.getAuditResult())) {
            emailUserDTO.setStatus("已驳回");
        } else {
            emailUserDTO.setStatus("已通过");
        }

        JSONObject emailJson = JSONObject.fromObject(emailUserDTO) ;

        rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                MQErpEnum.SEND_REMINDER_MAIL.getTags());

        retMap.put("code", "00");
        retMap.put("billCode", receiveBillModel.getCode());
        retMap.put("message", "成功");
        return retMap;
    }

    @Override
    public List<ToCReceiveBillToNCDTO> listReceiveBillsToNCById(String ids) throws SQLException {
        List<Long> idList = StrUtils.parseIds(ids);

        List<TocSettlementReceiveBillModel> billModels = tocSettlementReceiveBillDao.listByIds(idList);

        List<TocSettlementReceiveBillItemModel> itemModels = tocSettlementReceiveBillItemDao.listByProject(idList);
        Map<Long, List<TocSettlementReceiveBillItemModel>> itemModelsMap = new HashMap<>();

        for (TocSettlementReceiveBillItemModel itemModel : itemModels) {
            List<TocSettlementReceiveBillItemModel> existModels = new ArrayList<>();
            if (itemModelsMap.get(itemModel.getBillId()) != null) {
                existModels.addAll(itemModelsMap.get(itemModel.getBillId()));
            }
            existModels.add(itemModel);
            itemModelsMap.put(itemModel.getBillId(), existModels);
        }

        List<TocSettlementReceiveBillCostItemModel> costItemModels = tocSettlementReceiveBillCostItemDao.listByProject(idList);
        Map<Long, List<TocSettlementReceiveBillCostItemModel>> costItemModelsMap = new HashMap<>();

        for (TocSettlementReceiveBillCostItemModel itemModel : costItemModels) {
            List<TocSettlementReceiveBillCostItemModel> existModels = new ArrayList<>();
            if (costItemModelsMap.get(itemModel.getBillId()) != null) {
                existModels.addAll(costItemModelsMap.get(itemModel.getBillId()));
            }
            existModels.add(itemModel);
            costItemModelsMap.put(itemModel.getBillId(), existModels);
        }

        Map<String, ToCReceiveBillToNCDTO> resultMap = new HashMap<>();

        for (TocSettlementReceiveBillModel billModel : billModels) {

            //应收明细
            List<TocSettlementReceiveBillItemModel> tocSettlementReceiveBillItemModels = itemModelsMap.get(billModel.getId());
            if (tocSettlementReceiveBillItemModels != null) {
                for (TocSettlementReceiveBillItemModel itemModel : tocSettlementReceiveBillItemModels) {
                    SettlementConfigModel settlementConfig = settlementConfigDao.searchById(itemModel.getProjectId());
                    //根据费项NC收支费项id+母品牌id，
                    String key = settlementConfig.getPaymentSubjectId() + "_" + itemModel.getParentBrandId();
                    if (resultMap.containsKey(key)) {
                        ToCReceiveBillToNCDTO ncdto = resultMap.get(key);
                        BigDecimal price = ncdto.getPrice();
                        price = price.add(itemModel.getRmbAmount());
                        ncdto.setPrice(price);
                    } else {
                        Map<String, Object> brandParams = new HashMap<>();
                        brandParams.put("brandSuperiorId", itemModel.getParentBrandId());
                        BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(brandParams);
                        ToCReceiveBillToNCDTO ncdto = new ToCReceiveBillToNCDTO();
                        ncdto.setBillId(billModel.getId());
                        ncdto.setBillCode(billModel.getCode());
                        ncdto.setBuId(billModel.getBuId());
                        ncdto.setBuName(billModel.getBuName());
                        ncdto.setCurrency(DERP.CURRENCYCODE_CNY);
                        ncdto.setPrice(itemModel.getRmbAmount());
                        if (brandSuperior != null) {
                            ncdto.setParentBrandName(brandSuperior.getName());
                        }
                        ncdto.setPaymentSubjectId(settlementConfig.getPaymentSubjectId());
                        ncdto.setPaymentSubjectName(settlementConfig.getParentProjectName());
                        ncdto.setSettlementTypeLabel("应收");
                        ncdto.setSaleModelLabel("经销模式");
                        resultMap.put(key, ncdto);
                    }
                }
            }

            //费用明细
            List<TocSettlementReceiveBillCostItemModel> tocSettlementReceiveBillCostItemModels = costItemModelsMap.get(billModel.getId());
            if (tocSettlementReceiveBillCostItemModels != null) {
                for (TocSettlementReceiveBillCostItemModel itemModel : tocSettlementReceiveBillCostItemModels) {
                    SettlementConfigModel settlementConfig = settlementConfigDao.searchById(itemModel.getProjectId());
                    String key = settlementConfig.getPaymentSubjectId() + "_" + itemModel.getParentBrandId();
                    if (resultMap.containsKey(key)) {
                        ToCReceiveBillToNCDTO ncdto = resultMap.get(key);
                        BigDecimal price = ncdto.getPrice();
                        price = price.add(itemModel.getRmbAmount());
                        ncdto.setPrice(price);
                    } else {
                        Map<String, Object> brandParams = new HashMap<>();
                        brandParams.put("brandSuperiorId", itemModel.getParentBrandId());
                        BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(brandParams);
                        ToCReceiveBillToNCDTO ncdto = new ToCReceiveBillToNCDTO();
                        ncdto.setBillId(billModel.getId());
                        ncdto.setBillCode(billModel.getCode());
                        ncdto.setBuId(billModel.getBuId());
                        ncdto.setBuName(billModel.getBuName());
                        ncdto.setCurrency(DERP.CURRENCYCODE_CNY);
                        ncdto.setPrice(itemModel.getRmbAmount());
                        if (brandSuperior != null) {
                            ncdto.setParentBrandName(brandSuperior.getName());
                        }
                        ncdto.setPaymentSubjectId(settlementConfig.getPaymentSubjectId());
                        ncdto.setPaymentSubjectName(settlementConfig.getParentProjectName());
                        ncdto.setSettlementTypeLabel("应收");
                        ncdto.setSaleModelLabel("经销模式");
                        resultMap.put(key, ncdto);
                    }
                }
            }

        }


        List<ToCReceiveBillToNCDTO> resultList = new ArrayList<>(resultMap.values());
        return resultList;
    }

    @Override
    public Map<String, String> synNC( User user ,String ids) throws Exception {
        try {
            List<Long> idList = StrUtils.parseIds(ids);
            SimpleDateFormat month = new SimpleDateFormat("MM月") ;
            Map<String, String> result = new HashMap<>();
            List<TocSettlementReceiveBillModel> billModels = tocSettlementReceiveBillDao.listByIds(idList);

            Map<Long, CustomerInfoMogo> customerInfoMogoMap = new HashMap<>();
            Map<Long, MerchantBuRelMongo> merchantBuRelMongoMap = new HashMap<>();

            for (TocSettlementReceiveBillModel billModel : billModels) {
                Map<String, Object> customerParam = new HashMap<>();
                customerParam.put("customerid", billModel.getCustomerId());
                CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParam);
                if (customer == null) {
                    result.put("code", "01");
                    result.put("message", "客户不存在！");
                    return result;
                }
                customerInfoMogoMap.put(billModel.getId(), customer);

                Map<String, Object> buMap = new HashMap<>();
                buMap.put("buId", billModel.getBuId());
                buMap.put("merchantId", billModel.getMerchantId());
                buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
                MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);
                if (merchantBuRelMongo == null) {
                    result.put("code", "01");
                    result.put("message", "事业部不存在！");
                    return result;
                }
                merchantBuRelMongoMap.put(billModel.getId(), merchantBuRelMongo);
            }

            //应收明细
            List<TocSettlementReceiveBillItemModel> itemModels = tocSettlementReceiveBillItemDao.listByProject(idList);
            Map<Long, List<TocSettlementReceiveBillItemModel>> itemModelsMap = new HashMap<>();

            for (TocSettlementReceiveBillItemModel itemModel : itemModels) {
                List<TocSettlementReceiveBillItemModel> existModels = new ArrayList<>();
                if (itemModelsMap.get(itemModel.getBillId()) != null) {
                    existModels.addAll(itemModelsMap.get(itemModel.getBillId()));
                }
                existModels.add(itemModel);
                itemModelsMap.put(itemModel.getBillId(), existModels);
            }

            //费用明细
            List<TocSettlementReceiveBillCostItemModel> costItemModels = tocSettlementReceiveBillCostItemDao.listByProject(idList);
            Map<Long, List<TocSettlementReceiveBillCostItemModel>> costItemModelsMap = new HashMap<>();

            for (TocSettlementReceiveBillCostItemModel itemModel : costItemModels) {
                List<TocSettlementReceiveBillCostItemModel> existModels = new ArrayList<>();
                if (costItemModelsMap.get(itemModel.getBillId()) != null) {
                    existModels.addAll(costItemModelsMap.get(itemModel.getBillId()));
                }
                existModels.add(itemModel);
                costItemModelsMap.put(itemModel.getBillId(), existModels);
            }

            //每个应收分别推送NC
            for (TocSettlementReceiveBillModel billModel : billModels) {
                int index = 1;
                BigDecimal totalPrice = new BigDecimal("0");
                List<Details> detailsList = new ArrayList<Details>() ;

                List<TocSettlementReceiveBillItemModel> tocSettlementReceiveBillItemModels = itemModelsMap.get(billModel.getId());

                if (tocSettlementReceiveBillItemModels != null) {
                    for (TocSettlementReceiveBillItemModel itemModel : tocSettlementReceiveBillItemModels) {
                        if (itemModel.getRmbAmount().compareTo(new BigDecimal("0")) == 0) {
                            continue;
                        }
                        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(itemModel.getProjectId());
                        if (settlementConfigModel == null) {
                            result.put("code", "01");
                            result.put("message", "费项配置不存在！");
                            return result;
                        }
                        ReceivePaymentSubjectModel subjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
                        if (subjectModel == null) {
                            result.put("code", "01");
                            result.put("message", "NC收支费项不存在！");
                            return result;
                        }
                        Map<String, Object> brandParams = new HashMap<>();
                        brandParams.put("brandSuperiorId", itemModel.getParentBrandId());
                        BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(brandParams);
                        if (brandSuperior == null) {
                            result.put("code", "01");
                            result.put("message", "母品牌不存在！");
                            return result;
                        }
                        if (StringUtils.isBlank(brandSuperior.getNcCode())) {
                            result.put("code", "01");
                            result.put("message", "母品牌NC编码为空！");
                            return result;
                        }

                        Details details = new Details() ;
                        String billMonth = billModel.getSettlementDate() != null ? month.format(billModel.getSettlementDate()) : "";
                        String parentBrandName = StringUtils.isNotBlank(brandSuperior.getName()) ? brandSuperior.getName() : "";
                        String abstracts = billMonth
                                + DERP_ORDER.getLabelByKey(DERP.storePlatformList , billModel.getStorePlatformCode())
                                + parentBrandName + settlementConfigModel.getProjectName();
                        details.setSindex(String.valueOf(index));
                        details.setInExpCode(settlementConfigModel.getInExpCode());
                        details.setCurrency(DERP.CURRENCYCODE_CNY);
                        details.setAmount(itemModel.getRmbAmount());
                        details.setRate("0%");
                        details.setTax(new BigDecimal("0.00"));
                        details.setAbstracts(abstracts);
                        details.setSaleCode("009");
                        details.setDetpCode(merchantBuRelMongoMap.get(billModel.getId()).getBuCode());
                        details.setBrandCode(brandSuperior.getNcCode());
                        detailsList.add(details) ;
                        index++;
                        totalPrice = totalPrice.add(itemModel.getRmbAmount());
                    }
                }

                List<TocSettlementReceiveBillCostItemModel> tocSettlementReceiveBillCostItemModels = costItemModelsMap.get(billModel.getId());
                if (tocSettlementReceiveBillCostItemModels != null) {
                    for (TocSettlementReceiveBillCostItemModel itemModel : tocSettlementReceiveBillCostItemModels) {
                        if (itemModel.getRmbAmount().compareTo(new BigDecimal("0")) == 0) {
                            continue;
                        }

                        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(itemModel.getProjectId());
                        if (settlementConfigModel == null) {
                            result.put("code", "01");
                            result.put("message", "费项配置不存在！");
                            return result;
                        }
                        ReceivePaymentSubjectModel subjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
                        if (subjectModel == null) {
                            result.put("code", "01");
                            result.put("message", "NC收支费项不存在！");
                            return result;
                        }
                        Map<String, Object> brandParams = new HashMap<>();
                        brandParams.put("brandSuperiorId", itemModel.getParentBrandId());
                        BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(brandParams);
                        if (brandSuperior == null) {
                            result.put("code", "01");
                            result.put("message", "母品牌不存在！");
                            return result;
                        }
                        if (StringUtils.isBlank(brandSuperior.getNcCode())) {
                            result.put("code", "01");
                            result.put("message", "母品牌NC编码为空！");
                            return result;
                        }

                        Details details = new Details() ;
                        String billMonth = billModel.getSettlementDate() != null ? month.format(billModel.getSettlementDate()) : "";
                        String parentBrandName = StringUtils.isNotBlank(brandSuperior.getName()) ? brandSuperior.getName() : "";
                        String abstracts = billMonth
                                + DERP_ORDER.getLabelByKey(DERP.storePlatformList , billModel.getStorePlatformCode())
                                + parentBrandName + settlementConfigModel.getProjectName();
                        details.setSindex(String.valueOf(index));
                        details.setInExpCode(settlementConfigModel.getInExpCode());
                        details.setCurrency(DERP.CURRENCYCODE_CNY);
                        details.setAmount(itemModel.getRmbAmount());
                        details.setRate("0%");
                        details.setTax(new BigDecimal("0.00"));
//                details.setAccCode(subjectModel.getSubCode());
                        details.setAbstracts(abstracts);
                        details.setSaleCode("009");
                        details.setDetpCode(merchantBuRelMongoMap.get(billModel.getId()).getBuCode());
                        details.setBrandCode(brandSuperior.getNcCode());
                        detailsList.add(details) ;
                        index++;
                        totalPrice = totalPrice.add(itemModel.getRmbAmount());
                    }
                }

                Timestamp maxAuditDate = tocSettlementReceiveBillAuditItemDao.getMaxAuditDate(billModel.getId());
                ReceiveBillJsonRoot root = new ReceiveBillJsonRoot() ;
                root.setConfirmBillId(billModel.getCode());
                root.setSourceCode(ApolloUtil.ncSourceType);
                root.setType("1");
                root.setCorCcode(user.getTopidealCode());
                root.setCusCcode(customerInfoMogoMap.get(billModel.getId()).getMainId());
                root.setYearMonth(TimeUtils.formatMonth(billModel.getBillInDate()).replace("-", ""));
                root.setCreated(TimeUtils.formatFullTime(billModel.getBillInDate()));
                root.setTotalAmount(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                root.setTaxAmount(new BigDecimal("0")) ;
                root.setCurrency(DERP.CURRENCYCODE_CNY);
                root.setChecked("1");
                root.setInvoiced("0");
                root.setRemark("");
                root.setDetails(detailsList);

                JSONObject json = JSONObject.fromObject(root);
                String clearText = json.toString() ;
                //提交NC
                String ncResult = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_07.getUri(), clearText);

                JSONObject resultJson =JSONObject.fromObject(ncResult);
                if (resultJson.containsKey("code") && resultJson.getString("code").equals("1001")) {
                    TocSettlementReceiveBillModel updateModel = new TocSettlementReceiveBillModel() ;
                    updateModel.setId(billModel.getId());
                    updateModel.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_11);
                    updateModel.setNcSynDate(TimeUtils.getNow());
                    tocSettlementReceiveBillDao.modify(updateModel) ;
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
    public List<Map<String, Object>> listForExport(TocSettlementReceiveBillDTO dto, User user) throws Exception {
        try {
            List<Map<String, Object>> exportList = new ArrayList<>();

            List<TocSettlementReceiveBillDTO> billDTOs = tocSettlementReceiveBillDao.listForExport(dto);

            for (TocSettlementReceiveBillDTO billDTO : billDTOs) {
                Map<String, Object> map = new HashMap<>();
                map.put("merchantName", billDTO.getMerchantName());
                map.put("billCode", billDTO.getCode());
                map.put("externalCode", billDTO.getExternalCode());
                map.put("buName", billDTO.getBuName());
                map.put("shopTypeName", billDTO.getShopTypeName());
                map.put("customerName", billDTO.getCustomerName());
                map.put("storePlatformName", billDTO.getStorePlatformName());
                map.put("shopName", billDTO.getShopName());
                map.put("receivablePrice", billDTO.getReceivableAmount());
                map.put("oriReceivableAmount", billDTO.getOriReceivableAmount());
                map.put("oriCurrency", billDTO.getOriCurrency());
                map.put("month", TimeUtils.formatMonth(billDTO.getSettlementDate()));
                map.put("billInDate", billDTO.getBillInDate());
                map.put("billStatus", billDTO.getBillStatusLabel());
                map.put("creater", billDTO.getCreater());
                map.put("createTime", billDTO.getCreateDate());
                map.put("ncCode", billDTO.getNcCode());
                map.put("ncStatus", billDTO.getNcStatusLabel());
                map.put("voucherName", billDTO.getVoucherName());
                map.put("voucherStatus", billDTO.getVoucherStatusLabel());
                map.put("id", billDTO.getId());
                exportList.add(map);
            }

            return exportList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> listForExportItem(Long id, User user) throws SQLException {
        try {
            List<Map<String, Object>> exportList = new LinkedList<>();
            TocSettlementReceiveBillDTO tocSettlementReceiveBillDTO = tocSettlementReceiveBillDao.searchDTOById(id);

            Integer pageSize = 5000;
            Integer itemCount = tocSettlementReceiveBillItemDao.countAllByBillId(id);
            for (int i = 0; i < itemCount; ) {
                int pageSub = (i+pageSize) < itemCount ? (i+pageSize) : itemCount;
                List<TocSettlementReceiveBillItemDTO> itemDTOS = tocSettlementReceiveBillItemDao.listForExportItem(id, i, pageSize);
                i = pageSub;
                for (TocSettlementReceiveBillItemDTO itemDTO : itemDTOS) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("billCode", tocSettlementReceiveBillDTO.getCode());
                    map.put("externalCode", itemDTO.getExternalCode());
                    map.put("goodsNo", itemDTO.getGoodsNo());
                    map.put("goodsName", itemDTO.getGoodsName());
                    map.put("num", itemDTO.getNum());
                    map.put("projectName", itemDTO.getProjectName());
                    map.put("brandName", itemDTO.getParentBrandName());
                    map.put("originalAmount", itemDTO.getOriginalAmount());
                    map.put("settlementRate", itemDTO.getSettlementRate());
                    map.put("rmbAmount", itemDTO.getRmbAmount());
                    map.put("oriCurrency", tocSettlementReceiveBillDTO.getOriCurrency());

                    if (DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1.equals(itemDTO.getBillType())) {
                        if (itemDTO.getOriginalAmount() != null) {
                            map.put("originalAmount", itemDTO.getOriginalAmount().negate());
                        }
                        if (itemDTO.getRmbAmount() != null) {
                            map.put("rmbAmount", itemDTO.getRmbAmount().negate());
                        }

                    }
                    exportList.add(map);
                }
            }
            Integer costItemCount = tocSettlementReceiveBillCostItemDao.countAllByBillId(id);

            for (int i = 0; i < costItemCount; ) {
                int pageSub = (i + pageSize) < costItemCount ? (i + pageSize) : costItemCount;
                List<TocSettlementReceiveBillCostItemDTO> costItemDTOS = tocSettlementReceiveBillCostItemDao.listForExportItem(id, i, pageSize);
                i = pageSub;

                for (TocSettlementReceiveBillCostItemDTO itemDTO : costItemDTOS) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("billCode", tocSettlementReceiveBillDTO.getCode());
                    map.put("externalCode", itemDTO.getExternalCode());
//                    map.put("goodsNo", itemDTO.getGoodsNo());
//                    map.put("goodsName", itemDTO.getGoodsName());
                    map.put("num", itemDTO.getNum());
                    map.put("projectName", itemDTO.getProjectName());
                    map.put("brandName", itemDTO.getParentBrandName());
                    map.put("settlementRate", itemDTO.getSettlementRate());
                    map.put("originalAmount", itemDTO.getOriginalAmount());
                    map.put("rmbAmount", itemDTO.getRmbAmount());
                    map.put("oriCurrency", tocSettlementReceiveBillDTO.getOriCurrency());
                    if (DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1.equals(itemDTO.getBillType())) {
                        if (itemDTO.getOriginalAmount() != null) {
                            map.put("originalAmount", itemDTO.getOriginalAmount().negate());
                        }
                        if (itemDTO.getRmbAmount() != null) {
                            map.put("rmbAmount", itemDTO.getRmbAmount().negate());
                        }

                    }

                    exportList.add(map);
                }
            }
            return exportList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<String, String> saveInvalidBill(User user ,String id, String invalidRemark) throws Exception {
        Map<String, String> result = new HashMap<>();
        TocSettlementReceiveBillModel billModel = tocSettlementReceiveBillDao.searchById(Long.valueOf(id));
        if (billModel == null) {
            result.put("code", "01");
            result.put("message", "结算单不存在！");
            return result;
        }

        if (!DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_02.equals(billModel.getBillStatus())) {
            result.put("code", "01");
            result.put("message", "此账单账单状态不是待核销");
            return result;
        }

        //更新账单状态为“作废待审”
        TocSettlementReceiveBillModel model = new TocSettlementReceiveBillModel();
        model.setId(billModel.getId());
        model.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_05);
        tocSettlementReceiveBillDao.modify(model);
        TocSettlementReceiveBillAuditItemModel auditItemModel = new TocSettlementReceiveBillAuditItemModel();
        auditItemModel.setBillId(billModel.getId());
        auditItemModel.setSubmitId(user.getId());
        auditItemModel.setSubmitter(user.getName());
        auditItemModel.setSubmitDate(TimeUtils.getNow());
        auditItemModel.setSubmitRemark(invalidRemark);
        auditItemModel.setAuditType(DERP_ORDER.RECEIVEBILLAUDIT_AUDITTYPE_1);
        tocSettlementReceiveBillAuditItemDao.save(auditItemModel);

        /**
         * 发送审核提醒邮件
         */
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(billModel.getBuId());
        emailUserDTO.setBuName(billModel.getBuName());
        emailUserDTO.setMerchantId(billModel.getMerchantId());
        emailUserDTO.setMerchantName(billModel.getMerchantName());
        emailUserDTO.setOrderCode(billModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_16);
        emailUserDTO.setSupplier(billModel.getCustomerName());
        emailUserDTO.setAmount("CNY&nbsp;" + billModel.getReceivableAmount().toPlainString());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setCancelId(user.getId());
        emailUserDTO.setStorePlatformName(DERP.getLabelByKey(DERP.storePlatformList, billModel.getStorePlatformCode()));

        JSONObject emailJson = JSONObject.fromObject(emailUserDTO) ;

        rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                MQErpEnum.SEND_REMINDER_MAIL.getTags());

        result.put("code", "00");
        result.put("message", "保存成功！");
        return result;
    }

    @Override
    public int modify(TocSettlementReceiveBillModel model) throws SQLException {
        return tocSettlementReceiveBillDao.modify(model);
    }

    @Override
    public Map<String, String> validate(List<Long> ids) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Map<String, String> resultMap = new HashMap<>();
        Long customerId = null;
        String currency = null;
        String billMonth = null;
        for (int i = 0; i < ids.size(); i++) {
            TocSettlementReceiveBillModel billModel = tocSettlementReceiveBillDao.searchById(ids.get(i));
            if (!billModel.getInvoiceStatus().equals(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00)) {
                resultMap.put("code", "01");
                resultMap.put("msg", "开票的toc结算账单开票状态只能为“待开票”");
                return resultMap;
            }
            if (!(billModel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_02)
                    || billModel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_03)
                    || billModel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_04))) {
                resultMap.put("code", "01");
                resultMap.put("msg", "开票的toc结算账单状态只能为“待核销”、“部分核销”、“已核销”！");
                return resultMap;
            }
            if (i == 0) {
                customerId = billModel.getCustomerId();
            }
            if (!customerId.equals(billModel.getCustomerId())) {
                resultMap.put("code", "01");
                resultMap.put("msg", "仅能对同客户的toc结算账单进行同时合并开票");
                return resultMap;
            }

            if (i == 0) {
                currency = billModel.getSettlementCurrency();
            }
            if (!currency.equals(billModel.getSettlementCurrency())) {
                resultMap.put("code", "01");
                resultMap.put("msg", "仅能对相同结算币种的toc结算账单进行同时合并开票");
                return resultMap;
            }

            if (i == 0) {
                billMonth = sdf.format(billModel.getSettlementDate());
            }
            if (!billMonth.equals(sdf.format(billModel.getSettlementDate()))) {
                resultMap.put("code", "01");
                resultMap.put("msg", "仅能对相同账单日期的toc结算账单进行同时合并开票");
                return resultMap;
            }
        }

        resultMap.put("code", "00");
        resultMap.put("msg", "校验通过");
        return resultMap;
    }

    @Override
    public Map<String, Object> getInvoiceInfo(String ids) throws Exception {
        List<Long> idList = StrUtils.parseIds(ids);
        BigDecimal totalNum = new BigDecimal("0");
        BigDecimal totalAmount = new BigDecimal("0");

        List<TocSettlementReceiveBillModel> billModels = tocSettlementReceiveBillDao.listByIds(idList);
        //公司信息
        Map<String, Object> param = new HashMap<>();
        param.put("merchantId", billModels.get(0).getMerchantId());
        MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(param);

        //账单汇总
        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();

        List<Map<String, Object>> itemStatistics = tocSettlementReceiveBillItemDao.statisticsByBillIds(idList);
        if (itemStatistics != null) {
            for (Map<String, Object> itemMap : itemStatistics) {
                if (itemMap != null) {
                    BigDecimal itemNum = itemMap.get("num") == null ? new BigDecimal("0") : (BigDecimal) itemMap.get("num");
                    BigDecimal itemAmount = itemMap.get("rmbAmount") == null ? new BigDecimal("0") : (BigDecimal) itemMap.get("rmbAmount");
                    totalNum = totalNum.add(itemNum);
                    totalAmount = totalAmount.add(itemAmount);
                }
            }

        }

        List<Map<String, Object>> costItemStatistics = tocSettlementReceiveBillCostItemDao.statisticsByBillIds(idList);
        if (costItemStatistics != null) {
            for (Map<String, Object> itemMap : costItemStatistics) {
                if (itemMap != null) {
                    BigDecimal costItemNum = itemMap.get("num") == null ? new BigDecimal("0") : (BigDecimal) itemMap.get("num");
                    BigDecimal costItemAmount = itemMap.get("rmbAmount") == null ? new BigDecimal("0") : (BigDecimal) itemMap.get("rmbAmount");

                    totalNum = totalNum.add(costItemNum);
                    totalAmount = totalAmount.add(costItemAmount);
                }

            }

        }
        item.put("goodsName", "商品一批");
        item.put("totalNum", totalNum);
        item.put("totalAmount", totalAmount);
        items.add(item);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("items", items);
        resultMap.put("merchantInfo", merchantInfo);
        resultMap.put("totalAllAmount", totalAmount);
        resultMap.put("currency", billModels.get(0).getSettlementCurrency());
        resultMap.put("billDate", TimeUtils.formatMonth(billModels.get(0).getSettlementDate()));

        return resultMap;
    }

    @Override
    public void modifyInvoiceContract(TocReceiveBillInvoiceForm model, User user) throws Exception {
        synchronized (this) {
            ByteArrayOutputStream os = null;
            JSONObject dataJson = new JSONObject();
            JSONArray goodList = new JSONArray();
            try {
                //表头
                Map<String, Object> dataMap = new HashMap();
                dataMap.put("codes", model.getCodes());
                dataMap.put("ids", model.getIds());
                dataMap.put("tempId", model.getFileTempId());
                dataMap.put("fileTempCode", model.getFileTempCode());
                dataMap.put("currency", model.getCurrency());
                dataMap.put("customerId", model.getCustomerId());
                dataMap.put("customerName", model.getCustomerName());
                dataMap.put("billMonth", model.getBillMonth());
                dataMap.put("totalAllAmount", model.getTotalAllAmount());
                dataMap.put("merchantName", model.getMerchantName());
                dataMap.put("depositBank", model.getDepositBank());
                dataMap.put("bankAccount", model.getBankAccount());
                dataMap.put("merchantInvoiceName", model.getMerchantInvoiceName());
                dataMap.put("merchantEnglishName", model.getMerchantEnglishName());
                dataMap.put("swiftCode", model.getSwiftCode());
                dataJson = JSONObject.fromObject(dataMap);

                //商品
//                for (TocReceiveBillGoodsForm good : model.getGoodsList()) {
//                    JSONObject goods = new JSONObject();
//                    goods.put("goodsName", toNullStrWithReplace(good.getGoodsName()));
//                    goods.put("billDate", toNullStrWithReplace(good.getBillDate()));
//                    goods.put("totalAmount", toNullStrWithReplace(good.getTotalAmount()));
//                    goods.put("totalNum", toNullStrWithReplace(good.getTotalNum()));
//                    goodList.add(goods);
//                }

                FileTempModel fileTempModel = fileTempDao.searchById(model.getFileTempId());

                if (fileTempModel == null) {
                    throw new RuntimeException("发票模板不存在！");
                }

                if (StringUtils.isBlank(fileTempModel.getToUrl())) {
                    throw new RuntimeException("发票模板地址不能为空！");
                }

                //生成发票编码
                Calendar calendar = Calendar.getInstance();
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                int month = calendar.get(Calendar.MONTH) + 1;
                String monthStr = month > 9 ? String.valueOf(month) : "0" + month;
                String invoiceNoPrefix = null;
                if (DERP_SYS.MERCHANT_TOPIDEAL_CODE_BAOXIN.equals(user.getTopidealCode())) { //宝信
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_HNFH + year.substring(2);
                } else if (DERP_SYS.MERCHANT_TOPIDEAL_CODE_JIANTAI.equals(user.getTopidealCode())) { //健太
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_QTOP + year.substring(2);
                } else if (DERP_SYS.MERCHANT_TOPIDEAL_CODE_ZHUOYE.equals(user.getTopidealCode())) { //卓烨
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_TWKL + year.substring(2);
                } else if (DERP_SYS.MERCHANT_TOPIDEAL_CODE_YUANSUNTAI.equals(user.getTopidealCode())) { //元森泰
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_YSTA + year.substring(2);
                } else if (DERP_SYS.MERCHANT_TOPIDEAL_CODE_GUANGWANG.equals(user.getTopidealCode())) { //广旺
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_ABHG + year.substring(2);
                } else if (DERP_SYS.MERCHANT_TOPIDEAL_CODE_RUNBAI.equals(user.getTopidealCode())) { //润佰
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_RYBZ + year.substring(2);
                } else if (DERP_SYS.MERCHANT_TOPIDEAL_CODE_WANDAI.equals(user.getTopidealCode())) { //万代
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_WAMD + year.substring(2);
                } else if (DERP_SYS.MERCHANT_TOPIDEAL_CODE_XUANSHENG.equals(user.getTopidealCode())) { //轩盛有限公司
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_HIGH + year.substring(2);
                }
                Long invoiceValue = receiveInvoicenoDao.getMaxValue(invoiceNoPrefix);
                if (invoiceValue == null) {
                    invoiceValue = 1L;
                } else {
                    invoiceValue++;
                }

                String invoiceValueStr = String.format("%04d", invoiceValue);
                String invoiceNo = invoiceNoPrefix + monthStr + invoiceValueStr;
                //保存发票号当前值
                ReceiveInvoicenoModel receiveInvoicenoModel = new ReceiveInvoicenoModel();
                receiveInvoicenoModel.setInvoiceNoPrefix(invoiceNoPrefix);
                receiveInvoicenoModel.setValue(invoiceValue);
                receiveInvoicenoDao.save(receiveInvoicenoModel);

                dataJson.put("invoiceNo", invoiceNo);
                dataJson.put("itemList", model.getGoodsList());
//                mongoJSON.put("dataJson", dataJson.toString());

                model.setInvoiceNo(invoiceNo);

                //toc结算账单
                List<Long> idList = StrUtils.parseIds(model.getIds());
                List<TocSettlementReceiveBillModel> billModels = tocSettlementReceiveBillDao.listByIds(idList);
                List<String> codeList = new ArrayList<>();
                Long customerId = null;
                String customerName = null;
                for (TocSettlementReceiveBillModel billModel : billModels) {
                    codeList.add(billModel.getCode());
                    customerId = billModel.getCustomerId();
                    customerName = billModel.getCustomerName();
                }

                //2.生成pdf并保存
                String templatePath = "classpath:/customsTemplate/" + fileTempModel.getToUrl() +"_INVOICE.xlsx";
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource resource = resolver.getResource(templatePath);
                InputStream is = resource.getInputStream();
                Context context = new Context();
                context.putVar("dto", dataJson);

                JxlsHelper jxlsHelper = JxlsHelper.getInstance();
                PoiTransformer transformer  = (PoiTransformer) jxlsHelper.createTransformer(is, null);
                transformer.setFullFormulaRecalculationOnOpening(true);//打开文件时将重新计算所有公式
                //添加自定义功能
                JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
                Map<String, Object> funcs = new HashMap<String, Object>();
                funcs.put("timeUtils", new TimeUtils());
                JexlBuilder jb = new JexlBuilder();
                jb.namespaces(funcs);
                JexlEngine je = jb.create();
                evaluator.setJexlEngine(je);

                AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
                areaBuilder.setTransformer(transformer);
                List<Area> xlsAreaList = areaBuilder.build();
                jxlsHelper.setHideTemplateSheet(true);
                for (Area xlsArea : xlsAreaList) {
                    xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
                }
                Workbook wb = transformer.getWorkbook();

                //设置导出的页面的大小
                RectangleReadOnly pageSize = (RectangleReadOnly) PageSize.A4;
                os = new ByteArrayOutputStream();

                //生成发票文件
                Excel2PdfUtils.Excel2Pdf(wb, os, true, pageSize);
                AttachmentInfoMongo invoiceAttach = saveAttachmentFile(os, invoiceNo, user);

                //保存应收发票
                ReceiveBillInvoiceModel receiveBillInvoiceModel = new ReceiveBillInvoiceModel();
                receiveBillInvoiceModel.setMerchantId(user.getMerchantId());
                receiveBillInvoiceModel.setMerchantName(user.getMerchantName());
                receiveBillInvoiceModel.setCreaterId(user.getId());
                receiveBillInvoiceModel.setCreater(user.getName());
                receiveBillInvoiceModel.setCurrency(model.getCurrency());
                receiveBillInvoiceModel.setInvoiceNo(invoiceNo);
                receiveBillInvoiceModel.setCustomerId(customerId);
                receiveBillInvoiceModel.setCustomerName(customerName);
                receiveBillInvoiceModel.setInvoicePath(invoiceAttach.getAttachmentUrl());
                receiveBillInvoiceModel.setInvoiceAmount(model.getTotalAllAmount());
                receiveBillInvoiceModel.setInvoiceDate(TimeUtils.getNow());
                receiveBillInvoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_01);
                receiveBillInvoiceModel.setInvoiceType(DERP_ORDER.RECEIVEBILLINVOICE_INVOICETYPE_1);
                receiveBillInvoiceModel.setInvoiceRelCodes(StringUtils.join(codeList.toArray(), ","));
                receiveBillInvoiceModel.setInvoiceRelIds(model.getIds());

                Long id = receiveBillInvoiceDao.save(receiveBillInvoiceModel);
                for (String code : codeList) {
                    TocSettlementReceiveBillModel receiveBillModel = new TocSettlementReceiveBillModel();
                    receiveBillModel.setCode(code);
                    TocSettlementReceiveBillModel receiveModel = tocSettlementReceiveBillDao.searchByModel(receiveBillModel);
                    receiveModel.setInvoiceId(id);
                    receiveModel.setInvoiceNo(invoiceNo);
                    receiveModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_01);
                    tocSettlementReceiveBillDao.modify(receiveModel);
                }

                //应收单确认开票和平台结算单确认开票 即触发邮件提醒；
                net.sf.json.JSONArray attArray = new net.sf.json.JSONArray();
                for (String code : codeList) {
                    Map<String, Object> attQueryMap = new HashMap<String, Object>();
                    attQueryMap.put("relationCode", code);
                    attQueryMap.put("creatorName", "账单同步");
                    List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);
                    for (AttachmentInfoMongo att : attList) {
                        if (!DERP.DEL_CODE_006.equals(att.getStatus())) {
                            Map<String, Object> tempMap = new HashMap<String, Object>();
                            tempMap.put("attachmentName", att.getAttachmentName());
                            String attachmentUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                                    + att.getAttachmentName() + "&url=" + URLEncoder.encode(att.getAttachmentUrl());
                            tempMap.put("attachmentUrl", attachmentUrl);
                            JSONObject attJson = new JSONObject();
                            attJson.putAll(tempMap);
                            attArray.add(attJson);
                        }
                    }
                }

                TocSettlementReceiveBillModel receiveModel = new TocSettlementReceiveBillModel();
                receiveModel.setCode(codeList.get(0));
                TocSettlementReceiveBillModel receiveBill = tocSettlementReceiveBillDao.searchByModel(receiveModel);

                ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

                emailUserDTO.setBuId(receiveBill.getBuId());
                emailUserDTO.setBuName(receiveBill.getBuName());
                emailUserDTO.setMerchantId(user.getMerchantId());
                emailUserDTO.setMerchantName(user.getMerchantName());
                emailUserDTO.setOrderCode(invoiceNo);
                emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9);
                emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                        DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9));
                emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_5);
                emailUserDTO.setSupplier(customerName);
                emailUserDTO.setAmount(receiveBill.getSettlementCurrency() + "&nbsp;" + model.getTotalAllAmount());
                emailUserDTO.setDrawerId(user.getId());
                emailUserDTO.setUserName(user.getName());
                emailUserDTO.setAttArray(attArray);
                emailUserDTO.setStorePlatformName(DERP.getLabelByKey(DERP.storePlatformList, receiveBill.getStorePlatformCode()));

                JSONObject emailJson = JSONObject.fromObject(emailUserDTO) ;

                rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            } finally {
                os.close();
            }
        }
    }

    @Override
    public List<TocSettlementReceiveBillVerifyItemModel> listVerifyByBillId(Long billId) throws SQLException {
        TocSettlementReceiveBillVerifyItemModel itemModel = new TocSettlementReceiveBillVerifyItemModel();
        itemModel.setBillId(billId);
        return tocSettlementReceiveBillVerifyItemDao.list(itemModel);
    }

    @Override
    public Map<String, String> saveVerifyItem(TocSettlementReceiveBillVerifyItemDTO dto, User user) throws Exception {
        Map<String, String> resMap = new HashedMap();
        String receiveDate = dto.getReceiveDateStr();
        String today = TimeUtils.formatDay(new Date());
        if (today.compareTo(receiveDate) < 0) {
            resMap.put("code", "01");
            resMap.put("message", "核销日期不得晚于当前日期");
            return resMap;
        }

        //校验本次回款操作输入的“核销月份”是否大于应收关帐月份，不允许输入应收已关账的月份；
        TocSettlementReceiveBillModel receiveBillModel = tocSettlementReceiveBillDao.searchById(dto.getBillId());
//        ReceiveCloseAccountsModel closeAccountsModel = new ReceiveCloseAccountsModel();
//        closeAccountsModel.setMerchantId(receiveBillModel.getMerchantId());
//        closeAccountsModel.setBuId(receiveBillModel.getBuId());
//        closeAccountsModel.setStatus(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030);
//        closeAccountsModel.setMonth(dto.getVerifyMonth());
//        closeAccountsModel = receiveCloseAccountsDao.searchByModel(closeAccountsModel);

        Map<String, Object> param = new HashMap<>();
        param.put("merchantId", receiveBillModel.getMerchantId());
        param.put("buId", receiveBillModel.getBuId());
        param.put("status", DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030);
        ReceiveCloseAccountsDTO latestExcludeIdByMap = receiveCloseAccountsDao.getLatestExcludeIdByMap(param);

        if(latestExcludeIdByMap != null && latestExcludeIdByMap.getMonth().compareTo(dto.getVerifyMonth()) >=0 ) {
            resMap.put("code", "01");
            resMap.put("message", "不允许输入应收已关账的月份");
            return resMap;
        }

        //核销金额是否大于应收金额
        BigDecimal receivablePrice = tocSettlementReceiveBillItemDao.getTotalReceivePrice(dto.getBillId());
        BigDecimal costFree = tocSettlementReceiveBillCostItemDao.getTotalReceivePrice(dto.getBillId());
        BigDecimal verifyPrice = tocSettlementReceiveBillVerifyItemDao.getAllPriceByBillIds(Arrays.asList(dto.getBillId()));
        receivablePrice = receivablePrice == null ? new BigDecimal("0") : receivablePrice;
        costFree = costFree == null ? new BigDecimal("0") : costFree;
        verifyPrice = verifyPrice == null ? new BigDecimal("0") : verifyPrice;
        BigDecimal totalPrice = receivablePrice.add(costFree);
        BigDecimal tempPrice = verifyPrice.add(new BigDecimal(dto.getPrice().toString()));
        if (totalPrice.abs().compareTo(tempPrice.abs()) == -1) {
            resMap.put("code", "01");
            resMap.put("message", "核销金额不得大于应收金额");
            return resMap;
        }

        Timestamp verifyDate = TimeUtils.getNow();
        TocSettlementReceiveBillVerifyItemModel verifyItemModel = new TocSettlementReceiveBillVerifyItemModel();
        verifyItemModel.setBillId(dto.getBillId());
        verifyItemModel.setReceiceNo(dto.getReceiceNo());
        verifyItemModel.setReceiveDate(TimeUtils.parseDay(receiveDate));
        verifyItemModel.setVerifier(user.getName());
        verifyItemModel.setVerifyId(user.getId());
        verifyItemModel.setVerifyDate(verifyDate);
        verifyItemModel.setPrice(dto.getPrice());
        verifyItemModel.setVerifyMonth(dto.getVerifyMonth());
        tocSettlementReceiveBillVerifyItemDao.save(verifyItemModel);

        TocSettlementReceiveBillModel tocSettlementReceiveBillModel = new TocSettlementReceiveBillModel();
        tocSettlementReceiveBillModel.setId(dto.getBillId());
        if ((receivablePrice.add(costFree)).compareTo(verifyPrice.add(dto.getPrice())) == 0) {
            tocSettlementReceiveBillModel.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_04);
        } else {
            tocSettlementReceiveBillModel.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_03);
        }

        tocSettlementReceiveBillDao.modify(tocSettlementReceiveBillModel);

        receiveBillModel = tocSettlementReceiveBillDao.searchById(dto.getBillId());
        Map<String, Object> attQueryMap = new HashMap<String, Object>();
        attQueryMap.put("relationCode", receiveBillModel.getCode()) ;
        List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);

        net.sf.json.JSONArray attArray = new net.sf.json.JSONArray() ;

        for (AttachmentInfoMongo att : attList) {

            Map<String, Object> tempMap = new HashMap<String, Object>() ;
            tempMap.put("attachmentName", att.getAttachmentName()) ;
            String attachmentUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                    + att.getAttachmentName() + "&url=" + URLEncoder.encode(att.getAttachmentUrl());
            tempMap.put("attachmentUrl", attachmentUrl) ;

            JSONObject attJson = new JSONObject() ;
            attJson.putAll(tempMap);

            attArray.add(attJson) ;
        }

        // 发送MQ记录核销跟踪
        param.clear();
        param.put("operateType", DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITTYPE_0);
        param.put("receiveCodes", receiveBillModel.getCode());
        param.put("orderType", DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_1);
        JSONObject verifyJson = JSONObject.fromObject(param) ;
        rocketMQProducer.send(verifyJson.toString(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTopic(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTags());

        BigDecimal unVeriPrice = receivablePrice.add(costFree).subtract(verifyPrice).subtract(dto.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(receiveBillModel.getBuId());
        emailUserDTO.setBuName(receiveBillModel.getBuName());
        emailUserDTO.setMerchantId(receiveBillModel.getMerchantId());
        emailUserDTO.setMerchantName(receiveBillModel.getMerchantName());
        emailUserDTO.setOrderCode(receiveBillModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_4);
        emailUserDTO.setSupplier(receiveBillModel.getCustomerName());
        emailUserDTO.setAmount("CNY&nbsp;" + receivablePrice.add(costFree).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        emailUserDTO.setVeriAmount("CNY&nbsp;" + dto.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        emailUserDTO.setUnVeriAmount("CNY&nbsp;" + unVeriPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        emailUserDTO.setVerificationId(user.getId());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setAttArray(attArray);
        emailUserDTO.setStorePlatformName(DERP.getLabelByKey(DERP.storePlatformList, receiveBillModel.getStorePlatformCode()));

        JSONObject emailJson = JSONObject.fromObject(emailUserDTO);

        rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                MQErpEnum.SEND_REMINDER_MAIL.getTags());



        resMap.put("code", "00");
        resMap.put("message", "保存成功");
        return resMap;
    }

    @Override
    public BigDecimal statisticsRmbAmount(Long id) throws Exception {

        BigDecimal receivablePrice = tocSettlementReceiveBillItemDao.getTotalReceivePrice(id);
        BigDecimal costFree = tocSettlementReceiveBillCostItemDao.getTotalReceivePrice(id);

        receivablePrice = receivablePrice == null ? new BigDecimal("0") : receivablePrice;
        costFree = costFree == null ? new BigDecimal("0") : costFree;
        BigDecimal total = new BigDecimal("0");
        total = total.add(receivablePrice);
        total = total.add(costFree);
        return total;
    }

    @Override
    public List<Map<String, Object>> listForSummaryExport(TocSettlementReceiveBillDTO dto, User user) throws Exception {

        List<Map<String, Object>> exportMapList = new ArrayList<>();

        List<TocSettlementReceiveBillDTO> billDTOs = tocSettlementReceiveBillDao.listForExport(dto);

        List<Long> billIds = new ArrayList<>();
        Map<Long, String> billCodeMap = new HashMap<>();
        Map<Long, String> currencyMap = new HashMap<>();
        Map<Long, String> externalCodeMap = new HashMap<>();
        for (TocSettlementReceiveBillDTO billDTO : billDTOs) {
            billIds.add(billDTO.getId());
            billCodeMap.put(billDTO.getId(), billDTO.getCode());
            currencyMap.put(billDTO.getId(), billDTO.getOriCurrency());
            externalCodeMap.put(billDTO.getId(), billDTO.getExternalCode());
        }

        //查询应收明细
        List<TocSettlementReceiveBillItemModel> receiveBillItemModels = tocSettlementReceiveBillItemDao.listByPlatformProject(billIds);
        //查询费用明细
        List<TocSettlementReceiveBillCostItemModel> receiveBillCostItemModels = tocSettlementReceiveBillCostItemDao.listByPlatformProject(billIds);

        for (TocSettlementReceiveBillItemModel itemModel : receiveBillItemModels) {
            Map<String, Object> exportMap = new HashMap<>();
            exportMap.put("billCode", billCodeMap.get(itemModel.getBillId()));
            exportMap.put("externalCode", externalCodeMap.get(itemModel.getBillId()));
            exportMap.put("oriCurrency", currencyMap.get(itemModel.getBillId()));

//            PlatformSettlementConfigModel platformSettlementConfigModel = null;
//            if(itemModel.getPlatformProjectId() != null) {
//                platformSettlementConfigModel = platformSettlementConfigDao.searchById(itemModel.getPlatformProjectId());
//            }

            Map<String, Object> brandParams = new HashMap<>();
            brandParams.put("brandSuperiorId", itemModel.getParentBrandId());
            BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(brandParams);

//            if (platformSettlementConfigModel != null) {
//                exportMap.put("projectName", toNullStrWithReplace(platformSettlementConfigModel.getSettlementConfigName()));
//                exportMap.put("PlatformSettlementName", toNullStrWithReplace(platformSettlementConfigModel.getName()));
//            }
            exportMap.put("projectName", toNullStrWithReplace(itemModel.getProjectName()));
            exportMap.put("PlatformSettlementName", toNullStrWithReplace(itemModel.getPlatformProjectName()));
            exportMap.put("parentBrandName", "");
            if (brandSuperior != null) {
                exportMap.put("parentBrandName", brandSuperior.getName());
            }
            exportMap.put("receivePrice", itemModel.getOriginalAmount()==null?"" : itemModel.getOriginalAmount());
            exportMap.put("receiveRmbPrice", itemModel.getRmbAmount()==null?"" : itemModel.getRmbAmount());
            exportMap.put("num", itemModel.getNum()==null?"" : itemModel.getNum());

            String billType = DERP_ORDER.getLabelByKey(DERP_ORDER.tocReceiveBillCost_billTypeList, itemModel.getBillType());
            exportMap.put("billType", billType);
            exportMapList.add(exportMap);
        }

        for (TocSettlementReceiveBillCostItemModel itemModel : receiveBillCostItemModels) {
            Map<String, Object> exportMap = new HashMap<>();
            exportMap.put("billCode", billCodeMap.get(itemModel.getBillId()));
            exportMap.put("externalCode", externalCodeMap.get(itemModel.getBillId()));
            exportMap.put("oriCurrency", currencyMap.get(itemModel.getBillId()));
//            PlatformSettlementConfigModel platformSettlementConfigModel = platformSettlementConfigDao.searchById(itemModel.getPlatformProjectId());

            Map<String, Object> brandParams = new HashMap<>();
            brandParams.put("brandSuperiorId", itemModel.getParentBrandId());
            BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(brandParams);

//            if (platformSettlementConfigModel != null) {
//                exportMap.put("projectName", toNullStrWithReplace(platformSettlementConfigModel.getSettlementConfigName()));
//                exportMap.put("PlatformSettlementName", toNullStrWithReplace(platformSettlementConfigModel.getName()));
//            }
            exportMap.put("projectName", toNullStrWithReplace(itemModel.getProjectName()));
            exportMap.put("PlatformSettlementName", toNullStrWithReplace(itemModel.getPlatformProjectName()));

            exportMap.put("parentBrandName", "");
            if (brandSuperior != null) {
                exportMap.put("parentBrandName", brandSuperior.getName());
            }
            exportMap.put("receivePrice", itemModel.getOriginalAmount()==null?"" : itemModel.getOriginalAmount());
            exportMap.put("receiveRmbPrice", itemModel.getRmbAmount()==null?"" : itemModel.getRmbAmount());
            exportMap.put("num", itemModel.getNum()==null?"" : itemModel.getNum());
            String billType = DERP_ORDER.getLabelByKey(DERP_ORDER.tocReceiveBillCost_billTypeList, itemModel.getBillType());
            exportMap.put("billType", billType);
            exportMapList.add(exportMap);
        }

        return exportMapList;
    }

    @Override
    public String generateInvoiceHtml(TocReceiveBillInvoiceForm form, User user) throws Exception {
        Map<String, Object> map=new HashMap<>();
        FileTempModel fileTempModel = fileTempDao.searchById(form.getFileTempId());

        if (fileTempModel == null) {
            throw new RuntimeException("发票模板不存在！");
        }

        if (StringUtils.isBlank(fileTempModel.getToUrl())) {
            throw new RuntimeException("发票模板地址不能为空！");
        }

        Map<String, Object> resultMap = this.getInvoiceInfo(form.getIds());

        MerchantInfoMongo merchantInfo = (MerchantInfoMongo) resultMap.get("merchantInfo");
        String currency = (String) resultMap.get("currency");
        String billDate = (String) resultMap.get("billDate");
        BigDecimal totalAllAmount = (BigDecimal) resultMap.get("totalAllAmount");
        List<Map<String, Object>> items = (List<Map<String, Object>>) resultMap.get("items");

        List<InvoiceTemplateItemDTO> itemDTOS = new ArrayList<>();
        items.stream().forEach(eneity -> {
            String goodsName = (String) eneity.get("goodsName");
            BigDecimal totalNum = (BigDecimal) eneity.get("totalNum");
            BigDecimal totalAmount = (BigDecimal) eneity.get("totalAmount");
            InvoiceTemplateItemDTO itemDTO = new InvoiceTemplateItemDTO();
            itemDTO.setTotalNum(totalNum.toString());
            itemDTO.setTotalPrice(totalAmount.toString());
            itemDTO.setGoodsName(goodsName);
            itemDTOS.add(itemDTO);
        });

//        map.put("merchantInfo",merchantInfo);
//        map.put("merchantInvoiceName",merchantInfo.getFullName());
//        map.put("merchantEnglishName",merchantInfo.getEnglishName());
//        map.put("ids", form.getIds());
//        map.put("tempId", form.getTempld());
//        map.put("fileTempCode",fileTempModel.getCode());
//        map.put("currency",currency);
//        map.put("billDate",billDate);
//        map.put("totalAllAmount",totalAmount);
//        map.put("itemLists",items);
//        map.put("totalNum", items == null ? 0 : items.size());

        InvoiceTemplateDTO templateDTO = new InvoiceTemplateDTO();
        templateDTO.setMerchantId(merchantInfo.getMerchantId());
        templateDTO.setMerchantInvoiceName(merchantInfo.getFullName());
        templateDTO.setMerchantEnglishName(merchantInfo.getEnglishName());
        templateDTO.setBillMonth(billDate);
        templateDTO.setBillDate(billDate);
        templateDTO.setCurrency(currency);
        templateDTO.setTotalAllNum((items == null ? 0 : items.size()) + "");
        templateDTO.setTotalAllAmount(totalAllAmount.toString());
        templateDTO.setMerchantName(merchantInfo.getName());
        templateDTO.setDepositBank(merchantInfo.getDepositBank());
        templateDTO.setBankAccount(merchantInfo.getBankAccount());
        templateDTO.setSwiftCode(merchantInfo.getSwiftCode());
        templateDTO.setItemList(itemDTOS);
        templateDTO.setFileTempCode(fileTempModel.getCode());
        templateDTO.setFileTempId(fileTempModel.getId());

        String templatePath = "classpath:/customsTemplate/" + fileTempModel.getToUrl() +".xlsx";
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource=resolver.getResource(templatePath);
        InputStream is = resource.getInputStream();
        Context context = new Context();
        context.putVar("dto", templateDTO);

        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        PoiTransformer transformer  = (PoiTransformer) jxlsHelper.createTransformer(is, null);
        transformer.setFullFormulaRecalculationOnOpening(true);//打开文件时将重新计算所有公式
        //添加自定义功能
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("timeUtils", new TimeUtils());
        JexlBuilder jb = new JexlBuilder();
        jb.namespaces(funcs);
        JexlEngine je = jb.create();
        evaluator.setJexlEngine(je);

        AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
        areaBuilder.setTransformer(transformer);
        List<Area> xlsAreaList = areaBuilder.build();
        jxlsHelper.setHideTemplateSheet(true);
        for (Area xlsArea : xlsAreaList) {
            xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
        }
        Workbook wb = transformer.getWorkbook();

        String htmlExcel="";
        if (wb instanceof XSSFWorkbook) {
            XSSFWorkbook xWb = (XSSFWorkbook) wb;
            htmlExcel = ExcelConvertHTMLUtils.getExcelInfo(xWb,true);
        }else if(wb instanceof HSSFWorkbook){
            HSSFWorkbook hWb = (HSSFWorkbook) wb;
            htmlExcel = ExcelConvertHTMLUtils.getExcelInfo(hWb,true);
        }

        return htmlExcel;
    }

    @Override
    public String createReceiveBillExcel(FileTaskMongo task, String basePath) throws Exception {
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> paramMap = jsonData;
        Integer merchantId = (Integer) paramMap.get("merchantId");
        String billIds = (String) paramMap.get("billIds");
       long time = TimeUtils.getNow().getTime();
        basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + time;

        System.out.println("商家Id=" + merchantId + ",billIds=" + billIds + "，类型：ToC平台结算单---生成excel文件开始----------");

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        //表头数据
        TocSettlementReceiveBillDTO dto = new TocSettlementReceiveBillDTO();
        dto.setIds(billIds);
        List<Map<String,Object>> billExportList = listForExport(dto, null);

        Map<Long, String> billIdAndCodeMap = new HashMap<>();
        Map<Long, String> billIdAndCurrencyMap = new HashMap<>();
        billExportList.forEach(entity -> {
            Long id = (Long) entity.get("id");
            String billCode = (String) entity.get("billCode");
            billIdAndCodeMap.put(id, billCode);
            String oriCurrency = (String) entity.get("oriCurrency");
            billIdAndCurrencyMap.put(id, oriCurrency);
        });

        int pageSize = 5000; //每页5000
        int maxSize = 300000; //每个文件存放最大记录数

        TocSettlementReceiveBillItemDTO itemDTO = new TocSettlementReceiveBillItemDTO();
        TocSettlementReceiveBillCostItemDTO costItemDTO = new TocSettlementReceiveBillCostItemDTO();
        itemDTO.setBillIds(billIds);
        costItemDTO.setBillIds(billIds);
        int exportItemNum = tocSettlementReceiveBillItemDao.countByDTO(itemDTO);

        int mainIndexfile = 0;
        //每30w数据生成一个文件
        for (int i = 0; i < Math.ceil((float) exportItemNum / maxSize); i++) {

            List<Map<String, Object>> exportItemList = new ArrayList<>();

            Integer total = exportItemNum > maxSize * (i + 1) ? maxSize * (i + 1) : exportItemNum;

            //生成应收明细
            for (int j = maxSize * i; j < total; ) {
                int pageSub = (j + pageSize) < total ? (j + pageSize) : total;
                itemDTO.setBegin(j);
                itemDTO.setPageSize(pageSize);
                List<Map<String, Object>> itemList = tocSettlementReceiveBillItemDao.listForExportItemByDTO(itemDTO);

                for (Map<String, Object> item : itemList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("billCode", billIdAndCodeMap.get(item.get("bill_id")));
                    map.put("externalCode", item.get("external_code"));
                    map.put("goodsNo", item.get("goods_no"));
                    map.put("goodsName", item.get("goods_name"));
                    map.put("num", item.get("num"));
                    map.put("projectName", item.get("project_name"));
                    map.put("parentBrandName", item.get("parent_brand_name"));
                    map.put("originalAmount", item.get("original_amount"));
                    map.put("settlementRate", item.get("settlement_rate"));
                    map.put("rmbAmount", item.get("rmb_amount"));
                    map.put("oriCurrency", billIdAndCurrencyMap.get(item.get("bill_id")));
                    String billType = (String) item.get("bill_type");

                    if (DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1.equals(billType)) {
                        if (item.get("original_amount") != null) {
                            BigDecimal originalAmount = (BigDecimal) item.get("original_amount");
                            map.put("originalAmount", originalAmount.negate());
                        }
                        if (item.get("rmb_amount") != null) {
                            BigDecimal rmbAmount = (BigDecimal) item.get("rmb_amount");
                            map.put("rmbAmount", rmbAmount.negate());
                        }
                    }
                    exportItemList.add(map);
                }

                j = pageSub;
            }

            //表头信息
            String mainSheetName = "平台结算单列表";
            String[] mainColumns = {"公司","平台结算单号","外部结算单号","事业部","运营类型","客户名称","平台名称","店铺名称",
                    "应收金额(RMB)","应收金额（原币）","平台结算原币","账单月份","入账月份","结算日期","账单状态","创建人","创建时间","NC单号","NC状态","凭证名称","凭证状态"};
            String[] mainKeys = {"merchantName","billCode","externalCode","buName", "shopTypeName","customerName","storePlatformName","shopName",
                    "receivablePrice","oriReceivableAmount","oriCurrency","month","billInDate","billStatus", "creater","createTime", "ncCode", "ncStatus","voucherName","voucherStatus"};

            String itemSheetName = "应收详情";
            String[] itemColumns = {"平台结算单","外部订单号","商品货号","商品名称","销售数量","结算费项", "母品牌","结算金额（结算原币）","结算汇率","结算金额（RMB）"};
            String[] itemKeys = {"billCode","externalCode","goodsNo","goodsName","num","projectName", "parentBrandName","originalAmount",
                    "settlementRate", "rmbAmount"};

            //生成表格
            ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, billExportList);
            ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, exportItemList);

            List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();
            sheets.add(mainSheet);
            sheets.add(itemSheet);

            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
            //导出文件
            String fileName = task.getRemark() + i + ".xlsx";

            if (isExist) {
                //删除目录下的子文件
                DownloadExcelUtil.deleteFile(basePath);
                isExist = false;
            }

            //创建目录
            new File(basePath).mkdirs();

            FileOutputStream fileOut = new FileOutputStream(basePath + "/" + fileName);
            wb.write(fileOut);
            fileOut.close();

            System.out.println("第" + i + "个文件创建结束");
            mainIndexfile++;
        }

        //费用
        int exportCostItemNum = tocSettlementReceiveBillCostItemDao.countByDTO(itemDTO);

        //每30w数据生成一个文件
        for (int i = 0; i < Math.ceil((float) exportCostItemNum / maxSize); i++) {

            List<Map<String, Object>> exportItemList = new ArrayList<>();

            Integer total = exportCostItemNum > maxSize * (i + 1) ? maxSize * (i + 1) : exportCostItemNum;

            //生成应收明细
            for (int j = maxSize * i; j < total; ) {
                int pageSub = (j + pageSize) < total ? (j + pageSize) : total;
                costItemDTO.setBegin(j);
                costItemDTO.setPageSize(pageSize);
                List<Map<String, Object>> itemList = tocSettlementReceiveBillCostItemDao.listForExportItemByDTO(costItemDTO);

                for (Map<String, Object> item : itemList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("billCode", billIdAndCodeMap.get(item.get("bill_id")));
                    map.put("externalCode", item.get("external_code"));
                    map.put("num", item.get("num"));
                    map.put("projectName", item.get("project_name"));
                    map.put("parentBrandName", item.get("parent_brand_name"));
                    map.put("settlementRate", item.get("settlement_rate"));
                    map.put("originalAmount", item.get("original_amount"));
                    map.put("rmbAmount", item.get("rmb_amount"));
                    map.put("oriCurrency", billIdAndCurrencyMap.get(item.get("bill_id")));
                    if (DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1.equals(item.get("bill_type"))) {
                        if (item.get("original_amount") != null) {
                            map.put("originalAmount", ((BigDecimal)item.get("original_amount")).negate());
                        }
                        if (item.get("rmb_amount") != null) {
                            map.put("rmbAmount", ((BigDecimal)item.get("rmb_amount")).negate());
                        }

                    }
                    exportItemList.add(map);
                }

                j = pageSub;
            }

            String mainSheetName = "平台结算单列表";
            String[] mainColumns = {"公司","平台结算单号","外部结算单号","事业部","运营类型","客户名称","平台名称","店铺名称",
                    "应收金额(RMB)","应收金额（原币）","平台结算原币","账单月份","入账月份","结算日期","账单状态","创建人","创建时间","NC单号","NC状态","凭证名称","凭证状态"};
            String[] mainKeys = {"merchantName","billCode","externalCode","buName", "shopTypeName","customerName","storePlatformName","shopName",
                    "receivablePrice","oriReceivableAmount","oriCurrency","month","billInDate","billStatus", "creater","createTime", "ncCode", "ncStatus","voucherName","voucherStatus"};

            String itemSheetName = "应收详情";
            String[] itemColumns = {"平台结算单","外部订单号","商品货号","商品名称","销售数量","结算费项", "母品牌","结算金额（结算原币）","结算汇率","结算金额（RMB）"};
            String[] itemKeys = {"billCode","externalCode","goodsNo","goodsName","num","projectName", "parentBrandName","originalAmount",
                    "settlementRate", "rmbAmount"};

            //生成表格
            List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();
            if(exportItemNum <= 0) {
                ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, billExportList);
                sheets.add(mainSheet);
            }
            ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, exportItemList);

            sheets.add(itemSheet);

            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
            //导出文件
            String fileName = task.getRemark() + (mainIndexfile + i) + ".xlsx";

            if (isExist && exportItemNum <= 0) {
                //删除目录下的子文件
                DownloadExcelUtil.deleteFile(basePath);
                isExist = false;
            }

            //创建目录
            new File(basePath).mkdirs();

            FileOutputStream fileOut = new FileOutputStream(basePath + "/" + fileName);
            wb.write(fileOut);
            fileOut.close();

            System.out.println("第" + (mainIndexfile + i) + "个文件创建结束");
        }

        return basePath;
    }

    @Override
    public String createReceiveBillSummaryExcel(FileTaskMongo task, String basePath) throws Exception{
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> paramMap = jsonData;
        Integer merchantId = (Integer) paramMap.get("merchantId");
        String billIds = (String) paramMap.get("billIds");
        String month = TimeUtils.formatDay(new Date());

        basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + month;
        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        //表头信息
        String mainSheetName = "平台结算单列表";
        String[] mainColumns = {"公司","平台结算单号", "外部结算单号","事业部","运营类型","客户名称","平台名称","店铺名称",
                "应收金额(RMB)", "应收金额(原币)", "平台结算原币种","账单月份","入账月份","账单状态","创建人","创建时间","NC单号","NC状态","凭证名称","凭证状态"};
        String[] mainKeys = {"merchantName","billCode","externalCode","buName", "shopTypeName","customerName","storePlatformName","shopName",
                "receivablePrice", "oriReceivableAmount", "oriCurrency","month","billInDate","billStatus", "creater","createTime", "ncCode", "ncStatus","voucherName","voucherStatus"};

        //表体信息
        TocSettlementReceiveBillDTO dto = new TocSettlementReceiveBillDTO();
        dto.setMerchantId(merchantId.longValue());
        dto.setIds(billIds);
        List<Map<String,Object>> billExportList = listForExport(dto, null);

        String itemSheetName = "应收详情";
        String[] itemColumns = {"平台结算单","外部结算单号","结算费项", "平台费项名称", "母品牌", "补扣款类型", "结算金额（结算原币）","结算金额（RMB）"};
        String[] itemKeys = {"billCode", "externalCode", "projectName", "PlatformSettlementName", "parentBrandName", "billType", "receivePrice", "receiveRmbPrice"};

        //表体信息
        List<Map<String,Object>> itemList = listForSummaryExport(dto, null);

        //生成表格
        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, billExportList);
        ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, itemList);

        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
        sheets.add(mainSheet) ;
        sheets.add(itemSheet) ;

        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);

        //导出文件
        String fileName = task.getRemark() + ".xlsx";

        if (isExist) {
            //删除目录下的子文件
            DownloadExcelUtil.deleteFile(basePath);
        }

        //创建目录
        new File(basePath).mkdirs();

        FileOutputStream fileOut = new FileOutputStream(basePath + "/" + fileName);
        wb.write(fileOut);
        fileOut.close();

        System.out.println("文件创建结束");
        return basePath;
    }

    @Override
    public void
    modifyJSONContract(Map<String, Object> map, User user) throws Exception {
        synchronized (this) {
            ByteArrayOutputStream bos = null;
            try {
                Set<String> keys = map.keySet();
                JSONObject mongoJSON = new JSONObject() ;
                JSONObject dataJson = new JSONObject() ;
                JSONArray goodList = new JSONArray() ;
                for (String key : keys) {
                    if(key.indexOf(".") > -1) {
                        String[] org_param = key.split("\\.") ;
                        if(org_param[0].indexOf("detail") > -1) {
                            dataJson.put(org_param[1], map.get(key)) ;
                        }else if(org_param[0].indexOf("goods") > -1) {
                            List<String> vals = (ArrayList<String>)map.get(key) ;
                            if(vals.isEmpty()) {
                                continue ;
                            }
                            for (int i = 0 ; i < vals.size(); i ++) {
                                JSONObject goods = null ;
                                if(goodList.isEmpty() || goodList.size() < vals.size()) {
                                    goods = new JSONObject() ;
                                    goods.put(org_param[1], toNullStrWithReplace(vals.get(i))) ;
                                    goodList.add(goods) ;
                                }else {
                                    goods = (JSONObject)goodList.get(i) ;
                                    goods.put(org_param[1], toNullStrWithReplace(vals.get(i))) ;
                                }
                            }
                        }
                    }else {
                        mongoJSON.put(key, toNullStrWithReplace(map.get(key))) ;
                    }
                }

                String ids = (String) dataJson.get("ids");
                String currency = (String) dataJson.get("currency");
                String fileTempCode = (String) dataJson.get("fileTempCode");
                String totalAllAmount = (String) dataJson.get("totalAllAmount");
                double totalAllAmountDouble = new DecimalFormat().parse(totalAllAmount).doubleValue();
                //生成发票编码
                Calendar calendar = Calendar.getInstance();
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                int month = calendar.get(Calendar.MONTH)+1;
                String monthStr = month > 9 ? String.valueOf(month) : "0"+month;
                String invoiceNoPrefix = null;
                if ("0000138".equals(user.getTopidealCode())) { //宝信
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_HNFH+year.substring(2);
                } else if ("1000000204".equals(user.getTopidealCode())) { //健太
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_QTOP+year.substring(2);
                } else if ("0000134".equals(user.getTopidealCode())) { //卓烨
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_TWKL+year.substring(2);
                } else if ("1000004669".equals(user.getTopidealCode())) { //元森泰
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_YSTA+year.substring(2);
                } else if ("1000000645".equals(user.getTopidealCode())) { //广旺
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_ABHG+year.substring(2);
                } else if ("1000000626".equals(user.getTopidealCode())) { //润佰
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_RYBZ+year.substring(2);
                }else if ("1000005377".equals(user.getTopidealCode())) { //万代
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_WAMD+year.substring(2);
                }else if ("1000052958".equals(user.getTopidealCode())) { //轩盛有限公司
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_HIGH+year.substring(2);
                }
                Long invoiceValue = receiveInvoicenoDao.getMaxValue(invoiceNoPrefix);
                if (invoiceValue == null) {
                    invoiceValue = 1L;
                } else {
                    invoiceValue++;
                }
                String invoiceValueStr = String.format("%04d",invoiceValue);
                String invoiceNo = invoiceNoPrefix + monthStr + invoiceValueStr;
                //保存发票号当前值
                ReceiveInvoicenoModel receiveInvoicenoModel = new ReceiveInvoicenoModel();
                receiveInvoicenoModel.setInvoiceNoPrefix(invoiceNoPrefix);
                receiveInvoicenoModel.setValue(invoiceValue);
                receiveInvoicenoDao.save(receiveInvoicenoModel);

                dataJson.put("invoiceNo", invoiceNo) ;
                dataJson.put("goodsList", goodList) ;
                mongoJSON.put("dataJson", dataJson.toString()) ;
                String basePath = ApolloUtilDB.orderBasepath+"/BILL/"+user.getMerchantId();

                //toc结算账单
                List<Long> idList = StrUtils.parseIds(ids);
                List<TocSettlementReceiveBillModel> billModels = tocSettlementReceiveBillDao.listByIds(idList);
                List<String> codeList = new ArrayList<>();
                Long customerId = null;
                String customerName = null;
                for (TocSettlementReceiveBillModel billModel : billModels) {
                    codeList.add(billModel.getCode());
                    customerId = billModel.getCustomerId();
                    customerName = billModel.getCustomerName();
                }

                for (String code : codeList) {
                    mongoJSON.put("code", code);
                    mongoJSON.put("fileTempCode", fileTempCode);
                    Map<String, Object> removeMap = new HashMap<String, Object>();
                    removeMap.put("code", code);
                    fileTempDataMongoDao.remove(removeMap);
                    fileTempDataMongoDao.insertJson(mongoJSON.toString());
                }
                //生成发票文件
                bos = exportTempDateFile(codeList.get(0), fileTempCode);
                AttachmentInfoMongo invoiceAttach = saveAttachmentFile(bos, invoiceNo, user);

                //保存应收发票
                ReceiveBillInvoiceModel receiveBillInvoiceModel = new ReceiveBillInvoiceModel();
                receiveBillInvoiceModel.setMerchantId(user.getMerchantId());
                receiveBillInvoiceModel.setMerchantName(user.getMerchantName());
                receiveBillInvoiceModel.setCreaterId(user.getId());
                receiveBillInvoiceModel.setCreater(user.getName());
                receiveBillInvoiceModel.setCurrency(currency);

                receiveBillInvoiceModel.setInvoiceNo(invoiceNo);
                receiveBillInvoiceModel.setCustomerId(customerId);
                receiveBillInvoiceModel.setCustomerName(customerName);
                receiveBillInvoiceModel.setInvoicePath(invoiceAttach.getAttachmentUrl());
                receiveBillInvoiceModel.setInvoiceAmount(new BigDecimal(String.valueOf(totalAllAmountDouble)));
                receiveBillInvoiceModel.setInvoiceDate(TimeUtils.getNow());
                receiveBillInvoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_01);
                receiveBillInvoiceModel.setInvoiceType(DERP_ORDER.RECEIVEBILLINVOICE_INVOICETYPE_1);
                receiveBillInvoiceModel.setInvoiceRelCodes(StringUtils.join(codeList.toArray(), ","));
                receiveBillInvoiceModel.setInvoiceRelIds(ids);

                Long id = receiveBillInvoiceDao.save(receiveBillInvoiceModel);

                for (TocSettlementReceiveBillModel receiveBillModel : billModels) {
                    receiveBillModel.setInvoiceId(id);
                    receiveBillModel.setInvoiceNo(invoiceNo);
                    receiveBillModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_01);
                    tocSettlementReceiveBillDao.modify(receiveBillModel);
                }

                net.sf.json.JSONArray attArray = new net.sf.json.JSONArray();
                for (String code : codeList) {
                    Map<String, Object> attQueryMap = new HashMap<String, Object>();
                    attQueryMap.put("relationCode", code);
                    attQueryMap.put("creatorName", "账单同步");
                    List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);
                    for (AttachmentInfoMongo att : attList) {
                        if (!DERP.DEL_CODE_006.equals(att.getStatus())) {
                            Map<String, Object> tempMap = new HashMap<String, Object>();
                            tempMap.put("attachmentName", att.getAttachmentName());
                            String attachmentUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                                    + att.getAttachmentName() + "&url=" + URLEncoder.encode(att.getAttachmentUrl());
                            tempMap.put("attachmentUrl", attachmentUrl);
                            JSONObject attJson = new JSONObject();
                            attJson.putAll(tempMap);
                            attArray.add(attJson);
                        }
                    }
                }
                Map<String, Object> invoiceMap = new HashMap<String, Object>();
                invoiceMap.put("attachmentName", invoiceNo + "应收账单发票.pdf");
                String invoiceUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                        + invoiceAttach.getAttachmentName() + "&url=" + URLEncoder.encode(invoiceAttach.getAttachmentUrl());
                invoiceMap.put("attachmentUrl", invoiceUrl);
                JSONObject attJson = new JSONObject();
                attJson.putAll(invoiceMap);
                attArray.add(attJson);

                //封装发送邮件JSON
                ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO();

                emailUserDTO.setBuId(billModels.get(0).getBuId());
                emailUserDTO.setBuName(billModels.get(0).getBuName());
                emailUserDTO.setMerchantId(user.getMerchantId());
                emailUserDTO.setMerchantName(user.getMerchantName());
                emailUserDTO.setOrderCode(invoiceNo);
                emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9);
                emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                        DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9));
                emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_5);
                emailUserDTO.setSupplier(customerName);
                emailUserDTO.setAmount(currency + "&nbsp;" + totalAllAmount);
                emailUserDTO.setDrawerId(user.getId());
                emailUserDTO.setUserName(user.getName());
                emailUserDTO.setAttArray(attArray);
                JSONObject emailJson = JSONObject.fromObject(emailUserDTO);

                rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            } finally {
                bos.close();
            }
        }
    }

    @Override
    public String exportNcBillPDF(List<Long> ids) throws Exception {
        //创建目录
        String basePath = ApolloUtilDB.orderBasepath+"/temp/"+System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        //查询应收明细
        List<TocSettlementReceiveBillItemModel> receiveBillItemModelS = tocSettlementReceiveBillItemDao.listByProject(ids);
        //查询费用明细
        List<TocSettlementReceiveBillCostItemModel> receiveBillCostItemModels = tocSettlementReceiveBillCostItemDao.listByProject(ids);
        Map<Long, List<TocSettlementReceiveBillItemModel>> receiveBillItemModelSMap = new HashMap<>();
        Map<Long, List<TocSettlementReceiveBillCostItemModel>> receiveBillCostItemModelSMap = new HashMap<>();
        FileOutputStream fileOut = null;
        ByteArrayOutputStream bos = null;
        if (receiveBillItemModelS != null) {
            for (TocSettlementReceiveBillItemModel itemModel : receiveBillItemModelS) {
                if (receiveBillItemModelSMap.containsKey(itemModel.getBillId())) {
                    receiveBillItemModelSMap.get(itemModel.getBillId()).add(itemModel);
                } else {
                    List<TocSettlementReceiveBillItemModel> itemModelS = new ArrayList<>();
                    itemModelS.add(itemModel);
                    receiveBillItemModelSMap.put(itemModel.getBillId(), itemModelS);
                }
            }
        }

        if (receiveBillCostItemModels != null) {
            for (TocSettlementReceiveBillCostItemModel itemDTO : receiveBillCostItemModels) {
                if (receiveBillCostItemModelSMap.containsKey(itemDTO.getBillId())) {
                    receiveBillCostItemModelSMap.get(itemDTO.getBillId()).add(itemDTO);
                } else {
                    List<TocSettlementReceiveBillCostItemModel> itemModelS = new ArrayList<>();
                    itemModelS.add(itemDTO);
                    receiveBillCostItemModelSMap.put(itemDTO.getBillId(), itemModelS);
                }
            }
        }

        //遍历应收单生成pdf(一个应收单一个pdf)
        try {
            for (Long id : ids) {
                Map<String, Object> dataMap = new HashMap<>();
                List<Map<String, Object>> itemList = new ArrayList<>();
                TocSettlementReceiveBillModel receiveBillModel = tocSettlementReceiveBillDao.searchById(id);
                List<TocSettlementReceiveBillItemModel> itemModels = receiveBillItemModelSMap.get(id);
                List<TocSettlementReceiveBillCostItemModel> costItemModels = receiveBillCostItemModelSMap.get(id);
                Map<String, Object> merchantParam = new HashMap<>();
                merchantParam.put("merchantId", receiveBillModel.getMerchantId());
                MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantParam);
                Map<String, Object> customerParam = new HashMap<>();
                customerParam.put("customerid", receiveBillModel.getCustomerId());
                CustomerInfoMogo customerInfoMongo = customerInfoMongoDao.findOne(customerParam);

                BigDecimal totalItemPrice = new BigDecimal("0");
                BigDecimal totalItemRmbPrice = new BigDecimal("0");
                Integer totalItemNum = 0;

                BigDecimal totalPrice = new BigDecimal("0");
                BigDecimal totalRmbPrice = new BigDecimal("0");
                Integer totalNum = 0;
                if (itemModels != null) {
                    for (TocSettlementReceiveBillItemModel itemModel : itemModels) {
                        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(itemModel.getProjectId());

                        Map<String, Object> brandParams = new HashMap<>();
                        brandParams.put("brandSuperiorId", itemModel.getParentBrandId());
                        BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(brandParams);

                        Map<String, Object> map = new HashMap<>();
                        if (settlementConfigModel != null) {
                            map.put("projectName", toNullStrWithReplace(settlementConfigModel.getProjectName()));
                        }
                        map.put("parentBrandName", "");
                        if (brandSuperior != null) {
                            map.put("parentBrandName", toNullStrWithReplace(brandSuperior.getName()));
                        }
                        map.put("subjectName", toNullStrWithReplace(settlementConfigModel.getPaymentSubjectName()));
                        map.put("receivePrice", itemModel.getOriginalAmount()==null?"" : itemModel.getOriginalAmount());
                        map.put("receiveRmbPrice", itemModel.getRmbAmount()==null?"" : itemModel.getRmbAmount());
                        map.put("num", itemModel.getNum()==null?"" : itemModel.getNum());
                        itemList.add(map);
                        if (itemModel.getOriginalAmount() != null) {
                            totalItemPrice = totalItemPrice.add(itemModel.getOriginalAmount());
                        }
                        if (itemModel.getRmbAmount() != null) {
                            totalItemRmbPrice = totalItemRmbPrice.add(itemModel.getRmbAmount());
                        }
                        if (itemModel.getNum() != null) {
                            totalItemNum += itemModel.getNum();
                        }
                    }
                }
                if (costItemModels != null) {
                    for (TocSettlementReceiveBillCostItemModel costItemModel : costItemModels) {
                        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(costItemModel.getProjectId());

                        Map<String, Object> brandParams = new HashMap<>();
                        brandParams.put("brandSuperiorId", costItemModel.getParentBrandId());
                        BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(brandParams);

                        Map<String, Object> map = new HashMap<>();
                        if (settlementConfigModel != null) {
                            map.put("projectName", toNullStrWithReplace(settlementConfigModel.getProjectName()));
                        }
                        map.put("parentBrandName", "");
                        if (brandSuperior != null) {
                            map.put("parentBrandName", toNullStrWithReplace(brandSuperior.getName()));
                        }

                        map.put("subjectName", toNullStrWithReplace(settlementConfigModel.getPaymentSubjectName()));
                        map.put("receivePrice", costItemModel.getOriginalAmount()==null?"" : costItemModel.getOriginalAmount());
                        map.put("receiveRmbPrice", costItemModel.getRmbAmount()==null?"" : costItemModel.getRmbAmount());
                        map.put("num", costItemModel.getNum()==null?"" : costItemModel.getNum());
                        itemList.add(map);
                        if (costItemModel.getOriginalAmount() != null) {
                            totalPrice = totalPrice.add(costItemModel.getOriginalAmount());
                        }
                        if (costItemModel.getRmbAmount() != null) {
                            totalRmbPrice = totalRmbPrice.add(costItemModel.getRmbAmount());
                        }
                        if (costItemModel.getNum() != null) {
                            totalNum += costItemModel.getNum();
                        }
                    }
                }
                dataMap.put("goodsList", itemList);
                dataMap.put("merchantName", toNullStr(merchantInfoMongo.getFullName()));
                dataMap.put("billCode", toNullStr(receiveBillModel.getCode()));
                dataMap.put("createDate", receiveBillModel.getCreateDate() != null ? dateFormat.format(receiveBillModel.getCreateDate()) : "");
                dataMap.put("billMonth", receiveBillModel.getSettlementDate() != null ? monthFormat.format(receiveBillModel.getSettlementDate()) : "");
                dataMap.put("customer", toNullStr(customerInfoMongo.getName()));
                dataMap.put("buName", toNullStr(receiveBillModel.getBuName()));
                dataMap.put("billStatus", toNullStr(DERP_ORDER.getLabelByKey(DERP_ORDER.tocReceiveBill_billStatusList, receiveBillModel.getBillStatus())));
                dataMap.put("currency", receiveBillModel.getSettlementCurrency());

                dataMap.put("totalItemNum", totalItemNum);
                dataMap.put("totalItemAmount", totalItemPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                dataMap.put("totalItemRmbAmount", totalItemRmbPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                dataMap.put("totalItemAmountLabel", NumberToCN.number2CNMontrayUnit(totalItemPrice.setScale(2, BigDecimal.ROUND_HALF_UP)));

                dataMap.put("totalCostNum", totalNum);
                dataMap.put("totalCostAmount", totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                dataMap.put("totalCostRmbAmount", totalRmbPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                dataMap.put("totalCostAmountLabel", NumberToCN.number2CNMontrayUnit(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP)));

                totalPrice = totalPrice.add(totalItemPrice);
                totalRmbPrice = totalRmbPrice.add(totalItemRmbPrice);
                totalNum += totalItemNum;

                dataMap.put("totalNum", totalNum);
                dataMap.put("totalAmount", totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                dataMap.put("totalRmbAmount", totalRmbPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                dataMap.put("totalAmountLabel", NumberToCN.number2CNMontrayUnit(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP)));

                FileTempModel tempModel = new FileTempModel() ;
                tempModel.setCode("ToCReceiveInvoice");
                tempModel = fileTempDao.searchByModel(tempModel) ;
                String contentBody = tempModel.getContentBody();

                contentBody = FreemakerUtils.formatReplacementHtml(contentBody) ;
                contentBody = FreemakerUtils.genHtmlStrWithTemplate(dataMap, contentBody) ;
                contentBody = FreemakerUtils.ConvertLineBreaks(contentBody) ;

                bos = PdfUtils.html2Pdf(contentBody, PdfUtils.VERTICAL);

                new File(basePath).mkdirs();
                fileOut = new FileOutputStream(basePath+"/"+ receiveBillModel.getCode() +"应收账单.pdf");
                bos.writeTo(fileOut);
                fileOut.close();
                bos.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            fileOut.close();
            bos.close();
        }
        return basePath;
    }

    private Object toNullStrWithReplace(Object str) {
        if (str instanceof String) {
            String s = (String) str;
            if (StringUtils.isBlank(s)) {
                return "";
            }
            return s.replace("&", "&amp;");
        }
        return str;
    }

    private ByteArrayOutputStream exportTempDateFile(String orderCode, String fileTempCode) throws Exception {

        Map<String, Object> queryMap = new HashMap<String, Object>() ;
        queryMap.put("code", orderCode) ;
        queryMap.put("fileTempCode", fileTempCode) ;
        FileTempDataMongo fileMongo = fileTempDataMongoDao.findOne(queryMap) ;

        String dataJson = fileMongo.getDataJson();
        Map<String, Object> dataMap = com.alibaba.fastjson.JSONObject.parseObject(dataJson, Map.class);

        /*
         * 获取文件模版
         */
        FileTempModel tempModel = new FileTempModel() ;
        tempModel.setCode(fileTempCode);
        tempModel = fileTempDao.searchByModel(tempModel) ;

        String contentBody = tempModel.getContentBody();

        contentBody = FreemakerUtils.formatReplacementHtml(contentBody) ;
        contentBody = FreemakerUtils.genHtmlStrWithTemplate(dataMap, contentBody) ;
        contentBody = FreemakerUtils.ConvertLineBreaks(contentBody) ;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos = PdfUtils.html2Pdf(contentBody, PdfUtils.VERTICAL);

        return bos;
    }

    private String toNullStr(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str;
    }

    private String addWatermark(String invoiceNo, User user, String invoicePath) throws Exception {
        InputStream in = null;
        ByteArrayOutputStream output = null;
        try {
            String ext = "pdf";
            ResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
            Resource resource=resolver.getResource("classpath:/image/abolished.png");
            in = resource.getInputStream();
            output = new ByteArrayOutputStream();
            IOUtils.copyLarge(in, output);

            String tempPath = ApolloUtilDB.orderBasepath+"/temp/"+System.currentTimeMillis();
            Map<String, Object> map = PdfUtils.setWatermark(invoicePath, output.toByteArray(), invoiceNo+"ToC应收账单发票."+ext,tempPath);

            String saveUrl = (String) map.get("saveUrl");
            Integer fileSize = (Integer) map.get("fileSize");

            //逻辑删除之前的发票文件
            Map<String,Object> params = new HashMap<String,Object>() ;
            Map<String,Object> data = new HashMap<String,Object>() ;
            params.put("relationCode", invoiceNo + "-invoice") ;

            data.put("status", DERP_ORDER.ATTACHMENT_STATUS_006) ;
            data.put("modifyDate", new Date()) ;

            attachmentInfoMongoDao.update(params, data);

            //附件信息写入MongoDB
            AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo() ;
            attachmentInfoMongo.setAttachmentName(invoiceNo+"ToC应收账单发票."+ext); 		//附件名
            attachmentInfoMongo.setAttachmentSize(Long.valueOf(fileSize)); 		//附件大小
            attachmentInfoMongo.setAttachmentType(ext);		       	//附件类型
            attachmentInfoMongo.setCreator(user.getId());			//上传者
            attachmentInfoMongo.setCreatorName(user.getName());
            attachmentInfoMongo.setCreateDate(new Date());
            attachmentInfoMongo.setRelationCode(invoiceNo + "-invoice");
            attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
            attachmentInfoMongo.setAttachmentUrl(saveUrl);              //设置Url
            attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
            attachmentInfoMongo.setModule(SourceStatusEnum.YSZD.getKey());

            attachmentInfoMongoDao.insert(attachmentInfoMongo);
            return saveUrl;
        } catch (Exception e) {
            throw new RuntimeException("发票文件增加已作废水印失败");
        } finally {
            in.close();
            output.close();
        }
    }

    /**
     * 保存附件信息
     * @param bos 文件流
     * @param invoiceNo 目标文件名称
     * @param user 用户
     */
    private AttachmentInfoMongo saveAttachmentFile(ByteArrayOutputStream bos , String invoiceNo, User user) throws Exception {
        byte[] fileBytes = bos.toByteArray();
        String ext = "pdf";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileTypeCode", SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
        jsonObject.put("fileName", invoiceNo+"ToC应收账单发票."+ext);
        jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
        jsonObject.put("fileExt",ext);
        jsonObject.put("fileSize",String.valueOf(fileBytes.length));
        String resultJson=SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

        JSONObject jsonObj = JSONObject.fromObject(resultJson);

        if(jsonObj.getInt("code")!= 200){
            throw new RuntimeException("保存发票失败！");
        }
        //返回文件服务器存储URL
        String saveUrl = jsonObj.getString("url") ;
        //附件信息写入MongoDB
        AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo() ;
        attachmentInfoMongo.setAttachmentName(invoiceNo+"ToC应收账单发票."+ext); 		//附件名
        attachmentInfoMongo.setAttachmentSize(Long.valueOf(fileBytes.length)); 		//附件大小
        attachmentInfoMongo.setAttachmentType(ext);		       	//附件类型
        attachmentInfoMongo.setCreator(user.getId());
        attachmentInfoMongo.setCreatorName(user.getName());
        attachmentInfoMongo.setCreateDate(TimeUtils.getNow());
        attachmentInfoMongo.setRelationCode(invoiceNo + "-invoice");              //关联订单编码
        attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
        attachmentInfoMongo.setAttachmentUrl(saveUrl);              //设置Url
        attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
        attachmentInfoMongo.setModule(SourceStatusEnum.YSZD.getKey());
        attachmentInfoMongoDao.insert(attachmentInfoMongo);
        return attachmentInfoMongo;
    }
}
