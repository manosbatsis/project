package com.topideal.inventory.web;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.InventoryBatchDTO;
import com.topideal.inventory.service.InventoryBatchService;
import com.topideal.inventory.shiro.ShiroUtils;

import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 库存管理-商品库存明细-控制层 
 */
@RequestMapping("/productInventoryDetails")
@Controller
public class ProductInventoryDetailsController {

	// 批次库存明细service
	@Autowired
	private InventoryBatchService inventoryBatchService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(HttpSession session,Model model)throws Exception  {
		User user= ShiroUtils.getUser();
		return "/inventory/productInventoryDetails-list";
	}


	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listProductInventoryDetails.asyn")
	@ResponseBody
	private ViewResponseBean listProductInventoryDetails(HttpSession session,InventoryBatchDTO model,String depotIds) {
		try{
			// 响应结果集
			
			User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			List<Long> depotIdsList=new ArrayList<Long>();
			if (StringUtils.isNotBlank(depotIds)&&!"null".equals(depotIds)) {
				List<String> asList = Arrays.asList(depotIds.split(","));
				for (String depotIdStr : asList) {
					if (StringUtils.isNotBlank(depotIdStr))depotIdsList.add(Long.valueOf(depotIdStr));					
				}
			}
			model.setDepotIdsList(depotIdsList);
			model = inventoryBatchService.listProductInventoryDetailsByPage(model);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	
	
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/productInventoryDetailsToPage.html")
	public String productInventoryDetailsToPage(HttpSession session,Model model,String goodsNo,Long depotId)throws Exception  {
		
		User user= ShiroUtils.getUser();
		model.addAttribute("goodsNo", goodsNo);
		model.addAttribute("depotId", depotId);
		return "/inventory/productInventoryDetails-list";
	}
	
	
	
	/**
	 * 导出商品库存明细
	 * */
	@RequestMapping("/exportProductInventoryDetails.asyn")
	@ResponseBody
	private void exportProductInventoryDetails(HttpSession session, HttpServletResponse response, HttpServletRequest request, String depotIds,String goodsNo){

		try {
			String sheetName0 = "商品库存明细";

			User user= ShiroUtils.getUser();

			 Map<String, Object> resultMap = inventoryBatchService.exportProductInventoryDetailsMap(user.getMerchantId(), depotIds, goodsNo);
			String[] columns0 = { "商家名称", "仓库名称","条形码", "商品货号", "商品名称", "库存数量", "冻结数量", "坏品数量", "过期数量", "可用数量", "单位"};
			String[] keys0 = { "merchantName", "depotName", "barcode","goodsNo", "goodsName", "surplusNum", "freezeNum",
					"badNum", "okayNum", "availableNum", "unitLabel"};
			//生成表格
			//SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
			List<InventoryBatchDTO> result0 = (List<InventoryBatchDTO>) resultMap.get("result0");
			List<InventoryBatchDTO> result1 = (List<InventoryBatchDTO>) resultMap.get("result1");
			
			String sheetName1 = "批次库存明细";
			String[] columns1 = { "商家名称", "仓库名称","条形码", "商品货号", "商品名称","批次号","生产日期","失效日期","库存类型","是否过期","库存数量","理货单位","托盘号","品牌","标准条码"};
			String[] keys1 = { "merchantName", "depotName","barcode", "goodsNo", "goodsName", "batchNo","production_date",
					"overdue_date","typeLabel","isExpireLabel","surplusNum","unitLabel","brandName","commbarcode"};		
			List<ExportExcelSheet> sheetList=new ArrayList<>();
			ExportExcelSheet Sheet0=new ExportExcelSheet();
			Sheet0.setColums(columns0);
			Sheet0.setKeys(keys0);
			Sheet0.setSheetNames(sheetName0);
			Sheet0.setResultList(result0);
			sheetList.add(Sheet0);
			ExportExcelSheet Sheet1=new ExportExcelSheet();
			Sheet1.setColums(columns1);
			Sheet1.setKeys(keys1);
			Sheet1.setSheetNames(sheetName1);
			Sheet1.setResultList(result1);
			sheetList.add(Sheet1);
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList);
			
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	
}
