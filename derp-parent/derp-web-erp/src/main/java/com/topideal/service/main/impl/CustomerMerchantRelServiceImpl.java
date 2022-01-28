package com.topideal.service.main.impl;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.dao.main.CustomerMerchantRelDao;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.service.main.CustomerMerchantRelService;
import com.topideal.shiro.ShiroUtils;

/**
 * 客户公司中间表信息service实现类
 * 
 * @author tsh
 */
@Service
public class CustomerMerchantRelServiceImpl implements CustomerMerchantRelService {

    @Autowired
    private CustomerMerchantRelDao customerMerchantRelDao;

    @Override
    public CustomerMerchantRelModel searchDetail(Long id,User user ) {
        CustomerMerchantRelModel model = new CustomerMerchantRelModel();
        model.setCustomerId(id);
        model.setMerchantId(user.getMerchantId());
        model.setStatus("1");
        return customerMerchantRelDao.searchDetail(model);
    }

	@Override
	public CustomerMerchantRelModel getRateByCustomerAndMerchant(Long merchantId, Long customerId) throws SQLException {
		
		CustomerMerchantRelModel model = new CustomerMerchantRelModel();
        model.setCustomerId(customerId);
        model.setMerchantId(merchantId);
        model.setStatus(DERP_SYS.CUSTOMER_MERCHANT_REL_status1);
        
        model = customerMerchantRelDao.searchByModel(model) ;
        
		return model;
	}
}
