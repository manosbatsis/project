package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleReturnDeclareOrderDTO;
import com.topideal.entity.vo.sale.SaleReturnDeclareOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SaleReturnDeclareOrderMapper extends BaseMapper<SaleReturnDeclareOrderModel> {
    /**
     * 查询所有数据 分页
     * @return
     */
    PageDataModel<SaleReturnDeclareOrderDTO> listDTOByPage(SaleReturnDeclareOrderDTO dto)throws SQLException;
    /**
     * 根据id查询数据
     * @return
     */
    SaleReturnDeclareOrderDTO searchDetail(SaleReturnDeclareOrderDTO dto)throws SQLException;

    /**
     * 查询所有数据
     * @param dto
     * @return
     */
    List<SaleReturnDeclareOrderDTO> listReturnDeclareOrder(SaleReturnDeclareOrderDTO dto);
    
    int updateWithNull(SaleReturnDeclareOrderModel model) throws SQLException; 
}
