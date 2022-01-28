package com.topideal.order.web.purchase;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.purchase.PurchasingReportsDTO;
import com.topideal.order.service.purchase.PurchasingReportsService;
import com.topideal.order.shiro.ShiroUtils;


/**
 * 
  * 采购计划报表
 *
 */
@RequestMapping("/purchasingReports")
@Controller
public class PurchasingReportsController {
	protected Logger LOGGER = LoggerFactory.getLogger(PurchasingReportsController.class);
	
	@Autowired
	private PurchasingReportsService purchasingReportsService;
	/** 标题 */
	private static final String[] COLUMNS = { "公司", "平台","仓库", "报表日期", "商品编码", "条形码", "商品名称", "在途数量", "库存",
			"90天日均销量", "预计库存清空天数", "建议采购数量", "补货后预计库存清空天数","是否触发补货预警","箱规" };
	private static final String[] KEYS = {"merchantName","platform","warehouse","runDate","wareId","upc","name","transitStock","stock"
			,"avg90dayNum","stockSellDays","purchaseNum","supplySellDays","supplyWarning","cartonSize"} ;
	
	@Autowired
    private RMQProducer rocketMQProducer;
	
	/**
	 * 访问列表页面
	 */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		return "/derp/purchase/purchasingReports-list";
	}
	
	/**
	 * 获取分页数据
	 */
	@RequestMapping("/listPurchasingReports.asyn")
	@ResponseBody
	private ViewResponseBean listPurchasingReports(PurchasingReportsDTO dto,String reportDate) {
		try {
			User user= ShiroUtils.getUser(); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			if(StringUtils.isNotBlank(reportDate)) {
				dto.setRunDate(TimeUtils.strToSqlDate(reportDate));
			}
			// 响应结果集
			dto = purchasingReportsService.listPurchasingReports(dto);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 *导出
	 */
	@RequestMapping("/exportPurchasingReports.asyn")
	@ResponseBody
	public void exportPurchasingReports(HttpServletResponse response, HttpServletRequest request,PurchasingReportsDTO dto,String reportDate) throws Exception{		
		User user= ShiroUtils.getUser(); 
		String sheetName = "补货动销报表";
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
		if(StringUtils.isNotBlank(reportDate)) {
			dto.setRunDate(TimeUtils.strToSqlDate(reportDate));
		}
		List<PurchasingReportsDTO> list = purchasingReportsService.exportList(dto);
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS,list);
        // 导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	
	/**
	 * 获取导出销售订单的数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,PurchasingReportsDTO dto,String reportDate) throws Exception{
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user= ShiroUtils.getUser(); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			if(StringUtils.isNotBlank(reportDate)) {
				dto.setRunDate(TimeUtils.strToSqlDate(reportDate));
			}
			Integer count = purchasingReportsService.getOrderCount(dto);
			data.put("total",count);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}
	
}
