package com.topideal.webapi.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.constant.DerpBasic;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.main.CustomerBankDTO;
import com.topideal.entity.dto.main.CustomerInfoDTO;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.CustomerAptitudeModel;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.service.main.CustomerBankService;
import com.topideal.service.main.CustomerService;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.CustomerInfoForm;
import com.topideal.webapi.form.SupplierCustomerInfoAddForm;
import com.topideal.webapi.form.SupplierToDetailsResponseDTO;
import com.topideal.webapi.form.SupplierToEditResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 供应商信息 控制层
 * @author zhanghx
 */
@RestController
@RequestMapping("/webapi/system/supplier")
@Api(tags = "供应商信息 ")
public class APISupplierController {
	
	/** 标题  */
	private static final String[] COLUMNS= {"供应商编码","供应商名称","数据来源","组织机构代码","是否内部公司","营业执照号","手机","状态","采购币种","创建时间"};
	private static final String[] KEYS= {"code","name","sourceLabel","orgCode","typeLabel","creditCode","legalTel","statusLabel","currencyLabel","createDate"};

	// 客户信息service
	@Autowired
	private CustomerService customerService;
	@Autowired
    private MerchantInfoService merchantInfoService;
	@Autowired
	private CustomerBankService customerBankService;

	/**
	 * 访问列表页面
	 * */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toPage(String token) {
		try {
			User user = ShiroUtils.getUserByToken(token);
			Map<String, Object> resultMap=new HashMap<String, Object>();
			List<SelectBean> merchantList = customerService.getMerchantList(user) ;
			resultMap.put("merchantList", merchantList);
			
			resultMap.put("merchantId", user.getMerchantId());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */

	@ApiOperation(value = "访问新增页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "mainId", value = "待引入客商id", required = true),
	})
	@PostMapping(value="/toAddPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toAddPage(Long mainId,String token) throws SQLException {
		try {
			User user = ShiroUtils.getUserByToken(token);
			Map<String, Object> resultMap=new HashMap<String, Object>();
			List<SelectBean> returnMerchantList = customerService.getMerchantList(user) ;
			resultMap.put("merchantList", returnMerchantList);		
			if(mainId != null) {
				CustomerInfoModel customer = customerService.getCustomerMainInfo(mainId) ;
				resultMap.put("detail", customer);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SupplierToDetailsResponseDTO> toDetailsPage(Long id,String token)throws Exception{		
		try {
			SupplierToDetailsResponseDTO result=new SupplierToDetailsResponseDTO();
			
			User user = ShiroUtils.getUserByToken(token);
			CustomerInfoDTO customerInfo = customerService.searchDetail(id);
			result.setDetail(customerInfo);
			CustomerAptitudeModel customerAptitude = customerService.searchAptitude(id);
			result.setCustomerAptitude(customerAptitude);
			/*List<MerchantInfoModel> relList = customerService.getMerchantRelInfo(id) ;
			resultMap.put("relList", relList);*/
			
			Map<String, Object> paramMap=new HashMap<>();
			paramMap.put("customerId", id);
			paramMap.put("userId", user.getId());
			List<MerchantInfoDTO> relList1 = customerService.getMerchantRelInfoAndMerchantInfo(paramMap) ;
			result.setRelList1(relList1);
			CustomerBankDTO dto =new CustomerBankDTO();
			dto.setCustomerId(id);
			List<CustomerBankDTO> customerBankList = customerBankService.getCustomerBankList(dto) ;
			result.setCustomerBankList(customerBankList);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 访问编辑页面
	 * */
	@ApiOperation(value = "访问编辑页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SupplierToEditResponseDTO> toEditPage(Long id,String token)throws Exception {
		try {
			User user = ShiroUtils.getUserByToken(token);
			SupplierToEditResponseDTO result=new SupplierToEditResponseDTO();
			CustomerInfoDTO customerInfo = customerService.searchDetail(id);			
			result.setDetail(customerInfo);
			CustomerAptitudeModel customerAptitude = customerService.searchAptitude(id);
			result.setCustomerAptitude(customerAptitude);			
			List<SelectBean> returnMerchantList = customerService.getMerchantList(user) ;
			result.setMerchantList(returnMerchantList);
			CustomerBankDTO dto =new CustomerBankDTO();
			dto.setCustomerId(id);
			List<CustomerBankDTO> customerBankList = customerBankService.getCustomerBankList(dto) ;
			result.setCustomerBankList(customerBankList);
			
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
		return "/derp/main/supplier-import";
	}*/

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = " 获取分页数据")
	@PostMapping(value="/listSupplier.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listCustomer(CustomerInfoForm form) {
		try{
			CustomerInfoDTO dto=new CustomerInfoDTO();
			dto.setCode(form.getCode());
			dto.setName(form.getName());
			dto.setStatus(form.getStatus());
			dto.setMainId(form.getMainId());
			dto.setMerchantId(form.getMerchantId());
			dto.setSource(form.getSource());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			dto.setId(form.getId());
			// 查询商户类型为：2供应商
			dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
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
	@ApiOperation(value = "新增")
	@PostMapping(value="/saveSupplier.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveCustomer(SupplierCustomerInfoAddForm form) {
		Map<String, Object> customerMap=null;
		try {
			JSONObject jsonData = JSONObject.fromObject(form.getJson());
			
			// 客户银行信息
			Object itemBankListObj = jsonData.get("itemBankList");
			JSONArray bankArry=new JSONArray();		
			if (itemBankListObj!=null) {
				bankArry=(JSONArray) itemBankListObj;
			}
			Map<String, Object> bankMap=new HashMap<>();
			for (Object object2 : bankArry) {
				JSONObject jSONObject =(JSONObject) object2;
				String bankAccount = jSONObject.getString("bankAccount");
				bankAccount=bankAccount.trim();
				if (bankMap.containsKey(bankAccount)) {
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"银行账户不能重复");
				}				
				bankMap.put(bankAccount, bankAccount);
			}			
			
			//客户商家信息
			Object merchantListObj = jsonData.get("merchantList");
			JSONArray arry=new JSONArray();			
			if (merchantListObj!=null) {
				arry=(JSONArray) merchantListObj;
			}
			Map<String, Object> map=new HashMap<>();
			for (Object object2 : arry) {
				JSONObject jSONObject =(JSONObject) object2;
				String merchantId = jSONObject.getString("merchantId");
				String merchantName = jSONObject.getString("merchantName");
				if (map.containsKey(merchantId)) {
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"合作商家传值重复");

				}
				// 合作公司不能是内部公司的商家
				if (form.getInnerMerchantId()!=null&&form.getInnerMerchantId().toString().equals(merchantId)) {
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"合作公司不能是内部公司的商家");
				}
				map.put(merchantId, merchantName);
			}
			
			
			User user = ShiroUtils.getUserByToken(form.getToken());
			CustomerInfoDTO model=new CustomerInfoDTO();
			model.setSource(form.getSource());
			model.setSimpleName(form.getSimpleName());
			model.setName(form.getName());
			model.setCreditCode(form.getCreditCode());
			model.setOrgCode(form.getOrgCode());
			model.setType(form.getType());
			model.setCodeGrade(form.getCodeGrade());
			model.setNature(form.getNature());
			model.setChinaName(form.getChinaName());
			model.setEnSimpleName(form.getEnSimpleName());
			model.setEnName(form.getEnName());
			model.setMainId(form.getMainId());
			model.setSalesman(form.getSalesman());
			model.setCityCode(form.getCityCode());
			model.setCompanyAddr(form.getCompanyAddr());
			model.setBusinessAddress(form.getBusinessAddress());
			model.setEnBusinessAddress(form.getEnBusinessAddress());
			model.setCurrency(form.getCurrency());
			model.setInnerMerchantId(form.getInnerMerchantId());
			model.setOperationScope(form.getOperationScope());
			model.setFax(form.getFax());
			model.setEmail(form.getEmail());
			model.setRemark(form.getRemark());
			model.setDepositBank(form.getDepositBank());
			model.setBankAccount(form.getBankAccount());
			model.setBeneficiaryName(form.getBeneficiaryName());
			model.setBankAddress(form.getBankAddress());
			model.setSwiftCode(form.getSwiftCode());
			model.setLegalPerson(form.getLegalPerson());
			model.setLegalNationality(form.getLegalNationality());
			model.setLegalCardNo(form.getLegalCardNo());
			model.setLegalTel(form.getLegalTel());
			model.setoTelNo(form.getoTelNo());
		
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getName())
					.addObject(model.getCreditCode())
					.addObject(model.getOrgCode())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			// 存储商品信息
			model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);// 类型  1 客户  2 供应商',
			model.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);//状态(1使用中,0已禁用)
			if(StringUtils.isBlank(model.getSource())) {
				model.setSource(DERP_SYS.CUSTOMERINFO_SOURCE_2);
			}
			// 存储创建人
			model.setCreater(user.getId());
			customerMap = customerService.saveCustomer(model,arry,bankArry);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
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
			@ApiImplicitParam(name = "ids", value = "id集合多个用英文逗号隔开", required = true)
	})
	@PostMapping(value="/delSupplier.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}

	/**
	 * 修改
	 * */
	@ApiOperation(value = "修改")
	@PostMapping(value="/modifySupplier.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyCustomer(SupplierCustomerInfoAddForm form) {
		Map<String, Object> customerMap=null;
		try{
			JSONObject jsonData = JSONObject.fromObject(form.getJson());
			// 客户银行信息
			Object itemBankListObj = jsonData.get("itemBankList");
			JSONArray bankArry=new JSONArray();		
			if (itemBankListObj!=null) {
				bankArry=(JSONArray) itemBankListObj;
			}
			Map<String, Object> bankMap=new HashMap<>();
			for (Object object2 : bankArry) {
				JSONObject jSONObject =(JSONObject) object2;
				String bankAccount = jSONObject.getString("bankAccount");
				bankAccount=bankAccount.trim();
				if (bankMap.containsKey(bankAccount)) {
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"银行账户不能重复");	
				}				
				bankMap.put(bankAccount, bankAccount);
			}			
			
			
			Object object = jsonData.get("merchantList");
			Object deleteIdsObj = jsonData.get("deleteIds");
			List<Object> deleteIdsList=new ArrayList<>();
			if (deleteIdsObj==null || StringUtils.isBlank(deleteIdsObj.toString())) {
			}else {
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
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"合作商家传值重复");
				}
				// 合作公司不能是内部公司的商家
				if (form.getInnerMerchantId()!=null&&form.getInnerMerchantId().toString().equals(merchantId)) {
					return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"合作公司不能是内部公司的商家");
				}
				map.put(merchantId, merchantName);
			}
			
			
			CustomerInfoDTO model=new CustomerInfoDTO();
			model.setId(form.getId());
			model.setSource(form.getSource());
			model.setSimpleName(form.getSimpleName());
			model.setName(form.getName());
			model.setCreditCode(form.getCreditCode());
			model.setOrgCode(form.getOrgCode());
			model.setType(form.getType());
			model.setCodeGrade(form.getCodeGrade());
			model.setNature(form.getNature());
			model.setChinaName(form.getChinaName());
			model.setEnSimpleName(form.getEnSimpleName());
			model.setEnName(form.getEnName());
			model.setMainId(form.getMainId());
			model.setSalesman(form.getSalesman());
			model.setCityCode(form.getCityCode());
			model.setCompanyAddr(form.getCompanyAddr());
			model.setBusinessAddress(form.getBusinessAddress());
			model.setEnBusinessAddress(form.getEnBusinessAddress());
			model.setCurrency(form.getCurrency());
			model.setInnerMerchantId(form.getInnerMerchantId());
			model.setOperationScope(form.getOperationScope());
			model.setFax(form.getFax());
			model.setEmail(form.getEmail());
			model.setRemark(form.getRemark());
			model.setDepositBank(form.getDepositBank());
			model.setBankAccount(form.getBankAccount());
			model.setBeneficiaryName(form.getBeneficiaryName());
			model.setBankAddress(form.getBankAddress());
			model.setSwiftCode(form.getSwiftCode());
			model.setLegalPerson(form.getLegalPerson());
			model.setLegalNationality(form.getLegalNationality());
			model.setLegalCardNo(form.getLegalCardNo());
			model.setLegalTel(form.getLegalTel());
			model.setoTelNo(form.getoTelNo());
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
         	model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
            //修改时间
            model.setModifyDate(TimeUtils.getNow());
            customerMap = customerService.modifyCustomer(model,arry,deleteIdsList,bankArry);
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
	@ApiOperation(value = " 导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importSupplier.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean importSupplier(String token,@RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
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
			resultMap = customerService.saveImportSupplier(data, user.getMerchantId(), user.getId(), user.getMerchantName());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
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
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getSelectBean(String token) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			result = customerService.getSelectBeanBySupplier(user.getMerchantId());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
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
			@ApiImplicitParam(name = "merchantId", value = "商家id", required = true)
	})
	@PostMapping(value="/getSupplierByMerchantId.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getSupplierByMerchantId(String token,Long merchantId) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			result = customerService.getSelectBeanBySupplier(merchantId);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
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
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getAllSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getAllSelectBean() {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			result = customerService.getAllSupplierSelectBean();
		} catch (Exception e) {
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
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
	public void exportRelation(HttpServletResponse response, HttpServletRequest request,CustomerInfoForm form) throws Exception{
		try {
			// 查询商户类型为：1客户
			CustomerInfoDTO dto=new CustomerInfoDTO();
			dto.setCode(form.getCode());
			dto.setName(form.getName());
			dto.setStatus(form.getStatus());
			dto.setMainId(form.getMainId());
			dto.setMerchantId(form.getMerchantId());
			dto.setSource(form.getSource());
			dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
			String sheetName = "供应商信息";
	        List<CustomerInfoDTO> list = customerService.exportSupplierList(dto);
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
	/**
	 * 上传图片
	 * @param file
	 * @return
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "id", required = true),
		@ApiImplicitParam(name = "type", value = "type", required = true)
	})
	@PostMapping(value="/uploadFile.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean uploadFile(String token,@RequestParam(value = "file", required = false) MultipartFile file, Long id, String type) {
		String path = "";
		try{
            if(file==null){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            String fileName = file.getOriginalFilename();
            byte[] fileBytes = file.getBytes();
            Long fileSize = file.getSize();
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            User user = ShiroUtils.getUserByToken(token);
			path = customerService.uploadFile(fileName,fileBytes,fileSize,ext,user.getId(),user.getName(), id,type);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,path);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据id获取详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/getDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<CustomerInfoDTO> getSalePriceDetails(Long id) {
		//校验id是否正确
        boolean isRight = StrUtils.validateId(id);
        if(!isRight){
            //输入信息不完整
        	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        }
        CustomerInfoDTO customerInfo = new CustomerInfoDTO();
		try{
			customerInfo = customerService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,customerInfo);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	
	/**
	 * 修改
	 * */
	@ApiOperation(value = "启用/禁用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "status", value = "status", required = true)
	})
	@PostMapping(value="/modifyEnabledSupplier.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyEnabledSupplier(Long id,String status) {
		Map<String, Object> customerMap=null;
		try{
			CustomerInfoModel model=new CustomerInfoModel();
			model.setId(id);
			model.setStatus(status);
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            // 设置商家id
         	model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
            //修改时间
            model.setModifyDate(TimeUtils.getNow());
            boolean b = customerService.modifyEnabledCustomer(model);
            if (!b) {
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	

	@ApiOperation(value = "获取商家或者用户")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getMerchantInfo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getMerchantInfo(String token) {		
		try{
			User user = ShiroUtils.getUserByToken(token);
			List<MerchantInfoModel> merchantInfoModels = null ;			
			if (user.getUserType().equals(DERP_SYS.USERINFO_USERTYPE_1)) {
				merchantInfoModels = merchantInfoService.listMerchantInfo(new MerchantInfoModel()) ;
			}else {
				UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
				userMerchantRelModel.setUserId(user.getId());
				merchantInfoModels = merchantInfoService.getUserMerchantList(userMerchantRelModel);
			}			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,merchantInfoModels);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	@ApiOperation(value = "获取商家客户关系数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "客户id", required = true)
	})
	@PostMapping(value="/getMerchantRel.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getMerchantRel(String id) {
		try{
			
			List<CustomerMerchantRelModel> relList = customerService.getMerchantRel(id) ;
			ArrayList<DerpBasic> businessModelList = DERP_SYS.customerMerchantRel_businessModelList;
			ArrayList<DerpBasic> accountPeriodList = DERP_SYS.customerInfo_accountPeriodList;
			Map<String, Object> resultMap=new HashMap<>();
			resultMap.put("relList", relList);
			resultMap.put("businessModelList", businessModelList);
			resultMap.put("accountPeriodList", accountPeriodList);						
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功

        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	@ApiOperation(value = "保存商家供应商关系数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "客户id", required = true)
	})
	@PostMapping(value="/saveMerchantRel.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveMerchantRel(String merchantIds,Long customerId,String token) {
		try{
			CustomerMerchantRelModel relModel=new CustomerMerchantRelModel();
			relModel.setCustomerId(customerId);
			List<Long> list = null ;
			
			if(StringUtils.isNotBlank(merchantIds)) {
				list = StrUtils.parseIds(merchantIds);
			}else {
				list = new ArrayList<Long>() ;
			}
			User user = ShiroUtils.getUserByToken(token);
			customerService.saveMerchantRel(list, relModel,user) ;			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	/**
	 * 根据主数据获取某供应商关联的公司的下拉列表
	 * @return
	 */
	@ApiOperation(value = "根据主数据获取供应商关联的公司的下拉")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "mainId", value = "主数据客户id", required = true)
	})
	@PostMapping(value="/getSupplierMerchantRelByMainIdURL.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<CustomerMerchantRelModel>> getSupplierMerchantRelByMainIdURL(String token,String mainId) {
		List<CustomerMerchantRelModel> result = new ArrayList<CustomerMerchantRelModel>();
		try {
			CustomerInfoDTO dto=new CustomerInfoDTO();
			dto.setMainId(mainId);
			result = customerService.getSupplierMerchantRelByMainIdURL(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
}
