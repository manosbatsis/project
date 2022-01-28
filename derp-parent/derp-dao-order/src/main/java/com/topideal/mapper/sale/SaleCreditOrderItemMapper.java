package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.SaleCreditOrderItemDTO;
import com.topideal.entity.vo.sale.SaleCreditOrderItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SaleCreditOrderItemMapper extends BaseMapper<SaleCreditOrderItemModel> {

	List<SaleCreditOrderItemDTO> listDTO(SaleCreditOrderItemDTO dto) throws SQLException;

}
