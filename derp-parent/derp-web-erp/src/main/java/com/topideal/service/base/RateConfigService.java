package com.topideal.service.base;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.base.ExchangeRateConfigDTO;
import com.topideal.entity.vo.base.ExchangeRateConfigModel;


public interface RateConfigService {

	 /**
     * 分页查询
     * @param model
     * @return
	 * @throws SQLException 
     */
	ExchangeRateConfigDTO getListByPage(ExchangeRateConfigDTO dto) throws SQLException;
	
	/**
     * 新增
     * @param model  新增的参数
     * @return   自增长id
     * @throws SQLException
     */
	boolean saveRate(ExchangeRateConfigModel model) throws SQLException;

	/**
     * 删除
     * @param ids  待删除id
     * @return   删除记录数
     * @throws SQLException
     */
	boolean delRateConfig(List<Long> list) throws SQLException;
	
	//导出
	List<Map<String, Object>> listForExport(ExchangeRateConfigModel dto) throws Exception;
	
	//单个查询
	ExchangeRateConfigModel serchByModel(ExchangeRateConfigModel model)throws Exception;

}
