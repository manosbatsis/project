package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleOutDepotItemDTO;
import com.topideal.entity.vo.sale.SaleOutDepotItemModel;

/**
 * 销售出库表体 dao
 * @author lian_
 *
 */
public interface SaleOutDepotItemDao extends BaseDao<SaleOutDepotItemModel>{

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);
	
	List<SaleOutDepotItemDTO> listItemDTO(SaleOutDepotItemDTO dto) throws SQLException ;
	
	
	List<SaleOutDepotItemDTO> listItemByOverdueDate(SaleOutDepotItemDTO dto) throws SQLException ;
	/**生成上架入库-查询出库单商品剩余可分配余量按失效日期升序*/
	List<Map<String,Object>> listCountSurplusNum(Map<String,Object> map);
	/**
	 * 分组查询销售出库单商品id
	 * @param saleOutDepotId
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getItemGroupByOutId(Long saleOutDepotId)throws SQLException;

}

