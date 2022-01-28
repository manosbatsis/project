package com.topideal.web.derp.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.main.CustomerBankDTO;
import com.topideal.service.main.CustomerBankService;

/**
 * 客户银行关系表
 * @author 杨创
 *
 */
@Controller
@RequestMapping("customerBank")
public class CustomerBankController {

	
	@Autowired
	private CustomerBankService customerBankService ;


	
	@RequestMapping("getCustomerBankList.asyn")
	@ResponseBody
	public ViewResponseBean getCustomerBankList(Long customerId) {
		
		try {
			CustomerBankDTO dto =new CustomerBankDTO();
			dto.setCustomerId(customerId);
			List<CustomerBankDTO> customerBankList = customerBankService.getCustomerBankList(dto) ;		
			return ResponseFactory.success(customerBankList) ;
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
	}
	
	
}
