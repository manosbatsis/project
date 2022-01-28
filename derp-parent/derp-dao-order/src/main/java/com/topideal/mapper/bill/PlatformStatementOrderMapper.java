package com.topideal.mapper.bill;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.PlatformStatementOrderDTO;
import com.topideal.entity.dto.bill.PlatformStatementOrderExportDTO;
import com.topideal.entity.vo.bill.PlatformStatementOrderModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


@MyBatisRepository
public interface PlatformStatementOrderMapper extends BaseMapper<PlatformStatementOrderModel> {

	PageDataModel<PlatformStatementOrderDTO> getListByPage(PlatformStatementOrderDTO dto);

	PlatformStatementOrderDTO getDTO(PlatformStatementOrderDTO model);

	List<PlatformStatementOrderExportDTO> getExportOrders(PlatformStatementOrderExportDTO dto);

	List<PlatformStatementOrderDTO> listByIds(@Param("ids") List<Long> ids) throws Exception;

	/**
	 * 批量更新平台结算单信息
	 * */
	void batchUpdate(@Param("ids") List<Long> ids, @Param("billCode") String billCode, @Param("isCreateReceive")String isCreateReceive) throws SQLException;

	/**
	 * 统计导出数量
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	Integer countExportNum(PlatformStatementOrderExportDTO dto) ;
}
