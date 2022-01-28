package com.topideal.dao.purchase.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.WarehouseOrderRelDao;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.entity.vo.purchase.WarehouseOrderRelModel;
import com.topideal.mapper.purchase.WarehouseOrderRelMapper;

/**
 * 采购入库关联采购订单表 impl
 * 
 * @author lchenxing
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
		model.setCreateDate(TimeUtils.getNow());
		model.setModifyDate(TimeUtils.getNow());
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

	/**
	 * 根据采购单id获取最后入仓时间
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseWarehouseModel> getInboundDateById(Long id) throws SQLException {
		return mapper.getInboundDateById(id);
	}

	@Override
	public List<WarehouseOrderRelModel> listOrderByAudthTime(Long id) {
		return mapper.listOrderByAudthTime(id);
	}

	@Override
	public Integer countWriteOffNum(Long purchaseId, String isWriteOff) {
		return mapper.countWriteOffNum(purchaseId, isWriteOff);
	}

}
