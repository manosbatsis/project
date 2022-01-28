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
import com.topideal.entity.dto.bill.BillMonthlySnapshotDTO;
import com.topideal.order.service.bill.BillMonthlySnapshotService;
import com.topideal.order.service.bill.ReceiveCloseAccountsService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.BillMonthlySnapshotForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 月结快照
 */
@RestController
@RequestMapping("/webapi/order/billMonthlySnapshot")
@Api(tags = "月结快照")
public class APIBillMonthlySnapshotController {

    private static final String[] MAIN_COLUMNS = {"应收账单号", "账单类型", "事业部", "发票编号", "入账月份", "客户", "平台", "应收金额", "未核金额", "结算币种", "开票日期"};

    private static final String[] MAIN_KEYS = {"receiveCode", "billType", "buName", "invoiceNo", "creditMonth", "customerName", "storePlatformName", "receivableAmount",
            "nonverifyAmount", "currency", "invoiceDate"};


    @Autowired
    private BillMonthlySnapshotService billMonthlySnapshotService;

    @Autowired
    private ReceiveCloseAccountsService receiveCloseAccountsService;

    @Autowired
    private RMQProducer rocketMQProducer;

    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listBillMonthlySnapshot.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<BillMonthlySnapshotDTO> listBillMonthlySnapshot(BillMonthlySnapshotForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            BillMonthlySnapshotDTO dto = new BillMonthlySnapshotDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = billMonthlySnapshotService.listBillMonthlySnapshotByPage(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @ApiOperation(value = "刷新")
    @PostMapping(value = "/refreshMonthlySnapshot.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "month", value = "月结月份", required = true)})
    private ResponseBean refreshMonthlySnapshot(String token, String month) {
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

            JSONObject json = JSONObject.fromObject(body);
            SendResult result = rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_GENERATE_MONTHLY_SNAPSHOT.getTopic(), MQOrderEnum.TIMER_GENERATE_MONTHLY_SNAPSHOT.getTags());

            if (result == null || !result.getSendStatus().name().equals("SEND_OK")) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "刷新消息发送失败");
            }

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportMonthlySnapshot.asyn")
    public ResponseBean exportToBTempTrack(BillMonthlySnapshotForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {

        // 必填项空值校验
        boolean isNull = new EmptyCheckUtils()
                .addObject(form.getMonth()).empty();
        if (isNull) {
            // 输入信息不完整
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        }

        User user = ShiroUtils.getUserByToken(form.getToken());
        BillMonthlySnapshotDTO dto = new BillMonthlySnapshotDTO();
        BeanUtils.copyProperties(form, dto);
        dto.setMerchantId(user.getMerchantId());


        List<Map<String, Object>> mapList = billMonthlySnapshotService.listForExport(dto, user);

        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel("月结快照", MAIN_COLUMNS, MAIN_KEYS, mapList);
        //导出文件
        String fileName = form.getMonth() + "收款核销月结快照";
        FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

}