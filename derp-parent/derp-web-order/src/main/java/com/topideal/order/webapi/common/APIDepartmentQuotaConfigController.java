package com.topideal.order.webapi.common;

import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.common.DepartmentQuatoDTO;
import com.topideal.entity.dto.common.DepartmentQuatoItemDTO;
import com.topideal.order.service.common.DepartmentQuotaConfigService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.form.DepartmentQuatoForm;
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
import java.util.List;
import java.util.Map;


/**
 * 项目额度配置
 **/

@RestController
@RequestMapping("/webapi/order/departmentQuotaConfig")
@Api(tags = "部门项目额度配置")
public class APIDepartmentQuotaConfigController {

	private static final Logger LOG = Logger.getLogger(APIDepartmentQuotaConfigController.class) ;

    @Autowired
    private DepartmentQuotaConfigService departmentQuotaService;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "列表分页")
    @PostMapping(value="/getListByPage.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<DepartmentQuatoDTO> getListByPage(DepartmentQuatoForm form){

		try{

			DepartmentQuatoDTO dto = new DepartmentQuatoDTO() ;

			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());

			dto = departmentQuotaService.getListByPage(dto, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "列表分页")
    @PostMapping(value="/getItemList.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<DepartmentQuatoItemDTO> getItemList(Long departmentQuatoId){

		try{

			if(departmentQuatoId == null){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			List<DepartmentQuatoItemDTO> itemList = departmentQuotaService.getItemList(departmentQuatoId) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, itemList) ;

		}catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "新增或编辑部门额度配置")
	@PostMapping(value="/saveOrUpdateDepartmentQuotaConfig.asyn")
	public ResponseBean<String> saveOrUpdateDepartmentQuotaConfig(@RequestBody DepartmentQuatoForm form){

		try{

			boolean isEmpty = new EmptyCheckUtils().addObject(form.getToken()).empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			DepartmentQuatoDTO dto = new DepartmentQuatoDTO() ;

			BeanUtils.copyProperties(form, dto);

			if(StringUtils.isNotBlank(form.getEffectiveDateStr())){
				dto.setEffectiveDate(TimeUtils.parse(form.getEffectiveDateStr(), "yyyy-MM-dd HH:mm:ss"));
			}

			if (StringUtils.isNotBlank(form.getExpirationDateStr())){
				dto.setExpirationDate(TimeUtils.parse(form.getExpirationDateStr(), "yyyy-MM-dd HH:mm:ss"));
			}

			Long configId = departmentQuotaService.saveOrUpdateDepartmentQuotaConfig(dto, user) ;

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
	@ApiOperation(value = "审核部门额度配置")
	@PostMapping(value="/auditDepartmentQuotaConfig.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "记录ID", required = true) })
	public ResponseBean<String> auditDepartmentQuotaConfig(@RequestParam(value = "token", required = true) String token,
																	 @RequestParam(value = "id", required = true)Long id){

		try{

			User user = ShiroUtils.getUserByToken(token);

			DepartmentQuatoDTO dto = departmentQuotaService.getDepartmentQuotaConfigById(id);

			boolean isEmpty = new EmptyCheckUtils()
					.addObject(dto.getCurrency()).addObject(dto.getEffectiveDate())
					.addObject(dto.getExpirationDate()).addObject(dto.getDepartmentQuota())
					.addObject(dto.getDepartmentId())
					.empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			departmentQuotaService.auditDepartmentQuotaConfig(id, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "删除部门额度配置")
	@PostMapping(value="/delDepartmentQuotaConfig.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "记录ID,多个以逗号隔开", required = true) })
	public ResponseBean<String> delDepartmentQuotaConfig(@RequestParam(value = "token", required = true) String token,
															  @RequestParam(value = "ids", required = true)String ids){

		try{

			departmentQuotaService.delDepartmentQuotaConfig(ids) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据ID获取部门额度配置")
	@PostMapping(value="/getProjectQuotaConfigById.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "记录ID", required = true) })
	public ResponseBean<DepartmentQuatoDTO> getDepartmentQuotaConfigById(@RequestParam(value = "token", required = true) String token,
													  @RequestParam(value = "id", required = true)Long id){

		try{

			DepartmentQuatoDTO dto = departmentQuotaService.getDepartmentQuotaConfigById(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "调整部门额度")
	@PostMapping(value="/updateDepartmentQuota.asyn")
	public ResponseBean<String> updateDepartmentQuota(@RequestBody DepartmentQuatoForm form){

		try{

			boolean isEmpty = new EmptyCheckUtils()
					.addObject(form.getToken())
					.addObject(form.getId())
					.addObject(form.getDepartmentQuota())
					.empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			DepartmentQuatoDTO dto = new DepartmentQuatoDTO() ;

			BeanUtils.copyProperties(form, dto);

			departmentQuotaService.updateDepartmentQuota(dto, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

}
