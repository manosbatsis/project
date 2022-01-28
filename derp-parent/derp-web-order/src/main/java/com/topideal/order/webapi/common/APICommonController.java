package com.topideal.order.webapi.common;

import com.topideal.common.exception.DerpException;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.bill.OperateLogDTO;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.webapi.purchase.APIPurchaseOrderController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/webapi/order/common")
@Api(tags = "公共接口")
public class APICommonController {

	private static final Logger LOG = Logger.getLogger(APIPurchaseOrderController.class) ;

	@Autowired
	private CommonBusinessService commonBusinessService ;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取日志列表")
    @PostMapping(value = "/getOperateLogList.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "令牌", required = true),
        @ApiImplicitParam(name = "relCode", value = "关联单号", required = true),
        @ApiImplicitParam(name = "module", value = "模块"),
	})
	public ResponseBean<OperateLogDTO> getOperateLogList(String relCode, String module) {

		try {

			if(StringUtils.isBlank(relCode)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009.getCode(), "关联单号不能为空");
			}

			OperateLogModel queryModel = new OperateLogModel() ;
			queryModel.setRelCode(relCode);

			if(StringUtils.isNotBlank(module)) {
				queryModel.setModule(module);
			}

			List<OperateLogDTO> list = commonBusinessService.getOperateLogList(queryModel) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}
}
