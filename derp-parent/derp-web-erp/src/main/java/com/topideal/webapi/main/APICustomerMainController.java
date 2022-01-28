package com.topideal.webapi.main;

import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.main.CustomerMainDTO;
import com.topideal.service.main.CustomerMainService;
import com.topideal.webapi.form.CustomerMainForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户主服务信息 控制层
 * @author zhanghx
 */
@RestController
@RequestMapping("/webapi/system/customerMain")
@Api(tags = "待引入客商列表")
public class APICustomerMainController {
	
	// 客户信息service
	@Autowired
	private CustomerMainService customerMainService;

	/**
	 * 访问列表页面
	 * */
	/*@RequestMapping("/toPage.html")
	public String toPage() {
		return "/derp/main/customer-main-list";
	}*/

	
	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/toDetailsPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toDetailsPage(Long id)throws Exception{
		try {
			CustomerMainDTO customerInfo = customerMainService.searchDetail(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,customerInfo);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/listCustomerMain.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean listCustomer(CustomerMainForm form) {
		try{
			// 响应结果集
			CustomerMainDTO dto=new CustomerMainDTO();
			dto.setCcode(form.getCcode());
			dto.setCname(form.getCname());
			dto.setCshortname(form.getCshortname());
			dto.setIsSupplier(form.getIsSupplier());
			dto.setIsCustomer(form.getIsCustomer());
			dto.setMainStatus(form.getMainStatus());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			dto = customerMainService.listCustomerMain(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

}
