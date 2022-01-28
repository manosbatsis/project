package com.topideal.order.webapi.sale;


import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.InventoryLocationAdjustExportDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderDTO;
import com.topideal.entity.dto.sale.LocationAdjustmentOrderDTO;
import com.topideal.order.service.sale.LocationAdjustmentService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.InventoryLocationAdjustmentOrderForm;
import com.topideal.order.webapi.sale.form.LocationAdjustmentOrderForm;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/order/locationAdjustment")
@Api(tags = "库位调整单")
public class APILocationAdjustmentOrderController {

    private static final String[] MAIN_COLUMNS = {"库位调整单号", "调整单据类型", "事业部", "仓库", "客户名称", "平台", "订单号", "条形码", "商品名称", "调增类型",
            "调减类型", "调整数量", "归属月份","创建人","状态"} ;

    private static final String[] MAIN_KEYS = {"code", "orderTypeLabel", "buName", "depotName","customerName","platformName","orderCode", "barcode", "goodsName",
            "inStockLocationTypeName", "outStockLocationTypeName", "adjustNum", "month", "createName", "statusLabel"} ;

    @Autowired
    private LocationAdjustmentService locationAdjustmentService;

    @ApiOperation(value = "获取库位调整单列表")
    @PostMapping(value = "/listLocationAdjustment.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<LocationAdjustmentOrderDTO> listLocationAdjustment(LocationAdjustmentOrderForm form) {

        try {
            LocationAdjustmentOrderDTO dto = new LocationAdjustmentOrderDTO();
            User user = ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());

            dto = locationAdjustmentService.listInventoryLocationAdjustDTO(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }

    @ApiOperation(value = "导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value = "/importLocationAdjust.asyn", headers = "content-type=multipart/form-data")
    public ResponseBean<UploadResponse> importLocationAdjust(String token,
                                                             @ApiParam(value = "上传的文件", required = true)
                                                             @RequestParam(value = "file", required = true) MultipartFile file) {
        try {
            if (file == null) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }

            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
            if (data == null) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            User user = ShiroUtils.getUserByToken(token);
            Map<String, Object> resultMap = locationAdjustmentService.saveImportLocationAdjustment(data, user);

            Integer success = (Integer) resultMap.get("success");
            Integer failure = (Integer) resultMap.get("failure");
            List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");

            UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
            uploadResponse.setSuccess(success);
            uploadResponse.setFailure(failure);
            uploadResponse.setErrorList(errorList);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, uploadResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    @ApiOperation(value = "获取导出的数量")
    @ApiResponses({
            @ApiResponse(code=10000,message = "data = > 导出的库位调整单的数量")
    })
    @PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<Integer> getOrderCount(LocationAdjustmentOrderForm form){
        Integer result = 0;
        try{
            User user= ShiroUtils.getUserByToken(form.getToken());

            LocationAdjustmentOrderDTO dto = new LocationAdjustmentOrderDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());

            // 响应结果集
            result = locationAdjustmentService.getOrderCount(dto, user);
        } catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
    }


    @ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportLocationAdjust.asyn")
    private ResponseBean exportLocationAdjust(HttpServletResponse response, HttpServletRequest request, LocationAdjustmentOrderForm form) {
        try{
            User user= ShiroUtils.getUserByToken(form.getToken());

            LocationAdjustmentOrderDTO dto = new LocationAdjustmentOrderDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());

            List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;

            // 响应结果集
            List<LocationAdjustmentOrderDTO> mainList = locationAdjustmentService.getExportMainList(dto,user);

            String mainSheetName = "库位调整单";

            ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, MAIN_COLUMNS, MAIN_KEYS, mainList);
            sheetList.add(mainSheet) ;

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        }catch(Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
    })
    @PostMapping(value="/delLocationAdjust.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delLocationAdjust(String token, String ids) {
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//未知异常
            }

            if(StringUtils.isBlank(ids)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            boolean b = locationAdjustmentService.delLocationAdjust(ids);
            if(!b){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"删除失败");//未知异常
            }
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    @ApiOperation(value = "审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
    })
    @PostMapping(value="/auditLocationAdjust.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean auditLocationAdjust(String token, String ids) {
        try{

            User user= ShiroUtils.getUserByToken(token);

            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);

            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//未知异常
            }

            if(StringUtils.isBlank(ids)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            locationAdjustmentService.auditLocationAdjust(ids, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch(Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    @ApiOperation(value = "刷新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
    })
    @PostMapping(value="/refreshLocationAdjust.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean refreshLocationAdjust(String token, String ids) {
        try{

            User user= ShiroUtils.getUserByToken(token);

            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);

            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//未知异常
            }

            if(StringUtils.isBlank(ids)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            locationAdjustmentService.refreshLocationAdjust(ids, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch(Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

}
