package com.topideal.web.derp.main;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.main.CustomerMainDTO;
import com.topideal.service.main.CustomerMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * 客户主服务信息 控制层
 * @author zhanghx
 */
@RequestMapping("/customerMain")
@Controller
public class CustomerMainController {
	
	// 客户信息service
	@Autowired
	private CustomerMainService customerMainService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/main/customer-main-list";
	}

	
	/**
	 * 访问详情页面
	 * */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception{
		CustomerMainDTO customerInfo = customerMainService.searchDetail(id);
		model.addAttribute("detail", customerInfo);
		return "/derp/main/customer-main-details";
	}

	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listCustomerMain.asyn")
	@ResponseBody
	private ViewResponseBean listCustomer(CustomerMainDTO dto) {
		try{
			// 响应结果集
			dto = customerMainService.listCustomerMain(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
}
