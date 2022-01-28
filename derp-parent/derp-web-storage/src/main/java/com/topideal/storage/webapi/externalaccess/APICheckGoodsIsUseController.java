package com.topideal.storage.webapi.externalaccess;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.storage.service.adjustmenttype.CheckGoodsIsUseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 校验商品是否被使用
 */
@RestController
@RequestMapping("/webapi/storage/checkGoodsByStorage")
@Api(tags = "校验商品")
public class APICheckGoodsIsUseController {

	// 采购订单service
	@Autowired
	private CheckGoodsIsUseService checkGoodsIsUseService;
	
	/**
	 * 校验商品是否被使用
	 */
	@ApiOperation(value = "校验商品是否被使用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的商品ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/checkGoods.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Boolean> exportOrder(String token,String ids) throws Exception {
		boolean flag = true;
		try {
			if (StringUtils.isBlank(ids)) {				
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			List<Long> list = StrUtils.parseIds(ids);
			flag = checkGoodsIsUseService.checkGoodsIsUse(list);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,flag);
	}
	
}
