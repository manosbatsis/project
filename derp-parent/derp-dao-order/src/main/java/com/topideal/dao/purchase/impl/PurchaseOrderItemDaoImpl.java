package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.mapper.purchase.PurchaseOrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购订单表体 impl
 * 
 * @author lianchenxing
 */
@Repository
public class PurchaseOrderItemDaoImpl implements PurchaseOrderItemDao {

	@Autowired
	private PurchaseOrderItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<PurchaseOrderItemModel> list(PurchaseOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param PurchaseOrderItemDTO
	 */
	@Override
	public Long save(PurchaseOrderItemModel model) throws SQLException {
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
	public int modify(PurchaseOrderItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param PurchaseOrderItemDTO
	 */
	@Override
	public PurchaseOrderItemModel searchByPage(PurchaseOrderItemModel model) throws SQLException {
		PageDataModel<PurchaseOrderItemModel> pageModel = mapper.listByPage(model);
		return (PurchaseOrderItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public PurchaseOrderItemModel searchById(Long id) throws SQLException {
		PurchaseOrderItemModel model = new PurchaseOrderItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public PurchaseOrderItemModel searchByModel(PurchaseOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 根据多条采购订单条件查询
	 * 
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseOrderItemModel> getByOrderIds(List ids) throws SQLException {
		return mapper.getByOrderIds(ids);
	}

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	@Override
	public Integer checkGoodsIsUse(Long id) {
		return mapper.checkGoodsIsUse(id);
	}
	/**
	 * 查询采购单商品采购数量 按商品分组
	 * */
	public List<Map<String,Object>> getGoodsNumGroupBy(Map<String,Object> map){
		return mapper.getGoodsNumGroupBy(map);
	}

	@Override
	public Map<String, Object> countNumByGoodsNoAndIds(Map<String, Object> queryMap) {
		return mapper.countNumByGoodsNoAndIds(queryMap);
	}

	@Override
	public List<PurchaseOrderItemModel> getPurchaseOrderItem(PurchaseOrderItemModel model) {
		return mapper.getPurchaseOrderItem(model);
	}

	@Override
	public Map<String, Object> statisticsByIds(List<Long> idList) {
		return mapper.statisticsByIds(idList);
	}

	@Override
	public int countByOrderId(Long orderId) {
		return mapper.countByOrderId(orderId);
	}
}
