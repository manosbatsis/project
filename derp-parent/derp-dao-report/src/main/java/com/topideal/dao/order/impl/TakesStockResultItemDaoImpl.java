package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.storage.TakesStockResultItemDao;
import com.topideal.entity.vo.storage.TakesStockResultItemModel;
import com.topideal.mapper.storage.TakesStockResultItemMapper;

/**
 * 盘点结果详情表
 * @author zhanghx
 */
@Repository
public class TakesStockResultItemDaoImpl implements TakesStockResultItemDao {

	@Autowired
	private TakesStockResultItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<TakesStockResultItemModel> list(TakesStockResultItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(TakesStockResultItemModel model) throws SQLException {
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
	public int modify(TakesStockResultItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public TakesStockResultItemModel searchByPage(TakesStockResultItemModel model) throws SQLException {
		PageDataModel<TakesStockResultItemModel> pageModel = mapper.listByPage(model);
		return (TakesStockResultItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public TakesStockResultItemModel searchById(Long id) throws SQLException {
		TakesStockResultItemModel model = new TakesStockResultItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public TakesStockResultItemModel searchByModel(TakesStockResultItemModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 获取盘点结果盘盈数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> getTakesStockNumBySurplus(Map<String, Object> map) throws SQLException {
		return mapper.getTakesStockNumBySurplus(map);
	}

	/**
	 * 获取盘点结果盘亏数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> getTakesStockNumByDeficient(Map<String, Object> map) throws SQLException {
		return mapper.getTakesStockNumByDeficient(map);
	}

	/**
	 * 根据盘点结果ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getList(List ids) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getList(ids);
	}

	/**
	 * 事业部财务报表-获取盘点结果盘盈数量
	 * @param map
	 */
	public List<Map<String, Object>> getBuTakesStockSurplus(Map<String, Object> map){
		return mapper.getBuTakesStockSurplus(map);
	}

	/**
	 * 事业部财务报表-获取盘点结果盘亏数量
	 * @param map
	 */
	public List<Map<String, Object>> getBuTakesStockDeficient(Map<String, Object> map){
		return mapper.getBuTakesStockDeficient(map);
	}
	

	/**
	 * 获取(事业部财务经销存)盘盈盘亏明细表
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceTakesStockResults(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceTakesStockResults(map);
	}
	
	
}
