package com.topideal.webapi.main;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.main.EmailConfigDTO;
import com.topideal.entity.vo.main.EmailConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.main.CustomerService;
import com.topideal.service.main.EmailConfigService;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.EmailConfigForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 邮件发送配置信息控制层
 */
@RestController
@RequestMapping("/webapi/system/email")
@Api(tags = "邮件发送配置信息 ")
public class APIEmailConfigController {
	
	@Autowired
	private EmailConfigService emailConfigService;// 邮件发送配置信息service
	@Autowired
	private MerchantInfoService merchantInfoService;// 商家信息
	@Autowired
	private CustomerService customerService;// 客户
	


	/**
	 * 访问列表页面
	 */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toPage(String token) {
		try {
			Map<String, Object> resultMap=new HashMap<String, Object>();
			// 获取商家的下拉框
			User user = ShiroUtils.getUserByToken(token);
			MerchantInfoModel merchantInfoModel=new MerchantInfoModel();
			merchantInfoModel.setIsProxy(DERP_SYS.MERCHANTINFO_ISPROXY_0);
			List<SelectBean> merchantSelectBean = merchantInfoService.getSelectBean(merchantInfoModel);
			// 获取供应商下拉框
			List<SelectBean> customerSelectBean = customerService.getAllSelectBeanBySupplier();
			resultMap.put("merchantSelectBean", merchantSelectBean);
			resultMap.put("customerSelectBean", customerSelectBean);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 访问新增页面
	 * @throws SQLException 
	 */
	@ApiOperation(value = "访问新增页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/toAddPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toAddPage() throws SQLException {
		try {
			// 获取商家的下拉框
			MerchantInfoModel merchantInfoModel=new MerchantInfoModel();
			merchantInfoModel.setIsProxy(DERP_SYS.MERCHANTINFO_ISPROXY_0);
			List<SelectBean> merchantSelectBean = merchantInfoService.getSelectBean(merchantInfoModel);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,merchantSelectBean);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 校验此商家和供应商是否存在
	 */
	@ApiOperation(value = "校验此商家和供应商是否存在")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantId", value = "商家id", required = true)
	})
	@PostMapping(value="/getSelectBeanBySupplier.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean getSelectBeanBySupplier(Long merchantId) {
		List<SelectBean> customerSelectBean =null;
		try {
			customerSelectBean = customerService.getSelectBeanBySupplier(merchantId);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,customerSelectBean);//成功
		} catch (SQLException e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 访问详情页面
	 */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailsPage(Long id) throws Exception {
		try {
			// 查询邮件发送配置详情
			EmailConfigDTO detail = emailConfigService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,detail);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 访问编辑页面
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
			// 查询邮件发送配置详情
			EmailConfigDTO detail = emailConfigService.searchDetail(id);
			// 获取商家的下拉框
			MerchantInfoModel merchantInfoModel=new MerchantInfoModel();
			merchantInfoModel.setIsProxy(DERP_SYS.MERCHANTINFO_ISPROXY_0);
			List<SelectBean> merchantSelectBean = merchantInfoService.getSelectBean(merchantInfoModel);
			// 根据商家id获取供应商列表
			List<SelectBean> customerSelectBean = customerService.getSelectBeanBySupplier(detail.getMerchantId());
			resultMap.put("customerSelectBean", customerSelectBean);
			resultMap.put("merchantSelectBean", merchantSelectBean);
			resultMap.put("detail", detail);	
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取分页数据
	 */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "merchantId", value = "商家id"),
			@ApiImplicitParam(name = "customerId", value = "供应商id"),
			@ApiImplicitParam(name = "status", value = "状态")
	})
	@PostMapping(value="/listEmail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listEmail(Long merchantId,Long customerId,String status,int begin,int pageSize) {
		try {
			// 响应结果集
			EmailConfigDTO dto=new EmailConfigDTO();
			dto.setMerchantId(merchantId);
			dto.setCustomerId(customerId);
			dto.setStatus(status);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto = emailConfigService.listEmail(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 校验此商家和供应商是否存在
	 */
/*	@RequestMapping("/checkEmailExist.asyn")
	@ResponseBody
	private ViewResponseBean checkEmailExist(Long id) {
		int flag = 0;
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(id);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			DepotInfoModel model = depotService.searchDetail(id);
			if(model.getType().equals("a")){
				flag = 1;
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		//return ResponseFactory.success(flag);
		return null;
	}*/

	/**
	 * 新增
	 */

	@ApiOperation(value = "新增",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@PostMapping("/saveEmail.asyn")
	public ResponseBean saveEmail(EmailConfigForm form ) {
		Map<String, Object> saveEmail=new HashMap<>();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			// 存储信息
			EmailConfigModel model=new EmailConfigModel();
			model.setMerchantId(form.getMerchantId());
			model.setMerchantName(form.getMerchantName());
			model.setCustomerId(form.getCustomerId());
			model.setCustomerName(form.getCustomerName());
			model.setAccountPeriodDay(form.getAccountPeriodDay());
			model.setAdvanceReminderDay(form.getAdvanceReminderDay());
			model.setAccountUnitType(form.getAccountUnitType());
			model.setReminderUnitType(form.getReminderUnitType());
			model.setReminderType(form.getReminderType());
			model.setBaseTimeType(form.getBaseTimeType());			
			model.setStatus(DERP_SYS.EMAILCONFIG_STATUS_1);//'状态(1启用,0禁用)  新增默认启用
			saveEmail = emailConfigService.saveEmail(model);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,saveEmail);//成功
	}

	/**
	 * 删除
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id集合,多个用英文逗号隔开", required = true)
	})
	@PostMapping(value="/delEmail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delEmail(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			List list = StrUtils.parseIds(ids);
			
			boolean b=emailConfigService.delEmail(list);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}

	/**
	 * 修改
	 */
	@ApiOperation(value = "修改",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@PostMapping("/modifyEmail.asyn")
	public ResponseBean modifyEmail(EmailConfigForm form) {
		Map<String, Object> modifyEmail=new HashMap<>();
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateId(form.getId());
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			EmailConfigModel model=new EmailConfigModel();
			model.setId(form.getId());
			model.setMerchantId(form.getMerchantId());
			model.setMerchantName(form.getMerchantName());
			model.setCustomerId(form.getCustomerId());
			model.setCustomerName(form.getCustomerName());
			model.setAccountPeriodDay(form.getAccountPeriodDay());
			model.setAdvanceReminderDay(form.getAdvanceReminderDay());
			model.setAccountUnitType(form.getAccountUnitType());
			model.setReminderUnitType(form.getReminderUnitType());
			model.setReminderType(form.getReminderType());
			model.setBaseTimeType(form.getBaseTimeType());	
			model.setModifyDate(TimeUtils.getNow());
			modifyEmail = emailConfigService.modifyEmail(model);

		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,modifyEmail);//成功
	}
	
	/**
	 * 根据id禁用和启用
	 * 状态(1启用,0禁用)
	 * @return
	 */
	@ApiOperation(value = "根据id禁用和启用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "status", value = "状态", required = true)
	})
	@PostMapping(value="/auditEmail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean auditEmail(Long id , String status) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
			boolean b = emailConfigService.audit(id,status);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	

	
}
