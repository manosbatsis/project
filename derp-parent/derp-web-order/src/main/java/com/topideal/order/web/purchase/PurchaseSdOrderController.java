//package com.topideal.order.web.purchase;
//
//import com.topideal.common.exception.DerpException;
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.web.ResponseFactory;
//import com.topideal.common.system.web.StateCodeEnum;
//import com.topideal.common.system.web.ViewResponseBean;
//import com.topideal.common.tools.ExcelUtilXlsx;
//import com.topideal.common.tools.FileExportUtil;
//import com.topideal.entity.dto.purchase.PurchaseSdOrderDTO;
//import com.topideal.entity.dto.purchase.PurchaseSdOrderPageDTO;
//import com.topideal.entity.vo.purchase.PurchaseSdOrderSditemModel;
//import com.topideal.order.service.purchase.PurchaseSdOrderService;
//import com.topideal.order.shiro.ShiroUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/purchaseSdOrder")
//public class PurchaseSdOrderController {
//
//	private static final Logger LOG = Logger.getLogger(PurchaseSdOrderController.class) ;
//
//	private static final String[] MAIN_KEY = {"code", "typeLabel", "purchaseCode", "poNo", "merchantName", "supplierName", "buName", "depotName", "totalNum",
//			"totalAmount", "currencyLabel", "goodsNo", "goodsName", "num", "price", "amount", "sdTypeName", "sdPrice", "sdAmount"} ;
//	private static final String[] MAIN_COLS = {"SD单号", "单据类型", "关联单号", "PO号", "公司", "供应商", "事业部", "仓库", "数量",
//			"金额", "币种", "商品货号", "商品名称", "商品数量", "商品单价", "商品金额", "SD类型", "SD单价", "SD金额"} ;
//
//	@Autowired
//	private PurchaseSdOrderService purchaseSdOrderService ;
//
//	/**
//	 * 访问列表页面
//	 * */
//	@RequestMapping("/toPage.html")
//	public String toPage() {
//		return "/derp/purchase/purchase-sd-list";
//	}
//
//	/**
//	 * 获取分页数据
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping("/purchaseSdOrderList.asyn")
//	@ResponseBody
//	private ViewResponseBean purchaseSdOrderList(PurchaseSdOrderPageDTO dto) {
//		try{
//			User user = ShiroUtils.getUser();
//
//			dto.setMerchantId(user.getMerchantId());
//
//			// 响应结果集
//			dto = purchaseSdOrderService.getPurchaseSdOrderPageList(dto);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(dto);
//	}
//
//	/**
//	 * 详情
//	 */
//	@RequestMapping("/toDetailsPage.html")
//	public String toDetailsPage(Long id, Model model) {
//
//		try {
//			PurchaseSdOrderDTO dto = purchaseSdOrderService.searchDTOById(id) ;
//			List<PurchaseSdOrderSditemModel> sdItemList = purchaseSdOrderService.getSdItemList(id) ;
//
//			model.addAttribute("detail", dto) ;
//			model.addAttribute("sdItemList", sdItemList) ;
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//			LOG.error(e.getMessage(), e);
//		}
//
//		return "/derp/purchase/purchase-sd-details";
//	}
//
//	/**
//	 * 访问导入页面
//	 * */
//	@RequestMapping("/toImportPage.html")
//	public String toImportPage() {
//		return "/derp/purchase/purchase-sd-import";
//	}
//
//	@RequestMapping("/exportOrder.asyn")
//	public void exportOrder(HttpServletResponse response, HttpServletRequest request,
//			PurchaseSdOrderPageDTO dto) throws Exception {
//
//		User user = ShiroUtils.getUser();
//
//		dto.setMerchantId(user.getMerchantId());
//
//		List<PurchaseSdOrderPageDTO> exportList = purchaseSdOrderService.getExportSdOrder(dto) ;
//
//		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList("采购SD单导出", MAIN_COLS, MAIN_KEY, exportList);
//
//		// 导出文件
//		FileExportUtil.export2007ExcelFile(wb, response, request, "采购SD单导出");
//	}
//
//	@RequestMapping("/getAmountAdjustmentDetail.asyn")
//	@ResponseBody
//	public ViewResponseBean getAmountAdjustmentDetail(Long id) {
//		try {
//
//			Map<String, Object> map = purchaseSdOrderService.getAmountAdjustmentDetail(id) ;
//
//			return ResponseFactory.success(map) ;
//		}catch (Exception e) {
//			e.printStackTrace();
//			LOG.error(e.getMessage(), e);
//
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
//		}
//	}
//
//	@RequestMapping("/saveAmountAdjustment.asyn")
//	@ResponseBody
//	public ViewResponseBean saveAmountAdjustment(@RequestParam("items")String itemList,
//			@RequestParam("id")String id) {
//		try {
//
//			if(StringUtils.isBlank(id)) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_303) ;
//			}
//
//			purchaseSdOrderService.saveAmountAdjustment(id, itemList) ;
//
//			return ResponseFactory.success() ;
//		}catch (DerpException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
//		}catch (Exception e) {
//			LOG.error(e.getMessage(), e);
//
//			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
//		}
//	}
//
//	@RequestMapping("/saveSdOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean saveSdOrder(@RequestParam("sdItems")String itemList,
//			@RequestParam("id")String id) {
//		try {
//
//			if(StringUtils.isBlank(id)) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_303) ;
//			}
//
//			User user = ShiroUtils.getUser();
//
//			purchaseSdOrderService.saveSdOrder(id, user) ;
//
//			return ResponseFactory.success() ;
//		}catch (Exception e) {
//			e.printStackTrace();
//			LOG.error(e.getMessage(), e);
//
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
//		}
//	}
//
//	@RequestMapping("/importOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean importOrder(@RequestParam(value = "file", required = false) MultipartFile file) {
//
//		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
//
//		try{
//			if (file == null) {
//				// 输入信息不完整
//				return ResponseFactory.error(StateCodeEnum.ERROR_303);
//			}
//
//			List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
//
//			if (data == null) {// 数据为空
//				return ResponseFactory.error(StateCodeEnum.ERROR_302);
//			}
//
//			User user = ShiroUtils.getUser();
//
//			resultMap = purchaseSdOrderService.importOrder(data, user) ;
//
//		}catch(SQLException e){
//			e.printStackTrace();
//			LOG.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//			e.printStackTrace();
//			LOG.error(e.getMessage(), e);
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//        	e.printStackTrace();
//        	LOG.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(resultMap);
//	}
//
//	@RequestMapping("/delSdOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean delSdOrder(@RequestParam("ids")String ids) {
//		try {
//
//			if(StringUtils.isBlank(ids)) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_303) ;
//			}
//
//			purchaseSdOrderService.delSdOrder(ids) ;
//
//			return ResponseFactory.success() ;
//		}catch (Exception e) {
//			LOG.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
//		}
//	}
//}
