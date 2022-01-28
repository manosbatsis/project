package com.topideal.mapper.platformdata;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.vo.platformdata.AlipayMonthlyFeeModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface AlipayMonthlyFeeMapper extends BaseMapper<AlipayMonthlyFeeModel> {
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	Integer alipayBatchSave(List<AlipayMonthlyFeeModel> list);

	/**
	 * 根据外部订单号集合和商家id批量统计以“电商订单号+结算单号”为维度的每月结算费用
	 * @param partnerTransactionIds 外部订单号集合
	 * @param merchantId 商家id
	 * @return
	 */
	List<AlipayMonthlyFeeModel> statisticsByExCodesAndMerId(@Param("partnerTransactionIds") List<String> partnerTransactionIds,
															@Param("merchantId") Long merchantId) throws SQLException;

	/**
	 * 统计每月结算费用数量
	 * @param model
	 * @return
	 */
	Integer statisticsByModel(AlipayMonthlyFeeModel model) throws SQLException;

	/**
	 * 统计每月结算费用数量 （distinct）
	 * @param alipayMonthlyFeeModel
	 * @return
	 */
    Integer statisticsDistinctByModel(AlipayMonthlyFeeModel alipayMonthlyFeeModel);

    List<String> searchPartnerTransactionIdByPage(AlipayMonthlyFeeModel alipayMonthlyFeeModel);

    List<AlipayMonthlyFeeModel> listByMap(Map<String, Object> queryMap);

    PageDataModel<AlipayMonthlyFeeModel> listDistinctByPage(AlipayMonthlyFeeModel alipayMonthlyFeeModel);
}
