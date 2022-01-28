package com.topideal.order.webapi.bill;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillRebateItemDTO;
import com.topideal.order.service.bill.TobTemporaryReceiveBillService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.TobTemporaryReceiveBillForm;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * toB暂估核销
 */
@RestController
@RequestMapping("/webapi/order/toBTempBillVerify")
@Api(tags = "To B暂估核销")
public class APIToBTempBillVerifyController {

    private static final String[] MAIN_ITEM_COLUMNS = {"公司", "事业部", "PO号", "上架日期", "客户名称", "结算状态", "销售类型", "销售币种",
            "暂估应收金额", "已核销应收", "未核销应收"};

    private static final String[] MAIN_ITEM_KEYS = {"merchantName", "buName", "poNo", "shelfDate", "customerName", "status", "saleType", "currency",
            "shelfAmount", "verifyAmount", "nonVerifyAmount"};

    private static final String[] MAIN_REBATE_COLUMNS = {"公司", "事业部", "PO号", "上架日期", "客户名称", "结算状态", "销售类型", "销售币种",
            "暂估返利金额", "已核销返利", "未核销返利"};

    private static final String[] MAIN_REBATE_KEYS = {"merchantName", "buName", "poNo", "shelfDate", "customerName", "rebateStauts", "saleType", "currency",
            "shelfRebateAmount", "verifyRebateAmount", "nonVerifyRebateAmount"};


    private static final String[] ITEM_COLUMNS = {"事业部", "客户名称", "PO号", "结算状态", "销售单号", "上架单号", "上架日期", "商品货号", "母品牌", "商品名称",
            "上架好品量", "销售单价", "销售币种", "暂估应收", "核销应收金额", "未核销应收金额", "应收账单号"};

    private static final String[] ITEM_KEYS = {"buName", "customerName", "poNo", "status", "orderCode", "shelfCode", "shelfDate", "goodsNo", "parentBrandName",
            "goodsName", "shelfNum", "price", "currency", "receiveAmount", "verifyAmount", "nonVerifyAmount", "receiveCode"};

    private static final String[] REBATE_ITEM_COLUMNS = {"事业部", "客户名称", "PO号", "结算状态", "销售SD单号", "上架单号", "上架日期", "商品货号", "母品牌", "标准品牌", "商品名称",
            "上架好品量", "销售单价", "SD类型", "SD比例", "销售币种", "暂估返利", "核销返利金额", "未核销返利金额", "应收账单号"};

    private static final String[] REBATE_ITEM_KEYS = {"buName", "customerName", "poNo", "status", "relSdCode", "shelfCode", "shelfDate", "goodsNo", "parentBrandName", "brandName",
            "goodsName", "shelfNum", "price", "sdTypeName", "sdRatio", "currency", "receiveAmount", "verifyAmount", "nonVerifyAmount", "receiveCode"};


