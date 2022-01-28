package com.topideal.controller.log;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.service.APILogService;
import com.topideal.tools.CollectionEnum;

import net.sf.json.JSONObject;

/**
 *api接口日志-启用
 */
@RestController
@RequestMapping("/apilog")
public class APILogController {

	@Autowired
	private APILogService service;
	
	@RequestMapping("/toPage.html")
	public ModelAndView toPage(String point, String keyword, String id, String selectScope) throws Exception {
		ModelAndView modelAndView = new ModelAndView("api-logs");
		modelAndView.addObject("point", point);
		modelAndView.addObject("keyword", keyword);
		modelAndView.addObject("id", id);
		modelAndView.addObject("selectScope", selectScope);
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("/list.asyn")
	public ViewResponseBean searchLogAll(PageMongo pageMongo, String keyword, String state, String point,
			String derpCode, String endDateStr, String id, String selectScope) {
		ViewResponseBean viewResponseBean = new ViewResponseBean();
		try {
			pageMongo = service.searchLogByPage(pageMongo, keyword, state, point, derpCode, endDateStr, id, selectScope);
			// 解决主数据 返回报文 字段为null 问题
			List list = new ArrayList();
			for (int i = 0; i < pageMongo.getList().size(); i++) {
				JSONObject jsonModel = (JSONObject) pageMongo.getList().get(i);
				if(jsonModel.toString().contains(":null")){
					System.out.println("此字段为null,手动修改显示");
					jsonModel= JSONObject.fromObject(jsonModel.toString().replace(":null", ":\"此字段为null,手动修改显示\""));
				}
				list.add(jsonModel);
			}
			pageMongo.setList(list);
			viewResponseBean.setData(pageMongo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);

		}
		return ResponseFactory.success(pageMongo);
	}

	@ResponseBody
	@RequestMapping("/resetSend.asyn")
	public ViewResponseBean resetSend(String id) {
		if (StringUtils.isBlank(id)) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		ViewResponseBean viewResponseBean = new ViewResponseBean();
		try {
			service.resetSend(id, CollectionEnum.API_MONITOR.getCollectionName());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);

		}
		return ResponseFactory.success();
	}
	
	/**
	 * 批量推送
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/batchResetSend.asyn")
	public ViewResponseBean batchResetSend(String ids) {
		if (StringUtils.isBlank(ids)) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		List<String> list = StrUtils.parseIdsToStr(ids);
		try {
			service.resetSend(list ,CollectionEnum.API_MONITOR.getCollectionName());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

}
