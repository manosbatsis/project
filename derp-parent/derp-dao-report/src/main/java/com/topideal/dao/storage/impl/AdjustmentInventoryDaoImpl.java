package com.topideal.dao.storage.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.storage.AdjustmentInventoryDao;
import com.topideal.entity.vo.storage.AdjustmentInventoryModel;
import com.topideal.mapper.storage.AdjustmentInventoryMapper;

/**
 * 库存调整
 * @author zhanghx
 */
@Repository
public class AdjustmentInventoryDaoImpl implements AdjustmentInventoryDao {

	@Autowired
	private AdjustmentInventoryMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<AdjustmentInventoryModel> list(AdjustmentInventoryModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(AdjustmentInventoryModel model) throws SQLException {
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
	public int modify(AdjustmentInventoryModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public AdjustmentInventoryModel searchByPage(AdjustmentInventoryModel model) throws SQLException {
		PageDataModel<AdjustmentInventoryModel> pageModel = mapper.listByPage(model);
		return (AdjustmentInventoryModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public AdjustmentInventoryModel searchById(Long id) throws SQLException {
		AdjustmentInventoryModel model = new AdjustmentInventoryModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public AdjustmentInventoryModel searchByModel(AdjustmentInventoryModel model) throws SQLException {
		return mapper.get(model);
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
	 * 报表api 根据 商家开始时间结束时间分页查询库存调整单
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getListPage(Long merchantId, String startTime,String endTime,Integer startIndex,Integer pageSize,String inventoryType) throws SQLException {
		
		return mapper.getListPage(merchantId, startTime,endTime,startIndex,pageSize,inventoryType);
	}
	
	/**
	 * 报表api 根据 商家开始时间结束时间查询库存调整单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Integer getListCount(Long merchantId, String startTime, String endTime,String InventoryType) {
		// TODO Auto-generated method stub
		return mapper.getListCount(merchantId,startTime,endTime,InventoryType);
	}


	/**
	 * 事业部务经销存总表 本月销毁数量
	 */
	@Override
	public List<Map<String, Object>> getBuAdjustmentInventoryDestroyNum(Map<String, Object> paramMap) {
		return mapper.getBuAdjustmentInventoryDestroyNum(paramMap);
	}

	/**
	 * 获取(事业部财务经销存)销毁明细表
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceDestroy(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceDestroy(map);
	}

}
