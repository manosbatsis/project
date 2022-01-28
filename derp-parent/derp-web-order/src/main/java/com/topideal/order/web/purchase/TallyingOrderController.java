/*package com.topideal.order.web.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.purchase.TallyingItemBatchDTO;
import com.topideal.entity.dto.purchase.TallyingOrderDTO;
import com.topideal.entity.dto.purchase.TallyingOrderItemDTO;
import com.topideal.entity.vo.purchase.TallyingOrderItemModel;
import com.topideal.order.service.purchase.TallyingItemBatchService;
import com.topideal.order.service.purchase.TallyingOrderService;
import com.topideal.order.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

*//**
 * 理货单 controller
 * 
 *//*
@RequestMapping("/tallying")
@Controller
public class TallyingOrderController {
	
	private static final String[] MAIN_COLUMNS = {"理货单号", "预申报单号", "仓库", "事业部", "合同号", "理货时间", "确认时间", "状态"} ;
	private static final String[] MAIN_KEYS = {"code", "orderCode", "depotName", "buName","contractNo", "tallyingDate", "confirmDate", "state"};
	
	private static final String[] ITEM_COLIMNS = {"理货单号", "商品货号", "商品名称", "商品条形码", "海外仓理货单位", "采购数量", "理货数量", "缺少数量", "多货数量", "正常数量", "仓库名称"} ;
	private static final String[] ITEM_KEYS = {"code", "goodsNo", "goodsName", "barcode", "tallyingUnit", "purchaseNum", "tallyingNum", "lackNum", "multiNum", "normalNum", "depotName"};
	
	// 理货单service
	@Autowired
	private TallyingOrderService tallyingOrderService;
	// 理货单批次service
	 @Autowired
	 private TallyingItemBatchService tallyingItemBatchService;


	*//**
	 * 访问列表页面
	 *//*
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		return "/derp/purchase/tallying-list";
	}

	*//**
	 * 访问详情页面
	 *//*
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id, String pageSource) throws Exception {
		TallyingOrderDTO dto = tallyingOrderService.searchDTODetail(id);
		model.addAttribute("detail", dto);
		List<TallyingItemBatchDTO> list = tallyingItemBatchService.getGoodsAndBatch(id);
		model.addAttribute("batchBean", list);
		if (StringUtils.isNotBlank(pageSource)) {
			model.addAttribute("pageSource", pageSource);
		}
		return "/derp/purchase/tallying-details";
	}

	*//**
	 * 获取分页数据
	 * @param model
	 * @param tallyingDateStr
	 * @param session
	 * @return
	 *//*
	@RequestMapping("/listTallyingOrder.asyn")
	@ResponseBody
	private ViewResponseBean listTallyingOrder(TallyingOrderDTO dto) {
		try {
			User user= ShiroUtils.getUser(); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = tallyingOrderService.listPageByPurchase(dto, user);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}

	*//**
	 * 修改理货单状态（确认/驳回）
	 *//*
	@RequestMapping("/modifyOrderState.asyn")
	@ResponseBody
	private ViewResponseBean modifyTallyingOrder(HttpSession session, String ids, String state) {
		String result = "";
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser(); 
			// 响应结果集
			result = tallyingOrderService.modifyOrderState(ids, state, user.getId(), user.getName(),user.getTopidealCode());
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	@RequestMapping("/exportTallyingOrder.asyn")
	public void exportTallyingOrder(HttpServletResponse response, HttpServletRequest request,
			TallyingOrderDTO dto) throws Exception {
		
		User user= ShiroUtils.getUser(); 
		String sheetName = "采购理货单";
		dto.setMerchantId(user.getMerchantId());
		
		// 获取导出的主表信息
		List<TallyingOrderDTO> mainList = tallyingOrderService.getDetailsByExport(dto, user);
		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName, MAIN_COLUMNS, MAIN_KEYS, mainList);
		
		// 获取导出的明细信息
		List<TallyingOrderItemDTO> itemList = new ArrayList<TallyingOrderItemDTO>() ;
		for (TallyingOrderDTO tallyingOrderDTO: mainList) {
			for (TallyingOrderItemModel tallyingOrderItem : tallyingOrderDTO.getItemList()) {
				TallyingOrderItemDTO itemDTO = new TallyingOrderItemDTO(tallyingOrderItem) ;
				itemDTO.setCode(tallyingOrderDTO.getCode());
				itemDTO.setDepotName(tallyingOrderDTO.getDepotName());
				
				itemList.add(itemDTO) ;
			}
		}
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("理货商品详情", ITEM_COLIMNS, ITEM_KEYS, itemList) ;
		
		List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
		sheetList.add(mainSheet);
		sheetList.add(itemSheet);
		
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList);
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
}
*/