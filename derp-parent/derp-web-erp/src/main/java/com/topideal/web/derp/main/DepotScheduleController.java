package com.topideal.web.derp.main;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.vo.main.DepotScheduleModel;
import com.topideal.service.main.DepotScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 仓库附表管理 控制层
 */
@RequestMapping("/depotSchedule")
@Controller
public class DepotScheduleController {

	// 仓库附表信息service
	@Autowired
	private DepotScheduleService depotScheduleService;


	/**
	 * 根据仓库id获取下拉列表
	 */
	@RequestMapping("/loadSelectByDepotId.asyn")
	@ResponseBody
	public ViewResponseBean loadSelectByDepotId(DepotScheduleModel model) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {			
			result = depotScheduleService.loadSelectByDepotId(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}


		
}
