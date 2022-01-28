package com.topideal.report.webapi.automatic;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.VipAutomaticVerificationDTO;
import com.topideal.entity.dto.VipDifferenceAnalysisDTO;
import com.topideal.entity.dto.VipNondifferenceCheckDTO;
import com.topideal.report.service.automatic.VipAutoVeriService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.VipAutomaticVerificationForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/webapi/report/vipAutoVeri")
@Api(tags = "唯品账单校验")
public class APIVipAutoVeriController {

	@Autowired
	private VipAutoVeriService vipAutoVeriService;

	@ApiOperation(value = "获取分页数据")
	@PostMapping(value = "/listVipAutoVerification.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<VipAutomaticVerificationDTO> listVipAutoVerification(VipAutomaticVerificationForm form) {

		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			VipAutomaticVerificationDTO dto = new VipAutomaticVerificationDTO();
			BeanUtils.copyProperties(form, dto);
			dto.setMerchantId(user.getMerchantId());

			dto = vipAutoVeriService.listVipAutoVerification(dto);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	@ApiOperation(value = "根据查询条件导出数据", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value = "/export.asyn")
	public void export(VipAutomaticVerificationForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (StringUtils.isBlank(form.getCrawlerNo())
				&& StringUtils.isBlank(form.getPoNo())) {
			return;
		}

		User user = ShiroUtils.getUserByToken(form.getToken());
		VipAutomaticVerificationDTO dto = new VipAutomaticVerificationDTO();
		BeanUtils.copyProperties(form, dto);
		dto.setMerchantId(user.getMerchantId());

		String fileName = "唯品校验导出表";

		String mainsheet = "唯品校验汇总表";
		String[] mainKeys = {"merchantName", "platform", "month", "crawlerNo", "poNo", "crawlerGoodsNo", "billSalesAccount", "systemSalesOutAccount",
				"salesOutDifference", "billHcAccount", "systemHcAccount", "hcDifference",
				"billAdjustmentIncreaseAccount", "systemAdjustmentIncreaseAccount", "adjustmentIncreaseDifferent",
				"billAdjustmentDecreaseAccount", "systemAdjustmentDecreaseAccount",
				"adjustmentDecreaseDifferent", "verificationResultLabel"};
		String[] mainColumns = {"商家", "平台", "年月份", "账单号", "PO号", "唯品SKU", "账单销售总量",
				"系统销售出库总量", "销售出库差异", "账单红冲总量", "系统红冲总量", "红冲差异",
				"账单其他总量（调增）", "系统库存调整（调增）", "调增差异",
				"账单其他总量（调减）", "系统库存调整（调减）", "调减差异", "校验结果"};
		List<VipAutomaticVerificationDTO> mainList = vipAutoVeriService.getExportList(dto);

		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainsheet, mainColumns, mainKeys, mainList);

		//差异分析报告
		VipDifferenceAnalysisDTO differenceDTO = new VipDifferenceAnalysisDTO();
		differenceDTO.setMerchantId(user.getMerchantId());
		differenceDTO.setPoNo(dto.getPoNo());
		differenceDTO.setCrawlerNo(dto.getCrawlerNo());
		differenceDTO.setMonth(dto.getMonth());

		String differencesheet = "差异分析报告";
		String[] differenceKeys = {"merchantName", "platform", "month", "crawlerNo", "poNo", "crawlerGoodsNo",
				"billTypeLabel", "differenceAccount", "differenceResult"};
		String[] differenceColumns = {"商家", "平台", "年月份", "账单号", "PO号", "唯品SKU", "账单类型", "差异数量", "差异原因"};
		List<VipDifferenceAnalysisDTO> differenceList = vipAutoVeriService.getDifferenceExportList(differenceDTO);
		ExportExcelSheet differenceSheet = ExcelUtilXlsx.createSheet(differencesheet, differenceColumns, differenceKeys, differenceList);

		//无差异勾稽明细
		VipNondifferenceCheckDTO nondifferenceCheckDTO = new VipNondifferenceCheckDTO();
		nondifferenceCheckDTO.setMerchantId(dto.getMerchantId());
		nondifferenceCheckDTO.setCrawlerNo(dto.getCrawlerNo());
		nondifferenceCheckDTO.setMonth(dto.getMonth());
		nondifferenceCheckDTO.setPoNo(dto.getPoNo());

		String noDifferenceSheet = "无差异勾稽明细";
		String[] noDifferenceKeys = {"merchantName", "platform", "month", "crawlerNo", "poNo", "crawlerGoodsNo", "crawlerTypeLabel",
				"goodsNo", "orderCode", "orderTypeLabel", "account"};
		String[] noDifferencelumns = {"商家", "平台", "年月份", "账单号", "PO号", "唯品SKU", "账单类型", "映射商品货号", "系统单据号", "单据类型", "处理数量"};
		List<VipNondifferenceCheckDTO> noDifferenceList = vipAutoVeriService.getNoDifferenceExportList(nondifferenceCheckDTO);
		ExportExcelSheet nonDifferenceSheet = ExcelUtilXlsx.createSheet(noDifferenceSheet, noDifferencelumns, noDifferenceKeys, noDifferenceList);

		List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>();
		sheetList.add(mainSheet);
		sheetList.add(differenceSheet);
		sheetList.add(nonDifferenceSheet);

		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList);
		FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
	}
}
