package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.YunjiDeliveryDetailModel;

public interface YunjiDeliveryDetailDao extends BaseDao<YunjiDeliveryDetailModel>{
		


	/**
	 * 查询所有云集状态为未使用的所有云集发货爬虫明细
	 * @return
	 */
	public List<Map<String, Object>> getYunjiDeliveryDetail(Map<String, Object> map);
	
	/**
	 * 获取所有未使用的账单号
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getYunjiDeliveryDetailBySettleId(Map<String, Object> map);


	
	/**
	 * 修改云集发货为已使用
	 * @param model
	 * @throws SQLException
	 */
	public int modifyYunjiDeliveryDetail(YunjiDeliveryDetailModel model)throws SQLException;
	
	/**
	 * 修改云集账单发货详情和退货详情 错误信息
	 * @param model
	 * @throws SQLException
	 */
	public int updateYunjiDeliveryDetail(YunjiDeliveryDetailModel model)throws SQLException;


	/**
	 * 根据结算单号，sku汇总云集发货明细
	 * @param model
	 * @return 
	 * @throws SQLException
	 */
	public List<YunjiDeliveryDetailModel> getPlatformStatementSumData(Map<String, Object> itemQueryMap);

	/**
	 * 修改云集发货为未使用
	 * @param model
	 * @throws SQLException
	 */
	public int updateNotUsed(YunjiDeliveryDetailModel model)throws SQLException;


}
