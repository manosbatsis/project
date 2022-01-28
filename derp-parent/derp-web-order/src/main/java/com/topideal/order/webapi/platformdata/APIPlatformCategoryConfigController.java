package com.topideal.order.webapi.platformdata;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.entity.dto.platformdata.PlatformCategoryConfigDTO;
import com.topideal.entity.vo.platformdata.PlatformCategoryConfigModel;
import com.topideal.order.service.platformdata.PlatformCategoryConfigService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.platformdata.form.PlatformCategoryConfigForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 平台品类配置
 **/

@RestController
@RequestMapping("/webapi/order/platformCategoryConfig")
@Api(tags = "平台品类配置")
public class APIPlatformCategoryConfigController {

	private static final Logger LOG = Logger.getLogger(APIPlatformCategoryConfigController.class) ;

    @Autowired
    private PlatformCategoryConfigService platformCategoryConfigService;

	@ApiOperation(value = "列表分页")
    @PostMapping("/getListByPage.asyn")
    public ResponseBean<PlatformCategoryConfigDTO> getListByPage(PlatformCategoryConfigForm form){

		try{

			PlatformCategoryConfigDTO dto = new PlatformCategoryConfigDTO() ;

			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());

			dto = platformCategoryConfigService.getListByPage(dto) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation(value = "根据公司查询列表")
	@PostMapping("/getListByMerchantId.asyn")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true)})
	public ResponseBean<PlatformCategoryConfigDTO> getListByMerchantId(@RequestParam(value = "token", required = true) String token){

		try{

			PlatformCategoryConfigDTO dto = new PlatformCategoryConfigDTO() ;

			User user = ShiroUtils.getUserByToken(token);
			dto.setMerchantId(user.getMerchantId());

			List<PlatformCategoryConfigModel> list = platformCategoryConfigService.getListByMerchantId(dto) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list) ;

		}catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation(value = "新增平台品类配置")
	@PostMapping("/savePlatformCategoryConfig.asyn")
	public ResponseBean<PlatformCategoryConfigDTO> savePlatformCategoryConfig(PlatformCategoryConfigForm form){

		try{

			boolean isEmpty = new EmptyCheckUtils().addObject(form.getCustomerId())
					.addObject(form.getName()).addObject(form.getToken()).empty();

			if(isEmpty){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			PlatformCategoryConfigDTO dto = new PlatformCategoryConfigDTO() ;

			BeanUtils.copyProperties(form, dto);
			dto.setMerchantId(user.getMerchantId());
			dto.setMerchantName(user.getMerchantName());
			dto.setCreater(user.getId());
			dto.setCreateName(user.getName());

			platformCategoryConfigService.savePlatformCategoryConfig(dto) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch (DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 导入
	 *
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "导入", notes = "模版编码158")
	@PostMapping("/importPlatformCategoryConfig.asyn")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true)})
	public ResponseBean<UploadResponse> importPlatformCategoryConfig(@ApiParam(value = "上传的文件", required = true) @RequestParam(value = "file", required = true) MultipartFile file,
													   @RequestParam(value = "token", required = true) String token) {

		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集

		try {

			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}

			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream()) ;

			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user= ShiroUtils.getUserByToken(token);

			resultMap = platformCategoryConfigService.importPlatformCategoryConfig(data, user);

			Integer success = (Integer) resultMap.get("success");
			Integer failure = (Integer) resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, uploadResponse);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}
	@ApiOperation(value = "获取下拉")
    @PostMapping("/getSelectBean.asyn")
    public ResponseBean<List<SelectBean>> getSelectBean(PlatformCategoryConfigForm form){
		try{
			PlatformCategoryConfigDTO dto = new PlatformCategoryConfigDTO() ;
			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			List<SelectBean> result = platformCategoryConfigService.getSelectBean(dto) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result) ;
		}catch (Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}
}
