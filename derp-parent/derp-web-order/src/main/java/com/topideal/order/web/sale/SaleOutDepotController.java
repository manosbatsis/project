//package com.topideal.order.web.sale;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.web.ResponseFactory;
//import com.topideal.common.system.web.StateCodeEnum;
//import com.topideal.common.system.web.ViewResponseBean;
//import com.topideal.common.tools.ExcelUtil;
//import com.topideal.common.tools.ExcelUtilXlsx;
//import com.topideal.common.tools.FileExportUtil;
//import com.topideal.common.tools.StrUtils;
//import com.topideal.common.tools.excel.ExportExcelSheet;
//import com.topideal.entity.dto.sale.SaleOutDepotDTO;
//import com.topideal.entity.dto.sale.SaleOutDepotItemDTO;
//import com.topideal.entity.vo.sale.SaleShelfModel;
//import com.topideal.order.service.sale.SaleOutDepotService;
//import com.topideal.order.shiro.ShiroUtils;
//
///**
// * 销售出库 controller
// * 
// */
//@RequestMapping("/saleOut")
//@Controller
//public class SaleOutDepotController {
//	
//	// 销售出库service
//	@Autowired
//	private SaleOutDepotService saleOutDepotService;
//	
//	/**
//	 * 访问列表页面
//	 * */
//	@RequestMapping("/toPage.html")
//	public String toPage(Model model) throws Exception {
//		return "/derp/sale/saleout-list";
//	}
//	
//	/**
//	 * 访问详情页面
//	 * */
//	@RequestMapping("/toDetailsPage.html")
//	public String toDetailsPage(Model model, Long id, String pageSource)throws Exception{
//		SaleOutDepotDTO dto = saleOutDepotService.searchDetail(id);
//		List<SaleOutDepotItemDTO> itemList = saleOutDepotService.listItemBySaleOutDepotId(id);
//		dto.setItemList(itemList);
//		// 查询是否有上架信息
//		SaleShelfModel saleShelfModel1=new SaleShelfModel();
//		saleShelfModel1.setOrderId(id);
//		 SaleShelfModel saleShelfModel2 = saleOutDepotService.listSaleOutShelfByPage(saleShelfModel1);
//		 if(saleShelfModel2.getList().size()>0){	// 如果有商品上架信息时
//			 model.addAttribute("isNotShelf", "yes");
//		 }else{			// 如果没有商品上架信息时,隐藏整个商品上架明细模块
//			 model.addAttribute("isNotShelf", "no");	
//		 }
//		if (StringUtils.isNotBlank(pageSource)) {
//			model.addAttribute("pageSource",pageSource);
//		}
//		model.addAttribute("detail", dto);
//		return "/derp/sale/saleout-details";
//	}
//
//	/**
//	 * 获取分页数据
//	 * */
//	@RequestMapping("/listSaleOutDepot.asyn")
//	@ResponseBody
//	private ViewResponseBean listSaleSaleOutDepot(SaleOutDepotDTO dto, HttpSession session) {
//		try{
//			User user= ShiroUtils.getUser(); 	
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());		
//			// 响应结果集
//			dto = saleOutDepotService.listSaleOutDepotByPage(dto,user);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(dto);
//	}
//
//	/**
//	 * 访问出库单导入页面
//	 */
//	@RequestMapping("toImportPage.html")
//	public String toImportPage() {
//		return "/derp/sale/saleout-import";
//	}
//
//	@RequestMapping("importSaleOutDepot.asyn")
//	@ResponseBody
//	public ViewResponseBean importSaleOutDepot(@RequestParam(value = "file", required = false) MultipartFile file) {
//		Map<String , Object> resultMap = new HashMap<String , Object>();// 返回的结果集
//		try {
//			if (file == null) {
//				// 输入信息不完整
//				return ResponseFactory.error(StateCodeEnum.ERROR_303);
//			}
//			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
//					file.getOriginalFilename(), 2);
//			if (data == null) {// 数据为空
//				return ResponseFactory.error(StateCodeEnum.ERROR_302);
//			}
//			
//			User user= ShiroUtils.getUser();
//			resultMap = saleOutDepotService.importSaleOutDepot(user, data);
//			
//			return ResponseFactory.success(resultMap);
//		}catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		
//	}
//	/**
//	 * 删除手工导入订单
//	 * @param ids
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping("delSaleOutDepot.asyn")
//	@ResponseBody
//	public ViewResponseBean delSaleOutDepot(String ids) {
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
//			int rows = saleOutDepotService.delImportOrder(list);
//			
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
//	/**
//	 * 审核
//	 * */
//	@RequestMapping("/auditSaleOutDepot.asyn")
//	@ResponseBody
//	private ViewResponseBean auditSaleOutDepot(String ids, HttpSession session) {
//		Map<String,Object> bl = null;
//		try{
//			//校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List list = StrUtils.parseIds(ids);
//            User user= ShiroUtils.getUser(); 
//			// 响应结果集
//			 bl = saleOutDepotService.auditSaleOutDepot(list,user.getId(),user.getName(), user.getMerchantId(), user.getMerchantName(),user.getTopidealCode());
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(bl);
//	}
//	/**
//	 * 获取导出销售出库清单的数量
//	 */
//	@RequestMapping("/getOrderCount.asyn")
//	@ResponseBody
//	private ViewResponseBean getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOutDepotDTO dto,String deliverDateStr) throws Exception{
//		Map<String,Object> data=new HashMap<String,Object>();
//		try{
//			User user= ShiroUtils.getUser(); 	
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			// 响应结果集
//			List<SaleOutDepotDTO> result = saleOutDepotService.listSaleOutDepot(dto,user);
//			data.put("total",result.size());
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(data);
//	}
//	/**
//	 * 导出销售出库单
//	 * */
//	@RequestMapping("/exportSaleOutDepot.asyn")
//	@ResponseBody
//	private void exportSaleOutDepot(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOutDepotDTO dto,String deliverDateStr) throws Exception{
//		User user= ShiroUtils.getUser(); 	
//		// 设置商家id
//		dto.setMerchantId(user.getMerchantId());
//		// 响应结果集
//		List<SaleOutDepotDTO> result = saleOutDepotService.listSaleOutDepot(dto,user);
//		List<SaleOutDepotItemDTO> itemList = new ArrayList<SaleOutDepotItemDTO>();
//		for(SaleOutDepotDTO sModel:result){
//			List<SaleOutDepotItemDTO> itemList1 = saleOutDepotService.listItemBySaleOutDepotId(sModel.getId());
//			for(SaleOutDepotItemDTO item:itemList1){
//				item.setSaleOutDepotCode(sModel.getCode());
//			}
//			if(itemList1 != null && itemList1.size()>0){
//				itemList.addAll(itemList1);
//			}
//		}
//		String sheetName = "销售出库清单";
//        String[] columns={"出库清单编号","单据状态","商家名称","客户名称","出库仓库","事业部","销售类型","销售订单编号","唯品账单号","LBX单号","物流企业名称","发货时间","提单号","运单号","签收日期","上架日期","备注"};
//        String[] keys = {"code", "statusLabel", "merchantName", "customerName", "outDepotName", "buName", "saleTypeLabel", "saleOrderCode", "vipsBillNo", "lbxNo", "logisticsName", "deliverDate", "blNo", "wayBillNo", "receiveDate", "shelfDate", "remark"} ;
//        
//        String[] columns1={"出库清单编号","商品编号","商品货号","商品名称","商品条形码","数量","批次号","生产日期","失效日期","海外仓理货单位"};
//        String[] keys1 = {"saleOutDepotCode", "goodsCode", "goodsNo", "goodsName", "barcode", "transferNum", "transferBatchNo", "productionDate", "overdueDate", "tallyingUnitLabel"} ;
//        //生成表格
//        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("基本信息", columns, keys, result) ;
//		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("商品信息", columns1, keys1, itemList) ;
//		
//		List<ExportExcelSheet> sheets = new ArrayList<>() ;
//		sheets.add(mainSheet) ;
//		sheets.add(itemSheet) ;
//		
//		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
//		//导出文件
//		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
//	}
//	
//	/**
//	 * 获取上架明细分页数据
//	 * */
//	@RequestMapping("/listSaleOutShelf.asyn")
//	@ResponseBody
//	private ViewResponseBean listSaleOutShelf(SaleShelfModel model) {
//		try{
//			// 响应结果集
//			model = saleOutDepotService.listSaleOutShelfByPage(model);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(model);
//	}
//	/**
//	 * 删除手工导入订单
//	 * @param ids
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping("/delSaleOut.asyn")
//	@ResponseBody
//	public ViewResponseBean delSaleOut(String ids) {
//		//校验id是否正确
//        boolean isRight = StrUtils.validateIds(ids);
//        if(!isRight){
//            //输入信息不完整
//            return ResponseFactory.error(StateCodeEnum.ERROR_303);
//        }
//        List<Long> list = StrUtils.parseIds(ids);
//        try {
//			int rows = saleOutDepotService.delImportOrder(list);
//			if(rows > 0) {
//				return ResponseFactory.success() ;
//			}else {
//				return ResponseFactory.error(StateCodeEnum.ERROR_301) ;
//			}
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
//		}
//	}
//	/**
//	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
//	 * */
//	@RequestMapping("/getOrderGoodsInfo.asyn")
//	@ResponseBody
//	private ViewResponseBean getOrderGoodsInfo(String ids,String type) {
//		Map<String,Integer> map =new HashMap<String,Integer>();
//		try{
//			//校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List list = StrUtils.parseIds(ids);
//			// 响应结果集
//            map = saleOutDepotService.getOrderGoodsInfo(list);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		
//		return ResponseFactory.success(map);
//	}
//}
