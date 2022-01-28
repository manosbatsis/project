package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.AdjustmentInventoryItemDao;
import com.topideal.entity.vo.AdjustmentInventoryItemModel;
import com.topideal.mapper.AdjustmentInventoryItemMapper;

/**
 * 库存调整详情表
 * 
 * @author lchenxing
 */
@Repository
public class AdjustmentInventoryItemDaoImpl implements AdjustmentInventoryItemDao {

	@Autowired
	private AdjustmentInventoryItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<AdjustmentInventoryItemModel> list(AdjustmentInventoryItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(AdjustmentInventoryItemModel model) throws SQLException {
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
	public int modify(AdjustmentInventoryItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public AdjustmentInventoryItemModel searchByPage(AdjustmentInventoryItemModel model) throws SQLException {
		PageDataModel<AdjustmentInventoryItemModel> pageModel = mapper.listByPage(model);
		return (AdjustmentInventoryItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public AdjustmentInventoryItemModel searchById(Long id) throws SQLException {
		AdjustmentInventoryItemModel model = new AdjustmentInventoryItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public AdjustmentInventoryItemModel searchByModel(AdjustmentInventoryItemModel model) throws SQLException {
		return mapper.get(model);
	}

	public List<Map<String, Object>> getItemByInventoryIds(List<Long> inventoryIds) {
		return mapper.getItemByInventoryIds(inventoryIds);
	}

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	@Override
	public Integer checkGoodsIsUse(Long id) {
		return mapper.checkGoodsIsUse(id);
	}

	@Override
	public List<Map<String, Object>> getByInventoryIds(List<Long> inventoryIds) {
		return mapper.getByInventoryIds(inventoryIds);
	}

	@Override
	public Long countNoExistBu(Long id) throws SQLException {
		return mapper.countNoExistBu(id);
	}
}
