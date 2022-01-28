package com.topideal.report.service.reporting;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.SettlementPriceWarnconfigDTO;
import com.topideal.entity.vo.reporting.SettlementPriceWarnconfigModel;

/**
 *标准成本单价预警配置信息service
 */
public interface SettlementPriceWarnconfigService {
	
	/**
	 * 标准成本单价预警配置（分页）
	 * @param model 
	 * @return
	 */
	SettlementPriceWarnconfigDTO listEmail(User user,SettlementPriceWarnconfigDTO dto) throws SQLException;
	
	/**
	 * 新增标准成本单价预警配置信息
	 * @param model
	 * @return
	 */
	Map<String, Object> saveEmail(SettlementPriceWarnconfigModel model, User user) throws Exception;
	
	/**
	 * 根据id删除标准成本单价预警配置信息(支持批量)
	 * @param ids
	 * @return
	 */
	boolean delEmail(List<Long> ids) throws SQLException;
	
	/**
	 * 修改标准成本单价预警配置
	 * @param model
	 * @return
	 */
	Map<String, Object> modifyEmail(SettlementPriceWarnconfigModel model, User user) throws Exception;
	
	/**
	 * 根据id获取标准成本单价预警配置详情
	 * @param id
	 * @return
	 */
	SettlementPriceWarnconfigDTO searchDetail(Long id) throws SQLException;
	/**
	 * 根据id禁用和启用
	 * 状态(1启用,0禁用)
	 * @return
	 * @throws SQLException
	 */
	boolean audit(Long id, String status, User user) throws SQLException;
	
	
}
