package com.topideal.mapper.system;

import java.util.List;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.system.CustomerInfoModel;

@MyBatisRepository
public interface CustomerInfoMapper extends BaseMapper<CustomerInfoModel> {
	
	public List<SelectBean> listAllCustomer();
	public List<CustomerInfoModel> listAllCustomerByMerchant(@Param("merchantId") Long merchantId);
	/**
	 * 获取详情
	 */
	CustomerInfoModel getDetails(CustomerInfoModel model);
}
