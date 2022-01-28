package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventoryFreezeDetailsHistoryDao;
import com.topideal.entity.vo.InventoryFreezeDetailsHistoryModel;
import com.topideal.mapper.InventoryFreezeDetailsHistoryMapper;

/**
 * 库存冻结明细 daoImpl
 * 
 * @author lchenxing
 */
@Repository
public class InventoryFreezeDetailsHistoryDaoImpl implements InventoryFreezeDetailsHistoryDao {

	@Autowired
	private InventoryFreezeDetailsHistoryMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<InventoryFreezeDetailsHistoryModel> list(InventoryFreezeDetailsHistoryModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(InventoryFreezeDetailsHistoryModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param model
	 */
	@Override
	public int modify(InventoryFreezeDetailsHistoryModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public InventoryFreezeDetailsHistoryModel searchByPage(InventoryFreezeDetailsHistoryModel model)
			throws SQLException {
		PageDataModel<InventoryFreezeDetailsHistoryModel> pageModel = mapper.listByPage(model);
		return (InventoryFreezeDetailsHistoryModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public InventoryFreezeDetailsHistoryModel searchById(Long id) throws SQLException {
		InventoryFreezeDetailsHistoryModel model = new InventoryFreezeDetailsHistoryModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public InventoryFreezeDetailsHistoryModel searchByModel(InventoryFreezeDetailsHistoryModel model)
			throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 新增历史记录
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int insertHistory() throws SQLException {
		return mapper.insertHistory();
	}
	/**
	 * 根据ids批量新增历史记录
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int insertBathHistory(List ids) throws SQLException {
		return mapper.insertBathHistory(ids);
	}

}