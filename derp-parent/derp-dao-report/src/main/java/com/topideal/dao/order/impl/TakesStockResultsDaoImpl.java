package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.storage.TakesStockResultsDao;
import com.topideal.entity.vo.storage.TakesStockResultsModel;
import com.topideal.mapper.storage.TakesStockResultsMapper;

/**
 * 盘点结果表
 * @author zhanghx
 */
@Repository
public class TakesStockResultsDaoImpl implements TakesStockResultsDao {

	@Autowired
	private TakesStockResultsMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<TakesStockResultsModel> list(TakesStockResultsModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(TakesStockResultsModel model) throws SQLException {
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
	public int modify(TakesStockResultsModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public TakesStockResultsModel searchByPage(TakesStockResultsModel model) throws SQLException {
		PageDataModel<TakesStockResultsModel> pageModel = mapper.listByPage(model);
		return (TakesStockResultsModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public TakesStockResultsModel searchById(Long id) throws SQLException {
		TakesStockResultsModel model = new TakesStockResultsModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public TakesStockResultsModel searchByModel(TakesStockResultsModel model) throws SQLException {
		return mapper.get(model);
	}

	
	/**
	 * 报表api 根据 商家开始时间结束时间分页查询盘点结果
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getListPage(Long merchantId, String startTime,String endTime,Integer startIndex,Integer pageSize) throws SQLException {
		
		return mapper.getListPage(merchantId, startTime,endTime,startIndex,pageSize);
	}
	
	/**
	 * 报表api 根据 商家开始时间结束时间查询盘点结果总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Integer getListCount(Long merchantId, String startTime, String endTime) {
		// TODO Auto-generated method stub
		return mapper.getListCount(merchantId,startTime,endTime);
	}
	/**
	 * 根据创建修改时间查询出库单
	 * */
	public List<Map<String, Object>> getOutDepotOrderByTime(Map<String, Object> paramMap){
		return mapper.getOutDepotOrderByTime(paramMap);
	}
	/**
	 * 根据创建修改时间查询出库单统计数量
	 * */
	public Integer getOutDepotOrderByTimeCount(Map<String, Object> paramMap){
		return mapper.getOutDepotOrderByTimeCount(paramMap);
	}
	/**
	 * 根据出库单号查询出库单商品
	 * */
	public List<Map<String, Object>> getOutDepotItemByCodes(List<String> codes){
		return mapper.getOutDepotItemByCodes(codes);
	}
	/**
	 * 根据出库单查询商品批次
	 * */
	public List<Map<String, Object>> getItemBatchByCode(Map<String, Object> paramMap){
		return mapper.getItemBatchByCode(paramMap);
	}

	/**
	 * 唯品核销获取盘盈盘亏数
	 * @param queryMap
	 * @return
	 */
	@Override
	public Integer getStockAccount(Map<String, Object> queryMap) {
		return mapper.getStockAccount(queryMap);
	}

	/**
	 *获取唯品盘点结果明细
	 */
	@Override
	public List<Map<String, Object>> getVipStockResultsDetails(Map<String, Object> queryMap) {
		return mapper.getVipStockResultsDetails(queryMap);
	}

	@Override
	public Integer getStockSystemAccount(Map<String, Object> queryMap) {
		return mapper.getStockSystemAccount(queryMap);
	}
}
