package com.topideal.dao.inventory.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.inventory.InventoryBatchDao;
import com.topideal.entity.vo.inventory.InventoryBatchModel;
import com.topideal.mapper.inventory.InventoryBatchMapper;


@Repository
public class InventoryBatchDaoImpl implements InventoryBatchDao {
	@Autowired
	private InventoryBatchMapper mapper;

	/**
	 * 新增
	 * 
	 * @param InventoryBatchVo
	 */
	@Override
	public Long save(InventoryBatchModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param List
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param List
	 */
	@Override
	public int modify(InventoryBatchModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());// 获取当前系统时间
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param InventoryBatchVo
	 */
	@Override
	public InventoryBatchModel searchByPage(InventoryBatchModel model) throws SQLException {
		PageDataModel<InventoryBatchModel> pageModel = mapper.listByPage(model);
		return (InventoryBatchModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public InventoryBatchModel searchById(Long id) throws SQLException {
		InventoryBatchModel model = new InventoryBatchModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<InventoryBatchModel> list(InventoryBatchModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 根据实体类查询
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public InventoryBatchModel searchByModel(InventoryBatchModel model) throws SQLException {
		return mapper.get(model);
	}


	@Override
	public List<Map<String, Object>> getBatchNoDetails(Map<String, Object> map) {
		return mapper.getBatchNoDetails(map);
	}

	@Override
	public List<Map<String, Object>> getRepotApiListPage(Map<String, Object> map) {
		return mapper.getRepotApiListPage(map);
	}

	@Override
	public Integer getRepotApiCount(Map<String, Object> map) {
		return mapper.getRepotApiCount(map);
	}
}
