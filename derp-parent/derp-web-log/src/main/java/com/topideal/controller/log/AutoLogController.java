package com.topideal.controller.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.service.AutoLogService;

@RestController
@RequestMapping("/autoLog")
public class AutoLogController {
	
	@Autowired
	AutoLogService autoLogService ;
	
	@RequestMapping("/toPage.html")
	public ModelAndView toPage() throws Exception {
		ModelAndView modelAndView = new ModelAndView("auto-log");
		return modelAndView;
	}
	
	@RequestMapping("/list.asyn")
	public ViewResponseBean list(PageMongo pageMongo, String modelCode, String point, String keyword,
			String expType , String type ) {
		ViewResponseBean viewResponseBean = new ViewResponseBean();
		try {
			pageMongo = autoLogService.searchLog(pageMongo, modelCode, point, keyword, expType , type);
			viewResponseBean.setData(pageMongo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(pageMongo);
	}
	
	@RequestMapping("/batchResetSend.asyn")
	public ViewResponseBean batchResetSend(String ids) {
		try {
			autoLogService.batchResetSend(ids) ;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
}
