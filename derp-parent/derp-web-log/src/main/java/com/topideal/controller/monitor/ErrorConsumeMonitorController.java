package com.topideal.controller.monitor;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorMqModel;
import com.topideal.service.ConsumeMonitorService;

/**
 * 进境异常监控-启用
 * @author zhanghx
 */
@RestController
@RequestMapping("/errorMonitor")
public class ErrorConsumeMonitorController {

	@Autowired
	private ConsumeMonitorService service;
	
	/**
	 * 跳转分页
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toPage.html")
    public ModelAndView toPage(Model model) throws Exception {
		ModelAndView modelAndView = new ModelAndView("error-monitor-list");
		return modelAndView;
    }
	
	/**
	 * 获取分页数据
	 */
	@RequestMapping("/list.asyn")
	@ResponseBody
	private ViewResponseBean listDeclare(ConsumeMonitorMqDTO dto) {
		try {
			// 响应结果集
			dto = service.getErrorListByPage(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 全部重推
	 * @param 
	 * @return map
	 * @throws IOException 
	 */
	@RequestMapping("/allPush.asyn")
	@ResponseBody
	public ViewResponseBean allPush(ConsumeMonitorMqModel model, String endDateStr, String createDateStr) throws IOException{
		try{
			service.allPush(model, endDateStr, createDateStr);
		}catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
		return ResponseFactory.success();
	}
	
}
