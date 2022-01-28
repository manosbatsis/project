package com.topideal.web.derp.main;/*
package com.topideal.web.derp.main;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.main.MiningMarketingPriceDTO;
import com.topideal.entity.vo.main.MiningMarketingPriceModel;
import com.topideal.service.main.MiningMarketingPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

*/
/**
 * 采销报价
 * @author zhanghx
 *//*

@RequestMapping("/mining")
@Controller
public class MiningMarketingPriceController {

	//采销报价
	@Autowired
	private MiningMarketingPriceService miningMarketingPriceService;
	
	*/
/**
	 * 访问列表页面
	 *//*

	@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/main/mining-marketing-price-list";
	}
	
	*/
/**
	 * 获取分页数据
	 * @param model  
	 * @return
	 *//*

	@RequestMapping("/list.asyn")
	@ResponseBody
	private ViewResponseBean list(MiningMarketingPriceDTO dto) {
		try{
			// 响应结果集
			dto = miningMarketingPriceService.listByPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
	
}
*/
