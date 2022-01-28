package com.topideal.order.webapi.bill;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.bill.PlatformStatementItemDTO;
import com.topideal.entity.dto.bill.PlatformStatementOrderDTO;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.order.service.bill.PlatformStatementOrderService;
import com.topideal.order.service.timer.FileTaskService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.InvoiceTempForm;
import com.topideal.order.webapi.bill.form.PlatformStatementOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/order/platformStatementOrder")
@Api(tags = "平台结算单")
public class APIPlatformStatementOrderController {

    private static final Logger LOG = Logger.getLogger(APIPlatformStatementOrderController.class);

    private static final String[] KEYS = {"billCode", "month", "customerName", "typeLabel", "poNo", "barcode", "goodsName", "settlementNum", "settlementAmount", "settlementAmountRmb", "currency", "rate"};

    private static final String[] COLS = {"平台结算单号", "账单月份", "客户", "类型", "PO号", "商品条码", "商品名称", "结算数量", "结算金额(本币)", "结算金额(RMB)", "币种", "汇率"};

    @Autowired
    private PlatformStatementOrderService platformStatementOrderService;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private FileTaskService fileTaskService;


    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listPlatformStatementOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<PlatformStatementOrderDTO> listPlatformStatementOrder(PlatformStatementOrderForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            PlatformStatementOrderDTO dto = new PlatformStatementOrderDTO();
            BeanUtils.copyProperties(form, dto);
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            dto = platformStatementOrderService.listPlatformStatementOrder(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "根据平台结算单ID获取详情")
    @PostMapping("/getDetailsById.asyn")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "平台结算单id", required = true)})
    public ResponseBean<PlatformStatementOrderDTO> getDetailsById(@RequestParam(value = "token", required = true) String token, Long id) throws Exception {
        try {
            PlatformStatementOrderDTO dto = platformStatementOrderService.getDetails(id);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "根据平台结算单ID分页获取表体")
    @PostMapping("/listPlatformStatementItem.asyn")
    private ResponseBean<PlatformStatementItemDTO> listPlatformStatementItem(PlatformStatementOrderForm form) {

        try {
            PlatformStatementItemDTO dto = new PlatformStatementItemDTO();
            dto.setPlatformStatementOrderId(form.getPlatformStatementOrderId());
            // 响应结果集
            dto = platformStatementOrderService.listPlatformStatementItem(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "导出平台结算单")
    @PostMapping("/exportOrders.asyn")
    private ResponseBean exportOrders(PlatformStatementOrderForm form) throws Exception {

        if (StringUtils.isBlank(form.getMonth()) && StringUtils.isBlank(form.getIds())) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        }

        User user = ShiroUtils.getUserByToken(form.getToken());

        try {
            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_PLATFORMSTATEMENT);
            taskModel.setTaskName("平台结算单");
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setState("1");
            taskModel.setRemark("平台结算单导出");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("billCode", form.getBillCode());
            paramJson.put("customerType", form.getCustomerType());
            paramJson.put("isInvoice", form.getIsInvoice());
            paramJson.put("month", form.getMonth());
            paramJson.put("receiveCode", form.getReceiveCode());
            paramJson.put("ids", form.getIds());
            taskModel.setOwnMonth(form.getMonth() == null ? TimeUtils.formatMonth(new Date()) : form.getMonth());
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
            taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

	@ApiOperation(value = "刷新平台结算单")
	@PostMapping(value = "/flashPlatformStatementOrders.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean flashPlatformStatementOrders(PlatformStatementOrderForm form) {
        try {
			PlatformStatementOrderDTO dto = new PlatformStatementOrderDTO();
			BeanUtils.copyProperties(form, dto);

			if (StringUtils.isBlank(dto.getMonth())) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009.getCode(), "账单月份不能为空");
            }

            if (StringUtils.isBlank(dto.getCustomerType())) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009.getCode(), "客户不能为空");
            }

            User user = ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());

            //发送mq消息
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("merchantId", user.getMerchantId());
            body.put("month", dto.getMonth());
            body.put("customerType", dto.getCustomerType());
            body.put("billCode", dto.getBillCode() == null ? "" : dto.getBillCode());

            JSONObject json = JSONObject.fromObject(body);
            SendResult result = rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_PLATFORM_STATEMENT_ORDER.getTopic(), MQOrderEnum.TIMER_PLATFORM_STATEMENT_ORDER.getTags());
            LOG.info(result);

            if (result == null || !result.getSendStatus().name().equals("SEND_OK")) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "刷新消息发送失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }


    @ApiOperation(value = "校验平台结算单是否满足开票条件")
    @PostMapping(value = "/validateInfo.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "平台结算单id集合，多个用英文逗号隔开", required = true)})
    public ResponseBean validateInfo(@RequestParam(value = "token", required = true) String token, String ids) {
        try {
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            Map<String, String> resultMap = platformStatementOrderService.validate(list);
            String code = resultMap.get("code");
            String message = resultMap.get("msg");
            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "生成ToC应收账单")
    @PostMapping(value = "/saveToCReceiveBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "平台结算单id集合，多个用英文逗号隔开", required = true)})
    public ResponseBean saveToCReceiveBill(String ids, String token) throws Exception {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user = ShiroUtils.getUserByToken(token);
            List list = StrUtils.parseIds(ids);
            Map<String, String> resultMap = platformStatementOrderService.saveToCReceiveBill(list, user);
            String code = resultMap.get("code");
            String message = resultMap.get("msg");
            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "平台结算单开票")
    @PostMapping(value = "/toInvoicePage.asyn")
    public ResponseBean toInvoicePage(InvoiceTempForm form) {

        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            //返回发票模板的html
            String excelHtml = platformStatementOrderService.generateInvoiceHtml(form.getTempId(), form.getIds(), form.getInvoiceStatus(), user);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, excelHtml);

        } catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }

}
