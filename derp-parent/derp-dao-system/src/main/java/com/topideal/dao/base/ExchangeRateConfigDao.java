package com.topideal.dao.base;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.base.ExchangeRateConfigDTO;
import com.topideal.entity.vo.base.ExchangeRateConfigModel;


public interface ExchangeRateConfigDao extends BaseDao<ExchangeRateConfigModel>{
		

	 /**
     * 分页查询
     * @param model
     * @return
	 * @throws SQLException 
     */
	ExchangeRateConfigDTO getListByPage(ExchangeRateConfigDTO dto) throws SQLException;
	
	//导出
	List<Map<String, Object>> listForExport(ExchangeRateConfigModel dto) throws Exception;

}
