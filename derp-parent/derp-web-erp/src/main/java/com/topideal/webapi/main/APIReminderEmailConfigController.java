package com.topideal.webapi.main;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.main.*;
import com.topideal.entity.vo.user.RoleInfoModel;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.service.main.ReminderEmailConfigService;
import com.topideal.service.main.ReminderEmailRelService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.ReminderEmailConfigEnumDTO;
import com.topideal.webapi.form.ReminderEmailConfigForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮件提醒列表
 */
@RequestMapping("/webapi/system/reminderEmailConfig")
@RestController
@Api(tags = "邮件提醒列表信息 ")
public class APIReminderEmailConfigController {
    @Autowired
    ReminderEmailConfigService reminderEmailConfigService;
    @Autowired
    ReminderEmailRelService reminderEmailRelService;
    @Autowired
    private RMQProducer rocketMQProducer;

    /**
     * 获取分页数据
     *
     * @return
     */
    @ApiOperation(value = "获取列表分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "begin", value = "开始位置", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
            @ApiImplicitParam(name = "merchantId", value = "商家id"),
            @ApiImplicitParam(name = "buId", value = "事业部id"),
            @ApiImplicitParam(name = "businessModuleType", value = "业务模块"),
    })
    @PostMapping(value = "/listReminderEmail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listReminderEmail(String token, Long merchantId, Long buId, String businessModuleType, int begin, int pageSize) throws Exception {
        ReminderEmailConfigDTO dto = new ReminderEmailConfigDTO();
        try {
            dto.setBuId(buId);
            dto.setMerchantId(merchantId);
            dto.setBusinessModuleType(businessModuleType);
            dto.setBegin(begin);
            dto.setPageSize(pageSize);
            // 响应结果集
            dto = reminderEmailConfigService.getListByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true)
    })
    @PostMapping(value = "/delReminderEamil.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delReminderEamil(long id) {
        try {
            Map<String, Object> map = reminderEmailConfigService.deleteReminderEmailConfig(id);
            String code = (String) map.get("code");
            String message = (String) map.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/saveReminderEmail.asyn")
    public ResponseBean saveReminderEmail(@RequestBody ReminderEmailConfigForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            Map<String, Object> resultMap = reminderEmailConfigService.saveReminderEmailConfig(user, form);
            String code = (String) resultMap.get("code");
            String message = (String) resultMap.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    @ApiOperation(value = "修改")
    @PostMapping(value = "/modifyReminderEmail.asyn")
    public ResponseBean modifyReminderEmail(@RequestBody ReminderEmailConfigForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            Map<String, Object> resultMap = reminderEmailConfigService.updateReminderEmailConfig(user, form);
            String code = (String) resultMap.get("code");
            String message = (String) resultMap.get("message");
            if ("00".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/getAddEditInfo.asyn")
    @ApiOperation(value = "访问邮件提醒新增/编辑")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    public ResponseBean<ReminderEmailConfigResponseDTO> getAddPageInfo(@RequestParam(value = "token", required = true) String token,
                                                                       String id) {
        try {
            ReminderEmailConfigResponseDTO dto = new ReminderEmailConfigResponseDTO();
            dto.setBusinessModuleValue(DERP_SYS.reminderEmailConfigReminder_BuisList);
            dto.setReminderTypeValue(DERP_SYS.reminderEmailConfigReminder_AllTypeList);
            dto.setBillReminderTypeValue(DERP_SYS.receiveEmailConfigReminder_TypeList);
            dto.setReminderChannelValue(DERP_SYS.reminderEmailConfigReminder_ChannelList);
            dto.setReminderUserTypeValue(DERP_SYS.reminderEmailConfigReminder_AllUserTypeList);

            if (!StringUtils.isEmpty(id)) {
                ReminderEmailConfigDTO configDTO = reminderEmailConfigService.getById(Long.valueOf(id));
                dto.setConfigDTO(configDTO);
                List<ReminderEmailRelDTO> listReminderEmailRel = reminderEmailRelService.getConfigById(configDTO.getId());
                dto.setItems(listReminderEmailRel);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/getReminderByInfo.asyn")
    @ApiOperation(value = "根据业务模块类型获取操作节点，单据对象")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    public ResponseBean<ReminderEmailConfigEnumDTO> getReminderType(@RequestParam(value = "token", required = true) String token,
                                                                    String businessModuleType) {
        try {
            ReminderEmailConfigEnumDTO dto = new ReminderEmailConfigEnumDTO();
            if (DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1.equals(businessModuleType)) {
                dto.setReminderTypeValue(DERP_SYS.reminderEmailConfigReminder_ReceviceTypeList);
                dto.setReminderUserTypeValue(DERP_SYS.reminderEmailConfigReminder_BillTypeList);
            }
            if (DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2.equals(businessModuleType)) {
                dto.setReminderTypeValue(DERP_SYS.reminderEmailConfigReminder_PurchaseTypeList);
                dto.setReminderUserTypeValue(DERP_SYS.reminderEmailConfigReminder_UserPurchaseTypeList);
            }
            if (DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_3.equals(businessModuleType)) {
                dto.setReminderTypeValue(DERP_SYS.reminderEmailConfigReminder_SaleTypeList);
                dto.setReminderUserTypeValue(DERP_SYS.reminderEmailConfigReminder_UserSaleTypeList);
            }
            if (DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_4.equals(businessModuleType)) {
                dto.setReminderTypeValue(DERP_SYS.reminderEmailConfigReminder_PurchasePriceTypeList);
                dto.setReminderUserTypeValue(DERP_SYS.reminderEmailConfigReminder_UserPurchasePriceTypeList);
            }
            if (DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_5.equals(businessModuleType)) {
                dto.setReminderTypeValue(DERP_SYS.reminderEmailConfigReminder_SalePriceTypeList);
                dto.setReminderUserTypeValue(DERP_SYS.reminderEmailConfigReminder_UserSalePriceTypeList);
            }
            if (DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_6.equals(businessModuleType)) {
                dto.setReminderTypeValue(DERP_SYS.reminderEmailConfigReminder_SaleCreditTypeList);
                dto.setReminderUserTypeValue(DERP_SYS.reminderEmailConfigReminder_UserSaleCreditTypeList);
            }
            if (DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_7.equals(businessModuleType)) {
                dto.setReminderTypeValue(DERP_SYS.reminderEmailConfigReminder_PurchaseOrderTypeList);
                //dto.setReminderUserTypeValue(DERP_SYS.reminderEmailConfigReminder_UserSaleCreditTypeList);
            }
            if (DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8.equals(businessModuleType)) {
                dto.setReminderTypeValue(DERP_SYS.reminderEmailConfigReminder_PaymentOrderTypeList);
                dto.setReminderUserTypeValue(DERP_SYS.reminderEmailConfigReminder_UserPaymentOrderTypeList);
            }
            if (DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9.equals(businessModuleType) || DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1.equals(businessModuleType)) {
                dto.setReminderTypeValue(DERP_SYS.reminderEmailConfigReminder_ReceviceTypeList);
                dto.setReminderUserTypeValue(DERP_SYS.reminderEmailConfigReminder_BillTypeList);
            }
            if (DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10.equals(businessModuleType)) {
                dto.setReminderTypeValue(DERP_SYS.reminderEmailConfigReminder_AdvanceBillTypeList);
                dto.setReminderUserTypeValue(DERP_SYS.reminderEmailConfigReminder_UserAdvanceBillTypeList);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "获取业务模块数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value = "/getBusinessModuleType.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean getBusinessModuleType() {
        List list = new ArrayList();
        try {
            list = DERP_SYS.reminderEmailConfigReminder_BuisList;
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    @ApiOperation(value = "获取启用状态角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="/getRoleList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean getRoleList() {

        List<RoleInfoModel> roleList = null ;
        try {
            roleList = reminderEmailConfigService.getRoleList() ;
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,roleList);//成功
        } catch (SQLException e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    @ApiOperation(value = "获取启用状态用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="/getUserList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean getUserList() {

        List<UserInfoModel> userList = null ;
        try {
            userList = reminderEmailConfigService.getUserList() ;
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,userList);//成功
        } catch (SQLException e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
}
