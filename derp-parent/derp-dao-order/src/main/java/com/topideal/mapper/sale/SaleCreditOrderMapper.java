package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleCreditOrderDTO;
import com.topideal.entity.vo.sale.SaleCreditOrderModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


@MyBatisRepository
public interface SaleCreditOrderMapper extends BaseMapper<SaleCreditOrderModel> {
	/**
     * 查询所有数据 分页
     * @return
     */
    PageDataModel<SaleCreditOrderDTO> listDTOByPage(SaleCreditOrderDTO dto)throws SQLException;
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
	void batchUpdate(@Param("saleCreditModels") List<SaleCreditOrderModel> saleCreditModels);

	List<SaleCreditOrderModel> listByIds(@Param("ids") List<Long> ids);
}
