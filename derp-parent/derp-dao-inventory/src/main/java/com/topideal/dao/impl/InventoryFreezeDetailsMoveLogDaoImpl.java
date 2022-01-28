package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryFreezeDetailsMoveLogDao;
import com.topideal.entity.vo.InventoryFreezeDetailsMoveLogModel;
import com.topideal.mapper.InventoryFreezeDetailsMoveLogMapper;

/**
 * 库存冻结明细移动日志 daoImpl
 * 
 * @author lchenxing
 */
@Repository
public class InventoryFreezeDetailsMoveLogDaoImpl implements InventoryFreezeDetailsMoveLogDao {

	@Autowired
	private InventoryFreezeDetailsMoveLogMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<InventoryFreezeDetailsMoveLogModel> list(InventoryFreezeDetailsMoveLogModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(InventoryFreezeDetailsMoveLogModel model) throws SQLException {
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
	public int modify(InventoryFreezeDetailsMoveLogModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public InventoryFreezeDetailsMoveLogModel searchByPage(InventoryFreezeDetailsMoveLogModel model)
			throws SQLException {
		PageDataModel<InventoryFreezeDetailsMoveLogModel> pageModel = mapper.listByPage(model);
		return (InventoryFreezeDetailsMoveLogModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public InventoryFreezeDetailsMoveLogModel searchById(Long id) throws SQLException {
		InventoryFreezeDetailsMoveLogModel model = new InventoryFreezeDetailsMoveLogModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public InventoryFreezeDetailsMoveLogModel searchByModel(InventoryFreezeDetailsMoveLogModel model)
			throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 清空表
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int delAll() throws SQLException {
		return mapper.delAll();
	}

	/**
     * 批量新增
     * @param list
     * @return
     * @throws SQLException
     */
	@Override
	public int insertBatch(List<InventoryFreezeDetailsMoveLogModel> list) throws SQLException {
		return mapper.insertBatch(list);
	}

	/**
     * 删除type=1
     * @return
     * @throws SQLException
     */
	@Override
	public int delByType() throws SQLException {
		return mapper.delByType();
	}

}