package com.topideal.webapi.main;


import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.main.CostPriceDifferenceDTO;
import com.topideal.service.main.CostPriceDifferenceService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.CostPriceDifferenceForm;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/system/costPriceDifference")
@Api(tags = "成本单价差异表")
public class APICostPriceDifferenceController {

    private static final String[] MAIN_COLUMNS = {"公司主体", "事业部名称", "条形码", "商品名称", "类型", "币种", "固定成本单价", "采购价盘", "成本价差"};

    private static final String[] MAIN_KEYS = {"merchantName", "buName", "barcode", "goodsName", "stockLocationTypeName", "currency", "fixedCostStr", "purchasePriceStr", "agio"};

    @Autowired
    private CostPriceDifferenceService costPriceDifferenceService;

    @Autowired
    private RMQProducer rocketMQProducer;

    @ApiOperation(value = "获取成本单价差异表列表")
    @PostMapping(value = "/listCostPriceDifference.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<CostPriceDifferenceDTO> listCostPriceDifference(CostPriceDifferenceForm form) {

        try {
            CostPriceDifferenceDTO dto = new CostPriceDifferenceDTO();
            User user = ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());

            dto = costPriceDifferenceService.listCostPriceDifferenceDTO(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }


    @ApiOperation(value = "导出", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value = "/exportCostPriceDifference.asyn")
    private ResponseBean exportCostPriceDifference(HttpServletResponse response, HttpServletRequest request, CostPriceDifferenceForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());

            CostPriceDifferenceDTO dto = new CostPriceDifferenceDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());

            List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>();

            // 响应结果集
            List<CostPriceDifferenceDTO> mainList = costPriceDifferenceService.getExportMainList(dto, user);

            String mainSheetName = "成本单价差异";

            ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, MAIN_COLUMNS, MAIN_KEYS, mainList);
            sheetList.add(mainSheet);

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList);
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @ApiOperation(value = "刷新")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true)})
    @PostMapping(value = "/refreshCostPriceDifference.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean refreshCostPriceDifference(String token) {
        try {

            User user = ShiroUtils.getUserByToken(token);

            //发送mq消息
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("merchantId", user.getMerchantId());

            JSONObject json = JSONObject.fromObject(body);
            SendResult result = rocketMQProducer.send(json.toString(), MQErpEnum.GENERATE_COST_PRICE_DIFFERENCE.getTopic(), MQErpEnum.GENERATE_COST_PRICE_DIFFERENCE.getTags());

            if (result == null || !result.getSendStatus().name().equals("SEND_OK")) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "刷新消息发送失败");
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

}
