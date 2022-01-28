package com.topideal.order.webapi.sale;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.sale.CustomerQuotaConfigDTO;
import com.topideal.entity.vo.sale.CustomerQuotaConfigModel;
import com.topideal.order.service.sale.CustomerQuotaConfigService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.CustomerQuotaConfigForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
/**
 * webapi 客户额度配置
 *
 */
@RequestMapping("/webapi/order/customerQuotaConfig")
@RestController
@Api(tags = "客户额度配置")
public class ApiCustomerQuotaConfigController {
	/* 打印日志 */
	protected Logger logger = LoggerFactory.getLogger(ApiCustomerQuotaConfigController.class);
	
	// 销售订单service
	@Autowired
	private CustomerQuotaConfigService customerQuotaConfigService;
	
	
	@ApiOperation(value = "调整额度")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true),
			@ApiImplicitParam(name = "customerQuota", value = "客户额度", required = true)
	})
	@PostMapping(value="/updateCustomerQuota.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean updateCustomerQuota(String token,Long id,BigDecimal customerQuota) {
		
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(id).addObject(customerQuota).empty();
			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			CustomerQuotaConfigModel model = new CustomerQuotaConfigModel();
			model.setId(id);
			model.setCustomerQuota(customerQuota);
			model.setModifyDate(TimeUtils.getNow());
			model.setModifier(user.getId());
			model.setModifyName(user.getName());			
			customerQuotaConfigService.updateCustomerQuota(model,user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		
	}
	
	
	@ApiOperation(value = "查询列表数据 分页")
	@PostMapping(value="/listDTOByPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<CustomerQuotaConfigDTO> listDTOByPage(CustomerQuotaConfigForm form) {
		CustomerQuotaConfigDTO dto = new CustomerQuotaConfigDTO();
		try {
			dto.setBuId(form.getBuId());
			dto.setCustomerId(form.getCustomerId());
			dto.setStatus(form.getStatus());
			dto.setPageSize(form.getPageSize());
			dto.setBegin(form.getBegin());

			User user = ShiroUtils.getUserByToken(form.getToken());
			dto = customerQuotaConfigService.listDTOByPage(dto,user);
			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	@ApiOperation(value = "保存")	
	@PostMapping(value="/saveCustomerQuotaConfigDTO.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveCustomerQuotaConfigDTO(CustomerQuotaConfigForm form) {
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(form.getBuId()).empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			CustomerQuotaConfigDTO dto = new CustomerQuotaConfigDTO();
			BeanUtils.copyProperties(form, dto);
			dto.setEffectiveDate(TimeUtils.parseFullTime(form.getEffectiveDate()));
			dto.setExpirationDate(TimeUtils.parseFullTime(form.getExpirationDate()));
			User user = ShiroUtils.getUserByToken(form.getToken());
			
			customerQuotaConfigService.saveCustomerQuotaConfigDTO(dto,user);
			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 审核
	 * @param dto
	 * @throws Exception
	 */
	@ApiOperation(value = "审核")	
	@PostMapping(value="/auditCustomerQuotaConfigDTO.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean auditCustomerQuotaConfigDTO(CustomerQuotaConfigForm form) {
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(form.getBuId())
					.addObject(form.getCustomerId()).addObject(form.getCustomerQuota())
					.addObject(form.getEffectiveDate()).addObject(form.getExpirationDate())
					.addObject(form.getCurrency()).empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			CustomerQuotaConfigDTO dto = new CustomerQuotaConfigDTO();
			BeanUtils.copyProperties(form, dto);
			dto.setEffectiveDate(TimeUtils.parseFullTime(form.getEffectiveDate()));
			dto.setExpirationDate(TimeUtils.parseFullTime(form.getExpirationDate()));
			User user = ShiroUtils.getUserByToken(form.getToken());
			
			customerQuotaConfigService.auditCustomerQuotaConfigDTO(dto,user);
			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 删除
	 * @param ids
	 * @throws Exception
	 */
	@ApiOperation(value = "删除")	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据id集合，多个用逗号隔开", required = true)
	})
	@PostMapping(value="/delCustomerQuotaConfigDTO.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delCustomerQuotaConfigDTO(String ids, String token ) {
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(ids).empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			customerQuotaConfigService.delCustomerQuotaConfigDTO(ids);
			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 *  事业部+客户 查询历史配置记录，默认带出最后一个已审核记录
	 * @param ids
	 * @throws Exception
	 */
	/*@ApiOperation(value = " 事业部+客户 查询最新一个已审核记录")	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部id", required = true),
		@ApiImplicitParam(name = "customerId", value = "客户id", required = true)
	})
	@PostMapping(value="/getLastestCustomerQuotaConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<CustomerQuotaConfigDTO> getLastestCustomerQuotaConfig(Long buId,Long customerId, String token ) {
		CustomerQuotaConfigDTO dto = new CustomerQuotaConfigDTO();
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(buId).addObject(customerId).empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			dto = customerQuotaConfigService.getLastestCustomerQuotaConfig(buId,customerId);
			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}*/
}
