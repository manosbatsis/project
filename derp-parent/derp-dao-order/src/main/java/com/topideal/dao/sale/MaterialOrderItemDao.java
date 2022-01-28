package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.MaterialOrderItemDTO;
import com.topideal.entity.vo.sale.MaterialOrderItemModel;


public interface MaterialOrderItemDao extends BaseDao<MaterialOrderItemModel>{
		
	/**
	 * 根据表头Id删除不要的数据（除itemIds之外的数据）
	 * @param itemIds
	 * @param id 表头id
	 */
	void delBesidesIds(List<Long> itemIds, Long id) throws SQLException;
	/**
	 * 根据表头Id查询的数据（除itemIds之外的数据） 要删除商品id
	 * @param itemIds
	 * @param id
	 * @return
	 */
	List<MaterialOrderItemModel> getListByBesidesIds(List<Long> itemIds, Long id) throws SQLException;
	/**
	 * 根据条件查询
	 * @return
	 */
	List<MaterialOrderItemDTO> listDTO (MaterialOrderItemDTO dto) throws SQLException;
}
