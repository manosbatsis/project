package com.topideal.order.service.bill.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.topideal.dao.bill.*;
import com.topideal.entity.vo.bill.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.RectangleReadOnly;
import com.topideal.api.nc.NcClientUtils;
import com.topideal.api.nc.nc07.Details;
import com.topideal.api.nc.nc07.ReceiveBillJsonRoot;
import com.topideal.api.nc.nc08.ReceiveHcInvalidRoot;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.NumberToCN;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.FileTempDao;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.dao.sale.SaleOrderDao;
import com.topideal.dao.sale.SaleReturnOrderItemDao;
import com.topideal.entity.dto.bill.AdvanceBillDTO;
import com.topideal.entity.dto.bill.AdvanceBillDataDTO;
import com.topideal.entity.dto.bill.AdvanceBillDatasDTO;
import com.topideal.entity.dto.bill.AdvanceBillItemDTO;
import com.topideal.entity.dto.bill.AdvanceBillOperateItemDTO;
import com.topideal.entity.dto.bill.AdvanceBillVerifyItemDTO;
import com.topideal.entity.dto.bill.InvoiceTemplateItemDetailsYSDTO;
import com.topideal.entity.dto.bill.InvoiceTemplateItemYSDTO;
import com.topideal.entity.dto.bill.InvoiceTemplateYSDTO;
import com.topideal.entity.dto.bill.ReceiveBillToNCDTO;
import com.topideal.entity.dto.bill.ReceiveBillVerifyAdvanceDTO;
import com.topideal.entity.dto.bill.ReceiveBillVerifyItemDTO;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.sale.SaleOrderModel;
import com.topideal.enums.NcAPIEnum;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.AttachmentInfoMongoDao;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.order.service.bill.AdvanceBillService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.ExcelConvertHTMLUtils;
import com.topideal.order.tools.pdf.Excel2PdfUtils;
import com.topideal.order.tools.pdf.FreemakerUtils;
import com.topideal.order.tools.pdf.PdfUtils;
import com.topideal.order.webapi.bill.form.AdvanceAuditForm;
import com.topideal.order.webapi.bill.form.AdvanceBillSaveForm;
import com.topideal.order.webapi.bill.form.InvoiceTemplateYSForm;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultDefaultValueProcessor;

