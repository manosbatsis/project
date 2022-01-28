package com.topideal.dao.common;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.SdSaleTypeDTO;
import com.topideal.entity.vo.common.SdSaleTypeModel;

import java.sql.SQLException;
import java.util.List;


public interface SdSaleTypeDao extends BaseDao<SdSaleTypeModel>{
		
	SdSaleTypeDTO listDTOByPage(SdSaleTypeDTO dto) throws SQLException;
	/**
	 * 查询SD类型列表
	 * @param
	 * */
	List<SdSaleTypeDTO> listDTO(SdSaleTypeDTO dto) throws SQLException;
}
