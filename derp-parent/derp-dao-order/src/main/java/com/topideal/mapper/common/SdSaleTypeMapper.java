package com.topideal.mapper.common;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.SdSaleTypeDTO;
import com.topideal.entity.vo.common.SdSaleTypeModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface SdSaleTypeMapper extends BaseMapper<SdSaleTypeModel> {
	
	PageDataModel<SdSaleTypeDTO> listDTOByPage(SdSaleTypeDTO dto)throws SQLException;

	List<SdSaleTypeDTO> listDTO(SdSaleTypeDTO dto) throws SQLException;
}
