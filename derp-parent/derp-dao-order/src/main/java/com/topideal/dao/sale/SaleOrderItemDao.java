package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleOrderItemDTO;
import com.topideal.entity.vo.sale.SaleOrderItemModel;

/**
 * 销售订单商品 dao
 * @author lian_
 */
public interface SaleOrderItemDao extends BaseDao<SaleOrderItemModel>{
	
	/**
	 * 根据表头Id删除不要的数据（除itemIds之外的数据）
	 * @param itemIds
	 * @param id 表头id
	 */
	void delBesidesIds(List<Long> itemIds, Long id);
		
	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);
	/**查询销售单商品总数量
	 * @param code 销售单号
	 * @param goodsId 商品id
	 * */
	Integer getGoodsNumBySum(Map<String, Object> map);
	
	/**
	 * 根据表头Id查询的数据（除itemIds之外的数据） 要删除销售商品id
	 * @param itemIds
	 * @param id
	 * @return
	 */
	List<SaleOrderItemModel> getListByBesidesIds(List<Long> itemIds, Long id);
	
    /**
     * 列表查询
     * @param model
     * @return
     * @throws SQLException
     */
    List<SaleOrderItemDTO> listSaleOrderItemDTO(SaleOrderItemDTO dto)throws SQLException;

	/**
	 *  根据销售单号、po号、商品货号查询销售单明细
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<SaleOrderItemModel> getDetailsByReceive (Map<String, Object> map);
	/**
	 * 根据销售id 获取上架数量和上架金额
	 * @param orderId
	 * @return
	 */
	Map<String,Object> getShelfNumAndAmountByOrderId(Long orderId);
	
	int modifyWithNull(SaleOrderItemModel model) throws SQLException;
}

