package com.topideal.dao.purchase.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseWarehouseBatchDao;
import com.topideal.entity.vo.purchase.PurchaseWarehouseBatchModel;
import com.topideal.mapper.purchase.PurchaseWarehouseBatchMapper;

/**
 * 采购入库单商品批次详情 impl
 * 
 * @author lchenxing
 */
@Repository
public class PurchaseWarehouseBatchDaoImpl implements PurchaseWarehouseBatchDao {

	@Autowired
	private PurchaseWarehouseBatchMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<PurchaseWarehouseBatchModel> list(PurchaseWarehouseBatchModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param PurchaseWarehouseBatchModel
	 */
	@Override
	public Long save(PurchaseWarehouseBatchModel model) throws SQLException {
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
	@SuppressWarnings("rawtypes")
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
	public int modify(PurchaseWarehouseBatchModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param PurchaseWarehouseBatchModel
	 */
	@Override
	public PurchaseWarehouseBatchModel searchByPage(PurchaseWarehouseBatchModel model) throws SQLException {
		PageDataModel<PurchaseWarehouseBatchModel> pageModel = mapper.listByPage(model);
		return (PurchaseWarehouseBatchModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public PurchaseWarehouseBatchModel searchById(Long id) throws SQLException {
		PurchaseWarehouseBatchModel model = new PurchaseWarehouseBatchModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public PurchaseWarehouseBatchModel searchByModel(PurchaseWarehouseBatchModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 根据采购入库单id获取商品信息和批次信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseWarehouseBatchModel> getGoodsAndBatch(Long id) throws SQLException {
		return mapper.getGoodsAndBatch(id);
	}

	/**
	 * 根据采购入库单id获取商品信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseWarehouseBatchModel> getGoodsAndBatch2(Long id) throws SQLException {
		return mapper.getGoodsAndBatch2(id);
	}

	/**
	 * 根据商品表体id获取批次信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseWarehouseBatchModel> getGoodsAndBatch3(Map<String , Object> queryMap) throws SQLException {
		return mapper.getGoodsAndBatch3(queryMap);
	}

	
	@Override
	public List<PurchaseWarehouseBatchModel> getGoodBatchByWarehouseCodeAndGoodsNo(Map<String, Object> map)
			throws SQLException {
		return mapper.getGoodBatchByWarehouseCodeAndGoodsNo(map);
	}

	@Override
	public Integer getBatchNum(Map<String, Object> batchNumQueryMap) {
		return mapper.getBatchNum(batchNumQueryMap);
	}

}
