package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.YunjiReturnDetailModel;

public interface YunjiReturnDetailDao extends BaseDao<YunjiReturnDetailModel>{
		


	/**
	 * 查询所有云集状态为未使用的所有云集退货爬虫明细
	 * @return
	 */
	public List<Map<String, Object>> getYunjiReturnDetail(Map<String, Object> map);

	/**
	 * 修改云集退货为已使用
	 * @param model
	 * @throws SQLException
	 */
	public int modifyYunJiReturnDetail(YunjiReturnDetailModel model)throws SQLException;

	/**
	 * 修改云集账单退货详情错误信息
	 * @param model
	 * @throws SQLException
	 */
	public int updateYunjiReturnDetail(YunjiReturnDetailModel model)throws SQLException;

	/**
	 * 根据结算单号，sku汇总云集退货明细
	 * @param model
	 * @return 
	 * @throws SQLException
	 */
	public List<YunjiReturnDetailModel> getPlatformStatementSumData(Map<String, Object> itemQueryMap);

	/**
	 * 修改云集退货为未使用
	 * @param model
	 * @throws SQLException
	 */
	public int updateNotUsed(YunjiReturnDetailModel model)throws SQLException;

}
