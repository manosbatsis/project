package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.MaterialOutDepotDTO;
import com.topideal.entity.vo.sale.MaterialOutDepotModel;


public interface MaterialOutDepotDao extends BaseDao<MaterialOutDepotModel>{
	/**
     * 根据条件查询（分页）
     * @return
     */
	MaterialOutDepotDTO listDTOByPage(MaterialOutDepotDTO dto) throws SQLException;
	/**
     * 根据条件查询
     * @return
     */
	List<MaterialOutDepotDTO> listDTO(MaterialOutDepotDTO dto) throws SQLException;
	
	/**
	 * 根据id查询信息
	 * @param ids
	 * @return
	 */
	MaterialOutDepotDTO getDetailDTO(MaterialOutDepotDTO dto) throws SQLException;
}
