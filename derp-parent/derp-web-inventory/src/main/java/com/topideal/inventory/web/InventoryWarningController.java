package com.topideal.inventory.web;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.InventoryBatchDTO;
import com.topideal.inventory.service.InventoryBatchService;
import com.topideal.inventory.shiro.ShiroUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 库存管理-库存预警-控制层 
 */
@RequestMapping("/inventoryWarning")
@Controller
public class InventoryWarningController {


	// 批次库存service
	@Autowired
	private InventoryBatchService   inventoryBatchService;
	
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(HttpSession session,Model model, Long depotId)throws Exception  {
		User user= ShiroUtils.getUser();
		model.addAttribute("depotId", depotId);
		return "/inventory/inventoryWarning-list";
	}


	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listInventoryWarning.asyn")
	@ResponseBody
	private ViewResponseBean listInventoryWarning(HttpSession session,InventoryBatchDTO model,String validityTypes) {
		try{
			// 响应结果集
			User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			model =	inventoryBatchService.selectInventoryWarningByPage(model,validityTypes);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	

	
	/**
	 * 导出效期预警
	 * */
	@RequestMapping("/exportInventoryWarning.asyn")
	@ResponseBody
	private void exportInventoryWarning(HttpSession session, HttpServletResponse response, HttpServletRequest request, Long depotId,  String goodsNo,String validityType,String validityTypes){

		try {

			User user= ShiroUtils.getUser();
			InventoryBatchDTO model = new InventoryBatchDTO();
			model.setMerchantId(user.getMerchantId());
			model.setDepotId(depotId);
			model.setGoodsNo(goodsNo);
			model.setValidityType(validityType);
			String sheetName = "效期预警";
			// 根据勾选的获取信息
			List<Map<String, Object>> result = inventoryBatchService.exportInventoryWarningMap(model,validityTypes);
			String[] columns = { "商家名称", "仓库名称", "商品货号", "商品名称", "生产日期", "失效日期", "批次号", "总数量", "单位", "剩余效期（天）", "总效期（天）",
					"剩余效期","效期区间" };
			String[] keys = { "merchant_name", "depot_name", "goods_no", "goods_name", "production_date", "overdue_date",
					"batch_no", "surplus_num", "unit", "surplus_days", "total_days", "surplus_date","surplus_date_section" };
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
