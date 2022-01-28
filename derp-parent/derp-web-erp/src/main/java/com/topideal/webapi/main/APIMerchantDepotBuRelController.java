package com.topideal.webapi.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.vo.main.MerchantDepotBuRelModel;
import com.topideal.service.main.BusinessUnitService;
import com.topideal.service.main.MerchantBuRelService;
import com.topideal.service.main.MerchantDepotBuRelService;
import com.topideal.service.main.MerchantInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/webapi/system/merchantDepotBuRel")
@Api(tags = "商家仓库事业部关系")
public class APIMerchantDepotBuRelController {
	
	@Autowired
	BusinessUnitService businessUnitService ;
	@Autowired
	MerchantInfoService merchantInfoService ;
	@Autowired
	MerchantBuRelService merchantBuRelService ;
	@Autowired
	MerchantDepotBuRelService merchantDepotBuRelService;
	
	@ApiOperation(value = "根据商家和仓库,获取商家仓库事业部表id数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "merchantId", value = "商家id", required = true),
		@ApiImplicitParam(name = "depotId", value = "仓库id", required = true)
	})
	@PostMapping(value="/getSelectInfoByMerchantId.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getSelectInfoByMerchantId(Long merchantId,Long depotId) {		
		try{
			MerchantDepotBuRelModel model=new MerchantDepotBuRelModel();
			model.setMerchantId(merchantId);
			model.setDepotId(depotId);
			String buIds =  merchantDepotBuRelService.getSelectInfoByMerchantId(model) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,buIds);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
	
	@RequestMapping("/getBuNameByMerchantDepot.asyn")
	@ApiOperation(value = "根据商家和仓库,获取商家仓库事业部表名称数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "merchantId", value = "商家id", required = true),
		@ApiImplicitParam(name = "depotId", value = "仓库id", required = true)
	})
	@PostMapping(value="/getBuNameByMerchantDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getBuNameByMerchantDepot(Long merchantId,Long depotId) {
		
		try{
			MerchantDepotBuRelModel model=new MerchantDepotBuRelModel();
			model.setMerchantId(merchantId);
			model.setDepotId(depotId);
			String buName=  merchantDepotBuRelService.getBuNameByMerchantDepot(model) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,buName);//成功			
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
}
