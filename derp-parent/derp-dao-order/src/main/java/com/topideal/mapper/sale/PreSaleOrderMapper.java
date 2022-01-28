package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.PreSaleOrderDTO;
import com.topideal.entity.vo.sale.PreSaleOrderModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface PreSaleOrderMapper extends BaseMapper<PreSaleOrderModel> {
    /**
     * 根据条件查询（分页）
     * @return
     */
    PageDataModel<PreSaleOrderDTO> queryDTOListByPage(PreSaleOrderDTO dto)throws SQLException;
    /**
     * 条件过滤查询
     * @return
     */
    PreSaleOrderDTO getPreSaleOrderDTOById(PreSaleOrderDTO dto)throws SQLException;
    /**
     * 根据条件查询
     * @param dto
     * @return
     * @throws SQLException
     */
    List<PreSaleOrderDTO> queryDTOList(PreSaleOrderDTO dto)throws SQLException;
    /**
     * 逻辑删除
     * @param ids
     * @return
     */
    int delPreSaleOrder(List ids)throws SQLException;
    /**
     * 根据审核时间排序预售单
     * @param ids
     * @return
     */
    List<PreSaleOrderDTO> listPreSaleByAuditDate(List ids)throws SQLException;

    int getTotal(PreSaleOrderDTO dto) throws SQLException;
}
