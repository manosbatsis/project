package com.topideal.controller.log;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.service.ReportLogService;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 报表日志
 */
@RestController
@RequestMapping("/reportlog")
public class ReportLogController {

	/** 标题  */
	private static final String[] COLUMNS= {"接口编码","描述","请求时间","消费时间","耗时","状态","异常提示"};
	
	private static final String[] KEYS = {"point", "desc", "startDate", "endDate", "differenceTime", "state", "expMsg"} ;
	@Autowired
	private ReportLogService service;

	@RequestMapping("/toPage.html")
	public ModelAndView toPage() throws Exception {
		ModelAndView modelAndView = new ModelAndView("mq-report");
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("/list.asyn")
	public ViewResponseBean searchLogAll(PageMongo pageMongo, String state, String point, String endDateStr) {
		ViewResponseBean viewResponseBean = new ViewResponseBean();
		try {
			pageMongo = service.searchLog(pageMongo, state, point, endDateStr);
			viewResponseBean.setData(pageMongo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(pageMongo);
	}

	/**
	 * 统计数量
	 * @param keyword
	 * @param state
	 * @param point
	 * @param endDateStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/count.asyn")
	public ViewResponseBean countLog(String state, String point) {
		Long count = null;
		try {
			count = service.count(state, point);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(count);
	}
	
	/**
	 * 日志导出
	 * @param 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/exportLogs.asyn")
	public void exportRelation(HttpSession session, HttpServletResponse response, HttpServletRequest request, String state, String point) throws Exception{
        String sheetName = "报表日志";
        List<Map<String, Object>> list = service.searchListLog(state, point);
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, COLUMNS, KEYS, list) ;
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
}
