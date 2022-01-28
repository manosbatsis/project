package com.topideal.webapi.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.ApiSecretConfigDTO;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.base.ApiSecretConfigService;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.webapi.form.ApiSecretConfigForm;
import com.topideal.webapi.form.ApiToEditResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 接口配置 控制层
 * @author 杨创
 */
@RestController
@RequestMapping("/webapi/system/api")
@Api(tags = "接口配置")
public class APIApiSecretConfigController {

	//接口配置
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;
	//商家信息
	@Autowired
	private MerchantInfoService merchantInfoService;
	
	/**
	 * 访问列表页面
	 * */
/*	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/api-list";
	}*/
	
	/**
	 * 访问新增页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问新增页面获取商家下拉框")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
	})
	@PostMapping(value="/toAddPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> toAddPage() throws Exception {		
		try {
			List<SelectBean> list = merchantInfoService.getSelectBean(new MerchantInfoModel());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问编辑页面数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ApiToEditResponseDTO> toEditPage(Long id) throws Exception {
		try {
			ApiToEditResponseDTO response =new ApiToEditResponseDTO();
			ApiSecretConfigDTO apiModel = apiSecretConfigService.getDetails(id);		
			List<SelectBean> list = merchantInfoService.getSelectBean(new MerchantInfoModel());
			response.setDetail(apiModel);
			response.setMerchantList(list);;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,response);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 访问详情页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问详情页面数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ApiSecretConfigDTO> toDetailsPage(Long id) throws Exception {
		try {
			ApiSecretConfigDTO apiModel = apiSecretConfigService.getDetails(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,apiModel);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "appKey", value = "app_key"),
			@ApiImplicitParam(name = "platformName", value = "平台名称")
	})
	@PostMapping(value="/listApi.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<ApiSecretConfigDTO> listApi(String appKey,String platformName,int begin,int pageSize) {
		try{
			ApiSecretConfigDTO dto=new ApiSecretConfigDTO();
			dto.setAppKey(appKey);
			dto.setPlatformName(platformName);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			// 响应结果集
			dto = apiSecretConfigService.getListByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 新增
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "新增")
	@PostMapping(value="/saveApi.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveApi(ApiSecretConfigForm form) {
		try{
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(form.getMerchantId())
					.addObject(form.getAppKey())
					.addObject(form.getAppSecret())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
			}
			ApiSecretConfigModel model= new ApiSecretConfigModel();
			model.setMerchantId(form.getMerchantId());
			model.setAppKey(form.getAppKey());
			model.setAppSecret(form.getAppSecret());
			model.setRemark(form.getRemark());
			boolean b = apiSecretConfigService.saveApiSecret(model);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
		}
	}
	
	/**
	 * 编辑
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "编辑")
	@PostMapping(value="/modifyApi.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyApi(ApiSecretConfigForm form) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(form.getId());
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            ApiSecretConfigModel model= new ApiSecretConfigModel();
            model.setId(form.getId());
			model.setMerchantId(form.getMerchantId());
			model.setAppKey(form.getAppKey());
			model.setAppSecret(form.getAppSecret());
			model.setRemark(form.getRemark());
			boolean b = apiSecretConfigService.modify(model);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
		}
	}
	
	/**
	 * 禁用/启用
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "禁用/启用")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "id"),
		@ApiImplicitParam(name = "status", value = "启用/禁用状态"),		
	})
	@PostMapping(value="/auditApi.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean auditApi(Long id,String status) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            ApiSecretConfigModel model= new ApiSecretConfigModel();
            model.setId(id);
            model.setStatus(status);
			boolean b = apiSecretConfigService.modifyAuditApi(model);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
		}
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "id ,分隔")
	})
	@PostMapping(value="/delApi.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delApi(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List<Long> list = StrUtils.parseIds(ids);
            boolean b = apiSecretConfigService.delApiSecretConfig(list);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),e.getMessage());//未知异常
        }
	}
	
}
