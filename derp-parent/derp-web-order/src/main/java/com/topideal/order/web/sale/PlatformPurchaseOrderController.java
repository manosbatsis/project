//package com.topideal.order.web.sale;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
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
//import com.topideal.common.tools.StrUtils;
//import com.topideal.entity.dto.sale.PlatformPurchaseOrderDTO;
//import com.topideal.entity.dto.sale.PlatformPurchaseOrderExportDTO;
//import com.topideal.entity.dto.sale.PlatformPurchaseOrderItemDTO;
//import com.topideal.order.service.sale.PlatformPurchaseOrderService;
//import com.topideal.order.shiro.ShiroUtils;
//
///**
// * 平台采购单
// * @author gy
// *
// */
//@RequestMapping("/platformPurchaseOrder")
//@Controller
//public class PlatformPurchaseOrderController {
//
//	/* 打印日志 */
//	protected Logger LOGGER = LoggerFactory.getLogger(PlatformPurchaseOrderController.class);
//
//	private static final String[] MAIN_COLUMNS = {"公司", "PO号", "客户", "下单时间", "入库时间", "币种", "采购总金额","客户仓库", "平台状态",
//			"单据状态", "提交人", "转销人", "转销时间", "条形码", "商品名称",
//			"采购数量", "入库好品量", "入库坏品量" ,"单价", "金额"} ;
//
//	private static final String[] MAIN_KEYS = {"merchantName", "poNo", "customerName", "orderTime", "deliverDate", "currencyLabel", "amount",
//					"platformDepotName", "platformStatusLabel", "statusLabel", "submitName", "resaleName", "resaleDate",
//					"platformBarcode", "platformGoodsName", "num", "receiptOkNum", "receiptBadNum", "price", "itemAmount"} ;
//
//	@Autowired
//	PlatformPurchaseOrderService platformPurchaseService ;
//
//	/**
//	 * 访问列表页面
//	 * */
//	@RequestMapping("/toPage.html")
//	public String toPage(Model model) throws Exception {
//		return "/derp/sale/platform-purchase-list";
//	}
//
//	/**
//	 * 访问详情页面
//	 * */
//	@RequestMapping("/toDetailsPage.asyn")
//	@ResponseBody
//	public ViewResponseBean toDetailsPage(Long id){
//
//		try {
//			PlatformPurchaseOrderDTO dto = platformPurchaseService.searchDTOById(id) ;
//
//			return ResponseFactory.success(dto) ;
//
//		} catch (SQLException e) {
//			LOGGER.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//		}
//
//	}
//
//	/**
//	 * 获取分页数据
//	 * */
//	@RequestMapping("/listPlatformPurchaseOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean listPlatformPurchaseOrder(PlatformPurchaseOrderDTO dto,Model model) {
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			// 响应结果集
//			dto = platformPurchaseService.listPlatformPurchaseOrder(dto);
//		}catch(Exception e){
//			LOGGER.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(dto);
//	}
//	@RequestMapping("/listPlatformPurchaseOrderByCodeAndPoNo.asyn")
//	@ResponseBody
//	public ViewResponseBean listPlatformPurchaseOrderByCodeAndPoNo(String code, String poNo, Model model) {
//		List<PlatformPurchaseOrderDTO> list = null;
//		try{
//			PlatformPurchaseOrderDTO dto = new PlatformPurchaseOrderDTO();
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			dto.setPoNo(poNo);
//			// 响应结果集
//			list = platformPurchaseService.listPlatformPurchaseOrderByCodeAndPoNo(dto);
//		}catch(Exception e){
//			LOGGER.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(list);
//	}
//
//
//	/**
//	 * 提交、驳回
//	 * */
///*	@SuppressWarnings("unchecked")
//	@RequestMapping("/changeStatus.asyn")
//	@ResponseBody
//	public ViewResponseBean changeStatus(String ids, String status) {
//		try{
//			//校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List<Long> list = StrUtils.parseIds(ids);
//
//            platformPurchaseService.modifyStatus(list, status) ;
//
//		}catch(Exception e){
//			LOGGER.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success();
//	}*/
//
//	/**
//	 * 检查能否转销售单
//	 * */
//	@SuppressWarnings("unchecked")
//	@RequestMapping("/checkOrderToSales.asyn")
//	@ResponseBody
//	public ViewResponseBean checkOrderToSales(String ids) {
//		Map<String, Object> resultMap = null ;
//		try{
//			//校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List<Long> list = StrUtils.parseIds(ids);
//
//            resultMap = platformPurchaseService.checkOrderToSales(list) ;
//
//		}catch(Exception e){
//			LOGGER.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(resultMap);
//	}
//
//	/**
//	 * 导出
//	 * */
//	@RequestMapping("/exportPlatformPurchaseOrder.asyn")
//	@ResponseBody
//	private void exportPlatformPurchaseOrder(HttpServletResponse response,
//			HttpServletRequest request,PlatformPurchaseOrderExportDTO dto) throws Exception{
//		User user= ShiroUtils.getUser();
//		// 设置商家id
//		dto.setMerchantId(user.getMerchantId());
//
//		List<PlatformPurchaseOrderExportDTO> exportList = platformPurchaseService.getExportList(dto) ;
//
//        //生成表格
//		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList("平台采购订单", MAIN_COLUMNS, MAIN_KEYS, exportList) ;
//		//导出文件
//		FileExportUtil.export2007ExcelFile(wb, response, request, "平台采购订单导出");
//	}
//
//	/**
//	 * 弹框获取明细
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping("/getPlatformPurchaseOrderItems.asyn")
//	@ResponseBody
//	public ViewResponseBean getPlatformPurchaseOrderItems(String ids) {
//
//		//校验id是否正确
//        boolean isRight = StrUtils.validateIds(ids);
//        if(!isRight){
//            //输入信息不完整
//            return ResponseFactory.error(StateCodeEnum.ERROR_303);
//        }
//        List<Long> list = StrUtils.parseIds(ids);
//
//		try {
//			List<PlatformPurchaseOrderItemDTO> itemDTOList = platformPurchaseService.getPlatformPurchaseOrderItems(list) ;
//
//			return ResponseFactory.success(itemDTOList) ;
//		} catch (SQLException e) {
//			LOGGER.error(e.getMessage(), e);
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//	}
//}
