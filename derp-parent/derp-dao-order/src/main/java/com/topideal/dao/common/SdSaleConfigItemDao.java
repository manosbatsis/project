package com.topideal.dao.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.SdSaleConfigItemDTO;
import com.topideal.entity.vo.common.SdSaleConfigItemModel;


public interface SdSaleConfigItemDao extends BaseDao<SdSaleConfigItemModel>{
		
	List<SdSaleConfigItemDTO> listDTO(SdSaleConfigItemDTO dto)throws SQLException;
}
