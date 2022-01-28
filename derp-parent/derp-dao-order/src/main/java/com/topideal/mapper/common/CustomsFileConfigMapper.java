package com.topideal.mapper.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.CustomsFileConfigDTO;
import com.topideal.entity.vo.common.CustomsFileConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface CustomsFileConfigMapper extends BaseMapper<CustomsFileConfigModel> {
	/**
	 * 查询所有信息 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<CustomsFileConfigDTO> listDTOByPage(CustomsFileConfigDTO dto); 
	/**
	 * 获取数量
	 * @param dto
	 * @return
	 */
	Integer getCountOrder(CustomsFileConfigDTO dto) ;
	/**
	 * 根据条件查询
	 * @param dto
	 * @return
	 */
	List<CustomsFileConfigDTO> getExportTemplate(CustomsFileConfigDTO dto);
}
