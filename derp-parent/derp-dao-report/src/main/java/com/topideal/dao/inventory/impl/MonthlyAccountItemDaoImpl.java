package com.topideal.dao.inventory.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.inventory.MonthlyAccountItemDao;
import com.topideal.entity.vo.inventory.MonthlyAccountItemModel;
import com.topideal.mapper.inventory.MonthlyAccountItemMapper;

/**
 * 月结账单商品明细 impl
 * 
 * @author lchenxing
 */
@Repository
public class MonthlyAccountItemDaoImpl implements MonthlyAccountItemDao {

	@Autowired
	private MonthlyAccountItemMapper mapper; // 月结账单商品明细

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<MonthlyAccountItemModel> list(MonthlyAccountItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param MonthlyAccountItemModel
	 */
	@Override
	public Long save(MonthlyAccountItemModel model) throws SQLException {
		model.setCreateDate(TimeUtils.getNow());
		
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
	public int modify(MonthlyAccountItemModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param MonthlyAccountItemModel
	 */
	@Override
	public MonthlyAccountItemModel searchByPage(MonthlyAccountItemModel model) throws SQLException {
		PageDataModel<MonthlyAccountItemModel> pageModel = mapper.listByPage(model);
		return (MonthlyAccountItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public MonthlyAccountItemModel searchById(Long id) throws SQLException {
		MonthlyAccountItemModel model = new MonthlyAccountItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public MonthlyAccountItemModel searchByModel(MonthlyAccountItemModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
     * 根据月结id删除明细
     * @param monthlyAccountId
     */
	@Override
	public int delItemByMonthlyAccountId(Long monthlyAccountId) {
		return mapper.delItemByMonthlyAccountId(monthlyAccountId);
	}

	@Override
	public List<Map<String, Object>> getInventoryFallingPriceReserves(Map<String, Object> queryMap) {
		return mapper.getInventoryFallingPriceReserves(queryMap) ;
	}

}
