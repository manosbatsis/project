package com.topideal.web.derp.main;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.main.SupplierMerchandiseDTO;
import com.topideal.service.main.SupplierMerchandiseService;

@Controller
@RequestMapping("/supplierMerchandise")
public class SupplierMerchandiseController {

	@Autowired
	private SupplierMerchandiseService supplierMerchandiseService;



	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws SQLException {
		return "/derp/main/supplier-merchandise-list";
	}


	/**
	 * 列表所需数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/listSupplierMerchandise.asyn")
	@ResponseBody
	private ViewResponseBean listSupplierMerchandise(SupplierMerchandiseDTO dto) {
		try {
			dto = supplierMerchandiseService.listSupplierMerchandise(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);

		}

		return ResponseFactory.success(dto);
	}

	/**
	 * 导出商品信息
	 * @param response
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/export.asyn")
	public void export(HttpServletResponse response, HttpServletRequest request, SupplierMerchandiseDTO dto) throws Exception{

		String sheetName = "商品信息导出";
		List<SupplierMerchandiseDTO> list = supplierMerchandiseService.exportList(dto);
		/** 标题  */
		String[] COLUMNS= {"商品编码","商品名称","品牌名称","品牌编码","条形码","产品类型","建议零售价","经销商货号","事业部","数据来源"};
		String[] KEYS= {"goodsCode","name","brandName","brandCode","barcode","goodsType","salePrice","supplierGoodsNo","buName","sourceLabel"};
		
		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , list);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
}
