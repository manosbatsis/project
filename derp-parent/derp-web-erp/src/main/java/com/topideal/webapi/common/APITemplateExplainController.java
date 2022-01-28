package com.topideal.webapi.common;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.constant.DerpBasic;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.service.common.TemplateExplainService;
import com.topideal.webapi.common.dto.TemplateExplainDTO;
import com.topideal.webapi.common.form.TemplateExplainForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/10/29 11:48
 * @Description: 模板说明
 */
@RestController
@RequestMapping("/webapi/system/templateExplain")
@Api(tags = "模板说明")
public class APITemplateExplainController {

    private static final Logger LOG = Logger.getLogger(APITemplateExplainController.class);

    @Autowired
    private TemplateExplainService templateExplainService;

    @ApiOperation(value = "获取列表数据")
    @PostMapping(value="/listExplain.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<TemplateExplainDTO> listExplain(TemplateExplainForm form) {

        if (StringUtils.isBlank(form.getToken())) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        }

        // 必填项空值校验
        boolean isNull = new EmptyCheckUtils()
                .addObject(form.getType())
                .empty();

        if (isNull) {
            // 输入信息不完整
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10018);
        }

        try {
            TemplateExplainDTO dto = new TemplateExplainDTO();
            BeanUtils.copyProperties(form,dto);
            List<TemplateExplainDTO> templateExplainDTOS = templateExplainService.listByDTO(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, templateExplainDTOS);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "获取模板说明类目")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true)})
    @PostMapping(value="/listTemplateExplainCategory.asyn", consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<DerpBasic> listTemplateExplainCategory(String token) {

        if (StringUtils.isBlank(token)) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        }

        try {
            ArrayList<DerpBasic> templateExplainCategoryList = DERP_SYS.getConstantListByName("templateExplainCategoryList");
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, templateExplainCategoryList);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }
}
