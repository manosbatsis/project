package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.system.CustomerInfoModel;

/**
 * 客户信息service
 * @author longcheng.mao
 *
 */
public interface CustomerService {

	/**
	 * 获取使用中的客户列表
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> listAllCustomer() throws SQLException;
	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	CustomerInfoModel getDetails(Long id)throws SQLException;
}
