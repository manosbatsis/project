package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleReturnIdepotDTO;
import com.topideal.entity.vo.sale.SaleReturnIdepotModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售退货入库 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface SaleReturnIdepotMapper extends BaseMapper<SaleReturnIdepotModel> {

	/**
     * 分页查询
     * @param model
     * @return
     */
	PageDataModel<SaleReturnIdepotDTO> listDTOByPage(SaleReturnIdepotDTO dto)throws SQLException;
	
    /**
     * 通过id查询实体类信息
     * @param id
     * @return
     */
	SaleReturnIdepotDTO getDTOById(SaleReturnIdepotDTO dto)throws SQLException;
	/**
	 * 根据条件获取销售退货入库
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SaleReturnIdepotDTO> listSaleReturnIdepot(SaleReturnIdepotDTO dto)throws SQLException;
}

