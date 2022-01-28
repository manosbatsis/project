package com.topideal.dao.bill;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.ReceiveBillVerificationDTO;
import com.topideal.entity.vo.bill.ReceiveBillVerificationModel;


public interface ReceiveBillVerificationDao extends BaseDao<ReceiveBillVerificationModel>{
		

	 /**
     * 分页查询
     * @param model
     * @return
     */
	ReceiveBillVerificationDTO  listReceiveBillVerificationByPage(ReceiveBillVerificationDTO model)throws SQLException;


	/**
	 * 导出
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>>  exportBillVerification(ReceiveBillVerificationDTO dto)throws SQLException;
	
	/**
	 * 根据应收账单id删除
	 * @param receiveIdList
	 * @return
	 * @throws SQLException
	 */
	int deleteByReceiveId(List<Long> receiveIdList, String billType)throws SQLException;


	/**
	 * 应收账龄汇总
	 * @param queryMap
	 * @return
	 */
	Map<String, BigDecimal> getByUncollectedAmount(Map<String, Object> queryMap);


	/**
	 * 应收账龄报告（商家+事业部+客户+币种）
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getSummary(Map<String, Object> queryMap);


	/**
	 * 根据商家+事业部+客户+币种以及月份的维护获取未核销的金额
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getItemBySearch(Map<String, Object> queryMap);
}
