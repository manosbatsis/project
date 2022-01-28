package com.topideal.order.web.sale;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.topideal.entity.dto.sale.BuMoveInventoryDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryExportDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryItemDTO;
import com.topideal.order.service.sale.BuMoveInventoryService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 事业部移库单 controller
 */
@RequestMapping("/buMoveInventory")
@Controller
public class BuMoveInventoryController {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(BuMoveInventoryController.class);

	private static final String[] COLUMNS = { "移库单号", "来源业务单号", "移出事业部", "移入事业部", "仓库",
			"创建日期", "移库日期", "商品货号", "商品条码", "商品名称", "销售数量", "创建人", "移库状态"};

	private static final String[] KEYS = { "code", "businessNo", "outBuName", "inBuName", "depotName",
			"createDate","moveDate", "goodsNo", "barcode", "goodsName", "num", "createName",
			"statusLabel"};

	// 事业部移库单service
	@Autowired
	private BuMoveInventoryService buMoveInventoryService;
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model, HttpSession session) throws Exception {
		return "/derp/sale/bumove-inventory-list";
	}
	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listBuMoveInventory.asyn")
	@ResponseBody
	private ViewResponseBean listBuMoveInventory(BuMoveInventoryDTO dto, HttpSession session) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = buMoveInventoryService.listBuMoveInventoryByPage(dto,user);
		}catch(SQLException e){
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 访问详情页面
	 * */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception{
		BuMoveInventoryDTO buMoveInventoryDTO = buMoveInventoryService.searchDetail(id);
		List<BuMoveInventoryItemDTO> itemList = buMoveInventoryService.listItemByOrderId(id);
		buMoveInventoryDTO.setItemList(itemList);
		model.addAttribute("detail", buMoveInventoryDTO);
		return "/derp/sale/bumove-inventory-details";
	}
	/**
	 * 导入页面
	 * */
	@RequestMapping("/toImportPage.html")
	public String toImportPage(){
		return "/derp/sale/bumove-inventory-import";
	}

	/**
	 * 获取导出的数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,BuMoveInventoryDTO dto) throws Exception{
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			List<BuMoveInventoryDTO> result = buMoveInventoryService.listBuMoveInventoryDTO(dto,user);
			data.put("total",result.size());
		}catch(SQLException e){
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}
	/**
	 * 导出
	 * */
	@RequestMapping("/exportBuMoveInventory.asyn")
	@ResponseBody
	private void exportBuMoveInventory(HttpSession session, HttpServletResponse response, HttpServletRequest request,BuMoveInventoryDTO dto) throws Exception{
		User user= ShiroUtils.getUser();
		String sheetName = "移库单导出模板";
		dto.setMerchantId(user.getMerchantId());
		// 获取导出的信息
		List<BuMoveInventoryExportDTO> result = buMoveInventoryService.getDetailsByExport(dto,user);
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, result) ;
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	/**
	 * 删除
	 * */
	@RequestMapping("/delBuMoveInventory.asyn")
	@ResponseBody
	public ViewResponseBean delBuMoveInventory(String ids) {
		try{
			//校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if(!isRight){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			boolean b = buMoveInventoryService.delBuMoveInventory(list);
			if(!b){
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		}catch(SQLException e){
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(NullPointerException e){
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 导入
	 * @param file
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/importBuMoveInventory.asyn")
	@ResponseBody
	public ViewResponseBean importBuMoveInventory(@RequestParam(value = "file", required = false) MultipartFile file,
											HttpSession session) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 2);
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user= ShiroUtils.getUser();
			resultMap = buMoveInventoryService.saveImportBuMoveInventory(data, user.getId(), user.getName(),
					user.getMerchantId(), user.getMerchantName(),user.getTopidealCode());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

	@RequestMapping("/auditBuMoveInventory.asyn")
	@ResponseBody
	public ViewResponseBean auditBuMoveInventory(String ids) {
		//校验id是否正确
		boolean isRight = StrUtils.validateIds(ids);
		if(!isRight){
			//输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		List<Long> list = StrUtils.parseIds(ids);
		User user= ShiroUtils.getUser();
		try {
			buMoveInventoryService.auditBuMoveInventory(list,user) ;
			return ResponseFactory.success() ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305 , e) ;
		}
	}

}
