package com.topideal.report.webapi.automatic;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.YunjiAutomaticVerificationDTO;
import com.topideal.report.service.automatic.YunjiAutoVeriService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.YunjiAutomaticVerificationForm;
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
@RequestMapping("/webapi/report/yunjiAutoVeri")
@Api(tags = "云集账单校验")
public class APIYunjiAutoVeriController {

    @Autowired
    private YunjiAutoVeriService yunjiAutoVeriService;

    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listYunjiAutoVerification.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<YunjiAutomaticVerificationDTO> listYunjiAutoVerification(YunjiAutomaticVerificationForm form) {

        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            YunjiAutomaticVerificationDTO dto = new YunjiAutomaticVerificationDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = yunjiAutoVeriService.listYunjiAutoVerification(dto);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "统计导出数量")
    @PostMapping(value = "/getExportCount.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ViewResponseBean getExportCount(YunjiAutomaticVerificationForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            YunjiAutomaticVerificationDTO dto = new YunjiAutomaticVerificationDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            int count = yunjiAutoVeriService.getExportCount(dto);
            return ResponseFactory.success(count);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302);
        }
    }

    @ApiOperation(value = "根据查询条件导出数据", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value = "/export.asyn")
    public void export(YunjiAutomaticVerificationForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = ShiroUtils.getUserByToken(form.getToken());
        YunjiAutomaticVerificationDTO dto = new YunjiAutomaticVerificationDTO();
        BeanUtils.copyProperties(form, dto);
        dto.setMerchantId(user.getMerchantId());
        String fileName = "云集校验导出表";

        String mainsheet = "云集校验汇总表";
        String[] mainKeys = {"month", "settleId", "skuNo", "platformDeliveryAccount",
                "platformReturnAccount", "goodsNo", "systemDeliveryAccount",
                "systemReturnAccount", "deliveryDifferent", "returnDifferent", "result"};
        String[] mainColumns = {"账单月份", "结算单号", "云集商品编码", "平台发货量",
                "平台退货量", "商品货号", "系统出库量",
                "系统入库量", "出库差异", "入库差异", "原因"};
        List<YunjiAutomaticVerificationDTO> mainList = yunjiAutoVeriService.getExportList(dto);

        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainsheet, mainColumns, mainKeys, mainList);
        FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
    }
}
