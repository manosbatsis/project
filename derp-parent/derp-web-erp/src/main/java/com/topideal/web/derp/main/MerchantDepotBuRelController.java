package com.topideal.web.derp.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.vo.main.MerchantDepotBuRelModel;
import com.topideal.service.main.BusinessUnitService;
import com.topideal.service.main.MerchantBuRelService;
import com.topideal.service.main.MerchantDepotBuRelService;
import com.topideal.service.main.MerchantInfoService;

@Controller
@RequestMapping("merchantDepotBuRel")
public class MerchantDepotBuRelController {
	
	@Autowired
	BusinessUnitService businessUnitService ;
	@Autowired
	MerchantInfoService merchantInfoService ;
	@Autowired
	MerchantBuRelService merchantBuRelService ;
	@Autowired
	MerchantDepotBuRelService merchantDepotBuRelService;
	
	@RequestMapping("/getSelectInfoByMerchantId.asyn")
	@ResponseBody
	public ViewResponseBean getSelectInfoByMerchantId(MerchantDepotBuRelModel model) {
		
		try{
			String buIds =  merchantDepotBuRelService.getSelectInfoByMerchantId(model) ;
			
			return ResponseFactory.success(buIds);
			
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		
	}
	
	@RequestMapping("/getBuNameByMerchantDepot.asyn")
	@ResponseBody
	public ViewResponseBean getBuNameByMerchantDepot(MerchantDepotBuRelModel model) {
		
		try{
			String buName=  merchantDepotBuRelService.getBuNameByMerchantDepot(model) ;
			
			return ResponseFactory.success(buName);
			
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		
	}
}
