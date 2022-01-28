package com.topideal.order.web.externalaccess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.order.service.purchase.CheckGoodsIsUseService;

/**
 * 校验商品是否被使用
 */
@RequestMapping("/checkGoods")
@Controller
public class CheckGoodsIsUseController {

	// 采购订单service
	@Autowired
	private CheckGoodsIsUseService checkGoodsIsUseService;
	
	/**
	 * 校验商品是否被使用
	 */
	@RequestMapping("/checkGoods.asyn")
	@ResponseBody
	private ViewResponseBean exportOrder(String ids)
			throws Exception {
		boolean flag = true;
		try {
			List<Long> list = StrUtils.parseIds(ids);
			flag = checkGoodsIsUseService.checkGoodsIsUse(list);
		}catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(flag);
	}
	
}
