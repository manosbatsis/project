package com.topideal.order.webapi.common;

import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.common.QuotaPeriodConfigDTO;
import com.topideal.entity.dto.purchase.ProjectQuotaConfigDTO;
import com.topideal.order.service.common.QuotaPeriodConfigService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.form.QuotaPeriodConfigForm;
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
@RequestMapping("/webapi/order/quotaPeriodConfig")
@Api(tags = "额度期初配置")
public class APIQuotaPeriodConfigController {

	private static final Logger LOG = Logger.getLogger(APIQuotaPeriodConfigController.class) ;

    @Autowired
    private QuotaPeriodConfigService quotaPeriodConfigService;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "列表分页")
    @PostMapping(value="/getListByPage.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<QuotaPeriodConfigDTO> getListByPage(QuotaPeriodConfigForm form){

		try{

			QuotaPeriodConfigDTO dto = new QuotaPeriodConfigDTO() ;

			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());

			dto = quotaPeriodConfigService.getListByPage(dto, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "新增或编辑期初额度配置")
	@PostMapping(value="/saveOrUpdateQuotaPeriodConfig.asyn")
	public ResponseBean<String> saveOrUpdateQuotaPeriodConfig(@RequestBody QuotaPeriodConfigForm form){

		try{

			boolean isEmpty = new EmptyCheckUtils()
					.addObject(form.getToken())
					.empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			QuotaPeriodConfigDTO dto = new QuotaPeriodConfigDTO() ;

			BeanUtils.copyProperties(form, dto);

			if(StringUtils.isNotBlank(form.getPeriodDateStr())){
				dto.setPeriodDate(TimeUtils.parse(form.getPeriodDateStr(), "yyyy-MM-dd HH:mm:ss"));
			}

			Long configId = quotaPeriodConfigService.saveOrUpdateQuotaPeriodConfig(dto, user) ;

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
	@ApiOperation(value = "审核期初额度配置")
	@PostMapping(value="/auditQuotaPeriodConfig.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "记录ID", required = true) })
	public ResponseBean<String> auditQuotaPeriodConfig(@RequestParam(value = "token", required = true) String token,
																	 @RequestParam(value = "id", required = true)Long id){

		try{

			User user = ShiroUtils.getUserByToken(token);

			QuotaPeriodConfigDTO dto = quotaPeriodConfigService.getQuotaPeriodConfigById(id);

			boolean isEmpty = new EmptyCheckUtils()
					.addObject(dto.getBuId())
					.addObject(dto.getCurrency())
					.addObject(dto.getQuotaType())
					.addObject(dto.getPeriodQuota()).addObject(dto.getPeriodDate())
					.empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			quotaPeriodConfigService.auditQuotaPeriodConfig(id, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}


	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据ID获取期初额度配置")
	@PostMapping(value="/getQuotaPeriodConfigById.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "记录ID", required = true) })
	public ResponseBean<ProjectQuotaConfigDTO> getProjectQuotaConfigById(@RequestParam(value = "token", required = true) String token,
													  @RequestParam(value = "id", required = true)Long id){

		try{

			QuotaPeriodConfigDTO dto = quotaPeriodConfigService.getQuotaPeriodConfigById(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}


	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据事业部获取品牌下拉框")
	@PostMapping(value="/getQuotaSelectBeanByBuId.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "记录ID", required = true) })
	public ResponseBean<SelectBean> getQuotaSelectBeanByBuId(@RequestParam(value = "token", required = true) String token,
													  @RequestParam(value = "buId", required = true)Long buId){

		try{

			List<SelectBean> selectBeanList = quotaPeriodConfigService.getQuotaSelectBeanByBuId(buId);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, selectBeanList) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}
}
