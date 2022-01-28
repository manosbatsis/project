package com.topideal.order.webapi.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.entity.dto.bill.ReceiveCloseAccountsDTO;
import com.topideal.order.service.bill.ReceiveCloseAccountsService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.ReceiveCloseAccountForm;
import com.topideal.order.webapi.bill.form.ReceiveCloseAccountVerifyMonthForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/17 10:36
 * @Description: 应收关账
 */
@RestController
@RequestMapping("/webapi/order/receiveCloseAccount")
@Api(tags = "应收关账")
public class APIReceiveCloseAccountsController {

    @Autowired
    private ReceiveCloseAccountsService receiveCloseAccountsService;

    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<ReceiveCloseAccountsDTO> listByPage(ReceiveCloseAccountForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            ReceiveCloseAccountsDTO dto = new ReceiveCloseAccountsDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = receiveCloseAccountsService.listDTOByPage(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "更新关账与反关账")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true),
            @ApiImplicitParam(name = "operationType", value = "0-关账   1-反关账", required = true)})
    @PostMapping(value = "/updateAccountStatus.asyn")
    private ResponseBean updateAccountStatus(String token, Long id, String operationType) {
        try {
            User user = ShiroUtils.getUserByToken(token);

            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils()
                    .addObject(id).addObject(operationType).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            ReceiveCloseAccountsDTO dto = new ReceiveCloseAccountsDTO();
            dto.setMerchantId(user.getMerchantId());
            dto.setId(id);
            ResponseBean responseBean = receiveCloseAccountsService.updateAccountStatus(user, dto, operationType);
            return responseBean;
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "获取核销月份 (Tob/ToC)")
    @PostMapping(value = "/getVerifyMonth.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getVerifyMonth(ReceiveCloseAccountVerifyMonthForm form) {
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils()
                    .addObject(form.getReceiveDateStr()).addObject(form.getBuId()).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            User user = ShiroUtils.getUserByToken(form.getToken());
            ResponseBean responseBean = receiveCloseAccountsService.getVerifyMonth(user,form);
            return responseBean;
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "刷新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "month", value = "月份", required = true)})
    @PostMapping("/refresh.asyn")
    private ResponseBean refresh(String month,String token) {
        Map<String,Object> data = new HashMap<String,Object>();
        try{
            User user = ShiroUtils.getUserByToken(token);
            // 响应结果集
            boolean isNull = new EmptyCheckUtils().addObject(month).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            data = receiveCloseAccountsService.refresh(user, month);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, data);
    }


}
