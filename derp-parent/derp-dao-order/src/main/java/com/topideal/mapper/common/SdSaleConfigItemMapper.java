package com.topideal.mapper.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.common.SdSaleConfigItemDTO;
import com.topideal.entity.vo.common.SdSaleConfigItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SdSaleConfigItemMapper extends BaseMapper<SdSaleConfigItemModel> {
	
	List<SdSaleConfigItemDTO> listDTO(SdSaleConfigItemDTO dto)throws SQLException;

}
