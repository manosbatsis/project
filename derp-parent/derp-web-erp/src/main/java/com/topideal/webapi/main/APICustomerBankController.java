package com.topideal.webapi.main;

import com.topideal.entity.dto.main.CustomerBankDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.service.main.CustomerBankService;
import com.topideal.shiro.ShiroUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户银行信息
 * @author 杨创
 *
 */
@RestController
@RequestMapping("/webapi/system/customerBank")
@Api(tags = "客户银行信息")
public class APICustomerBankController {


	@Autowired
	private CustomerBankService customerBankService ;

	


	@ApiOperation(value = "获取客户银行信息回显")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "customerId", value = "客户id", required = true)
	})
	@PostMapping(value="/getCustomerBankList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<CustomerBankDTO> getCustomerBankList(String token,Long customerId) {

		try {
			CustomerBankDTO dto =new CustomerBankDTO();
			dto.setCustomerId(customerId);
			List<CustomerBankDTO> customerBankList = customerBankService.getCustomerBankList(dto) ;
			User user = ShiroUtils.getUserByToken(token);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,customerBankList);//成功

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}


	@ApiOperation(value = "根据商家+供应商获取客户银行信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "supplierId", value = "供应商id", required = true)
	})
	@PostMapping(value="/getCustomerBankInfo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getCustomerBankInfo(String token,Long supplierId) {

		try {
			User user=ShiroUtils.getUserByToken(token);

			List<CustomerBankDTO> list=customerBankService.getCustomerBySupplierId(supplierId,user.getMerchantId());

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}


	/**
	 * 根据id查看客户银行信息
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "根据id查看客户银行信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/getCustomerBankById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<CustomerBankDTO> getCustomerBankById(String token,Long id) {

		try {
			CustomerBankDTO customerBankDTO=customerBankService.getCustomerBankById(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,customerBankDTO);//成功

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

}
