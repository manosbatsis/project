package com.topideal.dao.main;


import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.SupplierInquiryPoolDTO;
import com.topideal.entity.vo.main.SupplierInquiryPoolModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 供应商询价池
 * @author lianchenxing
 *
 */
public interface SupplierInquiryPoolDao extends BaseDao<SupplierInquiryPoolModel> {
		
	/**
	 * 分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SupplierInquiryPoolDTO getlistByPage(SupplierInquiryPoolDTO dto) throws SQLException;


	/**
	 * 详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SupplierInquiryPoolDTO getDetails(Long id)throws SQLException;



	/**
	 * 导出 供应商询价池
	 * @param model
	 * @return
	 */
	List<SupplierInquiryPoolModel> exportList(SupplierInquiryPoolModel model) throws SQLException;


}

