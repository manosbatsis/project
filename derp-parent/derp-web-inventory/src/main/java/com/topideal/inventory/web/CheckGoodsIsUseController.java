package com.topideal.inventory.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.inventory.service.CheckGoodsIsUseService;
import com.topideal.inventory.shiro.ShiroUtils;

/**
 * 校验是否被使用
 */
@RequestMapping("/checkGoodsByInventory")
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
	
	/**
	 * 库存数据校验 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/checkDepot.asyn")
	@ResponseBody
	private ViewResponseBean checkDepot(String ids)
			throws Exception {
		boolean flag = true;
		try {
			User user= ShiroUtils.getUser();
			
			Long merchantId = null ;
			
			/*if(user.getUserType() != "1") {
				merchantId = user.getMerchantId() ;
			}*/
			
			List<Long> list = StrUtils.parseIds(ids);
			flag = checkGoodsIsUseService.checkDepotIsUse(list);
		}catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(flag);
	}
	
}
