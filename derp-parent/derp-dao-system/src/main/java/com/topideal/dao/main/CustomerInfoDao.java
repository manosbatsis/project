package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.CustomerInfoDTO;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;


public interface CustomerInfoDao extends BaseDao<CustomerInfoModel> {

	/**
	 * 根据条件查询客户下拉列表
	 * @param Long
	 * */
	//List<SelectBean> getSelectBeanByCustomer(Long id) throws SQLException;
	
	/**
	 * 根据条件查询供应商下拉列表
	 * @param Long
	 * */
	List<SelectBean> getSelectBeanBySupplier(Long id) throws SQLException;
	
	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	CustomerInfoDTO getDetails(Long id)throws SQLException;
	/**
	 * 根据商家ID获取客户下拉列表(必须是启用的状态)
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> getSelectBeanByMerchantId(CustomerInfoDTO dto) throws SQLException;

	/**
	 * 导出客户信息
	 * @param model
	 * @return
	 */
	List<CustomerInfoDTO> exportList(CustomerInfoDTO model) throws SQLException;
	/**
	 * 导出供应商信息
	 * @param model
	 * @return
	 */
	List<CustomerInfoDTO> exportSupplierList(CustomerInfoDTO model) throws SQLException;
	
	/**
	 * 获取详情
	 */
	List<CustomerInfoModel> getDetailsList(CustomerInfoModel model) throws SQLException;
	
	//List<SelectBean> getNameById(Long id) throws SQLException;
	
	/**
	 * 获取所有启用状态下的供应商下拉框
	 * */
	List<SelectBean> getAllSelectBeanBySupplier() throws SQLException;
	/**
	 * 获取客户或者供应商的公共方法
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean>  getCusOrSurpSelectBean(CustomerInfoDTO dto)throws SQLException;
	
	CustomerInfoDTO searchDTOByPage(CustomerInfoDTO dto) throws SQLException;

	CustomerInfoDTO getDetailsByRelId(Long relId);

	/**
	 * 根据查询条件获取客户下拉列表(必须是启用的状态)
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<CustomerInfoDTO> getSelectBeanByDTO(CustomerInfoDTO dto) throws SQLException;
	List<CustomerInfoDTO> getCustomerByMerchantId(CustomerInfoDTO dto) throws SQLException;
	List<CustomerMerchantRelModel> getSupplierMerchantRelByMainIdURL(CustomerInfoDTO dto) throws SQLException;
	/**
	 * 获取商家下仓库的下拉框
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	CustomerInfoDTO getCustomerByMainMerchantId(CustomerInfoDTO dto) throws SQLException;

}

