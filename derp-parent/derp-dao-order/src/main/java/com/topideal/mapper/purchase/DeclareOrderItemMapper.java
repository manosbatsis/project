package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.DeclareOrderItemDTO;
import com.topideal.entity.vo.purchase.DeclareOrderItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 预申报单表体 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface DeclareOrderItemMapper extends BaseMapper<DeclareOrderItemModel> {

	/**
	 * 获取已经生成过的预申报单商品
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
	Integer checkGoodsIsUse(@Param("id") Long id);

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
	PageDataModel<DeclareOrderItemDTO> getItemPopupListByPage(DeclareOrderItemDTO dto);


	List<DeclareOrderItemModel> listOrderByPrice(DeclareOrderItemModel model) throws SQLException;

	List<DeclareOrderItemDTO> listDTO(DeclareOrderItemDTO dto);
}

