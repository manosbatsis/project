package com.topideal.order.webapi.bill;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillCostItemMonthlyDTO;
import com.topideal.order.service.bill.ReceiveCloseAccountsService;
import com.topideal.order.service.bill.ToBTempCostMonthlySnapshotService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.ToBTemporaryMonthSnapshotForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
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
 * 费用月结快照
 */
@RestController
@RequestMapping("/webapi/order/toBTempCostMonthlySnapshot")
@Api(tags = "费用月结快照")
public class APIToBTempCostMonthlySnapshotController {

    private static final String[] MAIN_COLUMNS = {"月结月份", "公司", "事业部", "客户名称", "母品牌", "销售币种", "SD类型", "本月新增暂估", "本月核销应收", "累计未核销应收"};

    private static final String[] MAIN_KEYS = {"month", "merchantName", "buName", "customerName", "parentBrandName", "currency", "sdType", "receivableAmount",
            "verifyAmount", "nonVerifyAmount"};

    private static final String[] ITEM_COLUMNS = {"月结月份", "事业部", "客户名称", "PO号", "销售SD单号", "上架单号", "上架日期", "商品货号", "母品牌", "销售币种", "商品名称",
            "上架好品量", "销售单价", "SD类型", "SD比例", "销售币种" , "暂估返利", "核销返利金额", "未核销返利金额", "应收账单号"};


    private static final String[] ITEM_KEYS = {"month", "buName", "customerName", "poNo", "sdCode", "shelfCode", "shelfDate", "goodsNo", "parentBrandName", "currency",
            "goodsName", "shelfNum", "price", "sdType", "sdRatio", "currency", "rebateAmount", "verifyAmount", "nonVerifyAmount", "receiveCodes"};


    @Autowired
    private ToBTempCostMonthlySnapshotService toBTempCostMonthlySnapshotService;
    @Autowired
    private ReceiveCloseAccountsService receiveCloseAccountsService;
    @Autowired
    private RMQProducer rocketMQProducer;

    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listToBTempMonthlySnapshot.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<TobTemporaryReceiveBillCostItemMonthlyDTO> listToBTempMonthlySnapshot(ToBTemporaryMonthSnapshotForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TobTemporaryReceiveBillCostItemMonthlyDTO dto = new TobTemporaryReceiveBillCostItemMonthlyDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = toBTempCostMonthlySnapshotService.listToBTempCostMonthlyByPage(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @ApiOperation(value = "刷新")
    @PostMapping(value = "/flashTobTempMonthlySnapshot.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "month", value = "月结月份", required = true)})
    private ResponseBean flashTobTempMonthlySnapshot(@RequestParam(value = "token", required = true) String token, String month) {
        try {

            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils()
                    .addObject(month).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            User user = ShiroUtils.getUserByToken(token);

            //判断该月结月份是否全部关账
            boolean isClose = receiveCloseAccountsService.validateIsClose(user, month);
            if (isClose) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), "该公司该月结月份已全部关账，不能刷新！");
            }

            //发送mq消息
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("merchantId", user.getMerchantId());
            body.put("month", month);
            body.put("sourceType", "2");

            JSONObject json = JSONObject.fromObject(body);
            SendResult result = rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_GENERATE_TOB_TEMP_MONTHLY_REPORT.getTopic(), MQOrderEnum.TIMER_GENERATE_TOB_TEMP_MONTHLY_REPORT.getTags());

            if (result == null || !result.getSendStatus().name().equals("SEND_OK")) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "刷新消息发送失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }


    @ApiOperation(value = "月结导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportToBTempCostMonthlySnapshot.asyn")
    public void exportToBTempCostMonthlySnapshot(ToBTemporaryMonthSnapshotForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {

        // 必填项空值校验
        boolean isNull = new EmptyCheckUtils()
                .addObject(form.getMonth()).empty();
        if (isNull) {
            // 输入信息不完整
            throw new RuntimeException("月结月份不能为空！");
        }

        User user = ShiroUtils.getUserByToken(form.getToken());
        TobTemporaryReceiveBillCostItemMonthlyDTO dto = new TobTemporaryReceiveBillCostItemMonthlyDTO();
        BeanUtils.copyProperties(form, dto);
        dto.setMerchantId(user.getMerchantId());
        Map<String, List<Map<String, Object>>> resultMap = toBTempCostMonthlySnapshotService.exportToBTempCostMonthlySnapshot(dto, user);

        List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;

        List<Map<String, Object>> mainList = resultMap.get("mainList");
        String sheetName = "2B暂估返利汇总";
        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName, MAIN_COLUMNS, MAIN_KEYS, mainList);
        sheetList.add(mainSheet) ;

        List<Map<String, Object>> rebateItemMapList = resultMap.get("itemList");
        String rebateItemName = "PO暂估返利月结明细";
        ExportExcelSheet rebateItemSheet = ExcelUtilXlsx.createSheet(rebateItemName, ITEM_COLUMNS, ITEM_KEYS, rebateItemMapList);
        sheetList.add(rebateItemSheet) ;

        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;

        //导出文件
        String fileName = form.getMonth() + "2B暂估费用月结快照";
        FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
    }

}