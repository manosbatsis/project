package com.topideal.order.webapi.bill;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.order.service.bill.TobTemporaryReceiveBillService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.TobTemporaryReceiveBillForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.List;
import java.util.Map;

/**
 * toB收款跟踪
 */
@RestController
@RequestMapping("/webapi/order/toBTempBillTrack")
@Api(tags = "To B收款跟踪")
public class APIToBTempBillTrackController {

    private static final String[] MAIN_COLUMNS = {"公司", "事业部", "PO号", "上架日期", "客户名称", "应收金额", "销售币种", "计划回款周期", "实际回款周期"};

    private static final String[] MAIN_KEYS = {"merchantName", "buName", "poNo", "shelfDate", "customerName", "receiveAmount", "currency",
            "paymentPlanPeriod", "paymentRealPeriod"};


    @Autowired
    private TobTemporaryReceiveBillService tobTemporaryReceiveBillService;

    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listToBTempBillTrack.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listToBTempBillTrack(TobTemporaryReceiveBillForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TobTemporaryReceiveBillDTO dto = new TobTemporaryReceiveBillDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto.setSaleType(DERP_ORDER.TOBTEMPRECEIVEBILL_SALETYPE_1);
            dto = tobTemporaryReceiveBillService.listToBTempBillTrackByPage(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @ApiOperation(value = "导出ToB收款跟踪表",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportToBTempTrack.asyn")
    public void exportToBTempTrack(TobTemporaryReceiveBillForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        User user = ShiroUtils.getUserByToken(form.getToken());
        TobTemporaryReceiveBillDTO dto = new TobTemporaryReceiveBillDTO();
        BeanUtils.copyProperties(form, dto);
        dto.setMerchantId(user.getMerchantId());
        dto.setSaleType(DERP_ORDER.TOBTEMPRECEIVEBILL_SALETYPE_1);
        /*if (StringUtils.isNotBlank(form.getShelfMonth())) {
            dto.setShelfDate(TimeUtils.parse(form.getShelfMonth(), "yyyy-MM"));
        }*/

        String sheetName = "PO收款跟踪";

        List<Map<String, Object>> mapList = tobTemporaryReceiveBillService.listForExportToBTrack(dto, user);

        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, MAIN_COLUMNS, MAIN_KEYS, mapList);
        //导出文件
        String fileName = "To B收款跟踪导出模板";
        FileExportUtil.export2007ExcelFile(wb, response, request, fileName);

    }

}