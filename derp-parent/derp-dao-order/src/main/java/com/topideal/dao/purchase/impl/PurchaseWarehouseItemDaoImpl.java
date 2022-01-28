package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseWarehouseItemDao;
import com.topideal.entity.dto.purchase.PurchaseWriteOffItemDTO;
import com.topideal.entity.vo.purchase.PurchaseWarehouseItemModel;
import com.topideal.mapper.purchase.PurchaseWarehouseItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购入库商品列表 impl
 * 
 * @author lchenxing
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
	 * 根据勾选的入库单id查询入库勾稽明细
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseWarehouseItemModel> getDetails(List list) throws SQLException {
		return mapper.getDetails(list);
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
	 * 查询入库单商品、数量按商品分组合并
	 * */
	public List<Map<String, Object>> getWarehouseItem(Map<String,Object> paramMap){
		return mapper.getWarehouseItem(paramMap);
	}

	@Override
	public List<PurchaseWriteOffItemDTO> getDetailsByIds(List<Long> ids) throws SQLException {
		return mapper.getDetailsByIds(ids);
	}


}
