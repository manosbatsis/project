package com.topideal.report.webapi.reporting;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.report.service.reporting.BuFinanceInventorySummaryService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.BuFinanceInventorySummarySearchForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 财务进销存报表关账
 */
@RestController
@RequestMapping("/webapi/report/financeClose")
@Api(tags = "财务进销存报表关账")
public class APIFinanceCloseController {

    @Autowired
    public BuFinanceInventorySummaryService buservice;

    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listFnanceClose.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<BuFinanceInventorySummaryDTO> listFnanceClose(BuFinanceInventorySummarySearchForm form) {
        try {
        	BuFinanceInventorySummaryDTO dto=new BuFinanceInventorySummaryDTO();
            BeanUtils.copyProperties(form, dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            dto = buservice.getListDesc(dto,user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 关帐
     */
    @RequestMapping("/close.asyn")
    @ResponseBody
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "month", value = "日期", required = true),
            @ApiImplicitParam(name = "buId", value = "事业部Id", required = true)})
    public ResponseBean close(String month, Long buId,String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user=ShiroUtils.getUserByToken(token);
            Map<String, Object> map=buservice.closeReport(month,buId,user);
            String code=(String)map.get("code");
            String message=(String)map.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);

        } catch (Exception e) {
           e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }

    /**
     * 修改为未关账
     * @param month
     * @param buId
     * @return
     */
    @RequestMapping("/updateNotClose.asyn")
    @ResponseBody
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "month", value = "日期", required = true),
            @ApiImplicitParam(name = "buId", value = "事业部Id", required = true)})
    public ResponseBean updateNotClose(String month,Long buId,String token) {
        try {
            User user=ShiroUtils.getUserByToken(token);
            Map<String, Object> result=buservice.updateNotClose(month,buId,user);
            String code=(String)result.get("code");
            String message=(String)result.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


}
