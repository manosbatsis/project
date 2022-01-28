package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.MaterialOrderDTO;
import com.topideal.entity.vo.sale.MaterialOrderModel;


public interface MaterialOrderDao extends BaseDao<MaterialOrderModel>{

	/**
     * 根据条件查询（分页）
     * @return
     */
	MaterialOrderDTO listDTOByPage(MaterialOrderDTO dto) throws SQLException;
	/**
     * 根据条件查询
     * @return
     */
	List<MaterialOrderDTO> listDTO(MaterialOrderDTO dto) throws SQLException;
	/**
	 * 逻辑删除
	 * @param ids
	 * @return
	 */
	int delMaterialOrder(List ids) throws SQLException;
	/**
	 * 查询信息
	 * @param ids
	 * @return
	 */
	MaterialOrderDTO getMaterialOrderDTO(MaterialOrderDTO dto) throws SQLException;
}
