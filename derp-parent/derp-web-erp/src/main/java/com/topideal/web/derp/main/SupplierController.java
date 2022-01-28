 package com.topideal.web.derp.main;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
import com.topideal.dao.main.CustomerBankMerchantRelDao;
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
import com.topideal.service.user.UserMerchantRelService;
import com.topideal.shiro.ShiroUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 供应商信息 控制层
 * @author zhanghx
 */
@RequestMapping("/supplier")
@Controller
public class SupplierController {
	
	/** 标题  */
	private static final String[] COLUMNS= {"供应商编码","供应商名称","数据来源","组织机构代码","是否内部公司","营业执照号","手机","状态","采购币种","创建时间"};
	private static final String[] KEYS= {"code","name","sourceLabel","orgCode","typeLabel","creditCode","legalTel","statusLabel","currencyLabel","createDate"};

	// 客户信息service
	@Autowired
	private CustomerService customerService;
	@Autowired
    private MerchantInfoService merchantInfoService;
	@Autowired
	private UserMerchantRelService userMerchantRelService;
	
	@Autowired
	private CustomerBankService customerBankService;

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
		
		return "/derp/main/supplier-list";
	}

	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, Long mainId) throws SQLException {
		User user = ShiroUtils.getUser(); 
		List<SelectBean> returnMerchantList = customerService.getMerchantList(user) ;
		model.addAttribute("merchantList", returnMerchantList);
		
		if(mainId != null) {
			CustomerInfoModel customer = customerService.getCustomerMainInfo(mainId) ;
			model.addAttribute("detail", customer);
		}
		
		return "/derp/main/supplier-add";
	}

	/**
	 * 访问详情页面
	 * */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception{
		User user = ShiroUtils.getUser() ;
		CustomerInfoDTO customerInfo = customerService.searchDetail(id);
		model.addAttribute("detail", customerInfo);
		CustomerAptitudeModel customerAptitude = customerService.searchAptitude(id);
		model.addAttribute("customerAptitude", customerAptitude);
		List<MerchantInfoModel> relList = customerService.getMerchantRelInfo(id) ;
		model.addAttribute("relList", relList);
		
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("customerId", id);
		paramMap.put("userId", user.getId());
		List<MerchantInfoDTO> relList1 = customerService.getMerchantRelInfoAndMerchantInfo(paramMap) ;
		
		model.addAttribute("relList1", relList1);
		
		CustomerBankDTO dto =new CustomerBankDTO();
		dto.setCustomerId(id);
		List<CustomerBankDTO> customerBankList = customerBankService.getCustomerBankList(dto) ;
		model.addAttribute("customerBankList", customerBankList);
		return "/derp/main/supplier-details";
	}

	/**
	 * 访问编辑页面
	 * */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id)throws Exception {
		CustomerInfoDTO customerInfo = customerService.searchDetail(id);
		model.addAttribute("detail", customerInfo);
		CustomerAptitudeModel customerAptitude = customerService.searchAptitude(id);
		model.addAttribute("customerAptitude", customerAptitude);
		User user = ShiroUtils.getUser() ;
		List<SelectBean> returnMerchantList = customerService.getMerchantList(user) ;
		model.addAttribute("merchantList", returnMerchantList);
		/*CustomerBankDTO bankDto=new CustomerBankDTO();
		List<CustomerBankDTO> customerBankList = customerBankService.getCustomerBankList(bankDto);
		model.addAttribute("customerBankList", customerBankList);*/
		return "/derp/main/supplier-edit";
	}
	
	/**
	 * 访问批量导入页面
	 * */
	@RequestMapping("/toImportPage.html")
	public String toImportPage() {
		return "/derp/main/supplier-import";
	}

	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listSupplier.asyn")
	@ResponseBody
	private ViewResponseBean listCustomer(CustomerInfoDTO dto) {
		try{
			// 查询商户类型为：2供应商
			dto.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
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
	@RequestMapping("/saveSupplier.asyn")
	@ResponseBody
	public ViewResponseBean saveCustomer(String json,CustomerInfoDTO model) {
		Map<String, Object> customerMap=null;
		try {
			JSONObject jsonData = JSONObject.fromObject(json);
			
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
					return ResponseFactory.error(StateCodeEnum.ESSAGE_10018);	
				}				
				bankMap.put(bankAccount, bankAccount);
			}
			
			// 客户商家信息
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
					return ResponseFactory.error(StateCodeEnum.MESSAGE_10013);	
				}
				// 合作公司不能是内部公司的商家
				if (model.getInnerMerchantId()!=null&&model.getInnerMerchantId().toString().equals(merchantId)) {
					return ResponseFactory.error(StateCodeEnum.MESSAGE_10017);	
				}
				map.put(merchantId, merchantName);
			}
			
			
			User user= ShiroUtils.getUser();
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
			// 存储商品信息
			model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);// 类型  1 客户  2 供应商',
			model.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);//状态(1使用中,0已禁用)
			if(StringUtils.isBlank(model.getSource())) {
				model.setSource(DERP_SYS.CUSTOMERINFO_SOURCE_2);
			}
			// 存储创建人
			model.setCreater(user.getId());
			customerMap = customerService.saveCustomer(model,arry, bankArry);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(customerMap);
	}

	/**
	 * 删除
	 * */
	@RequestMapping("/delSupplier.asyn")
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
	@RequestMapping("/modifySupplier.asyn")
	@ResponseBody
	public ViewResponseBean modifyCustomer(CustomerInfoDTO model,String json) {
		Map<String, Object> customerMap=null;
		try{
			JSONObject jsonData = JSONObject.fromObject(json);
			
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
					return ResponseFactory.error(StateCodeEnum.ESSAGE_10018);	
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
					return ResponseFactory.error(StateCodeEnum.MESSAGE_10013);	
				}
				// 合作公司不能是内部公司的商家
				if (model.getInnerMerchantId()!=null&&model.getInnerMerchantId().toString().equals(merchantId)) {
					return ResponseFactory.error(StateCodeEnum.MESSAGE_10017);	
				}
				map.put(merchantId, merchantName);
			}
			
			
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
         	model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
            //修改时间
            model.setModifyDate(TimeUtils.getNow());
            customerMap = customerService.modifyCustomer(model,arry,deleteIdsList,bankArry);

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
	@RequestMapping("/importSupplier.asyn")
	@ResponseBody
	public ViewResponseBean importSupplier(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
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
			resultMap = customerService.saveImportSupplier(data, user.getMerchantId(), user.getId(), user.getMerchantName());
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
	public ViewResponseBean getSelectBean() {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUser();
			result = customerService.getSelectBeanBySupplier(user.getMerchantId());
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
	 * 获取下拉列表
	 * @return
	 */
	@RequestMapping("/getSupplierByMerchantId.asyn")
	@ResponseBody
	public ViewResponseBean getSupplierByMerchantId(Long merchantId) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUser();
			result = customerService.getSelectBeanBySupplier(merchantId);
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
	 * 获取下拉列表
	 * @return
	 */
	@RequestMapping("/getAllSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getAllSelectBean() {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			result = customerService.getAllSupplierSelectBean();
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
	 * 导出供应商信息
	 * @param response
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/exportSupplier.asyn")
	public void exportRelation(HttpServletResponse response, HttpServletRequest request, CustomerInfoDTO model) throws Exception{
		// 查询商户类型为：1客户
		model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
		String sheetName = "供应商信息";
        List<CustomerInfoDTO> list = customerService.exportSupplierList(model);
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, list);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	/**
	 * 上传图片
	 * @param file
	 * @return
	 */
	@RequestMapping("/uploadFile.asyn")
	@ResponseBody
	public ViewResponseBean uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, Long id, String type) {
		String path = "";
		try{
            if(file==null){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            String fileName = file.getOriginalFilename();
            byte[] fileBytes = file.getBytes();
            Long fileSize = file.getSize();
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            User user = ShiroUtils.getUser();
			path = customerService.uploadFile(fileName,fileBytes,fileSize,ext,user.getId(),user.getName(), id,type);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(path);
	}
	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/getDetails.asyn")
	@ResponseBody
	public ViewResponseBean getSalePriceDetails(Long id) {
		//校验id是否正确
        boolean isRight = StrUtils.validateId(id);
        if(!isRight){
            //输入信息不完整
            return ResponseFactory.error(StateCodeEnum.ERROR_303);
        }
        CustomerInfoDTO customerInfo = new CustomerInfoDTO();
		try{
			customerInfo = customerService.searchDetail(id);
        }catch(Exception e){
        	e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(customerInfo);
	}
	
	
	/**
	 * 修改
	 * */
	@RequestMapping("/modifyEnabledSupplier.asyn")
	@ResponseBody
	public ViewResponseBean modifyEnabledSupplier(CustomerInfoModel model) {
		Map<String, Object> customerMap=null;
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            // 设置商家id
         	model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
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
        return ResponseFactory.success(customerMap);
	}
	
	@RequestMapping("/getMerchantInfo.asyn")
	@ResponseBody
	public ViewResponseBean getMerchantInfo(Long id) {
		User user= ShiroUtils.getUser();
		try{
			
			List<MerchantInfoModel> merchantInfoModels = null ;
			
			if (user.getUserType().equals(DERP_SYS.USERINFO_USERTYPE_1)) {
				merchantInfoModels = merchantInfoService.listMerchantInfo(new MerchantInfoModel()) ;
			}else {
				UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
				userMerchantRelModel.setUserId(user.getId());
				merchantInfoModels = merchantInfoService.getUserMerchantList(userMerchantRelModel);
			}
			
			return ResponseFactory.success(merchantInfoModels);

        }catch(Exception e){
        	e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
	}
	
	@RequestMapping("/getMerchantRel.asyn")
	@ResponseBody
	public ViewResponseBean getMerchantRel(String id) {
		try{
			
			List<CustomerMerchantRelModel> relList = customerService.getMerchantRel(id) ;
			ArrayList<DerpBasic> businessModelList = DERP_SYS.customerMerchantRel_businessModelList;
			ArrayList<DerpBasic> accountPeriodList = DERP_SYS.customerInfo_accountPeriodList;
			Map<String, Object> resultMap=new HashMap<>();
			resultMap.put("relList", relList);
			resultMap.put("businessModelList", businessModelList);
			resultMap.put("accountPeriodList", accountPeriodList);					
			return ResponseFactory.success(resultMap);

        }catch(Exception e){
        	e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
	}
	
	@SuppressWarnings({ "unchecked" })
	@RequestMapping("/saveMerchantRel.asyn")
	@ResponseBody
	public ViewResponseBean saveMerchantRel(String merchantIds, CustomerMerchantRelModel relModel) {
		try{
			
			List<Long> list = null ;
			
			if(StringUtils.isNotBlank(merchantIds)) {
				list = StrUtils.parseIds(merchantIds);
			}else {
				list = new ArrayList<Long>() ;
			}
			User user = ShiroUtils.getUser() ;
			customerService.saveMerchantRel(list, relModel,user) ;
			
			return ResponseFactory.success();

        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
	}
	/**
	 * 根据主数据获取某供应商关联的公司的下拉列表
	 * @return
	 */
	@RequestMapping("/getSupplierMerchantRelByMainIdURL.asyn")
	@ResponseBody
	public ViewResponseBean getSupplierMerchantRelByMainIdURL(CustomerInfoDTO dto) {
		List<CustomerMerchantRelModel> result = new ArrayList<CustomerMerchantRelModel>();
		try {
			result = customerService.getSupplierMerchantRelByMainIdURL(dto);
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
