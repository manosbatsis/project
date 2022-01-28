/*package com.topideal.order.web.purchase;

import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.common.tools.excelReader.ExcelReader;
import com.topideal.entity.dto.purchase.PurchaseWarehouseDTO;
import com.topideal.entity.dto.purchase.PurchaseWarehouseExportBean;
import com.topideal.entity.dto.purchase.PurchaseWarehouseExportDTO;
import com.topideal.entity.dto.purchase.PurchaseWarehouseItemExportDTO;
import com.topideal.entity.vo.purchase.PurchaseWarehouseBatchModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.purchase.PurchaseWarehouseBatchService;
import com.topideal.order.service.purchase.PurchaseWarehouseService;
import com.topideal.order.shiro.ShiroUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*//**
 * 采购入库单 controller
 * 
 *//*
@RequestMapping("/warehouse")
@Controller
public class PurchaseWarehouseController {

	 打印日志 
	protected Logger LOGGER = LoggerFactory.getLogger(PurchaseWarehouseController.class);

	*//** 标题 *//*
	private static final String[] COLUMNS1 = { "公司名称", "事业部","入库单号", "仓库名称", "预申报单号", "理货单号", "理货时间", "合同号", "外部单号",
			"单据状态", "托板数量", "LBX单号", "入库时间" };
	private static final String[] KEYS1 = {"merchantName", "buName", "warehouseCode", "depotName", "declareCode", "tallyingCode", "tallyingDate", "contractNo", "extraCode",
			"stateLabel", "palletNum", "lbxNo", "inboundDate"} ;
	
	private static final String[] COLUMNS2 = { "企业单号", "商品货号", "商品名称", "商品条形码", "标准条码", "应收数量(申报数量)", "入库数量", "缺失数量", "多货数量","过期数量",
			"可售数量", "批次号", "生产日期", "失效日期","海外仓理货单位" };
	private static final String[] KEYS2 = {"declareCode", "goodsNo", "goodsName", "barcode", "commbarcode", "purchaseNum", "tallyingNum", "lackNum", "multiNum", "expireNum",
			"normalNum2", "batchNo", "productionDate", "overdueDate", "tallyingUnitLabel"} ;
	
	private static final String[] COLUMNS3 = { "入库单号", "商品货号","批次号","生产日期","失效日期", "是否勾稽", "勾稽数量", "未勾稽数量", "采购订单号" };
	private static final String[] KEYS3 = {"warehouseCode", "goodsNo", "batchNo", "productionDate", "overdueDate", "isArticulation", "num", "num2", "purchaseCode"} ;
	
	// 采购入库service
	@Autowired
	private PurchaseWarehouseService purchaseWarehouseService;
	// 采购入库商品批次
	@Autowired
	private PurchaseWarehouseBatchService purchaseWarehouseBatchService;
	@Autowired
	private CommonBusinessService commonBusinessService ;

	*//**
	 * 访问列表页面
	 *//*
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		return "/derp/purchase/warehouse-list";
	}

	*//**
	 * 访问详情页面
	 *//*
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id) throws Exception {
		PurchaseWarehouseDTO dto = purchaseWarehouseService.searchDTODetail(id);
		model.addAttribute("detail", dto);
		List<PurchaseWarehouseBatchModel> list = purchaseWarehouseBatchService.getGoodsAndBatch(id);
		model.addAttribute("batchBean", list);
		return "/derp/purchase/warehouse-details";
	}

	*//**
	 * 访问入库单导入页面
	 *//*
	@RequestMapping("/toImportPage.html")
	public String toImportPage() throws Exception {
		return "/derp/purchase/warehouse-import";
	}

	*//**
	 * 访问关联采购单导入页面
	 *//*
	@RequestMapping("/toRelationImportPage.html")
	public String toRelationImportPage() throws Exception {
		return "/derp/purchase/warehouse-relation-import";
	}

	*//**
	 * 获取分页数据
	 *//*
	@RequestMapping("/listPurchaseWarehouse.asyn")
	@ResponseBody
	private ViewResponseBean listPurchaseWarehouse(PurchaseWarehouseDTO dto) {
		try {
			User user= ShiroUtils.getUser(); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseWarehouseService.listPurchaseWarehousePage(dto, user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}

	*//**
	 * 确认入仓
	 *//*
	@SuppressWarnings("unchecked")
	@RequestMapping("/confirmDepot.asyn")
	@ResponseBody
	private ViewResponseBean confirmDepot(String ids, HttpSession session) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser(); 
			List<Long> list = StrUtils.parseIds(ids);
			
			boolean flag = true ;
			StringBuffer msgSb = new StringBuffer() ;
			
			List<InvetAddOrSubtractRootJson> jsonList = purchaseWarehouseService.confirmDepot(list, user.getId(), user.getName(),
					user.getTopidealCode());
			
			for (InvetAddOrSubtractRootJson invetAddOrSubtractRootJson : jsonList) {
				try {
					commonBusinessService.saveAutoPurchaseAnalysis(invetAddOrSubtractRootJson.getOrderNo());
				} catch (DerpException e) {
					
					flag &= false ;
					
					if(msgSb.length() > 0) {
						msgSb.append("<br/>") ;
					}
					
					msgSb.append(e.getMessage()) ;
					
					commonBusinessService.modifyCorrelationstatus((PurchaseWarehouseModel)e.getObj());
				}
			}
			
			if(flag) {
				purchaseWarehouseService.pushInventory(jsonList);
			}
			
			if(msgSb.length() == 0) {
				msgSb.append("成功") ;
			}
			
			return ResponseFactory.success(msgSb.toString());
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
	}

	*//**
	 * 关联采购单导入
	 * 
	 * @param
	 * @return int
	 * @throws IOException
	 *//*
	@RequestMapping("/importRelation.asyn")
	@ResponseBody
	public ViewResponseBean importRelation(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			ExcelReader reader = ExcelUtil.getExcelReader(file.getOriginalFilename()) ;
			List<Map<String, String>> data = reader.processSingleSheet(file.getInputStream());
			
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user= ShiroUtils.getUser(); 
			resultMap = purchaseWarehouseService.importRelation(data, user);
		} catch (DerpException e) {
			return ResponseFactory.success(e.getObj());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

	*//**
	 * 入库单导入
	 * 
	 * @param
	 * @return int
	 * @throws IOException
	 *//*
	@RequestMapping("/importWarehouse.asyn")
	@ResponseBody
	public ViewResponseBean importWarehouse(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 1);
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user= ShiroUtils.getUser(); 
			resultMap = purchaseWarehouseService.saveImportWarehouse(data, user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

	*//**
	 * 入库单明细导出
	 * 
	 * @param
	 * @return int
	 * @throws IOException
	 *//*
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportRelation.asyn")
	public void exportRelation(HttpSession session, HttpServletResponse response, HttpServletRequest request,
			String ids, PurchaseWarehouseDTO dto) throws Exception {
		String sheetName = "入库明细";
		// 根据勾选的获取信息
		List<Long> list = StrUtils.parseIds(ids);
		
		User user = ShiroUtils.getUser() ;
		dto.setMerchantId(user.getMerchantId());
		
		List<PurchaseWarehouseExportDTO> result = purchaseWarehouseService.getExportDetails(list, dto);
		
		List<PurchaseWarehouseItemExportDTO> itemList = new ArrayList<PurchaseWarehouseItemExportDTO>() ;
		
		for (PurchaseWarehouseExportDTO purchaseWarehouseExportDTO : result) {
			itemList.addAll(purchaseWarehouseExportDTO.getItemList()) ;
		}
		
		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("基本信息", COLUMNS1, KEYS1, result);
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("商品信息", COLUMNS2, KEYS2, itemList);
		
		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
		sheets.add(mainSheet) ;
		sheets.add(itemSheet) ;
		
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
		
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	*//**
	 * 入库单勾稽明细导出
	 * 
	 * @param
	 * @return int
	 * @throws IOException
	 *//*
	@SuppressWarnings("unchecked")
	@RequestMapping("/purchaseExportRelation.asyn")
	public void purchaseExportRelation(HttpSession session, HttpServletResponse response, HttpServletRequest request,
			String ids) throws Exception {
		User user= ShiroUtils.getUser(); 
		String sheetName = "入库勾稽关联导出";
		// 根据勾选的获取信息
		List<Long> list = StrUtils.parseIds(ids);
		List<PurchaseWarehouseExportBean> result = purchaseWarehouseService.getPurchaseExportDetails(list,
				user.getId());
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS3, KEYS3, result);
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	*//**
	 * 入库单勾稽明细校验仓库是否为在途仓
	 * 
	 * @param
	 * @return int
	 * @throws IOException
	 *//*
	@SuppressWarnings("unchecked")
	@RequestMapping("/checkWarehouseDepotType.asyn")
	@ResponseBody
	public ViewResponseBean checkExportRelation(String ids) throws Exception {
		String result = "";
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List<Long> list = StrUtils.parseIds(ids);
			result = purchaseWarehouseService.checkWarehouseDepotType(list);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}
	*//**
	 * 删除
	 * @param ids
	 * *//*
	@SuppressWarnings("unchecked")
	@RequestMapping("/delBatchByIds.asyn")
	@ResponseBody
	public ViewResponseBean delBatchByIds(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
           List<Long> list = StrUtils.parseIds(ids);
           purchaseWarehouseService.delBatchByIds(list);
           
           return ResponseFactory.success(); 
           
        }catch(Exception e){
        	LOGGER.error(e.getMessage(), e);
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
	}
}
*/