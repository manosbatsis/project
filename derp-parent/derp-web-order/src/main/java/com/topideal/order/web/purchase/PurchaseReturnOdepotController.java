/*package com.topideal.order.web.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excelReader.ExcelReader;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotItemModel;
import com.topideal.order.service.purchase.PurchaseReturnOdepotService;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*//**
 * 采购退货订单 控制层
 * 
 * @author Gy
 *//*
@RequestMapping("/purchaseReturnOdepot")
@Controller
public class PurchaseReturnOdepotController {
	
	 打印日志 
	protected Logger LOGGER = LoggerFactory.getLogger(PurchaseReturnOdepotController.class);

	private static final String[] COLUMNS = { "出库清单编号","供应商","公司", "事业部","采购退订单号","出库时间",
				"PO单号","提单号","出库仓库","币种","单据状态","商品货号",
				"商品名称","商品条形码","数量","批次号","生产日期","失效日期"};
	
	private static final String[] KEYS = { "code", "supplierName", "merchantName", "buName", "purchaseReturnCode", "returnOutDate",
			"poNo", "blNo", "outDepotName", "currencyLabel", "statusLabel", "goodsNo",
			"goodsName", "barcode", "num", "batchNo", "productionDate", "overdueDate"};

	// 采购退货出库订单service
	@Autowired
	PurchaseReturnOdepotService purchaseReturnOdepotService;

	*//**
	 * 访问列表页面
	 * 
	 * @param model
	 *//*
	@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/purchase/purchase-return-odepot-list";
	}
	
	*//**
	 * 访问列表页面
	 * 
	 * @param model
	 *//*
	@RequestMapping("/toImportPage.html")
	public String toImportPage() throws Exception {
		return "/derp/purchase/purchase-return-odepot-import";
	}

	*//**
	 * 获取分页数据
	 * 
	 * @param model
	 *//*
	@RequestMapping("/listPurchaseReturnOdepot.asyn")
	@ResponseBody
	private ViewResponseBean listPurchaseReturnOdepot(PurchaseReturnOdepotDTO dto) {
		try {
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseReturnOdepotService.listPurchaseReturnOdepotPage(dto, user);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	*//**
	 * 访问编辑页面
	 * @param model
	 * @param ids   采购退货订单 ID
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception{
		//初始化数据
		PurchaseReturnOdepotDTO dto= purchaseReturnOdepotService.getDTOById(id) ;
		List<PurchaseReturnOdepotItemModel> itemList = purchaseReturnOdepotService.getItemListByOrderId(id) ;
		model.addAttribute("detail", dto);
		model.addAttribute("itemList", itemList);
		
		return "/derp/purchase/purchase-return-odepot-details";
	}
	
	*//**
	 * 导入
	 * @param dto
	 * @return
	 *//*
	@RequestMapping("/importOrder.asyn")
	@ResponseBody
	public ViewResponseBean importOrder(@RequestParam(value = "file", required = false) MultipartFile file) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
		
		try{
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			ExcelReader excelReader = ExcelUtil.getExcelReader(file.getOriginalFilename());
			List<Map<String, String>> data = excelReader.processSingleSheet(file.getInputStream());
			
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
			User user = ShiroUtils.getUser() ;
			
			resultMap = purchaseReturnOdepotService.importPurchaseReturnOdepot(data, user) ;
			
		}catch(SQLException e){
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			e.printStackTrace();
			LOGGER.error(e.getMessage());
            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
        }catch(Exception e){
        	e.printStackTrace();
        	LOGGER.error(e.getMessage());
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(resultMap);
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("auditPurchaseReturnOrder.asyn")
	@ResponseBody
	public ViewResponseBean auditPurchaseReturnOrder(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			User user = ShiroUtils.getUser() ;
			
			List<Long> list = StrUtils.parseIds(ids);
			boolean b = purchaseReturnOdepotService.auditPurchaseReturnOrder(list, user);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			LOGGER.error(e.getMessage());
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (RuntimeException e) {
			LOGGER.error(e.getMessage());
			return ResponseFactory.error(StateCodeEnum.ERROR_301, e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	@RequestMapping("/exportPurchaseReturnOdepot.asyn")
	public void exportPurchaseReturnOdepot(HttpServletResponse response, HttpServletRequest request, PurchaseReturnOdepotDTO dto) throws Exception {
		User user= ShiroUtils.getUser(); 
		String sheetName = "采购退货出库订单";
		dto.setMerchantId(user.getMerchantId());
		// 获取导出的信息
		List<PurchaseReturnOdepotExportDTO> result = purchaseReturnOdepotService.getDetailsByExport(dto);
		
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, result) ;
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
}
*/