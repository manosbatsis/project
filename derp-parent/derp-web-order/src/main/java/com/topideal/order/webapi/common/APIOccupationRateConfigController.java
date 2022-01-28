package com.topideal.order.webapi.common;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.common.OccupationRateConfigDTO;
import com.topideal.order.service.common.OccupationRateConfigService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.form.OccupationRateConfigForm;
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

@RestController
@RequestMapping("/webapi/order/occupationRateConfig")
@Api(tags = "资金占用费率配置")
public class APIOccupationRateConfigController {
    @Autowired
    private OccupationRateConfigService occupationRateConfigService;

    @ApiOperation(value = "获取资金占用费率配置分页列表")
    @PostMapping(value = "/listDTOByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<OccupationRateConfigDTO> listDTOByPage(OccupationRateConfigForm form) {
        OccupationRateConfigDTO dto = new OccupationRateConfigDTO();
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());

            // 响应结果集
            dto = occupationRateConfigService.listDTOByPage(dto,user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
    @ApiOperation(value = "资金占用费率配置新增/编辑")
    @PostMapping(value = "/saveOccuptionRateCongfig.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean saveOccuptionRateCongfig(OccupationRateConfigForm form) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(form.getBuId()).addObject(form.getEffectiveDate())
                    .addObject(form.getExpirationDate()).addObject(form.getOccupationRate()).empty();
            if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            OccupationRateConfigDTO dto = new OccupationRateConfigDTO();
            BeanUtils.copyProperties(form,dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            if(form.getEffectiveDate() != null ){
                dto.setEffectiveDate(TimeUtils.parseFullTime(form.getEffectiveDate()));
            }
            if(form.getExpirationDate() != null ){
                dto.setExpirationDate(TimeUtils.parseFullTime(form.getExpirationDate()));
            }
            // 响应结果集
            occupationRateConfigService.saveOccuptionRateCongfig(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "选中的id集合，多个用逗号隔开", required = true)
    })
    @PostMapping(value = "/delOccuptionRateCongfig.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delOccuptionRateCongfig(String ids, String token) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
            if (isNull) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            // 响应结果集
            occupationRateConfigService.delOccuptionRateCongfig(ids);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }
}
