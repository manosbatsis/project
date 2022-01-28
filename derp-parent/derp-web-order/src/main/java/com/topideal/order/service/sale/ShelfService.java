package com.topideal.order.service.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.ShelfDTO;

public interface ShelfService {

	/**
	 * 获取分页
	 * @param shelfDTO
	 * @return
	 * @throws SQLException
	 */
	ShelfDTO listShelf(ShelfDTO shelfDTO, User user) throws SQLException;

	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 */
	ShelfDTO searchDetail(Long id)  throws SQLException ;

	/**
	 * 根据查询条件获取条数
	 * @param shelfModel
	 * @return
	 * @throws SQLException 
	 */
	Integer getOrderCount(ShelfDTO shelfDTO, User user) throws SQLException;

	/**
	 * 获取导出列表
	 * @param shelfModel
	 * @return
	 */
	List<Map<String,Object>> getExportList(ShelfDTO shelfDTO, User user);
	/**
	 * 生成销售SD
	 * @param ids
	 * @param user
	 * @throws Exception
	 */
	List<String> generateSaleSdOrder(String ids, User user) throws Exception;
	/**
	 * 获取
	 * @param shelfDTO
	 * @return
	 * @throws SQLException
	 */
	List<ShelfDTO> listShelfDTO(ShelfDTO shelfDTO) throws Exception;
	/**
	 * 批量生成上架入库单
	 * @param shelfIds
	 * @throws Exception
	 */
	void saveShelfIdepot(String shelfIds, User user) throws Exception;
	/**
	 * 批量删除上架单
	 * @param shelfIds
	 * @throws Exception
	 */
	void delShelfOrder(String shelfIds, User user, String token) throws Exception;

}
