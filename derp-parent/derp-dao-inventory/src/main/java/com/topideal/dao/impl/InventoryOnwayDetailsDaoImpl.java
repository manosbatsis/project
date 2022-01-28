package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventoryOnwayDetailsDao;
import com.topideal.entity.vo.InventoryOnwayDetailsModel;
import com.topideal.mapper.InventoryOnwayDetailsMapper;

/**
 * 库存在途明细 dao
 * 
 * @author lchenxing
 */
@Repository
public class InventoryOnwayDetailsDaoImpl implements InventoryOnwayDetailsDao {

	@Autowired
	private InventoryOnwayDetailsMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<InventoryOnwayDetailsModel> list(InventoryOnwayDetailsModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param InventoryOnwayDetailsModel
	 */
	@Override
	public Long save(InventoryOnwayDetailsModel model) throws SQLException {
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
	public int modify(InventoryOnwayDetailsModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param InventoryOnwayDetailsModel
	 */
	@Override
	public InventoryOnwayDetailsModel searchByPage(InventoryOnwayDetailsModel model) throws SQLException {
		PageDataModel<InventoryOnwayDetailsModel> pageModel = mapper.listByPage(model);
		return (InventoryOnwayDetailsModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public InventoryOnwayDetailsModel searchById(Long id) throws SQLException {
		InventoryOnwayDetailsModel model = new InventoryOnwayDetailsModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public InventoryOnwayDetailsModel searchByModel(InventoryOnwayDetailsModel model) throws SQLException {
		return mapper.get(model);
	}

}
