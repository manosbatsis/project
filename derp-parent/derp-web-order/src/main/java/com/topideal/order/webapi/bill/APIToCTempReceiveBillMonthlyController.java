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
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.bill.TocTempCostBillItemMonthlyDTO;
import com.topideal.entity.dto.bill.TocTempReceiveBillItemMonthlyDTO;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.order.service.bill.ReceiveCloseAccountsService;
import com.topideal.order.service.bill.TocTempReceiveBillMonthlyService;
import com.topideal.order.service.timer.FileTaskService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.TocTempReceiveBillMonthlyForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/18 11:58
 * @Description: ToC 暂估月结快照
 */
@RestController
@RequestMapping("/webapi/order/tocTempReceiveBillMonthly")
@Api(tags = "Toc月结快照")
public class APIToCTempReceiveBillMonthlyController {

    @Autowired
    private TocTempReceiveBillMonthlyService tocTempReceiveBillMonthlyService;
    @Autowired
    private ReceiveCloseAccountsService receiveCloseAccountsService;
    @Autowired
    private FileTaskService fileTaskService;
    @Autowired
    private RMQProducer rocketMQProducer;

    private static final String ORDER_TYPE_RECEIVE = "RECEIVE";
    private static final String ORDER_TYPE_COST = "COST";

    @ApiOperation(value = "获取收入月结分页数据")
    @PostMapping(value = "/listReceiveByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listReceiveByPage(TocTempReceiveBillMonthlyForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TocTempReceiveBillItemMonthlyDTO dto = new TocTempReceiveBillItemMonthlyDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = tocTempReceiveBillMonthlyService.listReceiveByPage(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "获取费用月结分页数据")
    @PostMapping(value = "/listCostByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listCostByPage(TocTempReceiveBillMonthlyForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TocTempCostBillItemMonthlyDTO dto = new TocTempCostBillItemMonthlyDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = tocTempReceiveBillMonthlyService.listCostByPage(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "刷新")
    @PostMapping(value = "/refresh.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "month", value = "月结月份", required = true),
            @ApiImplicitParam(name = "orderType", value = "类型： 0-费用 1-收入", required = true)})
    private ResponseBean refreshMonthlySnapshot(String token, String month, String orderType) {
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
            body.put("orderType", orderType);
            body.put("month", month);

            JSONObject json = JSONObject.fromObject(body);
            SendResult result = rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_GENERATE_TOC_TEMP_MONTHLY_SNAPSHOT.getTopic(), MQOrderEnum.TIMER_GENERATE_TOC_TEMP_MONTHLY_SNAPSHOT.getTags());

            if (result == null || !result.getSendStatus().name().equals("SEND_OK")) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "刷新消息发送失败");
            }

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "收入月结导出",produces= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PostMapping(value="/exportReceiveMonthly.asyn")
    public ResponseBean exportReceiveMonthly(TocTempReceiveBillMonthlyForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("code", "00");
        retMap.put("message", "成功");

        try {
            //获取当前用户
            User user=ShiroUtils.getUserByToken(form.getToken());

            if(StringUtils.isBlank(form.getMonth())) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "月份不能未空");
            }

            String[] split = form.getMonth().split("-");
            String year = split[0];
            String month = split[1];

            TocTempReceiveBillItemMonthlyDTO dto = new TocTempReceiveBillItemMonthlyDTO();
            BeanUtils.copyProperties(form, dto);

            int exportItemNum = tocTempReceiveBillMonthlyService.countByDTO(dto, ORDER_TYPE_RECEIVE);
            if(exportItemNum <= 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
            }

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_TOC_MONTHLY_RECEIVE);
            taskModel.setTaskName("2C暂估收入月结快照");
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setState("1");
            taskModel.setRemark(year + "年" + month + "月2C暂估收入月结快照");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("month", form.getMonth());
            paramJson.put("storePlatformCode", form.getStorePlatformCode());
            paramJson.put("buId", form.getBuId());
            paramJson.put("shopCode", form.getShopCode());
            paramJson.put("customerId", form.getCustomerId());
            paramJson.put("shopTypeCode", form.getShopTypeCode());
            taskModel.setOwnMonth(form.getMonth());
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
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    @ApiOperation(value = "费用月结导出",produces= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PostMapping(value="/exportCostMonthly.asyn")
    public ResponseBean exportCostMonthly(TocTempReceiveBillMonthlyForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("code", "00");
        retMap.put("message", "成功");

        try {
            //获取当前用户
            User user=ShiroUtils.getUserByToken(form.getToken());

            if(StringUtils.isBlank(form.getMonth())) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "月份不能未空");
            }

            String[] split = form.getMonth().split("-");
            String year = split[0];
            String month = split[1];

            TocTempReceiveBillItemMonthlyDTO dto = new TocTempReceiveBillItemMonthlyDTO();
            BeanUtils.copyProperties(form, dto);

            int exportItemNum = tocTempReceiveBillMonthlyService.countByDTO(dto, ORDER_TYPE_COST);
            if(exportItemNum <= 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
            }

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_TOC_MONTHLY_COST);
            taskModel.setTaskName("2C暂估费用月结快照");
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setState("1");
            taskModel.setRemark(year + "年" + month + "月2C暂估费用月结快照");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("month", form.getMonth());
            paramJson.put("storePlatformCode", form.getStorePlatformCode());
            paramJson.put("buId", form.getBuId());
            paramJson.put("shopCode", form.getShopCode());
            paramJson.put("customerId", form.getCustomerId());
            paramJson.put("shopTypeCode", form.getShopTypeCode());
            taskModel.setOwnMonth(form.getMonth());
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
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }
}
