package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleAnalysisDTO;
import com.topideal.entity.vo.sale.SaleAnalysisModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 销售出库分析表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface SaleAnalysisMapper extends BaseMapper<SaleAnalysisModel> {
	/**
	 * 根据条件查询（分页）
	 * @param model
	 * @return
	 */
	PageDataModel<SaleAnalysisDTO> queryDTOListByPage(SaleAnalysisDTO model)throws SQLException;

}

