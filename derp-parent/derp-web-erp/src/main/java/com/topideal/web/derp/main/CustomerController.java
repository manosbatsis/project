package com.topideal.web.derp.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.constant.DerpBasic;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
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
import com.topideal.service.user.UserMerchantRelService;
import com.topideal.shiro.ShiroUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 客户信息 控制层
 * @author zhanghx
 */
@RequestMapping("/customer")
@Controller
public class CustomerController {
	
	/** 标题  */
	private static final String[] COLUMNS= {"客户编码","客户名称","主数据ID","合作公司","数据来源","组织机构代码","是否内部公司","手机","业务员","状态","销售币种","渠道分类","创建时间"};
	private static final String[] KEYS= {"code","name","mainId","merchantNameStr","sourceLabel","orgCode","typeLabel","legalTel", "salesman" ,"statusLabel","currencyLabel","channelClassifyLabel","createDate"};

	// 客户信息service
	@Autowired
	private CustomerService customerService;

	// 客户公司中间表service
	@Autowired
	private CustomerMerchantRelService customerMerchantRelService;
	@Autowired
	private TariffRateConfigService tariffRateConfigService;
	@Autowired
	private UserMerchantRelService userMerchantRelService;
	

	
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
	@RequestMapping("/getCusOrSurpSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getCusOrSurpSelectBean(String typeFlag) {
		
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUser();
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
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}
	
