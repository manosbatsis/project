package com.topideal.mapper.main;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.InventoryLocationMappingDTO;
import com.topideal.entity.vo.main.InventoryLocationMappingModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface InventoryLocationMappingMapper extends BaseMapper<InventoryLocationMappingModel> {

	PageDataModel<InventoryLocationMappingDTO> getListByPage(InventoryLocationMappingDTO dto);

	List<InventoryLocationMappingDTO> listDTO(InventoryLocationMappingDTO dto);



}
