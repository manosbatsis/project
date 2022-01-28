package com.topideal.order.webapi.sale;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.sale.MerchandiseContrastDTO;
import com.topideal.entity.dto.sale.MerchandiseContrastItemDTO;
import com.topideal.entity.vo.sale.MerchandiseContrastItemModel;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.order.service.sale.MerchandiseContrastItemService;
import com.topideal.order.service.sale.MerchandiseContrastService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.MerchandiseContrastResponseDTO;
import com.topideal.order.webapi.sale.form.MerchandiseContrastForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * webapi 爬虫商品对照表
 */
@RestController
@RequestMapping("/webapi/order/contrast")
@Api(tags = "爬虫商品对照表")
public class APIMerchandiseContrastController {

	@Autowired
	private MerchandiseContrastService merchandiseContrastService;
	@Autowired
    private MerchandiseContrastItemService merchandiseContrastItemService;
	
	/**
	 * 获取分页数据
	 * @param dto
	 * @return
	 */
	@ApiOperation(value = "爬虫商品对照表信息")   	
   	@PostMapping(value="/contrastList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<MerchandiseContrastDTO> contrastList(MerchandiseContrastForm form) {
		MerchandiseContrastDTO dto = new MerchandiseContrastDTO();
		try{
			// 响应结果集
			dto.setPlatformName(form.getPlatformName());
			dto.setCrawlerGoodsNo(form.getCrawlerGoodsNo());
			dto.setGoodsNo(form.getGoodsNo());
			dto.setStatus(form.getStatus());
			dto.setMerchantId(StringUtils.isNotBlank(form.getMerchantId()) ? Long.valueOf(form.getMerchantId()) : null);
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			
			dto = merchandiseContrastService.getListByPage(dto);
			
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return  WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	/**
	 * 访问新增页面
	 * @param model
	 * */
	@ApiOperation(value = "编辑页面回显")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/toEditPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseContrastResponseDTO> toEditPage(String token,Long id) throws Exception{
		MerchandiseContrastResponseDTO responseDTO = new MerchandiseContrastResponseDTO();
		try {
			MerchandiseContrastDTO dto = merchandiseContrastService.searchDetail(id);
			MerchandiseContrastItemModel contrastItemModel = new MerchandiseContrastItemModel();
			contrastItemModel.setContrastId(dto.getId());
			List<MerchandiseContrastItemModel> itemModel = merchandiseContrastItemService.searchDetail(contrastItemModel);
			
			responseDTO.setMerchandiseContrastDTO(dto);
			responseDTO.setItemModel(itemModel);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return  WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}
	/**
	 * 新增/修改
	 * */
	@ApiOperation(value = "新增/修改")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "爬虫商品对照表表头表体信息json串", required = true)
	})
	@PostMapping(value="/saveContrast.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> saveContrast(String token,String json,HttpSession session) {
		String message = "";
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			Map<String, Object> retMap = new HashMap<String, Object>();
			User user= ShiroUtils.getUserByToken(token);
			retMap = merchandiseContrastService.saveContrast(json,user);
			message = (String)retMap.get("message");
			if("01".equals((String)retMap.get("code"))) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);
	}
	
	
	
	/**
	 * 根据ID获取商品详情
	 * @throws SQLException 
	 */
	@ApiOperation(value = "根据ID获取商品详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "爬虫商品对照表表id", required = true)
	})
	@PostMapping(value="/detail.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseContrastResponseDTO> detail(String token,Long id) throws SQLException {		
		MerchandiseContrastResponseDTO responseDTO = new MerchandiseContrastResponseDTO();
		try {
			MerchandiseContrastDTO dto = merchandiseContrastService.searchDetail(id);
			MerchandiseContrastItemModel contrastItemModel = new MerchandiseContrastItemModel();
			contrastItemModel.setContrastId(id);
			List<MerchandiseContrastItemDTO> itemModels = merchandiseContrastItemService.getContrastItemByContrastId(contrastItemModel);
			
			responseDTO.setMerchandiseContrastDTO(dto);
			responseDTO.setItemDTO(itemModels);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
		
	}
	
	/**
	 * 根据商家ID、货号查询商品
	 */
	@ApiOperation(value = "根据商家ID、货号查询商品")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "merchantId", value = "商家id", required = true),
		@ApiImplicitParam(name = "goodsNo", value = "商品货号", required = true)
	})
	@PostMapping(value="/getByMerchantAndGoodsNo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchandiseContrastResponseDTO> getByMerchantAndGoodsNo(String token,Long merchantId,String goodsNo) {
		MerchandiseContrastResponseDTO responseDTO = new MerchandiseContrastResponseDTO();		
		try{			
			List<MerchandiseInfoMogo>  merchandiseList = merchandiseContrastService.getMerchandiseList(merchantId,goodsNo);
		    if(merchandiseList==null||merchandiseList.size()<=0){		    	
		    	responseDTO.setCode("01");
		    	responseDTO.setMessage("未找到商品,请输入正确的货号");
		    }else if(merchandiseList.size()>1){
		    	responseDTO.setCode("01");
		    	responseDTO.setMessage("货号在此商家下存在多个商品请禁用不使用的商品");
		    }else{
		    	responseDTO.setCode("00");
		    	responseDTO.setMessage("查询成功");
		    	responseDTO.setMerchandiseInfoMogo(merchandiseList.get(0));
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}


	/**
	 * 根据查询条件导出
	 */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportContrast.asyn")
	public ResponseBean exportContrast(MerchandiseContrastForm form, HttpServletResponse response, HttpServletRequest request) throws SQLException {
		try {
			MerchandiseContrastDTO dto = new MerchandiseContrastDTO();
			// 响应结果集
			dto.setPlatformName(form.getPlatformName());
			dto.setCrawlerGoodsNo(form.getCrawlerGoodsNo());
			dto.setGoodsNo(form.getGoodsNo());
			dto.setStatus(form.getStatus());
			dto.setMerchantId(StringUtils.isNotBlank(form.getMerchantId()) ? Long.valueOf(form.getMerchantId()) : null);
			dto.setIds(form.getIds());

			//表头信息
			List<Map<String,Object>> list = merchandiseContrastService.listForExport(dto);

			//表头信息
			String sheetName = "爬虫商品对照信息";
			String[] columns = {"平台名称","用户名","爬虫商品货号","爬虫商品名称","公司名称","事业部","商品货号","商品名称","商品数量","标准销售单价","状态"};
			String[] keys = {"platform_name","platform_username","crawler_goods_no","crawler_goods_name",
					"merchant_name","bu_name","goods_no","goods_name","num","price","status"};

			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, list);
			//导出文件
			String fileName = "爬虫商品对照信息"+ TimeUtils.formatDay();
			FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}

}
