package com.topideal.order.webapi.externalaccess;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.order.service.purchase.CheckGoodsIsUseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 校验商品是否被使用
 */
@RestController
@RequestMapping("/webapi/order/checkGoods")
@Api(tags = "checkGoods-校验商品是否被使用接口")
public class APICheckGoodsIsUseController {

	// 采购订单service
	@Autowired
	private CheckGoodsIsUseService checkGoodsIsUseService;
	
	/**
	 * 校验商品是否被使用
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/checkGoods.asyn")
	@ApiOperation(value = "校验商品是否被使用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "商品ID，多个以','隔开", required = true)
	})
	private ResponseBean<Boolean> exportOrder(@RequestParam(value = "token", required = true)String token, 
			String ids)  {
		
		if(StringUtils.isBlank(ids)){
            //输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//文件不能为空
        }
		
		boolean flag = true;
		
		try {
			
			List<Long> list = StrUtils.parseIds(ids);
			flag = checkGoodsIsUseService.checkGoodsIsUse(list);
			
		}catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
		
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, flag) ;
	}
	
}
