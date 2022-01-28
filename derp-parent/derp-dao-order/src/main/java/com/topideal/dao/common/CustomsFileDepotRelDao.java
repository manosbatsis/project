package com.topideal.dao.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.CustomsFileDepotRelDTO;
import com.topideal.entity.vo.common.CustomsFileDepotRelModel;


public interface CustomsFileDepotRelDao extends BaseDao<CustomsFileDepotRelModel>{
	/**
	 * 根据条件删除
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public int delByModel(CustomsFileDepotRelModel model) throws SQLException;
	/**
	 * 根据条件查询
	 * @param dto
	 * @return
	 * @throws SQLException
	 */	
	List<CustomsFileDepotRelDTO> listDTO(CustomsFileDepotRelDTO dto) throws SQLException;
}
