package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleReturnOdepotDTO;
import com.topideal.entity.vo.sale.SaleReturnOdepotModel;

/**
 * 销售退货出库 dao
 * @author lian_
 *
 */
public interface SaleReturnOdepotDao extends BaseDao<SaleReturnOdepotModel>{
		
    /**
     * 分页查询
     * @param dto
     * @return
     */
	SaleReturnOdepotDTO  searchDTOByPage(SaleReturnOdepotDTO dto)throws SQLException;

    /**
     * 查询所有数据
     * @return
     */
	List<SaleReturnOdepotDTO> listSaleReturnOdepotDTO(SaleReturnOdepotDTO dto)throws SQLException;

    /**
     * 通过id查询实体类信息
     * @param id
     * @return
     */
    SaleReturnOdepotDTO searchDTOById(Long id)throws SQLException;
    
    int modifyWithNULL(SaleReturnOdepotModel model) throws SQLException;
    
}

