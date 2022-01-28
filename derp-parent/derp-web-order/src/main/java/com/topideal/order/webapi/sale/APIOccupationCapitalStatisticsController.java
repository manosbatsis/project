package com.topideal.order.webapi.sale;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.common.OccupationRateConfigDTO;
import com.topideal.entity.dto.sale.OccupationCapitalStatisticsDTO;
import com.topideal.order.service.sale.OccupationCapitalStatisticsService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.OccupationCapitalStatisticsForm;
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

@RestController
@RequestMapping("/webapi/order/occupationCapitalStatistics")
@Api(tags = "资金占用统计表")
public class APIOccupationCapitalStatisticsController {
    @Autowired
    private OccupationCapitalStatisticsService occupationCapitalStatisticsService;

    @ApiOperation(value = "获取资金占用统计表分页数据")
    @PostMapping(value = "/listDTOByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<OccupationRateConfigDTO> listDTOByPage(OccupationCapitalStatisticsForm form) {
        OccupationCapitalStatisticsDTO dto = new OccupationCapitalStatisticsDTO();
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());

            // 响应结果集
            dto = occupationCapitalStatisticsService.listDTOByPage(dto,user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @ApiOperation(value = "获取资金占用统计表导出数量")
    @PostMapping(value = "/getOrderCount.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<Integer> getOrderCount(OccupationCapitalStatisticsForm form) {
        try {
            OccupationCapitalStatisticsDTO dto = new OccupationCapitalStatisticsDTO();
            User user = ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            Integer  count = occupationCapitalStatisticsService.getOrderCount(dto,user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, count );//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @ApiOperation(value = "导出")
    @GetMapping(value = "/export.asyn")
    public void export(OccupationCapitalStatisticsForm form , HttpServletResponse response, HttpServletRequest request ) {
        try {
            OccupationCapitalStatisticsDTO dto = new OccupationCapitalStatisticsDTO();
            User user = ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());

            // 响应结果集
            List<OccupationCapitalStatisticsDTO> list = occupationCapitalStatisticsService.listDTO(dto,user);
            /** 标题  */
            String[] COLUMNS = {"权责月份","公司","事业部","销售赊销单号","PO号","客户","币种","赊销金额","起息日期","卓普信放款日期","已收保证金",
                    "收保证金日期","到期日期","垫付金额","回款本金","回款日期","垫付余款","利息","逾期天数","逾期费用","资金占用天数","资金占用费率(%)",
                    "资金占用费","毛利" };
            String[] KEYS = {"ownMonth", "merchantName", "buName", "creditOrderCode", "poNo", "customerName", "currency", "creditAmount",
                    "valueDate", "sapienceLoanDate","actualMarginAmount", "receiveMarginDate", "expireDate", "advancedAmount",
                    "principalAmount", "receiveDate", "advancedRestAmount", "interest", "overdueDays", "overdueAmount", "occupationDays",
                    "occupationRate", "occupationAmount", "grossProfit"};

            String sheetName = "资金占用统计表导出";

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, list);
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
