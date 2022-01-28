package com.topideal.mapper.sale;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.MaterialOrderItemDTO;
import com.topideal.entity.vo.sale.MaterialOrderItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface MaterialOrderItemMapper extends BaseMapper<MaterialOrderItemModel> {
	/**
	 * 根据表头Id删除不要的数据（除itemIds之外的数据）
	 * @param itemIds
	 * @param orderId
	 */
	void delBesidesIds(@Param("itemIds")List<Long> itemIds, @Param("orderId")Long orderId);
	/**
	 * 根据表头Id查询的数据（除itemIds之外的数据） 要删除商品id
	 * @param itemIds
	 * @param orderId
	 * @return
	 */
	List<MaterialOrderItemModel> getListByBesidesIds (@Param("itemIds")List<Long> itemIds, @Param("orderId")Long orderId);
	/**
	 * 根据条件查询
	 * @return
	 */
	List<MaterialOrderItemDTO> listDTO(MaterialOrderItemDTO dto);
}
