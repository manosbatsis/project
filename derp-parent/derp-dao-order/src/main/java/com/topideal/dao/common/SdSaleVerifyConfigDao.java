package com.topideal.dao.common;

import java.sql.SQLException;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.SdSaleVerifyConfigDTO;
import com.topideal.entity.vo.common.SdSaleVerifyConfigModel;


public interface SdSaleVerifyConfigDao extends BaseDao<SdSaleVerifyConfigModel>{
		
	/**
	 * 分页获取
	 * @param dto
	 * @return
	 */
	SdSaleVerifyConfigDTO listDTOByPage(SdSaleVerifyConfigDTO dto) throws SQLException ;
	/**
	 * 获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SdSaleVerifyConfigDTO searchDetail(Long id) throws SQLException ;

}
