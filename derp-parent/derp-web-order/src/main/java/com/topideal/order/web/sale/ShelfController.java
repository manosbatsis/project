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
//import com.topideal.entity.dto.sale.ShelfDTO;
//import com.topideal.entity.vo.sale.SaleShelfModel;
//import com.topideal.order.service.sale.SaleShelfService;
//import com.topideal.order.service.sale.ShelfService;
//import com.topideal.order.shiro.ShiroUtils;
//
///**
// * 销售上架入库 controller
// * 
// */
//@RequestMapping("/shelf")
//@Controller
//public class ShelfController {
//	
//	private static final String[] EXPORT_COLUMNS = { "上架单号", "销售订单号", "PO号", "客户","事业部", "上架日期", "单据状态",
//			"商品货号", "商品条码","标准条码", "商品名称", "上架量", "残损量", "少货量",
//			"操作人", "操作时间", "备注"};
//	private static final String[] EXPORT_KEYS = {"code" , "sale_order_code","po_no", "customer_name","bu_name","shelf_date","state",
//			"goods_no", "barcode", "commbarcode",  "goods_name","shelf_num", "damaged_num", "lack_num",
//			"operator", "modify_date", "remark"} ;
//
//	@Autowired
//	private ShelfService shelfService ;
//
//	@Autowired
//	private SaleShelfService saleShelfService;
//
//	/**
//	 * 访问上架单页面
//	 * */
//	@RequestMapping("/shelf.html")
//	public String shelf(Model model) throws Exception {
//		return "/derp/sale/sale-shelf-list";
//	}
//
//	/**
//	 * 访问详情页面
//	 * */
//	@RequestMapping("/toDetailsPage.html")
//	public String toDetailsPage(Model model, Long id, String pageSource)throws Exception{
//		ShelfDTO shelfDTO = shelfService.searchDetail(id);
//		List<SaleShelfModel> itemList = saleShelfService.listItemBySaleShelfId(id);
//		model.addAttribute("detail", shelfDTO);
//		model.addAttribute("itemList", itemList);
//		return "/derp/sale/sale-shelf-details";
//	}
//
//	/**
//	 * 获取上架单分页数据
//	 * */
//	@RequestMapping("/listShelf.asyn")
//	@ResponseBody
//	private ViewResponseBean listShelf(ShelfDTO dto) {
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			// 响应结果集
//			dto = shelfService.listShelf(dto);
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
//	private ViewResponseBean getOrderCount(ShelfDTO dto) throws Exception{
//		Map<String,Object> data=new HashMap<String,Object>();
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			// 响应结果集
//			Integer result = shelfService.getOrderCount(dto);
//			data.put("total",result);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(data);
//	}
//	/**
//	 * 导出销售上架单
//	 * */
//	@RequestMapping("/exportShelf.asyn")
//	@ResponseBody
//	private void exportShelf(HttpServletResponse response, HttpServletRequest request,ShelfDTO dto) throws Exception{
//		User user= ShiroUtils.getUser();
//		// 设置商家id
//		dto.setMerchantId(user.getMerchantId());
//		// 响应结果集
//		List<Map<String,Object>> result = shelfService.getExportList(dto);
//
//		String sheetName = "上架单";
//        //生成表格
//		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, EXPORT_COLUMNS, EXPORT_KEYS, result);
//		//导出文件
//		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
//	}
//	
//}
