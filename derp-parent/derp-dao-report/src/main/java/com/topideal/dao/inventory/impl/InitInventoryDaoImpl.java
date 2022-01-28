package com.topideal.dao.inventory.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.inventory.InitInventoryDao;
import com.topideal.entity.vo.inventory.InitInventoryModel;
import com.topideal.mapper.inventory.InitInventoryMapper;

/**
 * 期初库存 impl
 * 
 * @author lchenxing
 */
@Repository
public class InitInventoryDaoImpl implements InitInventoryDao {

	@Autowired
	private InitInventoryMapper mapper; // 期初库存mapper

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<InitInventoryModel> list(InitInventoryModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param InitInventoryModel
	 */
	@Override
	public Long save(InitInventoryModel model) throws SQLException {
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
	public int modify(InitInventoryModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param InitInventoryModel
	 */
	@Override
	public InitInventoryModel searchByPage(InitInventoryModel model) throws SQLException {
		PageDataModel<InitInventoryModel> pageModel = mapper.listByPage(model);
		return (InitInventoryModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public InitInventoryModel searchById(Long id) throws SQLException {
		InitInventoryModel model = new InitInventoryModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public InitInventoryModel searchByModel(InitInventoryModel model) throws SQLException {
		return mapper.get(model);
	}


	
	/**
	 * 商家、仓库、货号的期初库存量
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> getGoodsNoInitSum(Map<String, Object> paramMap) throws SQLException {
		return mapper.getGoodsNoInitSum(paramMap);
	}
	
}
