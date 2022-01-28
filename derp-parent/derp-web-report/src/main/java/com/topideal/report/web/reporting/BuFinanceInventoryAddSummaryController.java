/*
 * package com.topideal.report.web.reporting;
 * 
 * import com.topideal.common.system.auth.User; import
 * com.topideal.common.system.web.ResponseFactory; import
 * com.topideal.common.system.web.StateCodeEnum; import
 * com.topideal.common.system.web.ViewResponseBean; import
 * com.topideal.common.tools.ExcelUtilXlsx; import
 * com.topideal.common.tools.FileExportUtil; import
 * com.topideal.entity.dto.BuFinanceInventorySummaryDTO; import
 * com.topideal.report.service.reporting.BuFinanceInventorySummaryService;
 * import com.topideal.report.shiro.ShiroUtils; import
 * org.apache.poi.xssf.streaming.SXSSFWorkbook; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import org.springframework.util.StringUtils;
 * import org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.ResponseBody;
 * 
 * import javax.servlet.http.HttpServletRequest; import
 * javax.servlet.http.HttpServletResponse; import java.text.SimpleDateFormat;
 * import java.util.ArrayList; import java.util.Date; import java.util.List;
 * 
 *//**
	 * 累计汇总表
	 */
/*
 * @Controller
 * 
 * @RequestMapping("/buFinanceAdd") public class
 * BuFinanceInventoryAddSummaryController {
 * 
 * @Autowired public BuFinanceInventorySummaryService
 * buFinanceInventorySummaryService;
 * 
 * 
 * 
 *//**
	 * 访问列表页面
	 */
/*
 * @RequestMapping("/toPage.html") public String toPage(Model model,String
 * month)throws Exception { SimpleDateFormat sdf1 = new
 * SimpleDateFormat("yyyy"); if (StringUtils.isEmpty(month)) { SimpleDateFormat
 * sdf = new SimpleDateFormat("yyyy-MM"); model.addAttribute("nowStart",
 * sdf1.format(new Date())+"-01"); model.addAttribute("nowEnd", sdf.format(new
 * Date())); } else { model.addAttribute("nowStart",
 * sdf1.format(month+"-01")+"-01"); model.addAttribute("nowEnd", month); }
 * return "derp/reporting/buFinanceInventoryAddSummary-list"; }
 * 
 *//**
	 * 获取分页数据
	 */
/*
 * @RequestMapping("/buFinanceAddSummaryList.asyn")
 * 
 * @ResponseBody private ViewResponseBean
 * financeSummaryList(BuFinanceInventorySummaryDTO model, String brandIds) {
 * try{ // 响应结果集 User user=ShiroUtils.getUser();
 * model.setMerchantId(user.getMerchantId()); List<Long> parentBrandIds=new
 * ArrayList<Long>(); if(!StringUtils.isEmpty(brandIds)){ parentBrandIds=new
 * ArrayList<Long>(); String[] Ids = brandIds.split(","); for(String id:Ids){
 * if(!StringUtils.isEmpty(id)) parentBrandIds.add(Long.valueOf(id)); }
 * model.setParentBrandIds(parentBrandIds); } model =
 * buFinanceInventorySummaryService.getListAddByPage(model); }catch(Exception
 * e){ e.printStackTrace(); return
 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
 * ResponseFactory.success(model); }
 * 
 * 
 * 
 *//**
	 * 累计总表导出
	 *//*
		 * @RequestMapping("/export.asyn") private void createTask(HttpServletResponse
		 * response,HttpServletRequest request,BuFinanceInventorySummaryDTO model,
		 * String brandIds){ try { User user=ShiroUtils.getUser();
		 * model.setMerchantId(user.getMerchantId()); List<Long> parentBrandIds=new
		 * ArrayList<Long>(); if(!StringUtils.isEmpty(brandIds)){ parentBrandIds=new
		 * ArrayList<Long>(); String[] Ids = brandIds.split(","); for(String id:Ids){
		 * if(!StringUtils.isEmpty(id)) parentBrandIds.add(Long.valueOf(id)); }
		 * model.setParentBrandIds(parentBrandIds); } // 响应结果集
		 * List<BuFinanceInventorySummaryDTO> result =
		 * buFinanceInventorySummaryService.getListAddExport(model); String sheetName =
		 * "累计汇总表"; String[] columns = {
		 * "事业部","母品牌","标准品牌","二级分类","标准条码","商品名称","统计起始月份","统计结束月份","累计采购结转数量",
		 * "累计采购结转金额",
		 * "累计销售结转数量","累计销售结转金额","累计损益结转数量","累计损益结转金额","累计采购入库数量","累计来货残损数量",
		 * "累计事业部移库入数量","累计销售已上架数量","累计出库残损数量",
		 * "累计事业部移库出数量","累计销毁数量","累计盘盈数量","累计盘亏数量"}; String[] keys =
		 * {"buName","superiorParentBrand","brandName","typeName","groupCommbarcode",
		 * "goodsName","monthStart","monthEnd","purchaseEndNum","purchaseEndAmount",
		 * "saleEndNum","saleEndAmount","lossOverflowNum","lossOverflowAmount",
		 * "warehouseNum","inDamagedNum","moveInNum","saleShelfNum",
		 * "outDamagedNum","moveOutNum","destroyNum","profitNum","lossNum"} ;
		 * 
		 * // 生成表格 SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName,
		 * columns, keys, result) ; // 导出文件 FileExportUtil.export2007ExcelFile(wb,
		 * response, request, sheetName); } catch (Exception e) { e.printStackTrace(); }
		 * }
		 * 
		 * 
		 * }
		 */