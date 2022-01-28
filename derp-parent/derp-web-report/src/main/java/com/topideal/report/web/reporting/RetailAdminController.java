/*
package com.topideal.report.web.reporting;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.SaleDataDTO;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.report.service.reporting.RetailAdminService;
import com.topideal.report.shiro.ShiroUtils;

*/
/**
 * 供应链周报控制器
 *
 *//*

@RequestMapping("retailAdmin")
@Controller
public class RetailAdminController {

	private static final Logger LOGGER = Logger.getLogger(RetailAdminController.class);

	@Autowired
	private RetailAdminService  retailAdminService;

	*/
/**
	 * 主页
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 *//*

	@RequestMapping("toPage.html")
	public String toPage(Model model) throws SQLException {
		List<BusinessUnitModel> buList = retailAdminService.getBuList(ShiroUtils.getUser());
		model.addAttribute("buList",buList);

		return "derp/reporting/retailpage";
	}

	@RequestMapping("/getTargetAndAchieve.asyn")
	@ResponseBody
	public ViewResponseBean getTargetAndAchieve(Long buId, String month) throws SQLException {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			User user = ShiroUtils.getUser();
			resultMap = retailAdminService.getTargetAndAchieve(buId,month,user);
		} catch (NullPointerException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

	@RequestMapping("/getBrandSaleTop.asyn")
	@ResponseBody
	public ViewResponseBean getBrandSaleTop(SaleDataDTO dto) throws SQLException {
		List<SaleDataDTO> list = null;
		try {
			User user = ShiroUtils.getUser();
			list = retailAdminService.getBrandSaleTop(dto,user);
		} catch (NullPointerException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(list);
	}

	@RequestMapping("/getCusSaleTop.asyn")
	@ResponseBody
	public ViewResponseBean getCusSaleTop(SaleDataDTO dto) throws SQLException {
		List<SaleDataDTO> list = null;
		try {
			User user = ShiroUtils.getUser();
			list = retailAdminService.getCusSaleTop(dto,user);
		} catch (NullPointerException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(list);
	}


	@RequestMapping("/getInWarehouseData.asyn")
	@ResponseBody
	public ViewResponseBean getInWarehouseData(Long buId, String month) throws SQLException {
		List<Map<String, Object>> resultMap = new ArrayList<>();// 返回的结果集
		try {
			User user = ShiroUtils.getUser();
			resultMap = retailAdminService.getInWarehouseData(buId,month,user);
		} catch (NullPointerException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

	@RequestMapping("/getInventoryAnalysisData.asyn")
	@ResponseBody
	public ViewResponseBean getInventoryAnalysisData(Long buId, String month, String type) throws SQLException {
		List<Map<String, Object>> resultMap = new ArrayList<>();// 返回的结果集
		try {
			User user = ShiroUtils.getUser();
			resultMap = retailAdminService.getInventoryAnalysisData(buId,month,type,user);
		} catch (NullPointerException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}
}
*/
