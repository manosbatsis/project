package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.CustomerQuotaConfigDTO;
import com.topideal.entity.vo.sale.CustomerQuotaConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface CustomerQuotaConfigMapper extends BaseMapper<CustomerQuotaConfigModel> {

	/**
	 * 获取分页信息
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<CustomerQuotaConfigDTO> listDTOByPage(CustomerQuotaConfigDTO dto);
	
	/**
	 *  查询生效范围内的所有信息
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<CustomerQuotaConfigDTO> listEffectiveDTO(Map<String,Object> map) ;
}
