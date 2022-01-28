package com.topideal.order.webapi.common;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.common.SdTypeConfigDTO;
import com.topideal.entity.vo.common.SdTypeConfigModel;
import com.topideal.order.service.common.SdTypeService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.common.form.SdTypeConfigForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

/**
 * sd类型配置
 * @author gy
 *
 */

@RestController
@RequestMapping("/webapi/order/sdTypeConfig")
@Api(tags = "采购SD类型")
public class APISdTypeConfigController {
	
	private static final Logger LOG = Logger.getLogger(APISdTypeConfigController.class) ;
	
	@Autowired
	private SdTypeService sdTypeService;

	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "获取列表分页信息")
	@PostMapping(value = "/sdTypeConfigList.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SdTypeConfigDTO> sdTypeConfigList(SdTypeConfigForm form) {
		SdTypeConfigDTO dto = new SdTypeConfigDTO();
		try{
			BeanUtils.copyProperties(form, dto);
			// 响应结果集
			dto = sdTypeService.getSdTypeConfigListByPage(dto);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
	}

	@ApiOperation(value = "采购SD类型保存/编辑")
	@PostMapping(value = "saveOrModify.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveOrModify(SdTypeConfigForm form) {
		try{
			SdTypeConfigModel model = new SdTypeConfigModel();
			BeanUtils.copyProperties(form, model);
			User user = ShiroUtils.getUserByToken(form.getToken());
			// 响应结果集
			sdTypeService.saveOrModify(model, user);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
	}

	@ApiOperation(value = "获取采购SD类型详情")
	@PostMapping(value = "detail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SdTypeConfigModel> detail(SdTypeConfigForm form) {
		SdTypeConfigModel model = new SdTypeConfigModel();
		try{
			BeanUtils.copyProperties(form, model);
			// 响应结果集
			model = sdTypeService.searchByModel(model);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, model);
	}
	
	/**
	 * 获取已启用配置
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "获取已启用配置")
	@PostMapping(value = "getList.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SdTypeConfigModel>> getList(SdTypeConfigForm form) {
		List<SdTypeConfigModel> list = null ;
		try{
			SdTypeConfigModel model = new SdTypeConfigModel();
			BeanUtils.copyProperties(form, model);
			list = sdTypeService.getList(model) ;
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list);
	}
}
