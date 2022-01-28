package com.topideal.dao.impl;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.MonthlyAccountDao;
import com.topideal.entity.dto.MonthlyAccountDTO;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.mapper.MonthlyAccountMapper;

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
	 * 分页查询 数据DTO
	 * 
	 * @param MonthlyAccountModel
	 */
	@Override
	public MonthlyAccountDTO searchDTOByPage(MonthlyAccountDTO model) throws Exception {
		PageDataModel<MonthlyAccountDTO> pageModel = mapper.listDTOByPage(model);
		return (MonthlyAccountDTO) pageModel.getModel();
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
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public MonthlyAccountDTO searchDTOById(Long id) throws Exception {
		MonthlyAccountDTO model = new MonthlyAccountDTO();
		model.setId(id);
		return mapper.searchDTOById(model);
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
     *  用于库存新增扣减接口 根据归属月份查询未结转的账单
     * @param monthlyAccountModel
     * @return
     * @throws Exception
     */
	@Override
	public List<MonthlyAccountModel> getMonthlyAccountListByMonth(MonthlyAccountModel monthlyAccountModel)throws Exception{
		// TODO Auto-generated method stub
		return mapper.getMonthlyAccountListByMonth(monthlyAccountModel);
	}

	@Override
	public List<Map<String, Object>> exportMonthlyAccountMap(MonthlyAccountModel monthlyAccountModel)
			throws SQLException {
		// TODO Auto-generated method stub
		return mapper.exportMonthlyAccountMap(monthlyAccountModel);
	}

	@Override
	public List<MonthlyAccountModel> getMonthlyAccount(MonthlyAccountModel monthlyAccountModel)
			throws SQLException {
		return mapper.getMonthlyAccount(monthlyAccountModel);
	}
	
	/**
	 * 获取上个月的 月结数量大于0的商家和仓库
	 */
	@Override
	public List<MonthlyAccountModel> getAllMonthlyMerchantOrDepotByTime(String lastMonth) throws Exception {
		return mapper.getAllMonthlyMerchantOrDepotByTime(lastMonth);
	}

	/**
	 * 获取本月之前的月结账单
	 */
	@Override
	public List<MonthlyAccountModel> getBeforeMonthMonthlyAccount(MonthlyAccountModel monthlyAccountModel)
			throws Exception {		
		return mapper.getBeforeMonthMonthlyAccount(monthlyAccountModel);
	}


	@Override
	public List<MonthlyAccountModel> listOrderBySettlementMonth(MonthlyAccountModel monthlyAccountModel) throws SQLException {
		return mapper.listOrderBySettlementMonth(monthlyAccountModel);
	}

	
	// 查询之前是否有过月结
	@Override
	public int getAfterMonthlyAccountCount(MonthlyAccountModel monthlyAccountModel) throws SQLException {
		return mapper.getAfterMonthlyAccountCount(monthlyAccountModel);
	}

	@Override
	public int countByMonthlyAccount(MonthlyAccountModel monthlyAccountModel) throws SQLException {
		return mapper.countByMonthlyAccount(monthlyAccountModel);
	}


}
