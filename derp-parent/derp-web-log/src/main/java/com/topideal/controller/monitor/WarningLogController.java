package com.topideal.controller.monitor;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.LogWarningMqDTO;
import com.topideal.entity.vo.LogWarningMqModel;
import com.topideal.service.WarningLogService;

/**
 * mq日志预警-启用
 * @author zhanghx
 */
@RestController
@RequestMapping("/warninglog")
public class WarningLogController {

	@Autowired
	private WarningLogService service;
	
	/**
	 * 跳转分页
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toPage.html")
    public ModelAndView toPage() throws Exception {
		ModelAndView modelAndView = new ModelAndView("warning-list");
		return modelAndView;
    }
	
	/**
	 * 获取分页数据
	 */
	@RequestMapping("/list.asyn")
	@ResponseBody
	private ViewResponseBean listDeclare(LogWarningMqDTO dto) {
		try {
			// 响应结果集
			dto = service.listByPage(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
}
