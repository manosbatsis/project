package com.topideal.webapi.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.constant.DerpBasic;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.main.CustomerInfoDTO;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.entity.vo.main.TariffRateConfigModel;
import com.topideal.service.base.TariffRateConfigService;
import com.topideal.service.main.CustomerMerchantRelService;
import com.topideal.service.main.CustomerService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.CustomerInfoAddForm;
import com.topideal.webapi.form.CustomerInfoDetailForm;
import com.topideal.webapi.form.CustomerInfoForm;
import com.topideal.webapi.form.CustomerToAddPageResponseDTO;
import com.topideal.webapi.form.CustomerToPageResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 客户信息 控制层
 * @author zhanghx
 */
@RestController
@RequestMapping("/webapi/system/customer")
@Api(tags = "客户信息")
public class APICustomerController {
	
	private static final Logger LOG = Logger.getLogger(APICustomerController.class) ;
	
	/** 标题  */
	private static final String[] COLUMNS= {"客户编码","客户名称","主数据ID","关联公司","数据来源","组织机构代码","是否内部公司","手机","业务员","状态","销售币种","创建时间"};
	private static final String[] KEYS= {"code","name","mainId","merchantNameStr","sourceLabel","orgCode","typeLabel","legalTel", "salesman" ,"statusLabel","currencyLabel","createDate"};

	// 客户信息service
	@Autowired
	private CustomerService customerService;

	// 客户公司中间表service
	@Autowired
	private CustomerMerchantRelService customerMerchantRelService;
	@Autowired
	private TariffRateConfigService tariffRateConfigService;
	
	
	
