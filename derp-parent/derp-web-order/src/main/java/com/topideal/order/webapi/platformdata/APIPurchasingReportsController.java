package com.topideal.order.webapi.platformdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.purchase.PurchasingReportsDTO;
import com.topideal.order.service.purchase.PurchasingReportsService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.platformdata.form.PurchasingReportsForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 
 * 采购计划报表(补货动销报表)
 *
 */
@RestController
@RequestMapping("/webapi/order/purchasingReports")
@Api(tags = "purchasingReports-补货动销报表")
public class APIPurchasingReportsController {
	protected Logger LOGGER = LoggerFactory.getLogger(APIPurchasingReportsController.class);
	
	@Autowired
	private PurchasingReportsService purchasingReportsService;
	/** 标题 */
	private static final String[] COLUMNS = { "公司", "平台","仓库", "报表日期", "商品编码", "条形码", "商品名称", "在途数量", "库存",
			"90天日均销量", "预计库存清空天数", "建议采购数量", "补货后预计库存清空天数","是否触发补货预警","箱规" };
	private static final String[] KEYS = {"merchantName","platform","warehouse","runDate","wareId","upc","name","transitStock","stock"
			,"avg90dayNum","stockSellDays","purchaseNum","supplySellDays","supplyWarning","cartonSize"} ;
	
	/**
	 * 获取分页数据
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/listPurchasingReports.asyn")
	@ApiOperation(value = "获取分页数据")
	private ResponseBean<PurchasingReportsDTO> listPurchasingReports(PurchasingReportsForm form) {
		try {
			
			if(StringUtils.isBlank(form.getToken())) {
				 //输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			PurchasingReportsDTO dto = new PurchasingReportsDTO() ;
			dto.setWarehouse(form.getWarehouse());
			dto.setPlatform(form.getPlatform());
			dto.setWareId(form.getWareId());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			
			User user= ShiroUtils.getUserByToken(form.getToken()) ;
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			
			if(StringUtils.isNotBlank(form.getReportDate())) {
				dto.setRunDate(TimeUtils.strToSqlDate(form.getReportDate()));
			}
			
			// 响应结果集
			dto = purchasingReportsService.listPurchasingReports(dto);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
		
	}
	
	/**
	 *导出
	 */
	@GetMapping("/exportPurchasingReports.asyn")
	@ApiOperation(value = "导出")
	public void exportPurchasingReports(HttpServletResponse response, HttpServletRequest request,
			PurchasingReportsForm form) throws Exception{	
		
		if(StringUtils.isBlank(form.getToken())) {
			 //输入信息不完整
			return ;
		}
		
		String sheetName = "补货动销报表";
		
		PurchasingReportsDTO dto = new PurchasingReportsDTO() ;
		dto.setWarehouse(form.getWarehouse());
		dto.setPlatform(form.getPlatform());
		dto.setWareId(form.getWareId());
		
		User user= ShiroUtils.getUserByToken(form.getToken()) ;
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
		
		if(StringUtils.isNotBlank(form.getReportDate())) {
			dto.setRunDate(TimeUtils.strToSqlDate(form.getReportDate()));
		}
		
		List<PurchasingReportsDTO> list = purchasingReportsService.exportList(dto);
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS,list);
        // 导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	
	/**
	 * 获取导出销售订单的数量
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getOrderCount.asyn")
	@ApiOperation(value = "获取导出销售订单的数量")
	private ResponseBean<Map<String, Object>> getOrderCount(HttpServletResponse response, HttpServletRequest request,
			PurchasingReportsForm form) throws Exception{
		
		Map<String,Object> data=new HashMap<String,Object>();
		
		try{
			
			PurchasingReportsDTO dto = new PurchasingReportsDTO() ;
			dto.setWarehouse(form.getWarehouse());
			dto.setPlatform(form.getPlatform());
			dto.setWareId(form.getWareId());
			
			User user= ShiroUtils.getUserByToken(form.getToken()) ;
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			
			if(StringUtils.isNotBlank(form.getReportDate())) {
				dto.setRunDate(TimeUtils.strToSqlDate(form.getReportDate()));
			}
			
			Integer count = purchasingReportsService.getOrderCount(dto);
			
			data.put("total",count);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, data) ;
			
		}catch(Exception e){
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
		
	}
	
}
