package com.topideal.order.web.sale;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;
import com.topideal.entity.dto.sale.PreSaleOrderDTO;
import com.topideal.entity.dto.sale.PreSaleOrderItemDTO;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.base.DepotInfoService;
import com.topideal.order.service.sale.PreSaleOrderService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 预售单 controller
 *
 */
@RequestMapping("/preSale")
@Controller
public class PreSaleOrderController {
	// 预售单service
	@Autowired
	private PreSaleOrderService preSaleOrderService;
	// 仓库
	@Autowired
	private DepotInfoService depotInfoService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model, HttpSession session) throws Exception {
		return "/derp/sale/pre-sale-list";
	}

	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listPreSaleOrder.asyn")
	@ResponseBody
	private ViewResponseBean listPreSaleOrder(PreSaleOrderDTO dto, HttpSession session) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = preSaleOrderService.listPreSaleOrderByPage(dto,user);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 删除
	 * */
	@RequestMapping("/delPreSaleOrder.asyn")
	@ResponseBody
	public ViewResponseBean delPreSaleOrder(String ids) {
		try{
			//校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if(!isRight){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			boolean b = preSaleOrderService.delPreSaleOrder(list);
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
		PreSaleOrderDTO preSaleOrderDTO = new PreSaleOrderDTO();
		try {
			preSaleOrderDTO = preSaleOrderService.searchDetail(id);
			List<PreSaleOrderItemDTO> itemList = preSaleOrderService.listItemByOrderId(id);
			preSaleOrderDTO.setItemList(itemList);
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("detail", preSaleOrderDTO);
		return "/derp/sale/pre-sale-details";
	}
	/**
	 * 获取导出预售单的数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,PreSaleOrderDTO dto) throws Exception{
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			List<PreSaleOrderDTO> result = preSaleOrderService.listPreSaleOrder(dto,user);
			data.put("total",result.size());
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}
	/**
	 * 导出预售单
	 * */
	@RequestMapping("/exportPreSaleOrder.asyn")
	@ResponseBody
	private void exportPreSaleOrder(HttpSession session, HttpServletResponse response, HttpServletRequest request, PreSaleOrderDTO dto) throws Exception{
		User user= ShiroUtils.getUser();
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
		// 响应结果集
		List<PreSaleOrderDTO> result = preSaleOrderService.listPreSaleOrder(dto,user);
		List<PreSaleOrderItemDTO> itemList = new ArrayList<PreSaleOrderItemDTO>();
		for(PreSaleOrderDTO preSaleOrderDTO:result){
			List<PreSaleOrderItemDTO> itemList1 = preSaleOrderService.listItemByOrderId(preSaleOrderDTO.getId());
			for(PreSaleOrderItemDTO item:itemList1){
				item.setOrderCode(preSaleOrderDTO.getCode());
			}
			if(itemList1 != null && itemList1.size()>0){
				itemList.addAll(itemList1);
			}
		}
		String fileName = "预售单导出模板";
		String[] columns={"公司","事业部","预售单号","客户","销售类型","出仓仓库","PO号","销售币种","理货单位","创建人","审核人","审核时间"};
		String[] keys = {"merchantName", "buName", "code", "customerName", "businessModelLabel", "outDepotName", "poNo", "currencyLabel", "tallyingUnitLabel", "createName", "auditName", "auditDate"} ;

		String[] columns1={"预售单号","商品货号","商品名称","条码","预售数量","预售单价","预售总金额","品牌"};
		String[] keys1={"orderCode","goodsNo","goodsName","barcode","num","price","amount","brandName"};
		//生成表格

		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("预售单信息", columns, keys, result);
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("预售单商品信息", columns1, keys1, itemList);

		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
		sheets.add(mainSheet) ;
		sheets.add(itemSheet) ;

		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
	}
	/**
	 * 访问新增页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, HttpSession session)throws Exception{
		//初始化数据
		model.addAttribute("itemCount", 0);
		User user= ShiroUtils.getUser();
		model.addAttribute("merchantId", user.getMerchantId());
		model.addAttribute("merchantName", user.getMerchantName());
		return "/derp/sale/pre-sale-add";
	}
	/**
	 * 新增/修改(仅保存)
	 * */
	@RequestMapping("/saveOrModifyTempOrder.asyn")
	@ResponseBody
	public ViewResponseBean saveOrModifyTempOrder(String json,HttpSession session) {
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			boolean b = preSaleOrderService.saveOrModifyTempOrder(json,user.getId(),user.getName(), user.getTopidealCode());
			if(!b){
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}
	/**
	 * 提交并审核
	 * */
	@RequestMapping("/addPreSaleOrder.asyn")
	@ResponseBody
	public ViewResponseBean addPreSaleOrder(String json,HttpSession session) {
		String msg = null;
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			msg = preSaleOrderService.addPreSaleOrder(json,user.getId(),user.getName(), user.getTopidealCode());
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(msg);
	}
	/**
	 * 访问编辑页面
	 * */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id, HttpSession session)throws Exception{
		PreSaleOrderDTO preSaleOrderDTO = preSaleOrderService.searchDetail(id);
		List<PreSaleOrderItemDTO> itemList = preSaleOrderService.listItemByOrderId(id);
		preSaleOrderDTO.setItemList(itemList);
		model.addAttribute("detail", preSaleOrderDTO);
		model.addAttribute("itemCount", itemList.size());
		model.addAttribute("detail", preSaleOrderDTO);
		DepotInfoMongo outDepot = depotInfoService.getDetails(preSaleOrderDTO.getOutDepotId());

		int isTallyingRequired = 0;	// 理货单位是否必填
		// 若是海外仓，理货单位必填
		if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())){
			isTallyingRequired = 1;
		}
		model.addAttribute("isTallyingRequired", isTallyingRequired);
		return "/derp/sale/pre-sale-edit";
	}
	/**
	 * 修改并审核
	 * */
	@RequestMapping("/modifyPreSaleOrder.asyn")
	@ResponseBody
	public ViewResponseBean modifyPreSaleOrder(String json, HttpSession session) {
		String msg=null;
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			msg = preSaleOrderService.modifyPreSaleOrder(json,user.getId(),user.getName(),user.getTopidealCode());
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(msg);
	}

	/**
	 * 校验预售单能否转成销售单
	 * */
	@RequestMapping("/checkPreSale.asyn")
	@ResponseBody
	private ViewResponseBean checkPreSale(String ids,String codes) {
		List<PreSaleOrderCorrelationDTO> list = null;
		try{
			// 响应结果集
			list = preSaleOrderService.checkPreSale(ids,codes);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(list);
	}

	/**
	 * 校验能否转成采购订单
	 */
	@RequestMapping("/checkOrderState.asyn")
	@ResponseBody
	private ViewResponseBean checkOrderState(Long id) {
		PreSaleOrderDTO dto= null;
		try{
			// 响应结果集
			dto = preSaleOrderService.checkOrderState(id);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
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
		return "/derp/sale/pre-sale-import";
	}
	/**
	 * 导入预售单
	 * */
	@RequestMapping("/importPreSale.asyn")
	@ResponseBody
	public ViewResponseBean importPreSale(@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) {
		Map resultMap = new HashMap();//返回的结果集
		try{
			if(file==null){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 2);
			if(data == null){//数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user= ShiroUtils.getUser();
			resultMap = preSaleOrderService.saveImportPreSaleOrder(data,user.getId(),user.getName(),user.getMerchantId(),user.getMerchantName(), user.getTopidealCode(),user.getRelMerchantIds());
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(resultMap);
	}
	/**
	 * 根据PO号判断是否存在采购单
	 * */
	@RequestMapping("/checkExistPurchaseByPO.asyn")
	@ResponseBody
	private ViewResponseBean checkExistPurchaseByPO(String poNo) {
		Map<String,String> data=new HashMap<String,String>();
		try{
			// 响应结果集
			User user= ShiroUtils.getUser();
			data = preSaleOrderService.checkExistPurchaseByPO(poNo, user);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}
	/**
	 * 列表页面 生成采购订单
	 * @param json
	 * @param buId
	 * @param depotId
	 * @return
	 */
	@RequestMapping("/GeneratePurchaseOrder.asyn")
	@ResponseBody
	public ViewResponseBean GeneratePurchaseOrder(String json) {
		Long id = null;
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			id = preSaleOrderService.GeneratePurchaseOrder(json,user);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(id);
	}
}
