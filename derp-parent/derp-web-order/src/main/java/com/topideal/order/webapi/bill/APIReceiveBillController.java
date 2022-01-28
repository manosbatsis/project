package com.topideal.order.webapi.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.vo.bill.ReceiveBillAuditItemModel;
import com.topideal.entity.vo.bill.ReceiveBillOperateModel;
import com.topideal.entity.vo.bill.ReceiveBillVerifyItemModel;
import com.topideal.order.service.bill.*;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.webapi.bill.form.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 应收账单
 * @Author: Chen Yiluan
 * @Date: 2020/07/27 14:53
 **/
@RestController
@RequestMapping("/webapi/order/receiveBill")
@Api(tags = "To B应收账单")
public class APIReceiveBillController {

    @Autowired
    private ReceiveBillService receiveBillService;
    @Autowired
    private ReceiveBillCostItemService receiveBillCostItemService;
    @Autowired
    private ReceiveBillVerifyItemService receiveBillVerifyItemService;
    @Autowired
    private ReceiveBillOperateService receiveBillOperateService;
    @Autowired
    private AdvanceBillService advanceBillService;


    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listReceiveBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<ReceiveBillDTO> listReceiveBill(ReceiveBillForm form) {
        try {
            ReceiveBillDTO dto = new ReceiveBillDTO();
            User user = ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form, dto);
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            receiveBillService.listReceiveBillByPage(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "新增页面——分页数据")
    @PostMapping(value = "/listAddOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<ReceiveBillDTO> listAddOrder(ReceiveBillForm form) {
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils()
                    .addObject(form.getOrderType())
                    .empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            ReceiveBillDTO dto = new ReceiveBillDTO();
            BeanUtils.copyProperties(form, dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            ReceiveBillDTO receiveBillDTO = receiveBillService.listAddOrderByPage(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, receiveBillDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "新增页面——保存应收账单")
    @PostMapping(value = "/saveReceiveBill.asyn")
    private ResponseBean saveReceiveBill(@RequestBody ReceiveBillForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());

            // 响应结果集
            Map<String, String> result = receiveBillService.verifyAndSaveReceiveBill(form.getOrderLists(), user);
            String code = result.get("code");
            String message = result.get("message");
            String billCode = result.get("billCode");

            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), message);
            }

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, billCode);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "获取应收账单明细")
    @PostMapping(value = "/getDetail.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应收账单id", required = true)})
    public ResponseBean getDetail(String token, Long id) {

        try {
            String month = null;
            ReceiveBillDTO receiveBillModel = receiveBillService.searchDTOById(id);
            if (receiveBillModel.getBillDate() != null) {
                month = TimeUtils.formatMonth(receiveBillModel.getBillDate()).replace("-", "年");
            }
            BigDecimal totalPrice = new BigDecimal("0");
            BigDecimal totalNum = new BigDecimal("0");
            BigDecimal totalAllTax = new BigDecimal("0");
            BigDecimal totalAllTaxAmount = new BigDecimal("0");
            BigDecimal itemTotalPrice = new BigDecimal("0");
            BigDecimal itemTotalNum = new BigDecimal("0");
            BigDecimal costTotalPrice = new BigDecimal("0");
            BigDecimal costTotalNum = new BigDecimal("0");
            //应收结算
            List<Map<String, Object>> receiveMap = receiveBillService.receiveTotalById(id);
            for (Map<String, Object> map : receiveMap) {
                BigDecimal price = (BigDecimal) map.get("totalPrice");
                BigDecimal num = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
                BigDecimal totalTaxAmount = map.get("totalTaxAmount") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalTaxAmount");
                BigDecimal totalTax = map.get("totalTax") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalTax");
                if (price != null) {
                    totalPrice = totalPrice.add(price);
                    itemTotalPrice = itemTotalPrice.add(price);
                }
                totalNum = totalNum.add(num);
                totalAllTax = totalAllTax.add(totalTax);
                totalAllTaxAmount = totalAllTaxAmount.add(totalTaxAmount);
                itemTotalNum = itemTotalNum.add(num);
            }
            //抵扣费用
            List<ReceiveBillCostItemDTO> deductionMap = receiveBillService.deductionTotalById(id);
            for (ReceiveBillCostItemDTO itemModel : deductionMap) {
                BigDecimal price = itemModel.getPrice();
                if (price != null) {
                    totalPrice = totalPrice.add(price);
                    costTotalPrice = costTotalPrice.add(price);
                }
                if (itemModel.getTax() != null) {
                    totalAllTax = totalAllTax.add(itemModel.getTax());
                }

                if (itemModel.getTaxAmount() != null) {
                    totalAllTaxAmount = totalAllTaxAmount.add(itemModel.getTaxAmount());
                }
                totalNum = totalNum.add(new BigDecimal(itemModel.getNum()));
                costTotalNum = costTotalNum.add(new BigDecimal(itemModel.getNum()));
            }

            //审核记录
            List<ReceiveBillOperateModel> receiveBillOperateModels = receiveBillOperateService.listByBillId(id);
            //核算记录
            List<ReceiveBillVerifyItemModel> verifyItemModels = receiveBillVerifyItemService.listByBillId(id);
            BigDecimal alreadyPrice = new BigDecimal("0");
            for (ReceiveBillVerifyItemModel itemModel : verifyItemModels) {
                BigDecimal price = itemModel.getPrice();
                alreadyPrice = alreadyPrice.add(price);
            }

            List<ReceiveBillItemDTO> itemDTOS = receiveBillService.listReceiveItem(receiveBillModel);
            List<ReceiveBillCostItemDTO> costItemDTOS = receiveBillService.listReceiveCostItem(receiveBillModel);

            ReceiveBillDetailDTO detailDTO = new ReceiveBillDetailDTO();
            detailDTO.setMonth(month);
            detailDTO.setBill(receiveBillModel);
            detailDTO.setTotalNum(totalNum);
            detailDTO.setTotalPrice(totalPrice);
            detailDTO.setTotalPriceLabel(NumberToCN.number2CNMontrayUnit(totalPrice));
            detailDTO.setReceiveMap(receiveMap);
            detailDTO.setDeductionMap(deductionMap);
            detailDTO.setReceiveBillOperateModels(receiveBillOperateModels);
            detailDTO.setVerifyItemModels(verifyItemModels);
            detailDTO.setItemTotalNum(itemTotalNum);
            detailDTO.setItemTotalPrice(itemTotalPrice);
            detailDTO.setCostTotalPrice(costTotalPrice);
            detailDTO.setCostTotalNum(costTotalNum);
            detailDTO.setAlreadyPrice(alreadyPrice);
            detailDTO.setTotalTax(totalAllTax);
            detailDTO.setTotalTaxAmount(totalAllTaxAmount);
            detailDTO.setItemDTOS(itemDTOS);
            detailDTO.setCostItemDTOS(costItemDTOS);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, detailDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "提交")
    @PostMapping(value = "/submitReceiveBill.asyn")
    private ResponseBean submitReceiveBill(@RequestBody ReceiveBillSubmitForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            // 响应结果集
            Map<String, String> resultMap = receiveBillService.confirmReceiveBill(form, user);
            String code = resultMap.get("code");
            String message = resultMap.get("message");
            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "获取补扣款明细")
    @PostMapping(value = "/getFreeAddItems.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应收账单id", required = true)})
    public ResponseBean getFreeAddItems(String token, Long id){
        try {
            ReceiveBillDTO dto = receiveBillService.searchDTOById(id);
            //费用明细
            List<ReceiveBillCostItemDTO> receiveBillCostItemDTOS = receiveBillCostItemService.itemListGroupByBillId(id, true);
            List<SelectBean> brandSelectBeans = new ArrayList<>();

            dto.setReceiveBillCostItemDTOS(receiveBillCostItemDTOS);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }

    /**
     * 保存补扣款信息
     */
    @ApiOperation(value = "保存补扣款信息")
    @PostMapping("/saveReceiveBillCost.asyn")
    public ResponseBean saveReceiveBillCost(@RequestBody ReceiveBillForm form) {
        try {
            Map<String, String> retMap = receiveBillCostItemService.saveApiReceiveBillCost(form);

            if ("01".equals(retMap.get("code"))) {
                String msg = (String) retMap.get("msg");
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), msg);
            }

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }


    @ApiOperation(value = "获取核销明细")
    @PostMapping(value = "/getVerifyItems.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应收账单id", required = true)})
    public ResponseBean getVerifyItems(String token, Long id) {
        try {
            //核算记录
            List<ReceiveBillVerifyItemModel> verifyItemModels = receiveBillVerifyItemService.listByBillId(id);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, verifyItemModels);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "保存核销信息")
    @PostMapping(value = "/saveReceiveBillVerify.asyn")
    public ResponseBean saveReceiveBillVerify(@RequestBody ReceiveBillVerifyForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());

            Map<String, String> resultMap = receiveBillVerifyItemService.saveAPIVerifyItem(form, user);
            String code = resultMap.get("code");
            String message = resultMap.get("message");
            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "审核")
    @PostMapping(value = "/auditItem.asyn")
    public ResponseBean auditItem(@RequestBody ReceiveBillAuditForm form) {
        Map<String, String> result = new HashMap<>();
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils().addObject(form.getBillId())
                    .addObject(form.getAuditResult())
                    .addObject(form.getAuditRemark()).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            ReceiveBillAuditItemModel model = new ReceiveBillAuditItemModel();

            // 响应结果集
            Map<String, String> resultMap = receiveBillService.auditReceiveBill(form, user);
            String code = resultMap.get("code");
            String message = resultMap.get("message");
            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "刷新")
    @PostMapping(value = "/refreshReceiveBill.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应收账单id", required = true)})
    public ResponseBean refreshReceiveBill(String token, Long id) {
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            User user = ShiroUtils.getUserByToken(token);
            Map<String, String> resultMap = receiveBillService.refreshReceiveBill(id, user);
            String code = resultMap.get("code");
            String message = resultMap.get("message");
            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "删除")
    @PostMapping(value = "/delReceiveBill.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "应收账单id，多个则以英文逗号隔开", required = true)})
    public ResponseBean delReceiveBill(String token, String ids) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = receiveBillService.delReceiveBill(list);
            if (!b) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "校验应收单是否满足开票条件")
    @PostMapping(value = "/validateInfo.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "应收账单id，多个则以英文逗号隔开", required = true)})
    public ResponseBean validateInfo(String token, String ids) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);
            }
            List list = StrUtils.parseIds(ids);
            Map<String, String> resultMap = receiveBillService.validate(list);
            String code = resultMap.get("code");
            String message = resultMap.get("msg");
            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "提交作废申请")
    @PostMapping(value = "/saveInvalidBill.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "invalidRemark", value = "作废原因", required = true),
            @ApiImplicitParam(name = "ids", value = "应收账单id，多个则以英文逗号隔开", required = true)})
    public ResponseBean saveInvalidBill(String token, String ids, String invalidRemark) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils()
                    .addObject(ids).addObject(invalidRemark).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);
            }
            // 响应结果集
            Map<String, String> resultMap = receiveBillService.saveInvalidBill(ids, invalidRemark, user);
            String code = resultMap.get("code");
            String message = resultMap.get("message");
            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "根据应收账单号查询应收账单是否生成")
    @PostMapping(value = "/isBillSave.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "billCode", value = "应收账单号", required = true)})
    public ResponseBean isBillSave(String token, String billCode) {
        try {
            //校验id是否正确
            boolean isNull = new EmptyCheckUtils().addObject(billCode).empty();
            if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            Map<String, String> resultMap = receiveBillService.isBillSave(billCode);
            String code = resultMap.get("code");
            String billId = resultMap.get("billId");
            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, billId);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "导出Excel",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportReceiveBills.asyn")
    public void exportReceiveBills(ReceiveBillForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        User user= ShiroUtils.getUserByToken(form.getToken());

        ReceiveBillDTO dto = new ReceiveBillDTO();
        BeanUtils.copyProperties(form, dto);
        dto.setMerchantId(user.getMerchantId());

        //表头信息
        Map<Long, ReceiveBillDTO> receiveBillDTOMap = receiveBillService.listForExport(dto, user);
        List<Map<String,Object>> receiveBillList = new LinkedList<>();
        List<Long> billIds = new ArrayList<>();
        Map<Long, String> idCodeMap = new HashMap<>();
        Map<Long, String> customerNameMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (Long billId : receiveBillDTOMap.keySet()) {
            billIds.add(billId);
            ReceiveBillDTO receiveBillDTO = receiveBillDTOMap.get(billId);
            idCodeMap.put(billId, receiveBillDTO.getCode());
            customerNameMap.put(billId,receiveBillDTO.getCustomerName());
            BigDecimal receivedPrice = receiveBillDTO.getReceivedPrice() == null ? new BigDecimal("0") : receiveBillDTO.getReceivedPrice();
            Map<String, Object> receiveBillMap = new HashMap<>();
            receiveBillMap.put("code", receiveBillDTO.getCode());
            receiveBillMap.put("relCode", receiveBillDTO.getRelCode());
            receiveBillMap.put("customerName", receiveBillDTO.getCustomerName());
            receiveBillMap.put("buName", receiveBillDTO.getBuName());
            receiveBillMap.put("poNo", receiveBillDTO.getPoNo());
            receiveBillMap.put("invoiceNo", receiveBillDTO.getInvoiceNo());
            receiveBillMap.put("invoiceDate", receiveBillDTO.getInvoiceDate());
            receiveBillMap.put("receivablePrice", receiveBillDTO.getReceivablePrice());
            receiveBillMap.put("currency", receiveBillDTO.getCurrency());
            receiveBillMap.put("billMonth", receiveBillDTO.getBillDate() != null ? sdf.format(receiveBillDTO.getBillDate()) : null);
            receiveBillMap.put("receivedPrice", receiveBillDTO.getReceivedPrice());
            receiveBillMap.put("uncollectedPrice", receiveBillDTO.getReceivablePrice().subtract(receivedPrice));
            receiveBillMap.put("billStatus", receiveBillDTO.getBillStatusLabel());
            receiveBillMap.put("invoiceStatus", receiveBillDTO.getInvoiceStatusLabel());
            receiveBillMap.put("ncStatus", receiveBillDTO.getNcStatusLabel());
            receiveBillMap.put("ncCode", receiveBillDTO.getNcCode());
            receiveBillMap.put("voucherCode", receiveBillDTO.getVoucherCode());
            receiveBillMap.put("voucherStatus", receiveBillDTO.getVoucherStatusLabel());
            receiveBillMap.put("period", receiveBillDTO.getPeriod());
            receiveBillMap.put("creater", receiveBillDTO.getCreater());
            receiveBillMap.put("createDate", receiveBillDTO.getCreateDate());
            receiveBillList.add(receiveBillMap);
        }
        //表头信息
        String[] columns = {"应收账单号","关联业务单号","客户","事业部","PO号","发票号码","发票日期","应收金额","币种",
                "账单月份","已收金额", "未收金额","账单状态","开票状态","NC应收状态","NC单据","凭证编码","凭证状态","会计期间","创建人", "创建时间"};
        String[] keys = {"code","relCode","customerName","buName","poNo","invoiceNo","invoiceDate","receivablePrice","currency",
                "billMonth","receivedPrice", "uncollectedPrice", "billStatus","invoiceStatus","ncStatus","ncCode","voucherCode","voucherStatus",
                "period", "creater","createDate"};

        //应收明细
        List<Map<String,Object>> receiveBillItemList = receiveBillService.listForItemExport(billIds, idCodeMap,customerNameMap);
        String[] itemColumns = {"应收账单号","项目","PO号","平台SKU编码","标准条码","商品货号","商品名称","数量","结算金额",
                "系统业务单号","母品牌", "NC收支费项","科目名称"};
        String[] itemKeys = {"billCode","projectName","poNo","platformSku","commbarcode","goodsNo","goodsName","num",
                "price", "code", "parentBrandName","paymentSubjectName", "subjectName"};

        //费项明细
        List<Map<String,Object>> receiveBillCostItemList = receiveBillService.listForCostItemExport(billIds, idCodeMap,customerNameMap);
        String[] costColumns = {"应收账单号","费用类型","PO号","标准条码","商品货号","商品名称", "类型","数量","费用金额",
                "母品牌", "NC收支费项","备注", "发票描述"};
        String[] costKeys = {"billCode","projectName","poNo","commbarcode","goodsNo","goodsName","billType", "num",
                "price", "parentBrandName","paymentSubjectName", "remark", "invoiceDescription"};

        //生成表格
        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("应收账单信息", columns, keys, receiveBillList);
        ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("应收明细", itemColumns, itemKeys, receiveBillItemList);
        ExportExcelSheet costSheet = ExcelUtilXlsx.createSheet("费项明细", costColumns, costKeys, receiveBillCostItemList);

        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
        sheets.add(mainSheet) ;
        sheets.add(itemSheet) ;
        sheets.add(costSheet) ;

        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
        //导出文件
        String fileName = "应收账单"+ TimeUtils.formatDay();
        FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
    }


    @ApiOperation(value = "更新凭证信息")
    @PostMapping(value = "/updateVoucher.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "应收账单id，多个则以英文逗号隔开", required = true)})
    public ResponseBean updateVoucher(String token, String ids) {
        try{
            //校验id是否正确
            boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
            if (isNull) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            Map<String, String> resultMap = receiveBillService.updateVoucher(list);
            String code = resultMap.get("code");
            String message = resultMap.get("message");
            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "保存应收账单")
    @PostMapping("/saveAddReceiveBill.asyn")
    public ResponseBean saveAddReceiveBill(@RequestBody ReceiveBillForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            Map<String, String> retMap = receiveBillService.saveApiAddReceiveBill(form, user);
            if ("01".equals(retMap.get("code"))) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), retMap.get("msg"));
            }

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000.getCode(), retMap.get("billId"));
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "获取勾稽预收单分页数据")
    @PostMapping(value = "/listBeVerifyAdvanceBill.asyn")
    private ResponseBean listBeVerifyAdvanceBill(ReceiveBillVerifyAdvanceForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            ReceiveBillVerifyAdvanceDTO dto = new ReceiveBillVerifyAdvanceDTO();
            BeanUtils.copyProperties(form, dto);
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            advanceBillService.listVerifyAdvanceByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    /*@ApiOperation(value = "保存预收核销信息")
    @PostMapping(value = "/saveAdvanceVerify.asyn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "预收单id，多个则以英文逗号隔开", required = true)})
    public ResponseBean saveAdvanceVerify(String token, String ids) {
        try {
            Map<String, String> retMap = receiveBillVerifyItemService.saveAdvanceVerifyItem(ids);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }
*/

    @ApiOperation(value = "应收明细导入")
    @PostMapping(value = "/importReceiveItems.asyn")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "billId", value = "应收账单id", required = false)})
    public ResponseBean importReceiveItems(@RequestParam(value = "file", required = false) MultipartFile file, String token,
                                           Long billId) {
        try{
            if(file == null){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }

            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
            if(data == null){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            User user= ShiroUtils.getUserByToken(token);
            Map resultMap = receiveBillService.importReceiveItems(data, billId, user);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);
        } catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "应收明细导出",produces= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="/exportReceiveItems.asyn")
    public void exportReceiveItems(HttpServletResponse response, HttpServletRequest request, String json) throws Exception{
        OutputStream out = null;
        try{
            String fileName = "应收明细导出";
            List<ReceiveBillItemDTO> itemList=null;
            if(json != null || StringUtils.isNotBlank(json)){			//输入信息不完整
                itemList = receiveBillService.exportListItem(json);
            }
            Workbook workbook = null ;

            ReceiveBillExportDTO dto=new ReceiveBillExportDTO();
            dto.setItemList(itemList);
            workbook = DownloadExcelUtil.exportTemplateExcel("RECEIVEITEM", dto);

            FileExportUtil.setHeader(response, request, fileName +".xlsx");
            out = response.getOutputStream();
            workbook.write(out);
        }catch(Exception e) {
            System.err.println("文件导出异常:" + e);
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @ApiOperation(value = "费用明细导入")
    @PostMapping(value = "/importReceiveCostItems.asyn")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "billId", value = "应收账单id", required = false)})
    public ResponseBean importReceiveCostItems(@RequestParam(value = "file", required = false) MultipartFile file, String token,
                                                   Long billId) {
        try{
            if(file == null){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }
            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
            if(data == null){//数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            Map resultMap = receiveBillService.importReceiveCostItems(data, billId, user);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);
        } catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "费用明细导出",produces= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="/exportReceiveCostItems.asyn")
    public void exportReceiveCostItems(HttpServletResponse response, HttpServletRequest request, String json) throws Exception{
        OutputStream out = null;
        try{
            String fileName = "费用明细导出";
            List<ReceiveBillCostItemDTO> itemList=null;
            if(json != null || StringUtils.isNotBlank(json)){			//输入信息不完整
                itemList = receiveBillService.exportListCostItem(json);
            }
            Workbook workbook = null ;

            ReceiveBillExportDTO dto=new ReceiveBillExportDTO();
            dto.setCostItemList(itemList);
            workbook = DownloadExcelUtil.exportTemplateExcel("RECEIVECOST", dto);

            FileExportUtil.setHeader(response, request, fileName +".xlsx");
            out = response.getOutputStream();
            workbook.write(out);
        }catch(Exception e) {
            System.err.println("文件导出异常:" + e);
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value = "创建应收页面明细导入")
    @PostMapping(value = "/importAddReceiveItems.asyn")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = false)})
    public ResponseBean importAddReceiveItems(@RequestParam(value = "file", required = false) MultipartFile file, String token, Long customerId) {
        try{
            if(file == null){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }

            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
            if(data == null){//数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            User user= ShiroUtils.getUserByToken(token);
            Map resultMap = receiveBillService.importAddReceiveItems(data, customerId, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "应收账单开票")
    @PostMapping(value = "/toInvoicePage.asyn")
    public ResponseBean toInvoicePage(InvoiceTempForm form) {

        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            //返回发票模板的html
            String excelHtml = receiveBillService.generateInvoiceHtml(form.getTempId(), form.getIds(), form.getInvoiceStatus(), user);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, excelHtml);

        } catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }


    @ApiOperation(value = "生成发票")
    @PostMapping(value = "/saveContract.asyn")
    public ResponseBean saveContract(@RequestBody InvoiceTemplateForm templateForm) {
        try {

            if (StringUtils.isBlank(templateForm.getIds()) ||
                    StringUtils.isBlank(templateForm.getCodes())) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "关联应收单不能为空");
            }

            if (StringUtils.isBlank(String.valueOf(templateForm.getFileTempId()))) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "关联发票模板不能为空");
            }

            User user = ShiroUtils.getUserByToken(templateForm.getToken());

            receiveBillService.saveContract(user, templateForm);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), e.getMessage());
        }
    }


    @ApiOperation(value = "编辑页面保存")
    @PostMapping(value = "/saveReceiveBillFromEdit.asyn")
    private ResponseBean saveReceiveBillFromEdit(@RequestBody ReceiveBillSubmitForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            // 响应结果集
            Map<String, String> result = receiveBillService.saveReceiveBillFromEdit(form, user);
            if ("01".equals(result.get("code"))) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), result.get("message"));
            }

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "编辑页面——选择单据确认")
    @PostMapping(value = "/confirmOrder.asyn")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "billId", value = "应收单id", required = true),
            @ApiImplicitParam(name = "relCodes", value = "勾选单据单号，多个以逗号隔开", required = false)})
    private ResponseBean confirmOrder(String token, String relCodes, Long billId) {
        try {
            User user = ShiroUtils.getUserByToken(token);

            List<ReceiveBillItemDTO> itemDTOS = receiveBillService.chooseOrder(user, billId, relCodes);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, itemDTOS);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }
}
