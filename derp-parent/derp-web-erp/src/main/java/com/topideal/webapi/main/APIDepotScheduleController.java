package com.topideal.webapi.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.vo.main.DepotScheduleModel;
import com.topideal.service.main.DepotScheduleService;
import com.topideal.webapi.form.DepotScheduleForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 仓库附表管理 控制层
 */
@RestController
@RequestMapping("/webapi/system/depotSchedule")
@Api(tags = "仓库附表管理 ")
public class APIDepotScheduleController {

	// 仓库附表信息service
	@Autowired
	private DepotScheduleService depotScheduleService;


	/**
	 * 根据仓库id获取下拉列表
	 */
	@ApiOperation(value = "根据仓库id获取下拉列表",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@PostMapping("/loadSelectByDepotId.asyn")
	public ResponseBean loadSelectByDepotId(DepotScheduleForm form) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			DepotScheduleModel model=new DepotScheduleModel();
			model.setId(form.getId());
			model.setDepotId(form.getDepotId());
			model.setDepotName(form.getDepotName());
			model.setAddress(form.getAddress());			
			result = depotScheduleService.loadSelectByDepotId(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}


		
}
