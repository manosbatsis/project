//package com.topideal.order.web.sale;
//
//import com.topideal.common.constant.DERP;
//import com.topideal.common.constant.DERP_SYS;
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.web.ResponseFactory;
//import com.topideal.common.system.web.StateCodeEnum;
//import com.topideal.common.system.web.ViewResponseBean;
//import com.topideal.common.tools.ExcelUtil;
//import com.topideal.common.tools.ExcelUtilXlsx;
//import com.topideal.common.tools.FileExportUtil;
//import com.topideal.common.tools.StrUtils;
//import com.topideal.entity.dto.sale.OrderReturnIdepotBatchDTO;
//import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;
//import com.topideal.entity.dto.sale.OrderReturnIdepotItemDTO;
//import com.topideal.order.service.sale.OrderReturnIdepotService;
//import com.topideal.order.shiro.ShiroUtils;
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
//import javax.servlet.http.HttpSession;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 电商订单退货 controller
// * 
// */
//@RequestMapping("/orderReturnIdepot")
//@Controller
//public class OrderReturnIdepotController {
//
//	// 电商订单service
//	@Autowired
//	private OrderReturnIdepotService  orderReturnIdepotService;
//	/**
//	 * 访问列表页面
//	 * */
//	@RequestMapping("/toPage.html")
//	public String toPage(Model model) throws Exception {
//		return "/derp/sale/order-return-idepot-list";
//	}
//	/**
//	 * 查看某个商品的详情
//	 * */
//	@RequestMapping("/listItemDetailsById.html")
//	@ResponseBody
//	public ViewResponseBean listItemDetailsById(Long id)throws Exception{
//		List<OrderReturnIdepotItemDTO> itemList = new  ArrayList<OrderReturnIdepotItemDTO>();
//		OrderReturnIdepotDTO orderReturnIdepotDTO =new OrderReturnIdepotDTO();
//		try{
//			orderReturnIdepotDTO = orderReturnIdepotService.searchDetail(id);
//			itemList = orderReturnIdepotService.listItemByOrderId(id);
//			orderReturnIdepotDTO.setItemList(itemList);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(orderReturnIdepotDTO);
//	}
//	/**
//	 * 访问详情页面
//	 * */
//	@RequestMapping("/toDetailsPage.html")
//	public String toDetailsPage(Model model, Long id)throws Exception{
//		OrderReturnIdepotDTO orderReturnIdepotDTO = orderReturnIdepotService.searchDetail(id);
//		List<OrderReturnIdepotItemDTO> itemList = orderReturnIdepotService.listItemByOrderId(id);
//		orderReturnIdepotDTO.setItemList(itemList);
//
//		if(itemList == null){
//			itemList = new ArrayList<OrderReturnIdepotItemDTO>();
//		}
//		List<Long> itemIdList=new ArrayList<>();
//		for (int i = 0; i < itemList.size(); i++) {
//			itemIdList.add(itemList.get(i).getId());
//		}
//		List<OrderReturnIdepotBatchDTO> batchAllList  = orderReturnIdepotService.listOrderReturnBatchById(itemIdList);
//
//		// 脱敏信息 加****
//		orderReturnIdepotDTO.setBuyerName(StrUtils.desensitization(orderReturnIdepotDTO.getBuyerName()));
//		orderReturnIdepotDTO.setBuyerPhone(StrUtils.desensitization(orderReturnIdepotDTO.getBuyerPhone()));
//		orderReturnIdepotDTO.setReturnAddr(StrUtils.desensitization(orderReturnIdepotDTO.getReturnAddr()));
//		
//		model.addAttribute("detail", orderReturnIdepotDTO);
//		model.addAttribute("batchData", batchAllList);
//		return "/derp/sale/order-return-idepot-details";
//	}
//
//	/**
//	 * 访问导入页面
//	 *
//	 */
//	@RequestMapping("toImportPage.html")
//	public String toImportPage() {
//		return "/derp/sale/order-return-idepot-import";
//	}
//
//	/**
//	 * 获取分页数据
//	 * */
//	@RequestMapping("/listOrder.asyn")
//	@ResponseBody
//	private ViewResponseBean listOrder(OrderReturnIdepotDTO dto, HttpSession session) {
//		Map<String,Object> data=new HashMap<String,Object>();
//		try{
//			User user= ShiroUtils.getUser(); 
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			// 响应结果集
//			dto = orderReturnIdepotService.listOrderAndItemByPage(dto);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(dto);
//	}
//	/**
//	 * 获取导出电商订单退货的数量
//	 */
//	@RequestMapping("/getOrderCount.asyn")
//	@ResponseBody
//	private ViewResponseBean getOrderCount(OrderReturnIdepotDTO dto,HttpSession session) {
//		Map<String,Object> data=new HashMap<String,Object>();
//		try{
//			User user= ShiroUtils.getUser(); 
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			// 响应结果集
//			int count = orderReturnIdepotService.listOrder(dto);
//			data.put("total",count);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(data);
//	}
//	/**
//	 * 导出电商订单
//	 * */
//	@RequestMapping("/exportOrder.asyn")
//	@ResponseBody
//	private void exportOrder(HttpSession session, HttpServletResponse response, HttpServletRequest request, OrderReturnIdepotDTO dto) throws Exception{
//		User user= ShiroUtils.getUser(); 
//		// 设置商家id
//		dto.setMerchantId(user.getMerchantId());
//		//获取数据
//		List<Map<String,Object>> result = orderReturnIdepotService.getExportOrderMap(dto);
//		for (Map<String, Object> map : result) {
//			map.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, (String) map.get("store_platform_code")));
//			map.put("currency", DERP.getLabelByKey(DERP.currencyCodeList, (String) map.get("currency")));
//			map.put("shopTypeName", DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, (String) map.get("shop_type_code")));		
//			// 脱敏
//			map.put("buyer_name", StrUtils.desensitization((String) map.get("buyer_name")));
//			map.put("buyer_phone", StrUtils.desensitization((String) map.get("buyer_phone")));
//			map.put("return_addr", StrUtils.desensitization((String) map.get("return_addr")));
//
//		}
//        String sheetName = "电商退货订单导出模板";
//        String[] columns={"ERP系统订单号","入库单号" ,"来源交易订单","平台名称", "店铺名称","店铺类型","退货单类型","退货入库仓库","退货创建时间",
//        		"退货入库时间","事业部","客户","商品名称","商品货号","商品条形码","标准条码","退货正品数量","退货残品数量","退货总数量","销售单价", "销售金额" ,
//        		"实际退款总金额","币种","批次号","生产日期","失效日期","买家姓名","买家手机","退货人地址","邮编","物流公司","物流运单号","原发货单号"};
//        String[] keys={"code","in_depot_code","external_code","storePlatformName", "shop_name","shopTypeName" ,"return_type","return_in_depot_name",
//        		"return_in_create_date","return_in_date","bu_name","customer_name","in_goods_name","in_goods_no","barcode","commbarcode","batchReturnNum","batchBadGoodsNum",
//        		"batchTotalNum","price", "dec_total", "amount" ,"currency","batch_no","production_date","overdue_date","buyer_name","buyer_phone",
//        		"return_addr","postcode","logistics_name","logistics_way_bill_no","original_delivery_code"};
//        //生成表格
//		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
//		//导出文件
//		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
//	}
//	
//	@RequestMapping("importOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean importOrder(@RequestParam(value = "file", required = false) MultipartFile file) {
//		Map<String , Object> resultMap = new HashMap<String , Object>();// 返回的结果集
//		try {
//			if (file == null) {
//				// 输入信息不完整
//				return ResponseFactory.error(StateCodeEnum.ERROR_303);
//			}
//			
//			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
//			
//			if (data == null) {// 数据为空
//				return ResponseFactory.error(StateCodeEnum.ERROR_302);
//			}
//			
//			User user= ShiroUtils.getUser();
//			resultMap = orderReturnIdepotService.importOrder(user , data) ;
//			
//			return ResponseFactory.success(resultMap);
//		}catch (NullPointerException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//		}catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}	
//	}
//	
//	/**
//	 * 删除手工导入订单
//	 * @param ids
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping("delOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean delOrder(String ids) {
//		//校验id是否正确
//        boolean isRight = StrUtils.validateIds(ids);
//        if(!isRight){
//            //输入信息不完整
//            return ResponseFactory.error(StateCodeEnum.ERROR_303);
//        }
//        
//        List<Long> list = StrUtils.parseIds(ids);
//        
//        try {
//			int rows = orderReturnIdepotService.delImportOrder(list);
//			if(rows > 0) {
//				return ResponseFactory.success() ;
//			}else {
//				return ResponseFactory.error(StateCodeEnum.ERROR_301) ;
//			}
//			
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
//		}
//	}
//	
//	/**
//	 * 检查手工导入订单是否满足条件（导入并且待入库）
//	 * 
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping("checkConditionsOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean checkConditionsOrder(String ids) {
//		User user= ShiroUtils.getUser();
//		//校验id是否正确
//        boolean isRight = StrUtils.validateIds(ids);
//        if(!isRight){
//            //输入信息不完整
//            return ResponseFactory.error(StateCodeEnum.ERROR_303);
//        }
//        
//        List<Long> list = StrUtils.parseIds(ids);
//        
//        try {
//			boolean flag = orderReturnIdepotService.checkConditionsOrder(list) ;
//			
//			if(flag) {
//				
//				orderReturnIdepotService.getOrderGoodsInfo(list,user.getMerchantId()) ;
//				
//				return ResponseFactory.success() ;
//			}else {
//				return ResponseFactory.error(StateCodeEnum.ERROR_305 , new RuntimeException("操作失败，所选项存在非手工导入订单")) ;
//			}
//			
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
//		}
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping("examineOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean examineOrder(String ids) {
//		//校验id是否正确
//        boolean isRight = StrUtils.validateIds(ids);
//        if(!isRight){
//            //输入信息不完整
//            return ResponseFactory.error(StateCodeEnum.ERROR_303);
//        }
//        
//        List<Long> list = StrUtils.parseIds(ids);
//        
//        User user= ShiroUtils.getUser();
//        
//        try {
//			orderReturnIdepotService.examineOrder(list , user.getTopidealCode(),user) ;
//			
//			return ResponseFactory.success() ;
//			
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
//		}
//	}
//}
