package com.topideal.order.webapi.common;


import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.common.PlatformCostConfigDTO;
import com.topideal.entity.dto.common.PlatformCostConfigItemDTO;
import com.topideal.entity.dto.common.PlatformSettlementConfigDTO;
import com.topideal.entity.dto.common.SettlementConfigDTO;
import com.topideal.order.service.common.PlatformCostConfigService;
import com.topideal.order.service.common.PlatformSettlementConfigService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.form.PlatformCostConfigForm;
import com.topideal.order.webapi.common.form.PlatformCostItemConfigForm;
import com.topideal.order.webapi.common.form.SettlementConfigBeanForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 平台费用配置
 */
@RestController
@RequestMapping("/webapi/order/platformCostConfig")
@Api(tags = "平台费用配置")
public class APIPlatformCostConfigController {
    @Autowired
    private PlatformCostConfigService platformCostConfigService;
    @Autowired
    private PlatformSettlementConfigService platformSettlementConfigService;


    /**
     * 获取平台费用配置分页列表
     * @param form
     * @return
     */
    @ApiOperation(value = "获取平台费用配置分页列表")
    @PostMapping(value = "/listByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<PlatformCostConfigDTO> listDTOByPage(PlatformCostConfigForm form) {
        PlatformCostConfigDTO dto = new PlatformCostConfigDTO();
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form, dto);
            if(form.getMerchantId() == null) {
                dto.setMerchantId(user.getMerchantId());
            }
            // 响应结果集
            dto = platformCostConfigService.listDTOByPage(dto,user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    /**
     * 复制
     * @param token
     * @param id
     * @return
     */
    @ApiOperation(value = "编辑/复制 获取选中信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "平台费用配置id", required = true)
    })
    @PostMapping(value = "/getDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<PlatformCostConfigDTO> getDetail(String token,Long id) {
        PlatformCostConfigDTO dto = new PlatformCostConfigDTO();
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            // 响应结果集
            dto = platformCostConfigService.searchDetail(id);
            if(dto.getEffectiveDate()!=null){
                dto.setEffectiveDateStr(TimeUtils.format(dto.getEffectiveDate(),"yyyy-MM"));
            }
            if(dto.getExpirationDate()!=null){
                dto.setExpirationDateStr(TimeUtils.format(dto.getExpirationDate(),"yyyy-MM"));
            }
            List<PlatformCostConfigItemDTO> itemList = platformCostConfigService.searchItemDetail(id);
            if(itemList.size()>0){
                for(PlatformCostConfigItemDTO item:itemList){
                    item.setId(null);
                }
            }
            dto.setItemList(itemList);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    /**
     * 平台费用配置保存/审核
     * @param form
     * @return
     */
    @ApiOperation(value = "平台费用配置保存/审核")
    @PostMapping(value = "/savePlatFormConfig.asyn")
    public ResponseBean savePlatFormConfig(@RequestBody PlatformCostItemConfigForm form) {
        try {
            PlatformCostConfigDTO dto = new PlatformCostConfigDTO();
            boolean isNull = new EmptyCheckUtils().addObject(form.getMerchantId()).addObject(form.getBuId())
                    .addObject(form.getCustomerId()).addObject(form.getEffectiveDateStr())
                    .addObject(form.getExpirationDateStr()).addObject(form.getOperate()).empty();
            if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            BeanUtils.copyProperties(form,dto);
            if(StringUtils.isNotBlank(form.getEffectiveDateStr())) {
                dto.setEffectiveDate(TimeUtils.parseFullTime(form.getEffectiveDateStr()+"-01 00:00:00"));
            }else{
                dto.setEffectiveDate(TimeUtils.parseFullTime(TimeUtils.format(new Date(),"yyyy-MM")+"-01 00:00:00"));
            }
            if(StringUtils.isNotBlank(form.getExpirationDateStr())) {
                dto.setExpirationDate(TimeUtils.parseFullTime(form.getExpirationDateStr()+"-01 00:00:00"));
            }

            User user = ShiroUtils.getUserByToken(form.getToken());
            // 响应结果集
            Map map=platformCostConfigService.saveOrAudit(dto, user, form.getOperate());
            String message=(String)map.get("message");
            String code=(String)map.get("code");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);//失败
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    /**
     * 删除
     * @param token
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "平台费用配置id集合，多个用逗号隔开", required = true)
    })
    @PostMapping(value = "/delPlatFormConfig.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<PlatformCostConfigDTO> delSdSaleConfig(String token,String ids) {
        PlatformCostConfigDTO dto = new PlatformCostConfigDTO();
        try {
            boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
            if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            // 响应结果集
            Map map= platformCostConfigService.delSdSaleConfig(ids);
            String message=(String)map.get("message");
            String code=(String)map.get("code");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);//失败
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 获取下拉框
     * @return
     */
    @ApiOperation(value = "获取下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "storePlatformCode", value = "电商平台编码", required = true)
    })
    @PostMapping(value = "/getSelectBean.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getSelectBean(String token,String storePlatformCode) {
        List<PlatformSettlementConfigDTO> result = new ArrayList<PlatformSettlementConfigDTO>();
        try{
            // 响应结果集
            result = platformSettlementConfigService.getSelectBean(storePlatformCode);
        }catch(Exception e){
            e.printStackTrace();
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
    }

    /**
     * 反审
     * @param token
     * @param id
     * @return
     */
    @ApiOperation(value = "反审")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "平台费用id", required = true)
    })
    @PostMapping(value = "/counterTrial.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean counterTrial(String token,Long id) {
        PlatformCostConfigDTO dto = new PlatformCostConfigDTO();
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            // 响应结果集
            User user=ShiroUtils.getUserByToken(token);

            Map map= platformCostConfigService.counterTrial(id,user);
            String message=(String)map.get("message");
            String code=(String)map.get("code");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);//失败
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }

    }

}
