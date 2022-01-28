package com.topideal.order.webapi.filetemp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.common.CustomsFileConfigDTO;
import com.topideal.entity.dto.common.CustomsFileDepotRelDTO;
import com.topideal.entity.vo.common.CustomsFileDepotRelModel;
import com.topideal.order.service.filetemp.CustomsFileConfigService;
import com.topideal.order.service.filetemp.FileTempService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.filetemp.dto.CustomsFileConfigResqponseDTO;
import com.topideal.order.webapi.filetemp.form.CustomsFileConfigForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 清关单证模板配置控制器
 *
 */

@RestController
@RequestMapping("/webapi/order/customsFileConfig")
@Api(tags = "清关单证配置")
public class APICustomsFileConfigController {
	
	@Autowired
	CustomsFileConfigService customsFileConfigService ;
	@Autowired
	FileTempService fileTempService;
	
	
	@ApiOperation(value = "清关单证配置列表查询")
	@PostMapping(value="listCustomsFileConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<CustomsFileConfigDTO> listCustomsFileConfig(CustomsFileConfigForm form) {
		CustomsFileConfigDTO dto = new CustomsFileConfigDTO();
		try {			
			dto.setFileTempName(form.getFileTempName());
			dto.setDepotConfig(form.getDepotConfig());
			dto.setIsSameArea(form.getIsSameArea());
			dto.setStatus(form.getStatus());
			dto.setDepotIds(form.getDepotIds());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			
			dto = customsFileConfigService.listDTOByPage(dto) ;
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	/**
	 * 保存或修改
	 */
	@ApiOperation(value = "新增或修改")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "封装清关单证配置json串", required = true)
	})
	@PostMapping(value="saveOrModify.asyn" ,consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String, String>> saveOrModify(String token,String json) {		
		Map<String, String> result = new HashMap<String, String>();
		try {
			if(StringUtils.isBlank(json)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			result = customsFileConfigService.saveOrModity(json,user) ;
			String code = result.get("code");
			String message = result.get("message");
			if("01".equals(code)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017,message);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result) ;
	}
	

	/**
	 * 获取编辑页面关联仓库列表信息
	 */
	@ApiOperation(value = "获取编辑页面关联仓库列表信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "fileId", value = "单据id", required = true)
	})
	@PostMapping(value="/listDepotRel.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<CustomsFileDepotRelModel>> listDepotRel(String token,Long fileId) {
		List<CustomsFileDepotRelDTO> resultList = new ArrayList<>();
		try {
			if(fileId == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			resultList = customsFileConfigService.listDepotRel(fileId);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultList) ;
	}
	/**
	 * 删除
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids，多个用逗号隔开", required = true)
	})
	@PostMapping(value="/delCustomsFileConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delCustomsFileConfig(String token,String ids) {		
		try {
			if(StringUtils.isBlank(ids)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}			
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            List list = StrUtils.parseIds(ids);
			
            customsFileConfigService.delCustomsFileConfig(list);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;
	}
	/**
	 * 获取导出清关模板
	 */
	@ApiOperation(value = "获取导出清关模板")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "json", required = true)
	})
	@PostMapping(value="/getExportTemplate.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<CustomsFileConfigResqponseDTO> getExportTemplate(String token,String json) {		
		CustomsFileConfigResqponseDTO responseDTO = new CustomsFileConfigResqponseDTO();
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			
            if(StringUtils.isBlank(json)){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
           
            result = customsFileConfigService.getExportTemplate(json);
            responseDTO.setCode((String)result.get("code"));
            responseDTO.setOutList((List<CustomsFileConfigDTO>)result.get("outList"));
            responseDTO.setInList((List<CustomsFileConfigDTO>)result.get("inList"));
            
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO) ;
	}
}
