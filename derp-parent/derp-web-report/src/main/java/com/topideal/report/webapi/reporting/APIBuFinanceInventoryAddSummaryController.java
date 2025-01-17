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
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.report.service.reporting.BuFinanceInventorySummaryService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.BuFinanceInventoryAddResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 累计汇总表
 */
@RestController
@RequestMapping("/webapi/report/buFinanceAdd")
@Api(tags = "累计汇总表")
public class APIBuFinanceInventoryAddSummaryController {
	
	@Autowired
	public BuFinanceInventorySummaryService buFinanceInventorySummaryService;



	/**
	 * 访问列表页面
	 * */
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
	@ResponseBody
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "groupCommbarcode", value = "标准条码(组码)"),
			@ApiImplicitParam(name = "monthStart", value = "统计月份开始"),
			@ApiImplicitParam(name = "monthEnd", value = "统计月份结束"),
			@ApiImplicitParam(name = "brandIds", value = "品牌id 多个用英文逗号隔开"),
			@ApiImplicitParam(name = "goodsName", value = "商品名称"),
			@ApiImplicitParam(name = "typeId", value = "二级分类id")	
	})
	@PostMapping(value="/buFinanceAddSummaryList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<BuFinanceInventorySummaryDTO> financeSummaryList(String token,Integer begin,Integer pageSize,Long buId,String groupCommbarcode,
			String monthStart,String monthEnd,String brandIds,String goodsName,Long  typeId) {
		try{
			// 响应结果集
			User user=ShiroUtils.getUserByToken(token);
			BuFinanceInventorySummaryDTO model =new BuFinanceInventorySummaryDTO();
			model.setBegin(begin);
			model.setPageSize(pageSize);
			model.setBuId(buId);
			model.setGroupCommbarcode(groupCommbarcode);
			model.setMonthStart(monthStart);
			model.setMonthEnd(monthEnd);
			model.setGoodsName(goodsName);;
			model.setTypeId(typeId);
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
			model = buFinanceInventorySummaryService.getListAddByPage(user,model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	

	
	/**
	 * 累计总表导出
	 * */
	@ApiOperation(value = "累计总表导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
   		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "receiveCode", value = "应收账单号"),
		@ApiImplicitParam(name = "customerId", value = "客户id"),
		@ApiImplicitParam(name = "billMonth", value = "账单月份"),
		@ApiImplicitParam(name = "overdueStatus", value = "账期逾期")
	})
	@GetMapping(value="/export.asyn")
	private void createTask(HttpServletResponse response,HttpServletRequest request,
			String token,Long buId,String groupCommbarcode,String monthStart,String monthEnd,String goodsName,Long  typeId,
			String brandIds){
		try {
			BuFinanceInventorySummaryDTO model=new BuFinanceInventorySummaryDTO();
			User user=ShiroUtils.getUserByToken(token);
			model.setBuId(buId);
			model.setGroupCommbarcode(groupCommbarcode);
			model.setMonthStart(monthStart);
			model.setMonthEnd(monthEnd);
			model.setGoodsName(goodsName);;
			model.setTypeId(typeId);
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
			List<BuFinanceInventorySummaryDTO> result = buFinanceInventorySummaryService.getListAddExport(user,model);
			String sheetName = "累计汇总表";
			String[] columns = { "事业部","母品牌","标准品牌","二级分类","标准条码","商品名称","统计起始月份","统计结束月份","累计采购结转数量","累计采购结转金额",
					"累计销售结转数量","累计销售结转金额","累计损益结转数量","累计损益结转金额","累计采购入库数量","累计来货残损数量","累计事业部移库入数量","累计销售已上架数量","累计出库残损数量",
					"累计事业部移库出数量","累计销毁数量","累计盘盈数量","累计盘亏数量"};
			String[] keys = {"buName","superiorParentBrand","brandName","typeName","groupCommbarcode","goodsName","monthStart","monthEnd","purchaseEndNum","purchaseEndAmount",
					 "saleEndNum","saleEndAmount","lossOverflowNum","lossOverflowAmount","warehouseNum","inDamagedNum","moveInNum","saleShelfNum",
					 "outDamagedNum","moveOutNum","destroyNum","profitNum","lossNum"} ;
			
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result) ;
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
