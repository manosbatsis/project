package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleReturnOdepotDTO;
import com.topideal.entity.vo.sale.SaleReturnOdepotModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售退货出库 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface SaleReturnOdepotMapper extends BaseMapper<SaleReturnOdepotModel> {

    /**
     * 查询所有数据
     * @return
     */
    PageDataModel<SaleReturnOdepotDTO> listDTOByPage(SaleReturnOdepotDTO dto)throws SQLException;
    /**
     * 条件过滤查询
     * @return
     */
    SaleReturnOdepotDTO getSaleReturnOdepotById(SaleReturnOdepotDTO dto)throws SQLException;
    
	List<SaleReturnOdepotDTO> listSaleReturnOdepotDTO(SaleReturnOdepotDTO dto) throws SQLException;
	
	int modifyWithNULL(SaleReturnOdepotModel model) throws SQLException;
}

