package com.topideal.order.web.sale;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.InventoryLocationAdjustExportDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderItemDTO;
import com.topideal.order.service.base.DepotInfoService;
import com.topideal.order.service.sale.InventoryLocationAdjustmentOrderService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 库位调整单 controller
 * 
 */
@RequestMapping("/inventoryLocationAdjustment")
@Controller
public class InventoryLocationAdjustmentOrderController {

	private static final String[] MAIN_COLUMNS = {"单据类型","仓库", "事业部","客户名称","归属日期", "调整原因", "平台订单号", "调增商品货号",
			"调减商品货号", "调整数量", "库存类型","平台编码","店铺编码"} ;

	private static final String[] MAIN_KEYS = {"typeLabel","depotName", "buName","customerName","ownDate","remark", "externalCode", "increaseGoodsNo",
			 "reduceGoodsNo", "adjustNum", "inventoryTypeLabel","platformCode","shopCode"} ;
	// 库位调整单service
	@Autowired
	private InventoryLocationAdjustmentOrderService inventoryLocationAdjustmentOrderService;
	// 仓库
	@Autowired
	private DepotInfoService depotInfoService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model, HttpSession session) throws Exception {
		return "/derp/sale/inventory-location-adjustment-list";
	}

	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listInventoryLocationAdjust.asyn")
	@ResponseBody
	private ViewResponseBean listInventoryLocationAdjust(InventoryLocationAdjustmentOrderDTO dto, HttpSession session) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = inventoryLocationAdjustmentOrderService.listInventoryLocationAdjust(dto,user);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 访问导入页面
	 * */
	@RequestMapping("/toImportPage.html")
	public String toImportPage(){
		return "/derp/sale/inventory-location-adjustment-import";
	}
	/**
	 * 导入
	 * */
	@RequestMapping("/importInventoryLocationAdjust.asyn")
	@ResponseBody
	public ViewResponseBean importInventoryLocationAdjust(@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
		try {
			if(file==null){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 2);
			if(data == null){//数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user= ShiroUtils.getUser();
			resultMap = inventoryLocationAdjustmentOrderService.saveInventoryLocationAdjust(data,user);

		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

	/**
	 * 获取导出的数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount(InventoryLocationAdjustmentOrderDTO dto) throws Exception{
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			Integer result = inventoryLocationAdjustmentOrderService.getOrderCount(dto);
			data.put("total",result);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}

	/**
	 * 导出
	 * */
	@RequestMapping("/exportInventoryLocationAdjust.asyn")
	@ResponseBody
	private void exportInventoryLocationAdjust(HttpServletResponse response, HttpServletRequest request,InventoryLocationAdjustmentOrderDTO dto) throws Exception{
		User user= ShiroUtils.getUser();
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());

		List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;

		// 响应结果集
		List<InventoryLocationAdjustExportDTO> mainList = inventoryLocationAdjustmentOrderService.getExportMainList(dto,user);
		String mainSheetName = "库位调整单";

		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, MAIN_COLUMNS, MAIN_KEYS, mainList);
		sheetList.add(mainSheet) ;

		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
	}

	/**
	 * 删除
	 * */
	@RequestMapping("/delInventoryLocationAdjust.asyn")
	@ResponseBody
	public ViewResponseBean delInventoryLocationAdjust(String ids) {
		try{
			//校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if(!isRight){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			boolean b = inventoryLocationAdjustmentOrderService.delInventoryLocationAdjust(list);
			if(!b){
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}
	/**
	 * 访问详情页面
	 * */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception{
		InventoryLocationAdjustmentOrderDTO inventoryLocationAdjustmentOrderDTO = new InventoryLocationAdjustmentOrderDTO();
		try {
			inventoryLocationAdjustmentOrderDTO = inventoryLocationAdjustmentOrderService.searchDetail(id);
			List<InventoryLocationAdjustmentOrderItemDTO> itemList = inventoryLocationAdjustmentOrderService.listItemByOrderId(id);
			inventoryLocationAdjustmentOrderDTO.setItemList(itemList);
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("detail", inventoryLocationAdjustmentOrderDTO);
		return "/derp/sale/inventory-location-adjustment-detail";
	}

	/**
	 * 审核
	 * @param ids
	 * @return
	 */
	@RequestMapping("/auditInventoryLocationAdjust.asyn")
	@ResponseBody
	public ViewResponseBean auditInventoryLocationAdjust(String ids) {
		//校验id是否正确
		boolean isRight = StrUtils.validateIds(ids);
		if(!isRight){
			//输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		List<Long> list = StrUtils.parseIds(ids);
		User user= ShiroUtils.getUser();
		try {
			inventoryLocationAdjustmentOrderService.auditInventoryLocationAdjust(list,user) ;
			return ResponseFactory.success() ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
		}
	}
}
