package com.topideal.mapper.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.common.CustomsFileDepotRelDTO;
import com.topideal.entity.vo.common.CustomsFileDepotRelModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface CustomsFileDepotRelMapper extends BaseMapper<CustomsFileDepotRelModel> {
	/**
	 * 根据条件删除
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	int delByModel(CustomsFileDepotRelModel model);
	
	/**
	 * 根据条件查询
	 * @param dto
	 * @return
	 * @throws SQLException
	 */	
	List<CustomsFileDepotRelDTO> listDTO(CustomsFileDepotRelDTO dto);

}
