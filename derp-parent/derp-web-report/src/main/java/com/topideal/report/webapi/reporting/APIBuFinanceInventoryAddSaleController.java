package com.topideal.report.webapi.reporting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.SaleDataDTO;
import com.topideal.report.service.reporting.SaleDataService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.BuFinanceInventoryAddResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 累计汇采购总表
 */
@RestController
@RequestMapping("/webapi/report/buFinanceAddSale")
@Api(tags = "累计汇销售总表")
public class APIBuFinanceInventoryAddSaleController {
	
	@Autowired
	public SaleDataService saleDataService;


	/**
	 * 访问列表页面
	 * */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "month", value = "月份")
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BuFinanceInventoryAddResponseDTO> toPage(String month)throws Exception  {
		BuFinanceInventoryAddResponseDTO responseDTO = new BuFinanceInventoryAddResponseDTO();
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
			if (StringUtils.isEmpty(month)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");	
				responseDTO.setNowStart(sdf1.format(new Date())+"-01");
				responseDTO.setNowEnd(sdf.format(new Date()));
			} else {
				responseDTO.setNowStart(sdf1.format(month+"-01")+"-01");
				responseDTO.setNowEnd(month);				
			}	
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 获取分页数据
	 * */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "commbarcode", value = "标准条码"),
			@ApiImplicitParam(name = "monthStart", value = "统计月份开始", required = true),
			@ApiImplicitParam(name = "monthEnd", value = "统计月份结束",required = true),
			@ApiImplicitParam(name = "brandIds", value = "品牌id 多个用英文逗号隔开"),
			@ApiImplicitParam(name = "goodsName", value = "商品名称"),
			@ApiImplicitParam(name = "customerId", value = "客户id")	
	})
	@PostMapping(value="/buFinanceAddSaleList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleDataDTO> buFinanceAddSaleList(String token,Integer begin,Integer pageSize,Long buId,String commbarcode,
			String monthStart,String monthEnd,String brandIds,String goodsName,Long  customerId) {
		try{
			// 响应结果集
			User user=ShiroUtils.getUserByToken(token);
			SaleDataDTO model =new SaleDataDTO();
			model.setBegin(begin);
			model.setPageSize(pageSize);
			model.setBuId(buId);
			model.setCommbarcode(commbarcode);
			model.setMonthStart(monthStart);
			model.setMonthEnd(monthEnd);
			model.setGoodsName(goodsName);;
			model.setCustomerId(customerId);
			model.setMerchantId(user.getMerchantId());
			List<Long> parentBrandIds=new ArrayList<Long>();
			if(!StringUtils.isEmpty(brandIds)){
				parentBrandIds=new ArrayList<Long>();
				String[] Ids = brandIds.split(",");
				for(String id:Ids){
					if(!StringUtils.isEmpty(id)) parentBrandIds.add(Long.valueOf(id));
				}
				model.setParentBrandIds(parentBrandIds);
			}
			model = saleDataService.getListAddSaleByPage(user,model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	

	
	/**
	 * 累计采购汇总表导出
	 * */
	@ApiOperation(value = "累计采购汇总表导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
   		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部id"),
		@ApiImplicitParam(name = "commbarcode", value = "标准条码"),
		@ApiImplicitParam(name = "monthStart", value = "统计月份开始", required = true),
		@ApiImplicitParam(name = "monthEnd", value = "统计月份结束",required = true),
		@ApiImplicitParam(name = "brandIds", value = "品牌id 多个用英文逗号隔开"),
		@ApiImplicitParam(name = "goodsName", value = "商品名称"),
		@ApiImplicitParam(name = "customerId", value = "客户id")	
	})
	@GetMapping(value="/export.asyn")
	private void createTask(HttpServletResponse response,HttpServletRequest request,
			String token,Long buId,String commbarcode,String monthStart,String monthEnd,String goodsName,Long  customerId,
			String brandIds){
		try {
			User user=ShiroUtils.getUserByToken(token);
			SaleDataDTO model =new SaleDataDTO();
			model.setBuId(buId);
			model.setCommbarcode(commbarcode);
			model.setMonthStart(monthStart);
			model.setMonthEnd(monthEnd);
			model.setGoodsName(goodsName);;
			model.setCustomerId(customerId);
			model.setMerchantId(user.getMerchantId());
			List<Long> parentBrandIds=new ArrayList<Long>();
			if(!StringUtils.isEmpty(brandIds)){
				parentBrandIds=new ArrayList<Long>();
				String[] Ids = brandIds.split(",");
				for(String id:Ids){
					if(!StringUtils.isEmpty(id)) parentBrandIds.add(Long.valueOf(id));
				}
				model.setParentBrandIds(parentBrandIds);
			}
			// 响应结果集
			List<SaleDataDTO> result = saleDataService.getListAddExport(user,model);
			String sheetName = "累计销售汇总表";
			String[] columns = {"公司","事业部","客户名称","母品牌","标准品牌","标准条码","商品名称","统计起始月份","统计结束月份","累计销售数量","累计销售成本金额","销售币种","累计销售金额（原币）"};
			String[] keys = {"merchantName","buName","customerName","superiorParentBrand","brandParent","commbarcode","goodsName","monthStart","monthEnd","num","addWeightedAmount","saleCurrencyLabel","saleAmount"} ;
			
			
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result) ;
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
