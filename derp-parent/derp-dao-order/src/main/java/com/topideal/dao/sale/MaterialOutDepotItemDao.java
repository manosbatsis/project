package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.MaterialOutDepotItemDTO;
import com.topideal.entity.vo.sale.MaterialOutDepotItemModel;


public interface MaterialOutDepotItemDao extends BaseDao<MaterialOutDepotItemModel>{
		
	/**
	 * 根据条件查询信息
	 * @param ids
	 * @return
	 */
	List<MaterialOutDepotItemDTO> getDetailDTO(MaterialOutDepotItemDTO dto) throws SQLException;
}
