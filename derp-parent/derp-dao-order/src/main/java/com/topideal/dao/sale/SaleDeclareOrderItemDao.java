package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleDeclareOrderItemDTO;
import com.topideal.entity.vo.sale.SaleDeclareOrderItemModel;


public interface SaleDeclareOrderItemDao extends BaseDao<SaleDeclareOrderItemModel>{
		
	/**
	 * 根据orderId删除表体信息
	 * @param orderId
	 * @throws SQLException
	 */
	int delByOrderId(Long orderId) throws SQLException;
	/**
	 *  获取销售订单对应商品的总申报数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
    Integer getToTalDeclareNum (Map<String, Object> map) throws SQLException;
    /**
     * 查询所有数据
     * @return
     */
    List<SaleDeclareOrderItemDTO> listDTO(SaleDeclareOrderItemDTO dto)throws SQLException;
    /**
   	 *  获取销售商品弹窗
   	 * @param map
   	 * @return
   	 * @throws SQLException
   	 */
     SaleDeclareOrderItemDTO getSalePopupListByPage (SaleDeclareOrderItemDTO dto) throws SQLException;
	/**
	 * 查询所有数据 按价格排序
	 * @return
	 */
	List<SaleDeclareOrderItemModel> listOrderByPrice(SaleDeclareOrderItemModel model)throws SQLException;
}
