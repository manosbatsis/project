package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.TransferOrderDao;
import com.topideal.entity.vo.order.TransferOrderModel;
import com.topideal.mapper.order.TransferOrderMapper;

/**
 * 调拨订单 impl
 * @author zhanghx
 */
@Repository
public class TransferOrderDaoImpl implements TransferOrderDao {

	@Autowired
	private TransferOrderMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<TransferOrderModel> list(TransferOrderModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param TransferOrderModel
	 */
	@Override
	public Long save(TransferOrderModel model) throws SQLException {
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
	public int modify(TransferOrderModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param TransferOrderModel
	 */
	@Override
	public TransferOrderModel searchByPage(TransferOrderModel model) throws SQLException {
		PageDataModel<TransferOrderModel> pageModel = mapper.listByPage(model);
		return (TransferOrderModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public TransferOrderModel searchById(Long id) throws SQLException {
		TransferOrderModel model = new TransferOrderModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public TransferOrderModel searchByModel(TransferOrderModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 获取调拨单表体备注
	 * */
	/* public List<Map<String, Object>> getOrderRemark(List<String> list){
		 return mapper.getOrderRemark(list);
	 }*/
	

	/**
	 * 获取(事业部业务经销存)调拨在途明细表
	 */
	@Override
	public List<Map<String, Object>> getBuBusinessTransferNoshelfDetails(Map<String, Object> map) throws SQLException {
		return mapper.getBuBusinessTransferNoshelfDetails(map);
	}

	/**
	 * 获取(事业部业务经销存)(事业部财务经销存)累计调拨在途明细表 
	 */
	@Override
	public List<Map<String, Object>> getBuBusinessAddTransferNoshelfDetails(Map<String, Object> map) throws SQLException {
		return mapper.getBuBusinessAddTransferNoshelfDetails(map);
	}


	/**
	 * 本期事业部调拨在途 数量
	 */
	@Override
	public List<Map<String, Object>> getBuBusinessTransferNoshelfSum(Map<String, Object> map) throws SQLException {
		return mapper.getBuBusinessTransferNoshelfSum(map);
	}


	/**
	 *  获取事业部财务经销传 累计调拨在途量
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceAddTransferNoshelfNum(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceAddTransferNoshelfNum(map);
	}
}
