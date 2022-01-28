package com.topideal.report.webapi.reporting;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
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
import com.topideal.entity.dto.WeightedPriceDTO;
import com.topideal.report.service.reporting.BrandService;
import com.topideal.report.service.reporting.WeightedPriceService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.WeightedPriceForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 加权单价
 */
@RestController
@RequestMapping("/webapi/report/weightedPrice")
@Api(tags = "加权单价")
public class APIWeightedPriceController {

	@Autowired
	private WeightedPriceService weightedPriceService;
	@Autowired
	private BrandService brandService;	//品牌

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取列表分页信息")   	
   	@PostMapping(value="/listPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listPrice(WeightedPriceForm form) {
		WeightedPriceDTO dto = new WeightedPriceDTO();
		try{
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = weightedPriceService.listPriceDTO(user,dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 导出
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportWeightedPrice.asyn")
	private void exportWeightedPrice(HttpServletResponse response, HttpServletRequest request,WeightedPriceForm form) throws Exception{
		WeightedPriceDTO dto = new WeightedPriceDTO();
		BeanUtils.copyProperties(form, dto);
		User user = ShiroUtils.getUserByToken(form.getToken());
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
	
		// 响应结果集
		List<WeightedPriceDTO> result = weightedPriceService.listForExport(user,dto);
		String sheetName = "加权单价导出";
        String[] columns={"事业部", "标准条码", "商品名称", "品牌", "结算币种", "加权单价", "生效年月", "更新时间"};
        String[] keys = {"buName", "commbarcode", "goodsName", "brandName", "currencyLabel", "price", "effectiveMonth", "createDate"} ;
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	


}