    @Autowired
    private TobTemporaryReceiveBillService tobTemporaryReceiveBillService;
    @Autowired
    private RMQProducer rocketMQProducer;



    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listToBTempBillVerify.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<TobTemporaryReceiveBillDTO> listToBTempBillVerify(TobTemporaryReceiveBillForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TobTemporaryReceiveBillDTO dto = new TobTemporaryReceiveBillDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = tobTemporaryReceiveBillService.listToBTempBillVerifyByPage(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @ApiOperation(value = "根据To B暂估核销ID获取详情")
    @PostMapping("/getDetailsById.asyn")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "To B暂估核销id", required = true)})
    public ResponseBean<TobTemporaryReceiveBillDTO> getDetailsById(@RequestParam(value = "token", required = true) String token, Long id) throws Exception {
        try {
            TobTemporaryReceiveBillDTO dto = tobTemporaryReceiveBillService.getDetails(id);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "根据To B暂估核销ID分页获取核销明细")
    @PostMapping("/listToBTempItem.asyn")
    private ResponseBean<TobTemporaryReceiveBillItemDTO> listToBTempItem(TobTemporaryReceiveBillForm form) {

        try {
            TobTemporaryReceiveBillItemDTO dto = new TobTemporaryReceiveBillItemDTO();
            dto.setBillId(form.getReceiveId());
            BeanUtils.copyProperties(form, dto);
            dto = tobTemporaryReceiveBillService.listToBTempItemByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "根据To B暂估核销ID分页获取核销返利明细")
    @PostMapping("/listToBTempRebateItem.asyn")
    private ResponseBean<TobTemporaryReceiveBillRebateItemDTO> listToBTempRebateItem(TobTemporaryReceiveBillForm form) {

        try {
            TobTemporaryReceiveBillRebateItemDTO dto = new TobTemporaryReceiveBillRebateItemDTO();
            dto.setBillId(form.getReceiveId());
            BeanUtils.copyProperties(form, dto);
            dto = tobTemporaryReceiveBillService.listToBTempRebateItemByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "刷新To B暂估核销单")
    @PostMapping(value = "/flashTobTemporaryReceiveBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "To B暂估核销id", required = true)})
    private ResponseBean flashTobTemporaryReceiveBill(@RequestParam(value = "token", required = true) String token, Long id) {
        try {

            if (id == null) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009.getCode(), "To B暂估核销单id不能为空");
            }

            TobTemporaryReceiveBillDTO dto = tobTemporaryReceiveBillService.getDetails(id);

            //发送mq消息
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("orderCodes", dto.getShelfCode());

            JSONObject json = JSONObject.fromObject(body);
            SendResult result = rocketMQProducer.send(json.toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());

            if (result == null || !result.getSendStatus().name().equals("SEND_OK")) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "刷新消息发送失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }


    @ApiOperation(value = "获取导出To B暂估明细数量")
    @ApiResponses({
            @ApiResponse(code = 10000,message="data = > 导出的电商订单的数量")
    })
    @PostMapping(value="/getTempDetailsCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getTempDetailsCount(TobTemporaryReceiveBillForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TobTemporaryReceiveBillDTO dto = new TobTemporaryReceiveBillDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            Integer count = tobTemporaryReceiveBillService.getTempDetailsCount(dto);
            Map<String , Integer> map = new HashMap<>();
            map.put("count", count);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, map);
        } catch (Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }

    @ApiOperation(value = "导出To B暂估明细（收入）")
    @GetMapping(value = "/exportToBTempDetails.asyn")
    public void exportToBTempDetails(TobTemporaryReceiveBillForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        User user = ShiroUtils.getUserByToken(form.getToken());
        TobTemporaryReceiveBillDTO dto = new TobTemporaryReceiveBillDTO();
        BeanUtils.copyProperties(form, dto);
        dto.setMerchantId(user.getMerchantId());
        Map<String, List<Map<String, Object>>> resultMap = tobTemporaryReceiveBillService.exportTempDetails(dto, user);

        List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;

        List<Map<String, Object>> mainList = resultMap.get("mainList");
        String sheetName = "PO暂估核销汇总";
        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName, MAIN_ITEM_COLUMNS, MAIN_ITEM_KEYS, mainList);
        sheetList.add(mainSheet) ;

        List<Map<String, Object>> itemMapList = resultMap.get("itemMapList");
        String itemName = "PO应收暂估核销明细";
        ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemName, ITEM_COLUMNS, ITEM_KEYS, itemMapList);
        sheetList.add(itemSheet) ;

        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;

        //导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, "2B暂估收入明细");

    }

    @ApiOperation(value = "导出To B暂估明细（费用）")
    @GetMapping(value = "/exportToBTempRebateDetails.asyn")
    public void exportToBTempRebateDetails(TobTemporaryReceiveBillForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        User user = ShiroUtils.getUserByToken(form.getToken());
        TobTemporaryReceiveBillDTO dto = new TobTemporaryReceiveBillDTO();
        BeanUtils.copyProperties(form, dto);
        dto.setMerchantId(user.getMerchantId());
        Map<String, List<Map<String, Object>>> resultMap = tobTemporaryReceiveBillService.exportTempRebateDetails(dto, user);

        List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;

        List<Map<String, Object>> mainList = resultMap.get("mainList");
        String sheetName = "PO暂估核销汇总";
        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName, MAIN_REBATE_COLUMNS, MAIN_REBATE_KEYS, mainList);
        sheetList.add(mainSheet) ;

        List<Map<String, Object>> rebateItemMapList = resultMap.get("rebateItemMapList");
        String rebateItemName = "PO返利暂估核销明细";
        ExportExcelSheet rebateItemSheet = ExcelUtilXlsx.createSheet(rebateItemName, REBATE_ITEM_COLUMNS, REBATE_ITEM_KEYS, rebateItemMapList);
        sheetList.add(rebateItemSheet) ;

        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;

        //导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, "2B暂估核销费用明细");

    }


    @ApiOperation(value = "完结核销")
    @PostMapping(value = "/endTobTemporaryReceiveBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "type", value = "暂估类型 0-收入 1-费用", required = true),
            @ApiImplicitParam(name = "tobIds", value = "To B暂估核销id，多个以英文逗号隔开", required = true)})
    private ResponseBean endTobTemporaryReceiveBill(String token, String tobIds, String type) {
        try {

            if (StringUtils.isBlank(tobIds)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009.getCode(), "To B暂估核销单id不能为空");
            }

            if (StringUtils.isBlank(type)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009.getCode(), "暂估类型不能为空");
            }

            tobTemporaryReceiveBillService.endTobTemporaryReceiveBill(tobIds, type);

        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }


    @ApiOperation(value = "删除")
    @PostMapping(value = "/batchDelReceiveBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "tobIds", value = "To B暂估核销id，多个以英文逗号隔开", required = true)})
    private ResponseBean batchDelReceiveBill(String token, String tobIds) {
        try {

            if (StringUtils.isBlank(tobIds)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009.getCode(), "To B暂估核销单id不能为空");
            }

            tobTemporaryReceiveBillService.batchDelReceiveBill(tobIds);

        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    @ApiOperation(value = "刷新")
    @PostMapping(value = "/refreshTempBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "shelfMonth", value = "上架月份")})
    private ResponseBean refreshTempBill(String token, String shelfMonth) {
        try {

            User user = ShiroUtils.getUserByToken(token);
            Map<String, Object> tocTempMap = new HashMap<String, Object>();
            tocTempMap.put("merchantId", user.getMerchantId());
            tocTempMap.put("month", shelfMonth);
            tocTempMap.put("refresh", "1");
            JSONObject tocTempJson = JSONObject.fromObject(tocTempMap);
            rocketMQProducer.send(tocTempJson.toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());

        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

}