package com.topideal.order.webapi.common;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.common.TradeLinkConfigDTO;
import com.topideal.entity.vo.common.TradeLinkConfigModel;
import com.topideal.order.service.common.TradeLinkConfigService;
import com.topideal.order.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易链路配置表
 */
@RestController
@RequestMapping("/webapi/order/tradeLinkConfig")
@Api(tags = "交易链路配置")
public class APITradeLinkConfigController {

	@Autowired
	private TradeLinkConfigService tradeLinkConfigService;

	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "获取列表分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true)
	})
	@PostMapping(value = "/listTradeLinkConfig.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listTradeLinkConfig(String token, Integer pageSize, Integer begin) {
		TradeLinkConfigDTO dto = new TradeLinkConfigDTO();
		try{
			dto.setPageSize(pageSize);
			dto.setBegin(begin);
			// 响应结果集
			dto = tradeLinkConfigService.getTradeLinkConfigListByPage(dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
	}
	/**
	 * 删除
	 * */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "单据id", required = true)
	})
	@PostMapping(value = "/delTradeLinkConfig.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delTradeLinkConfig(String token, String id) {
		try{
			//校验id是否正确
			boolean isRight = StrUtils.validateIds(id);
			if(!isRight){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			boolean b = tradeLinkConfigService.delTradeLinkConfig(Long.valueOf(id));
			if(!b){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"删除失败");
			}
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 保存
	 * */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "json", value = "json串", required = true)
	})
	@PostMapping(value = "/saveTradeLinkConfig.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveTradeLinkConfig(String token, String json) {
		String msg = null;
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			msg = tradeLinkConfigService.saveTradeLinkConfig(json, user);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
	}
	/**
	 * 访问编辑页面
	 * */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "单据id", required = true)
	})
	@PostMapping(value = "/getDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean getDetail(String token, String id) {
		TradeLinkConfigModel tradeLinkConfigModel = null;
		try{
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			// 响应结果集
			tradeLinkConfigModel = tradeLinkConfigService.searchDetail(Long.valueOf(id));
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,tradeLinkConfigModel);
	}
	/**
	 * 修改
	 * */
	@ApiOperation(value = "修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "json", value = "json串", required = true)
	})
	@PostMapping(value = "/modifyTradeLinkConfig.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyTradeLinkConfig(String token, String json) {
		String msg = null;
		try {
			if (json == null || StringUtils.isBlank(json)) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			msg = tradeLinkConfigService.modifyTradeLinkConfig(json, user);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, msg);
	}
}