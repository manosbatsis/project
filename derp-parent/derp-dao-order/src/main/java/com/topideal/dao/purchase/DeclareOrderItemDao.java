package com.topideal.dao.purchase;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.DeclareOrderItemDTO;
import com.topideal.entity.vo.purchase.DeclareOrderItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 预申报单表体 dao
 *
 * @author lian_
 *
 */
public interface DeclareOrderItemDao extends BaseDao<DeclareOrderItemModel> {

	/**
	 * 获取已经生成过的预申报单商品
	 *
	 * @param goodsId
	 * @param purchaseOrderId
	 * @return
	 */
	DeclareOrderItemModel getDeclareOrderItems(Map<String,Object> paramMap);

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);

	/**
	 * 导出表体
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<DeclareOrderItemDTO> getDetailsByExport(DeclareOrderItemDTO dto) throws SQLException;
	/**
	 * 选择商品弹窗
	 * @param dto
	 * @return
	 */
	DeclareOrderItemDTO getItemPopupListByPage(DeclareOrderItemDTO dto);
	/**按商品、采购单价排序
	 */
	List<DeclareOrderItemModel> listOrderByPrice(DeclareOrderItemModel model) throws SQLException;

	List<DeclareOrderItemDTO> listDTO(DeclareOrderItemDTO dto);
}
