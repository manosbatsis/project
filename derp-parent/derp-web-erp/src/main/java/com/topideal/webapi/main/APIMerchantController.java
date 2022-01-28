package com.topideal.webapi.main;


import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.constant.DerpBasic;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.MerchantBuRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantRelModel;
import com.topideal.entity.vo.main.TariffRateConfigModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.service.base.TariffRateConfigService;
import com.topideal.service.main.DepotMerchantRelService;
import com.topideal.service.main.MerchantBuRelService;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.service.main.MerchantRelService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.MerchantInfoAddDepotForm;
import com.topideal.webapi.form.MerchantInfoAddForm;
import com.topideal.webapi.form.MerchantInfoResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 公司管理 控制层
 */
@RestController
@RequestMapping("/webapi/system/merchant")
@Api(tags = "公司管理")
public class APIMerchantController {

	// 公司信息
	@Autowired
	private MerchantInfoService merchantInfoService;
	// 子公司
	@Autowired
	private MerchantRelService merchantRelService;
	@Autowired
	private DepotMerchantRelService depotMerchantRelService;
	@Autowired
	private MerchantBuRelService merchantBuRelService ;
	@Autowired
	private TariffRateConfigService tariffRateConfigService;
	/**
	 * 访问列表页面
	 */
	/*@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/main/merchant-list";
	}*/

	/**
	 * 访问新增页面
	 */
	/*@RequestMapping("/toAddPage.html")
	public String toAddPage() throws Exception {
		return "/derp/main/merchant-add";
	}*/

