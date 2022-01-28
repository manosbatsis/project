package com.topideal.order.webapi.bill;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.entity.dto.bill.AccountingReminderConfigDTO;
import com.topideal.entity.dto.platformdata.PlatformCategoryConfigDTO;
import com.topideal.entity.dto.purchase.ProjectQuotaConfigDTO;
import com.topideal.entity.vo.platformdata.PlatformCategoryConfigModel;
import com.topideal.order.service.bill.AccountingReminderConfigService;
import com.topideal.order.service.platformdata.PlatformCategoryConfigService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.AccountingReminderConfigAddForm;
import com.topideal.order.webapi.bill.form.AccountingReminderConfigForm;
import com.topideal.order.webapi.platformdata.form.PlatformCategoryConfigForm;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账期提醒配置
 **/

@RestController
@RequestMapping("/webapi/order/accountingReminderConfig")
@Api(tags = "账期提醒配置")
public class APIAccountingReminderConfigController {

	private static final Logger LOG = Logger.getLogger(APIAccountingReminderConfigController.class) ;

    @Autowired
    private AccountingReminderConfigService accountingReminderConfigService;

	@ApiOperation(value = "列表分页")
    @PostMapping(value="/getListByPage.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<AccountingReminderConfigDTO> getListByPage(AccountingReminderConfigForm form){

		try{

			AccountingReminderConfigDTO dto = new AccountingReminderConfigDTO() ;

			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());

			String userType = user.getUserType();

			if(!DERP_SYS.USERINFO_USERTYPE_1.equals(userType)){
				dto.setMerchantId(user.getMerchantId());
			}

			dto = accountingReminderConfigService.getListByPage(dto) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation(value = "新增或编辑平台品类配置")
	@PostMapping(value="/saveOrUpdateAccountingReminderConfig.asyn", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<String> saveOrUpdateAccountingReminderConfig(@RequestBody AccountingReminderConfigAddForm form){

		try{

			boolean isEmpty = new EmptyCheckUtils().addObject(form.getCustomerId())
					.addObject(form.getBuId()).addObject(form.getToken())
					.addObject(form.getMerchantId()).addObject(form.getBaseDate())
					.addObject(form.getReminderType())
					.empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			if(form.getItemList() == null
				|| form.getItemList().isEmpty()){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "表体不能为空");
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			AccountingReminderConfigDTO dto = new AccountingReminderConfigDTO() ;

			BeanUtils.copyProperties(form, dto);

			Long configId = accountingReminderConfigService.saveOrUpdateAccountingReminderConfig(dto, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, configId) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation(value = "审核平台品类配置")
	@PostMapping(value="/auditAccountingReminderConfig.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "记录ID", required = true) })
	public ResponseBean<String> auditAccountingReminderConfig(@RequestParam(value = "token", required = true) String token,
																	 @RequestParam(value = "id", required = true)Long id){

		try{

			User user = ShiroUtils.getUserByToken(token);

			accountingReminderConfigService.auditAccountingReminderConfig(id, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation(value = "删除平台品类配置")
	@PostMapping(value="/delAccountingReminderConfig.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "记录ID,多个以逗号隔开", required = true) })
	public ResponseBean<String> delAccountingReminderConfig(@RequestParam(value = "token", required = true) String token,
															  @RequestParam(value = "ids", required = true)String ids){

		try{

			accountingReminderConfigService.delAccountingReminderConfig(ids) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation(value = "根据ID获取配置")
	@PostMapping(value="/getAccountingReminderConfig.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "记录ID", required = true) })
	public ResponseBean<AccountingReminderConfigDTO> getAccountingReminderConfig(@RequestParam(value = "token", required = true) String token,
														  @RequestParam(value = "id", required = true)Long id){

		try{

			AccountingReminderConfigDTO dto = accountingReminderConfigService.getAccountingReminderConfig(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation(value = "获取用户信息")
	@PostMapping(value="/getUserInfo.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true)})
	public ResponseBean<User> getUserInfo(@RequestParam(value = "token", required = true) String token){

		try{

			User user = ShiroUtils.getUserByToken(token);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, user) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}


}
