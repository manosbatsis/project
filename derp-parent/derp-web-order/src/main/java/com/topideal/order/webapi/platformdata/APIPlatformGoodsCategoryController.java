package com.topideal.order.webapi.platformdata;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.entity.dto.platformdata.PlatfromGoodsCategoryDTO;
import com.topideal.order.service.platformdata.PlatformGoodsCategoryService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.platformdata.form.PlatfromGoodsCategoryForm;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  平台商品品类
 **/

@RestController
@RequestMapping("/webapi/order/platformGoodsCategory")
@Api(tags = " 平台商品品类")
public class APIPlatformGoodsCategoryController {

	private static final Logger LOG = Logger.getLogger(APIPlatformGoodsCategoryController.class) ;

    @Autowired
    private PlatformGoodsCategoryService platformGoodsCategoryService;

	@ApiOperation(value = "列表分页")
    @PostMapping("/getListByPage.asyn")
    public ResponseBean<PlatfromGoodsCategoryDTO> getListByPage(PlatfromGoodsCategoryForm form){

		try{

			PlatfromGoodsCategoryDTO dto = new PlatfromGoodsCategoryDTO() ;

			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());

			dto = platformGoodsCategoryService.getListByPage(dto, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch (Exception e){
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
	@ApiOperation(value = "导入", notes = "模版编码159")
	@PostMapping("/importPlatformGoodsCategory.asyn")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true)})
	public ResponseBean<UploadResponse> importPlatformGoodsCategory(@ApiParam(value = "上传的文件", required = true) @RequestParam(value = "file", required = true) MultipartFile file,
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

			resultMap = platformGoodsCategoryService.importPlatformGoodsCategory(data, user);

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
}