	/**
	 * 访问编辑页面
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
	public ResponseBean toEditPage(Long id) throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			//公司明细
			MerchantInfoDTO merchant = merchantInfoService.searchDetail(id);
			resultMap.put("detail", merchant);
			//代理公司
			MerchantRelModel merchantRel = new MerchantRelModel();
			merchantRel.setMerchantId(id);
			List<MerchantRelModel> list = merchantRelService.list(merchantRel);
			resultMap.put("list", list);
			//仓库关联关系
			List<DepotMerchantRelDTO> depotMerchantRelList = depotMerchantRelService.getListByMerchantId(id);
			resultMap.put("depotMerchantRelList", depotMerchantRelList);
			//公司事业部
			MerchantBuRelModel merchantBuRelModel = new MerchantBuRelModel() ;
			merchantBuRelModel.setMerchantId(id);
			List<SelectBean> merchantBuRelList = merchantBuRelService.getSelectBean(merchantBuRelModel);
			resultMap.put("merchantBuRelList", merchantBuRelList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 访问详情页面
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
	public ResponseBean toDetailsPage(Long id) throws Exception {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			//公司明细
			MerchantInfoDTO merchant = merchantInfoService.searchDetail(id);
			resultMap.put("detail", merchant);
			//代理公司
			MerchantRelModel merchantRel = new MerchantRelModel();
			merchantRel.setMerchantId(id);
			List<MerchantRelModel> list = merchantRelService.list(merchantRel);
			resultMap.put("list", list);
			//仓库关联关系
			List<DepotMerchantRelDTO> depotMerchantRelList = depotMerchantRelService.getListByMerchantId(id);
			resultMap.put("depotMerchantRelList", depotMerchantRelList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 获取分页数据
	 * @return
	 */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "code", value = "公司编码"),
			@ApiImplicitParam(name = "name", value = "公司简称"),
			@ApiImplicitParam(name = "topidealCode", value = "卓志编码")
	})
	@PostMapping(value="/listMerchant.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<MerchantInfoDTO> listMerchant(String code,String name,
			String topidealCode,int begin,int pageSize) {
		try{
			// 响应结果集
			MerchantInfoDTO dto=new MerchantInfoDTO();
			dto.setCode(code);
			dto.setName(name);
			dto.setTopidealCode(topidealCode);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto = merchantInfoService.listMerchantInfoPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
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
			@ApiImplicitParam(name = "id", value = "id"),
			@ApiImplicitParam(name = "isProxy", value = "isProxy")
	})
	@PostMapping(value="/getSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getSelectBean(Long id,String isProxy) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			MerchantInfoModel model=new MerchantInfoModel();
			model.setId(id);
			model.setIsProxy(isProxy);
			result = merchantInfoService.getSelectBean(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
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
	@PostMapping(value="/saveMerchant.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean saveMerchant(@RequestBody MerchantInfoAddForm form) {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			MerchantInfoModel model=new MerchantInfoModel();
			//model.setId(form.getId());
			model.setTopidealCode(form.getTopidealCode());
			model.setName(form.getName());
			model.setRemark(form.getRemark());
			model.setTel(form.getTel());
			model.setEmail(form.getEmail());
			model.setIsProxy(form.getIsProxy());
			model.setPermission(form.getPermission());
			model.setInventoryResultEmail(form.getInventoryResultEmail());
			model.setFinancePayEmail(form.getFinancePayEmail());
			model.setRegisteredAddress(form.getRegisteredAddress());
			model.setFullName(form.getFullName());
			model.setArticulationPercent(form.getArticulationPercent());
			model.setYname(form.getYname());
			model.setEnglishName(form.getEnglishName());
			model.setAccountCurrency(form.getAccountCurrency());
			model.setAccountPrice(form.getAccountPrice());
			model.setDepositBank(form.getDepositBank());
			model.setBankAccount(form.getBankAccount());
			model.setBeneficiaryName(form.getBeneficiaryName());
			model.setBankAddress(form.getBankAddress());
			model.setSwiftCode(form.getSwiftCode());
			model.setRegisteredType(form.getRegisteredType());
			model.setEnglishRegisteredAddress(form.getEnglishRegisteredAddress());
			model.setRegistrationCert(form.getRegistrationCert());
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getName())
					.addObject(model.getTopidealCode())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			// 公司编码
			String code = TimeUtils.format(new Date(), "ddHHmmsssss");
			model.setCode("ERP" + code);
			// 存储创建人
			model.setCreater(user.getId());
			List<Long> merchantIdList = form.getMerchantIdList();
			List<MerchantInfoAddDepotForm> merchantDepotList = form.getMerchantDepotList();
			boolean b = merchantInfoService.saveMerchantInfo(model, merchantIdList, merchantDepotList);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 修改  公司信息
	 * @return
	 */
	@ApiOperation(value = "修改")
	@PostMapping(value="/modifyMerchant.asyn",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean modifyMerchant(@RequestBody MerchantInfoAddForm form) {
		try {
			MerchantInfoModel model=new MerchantInfoModel();
			model.setId(form.getId());
			model.setTopidealCode(form.getTopidealCode());
			model.setName(form.getName());
			model.setRemark(form.getRemark());
			model.setTel(form.getTel());
			model.setEmail(form.getEmail());
			model.setIsProxy(form.getIsProxy());
			model.setPermission(form.getPermission());
			model.setInventoryResultEmail(form.getInventoryResultEmail());
			model.setFinancePayEmail(form.getFinancePayEmail());
			model.setRegisteredAddress(form.getRegisteredAddress());
			model.setFullName(form.getFullName());
			model.setArticulationPercent(form.getArticulationPercent());
			model.setYname(form.getYname());
			model.setEnglishName(form.getEnglishName());
			model.setAccountCurrency(form.getAccountCurrency());
			model.setAccountPrice(form.getAccountPrice());
			model.setDepositBank(form.getDepositBank());
			model.setBankAccount(form.getBankAccount());
			model.setBeneficiaryName(form.getBeneficiaryName());
			model.setBankAddress(form.getBankAddress());
			model.setSwiftCode(form.getSwiftCode());
			model.setEnglishRegisteredAddress(form.getEnglishRegisteredAddress());
			model.setRegisteredType(form.getRegisteredType());
			model.setRegistrationCert(form.getRegistrationCert());
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getName())
					.addObject(model.getTopidealCode())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			List<Long> merchantIdList = form.getMerchantIdList();
			List<MerchantInfoAddDepotForm> merchantDepotList = form.getMerchantDepotList();
			boolean b = merchantInfoService.modifyMerchantInfo(model, merchantIdList, merchantDepotList);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 *禁用启用公司信息
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "禁用启用公司信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "商家id",required = true),
			@ApiImplicitParam(name = "status", value = "状态(1启用,0禁用",required = true)
	})
	@PostMapping(value="/isOrNotEnable.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean isOrNotEnable(Long id,String status) {
		try{
            //校验id是否正确
            if(id==null||StringUtils.isBlank(status)){
                //输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            boolean b = merchantInfoService.isOrNotEnable(id,status);
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
	 * 根据ID获取商品详情
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取商品详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id")
	})
	@PostMapping(value="/getMerchantInfoDetails.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getMerchandiseDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		MerchantInfoDTO model = new MerchantInfoDTO();
		try {
			model = merchantInfoService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	
	/**
	 * 获取关联公司id
	 * @return
	 */
	@ApiOperation(value = "获取关联公司id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantId", value = "商家id",required = true)
	})
	@PostMapping(value="/getRelMerchantIds.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getRelMerchantIds(Long merchantId) {
		List<MerchantRelModel>  merchantRelModelList=null;
		try {
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(merchantId).empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			MerchantRelModel  model=new MerchantRelModel();
			model.setMerchantId(merchantId);
			merchantRelModelList=merchantRelService.getRelMerchantIds(model);
			if(merchantRelModelList!=null&&merchantRelModelList.size()>0){
				StringBuffer str=new  StringBuffer();
				for(MerchantRelModel merchantRelModel:merchantRelModelList){
					str.append(merchantRelModel.getProxyMerchantId()).append(",");
				}
				str.deleteCharAt(str.length()-1);
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,str.toString());//成功
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	
	/**
	 * 获取当前公司的代理公司及自己下拉列表
	 * @return
	 */
	@ApiOperation(value = "获取当前公司的代理公司及自己下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getMerchantSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getMerchantSelectBean(String token) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			MerchantInfoModel model = new MerchantInfoModel();
			if(!"1".equals(user.getUserType())){
				model.setId(user.getMerchantId());
			}
			result = merchantInfoService.getSelectBeanById(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}


	/**
	 * 获取当前用户需绑定的公司下拉列表
	 * 1.后台管理员创建普通用户：查询所有公司
	 * 2.普通用户创建普通用户：查询该普通用户下绑定的公司
	 * @return
	 */
	@ApiOperation(value = "获取当前用户需绑定的公司下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id"),
			@ApiImplicitParam(name = "isProxy", value = "isProxy")
	})
	@PostMapping(value="/getUserMerchantSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getUserMerchantSelectBean(String token,Long id,String isProxy) {
		
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			MerchantInfoModel model=new MerchantInfoModel();
			model.setId(id);
			model.setIsProxy(isProxy);
			if (user.getUserType().equals(DERP_SYS.USERINFO_USERTYPE_1)) {
				result = merchantInfoService.getSelectBean(model);
			} else {
				UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
				userMerchantRelModel.setUserId(user.getId());
				result = merchantInfoService.getUserSelectBean(userMerchantRelModel);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	
	
	@ApiOperation(value = "获取当前用户需绑定的公司下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantIds", value = "商家id 多个用英文逗号隔开")
	})
	@PostMapping(value="/getMerchantList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<MerchantInfoResponseDTO> getMerchantList(String token,String merchantIds) {
		MerchantInfoResponseDTO dto=new MerchantInfoResponseDTO();

		try {
			
			User user = ShiroUtils.getUserByToken(token);
			List<Long> list = StrUtils.parseIds(merchantIds);
			List<MerchantInfoModel> result=merchantInfoService.getMerchantList(list);
			List<TariffRateConfigModel> tariffRateConfigList = tariffRateConfigService.getTariffRateConfigList(new TariffRateConfigModel());
			ArrayList<DerpBasic> settlementTypeList = DERP_SYS.customerMerchantRel_settlementTypeList;
			ArrayList<DerpBasic> accountPeriodList = DERP_SYS.customerInfo_accountPeriodList;
			ArrayList<DerpBasic> saleTypeList = DERP_ORDER.saleAnalysis_saleTypeList;
			dto.setResult(result);
			dto.setSettlementTypeList(settlementTypeList);
			dto.setAccountPeriodList(accountPeriodList);
			dto.setTariffRateConfigList(tariffRateConfigList);
			dto.setSaleTypeList(saleTypeList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		

	}
	
}
