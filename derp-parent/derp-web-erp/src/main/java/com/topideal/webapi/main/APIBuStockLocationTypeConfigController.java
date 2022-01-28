package com.topideal.webapi.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.entity.dto.main.BuStockLocationTypeConfigDTO;
import com.topideal.entity.vo.main.BuStockLocationTypeConfigModel;
import com.topideal.service.main.BuStockLocationTypeConfigService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.BuStockLocationTypeConfigForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/webapi/system/buStockLocationTypeConfig")
@Api(tags = "事业部库位类型配置")
public class APIBuStockLocationTypeConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIBuStockLocationTypeConfigController.class);

    @Autowired
    private BuStockLocationTypeConfigService service;

    @ApiOperation(value = "获取事业部库位类型分页数据")
    @PostMapping(value = "/listByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listByPage(BuStockLocationTypeConfigForm form) {
        try {
            boolean isEmpty = new EmptyCheckUtils()
                    .addObject(form.getToken())
                    .empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            User user = ShiroUtils.getUserByToken(form.getToken());
            BuStockLocationTypeConfigDTO dto = new BuStockLocationTypeConfigDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = service.listByPage(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    /**
     * 获取下拉列表
     * @return
     */
    @ApiOperation(value = "通过开店公司+开店事业部获取下拉列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "merchantId", value = "公司ID"),
            @ApiImplicitParam(name = "buId", value = "事业部ID", readOnly = true)
    })
    @PostMapping(value="/getSelectBeanByBu.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean getSelectBeanByBu(String token, Long merchantId, Long buId) {
        List<SelectBean> result = new ArrayList<SelectBean>();
        try {
            boolean isEmpty = new EmptyCheckUtils()
                    .addObject(buId).addObject(token)
                    .empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user = ShiroUtils.getUserByToken(token);
            BuStockLocationTypeConfigDTO dto = new BuStockLocationTypeConfigDTO();
            dto.setBuId(buId);
            if(merchantId == null) {
                dto.setMerchantId(user.getMerchantId());
            }else {
                dto.setMerchantId(merchantId);
            }
            result = service.getSelectBeanByBu(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    @ApiOperation(value = "新增事业部库位类型")
    @PostMapping(value = "/addStockLocationType.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean addStockLocationType(BuStockLocationTypeConfigForm form) {
        try {
            boolean isEmpty = new EmptyCheckUtils()
                    .addObject(form.getBuId()).addObject(form.getName())
                    .empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            User user = ShiroUtils.getUserByToken(form.getToken());
            BuStockLocationTypeConfigDTO dto = new BuStockLocationTypeConfigDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto.setMerchantName(user.getMerchantName());
            dto.setCreater(user.getId());
            dto.setCreaterName(user.getName());
            service.addStockLocationType(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true),
            @ApiImplicitParam(name = "status", value = "状态 (0:禁用 1：启用)", required = true)
    })
    @PostMapping(value = "/modifyStockLocationType.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean modifyStockLocationType(String token, Long id, String status) {
        try {
            boolean isEmpty = new EmptyCheckUtils()
                    .addObject(id).addObject(token)
                    .empty();

            if(isEmpty){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user = ShiroUtils.getUserByToken(token);
            BuStockLocationTypeConfigModel model = new BuStockLocationTypeConfigModel();
            model.setToken(token);
            model.setStatus(status);
            model.setId(id);
            service.modifyStockLocationType(user,model);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, model);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

}
