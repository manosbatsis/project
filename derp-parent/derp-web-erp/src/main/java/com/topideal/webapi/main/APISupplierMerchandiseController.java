package com.topideal.webapi.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.main.SupplierMerchandiseDTO;
import com.topideal.service.main.SupplierMerchandiseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/webapi/system/supplierMerchandise")
@Api(tags = "供应商商品列表")
public class APISupplierMerchandiseController {

	@Autowired
	private SupplierMerchandiseService supplierMerchandiseService;



	/*@RequestMapping("/toPage.html")
	public String toPage(Model model) throws SQLException {
		return "/derp/main/supplier-merchandise-list";
	}*/


	/**
	 * 列表所需数据
	 * 
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "goodsCode", value = "商品编码"),
			@ApiImplicitParam(name = "brandCode", value = "品牌编码"),
			@ApiImplicitParam(name = "barcode", value = "条形码"),
			@ApiImplicitParam(name = "buId", value = "事业部")
	})
	@PostMapping(value="/listSupplierMerchandise.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listSupplierMerchandise(String goodsCode,String brandCode,
			String barcode,Long buId,int begin,int pageSize) {
		try {
			SupplierMerchandiseDTO dto=new SupplierMerchandiseDTO();
			dto.setGoodsCode(goodsCode);
			dto.setBrandCode(brandCode);
			dto.setBarcode(barcode);
			dto.setBuId(buId);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto = supplierMerchandiseService.listSupplierMerchandise(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常

		}
	}

	/**
	 * 导出商品信息
	 * @param response
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/export.asyn")
	public void export(HttpServletResponse response, HttpServletRequest request, 
			String goodsCode,String brandCode,String barcode,Long buId) throws Exception{
		try {
			SupplierMerchandiseDTO dto=new SupplierMerchandiseDTO();
			dto.setGoodsCode(goodsCode);
			dto.setBrandCode(brandCode);
			dto.setBarcode(barcode);
			dto.setBuId(buId);

			String sheetName = "商品信息导出";
			List<SupplierMerchandiseDTO> list = supplierMerchandiseService.exportList(dto);
			/** 标题  */
			String[] COLUMNS= {"商品编码","商品名称","品牌名称","品牌编码","条形码","产品类型","建议零售价","经销商货号","事业部","数据来源"};
			String[] KEYS= {"goodsCode","name","brandName","brandCode","barcode","goodsType","salePrice","supplierGoodsNo","buName","sourceLabel"};
			
			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , list);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		} catch (Exception e) {
			e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
}