	/**
	 * 合作公司保存(客户)
	 * @param json
	 * @param model
	 * @return
	 */
	@RequestMapping("/saveMerchantRelJSon.asyn")
	@ResponseBody
	public ViewResponseBean saveMerchantRelJSon(String json) {
		
		try{
			User user = ShiroUtils.getUser() ;
			JSONObject jsonData = JSONObject.fromObject(json);
			String customerId = jsonData.getString("customerId");
			if (jsonData==null||StringUtils.isBlank(customerId)) {
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
			JSONArray  jSONArray = (JSONArray) jsonData.get("itemList");
			customerService.saveMerchantRelJSon(jSONArray,customerId,user) ;
			
			return ResponseFactory.success();

        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
	}
	
	/**
	 * 访问列表页面
	 * @throws SQLException 
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws SQLException {
		User user = ShiroUtils.getUser() ;
		List<SelectBean> merchantList = customerService.getMerchantList(user) ;
		model.addAttribute("merchantList", merchantList);
		model.addAttribute("merchantId", user.getMerchantId());
		return "/derp/main/customer-list";
	}

	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, Long mainId) throws SQLException {
		User user = ShiroUtils.getUser();
		model.addAttribute("user",user);
		List<SelectBean> returnMerchantList = customerService.getMerchantList(user) ;
		model.addAttribute("merchantList", returnMerchantList);
		if(mainId != null) {
			CustomerInfoModel customer = customerService.getCustomerMainInfo(mainId) ;
			model.addAttribute("detail", customer);
		}
		
		return "/derp/main/customer-add";
	}
	

	
	/**
	 * 访问批量导入页面
	 * */
	@RequestMapping("/toImportPage.html")
	public String toImportPage() {
		return "/derp/main/customer-import";
	}

	/**
	 * 访问详情页面
	 * */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception{
		User user = ShiroUtils.getUser();
		CustomerInfoDTO customerInfo = customerService.searchDetail(id);
		model.addAttribute("detail", customerInfo);
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("customerId", id);
		paramMap.put("userId", user.getId());
		List<MerchantInfoDTO> relList = customerService.getMerchantRelInfoAndMerchantInfo(paramMap) ;

		
		model.addAttribute("relList", relList);
		return "/derp/main/customer-details";
	}

	/**
	 * 访问编辑页面
	 * */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id)throws Exception {
		User user = ShiroUtils.getUser();
		model.addAttribute("user",user);
		CustomerInfoDTO customerInfo = customerService.searchDetail(id);
		model.addAttribute("detail", customerInfo);
		List<SelectBean> returnMerchantList = customerService.getMerchantList(user) ;
		model.addAttribute("merchantList", returnMerchantList);
		CustomerMerchantRelModel customerMerchantRelModel = customerMerchantRelService.searchDetail(id,user);
		model.addAttribute("customerMerchantRelModel",customerMerchantRelModel);
		return "/derp/main/customer-edit";
	}

	@RequestMapping("/getCustomerMerchantRelList.asyn")
	@ResponseBody
	private ViewResponseBean getCustomerMerchantRelList(Long customerId) {
		try{
			// 查询商户类型为：1客户
			User user = ShiroUtils.getUser();
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
	@RequestMapping("/listCustomer.asyn")
	@ResponseBody
	private ViewResponseBean listCustomer(CustomerInfoDTO dto) {
		try{
			// 查询商户类型为：1客户
			dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
			// 响应结果集
			dto = customerService.listCustomer(dto);

		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 新增
	 * */
	@RequestMapping("/saveCustomer.asyn")
	@ResponseBody
	public ViewResponseBean saveCustomer(String json,CustomerInfoDTO model) {
		Map<String, Object> customerMap=null;
		try {
			JSONObject jsonData = JSONObject.fromObject(json);
			Object merchantListObj = jsonData.get("merchantList");
			JSONArray arry=new JSONArray();			
			if (merchantListObj!=null) {
				arry=(JSONArray) merchantListObj;
			}
			User user= ShiroUtils.getUser();
			Map<String, Object> map=new HashMap<>();
			for (Object object2 : arry) {
				JSONObject jSONObject =(JSONObject) object2;
				String merchantId = jSONObject.getString("merchantId");
				String merchantName = jSONObject.getString("merchantName");
				if (map.containsKey(merchantId)) {
					return ResponseFactory.error(StateCodeEnum.MESSAGE_10013);	
				}
				// 合作公司不能是内部公司的商家
				if (model.getInnerMerchantId()!=null&&model.getInnerMerchantId().toString().equals(merchantId)) {
					return ResponseFactory.error(StateCodeEnum.MESSAGE_10017);	
				}

				map.put(merchantId, merchantName);
			}
			
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getName())
					.addObject(model.getCreditCode())
					.addObject(model.getOrgCode())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			} 
			// 设置商家id
			model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);// 类型  1 客户  2 供应商',
			model.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);//状态(1使用中,0已禁用)
			if(StringUtils.isBlank(model.getSource())) {
				model.setSource(DERP_SYS.CUSTOMERINFO_SOURCE_2);
			}
			// 存储创建人
			model.setCreater(user.getId());
			customerMap = customerService.saveCustomer(model,arry,null);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(customerMap);
	}

	/**
	 * 删除
	 * */
	@RequestMapping("/delCustomer.asyn")
	@ResponseBody
	public ViewResponseBean delCustomer(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = customerService.delCustomer(list);
            if(!b){
                return ResponseFactory.error(StateCodeEnum.ERROR_301);
            }
        }catch(Exception e){
        	e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}

	/**
	 * 修改
	 * */
	@RequestMapping("/modifyCustomer.asyn")
	@ResponseBody
	public ViewResponseBean modifyCustomer(String json,CustomerInfoDTO dto) {
		Map<String, Object> customerMap=null;
		try{
			
			JSONObject jsonData = JSONObject.fromObject(json);
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
					return ResponseFactory.error(StateCodeEnum.MESSAGE_10013);	
				}
				// 合作公司不能是内部公司的商家
				if (dto.getInnerMerchantId()!=null&&dto.getInnerMerchantId().toString().equals(merchantId)) {
					return ResponseFactory.error(StateCodeEnum.MESSAGE_10017);	
				}
				map.put(merchantId, merchantName);
			}
			
			//校验id是否正确
            boolean isRight = StrUtils.validateId(dto.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            // 设置商家id
			dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
            //修改时间
			dto.setModifyDate(TimeUtils.getNow());
            customerMap = customerService.modifyCustomer(dto,arry,deleteIdsList,null);
        }catch(Exception e){
        	e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(customerMap);
	}
	
	/**
	 * 导入
	 * @param 
	 * @return int
	 * @author zhanghx
	 * @throws IOException 
	 */
	@RequestMapping("/importCustomer.asyn")
	@ResponseBody
	public ViewResponseBean importCustomer(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
		Map resultMap = new HashMap();//返回的结果集
		try{
			if(file==null){
				//输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 1);
			if(data == null){//数据为空
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
			User user= ShiroUtils.getUser();
			resultMap = customerService.saveImportCustomer(data, user.getMerchantId(), user.getId(), user.getMerchantName());
        }catch(Exception e){
			e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
		return ResponseFactory.success(resultMap);
	}
	
	/**
	 * 获取下拉列表
	 * @return
	 */
	@RequestMapping("/getSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBean(String type) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUser();
			CustomerInfoDTO dto = new CustomerInfoDTO();
			if(StringUtils.isNotBlank(type)){
				dto.setType(type);// 客户类型(1内部,2外部)
			}else{
				dto.setMerchantId(user.getMerchantId());
			}
			result = customerService.getSelectBeanByMerchantId(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	/**
	 * 导出客户信息
	 * @param response
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/exportCustomer.asyn")
	public void exportRelation(HttpServletResponse response, HttpServletRequest request, CustomerInfoDTO model) throws Exception{
		User user= ShiroUtils.getUser();
		// 查询商户类型为：1客户
		model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
		String sheetName = "客户信息";
		List<CustomerInfoDTO> list = customerService.exportList(model);
		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , list);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
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
	@RequestMapping("/modifyEnabledCustomer.asyn")
	@ResponseBody
	public ViewResponseBean modifyEnabledCustomer(CustomerInfoModel model) {
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            User user= ShiroUtils.getUser();
            // 设置商家id
         	model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
            //修改时间
            model.setModifyDate(TimeUtils.getNow());
            boolean b = customerService.modifyEnabledCustomer(model);
            if (!b) {
            	return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
        }catch(Exception e){
        	e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}
	
	@RequestMapping("/getCurrency.asyn")
	@ResponseBody
	public ViewResponseBean getCurrency(String customerId) {
		
		if (StringUtils.isNotBlank(customerId)) {
			try {
				CustomerInfoDTO customer = customerService.searchDetailByRelId(Long.valueOf(customerId));
				String currency = "" ;
				
				if(customer!=null && StringUtils.isNotBlank(customer.getCurrency())) {
					currency = customer.getCurrency() ;
				}
				
				return ResponseFactory.success(currency) ;
			
			}  catch (Exception e) {
				return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
			}
		}
		
		List<Map<String, Object>> list = new ArrayList<>() ;

       for (DerpBasic derpBasic : DERP.currencyCodeList) {
           Map<String, Object> map = new HashMap<String, Object>();
           map.put("selectValue", derpBasic.getKey());
           map.put("selectLable", derpBasic.getValue());
           list.add(map);
       }
		
		return ResponseFactory.success(list) ;
	}
	
	/**
	 * 电商平台名称
	 * @param
	 * @return
	 */
	@RequestMapping("/getShopCode.asyn")
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
	}

	/**
	 * 获取下拉列表
	 * @return
	 */
	@RequestMapping("/getSelectBeanByDto.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBeanByDto(CustomerInfoDTO dto) {
		List<CustomerInfoDTO> result = new ArrayList<CustomerInfoDTO>();
		try {
			User user = ShiroUtils.getUser();
			dto.setMerchantId(user.getMerchantId());
			result = customerService.getSelectBeanByDTO(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}
	/**
	 * 根据id获取客户信息
	 */
	@RequestMapping("/getCustomerById.asyn")
	@ResponseBody
	private ViewResponseBean getCustomerById(Long id) {
		CustomerInfoDTO customerInfoDTO = null;
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			customerInfoDTO = customerService.searchDetail(id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(customerInfoDTO);
	}
	/**
	 * 获取下拉列表
	 * @return
	 */
	@RequestMapping("/getCustomerByMerchantId.asyn")
	@ResponseBody
	public ViewResponseBean getCustomerByMerchantId(CustomerInfoDTO dto) {
		List<CustomerInfoDTO> result = new ArrayList<CustomerInfoDTO>();
		try {
			result = customerService.getCustomerByMerchantId(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}
	/**
	 * 根据主数据id和商家获取供应商信息
	 * @return
	 */
	@RequestMapping("/getCustomerByMainMerchantId.asyn")
	@ResponseBody
	public ViewResponseBean getCustomerByMainMerchantId(CustomerInfoDTO dto) {
		CustomerInfoDTO result = new CustomerInfoDTO();
		try {
			User user = ShiroUtils.getUser();
			result = customerService.getCustomerByMainMerchantId(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}



}
