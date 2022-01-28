package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.PurchaseWarehouseItemDao;
import com.topideal.entity.vo.order.PurchaseAnalysisModel;
import com.topideal.entity.vo.order.PurchaseWarehouseItemModel;
import com.topideal.mapper.order.PurchaseWarehouseItemMapper;

/**
 * 采购入库商品列表 impl
 * @author zhanghx
 */
@Repository
public class PurchaseWarehouseItemDaoImpl implements PurchaseWarehouseItemDao {

	@Autowired
	private PurchaseWarehouseItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<PurchaseWarehouseItemModel> list(PurchaseWarehouseItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param PurchaseWarehouseItemModel
	 */
	@Override
	public Long save(PurchaseWarehouseItemModel model) throws SQLException {
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
	public int modify(PurchaseWarehouseItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param PurchaseWarehouseItemModel
	 */
	@Override
	public PurchaseWarehouseItemModel searchByPage(PurchaseWarehouseItemModel model) throws SQLException {
		PageDataModel<PurchaseWarehouseItemModel> pageModel = mapper.listByPage(model);
		return (PurchaseWarehouseItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public PurchaseWarehouseItemModel searchById(Long id) throws SQLException {
		PurchaseWarehouseItemModel model = new PurchaseWarehouseItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public PurchaseWarehouseItemModel searchByModel(PurchaseWarehouseItemModel model) throws SQLException {
		return mapper.get(model);
	}


	/**
	 * 分组查询采购入库数量（允许传付款主体查询）
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseWarehouseItemModel> getWarehouseNumGroupBy(Map<String, Object> paramMap) throws SQLException {
		return mapper.getWarehouseNumGroupBy(paramMap);
	}

	/**
	 * 根据采购入库单表体对象查查采购入库单
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String,Object>> getList(Long id) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getList(id);
	}


	/**
	 *事业部财务经销存采购入库加权
	 */
	@Override
	public List<Map<String,Object>> getBuPurWarehouseAndAnalysisWeighte(Map<String, Object> paramMap) throws SQLException {
		return mapper.getBuPurWarehouseAndAnalysisWeighte(paramMap);
	}

	/**
	 *事业部 财务经销存)获取采购残损明细表 来货残损数据
	 */
	@Override
	public List<Map<String, Object>> getBuFinancePurchaseDamagedWornExpireList(Map<String, Object> paramMap)
			throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getBuFinancePurchaseDamagedWornExpireList(paramMap);
	}
}
