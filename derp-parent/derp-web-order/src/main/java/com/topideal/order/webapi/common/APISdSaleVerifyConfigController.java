package com.topideal.order.webapi.common;

import org.apache.log4j.Logger;
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
import com.topideal.entity.dto.common.SdSaleVerifyConfigDTO;
import com.topideal.order.service.common.SdSaleVerifyConfigService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.form.SdSaleVerifyConfigForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/webapi/order/sdSaleVerifyConfig")
@Api(tags = "销售SD核销配置")
public class APISdSaleVerifyConfigController {
	private static final Logger LOGGER = Logger.getLogger(APISdSaleVerifyConfigController.class);
	
	@Autowired
	private SdSaleVerifyConfigService sdSaleVerifyConfigService;
	
	@ApiOperation(value = "获取列表信息")
	@PostMapping(value="/listDTOByPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SdSaleVerifyConfigDTO> listDTOByPage(SdSaleVerifyConfigForm form) {
		SdSaleVerifyConfigDTO dto = new SdSaleVerifyConfigDTO();
		try {
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());
			if(dto.getMerchantId() == null) {
				dto.setMerchantId(user.getMerchantId());
				dto.setMerchantName(user.getMerchantName());				
			}
			dto = sdSaleVerifyConfigService.listDTOByPage(dto, user);
			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	@ApiOperation(value = "获取详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "单据id", required = true)
	})
	@PostMapping(value="/searchDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SdSaleVerifyConfigDTO> searchDetail(Long id ,String token) {
		SdSaleVerifyConfigDTO dto = new SdSaleVerifyConfigDTO();
		try {
			dto = sdSaleVerifyConfigService.searchDetail(id);
			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	@ApiOperation(value = "保存/审核")
	@PostMapping(value="/saveOrAudit.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveOrAudit(SdSaleVerifyConfigForm form) {
		SdSaleVerifyConfigDTO dto = new SdSaleVerifyConfigDTO();
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(form.getBuId()).addObject(form.getCustomerId())
					.addObject(form.getIsMerchandiseVerify()).addObject(form.getVerifyType())
					.addObject(form.getOperate()).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			dto.setMerchantName(user.getMerchantName());				
			
			sdSaleVerifyConfigService.saveOrAudit(dto, user,form.getOperate());
			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	@ApiOperation(value = "更新启用/停用状态")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "单据id", required = true),
		@ApiImplicitParam(name = "status", value = "状态 1-启用 2-停用", required = true)
	})
	@PostMapping(value="/modifyStatus.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyStatus(Long id ,String status, String token) {
		try {
			User user = ShiroUtils.getUserByToken(token);
			sdSaleVerifyConfigService.modifyStatus(id, user, status);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的ids，多个用逗号隔开", required = true)
	})
	@PostMapping(value="/delVerifyConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delVerifyConfig(String ids ,String status, String token) {
		try {
			sdSaleVerifyConfigService.delVerifyConfig(ids);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
}
