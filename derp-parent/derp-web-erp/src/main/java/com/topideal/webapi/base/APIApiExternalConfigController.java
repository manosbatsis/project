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
import com.topideal.common.tools.AppKeyGeneratorUtil;
import com.topideal.common.tools.AppSecretGeneratorUtil;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;
import com.topideal.entity.vo.base.ApiExternalConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.base.ApiExternalConfigService;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.webapi.form.ApiExternalConfigForm;
import com.topideal.webapi.form.ApiExternalToEditResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 对外接口配置
 * @author lian_
 */
@RequestMapping("/webapi/system/apiExternal")
@RestController
@Api(tags = "对外接口配置")
public class APIApiExternalConfigController {

	//外部接口配置
	@Autowired
	private ApiExternalConfigService service;
	//商家
	@Autowired
	private MerchantInfoService merchantInfoService;
	
	/**
	 * 访问列表页面杨创
	 * */
/*	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/base/api-external-list";
	}*/
	
	/**
	 * 访问新增页面获取商家下拉框
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问新增页面获取商家下拉框")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
	})
	@PostMapping(value="/toAddPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>>  toAddPage() throws Exception {
		try {
			MerchantInfoModel merchant=new MerchantInfoModel();
			List<SelectBean> list = merchantInfoService.getSelectBean(merchant);
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
			@ApiImplicitParam(name = "id", value = "对外接口表id", required = true)
	})
	@PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ApiExternalToEditResponseDTO> toEditPage(Long id) throws Exception {
		try {
			ApiExternalToEditResponseDTO response=new ApiExternalToEditResponseDTO();
			ApiExternalConfigDTO apiModel = service.getDetails(id);
			MerchantInfoModel merchant=new MerchantInfoModel();
			List<SelectBean> list = merchantInfoService.getSelectBean(merchant);
			response.setDetail(apiModel);
			response.setMerchantList(list);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,apiModel);//成功
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
			@ApiImplicitParam(name = "id", value = "对外接口表id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ApiExternalConfigDTO>  toDetailsPage(Long id) throws Exception {		
		try {
			ApiExternalConfigDTO dto = service.getDetails(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
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
			@ApiImplicitParam(name = "merchantName", value = "公司"),
			@ApiImplicitParam(name = "platformName", value = "使用对象")
	})
	@PostMapping(value="/listApiExternal.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<ApiExternalConfigDTO>  listApiExternal(String merchantName,String platformName,Integer begin,Integer pageSize) {
		ApiExternalConfigDTO dto=new ApiExternalConfigDTO();
		try {
			dto.setMerchantName(merchantName);
			dto.setPlatformName(platformName);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			// 响应结果集
			dto = service.getListByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch (Exception e) {
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
	@PostMapping(value="/saveApiExternal.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveApiExternal(ApiExternalConfigForm form) {
		try {
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(form.getMerchantId())
					.addObject(form.getAppKey())
					.addObject(form.getAppSecret())
					.empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			ApiExternalConfigModel model=new ApiExternalConfigModel();
			model.setMerchantId(form.getMerchantId());
			model.setAppKey(form.getAppKey());
			model.setAppSecret(form.getAppSecret());
			model.setPlatformName(form.getPlatformName());
			model.setRemark(form.getRemark());
			boolean b = service.saveApiExternal(model);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常);
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
	@PostMapping(value="/delApiExternal.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delApiExternal(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = service.delApiExternal(list);
            if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常);
        }

	}
	/**
	 * 编辑
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "编辑")
	@PostMapping(value="/modifyApiExternal.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyApiExternal(ApiExternalConfigForm form) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(form.getId());
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            ApiExternalConfigModel model=new ApiExternalConfigModel();
            model.setId(form.getId());
			model.setMerchantId(form.getMerchantId());
			model.setAppKey(form.getAppKey());
			model.setAppSecret(form.getAppSecret());
			model.setPlatformName(form.getPlatformName());
			model.setRemark(form.getRemark());
			model.setModifyDate(TimeUtils.getNow());
            boolean b = service.modifyApiExternal(model);
            if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常);
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
		@ApiImplicitParam(name = "status", value = "禁用/启用状态")
	})
	@PostMapping(value="/auditApiExternal.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean auditApiExternal(Long id,String status) {
		try {
			//校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            ApiExternalConfigModel model=new ApiExternalConfigModel();
            model.setId(id);
            model.setStatus(status);
            model.setModifyDate(TimeUtils.getNow());
			boolean b = service.auditApiExternal(model);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常);
		}
	}
	
	/**
	 * 得到随机app_key和秘钥
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "生成随机app_key和秘钥")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
	})
	@PostMapping(value="/getAppKeyAndAppSecret.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<ApiExternalConfigModel> getAppKeyAndAppSecret() {		
		try {
			ApiExternalConfigModel apiModel = new ApiExternalConfigModel();			
			apiModel.setAppKey(AppKeyGeneratorUtil.KeyValue19()); //获取app_key
			apiModel.setAppSecret(AppSecretGeneratorUtil.KeyValue32());//获取秘钥
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,apiModel);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常);
		}

	}
	
	
}
