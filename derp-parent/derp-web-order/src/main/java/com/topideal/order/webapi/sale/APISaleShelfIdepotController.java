package com.topideal.order.webapi.sale;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.sale.SaleShelfIdepotDTO;
import com.topideal.entity.dto.sale.SaleShelfIdepotItemDTO;
import com.topideal.order.service.sale.SaleShelfIdepotService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.SaleShelfIdepotResponseDTO;
import com.topideal.order.webapi.sale.form.SaleShelfIdepotForm;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 销售上架入库 controller
 *
 */
@RequestMapping("/webapi/order/saleShelfIdepot")
@RestController
@Api(tags = "销售上架入库")
public class APISaleShelfIdepotController {

	private static final String[] EXPORT_COLUMNS = { "上架入库单号", "销售订单号", "入库仓库","事业部", "出仓仓库", "销售出库单",
			"PO号", "单据状态", "入库时间", "商品货号", "商品条码", "商品名称", "好品数量",
			"坏品数量", "入库批次号", "生产日期", "失效日期"};
	private static final String[] EXPORT_KEYS = {"code" , "sale_order_code","in_depot_name","bu_name","out_depot_name","sale_out_code",
			"po_no", "state", "shelf_date", "in_goods_no", "barcode", "in_goods_name", "normal_num",
			"damaged_num", "batch_no", "production_date", "overdue_date"} ;

	@Autowired
	private SaleShelfIdepotService saleShelfIdepotService ;


	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "查询详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleShelfIdepotResponseDTO> toDetailsPage(Model model, Long id, String token)throws Exception{
		SaleShelfIdepotResponseDTO  responseDTO = new SaleShelfIdepotResponseDTO();
		try {
			SaleShelfIdepotDTO dto = saleShelfIdepotService.searchDetail(id);
			List<SaleShelfIdepotItemDTO> itemList = saleShelfIdepotService.listItemBySaleShelfIdepotId(id);
			responseDTO.setSaleShelfIdepotDTO(dto);
			responseDTO.setItemList(itemList);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}

	/**
	 * 获取上架入库单分页数据
	 * */
	@ApiOperation(value = "查询销售上架入库列表信息")
   	@PostMapping(value="/listSaleShelfIdepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleShelfIdepotDTO> listSaleShelfIdepot(SaleShelfIdepotForm  form) {
		SaleShelfIdepotDTO dto = new SaleShelfIdepotDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setSaleOrderCode(form.getSaleOrderCode());
			dto.setSaleOutCode(form.getSaleOutCode());
			dto.setPoNo(form.getPoNo());
			dto.setInDepotId(StringUtils.isNotBlank(form.getInDepotId()) ? Long.valueOf(form.getInDepotId()) : null);
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setState(form.getState());
			dto.setShelfStartDate(form.getShelfStartDate());
			dto.setShelfEndDate(form.getShelfEndDate());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			dto.setCode1(form.getCode1());
			dto.setIsWriteOff(form.getIsWriteOff());

			// 响应结果集
			dto = saleShelfIdepotService.listSaleShelfIdepot(dto,user);

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 获取导出销售出库清单的数量
	 */
	@ApiOperation(value = "获取导出订单的数量")
	@ApiResponses({
			@ApiResponse(code = 10000,message="data = > 导出的销售上架入库单的数量")
	})
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(SaleShelfIdepotForm form) throws Exception{
		Integer total=0;
		try{
			SaleShelfIdepotDTO dto = new SaleShelfIdepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setSaleOrderCode(form.getSaleOrderCode());
			dto.setSaleOutCode(form.getSaleOutCode());
			dto.setPoNo(form.getPoNo());
			dto.setInDepotId(StringUtils.isNotBlank(form.getInDepotId()) ? Long.valueOf(form.getInDepotId()) : null);
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setState(form.getState());
			dto.setShelfStartDate(form.getShelfStartDate());
			dto.setShelfEndDate(form.getShelfEndDate());
			dto.setIsWriteOff(form.getIsWriteOff());
			// 响应结果集
			total = saleShelfIdepotService.getOrderCount(dto,user);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}

	/**
	 * 导出销售出库单
	 * */
	@ApiOperation(value = "导出上架入库",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportSaleShelfIdepot.asyn")
	private ResponseBean exportSaleShelfIdepot(HttpServletResponse response, HttpServletRequest request,SaleShelfIdepotForm form) throws Exception{

		try {
			SaleShelfIdepotDTO dto = new SaleShelfIdepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setSaleOrderCode(form.getSaleOrderCode());
			dto.setSaleOutCode(form.getSaleOutCode());
			dto.setPoNo(form.getPoNo());
			dto.setInDepotId(StringUtils.isNotBlank(form.getInDepotId()) ? Long.valueOf(form.getInDepotId()) : null);
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setState(form.getState());
			dto.setShelfStartDate(form.getShelfStartDate());
			dto.setShelfEndDate(form.getShelfEndDate());
			dto.setIsWriteOff(form.getIsWriteOff());

			// 响应结果集
			List<Map<String,Object>> result = saleShelfIdepotService.getExportList(dto,user);

			String sheetName = "上架入库单";
	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, EXPORT_COLUMNS, EXPORT_KEYS, result);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

}
