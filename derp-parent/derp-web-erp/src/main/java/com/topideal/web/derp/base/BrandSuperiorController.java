package com.topideal.web.derp.base;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.BrandSuperiorDTO;
import com.topideal.entity.vo.base.BrandSuperiorModel;
import com.topideal.service.base.BrandSuperiorService;
import com.topideal.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * 母品牌控制层
 */
@RequestMapping("/brandSuperior")
@Controller
public class BrandSuperiorController {

	@Autowired
	private BrandSuperiorService brandSuperiorService;


	/**
	 * 访问列表页面
	 * @throws SQLException 
	 */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws SQLException {
		return "/derp/base/brand-superior-list";
	}


	/**
	 * 获取分页数据
	 */
	@RequestMapping("/list.asyn")
	@ResponseBody
	public ViewResponseBean list(BrandSuperiorDTO dto) {
		try {
            dto = brandSuperiorService.listByPage(dto);
		}catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		}  catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 新增
	 */
	@RequestMapping("/save.asyn")
	@ResponseBody
	public ViewResponseBean save(BrandSuperiorModel model) {
		try {
			User user= ShiroUtils.getUser();
			brandSuperiorService.save(model,user);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 根据id获取数据
	 */
	@RequestMapping("/findById.asyn")
	@ResponseBody
	public ViewResponseBean findById(Long id) {
		BrandSuperiorModel model = null;
		try {
			User user= ShiroUtils.getUser();
			model = brandSuperiorService.findById(id);
		}catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		}  catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}

	/**
	 * 编辑
	 */
	@RequestMapping("/modify.asyn")
	@ResponseBody
	public ViewResponseBean modify(BrandSuperiorModel model) {
		try {
			User user= ShiroUtils.getUser();
			brandSuperiorService.modify(model,user);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 删除
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/delete.asyn")
	@ResponseBody
	public ViewResponseBean delete(String ids) {
		try {
			List<Long> list = StrUtils.parseIds(ids);
			brandSuperiorService.delete(list);
		}catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		}  catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	/**
	 * 获取下拉列表
	 *
	 * @return
	 */
	@RequestMapping("/getSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBean() {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			result = brandSuperiorService.getSelectBean();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

}