	/**
	 * 获取客户供应商下拉框公共方法
	 * typeFlag 
	 * 1.查询全部客户(包含启用禁用客户 客户列表用)
	 * 2.查询全部供应商 (包含启用禁用供应商列表用)
	 * 3.查询启用客户(全部)
	 * 4.查询启用供应商(全部)
	 * 5.查询当前商家的启用客户
	 * 6.查询当前商家启用供应商
	 * @return
	 */
	@ApiOperation(value = "获取客户供应商下拉框公共方法")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "typeFlag", value = " 1.查询全部客户(包含启用禁用客户 客户列表用)2.查询全部供应商 (包含启用禁用供应商列表用) 3.查询启用客户(全部)4.查询启用供应商(全部) 5.查询当前商家的启用客户6.查询当前商家启用供应商", required = true)
	})
	@PostMapping(value="/getCusOrSurpSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getCusOrSurpSelectBean(String typeFlag,String token ) {
		
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			CustomerInfoDTO dto = new CustomerInfoDTO();
			if ("1".equals(typeFlag)) {//查询全部客户(包含启用禁用客户 客户列表用)
				dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
			}else if ("2".equals(typeFlag)) {//查询全部供应商 (包含启用禁用供应商列表用)				
				dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
			}else if ("3".equals(typeFlag)) {//查询启用客户(全部)
				dto.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);
				dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
			}else if ("4".equals(typeFlag)) {//查询启用供应商(全部)
				dto.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);
				dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
			}else if ("5".equals(typeFlag)) {//查询当前商家的启用客户
				dto.setMerchantId(user.getMerchantId());
				dto.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);
				dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
			}else if ("6".equals(typeFlag)) {//查询当前商家启用供应商
				dto.setMerchantId(user.getMerchantId());
				dto.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);
				dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
			}

			result = customerService.getCusOrSurpSelectBean(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	
	
	/**
	 * 合作公司保存(客户)
	 * @param json
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "合作公司保存(客户)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "json", value = "json", required = true)
	})
	@PostMapping(value="/saveMerchantRelJSon.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveMerchantRelJSon(String json,String token) {
		
		try{
			User user = ShiroUtils.getUserByToken(token);
			JSONObject jsonData = JSONObject.fromObject(json);
			String customerId = jsonData.getString("customerId");
			if (jsonData==null||StringUtils.isBlank(customerId)) {				
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}			
			JSONArray  jSONArray = (JSONArray) jsonData.get("itemList");
			customerService.saveMerchantRelJSon(jSONArray,customerId,user) ;			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功

        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	/**
	 * 访问列表页面
	 * */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<CustomerToPageResponseDTO> toPage(String token) {
		try {
			CustomerToPageResponseDTO result=new CustomerToPageResponseDTO();
			User user = ShiroUtils.getUserByToken(token) ;
			Map<String, Object> resultMap=new HashMap<String, Object>();
			List<SelectBean> merchantList = customerService.getMerchantList(user) ;
			result.setMerchantId(user.getMerchantId());
			result.setMerchantList(merchantList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	@ApiOperation(value = "访问新增详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "mainId", value = "主数据id")
	})
	@PostMapping(value="/toAddPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<CustomerToAddPageResponseDTO> toAddPage(String token, Long mainId) throws SQLException {
		try {
			CustomerToAddPageResponseDTO result=new CustomerToAddPageResponseDTO();
			User user = ShiroUtils.getUserByToken(token);
			result.setUser(user);
			List<SelectBean> returnMerchantList = customerService.getMerchantList(user);			
			result.setMerchantList(returnMerchantList);
			if(mainId != null) {
				CustomerInfoModel customer = customerService.getCustomerMainInfo(mainId) ;
				result.setDetail(customer);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 访问批量导入页面
	 * */
	/*@RequestMapping("/toImportPage.html")
	public String toImportPage() {
		return "/derp/main/customer-import";
	}*/

	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailsPage(String token,Long id)throws Exception{
		try {
			User user = ShiroUtils.getUserByToken(token);
			Map<String, Object> resultMap=new HashMap<String, Object>();
			CustomerInfoDTO customerInfo = customerService.searchDetail(id);
			resultMap.put("detail", customerInfo);
			Map<String, Object> paramMap=new HashMap<>();
			paramMap.put("customerId", id);
			paramMap.put("userId", user.getId());
			List<MerchantInfoDTO> relList = customerService.getMerchantRelInfoAndMerchantInfo(paramMap) ;			
			resultMap.put("relList", relList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 访问编辑页面
	 * */
	@ApiOperation(value = "访问编辑详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toEditPage(String token,Long id)throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			User user = ShiroUtils.getUserByToken(token);
			resultMap.put("user",user);
			CustomerInfoDTO customerInfo = customerService.searchDetail(id);
			resultMap.put("detail", customerInfo);
			List<SelectBean> returnMerchantList = customerService.getMerchantList(user) ;
			resultMap.put("merchantList", returnMerchantList);
			CustomerMerchantRelModel customerMerchantRelModel = customerMerchantRelService.searchDetail(id,user);
			resultMap.put("customerMerchantRelModel",customerMerchantRelModel);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	@ApiOperation(value = "获取商家客户关系数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "customerId", value = "customerId", required = true)
	})
	@PostMapping(value="/getCustomerMerchantRelList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ViewResponseBean getCustomerMerchantRelList(String token,Long customerId) {
		try{
			// 查询商户类型为：1客户
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			Map<String, Object> paramMap=new HashMap<>();
			paramMap.put("customerId", customerId);
			paramMap.put("userId", user.getId());
			List<Map<String, Object>> relList = customerService.getCustomerMerchantRelList(paramMap);
			List<TariffRateConfigModel> tariffRateConfigList = tariffRateConfigService.getTariffRateConfigList(new TariffRateConfigModel());

			Map<String, Object> resultMap=new HashMap<>();
			ArrayList<DerpBasic> settlementTypeList = DERP_SYS.customerMerchantRel_settlementTypeList;
			ArrayList<DerpBasic> accountPeriodList = DERP_SYS.customerInfo_accountPeriodList;
			ArrayList<DerpBasic> businessModelList = DERP_SYS.customerMerchantRel_businessModelList;
			resultMap.put("relList", relList);
			resultMap.put("settlementTypeList", settlementTypeList);
			resultMap.put("accountPeriodList", accountPeriodList);
			resultMap.put("businessModelList", businessModelList);
			resultMap.put("tariffRateConfigList", tariffRateConfigList);
			return ResponseFactory.success(resultMap);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		
	}
	
	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/listCustomer.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listCustomer(CustomerInfoForm form) {
		try{
			CustomerInfoDTO dto= new CustomerInfoDTO();
			dto.setCode(form.getCode());
			dto.setName(form.getName());			
			dto.setStatus(form.getStatus());
			dto.setMainId(form.getMainId());
			dto.setMerchantId(form.getMerchantId());
			dto.setSource(form.getSource());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
			dto.setId(form.getId());
			// 响应结果集
			dto = customerService.listCustomer(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 新增
	 * */
	@ApiOperation(value = "新增数据")
	@PostMapping(value="/saveCustomer.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveCustomer(CustomerInfoAddForm form) {
		Map<String, Object> customerMap=null;
		try {
			JSONObject jsonData = JSONObject.fromObject(form.getJson());
			Object object = jsonData.get("merchantList");
			JSONArray arry=new JSONArray();
			
			if (object!=null) {
				arry=(JSONArray) object;
			}
			Map<String, Object> map=new HashMap<>();
			for (Object object2 : arry) {
				JSONObject jSONObject =(JSONObject) object2;
				String merchantId = jSONObject.getString("merchantId");
				String merchantName = jSONObject.getString("merchantName");
				if (map.containsKey(merchantId)) {
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"合作商家传值重复");//未知异常
				}
				map.put(merchantId, merchantName);
			}
			User user = ShiroUtils.getUserByToken(form.getToken());
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(form.getName())
					.addObject(form.getCreditCode())
					.addObject(form.getOrgCode())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			} 
			// 设置商家id
			CustomerInfoDTO model=new CustomerInfoDTO();
			model.setName(form.getName());
			model.setSimpleName(form.getSimpleName());
			model.setCreditCode(form.getCreditCode());
			model.setOrgCode(form.getOrgCode());
			model.setNature(form.getNature());
			model.setCurrency(form.getCurrency());
			model.setChinaName(form.getChinaName());
			model.setEnSimpleName(form.getEnSimpleName());
			model.setEnName(form.getEnName());
			model.setCompanyAddr(form.getCompanyAddr());
			model.setCodeGrade(form.getCodeGrade());
			model.setAuthNo(form.getAuthNo());
			model.setMainId(form.getMainId());
			model.setType(form.getType());
			model.setInnerMerchantId(form.getInnerMerchantId());
			model.setOperationScope(form.getOperationScope());
			model.setTaxNo(form.getTaxNo());
			model.setNcPlatformCode(form.getNcPlatformCode());
			model.setNcChannelCode(form.getNcChannelCode());
			model.setLegalPerson(form.getLegalPerson());
			model.setLegalNationality(form.getLegalNationality());
			model.setLegalCardNo(form.getLegalCardNo());
			model.setLegalTel(form.getLegalTel());
			model.setOTelNo(form.getoTelNo());
			model.setChannelClassify(form.getChannelClassify());
			model.setDepositBank(form.getDepositBank());
			model.setBankAccount(form.getBankAccount());
			model.setBankAddress(form.getBankAddress());
			model.setSwiftCode(form.getSwiftCode());
			model.setBeneficiaryName(form.getBeneficiaryName());
						
			model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);// 类型  1 客户  2 供应商',
			model.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);//状态(1使用中,0已禁用)
			if(StringUtils.isBlank(model.getSource())) {
				model.setSource(DERP_SYS.CUSTOMERINFO_SOURCE_2);
			}
			// 存储创建人
			model.setCreater(user.getId());
			customerMap = customerService.saveCustomer(model,arry,null);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,customerMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 删除
	 * */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id的集合,多个用逗号隔开", required = true)
	})
	@PostMapping(value="/delCustomer.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delCustomer(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = customerService.delCustomer(list);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}

	/**
	 * 修改
	 * */
	@ApiOperation(value = "修改数据")
	@PostMapping(value="/modifyCustomer.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyCustomer(CustomerInfoAddForm form) {
		Map<String, Object> customerMap=null;
		try{
			JSONObject jsonData = JSONObject.fromObject(form.getJson());
			Object object = jsonData.get("merchantList");
			Object deleteIdsObj = jsonData.get("deleteIds");
			List<Object> deleteIdsList=new ArrayList<>();
			if (deleteIdsObj!=null) {
				deleteIdsList = Arrays.asList(deleteIdsObj.toString().split(","));
			}
			
			JSONArray arry=new JSONArray();
			
			if (object!=null) {
				arry=(JSONArray) object;
			}
			Map<String, Object> map=new HashMap<>();
			for (Object object2 : arry) {
				JSONObject jSONObject =(JSONObject) object2;
				String merchantId = jSONObject.getString("merchantId");
				String merchantName = jSONObject.getString("merchantName");
				if (map.containsKey(merchantId)) {
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"合作商家传值重复");//未知异常
				}
				map.put(merchantId, merchantName);
			}
			//校验id是否正确
            boolean isRight = StrUtils.validateId(form.getId());
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
        	
			CustomerInfoDTO dto=new CustomerInfoDTO();
			dto.setId(form.getId());
			dto.setName(form.getName());
			dto.setSimpleName(form.getSimpleName());
			dto.setCreditCode(form.getCreditCode());
			dto.setOrgCode(form.getOrgCode());
			dto.setNature(form.getNature());
			dto.setCurrency(form.getCurrency());
			dto.setChinaName(form.getChinaName());
			dto.setEnSimpleName(form.getEnSimpleName());
			dto.setEnName(form.getEnName());
			dto.setCompanyAddr(form.getCompanyAddr());
			dto.setCodeGrade(form.getCodeGrade());
			dto.setAuthNo(form.getAuthNo());
			dto.setMainId(form.getMainId());
			dto.setType(form.getType());
			dto.setInnerMerchantId(form.getInnerMerchantId());
			dto.setOperationScope(form.getOperationScope());
			dto.setTaxNo(form.getTaxNo());
			dto.setNcPlatformCode(form.getNcPlatformCode());
			dto.setNcChannelCode(form.getNcChannelCode());
			dto.setLegalPerson(form.getLegalPerson());
			dto.setLegalNationality(form.getLegalNationality());
			dto.setLegalCardNo(form.getLegalCardNo());
			dto.setLegalTel(form.getLegalTel());
			dto.setOTelNo(form.getoTelNo());
			dto.setChannelClassify(form.getChannelClassify());
			dto.setBeneficiaryName(form.getBeneficiaryName());
			dto.setDepositBank(form.getDepositBank());
			dto.setBankAccount(form.getBankAccount());
			dto.setBankAddress(form.getBankAddress());
			dto.setSwiftCode(form.getSwiftCode());
						
            
            // 设置商家id
			dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
            //修改时间
			dto.setModifyDate(TimeUtils.getNow());
            customerMap = customerService.modifyCustomer(dto,arry,deleteIdsList,null);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,customerMap);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	/**
	 * 导入
	 * @param 
	 * @return int
	 * @author zhanghx
	 * @throws IOException 
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importCustomer.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importCustomer(String token,@RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
		Map resultMap = new HashMap();//返回的结果集
		try{
			if(file==null){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 1);
			if(data == null){//数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			User user = ShiroUtils.getUserByToken(token);
			resultMap = customerService.saveImportCustomer(data, user.getMerchantId(), user.getId(), user.getMerchantName());
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			Integer pass = (Integer)resultMap.get("pass");
			
			
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	/**
	 * 获取下拉列表
	 * @return
	 */
	@ApiOperation(value = "获取下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "type", value = "客户类型(1内部,2外部)")
	})
	@PostMapping(value="/getSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getSelectBean(String token,String type) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			CustomerInfoDTO dto = new CustomerInfoDTO();
			if(StringUtils.isNotBlank(type)){
				dto.setType(type);// 客户类型(1内部,2外部)
			}else{
				dto.setMerchantId(user.getMerchantId());
			}
			result = customerService.getSelectBeanByMerchantId(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 导出客户信息
	 * @param response
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportCustomer.asyn")
	public void exportRelation(String token,HttpServletResponse response, HttpServletRequest request, CustomerInfoForm form) throws Exception{
		try {
			User user = ShiroUtils.getUserByToken(token);
			// 查询商户类型为：1客户
			CustomerInfoDTO dto= new CustomerInfoDTO();
			dto.setCode(form.getCode());
			dto.setName(form.getName());			
			dto.setStatus(form.getStatus());
			dto.setMainId(form.getMainId());
			dto.setMerchantId(form.getMerchantId());
			dto.setSource(form.getSource());
			dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);

			String sheetName = "客户信息";
			List<CustomerInfoDTO> list = customerService.exportList(dto);
			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , list);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	
	/**
	 * 获取下拉列表
	 * @return
	 */
/*	@RequestMapping("/getNameById.asyn")
	@ResponseBody
	public ViewResponseBean getNameById() {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUser();
			result = customerService.getNameById(user.getMerchantId());
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}*/
	
	
	/**
	 * 启用禁用修改 
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "启用/禁用修改 ")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id",required = true),
			@ApiImplicitParam(name = "status", value = "状态",required = true)
	})
	@PostMapping(value="/modifyEnabledCustomer.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyEnabledCustomer(String token,Long id,String status) {
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user = ShiroUtils.getUserByToken(token);
            CustomerInfoModel model=new CustomerInfoModel();
            model.setId(id);
            model.setStatus(status);
            // 设置商家id
         	model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
            //修改时间
            model.setModifyDate(TimeUtils.getNow());
            boolean b = customerService.modifyEnabledCustomer(model);
            if (!b) {
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
			}
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
        }
	}
	
	@ApiOperation(value = "获取币种 ")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "customerId", value = "客户id",required = true)
	})
	@PostMapping(value="/getCurrency.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> getCurrency(String customerId) {		
		if (StringUtils.isNotBlank(customerId)) {
			try {
				CustomerInfoDTO customer = customerService.searchDetailByRelId(Long.valueOf(customerId));
				String currency = "" ;
				
				if(customer!=null && StringUtils.isNotBlank(customer.getCurrency())) {
					currency = customer.getCurrency() ;
				}
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,currency);//成功
			
			}  catch (Exception e) {
	        	e.printStackTrace();
	        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
			}
		}
		
	   List<Map<String, Object>> list = new ArrayList<>();
       for (DerpBasic derpBasic : DERP.currencyCodeList) {
           Map<String, Object> map = new HashMap<String, Object>();
           map.put("selectValue", derpBasic.getKey());
           map.put("selectLable", derpBasic.getValue());
           list.add(map);
       }
       return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);//成功
	}
	
	/**
	 * 电商平台名称
	 * @param
	 * @return
	 */
	/*@RequestMapping("/getShopCode.asyn")
	@ResponseBody
	public ViewResponseBean getShopCode() {

		List<Map<String, Object>> list = new ArrayList<>() ;
	       for (DerpBasic basic : DERP.storePlatformList) {
	           Map<String, Object> map = new HashMap<String, Object>();
	           map.put("selectValue", basic.getKey());
	           map.put("selectLable", basic.getValue());
	           list.add(map);
	       }
		
		return ResponseFactory.success(list) ;
	}*/

	/**
	 * 获取下拉列表
	 * @return
	 */
	@ApiOperation(value = "获取下拉列表")
	@PostMapping(value="/getSelectBeanByDto.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getSelectBeanByDto(CustomerInfoDetailForm form) {
		List<CustomerInfoDTO> result = new ArrayList<CustomerInfoDTO>();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			CustomerInfoDTO dto = new CustomerInfoDTO();
			dto.setName(form.getName());
			dto.setCusType(form.getCusType());
			dto.setMerchantId(user.getMerchantId());
			result = customerService.getSelectBeanByDTO(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
			e.printStackTrace();
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
		}
	}
	/**
	 * 根据id获取客户信息
	 */
	@ApiOperation(value = "根据id获取客户信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id",required = true)
	})
	@PostMapping(value="/getCustomerById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean getCustomerById(Long id) {
		CustomerInfoDTO customerInfoDTO = null;
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			customerInfoDTO = customerService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,customerInfoDTO);//成功
		} catch (Exception e) {
			e.printStackTrace();
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
		}
	}
	/**
	 * 获取下拉列表
	 * @return
	 */
	@ApiOperation(value = "获取下拉列表")
	@PostMapping(value="/getCustomerByMerchantId.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getCustomerByMerchantId(CustomerInfoDetailForm form) {
		List<CustomerInfoDTO> result = new ArrayList<CustomerInfoDTO>();
		try {
			CustomerInfoDTO dto=new CustomerInfoDTO();
			dto.setMerchantId(form.getMerchantId());
			dto.setMainId(form.getMainId());
			dto.setCusType(form.getCusType());
			result = customerService.getCustomerByMerchantId(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
			e.printStackTrace();
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
		}
	}
	/**
	 * 根据主数据id和商家获取供应商信息
	 * @return
	 */
	@ApiOperation(value = "根据主数据id和商家获取供应商信息")
	@PostMapping(value="/getCustomerByMainMerchantId.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getCustomerByMainMerchantId(CustomerInfoDetailForm form) {
		CustomerInfoDTO result = new CustomerInfoDTO();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			CustomerInfoDTO dto=new CustomerInfoDTO();
			dto.setMerchantId(form.getMerchantId());
			dto.setMainId(form.getMainId());
			result = customerService.getCustomerByMainMerchantId(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
			e.printStackTrace();
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据客户供应商Id和商家获取对应税率")
	@PostMapping(value="/getRateByCustomerAndMerchant.asyn")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "customerId", value = "客户/供应商ID",required = true)
	})
	public ResponseBean<CustomerMerchantRelModel> getRateByCustomerAndMerchant(
			@RequestParam(value="token", required = true)String token, 
			@RequestParam(value="customerId", required = true)Long customerId) {
		try {
			
			User user = ShiroUtils.getUserByToken(token);
			
			CustomerMerchantRelModel model = customerMerchantRelService.getRateByCustomerAndMerchant(user.getMerchantId(), customerId) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, model);//成功
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

}
