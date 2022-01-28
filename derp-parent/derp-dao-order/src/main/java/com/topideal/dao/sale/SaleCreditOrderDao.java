package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleCreditOrderDTO;
import com.topideal.entity.vo.bill.PaymentBillModel;
import com.topideal.entity.vo.sale.SaleCreditOrderModel;


public interface SaleCreditOrderDao extends BaseDao<SaleCreditOrderModel>{
	/**
     * 查询所有数据 分页
     * @return
     */
    SaleCreditOrderDTO listDTOByPage(SaleCreditOrderDTO dto) throws SQLException;
    /**
	 * 统计类型数量
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getCreditTypeNum(SaleCreditOrderDTO dto) throws SQLException;
	/**
	 * 获取详情
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SaleCreditOrderDTO searchDetail(SaleCreditOrderDTO dto) throws SQLException;

	/**
	 * 批量更新赊销单
	 * @param saleCreditModels
	 * @return
	 */
	void batchUpdate(List<SaleCreditOrderModel> saleCreditModels);

	List<SaleCreditOrderModel> listByIds(List<Long> ids);
}
