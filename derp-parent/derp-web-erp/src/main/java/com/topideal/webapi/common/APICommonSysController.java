package com.topideal.webapi.common;


import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.base.OperateSysLogDTO;
import com.topideal.service.base.OperateSysLogService;
import com.topideal.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/webapi/system/common")
@Api(tags = "sys公共接口")
public class APICommonSysController {

    private static final Logger LOG = Logger.getLogger(APICommonSysController.class) ;

    @Autowired
    private OperateSysLogService operateSysLogService;

    @ApiOperation(value = "根据id查看日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "业务id", required = true),
            @ApiImplicitParam(name = "module", value = "类型 1.平台备案商品管理 2：采购价格管理 3：销售价格管理 4.商品列表 5.品牌管理", required = true)
    })
    @PostMapping(value="/getOperateLogSysList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<OperateSysLogDTO> toDetailsLogById(Long id,String module, String token) throws Exception {
        try{
            User user = ShiroUtils.getUserByToken(token);
            OperateSysLogDTO operateSysLogDTO=new OperateSysLogDTO();
            operateSysLogDTO.setRelId(id);
            operateSysLogDTO.setModule(module);
            List<OperateSysLogDTO> dto=operateSysLogService.getOperateSysLog(operateSysLogDTO);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        }catch (Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


}
