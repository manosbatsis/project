package com.topideal.webapi.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.*;
import com.topideal.entity.dto.main.SupplierMerchandisePriceDTO;
import com.topideal.entity.vo.main.SupplierMerchandisePriceModel;
import com.topideal.service.main.CustomerService;
import com.topideal.service.main.SupplierMerchandisePriceService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.AddSupplierMerchandisePriceForm;
import com.topideal.webapi.form.SupplierMerchandisePriceEditForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商商品价目表 控制层
 */
@RestController
@RequestMapping("/webapi/system/supplierMerchandisePrice")
@Api(tags = "供应商商品价目表")
public class APISupplierMerchandisePriceController {
	private static final String[] MAIN_COLUMNS = {"所属公司", "事业部", "供应商编码", "供应商名称", "条形码", "标准条码", "价格类型", "数据来源", "商品名称", "报价生效日期",
			"报价失效日期", "报价币种", "供货价"} ;

	private static final String[] MAIN_KEYS = {"merchantName", "buName", "customerCode", "customerName", "barcode", "commbarcode", "stockLocationTypeName", "dataSourceLabel",
			"goodsName", "effectiveDate", "expiryDate", "currencyLabel", "supplyPrice"} ;

	private static final Logger LOG = Logger.getLogger(APISupplierMerchandisePriceController.class) ;

