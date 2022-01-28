package com.topideal.mapper.bill;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.ReceiveBillVerificationDTO;
import com.topideal.entity.vo.bill.ReceiveBillVerificationModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

@MyBatisRepository
public interface ReceiveBillVerificationMapper extends BaseMapper<ReceiveBillVerificationModel> {

	
	/**
	 * 分页
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<ReceiveBillVerificationDTO> listReceiveBillVerificationByPage(ReceiveBillVerificationDTO dto) throws SQLException;


	/**
	 * 导出
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>>  exportBillVerification(ReceiveBillVerificationDTO dto)throws SQLException;
	
	/**
	 * 根据应收账单id是删除
	 * @param receiveIdList
	 * @return
	 * @throws SQLException
	 */
	int deleteByReceiveId(@Param("receiveIdList")List<Long> receiveIdList, @Param("billType") String billType)throws SQLException;

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
