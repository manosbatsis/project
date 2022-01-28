package com.topideal.controller.log;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.service.CrawlerBillLogService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 爬虫日志-启用
 */
@RestController
@RequestMapping("/crawlerlog")
public class CrawlerBillLogController {

	
	@Autowired
	private CrawlerBillLogService service;

	@RequestMapping("/toPage.html")
	public ModelAndView toPage() throws Exception {
		ModelAndView modelAndView = new ModelAndView("crawler-list");
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("/list.asyn")
	public ViewResponseBean searchLogAll(PageMongo pageMongo, String platformType, String saleOutDepotCode, String status,
			String billCode, String goodsNo,String errorMsg,String createDateStr,Long depotId,Long merchantId,String poNo) {
		ViewResponseBean viewResponseBean = new ViewResponseBean();
		try {
			pageMongo = service.searchLog(pageMongo, platformType, saleOutDepotCode, status,
				 billCode, goodsNo, errorMsg, createDateStr,depotId,merchantId,poNo);
			viewResponseBean.setData(pageMongo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(pageMongo);
	}
	/**
	 * 批量出库
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendMQ.asyn")
	public ViewResponseBean sendMQ(String ids) {
		if (StringUtils.isBlank(ids)) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		try {
			service.sendMQ(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		try {
			Thread.currentThread().sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 统计数量
	 * @param model
	 * @param endDateStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/count.asyn")
	public ViewResponseBean countLog(String platformType, String saleOutDepotCode, String status,
			String billCode, String goodsNo,String errorMsg,String createDateStr,Long depotId,Long merchantId,String poNo) {
		Integer count = null;
		try {
			count = service.count(platformType, saleOutDepotCode, status,
					 billCode, goodsNo, errorMsg, createDateStr,depotId,merchantId,poNo);
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
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportLogs.asyn")
	public void exportLogs(HttpSession session, HttpServletResponse response, HttpServletRequest request,String platformType, String saleOutDepotCode, String status,
			String billCode, String goodsNo,String errorMsg,String createDateStr, Integer count,Long depotId,Long merchantId,String poNo) throws Exception{
        String sheetName = "爬虫日志";
        
        List<Map<String, Object>> list = service.getExportList(platformType, saleOutDepotCode, status,
				 billCode, goodsNo, errorMsg, createDateStr, count,depotId,merchantId,poNo);
        
        String[] columns={"平台类型","商家","账单号","出库单号","po号","商品货号","商品名称","出库状态","未出库数量","失败原因","创建时间"};
        String[] keys = {"platformType", "merchantName", "billCode", "saleOutDepotCode", "poNo", "goodsNo", "goodsName", "status", "num", "errorMsg", "createDate"} ;
        
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, list);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
}
