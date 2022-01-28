package com.topideal.service.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.main.InventoryLocationMappingDTO;
import com.topideal.entity.vo.main.InventoryLocationMappingModel;

public interface InventoryLocationMappingService {

	/**
	 * 获取商家列表
	 * @param user
	 * @return
	 */
	List<SelectBean> getMerchantList(User user);

	/**
	 * 获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	InventoryLocationMappingModel seachById(Long id) throws SQLException;

	/**
	 * 获取分页
	 * @param dto
	 * @return
	 */
	InventoryLocationMappingDTO listInventoryLocationMapping(InventoryLocationMappingDTO dto);

	/**
	 * 新增或修改
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	int saveOrModify(InventoryLocationMappingModel model,User user) throws SQLException;

	/**
	 * 删除
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	boolean deleteInventoryLocationMapping(List<Long> list) throws SQLException;

	/**
	 * 获取导出
	 * @param dto
	 * @param ids 
	 * @return
	 */
	List<InventoryLocationMappingDTO> exportInventoryLocationMapping(InventoryLocationMappingDTO dto);

	/**
	 * 导入
	 * @param data
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("rawtypes")
	Map importInventoryLocationMapping(List<Map<String, String>> data, User user) throws SQLException;

	/**
	 * 前端js-获取原商品ID
	 * @param dto
	 * @return
	 * @throws Exception 
	 */
	InventoryLocationMappingDTO getOriginalGoodsId(User user,InventoryLocationMappingDTO dto) throws Exception;
	/**
	 * 修改 原货号指定出库货号
	 * @param originalGoodsId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object>updateNotSettlement(Long id) throws Exception;

}
