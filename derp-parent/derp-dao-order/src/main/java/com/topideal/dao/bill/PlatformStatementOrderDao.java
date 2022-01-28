package com.topideal.dao.bill;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.PlatformStatementOrderDTO;
import com.topideal.entity.dto.bill.PlatformStatementOrderExportDTO;
import com.topideal.entity.vo.bill.PlatformStatementOrderModel;


public interface PlatformStatementOrderDao extends BaseDao<PlatformStatementOrderModel>{

	PlatformStatementOrderDTO getListByPage(PlatformStatementOrderDTO dto);

	PlatformStatementOrderDTO searchDTOById(Long id);

	List<PlatformStatementOrderExportDTO> getExportOrders(PlatformStatementOrderExportDTO dto);

	List<PlatformStatementOrderDTO> listByIds(List<Long> ids) throws Exception;

	/**
	 * 批量更新平台结算单信息
	 * */
	void batchUpdate(List<Long> ids, String billCode, String isCreateReceive) throws SQLException;

	/**
	 * 统计导出数量
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	Integer countExportNum(PlatformStatementOrderExportDTO dto);






}
