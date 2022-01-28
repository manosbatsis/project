package com.topideal.mapper.sale;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.MaterialOutDepotDTO;
import com.topideal.entity.vo.sale.MaterialOutDepotModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface MaterialOutDepotMapper extends BaseMapper<MaterialOutDepotModel> {
	/**
     * 根据条件查询（分页）
     * @return
     */
	PageDataModel<MaterialOutDepotDTO> listDTOByPage(MaterialOutDepotDTO dto);
	/**
     * 根据条件查询
     * @return
     */
	List<MaterialOutDepotDTO> listDTO(MaterialOutDepotDTO dto);
	
	/**
	 * 根据id查询信息
	 * @param ids
	 * @return
	 */
	MaterialOutDepotDTO getDetailDTO(MaterialOutDepotDTO dto);
}
