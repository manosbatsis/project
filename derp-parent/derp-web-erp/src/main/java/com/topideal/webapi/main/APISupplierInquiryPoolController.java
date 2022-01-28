package com.topideal.webapi.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.main.SupplierInquiryPoolDTO;
import com.topideal.entity.vo.main.SupplierInquiryPoolModel;
import com.topideal.service.base.BrandService;
import com.topideal.service.base.CountryOriginService;
import com.topideal.service.base.UnitInfoService;
import com.topideal.service.main.CustomerService;
import com.topideal.service.main.MerchandiseCatService;
import com.topideal.service.main.SupplierInquiryPoolService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.SupplierInquiryPoolAddForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 供应商询价池 控制层
 */
@RestController
@RequestMapping("/webapi/system/supplierInquiryPool")
@Api(tags = "供应商询价池 控制层")
public class APISupplierInquiryPoolController {
	
	/** 标题  */
	private static final String[] COLUMNS= {"品类","商品名称","品牌","供应商","起订量","计量单位","供货价","原产国","规格型号","录入日期"};
	private static final String[] KEYS= {"merchandiseCatName","goodsName","brandName",
			"customerName","minimum","unit","supplyPrice","countryName","spec","createDate"};

	// 供应商询价池service
	@Autowired
	private SupplierInquiryPoolService sIPoolService;
	// 品牌信息service
	@Autowired
	private BrandService brandService;
	// 客户/供应商service
	@Autowired
	private CustomerService customerService;
	// 商品分类
	@Autowired
	private MerchandiseCatService merchandiseCatService;
	// 原产国service
	@Autowired
	private CountryOriginService countryOriginService;
	// 标准单位
	@Autowired
	private UnitInfoService unitInfoService;
	
	/**
	 * 访问列表页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toPage(String token)throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			List<SelectBean> brandBean = brandService.getSelectBean();
			resultMap.put("brandBean", brandBean);
			User user = ShiroUtils.getUserByToken(token);
			List<SelectBean> supplierBean = customerService.getSelectBeanBySupplier(user.getMerchantId());
			resultMap.put("supplierBean", supplierBean);
			List<SelectBean> productTypeBean = merchandiseCatService.getSelectBean(new HashMap<String, Object>());
			resultMap.put("productTypeBean", productTypeBean);
			List<SelectBean> countryBean = countryOriginService.getSelectBean();
			resultMap.put("countryBean", countryBean);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	
	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */

