package com.topideal.order.webapi.common;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.common.SdPurchaseConfigDTO;
import com.topideal.entity.vo.common.SdPurchaseConfigItemModel;
import com.topideal.entity.vo.common.SdPurchaseConfigModel;
import com.topideal.entity.vo.common.SdTypeConfigModel;
import com.topideal.order.service.common.SdPurchaseConfigService;
import com.topideal.order.service.common.SdTypeService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.dto.SdPurchaseConfigResponseDTO;
import com.topideal.order.webapi.common.form.SdPurchaseConfigForm;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购sd配置
 * @author gy
 *
 */

@RestController
@RequestMapping("/webapi/order/sdPurchaseConfig")
@Api(tags = "采购SD配置")
public class APISdPurchaseConfigController {
	
	private static final Logger LOG = Logger.getLogger(APISdPurchaseConfigController.class) ;
	
	@Autowired
	private SdPurchaseConfigService sdPurchaseConfigService;
	@Autowired
	private SdTypeService sdTypeService ;

	/**
	 * 访问编辑详情
	 * @param
	 * */
	@ApiOperation(value = "获取编辑/复制 页面信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购SD配置id", required = true)
	})
	@PostMapping(value = "/getEditOrCopyPageInfo.asyn")
	public ResponseBean<SdPurchaseConfigResponseDTO> getEditOrCopyPageInfo(String token, Long id) throws Exception{
		SdPurchaseConfigResponseDTO responseDTO = new SdPurchaseConfigResponseDTO();
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			SdPurchaseConfigDTO dto = sdPurchaseConfigService.getDTOById(id) ;
			List<SdPurchaseConfigItemModel> itemList = sdPurchaseConfigService.getItemById(id) ;

			SdTypeConfigModel queryModel = new SdTypeConfigModel() ;
			queryModel.setType(DERP_ORDER.SDTYPECONFIG_TYPE_1);
			List<SdTypeConfigModel> singleList = sdTypeService.getList(queryModel);

			queryModel.setType(DERP_ORDER.SDTYPECONFIG_TYPE_2);
			List<SdTypeConfigModel> multiList = sdTypeService.getList(queryModel);

			responseDTO.setDetail(dto);
			responseDTO.setItemList(itemList);
			responseDTO.setSingleList(singleList);
			responseDTO.setMultiList(multiList);
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, responseDTO);//成功
	}

	@ApiOperation(value = "获取详情信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购SD配置id", required = true)
	})
	@PostMapping(value = "/getSdPurchaseConfigDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SdPurchaseConfigDTO> toDetailPage(String token, Long id) throws Exception{
		SdPurchaseConfigDTO dto = new SdPurchaseConfigDTO();
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			dto = sdPurchaseConfigService.getDTOById(id) ;
			List<SdPurchaseConfigItemModel> itemList = sdPurchaseConfigService.getItemById(id) ;

			dto.setItemList(itemList);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
	}
	
	/**
	 * 获取分页数据
	 * @param
	 * @return
	 */
	@ApiOperation(value = "获取列表分页信息")
	@PostMapping(value = "/sdPurchaseConfigList.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SdPurchaseConfigDTO> sdTypeConfigList(SdPurchaseConfigForm form) {
		SdPurchaseConfigDTO dto = new SdPurchaseConfigDTO();
		try{
			User user = ShiroUtils.getUserByToken(form.getToken());
			BeanUtils.copyProperties(form, dto);
			// 响应结果集
			dto = sdPurchaseConfigService.getSdPurchaseConfigListByPage(dto);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
	}

	@ApiOperation(value = "采购SD配置保存/编辑")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "itemList", value = "表体信息集合", required = true)
	})
	@PostMapping(value = "saveOrModify.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveOrModify(SdPurchaseConfigForm form, @RequestParam(value="itemList") String itemList) {
		try{
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(form.getMerchantId())
					.addObject(form.getSupplierId())
					.addObject(form.getEffectiveTime())
					.addObject(form.getInvalidTime())
					.addObject(form.getBuId()).empty();
			
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			SdPurchaseConfigModel model = new SdPurchaseConfigModel();
			BeanUtils.copyProperties(form, model);
			User user = ShiroUtils.getUserByToken(form.getToken());
			// 响应结果集
			sdPurchaseConfigService.saveOrModify(model, itemList, user) ;
		}catch(DerpException e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
	}
	
	/**
	 * 删除
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "采购SD配置删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "单据的id，多个用逗号隔开", required = true)
	})
	@PostMapping(value = "/delOrders.asyn",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean delOrders(String token, String ids) {
		try{
			if(StringUtils.isBlank(ids)) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			List<Long> list = (List<Long>)StrUtils.parseIds(ids);
			// 响应结果集
			sdPurchaseConfigService.delOrders(list);
		}catch(DerpException e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
	}

	@ApiOperation(value = "导入采购SD配置")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value = "/importSdPurchaseConfig.asyn", headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importSdPurchaseConfig(String token, HttpSession session , @ApiParam(value = "上传的文件", required = true)
													@RequestParam(value = "file", required = true) MultipartFile file) {
		
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}
			
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			resultMap = sdPurchaseConfigService.importSdPurchaseConfig(data) ;
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			List<Map<String, String>> dataList = (List<Map<String, String>>) resultMap.get("configItemList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			uploadResponse.setData(dataList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse) ;
			
		}catch(DerpException e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}
	
}