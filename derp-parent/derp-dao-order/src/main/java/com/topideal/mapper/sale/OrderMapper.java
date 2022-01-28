package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.mapper.BaseMapper;

/**
 * 电商订单表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface OrderMapper extends BaseMapper<OrderModel> {
	/**
	 * 根据条件获取电商订单信息（包括表体信息）
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	PageDataModel<OrderModel> listOrderAndItemByPage(OrderModel model)throws SQLException;
	/**
	 * 根据条件获取电商订单信息
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	int queryList(OrderModel model)throws SQLException;
	/**
	 * 根据条件获取需要导出的数据
	 * @return
	 */
	List<Map<String, Object>> getExportOrderMap(OrderDTO dto)throws SQLException;

	/**
	 * 根据外部单查询电商订单
	 * @param listExternalCode
	 * @return
	 * @throws SQLException
	 */
	List<OrderModel> selectByExternalCode(@Param("list")List list,@Param("merchantId")Long merchantId)throws SQLException;
	/**
	 * 根据条件获取电商订单数量
	 * @param dto
	 * @return
	 * @throws SQLException 
	 */
	Integer queryDtoListCount(OrderDTO dto)throws SQLException;
	/**
	 * 获取列表数据（表头）-分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<OrderDTO> newDtoListByPage(OrderDTO dto) throws SQLException;
	
	/**
	 * 根据ID查询手动导入订单总数
	 * @param ids
	 * @return
	 */
	int getImportOrderCountByIds(@Param("ids")List<Long> ids);
	/**
	 * 根据外部单号和商品ID查询已经发货订单总数
	 * @return
	 */
	Integer getGoodNumByExternalCodeAndGoodsId(Map<String, Object> map);
	
	OrderDTO searchDtoById(OrderDTO dto);

	/**
	 * 获取事业部补录列表数据-导出
	 * @param list
	 * @return
	 */
	List<OrderDTO> businessUnitRecordExport(OrderDTO dto) throws SQLException;
	
	PageDataModel<OrderDTO> listBusinessUnitRecordByPage(OrderDTO dto) throws SQLException;
	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String,Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String,Object> map);
	/**
	  * 获取表头税费运费与表体税费运费之和不等的电商订单
	 * @param dto
	 * @return
	 */
	List<Long> getApportionOrder(@Param("startDate")String startDate,@Param("endDate")String endDate);

	List<OrderDTO> listOrderByDTO(OrderDTO dto) throws SQLException;

	List<Map<String, Object>> countOrderByDTO(OrderDTO dto) throws SQLException;

	/**
	 * 根据平台订单号查询电商订单
	 * @param listExOrderIds
	 * @return
	 * @throws SQLException
	 */
	List<OrderModel> selectByExOrderId(@Param("list")List listExOrderIds,@Param("merchantId")Long merchantId)throws SQLException;

	List<Map<String, Object>> countOrderByCostDTO(OrderDTO dto) throws SQLException;

	/**
	 * 批量更新电商订单的是否已生成暂估费用为是
	 */
	Integer batchUpdateStatus(@Param("isGenerate") String isGenerate, @Param("ids") List<Long> ids);

	/**
	 * 根据电商订单批量更新电商订单的是否已生成暂估费用为是
	 * @param isGenerate
	 * @return
	 */
	Integer batchUpdateStatusByCode(@Param("isGenerate") String isGenerate, @Param("codes") List<String> codes);

	/**
	 * 根据条件获取需要导出的数据数量
	 * @return
	 */
	Integer getExportOrderCount(OrderDTO dto)throws SQLException;
	/**
	 * 批量插入
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	Integer batchSave(List<OrderModel> list) throws SQLException;

    List<OrderDTO> selectDTOListByMap(Map<String, Object> paramMap);

    int queryInvailDtoListCount(OrderDTO orderDTO);

	PageDataModel<OrderDTO> listInvailDTOByPage(OrderDTO orderDTO);
	/**
	 * 获取所有信息
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<OrderDTO> listDTO(OrderDTO dto)throws SQLException;
}

