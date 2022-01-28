package com.topideal.controller.monitor;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorMqModel;
import com.topideal.service.ConsumeMonitorService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

/**
 * Api日志监控-启用
 */
@RestController
@RequestMapping("/apimonitorlog")
public class ApiConsumeMonitorController {

	/** 标题  */
	private static final String[] COLUMNS= {"模块","接口编码","关键字","模块编码", "模块枚举","消费时间","状态","关闭时间","类型", "类型枚举","异常类型","公司","失败原因","创建时间"};
	
	private static final String[] KEYS = {"model","point","keyword", "modelCodeLabel", "modelCode", "consumeDate", "statusLabel", "closeTime", "typeLabel", "type", "errorTypeLabel","merchantName", "expMsg", "createTime"} ;
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
		ModelAndView modelAndView = new ModelAndView("api-consume-monitor-list");
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
			dto = service.listByPage(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 统计数量
	 * @param model
	 * @param endDateStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/count.asyn")
	public ViewResponseBean countLog(ConsumeMonitorMqModel model, String endDateStr, String createDateStr, String selectScope) {
		Long count = null;
		try {
			count = service.count(model, endDateStr, createDateStr, selectScope);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(count);
	}
	
	/**
	 * 日志导出
	 * @param session
	 * @param response
	 * @param request
	 * @param model
	 * @param endDateStr
	 * @throws Exception
	 */
	@RequestMapping("/exportLogs.asyn")
	public void exportRelation(HttpSession session, HttpServletResponse response, HttpServletRequest request, ConsumeMonitorMqDTO model, String endDateStr, Integer count, String createDateStr, String selectScope, String modelCode) throws Exception{
        String sheetName = "Api监控日志";
        List<ConsumeMonitorMqDTO> list = service.getExportList(model, endDateStr, count, createDateStr, selectScope, modelCode);
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, list);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	
	/**
	 * 批量关闭
	 */
	@RequestMapping("/closeBatch.asyn")
	@ResponseBody
	private ViewResponseBean closeBatch(String ids, String modelCode, HttpSession session) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List<Long> list = StrUtils.parseIds(ids);
			// 响应结果集
			service.closeBatch(list,modelCode);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 批量推送
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/batchResetSend.asyn")
	public ViewResponseBean batchResetSend(String ids,String modelCode) {
		if (StringUtils.isBlank(ids)) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		List<String> list = StrUtils.parseIdsToStr(ids);
		try {
			service.resetSend(list,modelCode);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
}
