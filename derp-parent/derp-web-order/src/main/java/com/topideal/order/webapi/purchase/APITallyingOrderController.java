package com.topideal.order.webapi.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
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
import com.topideal.order.webapi.purchase.form.TallyingOrderForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 理货单 controller
 * 
 */
@RestController
@RequestMapping("/webapi/order/tallying")
@Api(tags = "tallying-理货单列表")
public class APITallyingOrderController {
	
	private static final Logger LOG = Logger.getLogger(APITallyingOrderController.class) ;
	
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
	 
	/**
	 * 访问详情页面
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "理货单详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "理货单id", required = true)
	})
	@PostMapping(value="/getDetailById.asyn")
	public ResponseBean<TallyingOrderDTO> getDetailById(@RequestParam(value = "token", required = true)String token, 
			@RequestParam(value = "id", required = true)Long id) throws Exception {
		
		try{
			
			TallyingOrderDTO dto = tallyingOrderService.searchDTODetail(id);
			List<TallyingItemBatchDTO> list = tallyingItemBatchService.getGoodsAndBatch(id);
			
			dto.setBatchList(list);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);

		}catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		
	}

	/**
	 * 获取分页数据
	 * @param model
	 * @param tallyingDateStr
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取分页数据")
	@PostMapping("/listTallyingOrder.asyn")
	private ResponseBean<TallyingOrderDTO> listTallyingOrder(TallyingOrderForm form) {
		try {
			
			if(StringUtils.isBlank(form.getToken())) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			TallyingOrderDTO dto = new TallyingOrderDTO() ;
			
			BeanUtils.copyProperties(form, dto);
			
			User user = ShiroUtils.getUserByToken(form.getToken()) ;
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = tallyingOrderService.listPageByPurchase(dto,user);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		
	}

	/**
	 * 修改理货单状态（确认/驳回）
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/modifyOrderState.asyn")
	@ApiOperation(value = "修改理货单状态（确认/驳回）", notes="修改理货单状态，若修改成功，返回空字符。修改失败从data取失败原因")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "理货单ID，多个以','隔开", required = true),
			@ApiImplicitParam(name = "state", value = "状态（确认/驳回）", required = true)
	})
	private ResponseBean<String> modifyTallyingOrder(@RequestParam(value = "token", required = true)String token, 
			String ids, String state) {
		
		String result = "";
		
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			
			if (!isRight) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
			}
			
			User user= ShiroUtils.getUserByToken(token) ;
			// 响应结果集
			result = tallyingOrderService.modifyOrderState(ids, state, user.getId(), user.getName(),user.getTopidealCode());
		
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		
	}

	@GetMapping("/exportTallyingOrder.asyn")
	@ApiOperation(value = "导出理货单")
	public void exportTallyingOrder(HttpServletResponse response, HttpServletRequest request,
			TallyingOrderForm form) throws Exception {
		
		if(StringUtils.isBlank(form.getToken())) {
			return ;
		}
		
		TallyingOrderDTO dto = new TallyingOrderDTO() ;
		dto.setBuId(form.getBuId());
		dto.setCode(form.getCode());
		dto.setOrderCode(form.getOrderCode());
		dto.setState(form.getState());
		dto.setDepotId(form.getDepotId());
		dto.setContractNo(form.getContractNo());
		dto.setTallyingStartDate(form.getTallyingStartDate());
		dto.setTallyingEndDate(form.getTallyingEndDate());
		
		User user = ShiroUtils.getUserByToken(form.getToken()) ;
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
		
		String sheetName = "采购理货单";
		
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
