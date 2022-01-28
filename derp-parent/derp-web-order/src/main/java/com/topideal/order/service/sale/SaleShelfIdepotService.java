package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.SaleShelfIdepotDTO;
import com.topideal.entity.dto.sale.SaleShelfIdepotItemDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SaleShelfIdepotService {

	/**
	 * 获取分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SaleShelfIdepotDTO listSaleShelfIdepot(SaleShelfIdepotDTO dto, User user) throws SQLException;

	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 */
	SaleShelfIdepotDTO searchDetail(Long id) throws SQLException;

	/**
	 * 根据ID获取明细
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<SaleShelfIdepotItemDTO> listItemBySaleShelfIdepotId(Long id) throws SQLException;

	/**
	 * 根据查询条件获取条数
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	Integer getOrderCount(SaleShelfIdepotDTO dto, User user) throws SQLException;

	/**
	 * 获取导出列表
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getExportList(SaleShelfIdepotDTO dto, User user);

}
