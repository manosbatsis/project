package com.topideal.order.webapi.sale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.topideal.entity.dto.sale.CutomerQuotaWarningDTO;
import com.topideal.entity.dto.sale.CutomerQuotaWarningItemDTO;
import com.topideal.order.service.sale.CutomerQuotaWarningService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.CutomerQuotaWarningForm;
import com.topideal.order.webapi.sale.form.CutomerQuotaWarningItemForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * webapi 客户额度预警
 *
 */
@RequestMapping("/webapi/order/customerQuotaWarning")
@RestController
@Api(tags = "客户额度预警")
public class ApiCutomerQuotaWarningController {
    /* 打印日志 */
    protected Logger logger = LoggerFactory.getLogger(ApiCutomerQuotaWarningController.class);
    @Autowired
    private CutomerQuotaWarningService cutomerQuotaWarningService;
    @Autowired
    private RMQProducer rocketMQProducer;

    @ApiOperation(value = "查询列表数据 分页")
    @PostMapping(value="/listDTOByPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<CutomerQuotaWarningDTO> listDTOByPage(CutomerQuotaWarningForm form) {
        CutomerQuotaWarningDTO dto = new CutomerQuotaWarningDTO();
        try {
            dto.setBuId(form.getBuId());
            dto.setCustomerId(form.getCustomerId());
            dto.setPageSize(form.getPageSize());
            dto.setBegin(form.getBegin());

            User user = ShiroUtils.getUserByToken(form.getToken());
            dto = cutomerQuotaWarningService.listDTOByPage(dto,user);

        }catch(Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

    @ApiOperation(value = "查询详情表体列表分页")
    @PostMapping(value="/getItemListByPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<CutomerQuotaWarningItemDTO> getItemListByPage(CutomerQuotaWarningItemForm form) {
        CutomerQuotaWarningItemDTO dto = new CutomerQuotaWarningItemDTO();
        try {
            dto.setWaringId(form.getWaringId());
            dto.setType(form.getType());
            dto.setPageSize(form.getPageSize());
            dto.setBegin(form.getBegin());

            User user = ShiroUtils.getUserByToken(form.getToken());
            dto = cutomerQuotaWarningService.getItemListByPage(dto,user);

        }catch(Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }


    @ApiOperation(value = "导出")
    @GetMapping("/exportCustomerQuotaWarning.asyn")
    public void exportCustomerQuotaWarning(HttpServletResponse response, HttpServletRequest request,CutomerQuotaWarningForm form) throws Exception {

        CutomerQuotaWarningDTO dto = new CutomerQuotaWarningDTO();
        dto.setBuId(form.getBuId());
        dto.setCustomerId(form.getCustomerId());
        User user = ShiroUtils.getUserByToken(form.getToken());

        List<CutomerQuotaWarningDTO> dtoList = cutomerQuotaWarningService.listCutomerQuotaWarning(dto,user) ;

        String[] keys = {"buName", "customerName", "customerQuota", "saleNoshelfAmount", "nobillAmount","noconfirmAmount","noinvoiceAmount","noreturnAmount",
                "availableAmount", "currency", "createDate"} ;

        String[] cols = {"事业部", "客户名称", "客户总额度", "销售在途金额", "销售未结算金额","待确认账单金额","待开票金额","待回款金额",
                "可用额度", "币种", "数据刷新时间"} ;

        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList("客户额度预警", cols, keys, dtoList);

        FileExportUtil.export2007ExcelFile(wb, response, request, "客户额度预警");

    }

    @ApiOperation(value = "刷新")
    @PostMapping(value="/refreshCustomerQuotaWarning",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "令牌", required = true),
        @ApiImplicitParam(name = "buId", value = "事业部Id", required = true),
        @ApiImplicitParam(name = "customerId", value = "客户Id", required = true)
    })
    public ResponseBean<Map<String, Object>> refreshCustomerQuotaWarning(String token,String buId,String customerId){
        try{
            boolean isEmpty = new EmptyCheckUtils().addObject(buId).addObject(customerId).empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();

            Map<String, Object> body = new HashMap<String, Object>();
            body.put("buId", buId);
            body.put("customerId", customerId) ;

            JSONObject json = JSONObject.fromObject(body);
            System.out.println(json.toString());
            SendResult result = rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_CUSTOMER_QUATO_WARNING.getTopic(),MQOrderEnum.TIMER_CUSTOMER_QUATO_WARNING.getTags());
            System.out.println(result);
            if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
                resultMap.put("code", "01");
                resultMap.put("message", "刷新消息发送失败");
            }else{
                resultMap.put("code", "00");
                resultMap.put("message", "刷新成功");
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap) ;

        } catch (Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
        }

    }
}
