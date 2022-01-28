package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.YunjiDeliveryDetailModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface YunjiDeliveryDetailMapper extends BaseMapper<YunjiDeliveryDetailModel> {

	/**
	 * 查询所有云集状态为未使用的所有云集发货爬虫明细
	 * @return
	 */
	public List<Map<String, Object>> getYunjiDeliveryDetail(Map<String, Object> map);
	
	/**
	 * 获取所有未使用的账单号
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
	 *  修改云集账单发货详情和错误信息
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public int updateYunjiDeliveryDetail(YunjiDeliveryDetailModel model)throws SQLException;

	/**
	 * 根据结算单号，sku汇总云集发货明细
	 * @return 
	 */
	public List<YunjiDeliveryDetailModel> getPlatformStatementSumData(Map<String, Object> itemQueryMap);
	
	/**
	 * 修改云集发货为未使用
	 * @param model
	 * @throws SQLException
	 */
	public int updateNotUsed(YunjiDeliveryDetailModel model)throws SQLException;

}
