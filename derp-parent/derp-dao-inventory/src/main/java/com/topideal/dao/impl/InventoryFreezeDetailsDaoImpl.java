package com.topideal.dao.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventoryFreezeDetailsDao;
import com.topideal.entity.vo.InventoryFreezeDetailsModel;
import com.topideal.mapper.InventoryFreezeDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 库存冻结明细 impl
 *
 * @author lchenxing
 */
@Repository
public class InventoryFreezeDetailsDaoImpl implements InventoryFreezeDetailsDao {

	@Autowired
	private InventoryFreezeDetailsMapper mapper; // 库存冻结明细

	/**
	 * 列表查询
	 *
	 * @param model
	 */
	@Override
	public List<InventoryFreezeDetailsModel> list(InventoryFreezeDetailsModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 *
	 * @param model
	 */
	@Override
	public Long save(InventoryFreezeDetailsModel model) throws SQLException {
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
	public int modify(InventoryFreezeDetailsModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 *
	 * @param model
	 */
	@Override
	public InventoryFreezeDetailsModel searchByPage(InventoryFreezeDetailsModel model) throws SQLException {
		PageDataModel<InventoryFreezeDetailsModel> pageModel = mapper.listByPage(model);
		return (InventoryFreezeDetailsModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 *
	 * @param id
	 */
	@Override
	public InventoryFreezeDetailsModel searchById(Long id) throws SQLException {
		InventoryFreezeDetailsModel model = new InventoryFreezeDetailsModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 *
	 * @param model
	 */
	@Override
	public InventoryFreezeDetailsModel searchByModel(InventoryFreezeDetailsModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 查询 当前商家下仓库下的商品冻结数量
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public InventoryFreezeDetailsModel getInventoryFreezeNum(InventoryFreezeDetailsModel model) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getInventoryFreezeNum(model);
	}

	@Override
	public List<Map<String, Object>> exportInventoryFreezeDetailsMap(InventoryFreezeDetailsModel model)
			throws Exception {
		// TODO Auto-generated method stub
		return mapper.exportInventoryFreezeDetailsMap(model);
	}

	@Override
	public List<InventoryFreezeDetailsModel> listFreezeDetails(InventoryFreezeDetailsModel model) throws Exception {
		return mapper.listFreezeDetails(model);
	}

	/**
	 * 查询冻结的单号，不重复
	 *
	 * @param model
	 * @return
	 */
	@Override
	public InventoryFreezeDetailsModel getOrderNoByPage(InventoryFreezeDetailsModel model) throws Exception {
		PageDataModel<InventoryFreezeDetailsModel> pageModel = mapper.getOrderNoByPage(model);
		return (InventoryFreezeDetailsModel) pageModel.getModel();
	}

	/**
	 * 根据移动类型删除数据
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int delByMoveLogType() throws SQLException {
		return mapper.delByMoveLogType();
	}

	@Override
	public int insertBath(List ids) throws SQLException {
		return mapper.insertBath(ids);
	}

}
