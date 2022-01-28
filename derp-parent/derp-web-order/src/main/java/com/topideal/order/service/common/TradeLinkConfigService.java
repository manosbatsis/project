package com.topideal.order.service.common;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.common.TradeLinkConfigDTO;
import com.topideal.entity.vo.common.TradeLinkConfigModel;

import java.sql.SQLException;

public interface TradeLinkConfigService {
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	TradeLinkConfigDTO getTradeLinkConfigListByPage(TradeLinkConfigDTO dto) throws SQLException;

	/**
	 * 删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	boolean delTradeLinkConfig(Long ids)throws Exception;

	/**
	 * 保存
	 * @param json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	String saveTradeLinkConfig(String json, User user) throws SQLException;
	/**
	 * 修改
	 * @param json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	String modifyTradeLinkConfig(String json, User user) throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	TradeLinkConfigModel searchDetail(Long id) throws SQLException;
}

