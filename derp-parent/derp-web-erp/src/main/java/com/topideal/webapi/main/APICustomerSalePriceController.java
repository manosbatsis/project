package com.topideal.webapi.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.*;
import com.topideal.entity.dto.main.CustomerSalePriceCountDTO;
import com.topideal.entity.dto.main.CustomerSalePriceDTO;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.service.base.BrandService;
import com.topideal.service.main.CustomerSalePriceService;
import com.topideal.service.main.MerchandiseService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.AddCustomerSalePriceForm;
import com.topideal.webapi.form.CustomerSalePriceEditForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Arrays;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户销售价格表
 * @author lian_
 */
@RestController
@RequestMapping("/webapi/system/customerSale")
@Api(tags = "客户销售价格表")
public class APICustomerSalePriceController {
	private static final Logger LOG = Logger.getLogger(APICustomerSalePriceController.class) ;

	@Autowired
	private CustomerSalePriceService service;//客户销售价格
	// 商品信息service
	@Autowired
	private MerchandiseService merchandiseService;//品类
	// 品牌信息service
	@Autowired
	private BrandService brandService;	//品牌



	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "status", value = "状态"),
			@ApiImplicitParam(name = "customerIds", value = "供应商id多个用英文逗号隔开"),
			@ApiImplicitParam(name = "createName", value = "创建人"),
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "commbarcodes", value = "标准条码")
	})
	@PostMapping(value="/listSalePrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<CustomerSalePriceDTO> listSalePrice(String token,Integer begin,Integer pageSize,String status,
			String customerIds,String createName,Long buId,String commbarcodes) {
		CustomerSalePriceDTO dto=new CustomerSalePriceDTO();//
		try{
			User user = ShiroUtils.getUserByToken(token);			
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
//			if (StringUtils.isNotBlank(commbarcodes)) {
//				List<String> commbarcodeList=new ArrayList<String>();
//				String[] commbarcodeArr = commbarcodes.split(",");
//				for(String commbarcodeStr:commbarcodeArr){
//					if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
//				}
//				dto.setCommbarcodeList(commbarcodeList);
//			}
			if (StringUtils.isNotBlank(customerIds)) {
				List<Long> customerList = StrUtils.parseIds(customerIds);
				dto.setCustomerList(customerList);
			}
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto.setStatus(status);
			dto.setCreateName(createName);
			dto.setBuId(buId);
			dto.setCommbarcode(commbarcodes);
			// 响应结果集
			dto = service.listSalePrice(dto,user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}

	/**
	 * 新增
	 * @return
	 */
	@ApiOperation(value = "新增")
	@PostMapping(value = "/addCustomerPrice.asyn")
	public ResponseBean addCustomerPrice(AddCustomerSalePriceForm form) {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());

			CustomerSalePriceDTO dto = new CustomerSalePriceDTO();
			BeanUtils.copyProperties(form, dto);

			ResponseBean responseBean = service.addCustomerPrice(user, dto);
			return responseBean;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 获取编码
	 * @return
	 */
	@ApiOperation(value = "获取编码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "ID", required = false)
	})
	@GetMapping(value = "/getCode.asyn")
	public ResponseBean getCode(String token, Long id) {
		try {
			String code = service.preGetCode(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, code);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 获取状态数量
	 * @param
	 * @return
	 */
	@ApiOperation(value = "获取状态数量")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "status", value = "状态"),
			@ApiImplicitParam(name = "customerIds", value = "供应商id多个用英文逗号隔开"),
			@ApiImplicitParam(name = "createName", value = "创建人"),
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "commbarcodes", value = "标准条码")
	})
	@PostMapping(value="/listSalePriceCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listSalePriceCount(String token,String status,
			String customerIds,String createName,Long buId,String commbarcodes) {
		CustomerSalePriceDTO dto=new CustomerSalePriceDTO();
		try{
			dto.setStatus(status);
			dto.setCreateName(createName);
			dto.setBuId(buId);
			dto.setCommbarcode(commbarcodes);
			User user = ShiroUtils.getUserByToken(token);
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setMerchantName(user.getMerchantName());//设置商家名称
			// 响应结果集
			if (StringUtils.isNotBlank(customerIds)) {
				List<Long> customerList = StrUtils.parseIds(customerIds);
				dto.setCustomerList(customerList);
			}
			CustomerSalePriceCountDTO dtoCount = service.getCountStatus(dto,user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dtoCount);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}


	/**
	 * 提交
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "提交")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id多个用英文逗号隔开")
	})
	@PostMapping(value="/submitSMPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean submitSMPrice(String token,String ids) {
		//校验id是否正确
		boolean isRight = StrUtils.validateIds(ids);
		if(!isRight){
			//输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);//成功
		}

		try{
			User user= ShiroUtils.getUserByToken(token);
			Map<String,Object> map=service.submitSMPrice(ids,user);
			String code = (String) map.get("code");
			String message = (String) map.get("message");
			if ("00".equals(code)) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);//未知异常
			}

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
		}

	}


	/**
	 * 审核
	 */
	@ApiOperation(value = "审核")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id多个用英文逗号隔开"),
			@ApiImplicitParam(name = "type", value = "1 通过 2驳回")
	})
	@PostMapping(value="/auditSMPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean auditSMPrice(String token,String ids,String type) {
		//校验id是否正确
        boolean isRight = StrUtils.validateIds(ids);
        if(!isRight){
            //输入信息不完整
        	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//未知异常
        }
		try{
			User user= ShiroUtils.getUserByToken(token);
			List idList = Arrays.asList(ids.split(","));
			for (Object object : idList) {
				CustomerSalePriceDTO customerSalePriceDTO = service.searchDetail(Long.valueOf(object.toString()));
				if (customerSalePriceDTO!=null&&DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003.equals(customerSalePriceDTO.getStatus())) {
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"包含已经审核数据");//未知异常
				}
			}

			Map<String,Object> map=service.auditSMPrice(ids,user,type);
			String code = (String) map.get("code");
			String message = (String) map.get("message");
			if ("00".equals(code)) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);//未知异常
			}
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
        }
	}

	/**
	 * 编辑
	 * @return
	 */
	@ApiOperation(value = "编辑")
	@PostMapping(value="/editSMPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean editSMPrice(CustomerSalePriceEditForm form) throws ParseException {

		CustomerSalePriceDTO dto = new CustomerSalePriceDTO();
		String effectiveDateStr = form.getEffectiveDate();
		String expiryDateStr = form.getExpiryDate();
		dto.setEffectiveDate(Timestamp.valueOf(effectiveDateStr));
		dto.setExpiryDate(Timestamp.valueOf(expiryDateStr));
		dto.setId(form.getId());
		dto.setCurrency(form.getCurrency());
		dto.setSalePrice(form.getSalePrice());
		dto.setCode(form.getCode());
		dto.setRemark(form.getRemark());

		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			Map map=service.editSalePriceModel(dto, user);
			String code = (String) map.get("code");
			String message = (String) map.get("message");
			if ("00".equals(code)) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);//未知异常
			}
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常			
		}
	}

    /**
     * 导出
     * @throws IOException
     */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "status", value = "状态"),
		@ApiImplicitParam(name = "customerIds", value = "供应商id多个用英文逗号隔开"),
		@ApiImplicitParam(name = "createName", value = "创建人"),
		@ApiImplicitParam(name = "buId", value = "事业部id"),
		@ApiImplicitParam(name = "commbarcodes", value = "标准条码")
	})
	@GetMapping(value="/export.asyn")
    public void export(HttpServletResponse response, HttpServletRequest request,
    		String token,String status,String customerIds,String createName,Long buId,String commbarcodes) throws Exception {
        String sheetName = "销售价格管理";
        String[] COLUMNS = { "所属公司","事业部","客户编号","客户名称","标准条码","商品名称","品牌","规格型号","币种","销售价","状态","报价生效时间","报价失效时间","创建人",
        		"创建时间","审核人","审核时间"};
		String[] KEYS = {"merchantName","buName","customerCode" , "customerName" , "commbarcode","goodsName",
				"brandName","spec","currencyLabel","salePrice","statusLabel","effectiveDate",
				"expiryDate","createName","createDate","auditName","auditDate"};
		User user= ShiroUtils.getUserByToken(token);		
		CustomerSalePriceDTO dto= new  CustomerSalePriceDTO();
		dto.setMerchantId(user.getMerchantId());

		dto.setCommbarcode(commbarcodes);
		dto.setStatus(status);
		dto.setCreateName(createName);
		dto.setBuId(buId);
		if(StringUtils.isNotBlank(customerIds)) {
			dto.setCustomerList(StrUtils.parseIds(customerIds));
		}		
        // 获取导出的信息
        List<CustomerSalePriceDTO> list = service.getExportList(dto,user);
        // 生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList8(sheetName, COLUMNS, KEYS, list) ;
        // 导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
    }
	
	/**
	 * 删除
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id多个用英文逗号隔开")
	})
	@PostMapping(value="/delPriceSale.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delPriceSale(String token,String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
	        	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//未知异常
			}
			List list = StrUtils.parseIds(ids);
			boolean b = service.delPriceSale(list);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常		
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
		}
	}
	
	/**
	 * 导入销售价格
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importPriceSale.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean importPriceSale(String token,@RequestParam(value = "file", required = false) MultipartFile file) {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);//未知异常
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 1);
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);//未知异常
			}
			User user= ShiroUtils.getUserByToken(token);
			resultMap = service.importPriceSale(data,user);
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			Integer pass = (Integer)resultMap.get("pass");
			
			
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常	
		}
	}

	/**
	 * 商品信息弹框列表
	 * 
	 * @param model
	 *            商品信息
	 */
	/*
	 * @RequestMapping("/getSaleListByPage.asyn")
	 * 
	 * @ResponseBody public ViewResponseBean getSaleListByPage(MerchandiseInfoModel
	 * model, String unNeedIds) { try { User user = ShiroUtils.getUser(); List ids =
	 * null; if (!StringUtils.isEmpty(unNeedIds)) { ids =
	 * StrUtils.parseIds(unNeedIds); model.setIds(ids); }
	 * model.setMerchantId(user.getMerchantId()); model =
	 * merchandiseService.getSaleListByPage(model); } catch (SQLException e) {
	 * return ResponseFactory.error(StateCodeEnum.ERROR_302, e); } catch
	 * (NullPointerException e) { return
	 * ResponseFactory.error(StateCodeEnum.ERROR_304, e); } catch (Exception e) {
	 * return ResponseFactory.error(StateCodeEnum.ERROR_305, e); } return
	 * ResponseFactory.success(model); }
	 */

	/**
	 * 根据商品id获取商品信息
	 */
	@ApiOperation(value = "根据商品id获取商品信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id多个用英文逗号隔开")
	})
	@PostMapping(value="/getSaleListByIds.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<MerchandiseInfoModel>> getSaleListByIds(String ids) {
		List<MerchandiseInfoModel> result = new ArrayList<MerchandiseInfoModel>();
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);//未知异常
			}
			List list = StrUtils.parseIds(ids);
			result = merchandiseService.getSaleListByIds(list);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常	
		}
	}
	/**
	 * 根据ID获取详情
	 */
	@ApiOperation(value = "根据ID获取详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id")
	})
	@PostMapping(value="/getSalePriceDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getSalePriceDetails(Long id) {
		//校验id是否正确
        boolean isRight = StrUtils.validateId(id);
        if(!isRight){
            //输入信息不完整
        	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);//未知异常
        }

		try{
			CustomerSalePriceDTO customerSalePriceDTO = service.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,customerSalePriceDTO);//成功
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
        }
	}
	
	/**
	 * 根据客户ID回显客户编码和主数据客户ID
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	/*
	 * @RequestMapping("/getCodeById.asyn")
	 * 
	 * @ResponseBody public ViewResponseBean getCodeById(CustomerSalePriceModel
	 * model, Long customerId)throws Exception{ List<CustomerSalePriceModel>
	 * customerList=null; if(customerId != null){ customerList =
	 * service.getCodeById(customerId); if
	 * (customerList==null||customerList.size()==0) { return
	 * ResponseFactory.error(StateCodeEnum.ERROR_306); } } Map<String, Object> map =
	 * new HashMap<>(); map.put("customerList", customerList); return
	 * ResponseFactory.success(map); }
	 */

	@ApiOperation(value = "申请作废")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "单据id集合，多个用逗号隔开"),
			@ApiImplicitParam(name = "remark", value = "作废原因")
	})
	@PostMapping(value="/submitInvalid.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean submitInvalid(String token, String ids, String remark) throws Exception{
		try {
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(ids).addObject(remark).empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			User user= ShiroUtils.getUserByToken(token);
			service.submitInvalid(user,ids,remark);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	@ApiOperation(value = "作废审核")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "单据id集合，多个用逗号隔开"),
			@ApiImplicitParam(name = "auditResult", value = "审批结果 1-通过 2-驳回")
	})
	@PostMapping(value="/auditInvalid.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean auditInvalid(String token, String ids,String auditResult) throws Exception{
		try {
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(ids).addObject(auditResult).empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			User user= ShiroUtils.getUserByToken(token);

			service.auditInvalid(user,ids,auditResult);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
}
