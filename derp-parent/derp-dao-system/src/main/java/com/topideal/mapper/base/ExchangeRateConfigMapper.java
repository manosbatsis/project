package com.topideal.mapper.base;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.base.ExchangeRateConfigDTO;
import com.topideal.entity.vo.base.ExchangeRateConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface ExchangeRateConfigMapper extends BaseMapper<ExchangeRateConfigModel> {

	 /**
     * 分页查询
     * @param model
     * @return
	 * @throws SQLException 
     */
	PageDataModel <ExchangeRateConfigDTO> getListByPage(ExchangeRateConfigDTO dto) throws SQLException;
	//导出
	List<Map<String, Object>> listForExport(ExchangeRateConfigModel dto) throws Exception;

}
