package com.topideal.service.base;


import com.topideal.entity.dto.base.ExchangeRateDTO;
import com.topideal.entity.vo.base.ExchangeRateModel;
import com.topideal.entity.vo.timer.CurrencyExchangeRateResponseJson;
import org.apache.ibatis.jdbc.SQL;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;


public interface RateService {
	/**
	 * 根据id查找
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	ExchangeRateDTO searchDetail(Long id) throws SQLException;

	 /**
     * 分页查询
     * @param model
     * @return
	 * @throws SQLException 
     */
	ExchangeRateDTO getListByPage(ExchangeRateDTO dto) throws SQLException;
	
	/**
     * 新增
     * @param model  新增的参数
     * @return   自增长id
     * @throws SQLException
     */
	boolean saveRate(ExchangeRateModel model) throws SQLException;

	/**
     *  修改
     * @param model  修改的实体类参数
     * @return  修改的记录数
     * @throws SQLException
     */
	boolean modifyRate(ExchangeRateModel model) throws SQLException;

	/**
     * 删除
     * @param ids  待删除id
     * @return   删除记录数
     * @throws SQLException
     */
	boolean delRate(List<Long> list) throws SQLException;
	
	/**
	 * 数据是否存在
	 * @throws SQLException 
	 */
	boolean have(ExchangeRateModel model, Long id) throws SQLException;

	//导出
	List<Map<String, Object>> listForExport(ExchangeRateDTO dto) throws Exception;

}
