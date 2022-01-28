package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.sale.OrderHistoryDTO;
import com.topideal.entity.vo.sale.OrderHistoryModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface OrderHistoryMapper extends BaseMapper<OrderHistoryModel> {

    /**
     * 获取列表数据（表头）-分页
     * @param dto
     * @return
     * @throws SQLException
     */
    PageDataModel<OrderHistoryDTO> newDtoListByPage(OrderHistoryDTO dto) throws SQLException;
    /**
     * 根据条件获取电商订单数量
     * @param dto
     * @return
     * @throws SQLException
     */
    Integer queryDtoListCount(OrderDTO dto)throws SQLException;
    /**
     * 根据条件获取需要导出的数据
     * @return
     */
    List<Map<String, Object>> getExportOrderMap(OrderDTO dto)throws SQLException;

    OrderHistoryDTO searchDtoById(OrderHistoryDTO dto);
    /**
     * 根据条件获取需要导出的数据数量
     * @return
     */
    Integer getExportOrderCount(OrderDTO dto)throws SQLException;
}
