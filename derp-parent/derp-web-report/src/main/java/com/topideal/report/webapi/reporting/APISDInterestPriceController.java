package com.topideal.report.webapi.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.SdInterestPriceDTO;
import com.topideal.entity.dto.SdWeightedPriceDTO;
import com.topideal.report.service.reporting.SdInterestPriceService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.SdInterestPriceForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/8/19 15:52
 * @Description: 利息SD单价
 */
@RestController
@RequestMapping("/webapi/report/SDinterestPrice")
@Api(tags = "利息SD单价")
public class APISDInterestPriceController {

	@Autowired
	private SdInterestPriceService sdInterestPriceService;

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取列表分页信息")   	
   	@PostMapping(value="/listSDPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SdWeightedPriceDTO> listPrice(SdInterestPriceForm form) {
		SdInterestPriceDTO dto = new SdInterestPriceDTO();
		try{
			BeanUtils.copyProperties(form, dto);
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = sdInterestPriceService.listPriceDTO(user,dto);
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
	@GetMapping(value="/sdexportInterestPrice.asyn")
	private void exportInterestPrice(HttpServletResponse response, HttpServletRequest request,SdInterestPriceForm form) throws Exception{
		SdInterestPriceDTO dto = new SdInterestPriceDTO();
		dto.setGoodsName(form.getGoodsName());
		dto.setCommbarcode(form.getCommbarcode());
		dto.setBrandId(form.getBrandId());
		dto.setEffectiveMonth(form.getEffectiveMonth());
		dto.setBuId(form.getBuId());
		dto.setIds(form.getIds());

		User user= ShiroUtils.getUserByToken(form.getToken());	
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
	
		// 响应结果集
		List<SdInterestPriceDTO> result = sdInterestPriceService.listForExport(user,dto);
		String sheetName = "利息SD单价";
        String[] columns={"事业部", "标准条码", "商品名称", "标准品牌", "结算币种", "利息单价", "生效年月", "创建时间"};
        String[] keys = {"buName", "commbarcode", "goodsName", "brandName", "currency", "price", "effectiveMonth", "createDate"} ;
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	


}
