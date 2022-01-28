package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.WarehouseOrderRelDao;
import com.topideal.entity.vo.order.WarehouseOrderRelModel;
import com.topideal.mapper.order.WarehouseOrderRelMapper;

/**
 * 采购入库关联采购订单表 impl
 * @author zhanghx
 */
@Repository
public class WarehouseOrderRelDaoImpl implements WarehouseOrderRelDao {

	@Autowired
	private WarehouseOrderRelMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<WarehouseOrderRelModel> list(WarehouseOrderRelModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param WarehouseOrderRelModel
	 */
	@Override
	public Long save(WarehouseOrderRelModel model) throws SQLException {
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
	public int modify(WarehouseOrderRelModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param WarehouseOrderRelModel
	 */
	@Override
	public WarehouseOrderRelModel searchByPage(WarehouseOrderRelModel model) throws SQLException {
		PageDataModel<WarehouseOrderRelModel> pageModel = mapper.listByPage(model);
		return (WarehouseOrderRelModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public WarehouseOrderRelModel searchById(Long id) throws SQLException {
		WarehouseOrderRelModel model = new WarehouseOrderRelModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public WarehouseOrderRelModel searchByModel(WarehouseOrderRelModel model) throws SQLException {
		return mapper.get(model);
	}

	@Override
	public List<WarehouseOrderRelModel> getAscPurchaseOrderIdList(WarehouseOrderRelModel modle)throws SQLException  {
		// TODO Auto-generated method stub
		return mapper.getAscPurchaseOrderIdList(modle);
	}

}
