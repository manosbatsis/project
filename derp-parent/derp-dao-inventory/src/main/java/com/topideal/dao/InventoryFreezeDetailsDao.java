package com.topideal.dao;

import com.topideal.entity.vo.InventoryFreezeDetailsModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 库存冻结明细 dao
 *
 * @author lian_
 *
 */
public interface InventoryFreezeDetailsDao extends BaseDao<InventoryFreezeDetailsModel> {

	/**
	 * 查询 当前商家下仓库下的商品冻结数量
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	InventoryFreezeDetailsModel getInventoryFreezeNum(InventoryFreezeDetailsModel model) throws SQLException;

	/**
	 * 导出商品收发明细
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> exportInventoryFreezeDetailsMap(InventoryFreezeDetailsModel model) throws Exception;

	/**
	 * 根据条件获取冻结记录
	 *
	 * @param ifdmodel
	 * @return
	 */
	List<InventoryFreezeDetailsModel> listFreezeDetails(InventoryFreezeDetailsModel ifdmodel) throws Exception;

	/**
	 * 查询冻结的单号，不重复
	 *
	 * @param model
	 * @return
	 */
	InventoryFreezeDetailsModel getOrderNoByPage(InventoryFreezeDetailsModel model) throws Exception;

	/**
	 * 根据移动类型删除数据
	 * @return
	 * @throws SQLException
	 */
	int delByMoveLogType() throws SQLException;

	/**
	 * 根据ids批量新增历史记录
	 * @return
	 * @throws SQLException
	 */
	int insertBath(List ids) throws SQLException;

}
