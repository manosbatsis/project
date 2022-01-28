package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.CustomerQuotaConfigDTO;
import com.topideal.entity.vo.sale.CustomerQuotaConfigModel;


public interface CustomerQuotaConfigDao extends BaseDao<CustomerQuotaConfigModel> {
		
	/**
	 * 获取分页信息
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	CustomerQuotaConfigDTO listDTOByPage(CustomerQuotaConfigDTO dto) throws SQLException;
	
	/**
	 *  查询生效范围内的所有信息
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<CustomerQuotaConfigDTO> listEffectiveDTO(Map<String,Object> map) throws SQLException;

}
