package com.topideal.dao.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.CustomsFileConfigDTO;
import com.topideal.entity.vo.common.CustomsFileConfigModel;


public interface CustomsFileConfigDao extends BaseDao<CustomsFileConfigModel>{
	/**
	 * 查询所有信息 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public CustomsFileConfigDTO listDTOByPage(CustomsFileConfigDTO dto)throws SQLException;
	
	/**
	 * 获取数量
	 * @param dto
	 * @return
	 */
	public Integer getCountOrder(CustomsFileConfigDTO dto) throws SQLException;
	
	/**
	 * 根据条件查询
	 * @param dto
	 * @return
	 */
	public List<CustomsFileConfigDTO> getExportTemplate(CustomsFileConfigDTO dto);
}
