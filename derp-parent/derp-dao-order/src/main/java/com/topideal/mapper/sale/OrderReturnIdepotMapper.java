package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;
import com.topideal.entity.vo.sale.OrderReturnIdepotModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface OrderReturnIdepotMapper extends BaseMapper<OrderReturnIdepotModel> {
	/**
	 * 根据条件获取电商订单信息（包括表体信息）
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	PageDataModel<OrderReturnIdepotModel> listOrderAndItemByPage(OrderReturnIdepotModel model)throws SQLException;

	/**
	 * 根据时间、商家id获取每天的数量
	 * @param startDate
	 * @param endDate
	 * @param merchantId
	 * @return
	 */
	List<Map<String, Object>> getOrderDayList(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("merchantId")Long merchantId);
	/**
	 * 根据日期获取日期之前的数量
	 * @param date
	 * @param merchantId
	 * @return
	 */
	List<OrderReturnIdepotModel> getOrderListforDate(@Param("date")String date, @Param("merchantId")Long merchantId);

	
	/**
	 * 根据ID查询手动导入订单总数
	 * @param ids
	 * @return
	 */
	int getImportOrderCountByIds(@Param("ids")List<Long> ids);

    /**
     * 查询所有数据
     * @return
     */
    PageDataModel<OrderReturnIdepotDTO> listDTOByPage(OrderReturnIdepotDTO dto)throws SQLException;
    
    OrderReturnIdepotDTO  getOrderReturnIdepotDTOById(OrderReturnIdepotDTO dto);
	/**
	 * 获取列表数据（表头）-分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<OrderReturnIdepotDTO> newDTOListByPage(OrderReturnIdepotDTO dto)throws SQLException;
	/**
	 * 根据条件获取电商订单数量
	 * @param dto
	 * @return
	 * @throws SQLException 
	 */
	Integer queryDTOListCount(OrderReturnIdepotDTO dto)throws SQLException;
	/**
	 * 根据条件获取电商订单信息
	 * @param dto
	 * @return
	 * @throws SQLException 
	 */
	int queryDTOList(OrderReturnIdepotDTO dto)throws SQLException;
	/**
	 * 根据条件获取需要导出的数据
	 * @return
	 */
    List<Map<String, Object>> getExportOrderDTOMap(OrderReturnIdepotDTO dto) throws SQLException;

    /**
     * 根据外部订单号集合获取退款单
     *
     * @param dto
     * @return
	 */
	List<OrderReturnIdepotDTO> listRefundOrderDTO(OrderReturnIdepotDTO dto);

	List<Map<String, Object>> countOrderReturnIdepotByDTO(OrderReturnIdepotDTO dto) throws SQLException;

	/**
	 * 批量更新退款订单的是否已生成暂估费用为是
	 */
	Integer batchUpdateStatus(@Param("isGenerate") String isGenerate, @Param("ids") List<Long> ids);

	/**
	 * 根据退款订单单号批量更新电商订单的是否已生成暂估费用为否
	 *
	 * @param isGenerate
	 * @param codes
	 * @return
	 */
	Integer batchUpdateStatusByCode(@Param("isGenerate") String isGenerate, @Param("codes") List<String> codes);

    List<OrderReturnIdepotDTO> selectDTOListByMap(Map<String, Object> paramMap);
    /**
	 * 批量插入
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	Integer batchSave(List<OrderReturnIdepotModel> list) throws SQLException;
	/**
	 * 获取所有信息
	 * @param dto
	 * @return
	 */
	List<OrderReturnIdepotDTO> listDTO(OrderReturnIdepotDTO dto);

    Long statisticsDistinctByDTO(OrderReturnIdepotDTO orderReturnIdepotDTO);

	List<OrderReturnIdepotDTO> listDistinctOrderByDTO(OrderReturnIdepotDTO orderReturnIdepotDTO);
}
