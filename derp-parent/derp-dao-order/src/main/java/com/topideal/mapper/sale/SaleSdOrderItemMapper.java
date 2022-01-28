package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.SaleSdOrderItemDTO;
import com.topideal.entity.vo.sale.SaleSdOrderItemModel;
import com.topideal.mapper.BaseMapper;

import io.lettuce.core.dynamic.annotation.Param;


@MyBatisRepository
public interface SaleSdOrderItemMapper extends BaseMapper<SaleSdOrderItemModel> {
	
	List<SaleSdOrderItemDTO> listDTO(SaleSdOrderItemDTO dto)throws SQLException;
	
	int delItemBySaleSdIds(@Param("list")List<Long> saleSdOrderIdList)throws SQLException;


}
