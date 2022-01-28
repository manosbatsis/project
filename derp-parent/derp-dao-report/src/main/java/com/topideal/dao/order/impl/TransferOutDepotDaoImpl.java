package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.TransferOutDepotDao;
import com.topideal.entity.vo.order.TransferOutDepotModel;
import com.topideal.mapper.order.TransferOutDepotMapper;

/**
 * 调拨出库表 impl
 * @author zhanghx
 */
@Repository
public class TransferOutDepotDaoImpl implements TransferOutDepotDao {

	@Autowired
	private TransferOutDepotMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<TransferOutDepotModel> list(TransferOutDepotModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param TransferOutDepotModel
	 */
	@Override
	public Long save(TransferOutDepotModel model) throws SQLException {
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
	public int modify(TransferOutDepotModel model) throws SQLException {
		
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param TransferOutDepotModel
	 */
	@Override
	public TransferOutDepotModel searchByPage(TransferOutDepotModel model) throws SQLException {
		PageDataModel<TransferOutDepotModel> pageModel = mapper.listByPage(model);
		return (TransferOutDepotModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public TransferOutDepotModel searchById(Long id) throws SQLException {
		TransferOutDepotModel model = new TransferOutDepotModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public TransferOutDepotModel searchByModel(TransferOutDepotModel model) throws SQLException {
		return mapper.get(model);
	}
	
	public List<Map<String, Object>> getOutDepotOrderByTime(Map<String, Object> paramMap){
		return mapper.getOutDepotOrderByTime(paramMap);
	}
	/**
    * 根据创建修改时间查询出库单统计数量
    * */
	public Integer getOutDepotOrderByTimeCount(Map<String, Object> paramMap){
		return mapper.getOutDepotOrderByTimeCount(paramMap);
	}
	public List<Map<String, Object>> getOutDepotItemByCodes(List<String> codes){
		return mapper.getOutDepotItemByCodes(codes);
	}
	
	public List<Map<String, Object>> getItemBatchByCode(Map<String, Object> paramMap){
		return mapper.getItemBatchByCode(paramMap);
	}

}
