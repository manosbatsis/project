package com.topideal.order.web.common;

import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.common.TradeLinkConfigDTO;
import com.topideal.entity.vo.common.TradeLinkConfigModel;
import com.topideal.order.service.common.TradeLinkConfigService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 交易链路配置表
 */
@RequestMapping("/tradeLinkConfig")
@Controller
public class TradeLinkConfigController {

	@Autowired
	private TradeLinkConfigService tradeLinkConfigService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/common/trade-link-config-list";
	}
	
	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@RequestMapping("/listTradeLinkConfig.asyn")
	@ResponseBody
	private ViewResponseBean listTradeLinkConfig(TradeLinkConfigDTO dto) {
		try{
			// 响应结果集
			dto = tradeLinkConfigService.getTradeLinkConfigListByPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 删除
	 * */
	@RequestMapping("/delTradeLinkConfig.asyn")
	@ResponseBody
	public ViewResponseBean delTradeLinkConfig(String ids) {
		try{
			//校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if(!isRight){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			boolean b = tradeLinkConfigService.delTradeLinkConfig(Long.valueOf(ids));
			if(!b){
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}
	/**
	 * 保存
	 * */
	@RequestMapping("/saveTradeLinkConfig.asyn")
	@ResponseBody
	public ViewResponseBean saveTradeLinkConfig(String json) {
		String msg = null;
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			msg = tradeLinkConfigService.saveTradeLinkConfig(json, user);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(msg);
	}
	/**
	 * 访问编辑页面
	 * */
	@RequestMapping("/toEditPage.asyn")
	@ResponseBody
	private ViewResponseBean toEditPage(String id) {
		TradeLinkConfigModel tradeLinkConfigModel = null;
		try{
			// 响应结果集
			tradeLinkConfigModel = tradeLinkConfigService.searchDetail(Long.valueOf(id));
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(tradeLinkConfigModel);
	}
	/**
	 * 修改
	 * */
	@RequestMapping("/modifyTradeLinkConfig.asyn")
	@ResponseBody
	public ViewResponseBean modifyTradeLinkConfig(String json) {
		String msg = null;
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			msg = tradeLinkConfigService.modifyTradeLinkConfig(json, user);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(NullPointerException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(msg);
	}
}
