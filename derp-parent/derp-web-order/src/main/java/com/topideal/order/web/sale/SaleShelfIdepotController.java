//package com.topideal.order.web.sale;
//
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.web.ResponseFactory;
//import com.topideal.common.system.web.StateCodeEnum;
//import com.topideal.common.system.web.ViewResponseBean;
//import com.topideal.common.tools.ExcelUtilXlsx;
//import com.topideal.common.tools.FileExportUtil;
//import com.topideal.entity.dto.sale.SaleShelfIdepotDTO;
//import com.topideal.entity.vo.sale.SaleShelfIdepotItemModel;
//import com.topideal.order.service.sale.SaleShelfIdepotService;
//import com.topideal.order.shiro.ShiroUtils;
//
///**
// * 销售上架入库 controller
// *
// */
//@RequestMapping("/saleShelfIdepot")
//@Controller
//public class SaleShelfIdepotController {
//
//	private static final String[] EXPORT_COLUMNS = { "上架入库单号", "销售订单号", "入库仓库","事业部", "出仓仓库", "销售出库单",
//			"PO号", "单据状态", "入库时间", "商品货号", "商品条码", "商品名称", "好品数量",
//			"坏品数量", "入库批次号", "生产日期", "失效日期"};
//	private static final String[] EXPORT_KEYS = {"code" , "sale_order_code","in_depot_name","bu_name","out_depot_name","sale_out_code",
//			"po_no", "state", "shelf_date", "in_goods_no", "barcode", "in_goods_name", "normal_num",
//			"damaged_num", "batch_no", "production_date", "overdue_date"} ;
//
//	@Autowired
//	private SaleShelfIdepotService saleShelfIdepotService ;
//
//	/**
//	 * 访问上架入库单页面
//	 * */
//	@RequestMapping("/shelfIdepot.html")
//	public String shelfIdepot(Model model) throws Exception {
//		return "/derp/sale/sale-shelf-idepot-list";
//	}
//
//	/**
//	 * 访问详情页面
//	 * */
//	@RequestMapping("/toDetailsPage.html")
//	public String toDetailsPage(Model model, Long id, String pageSource)throws Exception{
//		SaleShelfIdepotDTO dto = saleShelfIdepotService.searchDetail(id);
//		List<SaleShelfIdepotItemModel> itemList = saleShelfIdepotService.listItemBySaleShelfIdepotId(id);
//		model.addAttribute("detail", dto);
//		model.addAttribute("itemList", itemList);
//		return "/derp/sale/sale-shelf-idepot-details";
//	}
//
//	/**
//	 * 获取上架入库单分页数据
//	 * */
//	@RequestMapping("/listSaleShelfIdepot.asyn")
//	@ResponseBody
//	private ViewResponseBean listSaleShelfIdepot(SaleShelfIdepotDTO dto) {
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			// 响应结果集
//			dto = saleShelfIdepotService.listSaleShelfIdepot(dto,user);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(dto);
//	}
//
//	/**
//	 * 获取导出销售出库清单的数量
//	 */
//	@RequestMapping("/getOrderCount.asyn")
//	@ResponseBody
//	private ViewResponseBean getOrderCount(SaleShelfIdepotDTO dto) throws Exception{
//		Map<String,Object> data=new HashMap<String,Object>();
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			// 响应结果集
//			Integer result = saleShelfIdepotService.getOrderCount(dto,user);
//			data.put("total",result);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(data);
//	}
//
//	/**
//	 * 导出销售出库单
//	 * */
//	@RequestMapping("/exportSaleShelfIdepot.asyn")
//	@ResponseBody
//	private void exportSaleShelfIdepot(HttpServletResponse response, HttpServletRequest request,SaleShelfIdepotDTO dto) throws Exception{
//		User user= ShiroUtils.getUser();
//		// 设置商家id
//		dto.setMerchantId(user.getMerchantId());
//		// 响应结果集
//		List<Map<String,Object>> result = saleShelfIdepotService.getExportList(dto,user);
//
//		String sheetName = "上架入库单";
//        //生成表格
//		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, EXPORT_COLUMNS, EXPORT_KEYS, result);
//		//导出文件
//		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
//	}
//
//}
