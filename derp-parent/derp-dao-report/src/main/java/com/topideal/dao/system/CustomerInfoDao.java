package com.topideal.dao.system;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.system.CustomerInfoModel;

/**
 * 客户dao
 * @author longcheng.mao
 *
 */
public interface CustomerInfoDao extends BaseDao<CustomerInfoModel> {

	List<SelectBean> listAllCustomer() throws SQLException;
	List<CustomerInfoModel> listAllCustomerByMerchant(@Param("merchantId") Long merchantId) throws SQLException;
	/**
	 * 获取详情
	 */
	CustomerInfoModel getDetails(CustomerInfoModel model) throws SQLException;
	
}
