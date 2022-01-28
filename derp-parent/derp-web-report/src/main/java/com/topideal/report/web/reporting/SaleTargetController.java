package com.topideal.report.web.reporting;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.SaleTargetDTO;
import com.topideal.report.service.reporting.SaleTargetService;
import com.topideal.report.shiro.ShiroUtils;

/**
 * 销售目标控制器
 * @author Gy
 *
 */
@RequestMapping("saleTarget")
@Controller
public class SaleTargetController {
	
	private static final Logger LOGGER = Logger.getLogger(SaleTargetController.class) ;
	
	private static final String [] COLUMNS = new String[] {"事业部", "商品条码", "商品名称", "标准条码", "销售计划月份", "类型", 
										"To B销量", "To C销量", "平台名称", "平台计划销量", "店铺编码", "店铺名称", "店铺计划销量"} ;
	private static final String [] KEYS = new String[] {"buName", "barcode", "goodsName", "commbarcode", "month", "typeLabel", 
										"toBNum", "toCNum", "storePlatformNameLabel", "storePlatformNum", "shopCode", "shopName", "shopNum"} ;
	
	@Autowired
	private SaleTargetService saleTargetService ;

	/**
	 * 列表页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("toPage.html")
	public String toPage(Model model) throws SQLException {
		return "derp/reporting/sale-target-list";
	}
	
	/**
	 * 导入页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("toImportPage.html")
	public String toImportPage(Model model) throws SQLException {
		return "derp/reporting/sale-target-import";
	}
	
	/**
	 * 详情页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("toDetailPage.html")
	public String toDetailPage(Model model, SaleTargetDTO dto) throws SQLException {
		
		Map<String, Object> resultMap = saleTargetService.getSaleTargetDetails(dto) ;
		
		model.addAllAttributes(resultMap) ;
		
		return "derp/reporting/sale-target-details";
	}
	
	@RequestMapping("listSaleTarget.asyn") 
	@ResponseBody
	public ViewResponseBean listSaleTarget(SaleTargetDTO dto) {
		try {
			User user = ShiroUtils.getUser();
			dto = saleTargetService.listSaleTarget(dto,user) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 导入
	 * @param file
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("importSaleTarget.asyn")
	@ResponseBody
	public ViewResponseBean importSaleTarget(@RequestParam(value = "file", required = false) MultipartFile file) throws SQLException {
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			List<Map<String, String>> saleTypeData = ExcelUtilXlsx.parseSheetBySheetName(file.getInputStream(), "按销售类型计划");
			List<Map<String, String>> platformData = ExcelUtilXlsx.parseSheetBySheetName(file.getInputStream(), "按平台计划");
			List<Map<String, String>> shopData = ExcelUtilXlsx.parseSheetBySheetName(file.getInputStream(), "按店铺计划");
			
			if (saleTypeData == null && platformData == null && shopData == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user= ShiroUtils.getUser();
			resultMap = saleTargetService.importSaleTarget(saleTypeData, platformData, shopData, user);
		} catch (NullPointerException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}
	
	@RequestMapping("exportSaleTarget.asyn")
	public void exportSaleTarget(String params,HttpServletRequest request ,HttpServletResponse response) throws Exception {
		
		String mainSheetName = "销售目标导出" ;
		//拆分前端导入的参数
		String[] arr = null;
		
		if(StringUtils.isNotBlank(params)) {
			arr = params.split(",") ;
		}else {
			arr = new String [] {} ;
		}
		User user = ShiroUtils.getUser();
		List<SaleTargetDTO> exportList = saleTargetService.exportSaleTarget(arr,user) ;
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainSheetName, COLUMNS, KEYS, exportList) ;
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
	}
	
	@RequestMapping("delSaleTarget.asyn")
	@ResponseBody
	public ViewResponseBean delSaleTarget(String params) throws Exception {
		
		try {
			if(StringUtils.isBlank(params)) {
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			String[] arr = params.split(",");
			saleTargetService.delSaleTarget(arr) ;
			
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		
		return ResponseFactory.success();
	}
}
