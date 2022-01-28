package com.topideal.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.constant.DerpBasic;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.ViewResponseBean;

@Controller
@RequestMapping("/index")
public class IndexController {

	 /**
     * 根据名称获取常量集合
     * */
    @RequestMapping("/getConstantListByName.asyn")
	@ResponseBody
	public ViewResponseBean getConstantListByName(String listName){
    	ArrayList<DerpBasic> list = DERP_LOG.getConstantListByName(listName);
    	
    	return ResponseFactory.success(list);
    }
}
