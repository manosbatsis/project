package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.CustomerInfoDTO;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface CustomerInfoMapper extends BaseMapper<CustomerInfoModel> {

	/**
	 * 根据条件查询供应商下拉列表
	 * @param Long
	 * */
	List<SelectBean> getSelectBeanBySupplier(@Param("id") Long id) throws SQLException;
	
	/**
	 * 分页
	 */
	PageDataModel<CustomerInfoDTO> getListByPage(CustomerInfoDTO dto) throws SQLException;
	
	/**
	 * 根据条件查询客户下拉列表
	 * @param Long
	 * */
	//List<SelectBean> getSelectBeanByCustomer(@Param("id") Long id) throws SQLException;
	/**
	 * 根据商家ID获取客户下拉列表(必须是启用的状态)
	 * @param dto
	 * */
	List<SelectBean> getSelectBeanByMerchantId(CustomerInfoDTO dto) throws SQLException;
	/**
	 * 获取详情
	 */
	CustomerInfoModel getDetails(CustomerInfoModel model) throws SQLException;
	
	/**
	 * 获取详情
	 */
	List<CustomerInfoModel> getDetailsList(CustomerInfoModel model) throws SQLException;

	/**
	 * 导出客户信息
	 * @param id
	 * @return
	 */
	List<CustomerInfoDTO> exportCustomerMap(CustomerInfoDTO model) throws SQLException;
	

	/**
	 * 导出供应商信息
	 * @param id
	 * @return
	 */
	List<CustomerInfoDTO> exportSupplier(CustomerInfoDTO model) throws SQLException;

	/**
	 * 根据客户ID回显客户编码和主数据客户ID
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	//List<SelectBean> getNameById(Long id) throws SQLException;
	/**
	 * 获取所有启用状态的供应商下拉框
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean>  getAllSelectBeanBySupplier()throws SQLException;
	
	/**
	 * 获取客户或者供应商的公共方法
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean>  getCusOrSurpSelectBean(CustomerInfoDTO dto)throws SQLException;
	

	CustomerInfoDTO getDTODetails(Long id);

	CustomerInfoDTO getDTODetailsByRelId(@Param("relId")Long relId);

	/**
	 * 根据查询条件获取客户下拉列表(必须是启用的状态)
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<CustomerInfoDTO> getSelectBeanByDTO(CustomerInfoDTO dto) throws SQLException;

	List<CustomerInfoDTO> getCustomerByMerchantId(CustomerInfoDTO dto) throws SQLException;
	List<CustomerMerchantRelModel> getSupplierMerchantRelByMainIdURL(CustomerInfoDTO dto) throws SQLException;

	CustomerInfoDTO getCustomerByMainMerchantId(CustomerInfoDTO dto) throws SQLException;

}
