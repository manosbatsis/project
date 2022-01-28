package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleAnalysisDTO;
import com.topideal.entity.vo.sale.SaleAnalysisModel;

/**
 * 销售出库分析表 dao
 * @author lian_
 *
 */
public interface SaleAnalysisDao extends BaseDao<SaleAnalysisModel>{
	/**
	 * 根据条件查询（分页）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	SaleAnalysisDTO queryDTOListByPage(SaleAnalysisDTO dto) throws SQLException;

}

