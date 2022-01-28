package com.topideal.order.webapi.bill;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.ReceiveAgingReportDTO;
import com.topideal.entity.dto.bill.ReceiveAgingReportItemDTO;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.order.service.bill.ReceiveAgingReportItemService;
import com.topideal.order.service.bill.ReceiveAgingReportService;
import com.topideal.order.service.timer.FileTaskService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.ReceiveAgingReportForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping("/webapi/order/receiveAgingReport")
@Api(tags = "应收账龄报告")
public class APIReceiveAgingReportController {

    private static final Logger LOGGER= LoggerFactory.getLogger(APIReceiveAgingReportController.class);

    @Autowired
    private ReceiveAgingReportService receiveAgingReportService;
    @Autowired
    private ReceiveAgingReportItemService receiveAgingReportItemService;
    @Autowired
    private FileTaskService fileTaskService;

    /**
     * 获取分页数据
     * @param form
     * @return
     */
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listReceiveAgingReport.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<ReceiveAgingReportDTO> listReceiveAgingReport(ReceiveAgingReportForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            ReceiveAgingReportDTO dto = new ReceiveAgingReportDTO();
            BeanUtils.copyProperties(form,dto);
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            dto = receiveAgingReportService.listReceiveBillByPage(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    /***
     * 根据id查看详情
     * @param token
     * @param id
     * @return
     */
    @PostMapping("getDetailsById.asyn")
    @ApiOperation(value = "根据id查看详情")
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true)
    })
    private ResponseBean<ReceiveAgingReportDTO> listReceiveAgingReport(@RequestParam(value="token", required=true) String token ,
                                                                       @RequestParam(value="id", required=true)Long id) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            // 响应结果集
            ReceiveAgingReportDTO dto=receiveAgingReportService.getDetailsById(id);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    /**
     * 导出Excel
     * @param form
     * @param response
     * @param request
     * @throws Exception
     */
    @ApiOperation(value = "导出Excel",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportReceiveAgingReport.asyn")
    public void exportReceiveAgingReport(ReceiveAgingReportForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        User user = ShiroUtils.getUserByToken(form.getToken());
        ReceiveAgingReportDTO dto = new ReceiveAgingReportDTO();
        BeanUtils.copyProperties(form, dto);
        dto.setMerchantId(user.getMerchantId());

        String mainSheetName = "应收账龄报告";
        //表头信息
        String[] columns = {"公司","事业部","客户名称","客户简称","渠道类型","币种",
                "应收账面余额（原币）","应收收入（原币）","应收费用（原币）","折算汇率","汇率日期","应收账面余额（人民币）","应收收入（人民币）","应收费用（人民币）","0~30天",
                "30~60天","60~90天","90~120天","120天以上","正常账期天数","账期内金额（原币）","逾期金额（原币）"};
        String[] keys = {"merchantName","buName","customerName","simpleName","channelTypeLabel","currency",
                "receiveIncomeOriginalAmount","originalAmount","costOriginalAmount","rate","effectiveDate","receiveCostOriginalAmount","rmbAmount","costRmbAmount",
                "thirtyAmount", "sixtyAmount","ninetyAmount", "onetwentyAmount", "twentyAboveAmount","accountDay","accountAmount","overdueAmount"};

        String itemSheetName = "应收明细";
        String[] itemColumns = {"公司","事业部","客户名称","客户简称","渠道类型","币种","单据类型","po号",
                "单据号","待核销金额","应收月份"};
        String[] itemKeys = {"merchantName","buName","customerName","simpleName","channelType","currency","orderType",
                "poNo","code","writtenOffAmount","month"};
        List<ReceiveAgingReportDTO> receiveAgingList= receiveAgingReportService.listForExport(dto, user);

        List<ReceiveAgingReportItemDTO> itemList = new ArrayList<ReceiveAgingReportItemDTO>();
        for(ReceiveAgingReportDTO item:receiveAgingList){
            ReceiveAgingReportItemDTO itemDto=new ReceiveAgingReportItemDTO();
            itemDto.setAgingReportId(item.getId());
            List<ReceiveAgingReportItemDTO> list=receiveAgingReportItemService.receiveAgingReportItem(itemDto);
            itemList.addAll(list);
        }

        //生成表格
        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, columns, keys, receiveAgingList);
        ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, itemList);

        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
        sheets.add(mainSheet) ;
        sheets.add(itemSheet) ;

        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
        //导出文件
        String fileName = "应收账龄报告导出";
        FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
    }

    @ApiOperation(value = "刷新")
    @PostMapping(value = "/refreshReceiveAgingReport.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean refreshReceiveAgingReport(ReceiveAgingReportForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            ReceiveAgingReportDTO dto = new ReceiveAgingReportDTO();
            BeanUtils.copyProperties(form,dto);
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            Map<String,Object> map=receiveAgingReportService.refreshReceiveAgingReport(dto);
            String code=(String)map.get("code");
            String message=(String)map.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    /**
     * 获取刷新日期
     * @param token
     * @return
     */
    @ApiOperation(value = "获取刷新日期")
    @PostMapping(value = "/getMaxRefreshDate.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getMaxRefreshDate(String token) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            // 响应结果集
            Map<String, Object> map = receiveAgingReportService.getMaxRefrshDate(user.getMerchantId());
            String date="";
            if(map!=null){
                date=map.get("maxDate").toString();
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, date);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "月结报告导出")
    @PostMapping(value = "/exportMonthlyReport.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "buId", value = "事业部id", required = false),
            @ApiImplicitParam(name = "month", value = "应收月份", required = true)
    })
    private ResponseBean exportMonthlyReport(String token, Long buId, String month) throws Exception {

        User user = ShiroUtils.getUserByToken(token);

        Map<String, Object> retMap = new HashMap<String, Object>();
        if (StringUtils.isBlank(month)) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请选择导出应收月份");
        }

        retMap.put("code", "00");
        retMap.put("message", "成功");

        try {

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_AGINGREPORTMONTHLY);
            taskModel.setTaskName("应收账龄月结导出");
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setOwnMonth(month);
            taskModel.setState("1");
            taskModel.setRemark(month + "应收账龄月结导出");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("month", month);
            paramJson.put("buId", buId);
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
            taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", "01");
            retMap.put("message", "网络故障");
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, retMap);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);
    }

}
