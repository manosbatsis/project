package com.topideal.order.web.common;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.common.SdTypeConfigDTO;
import com.topideal.entity.vo.common.SdTypeConfigModel;
import com.topideal.order.service.common.SdTypeService;
import com.topideal.order.shiro.ShiroUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;

/**
 * sd类型配置
 * @author gy
 *
 */

@Controller
@RequestMapping("/sdTypeConfig")
public class SdTypeConfigController {
	
	private static final Logger LOG = Logger.getLogger(SdTypeConfigController.class) ;
	
	@Autowired
	private SdTypeService sdTypeService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/common/sd-type-config-list";
	}
	
	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@RequestMapping("/sdTypeConfigList.asyn")
	@ResponseBody
	private ViewResponseBean sdTypeConfigList(SdTypeConfigDTO dto) {
		try{
			// 响应结果集
			dto = sdTypeService.getSdTypeConfigListByPage(dto);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
	@RequestMapping("saveOrModify.asyn")
	@ResponseBody
	public ViewResponseBean saveOrModify(SdTypeConfigModel model) {
		try{
			User user = ShiroUtils.getUser();
			// 响应结果集
			sdTypeService.saveOrModify(model, user);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success();
	}
	
	@RequestMapping("detail.asyn")
	@ResponseBody
	public ViewResponseBean detail(SdTypeConfigModel model) {
		try{
			// 响应结果集
			model = sdTypeService.searchByModel(model);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	/**
	 * 获取已启用配置
	 * @param model
	 * @return
	 */
	@RequestMapping("getList.asyn")
	@ResponseBody
	public ViewResponseBean getList(SdTypeConfigModel model) {
		
		List<SdTypeConfigModel> list = null ;
		
		try {
			list = sdTypeService.getList(model) ;
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		
		return ResponseFactory.success(list);
	}
}
