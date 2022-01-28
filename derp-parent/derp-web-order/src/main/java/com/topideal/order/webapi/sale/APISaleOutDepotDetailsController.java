package com.topideal.order.webapi.sale;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.sale.SaleOutDepotDTO;
import com.topideal.order.service.sale.SaleOutDepotService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.SaleOutDepotForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * webapi 销售出库明细
 * 
 */
@RequestMapping("/webapi/order/saleOutDetails")
@RestController
@Api(tags = "销售出库明细")
public class APISaleOutDepotDetailsController {

	// 销售出库service
	@Autowired
	private SaleOutDepotService saleOutDepotService;

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "销售出库明细列表信息")   	
   	@PostMapping(value="/listSaleOutDepotDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleOutDepotDTO> listSaleOutDepotDetails(SaleOutDepotForm form, HttpSession session) {
		SaleOutDepotDTO dto = new SaleOutDepotDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken()); 	
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setSaleOrderCode(form.getSaleOrderCode());
			dto.setCode(form.getCode());
			dto.setVipsBillNo(form.getVipsBillNo());
			dto.setLbxNo(form.getLbxNo());
			dto.setSaleType(form.getSaleType());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setGoodsStr(form.getGoodsStr());	
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			
			// 响应结果集
			dto = saleOutDepotService.listSaleOutDepotDetailsByPage(dto,user);
			
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	/**
	 * 获取导出销售出库明细的数量
	 */
	@ApiOperation(value = "获取导出销售出库明细的数量")
	@ApiResponses({
		@ApiResponse(code=10000 , message="data => 返回要导出的销售出库明细的数量")
	})
   	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOutDepotForm form) throws Exception{
		Integer total = 0;
		try{
			SaleOutDepotDTO dto = new SaleOutDepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken()); 	
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setSaleOrderCode(form.getSaleOrderCode());
			dto.setCode(form.getCode());
			dto.setVipsBillNo(form.getVipsBillNo());
			dto.setLbxNo(form.getLbxNo());
			dto.setSaleType(form.getSaleType());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setGoodsStr(form.getGoodsStr());
			dto.setIds(form.getIds());
			// 响应结果集
			List<SaleOutDepotDTO> result = saleOutDepotService.listSaleOutDepotDetails(dto,user);
			total = result.size();
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}
	/**
	 * 导出销售出库明细
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportSaleOutDepotDetails.asyn")
	private ResponseBean exportSaleOutDepotDetails(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOutDepotForm form) throws Exception{
		try {
			SaleOutDepotDTO dto = new SaleOutDepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken()); 	
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setSaleOrderCode(form.getSaleOrderCode());
			dto.setCode(form.getCode());
			dto.setVipsBillNo(form.getVipsBillNo());
			dto.setLbxNo(form.getLbxNo());
			dto.setSaleType(form.getSaleType());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setGoodsStr(form.getGoodsStr());
			dto.setIds(form.getIds());
			// 响应结果集
			List<SaleOutDepotDTO> result = saleOutDepotService.listSaleOutDepotDetails(dto,user);
			String sheetName = "销售出库明细";
	        String[] columns={"销售出库单号","销售订单号","出库仓名称","事业部","唯品账单号","LBX单号","PO号","客户名称","销售类型","商品货号","商品名称","出库数量","销售数量","海外仓理货单位"};
	        String[] keys = {"code", "saleOrderCode", "outDepotName", "buName", "vipsBillNo", "lbxNo", "poNo", "customerName", "saleTypeLabel", "goodsNo", "goodsName", "transferNum", "saleNum", "tallyingUnitLabel"} ;
	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
}
