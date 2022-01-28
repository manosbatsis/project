/*
 * package com.topideal.report.web.reporting;
 * 
 * import com.topideal.common.system.auth.User; import
 * com.topideal.common.system.web.ResponseFactory; import
 * com.topideal.common.system.web.StateCodeEnum; import
 * com.topideal.common.system.web.ViewResponseBean; import
 * com.topideal.common.tools.ExcelUtilXlsx; import
 * com.topideal.common.tools.FileExportUtil; import
 * com.topideal.entity.dto.SdWeightedPriceDTO; import
 * com.topideal.report.service.reporting.SdWeightedPriceService; import
 * com.topideal.report.shiro.ShiroUtils; import
 * org.apache.poi.xssf.streaming.SXSSFWorkbook; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.ResponseBody;
 * 
 * import javax.servlet.http.HttpServletRequest; import
 * javax.servlet.http.HttpServletResponse; import java.sql.SQLException; import
 * java.util.List;
 * 
 *//**
	 * 加权单价
	 */
/*
 * @RequestMapping("/SDweightedPrice")
 * 
 * @Controller public class SDWeightedPriceController {
 * 
 * @Autowired private SdWeightedPriceService sdweightedPriceService;
 * 
 * 
 *//**
	 * 访问列表页面
	 * 
	 * @throws SQLException
	 */
/*
 * @RequestMapping("/toPage.html") public String toPage(Model model) throws
 * SQLException {
 * 
 * return "derp/reporting/sd-weighted-price-list"; }
 * 
 *//**
	 * 获取分页数据
	 */
/*
 * @RequestMapping("/listSDPrice.asyn")
 * 
 * @ResponseBody private ViewResponseBean listPrice(SdWeightedPriceDTO dto) {
 * try{ User user= ShiroUtils.getUser(); // 设置商家id
 * dto.setMerchantId(user.getMerchantId()); // 响应结果集 dto =
 * sdweightedPriceService.listPriceDTO(dto); }catch(Exception e){
 * e.printStackTrace(); return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
 * } return ResponseFactory.success(dto); }
 * 
 *//**
	 * 导出
	 *//*
		 * @RequestMapping("/sdexportWeightedPrice.asyn")
		 * 
		 * @ResponseBody private void exportWeightedPrice(HttpServletResponse response,
		 * HttpServletRequest request,SdWeightedPriceDTO dto) throws Exception{ User
		 * user= ShiroUtils.getUser(); // 设置商家id
		 * dto.setMerchantId(user.getMerchantId());
		 * 
		 * // 响应结果集 List<SdWeightedPriceDTO> result =
		 * sdweightedPriceService.listForExport(dto); String sheetName = "SD加权单价";
		 * String[] columns={"事业部", "标准条码", "商品名称", "标准品牌", "结算币种", "加权单价", "生效年月",
		 * "更新时间"}; String[] keys = {"buName", "commbarcode", "goodsName", "brandName",
		 * "currencyLabel", "price", "effectiveMonth", "createDate"} ; //生成表格
		 * SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns,
		 * keys, result); //导出文件 FileExportUtil.export2007ExcelFile(wb, response,
		 * request, sheetName); }
		 * 
		 * 
		 * 
		 * }
		 */