	// 供应商商品价目表service
	@Autowired
	private SupplierMerchandisePriceService sMPriceService;
	// 客户/供应商service
	@Autowired
	private CustomerService customerService;
	/**
	 * 访问列表页面
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> toPage(String token) throws Exception {
		try {
			User user = ShiroUtils.getUserByToken(token);
			List<SelectBean> supplierBean = customerService.getAllSelectBeanBySupplier();
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,supplierBean);//成功

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}


	/**
	 * 获取分页数据
	 *
	 * @param
	 *
	 * @return
	 */
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "state", value = "状态"),
			@ApiImplicitParam(name = "customerIdsStr", value = "供应商id多个用英文逗号隔开"),
			@ApiImplicitParam(name = "createName", value = "创建人"),
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "commbarcodes", value = "标准条码 "),
			@ApiImplicitParam(name = "stockLocationTypeId", value = "库位类型id "),
			@ApiImplicitParam(name = "dataSource", value = "数据来源 ")
	})
	@PostMapping(value="/listSMPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SupplierMerchandisePriceDTO> listSMPrice(String token,int begin,int pageSize,String state,
			String customerIdsStr,String createName,Long buId,String commbarcodes, Long stockLocationTypeId, String dataSource) {

		try {
			User user = ShiroUtils.getUserByToken(token);
			SupplierMerchandisePriceDTO dto=new SupplierMerchandisePriceDTO();
			// 商家id
			dto.setMerchantId(user.getMerchantId());
			/*if (StringUtils.isNotBlank(commbarcodes)) {
				List<String> commbarcodeList=new ArrayList<String>();
				String[] commbarcodeArr = commbarcodes.split(",");
				for(String commbarcodeStr:commbarcodeArr){
					if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
				}
				dto.setCommbarcodeList(commbarcodeList);
			}	*/
			if (StringUtils.isNotBlank(customerIdsStr)) {
				List<Long> customerList = StrUtils.parseIds(customerIdsStr);
				dto.setSupplierList(customerList);
			}
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto.setMerchantId(user.getMerchantId());// 商家id
			dto.setCreateName(createName);
			dto.setBuId(buId);
			dto.setState(state);
			dto.setCommbarcode(commbarcodes);
			dto.setStockLocationTypeId(stockLocationTypeId);
			dto.setDataSource(dataSource);

			// 响应结果集
			dto = sMPriceService.listSMPrice(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 新增
	 * @return
	 */
	@ApiOperation(value = "新增")
	@PostMapping(value = "/addSMPrice.asyn")
	public ResponseBean addSMPrice(AddSupplierMerchandisePriceForm form) {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());

			SupplierMerchandisePriceDTO dto = new SupplierMerchandisePriceDTO();
			BeanUtils.copyProperties(form, dto);

			ResponseBean responseBean = sMPriceService.addSMPrice(user, dto);
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
			@ApiImplicitParam(name = "id", value = "ID", required = true)
	})
	@GetMapping(value = "/getCode.asyn")
	public ResponseBean getCode(String token, Long id) {
		try {
			String code = sMPriceService.preGetCode(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, code);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id集合,多个用英文逗号隔开", required = true)
	})
	@PostMapping(value="/delSMPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delSMPrice(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			List list = StrUtils.parseIds(ids);
			boolean b = sMPriceService.delSMPrice(list);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 批量导入
	 *
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importSMPrice.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean <UploadResponse> importSMPrice(String token,@RequestParam(value = "file", required = false) MultipartFile file) {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 1);
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			resultMap = sMPriceService.importSMPrice(data, user);
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			Integer pass = (Integer)resultMap.get("pass");


			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 获取导出数量
	 */
	@ApiOperation(value = "获取导出数量")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "state", value = "状态"),
			@ApiImplicitParam(name = "customerIdsStr", value = "供应商id多个用英文逗号隔开"),
			@ApiImplicitParam(name = "createName", value = "创建人"),
			@ApiImplicitParam(name = "buId", value = "事业部id"),
			@ApiImplicitParam(name = "commbarcodes", value = "标准条码多个用英文逗号隔开 "),
			@ApiImplicitParam(name = "stockLocationTypeId", value = "库位类型id "),
			@ApiImplicitParam(name = "dataSource", value = "数据来源 ")
	})
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,
			String token,String state,
			String customerIdsStr,String createName,Long buId,String commbarcodes, Long stockLocationTypeId, String dataSource) throws Exception{

		try{
			User user = ShiroUtils.getUserByToken(token);
			SupplierMerchandisePriceDTO dto=new SupplierMerchandisePriceDTO();
			// 商家id
			dto.setMerchantId(user.getMerchantId());
			/*if (StringUtils.isNotBlank(commbarcodes)) {
				List<String> commbarcodeList=new ArrayList<String>();
				String[] commbarcodeArr = commbarcodes.split(",");
				for(String commbarcodeStr:commbarcodeArr){
					if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
				}
				dto.setCommbarcodeList(commbarcodeList);
			}	*/
			if (StringUtils.isNotBlank(customerIdsStr)) {
				List<Long> customerList = StrUtils.parseIds(customerIdsStr);
				dto.setSupplierList(customerList);
			}

			dto.setMerchantId(user.getMerchantId());// 商家id
			dto.setCreateName(createName);
			dto.setBuId(buId);
			dto.setState(state);
			dto.setCommbarcode(commbarcodes);
			dto.setStockLocationTypeId(stockLocationTypeId);
			dto.setDataSource(dataSource);

			// 响应结果集
			List<SupplierMerchandisePriceDTO> result = sMPriceService.getSMPriceList(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result.size());//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	/**
	 * 导出
	 * */

	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "state", value = "状态"),
		@ApiImplicitParam(name = "customerIdsStr", value = "供应商id多个用英文逗号隔开"),
		@ApiImplicitParam(name = "createName", value = "创建人"),
		@ApiImplicitParam(name = "buId", value = "事业部id"),
		@ApiImplicitParam(name = "commbarcodes", value = "标准条码多个用英文逗号隔开 "),
		@ApiImplicitParam(name = "stockLocationTypeId", value = "库位类型id "),
		@ApiImplicitParam(name = "dataSource", value = "数据来源 "),
		@ApiImplicitParam(name = "ids", value = "选中的单据id，多个用逗号隔开 ")
	})
	@GetMapping(value="/exportSMPrice.asyn")
	private void exportSMPrice(HttpServletResponse response, HttpServletRequest request,
			String token,String state, String ids,
			String customerIdsStr,String createName,Long buId,String commbarcodes, Long stockLocationTypeId, String dataSource) throws Exception{
		try {
			User user = ShiroUtils.getUserByToken(token);
			// 设置商家id
			SupplierMerchandisePriceDTO dto=new SupplierMerchandisePriceDTO();
			// 商家id
			dto.setMerchantId(user.getMerchantId());
			/*if (StringUtils.isNotBlank(commbarcodes)) {
				List<String> commbarcodeList=new ArrayList<String>();
				String[] commbarcodeArr = commbarcodes.split(",");
				for(String commbarcodeStr:commbarcodeArr){
					if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
				}
				dto.setCommbarcodeList(commbarcodeList);
			}	*/
			if (StringUtils.isNotBlank(customerIdsStr)) {
				List<Long> customerList = StrUtils.parseIds(customerIdsStr);
				dto.setSupplierList(customerList);
			}

			// 商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCreateName(createName);
			dto.setBuId(buId);
			dto.setState(state);
			dto.setCommbarcode(commbarcodes);
			dto.setStockLocationTypeId(stockLocationTypeId);
			dto.setDataSource(dataSource);
			dto.setIds(StrUtils.parseIds(ids));
			// 响应结果集
			List<SupplierMerchandisePriceDTO> mainList = sMPriceService.getSMPriceList(dto);
			String mainSheetName = "供应商商品价目";

			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList8(mainSheetName, MAIN_COLUMNS, MAIN_KEYS, mainList) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
			//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 审核
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "审核")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id的集合,多个用英文逗号隔开", required = true),
			@ApiImplicitParam(name = "auditType", value = "审核类型 0-通过 1-驳回", required = true)
	})
	@PostMapping(value="/auditSMPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean auditSMPrice(String token,String ids, String auditType) {
		//校验id是否正确
		boolean isRight = StrUtils.validateIds(ids);
		if(!isRight){
			//输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		try {
			List<Long> list = StrUtils.parseIds(ids);
			User user = ShiroUtils.getUserByToken(token);
			sMPriceService.auditSMPrice(list,user,auditType) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	@ApiOperation(value = "获取供应商价目表数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "customerId", value = "供应商ID", required = true),
			@ApiImplicitParam(name = "currency", value = "币种"),
			@ApiImplicitParam(name = "commbarcodes", value = "标准条码"),
			@ApiImplicitParam(name = "effectiveDateStr", value = "生效时间"),
			@ApiImplicitParam(name = "buId", value = "事业部ID", required = true),
	})
	@PostMapping(value="/getSMPriceByPurchaseOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getSMPriceByPurchaseOrder(String token,Long customerId,String currency,
			String commbarcodes,String effectiveDateStr, Long buId) {

		try {
			SupplierMerchandisePriceModel model=new SupplierMerchandisePriceModel();
			model.setCustomerId(customerId);
			model.setCurrency(currency);
			model.setCommbarcode(commbarcodes);
			model.setBuId(buId);
			User user = ShiroUtils.getUserByToken(token);
			model.setMerchantId(user.getMerchantId());

			if(StringUtils.isNotBlank(effectiveDateStr)){
				model.setEffectiveDate(TimeUtils.parse(effectiveDateStr, "yyyy-MM-dd"));
			}

			com.alibaba.fastjson.JSONObject jsonObject = sMPriceService.getSMPriceByPurchaseOrder(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,jsonObject);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}


	@ApiOperation(value = "统计各个状态下的采购价格数量")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "commbarcodes", value = "标准条码"),
			@ApiImplicitParam(name = "buId", value = "事业部ID"),
			@ApiImplicitParam(name = "customerIdsStr", value = "供应商id多个用英文逗号隔开"),
			@ApiImplicitParam(name = "createName", value = "创建人名称")
	})
	@PostMapping(value="/statisticsStateNum.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SupplierMerchandisePriceDTO> statisticsStateNum(String token,
			String commbarcodes,Long buId,String createName,String customerIdsStr) {
		try {
			SupplierMerchandisePriceDTO dto = new SupplierMerchandisePriceDTO();
			User user = ShiroUtils.getUserByToken(token);
			dto.setMerchantId(user.getMerchantId());
			dto.setCreateName(createName);
			dto.setBuId(buId);
			dto.setCommbarcode(commbarcodes);
			/*if (StringUtils.isNotBlank(commbarcodes)) {
				List<String> commbarcodeList=new ArrayList<String>();
				String[] commbarcodeArr = commbarcodes.split(",");
				for(String commbarcodeStr:commbarcodeArr){
					if(!StringUtils.isEmpty(commbarcodeStr)) commbarcodeList.add(commbarcodeStr);
				}
				dto.setCommbarcodeList(commbarcodeList);
			}*/
			if (StringUtils.isNotBlank(customerIdsStr)) {
				List<Long> customerList = StrUtils.parseIds(customerIdsStr);
				dto.setSupplierList(customerList);
			}
			dto = sMPriceService.statisticsStateNum(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	@ApiOperation(value = "提交")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id的集合,多个用英文逗号隔开")
	})
	@PostMapping(value="/submitSMPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean submitSMPrice(String token, String ids) {
		//校验id是否正确
		boolean isRight = StrUtils.validateIds(ids);
		if(!isRight){
			//输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);//未知异常
		}
		List<Long> list = StrUtils.parseIds(ids);

		try {
			User user= ShiroUtils.getUserByToken(token);
			sMPriceService.submitSMPrice(list,user) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	@ApiOperation(value = "获取采购价格详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购价格id")
	})
	@PostMapping(value="/getDetailsById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SupplierMerchandisePriceDTO> getDetailsById(String token, Long id) throws Exception{
		try {
			//校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if(!isRight){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);//未知异常
			}
			SupplierMerchandisePriceDTO dto = sMPriceService.searchDTOById(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	@ApiOperation(value = "编辑保存采购价格")
	@PostMapping(value="/modifySMPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifySMPrice(SupplierMerchandisePriceEditForm form) throws Exception{
		try {
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(form.getId()).addObject(form.getCode())
					.addObject(form.getSupplyPrice()).addObject(form.getEffectiveDateStr()).addObject(form.getBuId())
					.addObject(form.getExpiryDateStr()).empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			User user= ShiroUtils.getUserByToken(form.getToken());
			SupplierMerchandisePriceDTO dto = new SupplierMerchandisePriceDTO();
			BeanUtils.copyProperties(form, dto);
			sMPriceService.modifySMPrice(dto, user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
		}
	}


	/**
	 * 申请作废
	 * @param token
	 * @param ids
	 * @param remark
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "申请作废")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购价格ids"),
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

			sMPriceService.submitInvalid(user,ids,remark);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
		}
	}


	/**
	 * 作废审核
	 * @param token
	 * @param ids
	 * @param auditResult
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "作废审核")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购价格ids"),
			@ApiImplicitParam(name = "auditResult", value = "审批结果 1-审批通过 2-驳回")
	})
	@PostMapping(value="/invalidAudit.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean invalidAudit(String token, String ids,String auditResult) throws Exception{
		try {
			//校验id是否正确
			boolean isNull = new EmptyCheckUtils().addObject(ids).addObject(auditResult).empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			User user= ShiroUtils.getUserByToken(token);

			sMPriceService.invalidAudit(user,ids,auditResult);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
		}
	}
}
