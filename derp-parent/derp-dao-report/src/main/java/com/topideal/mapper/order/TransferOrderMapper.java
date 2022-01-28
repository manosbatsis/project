package com.topideal.mapper.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.TransferOrderModel;
import com.topideal.mapper.BaseMapper;

/**
 * 调拨订单 mapper
 * @author zhanghx
 */
@MyBatisRepository
public interface TransferOrderMapper extends BaseMapper<TransferOrderModel> {
	
	/**
	 * 获取调拨单表体备注
	 * */
	 /*public List<Map<String, Object>> getOrderRemark(List<String> list);*/
	

	 /**
	  * 获取(事业部业务经销存)调拨在途明细表 
	  * @param map
	  * @return
	  * @throws SQLException
	  */
	 List<Map<String, Object>> getBuBusinessTransferNoshelfDetails(Map<String, Object> map) throws SQLException;

	 /**
	  * 获取(事业部业务经销存)(事业部财务经销存)累计调拨在途明细表 
	  * @param map
	  * @return
	  * @throws SQLException
	  */
	 List<Map<String, Object>> getBuBusinessAddTransferNoshelfDetails(Map<String, Object> map) throws SQLException;
	 
	 
	 /**
	  * 本期事业部调拨在途 数量
	  * @param map
	  * @return
	  * @throws SQLException
	  */
	 List<Map<String, Object>> getBuBusinessTransferNoshelfSum(Map<String, Object> map) throws SQLException;
	 

	 /**
	  * 获取事业部财务经销传 累计调拨在途量
	  * @param map
	  * @return
	  * @throws SQLException
	  */
	 List<Map<String, Object>> getBuFinanceAddTransferNoshelfNum(Map<String, Object> map) throws SQLException;
			 
}

