package com.topideal.dao.inventory.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.inventory.MonthlyAccountDao;
import com.topideal.entity.vo.inventory.MonthlyAccountModel;
import com.topideal.mapper.inventory.MonthlyAccountMapper;

/**
 * 月结账单 impl
 * 
 * @author lchenxing
 */
@Repository
public class MonthlyAccountDaoImpl implements MonthlyAccountDao {

	@Autowired
	private MonthlyAccountMapper mapper; // 月结账单

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<MonthlyAccountModel> list(MonthlyAccountModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param MonthlyAccountModel
	 */
	@Override
	public Long save(MonthlyAccountModel model) throws SQLException {
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
	public int modify(MonthlyAccountModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param MonthlyAccountModel
	 */
	@Override
	public MonthlyAccountModel searchByPage(MonthlyAccountModel model) throws SQLException {
		PageDataModel<MonthlyAccountModel> pageModel = mapper.listByPage(model);
		return (MonthlyAccountModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public MonthlyAccountModel searchById(Long id) throws SQLException {
		MonthlyAccountModel model = new MonthlyAccountModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public MonthlyAccountModel searchByModel(MonthlyAccountModel model) throws SQLException {
		return mapper.get(model);
	}


	/**
	 * 统计货品货号在仓库的月结库存
	 * 
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> getBeginSumByMonthlyAccountToGoodsNo(Map<String, Object> paramMap) throws SQLException {
		return mapper.getBeginSumByMonthlyAccountToGoodsNo(paramMap);
	}



	@Override
	public List<Map<String, Object>> getMonthlyDetails(Map<String, Object> map) {
		return mapper.getMonthlyDetails(map);
	}

	

	@Override
	public List<Map<String, Object>> getMonthListGroupByBarcode(Map<String, Object> params) {
		return mapper.getMonthListGroupByBarcode(params);
	}
}
