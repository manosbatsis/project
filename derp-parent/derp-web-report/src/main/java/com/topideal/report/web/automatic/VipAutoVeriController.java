package com.topideal.report.web.automatic;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.VipAutomaticVerificationDTO;
import com.topideal.entity.dto.VipDifferenceAnalysisDTO;
import com.topideal.entity.dto.VipNondifferenceCheckDTO;
import com.topideal.report.service.automatic.VipAutoVeriService;
import com.topideal.report.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/vipAutoVeri")
public class VipAutoVeriController {
	
	@Autowired
	private VipAutoVeriService vipAutoVeriService ;

	@RequestMapping("toPage.html")
	public String toPage(Model model) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
		String month = sdf.format(new Date());
		
		model.addAttribute("month", month) ;
		
		return "derp/automatic/vipAutoVerification-list" ;
	}
	
	/**
	 *  获取列表信息
	 * @param dto
	 * @return
	 */
	@RequestMapping("listVipAutoVerification.asyn")
	@ResponseBody
	public ViewResponseBean listVipAutoVerification(VipAutomaticVerificationDTO dto) {
		
		try {
			User user = ShiroUtils.getUser();
			
			dto.setMerchantId(user.getMerchantId());
			dto = vipAutoVeriService.listVipAutoVerification(dto) ;
			
			return ResponseFactory.success(dto) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
	}
	
	@RequestMapping("export.asyn")
	public void export(VipAutomaticVerificationDTO dto, HttpServletRequest request, HttpServletResponse response) {
		
		if(StringUtils.isBlank(dto.getCrawlerNo())
				&& StringUtils.isBlank(dto.getPoNo())) {
			return ;
		}
		
		User user = ShiroUtils.getUser();
		
		dto.setMerchantId(user.getMerchantId());
		
		String fileName = "唯品校验导出表";
		
		String mainsheet = "唯品校验汇总表" ;
		String[] mainKeys = { "merchantName", "platform", "month", "crawlerNo", "poNo", "crawlerGoodsNo", "billSalesAccount", "systemSalesOutAccount",
				"salesOutDifference", "billHcAccount", "systemHcAccount", "hcDifference",
				"billAdjustmentIncreaseAccount", "systemAdjustmentIncreaseAccount", "adjustmentIncreaseDifferent", 
				"billAdjustmentDecreaseAccount", "systemAdjustmentDecreaseAccount",
				"adjustmentDecreaseDifferent", "verificationResultLabel"} ;
		String[] mainColumns = {"商家", "平台", "年月份", "账单号", "PO号", "唯品SKU", "账单销售总量",
				"系统销售出库总量", "销售出库差异", "账单红冲总量", "系统红冲总量", "红冲差异",
				"账单其他总量（调增）", "系统库存调整（调增）", "调增差异", 
				"账单其他总量（调减）", "系统库存调整（调减）", "调减差异", "校验结果"} ;
		List<VipAutomaticVerificationDTO> mainList = vipAutoVeriService.getExportList(dto) ;
	
		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainsheet, mainColumns, mainKeys, mainList);
	
		//差异分析报告
		VipDifferenceAnalysisDTO differenceDTO = new VipDifferenceAnalysisDTO() ;
		differenceDTO.setMerchantId(user.getMerchantId());
		differenceDTO.setPoNo(dto.getPoNo());
		differenceDTO.setCrawlerNo(dto.getCrawlerNo());
		differenceDTO.setMonth(dto.getMonth());
		
		String differencesheet = "差异分析报告" ;
		String[] differenceKeys = {"merchantName", "platform", "month", "crawlerNo", "poNo", "crawlerGoodsNo",
				"billTypeLabel", "differenceAccount", "differenceResult"} ;
		String[] differenceColumns = {"商家", "平台", "年月份", "账单号", "PO号", "唯品SKU", "账单类型", "差异数量", "差异原因"} ;
		List<VipDifferenceAnalysisDTO> differenceList = vipAutoVeriService.getDifferenceExportList(differenceDTO) ;
		ExportExcelSheet differenceSheet = ExcelUtilXlsx.createSheet(differencesheet, differenceColumns, differenceKeys, differenceList);
	
		//无差异勾稽明细
		VipNondifferenceCheckDTO nondifferenceCheckDTO = new VipNondifferenceCheckDTO() ;
		nondifferenceCheckDTO.setMerchantId(dto.getMerchantId());
		nondifferenceCheckDTO.setCrawlerNo(dto.getCrawlerNo());
		nondifferenceCheckDTO.setMonth(dto.getMonth());
		nondifferenceCheckDTO.setPoNo(dto.getPoNo());
		
		String noDifferenceSheet = "无差异勾稽明细" ;
		String[] noDifferenceKeys = {"merchantName", "platform", "month", "crawlerNo", "poNo", "crawlerGoodsNo", "crawlerTypeLabel",
				"goodsNo", "orderCode", "orderTypeLabel", "account"} ;
		String[] noDifferencelumns = {"商家", "平台", "年月份", "账单号", "PO号", "唯品SKU", "账单类型", "映射商品货号", "系统单据号", "单据类型", "处理数量"} ;
		List<VipNondifferenceCheckDTO> noDifferenceList = vipAutoVeriService.getNoDifferenceExportList(nondifferenceCheckDTO) ;
		ExportExcelSheet nonDifferenceSheet = ExcelUtilXlsx.createSheet(noDifferenceSheet, noDifferencelumns, noDifferenceKeys, noDifferenceList);
		
		List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
		sheetList.add(mainSheet) ;
		sheetList.add(differenceSheet) ;
		sheetList.add(nonDifferenceSheet) ;
		
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList);
		FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
	}
}
