package com.topideal.order.webapi.purchase;

import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.common.DepartmentQuatoDTO;
import com.topideal.entity.dto.purchase.ProjectQuotaConfigDTO;
import com.topideal.order.service.purchase.ProjectQuotaConfigService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.form.DepartmentQuatoForm;
import com.topideal.order.webapi.purchase.form.ProjectQuotaConfigForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 项目额度配置
 **/

@RestController
@RequestMapping("/webapi/order/projectQuotaConfig")
@Api(tags = "项目额度配置")
public class APIProjectQuotaConfigController {

	private static final Logger LOG = Logger.getLogger(APIProjectQuotaConfigController.class) ;

    @Autowired
    private ProjectQuotaConfigService projectQuotaConfigService;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "列表分页")
    @PostMapping(value="/getListByPage.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<ProjectQuotaConfigDTO> getListByPage(ProjectQuotaConfigForm form){

		try{

			ProjectQuotaConfigDTO dto = new ProjectQuotaConfigDTO() ;

			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());

			dto = projectQuotaConfigService.getListByPage(dto, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "新增或编辑项目额度配置")
	@PostMapping(value="/saveOrUpdateProjectQuotaConfig.asyn")
	public ResponseBean<String> saveOrUpdateProjectQuotaConfig(@RequestBody ProjectQuotaConfigForm form){

		try{

			boolean isEmpty = new EmptyCheckUtils().addObject(form.getToken()).empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			ProjectQuotaConfigDTO dto = new ProjectQuotaConfigDTO() ;

			BeanUtils.copyProperties(form, dto);

			if(StringUtils.isNotBlank(form.getEffectiveDateStr())){
				dto.setEffectiveDate(TimeUtils.parse(form.getEffectiveDateStr(), "yyyy-MM-dd HH:mm:ss"));
			}

			if (StringUtils.isNotBlank(form.getExpirationDateStr())){
				dto.setExpirationDate(TimeUtils.parse(form.getExpirationDateStr(), "yyyy-MM-dd HH:mm:ss"));
			}

			Long configId = projectQuotaConfigService.saveOrUpdateProjectQuotaConfig(dto, user) ;

			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("configId", configId) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, returnMap) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "审核项目额度配置")
	@PostMapping(value="/auditProjectQuotaConfig.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "记录ID", required = true) })
	public ResponseBean<String> auditProjectQuotaConfig(@RequestParam(value = "token", required = true) String token,
																	 @RequestParam(value = "id", required = true)Long id){

		try{

			User user = ShiroUtils.getUserByToken(token);

			ProjectQuotaConfigDTO dto = projectQuotaConfigService.getProjectQuotaConfigById(id);

			boolean isEmpty = new EmptyCheckUtils()
					.addObject(dto.getBuId())
					.addObject(dto.getProjectQuota())
					.addObject(dto.getCurrency()).addObject(dto.getEffectiveDate())
					.addObject(dto.getExpirationDate()).addObject(dto.getQuotaType())
					.empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			projectQuotaConfigService.auditProjectQuotaConfig(id, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "删除项目额度配置")
	@PostMapping(value="/delProjectQuotaConfig.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "记录ID,多个以逗号隔开", required = true) })
	public ResponseBean<String> delProjectQuotaConfig(@RequestParam(value = "token", required = true) String token,
															  @RequestParam(value = "ids", required = true)String ids){

		try{

			projectQuotaConfigService.delProjectQuotaConfig(ids) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据ID获取项目额度配置")
	@PostMapping(value="/getProjectQuotaConfigById.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "记录ID", required = true) })
	public ResponseBean<ProjectQuotaConfigDTO> getProjectQuotaConfigById(@RequestParam(value = "token", required = true) String token,
													  @RequestParam(value = "id", required = true)Long id){

		try{

			ProjectQuotaConfigDTO dto = projectQuotaConfigService.getProjectQuotaConfigById(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取最新已审核期初账期信息")
	@PostMapping(value="/getLatestPeriodInfo.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部ID", required = true),
		@ApiImplicitParam(name = "superiorParentBrandId", value = "母品牌ID", required = true)})
	public ResponseBean<ProjectQuotaConfigDTO> getLatestPeriodInfo(@RequestParam(value = "token", required = true) String token,
																   @RequestParam(value = "buId", required = true) Long buId,
																   @RequestParam(value = "superiorParentBrandId", required = true) Long superiorParentBrandId){

		try{

			User user = ShiroUtils.getUserByToken(token);

			ProjectQuotaConfigDTO dto = projectQuotaConfigService.getLatestPeriodInfo(user, buId, superiorParentBrandId);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取最新已审核部门额度信息")
	@PostMapping(value="/getLatestDepartmentQuato.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部ID", required = true)})
	public ResponseBean<DepartmentQuatoDTO> getLatestDepartmentQuato(@RequestParam(value = "token", required = true) String token,
																   @RequestParam(value = "buId", required = true) Long buId){

		try{

			User user = ShiroUtils.getUserByToken(token);

			DepartmentQuatoDTO dto = projectQuotaConfigService.getLatestDepartmentQuato(user, buId);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "调整项目额度")
	@PostMapping(value="/updateProjectQuota.asyn")
	public ResponseBean<String> updateProjectQuota(@RequestBody ProjectQuotaConfigForm form){

		try{

			boolean isEmpty = new EmptyCheckUtils()
					.addObject(form.getToken())
					.addObject(form.getId())
					.addObject(form.getProjectQuota())
					.empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			ProjectQuotaConfigDTO dto = new ProjectQuotaConfigDTO() ;

			BeanUtils.copyProperties(form, dto);

			projectQuotaConfigService.updateProjectQuota(dto, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

}
