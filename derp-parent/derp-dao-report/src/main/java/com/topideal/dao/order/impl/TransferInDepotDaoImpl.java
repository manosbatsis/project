package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.TransferInDepotDao;
import com.topideal.entity.vo.order.TransferInDepotModel;
import com.topideal.mapper.order.TransferInDepotMapper;

/**
 * 调拨入库 impl
 * @author zhanghx
 */
@Repository
public class TransferInDepotDaoImpl implements TransferInDepotDao {

	@Autowired
	private TransferInDepotMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<TransferInDepotModel> list(TransferInDepotModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param TransferInDepotModel
	 */
	@Override
	public Long save(TransferInDepotModel model) throws SQLException {
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
	public int modify(TransferInDepotModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param TransferInDepotModel
	 */
	@Override
	public TransferInDepotModel searchByPage(TransferInDepotModel model) throws SQLException {
		PageDataModel<TransferInDepotModel> pageModel = mapper.listByPage(model);
		return (TransferInDepotModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public TransferInDepotModel searchById(Long id) throws SQLException {
		TransferInDepotModel model = new TransferInDepotModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public TransferInDepotModel searchByModel(TransferInDepotModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 报表api 根据 商家开始时间结束时间分页查询调拨入库单
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
	 * 报表api 根据 商家开始时间结束时间查询调拨入库单总数
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

}
