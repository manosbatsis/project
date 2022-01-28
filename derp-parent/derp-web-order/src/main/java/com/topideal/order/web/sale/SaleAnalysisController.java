package com.topideal.order.web.sale;

import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.sale.SaleAnalysisDTO;
import com.topideal.order.service.sale.SaleAnalysisService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 销售出库分析 controller
 * 
 */
@RequestMapping("/saleAnalysis")
@Controller
public class SaleAnalysisController {

	// 销售出库分析service
	@Autowired
	private SaleAnalysisService saleAnalysisService;
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		return "/derp/sale/saleanalysis-list";
	}
	

	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listSaleAnalysis.asyn")
	@ResponseBody
	private ViewResponseBean listSaleAnalysis(HttpSession session, SaleAnalysisDTO dto,String endDateStr) {
		try{
			User user= ShiroUtils.getUser(); 
			//设置时间相关的条件
			if(endDateStr != null){
				Timestamp endDate = Timestamp.valueOf(endDateStr+" 00:00:00"); 
				dto.setEndDate(endDate);
			}
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = saleAnalysisService.listSaleAnalysisDTO(dto,user);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
}
