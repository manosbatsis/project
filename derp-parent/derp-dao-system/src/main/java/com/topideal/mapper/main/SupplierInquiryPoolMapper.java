package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.SupplierInquiryPoolDTO;
import com.topideal.entity.vo.main.SupplierInquiryPoolModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 供应商询价池
 * @author lianchenxing
 *
 */
@MyBatisRepository
public interface SupplierInquiryPoolMapper extends BaseMapper<SupplierInquiryPoolModel> {

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<SupplierInquiryPoolDTO> getlistByPage(SupplierInquiryPoolDTO model) throws SQLException;
	/**
	 * 导出供应商询价池
	 * @param id
	 * @return
	 */
	List<SupplierInquiryPoolModel> exportSupplierInquiry(SupplierInquiryPoolModel model) throws SQLException;


	/**
	 * 详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SupplierInquiryPoolDTO getDetails(@Param("id")Long id) throws SQLException;
}