@Service
public class AdvanceBillServiceImpl implements AdvanceBillService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdvanceBillServiceImpl.class);
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;
    @Autowired
    private AdvanceBillDao advanceBillDao;
    @Autowired
    private AdvanceBillItemDao advanceBillItemDao;
    @Autowired
    private AdvanceBillOperateItemDao advanceBillOperateItemDao;
    @Autowired
    private AdvanceBillVerifyItemDao advanceBillVerifyItemDao;
    @Autowired
    private SettlementConfigDao settlementConfigDao;
    @Autowired
    private SaleOrderDao saleOrderDao;
    @Autowired
    private SaleReturnOrderItemDao saleReturnOrderItemDao;
    @Autowired
    private ReceiveBillInvoiceDao receiveBillInvoiceDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao;
    @Autowired
    private ReceivePaymentSubjectDao receivePaymentSubjectDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao;
    @Autowired
    private FileTempDao fileTempDao;
    @Autowired
    private ReceiveInvoicenoDao receiveInvoicenoDao;
    @Autowired
    private ReceiveBillVerifyItemDao receiveBillVerifyItemDao;
    @Autowired
    private ReceiveBillOperateDao receiveBillOperateDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private ReceiveCloseAccountsDao receiveCloseAccountsDao;

    
    @Override
    public void saveContract(User user, InvoiceTemplateYSForm templateForm) throws Exception {

        List<Long> billIds = StrUtils.parseIds(templateForm.getIds());
        List<String> codes = Arrays.asList(templateForm.getCodes());

        List<AdvanceBillDTO> billDTOS = advanceBillDao.listBillByRelIds(billIds);
        for (AdvanceBillDTO dto : billDTOS) {
            if (DERP_ORDER.ADVANCEBILL_INVOICESTATUS_01.equals(dto.getInvoiceStatus()) ||
                    DERP_ORDER.ADVANCEBILL_INVOICESTATUS_03.equals(dto.getInvoiceStatus())) {
                throw new RuntimeException("预收账单：" + dto.getCode() + "已开票，不能重复开票");
            }
        }
        


        FileTempModel fileTempModel = fileTempDao.searchById(templateForm.getFileTempId());

        if (fileTempModel == null) {
            throw new RuntimeException("发票模板不存在！");
        }

        if (StringUtils.isBlank(fileTempModel.getToUrl())) {
            throw new RuntimeException("发票模板地址不能为空！");
        }

        //1.生成发票编码
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthStr = month > 9 ? String.valueOf(month) : "0" + month;
        String invoiceNoPrefix = null;
        if ("0000138".equals(user.getTopidealCode())) { //宝信
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_HNFH + year.substring(2);
        } else if ("1000000204".equals(user.getTopidealCode())) { //健太
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_QTOP + year.substring(2);
        } else if ("0000134".equals(user.getTopidealCode())) { //卓烨
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_TWKL + year.substring(2);
        } else if ("1000004669".equals(user.getTopidealCode())) { //元森泰
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_YSTA + year.substring(2);
        } else if ("1000000645".equals(user.getTopidealCode())) { //广旺
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_ABHG + year.substring(2);
        } else if ("1000000626".equals(user.getTopidealCode())) { //润佰
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_RYBZ + year.substring(2);
        } else if ("1000005377".equals(user.getTopidealCode())) { //万代
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_WAMD + year.substring(2);
        } else if ("1000052958".equals(user.getTopidealCode())) { //轩盛有限公司
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

        templateForm.setInvoiceNo(invoiceNo);

        //2.生成pdf并保存
        String templatePath = "classpath:/customsTemplate/" + fileTempModel.getToUrl() +"_INVOICE.xlsx";
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(templatePath);
        InputStream is = resource.getInputStream();
        Context context = new Context();
        context.putVar("dto", templateForm);

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
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Excel2PdfUtils.Excel2Pdf(wb, os, true, pageSize);

        AttachmentInfoMongo invoiceAttach = saveAttachmentFile(os, invoiceNo, user);

        //3.保存发票信息
        double totalAllAmountDouble = new DecimalFormat().parse(templateForm.getTotalAllAmount()).doubleValue();
        Timestamp invoiceDate = TimeUtils.getNow();
        ReceiveBillInvoiceModel receiveBillInvoiceModel = new ReceiveBillInvoiceModel();
        receiveBillInvoiceModel.setMerchantId(user.getMerchantId());
        receiveBillInvoiceModel.setMerchantName(user.getMerchantName());
        receiveBillInvoiceModel.setCreaterId(user.getId());
        receiveBillInvoiceModel.setCreater(user.getName());

        receiveBillInvoiceModel.setInvoiceNo(invoiceNo);
        receiveBillInvoiceModel.setCustomerId(billDTOS.get(0).getCustomerId());
        receiveBillInvoiceModel.setCustomerName(billDTOS.get(0).getCustomerName());
        receiveBillInvoiceModel.setCurrency(billDTOS.get(0).getCurrency());

        receiveBillInvoiceModel.setInvoicePath(invoiceAttach.getAttachmentUrl());
        receiveBillInvoiceModel.setInvoiceAmount(new BigDecimal(String.valueOf(totalAllAmountDouble)));
        receiveBillInvoiceModel.setInvoiceDate(invoiceDate);
        receiveBillInvoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_01);
        receiveBillInvoiceModel.setInvoiceType(DERP_ORDER.RECEIVEBILLINVOICE_INVOICETYPE_2);
        receiveBillInvoiceModel.setInvoiceRelCodes(templateForm.getCodes());
        receiveBillInvoiceModel.setInvoiceRelIds(templateForm.getIds());
        receiveBillInvoiceModel.setInvoiceRelCodes(templateForm.getCodes());
        receiveBillInvoiceModel.setInvoiceRelIds(templateForm.getIds());
        Long id = receiveBillInvoiceDao.save(receiveBillInvoiceModel);

      //4.修改应预账单状态
        for (AdvanceBillDTO dto : billDTOS) {
        	AdvanceBillModel model = new AdvanceBillModel();
            model.setId(dto.getId());
            model.setInvoiceId(id);
            model.setInvoiceStatus(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_01);
            advanceBillDao.modify(model);

            //操作日志节点
            //添加操作日志
            AdvanceBillOperateItemModel opera=new AdvanceBillOperateItemModel();
            opera.setAdvanceId(model.getId());
            opera.setOperateId(user.getId());
            opera.setOperater(user.getName());
            opera.setOperateDate(TimeUtils.getNow());
            opera.setCreateDate(TimeUtils.getNow());
            opera.setOperateResult(DERP_ORDER.ADVANCEBILL_RESULT_2);
            opera.setOperateType(DERP_ORDER.ADVANCEBILL_TYPE_4);
            advanceBillOperateItemDao.save(opera);
			 
        }
        


        //5.保存发票文件到附件
        net.sf.json.JSONArray attArray = new net.sf.json.JSONArray();
        for (String code : codes) {
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

        //6.发送邮件
        
        Map<String, Object> invoiceMap = new HashMap<String, Object>();
        invoiceMap.put("attachmentName", invoiceNo + "预收账单发票.pdf");
        String invoiceUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                + invoiceAttach.getAttachmentName() + "&url=" + URLEncoder.encode(invoiceAttach.getAttachmentUrl());
        invoiceMap.put("attachmentUrl", invoiceUrl);
        JSONObject attJson = new JSONObject();
        attJson.putAll(invoiceMap);
        attArray.add(attJson);

        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        JsonConfig jsonConfig = new JsonConfig();
        //getReceiveOperators(billDTOS.get(0).getId(), emailUserDTO, jsonConfig);暂时不要

        emailUserDTO.setBuId(billDTOS.get(0).getBuId());
        emailUserDTO.setBuName(billDTOS.get(0).getBuName());
        emailUserDTO.setMerchantId(user.getMerchantId());
        emailUserDTO.setMerchantName(user.getMerchantName());
        emailUserDTO.setOrderCode(invoiceNo);
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_21);
        emailUserDTO.setSupplier(templateForm.getCustomerName());
        emailUserDTO.setCurrency(templateForm.getCurrency());
        emailUserDTO.setAmount(templateForm.getTotalAllAmount());// 页面传来的 预收金额
        emailUserDTO.setDrawerId(user.getId());
        
        // 提交人审核人
        AdvanceBillOperateItemModel opraeteI =new AdvanceBillOperateItemModel();
        opraeteI.setAdvanceId(billDTOS.get(0).getId());        
        List<AdvanceBillOperateItemModel> opraeteIList = advanceBillOperateItemDao.list(opraeteI);
        List<String> submitId=new ArrayList<String>();  
        Long auditorId=null;       
        for (AdvanceBillOperateItemModel operateItemModel : opraeteIList) {      	   
            if (DERP_ORDER.ADVANCEBILL_TYPE_0.equals(operateItemModel.getOperateType())&&operateItemModel.getOperateId()!=null) {
            	submitId.add(operateItemModel.getOperateId().toString());
            }            
            if (DERP_ORDER.ADVANCEBILL_TYPE_1.equals(operateItemModel.getOperateType())&&operateItemModel.getOperateId()!=null) {
            	auditorId=operateItemModel.getOperateId();
            } 
        }
        emailUserDTO.setSubmitId(submitId);//提交人
        emailUserDTO.setAuditorId(auditorId);//审核人
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setAttArray(attArray);

        JSONObject emailJson = JSONObject.fromObject(emailUserDTO, jsonConfig);

        rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                MQErpEnum.SEND_REMINDER_MAIL.getTags());     
        
    }
    
    /**
     * 获取(应收,预收)账单各个节点的操作人
     *
     * @param billId       应收id
     * @param emailUserDTO
     */
    private void getReceiveOperators(Long billId, ReminderEmailUserDTO emailUserDTO, JsonConfig jsonConfig) throws SQLException {
        ReceiveBillOperateModel operateModel = new ReceiveBillOperateModel();
        operateModel.setBillId(billId);
     //   operateModel.settype();
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
    
    /**
     * 保存附件信息
     *
     * @param bos       文件流
     * @param invoiceNo 目标文件名称
     * @param user      用户
     */
    private AttachmentInfoMongo saveAttachmentFile(ByteArrayOutputStream bos, String invoiceNo, User user) throws Exception {
        byte[] fileBytes = bos.toByteArray();
        String ext = "pdf";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileTypeCode", SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
        jsonObject.put("fileName", invoiceNo + "预收账单发票." + ext);
        jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
        jsonObject.put("fileExt", ext);
        jsonObject.put("fileSize", String.valueOf(fileBytes.length));
        String resultJson = SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

        JSONObject jsonObj = JSONObject.fromObject(resultJson);

        if (jsonObj.getInt("code") != 200) {
            throw new RuntimeException("保存发票失败！");
        }
        //返回文件服务器存储URL
        String saveUrl = jsonObj.getString("url");
        //附件信息写入MongoDB
        AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo();
        attachmentInfoMongo.setAttachmentName(invoiceNo + "预收账单发票." + ext);        //附件名
        attachmentInfoMongo.setAttachmentSize(Long.valueOf(fileBytes.length));        //附件大小
        attachmentInfoMongo.setAttachmentType(ext);                //附件类型
        attachmentInfoMongo.setCreator(user.getId());
        attachmentInfoMongo.setCreatorName(user.getName());
        attachmentInfoMongo.setCreateDate(TimeUtils.getNow());
        attachmentInfoMongo.setRelationCode(invoiceNo + "-invoice");              //关联订单编码
        attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
        attachmentInfoMongo.setAttachmentUrl(saveUrl);              //设置Url
        attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
        attachmentInfoMongo.setModule(SourceStatusEnum.YSKD.getKey());// 预付款单
        attachmentInfoMongoDao.insert(attachmentInfoMongo);
        return attachmentInfoMongo;
    }

    
    @Override
    public String generateInvoiceHtml(Long tempId, String ids, String invoiceSource, User user) throws Exception {

        FileTempModel fileTempModel = fileTempDao.searchById(tempId);

        if (fileTempModel == null) {
            throw new RuntimeException("发票模板不存在！");
        }

        if (StringUtils.isBlank(fileTempModel.getToUrl())) {
            throw new RuntimeException("发票模板地址不能为空！");
        }

        List<Long> billIds = StrUtils.parseIds(ids);

        List<AdvanceBillDTO> billDTOS = advanceBillDao.listBillByRelIds(billIds);

        String codes = billDTOS.stream().map(AdvanceBillDTO::getCode).collect(Collectors.joining());
        String poNos = billDTOS.stream().map(AdvanceBillDTO::getPoNo).collect(Collectors.joining());
        //String relCodes = billDTOS.stream().map(AdvanceBillDTO::getRelCode).collect(Collectors.joining());
        // 查询客户
        Map<String, Object> params = new HashMap<>();
        params.put("customerid", billDTOS.get(0).getCustomerId());
        params.put("cusType", "1");
        CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(params);

        Map<String, Object> param = new HashMap<>();
        param.put("merchantId", user.getMerchantId());
        MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(param);

        InvoiceTemplateYSDTO templateDTO = new InvoiceTemplateYSDTO();
        templateDTO.setFileTempId(tempId);
        templateDTO.setFileTempCode(fileTempModel.getCode());
        templateDTO.setIds(ids);
        templateDTO.setCurrency(billDTOS.get(0).getCurrency());
        templateDTO.setPoNos(poNos);
        templateDTO.setCodes(codes);
       // templateDTO.setRelCodes(relCodes);
        templateDTO.setMerchantId(merchantInfo.getMerchantId());
        templateDTO.setMerchantEnglishName(merchantInfo.getEnglishName());
        templateDTO.setMerchantInvoiceName(merchantInfo.getFullName());
        templateDTO.setEnglishRegisteredAddress(merchantInfo.getEnglishRegisteredAddress());
        templateDTO.setBankAccount(merchantInfo.getBankAccount());
        templateDTO.setSwiftCode(merchantInfo.getSwiftCode());
        templateDTO.setBankAddress(merchantInfo.getBankAddress());
        templateDTO.setBeneficiaryName(merchantInfo.getBeneficiaryName());
        templateDTO.setDepositBank(merchantInfo.getDepositBank());
        templateDTO.setCompanyAddr(customerInfo.getCompanyAddr());
        templateDTO.setCustomerId(customerInfo.getCustomerid());
        templateDTO.setCustomerEnName(customerInfo.getEnName());
        templateDTO.setCustomerName(customerInfo.getName());
        templateDTO.setCusBankAccount(customerInfo.getBankAccount());
        templateDTO.setCusDepositBank(customerInfo.getDepositBank());
        templateDTO.setTaxNo(customerInfo.getTaxNo());
        templateDTO.setEnBusinessAddress(customerInfo.getEnBusinessAddress());
     //  templateDTO.setInvoiceStatus(invoiceSource);

		/*
		 * ReceiveBillOperateModel operateModel =
		 * receiveBillOperateDao.getLatestOperate(DERP_ORDER.
		 * RECEIVEBILLOPERATE_OPERATENODE_1, billIds); if (operateModel != null) {
		 * templateDTO.setInvoiceDate(TimeUtils.format(operateModel.getOperateDate(),
		 * "yyyy/MM/dd")); }
		 */

        if (fileTempModel.getToUrl().contains("YS-BAOJIANPIN")) {//保健品预收通用摸版
        	listBAOJIANPINtemInfos(billIds, templateDTO);
        }

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
    
    /**
     * 保健品板取数逻辑
     * @param ids 应收id集合
     * @param templateDTO
     */
    public void listBAOJIANPINtemInfos(List<Long> ids, InvoiceTemplateYSDTO templateDTO) throws Exception {

    	// 根据预收单id查询销售订单明细
    	List<Map<String, Object>> saleItemList=saleOrderDao.getSaleOrderByAdvanceId(ids);
    	List<InvoiceTemplateItemDetailsYSDTO> itemDetailsList = new LinkedList<>();// 存储发票明细   	
    	Map<String, Map<String, Object>>codeListMap=new HashMap<>();// 用来合计总表数量和金额和单位
    	BigDecimal detailsTotalAmount=new BigDecimal("0");// 明细汇总金额  	
    	int detailsTotalNum=0;// 明细汇总数据
    	for (Map<String, Object> map : saleItemList) {
    		String code = (String) map.get("code");// 销售订单号
    		String poNo = (String) map.get("po_no");// po号
            String tallyingUnit = (String) map.get("tallying_unit");//单位
            String goodsNo = (String) map.get("goods_no");//货号
            String barcode = (String) map.get("barcode");//条码
            String goodsName = (String) map.get("goods_name");//名称
            Integer num = (Integer) map.get("num");	
            BigDecimal price = (BigDecimal) map.get("price");	
            price = price.setScale(2, BigDecimal.ROUND_HALF_UP);// 保留两位小数
            BigDecimal amount = (BigDecimal) map.get("amount");	        
            amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);// 保留两位小数
            detailsTotalNum=detailsTotalNum+num;
            detailsTotalAmount=detailsTotalAmount.add(amount);
    		InvoiceTemplateItemDetailsYSDTO  itemDetails=new InvoiceTemplateItemDetailsYSDTO();
    		itemDetails.setGoodsNo(goodsNo);
    		itemDetails.setBarcode(barcode);
    		itemDetails.setGoodsName(goodsName);
    		itemDetails.setTotalNum(num.toString());
    		itemDetails.setPrice(price.toString());
    		itemDetails.setTotalPrice(amount.toString());
    		String tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
    		itemDetails.setUnit(tallyingUnitLabel);
    		itemDetailsList.add(itemDetails);
    		
    		String orderKey=code+","+poNo;
    		// 发票总表的金额汇总  数量汇总
    		if (codeListMap.containsKey(orderKey)) {
    			Map<String, Object> codeMap = codeListMap.get(orderKey);
    			int numMap = (int) codeMap.get("num");
    			codeMap.put("num", num+numMap);
				codeListMap.put(orderKey, codeMap);
			}else {
				Map<String, Object>codeMap=new HashMap<>();
				codeMap.put("tallyingUnit", tallyingUnit);
				codeMap.put("num", num);
				codeListMap.put(orderKey, codeMap);
			}
    		
		}
    	templateDTO.setDetailsTotalNum(String.valueOf(detailsTotalNum));
    	templateDTO.setDetailsTotalAmount(detailsTotalAmount.toString());
    	templateDTO.setDetailsItemList(itemDetailsList);
    	
        Map<String, BigDecimal> poNoMap = new HashMap<>();
        BigDecimal totalAmount = new BigDecimal("0");
        int totalAllNum=0;
        List<Map<String, Object>> advanceBillItemList = advanceBillItemDao.getAdvanceBillItemList(ids);
        List<InvoiceTemplateItemYSDTO> itemList = new LinkedList<>();
        for (Map<String, Object> map : advanceBillItemList) {
        	String relCode=(String) map.get("rel_code");
            String poNo = (String) map.get("po_no");
            String remark = (String) map.get("remark");
            BigDecimal amount = (BigDecimal) map.get("amount");
            amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);// 保留两位小数
            String orderKey=relCode+","+poNo;
            Map<String, Object> codeMap = codeListMap.get(orderKey);
            
            
            totalAmount=totalAmount.add(amount);
            InvoiceTemplateItemYSDTO itemDTO = new InvoiceTemplateItemYSDTO();
            itemDTO.setPoNo(poNo);
            itemDTO.setRemark(remark);
            itemDTO.setAmount(amount.toString());
            if (codeMap!=null) {
            	String tallyingUnit = (String) codeMap.get("tallyingUnit");
            	int numMap = (int) codeMap.get("num");
            	totalAllNum=totalAllNum+numMap;
            	String tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
            	itemDTO.setUnit(tallyingUnitLabel);
            	itemDTO.setQuantity(String.valueOf(numMap));
			}
            itemList.add(itemDTO);                   
        }
        templateDTO.setTotalAllAmount(totalAmount.toString());
        templateDTO.setTotalAllNum(String.valueOf(totalAllNum));
        templateDTO.setItemList(itemList);
        
    }
    
    
    /**
     * 预收开票校验
     */
    @Override
    public Map<String, String> validate(List<Long> ids) throws SQLException {
        Map<String, String> resultMap = new HashMap<>();              
        //单个校验
        /**
         * 对单个账单进行开票时，开票仅对开票状态为“待开票”或“已作废”且账单状态非为“作废待审”、
         * “已作废”的预收账单可操作开票，即待提交、
         * 待审核的状态下均可开票。（注：检查每次开票成功后，均会在应收发票库记录发票信息）
         */
        if (ids.size()==1) {// 单个校验
        	 AdvanceBillModel advanceBillModel = advanceBillDao.searchById(ids.get(0));
        	 if (!(advanceBillModel.getInvoiceStatus().equals(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_00) ||
        			 advanceBillModel.getInvoiceStatus().equals(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_02))) {
                 resultMap.put("code", "01");
                 resultMap.put("msg", "开票的预收账单开票状态只能为“待开票”或“已作废");
                 return resultMap;
             }        	 
        	 if (advanceBillModel.getBillStatus().equals(DERP_ORDER.ADVANCEBILL_BILLSTATUS_05) 
                     ||advanceBillModel.getBillStatus().equals(DERP_ORDER.ADVANCEBILL_BILLSTATUS_06) ) {
                 resultMap.put("code", "01");
                 resultMap.put("msg", "开票账单状态需不能为“作废待审、“已作废！");
                 return resultMap;
             }
		}
              
        /**
         * 对于多个账单合并开票时，仅对“同公司+同事业部+同客户+同币种”可合并开票，
         * 合并开票时必须校验账单状态均为非“待提交、待审核”且开票状态为“待开票”或“已作废”；
         */
        Long customerId = null;
        String currency = null;
        Long buId=null;
        if (ids.size()>1) {// 多个校验        
        	  for (int i = 0; i < ids.size(); i++) {
                  AdvanceBillModel advanceBillModel = advanceBillDao.searchById(ids.get(i));
                  if (!(advanceBillModel.getInvoiceStatus().equals(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_00) ||
                			 advanceBillModel.getInvoiceStatus().equals(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_02))) {
                         resultMap.put("code", "01");
                         resultMap.put("msg", "开票的预收账单开票状态只能为“待开票”或“已作废");
                         return resultMap;
                  }  
                  if (advanceBillModel.getBillStatus().equals(DERP_ORDER.ADVANCEBILL_BILLSTATUS_00)
                          || advanceBillModel.getBillStatus().equals(DERP_ORDER.ADVANCEBILL_BILLSTATUS_01) 
                          ||advanceBillModel.getBillStatus().equals(DERP_ORDER.ADVANCEBILL_BILLSTATUS_05) 
                          ||advanceBillModel.getBillStatus().equals(DERP_ORDER.ADVANCEBILL_BILLSTATUS_06) ) {
                      resultMap.put("code", "01");
                      resultMap.put("msg", "合并开票时账单状态需不能为“待提交”、“待审核”、“作废待审、“已作废！");
                      return resultMap;
                  }
                                                    
                  if (i == 0) {
                      customerId = advanceBillModel.getCustomerId();
                      currency = advanceBillModel.getCurrency();
                      buId=advanceBillModel.getBuId();
                  }
                  
                  
                  if (!customerId.equals(advanceBillModel.getCustomerId())) {
                      resultMap.put("code", "01");
                      resultMap.put("msg", "仅能对同客户的预收账单进行同时合并开票");
                      return resultMap;
                  }

                  if (!currency.equals(advanceBillModel.getCurrency())) {
                      resultMap.put("code", "01");
                      resultMap.put("msg", "仅能对同币种的预收账单进行同时合并开票");
                      return resultMap;
                  }
                  if (!buId.equals(advanceBillModel.getBuId())) {
                      resultMap.put("code", "01");
                      resultMap.put("msg", "仅能对同事业部的预收账单进行同时合并开票");
                      return resultMap;
                  }
              }       	       	
		}
        resultMap.put("code", "00");
        resultMap.put("msg", "校验通过");
        return resultMap;
    }

    /**
     * 导出
     */
	@Override
	public Map<String, Object> exportAdvanceBill(User user,AdvanceBillDTO dto) throws SQLException {
		Map<String, Object>rusultMap=new HashedMap();
		  if(dto.getBuId() == null) {
              List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
              if (buList.isEmpty()) return rusultMap;
              dto.setBuList(buList);
          }
		  // 获取头
		  List<Map<String, Object>> advanceBillList = advanceBillDao.exportAdvanceBillMap(dto);
		  // 获取体
		  List<Map<String, Object>> advanceBillItemList = advanceBillDao.exportAdvanceBillItemMap(dto);
		  rusultMap.put("advanceBillList", advanceBillList);
		  rusultMap.put("advanceBillItemList", advanceBillItemList);		  
		return rusultMap;
	}
    
    @Override
    public AdvanceBillDTO listAdvanceBillByPage(User user,AdvanceBillDTO dto) throws IOException, SQLException {
        try{
            if(dto.getBuId() == null) {
                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if (buList.isEmpty()) {
                    dto.setTotal(0);
                    return dto;
                }
                dto.setBuList(buList);
            }
            AdvanceBillDTO advanceBillDTOs=advanceBillDao.listAdvanceBillByPage(dto);
            return advanceBillDTOs;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AdvanceBillDataDTO listAddOrderByPage(User user,AdvanceBillDatasDTO dto,String type) throws IOException, SQLException{
        try{
            if(dto.getBuId() == null) {
                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if (buList.isEmpty()) {
                    return null;
                }
                dto.setBuList(buList);
            }else{
                dto.setBuList(Arrays.asList(dto.getBuId()));
            }

            //PO号
            if (StringUtils.isNotBlank(dto.getPoNo())) {
                String poNoStr = dto.getPoNo();
                poNoStr = poNoStr.replaceAll("(\r\n|\r|\n|\n\r)", "&");
                List<String> poNos = Arrays.asList(poNoStr.split("&"));
                dto.setPoNos(poNos);
            }
            //关联业务单号
            if (StringUtils.isNotBlank(dto.getRelCode())) {
                String relCodeStr = dto.getRelCode();
                relCodeStr = relCodeStr.replaceAll("(\r\n|\r|\n|\n\r)", "&");
                List<String> relCodes = Arrays.asList(relCodeStr.split("&"));
                dto.setRelCodes(relCodes);
            }

            //获取单据类型为销售的业务单据
            if (DERP_ORDER.ADVANCEBILL_ORDERTYPE_1.equals(type)) { //销售订单
                AdvanceBillDataDTO advanceBillDTO = advanceBillDao.listAddSaleOrderByPage(dto);
                //销售单关联查询到的则默认取销售退货单整单数据；
                List<AdvanceBillDataDTO> advanceBillDTOList = advanceBillDTO.getList();
                List<String> orderCodeList = new ArrayList<>();

                for (AdvanceBillDataDTO advance : advanceBillDTOList) {
                    String relCode = advance.getRelCode();
                    orderCodeList.add(relCode);
                }
                advanceBillDTO.setOrderType(DERP_ORDER.ADVANCEBILL_ORDERTYPE_1);
                return advanceBillDTO;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map checkBuMerchantCurrency(List<AdvanceBillDataDTO> list) {
        Map map=new HashMap();
        if(list==null){
            map.put("code","01");
            map.put("message","数据为空");
            return map;
        }
        AdvanceBillSaveForm form=new AdvanceBillSaveForm();
        Map<String,Object> hashMap=new HashMap();
        List<AdvanceBillItemDTO> itemList =new ArrayList<>();
        int i=0;
        try{
            SettlementConfigModel settlementConfigModel = new SettlementConfigModel();
            settlementConfigModel.setProjectName("采销执行收入");
            SettlementConfigModel configModel = settlementConfigDao.searchByModel(settlementConfigModel);
            for(AdvanceBillDataDTO dto:list){
                String str=dto.getBuId()+"_"+dto.getCustomerId()+"_"+dto.getCurrency();
                if(i==0){
                    hashMap.put(str,dto);
                }
                if(!hashMap.containsKey(str)){
                    map.put("code","01");
                    map.put("message","单据必须为同客户+同事业部+同币种");
                    return map;
                }
                AdvanceBillItemDTO item=new AdvanceBillItemDTO();//封装表体数据
                item.setAmount(dto.getAmount());
                item.setRelCode(dto.getRelCode());
                item.setPoNo(dto.getPoNo());
                itemList.add(item);
                form.setBuId(dto.getBuId());//封装表头参数
                form.setBuName(dto.getBuName());
                form.setCustomerName(dto.getCustomerName());
                form.setCustomerId(dto.getCustomerId());
                form.setCurrency(dto.getCurrency());
                form.setOrderType(DERP_ORDER.ADVANCEBILL_ORDERTYPE_1);
                if (configModel != null) {
                    form.setProjectId(configModel.getId());
                    form.setProjectName(configModel.getProjectName());
                }
                i++;
            }
            //校验成功后生成预收账单Code
            String billCode = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSKD);
            form.setCode(billCode);
            form.setItemList(itemList);
        }catch (Exception e) {
            e.printStackTrace();
        }
        map.put("code","00");
        map.put("formData",form);
        return map;
    }

    /**
     * 保存或修改提交预收账单
     */
    @Override
    public Map<String, Object>  saveAdvanceBill(String type,AdvanceBillDTO dto)  {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            User user = ShiroUtils.getUserByToken(dto.getToken());
            //生成的预收账单
            AdvanceBillModel advanceBillModel = new AdvanceBillModel();
            //预收账单明细集合
            List<AdvanceBillItemModel> advanceBillItemModels = new ArrayList<>();
            //单据类型为销售
            if (DERP_ORDER.ADVANCEBILL_ORDERTYPE_1.equals(dto.getOrderType())) {
                if(dto.getItemList().size()==0){
                    resultMap.put("code","01");
                    resultMap.put("message","预收账单至少保留一条业务单据");
                    return resultMap;
                }
               // Map<String,List<Long>> map=new HashMap<>();
                //循环获取预收账单表体
                for(AdvanceBillItemDTO item:dto.getItemList()){
                    //提交操作需要校验费项和预收金额是否为空
                    if("0".equals(type)){
                        if(item.getProjectId()==null){
                            resultMap.put("code", "01");
                            resultMap.put("message", "业务单号:"+item.getRelCode()+"的费用项目不能为空");
                            return resultMap;
                        }
                        if(item.getAmount()==null){
                            resultMap.put("code", "01");
                            resultMap.put("message", "业务单号:"+item.getRelCode()+"的预收金额不能为空");
                            return resultMap;
                        }
                    }
                    //费项配置
                    SettlementConfigModel configSettlement = new SettlementConfigModel();
                    configSettlement.setId(item.getProjectId());
                    configSettlement.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
                    configSettlement.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
                    SettlementConfigModel toBSettlementConfigModel = settlementConfigDao.searchByModel(configSettlement);
                    if(toBSettlementConfigModel==null){
                        resultMap.put("code","01");
                        resultMap.put("message","没有找到相关的费项配置");
                        return resultMap;
                    }
                    //判断多个业务单据的费项是否重复
					/*
					 * if(map.containsKey(item.getRelCode())){ List<Long>
					 * list=map.get(item.getRelCode()); for(Long projectId:list){
					 * if(projectId==item.getProjectId()){ resultMap.put("code","01");
					 * resultMap.put("message","相同单据："+item.getRelCode()+"的费项配置不能重复"); return
					 * resultMap; } } }else{
					 * map.put(item.getRelCode(),Arrays.asList(item.getProjectId())); }
					 */
                    //销售订单
                    SaleOrderModel saleOrderModel = new SaleOrderModel();
                    saleOrderModel.setCode(item.getRelCode());
                    saleOrderModel.setMerchantId(user.getMerchantId());
                    SaleOrderModel existModel = saleOrderDao.searchByModel(saleOrderModel);
                    if (existModel == null) {
                        resultMap.put("code","01");
                        resultMap.put("message","业务单号:" + item.getRelCode() + "关联的销售订单不存在");
                        return resultMap;
                    } else if (DERP_ORDER.SALEORDER_STATE_008.equals(existModel.getState())) {
                        resultMap.put("code","01");
                        resultMap.put("message","业务单号:" + item.getRelCode() + "关联的销售订单状态是“待提交”");
                        return resultMap;
                    }
                    //预收账单表体明细
                    AdvanceBillItemModel advanceBillItemModel = new AdvanceBillItemModel();
                    advanceBillItemModel.setRelCode(existModel.getCode());
                    advanceBillItemModel.setProjectId(toBSettlementConfigModel.getId());
                    advanceBillItemModel.setProjectName(toBSettlementConfigModel.getProjectName());
                    advanceBillItemModel.setPoNo(existModel.getPoNo());
                    advanceBillItemModel.setAmount(item.getAmount()==null?new BigDecimal(0):item.getAmount());
                    advanceBillItemModel.setRemark(item.getRemark());
                    advanceBillItemModels.add(advanceBillItemModel);
                }
            }
            //修改或提交时获取预收账单id
            Long id=dto.getId();
            if(id!=null&&id>0){
                //删除预收账单明细
                advanceBillModel=advanceBillDao.searchById(dto.getId());
                if(!DERP_ORDER.ADVANCEBILL_BILLSTATUS_00.equals(advanceBillModel.getBillStatus())){
                    resultMap.put("code","01");
                    resultMap.put("message","预收账单不是待提交状态");
                    return resultMap;
                }
                advanceBillItemDao.delItems(dto.getId());
            }
            //新增或提交添加预收账单表头信息
            if(id==null||id==0){
                Timestamp date=TimeUtils.getNow();
                //String billCode = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSZD);
                advanceBillModel.setCode(dto.getCode());
                advanceBillModel.setCustomerId(dto.getCustomerId());
                advanceBillModel.setCustomerName(dto.getCustomerName());
                advanceBillModel.setBuId(dto.getBuId());
                advanceBillModel.setBuName(dto.getBuName());
                advanceBillModel.setCurrency(dto.getCurrency());
                advanceBillModel.setMerchantId(Long.valueOf(dto.getMerchantId()));
                advanceBillModel.setMerchantName(dto.getMerchantName());
                advanceBillModel.setBillStatus(DERP_ORDER.ADVANCEBILL_BILLSTATUS_00);
                advanceBillModel.setOrderType(DERP_ORDER.ADVANCEBILL_ORDERTYPE_1);
                advanceBillModel.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10);
                advanceBillModel.setCreateDate(date);
                advanceBillModel.setCreaterId(user.getId());
                advanceBillModel.setCreater(user.getName());
                advanceBillModel.setBillDate(date);
                advanceBillModel.setInvoiceStatus(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_00);
                id=advanceBillDao.save(advanceBillModel);
            }
            // 邮件的预收金额
            BigDecimal amount=new BigDecimal("0");
            //添加预收账单表体明细
            if(advanceBillItemModels.size() > 0) {
                for (AdvanceBillItemModel itemModel : advanceBillItemModels) {
                    itemModel.setAdvanceId(id);
                    itemModel.setCreateDate(TimeUtils.getNow());
                    advanceBillItemDao.save(itemModel);
                    amount=amount.add(itemModel.getAmount());
                }
            }
            //提交新增
            if("0".equals(type)){
                //提交成功后更新预收款单的状态为“待审核”;
                advanceBillModel.setBillStatus(DERP_ORDER.ADVANCEBILL_BILLSTATUS_01);
                advanceBillModel.setModifyDate(TimeUtils.getNow());
                advanceBillDao.modify(advanceBillModel);
                //添加操作日志
                AdvanceBillOperateItemModel opera=new AdvanceBillOperateItemModel();
                opera.setAdvanceId(advanceBillModel.getId());
                opera.setOperateId(user.getId());
                opera.setOperater(user.getName());
                opera.setOperateDate(TimeUtils.getNow());
                opera.setCreateDate(TimeUtils.getNow());
                opera.setOperateResult(DERP_ORDER.ADVANCEBILL_RESULT_2);
                opera.setOperateType(DERP_ORDER.ADVANCEBILL_TYPE_0);
                advanceBillOperateItemDao.save(opera);
                resultMap.put("code", "00");
                resultMap.put("message", "提交成功！");
                // 
                //预收提交提醒文案
              //封装发送邮件JSON  
                ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

                JsonConfig jsonConfig = new JsonConfig();
                //getReceiveOperators(billDTOS.get(0).getId(), emailUserDTO, jsonConfig);暂时不要

                emailUserDTO.setBuId(advanceBillModel.getBuId());
                emailUserDTO.setBuName(advanceBillModel.getBuName());
                emailUserDTO.setMerchantId(user.getMerchantId());
                emailUserDTO.setMerchantName(user.getMerchantName());
                emailUserDTO.setOrderCode(advanceBillModel.getCode());
                emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10);
                emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                        DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10));
                emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1);
                emailUserDTO.setSupplier(advanceBillModel.getCustomerName());
                emailUserDTO.setCurrency(advanceBillModel.getCurrency());
                emailUserDTO.setAmount(amount.toString());
                List<String> submitId=new ArrayList<String>();
                submitId.add(user.getId().toString());
                emailUserDTO.setSubmitId(submitId);
                emailUserDTO.setUserName(user.getName());
                //emailUserDTO.setAttArray(attArray);

                JSONObject emailJson = JSONObject.fromObject(emailUserDTO, jsonConfig);

                rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                        MQErpEnum.SEND_REMINDER_MAIL.getTags());   
                
                
            }else{
                resultMap.put("code", "00");
                resultMap.put("message", "保存成功！");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

/**
 * 提交作废申请
 * @throws Exception 
 */
    @Override
    public Map<String, Object> submitInvalidBill(Long id, String invalidRemark, String token) throws Exception {
        //校验预收单是否和应收核销是否关联
        Map<String, Object> resultMap=new HashMap<>();
        User user = ShiroUtils.getUserByToken(token);
        //查询预收账单表头信息
        AdvanceBillModel advanceBillModel=advanceBillDao.searchById(id);
        if(advanceBillModel==null){
            resultMap.put("code", "01");
            resultMap.put("message", "此账单状态信息不存在");
            return resultMap;
        }
        if (!DERP_ORDER.ADVANCEBILL_BILLSTATUS_03.equals(advanceBillModel.getBillStatus())) {
            resultMap.put("code", "01");
            resultMap.put("message", "仅对账单状态为“待收款”可操作作废！");
            return resultMap;
        }
        ReceiveBillVerifyItemModel verifyModel=new ReceiveBillVerifyItemModel();
        verifyModel.setAdvanceId(advanceBillModel.getId());
        List<ReceiveBillVerifyItemModel>  list=receiveBillVerifyItemDao.list(verifyModel);
        if(list.size() > 0){
            resultMap.put("code", "01");
            resultMap.put("message", "预收账单已勾稽，不能作废");
            return resultMap;
        }
        if(StringUtils.isBlank(invalidRemark)){
            resultMap.put("code", "01");
            resultMap.put("message", "作废备注不能为空");
            return resultMap;
        }
        //更新账单状态为“作废待审”
        advanceBillModel.setBillStatus(DERP_ORDER.ADVANCEBILL_BILLSTATUS_05);
        advanceBillDao.modify(advanceBillModel);

        //添加操作日志
        AdvanceBillOperateItemModel opera=new AdvanceBillOperateItemModel();
        opera.setAdvanceId(id);
        opera.setOperateId(user.getId());
        opera.setOperater(user.getName());
        opera.setOperateDate(TimeUtils.getNow());
        opera.setCreateDate(TimeUtils.getNow());
        opera.setOperateResult(DERP_ORDER.ADVANCEBILL_RESULT_3);
        opera.setOperateType(DERP_ORDER.ADVANCEBILL_TYPE_2);
        opera.setOperateRemark(invalidRemark);
        advanceBillOperateItemDao.save(opera);
        
        // 获取预收金额
        AdvanceBillDTO dto=advanceBillDao.getAdvanceById(id);
        
        //预收作废提交提醒文案
        //封装发送邮件JSON  
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;
        JsonConfig jsonConfig = new JsonConfig();
        //getReceiveOperators(billDTOS.get(0).getId(), emailUserDTO, jsonConfig);暂时不要

        emailUserDTO.setBuId(advanceBillModel.getBuId());
        emailUserDTO.setBuName(advanceBillModel.getBuName());
        emailUserDTO.setMerchantId(user.getMerchantId());
        emailUserDTO.setMerchantName(user.getMerchantName());
        emailUserDTO.setOrderCode(advanceBillModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_16);//作废提交
        emailUserDTO.setSupplier(advanceBillModel.getCustomerName());
        emailUserDTO.setSupplier(advanceBillModel.getCustomerName());
        emailUserDTO.setCurrency(advanceBillModel.getCurrency());
        emailUserDTO.setAmount(dto.getSumAmount().toString());
        emailUserDTO.setDrawerId(user.getId());
        
        // 提交人审核人
        AdvanceBillOperateItemModel opraeteI =new AdvanceBillOperateItemModel();
        opraeteI.setAdvanceId(id);        
        List<AdvanceBillOperateItemModel> opraeteIList = advanceBillOperateItemDao.list(opraeteI);
        List<String> submitId=new ArrayList<String>();  
        Long auditorId=null;       
        for (AdvanceBillOperateItemModel operateItemModel : opraeteIList) {      	   
            if (DERP_ORDER.ADVANCEBILL_TYPE_0.equals(operateItemModel.getOperateType())&&operateItemModel.getOperateId()!=null) {
            	submitId.add(operateItemModel.getOperateId().toString());
            }            
            if (DERP_ORDER.ADVANCEBILL_TYPE_1.equals(operateItemModel.getOperateType())&&operateItemModel.getOperateId()!=null) {
            	auditorId=operateItemModel.getOperateId();
            } 
        }
        emailUserDTO.setSubmitId(submitId);//提交人
        emailUserDTO.setAuditorId(auditorId);//审核人
        emailUserDTO.setUserName(user.getName());
        JSONObject emailJson = JSONObject.fromObject(emailUserDTO, jsonConfig);

        rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                MQErpEnum.SEND_REMINDER_MAIL.getTags()); 
        
        

        resultMap.put("code", "00");
        resultMap.put("message", "提交申请作废成功！");
        return resultMap;
    }

    @Override
    public AdvanceBillDTO getDetail(Long id) {
        BigDecimal sumAmount=new BigDecimal(0);//预收总额
        AdvanceBillDTO dto=advanceBillDao.getAdvanceById(id);
        try{
            if(dto==null){
                throw new Exception("该预收账单信息不存在");
            }
            //获取预收账单表体明细
            List<AdvanceBillItemDTO> itemList=advanceBillItemDao.getAdvanceById(dto.getId());
            for(AdvanceBillItemDTO item:itemList){
                sumAmount=sumAmount.add(item.getAmount());
                SettlementConfigModel configSettlement = new SettlementConfigModel();
                configSettlement.setId(item.getProjectId());
                configSettlement.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
                configSettlement.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
                SettlementConfigModel toBSettlementConfigModel = settlementConfigDao.searchByModel(configSettlement);
                if(toBSettlementConfigModel==null){
                    throw new Exception("费项配置信息不存在");
                }
                //一级费项名称
                item.setOneLevelProjectName(toBSettlementConfigModel.getParentProjectName());
            }
            //预收总额转为中文大写
            dto.setSumAmount(sumAmount);
            dto.setSumAmountStr(NumberToCN.number2CNMontrayUnit(sumAmount));
            dto.setItemList(itemList);

            //获取操作日志
            List<AdvanceBillOperateItemDTO> operateItem=advanceBillOperateItemDao.getAdvanceById(dto.getId());
            for(AdvanceBillOperateItemDTO operate:operateItem){
                String operateType=DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_typeList, operate.getOperateType());
                String operateResult=DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_resultList, operate.getOperateResult());
                if(DERP_ORDER.ADVANCEBILL_TYPE_1.equals(operate.getOperateType())||DERP_ORDER.ADVANCEBILL_TYPE_3.equals(operate.getOperateType())){
                    operate.setContent(operateType+":"+operateResult);
                }else{
                    operate.setContent(operateType);
                }
            }
            dto.setOperateList(operateItem);

            //获取收款记录
            List<AdvanceBillVerifyItemDTO> verifyItem=advanceBillVerifyItemDao.getAdvanceById(dto.getId());
            dto.setVerifyList(verifyItem);
            // 获取应收核销记录
            ReceiveBillVerifyItemDTO verifyModel=new ReceiveBillVerifyItemDTO();
            verifyModel.setAdvanceId(id);
            List<ReceiveBillVerifyItemDTO> receiveVerifyList=receiveBillVerifyItemDao.getReceiveBillVerifyItem(verifyModel);
            dto.setReceiveVerifyList(receiveVerifyList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dto;
    }

    @Override
    public Map<String, Object> delAdvanceBill(long userId, List<Long> ids) {
        Map<String, Object> resultMap=new HashMap<>();
        try{
            for (Long id : ids) {
                AdvanceBillModel model=advanceBillDao.searchById(id);
                if (!model.getBillStatus().equals(DERP_ORDER.ADVANCEBILL_BILLSTATUS_00)) {
                    resultMap.put("code","01");
                    resultMap.put("message","预收账单：" + model.getCode() + "不是待提交状态，不能删除");
                    return resultMap;
                }
            }
            int num=advanceBillDao.delete(ids);
            if(num>0){
                for(Long id : ids){
                    //删除预收账单详情
                    advanceBillItemDao.delItems(id);
                    //删除预收账单核销记录
                    advanceBillVerifyItemDao.delItems(id);
                    //删除预收账单操作日志
                    advanceBillOperateItemDao.delItems(id);
                }
            }else{
                resultMap.put("code", "01");
                resultMap.put("message", "删除失败！");
                return resultMap;
            }
            resultMap.put("code", "00");
            resultMap.put("message", "删除成功！");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> auditAdvanceItem(AdvanceAuditForm form) throws Exception {

        Map<String, Object> returnMap=new HashMap<>();
        User user = ShiroUtils.getUserByToken(form.getToken());
        //检查必填项是否填写，若无填写，根据检查结果提示错误“请选择审批结果”或“请输入审批备注”
        if(StringUtils.isBlank(form.getOperateResult())){
            returnMap.put("code","01");
            returnMap.put("message","请选择审批结果");
            return returnMap;
        }
        if(StringUtils.isBlank(form.getOperateRemark())){
            returnMap.put("code","01");
            returnMap.put("message","请输入审批备注");
            return returnMap;
        }
        //根据预收账单id获取详情
        AdvanceBillModel advanceBillmodel=advanceBillDao.searchById(form.getAdvanceId());
        if(advanceBillmodel==null){
            returnMap.put("code", "01");
            returnMap.put("message", "此预收账单信息不存在");
            return returnMap;
        }
        if (!(DERP_ORDER.ADVANCEBILL_BILLSTATUS_01.equals(advanceBillmodel.getBillStatus())
                || DERP_ORDER.ADVANCEBILL_BILLSTATUS_05.equals(advanceBillmodel.getBillStatus()))) {
            returnMap.put("code", "01");
            returnMap.put("message", "此账单账单状态不是待审核/作废审批");
            return returnMap;
        }

        /**
         * 预收审批类型审核逻辑：“审批结果”若为审批通过，则更新预收账单的账单状态为“待收款”；
         *                      “审批结果”若为驳回，则更新预收账单的账单状态为“待提交”；
         * 同时更新预收单的审批记录，记录审批操作结果及审批人、审批时间；
         */
        // 
       String  sendEmailflag=null;
        
        //发票则作废盖章标标识 （订单待审核驳回 或者 订单作废通过）
        boolean cancelSealFlag=false;
        if(DERP_ORDER.ADVANCEBILL_BILLSTATUS_01.equals(advanceBillmodel.getBillStatus())){
            AdvanceBillOperateItemModel operateModel=new AdvanceBillOperateItemModel();
            operateModel.setAdvanceId(advanceBillmodel.getId());
            operateModel.setOperateType(DERP_ORDER.ADVANCEBILL_TYPE_1);
            //审批结果”若为审批通过，则更新预收账单的账单状态为“待收款”,否则更新预收账单的账单状态为“待提交”；
            if(DERP_ORDER.ADVANCEBILL_RESULT_0.equals(form.getOperateResult())){
                advanceBillmodel.setBillStatus(DERP_ORDER.ADVANCEBILL_BILLSTATUS_03);
                sendEmailflag=DERP_SYS.REMINDER_EMAILCONFIG_TYPE_19;//审核通过
            }else{
            	sendEmailflag=DERP_SYS.REMINDER_EMAILCONFIG_TYPE_20;//审核驳回
                advanceBillmodel.setBillStatus(DERP_ORDER.ADVANCEBILL_BILLSTATUS_00);
                //发票则作废盖章标标识 （订单待审核驳回 或者 订单作废通过）
                if (!(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_00.equals(advanceBillmodel.getInvoiceStatus())||
                		DERP_ORDER.ADVANCEBILL_INVOICESTATUS_02.equals(advanceBillmodel.getInvoiceStatus()))
                		) {               	
                	cancelSealFlag=true;
				}

            }
            advanceBillmodel.setModifyDate(TimeUtils.getNow());
        }

        boolean isSync=false;
        Timestamp auditTime = TimeUtils.getNow();
       
        /**
         * 作废审批类型逻辑：
         *  若操作驳回，则更新账单状态为原始状态“待核销”。
         *  若作废的预收单审核通过，且NC同步状态为“已同步”，则更新当前该预收款单状态为“已作废”，并触发NC作废状态接口推送，对应接口为：4.8 结算账单红冲/作废接口 [业务系统-->前置接口层]     /external/settlement/concancel。
         *  若NC同步状态为“未同步”则不触发接口，仅更新当前该预收款单状态为“已作废”即可。
         */
        
        if(DERP_ORDER.ADVANCEBILL_BILLSTATUS_05.equals(advanceBillmodel.getBillStatus())){
            //审核通过
            if(DERP_ORDER.ADVANCEBILL_RESULT_0.equals(form.getOperateResult())){
            	sendEmailflag=DERP_SYS.REMINDER_EMAILCONFIG_TYPE_22;//作废审核通过
                //NC同步状态为“已同步”，则更新当前该预收款单状态为“已作废”，并触发NC作废状态接口推送，对应接口为：4.8 结算账单红冲/作废接口 [业务系统-->前置接口层]     /external/settlement/concancel。
                if(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_11.equals(advanceBillmodel.getNcStatus())){
                    advanceBillmodel.setBillStatus(DERP_ORDER.ADVANCEBILL_BILLSTATUS_06);
                    isSync=true;
                }else if(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10.equals(advanceBillmodel.getNcStatus())){
                    //若NC同步状态为“未同步”则不触发接口，仅更新当前该预收款单状态为“已作废”即可。
                    advanceBillmodel.setBillStatus(DERP_ORDER.ADVANCEBILL_BILLSTATUS_06);
                }
                // 发票则作废盖章标标识和订单开票状态 （订单待审核驳回 或者 订单作废通过）
                if (!(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_00.equals(advanceBillmodel.getInvoiceStatus())||
                		DERP_ORDER.ADVANCEBILL_INVOICESTATUS_02.equals(advanceBillmodel.getInvoiceStatus()))
                		) {
                	cancelSealFlag=true;
				}
            }else{
            	sendEmailflag=DERP_SYS.REMINDER_EMAILCONFIG_TYPE_23;//作废审核通过
                //操作驳回，作废驳回的情况 只有待收款才可以申请作废 隐藏 驳回不用修改订单状态
                advanceBillmodel.setBillStatus(DERP_ORDER.ADVANCEBILL_BILLSTATUS_03);
            }
        }
        //修改预收账单状态
        advanceBillDao.modify(advanceBillmodel);
        // 发票则作废盖章标标识和订单开票状态  清空订单的发票号 开票时间开票链接 清空订单开票订单id
        if (cancelSealFlag&&advanceBillmodel.getInvoiceId()!=null) {
        	ReceiveBillInvoiceModel receiveBillInvoice = receiveBillInvoiceDao.searchById(advanceBillmodel.getInvoiceId());
        	String updateUrl = addWatermark(receiveBillInvoice.getInvoiceNo(), user, receiveBillInvoice.getInvoicePath());
        	ReceiveBillInvoiceModel receiveBillInvoiceModel = new ReceiveBillInvoiceModel();
            receiveBillInvoiceModel.setId(advanceBillmodel.getInvoiceId());
            receiveBillInvoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_02);
            receiveBillInvoiceModel.setInvoicePath(updateUrl);
            receiveBillInvoiceModel.setModifyDate(TimeUtils.getNow());
            receiveBillInvoiceDao.modify(receiveBillInvoiceModel);
            // 获取预收ids
            String invoiceRelIds = receiveBillInvoice.getInvoiceRelIds();
            List<Long> relIdList = new ArrayList<>();
            if (StringUtils.isNotBlank(invoiceRelIds)) {
            	List<String> asList = Arrays.asList(invoiceRelIds.split(","));
            	for (String id : asList) {
            		relIdList.add(Long.valueOf(id));
				}
            } 
            
            // 批量修改预收单      
            relIdList.add(advanceBillmodel.getId());
            advanceBillDao.batchUpdateInvoiceStatus(relIdList,DERP_ORDER.ADVANCEBILL_INVOICESTATUS_02);
			
		}
        

        //添加预收账单操作日志记录
        AdvanceBillOperateItemModel itemModel=new AdvanceBillOperateItemModel();
        itemModel.setAdvanceId(advanceBillmodel.getId());
        itemModel.setOperateType(form.getOperateType());
        itemModel.setOperateResult(form.getOperateResult());
        itemModel.setOperateRemark(form.getOperateRemark());
        itemModel.setCreateDate(auditTime);
        itemModel.setOperateId(user.getId());
        itemModel.setOperater(user.getName());
        itemModel.setOperateDate(auditTime);
        advanceBillOperateItemDao.save(itemModel);


        //触发NC作废状态接口推送，对应接口为：4.8 结算账单红冲/作废接口 [业务系统-->前置接口层]     /external/settlement/concancel。
        if(isSync){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            ReceiveHcInvalidRoot invalidRoot = new ReceiveHcInvalidRoot();
            invalidRoot.setConfirmBillId(advanceBillmodel.getCode());
            invalidRoot.setSourceCode(ApolloUtil.ncSourceType);
            invalidRoot.setState("1");
            invalidRoot.setCancelTime(sdf.format(auditTime));
            invalidRoot.setRemark(form.getOperateRemark());
            JSONObject json = JSONObject.fromObject(invalidRoot);
            String clearText = json.toString();
            //提交NC
            String ncResult = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_08.getUri(), clearText);

            JSONObject resultJson = JSONObject.fromObject(ncResult);

            if (resultJson.containsKey("code") && resultJson.getString("code").equals("1002")) {
                throw new RuntimeException(resultJson.getString("msg"));
            }
        }
        
        // 获取预收金额
        AdvanceBillDTO dto=advanceBillDao.getAdvanceById(form.getAdvanceId());
        /**
         *  19:审核通过 20：审核驳回 22:作废审核通过 23:作废审核驳回 这四个邮件摸版共用一个摸版
         */
        
        //封装发送邮件JSON  
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;        
        JsonConfig jsonConfig = new JsonConfig();
        //getReceiveOperators(billDTOS.get(0).getId(), emailUserDTO, jsonConfig);暂时不要
        emailUserDTO.setBuId(advanceBillmodel.getBuId());
        emailUserDTO.setBuName(advanceBillmodel.getBuName());
        emailUserDTO.setMerchantId(user.getMerchantId());
        emailUserDTO.setMerchantName(user.getMerchantName());
        emailUserDTO.setOrderCode(advanceBillmodel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10));
        emailUserDTO.setType(sendEmailflag);
        emailUserDTO.setSupplier(advanceBillmodel.getCustomerName());
        emailUserDTO.setCurrency(advanceBillmodel.getCurrency());
        emailUserDTO.setAmount(dto.getSumAmount().toString());

        
     // 提交人审核人
        AdvanceBillOperateItemModel opraeteI =new AdvanceBillOperateItemModel();
        opraeteI.setAdvanceId(advanceBillmodel.getId());        
        List<AdvanceBillOperateItemModel> opraeteIList = advanceBillOperateItemDao.list(opraeteI);
        List<String> submitIdList=new ArrayList<String>();      
        for (AdvanceBillOperateItemModel operateItemModel : opraeteIList) {      	   
            if (DERP_ORDER.ADVANCEBILL_TYPE_0.equals(operateItemModel.getOperateType())&&operateItemModel.getOperateId()!=null) {
            	submitIdList.add(operateItemModel.getOperateId().toString());
            }            
        }
        emailUserDTO.setSubmitId(submitIdList);       
        emailUserDTO.setAuditorId(user.getId());//审核人
        emailUserDTO.setUserName(user.getName());
        //emailUserDTO.setAttArray(attArray);

        JSONObject emailJson = JSONObject.fromObject(emailUserDTO, jsonConfig);
		if (StringUtils.isNotBlank(sendEmailflag)) {
			rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
	                MQErpEnum.SEND_REMINDER_MAIL.getTags());  
		}
             
        returnMap.put("code","00");
        returnMap.put("message","保存成功！");
        return returnMap;
    }

    private String addWatermark(String invoiceNo, User user, String invoicePath) throws Exception {
        InputStream in = null;
        ByteArrayOutputStream output = null;
        try {
            String ext = "pdf";
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource resource = resolver.getResource("classpath:/image/abolished.png");
            in = resource.getInputStream();
            output = new ByteArrayOutputStream();
            IOUtils.copyLarge(in, output);

            String tempPath = ApolloUtilDB.orderBasepath + "/temp/" + System.currentTimeMillis();
            Map<String, Object> map = PdfUtils.setWatermark(invoicePath, output.toByteArray(), invoiceNo + "预收账单发票." + ext, tempPath);

            String saveUrl = (String) map.get("saveUrl");
            Integer fileSize = (Integer) map.get("fileSize");

            //逻辑删除之前的发票文件
            Map<String, Object> params = new HashMap<String, Object>();
            Map<String, Object> data = new HashMap<String, Object>();
            params.put("relationCode", invoiceNo + "-invoice");

            data.put("status", DERP_ORDER.ATTACHMENT_STATUS_006);
            data.put("modifyDate", new Date());

            attachmentInfoMongoDao.update(params, data);

            //附件信息写入MongoDB
            AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo();
            attachmentInfoMongo.setAttachmentName(invoiceNo + "预收账单发票." + ext);        //附件名
            attachmentInfoMongo.setAttachmentSize(Long.valueOf(fileSize));        //附件大小
            attachmentInfoMongo.setAttachmentType(ext);                //附件类型
            attachmentInfoMongo.setCreator(user.getId());            //上传者
            attachmentInfoMongo.setCreatorName(user.getName());
            attachmentInfoMongo.setCreateDate(new Date());
            attachmentInfoMongo.setRelationCode(invoiceNo + "-invoice");
            attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
            attachmentInfoMongo.setAttachmentUrl(saveUrl);              //设置Url
            attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
            attachmentInfoMongo.setModule(SourceStatusEnum.YSKD.getKey());
            attachmentInfoMongoDao.insert(attachmentInfoMongo);
            return saveUrl;
        } catch (Exception e) {
            throw new RuntimeException("发票文件增加已作废水印失败");
        } finally {
            in.close();
            output.close();
        }
    }
    
    @Override
    public List<ReceiveBillToNCDTO> listAdvanceBillsToNCById(String token,long id) throws SQLException {
        List<ReceiveBillToNCDTO> list=new ArrayList<>();
        //获取预收账单信息
        AdvanceBillModel model=advanceBillDao.searchById(id);
        //获取预收账单表体明细
        List<AdvanceBillItemDTO> itemList=advanceBillItemDao.getAdvanceById(id);
        if(model==null||itemList==null){
            return list;
        }
        for(AdvanceBillItemDTO item:itemList){
            ReceiveBillToNCDTO ncdto=new ReceiveBillToNCDTO();
            ncdto.setBillCode(model.getCode());
            ncdto.setBuName(model.getBuName());
            ncdto.setBuId(model.getBuId());
            ncdto.setCurrency(model.getCurrency());
            ncdto.setNcChannelCode(model.getNcCode());
            ncdto.setSettlementType(DERP_ORDER.SETTLEMENT_TYPE_2);
            SettlementConfigModel toBSettlementConfigModel = settlementConfigDao.searchById(item.getProjectId());
            if(toBSettlementConfigModel!=null){
                ncdto.setPaymentSubjectName(toBSettlementConfigModel.getPaymentSubjectName());
                ncdto.setPaymentSubjectId(toBSettlementConfigModel.getPaymentSubjectId());
            }
            ncdto.setPrice(item.getAmount());
            ncdto.setBillId(model.getId());
            list.add(ncdto);
        }
        Collections.sort(list, new Comparator<ReceiveBillToNCDTO>() {
            @Override
            public int compare(ReceiveBillToNCDTO o1, ReceiveBillToNCDTO o2) {
                return o1.getBillCode().compareTo(o2.getBillCode());
            }
        });
        return list;
    }

    @Override
    public Map<String, Object> synNC(long id,String token) throws Exception {
        try{
            User user=ShiroUtils.getUserByToken(token);
            SimpleDateFormat month = new SimpleDateFormat("MM月") ;
            Map<String, Object> result = new HashMap<>();

            //获取预收账单
            AdvanceBillModel advanceBillModel=advanceBillDao.searchById(id);
            //获取预收账单明细记录
            List<AdvanceBillItemDTO> itemList=advanceBillItemDao.synNcItemByIds(Arrays.asList(id));

            Map<String, Object> customerParam = new HashMap<>();
            customerParam.put("customerid", advanceBillModel.getCustomerId());
            CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParam);
            if (customer == null) {
                result.put("code", "01");
                result.put("message", "客户不存在！");
                return result;
            }

            Map<String, Object> buMap = new HashMap<String, Object>();
            buMap.put("buId", advanceBillModel.getBuId());
            buMap.put("merchantId", advanceBillModel.getMerchantId());
            buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
            MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);
            if (merchantBuRelMongo == null) {
                result.put("code", "01");
                result.put("message", "事业部不存在！");
                return result;
            }

            int index = 1;
            BigDecimal totalPrice = new BigDecimal("0");//存储预收金额
            Timestamp synDate = TimeUtils.getNow();
            //存储表体明细记录
            List<Details> detailsList = new ArrayList<Details>() ;

            //根据费项+预收账单维度获取账单明细
            for(AdvanceBillItemDTO dto:itemList){
                BigDecimal amount=dto.getSumAmount()==null?new BigDecimal(0):dto.getSumAmount();
                SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(dto.getProjectId());
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
                //details 标签 循环体
                Details details=new Details();
                details.setSindex(String.valueOf(index));
                details.setInExpCode(settlementConfigModel.getInExpCode());
                details.setCurrency(advanceBillModel.getCurrency());
                details.setAmount(dto.getSumAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                details.setRate("0%");
                details.setTax(new BigDecimal("0.00"));
                details.setDetpCode(merchantBuRelMongo.getBuCode());
                details.setExternalOrder(advanceBillModel.getPoNo());
                detailsList.add(details) ;
                index++;
                totalPrice=totalPrice.add(amount);
            }
            //获取预收账单最新的审核记录
            List<Map<String, Object>> auditMapList = advanceBillOperateItemDao.getMaxAuditDate(Arrays.asList(advanceBillModel.getId()));
            Date operateDate=null;
            if(auditMapList.size()>0){
                operateDate=(Date)auditMapList.get(0).get("operateDate");
            }

            //财务 NC 4.7 结算账单接收接口 实体类
            ReceiveBillJsonRoot root = new ReceiveBillJsonRoot();
            root.setConfirmBillId(advanceBillModel.getCode());
            root.setSourceCode(ApolloUtil.ncSourceType);
            root.setType("3");
            root.setCorCcode(user.getTopidealCode());
            root.setCusCcode(customer.getMainId());
            root.setYearMonth(TimeUtils.formatMonth(operateDate).replace("-", ""));
            root.setCreated(TimeUtils.formatFullTime(operateDate));
            root.setTotalAmount(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP)) ;
            root.setTaxAmount(new BigDecimal("0")) ;
            root.setCurrency(advanceBillModel.getCurrency());
            root.setChecked("1");
            root.setInvoiced("0");
            root.setRemark("");
            root.setDetails(detailsList);
            //将对象转换为json格式
            JSONObject json = JSONObject.fromObject(root);
            String clearText = json.toString() ;
            LOGGER.info("--------预收账单同步NC的报文：----------"+clearText);
            //提交NC
            String ncResult = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_07.getUri(), clearText);
            //解析返回的json
            JSONObject resultJson =JSONObject.fromObject(ncResult);

            if (resultJson.containsKey("code") && resultJson.getString("code").equals("1001")) {
                //同步NC成功则回填预收账单的NC状态
                AdvanceBillModel billModel=new AdvanceBillModel();
                billModel.setId(advanceBillModel.getId());
                billModel.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_11);
                billModel.setNcSynDate(synDate);
                billModel.setSynchronizerId(user.getId());
                billModel.setSynchronizer(user.getName());
                advanceBillDao.modify(billModel);
            }else if (resultJson.containsKey("code") && resultJson.getString("code").equals("1002")) {
                result.put("code", "01");
                result.put("message", resultJson.getString("msg"));
                return result;
            } else {
                throw new RuntimeException(resultJson.toString());
            }
            result.put("code", "00");
            result.put("message", "成功");
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("提交NC失败");
        }
    }

    @Override
    public String exportNcBillPDF(List<Long> ids) throws Exception {
        //创建目录
        String basePath = ApolloUtilDB.orderBasepath+"/temp/"+System.currentTimeMillis();
        //日期格式化
        SimpleDateFormat monthSdf = new SimpleDateFormat("yyyy年MM月");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        FileOutputStream fileOut = null;
        ByteArrayOutputStream bos = null;
        try{
            //遍历预收单生成pdf(一个预收单一个pdf)
            for (Long id : ids) {
                Map<String, Object> dataMap = new HashMap<>();
                List<Map<String, Object>> lists = new ArrayList<>();
                //查询预收账单记录
                AdvanceBillModel advanceBillModel=advanceBillDao.searchById(id);
                Map<String, Object> merchantParam = new HashMap<>();
                merchantParam.put("merchantId", advanceBillModel.getMerchantId());
                MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantParam);
                Map<String, Object> customerParam = new HashMap<>();
                customerParam.put("customerid", advanceBillModel.getCustomerId());
                CustomerInfoMogo customerInfoMongo = customerInfoMongoDao.findOne(customerParam);

                BigDecimal totalPrice = new BigDecimal("0");//预收总金额

                //预收账单明细表体
                if(advanceBillModel!=null){
                    List<AdvanceBillItemDTO> itemList=advanceBillItemDao.getAdvanceById(advanceBillModel.getId());
                    for(AdvanceBillItemDTO itemDTO:itemList){
                        SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(itemDTO.getProjectId());
                        Map<String, Object> map = new HashMap<>();
                        if (settlementConfigModel != null) {
                            map.put("projectName", toNullStrWithReplace(settlementConfigModel.getProjectName()));
                        }
                        ReceivePaymentSubjectModel subjectModel=receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
                        if(subjectModel != null){
                            map.put("subjectName", toNullStrWithReplace(subjectModel.getSubCode() + "\\" + subjectModel.getSubName()));
                        }
                        map.put("poNo", toNullStrWithReplace(itemDTO.getPoNo()));
                        map.put("receivePrice", itemDTO.getAmount()==null?"" : itemDTO.getAmount());
                        lists.add(map);
                        if (itemDTO.getAmount() != null) {
                            totalPrice = totalPrice.add(itemDTO.getAmount());
                        }
                    }
                }
                dataMap.put("goodsList", lists);
                dataMap.put("code", advanceBillModel.getCode());
                dataMap.put("buName", toNullStr(advanceBillModel.getBuName()));
                dataMap.put("customer", toNullStr(customerInfoMongo.getName()));
                dataMap.put("currency", advanceBillModel.getCurrency());
                dataMap.put("createDate", advanceBillModel.getCreateDate() == null ? "" : dateFormat.format(advanceBillModel.getCreateDate()));
                dataMap.put("billStatus", toNullStr(DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_billStatusList, advanceBillModel.getBillStatus())));
                dataMap.put("voucherStatus", toNullStr(DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_voucherStatusList , advanceBillModel.getVoucherStatus())));
                dataMap.put("settlementType", toNullStr(DERP_ORDER.getLabelByKey(DERP_ORDER.settlement_typeList, DERP_ORDER.SETTLEMENT_TYPE_2)));
                dataMap.put("billMonth", advanceBillModel.getBillDate() != null ? monthSdf.format(advanceBillModel.getBillDate()) : "");
                dataMap.put("totalAmount", totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                dataMap.put("totalAmountLabel", NumberToCN.number2CNMontrayUnit(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP)));

                FileTempModel tempModel = new FileTempModel() ;
                tempModel.setCode("AdvanceBill");
                tempModel = fileTempDao.searchByModel(tempModel) ;
                String contentBody = tempModel.getContentBody();

                contentBody = FreemakerUtils.formatReplacementHtml(contentBody) ;
                contentBody = FreemakerUtils.genHtmlStrWithTemplate(dataMap, contentBody) ;
                contentBody = FreemakerUtils.ConvertLineBreaks(contentBody) ;

                bos = PdfUtils.html2Pdf(contentBody, PdfUtils.VERTICAL);

                new File(basePath).mkdirs();
                fileOut = new FileOutputStream(basePath+"/"+ advanceBillModel.getCode() +"预收账单.pdf");
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

    private String toNullStrWithReplace(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str.replace("&", "&amp;");
    }

    @Override
    public ReceiveBillVerifyAdvanceDTO listVerifyAdvanceByPage(ReceiveBillVerifyAdvanceDTO dto) throws Exception {
        ReceiveBillVerifyAdvanceDTO advanceBillDTO = advanceBillDao.listVerifyAdvanceByPage(dto);
        List<ReceiveBillVerifyAdvanceDTO> advanceBillDTOS = advanceBillDTO.getList();

        List<Long> advanceIds = new ArrayList<>();

        for (ReceiveBillVerifyAdvanceDTO billDTO : advanceBillDTOS) {
            advanceIds.add(billDTO.getAdvanceId());
        }

        Map<Long, AdvanceBillVerifyItemModel> verifyDateMap = new HashMap<>();
        Map<Long, BigDecimal> advanceVerifyAmountMap = new HashMap<>();
        if (!advanceIds.isEmpty()) {

            //预收款单的核销收款的最新时间
            List<AdvanceBillVerifyItemModel> verifyModelByAdvanceIds = advanceBillVerifyItemDao.getLatestVerifyModelByAdvanceIds(advanceIds);

            for (AdvanceBillVerifyItemModel verifyItemModel : verifyModelByAdvanceIds) {
                verifyDateMap.put(verifyItemModel.getAdvanceId(), verifyItemModel);
            }

            //预收单的已核销金额
            List<Map<String, Object>> verifyPriceList = receiveBillVerifyItemDao.getTotalVerifyPriceByAdvanceId(advanceIds);

            for (Map<String, Object> map : verifyPriceList) {
                if (map == null) {
                    continue;
                }
                Long advanceId = (Long) map.get("advanceId");
                BigDecimal advanceAmount = (BigDecimal) map.get("price");
                advanceVerifyAmountMap.put(advanceId, advanceAmount);
            }

        }

        for (ReceiveBillVerifyAdvanceDTO billDTO : advanceBillDTOS) {

            billDTO.setBeVerifyAmount(billDTO.getAmount());
            AdvanceBillVerifyItemModel verifyItemModel = verifyDateMap.get(billDTO.getAdvanceId());
            BigDecimal verifyAmount = advanceVerifyAmountMap.get(billDTO.getAdvanceId());
            if (verifyItemModel != null) {
                billDTO.setReceiveDate(new Timestamp(verifyItemModel.getVerifyDate().getTime()));

                ReceiveCloseAccountsModel model = new ReceiveCloseAccountsModel();
                model.setMonth(TimeUtils.formatMonth(verifyItemModel.getVerifyDate()));
                model.setBuId(billDTO.getBuId());
                model.setMerchantId(billDTO.getMerchantId());
                model.setStatus(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_029);
                ReceiveCloseAccountsModel latestModel = receiveCloseAccountsDao.getLatestByModel(model);

                if (latestModel != null) {
                    billDTO.setVerifyMonth(latestModel.getMonth());
                }
            }

            if (verifyAmount != null) {
                BigDecimal beVerifyAmount = billDTO.getAmount().subtract(verifyAmount);
                billDTO.setBeVerifyAmount(beVerifyAmount);
            }
        }
        advanceBillDTO.setList(advanceBillDTOS);
        return advanceBillDTO;
    }

    @Override
    public Map<String, Object> updateVoucher(List<Long> ids) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<AdvanceBillModel> advanceBillDTOS = advanceBillDao.listByIds(ids);
        List<String> billCodeList = new ArrayList<>();
        for (AdvanceBillModel advanceBillDTO : advanceBillDTOS) {
            billCodeList.add(advanceBillDTO.getCode());
        }
        String billCodes = org.apache.commons.lang3.StringUtils.join(billCodeList.toArray(), ",");
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("datasource", "1");
        body.put("billCodes", billCodes);
        JSONObject json = JSONObject.fromObject(body);
        System.out.println(json.toString());
        SendResult result =rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_ADVANCE_BILL_VOUCHER_BACKFILL.getTopic(), MQOrderEnum.TIMER_ADVANCE_BILL_VOUCHER_BACKFILL.getTags());
        System.out.println(result);
        if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
            resultMap.put("code", "01");
            resultMap.put("message", "更新凭证信息消息发送失败");
            return resultMap;
        }
        resultMap.put("code", "00");
        resultMap.put("message", "成功");
        return resultMap;
    }


    private String toNullStr(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str;
    }




}
