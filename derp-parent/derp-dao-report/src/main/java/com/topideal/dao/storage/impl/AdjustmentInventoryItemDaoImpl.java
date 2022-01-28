package com.topideal.dao.storage.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.storage.AdjustmentInventoryItemDao;
import com.topideal.entity.vo.storage.AdjustmentInventoryItemModel;
import com.topideal.mapper.storage.AdjustmentInventoryItemMapper;

/**
 * 库存调整详情表
 * @author zhanghx
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
	@SuppressWarnings("rawtypes")
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

	/**
	 * 获取库存调整数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> getAdjustmentNum(Map<String, Object> map) throws SQLException {
		return mapper.getAdjustmentNum(map);
	}

	
	/**
	 * 根据库存调整ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getList(List ids) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getList(ids);
	}

	
	/**
	 * 根据业务类型获取数量
	 */
	@Override
	public Integer getInventoryNumByModel(Map<String, Object> queryMap) {
		return mapper.getInventoryNumByModel(queryMap);
	}

	
	/**
	 * 获取唯品by po 库存调整明细
	 */
	@Override
	public List<Map<String, Object>> getVipAjuInvenDetails(Map<String, Object> queryMap) {
		return mapper.getVipAjuInvenDetails(queryMap);
	}

	@Override
	public Integer getInventorySystemNumByModel(Map<String, Object> queryMap) {
		return mapper.getInventorySystemNumByModel(queryMap);
	}

}
