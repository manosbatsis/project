package com.topideal.mapper.sale;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.MaterialOutDepotItemDTO;
import com.topideal.entity.vo.sale.MaterialOutDepotItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface MaterialOutDepotItemMapper extends BaseMapper<MaterialOutDepotItemModel> {
	/**
	 * 根据条件查询信息
	 * @param ids
	 * @return
	 */
	List<MaterialOutDepotItemDTO> getDetailDTO(MaterialOutDepotItemDTO dto);
}
