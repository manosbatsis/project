package com.topideal.inventory.web;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.InventorySummaryDTO;
import com.topideal.entity.vo.InventorySummaryModel;
import com.topideal.inventory.service.InventorySummaryService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.tools.DownloadExcelUtil;

/**
 * 库存管理-商品收发汇总-控制层 
 */
@RequestMapping("/inventorySummary")
@Controller
public class InventorySummaryController {

	// 商品收发汇总service
	@Autowired
	private InventorySummaryService inventorySummaryService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(HttpSession session,Model model)throws Exception  {
		User user= ShiroUtils.getUser();
		model.addAttribute("currentMonth", TimeUtils.formatMonth(new Date()));
		return "/inventory/inventorySummary-list";
	}


	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listInventorySummary.asyn")
	@ResponseBody
	private ViewResponseBean listInventorySummary(HttpSession session,InventorySummaryDTO model) {
		try{
		 	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			// 响应结果集
		 	User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			
			if(StringUtils.isNotBlank(model.getCurrentMonth())){
				model.setLastMonth(TimeUtils.getLastMonth(dateFormat.parse(model.getCurrentMonth())));
			}
			model = inventorySummaryService.listInventorySummaryByPage(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	

	/**
	 * 商品收发汇总
	 * */
	@RequestMapping("/exportInventorySummary.asyn")
	@ResponseBody
	private void exportInventorySummary(HttpSession session, HttpServletResponse response, HttpServletRequest request,Long depotId,String goodsNo,String currentMonth){
		
		try {

			User user= ShiroUtils.getUser();
			//校验id是否正确
	        String sheetName = "商品收发汇总";
	        //根据勾选的获取信息
	        List<Map<String,Object>> result = inventorySummaryService.exportInventorySummaryMap(user.getMerchantId(), depotId, goodsNo, currentMonth);
	        String[] columns={"商家名称","仓库名称","商品货号","商品名称","本月期初库存","本月累计入库","本月累计出库","库存余额","销售在途量","电商在途量","调拨出库在途","可用库存","单位","库存周转天数(按120天订单算)","在库库存断销时间"};
	        String[] keys={"merchantName","depotName","goodsNo","goodsName","openingInventoryNum","inDepotTotal","outDepotTotal","surplusNum","saleOnwayNum","eOnwayNum","transferOutNum","availableNum","unit","turnoverDays","noSaleDate"};
	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
