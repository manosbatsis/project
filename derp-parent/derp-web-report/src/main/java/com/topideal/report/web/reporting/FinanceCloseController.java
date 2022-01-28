package com.topideal.report.web.reporting;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.report.service.reporting.BuFinanceInventorySummaryService;
import com.topideal.report.shiro.ShiroUtils;

/**
 * 财务进销存报表关账
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/financeClose")
public class FinanceCloseController {
	

	@Autowired
	public BuFinanceInventorySummaryService buservice;
	
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPageJb(Model model)throws Exception  {
		return "derp/reporting/financialClose-list";
	}
	
	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/financeSummaryList.asyn")
	@ResponseBody
	private ViewResponseBean financeSummaryJbList(BuFinanceInventorySummaryDTO model) {
		try{
			// 响应结果集
			User user=ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			model=buservice.getListDesc(model,user);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	/**
	 * 关帐
	 * @param id
	 */
	@RequestMapping("/close.asyn")
	@ResponseBody
	public ViewResponseBean submitDeclareOrder(String month,Long buId) {
		Map<String, Object> result = new HashMap<>();
		try {
			User user=ShiroUtils.getUser();			
			result=buservice.closeReport(month,buId,user);
		
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}
	
	/**
	 * 修改为未关账
	 * @param month
	 * @param buId
	 * @return
	 */
	@RequestMapping("/updateNotClose.asyn")
	@ResponseBody
	public ViewResponseBean updateNotClose(String month,Long buId) {
		Map<String, Object> result = new HashMap<>();
		try {
			User user=ShiroUtils.getUser();			
			result=buservice.updateNotClose(month,buId,user);
		
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}
	
	

	
}