	@ApiOperation(value = "访问编辑页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toEditPage(Long id)throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			SupplierInquiryPoolDTO sIPoolModel = sIPoolService.searchDetail(id);
			resultMap.put("detail", sIPoolModel);
			List<SelectBean> catBean = merchandiseCatService.getSelectBean(new HashMap<String, Object>());
			resultMap.put("catBean", catBean);
			List<SelectBean> productTypeBean = merchandiseCatService.getSelectBean(new HashMap<String, Object>());
			resultMap.put("productTypeBean", productTypeBean);
			List<SelectBean> unitBean = unitInfoService.getSelectBean();
			resultMap.put("unitBean", unitBean);
			List<SelectBean> brandBean = brandService.getSelectBean();
			resultMap.put("brandBean", brandBean);
			List<SelectBean> countryBean = countryOriginService.getSelectBean();
			resultMap.put("countryBean", countryBean);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	/**
	 * 访问详情页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailsPage(Long id)throws Exception {
		try {
			SupplierInquiryPoolDTO sIPoolModel = sIPoolService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,sIPoolModel);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 导入页面
	 * */
	/*@RequestMapping("/toImportPage.html")
	public String toImportPage(){
		return "/derp/main/supplierInquiryPool-import";
	}*/

	/**
	 * 获取分页数据
	 * @param model  供应商询价池信息
	 * @return
	 */
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "customerId", value = "供应商id"),
			@ApiImplicitParam(name = "brandId", value = "供应商品牌 "),
			@ApiImplicitParam(name = "goodsName", value = "商品名称 "),
			@ApiImplicitParam(name = "merchandiseCatId", value = "商品分类id"),
			@ApiImplicitParam(name = "countryId", value = "原产国id")
	})
	@PostMapping(value="/listSIPool.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listSIPool(String token,Long customerId,Long brandId,String goodsName,String goodsNo
			,Long merchandiseCatId,Long countryId,int begin,int pageSize) {
		try{
			User user = ShiroUtils.getUserByToken(token);
			//数据权限过滤
			SupplierInquiryPoolDTO dto=new SupplierInquiryPoolDTO();
			dto.setCustomerId(customerId);
			dto.setBrandId(brandId);
			dto.setGoodsName(goodsName);
			//dto.setgoodsNo
			dto.setMerchandiseCatId(merchandiseCatId);
			dto.setCountryId(countryId);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = sIPoolService.listSIPool(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id集合多个用英文逗号隔开", required = true)
	})
	@PostMapping(value="/delSIPool.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delSIPool(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
            List list = StrUtils.parseIds(ids);
            boolean b = sIPoolService.delSIPool(list);
            if(!b){
    			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,b);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }

	}
	
	/**
	 * 批量导入
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importSIPool.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean importSIPool(String token,@RequestParam(value = "file", required = false) MultipartFile file) {
		Map resultMap = new HashMap();//返回的结果集
		try{
            if(file==null){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
            Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 1);
			if(data == null){//数据为空
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
            }
			User user = ShiroUtils.getUserByToken(token);
			resultMap = sIPoolService.importSIPool(data,user.getMerchantId(),user.getId());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	/**
	 * 修改
	 * @param model  供应商询价池信息
	 * @return
	 */
	@RequestMapping("/modifySIPool.asyn")
	@ResponseBody
	public ResponseBean modifySIPool(SupplierInquiryPoolAddForm form) {
		try{
			SupplierInquiryPoolModel model=new SupplierInquiryPoolModel();
			model.setId(form.getId());
			model.setMerchandiseCatName(form.getMerchandiseCatName());
			model.setBrandName(form.getBrandName());
			model.setSupplyPrice(form.getSupplyPrice());
			model.setGoodsId(form.getGoodsId());
			model.setCustomerCode(form.getCustomerCode());
			model.setCustomerName(form.getCustomerName());
			model.setSpec(form.getSpec());
			model.setUnit(form.getUnit());
			model.setCountryId(form.getCountryId());
			model.setMerchandiseCatId(form.getMerchandiseCatId());
			model.setBrandId(form.getBrandId());
			model.setCustomerId(form.getCustomerId());
			model.setMaximum(form.getMaximum());
			model.setGoodsName(form.getGoodsName());
			model.setMinimum(form.getMinimum());
			model.setCountryName(form.getCountryName());
			model.setUnitId(form.getUnitId());
			model.setMerchantId(form.getMerchantId());
			
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
            User user = ShiroUtils.getUserByToken(form.getToken());
            boolean b = sIPoolService.modifySIPool(model,user.getId());
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }

	}
	
	/**
	 * 导出供应商信息
	 * @param response
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportSupplier.asyn")
	public void exportRelation(HttpServletResponse response, HttpServletRequest request, String token,Long customerId,Long brandId,String goodsName,String goodsNo
			,Long merchandiseCatId,Long countryId) throws Exception{
		try {
			SupplierInquiryPoolModel model=new SupplierInquiryPoolModel();
			SupplierInquiryPoolDTO dto=new SupplierInquiryPoolDTO();
			dto.setCustomerId(customerId);
			dto.setBrandId(brandId);
			dto.setGoodsName(goodsName);
			//dto.setgoodsNo
			dto.setMerchandiseCatId(merchandiseCatId);
			dto.setCountryId(countryId);
			String sheetName = "供应商询价池导出";
	        List<SupplierInquiryPoolModel> list = sIPoolService.exportList(model);
	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, list);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
        	e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
		
	}
}
