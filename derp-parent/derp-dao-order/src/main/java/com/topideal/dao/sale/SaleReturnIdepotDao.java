package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleReturnIdepotDTO;
import com.topideal.entity.dto.sale.SaleReturnIdepotItemDTO;
import com.topideal.entity.vo.sale.SaleReturnIdepotModel;

/**
 * 销售退货入库 dao
 * @author lian_
 *
 */
public interface SaleReturnIdepotDao extends BaseDao<SaleReturnIdepotModel>{
		

    /**
     * 分页查询
     * @param model
     * @return
     */
	SaleReturnIdepotDTO searchDTOByPage(SaleReturnIdepotDTO dto)throws SQLException;

    /**
     * 通过id查询实体类信息
     * @param id
     * @return
     */
	SaleReturnIdepotDTO searchDTOById(Long id)throws SQLException;

    /**
     * 列表查询
     * @param dto
     * @return
     * @throws SQLException
     */
    List<SaleReturnIdepotDTO> listSaleReturnIdepotDTO(SaleReturnIdepotDTO dto)throws SQLException;


}

