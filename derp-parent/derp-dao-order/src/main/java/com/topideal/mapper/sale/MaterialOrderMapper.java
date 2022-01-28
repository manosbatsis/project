package com.topideal.mapper.sale;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.MaterialOrderDTO;
import com.topideal.entity.vo.sale.MaterialOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface MaterialOrderMapper extends BaseMapper<MaterialOrderModel> {

	/**
     * 根据条件查询（分页）
     * @return
     */
	PageDataModel<MaterialOrderDTO> listDTOByPage(MaterialOrderDTO dto);
	/**
     * 根据条件查询
     * @return
     */
	List<MaterialOrderDTO> listDTO(MaterialOrderDTO dto);
	/**
	 * 逻辑删除
	 * @param ids
	 * @return
	 */
	int delMaterialOrder(List ids) ;
	/**
	 * 根据id查询信息
	 * @param ids
	 * @return
	 */
	MaterialOrderDTO getMaterialOrderDTO(MaterialOrderDTO dto);

}
