package com.topideal.order.webapi.common;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.exception.DerpException;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.entity.dto.common.LogisticsContactTemplateDTO;
import com.topideal.entity.dto.common.LogisticsContactTemplateLinkDTO;
import com.topideal.entity.vo.common.LogisticsContactTemplateLinkModel;
import com.topideal.entity.vo.common.LogisticsContactTemplateModel;
import com.topideal.order.service.common.LogisticsContactService;
import com.topideal.order.webapi.common.form.LogisticsContactTemplateForm;
import com.topideal.order.webapi.common.form.LogisticsContactTemplateLinkForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/webapi/order/common")
@Api(tags = "物流联系人接口")
public class APILogisticsContactController {

	private static final Logger LOG = Logger.getLogger(APILogisticsContactController.class) ;
	@Autowired
	private LogisticsContactService logisticsContactService ;
	
	@SuppressWarnings("unchecked")
	@PostMapping("/saveOrUpdateLogisTemplate.asyn")
	@ApiOperation(value = "新增或编辑物流联系人", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<LogisticsContactTemplateDTO> saveOrUpdateLogisTemplate(LogisticsContactTemplateForm form) {
		
		try {
			
			boolean isEmpty = new EmptyCheckUtils().addObject(form.getDetails())
			.addObject(form.getName())
			.addObject(form.getType())
			.empty();
			
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}
			
			LogisticsContactTemplateModel saveModel = new LogisticsContactTemplateModel() ;
			
			BeanUtils.copyProperties(form, saveModel);
			
			LogisticsContactTemplateDTO dto = logisticsContactService.saveOrUpdateLogisTemplate(saveModel) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
			
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/detailsTemplate.asyn")
	@ApiOperation(value = "获取物流联系人", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "物流联系人ID", required = true)})
	public ResponseBean<LogisticsContactTemplateDTO> detailsTemplate(Long id) {
		
		try {
			
			boolean isEmpty = new EmptyCheckUtils().addObject(id).empty();
			
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}
			
			LogisticsContactTemplateDTO dto = logisticsContactService.getDetailsTemplate(id) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
			
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/delTemplate.asyn")
	@ApiOperation(value = "获取物流联系人", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "物流联系人ID", required = true)})
	public ResponseBean<String> delTemplate(Long id) {
		
		try {
			
			boolean isEmpty = new EmptyCheckUtils().addObject(id).empty();
			
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}
			
			logisticsContactService.delTemplate(id) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
			
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/listTemplate.asyn")
	@ApiOperation(value = "获取物流联系人列表", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "type", value = "类型", required = true)})
	public ResponseBean<LogisticsContactTemplateDTO> listTemplate(String type) {
		
		try {
			
			List<LogisticsContactTemplateDTO> list = logisticsContactService.listTemplate(type) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list);//成功
			
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/saveOrUpdateLogisTemplateLink.asyn")
	@ApiOperation(value = "新增或编辑物流联系常用模版", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<LogisticsContactTemplateDTO> saveOrUpdateLogisTemplateLink(LogisticsContactTemplateLinkForm form) {
		
		try {
			
			boolean isEmpty = new EmptyCheckUtils()
			.addObject(form.getName())
			.empty();
			
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}
			
			LogisticsContactTemplateLinkModel saveModel = new LogisticsContactTemplateLinkModel() ;
			
			BeanUtils.copyProperties(form, saveModel);
			
			LogisticsContactTemplateDTO dto = logisticsContactService.saveOrUpdateLogisTemplateLink(saveModel) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
			
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/detailsTemplateLink.asyn")
	@ApiOperation(value = "获取物流联系常用模版", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "物流联系常用模版ID", required = true)})
	public ResponseBean<LogisticsContactTemplateLinkDTO> detailsTemplateLink(Long id) {
		
		try {
			
			boolean isEmpty = new EmptyCheckUtils().addObject(id).empty();
			
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}
			
			LogisticsContactTemplateLinkDTO dto = logisticsContactService.getDetailsTemplateLink(id) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
			
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/delTemplateLink.asyn")
	@ApiOperation(value = "删除物流联系常用模版", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "物流联系常用模版ID", required = true)})
	public ResponseBean<String> delTemplateLink(Long id) {
		
		try {
			
			boolean isEmpty = new EmptyCheckUtils().addObject(id).empty();
			
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}
			
			logisticsContactService.delTemplateLink(id) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
			
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/listTemplateLink.asyn")
	@ApiOperation(value = "获取物流联系常用模版列表", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<LogisticsContactTemplateLinkDTO> listTemplateLink() {
		
		try {
			
			List<LogisticsContactTemplateLinkDTO> list = logisticsContactService.listTemplateLink() ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list);//成功
			
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
		
	}
}
