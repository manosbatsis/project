package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;
import com.topideal.entity.vo.sale.OrderReturnIdepotModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface OrderReturnIdepotDao extends BaseDao<OrderReturnIdepotModel> {
	/**
	 * 根据条件获取电商订单信息（包括表体信息）
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	//OrderReturnIdepotModel listOrderAndItemByPage(OrderReturnIdepotModel model) throws SQLException;
	/**
	 * 根据条件获取电商订单信息
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
//	int queryList(OrderReturnIdepotModel model) throws SQLException;
	/**
	 * 根据条件获取需要导出的数据
	 * @param list
	 * @return
	 */
   // List<Map<String, Object>> getExportOrderMap(OrderReturnIdepotModel model)throws SQLException;
	/**
	 * 根据时间、商家id获取每天的数量
	 * @param startDate
	 * @param endDate
	 * @param merchantId
	 * @return
	 */
	List<Map<String, Object>> getOrderDayList(String startDate, String endDate, Long merchantId);
	/**
	 * 根据日期获取日期之前的数量
	 * @param date
	 * @param merchantId
	 * @return
	 */
	List<OrderReturnIdepotModel> getOrderListforDate(String date, Long merchantId);
		


	/**
	 * 根据条件获取电商订单数量
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
//	Integer queryListCount(OrderReturnIdepotModel model) throws SQLException;
	/**
	 * 获取列表数据（表头）-分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
//	OrderReturnIdepotModel newListByPage(OrderReturnIdepotModel  model) throws SQLException;
	
	/**
	 * 查询手动导入数据
	 * @param list
	 * @return 
	 * @throws Exception
	 */
	int getImportOrderCountByIds(List<Long> list) throws Exception;

	 /**
     * 分页查询
     * @param dto
     * @return
     */
	OrderReturnIdepotDTO  searchDTOByPage(OrderReturnIdepotDTO dto)throws SQLException;

    /**
     * 通过id查询实体类信息
     * @param id
     * @return
     */
	OrderReturnIdepotDTO searchDTOById(Long id)throws SQLException;

	/**
	 * 获取列表数据（表头）-分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	OrderReturnIdepotDTO newDTOListByPage(OrderReturnIdepotDTO  dto) throws SQLException;

	/**
	 * 根据条件获取电商订单数量
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	Integer queryDTOListCount(OrderReturnIdepotDTO dto) throws SQLException;


	/**
	 * 根据条件获取电商订单信息
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	int queryDTOList(OrderReturnIdepotDTO dto) throws SQLException;
	
	/**
	 * 根据条件获取需要导出的数据
	 * @param list
	 * @return
	 */
    List<Map<String, Object>> getExportOrderDTOMap(OrderReturnIdepotDTO dto)throws SQLException;

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
    Integer batchUpdateStatus(String isGenerate, List<Long> ids);

    /**
     * 根据退款订单单号批量更新电商订单的是否已生成暂估费用为否
     *
     * @param isGenerate
     * @param codes
     * @return
     */
    Integer batchUpdateStatusByCode(String isGenerate, List<String> codes);

	/**
	 * 根据map 获取DTO list
	 * @param paramMap
	 * @return
	 */
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

	/**
	 * count distinct externalCode
	 * @param orderReturnIdepotDTO
	 * @return
	 */
    Long statisticsDistinctByDTO(OrderReturnIdepotDTO orderReturnIdepotDTO);

	List<OrderReturnIdepotDTO> listDistinctOrderByDTO(OrderReturnIdepotDTO orderReturnIdepotDTO);
}
