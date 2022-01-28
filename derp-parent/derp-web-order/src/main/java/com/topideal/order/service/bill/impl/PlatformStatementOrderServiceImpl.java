package com.topideal.order.service.bill.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.PlatformStatementItemDao;
import com.topideal.dao.bill.PlatformStatementOrderDao;
import com.topideal.dao.bill.ReceiveBillDao;
import com.topideal.dao.bill.ReceiveBillInvoiceDao;
import com.topideal.dao.common.FileTempDao;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.vo.bill.PlatformStatementOrderModel;
import com.topideal.entity.vo.bill.ReceiveBillInvoiceModel;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import com.topideal.entity.vo.bill.ReceiveBillOperateModel;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.order.service.bill.PlatformStatementOrderService;
import com.topideal.order.service.sale.impl.OrderServiceImpl;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.tools.ExcelConvertHTMLUtils;
import net.sf.json.JSONObject;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlatformStatementOrderServiceImpl implements PlatformStatementOrderService{

    protected Logger LOGGER = LoggerFactory.getLogger(PlatformStatementOrderServiceImpl.class);

    private static final String[] KEYS = {"billCode", "month", "customerName", "typeLabel", "poNo", "barcode", "goodsName", "settlementNum", "settlementAmount", "settlementAmountRmb", "currency", "rate"} ;

    private static final String[] COLS = {"平台结算单号", "账单月份", "客户", "类型", "PO号", "商品条码", "商品名称", "结算数量", "结算金额(本币)", "结算金额(RMB)", "币种", "汇率"} ;

    @Autowired
	private PlatformStatementOrderDao platformStatementOrderDao ;
	@Autowired
	private PlatformStatementItemDao platformStatementItemDao ;
    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private ReceiveBillInvoiceDao receiveBillInvoiceDao;
    @Autowired
    private FileTempDao fileTempDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;

	@Override
	public PlatformStatementOrderDTO listPlatformStatementOrder(PlatformStatementOrderDTO dto) throws SQLException {
        PlatformStatementOrderDTO  platformStatementOrderDTO = platformStatementOrderDao.getListByPage(dto);
        List<PlatformStatementOrderDTO> platformStatementOrderDTOS = platformStatementOrderDTO.getList();

        for (PlatformStatementOrderDTO orderDTO : platformStatementOrderDTOS) {
            if (orderDTO.getInvoiceId() != null) {
                ReceiveBillInvoiceModel receiveBillInvoiceModel = receiveBillInvoiceDao.searchById(orderDTO.getInvoiceId());
                orderDTO.setInvoicePath(receiveBillInvoiceModel.getInvoicePath());
            }
        }

        platformStatementOrderDTO.setList(platformStatementOrderDTOS);
		return platformStatementOrderDTO;
	}

	@Override
	public PlatformStatementOrderDTO getDetails(Long id) {
		return platformStatementOrderDao.searchDTOById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PlatformStatementItemDTO listPlatformStatementItem(PlatformStatementItemDTO dto) throws SQLException {
		
		dto = platformStatementItemDao.getListByPage(dto);
		
		List<PlatformStatementItemDTO> itemList = (List<PlatformStatementItemDTO>)dto.getList() ;
		
		for (PlatformStatementItemDTO item : itemList) {
			
			if(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_11.equals(item.getType())) {
				item.setTypeLabel(item.getSettlementConfigName());
			}
			
		}
		
		dto.setList(itemList);
		
		return dto;
	}

	@Override
	public String createPlatformSettlementOrderExcel(FileTaskMongo task, String basePath) throws Exception {

        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> paramMap = jsonData;
        Integer merchantId = (Integer) paramMap.get("merchantId");
        String billCode = (String) paramMap.get("billCode");
        String customerType = (String) paramMap.get("customerType");
        String month = (String) paramMap.get("month");
        String isInvoice = (String) paramMap.get("isInvoice");
        String receiveCode = (String) paramMap.get("receiveCode");
        String ids = (String) paramMap.get("ids");

        PlatformStatementOrderExportDTO dto = new PlatformStatementOrderExportDTO();
        dto.setMerchantId(Long.valueOf(merchantId));
        dto.setBillCode(billCode);
        dto.setCustomerType(customerType);
        dto.setMonth(month);
        dto.setIsInvoice(isInvoice);
        dto.setReceiveCode(receiveCode);
        dto.setIds(ids);

        basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + System.currentTimeMillis() + UUID.randomUUID() ;

        LOGGER.info("--------------------平台结算单生成Excel---开始----------------------");

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

	    Integer exportItemNum = platformStatementOrderDao.countExportNum(dto);

        int pageSize = 5000; //每页5000
        int maxSize = 300000; //每个文件存放最大记录数

        for (int i = 0; i < Math.ceil((float) exportItemNum / maxSize); i++) {

            List<Map<String, Object>> exportItemList = new ArrayList<>();

            Integer total = exportItemNum > maxSize * (i + 1) ? maxSize * (i + 1) : exportItemNum;

            List<PlatformStatementOrderExportDTO> exportList = new ArrayList<>();

            for (int j = maxSize * i; j < total; ) {
                int pageSub = (j + pageSize) < total ? (j + pageSize) : total;
                dto.setBegin(j);
                dto.setPageSize(pageSize);
                List<PlatformStatementOrderExportDTO> exportOrders = platformStatementOrderDao.getExportOrders(dto);

                for (PlatformStatementOrderExportDTO platformStatementOrderExportDTO : exportOrders) {

                    if(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_11.equals(platformStatementOrderExportDTO.getType())) {
                        platformStatementOrderExportDTO.setTypeLabel(platformStatementOrderExportDTO.getSettlementConfigName());
                    }

                }

                exportList.addAll(exportOrders);
                j = pageSub;
            }

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList("平台结算单", COLS, KEYS, exportList);
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
            exportItemList.clear();
        }
		return basePath;
	}

    @Override
    public List<PlatformStatementOrderDTO> listPlatformStatementOrderByIds(List<Long> ids) throws Exception {
        return platformStatementOrderDao.listByIds(ids);
    }

    @Override
    public Map<String, String> validate(List<Long> ids) throws SQLException {
        Map<String, String> resultMap = new HashMap<>();
        if (ids.size() > 1) {
            resultMap.put("code", "01");
            resultMap.put("msg", "只能选择一单进行开票！");
            return resultMap;
        }
        for (int i = 0; i < ids.size(); i++) {
            PlatformStatementOrderModel platformStatementOrderModel = platformStatementOrderDao.searchById(ids.get(i));
            if (DERP_ORDER.PLATFORM_STATEMENT_IS_INVOICE_1.equals(platformStatementOrderModel.getIsInvoice())) {
                resultMap.put("code", "01");
                resultMap.put("msg", "仅对标识为“未开票”的平台结算单进行开票！");
                return resultMap;
            }
            //查询该平台结算单是否存在关联发票号码
            ReceiveBillModel receiveBillModel = new ReceiveBillModel();
            receiveBillModel.setRelCode(platformStatementOrderModel.getBillCode());
            List<ReceiveBillModel> billModel = receiveBillDao.list(receiveBillModel);
            for (ReceiveBillModel model : billModel) {
                if (!DERP.DEL_CODE_006.equals(model.getBillStatus()) && !(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00.equals(model.getInvoiceStatus())
                || DERP_ORDER.RECEIVEBILL_INVOICESTATUS_02.equals(model.getInvoiceStatus()))) {
                    resultMap.put("code", "01");
                    resultMap.put("msg", "该平台结算单:" + platformStatementOrderModel.getBillCode()
                            + "对应的应收账单：" + model.getCode() + "已开票，不能再重复开票");
                    return resultMap;
                }
            }
        }

        resultMap.put("code", "00");
        resultMap.put("msg", "校验通过");
        return resultMap;
    }

    @Override
    public Map<String, String> saveToCReceiveBill(List<Long> ids, User user) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
	    List<PlatformStatementOrderDTO> orderDTOS = platformStatementOrderDao.listByIds(ids);
	    List<String> keyList = new ArrayList<>();
	    for (PlatformStatementOrderDTO dto : orderDTOS) {
	        if (!DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_0.equals(dto.getIsCreateReceive())) {
                resultMap.put("code", "01");
                resultMap.put("msg", "生成ToC应收账单必须为“未创建应收”的！");
                return resultMap;
            }
	        String key = dto.getMerchantId() + "_" + dto.getCustomerId() + "-" + dto.getCustomerType()
                    + "_" + dto.getShopCode() + "_" + dto.getBuId() + "_" + dto.getCurrency() + "_" + dto.getMonth();
	        if (keyList.isEmpty()) {
                keyList.add(key);
                continue;
            }

	        if (!keyList.contains(key)) {
                resultMap.put("code", "01");
                resultMap.put("msg", "必须相同“公司+平台+相同店铺+相同事业部+相同结算币种+相同月份”维度生成ToC应收账单！");
                return resultMap;
            }
        }
	    //更新平台结算单的是否创建应收状态为“生成中”
        platformStatementOrderDao.batchUpdate(ids, "", DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_2);
        //发送mq消息
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("billIds", StringUtils.join(ids.toArray(), ","));
        body.put("userId", user.getId());
        body.put("userName", user.getName());
        JSONObject json = JSONObject.fromObject(body);
        SendResult result =rocketMQProducer.send(json.toString(), MQOrderEnum.TOC_RECEIVE_BILL_GENERATE_BY_STATEMENT.getTopic(),
                MQOrderEnum.TOC_RECEIVE_BILL_GENERATE_BY_STATEMENT.getTags());
        if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
            resultMap.put("code", "01");
            resultMap.put("message", "生成ToC应收账单消息发送失败");
            return resultMap;
        }

        resultMap.put("code", "00");
        resultMap.put("message", "成功");
        return resultMap;
    }

    @Override
    public String generateInvoiceHtml(Long tempId, String ids, String invoiceSource, User user) throws Exception {
        FileTempModel fileTempModel = fileTempDao.searchById(tempId);

        if (fileTempModel == null) {
            throw new RuntimeException("发票模板不存在！");
        }

        if (org.apache.commons.lang.StringUtils.isBlank(fileTempModel.getToUrl())) {
            throw new RuntimeException("发票模板地址不能为空！");
        }

        List<Long> billIds = StrUtils.parseIds(ids);

        List<PlatformStatementOrderDTO> billDTOS = platformStatementOrderDao.listByIds(billIds);

        String relCodes = billDTOS.stream().map(PlatformStatementOrderDTO::getBillCode).collect(Collectors.joining());

        Map<String, Object> params = new HashMap<>();
        params.put("customerid", billDTOS.get(0).getCustomerId());
        params.put("cusType", "1");
        CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(params);

        Map<String, Object> param = new HashMap<>();
        param.put("merchantId", user.getMerchantId());
        MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(param);

        InvoiceTemplateDTO templateDTO = new InvoiceTemplateDTO();
        templateDTO.setFileTempId(tempId);
        templateDTO.setFileTempCode(fileTempModel.getCode());
        templateDTO.setIds(ids);
        templateDTO.setCurrency(billDTOS.get(0).getCurrency());
        templateDTO.setCodes(relCodes);
        templateDTO.setRelCodes(relCodes);
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
        templateDTO.setInvoiceStatus(invoiceSource);


        if (fileTempModel.getToUrl().contains("WEIPIN")) {//唯品发票取值
            listApiWPItemInfos(billIds, templateDTO);
        } else {
            listApiItemInfos(billIds, templateDTO);
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

    @Override
    public List<Map<String, Object>> listInvoiceItemInfos(List<Long> ids) throws Exception {
        List<Map<String, Object>> mapList = new LinkedList<>();
        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal totalAllNum = new BigDecimal("0");
        BigDecimal totalAllPrice = new BigDecimal("0");
        List<Map<String, Object>> itemList = platformStatementItemDao.listInvoiceItem(ids, null);
        for (int i = 0; i < itemList.size() ; i++) {
            Map<String, Object> map = itemList.get(i);
            BigDecimal totalPrice = map.get("totalPrice") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
            String goodsNo = (String) map.get("goodsNo");
            map.put("barcode", goodsNo);
            map.put("unit", "");
            if (totalNum.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal price = totalPrice.divide(totalNum,2, BigDecimal.ROUND_HALF_UP);
                map.put("price", StrUtils.doubleFormatString(price.doubleValue()));
                totalAllPrice = totalAllPrice.add(price);
            }
            totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
            map.put("totalPrice",StrUtils.doubleFormatString(totalPrice.doubleValue()));
            map.put("totalNum",StrUtils.intFormatString(totalNum.intValue()));
            map.put("index",i+1);
            totalAllAmount = totalAllAmount.add(totalPrice);
            totalAllNum = totalAllNum.add(totalNum);
            mapList.add(map);
        }
        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("totalAllAmount",StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        totalMap.put("totalAllNum",StrUtils.intFormatString(totalAllNum.intValue()));
        totalMap.put("totalAllPrice",totalAllPrice);
        mapList.add(totalMap);
        return mapList;
    }

    @Override
    public List<Map<String, Object>> listWPInvoiceItemInfos(List<Long> ids) throws Exception {
        List<Map<String, Object>> mapList = new LinkedList<>();
        List<Map<String, Object>> typeItemList = platformStatementItemDao.listInvoiceItemByType(ids);
        Map<String, Map<String, BigDecimal>> goodsTypeMap = new HashMap<>();
        for (Map<String, Object> typeItem : typeItemList) {
            String goodsNo = (String) typeItem.get("goodsNo");
            String brandParent = (String) typeItem.get("brandParent");
            String key = goodsNo + "__" + brandParent;
            if (StringUtils.isNotBlank(key)) {
                String type = (String) typeItem.get("type");
                BigDecimal totalPrice = (BigDecimal) typeItem.get("totalPrice");
                Map<String, BigDecimal> costMap = new HashMap<>();
                if (goodsTypeMap.containsKey(key)) {
                    costMap.putAll(goodsTypeMap.get(key));
                }
                if (DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_7.equals(type)) {
                    if (costMap.containsKey("customerReturn")) {
                        BigDecimal customerReturn = costMap.get("customerReturn");
                        customerReturn = customerReturn.add(totalPrice);
                        costMap.put("customerReturn", customerReturn);
                    } else {
                        costMap.put("customerReturn", totalPrice);
                    }

                } else if (DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_8.equals(type)) {
                    if (costMap.containsKey("promotionDiscounts")) {
                        BigDecimal promotionDiscounts = costMap.get("promotionDiscounts");
                        promotionDiscounts = promotionDiscounts.add(totalPrice);
                        costMap.put("promotionDiscounts", promotionDiscounts);
                    } else {
                        costMap.put("promotionDiscounts", totalPrice);
                    }
                } else if (DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_9.equals(type)) {
                    if (costMap.containsKey("extraDiscount")) {
                        BigDecimal extraDiscount = costMap.get("extraDiscount");
                        extraDiscount = extraDiscount.add(totalPrice);
                        costMap.put("extraDiscount", extraDiscount);
                    } else {
                        costMap.put("extraDiscount", totalPrice);
                    }
                }
                goodsTypeMap.put(key, costMap);
            }
        }
        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal customerReturnAmount = new BigDecimal("0");
        BigDecimal promotionDiscountsAmount = new BigDecimal("0");
        BigDecimal extraDiscountAmount = new BigDecimal("0");
        BigDecimal customerReturnBrandAmount = new BigDecimal("0");
        BigDecimal promotionDiscountsBrandAmount = new BigDecimal("0");
        BigDecimal extraDiscountBrandAmount = new BigDecimal("0");

        List<Map<String, Object>> noMergeItemList = platformStatementItemDao.listInvoiceItem(ids, "weiping");
        Map<String, Map<String, Object>> mergeItemMap = new LinkedHashMap<>();
        List<Map<String, Object>> itemList = new ArrayList<>();
        //合并相同条码+母品牌的数据（可能商品名称存在差异）
        for (int i = 0; i < noMergeItemList.size(); i++) {
            Map<String, Object> map = noMergeItemList.get(i);
            String goodsNo = (String) map.get("goodsNo");
            String brandParent = (String) map.get("brandParent");
            String key = goodsNo + "__" + brandParent;
            if (mergeItemMap.containsKey(key)) {
                Map<String, Object> existMap = mergeItemMap.get(key);
                BigDecimal existTotalPrice = existMap.get("totalPrice") == null ? BigDecimal.ZERO : (BigDecimal) existMap.get("totalPrice");
                BigDecimal existTotalNum = existMap.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) existMap.get("totalNum");
                BigDecimal totalPrice = map.get("totalPrice") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalPrice");
                BigDecimal totalNum = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
                totalPrice = totalPrice.add(existTotalPrice);
                totalNum = totalNum.add(existTotalNum);
                map.put("totalPrice", totalPrice);
                map.put("totalNum", totalNum);
            }
            mergeItemMap.put(key, map);
        }
        for (String key : mergeItemMap.keySet()) {
            Map<String, Object> existMap = mergeItemMap.get(key);
            itemList.add(existMap);
        }
        //已统计品牌对应的商品数量
        Map<String, List<Map<String, Object>>> existBrandMap = new HashMap<>();
        for (int i = 0; i < itemList.size(); i++) {
            Map<String, Object> map = itemList.get(i);
            BigDecimal totalPrice = map.get("totalPrice") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
            String goodsNo = (String) map.get("goodsNo");
            String brandName = (String) map.get("brandParent");
            if (totalNum.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal price = totalPrice.divide(totalNum,2, BigDecimal.ROUND_HALF_UP);
                BigDecimal exactPrice = totalPrice.divide(totalNum,8, BigDecimal.ROUND_HALF_UP);
                map.put("price", StrUtils.doubleFormatString(price.doubleValue()));
                map.put("exactPrice", exactPrice);
            }
            String key = goodsNo + "__" + brandName;
            if (goodsTypeMap.containsKey(key)) {
                if (goodsTypeMap.get(key).containsKey("customerReturn")) {
                    BigDecimal tempAmount = goodsTypeMap.get(key).get("customerReturn");
                    map.put("customerReturn", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsTypeMap.get(key).containsKey("promotionDiscounts")) {
                    BigDecimal tempAmount = goodsTypeMap.get(key).get("promotionDiscounts");
                    map.put("promotionDiscounts", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsTypeMap.get(key).containsKey("extraDiscount")) {
                    BigDecimal tempAmount = goodsTypeMap.get(key).get("extraDiscount");
                    map.put("extraDiscount", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                goodsTypeMap.remove(key);
            } else {
                map.put("customerReturn", new BigDecimal("0"));
                map.put("promotionDiscounts", new BigDecimal("0"));
                map.put("extraDiscount", new BigDecimal("0"));
            }
            map.put("totalPrice", totalPrice);
            if (existBrandMap.containsKey(brandName)) {
                existBrandMap.get(brandName).add(map);
            } else {
                List<Map<String, Object>> goodsList = new ArrayList<>();
                goodsList.add(map);
                existBrandMap.put(brandName, goodsList);
            }
        }

        Map<String, List<Map<String, Object>>> noExistBrandMap = new HashMap<>(); //未统计的品牌商品信息
        //遍历未统计的品牌商品信息并小计
        for (String key : goodsTypeMap.keySet()) {
            String[] keys = key.split("__");
            Map<String, Object> map = new HashMap<>();
            map.put("goodsName", keys[0]);
            if (goodsTypeMap.get(key).containsKey("customerReturn")) {
                BigDecimal tempAmount = goodsTypeMap.get(key).get("customerReturn");
                map.put("customerReturn", tempAmount);
            }
            if (goodsTypeMap.get(key).containsKey("promotionDiscounts")) {
                BigDecimal tempAmount = goodsTypeMap.get(key).get("promotionDiscounts");
                map.put("promotionDiscounts", tempAmount);
            }
            if (goodsTypeMap.get(key).containsKey("extraDiscount")) {
                BigDecimal tempAmount = goodsTypeMap.get(key).get("extraDiscount");
                map.put("extraDiscount", tempAmount);
            }
            if (noExistBrandMap.containsKey(keys[1])) {
                noExistBrandMap.get(keys[1]).add(map);
            } else {
                List<Map<String, Object>> goodsList = new ArrayList<>();
                goodsList.add(map);
                noExistBrandMap.put(keys[1], goodsList);
            }
        }

        //遍历并合并未统计到的品牌信息
        for (String brandName : noExistBrandMap.keySet()) {
            BigDecimal totalBrandAmount = new BigDecimal("0");
            List<Map<String, Object>> goodsList = noExistBrandMap.get(brandName);
            if (existBrandMap.containsKey(brandName)) {
                List<Map<String, Object>> existGoodsList = existBrandMap.get(brandName);
                for (Map<String, Object> exist : existGoodsList) {
                    BigDecimal customerReturn = exist.get("customerReturn")==null ? new BigDecimal("0") : (BigDecimal) exist.get("customerReturn");;
                    customerReturnBrandAmount = customerReturnBrandAmount.add(customerReturn);
                    BigDecimal promotionDiscounts = exist.get("promotionDiscounts")==null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscounts");;
                    promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(promotionDiscounts);
                    BigDecimal extraDiscount = exist.get("extraDiscount")==null ? new BigDecimal("0") : (BigDecimal) exist.get("extraDiscount");;
                    extraDiscountBrandAmount = extraDiscountBrandAmount.add(extraDiscount);
                    BigDecimal totalPrice = (BigDecimal) exist.get("totalPrice");
                    totalBrandAmount = totalBrandAmount.add(totalPrice);
                    exist.put("customerReturn", StrUtils.doubleFormatString(customerReturn.doubleValue()));
                    exist.put("promotionDiscounts", StrUtils.doubleFormatString(promotionDiscounts.doubleValue()));
                    exist.put("extraDiscount", StrUtils.doubleFormatString(extraDiscount.doubleValue()));
                    exist.put("totalPrice", StrUtils.doubleFormatString(totalPrice.doubleValue()));
                    mapList.add(exist);
                }
                existBrandMap.remove(brandName);
            }
            for (int i = 0; i < goodsList.size(); i++) {
                BigDecimal totalPrice = new BigDecimal("0");
                Map<String, Object> goodsMap = goodsList.get(i);
                if (goodsMap.containsKey("customerReturn")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("customerReturn");
                    goodsMap.put("customerReturn", StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    customerReturnBrandAmount = customerReturnBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("promotionDiscounts")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("promotionDiscounts");
                    goodsMap.put("promotionDiscounts", StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("extraDiscount")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("extraDiscount");
                    goodsMap.put("extraDiscount", StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    extraDiscountBrandAmount = extraDiscountBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                totalBrandAmount = totalBrandAmount.add(totalPrice);
                goodsMap.put("totalPrice", StrUtils.doubleFormatString(totalPrice.doubleValue()));
                mapList.add(goodsMap);
            }
            //品牌小计
            Map<String, Object> brandMap = new HashMap<>();
            brandMap.put("brandName", brandName + "-合计");
            brandMap.put("totalPrice", StrUtils.doubleFormatString(totalBrandAmount.doubleValue()));
            brandMap.put("customerReturn", StrUtils.doubleFormatString(customerReturnBrandAmount.doubleValue()));
            brandMap.put("promotionDiscounts", StrUtils.doubleFormatString(promotionDiscountsBrandAmount.doubleValue()));
            brandMap.put("extraDiscount", StrUtils.doubleFormatString(extraDiscountBrandAmount.doubleValue()));
            totalAllAmount = totalAllAmount.add(totalBrandAmount);
            customerReturnAmount = customerReturnAmount.add(customerReturnBrandAmount);
            promotionDiscountsAmount = promotionDiscountsAmount.add(promotionDiscountsBrandAmount);
            extraDiscountAmount = extraDiscountAmount.add(extraDiscountBrandAmount);
            customerReturnBrandAmount = new BigDecimal("0");
            promotionDiscountsBrandAmount = new BigDecimal("0");
            extraDiscountBrandAmount = new BigDecimal("0");
            mapList.add(brandMap);
        }
        //添加已统计的品牌信息
        for (String brandName : existBrandMap.keySet()) {
            BigDecimal totalBrandAmount = new BigDecimal("0");
            List<Map<String, Object>> existGoodsList = existBrandMap.get(brandName);
            for (Map<String, Object> exist : existGoodsList) {
                BigDecimal customerReturn = exist.get("customerReturn")==null ? new BigDecimal("0") : (BigDecimal) exist.get("customerReturn");
                customerReturnBrandAmount = customerReturnBrandAmount.add(customerReturn);
                BigDecimal promotionDiscounts = exist.get("promotionDiscounts")==null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscounts");
                promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(promotionDiscounts);
                BigDecimal extraDiscount = exist.get("extraDiscount")==null ? new BigDecimal("0") : (BigDecimal) exist.get("extraDiscount");
                extraDiscountBrandAmount = extraDiscountBrandAmount.add(extraDiscount);
                BigDecimal totalPrice = (BigDecimal) exist.get("totalPrice");
                totalBrandAmount = totalBrandAmount.add(totalPrice);
                exist.put("customerReturn", StrUtils.doubleFormatString(customerReturn.doubleValue()));
                exist.put("promotionDiscounts", StrUtils.doubleFormatString(promotionDiscounts.doubleValue()));
                exist.put("extraDiscount", StrUtils.doubleFormatString(extraDiscount.doubleValue()));
                exist.put("totalPrice", StrUtils.doubleFormatString(totalPrice.doubleValue()));
                mapList.add(exist);
            }
            //品牌小计
            Map<String, Object> brandMap = new HashMap<>();
            brandMap.put("brandName", brandName + "-合计");
            brandMap.put("totalPrice", StrUtils.doubleFormatString(totalBrandAmount.doubleValue()));
            brandMap.put("customerReturn", StrUtils.doubleFormatString(customerReturnBrandAmount.doubleValue()));
            brandMap.put("promotionDiscounts", StrUtils.doubleFormatString(promotionDiscountsBrandAmount.doubleValue()));
            brandMap.put("extraDiscount", StrUtils.doubleFormatString(extraDiscountBrandAmount.doubleValue()));
            totalAllAmount = totalAllAmount.add(totalBrandAmount);
            customerReturnAmount = customerReturnAmount.add(customerReturnBrandAmount);
            promotionDiscountsAmount = promotionDiscountsAmount.add(promotionDiscountsBrandAmount);
            extraDiscountAmount = extraDiscountAmount.add(extraDiscountBrandAmount);
            customerReturnBrandAmount = new BigDecimal("0");
            promotionDiscountsBrandAmount = new BigDecimal("0");
            extraDiscountBrandAmount = new BigDecimal("0");
            mapList.add(brandMap);
        }

        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        customerReturnAmount = customerReturnAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        promotionDiscountsAmount = promotionDiscountsAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        extraDiscountAmount = extraDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("totalAllAmount", StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        totalMap.put("customerReturnAmount", StrUtils.doubleFormatString(customerReturnAmount.doubleValue()));
        totalMap.put("promotionDiscountsAmount", StrUtils.doubleFormatString(promotionDiscountsAmount.doubleValue()));
        totalMap.put("extraDiscountAmount", StrUtils.doubleFormatString(extraDiscountAmount.doubleValue()));
        mapList.add(totalMap);
        return mapList;
    }


    private void listApiWPItemInfos(List<Long> ids, InvoiceTemplateDTO templateDTO) throws Exception {

        List<InvoiceWeiPinTemplateItemDTO> wpItemList = new LinkedList<>();

        List<Map<String, Object>> typeItemList = platformStatementItemDao.listInvoiceItemByType(ids);
        Map<String, Map<String, BigDecimal>> goodsTypeMap = new HashMap<>();
        for (Map<String, Object> typeItem : typeItemList) {
            String goodsNo = (String) typeItem.get("goodsNo");
            String brandParent = (String) typeItem.get("brandParent");
            String key = goodsNo + "__" + brandParent;
            if (StringUtils.isNotBlank(key)) {
                String type = (String) typeItem.get("type");
                BigDecimal totalPrice = (BigDecimal) typeItem.get("totalPrice");
                Map<String, BigDecimal> costMap = new HashMap<>();
                if (goodsTypeMap.containsKey(key)) {
                    costMap.putAll(goodsTypeMap.get(key));
                }
                if (DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_7.equals(type)) {
                    if (costMap.containsKey("customerReturn")) {
                        BigDecimal customerReturn = costMap.get("customerReturn");
                        customerReturn = customerReturn.add(totalPrice);
                        costMap.put("customerReturn", customerReturn);
                    } else {
                        costMap.put("customerReturn", totalPrice);
                    }

                } else if (DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_8.equals(type)) {
                    if (costMap.containsKey("promotionDiscounts")) {
                        BigDecimal promotionDiscounts = costMap.get("promotionDiscounts");
                        promotionDiscounts = promotionDiscounts.add(totalPrice);
                        costMap.put("promotionDiscounts", promotionDiscounts);
                    } else {
                        costMap.put("promotionDiscounts", totalPrice);
                    }
                } else if (DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_9.equals(type)) {
                    if (costMap.containsKey("extraDiscount")) {
                        BigDecimal extraDiscount = costMap.get("extraDiscount");
                        extraDiscount = extraDiscount.add(totalPrice);
                        costMap.put("extraDiscount", extraDiscount);
                    } else {
                        costMap.put("extraDiscount", totalPrice);
                    }
                }
                goodsTypeMap.put(key, costMap);
            }
        }
        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal customerReturnAmount = new BigDecimal("0");
        BigDecimal promotionDiscountsAmount = new BigDecimal("0");
        BigDecimal extraDiscountAmount = new BigDecimal("0");
        BigDecimal customerReturnBrandAmount = new BigDecimal("0");
        BigDecimal promotionDiscountsBrandAmount = new BigDecimal("0");
        BigDecimal extraDiscountBrandAmount = new BigDecimal("0");

        List<Map<String, Object>> noMergeItemList = platformStatementItemDao.listInvoiceItem(ids, "weiping");
        Map<String, Map<String, Object>> mergeItemMap = new LinkedHashMap<>();
        List<Map<String, Object>> itemList = new ArrayList<>();
        //合并相同条码+母品牌的数据（可能商品名称存在差异）
        for (int i = 0; i < noMergeItemList.size(); i++) {
            Map<String, Object> map = noMergeItemList.get(i);
            String goodsNo = (String) map.get("goodsNo");
            String brandParent = (String) map.get("brandParent");
            String key = goodsNo + "__" + brandParent;
            if (mergeItemMap.containsKey(key)) {
                Map<String, Object> existMap = mergeItemMap.get(key);
                BigDecimal existTotalPrice = existMap.get("totalPrice") == null ? BigDecimal.ZERO : (BigDecimal) existMap.get("totalPrice");
                BigDecimal existTotalNum = existMap.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) existMap.get("totalNum");
                BigDecimal totalPrice = map.get("totalPrice") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalPrice");
                BigDecimal totalNum = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
                totalPrice = totalPrice.add(existTotalPrice);
                totalNum = totalNum.add(existTotalNum);
                map.put("totalPrice", totalPrice);
                map.put("totalNum", totalNum);
            }
            mergeItemMap.put(key, map);
        }
        for (String key : mergeItemMap.keySet()) {
            Map<String, Object> existMap = mergeItemMap.get(key);
            itemList.add(existMap);
        }
        //已统计品牌对应的商品数量
        Map<String, List<Map<String, Object>>> existBrandMap = new HashMap<>();
        for (int i = 0; i < itemList.size(); i++) {
            Map<String, Object> map = itemList.get(i);
            BigDecimal totalPrice = map.get("totalPrice") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
            String goodsNo = (String) map.get("goodsNo");
            String brandName = (String) map.get("brandParent");
            if (totalNum.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal price = totalPrice.divide(totalNum,2, BigDecimal.ROUND_HALF_UP);
                BigDecimal exactPrice = totalPrice.divide(totalNum,8, BigDecimal.ROUND_HALF_UP);
                map.put("price", StrUtils.doubleFormatString(price.doubleValue()));
                map.put("exactPrice", exactPrice);
            }
            String key = goodsNo + "__" + brandName;
            if (goodsTypeMap.containsKey(key)) {
                if (goodsTypeMap.get(key).containsKey("customerReturn")) {
                    BigDecimal tempAmount = goodsTypeMap.get(key).get("customerReturn");
                    map.put("customerReturn", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsTypeMap.get(key).containsKey("promotionDiscounts")) {
                    BigDecimal tempAmount = goodsTypeMap.get(key).get("promotionDiscounts");
                    map.put("promotionDiscounts", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsTypeMap.get(key).containsKey("extraDiscount")) {
                    BigDecimal tempAmount = goodsTypeMap.get(key).get("extraDiscount");
                    map.put("extraDiscount", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                goodsTypeMap.remove(key);
            } else {
                map.put("customerReturn", new BigDecimal("0"));
                map.put("promotionDiscounts", new BigDecimal("0"));
                map.put("extraDiscount", new BigDecimal("0"));
            }
            map.put("totalPrice", totalPrice);
            if (existBrandMap.containsKey(brandName)) {
                existBrandMap.get(brandName).add(map);
            } else {
                List<Map<String, Object>> goodsList = new ArrayList<>();
                goodsList.add(map);
                existBrandMap.put(brandName, goodsList);
            }
        }

        Map<String, List<Map<String, Object>>> noExistBrandMap = new HashMap<>(); //未统计的品牌商品信息
        //遍历未统计的品牌商品信息并小计
        for (String key : goodsTypeMap.keySet()) {
            String[] keys = key.split("__");
            Map<String, Object> map = new HashMap<>();
            map.put("goodsName", keys[0]);
            if (goodsTypeMap.get(key).containsKey("customerReturn")) {
                BigDecimal tempAmount = goodsTypeMap.get(key).get("customerReturn");
                map.put("customerReturn", tempAmount);
            }
            if (goodsTypeMap.get(key).containsKey("promotionDiscounts")) {
                BigDecimal tempAmount = goodsTypeMap.get(key).get("promotionDiscounts");
                map.put("promotionDiscounts", tempAmount);
            }
            if (goodsTypeMap.get(key).containsKey("extraDiscount")) {
                BigDecimal tempAmount = goodsTypeMap.get(key).get("extraDiscount");
                map.put("extraDiscount", tempAmount);
            }
            if (noExistBrandMap.containsKey(keys[1])) {
                noExistBrandMap.get(keys[1]).add(map);
            } else {
                List<Map<String, Object>> goodsList = new ArrayList<>();
                goodsList.add(map);
                noExistBrandMap.put(keys[1], goodsList);
            }
        }

        //遍历并合并未统计到的品牌信息
        for (String brandName : noExistBrandMap.keySet()) {
            BigDecimal totalBrandAmount = new BigDecimal("0");
            List<Map<String, Object>> goodsList = noExistBrandMap.get(brandName);
            if (existBrandMap.containsKey(brandName)) {
                List<Map<String, Object>> existGoodsList = existBrandMap.get(brandName);
                for (Map<String, Object> exist : existGoodsList) {
                    BigDecimal customerReturn = exist.get("customerReturn")==null ? new BigDecimal("0") : (BigDecimal) exist.get("customerReturn");;
                    customerReturnBrandAmount = customerReturnBrandAmount.add(customerReturn);
                    BigDecimal promotionDiscounts = exist.get("promotionDiscounts")==null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscounts");;
                    promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(promotionDiscounts);
                    BigDecimal extraDiscount = exist.get("extraDiscount")==null ? new BigDecimal("0") : (BigDecimal) exist.get("extraDiscount");;
                    extraDiscountBrandAmount = extraDiscountBrandAmount.add(extraDiscount);
                    BigDecimal totalPrice = (BigDecimal) exist.get("totalPrice");
                    totalBrandAmount = totalBrandAmount.add(totalPrice);

                    BigDecimal totalNum = exist.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) exist.get("totalNum");
                    String goodsName = (String) exist.get("goodsName");
                    String price = (String) exist.get("price");

                    InvoiceWeiPinTemplateItemDTO weiPinTemplateItemDTO = new InvoiceWeiPinTemplateItemDTO();
                    weiPinTemplateItemDTO.setTotalNum(String.valueOf(totalNum));
                    weiPinTemplateItemDTO.setPrice(price);
                    weiPinTemplateItemDTO.setGoodsName(goodsName);
                    weiPinTemplateItemDTO.setCustomerReturn(StrUtils.doubleFormatString(customerReturn.doubleValue()));
                    weiPinTemplateItemDTO.setPromotionDiscounts(StrUtils.doubleFormatString(promotionDiscounts.doubleValue()));
                    weiPinTemplateItemDTO.setExtraDiscount(StrUtils.doubleFormatString(extraDiscount.doubleValue()));
                    weiPinTemplateItemDTO.setTotalPrice(StrUtils.doubleFormatString(totalPrice.doubleValue()));

                    wpItemList.add(weiPinTemplateItemDTO);
                }
                existBrandMap.remove(brandName);
            }
            for (int i = 0; i < goodsList.size(); i++) {
                BigDecimal totalPrice = new BigDecimal("0");
                Map<String, Object> goodsMap = goodsList.get(i);

                BigDecimal totalNum = goodsMap.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) goodsMap.get("totalNum");
                String goodsName = (String) goodsMap.get("goodsName");
                String price = (String) goodsMap.get("price");
                InvoiceWeiPinTemplateItemDTO weiPinTemplateItemDTO = new InvoiceWeiPinTemplateItemDTO();
                weiPinTemplateItemDTO.setTotalNum(String.valueOf(totalNum));
                weiPinTemplateItemDTO.setPrice(price);
                weiPinTemplateItemDTO.setGoodsName(goodsName);

                if (goodsMap.containsKey("customerReturn")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("customerReturn");
                    weiPinTemplateItemDTO.setCustomerReturn(StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    customerReturnBrandAmount = customerReturnBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("promotionDiscounts")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("promotionDiscounts");
                    weiPinTemplateItemDTO.setPromotionDiscounts(StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("extraDiscount")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("extraDiscount");
                    weiPinTemplateItemDTO.setExtraDiscount(StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    extraDiscountBrandAmount = extraDiscountBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                totalBrandAmount = totalBrandAmount.add(totalPrice);
                weiPinTemplateItemDTO.setTotalPrice(StrUtils.doubleFormatString(totalPrice.doubleValue()));
                wpItemList.add(weiPinTemplateItemDTO);
            }
            //品牌小计
            InvoiceWeiPinTemplateItemDTO weiPinTemplateItemDTO = new InvoiceWeiPinTemplateItemDTO();
            weiPinTemplateItemDTO.setGoodsName(brandName + "-合计");
            weiPinTemplateItemDTO.setCustomerReturn(StrUtils.doubleFormatString(customerReturnBrandAmount.doubleValue()));
            weiPinTemplateItemDTO.setPromotionDiscounts(StrUtils.doubleFormatString(promotionDiscountsBrandAmount.doubleValue()));
            weiPinTemplateItemDTO.setExtraDiscount(StrUtils.doubleFormatString(extraDiscountBrandAmount.doubleValue()));
            weiPinTemplateItemDTO.setTotalPrice(StrUtils.doubleFormatString(totalBrandAmount.doubleValue()));

            wpItemList.add(weiPinTemplateItemDTO);

            totalAllAmount = totalAllAmount.add(totalBrandAmount);
            customerReturnAmount = customerReturnAmount.add(customerReturnBrandAmount);
            promotionDiscountsAmount = promotionDiscountsAmount.add(promotionDiscountsBrandAmount);
            extraDiscountAmount = extraDiscountAmount.add(extraDiscountBrandAmount);
            customerReturnBrandAmount = new BigDecimal("0");
            promotionDiscountsBrandAmount = new BigDecimal("0");
            extraDiscountBrandAmount = new BigDecimal("0");
        }
        //添加已统计的品牌信息
        for (String brandName : existBrandMap.keySet()) {
            BigDecimal totalBrandAmount = new BigDecimal("0");
            List<Map<String, Object>> existGoodsList = existBrandMap.get(brandName);
            for (Map<String, Object> exist : existGoodsList) {
                BigDecimal customerReturn = exist.get("customerReturn")==null ? new BigDecimal("0") : (BigDecimal) exist.get("customerReturn");
                customerReturnBrandAmount = customerReturnBrandAmount.add(customerReturn);
                BigDecimal promotionDiscounts = exist.get("promotionDiscounts")==null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscounts");
                promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(promotionDiscounts);
                BigDecimal extraDiscount = exist.get("extraDiscount")==null ? new BigDecimal("0") : (BigDecimal) exist.get("extraDiscount");
                extraDiscountBrandAmount = extraDiscountBrandAmount.add(extraDiscount);
                BigDecimal totalPrice = (BigDecimal) exist.get("totalPrice");
                totalBrandAmount = totalBrandAmount.add(totalPrice);

                BigDecimal totalNum = exist.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) exist.get("totalNum");
                String goodsName = (String) exist.get("goodsName");
                String price = (String) exist.get("price");

                InvoiceWeiPinTemplateItemDTO weiPinTemplateItemDTO = new InvoiceWeiPinTemplateItemDTO();
                weiPinTemplateItemDTO.setTotalNum(String.valueOf(totalNum));
                weiPinTemplateItemDTO.setPrice(price);
                weiPinTemplateItemDTO.setGoodsName(goodsName);
                weiPinTemplateItemDTO.setCustomerReturn(StrUtils.doubleFormatString(customerReturn.doubleValue()));
                weiPinTemplateItemDTO.setPromotionDiscounts(StrUtils.doubleFormatString(promotionDiscounts.doubleValue()));
                weiPinTemplateItemDTO.setExtraDiscount(StrUtils.doubleFormatString(extraDiscount.doubleValue()));
                weiPinTemplateItemDTO.setTotalPrice(StrUtils.doubleFormatString(totalPrice.doubleValue()));

                wpItemList.add(weiPinTemplateItemDTO);
            }

            InvoiceWeiPinTemplateItemDTO weiPinTemplateItemDTO = new InvoiceWeiPinTemplateItemDTO();
            weiPinTemplateItemDTO.setGoodsName(brandName + "-合计");
            weiPinTemplateItemDTO.setCustomerReturn(StrUtils.doubleFormatString(customerReturnBrandAmount.doubleValue()));
            weiPinTemplateItemDTO.setPromotionDiscounts(StrUtils.doubleFormatString(promotionDiscountsBrandAmount.doubleValue()));
            weiPinTemplateItemDTO.setExtraDiscount(StrUtils.doubleFormatString(extraDiscountBrandAmount.doubleValue()));
            weiPinTemplateItemDTO.setTotalPrice(StrUtils.doubleFormatString(totalBrandAmount.doubleValue()));

            wpItemList.add(weiPinTemplateItemDTO);
            totalAllAmount = totalAllAmount.add(totalBrandAmount);
            customerReturnAmount = customerReturnAmount.add(customerReturnBrandAmount);
            promotionDiscountsAmount = promotionDiscountsAmount.add(promotionDiscountsBrandAmount);
            extraDiscountAmount = extraDiscountAmount.add(extraDiscountBrandAmount);
            customerReturnBrandAmount = new BigDecimal("0");
            promotionDiscountsBrandAmount = new BigDecimal("0");
            extraDiscountBrandAmount = new BigDecimal("0");
        }

        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        customerReturnAmount = customerReturnAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        promotionDiscountsAmount = promotionDiscountsAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        extraDiscountAmount = extraDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

        templateDTO.setWpItemList(wpItemList);
        templateDTO.setTotalAllAmount(StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        templateDTO.setCustomerReturnAll(StrUtils.doubleFormatString(customerReturnAmount.doubleValue()));
        templateDTO.setPromotionDiscountsAll(StrUtils.doubleFormatString(promotionDiscountsAmount.doubleValue()));
        templateDTO.setExtraDiscountAll(StrUtils.doubleFormatString(extraDiscountAmount.doubleValue()));
    }

    private void listApiItemInfos(List<Long> ids, InvoiceTemplateDTO templateDTO) throws Exception {
        List<InvoiceTemplateItemDTO> templateItemDTOS = new LinkedList<>();

        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal totalAllNum = new BigDecimal("0");
        BigDecimal totalAllPrice = new BigDecimal("0");
        List<Map<String, Object>> itemList = platformStatementItemDao.listInvoiceItem(ids, null);
        for (int i = 0; i < itemList.size() ; i++) {
            Map<String, Object> map = itemList.get(i);
            BigDecimal totalPrice = map.get("totalPrice") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
            InvoiceTemplateItemDTO itemDTO = new InvoiceTemplateItemDTO();
            String goodsNo = (String) map.get("goodsNo");
            map.put("barcode", goodsNo);
            map.put("unit", "");
            if (totalNum.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal price = totalPrice.divide(totalNum,2, BigDecimal.ROUND_HALF_UP);
                itemDTO.setPrice(StrUtils.doubleFormatString(price.doubleValue()));
                totalAllPrice = totalAllPrice.add(price);
            }
            totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);

            itemDTO.setBarcode(goodsNo);
            itemDTO.setUnit("");
            itemDTO.setTotalNum(StrUtils.intFormatString(totalNum.intValue()));
            itemDTO.setTotalPrice(StrUtils.intFormatString(totalPrice.intValue()));
            itemDTO.setIndex(String.valueOf(i+1));
            templateItemDTOS.add(itemDTO);

            totalAllAmount = totalAllAmount.add(totalPrice);
            totalAllNum = totalAllNum.add(totalNum);

        }
        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        templateDTO.setTotalAllAmount(StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        templateDTO.setTotalAllNum(StrUtils.intFormatString(totalAllNum.intValue()));
        templateDTO.setItemList(templateItemDTOS);
    }
}
